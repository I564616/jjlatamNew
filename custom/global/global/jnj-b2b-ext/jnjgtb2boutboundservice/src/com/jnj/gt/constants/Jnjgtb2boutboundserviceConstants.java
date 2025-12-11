/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.constants;

/**
 * Global class for all Jnjgtb2boutboundservice constants. You can add global constants for your extension into this
 * class.
 */
public final class Jnjgtb2boutboundserviceConstants extends GeneratedJnjgtb2boutboundserviceConstants
{
	public static final String EXTENSIONNAME = "jnjgtb2boutboundservice";

	public static final String EMPTY_STRING = "";
	public static final String COMMA_STRING = ",";
	public static final String Y_STRING = "Y";
	public static final String N_STRING = "N";
	public static final String DISCOUNT_VALUE = "Misc. Discount";
	public static final String TAX_VALUE = "Misc. Discount";
	public static final String STOP_SAP_OUTBOUND_CALLING = "jnj.webservice.stop.sap.calling";
	public static final String RESPONSE_DATE_FORMAT = "response.date.format";
	public static final String REQUEST_DATE_FORMAT = "request.date.format";
	public static final String CONSUMER_REQUEST_DATE_FORMAT = "consumer.request.date.format";
	public static final String DATE_FORMAT_FOR_FILE_NAME = "MMddyyyyHHmmssSSS";
	public static final String SUCCESS = "success";
	public static final String ITEM_CATEGORY_FOR_MDD = "jnj.gt.order.mdd.ignored.item.categories";
	public static final String LINE_NUMBER_EXCLUDED = "jnj.gt.order.mdd.line.number.excluded";
	public static final String UNLOADING_POINT_ID = "OrderUnloadingPoint";
	public static final String UNLOADING_POINT_KEY = "unloadingPoint";
	public static final String ORDER_REASON_FOR_INTERNATIONAL = "orderReasonForInternational";
	public static final String ORDER_REASON_FOR_STANDARD = "orderReasonForStandard";
	public static final String ORDER_REASON_FOR_DELIVERED = "orderReasonForDelivered";
	public static final String ORDER_REASON_FOR_REPLENISHMENT = "orderReasonForReplenishment";
	public static final String ORDER_REASON = "OrderReason";
	public static final String CREDIT_CARD_YEAR_INITIAL = "credit.card.expiration.year.initial";
	public static final String IS_HOLD_CODE_SET = "jnj.gt.outbound.hold.code";
	public static final String WM_CONTROL_AREA = "WmControlArea";
	public static final String INTERFACE_NUMBER = "interFaceNumber";
	public static final String BUSINESS_SECTOR = "businessSector";
	public static final String SOURCE_SYS_ID = "sourceSysId";
	public static final String ONE_TIME_SHIPPING_VALUES = "oneTimeShippingValue";
	public static final String ZCODE = "zcode";
	public static final String REGION = "region";
	public static final String BUSINESS_OBJECT_NAME = "businessObjectName";
	public static final String SALES_ORG = "salesOrg";
	public static final String CONSUMER_DATE_FORMATER = "consumerDateFormater";
	public static final String SET_AVAILABLE_QUANTITY = "jnj.gt.set.available.quantity";
	public static final String EXCEPTED_DATE_FORMAT = "jnj.gt.excepted.date.format";
	//Used to define excluded date format... Date in this format will not be processed in Portal
	public static final String CONS_EXCEPTED_DATE_FORMAT = "jnj.gt.cons.excepted.date.format";
	public static final String CART_CONS_INVALID_DELIVERY_DATE = "cart.consumer.invalid.delivery.date";
	public static final String DELIVERY_BLOCK = "deliveryBlock";
	public static final String CONSUMER_ERROR = "consumerError";
	public static final String EMPTY_SPACE = " ";
	public static final String RISK_CATEGORY_CODES = "jnj.gt.order.mdd.riskCategoryCodes";

	private Jnjgtb2boutboundserviceConstants()
	{
		super();
		assert false;
	}

	public interface Order
	{
		public static final String ORDER_CHANNEL = "orderChannel";
		public static final String HOLD_CODE = "holdCode";
		public static final String HOLD_CODE_FOR_GATE_WAY_ORDER = "holdCodeForGateWayOrder";
		public static final String ORDER_SOURCE = "orderSource";
		public static final String ORDER_SOURCE_FOR_REST = "orderSourceForRest";
		public static final String HOLD_CODE_WITH_LOT_NO = "holdCodeWithLotNo";
	}

	public interface SimulateOrder
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.simulate.order.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.simulate.order.mock.xml.classpath";
	}

	public interface CreateOrder
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.create.order.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.create.order.mock.xml.classpath";
	}

	public interface OrderChange
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.order.change.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.order.change.mock.xml.classpath";
	}

	public interface OrderReturn
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.order.return.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.order.return.mock.xml.classpath";
	}

	public interface SimulateDeliveredOrder
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.simulate.delivered.order.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.simulate.delivered.order.mock.xml.classpath";
	}

	public interface CreateDeliveredOrder
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.create.delivered.order.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.create.delivered.order.mock.xml.classpath";
	}

	public interface GetPriceQuote
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.price.quote.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.price.quote.mock.xml.classpath";
	}

	public interface CreateConsOrd
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.create.cons.ord.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.create.cons.ord.mock.xml.classpath";
		public static final String ORDER_CHANNEL_FOR_CONS = "orderChannelForCons";
		public static final String REQ_TYPE_FOR_SIM_CONS = "reqTypeForSimCons";
	}

	public interface ProofOfDelivery
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.proof.delivery.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.proof.delivery.mock.xml.classpath";
		public static final String LANGUAGE_CODE = "jnj.gt.proof.delivery.language.code";
		public static final String SERVICE_ID = "jnj.gt.proof.delivery.service.id";
		public static final String MAJOR = "jnj.gt.proof.delivery.major";
		public static final String INTERMEDIATE = "jnj.gt.proof.delivery.intermediate";
		public static final String MINOR = "jnj.gt.proof.delivery.minor";
		public static final String ACCOUNT_NUMBER = "jnj.gt.proof.delivery.account.number";
		public static final String METER_NUMBER = "jnj.gt.proof.delivery.meter.number";
		public static final String KEY = "jnj.gt.proof.delivery.key";
		public static final String PASSWORD = "jnj.gt.proof.delivery.password";
		public static final String FILE_NAME = "jnj.gt.proof.delivery.file.name";
		public static final String FILE_EXTENSION = ".pdf";
		public static final String FILE_LOCATION = "jnj.gt.proof.delivery.location";
	}

	public interface CmodRga
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.cmod.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.cmod.mock.xml.classpath";
		public static final String PAGE_INDEX = "jnj.webservice.cmod.page.index";
		public static final String PAGE_SIZE = "jnj.webservice.cmod.page.size";
		public static final String ORDER_BY = "jnj.webservice.cmod.order.by";
		public static final String REPORTS_CMOD = "jnj.webservice.cmod.reports";
		public static final String TNAME_CMOD = "jnj.webservice.cmod.tname";
		public static final String REPORTS_RGA = "jnj.webservice.rga.reports";
		public static final String TNAME_RGA = "jnj.webservice.rga.tname";
		public static final String DATE_FROMAT = "jnj.webservice.date.format";
		public static final String DATE_TO_VALUE = "jnj.webservice.date.to.value";
		public static final String DATE_FROM_VALUE = "jnj.webservice.date.from.value";
		public static final String KEY_TO_REPLACE_LEADING_ZEROS = "jnj.webservice.cmod.leading.zero.key";
	}

	public interface WebServiceConnection
	{
		public static final String WEBSERVICE_ORDER_USER = "jnj.webservice.order.user";
		public static final String WEBSERVICE_ORDER_PWD = "jnj.webservice.order.password";
		public static final String WEBSERVICE_CONNECTION_TIME_OUT = "jnj.webservice.connectionTimeOut";
		//public static final String WEBSERVICE_EXTENDED_CONNECTION_TIME_OUT = "jnj.webservice.connection.extendedTimeOut";
		public static final String WEBSERVICE_READ_TIME_OUT = "jnj.webservice.readTimeOut";
		public static final String WEBSERVICE_ORDER_CONSUMER_USER = "jnj.webservice.order.consumer.user";
		public static final String WEBSERVICE_ORDER_CONSUMER_PWD = "jnj.webservice.order.consumer.password";
		public static final String WEBSERVICE_EXTENDED_READ_TIME_OUT = "jnj.webservice.extended.readTimeOut";

		public static final String SIMULATE_WS_STANDARD_CONNECTION_TIME_OUT = "jnj.simulate.ws.standard.conn.timeout";
		public static final String CREATE_WS_STANDARD_CONNECTION_TIME_OUT = "jnj.create.ws.standard.conn.timeout";
		public static final String SIMULATE_WS_EXTENDED_CONNECTION_TIME_OUT = "jnj.simulate.ws.extended.conn.timeout";
		public static final String CREATE_WS_EXTENDED_CONNECTION_TIME_OUT = "jnj.create.ws.extended.conn.timeout";

		public static final String ORDER_WS_STANDARD_READ_TIME_OUT = "jnj.simulate.ws.standard.read.timeout";
		public static final String ORDER_WS_EXTENDED_READ_TIME_OUT = "jnj.simulate.ws.extended.read.timeout";
		public static final String WEBSERVICE_ORDER_CRONJOB_CONNECTION_TIME_OUT = "jnj.webservice.order.cronjob.connectionTimeOut";
		public static final String WEBSERVICE_ORDER_CRONJOB_READ_TIME_OUT = "jnj.webservice.order.cronjob.readTimeOut";
				
	}

	public interface ConsignmentStock
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.consignment.stock.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.consignment.stock.mock.xml.classpath";
	}

	// Added for logging purpose
	public interface Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String SIMULATE_ORDER = "Simulate Order";
		public static final String CREATE_ORDER = "Create Order";
		public static final String ORDER_CHANGE = "Order Change";
		public static final String CREATE_DELIVERY_ORDER = "Create Delivery Order";
		public static final String TERRITORY_FEED = "Territory Feed";
		public static final String ORDER_TEMPLATE_FEED = "Order Template Feed";
		public static final String SIMULATE_DELIVERED_ORDER = "Simulate Delivered Order";
		public static final String CREATE_DELIVERED_ORDER = "Create Delivered Order";
		public static final String ORDER_RETURN = "Order Return";
		public static final String GET_PRICE_QUOTE = "Get Price Quote";
		public static final String GET_CONTRACT_PRICE = "Get Contract Price";
		public static final String PROOF_OF_DELIVERY = "Proof Of Delivery";
		public static final String CREATE_CONS_ORD = "Create Cons Ord";
		public static final String SIMULATE_CONS_ORD = "Simulate Cons Ord";
		public static final String CONSIGNMENT_STOCK = "Consignment Stock";
		public static final String CMOD_RGA_CALL = "Cmod Rga Call";
	}
	
	//Added for Serialization COnnection Param
	public interface Serialization
	{
		public static final String WEBSERVICE_SERIAL_USER = "jnj.webservice.serial.user";
		public static final String WEBSERVICE_SERIAL_PWD = "jnj.webservice.serial.password";
		public static final String WEBSERVICE_SERIAL_URL = "jnj.webservice.verify.serial.endpoint.url";
		public static final String VERIFY_SERIAL_RESPONSE = "Verify Serial Response";
	}
			
		
	
	public interface SapErrorResponse
	{
		public static final String NOQTY="NOQTY,1001,No stock quantity,ErrLog_1";
		public static final String BATCH_NOT_FOUND="BATCH_NOT_FOUND,1002,Invalid Batch Number,ErrLog_1";
		public static final String SERIAL_NOT_FOUND="SERIAL_NOT_FOUND,1003,Invalid Serial Number,ErrLog_1";
		public static final String PRICING_INCOMPLETE="PRICING_INCOMPLETE,1004,Pricing is incomplete,ErrLog_1";
		public static final String CBC_QTY_EXCEED="CBC_QTY_EXCEED,1005,Consignment Business case quantity exceeded for the vendor,ErrLog_1";
		public static final String SAP_ERROR_MOCK_THRESHOLD = "sap.error.mock.threshold";
	}
}
