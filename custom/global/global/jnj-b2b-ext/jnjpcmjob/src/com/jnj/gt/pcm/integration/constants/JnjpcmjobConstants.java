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
 * Global class for all Jnjpcmjob constants. You can add global constants for your extension into this class.
 */
public final class JnjpcmjobConstants extends GeneratedJnjpcmjobConstants
{
	public static final String EXTENSIONNAME = "jnjpcmjob";
	public static final String PLATFORM_LOGO_CODE = "jnjpcmjobPlatformLogo";

	private JnjpcmjobConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public static final class Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String PRODUCT_DETAIL_TO_PCM = "Product Detail to PCM";
		public static final String EARLY_ZIP_CODE_CRONJOB = "Early Zip Code Cron Job";

		private Logging()
		{
		}
	}

	
}
