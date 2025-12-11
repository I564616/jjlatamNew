/*
] * [y] hybris Platform
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
package com.jnj.constants;

/**
 * Global class for all Jnjutil constants. You can add global constants for your extension into this class.
 */
public final class JnjutilConstants extends GeneratedJnjutilConstants
{
	public static final String EXTENSIONNAME = "jnjutil";
	public static final String PARAMS = "{param}";


	private JnjutilConstants()
	{
		//empty to avoid instantiating this constant class
	}


	public interface Logging
	{
		public static final String HYPHEN = " - ";
		public static final String USER_MAMANGEMENT_SEARCH_FACADE = "searchCustomers()";
		public static final String USER_MAMANGEMENT_SEARCH_SERVICE = "searchCustomers()";
		public static final String FILE_PURGING_SAP = "File Purging for SAP folders ";
		public static final String BEGIN_OF_METHOD = "Start - ";
		public static final String VALIDATE_CART = "validate cart";

	}
	// implement here constants used by this extension
}
