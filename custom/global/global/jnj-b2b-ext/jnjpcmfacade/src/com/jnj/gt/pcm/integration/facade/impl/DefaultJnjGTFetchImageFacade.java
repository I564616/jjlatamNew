/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.facade.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import com.jnj.gt.pcm.integration.data.JnjGTProductImageResponse;
import com.jnj.gt.pcm.integration.facade.JnjGTFetchImageFacade;
import com.jnj.gt.pcm.integration.util.JnjPCMCommonFacadeUtil;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import org.apache.commons.collections4.CollectionUtils;

/**
 * This class contains implementation of pulling image data from P360 and saving it in the product model
 *
 */
public class DefaultJnjGTFetchImageFacade implements JnjGTFetchImageFacade
{
    private static final int START_INDEX = 0;
	private static final char END_INDEX = '.';
    private static final int FIRST_ID = 1;
	private static final String IMAGE = "image";
	private static final String CODE = "code";
	private static final String FULL_URL = "fullUrl";
	private static final String IMAGE_FILE_FORMAT = "imageFileFormat";
	private static final String IMAGE_LAST_UPD_DATE = "imageLastUpdDate";
	private static final String IMAGE_SIZE = "imageSize";
	private static final String IMAGE_TYPE = "imageType";
	private static final String QUALIFIER = "qualifier";
	private static final String FILE_NAME = "fileName";
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private static final int INITIAL_CURRENT_PAGE = 2;

	private static final Logger LOG = Logger.getLogger(DefaultJnjGTFetchImageFacade.class);

	private RestTemplate restTemplate;
	private MediaFormatModel mediaFormat300;
	private MediaFormatModel mediaFormat96;
	private MediaFormatModel mediaFormat65;
	private MediaFormatModel mediaFormat515;

	protected MediaFolderModel mediaFolderModel = null;

	String accessToken = "";
	boolean runRegionalFlag;

	private ModelService modelService;
	private CatalogVersionService catalogVersionService;
	private FlexibleSearchService flexibleSearchService;
	private JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil;
	private JnjGTProductFeedService jnjGTProductFeedService;
	private ConfigurationService configurationService;
	protected MediaService mediaService;

	private HttpEntity<JSONObject> request1;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.pcmintegration.impl.JnjGTFetchImageFacade#getPCMImageData( boolean)
	 */
	@Override
	public boolean getPCMImageData(final JnjGTPCMIntegrationCronJobModel jobModel)
	{
		runRegionalFlag = jobModel.getIsRegionJob();
		String url = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_IMAGE_INTEGRATION_COUNTRY_URL);
		String regionOrCountryParam = JnjpcmfacadeConstants.PCMIntegration.COUNTRY;

		if (runRegionalFlag)
		{
			url = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_IMAGE_INTEGRATION_REGION_URL);
			regionOrCountryParam = JnjpcmfacadeConstants.PCMIntegration.REGION;			
		}

		final JSONParser parser = new JSONParser();
		try
		{
			initializeVariables();
			final String jsonData = getJsonData(url, jobModel, regionOrCountryParam, FIRST_ID);

			final JSONObject obj = (JSONObject) parser.parse(jsonData);

			final Object objImage = getProdObject(obj);

			if (objImage != null)
			{
				processPCMApiImages(obj, url, regionOrCountryParam, jobModel);
			}
			else
			{
				LOG.info("No  Response Found For sector: " + jobModel.getSector());
			}

		}
		catch (final Exception exception)
		{
			LOG.error("Exception occurred while fetchin data from P360", exception);
			return false;
		}

		return true;
	}

	private void processPCMApiImages(final JSONObject obj, final String url, final String regionOrCountryParam,
			 final JnjGTPCMIntegrationCronJobModel jobModel)
	{
		final Map<String, Map<String, List<JnjGTProductImageResponse>>> imageMap = new HashMap<>();
		try
		{
			final Object object = getProdObject(obj);
			final JSONObject paginationData = (JSONObject) obj.get(JnjpcmfacadeConstants.PCMIntegration.PAGINATION_DATA);
			final int numberOfPages = Integer
					.parseInt(paginationData.get(JnjpcmfacadeConstants.PCMIntegration.NUMBER_OF_PAGES).toString());
			LOG.info("Total number of pages " + numberOfPages);
			final JSONArray jsonArray = (JSONArray) object;

			populatePCMImage(jsonArray, imageMap);

			final JSONParser parser = new JSONParser();
			for (int currentPageNumber = INITIAL_CURRENT_PAGE; currentPageNumber <= numberOfPages; currentPageNumber++)
			{
				final String jsonData = getJsonData(url, jobModel, regionOrCountryParam, currentPageNumber);
				LOG.info("Hitting for currentpage " + currentPageNumber);
				final JSONObject productsObject = (JSONObject) parser.parse(jsonData);
				final Object imagesObject = getProdObject(productsObject);
				final JSONArray jsonImgArray = (JSONArray) imagesObject;

				populatePCMImage(jsonImgArray, imageMap);
			}

			populateProductImages(imageMap, jobModel);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured during parsing of data from PCM ", e);
		}
	}

	protected Object getProdObject(final JSONObject obj)
	{
		return obj.get(JnjpcmfacadeConstants.PCMIntegration.IMAGES);
	}

	protected String getJsonData(final String url, final JnjGTPCMIntegrationCronJobModel jobModel,
			final String regionOrCountryParam, int currentPageNumber) {
		String jsonData = null;
		accessToken = jnjPCMCommonFacadeUtil.getAccessToken();
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ISO_CODE, jobModel.getSector());
		builder.queryParam(regionOrCountryParam, jobModel.getCountryCode());
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.PAGE_ID, String.valueOf(currentPageNumber));
		builder.queryParam(JnjpcmfacadeConstants.PCMIntegration.ACCESS_TOKEN, accessToken);
		jsonData = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, request1, String.class)
				.getBody();

		return jsonData;

	}

	protected void populatePCMImage(final JSONArray jsonArray,
			final Map<String, Map<String, List<JnjGTProductImageResponse>>> imageMap) {
		for (int objIndex = 0; objIndex < jsonArray.size(); objIndex++) {
			Set<Entry<String, Object>> set = null;
			final JSONObject innerObj = (JSONObject) jsonArray.get(objIndex);
			set = innerObj.entrySet();
			populateImageList(imageMap, set, innerObj);
		}

	}

	private void populateImageList(final Map<String, Map<String, List<JnjGTProductImageResponse>>> imageMap,
			Set<Entry<String, Object>> set, final JSONObject innerObj) {
		List<JnjGTProductImageResponse> imageResponseList;
		Map<String, List<JnjGTProductImageResponse>> productMap;
		for (int index = 0; index < set.size(); index++) {
			
			final JSONObject obj3 = (JSONObject) innerObj.get(IMAGE);
			boolean isValidImage = vaidateImage(obj3);
			
			if (isValidImage) {
				try {
					String fileName = obj3.get(FILE_NAME).toString();
					productMap = populateProductMap(imageMap, obj3);
					final String containerQualifier = StringUtils.substring(obj3.get(QUALIFIER).toString(), 0,
							StringUtils.lastOrdinalIndexOf(obj3.get(QUALIFIER).toString(), "-", 3));
					if (productMap.containsKey(containerQualifier)) {
						imageResponseList = productMap.get(containerQualifier);
						if (CollectionUtils.isNotEmpty(imageResponseList)) {
							boolean isImageFormatExists = imageResponseList.stream()
									.anyMatch(response -> fileName.equals(response.getFileName()));
							if (isImageFormatExists) {
				            	continue;
				            }   
						}
					} else {
						imageResponseList = new ArrayList<>();
					}

					final JnjGTProductImageResponse imageResponse = new JnjGTProductImageResponse();
					imageResponse.setCode(obj3.get(CODE).toString());
					imageResponse.setFileName(fileName);
					imageResponse.setUrl(obj3.get(FULL_URL).toString());
					imageResponse.setImageFileFormat(obj3.get(IMAGE_FILE_FORMAT).toString());
					imageResponse.setImageLastUpdDate(obj3.get(IMAGE_LAST_UPD_DATE).toString());
					imageResponse.setImageSize(obj3.get(IMAGE_SIZE).toString());
					imageResponse.setImageType(obj3.get(IMAGE_TYPE).toString());
					imageResponse.setQualifier(obj3.get(QUALIFIER).toString());
					final String fileNameString = obj3.get(FILE_NAME).toString();
					final String mediaCode = fileNameString.substring(START_INDEX,
							fileNameString.lastIndexOf(END_INDEX));
					imageResponse.setMediaCode(mediaCode);
					imageResponseList.add(imageResponse);
					productMap.put(containerQualifier, imageResponseList);
					imageMap.put(imageResponse.getCode(), productMap);
				} catch (final Exception e) {
					LOG.error("excception occur while parsing image data" + obj3,e);
				}
			}
		}
	}
	
	private static Map<String, List<JnjGTProductImageResponse>> populateProductMap(
			final Map<String, Map<String, List<JnjGTProductImageResponse>>> imageMap, final JSONObject obj3) {
		Map<String, List<JnjGTProductImageResponse>> productMap;
		if (imageMap.containsKey(obj3.get(CODE).toString())) {
			productMap = imageMap.get(obj3.get(CODE).toString());
		} else {
			productMap = new HashMap<>();
		}
		return productMap;
	}

	private void populateProductImages(final Map<String, Map<String, List<JnjGTProductImageResponse>>> imageMap,
			final JnjGTPCMIntegrationCronJobModel jobModel)

	{

		populateMediaFormat();
		getMediaFolder();

		for (final Map.Entry productData : imageMap.entrySet()) {
			final JnJProductModel jnJProductModel = getProductModel(productData.getKey().toString(),
					jobModel.getCatalogVersion());
			if (null == jnJProductModel) {
				continue;
			}
			final List<MediaContainerModel> mediaContainerModelList = new ArrayList<>();
			MediaModel mainImageMedia = null;
			MediaModel thumbNailMedia = null;
			String imageUpdateDate = "";
			final CatalogVersionModel catalogVersion = jnJProductModel.getCatalogVersion();
			final Map<String, List<JnjGTProductImageResponse>> containerMap = imageMap
					.get(productData.getKey().toString());
			final Map<String, List<JnjGTProductImageResponse>> sortedContainerMap = new TreeMap<>(containerMap);
			boolean firstflag = true;
			for (final Map.Entry containerData : sortedContainerMap.entrySet()) {
				try {
					final List<JnjGTProductImageResponse> imageList = (List<JnjGTProductImageResponse>) containerData
							.getValue();
					if (!imageList.isEmpty()) {
						final List<MediaModel> mediaModelList = new ArrayList<>();
						for (final JnjGTProductImageResponse response : imageList) {
							if (response.getQualifier()
									.equalsIgnoreCase(JnjpcmfacadeConstants.PCMIntegration.EMPTY_STRING)
									|| response.getQualifier().isEmpty() || response.getImageFileFormat()
											.contains(JnjpcmfacadeConstants.PCMIntegration.TIFF)) {
								continue;
							}
							MediaModel media = null;
							if (StringUtils.equalsIgnoreCase(response.getImageSize(),
									JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_ZOOM)) {
								media = createMedia(catalogVersion, response, mediaFormat515);
								mediaModelList.add(media);
							} else if (StringUtils.equalsIgnoreCase(response.getImageSize(),
									JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_MAIN)) {
								media = createMedia(catalogVersion, response, mediaFormat300);
								mediaModelList.add(media);
								if (firstflag) {
									mainImageMedia = media;
									imageUpdateDate = response.getImageLastUpdDate();
								}
							} else if (StringUtils.equalsIgnoreCase(response.getImageSize(),
									JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_CART_ICON)) {
								media = createMedia(catalogVersion, response, mediaFormat96);
								mediaModelList.add(media);
							} else if (StringUtils.equalsIgnoreCase(response.getImageSize(),
									JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_THUMBNAIL)) {
								media = createMedia(catalogVersion, response, mediaFormat65);
								mediaModelList.add(media);
								if (firstflag) {
									thumbNailMedia = media;
								}
							} else if (StringUtils.equalsIgnoreCase(response.getImageSize(),
									JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_ORIGINAL)) {
								media = createMedia(catalogVersion, response, null);
								LOG.info("#######################" + media);
								mediaModelList.add(media);
								LOG.info("#######################" + media);
							}
						}
						saveModelList(mediaModelList);
						LOG.debug("MediaModelList" + mediaModelList.size());
						LOG.debug("MediaModelList" + mediaModelList);
						mediaContainerModelList.add(
								setContainerModel(mediaModelList, jnJProductModel, containerData.getKey().toString()));
						mediaModelList.clear();
					}
					firstflag = false;
				} catch (Exception e) {
					LOG.error("exception in saving media for product " + productData.getKey().toString());
				}

			}
			saveProductMedia(jnJProductModel, mediaContainerModelList, mainImageMedia, thumbNailMedia, imageUpdateDate);
		}
	}

	private void saveModelList(final List<MediaModel> mediaModelList) {
		try
		{
		modelService.saveAll(mediaModelList);
		}catch (final ModelSavingException e)
		{
			LOG.info("Error occured while saving media list" + e);
		}
	}

	private void saveProductMedia(final JnJProductModel jnJProductModel, final List<MediaContainerModel> mediaContainerModelList,
			final MediaModel mainImageMedia, final MediaModel thumbNailMedia, final String imageUpdateDate)
	{
		try
		{
			jnJProductModel.setPicture(mainImageMedia);
			jnJProductModel.setThumbnail(thumbNailMedia);
			jnJProductModel.setGalleryImages(mediaContainerModelList);
			jnJProductModel.setImageLastUpdateDate(new SimpleDateFormat(DATE_FORMAT).parse(imageUpdateDate));
			jnJProductModel.setImageNeedsToBeConverted(true);

			modelService.saveAll(jnJProductModel);
		}
		catch (final ParseException e)
		{
			LOG.error("error while setting updating media on product: " + e);
		}
		catch (final ModelSavingException e)
		{
			LOG.info("Error occured while saving product model" + e);
		}
	}

	private MediaContainerModel setContainerModel(final List<MediaModel> mediaModelList, final JnJProductModel jnJProductModel,
			final String containerQualifier)
	{
		final MediaContainerModel containerModel = modelService.create(MediaContainerModel.class);
		containerModel.setCatalogVersion(jnJProductModel.getCatalogVersion());
		containerModel.setQualifier(containerQualifier);

		try
		{
			final MediaContainerModel contanerModelExisted = flexibleSearchService.getModelByExample(containerModel);
			if (null != contanerModelExisted)
			{
				modelService.remove(contanerModelExisted);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Search results Not found for media container " + containerQualifier, exception);
		}

		containerModel.setMedias(mediaModelList);

		modelService.save(containerModel);
		LOG.info("Media is created Successfully for product " + jnJProductModel.getName() + "and for media container"
				+ containerModel.getQualifier());

		return containerModel;
	}
	

	private MediaModel createMedia(final CatalogVersionModel catalogversion,
			final JnjGTProductImageResponse gtProductImageResponse, final MediaFormatModel mediaFormatModel)
	{
	    final MediaModel mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
		mediaModel.setCode(gtProductImageResponse.getMediaCode());
		mediaModel.setCatalogVersion(catalogversion);

		try
		{
			final MediaModel mediaModelExisted = flexibleSearchService.getModelByExample(mediaModel);

			if (mediaModelExisted != null)
			{
				modelService.remove(mediaModelExisted);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("No Media found for id =" + gtProductImageResponse.getMediaCode(), exception);
		}

		if (mediaFormatModel != null)
		{
			mediaModel.setMediaFormat(mediaFormatModel);
		}
		if (null != mediaFolderModel)
		{
			mediaModel.setFolder(mediaFolderModel);
		}
		mediaModel.setURL(gtProductImageResponse.getUrl());
		mediaModel.setMime(gtProductImageResponse.getImageFileFormat());
		mediaModel.setRealFileName(gtProductImageResponse.getFileName());

		modelService.save(mediaModel);
		return mediaModel;

	}


	private JnJProductModel getProductModel(final String code, final CatalogVersionModel catalogVersion)
	{
		return jnjGTProductFeedService.getProductByCode(code, catalogVersion);
	}

	private void populateMediaFormat()
	{

		mediaFormat300 = getMediaFormat(mediaFormat300, JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_MAIN);
		mediaFormat96 = getMediaFormat(mediaFormat96, JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_CART_ICON);
		mediaFormat65 = getMediaFormat(mediaFormat65, JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_THUMBNAIL);
		mediaFormat515 = getMediaFormat(mediaFormat515, JnjpcmfacadeConstants.PCMIntegration.MEDIA_FORMAT_ZOOM);
	}

	private MediaFormatModel getMediaFormat(MediaFormatModel mediaFormat, final String qualifier)
	{
		if (mediaFormat == null)
		{
			final MediaFormatModel formatModel = modelService.create(MediaFormatModel.class);
			formatModel.setQualifier(qualifier);
			mediaFormat = flexibleSearchService.getModelByExample(formatModel);
		}
		return mediaFormat;
	}

	public MediaFolderModel getMediaFolder()
	{
		try
		{
			mediaFolderModel = mediaService.getFolder(JnjpcmfacadeConstants.PCMIntegration.MEDIA_FOLDER);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error("Media Folder Not Found:- " + JnjpcmfacadeConstants.PCMIntegration.MEDIA_FOLDER);
			mediaFolderModel = null;
		}
		catch (final AmbiguousIdentifierException e)
		{
			LOG.error("Multiple Media Folder Found:- " + JnjpcmfacadeConstants.PCMIntegration.MEDIA_FOLDER);
			mediaFolderModel = null;
		}

		return mediaFolderModel;
	}

	protected void initializeVariables()
	{

		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new BufferedImageHttpMessageConverter());

		final HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent",
				configurationService.getConfiguration().getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_USER_AGENT));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		final JSONObject product = new JSONObject();
		product.put(JnjpcmfacadeConstants.PCMIntegration.START_DATE, getStartDate());
		final JSONObject products = new JSONObject();
		products.put(JnjpcmfacadeConstants.PCMIntegration.PRODUCT, product);
		final JSONArray productArray = new JSONArray();
		productArray.add(products);
		final JSONObject productList = new JSONObject();
		productList.put(JnjpcmfacadeConstants.PCMIntegration.PRODUCTS, productArray);
		request1 = new HttpEntity<>(productList, headers);
	}

	protected String getStartDate()
	{
		String date;
		if (configurationService.getConfiguration()
				.getBoolean(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_BACKDATE_FLAG))
		{
			date = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_BACKDATE);
		}
		else
		{
			final String dateFrequency = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_FREQUENCY);
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, (-(Integer.parseInt(dateFrequency))));
			final Date startdate = calendar.getTime();
			final DateFormat dateFormat = new SimpleDateFormat(JnjpcmfacadeConstants.PCMIntegration.DATE_FORMAT);
			date = dateFormat.format(startdate);
		}

		return date;
	}
	
	private boolean vaidateImage(final JSONObject obj3) {
		boolean isValidImage = true;
		
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(FILE_NAME));
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(QUALIFIER));
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(CODE)) ;
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(FULL_URL)) ;
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(IMAGE_FILE_FORMAT)) ;
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(IMAGE_LAST_UPD_DATE)) ;
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(IMAGE_SIZE)) ;
		isValidImage = (null != obj3 && isValidImage && null != obj3.get(IMAGE_TYPE)) ;
		
		return isValidImage;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public JnjPCMCommonFacadeUtil getJnjPCMCommonFacadeUtil()
	{
		return jnjPCMCommonFacadeUtil;
	}

	public void setJnjPCMCommonFacadeUtil(final JnjPCMCommonFacadeUtil jnjPCMCommonFacadeUtil)
	{
		this.jnjPCMCommonFacadeUtil = jnjPCMCommonFacadeUtil;
	}

	public JnjGTProductFeedService getJnjGTProductFeedService()
	{
		return jnjGTProductFeedService;
	}

	public void setJnjGTProductFeedService(final JnjGTProductFeedService jnjGTProductFeedService)
	{
		this.jnjGTProductFeedService = jnjGTProductFeedService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

}
