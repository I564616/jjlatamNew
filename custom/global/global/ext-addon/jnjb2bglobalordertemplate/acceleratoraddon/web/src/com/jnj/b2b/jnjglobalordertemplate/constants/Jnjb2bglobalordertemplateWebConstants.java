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
package com.jnj.b2b.jnjglobalordertemplate.constants;

/**
 * Global class for all Jnjb2bglobalordertemplate web constants. You can add global constants for your extension into this class.
 */
public final class Jnjb2bglobalordertemplateWebConstants
{
	//Dummy field to avoid pmd error - delete when you add the first real constant!
	public static final String deleteThisDummyField = "DELETE ME";

	private Jnjb2bglobalordertemplateWebConstants()
	{
		//empty to avoid instantiating this constant class
	}
	public interface JnjGTExcelPdfViewLabels
	{
   	interface ShippingDetailList
   	{
   		String HEADER_DATA = "Header Data";
   		String SOLD_TO_ACCOUNT = "Sold To Account";
   		String SHIP_TO_NAME = "Ship to Name";
   		String SHIP_TO_ADDRESS = "Ship To Address";
   		String DELIVERY_NUMBER = "Delivery Number";
   		String DELIVERY_DATE = "Delivery Date";
   		String ORDER_NUMBER = "Order Number";
   		String CUSTOMER_PO = "Customer PO";
   		
   		String NO_CARTONS = "No. Cartons";
   		String TOTAL_WEIGHT = "Total Weight";
   		String SHIPPING_INSTRUCTIONS = "Shipping Instructions";
   		String OPERTAION_DATE = "Operation Date";
   		String PATIENT_ID = "Patient ID";
   		
   		String SURGEON = "Surgeon";
   		String LINE_NO = "Line No.";
   		String PRODUCT_CODE = "Product Code";
   		String PRODUCT_DESCRIPTION = "Product Description";
   		String BATCH_SPLIT_QTY = "Batch Split Qty";
   		String UOM = "UOM";
   		String UOM_CONVERSION = "UOM Conversion";
   		String BATCH_SERIAL = "Batch/Serial";
   		String EXPIRY_DATE = "Expiry Date";
   		String TOTAL_QTY = "Total Qty";
   		public static final String PACKING_LIST_FILENAME = "Packing List Data";
   		public static final String ITEM_DATA = "Item Data";
   	}
   	
	}


	// implement here constants used by this extension
}
