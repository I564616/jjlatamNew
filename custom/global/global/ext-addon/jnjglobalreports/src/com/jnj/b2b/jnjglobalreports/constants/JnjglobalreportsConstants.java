/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.jnjglobalreports.constants;

/**
 * Global class for all Jnjglobalreports constants. You can add global constants for your extension into this class.
 */
public final class JnjglobalreportsConstants extends GeneratedJnjglobalreportsConstants
{
	public static final String EXTENSIONNAME = "jnjglobalreports";
	public static final String SPACE = " ";
	public static final String CONST_COMMA = ",";

	private JnjglobalreportsConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface Reports
	{
		public static final String ACCOUNTS_MAP = "accountsMap";
		public static final String CURRENT_ACCOUNT_ID = "currentAccountId";
		public static final String ACCOUNT_SELECTED_VALUE="accountsSelectedValue";
		public static final String SORT_OPTIONS = "sortOptions";
		public static final String OBTAINED_DATE_FORMAT = "MM/dd/yyyy";
		public static final String ORDERED_FROM_MDD = "orderedFrom";
		public static final String ORDERED_FROM_CONS = "orderedFromCons";
		public static final String PRODUCT_TO_DISPLAY = "productDropDown";
		public static final String ANALYSIS_VARIABLE = "analysisVariable";
	}

	// implement here constants used by this extension
}
