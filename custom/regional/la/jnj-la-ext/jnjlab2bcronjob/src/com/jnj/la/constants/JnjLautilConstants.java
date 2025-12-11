/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.constants;

/**
 *
 */
public class JnjLautilConstants
{
	public interface Logging
	{
		public static final String HYPHEN = " - ";
		public static final String UPSERT_PRODUCT_NAME = "UpsertProducts Interface";
		public static final String USER_MAMANGEMENT_SEARCH_FACADE = "searchCustomers()";
		public static final String USER_MAMANGEMENT_SEARCH_SERVICE = "searchCustomers()";
		public static final String FILE_PURGING_SAP = "File Purging for SAP folders ";
		public static final String BEGIN_OF_METHOD = "Start - ";
		public static final String VALIDATE_CART = "validate cart";

		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String END_OF_METHOD = "End of method";


		public static final String LOAD_INVOICES_JOB = "Load Invoices Interface";
		public static final String LOAD_INVOICE_PERFORM = "perform() method of Cron Job LoadInvoices interface";
		public static final String LOAD_INVOICE_GET_INVOICES = "getLoadInvoices()";
		public static final String LOAD_INVOICE_READ_INVOICES = "readInvoiceXMLFile()";
		public static final String LOAD_INVOICE_CHECK_HEADER = "checkHeaderEndElement()";
		public static final String LOAD_INVOICE_CHECK_LINEITEM = "checkLineItemEndElement()";
		public static final String LOAD_INVOICE_MAP_INVOICE_MODEL = "mapInvoiceToModel()";
		public static final String LOAD_INVOICE_ADD_TO_ORDER = "addDataToInvoiceOrderModel()";
		public static final String LOAD_INVOICE_ADD_TO_ENTRY = "addDataToInvoiceEntryModel()";
		public static final String LOAD_INVOICE_GET_B2BUNIT = "getB2bUnitForId()";
		public static final String LOAD_INVOICE_SAVE_ORDER = "saveInvoiceOrder()";
		public static final String LOAD_INVOICE_SAVE_ENTRY = "saveInvoiceEntry()";
	}
}
