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
package com.jnj.b2b.storefront.constants;

/**
 * Constants used in the Web tier.
 */
public final class WebConstants
{
	private WebConstants()
	{
		//empty
	}

	public static final String MODEL_KEY_ADDITIONAL_BREADCRUMB = "additionalBreadcrumb";

	public static final String BREADCRUMBS_KEY = "breadcrumbs";

	public static final String CONTINUE_URL = "session_continue_url";

	public static final String CART_RESTORATION = "cart_restoration";

	public static final String URL_ENCODING_ATTRIBUTES="encodingAttributes";

	public static final String LANGUAGE_ENCODING="languageEncoding";

	public static final String CURRENCY_ENCODING="currencyEncoding";

	public static final String MULTI_DIMENSIONAL_PRODUCT="multiDimensionalProduct";
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	public interface CheckOut
	{
		public static final String ORDER_CONFIRMATION_DOWNLOAD = "downloadData()";
		public static final String REPLENISH_ORDER_CONFIRMATION_CMS_PAGE = "replenishmentConfirmation";
	}
	
	/**
	 * The Interface InvoiceDetails.
	 */
	public interface InvoiceDetails
	{

		/** The Constant CONTENT_TYPE. */
		public static final String CONTENT_TYPE = "text/xml";

		/** The Constant HEADER_PARAM. */
		public static final String HEADER_PARAM = "Content-Disposition";

		/** The Constant HEADER_PARAM_VALUE. */
		public static final String HEADER_PARAM_VALUE = "attachment; filename=";

		/** The Constant FILE_TYPE. */
		public static final String FILE_TYPE = ".XML";

		/** The Constant INOVICE_ORDER_DETAILS_CMS_PAGE. */
		public static final String INOVICE_ORDER_DETAILS_CMS_PAGE = "invoiceOrderDetails";

		/** The Constant PDF_FILE_TYPE. */
		public static final String PDF_FILE_TYPE = ".pdf";
	}
}
