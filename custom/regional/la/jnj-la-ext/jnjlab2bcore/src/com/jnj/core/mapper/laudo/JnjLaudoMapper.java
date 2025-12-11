/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.mapper.laudo;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.Severity;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.core.services.JnjCustomerService;
import com.jnj.core.services.laudo.JnjLaudoService;
import com.jnj.core.services.laudo.impl.JnjLaudoServiceImpl;
import com.jnj.core.services.media.JnjLatamMediaService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjLaudoData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Laudo;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Mapper class for Mapping Laudo Data to Hybris.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLaudoMapper
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjLaudoMapper.class);

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The jnj laudo service. */
	@Autowired
	private JnjLaudoService jnjLaudoService;

	/** The media service. */
	@Autowired
	private MediaService mediaService;

	/** The jnj customer service. */
	@Autowired
	private JnjCustomerService jnjCustomerService;

	/** The jnj media service. */
	@Autowired
	private JnjLatamMediaService jnjLatamMediaService;

	/** The c ms site service. */
	@Autowired
	private CMSSiteService cMSSiteService;

	/** The jnj laudo model. */
	private JnjLaudoModel jnjLaudoModel;
	
	private UserService userService;
	


	public UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	@Autowired
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;


	public void mapLaudoToHybris(final JnjLaudoData jnjLaudoData) throws BusinessException
	{
		final String METHOD_MAP_LAUDO_TO_HYBRIS = "Mapper - mapLaudoToHybris()";
		jnjLaudoModel = null;
		if (null != jnjLaudoData)
		{
			/* Fetching Existing Laudo Models on the basis of country and file name */
			/* Fetching Country of the Logged In User */
			final CountryModel countryModel = jnjLatamCustomerService.getCountryOfUser();
			if (null == countryModel)
			{
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_MAP_LAUDO_TO_HYBRIS,
						"No country Fetched for the Logged In User. Initiating BusinessException", JnjLaudoMapper.class);
				throw new BusinessException("No country Fetched for the Logged In User.", Laudo.LAUDO_EXCEPTION_CODE_EMSG007,
						Severity.BUSINESS_EXCEPTION);

			}
			else
			{
				JnjGTCoreUtil
						.logDebugMessage(Jnjlab2bcoreConstants.LAUDO,
								METHOD_MAP_LAUDO_TO_HYBRIS, "Hitting DAO layer to fetch existying Laudo Model with File name. = "
										+ jnjLaudoData.getPdfFileName() + " and Country. = " + countryModel.getIsocode(),
								JnjLaudoMapper.class);
				final boolean fileNameExist = jnjLaudoService.checkFileNameExists(jnjLaudoData, countryModel);
				if (fileNameExist)
				{
					JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_MAP_LAUDO_TO_HYBRIS,
							"file name is already exist ino hybris mode . Initiating BusinessException", JnjLaudoMapper.class);
					throw new BusinessException("File name : " + jnjLaudoData.getPdfFileName()
							+ " already exist in hybris for country : " + countryModel.getIsocode(), Laudo.LAUDO_EXCEPTION_CODE_EMSG008,
							Severity.BUSINESS_EXCEPTION);
				}
			}

			/* Fetching Existing Laudo Models */
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO,
					METHOD_MAP_LAUDO_TO_HYBRIS, "Hitting DAO layer to fetch existying Laudo Model with LoudoNo. = "
							+ jnjLaudoData.getLaudoNumber() + " and ProductCode. = " + jnjLaudoData.getProductCode(),
					JnjLaudoMapper.class);
			jnjLaudoModel = modelService.create(JnjLaudoModel.class);
			
			/* Mapping CSV values from LaudoData to LaudoModel */
			jnjLaudoModel.setProductCode(jnjLaudoData.getProductCode());
			jnjLaudoModel.setLaudoNumber(jnjLaudoData.getLaudoNumber());
			jnjLaudoModel.setExpirationDate(jnjLaudoData.getExpirationDate());
			jnjLaudoModel.setFileName(jnjLaudoData.getPdfFileName().toLowerCase());
			jnjLaudoModel.setCountry(countryModel);
			jnjLaudoModel.setBatchRequired(jnjLaudoData.getBatchRequired());

			/* Calling the Service to save Laudo Model. */

			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO,
					METHOD_MAP_LAUDO_TO_HYBRIS, "Calling Service layer to save Laudo Model with LoudoNo. = "
							+ jnjLaudoModel.getLaudoNumber() + " and ProductCode. = " + jnjLaudoModel.getProductCode(),
					JnjLaudoMapper.class);
			jnjLaudoService.saveLaudoDetails(jnjLaudoModel);

		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_MAP_LAUDO_TO_HYBRIS,
					"JnjLaudoData received as Null. Initiating BusinessException.", JnjLaudoMapper.class);
			throw new BusinessException("JnjLaudoData received as Null. ", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
					Severity.BUSINESS_EXCEPTION);
		}
	}


	public void addOrUpdatePdfToLaudo(final File pdfFile) throws BusinessException, IOException
	{
		final String METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO = "Mapper - addOrUpdatePdfToLaudo()";
		if (pdfFile.exists())
		{
			jnjLaudoModel = jnjLaudoService.getLaudoByFileName(pdfFile.getName().toLowerCase());

			if (null == jnjLaudoModel)
			{
				throw new BusinessException("No Laudo Entry Found for file with fileName: " + pdfFile.getName(),
						Laudo.LAUDO_GENERAL_EXCEPTION_CODE, Severity.BUSINESS_EXCEPTION);
			}

			final Set<CatalogVersionModel> catalogVersions = getCurrentContentCatalogVersions();
			if (null != catalogVersions && !catalogVersions.isEmpty())
			{
				for (final CatalogVersionModel catalogVersionModel : catalogVersions)
				{
					InputStream inputStream = null;
					final MediaModel mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
					mediaModel.setFolder(mediaService.getFolder(Config.getString(Jnjlab2bcoreConstants.Laudo.LAUDO_MEDIA_FOLDER_NAME,
							Jnjlab2bcoreConstants.Laudo.DEFAULT_LAUDO_MEDIA_FOLDER_NAME))); //Setting the Media folder
					mediaModel.setCatalogVersion(catalogVersionModel);
					mediaModel.setCode(pdfFile.getName().toLowerCase());
					mediaModel.setRealFileName(pdfFile.getName().toLowerCase());
					
					final UserModel currentUserModel = userService.getCurrentUser();
					
					JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO, "Laudo File name: "+pdfFile.getName().toLowerCase()+" User id: "+currentUserModel.getUid(), JnjLaudoMapper.class);
					try
					{
						jnjLatamMediaService.saveMedia(mediaModel);
						inputStream = new FileInputStream(pdfFile); // Creating InputStream from PDF file,
					}
					catch (final FileNotFoundException fileNotFoundException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"FileNotFoundException occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName().toLowerCase()+" User id: "+currentUserModel.getUid(), fileNotFoundException,
								JnjLaudoMapper.class);
						throw new BusinessException("FileNotFoundException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					catch (final ModelSavingException modelSavingException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"ModelSavingException occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName().toLowerCase()+" User id: "+currentUserModel.getUid(), modelSavingException,
								JnjLaudoMapper.class);
						throw new BusinessException("FileNotFoundException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					catch (final Exception exception)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"Exception occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName().toLowerCase()+" User id: "+currentUserModel.getUid(), exception, JnjLaudoMapper.class);
						throw new BusinessException("FileNotFoundException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					try
					{
						mediaService.setStreamForMedia(mediaModel, inputStream, pdfFile.getName(),
								Files.probeContentType(Path.of(pdfFile.getAbsolutePath()))); //Setting the File into the Media Model
					}
					catch (final MediaIOException mediaIOException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"MediaIOException occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName()+" User id: "+currentUserModel.getUid(), mediaIOException, JnjLaudoMapper.class);
						throw new BusinessException("MediaIOException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					catch (final IllegalArgumentException illegalArgumentException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"IllegalArgumentException occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName()+" User id: "+currentUserModel.getUid(), illegalArgumentException,
								JnjLaudoMapper.class);
						throw new BusinessException("IllegalArgumentException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					catch (final IOException iOException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"IOException occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName()+" User id: "+currentUserModel.getUid(), iOException, JnjLaudoMapper.class);
						throw new BusinessException("IOException occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					catch (final Exception exception)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
								"Exception occured. Cascading to BusinessException. "+"File name: "+pdfFile.getName()+" User id: "+currentUserModel.getUid(), exception, JnjLaudoMapper.class);
						throw new BusinessException("Exception occured.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
								Severity.BUSINESS_EXCEPTION);
					}
					finally
					{
						if (null != inputStream)
						{
							inputStream.close();
						}
					}

					//Setting the Media in the Laudo Model.
					jnjLaudoModel.setFileMedia(mediaModel, Locale.of("es"));
					jnjLaudoModel.setFileMedia(mediaModel, Locale.of("en"));
					jnjLaudoModel.setFileMedia(mediaModel, Locale.of("pt"));
					//Calling the Service to save Laudo Model.
					jnjLaudoService.saveLaudoDetails(jnjLaudoModel);
				}
			}
			else
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
						"Cannot Retirve Content Catalog Versions. Throwing Exception.", JnjLaudoMapper.class);
				throw new BusinessException("Cannot Retirve Content Catalog Versions.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
						Severity.BUSINESS_EXCEPTION);
			}
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_ADD_OR_UPDATE_PDF_TO_LAUDO,
					"PDF file does not exists. Initiating BusinessException.", JnjLaudoMapper.class);
			throw new BusinessException("PDF file does not exists.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
					Severity.BUSINESS_EXCEPTION);
		}
	}

	/**
	 * This method fetched the All the versions of Current Content Catalog.
	 *
	 * @return the current content catalog versions
	 * @throws BusinessException
	 *            the business exception
	 */
	public Set<CatalogVersionModel> getCurrentContentCatalogVersions() throws BusinessException
	{

		final String METHOD_GET_CURRENT_CONTENT_CATALOG = "Mapper - getCurrentContentCatalogVersions()";
		//Getting Both 'Online' and 'Staged' versions of ContentCatalaog
		final List<ContentCatalogModel> contentCatalogs = cMSSiteService.getCurrentSite().getContentCatalogs();
		ContentCatalogModel contentCatalogModel = null;
		if (null != contentCatalogs && !(contentCatalogs.isEmpty()))
		{
			contentCatalogModel = contentCatalogs.get(0);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_CURRENT_CONTENT_CATALOG,
					"Cannot Retirve Content Catalog. Throwing Exception.", JnjLaudoServiceImpl.class);
			throw new BusinessException("Cannot Retirve Content Catalog");
		}
		final Set<CatalogVersionModel> catalogVersions = contentCatalogModel.getCatalogVersions();

		return catalogVersions;
	}
}
