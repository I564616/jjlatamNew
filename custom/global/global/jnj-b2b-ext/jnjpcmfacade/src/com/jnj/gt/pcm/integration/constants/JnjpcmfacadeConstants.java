/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.jnj.gt.pcm.integration.constants;

/**
 * Global class for all Jnjpcmfacade constants. You can add global constants for your extension into this class.
 */
public final class JnjpcmfacadeConstants extends GeneratedJnjpcmfacadeConstants
{
	public static final String EXTENSIONNAME = "jnjpcmfacade";
	

	private JnjpcmfacadeConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension



	public static final class PCMIntegration
	{
		//pcm job
		public static final String PAGINATION_DATA = "paginationData";
		public static final String TOTAL_NUMBER_OF_RESULTS = "totalNumberOfResults";
		public static final String NUMBER_OF_PAGES = "numberOfPages";
		public static final String START_DATE = "startDate";
		public static final String ISO_CODE = "isoCode";
		public static final String PAGE_ID = "pageId";
		public static final String PRODUCTS = "products";
		public static final String ACCESS_TOKEN = "access_token";
		public static final String TRUE = "TRUE";
		public static final String DATE_FORMAT = "MM-dd-yyyy";
		public static final String COUNTRY = "country";
		public static final String REGION = "region";
		public static final String EMPTY_STRING = "";

		public static final String PCM_INTEGRATION_JOB_USERNAME = "pcm.integration.job.username";
		public static final String PCM_INTEGRATION_JOB_PASSWORD = "pcm.integration.job.password";
		public static final String PCM_INTEGRATION_JOB_CLIENT_ID = "pcm.integration.job.clientId";
		public static final String PCM_INTEGRATION_JOB_CLIENT_SECRET = "pcm.integration.job.clientSecret";
		public static final String PCM_INTEGRATION_JOB_ACCESS_TOKEN_URL = "pcm.integration.job.accessTokenUrl";
		public static final String PCM_INTEGRATION_USER_AGENT = "pcm.integration.user.agent";

		public static final String PCM_INTEGRATION_COUNTRY_URL = "pcm.integration.country.url";
		public static final String PCM_INTEGRATION_REGION_URL = "pcm.integration.region.url";
		public static final String PCM_INTEGRATION_CATEGORY_COUNTRY_URL = "pcm.integration.category.country.url";

		public static final String PCM_INTEGRATION_JOB_BACKDATE_FLAG = "pcm.integration.job.backdate.flag";
		public static final String PCM_INTEGRATION_JOB_BACKDATE = "pcm.integration.job.backdate";
		public static final String PCM_INTEGRATION_JOB_FREQUENCY = "pcm.integration.job.frequency";

		//image job
		public static final String MEDIA_FORMAT_MAIN = "300Wx300H";
		public static final String MEDIA_FORMAT_THUMBNAIL = "65Wx65H";
		public static final String MEDIA_FORMAT_CART_ICON = "96Wx96H";
		public static final String MEDIA_FORMAT_ZOOM = "515Wx515H";
		public static final String MEDIA_FORMAT_TIFF = "30Wx30H";
		public static final String MEDIA_FORMAT_ORIGINAL = "original";
		public static final String MEDIA_FOLDER = "images";
		public static final String TIFF = ".tiff";
		public static final String MAIN = "MAIN";
		public static final String ORIGINAL = "original";
		public static final String IMAGES = "images";
		public static final String PRODUCT = "product";
		public static final String CATEGORIES = "categories";

		public static final String PCM_IMAGE_INTEGRATION_COUNTRY_URL = "pcm.image.integration.country.url";
		public static final String PCM_IMAGE_INTEGRATION_REGION_URL = "pcm.image.integration.region.url";

		private PCMIntegration()
		{
		}
	}
}
