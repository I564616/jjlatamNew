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
package com.jnj.facades.constants;

/**
 * Global class for all B2BAcceleratorFacades constants. You can add global constants for your extension into this
 * class.
 */
@SuppressWarnings("PMD")
public class Jnjb2bFacadesConstants extends GeneratedJnjb2bFacadesConstants
{
	public static final String EXTENSIONNAME = "jnjb2bfacades";
	public static final String ZERO_IN_FLOAT = "0.0";
	public static final String ZERO_IN_DOUBLE_VALUE_STRING = "0.00";
	public static final String COMMA_IN_STRING = ",";
	public static final String EMPTY_STRING = "";
	public static final double ZERO_IN_DOUBLE = 0.0;
	public static final String PIPE_STRING = "|";	
	public static final String START_DATE_CONFIG_NUM = "rgaCod.startDate.config.number";
	public static final String END_DATE_CONFIG_NUM = "rgaCod.endDate.config.number";
	public static final String RGA_OTC_LINK = "rga.usr.otc.link";
	public static final String COD_OTC_LINK = "cod.usr.otc.link";
	public static final String MDD_SHIPMENT_FREIGHT_CARRIER_CODE = "mdd.shipment.freight.carrier.code";
	public static final String CONSUMER_SHIPMENT_FREIGHT_CARRIER_CODE = "consumer.shipment.freight.carrier.code";
	public static final String Y_STRING = "Y";
	public static final String MDD_TEMP_ORDER_PRICE_FLAG = "mddTempOrderPriceFlag";


	
	private Jnjb2bFacadesConstants()
	{
		super();
		assert false;
	}

	/**
	 * Komal :The Interface AddToCart
	 */
	public interface AddToCart
	{

		public static final String ADD_TO_CART = "addToCart";


		public static final String ADD_CONTRACT_TO_CART = "addToCartToContract";
	}


	public interface Contract
	{
		public static final String ADD_CONTRACT_TO_CART = "getContractDetailsById";
		public static final String CONTRACT_CONVERTER = "contractConverter";
		public static final String GET_CONTRACTS = "getContracts";
	}


	public interface Template
	{
		public static final String SAVE_AS_TEMPLATE_BY_CART = "saveAsTempateByCart";
	}


	// Added for the Cart and Check Out Validation
	public interface Validation
	{
		public static final String UPDATE_DELIVERY_DATE = "updateDeliveryDate";
		public static final String DATE_FORMAT = "MM/dd/yyyy";
		public static final String GET_PRICE = "getPrice";
		public static final String UPDATE_EXPECTED_PRICE = "updateExpectedPrice";

	}

	// Added for all Constants related to Order on Cart Page
	public interface Order
	{
		public static final String REQUEST_TYPE_SIMULATION = "requestTypeSimulation";
		public static final String REQUEST_TYPE_PRICING = "requestTypePricing";
		public static final String REQUEST_TYPE_CREATION = "requestTypeCreation";
		public static final String DISTRIBUTION_CHANNEL = "distributionChannel";
		public static final String DIVISION = "division";
		public static final String ORDER_CHANNEL = "orderChannel";
		public static final String NAMED_DELIVERY_DATE = "namedDeliveryDate";
		public static final String ORDER_REASON = "orderReason";
		public static final String EMPTY_STRING = "";
		public static final String TILT_SEPARATOR = "~";
		public static final String CHANGE_REASON = "changeReason";
		public static final String PRICE_CHECK_DECIMAL_FORMAT = "#.00";
		public static final String TXT_STRING = "txt";
		public static final String EAN_STRING = "ean";
		public static final String PEDIDO = "Pedido";
		public static final String FILE_EXTENSION_SPILTTER = "\\.";
		public static final int ONE_AS_INT = 1;
		public static final int ZERO_AS_INT = 0;
		public static final String XML_STRING = "xml";
		public static final String SUCCESS = "success";
		public static final String ERROR = "error";
		public static final String PROCESS_PURCHASE_ORDER = "ProcessPurchaseOrder";
		public static final String CART_TOTAL = "cartTotal";
		public static final String ENTRY_TOTAL_PRICE = "entryTotalPrice";
		public static final String REQUEST_DATE_FORMAT = "request.date.format";
		public static final String RESPONSE_DATE_FORMAT = "response.date.format";
		public static final int TEN = 10;
		public static final int ONE = 1;
	}

	// Added for logging purpose
	public interface Logging
	{
		public static final String SUBMIT_ORDER_EDI = "Submit Order EDI";
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String CREATE_ORDER = "Create Order";
		public static final String VALIDATE_ORDER = "Validate Order";
		public static final String SUBMIT_ORDER_CRON_JOB = "Submit Order Cron Job";
		public static final String SUBMIT_ORDER = "Submit Order";
		public static final String GET_INVOICE_DOCUMENT = "Get Invoice Document";
		public static final String GET_PRICE = "Get Price";
		public static final String DOT_STRING = ".";
		public static final String CHECK_OUT_FUNCTIONALITY = "Check Out Functionality";
		public static final String PLACE_ORDER = "Place Order";
		public static final String CART_PAGE = "Cart Page";
		public static final String SAVE_CREDIT_CARD_INFO = "Save Credit Card Info";
		public static final String RETURN_ORDER = "Return Order";
	}
	
	public interface Paymetric
	{
		public static final String PAYMETRIC_RESPONSE = "PaymetricResponse";
		public static final String CC_TOKEN = "CCToken";
		public static final String MONTH = "Month";
		public static final String YEAR = "Year";
		public static final String CC_TYPE = "CCType";
		public static final String CC_STRING = "CC";
	}
	

	
	public interface OrderStatusInbound
	{
		public static final String VALIDATION_CSR_EMAIL_KEY = "order.status.file.validation.email.id";
		public static final String RECORD_COUNT_DELIMETER = ":";
	}

}
