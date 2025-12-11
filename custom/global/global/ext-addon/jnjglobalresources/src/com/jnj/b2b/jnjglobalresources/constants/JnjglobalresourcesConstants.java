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
package com.jnj.b2b.jnjglobalresources.constants;

/**
 * Global class for all Jnjglobalresources constants. You can add global constants for your extension into this class.
 */
public final class JnjglobalresourcesConstants extends GeneratedJnjglobalresourcesConstants
{
	public static final String EXTENSIONNAME = "jnjglobalresources";

	private JnjglobalresourcesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public interface Resources
	{
		public static final String USEFUL_LINK_COMPONENT_ID = "Jnjusefullink";
		public static final String TRAINING_RESOURCES_LINK_COMPONENT_ID = "Jnjtrainingresources";
		public static final String POLICY_FEES_LINK_COMPONENT_ID = "jnjpoliciesandfees";
		public static final String CPSIA_LINK_COMPONENT_ID = "jnjcpsia";
		public static final String TERMS_SALES_LINK_COMPONENT_ID = "jnjtermsofsale";
		public static final String USER_MANAGEMENT_LINK_COMPONENT_ID = "Jnjusermanagement";
	}
	public interface Register
	{
		public static final String VIEW_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewOnlyGroup";
		public static final String VIEW_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewOnlySalesGroup";
		public static final String PLACE_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderGroup";
		public static final String PLACE_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderSalesGroup";
		public static final String USER_GROUP_NO_CHARGE_USERS = "user.groups.noChargeGroup";
	}
}
