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

import com.jnj.gt.constants.GeneratedJnjgtb2binboundserviceConstants;

import java.util.Arrays;
import java.util.List;


/**
 * Global class for all Jnjgtb2binboundservice constants. You can add global constants for your extension into this
 * class.
 */
public final class Jnjgtb2binboundserviceConstants extends GeneratedJnjgtb2binboundserviceConstants
{
	public static final String EXTENSIONNAME = "jnjgtb2binboundservice";

	private Jnjgtb2binboundserviceConstants()
	{
		super();
		assert false;
	}

	// implement here constants used by this extension
	public static final String FEED_RETRY_COUNT = "feeds.retry.count";
	public static final String PIPE_STRING = "|";
	public static final String UNDER_SCORE_STRING = "_";
	public static final String MDD_SOURCE_SYS_ID = "jnj.mdd.source.sys.id";
	public static final String CONSUMER_USA_SOURCE_SYS_ID = "jnj.consumer.source.sys.id";
	public static final String CONSUMER_CANADA_SOURCE_SYS_ID = "jnj.consumer.ca.source.sys.id";
	public static final String PRICE_LIST_VALUE = "jnjgtb2binboundservice.pricelist.value";
	public static final String COMMA_STRING = ",";
	public static final int FOUR = 4;
	public static final String COUNTRY_ISO_CODE = "jnj.gt.country.code";
	public static final String DATE_FORMAT_FOR_FILE_NAME = "MMddyyyyHHmmssSSS";
	public static final String MDD_SOURCE_SYS_ID_FOR_CUST = "jnj.mdd.source.sys.customer.id";
	public static final String CONS_SOURCE_SYS_ID_FOR_CUST = "jnj.consumer.source.sys.customer.id";
	public static final String PROCEDURE_EXECUTION_START_STRING = "{call ";
	public static final String PROCEDURE_EXECUTION_END_STRING = "}";
	public static final String ADDRESS_FLAG = "1";

	public interface B2BUnit
	{
		public static final String SHIP_TO_INDC = "SH";
		public static final String SOLD_TO_INDC = "SO";
		public static final String BILL_TO_INDC = "BT";
		public static final String PAY_FROM_INDC = "PF";
		public static final String CONS_SHIP_TO_INDC = "jnj.gt.inbound.cons.ship.to.ind";
		public static final String CONS_BILL_TO_INDC = "jnj.gt.inbound.cons.bill.to.ind";
		public static final String CONS_PAY_FROM_INDC = "jnj.gt.inbound.cons.pay.from.ind";
		public static final String MDD_SHIP_TO_INDC = "jnj.gt.inbound.mdd.ship.to.ind";
		public static final String MDD_BILL_TO_INDC = "jnj.gt.inbound.mdd.bill.to.ind";
		public static final String MDD_PAY_FROM_INDC = "jnj.gt.inbound.mdd.pay.from.ind";
		public static final String SOLD_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and (({soldToAddress}='1' and {sourceSysId} in (?mddSourceSysId)))";
		//public static final String SOLD_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and (({shipToAddress}='1' and {sourceSysId} in (?mddSourceSysId)))";
		//public static final String SHIP_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {shipToAddress}='1' and {sourceSysId} in (?sourceSysId)";
		public static final String SHIP_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {shipToAddress}='1' and {sourceSysId} in (?mddSourceSysId)";
		//public static final String SHIP_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {shipToAddress}='1' and {sourceSysId} in (?mddSourceSysId)";
		public static final String BILL_TO_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {billToAddress}='1' and {sourceSysId} in (?mddSourceSysId)";
		public static final String BILL_TO_INDC_QUERY_EMEA = "select {pk} from {JnjGTIntPartnerFunc} where {partFuncCode} in (?billToInd,?primaryBillToInd) and {customerNumber} in ({{select {uid} from {JnjGTIntB2BUnit} where {billToAddress} = 1}}) and {partCustNo} in ({{select {uid} from {JnjGTIntB2BUnit} where {shipToAddress} = 1}}) and {sourceSysId} in (?mddSourceSysId)";
		public static final String PAY_FROM_INDC_QUERY = "select {pk} from {JnjGTIntB2BUnit} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {payFromAddress}='1'";
		public static final String SALES_ORG_QUERY = "select {pk} from {JnjGTIntSalesOrg} where {customerNumber}=?customerNumber";
		public static final String GET_CATEGORIES_QUERY = "select {pk} from {JnjEMEAIntCategory}";
		public static final String GET_CATEGORIES_DESC_QUERY = "select {pk} from {JnjEMEAIntCategoryDesc}";
		public static final String GET_CATEGORIES_DESC_BY_CODE_QUERY = "select {pk} from {JnjEMEAIntCategoryDesc} where {categoryCode}=?categoryCode and {sourceSysId} in (?mddSourceSysId)";
		public static final String PARTNER_FUNC_QUERY = "select {pk} from {JnjGTIntPartnerFunc} where  {customerNumber}=?customerNumber";
		public static final String PARTNER_FUNC_QUERY_BY_PARTNER_CUST_NO = "select {pk} from {JnjGTIntPartnerFunc} where  {partCustNo}=?partCustNo";
		public static final String AFFIL_QUERY_CUST_NUM = "select {pk} from {JnjGTIntAffiliation} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {parentCustNo}=?parentCustNo";
		public static final String AFFIL_QUERY = "select {pk} from {JnjGTIntAffiliation} where {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}})";
		public static final String CLASS_OF_TRADE_ID = "CoT Group";
	}

	public interface EarlyZipCode
	{
		public static final String TXT_FILE_NAME = "text";
		public static final String EXCEL_FILE_NAME = "excel";
		public static final String TXT_FILE_URL = "jnj.gt.early.zip.code.txt.url";
		public static final String EXCEL_FILE_URL = "jnj.gt.early.zip.code.excel.url";
		public static final String DIR_PATH = "jnj.na.early.zip.code.dir";
		public static final String TXT_FILE_EXTENSION = ".txt";
		public static final String EXCEL_FILE_EXTENSION = ".xls";
		public static final String ARCHIVE_FOLDER = "jnj.na.archive.folder";
		public static final String EARLY_ZIP_CODE_FOLDER = "jnj.na.early.zip.code.folder";
		public static final String FEDEX = "jnj.gt.early.fedex";
		public static final String UPS = "jnj.gt.early.ups";
	}

	public interface Product
	{
		public static final String DEA_REG_IND_VALUE = "1";
		public static final String NOT_APPLICABLE_STATUS_CODE = "jnj.na.product.status.na";
		public static final String INACTIVE_STATUS_CODE = "jnj.na.product.status.inactive";
		public static final String ACTIVE_STATUS_CODE = "jnj.na.product.status.active";
		public static final String ACTIVE_STATUS_CODE_ONE = "jnj.na.product.status.active.one";
		public static final String ACTIVE_STATUS_CODE_TWO = "jnj.na.product.status.active.two";
		public static final String CONSUMER_GROUP_ID_TO_CHECK = "jnj.consumer.grp.id";


		public static final String ROOT_CATEGORY_ID = "Categories";

		//TODO public static final String ROOT_CATEGORY_ID = "MDD";

		public static final String DEFAULT_CATEGORY_ID = "DefaultCategory";

		/**
		 * Constant for Catalog Version Name.
		 */
		public static final String STAGED_CATALOG_VERSION = "Staged";

		public static final String ONLINE_CATALOG_VERSION = "Online";

		/**
		 * Constant for MDD Catalog Id.
		 */
		public static final String MDD_CATALOG_ID = "mddProdCatalog";

		/**
		 * Constant for Consumer USA Catalog Id.
		 */
		public static final String CONSUMER_USA_CATALOG_ID = "consNAProdCatalog";

		/**
		 * Constant for Consumer Canada Catalog Id.
		 */
		public static final String CONSUMER_CANADA_CATALOG_ID = "caProdCatalog";//modified for canada
		/**
		 * Constant for MDD key.
		 */
		public static final String CONSUMER_SRC_SYS = "CONSUMER";

		/**
		 * Constant for MDD key.
		 */
		public static final String MDD_SRC_SYS = "MDD";

		/**
		 * Constant for Unit Code for EACHES UoM.
		 */
		public static final String EACH = "EA";

		public static final String BASE_MOD_CODE = "00";
		public static final String JNJ_UNIT_TYPE = "jnjGTUnit";

		public static final String NA_REGION_CODE = "USA";
		public static final String LANG_CODE = "EN";
		public static final String MATERIAL_INDICATOR = "jnj.na.product.exclusion.material.ind";

		public static final String MDD_LEVEL_1_CATEG_PREFIX = "F-";
		public static final String MDD_LEVEL_2_CATEG_PREFIX = "D-";
		public static final String CONSUMER_LEVEL_1_CATEG_PREFIX = "B-";
		public static final String CONSUMER_LEVEL_2_CATEG_PREFIX = "SB-";

		public static final String MDD_ACTIVE_PROD_STATUS_CODES_KEY = "jnj.gt.product.local.feed.active.status";
		public static final String MDD_DISCONTINUED_PROD_STATUS_CODES_KEY = "jnj.gt.product.local.feed.discontinued.status";

		public static final String MDD_FILTER_USA_OTC_READY_RECORDS_FLAG_KEY = "jnj.gt.product.feed.filter.usaOtcInd.flag";

		public static final String DATE_FORMAT_KEY = "jnj.gt.inbound.products.date.format";

		public static final String SOURCE_SYS_NAME_MAPPING_US_KEY = "jnj.na.inbound.product.srcsystem.usa";

		public static final String SOURCE_SYS_NAME_MAPPING_CA_KEY = "jnj.na.inbound.product.srcsystem.ca";

		public static final String OCD_PRODUCT_DIVISIONS = "jnj.gt.product.ocd.division.codes";

		public static final String JNJ_SALES_ORG_CODE = "jnj.sales.org.code";

	}

	// Added for logging purpose
	public interface Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String CUSTOMER_MASTER_FEED = "Customer Master Feed";
		public static final String CATEGORY_MASTER_FEED = "Category Master Feed";
		public static final String SURGEON_FEED = "Surgeon Feed";
		public static final String SALES_ALIGNMENT_FEED = "Sales Alignment Feed";
		public static final String ORDER_SYNC_FEED = "Order Sync Feed";
		public static final String TERRITORY_FEED = "Territory Feed";
		public static final String ORDER_TEMPLATE_FEED = "Order Template Feed";
		public static final String CREDIT_CARD_FEED = "Credit Card Feed";
		public static final String USER_PROFILE_FEED = "User Profile Feed";
		public static final String CUSTOMER_LOCAL_FEED = "Customer Local Feed";
		public static final String AFFILIATION_FEED = "Affiliation Feed";
		public static final String PRODUCT_MASTER = "Product Master Feed";
		public static final String CATEGORY_MASTER = "Category Master Feed";
		public static final String PRODUCT_LOCAL = "Product Local Feed";
		public static final String PRODUCT_LIST_PRICE = "Product List Price Feed";
		public static final String LOT_MASTER = "Product Lot Master Feed";
		public static final String PRODUCT_EXCLUSION_FEED = "Product Exclusion Feed";
		public static final String CPSIA = "Product CPSIA Feed";
		public static final String INVOICE = "Inovice Feed";
		public static final String EARLY_ZIP_CODE_FEED = "Early Zip Code Feed";
		public static final String SHIPMENT_TRACKING_FEED = "Shipment Tracking Feed";
	}

	public interface UserProfile
	{
		public static final String SHIP_TO_INDC = "SH";
		public static final String PHONE = "1";
		public static final String MOBILE = "2";
		public static final String FAX = "3";
		public static final String MDD = "MDD";
		public static final String CONSUMER = "CONSUMER";
		public static final String PRIVATE_STATUS_CODE = "1";
		public static final String ACTIVE_STATUS_CODE = "0";
		public static final String DISABLE_STATUS_CODE = "1";
		public static final String ACCESS_BY_TERRITORY = "O";
		public static final String ACCESS_BY_WWID = "W";
		public static final Object NO_CHARGE_PERMISSION = "39";


	}

	public interface SalesAlignment
	{
		public static final String FLAG_NO = "N";
		public static final String FLAG_YES = "Y";
	}

	public interface Territory
	{
		public static final String TERRITORY_HASH_CODE = "jnj.na.inbound.territory.hashcode";
		public static final String CONSUMER_DIVISON = "jnj.na.inbound.territory.consumerdivison";
	}

	public interface Order
	{

		public static final String EXECUTE_ORDER_PROCEDURE_NAME_TRUNCATE_TUNED = "execute.order.procedure.name.truncate.tuned";
		public static final String PRC_COND_TYPE_MANUAL_FEE = "prcCondTypeManualFee";
		public static final String PRC_COND_TYPE_FREIGHT_FEE = "prcCondTypeFreightFee";
		public static final String PRC_COND_TYPE_MIN_FEE = "prcCondTypeMinimumFee";
		public static final String PRC_COND_TYPE_EXPEDIT_FEE = "prcCondTypeExpeditFee";
		public static final String PRC_COND_TYPE_MDD_TAX = "prcCondTypeMddTax";
		public static final String SAP_ONLY_USER = "dummy@sapuser.com";
		public static final String CONFIRMED_SCHEDULE_LINE_STATUS = "jnj.na.schedule.line.confirmed.status";
		public static final String SCHEDULE_LINE_STATUS_UC = "UC";
		public static final String SCHEDULE_LINE_STATUS_CANCELLED = "RJ";
		public static final String PRODUCT_DIVISION = "OCD";
		public static final String DATE_FORMAT_KEY = "jnj.na.inbound.order.date.format";
		public static final String DUMMY_PRODUCT_CODE = "dummyProduct";
		public static final String DEFAULT_CURRENCY_KEY = "jnj.gt.order.default.currency";
		public static final String EXECUTE_ORDER_PROCEDURE_NAME = "execute.order.procedure.name";
		public static final String EXECUTE_ORDER_PROCEDURE_NAME_TUNED = "execute.order.procedure.name.tuned";
		public static final String EXECUTE_ORDER_PROCEDURE_CRONJOB_NAME = "execute.order.procedure.cronjob.name";


		public interface StatusInbound
		{
			public static final String ORDER_HEADER_CSV_FILE_COUNTER_KEY = "orderHeaderCsvCounter";
			public static final String ORDER_lINE_CSV_FILE_COUNTER_KEY = "orderLineCsvCounter";
			public static final String ORDER_SCH_LINE_CSV_FILE_COUNTER_KEY = "orderSchLineCsvCounter";
			public static final String ORDER_HEADER_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.order.file.prefix";
			public static final String ORDER_LINE_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.order.line.file.prefix";
			public static final String ORDER_SCH_LINE_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.order.sch.line.file.prefix";
		}

	}


	public interface Invoice
	{
		public static final String HSA_CONDITION_TYPE = "prcHsaCondtype";
		public static final String DROP_SHIP_CONDITION_TYPE = "prcDropshipCondType";
		public static final String MIN_ORDER_QTY_CONDITION_TYPE = "prcMinOrderQtyCondType";
		public static final List<String> FREIGHT_HANDLING_CONDITION_TYPES = Arrays.asList("ZFR2", "ZFR1", "ZFRM", "ZFRT", "ZHCG");
		public static final List<String> TAXES_CONDITION_TYPES = Arrays.asList("XR1", "XR2", "XR3", "XR4", "XR5", "XR6");
	}

	public interface ShipmentInbound
	{
		public static final String SHIP_TRCK_LINE_QUERY = "select {pk} from {JnjGTIntShipTrckLine}";
		public static final String SHIP_TRACK_DATE_FORMAT_KEY = "jnj.na.inbound.shipment.date.format";
		public static final String SHIPMENT_HEADER_CSV_FILE_COUNTER_KEY = "shipmentHeaderCsvCounter";
		public static final String SHIPMENT_lINE_CSV_FILE_COUNTER_KEY = "shipmentLineCsvCounter";
		public static final String SHIPMENT_HEADER_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.shipment.header.prefix";
		public static final String SHIPMENT_LINE_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.shipment.line.prefix";
	}
}
