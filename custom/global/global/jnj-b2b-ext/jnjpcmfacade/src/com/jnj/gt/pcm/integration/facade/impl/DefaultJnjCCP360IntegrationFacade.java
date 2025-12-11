/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.facade.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import com.jnj.gt.pcm.integration.core.job.service.JnjCCP360IntegrationService;
import com.jnj.gt.pcm.integration.data.JnjGTClassificationData;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataLocalAttributes;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataResponse;
import com.jnj.gt.pcm.integration.facade.JnjCCP360IntegrationFacade;
import com.jnj.gt.pcm.integration.util.JnjPCMCommonFacadeUtil;
import com.jnj.gt.service.product.JnjGTProductFeedService;


/**
 * This class handles the generation and upload of CSV files in SMTP server of P360
 *
 */
public class DefaultJnjCCP360IntegrationFacade implements JnjCCP360IntegrationFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjCCP360IntegrationFacade.class);
	private static final String CLASSIFICATION_NAME_VERSION = "pcmMedicalClassCatalog/1.0/";
	private RestTemplate restTemplate;
	private HttpEntity<String> request1;
	String accessToken = "";

	private JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil;
	private JnjCCP360IntegrationService jnjCCP360IntegrationService;
	private ConfigurationService configurationService;
	private JnjGTProductFeedService jnjGTProductFeedService;
	private ModelService modelService;
	private ClassificationService classificationService;
	

	@Override
	public String getJsonDataForProduct(final String url, final String startDate, final JnjGTPCMIntegrationCronJobModel jobModel,
			final String regionOrCountryParam, final int currentPageNumber)
	{

		String jsonData = null;
		initializeVariables();
		accessToken = jnjPCMCommonFacadeUtil.getAccessToken();
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.START_DATE, startDate);
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ISO_CODE, jobModel.getSector());
		builder.queryParam(regionOrCountryParam, jobModel.getCountryCode());
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.PAGE_ID, String.valueOf(currentPageNumber));
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ACCESS_TOKEN, accessToken);
		jsonData = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, request1, String.class).getBody();

		return jsonData;

	}

	@Override
	public String getJsonDataForCategory(final String url, final String startDate, final JnjGTPCMIntegrationCronJobModel jobModel,
			final int currentPageNumber)
	{
		String jsonData = null;
		initializeVariables();
		accessToken = jnjPCMCommonFacadeUtil.getAccessToken();
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.START_DATE, startDate);
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ISO_CODE, jobModel.getSector());
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.PAGE_ID, String.valueOf(currentPageNumber));
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ACCESS_TOKEN, accessToken);
		jsonData = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, request1, String.class).getBody();

		return jsonData;

	}

	protected void initializeVariables()
	{
		if (restTemplate == null)
		{
			restTemplate = new RestTemplate();
		}
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new BufferedImageHttpMessageConverter());

		final HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent",
				configurationService.getConfiguration().getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_USER_AGENT));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		if (request1 == null)
		{
			request1 = new HttpEntity<>(headers);
		}

	}

	@Override
	public boolean uploadProductDetailFile()
	{

		final String startDate = jnjPCMCommonFacadeUtil.getStartDate();
		return jnjCCP360IntegrationService.uploadProductDetailFile(startDate);
	}

	@Override
	public JSONArray getAttributeJsonArray(final JSONObject productObject, final String attributeName)
	{
		JSONArray productAttributeList;
		if (productObject.get(attributeName) instanceof JSONArray)
		{
			productAttributeList = (JSONArray) productObject.get(attributeName);
		}
		else
		{
			productAttributeList = new JSONArray();
			if (productObject.get(attributeName) != null)
			{
				productAttributeList.add(productObject.get(attributeName));
			}
		}
		return productAttributeList;
	}

	
	@Override
	public void populateProductDetails(String productCode, JnJProductModel jnjProductModel, JnjPCMGTProductDataResponse response, CatalogVersionModel catalogVersion) {
		
		final List<JnjPCMGTProductDataLocalAttributes> aboutBrandList = response.getAboutBrand();
		final List<JnjPCMGTProductDataLocalAttributes> longDescriptionList = response.getLongDescription();
		final List<JnjPCMGTProductDataLocalAttributes> shortOverviewList = response.getShortOverview();
		final List<String> countriesList = response.getCountries();
		
		populateProductSuperCategories(jnjProductModel, response.getbrands(), productCode, catalogVersion);
		populateBrandDesription(aboutBrandList, jnjProductModel);
		populateLongDescription(longDescriptionList, productCode, jnjProductModel);
		populateShortOverview(shortOverviewList, productCode, jnjProductModel);
		populateProductCountries(countriesList, jnjProductModel, productCode);
		if (null != response.getRegions())
		{
			jnjProductModel.setRegionCode(response.getRegions());
			LOG.info("RegionCode updated successfuly for product : " + productCode);
		}		
		populateRelatedProducts(response.getRelatedProductCodes(), jnjProductModel, productCode, catalogVersion);
		populateProductDocument(response.getProductDocumentsList(), jnjProductModel, productCode);
		populateClassificationattributes(response.getClassificationData(), jnjProductModel);
		
	}

	protected void populateLongDescription(final List<JnjPCMGTProductDataLocalAttributes> longDescriptionList,
			final String productCode, final JnJProductModel jnjProductModel)
	{
		if (null != longDescriptionList)
		{
			for (final JnjPCMGTProductDataLocalAttributes attributes : longDescriptionList)
			{
				final Locale locale = createLocale(attributes.getLocale().toLowerCase(Locale.ENGLISH));
				try {
					jnjProductModel.setDescription(attributes.getAttrValue(), locale);
				} catch (IllegalArgumentException e) {
						LOG.info("local unavailable for the local"+locale);
				}
			}
			LOG.info("Description updated successfuly for product : " + productCode);
		}
	}

	protected void populateShortOverview(final List<JnjPCMGTProductDataLocalAttributes> shortOverviewList,
			final String productCode, final JnJProductModel jnjProductModel)
	{
		if (null != shortOverviewList)
		{

			for (final JnjPCMGTProductDataLocalAttributes attributes : shortOverviewList)
			{
				final Locale locale = createLocale(attributes.getLocale().toLowerCase(Locale.ENGLISH));
				try {
					jnjProductModel.setShortOverview(attributes.getAttrValue(), locale);
				} catch (IllegalArgumentException e) {
					LOG.info("local unavailable for the local" + locale);
				}
			}
			LOG.info("Short Overview updated successfuly for product : " + productCode);
		}
	}

	protected void populateBrandDesription(final List<JnjPCMGTProductDataLocalAttributes> aboutBrandList,
			final JnJProductModel jnjProductModel)
	{
		if (null != aboutBrandList)
		{
			for (final JnjPCMGTProductDataLocalAttributes attributes : aboutBrandList) {
				final Locale locale = createLocale(attributes.getLocale().toLowerCase(Locale.ENGLISH));
				try {
					jnjProductModel.setAboutTheBrand(attributes.getAttrValue(), locale);
				} catch (IllegalArgumentException e) {
					LOG.info("local unavailable for the local" + locale);
				}
				LOG.info("Brand Description updated successfuly for Product : " + jnjProductModel.getCode());

			}
		}
	}

	protected void populateProductCountries(final List<String> countriesList, final JnJProductModel jnjProductModel,
			final String productCode)
	{
		if (null != countriesList)
		{
			final List<CountryModel> countries = new ArrayList<>();
			for (final String country : countriesList)
			{
				CountryModel countryModel = null;
				try
				{
					countryModel = jnjGTProductFeedService.getCountryByIso(country);
				}
				catch (final UnknownIdentifierException exception)
				{
					LOG.error("No country for this isocode: " + country + " " + exception);
				}
				if (null != countryModel)
				{
					countries.add(countryModel);
				}
			}
			if (!countries.isEmpty())
			{
				jnjProductModel.setCountries(countries);
			}
			LOG.info("Countries updated successfuly for product : " + productCode);
		}
	}

	protected void populateProductSuperCategories(final JnJProductModel jnjProductModel, final String brandCodes,
			final String productCode, final CatalogVersionModel catalogVersion)
	{
		final List<CategoryModel> catList = new ArrayList<>();
		final List<String> categoryCodes = Arrays.asList(brandCodes.split(Jnjb2bCoreConstants.SYMBOl_COMMA));
		final ListIterator<String> iterator = categoryCodes.listIterator();
		while (iterator.hasNext())
		{
			CategoryModel catModel = null;
			try
			{
				catModel = getJnjCCP360IntegrationService().getCategoryModel(iterator.next(), catalogVersion);
				if (catModel != null)
				{
					catList.add(catModel);
				}
			}
			catch (final ModelNotFoundException exception)
			{
				LOG.error("No category for the code: " + exception);
			}

			if (!catList.isEmpty())
			{
				jnjProductModel.setSupercategories(catList);
			}
			LOG.info("Categories updated successfuly for product : " + productCode);
		}
	}

	protected void populateRelatedProducts(final String relatedProducts, final JnJProductModel jnjProductModel,
			final String productCode, final CatalogVersionModel catalogVersion)
	{
		if (null != relatedProducts)
		{
			final List<ProductReferenceModel> refList = new ArrayList<>();
			final List<String> relatedProductCodes = Arrays.asList(relatedProducts.split(Jnjb2bCoreConstants.SYMBOl_COMMA));
			final ListIterator<String> iterator = relatedProductCodes.listIterator();
			while (iterator.hasNext())
			{
				final JnJProductModel targetModel = getProductModel(iterator.next().trim(), catalogVersion);
				if (targetModel != null)
				{
					updateProductRefList(refList, jnjProductModel, targetModel);
				}

			}
			if (!refList.isEmpty())
			{
				if (jnjProductModel.getProductReferences() != null && !jnjProductModel.getProductReferences().isEmpty())
				{
					Collection<ProductReferenceModel> existingReferenceList = jnjProductModel.getProductReferences();
					modelService.removeAll(existingReferenceList);
					modelService.refresh(jnjProductModel);
				}
				jnjProductModel.setProductReferences(refList);
			}
			LOG.info("Related Products updated successfuly for product : " + productCode);
		}
	}

	/**
	 * @param targetModel
	 * @param jnjProductModel
	 * @param refList
	 *
	 */
	protected void updateProductRefList(final List<ProductReferenceModel> refList, final JnJProductModel jnjProductModel,
			final JnJProductModel targetModel)
	{

		final List<ProductReferenceModel> referenceMOdelList = getJnjCCP360IntegrationService()
				.getProductReferenceModelList(jnjProductModel, targetModel);
		if (referenceMOdelList != null && !referenceMOdelList.isEmpty())
		{
			refList.add(referenceMOdelList.get(0));
		}
		else
		{
			final ProductReferenceModel refModel = new ProductReferenceModel();
			refModel.setActive(Boolean.TRUE);
			refModel.setPreselected(Boolean.FALSE);
			refModel.setReferenceType(ProductReferenceTypeEnum.RELATED_PRODUCTS);
			refModel.setSource(jnjProductModel);
			refModel.setTarget(targetModel);
			refList.add(refModel);
		}


	}

	protected void populateProductDocument(final Map<String, String> productDocuments, final JnJProductModel jnjProductModel,
			final String productCode)
	{
		if (null != productDocuments)
		{
			final List<ProductDocumentsModel> productDocumentsList = new ArrayList<>();
			final Iterator<Map.Entry<String, String>> itr = productDocuments.entrySet().iterator();
			while (itr.hasNext())
			{
				ProductDocumentsModel productDocument = null;
				final Entry<String, String> entry = itr.next();
				productDocument = getJnjCCP360IntegrationService().getProductDocumentByNameAndUrl(entry.getKey(), entry.getValue());
				if (null != productDocument)
				{
					productDocumentsList.add(productDocument);
				}
				else
				{
					productDocument = new ProductDocumentsModel();
					productDocument.setName(entry.getKey());
					productDocument.setUrlLink(entry.getValue());
					productDocumentsList.add(productDocument);
				}
			}
			if (!productDocumentsList.isEmpty())
			{
				jnjProductModel.setProductDocumentlist(productDocumentsList);
			}
			LOG.info("Product Documents List updated successfuly for product : " + productCode);
		}
	}

	@Override
	public JnJProductModel getProductModel(final String code, final CatalogVersionModel catalogVersion)
	{
		return jnjGTProductFeedService.getProductByCode(code, catalogVersion);
	}

	protected void populateClassificationattributes(final List<JnjGTClassificationData> classificationFeatureData,
			final JnJProductModel jnjProductModel)
	{
		if (null != classificationFeatureData && !classificationFeatureData.isEmpty())
		{
			for (final JnjGTClassificationData classificationData : classificationFeatureData)
			{

				final FeatureList featureList = classificationService.getFeatures(jnjProductModel);
				final String classificationFeatureName=classificationData.getFeatureName().toLowerCase(Locale.ENGLISH);
				final Feature feature = featureList.getFeatureByCode(CLASSIFICATION_NAME_VERSION	+ classificationData.getClassificationClass() + "." +classificationFeatureName );
				addFeatureValue(jnjProductModel, classificationData, featureList, feature);
			}

		}

	}

	/**
	 * @param jnjProductModel
	 * @param classificationData
	 * @param featureList
	 * @param feature
	 */
	private void addFeatureValue(final JnJProductModel jnjProductModel,
			final JnjGTClassificationData classificationData, final FeatureList featureList, final Feature feature) {
		if (feature != null)
		{
			FeatureValue value = feature.getValue();
			if (value != null)
			{
				value.setValue(classificationData.getFeatureValue());
			}
			else
			{
				value = new FeatureValue(classificationData.getFeatureValue());
				feature.addValue(value);
			}
			classificationService.setFeatures(jnjProductModel, featureList);
		}
	}
	
	private static Locale createLocale(final String language)
	{
		if(language.contains("_")){
				String[] lang = language.split("_");
				return Locale.of(lang[0], lang[1]);
		}else{
			return Locale.of(language.toLowerCase(Locale.ENGLISH));
		}
	}
	
	public JnjPCMCommonFacadeUtil getJnjPCMCommonFacadeUtil()
	{
		return jnjPCMCommonFacadeUtil;
	}

	public void setJnjPCMCommonFacadeUtil(final JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil)
	{
		this.jnjPCMCommonFacadeUtil = jnjPCMCommonFacadeUtil;
	}

	public JnjCCP360IntegrationService getJnjCCP360IntegrationService()
	{
		return jnjCCP360IntegrationService;
	}

	public void setJnjCCP360IntegrationService(final JnjCCP360IntegrationService jnjCCP360IntegrationService)
	{
		this.jnjCCP360IntegrationService = jnjCCP360IntegrationService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public JnjGTProductFeedService getJnjGTProductFeedService() {
		return jnjGTProductFeedService;
	}

	public void setJnjGTProductFeedService(JnjGTProductFeedService jnjGTProductFeedService) {
		this.jnjGTProductFeedService = jnjGTProductFeedService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public ClassificationService getClassificationService() {
		return classificationService;
	}

	public void setClassificationService(ClassificationService classificationService) {
		this.classificationService = classificationService;
	}
	
	

}
