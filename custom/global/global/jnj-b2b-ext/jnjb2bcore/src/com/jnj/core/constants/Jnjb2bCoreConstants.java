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
package com.jnj.core.constants;

import de.hybris.platform.util.Config;

import java.io.File;

import com.jnj.core.util.JnJCommonUtil;

import java.util.Arrays;
import java.util.List;



/**
 * Global class for all B2BAcceleratorCore constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("PMD")
public class Jnjb2bCoreConstants extends GeneratedJnjb2bCoreConstants
{
	public static final String EXTENSIONNAME = "jnjb2bcore";
	public static final String SITE_NAME = "JNJ_SITE_NAME";
	public static final String SITE_URL = "JNJ_SITE_URL";
	public static final String MDD = "MDD";
	public static final String CONS = "CONS";
	public static final String CONSUMER = "CONSUMER";
	public static final String CONST_CODE_TYPE = "E";
	
	public static final String ORDER360_FROM_EMAIL_NAME = "order360.from.email.name";
	public static final String ORDER360_FROM_EMAIL_NAME_DEFAULT = "JJ Customer Connect";
	public static final String ORDER360_FROM_EMAIL_ADDRESS = "order360.from.email.address";
	public static final String ORDER360_FROM_EMAIL_ADDRESS_DEFAULT = "no-reply@jjcustomerconnect.com";

	public static final String MDD_STORE_ID_KEY = "jnj.store.mdd.id.key";
	public static final String MDD_SITE_ID_KEY = "jnj.site.mddSite.id.key";
	public static final String CONSUMER_SITE_ID_KEY = "jnj.site.consumerSite.id.Key";
	public static final String CONSUMER_STORE_ID_KEY = "jnj.store.consumer.id.key";
	public static final String MDD_CONTENT_CATALOG = "mdd.content.catalog";

	public static final String MDD_SITE_ID = Config.getParameter(MDD_SITE_ID_KEY);
	public static final String MDD_STORE_ID = Config.getParameter(MDD_STORE_ID_KEY);
	public static final String CONSUMER_SITE_ID = Config.getParameter(CONSUMER_SITE_ID_KEY);
	public static final String CONSUMER_STORE_ID = Config.getParameter(CONSUMER_STORE_ID_KEY);

	public static final String MDD_SITE_URL_KEY = "jnj.na.mdd.site.url";
	public static final String CONS_SITE_URL_KEY = "jnj.na.cons.site.url";
	public static final String SYMBOl_PIPE = "|";
	public static final String SYMBOl_SEMI_COLON = ";";
	public static final String SYMBOl_DOT = ".";
	public static final String SYMBOl_COMMA = ",";
	public static final String SYMBOl_DASH = "-";
	public static final String SELECT_ALL_OPTION = "All";
	public static final String DOUBLE_FORWARD_SLASH = "\\";
	public static final String REQUEST_DATE_FORMAT = "request.date.format";
	public static final String Y_STRING = "Y";
	public static final String STOP_SAP_OUTBOUND_CALLING = "jnj.webservice.stop.sap.calling";
	public static final String HOT_FOLDER_ROOT_PATH = "jnj.na.inbound.impex.basefolder";
	public static final String HOT_FOLDER_SURGEON_INBOUND_ROOT_PATH = "jnj.na.surgeon.inbound.impex.basefolder";
	public static final String SHIPPING_METHOD = "ShippingMethod";
	public static final String SHIPPING_METHOD_REVERSAL = "ShippingMethodReverse";
	public static final String CONS_SITE_UID = "consNAEpicCMSite";
	public static final String CREDIT_CARD_MASKING = "**** **** ****";
	public static final String CONSUMER_US_PRODUCT_CATALOG_ID = "consNAProdCatalog";
	public static final String CONSUMER_CA_PRODUCT_CATALOG_ID = "caProdCatalog"; //modified for canada
	public static final String MDD_NA_PRODUCT_CATALOG_ID = "mddNAProdCatalog";
	public static final String CONS_NA_PRODUCT_CATALOG_ID = "consNAProdCatalog";
	public static final String SYMBOL_UNDERSCORE = "_";
	public static final String EMPTY_STRING = "";
	public static final String CONSUMER_US_PRODUCT_CATALOG_SYNC_JOB = "SyncCONSproductStaged->CONSproductOnline";
	public static final String MDD_NA_PRODUCT_CATALOG_SYNC_JOB = "Sync-MDDproductStaged->MDDproductOnline";
	public final static String DELIVERY_TIME_CONS = "consumer.cart.deliveryTimeCons";
	public final static String DELIVERY_TIME_ONE = "1";
	public final static String DELIVERY_TIME_TWO = "2";
	public final static String PDF_EXTENSION = ".pdf";
	public static final String PORTAL_DATE_FMT_DFLT="portal.date.format.default";
	public static final String GENERIC_DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
			
	public static final String GENERIC_TIME_STAMP_FORMAT = "dd/MM/yyyy HH:mm:sss z";
	public static final String GENERIC_DAO_DATE_FORMAT = "yyyy-MM-dd";
	public static final String CURRENT_B2BUNIT = "CURRENT_B2BUNIT";
	public static final String CURRENT_PAGE_TYPE = "currentPageType";
	public static final String EPIC_SITE_URL = "jnj.epic.site.url";
	public static final String EMAIL_LOG_HOSTNAME = "email.logo.hostname";
	public static final String EXPORT_EMAIL_FROM = "export.email.from";
	public static final String EXPORT_EMAIL_DISPLAY_NAME = "export.email.displayName";
	public static final String EMAIL_ATTACHMENT_PATH = "attachmentPath";
	public static final String EXPORT_EMAIL_ATTACHMENT_PATH_KEY = "export.email.attachment.path";
	public static final String EXPORT_EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	public static final String USE_DIRECT_PATH = "useDirectPath";
	public static final String N_STRING = "N";
	public static final String US = "US";
	public static final String DELETE_FILE = "deleteFile";
	public static final int NUMBER_ZERO = 0;
	public static final String MDD_EXPORT_FILE_MEDIA_KEY = "mddCatalogExportFileMedia";
	public static final String MDD_PDF_EXPORT_FILE_MEDIA_KEY = "mddCatalogPdfExportFileMedia";
	
	//Added for AAOL-5163
	public static final String EMAIL_ATTACHMENT_LIST = "attachmentList";
	public static final String CUSTOMER_SERVICE_EMAIL = "customer.service.email.id";
	public static final String MAIL_FOR = "emailFor";

	
	public static final String CONSUMER_CARIBBEAN_CUSTOMER_GROUP = "consumer.customerGroup.caribbean";
	public static final String CONSUMER_PUERTO_CUSTOMER_GROUP = "consumer.customerGroup.puerto";
	public static final String CONSUMER_CARIBBEAN_URL = "consumer.customerGroup.caribbean.url";
	public static final String CONSUMER_PUERTO_URL = "consumer.customerGroup.puerto.url";
	public static final String BYEPASS_LANGUAGE_OVERRIDE_OPTION = "byepass.language.override.option";
	
	public static final String GROUP_PLACE_ORDER = "placeOrderBuyerGroup";
	public static final String GROUP_ORDER_HISTORY = "orderHistoryGroup";
	public static final String GROUP_CATALOG = "catalogGroup";
	public static final String GROUP_PLACE_ORDER_RES_COMM_USER = "placeOrderRestrictionCommercialUserGroup";
	public static final String GROUP_PHARMA_COMMERCIAL_USER = "pharmaCommercialUserGroup";
	public static final String GROUP_MDD_COMMERCIAL_USER = "mddCommercialUserGroup";
	
	
	public static final String TEMP_READ_CONS_INVENTORY_REPORT_PATH = "temp.read.inventory.report.Path";
	public static final String TEMP_READ_INVOICE_CLEARING_REPORT_PATH = "temp.read.invoice.clearing.report.Path"; //AAOL-2426 changes
	public static final String TEMP_READ_CONS_ACCOUNT_AGING_REPORT_PATH = "temp.read.accountAging.report.Path";
	public static final String TEMP_READ_CONS_BALANCE_SUMMARY_REPORT_PATH = "temp.read.balanceSummary.report.Path";
	public static final String TEMP_READ_CONS_PAYMENT_SUMMARY_REPORT_PATH = "temp.read.paymentSummary.report.Path";
	public static final String TEMP_READ_CONS_CREDIT_SUMMARY_REPORT_PATH = "temp.read.creditSummary.report.Path";
	public static final String TEMP_READ_INENTORY_REPORT_PATH = "temp.read.inventory.response.report.Path";
	
	//Added for reason to enable/disable forceNewEntry in add to cart & Zero Pricing
	public static final String FORCE_NEW_ENTRY = "enable.forceNewEntry.cart";
	public static final String CHECK_ZERO_PRICING = "region.check.zero.pricing";
	/**
	* BYEPASS_POPUP_TO_PDF_IN_LANGUAGE_LIST is used to identify for the langauge based on / off pdf
	*/
	public static final String BYEPASS_POPUP_TO_PDF_IN_LANGUAGE_LIST = "byepass.popup.to.pdf.in.languages";
	/**
	 * Constant for MDD Catalog Id.
	 */
	//rama
	public static final String MDD_CATALOG_ID = "mddProdCatalog";//mddNAProdCatalog
	public static final String TEMP_READ_PROPOSED_FILE_PATH = "temp.read.proposed.path";
	
	/**
	 * Constant for Consumer Catalog Id.
	 */
	public static final String CONSUMER_CATALOG_ID = "consNAProdCatalog";
	public static final String CANADA_CONSUMER_CATALOG_ID = "caProdCatalog";
	public static final String ONLINE = "Online";
	public static final String STAGED = "Staged";
	//Added for NAGT 
	public static final String MITEK_FREIGHT_CHARGES = "mitek.freight.charges";

	public static final String RESPONSE_DATE_FORMAT = "response.date.format";
	/* Hybris understandable date format used for database queries. */
	public static final String HYBRIS_UNDERSTANDABLE_DATE_FORMAT = JnJCommonUtil.getValue("hybris.date.format");
	public static final String SESSION_REDIRECTION_KEY = "jnj.na.redirectUrls";

//	public static final String JNJ_NA_TARGET_URL = "jnjNATargetUrl";
	public static final String JNJ_GT_TARGET_URL = "jnjGTTargetUrl";

	public static final String OUTSALESORDERREJECTIONSTATUS_B = "out.sales.order.rejection.status.b";
	public static final String OUTSALESORDERREJECTIONSTATUS_C = "out.sales.order.rejection.status.c";
	public static final String COUNTRY_ISO_MEXICO = "MX";
	public static final String COUNTRY_ISO_BRAZIL = "BR";
	public static final String COUNTRY_ISO_PANAMA = "PA"; // contract impl
	
	public static final String DELETE_INT_RECORD_COUNTS = "deleteIntRecordCounts";
	public static final String SPECIALITY_CUSTOMER_GROUP = "consumer.searchPage.specialityCustomer.group";
	public static final String FILE_SIZE_LIMIT = "feeds.fileSize";
	public static final String COUNTRY_ISO_EQUADOR = "EC";

	//Changes for AAOL - 6138
	public static final String DATE_FORMAT = "portal.date.format";
	public static final String TIME_STAMP_FORMAT = "portal.time.stamp.format";
	public static final String REQUESTDELIVERYDATE_FORMAT = "EEE MMM d HH:mm:ss zzz yyyy";

	//Changes for Expired Password reset - Hotfix
	public static final String EXPIRED_PWD_RESET = "expirePwdReset";
   
	public static final String CLOUDMERSIVE_API_URL = "cloudmersive.api.url";
	public static final String CLOUDMERSIVE_API_KEY = "cloudmersive.api.key";
    
	public interface B2BUnit
	{
		public static final String SALES_ORG_QUERY = "select {pk} from {JnjGTSalesOrgCustomer} where {division}=?division and {sourceSysId}=?sourceSysId";
		public static final String SALES_ORG_QUERY_WITH = " and {salesOrg}=?salesOrg and  {distributionChannel}=?distributionChannel";

		/** Different Account types keys **/
		public static final String INTERNATIONAL_AFFILIATION = "account.type.affiliation";
		public static final String B2B_ACC_TYPE_AFFILIATE = "account.type.affiliation";
		public static final String B2B_ACC_TYPE_TRADE = "account.type.trade";
		public static final String B2B_ACC_TYPE_CONS_AFFILIATE = "account.type.consumerAffiliate";
		public static final String B2B_ACC_TYPE_HOUSE = "account.type.house";
		public static final String B2B_ACC_TYPE_HOSPITAL = "account.type.hospital";
		public static final String B2B_ACC_TYPE_GOVERNMENT = "account.type.government";
		public static final String B2B_ACC_TYPE_CSC = "account.type.csc";
		public static final String B2B_ACC_TYPE_DISTRIBUTOR = "account.type.distributor";
		public static final String B2B_ACC_TYPE_SALES_REP = "account.type.salesRepAccountNumber";
		public static final String B2BCUSTOMERGROUP = "b2bcustomergroup";
		public static final String JNJDUMMYUNIT = "JnJDummyUnit";

	}

	public interface GetContractPrice
	{
		public static final String SYSTEM = "System";
		public static final String USERNAME = "UserName";
		public static final String INTERFACE_ID = "InterfaceID";
		public static final String ORDER_SOURCE = "OrderSource";
		public static final String ORDER_CHANNEL = "OrderChannel";
		public static final String SYNC = "SYNC";
		public static final String ORDER_TYPE = "OrderType";
		public static final String E_CHANNEL_INDICATOR = "EChannelIndicator";
		public static final String GET_CONTRACT_PRICE = "GetContractPrice";
		public static final String WEBSERVICE_ORDER_URL = "jnj.webservice.contract.price.endpoint.url";
		public static final String CREATE_WEBSERVICE_ORDER_URL = "jnj.webservice.create.order.endpoint.url";
		public static final String MOCK_XML_CLASS_PATH = "jnj.webservice.contract.price.mock.xml.classpath";
		public static final String GET_CONTRACT_PRICE_WEBSERVICE_CONNECTION_TIME_OUT = "jnj.webservice.connectionTimeOut.contract.price";
		public static final String GET_CONTRACT_PRICE_WEBSERVICE_READ_TIME_OUT = "jnj.webservice.readTimeOut.contract.price";
		//Temporay change to skip WS call iN Dev.
		public static final String BYEPASS_PRICE_SERVICE_CALL = "byepass.price.service.call";

	}

	public interface WebServiceConnection
	{
		public static final String WEBSERVICE_ORDER_USER = "jnj.webservice.order.user";
		public static final String WEBSERVICE_ORDER_PWD = "jnj.webservice.order.password";
		public static final String WEBSERVICE_CONNECTION_TIME_OUT = "jnj.webservice.connectionTimeOut";
		public static final String WEBSERVICE_READ_TIME_OUT = "jnj.webservice.readTimeOut";
	}

	/**
	 * Below interface added for LOGGING
	 */
	/*public interface Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String CUSTOMER_MASTER_FEED = "Customer Master Feed";
		public static final String HOME_PAGE = "Home Page";
		public static final String LOGIN = "Login";
		public static final String PRIVACY_POLICY_CHECK = "Privacy Policy Check.";
		public static final String ACCOUNTS = "Accounts";
		public static final String PASSWORD_EXPIRY_NAME = "Password Expiry";
		public static final String VALIDATE_CART = "Validate Cart";
		public static final String SURVEY = "Survey";
		public static final String CHANGE_PASSWORD = "Changepassword";
		public static final String REGISTRATION_EMAIL = "Registration Email";
		public static final String PERSONAL_INFORMATION = "Edit Personal Inforation";
		public static final String UPDATE_EMAIL_PREFRENCES = "Update Email Prefrences";
		public static final String ADD_ACCOUNT_EMAIL = "Add Account Email";
		public static final String CHANGE_SECURITY_Question = "Change Security Questions";
		public static final String Get_SECURITY_Question = "Get Security Questions";
		public static final String VERIFY_SECURITY_QUESTION = "Verify Security Questions";
		public static final String SECRET_QUESTION = "Verify Security Questions";
		public static final String UPDATE_PASSWORD = "Updatepassword";
		public static final String EMAIL_PREFERENCES = "EmailPreferences";
		public static final String ADD_ACCOUNT = "Addaccount";
		public static final String ADD_NEW_ACCOUNT = "Addnewaccount";
		public static final String ADD_EXISTING_ACCOUNT = "Addexistingaccount";
		public static final String CREATE_BREADCRUMB = "Createbreadcrumb";
		public static final String ADD_EXISTING_ACCOUNT_EMAIL = "Add Account Email";
		public static final String REPORTS_NAME = "Reports";
		public static final String DATE_UTIL = "Date Util";
		public static final String ORDER_HISTORY_UPDATE_PO = "Update Purchase Order Number";
		public static final String USER_MAMANGEMENT_SEARCH_FACADE = "searchCustomers()";
		public static final String USER_MAMANGEMENT_SEARCH_SERVICE = "searchCustomers()";
		public static final String APPROVED_USER_EMAIL = "ApprovedUserEmail";
		public static final String REJECT_USER_EMAIL = "rejectUserEmail()";
		public static final String PASSWORD_EXPIRY_EMAIL = "password expiry";
		public static final String WARNING_EMAIL = "Warning Email";
		public static final String METHOD_SEND_EMAIL_NOTIFICATION = "Method Send Email Notification";
		public static final String HOME_ORDER_STATUS = "homeOrderStatus";
		public static final String HOME_QUICK_CART = "homeAddQucikToCart";
		public static final String HOME_MULTIPLE_CART = "homeMultipleCart";
		public static final String HOME_START_RETURN = "homeStartReturn";
		public static final String HOME_START_RETURN_POST = "homeStartReturnPost";
		public static final String HOME_START_NEW_ORDER = "homeNewOrder";
		public static final String HOME_START_NEW_ORDER_POST = "homeNewOrderPost";
		public static final String HOME_START_NEW_QUOTE_POST = "homeNewQuote";
		public static final String HOME_START_NEW_QUOTE = "homeNewQuotePost";
		public static final String SAVE_CREDIT_CARD_INFO = "Save Credit Card Info";
		public static final String INITIATE_REPLENISH_ORDER = "Initiate Replenish Order";
		public static final String ORDER_CONFIRMATION_EMAIL = "CartOrderConfirmation";
		public static final String HOME_VIEW_TEMPLATE_DETAIL = "Home Template Details";
		public static final String HOME_GET_BROADCAST_CONTENT = "Home BroadCast Content";
		public static final String GET_CONTRACT_PRICE = "Get Contract Price";
		public static final String USER_PASSWORD_EXPIRY_EMAIL = "Password expiry email / Reset Password Link email.";
		public static final String RENAME_SURGEON_INBOUND_FILES = "Rename Surgeon Inbound Files";
		public static final String PROCESS_SURGEON_INT_RECORDS = "Process Surgeon Intermediate Records";
		public static final String RENAME_ORDER_STATUS_INBOUND_FILES = "Rename Order Status Inbound Files";
		public static final String PROCESS_ORDER_STATUS_INT_RECORDS = "Process Order status Intermediate Records";
		public static final String RENAME_SHIP_TRACK_INBOUND_FILES = "Rename Ship & Tracking Inbound Files";
		public static final String PROCESS_SHIP_TRACK_INT_RECORDS = "Process Ship & Tracking  Intermediate Records";
		public static final String PRODUCT_DETAILS_EMAIL = "Product Details Email";
		public static final String CA_PCM_PROD_EXCLUSION = "Canada Pcm Product Exclusion";
		public static final String US_PCM_PROD_EXCLUSION = "US Pcm Product Exclusion";
		public static final String CUSTOMER_ALIGNMENT = "Customer Alignment";
		public static final String CMOD_RGA_CALL = "Cmod Rga Call";
		public static final String RESET_PASSWORD_CRONJOB = "Reset Password Details Job";
		public static final String RESET_PASSWORD_DETAILS = "Reset Password Toke and Url";
		public static final String EXPORT_CATALOG_EMAIL = "Export Catalog Email";
		public static final String ORDER_SHIPMENT_EMAIL_NOTIFICATION = "Order Shipment Email Notification Job";
		public static final String BACKORDER_EMAIL_NOTIFICATION = "Backorder Email Notification Job";
	}*/

	/**
	 * Below interface added for the Reports functionality
	 *
	 * @author Accenture
	 * @version 1.0
	 */
	public interface Reports
	{
		public static final String ACCOUNTS_MAP = "accountsMap";
		public static final String CURRENT_ACCOUNT_ID = "currentAccountId";
		public static final String SORT_OPTIONS = "sortOptions";
		public static final String OBTAINED_DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
		public static final String ORDERED_FROM_MDD = "orderedFrom";
		public static final String ORDERED_FROM_CONS = "orderedFromCons";
		public static final String PRODUCT_TO_DISPLAY = "productDropDown";
		public static final String ANALYSIS_VARIABLE = "analysisVariable";
		public static final String EXCLUDED_ORDER_TYPES = "reports.excluded.order.types";
		public static final List<String> NON_ZRE_EXCLUDED_SCH_LINE_STATUS = Arrays.asList("RJ", "UC", "CN", "CB");
		public static final String RESTRICTED_ITEM_CATEGORIES = "reports.restricted.item.categories";
		
		
		// AAOL-4347 surabhi
		public static final String CATEGORIES="report.categories";
		public static final String FINANCIAL_REPORT_TYPE ="financial.reportTypes";
		public static final String ORDER_REPORT_TYPE ="order.reportTypes";
		public static final String INVENTORY_REPORT_TYPE ="inventory.reportTypes";

	}

	/**
	 * Below interface added for the Login functionality
	 *
	 * @author Accenture
	 * @version 1.0
	 */
	public interface Login
	{
		public static final String USER_NOT_AUTHORIZED_KEY = "unauthorized";
		public static final String USER_NOT_AUTHORIZED_VALUE = "login.error.unauthorized";

		public static final String USER_GROUP_PLACE_ORDER = "user.groups.ordering.placeOrderGroup";
		public static final String USER_GROUP_VIEW_ONLY = "user.groups.ordering.viewOnlyGroup";
		public static final String USER_GROUP_NO_CHARGE_USERS = "user.groups.noChargeGroup";

		public static final String USER_GROUP_VIEW_ONLY_SALES_REP = "user.groups.ordering.viewOnlySalesGroup";
		public static final String USER_GROUP_PLACED_ORDER_SALES_REP = "user.groups.ordering.viewAndPlaceOrderSalesGroup";

		public static final String USER_GROUP_ADMIN = "user.groups.adminGroup";
		public static final String USER_GROUP_CSR = "user.groups.csrGroup";
		public static final String ACCOUNT_IN_PROCESS = "login.error.account.inprocess";
		public static final String ORDERING_RIGHTS = "orderingRights";
		public static final String USER_TYPE = "userType";
		public static final String SALESREP_ORDERING_RIGHTS = "salesRepOrderingRights";
		public static final String FIRST_TIME_LOGIN = "firstTimeLogin";
		public static final String ACCOUNT_LIST = "accountList";
		public static final String DAYS_BEFORE_PASSWORD_EXPIRES_KEY = "storefront.password.expiry.days";
		public static final String DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
		public static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
		public static final String EXPIRED_PASSWORD_LOGIN_URL = "/login?passwordExpired=";
		public static final String EXPIRED_PASSWORD_PARAM = "passwordExpired";
		public static final String EMAIL = "email";
		public static final String PASSWORD_EXPIRY_UNAUTHORIZED_KEY = "login.password.expiry.unauthorized";
		public static final String PASSWORD_EXPIRY_ERROR = "passwordExpiryError";
		public static final String LOGOUT_URL = "?signout=true";
		public static final String LOGOUT_PARAM = "signout";
		public static final String SURVEY = "survey";
		public static final String SURVEY_COMPONENT_UID_KEY = "login.survey.component.uid";
		public static final String ACCOUNT_UID = "accountUID";
		public static final String ACCOUNT_NAME = "accountName";
		public static final String ACCOUNT_GLN = "accountGLN";
		public static final String PASSWORD_EXPIRE_TOKEN = "passwordExpireToken";
		public static final String CUSTOMER_lOGIN_DISABLE_DAYS = "jnj.gt.disableLogin.days";
		public static final String DAYS_BEFORE_SEND_WARNING_MAIL = "jnj.gt.daysBeforeSEndingWarningMail.days";
		public static final String DATE = "DATE";
		public static final String SALES_REP_USER = "salesRepUser";
		public static final String NO_CHARGE_INTERNAL_USER = "noChargeInternalUser";
		public static final String HASH_CONSUMER_ID = "#####|Consumer";
		public static final String SESSION_EXPIRED_PARAM = "sessionExpired";
		
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
		public static final String SSO_IDP_SHOP_URL ="sso.idp.shop.url";
	}

	/**
	 * Below interface added for the Self-Registration functionality
	 *
	 */
	/*public interface Register
	{
		public static final String EMAIL_PREFERENCE = "emailPreference";
		public static final String BUSINESS_TYPE = "businessType";
		public static final String ESTIMATED_AMOUNT = "estimatedAmount";
		public static final String PURCHASE_PRODUCT = "purchaseProduct";
		public static final String COMMA_SEPARATOR = ",";
		public static final String SECURITY_QUESTIONS = "securityQues";
		public static final String DIVISIONS = "divisions";
		public static final String TERRITORIES = "territories";
		public static final String DEPARTMENTS = "departments";
		public static final String CONSUMER_DIVISONS = "consumerDivisons";
		public static final String VIEW_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewOnlyGroup";
		public static final String PLACE_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderGroup";
		public static final String VIEW_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewOnlySalesGroup";
		public static final String PLACE_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderSalesGroup";
		public static final String REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS = "user.registration.csr.email";
		public static final String REGISTER = "register";
		public static final String USER_ACTIVATION = "activation";
		public static final String REGISTRATION_ERROR = "registationError";
		public static final String REGISTRATION_ERROR_MSG = "This email id is already registered";
		public static final String REGISTRATION_GLOBAL_MSG = "Something went wrong while registering. Kindly try again later.";
		public static final String REGISTRATION_EMAIL_DEPUY_SPINE_EMAIL_ADDRESS = "registration.depuy.spine.email";
		public static final String REGISTRATION_EMAIL_DEPUY_MITEK_EMAIL_ADDRESS = "registration.depuy.mitek.email";
		public static final String USER_ACTIVATION_PAGE = "userActivationPage";
		public static final String REGISTER_SUCCESS = "registerSuccessPage";
		public static final String ACTIVATION_SUCCESS = "activationSuccessPage";
		public static final String REASON_CODE = "reasonCode";

	}*/

	public interface Resources
	{
		public static final String USEFUL_LINK_COMPONENT_ID = "Jnjusefullink";
		public static final String TRAINING_RESOURCES_LINK_COMPONENT_ID = "Jnjtrainingresources";
		public static final String POLICY_FEES_LINK_COMPONENT_ID = "jnjpoliciesandfees";
		public static final String CPSIA_LINK_COMPONENT_ID = "jnjcpsia";
		public static final String TERMS_SALES_LINK_COMPONENT_ID = "jnjtermsofsale";
		public static final String USER_MANAGEMENT_LINK_COMPONENT_ID = "Jnjusermanagement";
	}

	public interface Profile
	{
		public static final String PERSONAL_INFORMATION_LINK_COMPONENT_ID = "jnjGTPersonalinformation";
		public static final String EMAIL_PREFRENCES_LINK_COMPONENT_ID = "jnjGTEmailpreferences";
		public static final String Change_Password_LINK_COMPONENT_ID = "jnjGTChangepassword";
		public static final String ADD_ACCOUNT_LINK_COMPONENT_ID = "jnjGTAddaccount";
		public static final String CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID = "jnjGTChangesecurityquestion";
		
		/*public static final String PERSONAL_INFORMATION_LINK_COMPONENT_ID = "jnjGTPersonalinformation";
		public static final String EMAIL_PREFRENCES_LINK_COMPONENT_ID = "jnjNAEmailpreferences";
		public static final String Change_Password_LINK_COMPONENT_ID = "jnjNAChangepassword";
		public static final String ADD_ACCOUNT_LINK_COMPONENT_ID = "jnjNAAddaccount";
		public static final String CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID = "jnjNAChangesecurityquestion";*/
		
		public static final String Add_NEW_ACCOUNT_EMAIL_SUBJECT = "label.addNewAccountEmail.subject";
		public static final String YES = "YES";
		public static final String NO = "NO";
	}

	public interface Solr
	{
		public static final String RESTRICTED_FIELD = "restricted_field";
		public static final String POST_PROCESSOR = "queryPostProcessor";
		public static final String GET_RESTRICTED_VALUES = "getRestrictedValues";
		public static final String DEFAULTUG = "DEFAULTJNJGT";
		public static final String COLON = ":";
		public static final String ENABLE_PARTIAL_SEARCH = "search.enable.partial.search";
		public static final String RESTRICTED_MANU_FIELD = "restricted_manu_field";
		public static final String SOLR_FACET_LISTENER = "solrFacetSearchListener";

	}

	public interface Cart 
	{
		public static final String IGNORE_UPC = "IGNORE_UPC";
		public static final String INVALID_PRODUCTCODE = "cart.addToCart.productCodeInvalid";
		public static final String VALID_PRODUCTCODE = "Success";
		public static final String PLACEORDERGROUP = "placeOrderSalesGroup";
		public static final String REASON_CODE = "reasonCode";
		public static final String DIVISION_SPINE = "division.spine";
		public static final String DIVISION_CODMAN = "division.codman";
		public static final String DIVISION_MITEK = "division.mitek";
		public static final String DIVISION_CORDIS = "division.cordis";
		public static final String DIVISION_ASP = "division.asp";
		public static final String DIVISION_OCD = "division.ocd";
		public static final String DIVISION_VX = "division.vx";
		public static final String DIVISION_C4 = "division.c4";
		
		public static final String DELIVERED_ORDER_FORM_UPLOAD_SUCCESS = "cart.delivered.order.form.upload.success";
		public static final String DELIVERED_ORDER_FORM_UPLOAD_FAILURE = "cart.delivered.order.form.upload.failure";
		
		public static final String VALID_DCHAIN_CODE_FOR_ZDEL09 = "09";
		public static final String VALID_DCHAIN_CODE_FOR_ZDEL04 = "04";
		public static final String REASON_CODE_RETURNS_EXCEPT_CSC_AND_DIST = "reasonCodeReturnExceptCSCnDIST";
		public static final String REASON_CODE_RETURNS_CSC_AND_DIST = "reasonCodeReturnCSCnDIST";
		public static final String REASON_CODE_NO_CHARGE = "reasonCodeNoCharge";
		public static final String CART_FILE_UPLOAD_PATH = "cart.file.upload.path";
		public static final String SALES_REP_ACC_NUM_FOR_NO_CHARGE_ORDERS = "noCharge.salesRep.accounts";
		public static final String SALES_REP_ACC_NUM_FOR_DELIVERED_ORDERS = "deliveredOrder.salesRep.accounts";
		public static final String SALES_REP_ACC_NUM_FOR_REPLENISH_ORDERS = "replineshmentOrder.salesRep.accounts";
		public static final String SALES_REP_ACC_NUM_FOR_INTERNATIONAL_ORDERS = "international.salesRep.accounts";
		public static final String PURCHASE_ORDER = "purchaseOrder";
		public static final String SHIPPING_METHOD_FDESO = "shipping.method.FDESO";
		public static final String SHIPPING_METHOD_FDEFO = "shipping.method.FDEFO";
		public static final String SHIPPING_METHOD_FDESTO = "shipping.method.FDESTO";
		public static final String SHIPPING_METHOD_FDEPTO = "shipping.method.FDEPTO";
		public static final String SHIPPING_METHOD_FDEPO = "shipping.method.FDEPO";
		public static final String SHIPPING_METHOD_FDE2D = "shipping.method.FDE2D";
		public static final String SHIPPING_METHOD_UPSSE = "shipping.method.UPSSE";
		public static final String SHIPPING_METHOD_UPSSN = "shipping.method.UPSSN";
		public static final String SHIPPING_METHOD_UPSSL = "shipping.method.UPSSL";
		public static final String SHIPPING_METHOD_FDESA = "shipping.method.FDESA";
		public static final String SHIPPING_METHODS = "shipping.methods";
		public static final String SHIPPING_METHODS_STANDARD = "shipping.method.standard";
		public static final String EARLY_ZIP_CODE_FEDEX = "early.zipcode.courier.fedex";
		public static final String EARLY_ZIP_CODE_UPS = "early.zipcode.courier.ups";
		public static final String EXCLUDED_PRODUCTS = "jnj.na.order.excluded.product.key";
		public static final String EXCLUDED_PRODUCTS_WITH_SAP_ORDER_NUMBER = "jnj.na.order.excProd.with.sapOrdNum.key";
		public static final String ORDER_TYPE_CONSTANT = "cart.common.orderType";
		public static final String SPINE_DEFAULT_LOT = "spine.default.lot.num";
		public static final String CODMAN_DEFAULT_LOT = "codman.default.lot.num";
		public static final String SERVICE_FEE_PRODUCT_CODES = "service.fee.product.codes";
		public static final String BASKET_PAGE_MESSAGE_QTY_ADJUSTED = "basket.page.message.qtyAdjusted";
		public static final String BASKET_PAGE_MESSAGE_MIN_QTY_ADDED = "basket.page.message.minQtyAdded";
		public static final String ALL_ENTRIES_REMOVED = "allEntreisRemoved";
		public static final String EXP_SHIPPING_METHOD_PREFIX ="shipping.method.exp.prefix";
		public static final String EXP_UPS_SHIPPING_METHOD_EXCEPTIONAL ="shipping.method.ups.exceptional";
	}

	public interface PLP
	{
		public static final String SEARCH_GROUPS = "storefront.search.group.results";
		public static final String ROOT_CATEGORY_CODE = "Categories";
		public static final String SEARCH_RESULT = "Search Results";
		public static final String DEFUALT_ACTIVE = ":relevance:status:ACTIVE";
		public static final String ONLY_ACTIVE = "relevance:status:ACTIVE";
	}

	public interface SalesAlignment
	{
		public static final String CONSUMER = "CONSUMER";
		public static final String MDD = "MDD";
		public static final String PHARMA = "PHARMA";
		public static final String ACCESS_BY_WWID = "WWID";
	}

	public interface Order
	{
		public static final String TAX_VALUE = "Misc. Tax";
		public static final String DISCOUNT_VALUE = "Misc. Discount";
		public static final String B2BUNIT_SALESORG_MAP = "SalesOrgMap";
		public static final String INTERFACE_NUMBER="InterfaceNumber";
		public static final String BUSSINESS_OBJ_NAME="BusinessObjName";
		public static final String ORDER_DETAIL_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD = "orderDetail.pdfAndExcel.download.lineThreshold";
		public static final String PRICE_OVERRIDE_HOLD_CODE = "PA";
		
		public interface SortOption
		{
			public static final String DEFAULT_SORT_CODE = "Order Date - Newest to Oldest";
			public static final String ORDER_DATE_OLDEST_TO_NEWEST = "Order Date - oldest to newest";
			public static final String ORDER_NUMBER_INCREASING = "Order Number - increasing";
			public static final String ORDER_NUMBER_DECREASING = "Order Number - decreasing";
			public static final String ORDER_TOTAL_INCREASING = "Order Total - increasing";
			public static final String ORDER_TOTAL_DECREASING = "Order Total - decreasing";
			public static final String ORDER_ACCOUNT_DECREASING = "Account Number - decreasing";
			public static final String ORDER_ACCOUNT_INCREASING = "Account Number - increasing";
			
			public static final String PO_NUMBER_INCREASING = "Order poNumber - increasing";
			public static final String PO_NUMBER_DECREASING = "Order poNumber - decreasing";
			public static final String ORDER_TYPE_INCREASING = "Order Type - increasing";
			public static final String ORDER_TYPE_DECREASING = "Order Type - decreasing";
			public static final String SHIP_TO_INCREASING = "Order ShipTo - increasing";
			public static final String SHIP_TO_DECREASING = "Order ShipTo - decreasing";
			public static final String CHANNEL_INCREASING = "Order Channel - increasing";
			public static final String CHANNEL_DECREASING = "Order Channel - decreasing";
			public static final String STATUS_INCREASING = "Order Status - increasing";
			public static final String STATUS_DECREASING = "Order Status - decreasing";
			
			
		}

		public interface SearchOption
		{
			public static final String ORDER_NUMBER = "Order Number";
			public static final String PRODUCT_NUMBER = "Product Number";
			public static final String PO_NUMBER = "PO Number";
			public static final String DEALER_PO_NUMBER = "Dealer PO Number";
			public static final String INVOICE_NUMBER = "Invoice Number";
			// Added by Pradeep chandupatla to display the credit memo and debit memo in search field GTR-1797
			public static final String CREDIT_MEMO = "Credit Memo";
			public static final String DEBIT_MEMO = "Debit Memo";
		}

		public interface ChannelOption
		{
			public static final String PHONE = "Phone";
			public static final String FAX = "Fax";
			public static final String EMAIL = "Email";
			public static final String WEB = "Web";
			public static final String EDI = "EDI";
			public static final String VMI = "VMI";
			public static final String OTHER = "Other";

			/*** Constant values for the property keys, which corresponds to actual Channel values. ***/
			public static final String CHANNEL_PHONE_VALUES = "orderhistory.channel.phone.values";
			public static final String CHANNEL_WEB_VALUES = "orderhistory.channel.web.values";
			public static final String CHANNEL_OTHER_VALUES = "orderhistory.channel.other.values";
			public static final String CHANNEL_FAX_VALUES = "orderhistory.channel.fax.values";
			public static final String CHANNEL_EMAIL_VALUES = "orderhistory.channel.email.values";
			public static final String CHANNEL_VMI_VALUES = "orderhistory.channel.vmi.values";
			public static final String CHANNEL_EDI_VALUES = "orderhistory.channel.edi.values";
		}

		public static final String PAYMENT_METHOD_KEY = "orderPaymentType";
		public static final String DUMMY_SAP_USER_ID = "dummy@sapuser.com";
		public static final String PAYMENT_CARD_NUMBER_MASK = "**** **** **** ";


		public interface DisputeInquiry
		{
			public static final String DISPUTE_INQUIRY_FILE_PREFIX = "taxexemptcertificate";
			public static final String DISPUTE_INQUIRY_CSR_EMAIL_KEY = "dispute.inquiry.csr.email";
			public static final String DISPUTE_INQUIRY_FILE_PATH_KEY = "dispute.filepath.root";
			public static final String DISPUTE_INQUIRY_FROM_EMAIL_ADDRESS = "dispute.fromEmailAdress";
		}

		public interface Invoice
		{
			public static final String INVOICE_STATUS_KEY = "invoicestatus";
			public static final String PROOF_OF_DELIVERY_DOC_BUFFER = "invoice.deliveryproof.document.buffer.size";
			public static final String PROOF_OF_DELIVERY_TEST_IND = "jnj.na.proof.delivery.test.flag";
			public static final String GHX_E_INVOICING_URL_KEY = "ghxeInvoicingUrl";
		}

		public interface PackingList
		{
			public static final String SDC_URL_KEY = "jnj.na.packing.list.sdc.url";
			public static final String MLC_URL_KEY = "jnj.na.packing.list.mlc.url";
			public static final String PARAM_ENV_SDC_KEY = "jnj.na.packing.list.param.envid.sdc";
			public static final String PARAM_ENV_MLC_KEY = "jnj.na.packing.list.param.envid.mlc";
			public static final String SDC_PARAM_SERVER_KEY = "jnj.na.packing.list.param.server.sdc";
			public static final String MLC_PARAM_SERVER_KEY = "jnj.na.packing.list.param.server.mlc";
			public static final String PARAM_REPORT_KEY = "jnj.na.packing.list.param.report";
			public static final String PARAM_DISTRIBUTION_KEY = "jnj.na.packing.list.param.distribution";
			public static final String PARAM_DESTYPE_KEY = "jnj.na.packing.list.param.destype";
			public static final String PARAM_DESFORMAT_KEY = "jnj.na.packing.list.param.desformat";
			public static final String PARAM_STATUS_FORMAT_KEY = "jnj.na.packing.list.param.statusformat";
			public static final String PARAM_ORD_NUM_KEY = "jnj.na.packing.list.param.orderNumber";
			public static final String SDC_SERVER_DC_VALUES_KEY = "jnj.na.packing.list.sdc.dc.values";
			public static final String MLC_SERVER_DC_VALUES_KEY = "jnj.na.packing.list.mlc.dc.values";
			public static final String SDC_REPUSER_KEY = "jnj.na.packing.list.sdc.repuser";
			public static final String MLC_REPUSER_KEY = "jnj.na.packing.list.mlc.repuser";

		}
		public static final String REQUEST_TYPE_SIMULATION = "requestTypeSimulation";
		public static final String SALES_ORGANISATION = "salesOrg";
		public static final String CONSUMER_VALID_ITEM_CATEGORY_KEY = "order.line.item.to.process";
		public static final String SCHEDULE_LINE_STATUS_UC = "UC";
		public static final String SCHEDULE_LINE_STATUS_CANCELLED = "RJ";
		public static final String PRODUCT_DIVISION = "OCD";
		public static final String CONFIRMED_SCHEDULE_LINE_STATUS = "jnj.na.schedule.line.confirmed.status";
		public static final String CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN = "consumer.order.line.item.category";
		public static final String CONSUMER_ORDER_LINE_HIGH_LEVEL_VALUE = "consumer.order.line.high.level.value";
		public static final String BACKORDER_EMAIL_AVAILABLILITY_DATE_FORMAT_KEY = "jnj.na.backorder.availabledate.format";
		public static final String BACKORDER_REPORT_CONFIGURED_DAYS = "jnj.na.backorder.report.days";
		public static final String DATE_FORMAT="date.format";
		public static final String CONSUMER_PRODUCT_LEVEL_ERROR_CODE = "consumer.product.level.error.code";
		public static final String CONSUMER_PRODUCT_EFFECTIVITY_ERROR_CODE = "consumer.product.effectivity.error.code";
		public static final String CONSUMER_PRODUCT_EXCLUSION_BLOCK_ERROR_CODE = "consumer.product.exclusion.block.error.code";
		public static final String CONSUMER_PRODUCT_FIRST_DEA_BLOCK_ERROR_CODE = "consumer.product.first.dea.block.error.code";
		public static final String CONSUMER_PRODUCT_SECOND_DEA_BLOCK_ERROR_CODE = "consumer.product.second.dea.block.error.code";
		public static final String CONSUMER_PRODUCT_MULTIPLE_BLOCK_ERROR_CODE = "consumer.product.multiple.block.error.code";
		public static final String CONSUMER_ORDER_TAPA_LINE_ITEM_CATEGORY = "consumer.order.tapa.line.item.category";
		public static final String ORDER_ENTRY_REASON_FOR_REJECTION_KEY = "order.entry.reasonOfRejection.";
		public static final String ORDER_STATUS = "PENDING";
		public static final String CODE_STRING = "code";
		public static final String ID_STRING = "id";
		public static final String SUBMIT_ORDER_QUERY = "select {pk} from {order} where {status} in ({{ select {pk} from {orderstatus} where {code} = ?code }}) and {sapOrderNumber} is null and {orderType} IN ({{select {pk} from {JnjOrderTypesEnum} where {code} not in (?excludedOrderType)}}) and {site} in ({{select {pk} from {CMSSite} where {uid} in ('mddNAEpicCMSite','consNAEpicCMSite') }})";
		public static final String ORDER_STATUS_QUERY = "select {pk} from {JnjOrdStsMapping} where {overAllStatus}  = ?overAllStatus and {rejectionStatus}  = ?rejectionStatus and {creditStatus}  = ?creditStatus and {deliveryStatus}  = ?deliveryStatus and {invoiceStatus}  = ?invoiceStatus";
		public static final String ORDER_ENTRY_STATUS_QUERY = "select {pk} from {JnjOrdEntStsMapping} where {overAllStatus}  = ?overAllStatus and {rejectionStatus}  = ?rejectionStatus and {deliveryStatus}  = ?deliveryStatus and {invoiceStatus}  = ?invoiceStatus";
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
		public static final String PURCHASE_ORDER = "PurchaseOrder";
		public static final String HEADER = "Header";
		public static final String LINE = "Line";
		public static final String ID = "Id";
		public static final String CUSTOMER_ITEM_ID = "CustomerItemId";
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
		public static final String ZERO_THREE_STRING = "03";
		public static final String PO_DATE_FORMAT = "MMddyyyy";
		public static final String DATE_FORMAT_FOR_FILE_NAME = "MMddyyyyHHmmssSSS";
		public static final String FILE_NAME_INITAL = "submitOrderEdi";
		public static final String XML_FILE_NAME_EXTENSION = ".xml";
		public static final String SLASH = File.separator;
		public static final String UNDER_SCORE = "_";
		public static final String COLON = ":";
		public static final int SHIP_TO_NUMBER_START_INDEX = 99;
		public static final int SHIP_TO_NUMBER_END_INDEX = 112;
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
		public static final String MATERIAL_STRING = "<MATERIAL>";
		public static final String EXP_DELIVERY_DATE_FORMAT = "yyyy-MM-dd";
		public static final String DATA_ROW_STRING = "for datarow -";
		//CP-02 Changes :Komal Sehgal
		public static final String SAP_ORDER_HARD_ERROR_TYPE = "E";
		public static final String SAP_ORDER_HARD_ERROR_ID = "V1";
		//Global Code Changes
		public static final String KEY_STRING = "key";
		public static final String CONFIG_QUERY_FOR_KEY = " and {key}=?key";
		public static final String EXCLUDED_ORDER_TYPE = "cart.excluded.order.type";
		public static final String PRICE_OVERRIDE_THRESHOLD = "zdel.price.override.threshold";
		public static final String MAX_PRICE_OVERRIDE_THRESHOLD = "zdel.price.override.threshold.max";
		public static final String PRICE_OVERRIDE_WARNING_MESSAGE = "zdel.price.override.threshold.warning";
		public static final String PRICE_OVERRIDE_CONTRACTNUM_MESSAGE = "zdel.price.override.contractNum.message";
		public static final String PRICE_OVERRIDE_CONTRACTNUM_FLG = "zdel.price.override.contract.flag";
	}


	public interface UserSearch
	{
		public static final String PHONE = "phone";
		public static final String LIKE = "like";
		public static final String SECTOR = "sector";
		public static final String ROLE = "role";
		public static final String LASTNAME = "lastName";
		public static final String FIRSTNAME = "firstName";
		public static final String ACCOUNTNAME = "accountName";
		public static final String ACCOUNTNUMBER = "accountNumber";
		public static final String STATUS = "status";
		public static final String EMAIL = "email";
		public static final String USER_TIER_TYPE = "userTierType";
		public static final String USER_TIER2 = "tier2";
		public static final String USER_TIER1 = "tier1";
		public static final String DESIRED_GROUPS_TIER2 = "desired.groups.tier2";
		public static final String DESIRED_GROUPS_TIER1 = "desired.groups.tier1";
		public static final String GLOBAL_ACCOUNT_ID = "JnjGlobalUnit";
		public static final String B2B_UNIT_IDS = "b2bUnitIds";
		public static final String SEARCH_PHONE = "searchPhone";
		public static final String USERGROUPS = "usergroups";
		public static final String FIND_B2BCUSTOMERS_QUERY = "SELECT DISTINCT {customer:pk},{customer:name},{customer:status} from {JnjB2BCustomer As customer JOIN Address As address on  {address:owner}= {customer:pk}"
				+ "JOIN   PrincipalGroupRelation  AS b2bunitrelation ON {b2bunitrelation:source} = {customer:pk}"
				+ "JOIN   JnjB2BUnit 	AS b2bunit	ON {b2bunit:pk} = {b2bunitrelation:target}"
				+ "JOIN   PrincipalGroupRelation 	AS desiredgrouprelations	ON {desiredgrouprelations:source} = {customer:pk} "
				+ "JOIN   UserGroup 	AS desiredgroups    ON {desiredgroups:pk} = {desiredgrouprelations:target}}"
				+ "WHERE {desiredgroups:uid} IN (?usergroups) ";
		public static final String SEARCH_BY_PHONE = " and {address:contactAddress}=1 and  {address:phone1} LIKE ?searchPhone ";
		public static final String SEARCH_BY_LASTNAME = " and LOWER({customer:name}) LIKE ?searchLastName ";
		public static final String SEARCH_BY_FIRSTNAME = " and LOWER({customer:name}) LIKE ?searchFirstName ";
		public static final String SEARCH_BY_STATUS = " and {customer:status} IN ({{select {pk} from {CustomerStatus} where {code}=?searchStatus}}) ";
		public static final String SEARCH_BY_ROLE = " and {desiredgroups:uid} in(?searchRole)";
		public static final String SEARCH_BY_EMAIL = " and LOWER({customer:email}) Like ?searchByEmail";
		public static final String SEARCH_BY_ACCOUNTNUMER = " and {b2bunit:uid} Like?searchAccountNumber";
		public static final String SEARCH_BY_ACCOUNTNAME = " and LOWER({b2bunit:name}) Like?searchAccountName";
		public static final String SEARCH_BY_MDD = " and {mddSector}=1";
		public static final String SEARCH_BY_CONS = " and {consumerSector}=1";
		public static final String SEARCH_BY_PHARMA = " and {pharmaSector}=1";
		public static final String ORDER_BY_NAME = " ORDER BY {customer:name}";
		public static final String ORDER_BY_STATUS = " ORDER BY {customer:status}";
		public static final String ORDER_BY_ROLE = " ORDER BY {desiredgroups:locname}";
		public static final String LIKE_WILDCARD_CHARACTER = "%";
		public static final String SPACE = " ";
		public static final String HYPHEN = "-";
		public static final String SEARCH_BY_SELECTED_B2B_UNIT = "and {b2bunit:uid} IN (?b2bUnitIds)";
		public static final String SORT_USER_BY_NAME = "name";
		public static final String SORT_USER_BY_ROLE = "role";
		public static final String SORT_USER_BY_STATUS = "status";
		public static final String HELP_URL = "/help";
		public static final String PASSWORD_RESET_URL = "/login?passwordExpireToken=";
		public static final String PASSWORD_RESET_URL_QUERY_PARAM = "&email=";
		public static final String PASSWORDEXPIRY_EMAILADDRESS = "password.expiry.email";
		public static final String PENDING_PROFILE_STATUS = "Pending Profile";
		public static final String CUSTOMER_SERVICES_NUMBER = "customerServicesNumber";
		public static final String PASSWORD_CHANGE__SUCCESS_MESSAGE = "editUser.passwordExpireSuccessMessage";
		public static final String PASSWORD_CHANGE_FAILURE_MESSAGE = "editUser.passwordExpireFailureMessage";
		public static final String FAIL = "FAIL";
		public static final String RESET_PASSWORD_URL = "/passwordExpiredEmail?passwordExpireToken=";
	}

	public interface TemplateSearch
	{
		
		public static final String FIND_ORDER_TEMPLATE_B2BUNIT = "select {pk} from {JnjOrderTemplate as order} WHERE  {order:unit}=?unit and ({order:visibleto} =?userpk or {order:visibleto} =?unit )";


		public static final String FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR = "select {temp:pk} from {JnjOrderTemplate as  temp JOIN Jnjb2bcustomer as customer on {customer:pk} = {temp:author}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit )  order by {customer.NAME} ";
		public static final String FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS = "select {temp:pk} from {JnjOrderTemplate as temp JOIN ShareStatus as status on {status:pk} = {temp:sharestatus}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit )  order by  {status.CODE} ";

		public static final String SORT_TEMPLATES_BY_DATE = " ORDER BY {order:creationTime}";

		public static final String SORT_TEMPLATES_BY_NUMBER = " ORDER BY {order:templateNumber}";

		public static final String SORT_TEMPLATES_BY_NAME = " ORDER BY {order:name}";

		public static final String SORT_TEMPLATES_BY_DATE_PRODUCTCODE = " ORDER BY {order:creationTime}";

		public static final String SORT_TEMPLATES_BY_NUMBER_PRODUCTCODE = " ORDER BY {order:templateNumber}";

		public static final String SORT_TEMPLATES_BY_NAME_PRODUCTCODE = " ORDER BY {order:name}";

		public static final String SORT_TEMPLATES_BY_AUTOR = " order by {customer:name} ";

		public static final String SORT_TEMPLATES_BY_STATUS = " order by {status.CODE} ";

		public static final String SORT_TEMPLATES_BY_LINE = " order by LENGTH({order:entryList})";

		public static final String SORT_ORDER = "DESC";

		public static final String SEARCH_TEMPLATE_BY_NUMBER = "select distinct{pk},{order:name},{order:templateNumber},{order:entryList},{order:creationTime} from {JnjOrderTemplate  as order join JnjTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
				+ "} where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit) and ({entry:product}= ?searchText or {entry:referencedVariant}=?searchText)";

		public static final String SEARCH_TEMPLATE_BY_NAME = "select {pk} from {JnjOrderTemplate as order} where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit ) and  LOWER({order:name}) like ?searchText ";

		public static final String SEARCH_TEMPLATE_FOR_SORT_BY_AUTHOR = "select {temp:pk} from {JnjOrderTemplate as temp JOIN Jnjb2bcustomer as customer on {customer:pk} = {temp:author}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit ) ";

		public static final String SEARCH_TEMPLATE_FOR_SORT_BY_STATUS = "select {temp:pk} from {JnjOrderTemplate as temp JOIN ShareStatus as status on {status:pk} = {temp:sharestatus}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit ) ";

		public static final String SEARCH_TEMPLATE_BY_NAME_SORT_BY_AUTHOR = SEARCH_TEMPLATE_FOR_SORT_BY_AUTHOR
				+ " and {temp:name} like ?searchText  ";

		public static final String SEARCH_TEMPLATE_BY_NUMBER_SORT_BY_AUTHOR = SEARCH_TEMPLATE_FOR_SORT_BY_AUTHOR
				+ " and {temp:code} like ?searchText  ";

		public static final String SEARCH_TEMPLATE_BY_NAME_SORT_BY_STATUS = SEARCH_TEMPLATE_FOR_SORT_BY_STATUS
				+ " and {temp:name} like ?searchText  ";

		public static final String SEARCH_TEMPLATE_BY_NUMBER_SORT_BY_STATUS = SEARCH_TEMPLATE_FOR_SORT_BY_STATUS
				+ " and {temp:code} like ?searchText  ";

		public static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_AUTHOR = "select distinct{pk},{order:name}, {order:templateNumber},{order:entryList},{order:creationTime},{customer.NAME} from {JnjOrderTemplate  as order join JnjNAOrderTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
				+ " JOIN Jnjb2bcustomer as customer on {customer:pk} = {order:author}}"
				+ " where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit) and ({entry:product}= ?searchText or {entry:referencedVariant}=?searchText)";

		public static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_STATUS = "select distinct{pk},{order:name}, {order:templateNumber},{order:entryList},{order:creationTime},{status.CODE} from {JnjOrderTemplate  as order join JnjNAOrderTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
				+ " JOIN ShareStatus as status on {status:pk} = {order:sharestatus}}"
				+ " where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit) and ({entry:product}= ?searchText or {entry:referencedVariant}=?searchText)";

		public static final String SEARCH_TEMPLATE_DETAIL = "select {pk} from {JnjOrderTemplate} where {unit}=?unit  and ({visibleto} =?userpk or {visibleto} =?unit ) and  {code} like ?searchText ";
		//public static final String SEARCH_TEMPLATE_DETAIL = "select {pk} from {JnjOrderTemplate as temp JOIN ShareStatus as status on {status:pk} = {temp:sharestatus}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit )  and  {code} like ?searchText ";

		public static final String SORT_TEMPLATE_NAME_ASC = "SortTemplateNameAsc";
		public static final String SORT_TEMPLATE_NAME_DESC = "SortTemplateNameDesc";
		public static final String SORT_TEMPLATE_NUMBER_ASC = "SortTemplateNumberAsc";
		public static final String SORT_TEMPLATE_NUMBER_DESC = "SortTemplateNumberDesc";
		public static final String SORT_AUTHOR_ASC = "SortAuthorAsc";
		public static final String SORT_AUTHOR_DESC = "SortAuthorDesc";
		public static final String SORT_LINES_ASC = "SortLinesAsc";
		public static final String SORT_LINES_DESC = "SortLinesDesc";
		public static final String SORT_SHARE_STATUS_ASC = "SortShareStatusAsc";
		public static final String SORT_SHARE_STATUS_DESC = "SortShareStatusDesc";
		public static final String SORT_CREATION_DATE_DESC = "SortCreationDateDesc";
		public static final String SORT_CREATION_DATE_ASC = "SortCreationDateAsc";

		public static final String SEARCH = "Search";
		public static final String LIKE_CHARACTER = "%";
		public static final String PDF_FILE_NAME = "Template_Order_List";
		public static final String EXCEL_FILE_NAME = "Template_Order_List";
		public static final String GROUP_BY_DROP_DOWN_ID = "order.template.groupby";
		public static final String GROUP_BY_DETAIL_DROP_DOWN_ID = "order.template.detail.groupby";
		public static final String SITE_LOGO_IMAGE = "newSiteLogoImage1";
		public static final String SITE_LOGO_IMAGE_2 = "newSiteLogoImage2";
		public static final String SITE_LOGO_IMAGE_10 = "newSiteLogoImage10";
		public static final String SITE_LOGO = "siteLogoImage";
	}


	public interface HomePage
	{
                public static final String HARD_STOP = "hard.stop.error";
		public static final String INVALID_PRODUCT = "Invalid_Product";
		public static final String ONE_INVALID_PRODUCT = "One_Invalid_Product";
		public static final String ORDER_RETURN_DEDIRED_CLASS_OF_TRADE = "orderReturn.desired.classOfTrade";
		public static final String ORDER_RETURN_KEEP_ITEMS = "keepItems";
		public static final String ORDER_RETURN = "RETURNS";
		public static final String ORDER_QUOTE = "QUOTE";
		public static final String PRODUCT_INVALID_ERROR = "home.addtoCart.errorMessage";
		public static final String PRODUCT_ADDTOCART_SUCCESS = "SUCCESS";
		public static final String PRODUCT_INVALID_ERROR_QUNATITY = "home.addtoCart.quantity.errorMessage";
		public static final String PRODUCT_NOT_ADDED_ERROR = "home.addtoCart.quantity.notadded.errorMessage";
		public static final String CONTENT_TYPE_HTML = "HTML";
		public static final String CONTENT_TYPE_TEXT = "TEXT";
		public static final String BROADCAST_CHARACTERS = "broadCast.content.characters";

	}

	public interface PayMetrics
	{
		public static final String MY_GUID = "pay.metrics.myguid";
		public static final String XML_PAYLOAD_BEFORE_URL = "pay.metrics.xmlpayload.before.url";
		public static final String XML_PAYLOAD_AFTER_URL = "pay.metrics.xmlpayload.after.url";
		public static final String SECRET_KEY_SPEC = "pay.metrics.secretkeyspec.bytearray.value";
		public static final String SECRET_KEY_SPEC_ALGORITHM = "pay.metrics.secretkeyspec.algorithm.value";
		public static final String SIGN_PAYLOAD_MYGUID = "pay.metrics.signpayload.myguid";
		public static final String SIGN_PAYLOAD_SIGNATURE = "pay.metrics.signpayload.signature";
		public static final String SIGNATURE_TAG_END = "pay.metrics.signpayload.signature.end";
		public static final String MERCHANT = "pay.metrics.signpayload.merchant";
		public static final String PM_RESPONSE = "pay.metrics.pmresponse";
		public static final String SIGNATURE = "pay.metrics.signature";
		public static final String URL = "pay.metrics.url";
		public static final String REMAINING_URL = "pay.metrics.remaining.url.part";
	}

	public interface Surgeon
	{
		public static final String SURGEON_INBOUND_FILE_PREFIX_KEY = "jnj.na.inbound.surgeon.file.prefix";
		public static final String SURGEON_CSV_FILE_COUNTER_KEY = "surgeonCsvCounter";
		public static final String SURGEON_FILE_FOOTER_TEXT_KEY = "jnj.na.surgeon.footer.text";
	}

	public interface Product
	{
		public static final String DELIVERY_SALES_GTIN_UPDATE_TARGET_VERSION = "jnj.na.update.gtin.target.catalog.version";
		public static final String PRODUCT_GET_CONTRACT_PRICE_QUANTITY = "jnj.na.product.get.price.quantity";
		public static final String JNJ_UNIT_TYPE = "jnjGTUnit";
		public static final String CONSUMER_ACTIVE_PRODUCT_STATUS_KEY = "jnj.na.product.status.active";
		public static final String CONSUMER_INACTIVE_PRODUCT_STATUS_KEY = "jnj.na.product.status.inactive";
		public static final String SEARCH_RES_DOWNLOAD_FLAG = "isSearchResDownload";
		public static final String NON_REMAINING_PRO_CODE = "000000";
	}

	public interface Exclusions
	{
		public static final String US_PRD_EXCL_QUERY = "select {pk} from {JnjGTExProductAttribute}";
		public static final String PORTAL_INDC_NOT_SET_US_PRD_QUERY = "SELECT {pk} FROM {JnJProduct} where {productStatusCode} IN (?productStatusCode) and {catalogversion} = ?catalogversion and {pcmModStatus} Not IN ({{select {pk} from {JnjGTModStatus} where {code}=?code}}) order by {materialBaseNum}";
		public static final String PRODUCTS_HAVE_TEMP_PUBLISH_IND = "select {pk} from {JnJProduct} where {materialBaseNum} in ({{select {code} from {JnJProduct} where {materialBaseProduct} is null and {tempPublisInd}=0 and {catalogversion} = ?catalogversion}}) and {catalogversion} = ?catalogversion and {pcmInd}=1";
	}

	public interface EmailPreferences
	{
		public static final String WEBSITE_UPDATES = "emailPreference1";
		public static final String ORGANIZATION_CHANGES = "emailPreference2";
		public static final String PLACED_ORDER_SHIPS = "emailPreference3";
		public static final String ACCOUNT_ORDER_SHIPS = "emailPreference4";
		public static final String PLACED_ORDER_LINE_CUTS = "emailPreference5";
		public static final String ACCOUNT_ORDER_LINE_CUTS = "emailPreference6";
		public static final String BACKORDER_AVAILABILITY_DATE_AVAILABLE = "emailPreference7";
		public static final String OPENLINE_AND_BACKORDER_AVAILABILITY = "emailPreference8";
	}
	
	public interface Email
	{
		public static final String COMPREHENSIVE_ORDER_CONFIRMATION_FROM_EMAIL = "jnj.comprehensive.order.confirmation.fromEmail";
		public static final String COMPREHENSIVE_ORDER_CONFIRMATION_DISPLAY_NAME = "jnj.comprehensive.order.confirmation.displayName";
		public static final String COMPREHENSIVE_ORDER_CONFIRMATION_TO_EMAIL = "jnj.comprehensive.order.confirmation.toEmail";
		public static final String COMPREHENSIVE_ORDER_CONFIRMATION_TO_DISPLAY_NAME = "jnj.comprehensive.order.confirmation.toDisplayName";
	}
	
	public static String KEY_Valid_COUNTRIES = "jnj.valid.countries.isoCode";
	public static String KEY_Valid_CURRENCY = ".currency.isoCode";
	public static String RESTRICTED_CATEGORIES = "jnjUserRestrictedCategories";
	public static String CATALOG_VERSION = "catalogVersion";
	public static String CATALOG_ID = "catalogId";
	public static final String CONST_COMMA = ",";
	public static final String PERCENTAGE_SYMBOL = "%";
	public static final String CONST_DOT = ".";
	public static final String SYMBOL_COMMA = ",";
	public static final long DEFAULT_ADD_TO_CART_QTY = 1L;
	public static final String PRODUCT_CATALOG = "ProductCatalog";
	public static final String CATALOG_VERSION_ONLINE = "Online";
	public static final String FEED_FILEPATH_ROOT = "feed.filepath.root";
	public static final String FEED_FILE_NAME_PREFIX = ".prefix";
	public static final String FEED_FILEPATH_ROOT_TEMP = "feed.filepath.root.temp";
	public static final String FEED_FILEPATH_INCOMING = "feed.filepath.root.incoming";
	public static final String FEED_FILEPATH_OUTGOING = "feed.filepath.root.outgoing";
	public static final String FEED_RETRY_COUNT = "feeds.retry.count";
	public static final String ORDER_VALIDATION_LINES_COUNT = "order.validation.lines.count";
	public static final String CART_PAGE_ENTRIES_SIZE = "cartpage.entries.page.size";
	//public static final String HOT_FOLDER_ROOT_PATH = "jnjb2bcore.batch.impex.basefolder";
	public static final String INVOICE_OPEN_TEXT_URL = "invoice.open.text.url";
	public static final String INVOICE_OPEN_TEXT_HOST = "invoice.open.text.host";
	public static final String NUMBER_ONE = "1";
	public static final String UNDERSCORE_SYMBOL = "_";
	public static final String HASH_SYMBOL = "#";
	public static final String FIFTEEN_MINUTES_MILLIS = "900000";
	public static final String SAP_ORDER_TYPE_ZORD = "ZORD";
	public static final String SAP_ORDER_TYPE_ZOR = "ZOR";
	public static final String HOSPITAL = "hospital";
	public static final String FEEDS_ARCHIVE_FOLDER_NAME = "/archive";
	public static final String FEEDS_ERROR_FOLDER_NAME = "/error";
	public static final String REQ_PROD_SECOTRS_KEY = "required.product.sectors.mapping";
	public static final String VALID_CHEKOUT_ROLES_KEY = "valid.chekout.user.roles";
	public static final String VALID_EDI_SUBMIT_ORDER_ROLES_KEY = "valid.edi.submit.order.user.roles";
	public static final String EMPENHO_DOCS_PREFIX_KEY = "empenho.doc.prefix";
	public static final String MAX_QTY_LIMIT_EXCEED = "maxQtyLimitExceed";
	public static final String ADDTO_CART_ERROR = "Error";

	public static final String PHARMA_CATEGORY_CODE = "category.pharma.code";
	public static final String MDD_CATEGORY_CODE = "category.mdd.code";
	public static final String PHR_SECTOR = "PHR";
	public static final String MDD_SECTOR = "MDD";
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	public static final String SUBMIT_ORDER = "SubmitOrder";
	public static final String SIMULATE_ORDER = "SimulateOrder";
	public static final String CREATE_ORDER = "CreateOrder";
	public static final String GET_PRICE = "GetPrice";
	public static final String QUERY_NFE = "QueryNFe";
	public static final String GET_INVOICE = "GetInvoiceDocument";
	public static final String MILI_SECOND_FORMATTER = "YYYYMMddHHmmssSSS";
	public static final String SECOND_FORMATTER = "YYYYMMddHHmmss";
	//public static final String Y_STRING = "Y";
	public static final String SPACE = " ";
	public static final String HYPHEN = "-";
	public static final String IDOC_NUMBER = "DOCNUM";
	public static final String IDOC_PARENT_TAG = "IDOC";
	public static final String OTHER_GROUP_USER_ROLES = "usergroup.roles.otherordergroup";
	//CR 31439:User Management 
	public static final String BRAZIL_MASTER_B2BUNIT = "brazil.master.b2bunitid";
	public static final String MEXICO_MASTER_B2BUNIT = "mexico.master.b2bunitid";
	public static final String INT_RECORD_STATUS = "recordStatus";
	public static final String INT_RECORD_RETRY = "recordRetry";
	public static final String XSD_VALIDATION_ERROR_MESSAGE = "File is not valid against the XSD";
	public static final Integer MAX_WRITE_ATTEMPTS = Integer.valueOf(Config.getInt(Jnjb2bCoreConstants.FEED_RETRY_COUNT, 3));
	public static final int BATCH_SIZE = Config.getInt("rsa.products.batch.size", 1000);
	public static final String INTERFACE_NOTIFICATION_EMAIL_ID_KEY = "interface.notification.email";
	public static final String FEEDS_XSD_FOLDER_KEY = "feed.filepath.root.xsd";
	public static final String FEEDS_XSD_FILE_EXTENSION = "xsd";
	public static final String FEEDS_FORCE_VALIDATION_FLAG = "feedForceValidationFlag";
	public static final String XSD_VALIDAION_REQUIRED = "feed.xsd.validtion.required";
	public static final String CATALOG_VERSION_STAGED = "Staged";
	public static final int PASSWORD_EXPIRY_TIME_FRAME_IN_DAYS = 90;
	public static final String FEED_FILE_PURGE_DAYS = "feed.purge.days";
	public static final String FEED_FILEPATH_INCOMING_SAP_PURGE_PREFIX = "feed.filepath.root.incoming.sap.purge.prefix";
	public static final String FEED_FILEPATH_INCOMING_SAP_PURGE_FOLDERS = "feed.filepath.root.incoming.sap.purge.folders.name";
	public static final String FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_PREFIX = "feed.filepath.root.outgoing.hybris.purge.prefix";
	public static final String FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_FOLDERS = "feed.filepath.root.outgoing.hybris.purge.folders.name";
	public static final String FILEPATH_HYBRIS_LOG_PURGE_FOLDERS = "hybris.log.purge.folders.name";
	public static final String FILEPATH_JNJ_LOG_PURGE_FOLDER = "jnj.log.purge.folder.name";
	public static final String FEED_FILEPATH_FOLDER_NAME_PREFIX = "feed.filepath.root.incoming.sap.purge.folders.name.prefix";
	public static final String FILEPATH_JNJ_LOG_PURGE_FILE_PATTERN = "jnj.log.purge.file.pattern";
	//public static final String SITE_NAME = "JNJ_SITE_NAME";
	//public static final String MDD = "MDD";
	//public static final String CONS = "CONS";

	private Jnjb2bCoreConstants()
	{
		super();
		assert false;
	}

	public interface UpsertProduct
	{
		public static final String UPSERT_PRODUCT_E1MARAM = "E1MARAM";
		public static final String UPSERT_PRODUCT_CODE = "MATNR";
		public static final String UPSERT_PRODUCT_TYPE_CODE = "MTART";
		public static final String UPSERT_PRODUCT_BASE_UNIT = "MEINS";
		public static final String UPSERT_PRODUCT_EAN = "EAN11";
		public static final String UPSERT_PRODUCT_ORIGIN_COUNTRY = "HERKL";
		public static final String UPSERT_PRODUCT_SECTOR = "ZZSECTOR";
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
		public static final String CATALOG_VER_STAGED = "Staged";
		public static final String BRAZIL_PRODUCT_CATALOG = "brProductCatalog";
		public static final String MEXICO_PRODUCT_CATALOG = "mxProductCatalog";
		public static final String SYNC_CRON_JOB_BR = "Sync-MasterStaged-BrazilStaged";
		public static final String SYNC_CRON_JOB_MX = "Sync-MasterStaged-MexicoStaged";
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
	}


	public interface UpsertCustomer
	{
		public static final String UPSERT_CUSTOMER_E1KNA1M = "E1KNA1M";
		public static final String UPSERT_CUSTOMER_ACCOUNT_TYPE = "KTOKD";
		public static final String UPSERT_CUSTOMER_CENTRAL_ORDER_BLOCK = "AUFSD";
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
		public static final String INDIRECT_CUSTOMER_QUERY = "select {pk} from {JnjIndirectCustomer} where {country}=?country";
		public static final String INDIRECT_CUSTOMER_QUERY_FOR_ID_COUNTRY = "select {pk} from {JnjIndirectCustomer} where {country}=?country and {indirectCustomer}=?indirectCustomer";
		public static final String COUNTRY_STRING = "country";
		public static final String INDIRECT_CUSTOMER_STRING = "indirectCustomer";
		//CR 31439:User Management 
		public static final String OTHER_GROUP_USER_ROLES = "usergroup.roles.otherordergroup";
		public static final String BRAZIL_MASTER_B2BUNIT = "brazil.master.b2bunitid";
		public static final String MEXICO_MASTER_B2BUNIT = "mexico.master.b2bunitid";
		public static final String KEY_ACCOUNT_HOSPITAL_INDICATOR_STRING = "keyHospital";
		public static final String KEY_ACCOUNT_DISTRIBUTOR_INDICATOR_STRING = "keyDistributor";
		public static final String STRING_HOSPITAL = "hospital";
		public static final String STRING_DISTRIBUTOR = "distributor";
		public static final String MEXICO_COUNTRY_CODE = "jnj.customer.MX";
		public static final String BRAZIL_COUNTRY_CODE = "jnj.customer.BR";
		public static final String COUNTRIES_STRING = "countries";
		public static final String COUNTRY_PK_STRING = "pk";
		public static final String ISOCODE_STRING = "isocode";
		

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
		public static final String DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
		public static final String DECIMAL_POINT = ".";
		public static final String DECIMAL_SPLIT_EXPRESSION = "\\.";
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
	}

	/**
	 * Constants added for the Order
	 * 
	 * @author sumit.y.kumar
	 * 
	 */
	/*public interface Order
	{
		public static final String ORDER_STATUS = "PENDING";
		public static final String CODE_STRING = "code";
		public static final String ID_STRING = "id";
		public static final String SUBMIT_ORDER_QUERY = "select {pk} from {order} where {status} in ({{ select {pk} from {orderstatus} where {code} = ?code }}) and {sapOrderNumber} is null and {orderType} IN ({{select {pk} from {JnjOrderTypesEnum} where {code} not in (?excludedOrderType)}}) and {site} in ({{select {pk} from {CMSSite} where {uid} in ('mddNAEpicCMSite','consNAEpicCMSite') }})";
		public static final String ORDER_STATUS_QUERY = "select {pk} from {JnjOrdStsMapping} where {overAllStatus}  = ?overAllStatus and {rejectionStatus}  = ?rejectionStatus and {creditStatus}  = ?creditStatus and {deliveryStatus}  = ?deliveryStatus and {invoiceStatus}  = ?invoiceStatus";
		public static final String ORDER_ENTRY_STATUS_QUERY = "select {pk} from {JnjOrdEntStsMapping} where {overAllStatus}  = ?overAllStatus and {rejectionStatus}  = ?rejectionStatus and {deliveryStatus}  = ?deliveryStatus and {invoiceStatus}  = ?invoiceStatus";
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
		public static final String PURCHASE_ORDER = "PurchaseOrder";
		public static final String HEADER = "Header";
		public static final String LINE = "Line";
		public static final String ID = "Id";
		public static final String CUSTOMER_ITEM_ID = "CustomerItemId";
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
		public static final String ZERO_THREE_STRING = "03";
		public static final String PO_DATE_FORMAT = "MMddyyyy";
		public static final String DATE_FORMAT_FOR_FILE_NAME = "MMddyyyyHHmmssSSS";
		public static final String FILE_NAME_INITAL = "submitOrderEdi";
		public static final String XML_FILE_NAME_EXTENSION = ".xml";
		public static final String SLASH = File.separator;
		public static final String UNDER_SCORE = "_";
		public static final String COLON = ":";
		public static final int SHIP_TO_NUMBER_START_INDEX = 99;
		public static final int SHIP_TO_NUMBER_END_INDEX = 112;
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
		public static final String MATERIAL_STRING = "<MATERIAL>";
		public static final String EXP_DELIVERY_DATE_FORMAT = "yyyy-MM-dd";
		public static final String DATA_ROW_STRING = "for datarow -";
		//CP-02 Changes :Komal Sehgal
		public static final String SAP_ORDER_HARD_ERROR_TYPE = "E";
		public static final String SAP_ORDER_HARD_ERROR_ID = "V1";
		//Global Code Changes
		public static final String KEY_STRING = "key";
		public static final String CONFIG_QUERY_FOR_KEY = " and {key}=?key";
		public static final String EXCLUDED_ORDER_TYPE = "cart.excluded.order.type";
	}*/


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

		public static final String NAGS_MBOX_SFTP_DESTDIRECTORY = "nags.mbox.sftpdestdirectory";
		public static final String SFTP_PORT_SUBMIT_EDI = "jnj.sftp.submitedi.port";
		public static final String SFTP_PASSWORD_SUBMIT_EDI = "jnj.sftp.submitedi.password";
		public static final String NAGS_MBOX_SFTP_HOSTNAME = "nags.mbox.sftphostname";
		public static final String NAGS_MBOX_SFTP_USERNAME = "nags.mbox.sftpusername";
		public static final String NAGS_MBOX_SFTP_PASSWORD = "nags.mbox.sftppassword";
		public static final String NAGS_MBOX_SFTP_PORT = "nags.mbox.sftpport";

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
		public static final String GET_CONTRACT_BY_B2BUNIT = "select {pk} from {JnjContract} where {pk} in ("
				+ "{{ select {contract:pk} from {JnjContract as contract join JnJB2bUnitToContractRel as rel on {contract:pk} ={rel:target} join JnJB2BUnit as bu on {rel:source} = {bu:pk} } "
				+ "where {bu:pk}=?selectedB2BUnit }})";				
		public static final String SORT_CONTRACT_BY_NUMBER = " ORDER BY {JnjContract.eCCContractNum} ";
		public static final String SORT_CONTRACT_BY_CREATIONDATE = " ORDER BY {JnjContract.CREATIONTIME} ";
		public static final String SORT_CONTRACT_BY_INDIRECT_CUSTOMER = " ORDER BY {JnjContract.INDIRECTCUSTOMER} ";
		public static final String SORT_CONTRACT_BY_EXPIRE_ENDDATE = " ORDER BY {JnjContract.ENDDATE} ";
		public static final String SORT_ORDER = "DESC";
		public static final String FIND_BY_CONTRACT_NUMBER = " and LOWER({eCCContractNum}) like ?searchByCriteria";
		public static final String FIND_BY_TENDER_NUMBER = " and LOWER({tenderNum}) like ?searchByCriteria";
		public static final String FIND_BY_INDIRECT_NUMBER = " and LOWER({indirectCustomer}) like ?searchByCriteria";
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
		public static final String FIND_BY_ALL = " and (LOWER({eCCContractNum}) like ?searchByCriteria or LOWER({tenderNum}) like ?searchByCriteria or LOWER({indirectCustomer}) like ?searchByCriteria)";
		public static final String ACTIVE = "1";
		public static final String INACTIVE = "0";
		public static final String FILTER_BY_STATUS = " and {active}=?filterCriteria2";
		
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
	}

	public interface solrConfig
	{
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
		public static final String LOAD_CONTRACT_DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
		public static final String LOAD_CONTRACT_PRICE_CURRENCY_ISO_CODE = "USD";
		public static final String LOAD_CONTRACT_CONTRACT_STATUS = "Completed";
		public static final String LOAD_CONTRACT_FOLDER_KEY = "loadContracts.folder.name";
		public static final String LOAD_CONTRACT_ORDER_REASON_1 = "Z19";
		public static final String LOAD_CONTRACT_ORDER_REASON_2 = "Z20";
		public static final String LOAD_CONTRACT_ORDER_REASON_3 = "Z26";
		public static final String LOAD_CONTRACT_IMT_RECORD_STATUS = "recordStatus";
		public static final String LOAD_CONTRACT_IMT_RECORD_RETRY = "recordRetry";
		public static final String LOAD_CONTRACT_IMT_DASHBOARD_SENT = "sentToDashboard";
		public static final String LOAD_CONTRACT_STATUS_A = "A";
		public static final String LOAD_CONTRACT_STATUS_B = "B";
		public static final String LOAD_CONTRACT_STATUS_C = "C";

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
		public static final String DATE_FORMAT = Config.getParameter(PORTAL_DATE_FMT_DFLT);
		public static final String SEARCH_TERM = "searchTerm";
	}


	public interface UploadFile
	{
		public static final String SHARED_FOLDER_LOCATION_ORDER = "jnj.shared.order.folder";
		public static final String SFTP_SOURCE_ORDER_FOLDER = "jnj.sftp.source.order.folder";
		public static final String SFTP_DESTINATION_SUBMIT_ORDER_EDI_FOLDER = "jnj.sftp.destination.submitedi.folder";
		public static final String SFTP_DESTINATION_EMPHENO_FOLDER = "jnj.sftp.destination.empenho.folder";
		public static final String FEED_FILEPATH_EMPENHO_DOC_ERROR = "jnj.shared.outgoing.empenhodoc.error.folder";
		public static final String SFTP_CONNECTION__TYPE_EMPHENO = "empenho";
		public static final String SFTP_CONNECTION__TYPE_SUBMITEDI = "submitEdI";
		public static final String SFTP_CONNECTION__TYPE_SELLOUT_MDD = "selloutMdd";
		public static final String SFTP_CONNECTION__TYPE_SELLOUT_PHARMA = "selloutPharma";

		public static final String SFTP_CONNECTION_TYPE_NAGS_FILE_MBOX = "nagsFileMbox";
		public static final String SFTP_CONNECTION_TYPE_EMPHENO = "empenho";
		public static final String SFTP_CONNECTION_TYPE_SUBMITEDI = "submitEdI";
		public static final String SFTP_CONNECTION_TYPE_SELLOUT_MDD = "selloutMdd";
		public static final String SFTP_CONNECTION_TYPE_SELLOUT_PHARMA = "selloutPharma";



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
		//public static final String REGISTRATION_ERROR = "registationError";
		//public static final String REGISTRATION_ERROR_MSG = "This email id is already registered";
		public static final String COUNTRY_CODE_BR = "BR";
		public static final String DEF_B2B_UNIT_BR = "JnJMasterBR";
		public static final String DEF_B2B_UNIT_MX = "JnJMasterMX";
		public static final String REGISTER_LABEL = "register";
		public static final String REGISTER_EMAIL_FROM = "registerEmailFrom";
		public static final String REGISTER_EMAIL_TO = "register.email.to.";
		public static final String REGISTER_EMAIL_DISPLAY_NAME = "registerEmailFromDisplayName";
		public static final String EMAIL_PREFERENCE = "emailPreference";
		public static final String BUSINESS_TYPE = "businessType";
		public static final String ESTIMATED_AMOUNT = "estimatedAmount";
		public static final String PURCHASE_PRODUCT = "purchaseProduct";
		public static final String COMMA_SEPARATOR = ",";
		public static final String SECURITY_QUESTIONS = "securityQues";
		public static final String DIVISIONS = "divisions";
		public static final String TERRITORIES = "territories";
		public static final String DEPARTMENTS = "departments";
		public static final String CONSUMER_DIVISONS = "consumerDivisons";
		public static final String VIEW_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewOnlyGroup";
		public static final String PLACE_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderGroup";
		public static final String VIEW_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewOnlySalesGroup";
		public static final String PLACE_ORDER_SALES_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderSalesGroup";
		public static final String USER_GROUP_NO_CHARGE_USERS = "user.groups.noChargeGroup";
		public static final String REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS = "user.registration.csr.email";
		// jjepic-601
		public static final String REGISTRATION_FROM_EMAIL_ADDRESS = "user.registration.from.address";
		public static final String REGISTER = "register";
		public static final String USER_ACTIVATION = "activation";
		public static final String REGISTRATION_ERROR = "registationError";
		public static final String REGISTRATION_ERROR_MSG = "This email id is already registered";
		public static final String REGISTRATION_GLOBAL_MSG = "Something went wrong while registering. Kindly try again later.";
		public static final String REGISTRATION_EMAIL_DEPUY_SPINE_EMAIL_ADDRESS = "registration.depuy.spine.email";
		public static final String REGISTRATION_EMAIL_DEPUY_MITEK_EMAIL_ADDRESS = "registration.depuy.mitek.email";
		public static final String USER_ACTIVATION_PAGE = "userActivationPage";
		public static final String REGISTER_SUCCESS = "registerSuccessPage";
		public static final String ACTIVATION_SUCCESS = "activationSuccessPage";
		public static final String REASON_CODE = "reasonCode";


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
		public static final String CMSSITE = "CMSite";

	}


	public interface Logging
	{
		public static final String LOAD_TRANSLATION = "Load Translation Interface";
		public static final String UPSERT_PRODUCT_NAME = "UpsertProducts Interface";
		public static final String UPSERT_CUSTOMER_NAME = "UpsertCustomer Interface";
		public static final String SOLR_SEARCH = "Solr Search";
		public static final String SALES_ORG_CUSTOMER_DATA_LOAD = "SalesOrg Customer  Interface";
		//public static final String BEGIN_OF_METHOD = "Begin of method";
		//public static final String END_OF_METHOD = "End of method";
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
		public static final String CONTRACT_GET_CONTRACTS_PRODUCT = "getContractProduct()";
		public static final String CONTRACT_REMOVE_NON_CONTRACTS_PRODUCT = "removeNonContractProduct()";
		public static final String CONTRACTS_LIST = "downloadContractsList()";
		public static final String CONTRACTS_DETAIL_LIST = "downloadDetailData()";
		public static final String CONTRACTS_DAO = "My Contracts Dao";
		public static final String CONTRACTS_SERVICE = "Contracts Service";
		public static final String SALES_ORG_CUSTOMER_SERVICE = "Sales Org Customer Service";
		public static final String PRODUCT_SERVICE = "My Product Service";
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
		public static final String USER_LOGIN_JOB_METHOD_NAME = "perform() method of Cron Job For User Login Job";
		public static final String FEED_CONTRACTS_JOB__METHOD_EXCEPTION = "Exception occured inside perform() method of Cron Job";
		public static final String LOAD_CONTRACTS_INTERFACE = "Load Contract Interface";
		public static final String SFTP_UTIL = "Sft_Util";
		public static final String SFTP_UTIL_METHOD = "uploadFileToSftp()";
		public static final String SFTP_CREATE_DEFAULT_OPTION_METHOD = "createDefaultOptions()";
		public static final String MY_TEMPLATE = "myTemplate";
		public static final String MY_TEMPLATE_FACADE = "myTemplateFacade";
		public static final String SHOW_TEMPLATE = "showTemplate()";
		public static final String TEMPLATE_ADD_TO_CART = "saveTemplateByAddToCart()";
		public static final String GET_ORDER_TEMPLATE_DETAIL = "getTemplateOrderDetails()";
		public static final String GET_DELETE_TEMPLATE_PAGE = "getDeleteTemplatePage()";
		public static final String DELETE_TEMPLATE = "deleteTemplate()";
		public static final String ADD_TO_CART_BY_TEMPLATE = "addTemplateToCart()";
		public static final String DOWNLOAD_DATA = "downloadData()";
		public static final String GET_TEMPLATE_FOR_B2BUNIT = "getTemplatesForB2BUnit()";
		public static final String TEMPLATE_CONVERTER = "templateConverter()";
		public static final String SEARCH_ORDER_TEMPLATE = "searchOrderTemplate()";
		public static final String GET_TEMPLATE_FOR_CODE = "getTemplateForCode()";
		public static final String GET_PAGED_TEMPLATES = "getPagedOrderTemplates()";
		public static final String QUERY_NFE = "Electronic Nota Fiscal";
		public static final String INVOICE_SERVICE = "Invoice Service";
		public static final String ORDER_HISTORY_INVOICE_DETAILS = "My Account - Order History - Inovoice Details";
		public static final String INVOICE_ORDER_POPULATOR = "Invoice Order Data Populator";
		public static final String INVOICE_ENTRY_DATA_POPULATOR = "Invoice Entry Data Populator";
		public static final String METHOD_POPULATE = "populate()";
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
		public static final String EMAIL_NOTIFICATION_PROCESS = "Email Notification Process";
		public static final String LAUDO = "Laudo";
		public static final String LOAD_CUSTOMER_ELIGIBILITY_INTERFACE = "Customer Eligibility Interface";
		public static final String JNJ_ZIP_FILE_UTIL = "JnjZipFileUtil";
		public static final String IMPEX_IMPORT = "Impex-Import";
		public static final String INTERFACE_OPERATION_ARCHITECTURE_UTIL = "Interface Operation Architecture Utility";
		public static final String INTERFACE_LOAD_CONTRACT = "Load Contract Interface";
		public static final String INTERCEPTOR_PREPARE_JNJPARAGRAPHCOMPONENT = "JnjParagraphComponent Prepare Interceptor";
		public static final String JNJ_USER_LOGIN_JOB = "JnjUserLoginJob";
		public static final String SENT_ACCOUNT_ACIVE_EMAIL = "sendAccountActiveEmail";
		public static final String INTIALIZE_CREATE_USER_EVENT = "initializeCreateuserEvent";
		public static final String GET_USER_ROLES_FOR_APPROVAL_EMAIL = "getUserRolesForApproverEmail";
		public static final String LEGACY_POLICY_CHECK = "Legacy/Policy Check";
		public static final String REGISTRAION = "Registration";
		public static final String USER_MANAGEMENT = "userManagementController";
		public static final String USER_APPROVER_EMAIL = "sentApprovedProfileEmail()";
		public static final String CREATE_USER_EMAIL = "sentCreateProfileEmail()";
		public static final String TEMPORARY_PWD_EMAIL = "sentTemporaryPwdEmail()";
		public static final String VERIFY_PASSWORD_TOKEN = "verifyPasswordToken()";
		public static final String USER_REJECTION_EMAIL = "userRejectionEmail()";
		public static final String FILE_PURGING_SAP = "File Purging for SAP folders - ";
		public final static String CRON_JOB_FILE_PURGE = "Cron Job For File Purging";
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		//public static final String HYPHEN = "-";
		//public static final String START_TIME = "startTime:";
		//public static final String END_TIME = "endTime:";
		public static final String CUSTOMER_MASTER_FEED = "Customer Master Feed";
		public static final String HOME_PAGE = "Home Page";
		public static final String LOGIN = "Login";
		public static final String PRIVACY_POLICY_CHECK = "Privacy Policy Check.";
		public static final String ACCOUNTS = "Accounts";
		public static final String PASSWORD_EXPIRY_NAME = "Password Expiry";
		public static final String VALIDATE_CART = "Validate Cart";
		public static final String SURVEY = "Survey";
		public static final String CHANGE_PASSWORD = "Changepassword";
		public static final String REGISTRATION_EMAIL = "Registration Email";
		public static final String PERSONAL_INFORMATION = "Edit Personal Inforation";
		public static final String UPDATE_EMAIL_PREFRENCES = "Update Email Prefrences";
		public static final String ADD_ACCOUNT_EMAIL = "Add Account Email";
		public static final String CHANGE_SECURITY_Question = "Change Security Questions";
		public static final String Get_SECURITY_Question = "Get Security Questions";
		public static final String VERIFY_SECURITY_QUESTION = "Verify Security Questions";
		public static final String SECRET_QUESTION = "Verify Security Questions";
		public static final String UPDATE_PASSWORD = "Updatepassword";
		public static final String EMAIL_PREFERENCES = "EmailPreferences";
		public static final String ADD_ACCOUNT = "Addaccount";
		public static final String ADD_NEW_ACCOUNT = "Addnewaccount";
		public static final String ADD_EXISTING_ACCOUNT = "Addexistingaccount";
		public static final String CREATE_BREADCRUMB = "Createbreadcrumb";
		public static final String ADD_EXISTING_ACCOUNT_EMAIL = "Add Account Email";
		public static final String REPORTS_NAME = "Reports";
		public static final String DATE_UTIL = "Date Util";
		public static final String ORDER_HISTORY_UPDATE_PO = "Update Purchase Order Number";
		public static final String USER_MAMANGEMENT_SEARCH_FACADE = "searchCustomers()";
		public static final String USER_MAMANGEMENT_SEARCH_SERVICE = "searchCustomers()";
		public static final String APPROVED_USER_EMAIL = "ApprovedUserEmail";
		public static final String REJECT_USER_EMAIL = "rejectUserEmail()";
		public static final String PASSWORD_EXPIRY_EMAIL = "password expiry";
		public static final String WARNING_EMAIL = "Warning Email";
		public static final String METHOD_SEND_EMAIL_NOTIFICATION = "Method Send Email Notification";
		public static final String HOME_ORDER_STATUS = "homeOrderStatus";
		public static final String HOME_QUICK_CART = "homeAddQucikToCart";
		public static final String HOME_MULTIPLE_CART = "homeMultipleCart";
		public static final String HOME_START_RETURN = "homeStartReturn";
		public static final String HOME_START_RETURN_POST = "homeStartReturnPost";
		public static final String HOME_START_NEW_ORDER = "homeNewOrder";
		public static final String HOME_START_NEW_ORDER_POST = "homeNewOrderPost";
		public static final String HOME_START_NEW_QUOTE_POST = "homeNewQuote";
		public static final String HOME_START_NEW_QUOTE = "homeNewQuotePost";
		public static final String SAVE_CREDIT_CARD_INFO = "Save Credit Card Info";
		public static final String INITIATE_REPLENISH_ORDER = "Initiate Replenish Order";
		public static final String ORDER_CONFIRMATION_EMAIL = "CartOrderConfirmation";
		public static final String HOME_VIEW_TEMPLATE_DETAIL = "Home Template Details";
		public static final String HOME_GET_BROADCAST_CONTENT = "Home BroadCast Content";
		public static final String GET_CONTRACT_PRICE = "Get Contract Price";
		public static final String USER_PASSWORD_EXPIRY_EMAIL = "Password expiry email / Reset Password Link email.";
		public static final String RENAME_SURGEON_INBOUND_FILES = "Rename Surgeon Inbound Files";
		public static final String PROCESS_SURGEON_INT_RECORDS = "Process Surgeon Intermediate Records";
		public static final String RENAME_ORDER_STATUS_INBOUND_FILES = "Rename Order Status Inbound Files";
		public static final String PROCESS_ORDER_STATUS_INT_RECORDS = "Process Order status Intermediate Records";
		public static final String RENAME_SHIP_TRACK_INBOUND_FILES = "Rename Ship & Tracking Inbound Files";
		public static final String PROCESS_SHIP_TRACK_INT_RECORDS = "Process Ship & Tracking  Intermediate Records";
		public static final String PRODUCT_DETAILS_EMAIL = "Product Details Email";
		public static final String CA_PCM_PROD_EXCLUSION = "Canada Pcm Product Exclusion";
		public static final String US_PCM_PROD_EXCLUSION = "US Pcm Product Exclusion";
		public static final String CUSTOMER_ALIGNMENT = "Customer Alignment";
		public static final String CMOD_RGA_CALL = "Cmod Rga Call";
		public static final String RESET_PASSWORD_CRONJOB = "Reset Password Details Job";
		public static final String RESET_PASSWORD_DETAILS = "Reset Password Toke and Url";
		public static final String EXPORT_CATALOG_EMAIL = "Export Catalog Email";
		public static final String ORDER_SHIPMENT_EMAIL_NOTIFICATION = "Order Shipment Email Notification Job";
		public static final String BACKORDER_EMAIL_NOTIFICATION = "Backorder Email Notification Job";
        public static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
        public static final String SHIPPEDORDER_EMAIL_NOTIFICATION = "Shippedorder Email Notification Job";
      	public static final String PASSWORD_EXPIRY = "Password Expiry";
      	public static final String ADDTOCARTCONT = "JnjGTAddToCartController";
      	public static final String ADDTOCARTCONT_REMOVEITEMINCART = "removeCartItem()";
        public static final String ORDER_CANCEL_EMAIL = "sentOrderCancelEmail()";
	}

	/*public interface Login
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
	}*/

	public interface SynchronizeOrder
	{
		public static final String KEY_FOR_SAP_ORDER_COUNTER = "counterSAPOrder";
		public static final String KEY_FOR_SAP_ORDER_LINE_COUNTER = "counterSAPOrderLine";
		public static final String KEY_FOR_SAP_SCHEDULE_LINE_COUNTER = "counterSAPScheduleLine";
		public static final String KEY_FOR_SAP_ORDER_FILE_NAME = "syncOrder.sapOrder.fileName";
		public static final String KEY_FOR_SAP_ORDER_LINE_FILE_NAME = "syncOrder.sapOrderEntry.fileName";
		public static final String KEY_FOR_SAP_SCHEDULE_LINE_FILE_NAME = "syncOrder.sapScheduleLine.fileName";
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
		public static final String CATALOG_RESULT_PAGE_LIMIT = "catalog.result.page.limit"; //AFFG-20804

	}

	public interface MyAccount
	{
		public static final String SIDE_SLOT_COMPONENT_ID = "SideContentSlot";
		public static final String REPORT_LINK_COMPONENT_ID = "ReportsLinkComponent";
		public static final String MY_CONTRACT_LINK_COMPONENT_ID = "MyContractsLinkComponent";
		public static final String USERMANAGEMENT_LINK_COMPONENT_ID = "UserManagmentLinkComponent";
		public static final String REPORT_LINK_VISIBLE = "report";
		public static final String USERMANAGEMENT_LINK_VISIBLE = "usermanagement";
		public static final String MY_CONTRACT_LINK_VISIBLE = "contract";
		public static final String CONTRACT_KEY="CONTRACT";
		public static final String NON_CONTRACT_KEY="NONCONTRACT";
		

	}


	public interface UserCreation
	{
		public static final String ALPHA_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		public static final String NUMBER_SET = "0123456789";
		public static final int LENGTH_OF_PASSWORD = 8;
		public static final String HOST_NAME = "email.logo.hostname";
		public static final String B2B_CUSTOMER_ID = "b2bcustomergroup";

	}

	public interface URL
	{
		public static final String CREATE_WEBSERVICE_ORDER_URL = "jnj.webservice.create.order.endpoint.url";
		
		
	}
	public interface ClassOfTrade
	{
		public static final String DENTAL_PRICE="3500";
		public static final String OTHERS="20000";
	}
		
	public static final String BASE_DOMAIN = "baseDomain";
	public static final String BASESITE_URL = "baseSite.http";
	public static final String SECURE_BASESITE_URL = "baseSite.https";
	public static final String MULTIPLE = "Multiple";
	public static final String RECAPTCHA_PUBLIC_KEY = "recaptcha.publickey";
	public static final String RECAPTCHA_PRIVATE_KEY = "recaptcha.privatekey";
	public static final String RECAPTCHA_LANGUAGE = "repcatcha.language.code";
	
	public interface Dropshipment{
		public static final String NO_SPLIT_LIST = "no.split.list";
		public static final String NOT_FOUND_ERROR = "dropshipment.error.not.found";
		public static final String IS_NON_GLOBAL = "is.non.global";

	}
	
	//Changes for Bonus Item
	public interface BonusItem{
		public static final String INC_MAT_ID = "inc.mat.id";
		public static final String EXC_MAT_ID = "exc.mat.id";
		public static final String MIN_QLFY_QTY = "min.qlfy.qty";
		public static final String BONUS_ITEM_QTY = "bonus.item.qty";
		public static final String CHRGD_ITEM_CATEGORY = "ZTAN";
		public static final String FREE_ITEM_CATEGORY = "ZFNT";


	}
	
	//Changes for Serialization
	public interface Serialization{
		public static final String GTIN_PREFIX = "(01)";
		public static final String SERIAL_PREFIX = "(21)";
		public static final String BATCH_PREFIX = "(10)";
		public static final String DATE_PREFIX = "(17)";
		public static final String DEFAULT_DAY = "05";
		public static final String APP_ID = "jnj.serial.app.id";
		public static final String KNOWN = "Known";
	}
	//Soumitra-AAOL-3784: adding for Consignment charge Batch and serial no. given that we do not have the details of how these will be fetched from SAP.
	//WARNING - should be removed when the SAP integration is done.
	public interface ConsignmentCharge
	{
		public static final List<String> BATCH_NO = Arrays.asList("BN1039","BN1239","BN1967");
		public static final List<String> SERIAL_NO = Arrays.asList("SN6948","SN5676","SN4126");
	}
	
	//Added for PDF files
	public interface OrderPdf{
		public static final String PO_NUMBER = "order.confirmation.ponumber"; 
		public static final String JJORDERNUMBER = "order.confirmation.jjordernumber"; 
		public static final String ORDERTYPE = "order.confirmation.ordertype"; 
		public static final String ADDRESS = "order.confirmation.address"; 
		public static final String SALES_FORBIDDEN = "order.confirmation.salesforbidden"; 
		public static final String ORDER = "order.confirmation.order"; 
		public static final String TOTAL_NET = "order.confirmation.total.net";
		public static final String TOTAL_FEES = "order.confirmation.total.fees"; 
		public static final String FREIGHT_FEES = "order.confirmation.total.freightfees"; 
		public static final String EXPEDITED_FEES = "order.confirmation.total.expeditedfees"; 
		public static final String HSA_PROMOTION = "order.confirmation.hsa.promotion"; 
		public static final String DROPSHIP_FEES = "order.confirmation.dropship.fees"; 
		public static final String MINIMUM_FEES = "order.confirmation.minimum.fees"; 
		public static final String TAXES = "order.confirmation.taxes"; 
		public static final String DISCOUNTS = "order.confirmation.discounts"; 
		public static final String TOTALGROSS = "order.confirmation.totalgross.price"; 
		public static final String ENTRY_NUMBER = "order.confirmation.entry.number"; 
		public static final String PRODUCT_NAME = "order.confirmation.product.name"; 
		public static final String PRODUCT_DESC = "order.confirmation.Product.Description"; 
		public static final String PRODUCT_QUANTITY = "order.confirmation.Product.Quantity"; 
		public static final String DELIVERY_DATE = "order.confirmation.Estimated.DeliveryDate"; 
		public static final String INDIRECT_CUST = "order.confirmation.indirectCustomer"; 
		public static final String ITEMPRICE = "order.confirmation.itemPrice"; 
		public static final String TOTAL = "order.confirmation.total";
		public static final String BATCH_NUM ="delivery.xls.batchNo";
		public static final String SERIAL_NUM ="delivery.xls.serialNo";
		public static final String UNIT_PRICE ="order.confirmation.pdf.unit.price";
		public static final String TOTAL_PRICE ="order.confirmation.pdf.total.price";
		public static final String NOTE="order.confirmation.pdf.note";
		public static final String CUSTOMER_PO_NUMBER = "order.confirmation.customerPoNumber"; 
		public static final String PO_DATE = "order.confirmation.podate"; 
		public static final String REQUIRED_DELIVERY_DATE ="order.confirmation.requiredDeliveryDate";
		public static final String STOCK_LOCATION_ACCOUNT ="order.confirmation.stockLocationAccount";
		public static final String END_USER ="order.confirmation.endUser";
		public static final String SHIPPING_INSTRUCTIONS ="order.confirmation.shippingInstructions";
		public static final String OVERALL_STATUS ="order.confirmation.overallStatus";
		public static final String SHIP_TO_ADDRESS = "order.confirmation.shipToAddress"; 
		public static final String BILL_TO_ADDRESS = "order.confirmation.billToAddress"; 
		public static final String LINE_NUMBER = "order.confirmation.lineNumber";
		public static final String PRODUCT = "order.confirmation.product"; 
		public static final String QUANTITY = "order.confirmation.quantity"; 
		public static final String UNIT = "order.confirmation.unit";
		public static final String DOWNLOAD_DATE = "order.confirmation.downloadDate"; 
		public static final String RETURN_CREATED_DATE = "order.confirmation.returnCreatedDate";
		public static final String CONSIGNMENT_FILLUP_ORDER = "cart.common.orderType.KB";
		public static final String CONSIGNMENT_RETURN_ORDER = "cart.common.orderType.KA";
		public static final String CONSIGNMENT_CHARGE_ORDER = "cart.common.orderType.KE";
		public static final String NOT_AVAILABLE = "cart.return.entries.notAvailable";
		public static final String CONSIGNMENT_NOTE_1="order.confirmation.pdf.consignment.note1";
		public static final String CONSIGNMENT_NOTE_2="order.confirmation.pdf.consignment.note2";
		public static final String ORDERCONFIRMATION   = "order.confirmation.pdf.order.confirmation";
		public static final String ORDERCONFIRMATIONSX   = "order.confirmation.pdf.order.confirmationSX";
		public static final String DOWNLOADDATE 		 = "order.confirmation.pdf.download.date";	
		public static final String CUSTOMERPOREF 		 = "order.confirmation.pdf.customer.poreference";	
		public static final String ORDERNO   		 	 = "order.confirmation.pdf.order.number";
		public static final String SHIPMETHOD          = "order.confirmation.pdf.shipping.method";
		public static final String SHIPADD         = "order.confirmation.pdf.shipname.address";
		public static final String BILLNAMEADD         = "order.confirmation.pdf.billname.address";
		public static final String PRODUCTCODE   		 = "order.confirmation.pdf.product.code";	
		public static final String GTINSHIPUNIT 		 = "order.confirmation.pdf.gtin.shipping.unit";	
		public static final String DESCRIPTION     	 = "order.confirmation.pdf.description";
		public static final String UNITMEASURE       	 = "order.confirmation.pdf.uom";	
		public static final String UNITPRICE      	 =	"order.confirmation.pdf.unit.price";
		public static final String REQUESTDELIVERYDATE    =  "odr.cfm.pdf.dDt";
		public static final String FOOTER_ONE           = "price.inquiry.pdf.footer_one";
		public static final String FOOTER_TWO           = "price.inquiry.pdf.footer_two";
		public static final String STANDARDORDERDELIVERY= "order.shipping.method";
		
		public static final String TEMPLATE_FOOTER_ONE  = "pdf.copyright.footerone";
		public static final String TEMPLATE_FOOTER_TWO  = "pdf.copyright.footer";
		public static final String DATEFORMAT = "date.dateformat";
		public static final String DOWNLOADDATEFORMAT = "downloadDate.dateformat";
		public static final String PDFFILENAME = "pdf.fileName";
		
		
		
	}
}

