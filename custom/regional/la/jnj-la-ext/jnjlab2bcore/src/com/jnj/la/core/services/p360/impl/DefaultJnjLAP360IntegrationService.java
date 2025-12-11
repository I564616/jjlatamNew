/**
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.p360.impl;

import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.pcm.integration.core.job.service.impl.DefaultJnjCCP360IntegrationService;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.daos.JnjLaProductDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class overrides the behavior of GT's P360 Integration Service and has the customized version for LA region
 *
 */
public class DefaultJnjLAP360IntegrationService extends DefaultJnjCCP360IntegrationService {
	private static final String DATE_FORMAT = "yyyyMMdd_hhmmssSSS";
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjLAP360IntegrationService.class);
	private JnjLaProductDao jnjLAProductDao;

	@Override
	public boolean uploadProductDetailFile(final String startDate) {
		final String methodName = "uploadProductDetailFile()";
		boolean uploadFile = false;

		final List<JnJProductModel> mddProdList = this.jnjLAProductDao.getProductsForSector(startDate, Jnjlab2bcoreConstants.MASTER_PRODUCT_CATALOG_ID, Jnjlab2bcoreConstants.CATALOG_VERSION_STAGED, Jnjlab2bcoreConstants.MDD_SECTOR);
		final List<JnJProductModel> phrProdList = this.jnjLAProductDao.getProductsForSector(startDate, Jnjlab2bcoreConstants.MASTER_PRODUCT_CATALOG_ID, Jnjlab2bcoreConstants.CATALOG_VERSION_STAGED, Jnjlab2bcoreConstants.PHR_SECTOR);
		if (!mddProdList.isEmpty()) {
			uploadFile = this.createProductDetailCSVFile(mddProdList, MDD_FILE_NAME);
			LOGGER.info(methodName + " MDD products Uploaded to P360");
		}
		if (!phrProdList.isEmpty()) {
			uploadFile = this.createProductDetailCSVFile(phrProdList, PHR_FILE_NAME);
			LOGGER.info(methodName + " PHARMA products Uploaded to P360");
		}
		return uploadFile;
	}

	@Override
	public boolean createProductDetailCSVFile(final List<JnJProductModel> productDetailList, final String fileNamePrefix) {
		boolean createFile = true;
		boolean uploadFile = true;
		final String region = configurationService.getConfiguration().getString(REGION);
		final DateFormat df2 = new SimpleDateFormat(DATE_FORMAT);
		final Date date = new Date();
		final String tempFolderPath = configurationService.getConfiguration().getString(UPLOAD_TEMP_FILE_LOCATION);
		final String metaDataOnlyCsvFileName = tempFolderPath + fileNamePrefix + Jnjlab2bcoreConstants.UNDERSCORE_SYMBOL + df2.format(date) + CSV;
		final File copyMetaDataCsvFileOnly = new File(metaDataOnlyCsvFileName);
		try (FileWriter writer = new FileWriter(copyMetaDataCsvFileOnly)) {
			writer.append(PRODUCT_CODE);
			writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
			writer.append(PRODUCT_NAME);
			writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
			writer.append(REGION_LABEL);
			writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
			writer.append(COUNTRY);
			writer.append(Jnjlab2bcoreConstants.NEXT_LINE);
			for (final JnJProductModel productModel : productDetailList) {
				final StringBuilder countriesName = jnjGTP360CountryNameStrategy.getCountriesName(productModel);
				writer.append(productModel.getCode());
				writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
				if (productModel.getName() != null) {
					writer.append(productModel.getName().replaceAll(Jnjlab2bcoreConstants.CONST_COMMA, StringUtils.EMPTY));
				} else {
					writer.append(StringUtils.EMPTY);
				}
				writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
				writer.append(region);
				writer.append(Jnjlab2bcoreConstants.CONST_COMMA);
				if (countriesName.length() != COUNT_ZERO) {
					writer.append(countriesName.toString());
				} else {
					writer.append(StringUtils.EMPTY);
				}
				writer.append(Jnjlab2bcoreConstants.NEXT_LINE);

			}
		} catch (final IOException e) {
			createFile = false;
			LOGGER.error("Error thrown while creating CSV File", e);
		}

		if (createFile) {
			LOGGER.info("File name "+metaDataOnlyCsvFileName);
			uploadFile = establishFTPConnection(tempFolderPath, metaDataOnlyCsvFileName);
			LOGGER.debug("Upload File Status " + uploadFile);
		}

		return uploadFile;
	}

	public JnjLaProductDao getJnjLAProductDao() {
		return jnjLAProductDao;
	}

	public void setJnjLAProductDao(JnjLaProductDao jnjLAProductDao) {
		this.jnjLAProductDao = jnjLAProductDao;
	}
}