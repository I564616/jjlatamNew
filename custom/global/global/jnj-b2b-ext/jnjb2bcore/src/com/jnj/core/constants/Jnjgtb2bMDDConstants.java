/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.constants;
import com.jnj.core.constants.GeneratedJnjb2bCoreConstants;


/**
 * Global class for all MDD specific jnjb2bcore constants. You can add global constants for your extension into this
 * class.
 * 
 * @author Accenture
 * @version 1.0
 */
public final class Jnjgtb2bMDDConstants extends GeneratedJnjb2bCoreConstants
{
	public static final String EXTENSIONNAME = "jnjb2bcore";
	public static final String GTIN = "EAN";
	public static final String FIELDLIST = "storefront.fields.list.search";

	private Jnjgtb2bMDDConstants()
	{
		super();
		assert false;
	}

	public interface Cart
	{
		public static final String NOT_SALEABLE = "cart.addToCart.productNotSaleable";
		public static final String DIVISION_ERROR = "cart.addToCart.productDivisionMismatch";
		public static final String APPROVERS = "approvers";
		public static final String PRICE_OVERRIDE_CODES = "priceOverCodes";
		public static final String DEFAULT_OVERRIDDEN_REASON = "15";
		public static final String DEFAULT_OVERRIDDEN_APPROVER = "ZD";
		public static final String ORDER_TYPES_PRIORITY = "default.order.types.priority";
		//Changes for AAOL-561 and AAOL-576
		public static final String NOT_APPLICABLE = "cart.product.notApplicable";
		public static final String OBSOLETE = "cart.product.obsolete";
		public static final String DISCONTINUED = "cart.product.discontinued";
		//AAOL-4150
		public static final String OBSOLETE_ERROR = "cart.product.obsolete.errorMesg";
	}
	public interface ProductWorkflows
	{
		public static final String NEW_PRODUCT_WORKFLOW_TO_EMAIL_US = "pcm.new.product.email.user.group.us";
		public static final String NEW_PRODUCT_WORKFLOW_TO_EMAIL_CA = "pcm.new.product.email.user.group.ca";
		public static final String EXTERNAL_USER_GROUP_US = "pcm.users.external.group.us";
		public static final String EXTERNAL_USER_GROUP_CA = "pcm.users.external.group.ca";
		public static final String NEW_PRODUCT_WORKFLOW_FROM_EMAIL = "pcm.new.product.email.from.id";
		public static final String NEW_PRODUCT_EMAIL_UPC = "newProductUpc";
		public static final String NEW_PRODUCT_EMAIL_SUMMARY = "newProductSummary";
		public static final String NEW_PRODUCT_COUNTRY = "country";
		public static final String NEW_PRODUCT = "newProduct";
		public static final String UPDATE_PRODUCT = "updateProduct";

	}
	
}
