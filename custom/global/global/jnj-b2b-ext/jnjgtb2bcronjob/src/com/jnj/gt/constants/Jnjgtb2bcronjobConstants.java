/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.constants;

/**
 * Global class for all Jnjgtb2bcronjob constants. You can add global constants for your extension into this class.
 */
public final class Jnjgtb2bcronjobConstants extends GeneratedJnjgtb2bcronjobConstants
{
	public static final String EXTENSIONNAME = "jnjgtb2bcronjob";

	private Jnjgtb2bcronjobConstants()
	{
		super();
		assert false;
	}
	
    public interface Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String CREATE_ORDER_CRONJOB = "Create Order Cron Job";
		public static final String EARLY_ZIP_CODE_CRONJOB = "Early Zip Code Cron Job";
	}

	public interface OrderStatusInbound
	{
		public static final int HEADER_ROWS_COUNT = 3;
		public static final String FOOTER_STARTING_CHARACTER = "#";
		public static final String RECORD_COUNT_DELIMETER = ":";

	}
	// implement here constants used by this extension
}
