/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.jnj.core.constants;

import com.jnj.core.constants.GeneratedJnjb2bCoreConstants;

/**
 * Global class for all jnjb2bcore constants. You can add global constants for your extension into this class.
 */
public final class Jnjgtb2bCONSConstants extends GeneratedJnjb2bCoreConstants
{
	public static final String EXTENSIONNAME = "jnjb2bcore";
	public static final String UPC = "UPC";
	public static final String ALLB2BUNIT = "ALLB2BUNIT";
	public static final String COMING_SOON = "category.result.coming.soon";
	public static final String NEW = "category.result.new";
	public static final String PRODUCT_CODE_STATUS_CON1 = "1";
	public static final String PRODUCT_CODE_STATUS_CON2 = "2";
	public static final String PRODUCT_CODE_STATUS_DIS_CON = "9";
	public static final String DISCOUNTINUE = "category.result.dis.con";

	private Jnjgtb2bCONSConstants()
	{
		super();
		assert false;
	}

	public interface OrderFeed
	{
		public static final String ITEM_CATEGORY = "order.line.item.to.process";
		
	}
	public interface InvoiceFeed
	{
		public static final String ITEM_CATEGORY = "invoice.line.item.to.process";
	}
	
	public static final String SYMBOl_COMMA = ",";
}
