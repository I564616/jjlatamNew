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
package com.jnj.la.core.constants;

import com.jnj.core.constants.Jnjb2bCoreConstants;

import de.hybris.platform.util.Config;

import java.io.File;
/**
 * Global class for all Jnjlab2bcore constants. You can add global constants for your extension into this class.
 */
public final class Jnjlab2bcoreConstants extends GeneratedJnjlab2bcoreConstants
{

	public static final String LANGUAGE_ISOCODE_PT = "PT";
	public static final String LANGUAGE_ISOCODE_ES = "ES";
	public static final String LANGUAGE_ISOCODE_EN = "EN";
	public static final String DOCUMENT_EXPIRE_DAYS="documentDeleteBackend";
	public static final int    DOCUMENT_EXPIRE_MIN_DAYS=30;
	public static final String DOCUMENT_EXPIRE_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	public static final String BATCH_COUNTRIES="batchEnableCountries";
	public static final String FREE_GOODS_MAP = "freeGoodsMap";
	public static final String DEFAULT_SITE_NAME = "MDD";
	public static final String CLUSTER_ID = "cluster.id";
	public static final int ZERO = 0;
	public static final String LA_DATE_FORMAT = "dd/MM/yyyy";
	public static final String KEY_INDIRECT_PAYER_VALID_COUNTRIES = "jnj.indirect.payer.enabled.countries.list";
	public static final String KEY_PRICE_GROUP_VALID_COUNTRIES = "jnj.price.group.upsert.countries.list";
	public static final String KEY_TAXES_LOCAL_COUNTRIES = "jnj.uses.taxes.local.countries";
	public static final String KEY_TAXES_COUNTRIES = "jnj.uses.taxes.countries";
	public static final String KEY_SHIPPING_ADDRESS_REGION = "jnj.shipping.address.region";
	public static final String CONSUMER_CARIBBEAN_CUSTOMER_GROUP = "consumer.customerGroup.caribbean";
	public static final String CONSUMER_PUERTO_CUSTOMER_GROUP = "consumer.customerGroup.puerto";
	public static final String CONSUMER_CARIBBEAN_URL = "consumer.customerGroup.caribbean.url";
	public static final String CONSUMER_PUERTO_URL = "consumer.customerGroup.puerto.url";
	public static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
	public static final String RETRY_ATTEMPT = "rsa.db.validation.query.timeout.retry.attempts";
	public static final String CONNECTION_WAIT_PERIOD_TIME = "rsa.db.validation.retryInterval";
	public static final String JOB_MONITORING_EMAIL_ATTACHMENT_PATH = "job.monitoring.attachment.path";
	public static final String CURRENT_BASE_SITE = "current.base.site";
	public static final String PRODUCT_COUNT_LIMIT = "product.count.limit";

	public static final String EXTENSIONNAME = "jnjlab2bcore";
	public static final String MASTER_SALES_ORG_SUFFIX = "Master";
	public static final String KEY_VALID_STORES = "jnj.valid.stores.isoCode";
	public static final String EMPENHO_FILES_FULL_PATH = "empenhoFilesFullPath";
	public static final String AR_SITE_NAME = "AR";
	public static final String CL_SITE_NAME = "CL";
	public static final String UY_SITE_NAME = "UY";
	public static final String CENCA_SITE_NAME = "CENCA";
	public static final String EC_SITE_NAME = "EC";
	public static final String PERU_SITE_NAME = "PERU";
	public static final String UY_SHIP_TO = "UY";

	public static final String KEY_AR_VALID_COUNTRIES = "jnj.valid.ar.countries.isoCode";
	public static final String KEY_CENCA_VALID_COUNTRIES = "jnj.valid.cenca.countries.isoCode";
	public static final String KEY_CENCA_AVAILABLE_SALES_ORG = "jnj.cenca.available.sales.org";
	public static final String KEY_PE_VALID_COUNTRIES = "jnj.valid.pe.countries.isoCode";
	public static final String KEY_PR_VALID_COUNTRIES = "jnj.valid.pr.countries.isoCode";
	public static final String KEY_UY_VALID_COUNTRIES = "jnj.valid.uy.countries.isoCode";
	public static final String KEY_AVOID_DUPLICATED_EMAILS = "jnj.avoid.duplicated.emails.to.countries";
	public static final String COLDCHAIN_RULE_ENABLE = "coldChainRule.enabled.";
	public static final String BASE_STORE = "BaseStore";
	public static final String BR_BASE_STORE = "brBaseStore";
	public static final String BR_PHARMA_SALESORG="BR01";
	public static final String BR_MDD_SALESORG="BR02";
	public static final float ZERO_IN_FLOAT = 0.0f;
	public static final String KEY_LATAM_EFFECTIVE_GROUPS = "jnj.latam.effective.groups";
	public static final String KEY_PE_ZORD_VALID_COUNTRIES = "jnj.valid.zord.pe.countries.isoCode";

	public static final String ZTAS_ITEM_CATEGORY = "ZTAS";
	public static final String ZFNN_ITEM_CATEGORY = "ZFNN";
	public static final String ZFRY_ITEM_CATEGORY = "ZFRY";
	public static final String JNJ_CONSOLIDATED_EMAIL_ORDER_TYPES = "jnj.la.consolidatedEmail.inclusionOrderTypeList";
	public static final String JNJ_CONSOLIDATED_EMAIL_WEEK_DAYS = "jnj.la.consolidated.email.week.day";
	public static final String JNJLADUMMYUNIT = "JnJLaDummyUnit";
	
	
	public static final String INVOICE_HISTORY_PAGE_INVOICE_NUMBER = "invoiceHistoryPage.invoiceNumber";
	public static final String INVOICE_HISTORY_PAGE_INVOICE_ORDER_NUMBER = "invoiceHistoryPage.invoiceOrderNumber";
	public static final String INVOICE_HISTORY_PAGE_INVOICE_DATE = "invoiceHistoryPage.invoiceDate";
	public static final String INVOICE_HISTORY_PAGE_INVOICE_TOTAL = "invoiceHistoryPage.invoiceTotal";
	public static final String INVOICE_HISTORY_PAGE_HEADING = "invoiceHistoryPage.heading";
	public static final String INVOICE_HISTORY_PAGE_ESTIMATED_DELIVERY_DATE = "invoiceHistoryPage.estimated.delivery.date";
	public static final String INVOICE_HISTORY_PAGE_ACTUAL_DELIVERY_DATE = "invoiceHistoryPage.actual.delivery.date";

	public static final String CLS_PAGE_HEADING = "clsPage.heading";
	public static final String CLS_UPLOAD_DELIVERY_DATES_HEADING = "clsPage.uploadDeliveryDates.heading";

    public static final String INVOICE_EXPORT_TO_EXCEL = "Invoice Export to Excel";
	public static final String INVOICE_HISTORY_ORDER_NOT_LOADED = "invoice.history.orderNotLoaded";
    public static final String INVOICE_HISTORY_ORDER_NOT_LOADED_MESSAGE = "invoice.history.orderNotLoadedMessage";

    public static final String ORDERS_FROM_DAYS_TO_SEND_CONSOLIDATED_EMAIL = "jnj.orders.from.days.to.send.consolidated.email";
    public static final String ORDERS_CREATION_DATE_TO_SEND_CONSOLIDATED_EMAIL = "jnj.orders.creation.date.to.send.consolidated.email";
    public static final String UPDATE_CARRIER_STATUS_BATCH_SIZE = "jnj.update.carrier.status.batch.size";

	public static final Integer PDF_EXCEL_MAX_PAGE_SIZE = 1000;
	
	public static final String CURRENT_B2B_UNIT = "currentB2bUnit";
	public static final String IN_CORRECT_SHIPTO = "incorrectShipToSelected";
	public static final String CENCA_SALES_ORG_LIST = "cenca.salesorg.list";

	public static final String DAYS_OF_HISTORY_PRODUCTS_BOUGHT_TOGETHER = "days.of.history.products.bought.together";
	public static final Integer DAYS_OF_HISTORY_PRODUCTS_BOUGHT_TOGETHER_DEFAULT=90;

	public static long BUFFER_SIZE = 15728640;
	public static final String KEY_VALID_COUNTRIES = "jnj.valid.countries.isoCode";

	public static final String KEY_VALID_CURRENCY = ".currency.isoCode";
	public static final String RESTRICTED_CATEGORIES = "jnjUserRestrictedCategories";
	public static final String CATALOG_VERSION = "catalogVersion";
	public static final String CATALOG_ID = "catalogId";
	public static final String CONST_COMMA = ",";
	public static final String CONST_DOT = ".";
	public static final String CONST_CODE_TYPE = "E";
	public static final String SYMBOL_TILDE = "~";
	public static final long DEFAULT_ADD_TO_CART_QTY = 1L;
	public static final String PRODUCT_CATALOG = "ProductCatalog";
	public static final String CONTENT_CATALOG = "ContentCatalog";
	public static final String CATALOG_VERSION_ONLINE = "Online";
	public static final String FEED_FILEPATH_ROOT = "feed.filepath.root";
	public static final String FEED_FILE_NAME_PREFIX = ".prefix";
	public static final String FEED_FILEPATH_ROOT_TEMP = "feed.filepath.root.temp";
	public static final String FEED_FILEPATH_INCOMING = "feed.filepath.root.incoming";
	public static final String FEED_FILEPATH_OUTGOING = "feed.filepath.root.outgoing";
	public static final String FEED_RETRY_COUNT = "feeds.retry.count";
	public static final String ORDER_RETRY_COUNT = "orders.retry.count";
	public static final String FEED_FILE_PURGE_DAYS = "feed.purge.days";
	public static final String FEED_FILEPATH_INCOMING_SAP_PURGE_PREFIX = "feed.filepath.root.incoming.sap.purge.prefix";
	public static final String FEED_FILEPATH_INCOMING_SAP_PURGE_FOLDERS = "feed.filepath.root.incoming.sap.purge.folders.name";
	public static final String FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_PREFIX = "feed.filepath.root.outgoing.hybris.purge.prefix";
	public static final String FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_FOLDERS = "feed.filepath.root.outgoing.hybris.purge.folders.name";
	public static final String FILEPATH_HYBRIS_LOG_PURGE_FOLDERS = "hybris.log.purge.folders.name";
	public static final String FILEPATH_JNJ_LOG_PURGE_FOLDER = "jnj.log.purge.folder.name";
	public static final String FEED_FILEPATH_FOLDER_NAME_PREFIX = "feed.filepath.root.incoming.sap.purge.folders.name.prefix";
	public static final String FILEPATH_JNJ_LOG_PURGE_FILE_PATTERN = "jnj.log.purge.file.pattern";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSE_BRACKET = ")";
	public static final String CATEGORIES = "Categories";
	public static final String OUTSALESORDERCREDITSTATUS_B = "out.sales.order.credit.status.b";
	public static final String OUTSALESORDERCREDITSTATUS_C = "out.sales.order.credit.status.c";
	public static final String OUTSALESORDERREJECTIONSTATUS_B = "out.sales.order.rejection.status.b";
	public static final String OUTSALESORDERREJECTIONSTATUS_C = "out.sales.order.rejection.status.c";
	public static final String ORDER_VALIDATION_LINES_COUNT = "order.validation.lines.count";
	public static final String ORDER_VALIDATION_MAX_LINES = "order.validation.max.lines";
	public static final String CART_PAGE_ENTRIES_SIZE = "cartpage.entries.page.size";
	public static final String HOT_FOLDER_ROOT_PATH = "jnjb2bcore.batch.impex.basefolder";
	public static final String INVOICE_OPEN_TEXT_URL = "invoice.open.text.url";
	public static final String INVOICE_OPEN_TEXT_HOST = "invoice.open.text.host";
	public static final String NUMBER_ONE = "1";
	public static final String NUMBER_ZERO = "0";
	public static final String UNDERSCORE_SYMBOL = "_";
	public static final String HASH_SYMBOL = "#";
	public static final String FIFTEEN_MINUTES_MILLIS = "900000";
	public static final String SAP_ORDER_TYPE_ZORD = "ZORD";
	public static final String SAP_ORDER_TYPE_ZOR = "ZOR";
	public static final String SAP_ORDER_TYPE_ZOCR = "ZOCR";
	public static final String SAP_ORDER_TYPE_ZODR = "ZODR";
	public static final String GOVERNMENT = "government";
	public static final String HOSPITAL = "hospital";
	public static final String FEEDS_ARCHIVE_FOLDER_NAME = "/archive";
	public static final String FEEDS_ERROR_FOLDER_NAME = "/error";
	public static final String REQ_PROD_SECOTRS_KEY = "required.product.sectors.mapping";
	public static final String VALID_CHEKOUT_ROLES_KEY = "valid.chekout.user.roles";
	public static final String VALID_EDI_SUBMIT_ORDER_ROLES_KEY = "valid.edi.submit.order.user.roles";
	public static final String EMPENHO_DOCS_PREFIX_KEY = "empenho.doc.prefix";
	public static final String MAX_QTY_LIMIT_EXCEED = "maxQtyLimitExceed";
	public static final String ADDTO_CART_ERROR = "Error";
	public static final String ADDTO_CART_CATEGORY_ERROR = "CategoryError";
	public static final String ALL_HEADER_STATUS = "all.header.status";
	public static final String ALL_LINE_STATUS = "all.line.status";

	public static final String PHARMA_CATEGORY_CODE = "category.pharma.code";
	public static final String MDD_CATEGORY_CODE = "category.mdd.code";
	public static final String SUPER_PARENT_CATEGORY = "Categories";
	public static final String PHR_SECTOR = "PHR";
	public static final String MDD_SECTOR = "MDD";
	public static final String MDD_PHR_SECTOR = "MDD_PHR";
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	public static final String SUBMIT_ORDER = "SubmitOrder";
	public static final String SIMULATE_ORDER = "SimulateOrder";
	public static final String GET_PRICE = "GetPrice";
	public static final String QUERY_NFE = "QueryNFe";
	public static final String GET_INVOICE = "GetInvoiceDocument";
	public static final String MILI_SECOND_FORMATTER = "YYYYMMddHHmmssSSS";
	public static final String SECOND_FORMATTER = "YYYYMMddHHmmss";
	public static final String Y_STRING = "Y";
	public static final String N_STRING = "N";
	public static final String SPACE = " ";
	public static final String IDOC_NUMBER = "DOCNUM";
	public static final String IDOC_PARENT_TAG = "IDOC";
	public static final String OTHER_GROUP_USER_ROLES = "usergroup.roles.otherordergroup";
	//User Management
	public static final String BRAZIL_MASTER_B2BUNIT = "brazil.master.b2bunitid";
	public static final String MEXICO_MASTER_B2BUNIT = "mexico.master.b2bunitid";
	public static final String CENCA_MASTER_B2BUNIT = "cenca.master.b2bunitid";
	public static final String ECUADOR_MASTER_B2BUNIT = "ecuador.master.b2bunitid";
	public static final String PARENT_B2BUNIT_GROUPS = "jnj.parent.b2bUnit.groups";
	public static final String INT_RECORD_STATUS = "recordStatus";
	public static final String INT_RECORD_RETRY = "recordRetry";
	public static final String XSD_VALIDATION_ERROR_MESSAGE = "File is not valid against the XSD";
	public static final Integer MAX_WRITE_ATTEMPTS = Integer.valueOf(Config.getInt(Jnjb2bCoreConstants.FEED_RETRY_COUNT, 3));
	public static final String INTERFACE_NOTIFICATION_EMAIL_ID_KEY = "interface.notification.email";
	public static final String INTERFACE_NOTIFICATION_DEFAULT_LOCALE_ISO_KEY = "interface.notification.email.defaultlocale.iso";
	public static final String FEEDS_XSD_FOLDER_KEY = "feed.filepath.root.xsd";
	public static final String FEEDS_XSD_FILE_EXTENSION = "xsd";
	public static final String FEEDS_FORCE_VALIDATION_FLAG = "feedForceValidationFlag";
	public static final String XSD_VALIDAION_REQUIRED = "feed.xsd.validtion.required";
	public static final String CATALOG_VERSION_STAGED = "Staged";
	public static final int PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS = 90;
	public static final String COUNTRY_ISO_ARGENTINA = "AR";
	public static final String COUNTRY_ISO_BOLIVIA = "BO";
	public static final String COUNTRY_ISO_BRAZIL = "BR";
	public static final String COUNTRY_ISO_CHILE = "CL";
	public static final String COUNTRY_ISO_COLOMBIA = "CO";
	public static final String COUNTRY_ISO_COSTA_RICA = "CR";
	public static final String COUNTRY_ISO_EQUADOR = "EC";
	public static final String COUNTRY_ISO_MEXICO = "MX";
	public static final String COUNTRY_ISO_PANAMA = "PA";
	public static final String COUNTRY_ISO_PERU = "PE";
	public static final String COUNTRY_ISO_URUGUAY = "UY";
	// Introduced CENCA constant
	public static final String COUNTRY_ISO_CENCA = "CENCA";
	public static final String COUNTRY_ISO_ECUADOR = "EC";
	public static final String COUNTRY_ISO_DOMINICAN = "DO";
	public static final String COUNTRY_ISO_PUERTORICO = "PR";
	public static final String COUNTRY_ISO_PARAGUAY = "PY";
	public static final String PAYER_TYPE_ACCOUNT = "Account Payment";
	public static final String MASTER_CONTENT_CATALOG_ID = "mstrContentCatalog";
	public static final String EMPTY_STRING = "";
	public static final String SITE_LOGO_IMAGE_ID = "siteLogoImage";
	public static final String MISSING_PRODUCT_THUMBNAIL = "missingProductImage";
	public static final String PDF_CSS_MEDIA_ID = "pdfCss";
	public static final String FILE_SIZE_LIMIT = "feeds.fileSize";

	public static final String SOLR_INDEX_INDIRECT_CUSTOMER_INPUT_NAME = "solr.index.indirect.customer.input.name";
	public static final String FALSE = "false";

	public static final String SHA_ENCODING = "sha256";

	public static final String PANARELLO = "PANARELLO";
	public static final String BREAK_LINE = "<br/>";
	public static final String URL_SECURED_PROTOCOL_PREFIX = "https://";
	public static final String DOC_TYPE_PREFIX = "order.doctype.";
	public static final String PO_TYPE_PREFIX = "order.potype.";
	public static final String MONTH_YEAR_DATE_FORMAT = "MMM yyyy";
	public static final String DEFAULT_PAGE_SIZE = "pagination.number.results.count";
	public static final String LANGUAGE_ENGLISH_ISO = "en";

	public static final String ORDER_RETRY_COUNT_SUBMIT_ORDER = "orders.retry.count.submit.order";

	public static final String SYMBOL_LEFT_PARENTHESIS = "(";
	public static final String SYMBOL_RIGHT_PARENTHESIS = ")";
	public static final String FILE_EXTENSION_XLSX = "xlsx";
	public static final String FILE_EXTENSION_XLS = "xls";
	public static final String FILE_EXTENSION_PDF = "pdf";
	public static final String FILE_EXTENSION_DOCX = "docx";
	public static final String FILE_EXTENSION_DOC = "doc";

	public static final String ENABLE_USER = "enableUser";
	public static final String CREATE_USER = "createUser";
	public static final String CREATE_USER_PASSWORD = "createUserPassword";

	public static final String PO_NUMBER_STRING = "JJ";
	public static final String SEQUENCE_NUMBER = "poNumberSequence";

	public static final String DAYS_OF_WEEK = "daysOfWeek.localized.values";
	public static final String SYMBOL_HYPHEN = "-";
	public static final String INDIRECT_CUSOMTER_LIMIT = "jnj.indirectCustomer.limit";

	public static final String EXCEPTION_ORDER_TYPE = "jnj.exception.ordertype";
	public static final String SOLDTO_EXCEPTIONS = "jnj.soldto.exceptions";
	public static final String ORDERTYPE_EXCEPTIONS = "orderType.exceptions";
	public static final String PANAMA_SHIPTO_SPECIFIC_REPLACE_ORDERTYPE = "panama.shipTo.specific.replace.orderType";
	public static final String PANAMA_SHIPTO_SPECIFIC_ORDERTYPE = "panama.shipTo.specific.orderType";
	public static final String SALES_ORGANIZATION_PR = "PR01";
	public static final String SALES_ORGANIZATION_DO = "DO01";
	public static final String SALES_ORGANIZATION_PA = "PA02";
	public static final String NO_PRODUCT_FOUND = "No Product found";

	public static final String INVOICE_MEDIA_FOLDER_NAME = "invoice.media.folder.name";
	public static final String DEFAULT_INVOICE_MEDIA_FOLDER_NAME = "invoiceFile";
	public static final String UPLOAD_INVOICE_ERROR = "Error";
	public static final String SITE_NAME_ARGENTINA = "Argentina";
	public static final String SITE_NAME_CHILE = "Chile";
	public static final String SITE_NAME_BRAZIL = "Brazil";
	public static final String SITE_NAME_MEXICO = "Mexico";
	public static final String SITE_NAME_URUGUAY = "Uruguay";
	public static final String ELIGIBLE_INDUSTRY_CODES_FOR_COMMERCIAL_USER="eligible.industry.codes.commercial.user";
	public static final String CONTRACT_PRODUCT_SALES_ORG="contract.product.salesorg";

	public static final String MASTER_PRODUCT_CATALOG_ID = "mstrProductCatalog";

	public static final String NEXT_LINE = "\n";

	public static final String SECTOR = "sector";

	public static final String FROM_DATE="fromDate";

	public static final String CATEGORY_CODE="categoryCode";
	public static final String EXPORT_FILE_URL="mddExportFileUrl";
	public static final String PDF_FILE_URL="mddPdfExportFileUrl";
	public static final String IS_MDD_SITE="isMddSite";
	public static final String PAGE_TYPE="pageType";
	public static final String SEARCH_SORT_FORM="searchSortForm";
	public static final String SHOW_MORE_COUNTER="showMoreCounter";
	public static final String IS_INT_AFF="isinternationalAff";
	public static final String META_ROBOTS="metaRobots";
	public static final String META_ROBOTS_VALUE="no-index,follow";
	public static final String GROUPS="groups";
	public static final String CURRENT_ACCOUNT="currentAccount";
	public static final String RESULT_EXCEEDED="resultLimitExceeded";
	public static final String SITE_LOGO_PATH="siteLogoPath";
	public static final String SITE_LOGO_PROPERTY="siteLogo";
	public static final String SYS_MASTER="sys_master";
	public static final String EPIC_EMAIL_LOGO="epicEmailLogoImageOne";
	public static final String LOGO_URL="jnjConnectLogoURL";
	public static final String LOGO_URL2="jnjConnectLogoURL2";
	public static final String SHOW_CHANGE_ACC_LINK="showChangeAccountLink";
	public static final String USER_EMAIL="userEmail";
	public static final String SUB_CATEGORIES_DATAS="subCategoriesDatas";
	public static final String ACTIVE_USER_REPORT = "activeUserReport";
	public static final String ORDER_SOURCE_INFO = "JJCC";
	public static final String GROUP_PHARMA_COMMERCIAL_USER = "pharmaCommercialUserGroup";
	public static final String GROUP_MDD_COMMERCIAL_USER = "mddCommercialUserGroup";
	public static final String ORDER_SOURCE_INFO_FOR_B2ALL = "b2all.order.source.info";
	public static final String DISTRIBUTOR_INDUSTRY_CODE = "distributor.industry.code";
	public static final String VTEX_ORDER_CHANNEL="vtexOrderChannel";
	private Jnjlab2bcoreConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface UploadEdiFiles
	{
		public static final String USERNAME = "userName";
		public static final String FILTER_BY_CUST = "Customer";
		public static final String FILTER_BY_USER = "User";
		public static final String FILTER_FLAG = "filterFlag";

	}

	public static final class CDL
	{
		public static final String CDL_SRC_FILE_PATH = "cdl.src.file.path";
		public static final String CDL_DEST_FILE_PATH = "cdl.dest.file.path";
		public static final String CDL_FILE_HOSTNAME = "cdl.mbox.hostname";
		public static final String CDL_FILE_USERNAME = "cdl.mbox.username";
		public static final String CDL_FILE_PASS = "cdl.mbox.password";
		public static final int FTP_PORT_NUMBER = 22;

		private CDL(){

		}
	}

	
	public interface SubmitEdiOrderFiles
	{
		public static final String SAO_LUIZ_EXTENSION = "#OrderUpload";

	}

	public static final String LAUDO = "Laudo";

	public interface Encryption
	{
		public static final String ENCRYPTION_FODLER_NAME = "encryption.keys.folderName";
		public static final String ENCRYPTION_ARMORED = "encryption.armored";
		public static final String ENCRYPTION_BITS_VALUE = "encryption.bitsValue";
		public static final String ENCRYPTION_PASSWORD = "encryption.password";
		public static final String ENCRYPTION_USER_ID = "encryption.userId";
		public static final String ENCRYPTION_PUBLIC_KEY_NAME = "encryption.publicKey.name";
		public static final String ENCRYPTION_PRIVATE_KEY_NAME = "encryption.privateKey.name";
		public static final String KEY_FILE_EXTENSION_ASC = ".asc";

	}

	public interface UpsertProduct
	{
		public static final String UPSERT_PRODUCT_E1MARAM = "E1MARAM";
		public static final String UPSERT_PRODUCT_CODE = "MATNR";
		public static final String UPSERT_PRODUCT_TYPE_CODE = "MTART";
		public static final String UPSERT_PRODUCT_COMMERCE_FLAG = "ZZCOMMERCEFLAG";
		public static final String UPSERT_PRODUCT_BASE_UNIT = "MEINS";
		public static final String UPSERT_PRODUCT_EAN = "EAN11";
		public static final String UPSERT_PRODUCT_ORIGIN_COUNTRY = "HERKL";
		public static final String UPSERT_PRODUCT_SECTOR = "ZZSECTOR";
		public static final String UPSERT_PRODUCT_CATALOG_ID = "ZCATNUMBER";
		public static final String UPSERT_PRODUCT_ACTION_CODE = "LAEDA";
		public static final String UPSERT_PRODUCT_CATEGORY = "PRDHA";
		public static final String UPSERT_PRODUCT_E1MAKTM = "E1MAKTM";
		public static final String UPSERT_PRODUCT_E1MARMM = "E1MARMM";
		public static final String UPSERT_PRODUCT_E1MVKEM = "E1MVKEM";
		public static final String UPSERT_PRODUCT_NAME = "MAKTX";
		public static final String UPSERT_PRODUCT_LANGUAGE = "SPRAS_ISO";
		public static final String UPSERT_PRODUCT_DESCRIPTION = "MAKTG";
		public static final String UPSERT_PRODUCT_ALTERNATE_UOM = "MEINH";
		public static final String UPSERT_PRODUCT_NUMERATOR_UOM = "UMREZ";
		public static final String UPSERT_PRODUCT_DENOMINATOR_UOM = "UMREN";
		public static final String UPSERT_PRODUCT_HEIGHT = "HOEHE";
		public static final String UPSERT_PRODUCT_WIDTH = "BREIT";
		public static final String UPSERT_PRODUCT_LENGTH = "LAENG";
		public static final String UPSERT_PRODUCT_UNIT_DIMENSTION = "MEABM";
		public static final String UPSERT_PRODUCT_SHIPPING_WEIGHT = "BRGEW";
		public static final String UPSERT_PRODUCT_SHIPPING_UNIT = "GEWEI";
		public static final String UPSERT_PRODUCT_STATUS = "VMSTA";
		public static final String UPSERT_PRODUCT_UNIT_OF_MEASURE = "VRKME";
		public static final String UPSERT_PRODUCT_DILIVERY_UNIT_OF_MEASURE = "SCHME";
		public static final String UPSERT_PRODUCT_COLD_CHAIN_PRODUCT = "MTPOS";
		public static final String UPSERT_PRODUCT_SALES_ORG = "VKORG";
		public static final String UPSERT_PRODUCT_FILE_CONTAINS = "product";
		public static final String UPSERT_PRODUCT_MAPPER_BEAN = "JnjProductDataLoadMapper";
		public static final String UPSERT_PRODUCT_MODEL_BEAN = "modelService";
		public static final String UPSERT_PRODUCT_PRODUCT_BEAN = "productService";
		public static final String UPSERT_PRODUCT_COLD_CHAIN_VALUE = "ZORC";
		public static final String UPSERT_PRODUCT_COUNTRY_BR_VALUE = "BR";
		public static final String UPSERT_PRODUCT_COUNTRY_MX_VALUE = "MX";
		public static final String UPSERT_PRODUCT_MATERIAL_VALUE = "jnj.product.MTART.values";
		public static final String UPSERT_PRODUCT_ZZSECTOR_VALUE = "jnj.product.ZZSECTOR.values";
		public static final String UPSERT_PRODUCT_FILE_PATH = "upsertProduct.folder.name";
		public static final String UPSERT_PRODUCT_XSD_PATH = "C:\\Users\\sandeep.y.kumar\\Desktop\\J & J\\UPSERT_PRODUCT\\xsd\\MATMAS_GLOBAL.xsd";
		public static final String UPSERT_PRODUCT_MASTER_CATALOG_ID = "mstrProductCatalog";
		public static final String UPSERT_PRODUCT_BASE_UNIT_TYPE = "PA";
		public static final String UPSERT_PRODUCT_STAGED = "Staged";
		public static final String UPSERT_PRODUCT_DEFAULT_CATEGORY = "DefaultCategory";
		public static final String MEXICO_COUNTRY_CODE = "MX";
		public static final String BRAZIL_COUNTRY_CODE = "BR";
		public static final String PANAMA_COUNTRY_CODE = "PA";
		public static final String ECUADOR_COUNTRY_CODE = "EC";
		public static final String CATALOG_VER_STAGED = "Staged";
		public static final String BRAZIL_PRODUCT_CATALOG = "brProductCatalog";
		public static final String MEXICO_PRODUCT_CATALOG = "mxProductCatalog";
		public static final String CENCA_PRODUCT_CATALOG = "cencaProductCatalog";
		public static final String ECUADOR_PRODUCT_CATALOG = "ecProductCatalog";
		public static final String ARGENTINA_PRODUCT_CATALOG = "arProductCatalog";
		public static final String URUGUAY_PRODUCT_CATALOG = "uyProductCatalog";
		public static final String CHILE_PRODUCT_CATALOG = "clProductCatalog";
		public static final String SYNC_CRON_JOB_BR = "Sync-MasterStaged-BrazilStaged";
		public static final String SYNC_CRON_JOB_MX = "Sync-MasterStaged-MexicoStaged";
		public static final String SYNC_CRON_JOB_CENCA = "Sync-MasterStaged-CencaStaged";
		public static final String SYNC_CRON_JOB_EC = "Sync-MasterStaged-EcuadorStaged";
		public static final String SYNC_BR_PROD_STG_TO_ONLINE = "Sync-BrazilStaged->BrazilOnline";
		public static final String SYNC_MX_PROD_STG_TO_ONLINE = "Sync-MexicoStaged->MexicoOnline";
		public static final String SYNC_CENCA_PROD_STG_TO_ONLINE = "Sync-CencaStaged->CencaOnline";
		public static final String SYNC_EC_PROD_STG_TO_ONLINE = "Sync-EcuadorStaged->EcuadorOnline";
		public static final String SYNC_AR_PROD_STG_TO_ONLINE = "Sync-ArgentinaStaged->ArgentinaOnline";
		public static final String SYNC_UY_PROD_STG_TO_ONLINE = "Sync-UruguayStaged->UruguayOnline";
		public static final String SYNC_CL_PROD_STG_TO_ONLINE = "Sync-ChileStaged->ChileOnline";
		public static final String SYNC_CO_PROD_STG_TO_ONLINE = "Sync-ColombiaStaged->ColombiaOnline";
		public static final String SYNC_PE_PROD_STG_TO_ONLINE = "Sync-PeruStaged->PeruOnline";
		public static final String SYNC_PR_PROD_STG_TO_ONLINE = "Sync-PuertoStaged->PuertoOnline";
		public static final String PR_SYNC_CATALOG_PREFIX = "Puerto";
		public static final String CATALOG_MASTER = "mstrProductCatalog";
		public static final String UPSERT_PRODUCT_STATUS_VALUES = "jnj.product.VMSTA.values";
		public static final String UPSERT_PRODUCT_STATUS_D0 = "D0";
		public static final String UPSERT_PRODUCT_STATUS_D1 = "D1";
		public static final String UPSERT_PRODUCT_STATUS_D2 = "D2";
		public static final String UPSERT_PRODUCT_STATUS_D3 = "D3";
		public static final String UPSERT_PRODUCT_STATUS_D4 = "D4";
		public static final String UPSERT_PRODUCT_STATUS_D5 = "D5";
		public static final String UPSERT_PRODUCT_STATUS_QA = "QA";
		public static final String UPSERT_PRODUCT_STATUS_CT = "CT";
		public static final String UPSERT_PRODUCT_ONE = "1";
		public static final String UPSERT_PRODUCT_MARM_UNIT = "MEINH";
		public static final String UPSERT_PRODUCT_MARM_NUMERATOR = "UMREZ";
		public static final String UPSERT_PRODUCT_OFFLINE_DATE = "11/16/1983";
		public static final String UPSERT_PRODUCT_OFFLINE_DATE_FORMATTER = "MM/dd/yyyy";
		public static final String UPSERT_PRODUCT_INT_RECORD_STATUS = "recordStatus";
		public static final String UPSERT_PRODUCT_INT_RECORD_RETRY = "recordRetry";
		public static final String COMMERCIAL_PRIMARY_CODE = "X";

		public static final String PRODUCT_TEST_DATA = "jnj.upsertproduct.testdata";
		public static final String IS_PRODUCT_TEST_SETUP_REQUIRED = "jnj.upsertproduct.test.setup.required";
		public static final String PRODUCT_LAST_UPDATED_END_DATE = "jnj.upsertproduct.last.updated.end.date";

		public static final String SYNC_CRON_JOB_PREFIX = "Sync-MasterStaged-";

	}


	public interface UpsertCustomer
	{
		public static final String UPSERT_CUSTOMER_E1KNA1M = "E1KNA1M";
		public static final String UPSERT_CUSTOMER_ACCOUNT_TYPE = "KTOKD";
		public static final String UPSERT_CUSTOMER_CENTRAL_ORDER_BLOCK = "AUFSD";
		public static final String UPSERT_CUSTOMER_CNPJ = "STCD1";
		public static final String UPSERT_CUSTOMER_CITY = "ORT01";
		public static final String UPSERT_CUSTOMER_POSTAL_CODE = "PSTLZ";
		public static final String UPSERT_CUSTOMER_COUNTRY = "LAND1";
		public static final String UPSERT_CUSTOMER_REGION = "REGIO";
		public static final String UPSERT_CUSTOMER_ID = "KUNNR";
		public static final String UPSERT_CUSTOMER_ADDRESS_ID = "KUNNR";
		public static final String UPSERT_CUSTOMER_CODE = "KUNNR";
		public static final String UPSERT_CUSTOMER_INDICATOR = "KATR1";
		public static final String UPSERT_CUSTOMER_DISTRICT = "ORT02";
		public static final String UPSERT_CUSTOMER_INDUSTRY_CODE_1 = "BRAN1";
		public static final String UPSERT_CUSTOMER_INDUSTRY_CODE_2 = "BRAN2";
		public static final String UPSERT_CUSTOMER_NAME1 = "NAME1";
		public static final String UPSERT_CUSTOMER_NAME2 = "NAME2";
		public static final String UPSERT_CUSTOMER_STREET = "STRAS";
		public static final String UPSERT_CUSTOMER_TELEPHONE = "TELF1";
		public static final String UPSERT_CUSTOMER_E1KNVVM = "E1KNVVM";
		public static final String UPSERT_CUSTOMER_DISTRIBUTION_CHANNEL = "VTWEG";
		public static final String UPSERT_CUSTOMER_UNIT_DIVISION = "SPART";
		public static final String UPSERT_CUSTOMER_PRODUCT_ATTRIBUTE1 = "PRAT1";
		public static final String UPSERT_CUSTOMER_PRODUCT_ATTRIBUTE2 = "PRAT2";
		public static final String UPSERT_CUSTOMER_PRODUCT_ATTRIBUTE3 = "PRAT3";
		public static final String UPSERT_CUSTOMER_PARTNER_FUNCTION = "E1KNVPM";
		public static final String UPSERT_CUSTOMER_SALES_ORG = "VKORG";
		public static final String UPSERT_CUSTOMER_PARTNER_TYPE = "PARVW";
		public static final String UPSERT_CUSTOMER_PARTNER_ID = "KUNN2";
		public static final String UPSERT_CUSTOMER_E1KNVPM = "E1KNVPM";
		public static final String UPSERT_CUSTOMER_FILE_PATH = "upsertCustomer.folder.name";
		public static final String UPSERT_CUSTOMER_XSD_PATH = "C:\\Users\\sandeep.y.kumar\\Desktop\\J & J\\Upsert Customer\\xsd\\CUSTOMER_GLOBAL_V2.xsd";
		public static final String UPSERT_CUSTOMER_FILE_CONTAINS = "customer";
		public static final String UPSERT_CUSTOMER_HOSPITAL_TYPE = "Hospital";
		public static final String UPSERT_CUSTOMER_DISTRIBUTER_TYPE = "Distributor";
		public static final String UPSERT_CUSTOMER_THIRD_PARTY_TYPE = "3rd Party Sales Rep";
		public static final String UPSERT_CUSTOMER_SALES_REP_TYPE = "Sales Rep";
		public static final String UPSERT_CUSTOMER_MAPPER_BEAN = "jnjCustomerDataService";
		public static final String UPSERT_CUSTOMER_B2B_UNIT_NAME = "JnJMexicoB2BUnit";
		public static final String UPSERT_CUSTOMER_COUNTRY_BR_VALUE = "BR";
		public static final String UPSERT_CUSTOMER_COUNTRY_MX_VALUE = "MX";
		public static final String UNPROCESSED_SAPDATA_ROW = "1";
		public static final String BRAZIL_COUNTRY_ISO = "BR";
		public static final String MASTER_B2BUNIT_INITIAL = "JnJMaster";
		public static final String ATTRIBUTE_MDD_X = "X";
		public static final String SECTOR_MDD = "MDD";
		public static final String SECTOR_PHR = "PHR";
		public static final String GROUP = "Group";
		public static final String BRAZIL_COUNTRY_CURRENCY_ISO = "BRL";
		public static final String MEXICO_COUNTRY_CURRENCY_ISO = "MXN";
		public static final String HOSPITAL_CODE = "0700";
		public static final String INDUSTRYCODEPREFIX = "jnj.industryCode.";
		public static final Object DISTRIBUTOR_CODE = "1500";
		public static final Object RETAIL_CODE = "1400";
		public static final Object GOVERMENT_CODE = "0500";
		public static final Object GOVERMENT_CODE1 = "0900";
		public static final Object AFFILIATES_CODE = "0100";
		public static final String SALES_REP_CODE1 = "0400";
		public static final String SALES_REP_CODE2 = "2930";
		public static final String THIRD_PARTY_SALES_REP_CODE1 = "0400";
		public static final String THIRD_PARTY_SALES_REP_CODE2 = "0410";
		public static final String THIRD_PARTY_SALES_REP = "jnj.customerGroup.thirdPartySalesRep";
		public static final String SALES_REP = "jnj.customerGroup.salesRep";
		public static final String ACCOUNT_TYPE_SOLDTO = "jnj.accountType.soldTo";
		public static final String ACCOUNT_TYPE_KEY_ACCOUNT = "jnj.accountType.keyAccount";
		public static final String ACCOUNT_TYPE_SHIPTO = "jnj.accountType.shipTo";
		public static final String ACCOUNT_TYPE_INDIRECT_CUSTOMER = "jnj.accountType.indirectCustomer";
		public static final String PARTNERFUNCTION_SOLDTO = "AG";
		public static final String ACCOUNT_TYPE_SALESREP = "jnj.accountType.salesRep";
		public static final String PARTNERFUNCTION_SHIPTO = "WE";
		public static final String DUMMY_EMAIL_CUSTOMER = "@jnj.com";
		public static final String CUSTOMER_ID = "customerid";
		public static final String INDIRECT_CUSTOMER_QUERY = "select {pk} from {JnjIndirectCustomer} where {country} in (?countries)";
		public static final String INDIRECT_CUSTOMER_QUERY_FOR_ID_COUNTRY = "select {pk} from {JnjIndirectCustomer} where {country}=?country and {indirectCustomer}=?indirectCustomer";
		public static final String COUNTRY_STRING = "country";
		public static final String INDIRECT_CUSTOMER_NAME_STRING = "indirectCustomerName";
		public static final String COUNTRY_PK_STRING = "pk";
		public static final String COUNTRIES_STRING = "countries";
		public static final String ISOCODE_STRING = "isocode";
		public static final String INDIRECT_CUSTOMER_STRING = "indirectCustomer";
		public static final String FILE_NAME_SUFFIX = "jnj.fileName.suffix";
		public static final String OTHER_GROUP_USER_ROLES = "usergroup.roles.otherordergroup";
		public static final String BRAZIL_MASTER_B2BUNIT = "brazil.master.b2bunitid";
		public static final String MEXICO_MASTER_B2BUNIT = "mexico.master.b2bunitid";
		public static final String KEY_ACCOUNT_HOSPITAL_INDICATOR_STRING = "keyHospital";
		public static final String KEY_ACCOUNT_DISTRIBUTOR_INDICATOR_STRING = "keyDistributor";
		public static final String STRING_HOSPITAL = "hospital";
		public static final String STRING_DISTRIBUTOR = "distributor";
		public static final String MEXICO_COUNTRY_CODE = "jnj.customer.MX";
		public static final String BRAZIL_COUNTRY_CODE = "jnj.customer.BR";
		public static final String BRAZIL_COUNTRY_CODE_VALUE = "Brazil";
		public static final String MEXICO_COUNTRY_CODE_VALUE = "Mexico";
		public static final String UPSERT_CUSTOMER_BOTH_INDICATOR = "KATR1";
		public static final String BOTH_INDICATOR_B = "B";
		public static final String BOTH_INDICATOR_A = "A";
		public static final String BOTH_INDICATOR_C = "C";
		public static final String BOTH_INDICATOR_D = "D";
		public static final String INDIRECT_CUSTOMER_Y = "Y";
		public static final String INDIRECT_CUSTOMER_N = "N";
		public static final String UPSERT_CUSTOMER_KZTLF = "KZTLF";
		public static final String UPSERT_CUSTOMER_KZTLF_B = "upsert.customer.kztlf.b";
		public static final String UPSERT_CUSTOMER_KZTLF_C = "upsert.customer.kztlf.c";
		public static final String CENCA_COUNTRIES = "jnj.cenca.countries.isoCode";
		public static final String SITE_NAME_ARGENTINA = "Argentina";
		public static final String SITE_NAME_CHILE = "Chile";
		public static final String SITE_NAME_BRAZIL = "Brazil";
		public static final String SITE_NAME_MEXICO = "Mexico";
		public static final String SITE_NAME_URUGUAY = "Uruguay";
		public static final String IS_TEST_SETUP_REQUIRED_FOR_CONTRACT = "jnj.loadcontracts.test.setup.required";
		public static final String TEST_DATA_FOR_CONTRACT = "jnj.loadcontracts.testdata";
		public static final String ALL_VALID_ACCOUNT_TYPES = "jnj.all.valid.accountType";
		public static final String TEST_DATA = "jnj.upsertcustomer.testdata";
		public static final String IS_TEST_SETUP_REQUIRED = "jnj.upsertcustomer.test.setup.required";
		public static final String DUMMY_USER = "dummy@sapuser.com";
		public static final String INDIRECT_PAYER_QUERY = "select {pk} from {JnjIndirectPayer} where {country}=?country";
		public static final String INDIRECT_PAYER_QUERY_FOR_ID_COUNTRY = "select {pk} from {JnjIndirectPayer} where {country}=?country and {indirectPayer}=?indirectPayer";
		public static final String INDIRECT_PAYER_STRING = "indirectPayer";
		public static final String INDIRECT_PAYER_NAME_STRING = "indirectPayerName";
		public static final String CUSTOMER_LAST_UPDATED_END_DATE = "jnj.upsertCustomer.last.updated.end.date";
	}


	public interface LoadInvoices
	{
		public static final String LOAD_INVOICES_FILE_PATH = "C:\\Users\\ila.sharma\\Desktop\\J&J\\files";
		public static final String LOAD_INVOICES_FILE_NAME = "LoadInvoices";
		public static final String LOAD_INVOICES_EXTRACT = "InvoiceExtract";
		public static final String LOAD_INVOICES_HEADER = "HeaderData";
		public static final String LOAD_INVOICES_LINEITEM = "LineItemData";
		public static final String LOAD_INVOICES_GENERAL = "GeneralData";
		public static final String LOAD_INVOICES_DOC_NUMBER = "InvoiceDocumentNumber";
		public static final String LOAD_INVOICES_BILL_DOC_CURRENCY = "NetValueOfTheBillingItemInDocumentCurrency";
		public static final String LOAD_INVOICES_BILL_DOC = "BillingdocumentCancelledOrNot";
		public static final String LOAD_INVOICES_BILL_DOC_NUMBER = "CancelledBillingDocumentNumber";
		public static final String LOAD_INVOICES_ORG_DATA = "OrganizationalData";
		public static final String LOAD_INVOICES_BILLING_TYPE = "BillingType";
		public static final String LOAD_INVOICES_DATE_SEGMENT = "DateSegment";
		public static final String LOAD_INVOICES_CREATION_DATE = "DateOnWhichInvoiceWasCreated";
		public static final String LOAD_INVOICES_PARTNER_INFO = "PartnerInformation";
		public static final String LOAD_INVOICES_SOLD_TO_PARTY = "SoldToParty";
		public static final String LOAD_INVOICES_PAYER = "Payer";
		public static final String LOAD_INVOICES_REFERENCE = "ReferenceData";
		public static final String LOAD_INVOICES_PO_NUMBER = "CustomerPurchaseOrderNumber";
		public static final String LOAD_INVOICES_ITEM_NUMBER = "ItemNo";
		public static final String LOAD_INVOICES_QTY = "ActualInvoicedQuantity";
		public static final String LOAD_INVOICES_ORDER_REASON = "OrderReason";
		public static final String LOAD_INVOICES_ORDER_ID = "ObjectIdentification";
		public static final String LOAD_INVOICES_MATERIAL = "Material";
		public static final String LOAD_INVOICES_BATCH_NUMBER = "BatchNumberLot";
		public static final String LOAD_INVOICES_REF_SALES_ORDER = "RefereceSalesOrder";
		public static final String LOAD_INVOICES_REF_SALES_ORDER_NUMBER = "ReferenceSalesOrderItemNumber";
		public static final String LOAD_INVOICES_REGION_DOC_ISSUER = "RegionOfDocumentIssuer";
		public static final String LOAD_INVOICES_YR_DOC_DATE = "YearOfDocumentDate";
		public static final String LOAD_INVOICES_MO_DOC_DATE = "MonthOfDocumentDate";
		public static final String LOAD_INVOICES_CNPJ_DOC_ISSUER = "CNPJNumberOfDocumentIssuer";
		public static final String LOAD_INVOICES_MODEL_NOTA_FISCAL = "ModelOfNotaFiscal";
		public static final String LOAD_INVOICES_SERIES = "Series";
		public static final String LOAD_INVOICES_NINE_DIGIT_NUMBER = "NineDigitDocumentNumber";
		public static final String LOAD_INVOICES_RANDOM_ACCESS_KEY = "RandomNumberInAccessKey";
		public static final String LOAD_INVOICES_CHECK_DIGIT_KEY = "CheckDigitinAccessKey";
		public static final String RSA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
		public static final String DECIMAL_POINT = ".";
		public static final String DECIMAL_SPLIT_EXPRESSION = "\\.";
		public static final String INVOICE_LAST_UPDATED_END_DATE = "jnj.invoice.last.updated.end.date";
		public static final String EMAIL_NOTIFICATION = "Email Notification";
		public static final String PIPE_SPLIT_EXPRESSION = "\\|";
		public static final String SEMICOLON = ";";		

	}

	/**
	 * Constants added for the load translation module
	 *
	 * @author sanchit.a.kumar
	 *
	 */
	public interface LoadTranslation
	{
		public static final String LOAD_TRANSLATION_FILE_NAME_PREFIX = "loadtranslation";
		public static final String LOAD_TRANSLATION_RECORD_PARENT = "Z1CMIR";
		public static final String LOAD_TRANSLATION_CUSTOMER_NUMBER = "KUNNR";
		public static final String LOAD_TRANSLATION_PRODUCT_ID = "MATNR";
		public static final String LOAD_TRANSLATION_CUSTOMER_PRODUCT_CODE = "KDMAT";
		public static final String LOAD_TRANSLATION_BASE_UOM = "MEINS";
		public static final String LOAD_TRANSLATION_CUSTOMER_UOM = "ZZMEINH";
		public static final String LOAD_TRANSLATION_DENOMINATOR = "ZZUMREN";
		public static final String LOAD_TRANSLATION_NUMERATOR = "ZZUMREZ";
		public static final String LOAD_TRANSLATION_CUSTOMER_DEV_UOM = "RDPRF";
		public static final String LOAD_TRANSLATION_FOLDER_KEY = "loadTranslation.folder.name";
		public static final String LOAD_TRANSLATION_JNJ_UNIT_TYPE = "jnjUnit";
		public static final String LOAD_TRANSLATION_CUSTOM_UNIT_TYPE = "customUnit";
		public static final String LOAD_TRANSLATION_XSD = "C:\\Users\\bhanu.pratap.jain\\projectData\\loadTranslation\\GlobalCustomerMaterialInfo.xsd";
	}


	/**
	 * Interface Constants for Customer Eligibility Dataload specifying Tag/Element names.
	 *
	 * @author akash.rawat
	 *
	 */
	public interface CustomerEligiblity
	{
		String CUSTOMER_ELIGIBILITY_DATA_LOAD_PATH = "customerEligibility.folder.name";
		String EXCLUSION_TABLE_RECORD = "ExclusionTableRecord";
		String KUNAG = "KUNAG";
		String ZZPRODH1 = "ZZPRODH1";
		String ZZPRODH2 = "ZZPRODH2";
		String ZZPRODH3 = "ZZPRODH3";
		String STATUS = "UPDKZ";
		String CUSTOMER_ELIGIBILITY_DATA_LOAD_RSA_VIEW = "STG_PRODUCT_EXCLUSION";
		String CUSTOMER_ELIGIBILITY_DATA_LOAD_RSA_SCHEMA = "HYBRIS";
	}

	/**
	 * Constants added for the Order
	 *
	 * @author sumit.y.kumar
	 *
	 */
	public interface Order
	{
		public static final String ORDER_STATUS = "CREATED";
		public static final String CODE_STRING = "code";
		public static final String ID_STRING = "id";
		public static final String SUBMIT_ORDER_QUERY = "select {pk} from {order} where {status} in ({{ select {pk} from {orderstatus} where {code} = ?code }}) and {sapOrderNumber} is null";

		public static final String OVER_ALL_STATUS_VALUE = "{overAllStatus}  = ?overAllStatus ";
		public static final String OVER_ALL_STATUS_IS_NULL = "{overAllStatus}  IS NULL ";
		public static final String REJECTION_STATUS_VALUE = "and {rejectionStatus}  = ?rejectionStatus ";
		public static final String REJECTION_STATUS_IS_NULL = "and {rejectionStatus}  IS NULL ";
		public static final String CREDIT_STATUS_VALUE = "and {creditStatus}  = ?creditStatus ";
		public static final String CREDIT_STATUS_IS_NULL = "and {creditStatus}  IS NULL ";
		public static final String DELIVERY_STATUS_VALUE = "and {deliveryStatus}  = ?deliveryStatus ";
		public static final String DELIVERY_STATUS_IS_NULL = "and {deliveryStatus} IS NULL ";
		public static final String INVOICE_STATUS_VALUE = "and {invoiceStatus}  = ?invoiceStatus ";
		public static final String INVOICE_STATUS_IS_NULL = "and {invoiceStatus} IS NULL ";

		public static final String ORDER_ENTRY_STATUS_QUERY = "select {pk} from {JnjOrdEntStsMapping} where {overAllStatus}  ";
		public static final String CONFIG_QUERY = "select {pk} from {JnjConfig} where {ID}=?id";
		public static final String PIPE_STRING = "|";
		public static final String LINE_NUMBER = "LineNumber";
		public static final String CABECALHO = "Cabecalho";
		public static final String REQUISICAO = "Requisicao";
		public static final String PEDIDO = "Pedido";
		public static final String ITEMS_REQUISICAO = "Itens_Requisicao";
		public static final String OBSERVACAO = "Observacao";
		public static final String ITEM_REQUISICAO = "Item_Requisicao";
		public static final String CAMPO_EXTRA = "Campo_Extra";
		public static final String CODIGO_PRODUTO = "Codigo_Produto";
		public static final String QUANTIDADE = "Quantidade";
		public static final String VALOR = "Valor";
		public static final String DTREMESSA = "Dtremessa";
		public static final String PROCESS_PURCHASE_ORDER = "ProcessPurchaseOrder";
		public static final String SENDER = "Sender";
		public static final String LOGICAL_ID = "LogicalId";
		public static final String PURCHASE_ORDER = "PurchaseOrder";
		public static final String HEADER = "Header";
		public static final String LINE = "Line";
		public static final String ID = "Id";
		public static final String CUSTOMER_ITEM_ID = "CustomerItemId";
		public static final String ITEM_ID = "ItemId";
		public static final String CUSTOMER_DOCUMENT_ID = "CustomerDocumentId";
		public static final String Order_QUANTITY = "OrderQuantity";
		public static final String NEED_DELIVERY_DATE = "NeedDeliveryDate";
		public static final String PARTY_ID = "PartyId";
		public static final String SEMI_COLON = ";";
		public static final int ZERO = 0;
		public static final int ONE = 1;
		public static final int TWO = 2;
		public static final int THREE = 3;
		public static final int EIGHT = 8;
		public static final int PO_END_INDEX = 17;
		public static final int EAN_PRODUCT_NUMBER_START_INDEX = 22;
		public static final int EAN_PRODUCT_NUMBER_END_INDEX = 25;
		public static final int QUANTITY_START_INDEX = 40;
		public static final int QUANTITY_END_INDEX = 43;
		public static final int EXPECTED_PRICE_START_INDEX = 46;
		public static final int EXPECTED_PRICE_END_INDEX = 58;
		public static final String ZERO_ONE_STRING = "01";
		public static final String ZERO_TWO_STRING = "02";
		public static final String ZERO_THREE_STRING = "03";
		public static final String PO_DATE_FORMAT = "MMddyyyy";
		public static final String PANARELLO = "PANARELLO";
		public static final String DATE_FORMAT_FOR_FILE_NAME = "MMddyyyyHHmmssSSS";
		public static final String FILE_NAME_INITAL = "submitOrderEdi";
		public static final String XML_FILE_NAME_EXTENSION = ".xml";
		public static final String SLASH = File.separator;
		public static final String UNDER_SCORE = "_";
		public static final String COLON = ":";
		public static final int SHIP_TO_NUMBER_START_INDEX = 263;
		public static final int SHIP_TO_NUMBER_END_INDEX = 277;
		public static final int SOLD_TO_NUMBER_START_INDEX = 192;
		public static final int SOLD_TO_NUMBER_END_INDEX = 206;
		public static final int FOUR = 4;
		public static final String CENTRO = "Centro";
		public static final String OVER_ALL_STATUS = "overAllStatus";
		public static final String REJECTION_STATUS = "rejectionStatus";
		public static final String INVOICE_STATUS = "invoiceStatus";
		public static final String DELIVERY_STATUS = "deliveryStatus";
		public static final String CREDIT_STATUS = "creditStatus";
		public static final String EMPTY_STRING = "";
		public static final int TEN = 10;
		public static final String PIPE_STRING_SEPARATOR = "\\|";
		public static final int FIVE = 5;
		public static final int SIX = 6;
		public static final String MATERIAL_STRING = "<MATERIAL>";
		public static final String QUANTITY_STRING = "<QUANTITY>";
		public static final String EXP_DELIVERY_DATE_FORMAT = "yyyy-MM-dd";
		public static final String SAO_LUIZ_DATE_FORMAT = "MM/dd/yyyy";
		public static final String DATA_ROW_STRING = "for datarow -";
		public static final String SAP_ORDER_HARD_ERROR_TYPE = "E";
		public static final String SAP_ORDER_HARD_ERROR_ID = "V1";
		public static final String TAX_VALUE = "Misc. Tax";
		public static final String DISCOUNT_VALUE = "Misc. Discount";
		public static final String B2BUNIT_SALESORG_MAP = "SalesOrgMap";
		public static final String REQUESTED_DELIVERY_DATE_FORMATTER = "yyyyMMdd";
		public static final String REQUESTED_DELIVERY_DATE_FORMATTER_FINAL = "MMddyyyy";
		public static final String FIND_CART_FOR_GIVEN_CODE = "select {pk} from {cart} where {code}=?code";
		public static final String DATE_FORMAT = "MM/dd/yyyy";
		public static final String ORDER_CHANNEL = "orderChannel";
		public static final String ORDER_CHANNEL_CODE_NULL = "NULL";
		public static final String RSA_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";
		public static final String SYNC_ORDER_LAST_UPDATED_END_DATE = "jnj.syncOrder.last.updated.end.date";
		public static final String SYNC_ORDER_SALES_ORGS_LIST = "jnj.syncOrder.salesOrgs.list";
		public static final String SYNC_ORDER_MAX_RESULT_SET = "jnj.syncOrder.maxresultset";
		public static final String SYNC_ORDER_CREATION_DATE_FOR_ORDERS = "jnj.syncOrder.orderCreation.days";
		public static final String SYNC_ORDER_TEST_DATA = "jnj.syncOrder.testdata";
		public static final String SYNC_ORDER_TEST_SETUP_REQUIRED = "jnj.syncOrder.test.setup.required";
		public static final String EMPENHO_SFPT_TRANSFER_MAX_ATTEMPTS = "jnj.sftp.empenho.transfer.max.attemps";
		public static final String DEFAULT_STATUS_FOR_FAILED_ORDERS = "CREATED";
		public static final String RETRY_ERP_STATUS="jnj.LaCreateERPOrderCronJob.retry.statuses";
		public static final String ORDER_CONFIRMATION_FROM_EMAIL_KEY = "order.confirmation.from.email";
		public static final String ORDER_CONFIRMATION_FROM_EMAIL_NAME_KEY = "order.confirmation.from.email.displayname";
		public static final String ORDER_CONFIRMATION_FROM_EMAIL_DEFAULT = "no-reply@jjcustomerconnect.com";
		public static final String ORDER_CONFIRMATION_FROM_EMAIL_NAME_DEFAULT = "JJ Customer Connect";
		public static final String SFTP_RETRY_ATTEMPTS = "jnj.sftp.retry.attemps";
		public static final String SFTP_CONNECTION_WAITING_TIME = "jnj.sftp.connection.waiting.time";
		public static final String USE_RETRY_LOGIC = "jnj.sftp.connection.retry";
		public static final String USE_RETRY_LOGIC_EMPENHO = "jnj.empenho.sftp.connection.retry";
		public static final String EMPENHO_SFTP_CONNECTION_WAITING_TIME = "jnj.empenho.sftp.connection.waiting.time";
		public static final String DEFAULT_DELIVERY_DATE = "order.delivery.defaultdate";
		public static final String DEFAULT_DELIVERY_DT = "order.delivery.schedule.date"; 
		public static final String ORDER_DELIVERY_UNAVAILABLE = "order.delivery.unavailable";
		public static final String USE_CREATED_DATE = "jnj.syncOrder.use.created.date";
		public static final String BATCH_ORDER_GRACE_TIME = "jnj.batch.order.grace.time";
		public static final String BATCH_ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

		public interface SortOption
		{
			public static final String ORDER_DATE_NEWEST_TO_OLDEST = "order.history.sortBy.date.newToOld";
			public static final String ORDER_DATE_OLDEST_TO_NEWEST = "order.history.sortBy.date.oldToNew";
			public static final String ORDER_NUMBER_INCREASING = "order.history.sortBy.jnjOrderNr.increading";
			public static final String ORDER_NUMBER_DECREASING = "order.history.sortBy.jnjOrderNr.decreasing";
			public static final String ORDER_TOTAL_LOW_TO_HIGH = "order.history.sortBy.price.lowToHigh";
			public static final String ORDER_TOTAL_HIGH_TO_LOW = "order.history.sortBy.price.highToLow";
			public static final String CONTRACT_NUMBER_INCREASING = "Contract Number - increasing";
			public static final String CONTRACT_NUMBER_DECREASING = "Contract Number - decreasing";
			public static final String SAP_NUMBER_INCREASING = "SAP Number - increasing";
			public static final String SAP_NUMBER_DECREASING = "SAP Number - decreasing";
		}

		public interface SearchOption
		{
			public static final String CUSTOMER_PO = "order.history.search.customerPO";
			public static final String JNJ_ORDER_NUMBER = "order.history.search.jnjOrderNr";
			public static final String CONTRACT_NUMBER = "order.history.search.contractNr";
			public static final String PRODUCT_CODE = "order.history.search.productCode";
			public static final String INVOICE_NUMBER = "order.history.search.invoiceNr";
			public static final String INDIRECT_CUSTOMER = "order.history.search.indirectCusromer";
		}

		public interface OrderType
		{
			public static final String ORDET_TYPE_9SE1 = "9SE1";
			public static final String ORDET_TYPE_ZCT = "ZCT";
			public static final String ORDET_TYPE_ZEX = "ZEX";
			public static final String ORDET_TYPE_ZFR = "ZFR";
			public static final String ORDET_TYPE_ZINS = "ZINS";
			public static final String ORDET_TYPE_ZKA = "ZKA";
			public static final String ORDET_TYPE_ZKB = "ZKB";
			public static final String ORDET_TYPE_ZKE = "ZKE";
			public static final String ORDET_TYPE_ZKEI = "ZKEI";
			public static final String ORDET_TYPE_ZKR = "ZKR";
			public static final String ORDET_TYPE_ZLZ = "ZLZ";
			public static final String ORDET_TYPE_ZMIS = "ZMIS";
			public static final String ORDET_TYPE_ZOCR = "ZOCR";
			public static final String ORDET_TYPE_ZODR = "ZODR";
			public static final String ORDET_TYPE_ZOR = "ZOR";
			public static final String ORDET_TYPE_ZOR1 = "ZOR1";
			public static final String ORDET_TYPE_ZORD = "ZORD";
			public static final String ORDET_TYPE_ZQT = "ZQT";
			public static final String ORDET_TYPE_ZRCT = "ZRCT";
			public static final String ORDET_TYPE_ZREC = "ZREC";
			public static final String ORDET_TYPE_ZRED = "ZRED";
			public static final String ORDET_TYPE_ZRER = "ZRER";
			public static final String ORDET_TYPE_ZRO = "ZRO";
			public static final String ORDET_TYPE_ZSER = "ZSER";
			public static final String ORDET_TYPE_ZSM = "ZSM";
			public static final String EXCEPTION_SOLD_TO = "ZOR";
		}

		public interface OrderChannel
		{
			public static final String CHANNEL_SERVICE_VALUES = "orderhistory.channel.service.values";

			public static final String CHANNEL_DFUE = "DFUE";
			public static final String CHANNEL_E001 = "E001";
			public static final String CHANNEL_E003 = "E003";
			public static final String CHANNEL_E004 = "E004";
			public static final String CHANNEL_E006 = "E006";
			public static final String CHANNEL_E008 = "E008";
			public static final String CHANNEL_M002 = "M002";
			public static final String CHANNEL_M004 = "M004";

		}
	}


	/**
	 * Constants added for the DocumenTransfer
	 *
	 * @author komal.sehgal
	 *
	 */

	public interface DocumenTransfer
	{

		public static final String SFTP_HOSTNAME_EMPHENO = "jnj.sftp.empenho.hostname";
		public static final String SFTP_USERNAME_EMPHENO = "jnj.sftp.empenho.username";
		public static final String SFTP_PASSWORD_EMPHENO = "jnj.sftp.empenho.password";
		public static final String SFTP_PORT_EMPHENO = "jnj.sftp.empenho.port";

		public static final String SFTP_HOSTNAME_SUBMIT_EdI = "jnj.sftp.submitedi.hostname";
		public static final String SFTP_USERNAME_SUBMIT_EdI = "jnj.sftp.submitedi.username";
		public static final String SFTP_PASSWORD_SUBMIT_EdI = "jnj.sftp.submitedi.password";
		public static final String SFTP_PORT_SUBMIT_EdI = "jnj.sftp.submitedi.port";

		public static final String SFTP_HOSTNAME_SELLOUT_MDD = "jnj.sftp.sellout.mdd.hostname";
		public static final String SFTP_USERNAME_SELLOUT_MDD = "jnj.sftp.sellout.mdd.username";
		public static final String SFTP_PASSWORD_SELLOUT_MDD = "jnj.sftp.sellout.mdd.password";
		public static final String SFTP_PORT_SELLOUT_MDD = "jnj.sftp.sellout.mdd.port";

		public static final String SFTP_HOSTNAME_SELLOUT_PHARMA = "jnj.sftp.sellout.pharma.hostname";
		public static final String SFTP_USERNAME_SELLOUT_PHARMA = "jnj.sftp.sellout.pharma.username";
		public static final String SFTP_PASSWORD_SELLOUT_PHARMA = "jnj.sftp.sellout.pharma.password";
		public static final String SFTP_PORT_SELLOUT_PHARMA = "jnj.sftp.sellout.pharma.port";

		public static final String METHOD_NAME_UPLOAD_FILE = "uploadFileToSharedFolder";
		public static final String DELIMITER = "/";
		public static final String METHOD_NAME_UPLOAD_FILE_SFTP_SERVER = "uploadFileToSftp";
	}

	public interface SalesOrgCustomer
	{
		public static final String SALES_ORG_CUSTOMER_FILE_PATH = "salesorgcust.folder.name";
		public static final String SALES_ORG_CUSTOMER_FILE_CONTAINS = "customersectorandsalesorg";
		public static final String SALES_ORG_Z1SOHYB = "Z1SOHYB";
		public static final String SALES_ORG_KUNNR = "KUNNR";
		public static final String SALES_ORG_ZZSECTOR = "ZZSECTOR";
		public static final String SALES_ORG_VKORG = "VKORG";
		public static final String SALES_ORG_XSD_FILE_PATH = "C:\\Users\\sandeep.y.kumar\\Desktop\\J & J\\sales Org customer\\xsd\\CustomerSectorAndSalesOrg.xsd";
		public static final String FETCH_SALES_ORG_CUSTOMER_MODEL = "select {pk} from {JnJSalesOrgCustomer} where {customerId}=?customerId and {sector}=?sector";
		public static final String FETCH_INT_SALES_ORG_CUSTOMER_MODEL = "select {pk} from {JnJIntSalesOrgCustomer} where {customerId}=?customerId and {sector}=?sector";
		public static final String FETCH_INT_SALES_ORG_CUSTOMER_STATUS = "select {c:pk} from {JnjIntSalesOrgcustomer as c} where {c:recordstatus} IN ({{select {pk} from {RecordStatus} where {code}=?recordStatus}})";
		public static final String SALES_ORG_CUSTOMER_INT_RECORD_STATUS = "recordStatus";
		public static final String SALES_ORG_CUSTOMER_INT_RECORD_RETRY = "recordRetry";
	}

	/**
	 * Constants added for the Contact Us module
	 *
	 * @author sanchit.a.kumar
	 *
	 */
	public interface ContactUs
	{
		public static final String UNDERSCORE_SYMBOL = "_";
		public static final String PLACE_ORDER = "placeOrder";
		public static final String TRACKING_ORDER = "trackOrder";
		public static final String BID_OPPORTUNITY = "opportunity";
		public static final String BID_ORDER = "bidOrder";
		public static final String BID_UPDATE = "bidUpdate";
		public static final String ACCOUNT_MANAGEMENT = "accountMngmnt";
		public static final String OTHER = "others";
		public static final String PLACE_ORDER_SHORT = "plcOrdr";
		public static final String TRACKING_ORDER_SHORT = "trckOrdr";
		public static final String BID_OPPORTUNITY_SHORT = "bidOpp";
		public static final String BID_ORDER_SHORT = "bidOrdr";
		public static final String BID_UPDATE_SHORT = "bidUpdt";
		public static final String ACCOUNT_MANAGEMENT_SHORT = "accMgmt";
		public static final String OTHER_SHORT = "other";
		public static final String EMAIL_ADDRESS_QUERY = "select {value} from {JnjConfig} where {id}=?id";
		public static final String HIDE_HELP_NEWS_TAB_GROUP = "hideHelpNewsTabGroup";
		public static final String HIDE_HELP_PLACE_ORDER_GROUP = "hideHelpPlaceOrderTabGroup";
		public static final String HIDE_HELP_ORDER_STATUS_GROUP = "hideHelpOrderStatusTabGroup";
		public static final String HIDE_HELP_BIDS_TAB_GROUP = "hideHelpNewsBidsGroup";
	}



	/**
	 * Constants added for the Contract
	 *
	 * @author komal.sehgal
	 *
	 */
	public interface Contract
	{
		public static final String GETCONTRACT_DETAILS_BY_ID = "getContractDetailsById";
		public static final String FIND_CONTRACT_FOR_B2BUNIT = "select {pk} from {JnjContract} WHERE  {unit}=?unit";
		public static final String SORT_CONTRACT_BY_NUMBER = " ORDER BY {JnjContract.eCCContractNum} ";
		public static final String SORT_CONTRACT_BY_CREATIONDATE = " ORDER BY {JnjContract.CREATIONTIME} ";
		public static final String SORT_CONTRACT_BY_INDIRECT_CUSTOMER = " ORDER BY {JnjContract.INDIRECTCUSTOMER} ";
		public static final String SORT_ORDER = "DESC";
		public static final String FIND_BY_CONTRACT_NUMBER = " and {eCCContractNum} like ?searchByCriteria";
		public static final String FIND_BY_TENDER_NUMBER = " and {tenderNum} like ?searchByCriteria";
		public static final String FIND_BY_INDIRECT_NUMBER = " and {indirectCustomer} like ?searchByCriteria";
		public static final String FIND_BY_CONTRCAT_TYPE = " and {contractOrderReason}=?selectCriteria";
		public static final String FIND_CONTRACT_BY_NUMBER = "select {pk} from {JnjContract} where {ECCCONTRACTNUM}=?eCCContractNum";
		public static final String ECCUMBER = "eCCContractNum";
		public static final String CONTRACTNUMBER = "contractNumber";
		public static final String TENDERNUMBER = "tenderNum";
		public static final String INDIRECTCUSTOMER = "indirectCustomer";
		public static final String BID = "Z19";
		public static final String COMMERCIAL = "Z20";
		public static final String SORT_BY_CONTRACT_NUMBER_INC = "contractNumberIncreasing";
		public static final String SORT_BY_CONTRACT_NUMBER_DESC = "contractNumberDecreasing";
		public static final String SORT_BY_CREATION_DATE_INC = "contractCreationDateIncreasing";
		public static final String SORT_BY_CREATION_DATE_DESC = "contractCreationDateDecreasing";
		public static final String SORT_BY_INDIRECT_CUSTOMER_INC = "contractIndirectCustomerIncreasing";
		public static final String SORT_BY_INDIRECT_CUSTOMER_DESC = "contractIndirectCustomerDecreasing";
		public static final String CONTRACT_LAST_UPDATED_END_DATE = "jnj.contract.last.updated.end.date";
		public static final String LOAD_CONTRACT_ORDER_REASON_Z48 = "Z48";
		public static final String CONTRACT_ZCI = "ZCI";
	}

	/**
	 * Constants added for the Add to cart
	 *
	 * @author komal.sehgal
	 *
	 */
	public interface AddToCart
	{
		public static final String ADD_TO_CART = "addToCart";
		public static final String UPDATE_CONTRACT_PRICE = "updateContractPrice";
		public static final String ADD_CONTRACT_TO_CART = "addToCartToContract";
		public static final String ADD_TO_CART_FROM_ORDER = "addToCartFromOrder";
	}

	public interface solrConfig
	{
		public static String ALL_CATEGORY = "!allCategories_string_mv";
		public static String URL_ENCODING = "UTF-8";
		public static String SUGGEST = "/suggest";
		public static String SPELLCHECK_DICTIONARY = "spellcheck.dictionary";
		public static String SPELLCHECK_Q = "spellcheck.q";
	}

	public interface Template
	{

		public static final String SAVE_AS_TEMPLATE_BY_CART = "saveAsTempateByCart";
	}

	public interface OrderHistory
	{

		public static final String CONTRACT_NUMBER = "Contract Number";
		public static final String ORDER_NUMBER = "Order Number";
		public static final String PRODUCT_SKU = "Product SKU";
		public static final String INVOICE_NUMBER = "Invoice Number";
		public static final String INDIRECT_CUSTOMER = "Indirect Customer";
		public static final String QUERY_CONTRACT_NUMBER = "contractnumber";
		public static final String QUERY_ORDER_NUMBER = "code";
		public static final String QUERY_PRODUCT_SKU = "product";
		public static final String QUERY_INVOICE_NUMBER = "invoicenumber";
		public static final String QUERY_INDIRECT_CUSTOMER = "indirectcustomer";
		public static final String VALIDATION_CODE_NULL_CHECK = "Id must not be null";
		public static final String CODE = "id";
		public static final String B2BUNIT = "unit";
		public static final String QUERY_FOR_ORDER_DETALS_PART1 = "select {pk} from {order} where {unit}=?unit and";
		public static final String QUERY_FOR_ORDER_DETAILS_PART2 = "=?subQuery";
		public static final String CLIENT_NUMBER = "clientNumber";
		public static final String JNJ_ORDER_NUMBER = "jnjOrderNumber";
		public static final String CONTRCT_NUMBER = "contactNumber";
		public static final String DATE = "date";
		public static final String TOTAL_ORDER = "totalOrder";
		public static final String ORDER_STATUS = "orderStatus";
		public static final String ORDER_HISTORY = "orderHistory";
		public static final String ORDER_DETAILS = "orderDetails";
	}

	/**
	 * Constants added for the Cart and Check Out Validation
	 *
	 * @author sumit.y.kumar
	 */
	public interface Validation
	{
		public static final String SAVE_EXP_DELIVERY_DATE = "saveExpDeliveryDate";
		public static final String DATE_FORMAT = "MM/dd/yyyy";
	}

	/**
	 * Constants added for Load Contract
	 *
	 * @author bhanu
	 */
	public interface LoadContract
	{
		public static final String FILE_PREFIX = "loadcontract";
		public static final String LOAD_CONTRACT_Z1COHYB = "Z1COHYB";
		public static final String LOAD_CONTRACT_Z1COHYT = "Z1COHYT";
		public static final String LOAD_CONTRACT_KUNNR = "KUNNR";
		public static final String LOAD_CONTRACT_ZI = "KUNNR_ZI";
		public static final String LOAD_CONTRACT_VBELN = "VBELN";
		public static final String LOAD_CONTRACT_BSTNK = "BSTNK";
		public static final String LOAD_CONTRACT_GUEBG = "GUEBG";
		public static final String LOAD_CONTRACT_GUEEN = "GUEEN";
		public static final String LOAD_CONTRACT_WAERK = "WAERK";
		public static final String LOAD_CONTRACT_AUGRU = "AUGRU";
		public static final String LOAD_CONTRACT_AUART = "AUART";
		public static final String LOAD_CONTRACT_NETWR = "NETWR";
		public static final String LOAD_CONTRACT_BALANCE = "BALANCE";
		public static final String LOAD_CONTRACT_GBSTK = "GBSTK";
		public static final String LOAD_CONTRACT_MEGRU = "MEGRU";
		public static final String LOAD_CONTRACT_KDMAT = "KDMAT";
		public static final String LOAD_CONTRACT_MATNR = "MATNR";
		public static final String LOAD_CONTRACT_GBSTA = "GBSTA";
		public static final String LOAD_CONTRACT_KSCHL = "KBETR";
		public static final String LOAD_CONTRACT_POSNR = "POSNR";
		public static final String LOAD_CONTRACT_POSEX = "POSEX";
		public static final String LOAD_CONTRACT_ZMENG = "ZMENG";
		public static final String LOAD_CONTRACT_KWMENG = "KWMENG";
		public static final String LOAD_CONTRACT_DATE_FORMAT = "MM/dd/yyyy";
		public static final String LOAD_CONTRACT_PRICE_CURRENCY_ISO_CODE = "USD";
		public static final String LOAD_CONTRACT_CONTRACT_STATUS = "Completed";
		public static final String LOAD_CONTRACT_FOLDER_KEY = "loadContracts.folder.name";
		public static final String LOAD_CONTRACT_VALID_DOCUMENT_TYPE = "jnj.load.contract.valid.document.types";
		public static final String LOAD_CONTRACT_ORDER_REASON_1 = "Z19";
		public static final String LOAD_CONTRACT_ORDER_REASON_2 = "Z20";
		public static final String LOAD_CONTRACT_ORDER_REASON_3 = "Z26";
		public static final String LOAD_CONTRACT_IMT_RECORD_STATUS = "recordStatus";
		public static final String LOAD_CONTRACT_IMT_RECORD_RETRY = "recordRetry";
		public static final String LOAD_CONTRACT_IMT_DASHBOARD_SENT = "sentToDashboard";
		public static final String LOAD_CONTRACT_STATUS_A = "A";
		public static final String LOAD_CONTRACT_STATUS_B = "B";
		public static final String LOAD_CONTRACT_STATUS_C = "C";
		public static final String LOAD_CONTRACT_START_DATE_FORMAT = "yyyy-MM-dd";
		public static final String LOAD_CONTRACT_ORDER_REASONS_DOC = "jnj.latam.contract.order.reasons.";

	}

	/**
	 * Constants added for Cross Reference Table
	 *
	 * @author ila.sharma
	 */
	public interface CrossReferenceTable
	{
		public static final String YOUR_ID = "yourId";
		public static final String JNJ_ID = "jnJId";
		public static final String PRODUCT_ID = "product";
		public static final String LIST_SIZE = "listSize";
		public static final String CROSS_REFERENCE = "crossRefTable";
		public static final String ERROR_MSG = "An error has occurred while performing this action.";
		public static final String ERROR_MSG_KEY = "errorMsg";
	}

	/**
	 * Constants added for Cross Reference Table
	 *
	 * @author ila.sharma
	 */
	public interface SellOutReports
	{
		public static final String SORT_ORDER_DESC = "Newest to oldest";
		public static final String SORT_ORDER_ASC = "Oldest to newest";
		public static final String DATA_LIST = "dataList";
		public static final String SORT_FLAG = "sortflag";
		public static final String REDIRECT_TO_MAIN = "/my-account/sellout";
		public static final String MEDICAL = "Medical";
		public static final String SLASH = "\\";
		public static final String SELL_OUT_REPORTS = "sellOutReports";
		public static final String SHARED_FOLDER_LOCATION_SELLOUT_MEDICAL = "jnj.shared.sellout.medical.folder";
		public static final String SHARED_FOLDER_LOCATION_SELLOUT_PHARMA = "jnj.shared.sellout.pharma.folder";
		public static final String MEDICAL_REMOTE_PATH = "jnj.sftp.destination.sellout.mdd.folder";
		public static final String PHARMA_REMOTE_PATH = "jnj.sftp.destination.sellout.pharma.folder";
		public static final String PT_DATE_FORMAT = "dd/MM/yyyy";
		public static final String EN_DATE_FORMAT = "MM/dd/yyyy";
		public static final String SEARCH_TERM = "searchTerm";
		public static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";// Added for EDI changes
	}



	/**
	 * Constants added for Cross Reference Table
	 *
	 * @author sandeep.y.kumar
	 */
	public interface UploadOrder
	{
		public static final String SORT_ORDER_DESC = "Newest to oldest";
		public static final String DATA_LIST = "dataList";
		public static final String SORT_FLAG = "sortflag";
		public static final String REDIRECT_TO_MAIN = "/my-account/uploadorders";
		public static final String UPLOAD_ORDERS = "uploadOrders";
		public static final String DATE_FORMAT = "dd/MM/yyyy";
		public static final String SEARCH_TERM = "searchTerm";
		public static final String SUCCESS_STATUS = "Sent Successfully";
		public static final String ERROR_STATUS = "Error";
		public static final String RECEIVED_STATUS = "Received";
		public static final String PARTIAL_SUCCESS = "Sent With Restrictions";
		public static final String SFTP_ERROR = "text.uploadOrder.sftp.error";
		public static final String FIND_B2BUNIT_CNPJ_QUERY = "select {pk} from {JnJLaB2BUnit} where {cnpj}=?cnpj";
		public static final String FIND_B2BUNIT_FOR_TXT = "select {pk} from {JnjB2bUnit} where {uid}=?uid";
	}

	public interface UploadFile
	{
		public static final String SHARED_FOLDER_LOCATION_EDI_ORDER = "jnj.shared.order.edi.folder";
		public static final String SHARED_FOLDER_LOCATION_ORDER = "jnj.shared.order.folder";
		public static final String SFTP_SOURCE_ORDER_FOLDER = "jnj.sftp.source.order.folder";
		public static final String SFTP_SOURCE_ORDER_FOLDER_ARCHIVE = "jnj.sftp.source.order.folder.archive";
		public static final String SFTP_DESTINATION_SUBMIT_ORDER_EDI_FOLDER = "jnj.sftp.destination.submitedi.folder";
		public static final String SFTP_DESTINATION_EMPHENO_FOLDER = "jnj.sftp.destination.empenho.folder";
		public static final String FEED_FILEPATH_EMPENHO_DOC_ERROR = "jnj.shared.outgoing.empenhodoc.error.folder";
		public static final String SFTP_CONNECTION__TYPE_EMPHENO = "empenho";
		public static final String SFTP_CONNECTION__TYPE_SUBMITEDI = "submitEdI";
		public static final String SFTP_CONNECTION__TYPE_SELLOUT_MDD = "selloutMdd";
		public static final String SFTP_CONNECTION__TYPE_SELLOUT_PHARMA = "selloutPharma";
	}

	public interface Nfe
	{
		public static final String NFE_TEMP_FILE_LOCATION_KEY = "temp.nfe.folder.name";
		public static final String NFE_TEMP_FILE_NAME = "tempNfe.xml";
	}

	public interface Register
	{
		public static final String RECAPTCHA_PUBLIC_KEY = "6Le8aucSAAAAABI48-kxndA2-KpZRwd5GW8xF1YN";
		public static final String RECAPTCHA_FIELD_1 = "comment";
		public static final String RECAPTCHA_FIELD_2 = "captcha";
		public static final String RECAPTCHA_BAD_FIELD = "errors.badCaptcha";
		public static final String RECAPTCHA_TRY_AGAIN = "Please try again.";
		public static final String RECAPTCHA_ERROR_KEY = "errorMsg";
		public static final String RECAPTCHA_ERROR_MSG = "Invalid Validation Code!";
		public static final String BREADCRUMBS = "breadcrumbs";
		public static final String HEADER_LINK = "header.link.login";
		public static final String HASH = "#";
		public static final String FORM_GLOBAL_ERROR = "form.global.error";
		public static final String REGISTRATION_ERROR = "registationError";
		public static final String REGISTRATION_ERROR_MSG = "This email id is already registered";
		public static final String COUNTRY_CODE_BR = "BR";
		public static final String DEF_B2B_UNIT_BR = "JnJMasterBR";
		public static final String DEF_B2B_UNIT_MX = "JnJMasterMX";
		public static final String REGISTER_LABEL = "register";
		public static final String REGISTER_EMAIL_FROM = "registerEmailFrom";
		public static final String REGISTER_EMAIL_TO = "register.email.to.";
		public static final String REGISTER_EMAIL_DISPLAY_NAME = "registerEmailFromDisplayName";


	}

	/**
	 * Constants added for User Roles
	 *
	 * @author sumit.y.kumar
	 *
	 */
	public interface UserRoles
	{
		public static final String INDIRECT_CUSTOMER_GROUP_KEY = "cart.display.indirectCustomer.groups";
		public static final String EXPECTED_PRICE_GROUP_KEY = "cart.display.expectedPrice.groups";
		public static final String PRICE_CHANGE_GROUP_KEY = "cart.display.priceChange.groups";
		public static final String DISTRIBUTOR_GROUP_KEY = "services.display.distributor.groups";
		public static final String B2B_CUSTOMER_GROUP = "b2bcustomergroup";
		public static final String USER_ADMIN_GROUP = "useradminGroup";
		public static final String SALES_REP_GROUP = "salesRepGroup";
		public static final String SALES_REP_THIRD_PARTY_GROUP = "salesThirdPartyGroup";
		public static final String ACC_MANAGER_GROUP = "accountManagerGroup";
		public static final String KEY_ACC_MANAGER_GROUP = "keyAccountManagerGroup";
		public static final String USER_RESTRICTED_ROLES = "valid.restricted.user.roles";
		public static final String BASE_STORE = "BaseStore";
		public static final String CMSSITE = "CMSsite";
		public static final String USER_ROLES = "jnj.session.userRoles";

	}


	public interface Logging
	{
		public static final String SUBMIT_ORDER_FILES_INTERFACE = "Submit EDI Order Files Interface";
		public static final String SYNCHRONIZE_ORDER_INTERFACE = "Syncronize Orders Interface";
		public static final String LOAD_TRANSLATION = "Load Translation Interface";
		public static final String UPSERT_PRODUCT_NAME = "UpsertProducts Interface";
		public static final String CUSTOMER_PRICE_SERVICE = "customer_Price_service";
		public static final String UPSERT_CUSTOMER_NAME = "UpsertCustomer Interface";
		public static final String TEMPLATE_ORDER_PAGE = "Template Order Page";
		public static final String PASSWORD_EXPIRY = "Password Expiry";
		public static final String SOLR_SEARCH = "Solr Search";
		public static final String SALES_ORG_CUSTOMER_DATA_LOAD = "SalesOrg Customer  Interface";
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String FILE_PICKING_UTILITY = "FilePickingUtil";
		public static final String CONTRACTS = "My Contracts Controller";
		public static final String CONTRACTS_FACADE = "My Contracts Facade";
		public static final String CONTRACTS_SHOW_CONTRACTS_METHOD = "showContracts()";
		public static final String CONTRACTS_GET_CONTRACTS_METHOD = "getContracts()";
		public static final String CONTRACTS_GET_CONTRACTS_DETAILS = "getContractDetails()";
		public static final String CONTRACTS_SEARCH_CONTRACT = "searchContracts()";
		public static final String CONTRACT_DETAILS_BY_ID = "getContractDetailsById()";
		public static final String CONTRACT_PRODUCT_EXIST = "isProductsExistsInContract()";
		public static final String CONTRACT_GET_PAGED_CONTRACTS = "getPagedContracts()";
		public static final String CONTRACTS_DAO = "My Contracts Dao";
		public static final String CONTRACTS_SERVICE = "Contracts Service";
		public static final String SALES_ORG_CUSTOMER_SERVICE = "Sales Org Customer Service";
		public static final String PRODUCT_SERVICE = "My Product Service";
		public static final String AUTO_COMPLETE_SERVICE = "Jnj Auto Complete Service";
		public static final String SAVECONTRACT = "saveContract()";
		public static final String GET_LOADE_TRANSLATION_MODEL = "getLoadTranslationModel";
		public static final String HYPHEN = " - ";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String SUBMIT_ORDER_EDI = "Submit Order EDI";
		public static final String FORGOT_PASSWORD_NAME = "Forgot Password";
		public static final String SYNC_ORDER_RENAME_FILES = "Synchronise Order Hot Folder Files Rename";
		public static final String SYNC_ORDER_RENAME_FILES_CRON_JOB_METHOD = "perform() method of Cron Job";
		public static final String SYNC_ORDER_RENAME_FILES_CRON_JOB_METHOD_EXCEPTION = "Exception occured inside perform() method of Cron Job";
		public static final String SYNC_ORDER_READING_COUNTERS = "Reading Counters From Config Table";
		public static final String SYNC_ORDER_GETTING_FILES = "Calling Util. to Read and Sort Files";
		public static final String SYNC_ORDER_RENAMING_SAP_ORDER = "Renaming SAP Order File";
		public static final String SYNC_ORDER_RENAMING_SAP_ORDER_LINE = "Renaming SAP Order Line File";
		public static final String SYNC_ORDER_RENAMING_SAP_SCHEDULE_LINE = "Renaming SAP Schedule Line File";
		public static final String SYNC_ORDER_PERSISTING_COUTNERS = "Saving Counters to Hybris";
		public static final String SYNC_ORDER_RENAME_METHOD_NAME = "renameFiles()";
		public static final String PICK_AND_SORT_FILES = "pickAndSortFilesForHotFolder()";
		public static final String FEED_CONTRACTS_JOB = "Cron Job For LoadContract Interface";
		public static final String FEED_CONTRACTS_JOB_METHOD_NAME = "perform() method of Cron Job For LoadContract Interface";
		public static final String FEED_DROPSHIPMENT_JOB_METHOD_NAME = "perform() method of Cron Job For LoadDropshipment Interface";
		public static final String USER_LOGIN_JOB_METHOD_NAME = "perform() method of Cron Job For User Login Job";
		public static final String FEED_CONTRACTS_JOB__METHOD_EXCEPTION = "Exception occured inside perform() method of Cron Job";
		public static final String LOAD_CONTRACTS_INTERFACE = "Load Contract Interface";
		public static final String LOAD_CONTRACTS_INTERFACE_NO_RECORD_FOUND = "No Contract Record found";
		public static final String SFTP_UTIL = "Sft_Util";
		public static final String SFTP_UTIL_METHOD = "uploadFileToSftp()";
		public static final String SFTP_CREATE_DEFAULT_OPTION_METHOD = "createDefaultOptions()";
		public static final String MY_TEMPLATE = "myTemplate";
		public static final String MY_TEMPLATE_FACADE = "myTemplateFacade";
		public static final String QUERY_NFE = "Electronic Nota Fiscal";
		public static final String INVOICE_SERVICE = "Invoice Service";
		public static final String ORDER_HISTORY_INVOICE_DETAILS = "My Account - Order History - Inovoice Details";
		public static final String INVOICE_ORDER_POPULATOR = "Invoice Order Data Populator";
		public static final String INVOICE_ENTRY_DATA_POPULATOR = "Invoice Entry Data Populator";
		public static final String METHOD_POPULATE = "populate()";
		public static final String LOAD_INVOICES_JOB = "Load Invoices Interface";
		public static final String LOAD_DROP_SHIPMENT_JOB = " Load Drop Shipment Interface";
		public static final String LOAD_INVOICE_PERFORM = "perform() method of Cron Job LoadInvoices interface";
		public static final String LOAD_INVOICE_GET_INVOICES = "getLoadInvoices()";
		public static final String LOAD_INVOICE_READ_INVOICES = "readInvoiceXMLFile()";
		public static final String LOAD_DROPSHIPMENT_READ_INVOICES = "readDropshipmentXMLFile()";
		public static final String LOAD_INVOICE_CHECK_HEADER = "checkHeaderEndElement()";
		public static final String LOAD_INVOICE_CHECK_LINEITEM = "checkLineItemEndElement()";
		public static final String LOAD_INVOICE_MAP_INVOICE_MODEL = "mapInvoiceToModel()";
		public static final String LOAD_INVOICE_ADD_TO_ORDER = "addDataToInvoiceOrderModel()";
		public static final String LOAD_INVOICE_ADD_TO_ENTRY = "addDataToInvoiceEntryModel()";
		public static final String LOAD_INVOICE_GET_B2BUNIT = "getB2bUnitForId()";
		public static final String LOAD_INVOICE_SAVE_ORDER = "saveInvoiceOrder()";
		public static final String LOAD_INVOICE_SAVE_ENTRY = "saveInvoiceEntry()";
		public static final String FORMS_NAME = "Forms";
		public static final String CMS_PAGE_VIEW_HANDLER = "CmsPageBeforeViewHandler";
		public static final String REGISTER_EMAIL = "registerEmail";
		public static final String CMS_PAGE_VIEW_HANDLER_METHOD_NAME = "beforeView() method ";
		public static final String SUBMIT_ORDER = "Submit Order";
		public static final String GET_RECENTLY_USED_CONTRACTS = "getRecenlyUsedContracts()";
		public static final String REPLENISH_ORDER_JOB = "JnjB2bAcceleratorCartToOrderJob";
		public static final String NEWS_RELEASES = "News Releases";
		public static final String PRODUCT_DISPLAY_PAGE = "Product Display";
		public static final String JNJ_CMS_UTIL = "JnJ CMS Util";
		public static final String JNJ_CMS_COMPONET_DAO = "JnjCmsComponentDao";
		public static final String JNJ_CMS_COMPONET_SERVICE = "JnjCmsComponentService";
		public static final String JNJ_CMS_COMPONET_FACADE = "JnjCmsComponentFacade";
		public static final String DOWNLOAD_PHARMA_PRICE_LIST = "Download Pharma Price List";
		public static final String UNIT_SERVICE = "Unit Service";
		public static final String COMPANY_SERVCE = "Company Service";
		public static final String EMAIL_NOTIFICATION_PROCESS = "Email Notification Process";
		public static final String LAUDO = "Laudo";
		public static final String LOAD_CUSTOMER_ELIGIBILITY_INTERFACE = "Customer Eligibility Interface";
		public static final String JNJ_ZIP_FILE_UTIL = "JnjZipFileUtil";
		public static final String INTERFACE_OPERATION_ARCHITECTURE_UTIL = "Interface Operation Architecture Utility";
		public static final String INTERFACE_LOAD_CONTRACT = "Load Contract Interface";
		public static final String INTERCEPTOR_PREPARE_JNJPARAGRAPHCOMPONENT = "JnjParagraphComponent Prepare Interceptor";
		public static final String JNJ_USER_LOGIN_JOB = "JnjUserLoginJob";
		public static final String SENT_ACCOUNT_ACIVE_EMAIL = "sendAccountActiveEmail";
		public static final String INTIALIZE_CREATE_USER_EVENT = "initializeCreateuserEvent";
		public static final String GET_USER_ROLES_FOR_APPROVAL_EMAIL = "getUserRolesForApproverEmail";
		public static final String LEGACY_POLICY_CHECK = "Legacy/Policy Check";
		public static final String ORDER_HISTORY = "Order History";
		public static final String ORDER_DETAIL = "Order Detail";
		public static final String HOME_PAGE = "Home Page";
		public final static String CRON_JOB_FILE_PURGE = "Cron Job For File Purging";
		public static final String FILE_PURGING_SAP = "File Purging for SAP";
		public static final String FILE_PURGING_HYBRIS = "File Purging for Hybris";
		public static final String FILE_PURGING_LOG = "File Purging for Log";
		public static final String JNJ_ENCYPTION = "JnJ Encryption";
		public static final String HELP_PAGE = "Help Page";
		public static final String LOAD_TRANSLATION_SERVICE = "Load Translation Service";
		public static final String ORDER_FLOW = "Order Flow";
		public static final String IMPORT_FROM_EXCEL = "Import From Excel";
		public static final String ADDRESS_SERVICE = "Address Service";
		public static final String SET_SHOW_PRICE_JOB = "Set Show Price Job";
		public static final String SET_SHOW_PLANNED_DATE = "Set Show Planned Date";
		public static final String SYNC_ORDER_CLENUP_JOB = "Sync Order Cleanup Job";
		public static final String FEED_TRANSLATION_JOB__METHOD_EXCEPTION = "Exception occured inside perform() method of Load Translation Cron Job";
		public static final String SALES_ORG_CUST = "Sales Org Customer";

		public static final String GET_INVOICE_DOCUMENT = "Get Invoice Document";
		public static final String GET_EMAIL_ERROR_VM = "emails-handleError.vm";


		public static final String SYNC_PRODUCT_MASTER = "Sync Master To Local Catalog";
		public static final String CREATE_MESSAGE_DATA = "Create Message Data";
		public static final String CART_CALCULATE = "Cart Calculate";
		public static final String CART_RECALCULATE = "Cart Realculate";

        String UPDATE_ORDER_STATUS = "Update Order Status Job";
        String UPDATE_ORDER_HEADER_STATUS = "Update Order Header Status Job";
        String UPDATE_DELIVERY_DATE = "Update Delivery Date Job";
        String UPDATE_CARRIER_STATUS = "Update Carrier Status Job";
        String IMMEDIATE_ORDER_EMAIL = "Immediate Order Status Email Job";
        String CONSOLIDATED_ORDER_EMAIL = "Consolidated Order Status Email Job";       
        public static final String ACTIVE_USER_REPORT_EMAIL = "Active User Report Email";
    }

	public interface Login
	{
		public static final String LOGIN_ERROR_LIMIT = "jnj.login.warn.limit";
		public static final String LOGIN_ERROR_ACCOUNT_DISABLED = "login.error.account.disbaled";
		public static final String LOGIN_ERROR_NEW_ACCOUNT_DISABLED = "login.error.new.user.account.disbaled";
		public static final String LOGIN_WARNING_ACCOUNT_DISABLED = "login.warn.account.disabled";
		public static final String LOGIN_ERROR_NOT_FOUND = "login.error.account.not.found.title";
		public static final String LOGIN_ERROR_INVALID_COUNTRY = "login.error.invalid.country";
		public static final String HEADER_LINK = "header.link.login";
		public static final String LOGIN_ERROR = "loginError";
		public static final String USER_INVALID_COUNTRY_VARIABLE = "invalidCountry";
		public static final String USER_INVALID_COUNTRY_VALUE = "true";
		public static final String NEW_USER_DISABLED = "newUserDisabled";
		public static final String LEGAL_NOTICE_COMPONENT_UID_KEY = "login.legalNotice.component.uid";
		public static final String PRIVACY_POLICY_COMPONENT_UID_KEY = "login.privacyPolicy.component.uid";
		public static final String LOGIN_CONTACT_INFO_COMPONENT_UID_KEY = "login.contactInfo.component.uid";
		public static final String FOOTER_CONTACT_INFO_COMPONENT_UID_KEY = "footer.contactInfo.component.uid";
		public static final String PRIVACY_POLICY_PDF = "pdfForMXPrivacyPolicyLink";
		public static final String BU_LIST = "bulist";
	}

	public interface SynchronizeOrder
	{
		public static final String JNJ_SYNC_ORDER_THREADS_COUNT = "jnj.syncOrder.threads.count";

		public static final String KEY_FOR_SAP_ORDER_COUNTER = "counterSAPOrder";
		public static final String KEY_FOR_SAP_ORDER_LINE_COUNTER = "counterSAPOrderLine";
		public static final String KEY_FOR_SAP_SCHEDULE_LINE_COUNTER = "counterSAPScheduleLine";
		public static final String KEY_FOR_SAP_FILE_COUNT = "syncOrderFileCount";
		public static final String KEY_FOR_SAP_ORDER_FILE_NAME = "syncOrder.sapOrder.fileName";
		public static final String KEY_FOR_SAP_ORDER_LINE_FILE_NAME = "syncOrder.sapOrderEntry.fileName";
		public static final String KEY_FOR_SAP_SCHEDULE_LINE_FILE_NAME = "syncOrder.sapScheduleLine.fileName";
		public static final String JNJ_SYNCHRONIZE_ORDER_WRITE_COMPOSITE_JOB = "JnjSynchronizeOrderWriteCompositeJob";
		public static final String JNJ_SYNCHRONIZE_ORDER_RENAME_JOB = "jnjSynchronizeOrdersRenameFileJob";
		public static final String JNJ_SYNCHRONIZE_ORDER_PROCESS_DELAY = "syncorder.process.delay";
	}

	public interface Forms
	{
		public static final String FORMS_FROM_EMAIL = "formsFromEmail";
		public static final String FORMS_FROM_DISPLAY_NAME = "formsFromDisplayName";
		public static final String SERVICES_CMS_PAGE = "jnjServicesPage";
		public static final String ADD_INDIRECT_CUSTOMER_CMS_PAGE = "addIndirectCustomer";
		public static final String ADD_INDIRECT_PAYER_CMS_PAGE = "addIndirectPayer";
		public static final String CONSIGNMENT_ISSUE_CMS_PAGE = "consignmentIssue";
		public static final String INTEGRAL_SERVICES_CMS_PAGE = "integralServices";
		public static final String SERVICE_FORM_CMS_PAGE = "serviceOCD";
		public static final String SYNTHES_CMS_PAGE = "synthes";
		public static final String SELECT_DATE_TEXT = "misc.services.selectDate";

		public static final String IS_DISTRIBUTOR = "isDistributor";

		public static final String PERCENTAGE_SYMBOL = "%";
		public static final String OCD_FORM_CONFIG_KEY = "ocdFormModels";
		public static final String INDIRECT_CUSTOMER_FORM_CONFIG_KEY = "indirectFormCustomerType";
		public static final String NO_BLANK_ERROR = "noBlankError";
		public static final String NO_BLANK_ERROR_MESSAGE_KEY = "misc.services.noBlankError";
		public static final String BLANK_DATE = "noBlankDateError";
		public static final String BLANK_DATE_MESSAGE_KEY = "misc.services.pleaseSelectDate";
		public static final String INSERT_HOUR = "insertHourError";
		public static final String INSERT_HOUR_MESSAGE_KEY = "misc.services.insertHourError";
		public static final String SUCCESS = "success";
		public static final String TRUE = "true";
		public static final String FALSE = "false";
		public static final String BREADCRUMB_KEY_SERVICES = "header.link.services";
		public static final String BREADCRUMB_KEY_INDIRECT = "header.link.indirectcust";
		public static final String BREADCRUMB_KEY_CONSIGNMENT = "header.link.consigment";
		public static final String BREADCRUMB_KEY_INTEGRAL = "header.link.integral";
		public static final String BREADCRUMB_KEY_OCD = "header.link.ocd";
		public static final String BREADCRUMB_KEY_SYNTHES = "header.link.synthes";
		public static final String BREADCRUMBS = "breadcrumbs";
		public static final String SLASH_SYMBOL = "/";
		public static final String DASH_SYMBOL = "-";
		public static final String SHOW_ADD_INDIRECT = "showAddIndirect";
		public static final String SHOW_CONSIGNMENT = "showConsignment";
		public static final String SHOW_INTEGRAL_SERVICES = "showIntegralServices";
		public static final String SHOW_SERVICE_FORM = "showServiceForm";
		public static final String SHOW_SYNTHESIS = "showSynthesis";
		public static final String SHOW_DOWNLOAD_PHARMA_PRICE_LIST = "showDownloadPharmaPriceList";

		public static final String SHOW_DOWNLOAD_LAUDO = "showDownloadLaudo";
		public static final String SHOW_MANAGE_LAUDO = "showManageLaudo";
		public static final String SHOW_CROSS_REFERENCE_TABLE = "showCrossReferenceTable";
		public static final String SHOW_ADD_INDIRECT_PAYER = "showIndirectPayer";
		public static final String CROSS_REFERENCE_TABLE = "crossReferenceTable";
		public static final String CURRENT_SITE_COUNTRY_REGION = "currentSiteCountryRegionList";

	}

	public interface WebServiceConnection
	{
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.order.endpoint.url";
		public static final String WEBSERVICE_ORDER_USER = "jnj.webservice.order.user";
		public static final String WEBSERVICE_ORDER_PWD = "jnj.webservice.order.password";
		public static final String WEBSERVICE_CONNECTION_TIMEOUT = "jnj.webservice.connectionTimeOut";
		public static final String WEBSERVICE_READ_TIMEOUT = "jnj.webservice.readTimeOut";
		public static final int WEBSERVICE_DEFAULT_TIMEOUT = 130000;

	}

	public interface PostRedirectionURI
	{
		public static final String REDIRECT_PREFIX = "redirect:";
		public static final String SERVICES_PREFIX = "/services";
		public static final String SERVICES_INDIRECT_CUSTOMER = "/addIndirectCustomer";
		public static final String SERVICES_CONSIGNMENT = "/consignmentIssue";
		public static final String SERVICES_INTEGRAL = "/integralServices";
		public static final String SERVICES_OCD = "/serviceForm";
		public static final String SERVICES_SYNTHES = "/synthes";
		public static final String SERVICES_INDIRECT_PAYER = "/addIndirectPayer";
		public static final String CROSS_REFERENCE_TABLE = "/crossReferenceTable";
	}

	public interface NewsReleases
	{
		public static final char BLANK_DELIMITER = ' ';
		public static final String NUMBER_OF_CHARACTERS_KEY = "news.content.characters";

	}

	public interface MediaFolder
	{
		public static final String MEDIA_DIR_KEY = "media.read.dir";
		public static final String MEDIA_ROOT_FOLDER_KEY = "media.read.root.folder";
		public static final String LATAM_CATALOG_RESULT_PAGE_LIMIT = "latam.catalog.result.page.limit";
	}

	public interface MyAccount
	{
		public static final String SIDE_SLOT_COMPONENT_ID = "SideContentSlot";
		public static final String REPORT_LINK_COMPONENT_ID = "ReportsLinkComponent";
		public static final String MY_CONTRACT_LINK_COMPONENT_ID = "MyContractsLinkComponent";
		public static final String USERMANAGEMENT_LINK_COMPONENT_ID = "UserManagmentLinkComponent";
		public static final String MY_PROFILE_LINK_COMPONENT_ID = "MyProfileLinkComponent";
		public static final String CROSS_REFERENCE_LINK_COMPONENT_ID = "CrossReferenceLinkComponent";
		public static final String ADDRESS_BOOK_LINK_COMPONENT_ID = "AddressBookLinkComponent";
		public static final String ORDER_HISTORY_LINK_COMPONENT_ID = "OrderHistoryLinkComponent";
		public static final String SHOW_REPORT = "showReport";
		public static final String SHOW_USER_MANAGEMENT = "showUserManagement";
		public static final String SHOW_MY_CONTRACT = "showContract";
		public static final String SHOW_MY_PROFILE = "showMyProfile";
		public static final String SHOW_CROSS_REFERENCE = "showCrossReference";
		public static final String SHOW_ADDRESS_BOOK = "showAddressBook";
		public static final String SHOW_ORDER_HISTORY = "showOderHistory";
		//TODO: To Remove
		public static final String REPORT_LINK_VISIBLE = "report";
		public static final String USERMANAGEMENT_LINK_VISIBLE = "usermanagement";
		public static final String MY_CONTRACT_LINK_VISIBLE = "contract";
		public static final String UPDATE_SECRET_QUES_ANSWER_ERROR = "account.page.updatesecretques.answerError";

	}

	public interface ServicesForms
	{
		public static final String SIDE_SLOT_COMPONENT_ID = "ServicesSideContentSlot";
		public static final String ADD_INDIRECT_CUST_LINK_COMPONENT_ID = "AddIndirectCustomerLinkComponent";
		public static final String CONSIGNMENT_LINK_COMPONENT_ID = "ConsignmentLinkComponent";
		public static final String INTEGRAL_SERVICES_LINK_COMPONENT_ID = "IntegralServicesLinkComponent";
		public static final String SERVICE_FORM_LINK_COMPONENT_ID = "ServiceFormLinkComponent";
		public static final String SYNTHESIS_LINK_COMPONENT_ID = "SynthesLinkComponent";
		public static final String DOWNLOAD_PHARMA_PRICE_LIST_LINK_COMPONENT_ID = "DownloadPharmaPriceListComponent";
		public static final String LAUDO_DOWNLOAD_LINK_COMPONENT_ID = "LaudoDownloadLinkComponent";
		public static final String LAUDO_MANAGE_LINK_COMPONENT_ID = "LaudoManageLinkComponent";
		public static final String CROSS_REFERENCE_TABLE_LINK_COMPONENT_ID = "CrossReferenceTableLinkComponent";
		public static final String ADD_INDIRECT_PAYER_LINK_COMPONENT_ID = "AddIndirectPayerLinkComponent";

	}


	public interface Laudo
	{
		public static final String LAUDO_PRODUCT_POS_KEY = "laudo.model.product.pos";
		public static final String LAUDO_LOT_NUMBER_POS_KEY = "laudo.model.lotNo.pos";
		public static final String LAUDO_EXPIRATION_DATE_POS_KEY = "laudo.model.expirationDate.pos";
		public static final String LAUDO_FILE_NAME_POS_KEY = "laudo.model.fileName.pos";
		public static final String LAUDO_EXPIRED_DOCUMENT_DELETE = "laudo.model.expiredDocument.pos";
		public static final String LAUDO_COUNTRY_POS_KEY = "laudo.model.country.pos";
		public static final String LAUDO_EN_DATE_FORMAT = "MM/dd/yyyy";
		public static final String LAUDO_PT_DATE_FORMAT = "dd/MM/yyyy";
		public static final String LAUDO_DATE_FORMAT = "MM/dd/yyyy";
		public static final String LAUDO_MEDIA_FOLDER_NAME = "laudo.media.folder.name";
		public static final String DEFAULT_LAUDO_MEDIA_FOLDER_NAME = "laudo";
		public static final String LAUDO_TEMP_MEDIA_FOLDER_NAME = "laudo.temp.folder.name";
		public static final String LAUDO_SEARCH_BY_PRODUCT_AND_LAUDO = "productAndLaudo";
		public static final String LAUDO_DEFAULT_DATE = "Select a Date";
		public static final String LAUDO_FILE_FORMAT_PDF = "pdf";
		public static final String LAUDO_FILE_FORMAT_CSV = "csv";
		public static final String LAUDO_FILE_FORMAT_ZIP = "zip";
		public static final String LAUDO_LOCK_ENTRY = "lockEntry";
		public static final String LAUDO_UNLOCK_ENTRY = "unlockEntry";
		public static final String LAUDO_READ_LOCK = "readLock";
		public static final String LAUDO_WRITE_LOCK = "writeLock";
		public static final String LAUDO_MANDATORY_ERROR_KEY = "services.laudo.download.mandatoryError";
		public static final String LAUDO_GENERAL_EXCEPTION_CODE = "EMSG005";
		public static final String LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE = "misc.services.laudo.generalError";
		/**
		 * EMSG007:: To upload the file you need your user to be properly configured. Please assign the JnJLaudo group
		 * correspondent to your country and try again.
		 */
		public static final String LAUDO_EXCEPTION_CODE_EMSG007 = "EMSG007";
		/**
		 * EMSG008::This file already exists.
		 */
		public static final String LAUDO_EXCEPTION_CODE_EMSG008 = "EMSG008";
		/**
		 * EMSG011::The CSV file provided does not match the required layout. Please select the correct file and try
		 * again.
		 */
		public static final String LAUDO_EXCEPTION_CODE_EMSG011 = "EMSG011";
		/**
		 * EM_CSV_LINE_LEVER:: This represents that there are line level errors while parsing CSV File
		 */
		public static final String LAUDO_EXCEPTION_CSV_LINE_ERRORS = "EM_CSV_LINE_LEVER";
		/**
		 * An error has occurred and the file could not be downloaded. Please try again or choose another file.
		 */
		public static final String LAUDO_EXCEPTION_CODE_EMSG045 = "EMSG045";
		/**
		 * EM_CSV_LINE_LEVER:: This represents that there are line level errors Deleting Laudos
		 */
		public static final String LAUDO_EXCEPTION_DELETE_LINE_ERRORS = "EM_DELETE_LINE_LEVER";
		/**
		 * EMSG046::The file you are trying to upload is not in accordance with the filename provided. Please check and
		 * try again.
		 */
		public static final String LAUDO_EXCEPTION_CODE_EMSG046 = "EMSG046";
		public static final String LAUDO_FILE_SUCCESS_FLAG = "fileScuccess";
		public static final String STATUS_EXCEPTION_ILLEGAL_STATE = "IllegalStateException";
		public static final String STATUS_EXCEPTION_IO = "IOException";
		public static final String STATUS_EXCEPTION_BUSINESS = "BusinessException";
		public static final String STATUS_UNSUPPORTED_ENCODING_EXCEPTION = "UnsupportedEncodingException";
		public static final String STATUS_CSV_PARTIALLY_VALID = "Partialy Valid CSV";
		public static final String STATUS_CSV_INVALID = "InValid CSV";
		public static final String STATUS_HYPHEN = "-";
		public static final String STATUS_SUCCESS_KEY = "laudo.services.file.process.sucess";
		public static final String STATUS_PARTIAL_SUCCESS_KEY = "laudo.services.file.process.partial";
		public static final String STATUS_FAILURE_KEY = "laudo.services.file.process.failure";
		public static final String LAUDO_CSV_COLUMN_EXCEED_MESSAGE_CODE_1 = "misc.services.laudo.csv.columnExceed1";
		public static final String LAUDO_CSV_COLUMN_EXCEED_MESSAGE_CODE_2 = "misc.services.laudo.csv.columnExceed2";
		public static final String LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_1 = "misc.services.laudo.csv.rowNullError1";
		public static final String LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_2 = "misc.services.laudo.csv.rowNullError2";
		public static final String LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_3 = "misc.services.laudo.csv.rowNullError3";
		public static final String LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_1 = "misc.services.laudo.csv.rowLenghtError1";
		public static final String LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_2 = "misc.services.laudo.csv.rowLenghtError2";
		public static final String LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_3 = "misc.services.laudo.csv.rowLenghtError3";
		public static final String LAUDO_CSV_ROW_DATE_ERROR_MESSAGE_CODE_1 = "misc.services.laudo.csv.rowDateError1";
		public static final String LAUDO_CSV_ROW_DATE_ERROR_MESSAGE_CODE_2 = "misc.services.laudo.csv.rowDateError2";
		public static final String LAUDO_EXCEPTION_EMSG007_MESSAGE_CODE = "misc.services.laudo.exception.emsg007";
		public static final String LAUDO_EXCEPTION_EMSG008_MESSAGE_CODE = "misc.services.laudo.exception.emsg008";
		public static final String LAUDO_EXCEPTION_EMSG0011_MESSAGE_CODE = "misc.services.laudo.exception.emsg0011";
		public static final String LAUDO_EXCEPTION_EMSG0045_MESSAGE_CODE = "misc.services.laudo.exception.emsg0045";
		public static final String LAUDO_EXCEPTION_EMSG0046_MESSAGE_CODE = "misc.services.laudo.exception.emsg0046";


	}

	public interface UserCreation
	{
		public static final String ALPHA_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		public static final String NUMBER_SET = "0123456789";
		public static final int LENGTH_OF_PASSWORD = 8;
		public static final String HOST_NAME = "email.logo.hostname";
		public static final String HOST_NAME_BR = "website.brCMSsite.https";
		public static final String HOST_NAME_MX = "website.mxCMSsite.https";
		public static final String HOST_NAME_CENCA = "website.cencaCMSsite.https";
		public static final String HOST_NAME_EC = "website.ecCMSsite.https";
		public static final String HOST_NAME_LOGO_BR = "site.hostname.secured.br";
		public static final String HOST_NAME_LOGO_MX = "site.hostname.secured.mx";
		public static final String HOST_NAME_LOGO_CENCA = "site.hostname.secured.cenca";
		public static final String HOST_NAME_LOGO_EC = "site.hostname.secured.ec";
		public static final String B2B_CUSTOMER_ID = "b2bcustomergroup";
		public static final String INVOICE_NOTIFICATION = "emailPreference2";
		
	}

	public interface Cart
	{
		public static final String HIDE_CHECKOUT = "hideCheckOut";
		public static final String UPLOAD_EXCEL_RESULT = "uploadExcel";
		public static final String SKIP_CART_VALIDATION = "cart.bypassvalidation";
		public static final String SKIP_CART_EXCEPTION = "cart.skipExceptionForCheckout";
		public static final String HIDE_DISCLAIMER = "hideDisclaimer";
		public static final String DROPSHIMENT_ERROR = "dropshipmentError";
		public static final String CATEGORY_ERROR = "homePage.quickCart.errorCategoryMsg";
		public static final String OTHER_CATEGORY_ERROR = "contract.restricted.product";
	}

	public interface FilePurge
	{
		public final static String CRON_JOB_FILE_PURGE = "Cron Job For File Purging";
	}



	public interface UploadExcel
	{
		/* MSG006:: Row [X] - Invalid product ID (Y). */
		public static final String MESSAGE_CODE_MSG006 = "uploadExcel.message.msg006";
		/* MSG014:: An exception occurred while reading the row */
		public static final String MESSAGE_CODE_MSG014 = "uploadExcel.message.msg014";
		/* MSG015:: Maximum limit of quantity is achieved. */
		public static final String MESSAGE_CODE_MSG015 = "uploadExcel.message.msg015";
		/* MSG011:: Invalid Indirect Customer (Y). It has been discarded. */
		public static final String MESSAGE_CODE_MSG011 = "uploadExcel.message.msg011";
		/* MSG012:: Invalid Indirect Customer (Y), the one already in the cart has been kept. */
		public static final String MESSAGE_CODE_MSG012 = "uploadExcel.message.msg012";
		/* MSG013:: Invalid quantity (QTD) */
		public static final String MESSAGE_CODE_MSG013 = "uploadExcel.message.msg013";
		/* MSG008:: Your file was uploaded with warnings. Warning Details. */
		public static final String MESSAGE_CODE_MSG008 = "uploadExcel.message.msg008";
		/* MSG004:: Your file contains one or more errors and could not be uploaded. Please try again. */
		public static final String MESSAGE_CODE_MSG004 = "uploadExcel.message.msg004";
		/* MSG002:: File successfully uploaded, items have been added to your cart. */
		public static final String MESSAGE_CODE_MSG002 = "uploadExcel.message.msg002";
		/* MSG016:: File Size Error. */
		public static final String MESSAGE_CODE_MSG016 = "uploadExcel.message.msg016";
		/* MSG018:: This product is not part of the picked contract. */
		public static final String MESSAGE_CODE_MSG018 = "uploadExcel.message.msg018";
		/* MSG018:: This product is not active in the contract present in the cart. */
		public static final String MESSAGE_CODE_MSG019 = "uploadExcel.message.msg019";

		public static final String HEADER_STATUS_SUCCESS = "success";
		public static final String HEADER_STATUS_INFO = "info";
		public static final String HEADER_STATUS_ERROR = "error";
	}


	public interface CategoryPrice
	{
		public static final String BR_MDD_SHOW_PRICE = "br.mdd.showprice";
		public static final String BR_PHR_SHOW_PRICE = "br.phr.showprice";
		public static final String MX_MDD_SHOW_PRICE = "mx.mdd.showprice";
		public static final String MX_PHR_SHOW_PRICE = "mx.phr.showprice";
		public static final String CATEGORY_CODE_MDD = "MDD";
		public static final String CATEGORY_CODE_PHR = "Pharma";
	}

	public interface CategoryPlannedDate
	{
		public static final String BR_MDD_SHOW_PLANNED_DATE = "br.mdd.showplannedDate";
		public static final String BR_PHR_SHOW_PLANNED_DATE = "br.phr.showplannedDate";
		public static final String MX_MDD_SHOW_PLANNED_DATE = "mx.mdd.showplannedDate";
		public static final String MX_PHR_SHOW_PLANNED_DATE = "mx.phr.showplannedDate";

		public static final String CE_MDD_SHOW_PLANNED_DATE = "ce.mdd.showplannedDate";
		public static final String CE_PHR_SHOW_PLANNED_DATE = "ce.phr.showplannedDate";

		public static final String EC_MDD_SHOW_PLANNED_DATE = "ec.mdd.showplannedDate";
		public static final String EC_PHR_SHOW_PLANNED_DATE = "ec.phr.showplannedDate";
		public static final String MDD_SHOW_PLANNED_DATE_SUFIX = ".mdd.showplannedDate";
		public static final String PHR_SHOW_PLANNED_DATE_SUFIX = ".mdd.showplannedDate";
	}

	public interface Dropshipment
	{
		public static final String LOAD_DROPSHIPMENT_ZSD_H_COMBINE = "ZSD_H_COMBINE";
		public static final String LOAD_DROPSHIPMENT_ZSD_IT_COMBINE = "ZSD_IT_COMBINE";
		// DocumentType
		public static final String AUART = "AUART";
		// Sales Org
		public static final String VKORG = "VKORG";
		// Indicator
		public static final String UPDKZ = "UPDKZ";
		// Principal
		public static final String PRINCIPAL = "PRINCIPAL";
		//Shipper
		public static final String SHIPPER = "SHIPPER";
		// Destination Country
		public static final String LAND1 = "LAND1";
		// Material Number
		public static final String MATNR = "MATNR";
		// Ecare Id
		public static final String ECARE_ID = "ECARE_ID";
		//Shipper Medical
		public static final String SHIPPERMD = "SHIPPER_MD";
		// Sold To
		public static final String SOLDTO = "SOLD_TO";
		//Shipped To
		public static final String SHIPTO = "SHIP_TO";

		public static final String NOT_FOUND_ERROR = "dropshipment.error.not.found";

	}

	public interface UserSearch
	{
		String FIND_B2B_ALL_CUSTOMERS = "SELECT DISTINCT {customer:pk},{customer:name},{customer:email},{customer:status} from {JnjB2BCustomer As customer LEFT JOIN Address As address on  {address:owner}= {customer:pk}"
			+ "JOIN   PrincipalGroupRelation  AS b2bunitrelation ON {b2bunitrelation:source} = {customer:pk}"
			+ "JOIN   JnjB2BUnit 	AS b2bunit	ON {b2bunit:pk} = {b2bunitrelation:target}"
			+ "JOIN   PrincipalGroupRelation 	AS desiredgrouprelations	ON {desiredgrouprelations:source} = {customer:pk} "
			+ "JOIN   UserGroup 	AS desiredgroups    ON {desiredgroups:pk} = {desiredgrouprelations:target}} "
			+ "WHERE {desiredgroups:uid} IN (?usergroups) AND {b2bunit:uid}= ?currentB2bUnit";
		String SEARCH_CUSTOMERS = "SEARCH CUSTOMERS";
	}

	public interface Invoice
	{
		interface SortColumn
		{
			String INVOICE_NUMBER = "invoiceNumber";
			String ORDER_NUMBER = "orderNumber";
			String INVOICE_TOTAL = "invoiceTotal";
			String CREATION_DATE = "creationDate";
		}

		interface SearchOption
		{
			String INVOICE_NUMBER = "invoiceNumber";
			String ORDER_NUMBER = "orderNumber";
			String LOT_NUMBER = "lotNumber";
			String PRODUCT_NUMBER = "productNumber";
			String PO_NUMBER = "poNumber";
			String NONE = "none";
		}

		interface SortOption
		{
			String DEFAULT_SORT_CODE = "Order Date - Newest to Oldest";
			String INVOICE_DATE_OLDEST_TO_NEWEST = "Invoice Date - oldest to newest";
			String INVOICE_DATE_NEWEST_TO_OLDEST = "Invoice Date - newest to oldest";
			String INVOICE_NUMBER_INCREASING = "Invoice Number - increasing";
			String INVOICE_NUMBER_DECREASING = "Invoice Number - decreasing";
			String ORDER_NUMBER_INCREASING = "Order Number - increasing";
			String ORDER_NUMBER_DECREASING = "Order Number - decreasing";
			String INVOICE_TOTAL_INCREASING = "Invoice Total - increasing";
			String INVOICE_TOTAL_DECREASING = "Invoice Total - decreasing";
		}

	}

	public interface UploadedInvoiceFile {
		String TIMEOUT_MINUTES = "jnj.invoice.uploadedFile.timeout.minutes";
		String CLEANUP_DAYS = "jnj.invoice.uploadedFile.cleanup.days";
        String UPLOADED_FILE_FOLDER = "jnj.invoice.uploadedFile.folder";
        String DATE_FORMAT = "jnj.invoice.uploadedFile.date.format";

        String XSL_EXTENSION = "_errors.xls";

        String INVOICE_NUMBER = "Invoice Number";
        String ESTIMATED_DELIVERY_DATE = "Estimated Delivery Date";
        String ACTUAL_DELIVERY_DATE = "Actual Delivery Date";

        String ERROR_MESSAGE = "Error message";

        int HEADER_ROW_INDEX = 0;
        int FIRST_ROW = 1;

        int INVOICE_NUMBER_INDEX = 0;
        int ESTIMATED_DELIVERY_DATE_INDEX = 1;
        int ACTUAL_DELIVERY_DATE_INDEX = 2;
        int ERROR_MESSAGE_INDEX = 3;

        long CLEANUP_DAYS_DEFAULT = 90L;

        String DATES_IN_WRONG_FORMAT = "Dates using the wrong format";
        String INVOICE_NUMBER_NOT_FOUND = "Invoice number not found";

    }
	
    public interface SapFailedOrderReport {
		
		public static final String MAX_RETRY_COUNT_KEY = "jnj.la.send.erp.order.retrycount";
		
		public static final String TO_EMAIL_KEY = "jnj.la.sap.failed.orders.report.to.email";
		public static final String FROM_EMAIL_KEY = "jnj.la.sap.failed.orders.report.fromEmail";
		public static final String FROM_EMAIL_NAME_KEY = "jnj.la.sap.failed.orders.report.fromEmail.name";
		public static final String FILE_PATH_KEY = "jnj.la.sap.failed.orders.report.file.path.temp";
		public static final String FILE_NAME_KEY = "jnj.la.sap.failed.orders.report.fileName";
		public static final String REPORT_SHEET_NAME_KEY = "jnj.la.sap.failed.orders.report.sheet1Name";
		public static final String DATE_FORMAT = "MMddYYYY";
		public static final String TIME_ZONE = "EST5EDT";
		public static final String XLS = ".xls";
		
		public static final String CREATION_ATTEMPT_DATE = "Creation Attempt Date";
		public static final String ORDER_NUMBER ="Order #";
		public static final String ACCOUNT_NUMBER ="Account #";
		public static final String ACCOUNT_DESCRIPTION = "Acc. Description";
		public static final String SHIP_TO = "Ship To";
		public static final String ERROR = "Error";
		
		public static final String FROM_EMAIL_NAME = "fromEmailName";
		public static final String FROM_EMAIL_ADDRESS = "fromEmail";
		public static final String TO_EMAIL = "toEmail";
		public static final String ATTACHMENT_FILE_NAME = "attachmentFileName";
		public static final String ATTACHMENT_FILE_PATH = "attachmentPath";
		
		public static final String SAP_FAILED_ORDER_EMAIL_PROCESS_NAME = "JnjLaSAPFailedOrdersReportEmailProcess";
		public static final String SAP_FAILED_ORDER_STATUS = "jnj.la.sap.failed.orders.status";
		public static final String SAP_FAILED_ORDER_STATUS_DELIMITER = ",";
	}
}
