/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.facade.impl;


import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import com.jnj.gt.pcm.integration.data.JnjGTClassificationData;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataLocalAttributes;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataResponse;
import com.jnj.gt.pcm.integration.facade.JnjCCP360IntegrationFacade;
import com.jnj.gt.pcm.integration.facade.JnjGTProductContentFacade;
import com.jnj.gt.pcm.integration.util.JnjPCMCommonFacadeUtil;

/**
 * This class contains implementation of pulling Product rich content data from
 * P360 and updating in Hybris DB
 *
 */
public class DefaultJnjGTProductContentFacade implements JnjGTProductContentFacade {
	private static final Logger LOG = Logger.getLogger(DefaultJnjGTProductContentFacade.class);

	private static final String CLASSIFICATION_CLASS_CODE = "classificationClassCode";
	private static final String FEATURE_CODE = "featureCode";
	private static final String FEATURE_VALUE = "featureValue";
	private static final String LANGUAGE = "featureLanguage";
	private static final String PRODUCT = "product";
	private static final String ABOUT_BRAND = "aboutBrand";
	private static final String LONG_DESCRIPTION = "longDescription";
	private static final String SHORT_OVERVIEW = "shortOverview";
	private static final String PRODUCT_DOCUMENTS_LIST = "productDocumentsList";
	private static final String COUNTRIES = "countries";
	private static final String RELATED_PRODUCT_CODES = "relatedProductCodes";
	private static final String SUBMISSION_ITEM_NUMBER = "submissionItemNumber";
	private static final String COPY_EXPIRATION_DATE = "copyExpirationDate";
	private static final String REGIONS = "regions";
	private static final String BRANDS = "brands";
	private static final String NAME = "name";
	private static final String URL_LINK = "urlLink";
	private static final String CODE = "code";
	private static final String ATTR_VALUE = "attrValue";
	private static final String LOCALE = "locale";
	private static final int COUNT_ZERO = 0;
	private static final int INITIAL_CURRENT_PAGE = 2;
	private static final int FIRST_PAGE_ID = 1;

	boolean runRegionalFlag;
	private JnjCCP360IntegrationFacade jnjCCP360IntegrationFacade;
	private JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil;
	private ConfigurationService configurationService;
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.jnj.facades.pcmintegration.impl.JnjGTProductContentFacade#getProductData(
	 * boolean)
	 */
	@Override
	public boolean getProductData(final JnjGTPCMIntegrationCronJobModel jobModel) {
		runRegionalFlag = jobModel.getIsRegionJob();
		String url = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_COUNTRY_URL);
		String regionOrCountryParam = JnjpcmfacadeConstants.PCMIntegration.COUNTRY;

		if (runRegionalFlag) {
			url = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_REGION_URL);
			regionOrCountryParam = JnjpcmfacadeConstants.PCMIntegration.REGION;
		
		}
		final String startDate = jnjPCMCommonFacadeUtil.getStartDate();

		final JSONParser parser = new JSONParser();
		try {
			final String jsonData = jnjCCP360IntegrationFacade.getJsonDataForProduct(url, startDate, jobModel, regionOrCountryParam,
					 FIRST_PAGE_ID);
			final JSONObject obj = (JSONObject) parser.parse(jsonData);
			final Object object = getProdObject(obj);

			if (null != object) {
				retrieveProductDataAndPopulate(obj, url, startDate, regionOrCountryParam, jobModel);
			} else {
				LOG.info("No Product Response Found For Date: " + startDate + " & sector: " + jobModel.getSector());
			}

		} catch (final Exception e) {
			LOG.error(e);
			return false;
		}
		return true;
	}

	protected void retrieveProductDataAndPopulate(final JSONObject obj, final String url, final String startDate,
			final String regionOrCountryParam,
			final JnjGTPCMIntegrationCronJobModel jobModel) throws org.json.simple.parser.ParseException {
		Map<String, JnjPCMGTProductDataResponse> productDataMap = null;
		final Object object = getProdObject(obj);
		final JSONObject paginationData = (JSONObject) obj.get(JnjpcmfacadeConstants.PCMIntegration.PAGINATION_DATA);
		final JSONArray jsonArray = (JSONArray) object;
		final JSONParser parser = new JSONParser();
		final int totalAssetCount = Integer
				.parseInt(paginationData.get(JnjpcmfacadeConstants.PCMIntegration.TOTAL_NUMBER_OF_RESULTS).toString());
		final int numberOfPages = Integer
				.parseInt(paginationData.get(JnjpcmfacadeConstants.PCMIntegration.NUMBER_OF_PAGES).toString());
		LOG.info("Total number of pages" + numberOfPages);
		LOG.info("Total Count" + totalAssetCount);
		productDataMap = productCodeAndDataMap(jsonArray);
		populateProductContent(productDataMap, jobModel);

		for (int currentPageNumber = INITIAL_CURRENT_PAGE; currentPageNumber <= numberOfPages; currentPageNumber++) {
			final String jsonData = jnjCCP360IntegrationFacade.getJsonDataForProduct(url, startDate, jobModel, regionOrCountryParam,
					 currentPageNumber);
			LOG.info("Hitting for currentpage " + currentPageNumber);
			try {
				final JSONObject productsObject = (JSONObject) parser.parse(jsonData);
				final Object paginatedObject = getProdObject(productsObject);
				final JSONArray paginatedProductsArray = (JSONArray) paginatedObject;
				productDataMap = productCodeAndDataMap(paginatedProductsArray);
				populateProductContent(productDataMap, jobModel);
			} catch (final ParseException e) {
				LOG.debug(e);
			}
		}
	}

	protected Object getProdObject(final JSONObject obj) {
		return obj.get(JnjpcmfacadeConstants.PCMIntegration.PRODUCTS);
	}

	protected Map<String, JnjPCMGTProductDataResponse> productCodeAndDataMap(final JSONArray jsonArray) {
		final Map<String, JnjPCMGTProductDataResponse> productDataMap = new HashMap<>();
		if (jsonArray != null) {
			for (int i = COUNT_ZERO; i < jsonArray.size(); i++) {
				updateProductDataMap(jsonArray, productDataMap, i);
			}

		}
		return productDataMap;
	}

	protected void updateProductDataMap(final JSONArray jsonArray,
			final Map<String, JnjPCMGTProductDataResponse> productDataMap, final int i) {
		final JSONObject innerObj = (JSONObject) jsonArray.get(i);
		final JSONObject productObject = (JSONObject) innerObj.get(PRODUCT);

		final JSONArray aboutBrandList = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, ABOUT_BRAND);
		final JSONArray longDescriptionList = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, LONG_DESCRIPTION);
		final JSONArray shortOverviewList = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, SHORT_OVERVIEW);
		final JSONArray productDocumentsList = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, PRODUCT_DOCUMENTS_LIST);
		final JSONArray countriesList = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, COUNTRIES);
		final JSONArray classificationData = jnjCCP360IntegrationFacade.getAttributeJsonArray(productObject, "featureList");

		final List<JnjPCMGTProductDataLocalAttributes> aboutBrand = new ArrayList<>();
		final List<JnjPCMGTProductDataLocalAttributes> longDescription = new ArrayList<>();
		final List<JnjPCMGTProductDataLocalAttributes> shortOverview = new ArrayList<>();

		final JnjPCMGTProductDataResponse response = new JnjPCMGTProductDataResponse();

		if (productObject.get(CODE) != null) {
			response.setCode(productObject.get(CODE).toString().trim());
		}
		if (productObject.get(RELATED_PRODUCT_CODES) != null) {
			response.setRelatedProductCodes(productObject.get(RELATED_PRODUCT_CODES).toString());
		}
		if (productObject.get(SUBMISSION_ITEM_NUMBER) != null) {
			response.setSubmissionItemNumber(productObject.get(SUBMISSION_ITEM_NUMBER).toString());
		}
		if (productObject.get(COPY_EXPIRATION_DATE) != null) {
			response.setCopyExpirationDate(productObject.get(COPY_EXPIRATION_DATE).toString());
		}
		if (productObject.get(REGIONS) != null) {
			response.setRegions(productObject.get(REGIONS).toString());
		}
		if (productObject.get(BRANDS) != null) {
			response.setbrands(productObject.get(BRANDS).toString());
		}
		response.setAboutBrand(setResponseLocalAttributes(aboutBrand, aboutBrandList));
		response.setLongDescription(setResponseLocalAttributes(longDescription, longDescriptionList));
		response.setShortOverview(setResponseLocalAttributes(shortOverview, shortOverviewList));
		populateCountriesInResponse(response, countriesList);
		populateProductDocumentInResponse(response, productDocumentsList);
		populateClassificationDataInResponse(response, classificationData);

		productDataMap.put(productObject.get(CODE).toString(), response);

	}

	protected void populateCountriesInResponse(final JnjPCMGTProductDataResponse response,
			final JSONArray countriesList) {
		if (null != countriesList && countriesList.size() > COUNT_ZERO) {
			final List<String> countries = new ArrayList<>();
			for (int k = COUNT_ZERO; k < countriesList.size(); k++) {
				countries.add(countriesList.get(k).toString());
			}
			response.setCountries(countries);
		}
	}

	protected void populateProductDocumentInResponse(final JnjPCMGTProductDataResponse response,
			final JSONArray productDocumentsList) {
		if (null != productDocumentsList && productDocumentsList.size() > COUNT_ZERO) {

			final Map<String, String> productDocuments = new HashMap<>();
			for (int k = COUNT_ZERO; k < productDocumentsList.size(); k++) {
				final JSONObject productDocumentsObject = (JSONObject) productDocumentsList.get(k);
				if (null != productDocumentsObject && null != productDocumentsObject.get(NAME)
						&& null != productDocumentsObject.get(URL_LINK)) {
					productDocuments.put(productDocumentsObject.get(NAME).toString(),
							productDocumentsObject.get(URL_LINK).toString());
				}

			}
			response.setProductDocumentsList(productDocuments);
		}
	}

	protected void populateClassificationDataInResponse(final JnjPCMGTProductDataResponse response,
			final JSONArray classificationData) {
		if (null != classificationData && classificationData.size() > COUNT_ZERO) {
			final List<JnjGTClassificationData> classificationFeatureData = new ArrayList<>();
			for (int k = COUNT_ZERO; k < classificationData.size(); k++) {
				final JSONObject classificationObject = (JSONObject) classificationData.get(k);

				final JnjGTClassificationData jnjFeatureData = new JnjGTClassificationData();
				jnjFeatureData.setClassificationClass(classificationObject.get(CLASSIFICATION_CLASS_CODE).toString());
				jnjFeatureData.setFeatureName(classificationObject.get(FEATURE_CODE).toString());
				jnjFeatureData.setFeatureValue(classificationObject.get(FEATURE_VALUE).toString());
				jnjFeatureData.setLanguage(classificationObject.get(LANGUAGE).toString());
				classificationFeatureData.add(jnjFeatureData);
			}
			response.setClassificationData(classificationFeatureData);
		}
	}

	protected List<JnjPCMGTProductDataLocalAttributes> setResponseLocalAttributes(
			final List<JnjPCMGTProductDataLocalAttributes> productAttribute, final JSONArray producAttributeList) {

		if (producAttributeList != null && producAttributeList.size() > COUNT_ZERO) {	

			for (int k = COUNT_ZERO; k < producAttributeList.size(); k++) {
				final JSONObject productJsn = (JSONObject) producAttributeList.get(k);
				
				if (null != productJsn && null != productJsn.get(LOCALE) && null != productJsn.get(ATTR_VALUE)) {
					final JnjPCMGTProductDataLocalAttributes productNameLoc = new JnjPCMGTProductDataLocalAttributes(
							productJsn.get(LOCALE).toString(),productJsn.get(ATTR_VALUE).toString());
					productAttribute.add(productNameLoc);
				}

			}
		}
		return productAttribute;
	}
	
	/**
	 * This method is used to populate product content
	 * 
	 * @param productDataMap Input parameter which stores P360 response
	 * @param jobModel parameter to fetch CronJob Model
	 */
	public void populateProductContent(final Map<String, JnjPCMGTProductDataResponse> productDataMap,
			final JnjGTPCMIntegrationCronJobModel jobModel) {

		for (final Map.Entry<String, JnjPCMGTProductDataResponse> prodctData : productDataMap.entrySet()) {
			final String productCode = prodctData.getKey();
			saveProductContent(jobModel.getCatalogVersion(), prodctData, productCode);
		}
	}
	
	/**
	 * 
	 * @param catalogVersion
	 * @param prodctData
	 * @param productCode
	 */

	private void saveProductContent(final CatalogVersionModel catalogVersion,
			final Map.Entry<String, JnjPCMGTProductDataResponse> prodctData, final String productCode) {
		try {
			final JnjPCMGTProductDataResponse response = prodctData.getValue();
			if (null != productCode && null != catalogVersion) {
				final JnJProductModel jnjProductModel = jnjCCP360IntegrationFacade.getProductModel(productCode,
						catalogVersion);
				if (null == jnjProductModel) {
					LOG.info("PRODUCT MODEL NOT AVAILABLE IN HYBRIS: " + productCode);
					return;
				}
				jnjCCP360IntegrationFacade.populateProductDetails(productCode, jnjProductModel, response,
						catalogVersion);

				modelService.save(jnjProductModel);
				LOG.info("Product Model saved for product : " + productCode);
			}
		 } catch (Exception e) {
			   LOG.error("context",e);
			   LOG.info("Product Model could not be saved for product : " + productCode);
		}
	}

	public JnjCCP360IntegrationFacade getJnjCCP360IntegrationFacade() {
		return jnjCCP360IntegrationFacade;
	}

	public void setJnjCCP360IntegrationFacade(JnjCCP360IntegrationFacade jnjCCP360IntegrationFacade) {
		this.jnjCCP360IntegrationFacade = jnjCCP360IntegrationFacade;
	}

	public JnjPCMCommonFacadeUtil getJnjPCMCommonFacadeUtil() {
		return jnjPCMCommonFacadeUtil;
	}

	public void setJnjPCMCommonFacadeUtil(JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil) {
		this.jnjPCMCommonFacadeUtil = jnjPCMCommonFacadeUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}	
	
	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
}
