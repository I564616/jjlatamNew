/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.core.job.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.gt.pcm.integration.core.job.dao.JnjCCP360IntegrationDAO;
import com.jnj.gt.pcm.integration.core.job.service.JnjCCP360IntegrationService;
import com.jnj.gt.pcm.integration.core.job.service.strategy.JnjGTP360CountryNameStrategy;

/**
 * Implementation Class  for JJCC P360 Integration Inteface
 * 
 */

public class DefaultJnjCCP360IntegrationService implements JnjCCP360IntegrationService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCCP360IntegrationService.class);

	protected static final String CSV = ".csv";
	protected static final String REGION_LABEL = "Region";
	protected static final CharSequence PRODUCT_CODE = "Product Code";
	protected static final CharSequence PRODUCT_NAME = "Product Name";
	protected static final CharSequence COUNTRY = "Country";

	protected static final String REGION = "jjcc.product.region";
	private static final String P360_FTP_HOST = "jnj.p360.ftp.host";
	private static final String P360_FTP_USER = "jnj.p360.ftp.user";
	private static final String P360_FTP_PASS = "jnj.p360.ftp.password";
	private static final String P360_FTP_REMOTE_FILE_PATH = "jnj.p360.ftp.remote.file.path";
	private static final String P360_FTP_PORT = "jnj.p360.ftp.port";

	private static final String PHR = "_PHR";
	protected static final String MDD_FILE_NAME = "MEDICALDEVICES_PCM_PRODUCTS";
	protected static final String PHR_FILE_NAME = "PHARMACEUTICAL_PCM_PRODUCTS";

	protected static final String UPLOAD_TEMP_FILE_LOCATION = "upload.p360.file.location";
	protected static final int COUNT_ZERO = 0;

	private JnjCCP360IntegrationDAO jnjCCP360IntegrationDao;
	protected ConfigurationService configurationService;
	protected JnjGTP360CountryNameStrategy jnjGTP360CountryNameStrategy;
	
	@Override
	public CategoryModel getCategoryModel(final String categoryCode, final CatalogVersionModel catalogVersionModel)
	{
		return jnjCCP360IntegrationDao.getCategoryModel(categoryCode, catalogVersionModel);
	}


	@Override
	public boolean uploadProductDetailFile(final String startDate)
	{

		boolean uploadFile = false;
		boolean uploadMddFile = true;
		boolean uploadPhrFile = true;
		final List<JnJProductModel> productDetailList = jnjCCP360IntegrationDao.getProductDetailList(startDate);

		final List<JnJProductModel> mddProdList = new ArrayList<>();
		final List<JnJProductModel> phrProdList = new ArrayList<>();


		for (final JnJProductModel jnjProduct : productDetailList)
		{
			if (jnjProduct.getCode() != null && jnjProduct.getCode().endsWith(PHR))
			{
				phrProdList.add(jnjProduct);
			} else {
				mddProdList.add(jnjProduct);
			}
		}

		if (!mddProdList.isEmpty())
		{
			LOGGER.info("MDD products Available");
			uploadMddFile = createProductDetailCSVFile(mddProdList, MDD_FILE_NAME);
		}
		if (!phrProdList.isEmpty())
		{
			LOGGER.info("PHARMA products Available");
			uploadPhrFile = createProductDetailCSVFile(phrProdList, PHR_FILE_NAME);
		}

		if (uploadMddFile && uploadPhrFile)
		{
			uploadFile = true;
		}

		return uploadFile;
	}


	protected boolean createProductDetailCSVFile(final List<JnJProductModel> productDetailList, final String fileNamePrefix)
	{
		boolean createFile = true;
		boolean uploadFile = true;
		final String region = configurationService.getConfiguration().getString(REGION);


		final DateFormat df2 = new SimpleDateFormat("yyyyMMdd_hhmmssSSS");

		final Date date = new Date();

		final String tempFolderPath = configurationService.getConfiguration().getString(UPLOAD_TEMP_FILE_LOCATION);

		final String metaDataOnlyCsvFileName = tempFolderPath + fileNamePrefix + "_" + df2.format(date) + CSV;

		final File copyMetaDataCsvFileOnly = new File(metaDataOnlyCsvFileName);



		try (FileWriter writer = new FileWriter(copyMetaDataCsvFileOnly))
		{
			writer.append(PRODUCT_CODE);
			writer.append(",");
			writer.append(PRODUCT_NAME);
			writer.append(",");
			writer.append(REGION_LABEL);
			writer.append(",");
			writer.append(COUNTRY);
			writer.append('\n');


			for (final JnJProductModel productModel : productDetailList)
			{

				final StringBuilder countriesName = jnjGTP360CountryNameStrategy.getCountriesName(productModel);

				writer.append(productModel.getCode());
				writer.append(",");
				if (productModel.getName() != null)
				{
					writer.append(productModel.getName().replaceAll(",", ""));
				}
				else
				{
					writer.append("");
				}
				writer.append(",");
				writer.append(region);
				writer.append(",");
				if (countriesName.length() != COUNT_ZERO)
				{
					writer.append(countriesName.toString());
				}
				else
				{
					writer.append("");
				}
				writer.append("\n");


			}
		}
		catch (final IOException e)
		{
			createFile = false;
			LOGGER.error("Error thrown while creating CSV File", e);
		}

		if (createFile)
		{
			uploadFile = establishFTPConnection(tempFolderPath, metaDataOnlyCsvFileName);
			LOGGER.info("Upload File Status " + uploadFile);
		}

		return uploadFile;
	}

	protected boolean establishFTPConnection(String fullFilePath, final String metaDataOnlyCsvFileName)
	{

		if (StringUtils.isNotEmpty(fullFilePath))
		{
			fullFilePath = fullFilePath.replace('\\', '/');
		}

		final String ftpHost = configurationService.getConfiguration().getString(P360_FTP_HOST);		
		final String ftpUser = configurationService.getConfiguration().getString(P360_FTP_USER);		
		final String ftpPassword = configurationService.getConfiguration().getString(P360_FTP_PASS);		
		final String ftpRemoteFilePath = configurationService.getConfiguration().getString(P360_FTP_REMOTE_FILE_PATH);		
		final String ftpPort = configurationService.getConfiguration().getString(P360_FTP_PORT);

		Session session = null;
		boolean uploadFileStatus = true;
		ChannelSftp channelSftp = null;
		Channel channel = null;

		try
		{
			final JSch jsch = new JSch();

			session = jsch.getSession(ftpUser, ftpHost, 22);
			session.setPassword(ftpPassword);

			LOGGER.info("SFTP: Connection Details  HostName:--> " + ftpHost + " UserName:-->" + ftpUser + " Port:-->" + ftpPort
					+ " Password Key:-->" + ftpPassword);

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			uploadFileStatus = uploadProductDetailFIleToFTP(fullFilePath, ftpRemoteFilePath, channelSftp);

			LOGGER.info("status sftpChannel has been successfully Connected!!" + uploadFileStatus);
		}
		catch (final JSchException jschException)
		{
			LOGGER.error("Jsch exception occured ", jschException);
			uploadFileStatus = false;
		}

		finally
		{
			if (channelSftp != null)
			{
				channelSftp.disconnect();
				LOGGER.info("Channel has been successfully Disconnected!!!");
				session.disconnect();

				try
				{
					Files.delete(Path.of(metaDataOnlyCsvFileName));
				}
				catch (final IOException iOException)
				{
					LOGGER.error("Exception occured while deleting file ", iOException);
				}
				LOGGER.info("File is deleted form NFS_DATA successfully.");
			}
			LOGGER.info("Session has been successfully Disconnected!!");

		}

		return uploadFileStatus;

	}

	private static boolean uploadProductDetailFIleToFTP(final String fullFilePath, final String ftpRemoteFilePath,
			final ChannelSftp channelSftp)
	{
		boolean fileUploadSuccess = true;

		try
		{
			channelSftp.cd(ftpRemoteFilePath);
			final File[] filesInDirectory = new File(fullFilePath).listFiles();
			if (filesInDirectory.length != COUNT_ZERO)
			{
				LOGGER.info("No of Files " + filesInDirectory.length);
				for (final File file : filesInDirectory)
				{
					fileUploadSuccess = uploadEachFile(file, channelSftp);

				}

			}

		}
		catch (final SftpException sftpException)
		{
			fileUploadSuccess = false;
			LOGGER.error("Exception occured while switching to Mbox directory", sftpException);
		}
		LOGGER.info("File Upload Success Status " + fileUploadSuccess);
		return fileUploadSuccess;
	}

	protected static boolean uploadEachFile(final File file, final ChannelSftp channelSftp)
	{
		boolean fileUploadSuccess = true;
		try (FileInputStream iputStream = new FileInputStream(file))
		{
			channelSftp.put(iputStream, file.getName());
		}
		catch (final FileNotFoundException fileNotFoundException)
		{
			fileUploadSuccess = false;
			LOGGER.error("Exception occured while uploading file in Mbox", fileNotFoundException);
		}
		catch (final IOException iOException)
		{
			fileUploadSuccess = false;
			LOGGER.error("Exception occured while creating file stream", iOException);
		}
		catch (final SftpException sftpException)
		{
			fileUploadSuccess = false;
			LOGGER.error("Exception occured while switching to Mbox directory", sftpException);
		}
		return fileUploadSuccess;
	}

	

	@Override
	public ProductDocumentsModel getProductDocumentByNameAndUrl(final String name, final String url)
	{

		return jnjCCP360IntegrationDao.getProductDocumentByNameAndUrl(name, url);
	}

	@Override
	public List<ProductReferenceModel> getProductReferenceModelList(final JnJProductModel jnjProductModel,
			final JnJProductModel targetModel)
	{
		return jnjCCP360IntegrationDao.getProductReferenceModelList(jnjProductModel, targetModel);
	}

	public JnjCCP360IntegrationDAO getJnjCCP360IntegrationDao()
	{
		return jnjCCP360IntegrationDao;
	}
	
	public void setJnjCCP360IntegrationDao(final JnjCCP360IntegrationDAO jnjCCP360IntegrationDao)
	{
		this.jnjCCP360IntegrationDao = jnjCCP360IntegrationDao;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
	
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public JnjGTP360CountryNameStrategy getJnjGTP360CountryNameStrategy() {
		return jnjGTP360CountryNameStrategy;
	}
	
	public void setJnjGTP360CountryNameStrategy(final JnjGTP360CountryNameStrategy jnjGTP360CountryNameStrategy) {
		this.jnjGTP360CountryNameStrategy = jnjGTP360CountryNameStrategy;
	}	
	
}
