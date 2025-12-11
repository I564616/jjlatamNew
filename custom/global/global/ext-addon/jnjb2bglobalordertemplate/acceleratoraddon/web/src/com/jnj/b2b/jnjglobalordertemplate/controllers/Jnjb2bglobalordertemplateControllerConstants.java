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
package com.jnj.b2b.jnjglobalordertemplate.controllers;

/**
 */
public interface Jnjb2bglobalordertemplateControllerConstants
{

	// implement here controller constants used by this extension


	String ADDON_PREFIX = "addon:/jnjb2bglobalordertemplate/";

	interface Views {

		interface Pages {

			interface Template {
				String TemplatePage ="pages/template/orderTemplatePage";
				String TemplateDetailPage = "pages/template/orderTemplateDetailPage";
				String TemplateCreate="pages/template/createOrderTemplatePage";
				String TemplateEdit="pages/template/editOrderTemplatePage";
			}
/***added by Shamili*****/
			interface Order {
				String OrderDetailConsignmentChargePage ="pages/order/consignment/orderDetailChargePage"; 
				String OrderHistoryPage ="pages/order/orderHistoryPage";
				String OrderDetailPage ="pages/order/orderDetailPage";
				String SurgeryInfoOrderPopUp = "pages/order/surgeryInfoOrderPopUp";
				String OrderDetailConsignmentReturnPage ="pages/order/consignment/orderDetailPageConsignmentReturn";
				String OrderDetailConsignmentFillupPage ="pages/order/consignment/orderDetailPageConsignmentFillup";

			}
			interface Invoice{
				String InvoiceDetailPage ="pages/invoice/invoiceDetailPage";
					String InvoiceDetailChargePage ="pages/invoice/consignment/invoiceDetailChargePage";
			}
		}
		/*********/
		
		interface Fragments {
			interface Template {
				String DeleteTemplatePopup = "fragments/template/deleteTemplate";
				String OrderHistoryPopups ="fragments/order/orderHistoryPopups";
				/*AAOL-3681*/
				String DisputeOrderAndItemPage = "fragments/order/disputeOrderAndItemPage";
			}
			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
			}
			
			interface Order
			{
				String OrderHistoryPopup = "fragments/order/orderHistoryPopups";
			}

		}
	}
	
	interface JnjGTExcelPdfViewLabels
	{
	interface OrderHistory
	{
		String BILL_TO_ACC_NUMBER = "Bill-To Account Number";
		String BILL_TO_ADDRESS = "Bill-To Address";
		String SOLD_TO_ACC_NUMBER = "Sold-To Account Number";
		String SOLD_TO_ADDRESS = "Sold-To Address";
		String SHIP_TO_ACC_NUMBER = "Ship-To Account Number";
		String SHIP_TO_ACC_NAME = "Ship-To Account Name";
		String CUSTOMER_PO = "Customer PO Number";
		String ORDER_NUMBER = "Order Number";
		String ORDER_DATE = "Order Date";
		String ORDER_STATUS = "Order Status";
		String ORDER_TOTAL = "Order Total";
		String ORDER_CHANNEL = "Order Channel";
		String DELIVERY_DATE = "Delivery Date";
		String ORDER_TYPE = "Order Type";

		public static final String ORDER_HISTORY = "OrderHistorySearchResult";
	}
	interface OrderDetail
	{
		String ORDER_NUMBER = "Order Number";
		String ACCOUNT_NUMBER = "Account Number";
		String SHIP_TO_ADDRESS = "Ship-To Address";
		String ATTENTION = "Attention";
		String BILLING_NAME_ADDRESS = "Sold-To Address";
		String DEALER_PO = "Dealer PO Number";
		String DROP_SHIP_ACC = "Drop-Ship Account";
		String REQ_DELIVERY_DATE = "Requested Delivery Date";
		String EXPECTED_DELIVERY_DATE = "Expected Delivery Date";
		String ACTUAL_SHIP_DATE = "Actual Ship Date";
		String ACTUAL_DELIVERY_DATE = "Actual Delivery Date";
		String SHIPMENT_LOCATION = "Shipment Location";
		String LINE_STATUS = "Line Status";
		String PRODUCT_NAME = "Product Name";
		String PRODUCT_ID = "Product ID";
		String CONS_PRODUCT_CODE = "Product Code and UPC";
		String GTIN = "GTIN";
		String UNIT_OF_MEASURE = "Unit of Measure";
		String QUANTITY = "Quantity";
		String UNIT_PRICE = "Unit Item Price";
		String TOTAL_PRICE = "Total";
		String ORDER_CHANNEL = "order.channel.";
		String SURGEON_NAME = "Surgeon Name";
		String DISTRIBUTOR_PO_NUMBER = "Distributor PO Number";
		String SHIP_TO_ACCOUNT = "Ship-To Account";
		String LOT_NUMBER = "Lot Number";
		String LOT_COMMENT = "Lot Comment";
		String CONTRACT_NUMBER = "Contract Number";
		String SPECIAL_STOCK_PARTNER = "Special Stock Partner";
		String EXTENDED_PRICE = "Extended Price";
		String ESTIMATED_DELIVERY = "Estimated Delivery Date";
		String SHIPPING_METHOD = "Shipping Method";

		String ORDER_INFORMATION = "Order Information";
		String BILLING_INFORMATION = "Billing Information";
		String SHIPPING_INFORMATION = "Shipping Information";
		String JNJ_ORDER_NUMBER = "J&J Order Number:";
		String PO_NUMBER = "Purchase Order:";
		String ORDER_DATE = "Order Date:";
		String ORDER_STATUS = "Status :";
		String ORDER_TYPE = "Order Type :";
		String ORDER_BY = "Ordered By :";
		String NUMBER_OF_LINES = "Number of Lines:";
		String SOLD_TO_ACC_NUMBER = "Sold-To Account Number :";
		String GLN = "GLN :";
		String PAYMENT_METHOD = "Payment method :";
		String CREDIT_CARD_TYPE = "Credit Card Type :";
		String CREDIT_CARD_NUMBER = "Credit Card Number :";
		String CREDIT_CARD_EXPIRY = "Credit Card Expiration Date :";
		String DELIVERY_INFORMATION = "Delivery Information";
		String PACKING_LIST = "Packing List";
		String CARRIER = "Carrier";
		String BILL_OF_LANDING = "Bill of Lading";
		String LINE = "Line";
		String PRODUCT_CODE = "Product Code and GTIN";
		String CONTRACT = "Contract";
		String QTY = "Qty";
		String UOM = "UOM";
		String STATUS = "Status";
		String ITEM_PRICE = "Item Price";
		String TOTAL = "Total";
		String SUB_TOTAL = "Sub-Total:";
		String FEES_FREIGHT = "Fees/Freight:";
		String TAXES = "Taxes:";
		String ORDER_TOTAL = "Order Total:";
		String TRACKING = "Tracking";
		String REASON_CODE = "Reason Code";
		String CORDIS_HOUSE_ACCOUNT = "Cordis House Account:";
		String SPINE_SALES_REP_UCN = "Spine Sales Rep UCN:";
		String SALES_TERRITORY = "Sales Territory:";
		String SSP = "SSP";
		String SHIPPING_DATE = "Ship Date";
		String DELIVERY_DATE = "Delivery Date";
		String SURGERY_INFORMATION = "Surgery Information";
		String TOTAL_WEIGHT = "Total Weight:";
		String TOTAL_VOLUME = "Total Cubic Volume:";

		String SURGEON = "Surgeon:";
		String CASE_DATE = "Case Date:";
		String SURGERY_SPECIALITY = "Surgery Specialty:";
		String LEVELS_INSTRUMENTED = "# Levels Instrumented:";
		String ORTHOBIOLOGICS = "Orthobiologics:";
		String PATHOLOGY = "Pathology:";
		String SURGICAL_APPROACH = "Surgical Approach:";
		String ZONE = "Zone:";
		String INTERBODY = "Interbody:";
		String FILE_CASE = "File/Case #:";

	}
	interface InvoiceDetails
	{
		String SUMMARY_HEADER = "Invoice Details";
		String LINE_HEADER = "Invoice Line Details";
		String INVOICE_NUMBER = "Invoice Number";
		String SHIP_DATE = "Ship Date";
		String BILL_OF_LANDING = "Bill of Lading";
		String CARRIER_NAME = "Carrier Name";
		String PACKING_LIST = "Packing List";
		String SHIPPING_METHOD = "Shipping Method";
		String ITEM_PRICE = "Item Price";
		String EXTENDED_PRICE = "Extended Price";
		String CONTRACT_NUMBER = "Contract Number";
		String ORDERED_QUANTITY = "Ordered Quantity";
		String INVOICED_QUANTITY = "Invoiced Quantity";
		String UPC = "UPC";
		String INVOICED_DISCLAIMER = "invoiced.disclaimer";
		String INVOICED_DISCLAIMER_MSG = "invoiced.disclaimer.message";

	}
}
}
