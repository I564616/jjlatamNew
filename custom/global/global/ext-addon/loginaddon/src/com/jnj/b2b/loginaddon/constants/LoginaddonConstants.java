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
package com.jnj.b2b.loginaddon.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Loginaddon constants. You can add global constants for your extension into this class.
 */
public final class LoginaddonConstants extends GeneratedLoginaddonConstants
{
	public static final String EXTENSIONNAME = "loginaddon";
	public static final String CONS = "CONS";
	public static final String MDD_SITE_ID_KEY = "jnj.site.mddSite.id.key";
	public static final String MDD = "MDD";

	public static final String SITE_NAME = "JNJ_SITE_NAME";
	public static final String US = "US";
	public static final String UNDERSCORE_SYMBOL = "_";
	public static final String HASH_SYMBOL = "#";

	public static final String JNJ_GT_TARGET_URL = "jnjGTTargetUrl";
	public static final String CONS_SITE_URL_KEY = "jnj.na.cons.site.url";
	public static final String MDD_SITE_URL_KEY = "jnj.na.mdd.site.url";
	public static final String SYMBOL_COMMA = ",";
	public static final String SYMBOl_COMMA = ",";
	public static final String SHOW_CONTRACT_LINK= "sessionShowContractLink";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String CartPage = "addon:/cartandcheckoutaddon/cartPage";
	public static final String UPLOAD_ERROR="Uploaded Product template is empty. Please fill the template with at least one valid product and try again.";
	public static final String GT_CAPTCHA_VALIDATION_ERROR = "invalidCaptcha";

	public static final String MDD_SITE_ID = Config.getParameter(MDD_SITE_ID_KEY);
	private LoginaddonConstants()
	{
		//empty to avoid instantiating this constant class
		super();
		assert false;
	}

	public interface Login
	{
		public static final String LOGIN_ERROR_LIMIT = "jnj.login.warn.limit";
		public static final String USER_NOT_AUTHORIZED_KEY = "unauthorized";
		public static final String USER_NOT_AUTHORIZED_VALUE = "login.error.unauthorized";

		public static final String USER_GROUP_PLACE_ORDER = "user.groups.ordering.placeOrderGroup";
		public static final String USER_GROUP_VIEW_ONLY = "user.groups.ordering.viewOnlyGroup";
		public static final String USER_GROUP_NO_CHARGE_USERS = "user.groups.noChargeGroup";
		public static final String PLACE_ORDER_BUYER_USER_ROLE = "user.groups.ordering.viewAndPlaceOrderGroup";
		
		

		public static final String USER_GROUP_VIEW_ONLY_SALES_REP = "user.groups.ordering.viewOnlySalesGroup";
		public static final String USER_GROUP_PLACED_ORDER_SALES_REP = "user.groups.ordering.viewAndPlaceOrderSalesGroup";
		public static final String FIRST_LOGIN_CHECK = "isJustLoggedIn";

		public static final String USER_GROUP_ADMIN = "user.groups.adminGroup";
		public static final String USER_GROUP_CSR = "user.groups.csrGroup";
		public static final String ACCOUNT_IN_PROCESS = "login.error.account.inprocess";
		public static final String ORDERING_RIGHTS = "orderingRights";
		public static final String USER_TYPE = "userType";
		public static final String SALESREP_ORDERING_RIGHTS = "salesRepOrderingRights";
		public static final String FIRST_TIME_LOGIN = "firstTimeLogin";
		public static final String ACCOUNT_LIST = "accountList";
		public static final String DAYS_BEFORE_PASSWORD_EXPIRES_KEY = "storefront.password.expiry.days";
		public static final String DAYS_BEFORE_PASSWORD_EXPIRES_DATE_FORMAT = "MM/dd/yyyy";
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
		public static final String CUSTOMER_lOGIN_DISABLE_DAYS = "jnj.na.disableLogin.days";
		public static final String DAYS_BEFORE_SEND_WARNING_MAIL = "jnj.na.daysBeforeSEndingWarningMail.days";
		public static final String DATE = "DATE";
		public static final String SALES_REP_USER = "salesRepUser";
		public static final String NO_CHARGE_INTERNAL_USER = "noChargeInternalUser";
		public static final String HASH_CONSUMER_ID = "#####|Consumer";
		public static final String SESSION_EXPIRED_PARAM = "sessionExpired";

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
		
		public static final String PASSWORD_EXPIRED_FOR_USER ="passwordExpiredForUser";
		
		//Changes for Serialization
		public static final String USER_GROUP_SERIALIZATION = "user.groups.serialization";
		//New password reset route
		public static final String MULTI_MODE_PASSWORD_RESET_ROUTE ="gt.multimode.passwordreset.route.enable";
		public static final String PASSWORD_EMAIL_TOKEN_VERIFICATION="emailTokenVerification";
		public static final String PASSWORD_EMAIL_TOKEN_VERIFIED="tokenVerified";
		public static final String PASSWORD_RESET_EMAIL="email";
		public static final String PASSWORD_EMAIL_TOKEN="token";
		public static final String PASSWORD_RESET_ERROR_MESSAGE="errorMessage";
		public static final String PASSWORD_RESET_ERROR_CODE="errorCode";
		public static final String PASSWORD_EMAIL_TOKEN_OPTION="tokenOption";
		public static final String PASSWORD_EMAIL_RESET_FLAG="resetFlag";

	}

	public interface Logging
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
		public static final String USEFUL_LINKS_CHECK = "Useful Links Check.";
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
		public static final String FORGOT_PASSWORD_NAME = "Forgot Password";

	}

	public interface Register
	{
		public static final String EMAIL_PREFERENCE = "emailPreference";
		public static final String BUSINESS_TYPE = "businessType";
		public static final String ESTIMATED_AMOUNT = "estimatedAmount";
		public static final String PURCHASE_PRODUCT = "purchaseProduct";
		public static final String COMMA_SEPARATOR = ",";
		public static final String EMPTY_STRING = "";
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
		public static final String SSRF_CHECK_FAILED_INPUT = "ssrf.check.failed.input";
		public static final String REGISTRATION_GLOBAL_MSG = "Something went wrong while registering. Kindly try again later.";
		public static final String REGISTRATION_EMAIL_DEPUY_SPINE_EMAIL_ADDRESS = "registration.depuy.spine.email";
		public static final String REGISTRATION_EMAIL_DEPUY_MITEK_EMAIL_ADDRESS = "registration.depuy.mitek.email";
		public static final String USER_ACTIVATION_PAGE = "userActivationPage";
		public static final String REGISTER_SUCCESS = "registerSuccessPage";
		public static final String ACTIVATION_SUCCESS = "activationSuccessPage";
		public static final String REASON_CODE = "reasonCode";
		public static final String FIRST_RESET_PASSWORD = "firstTimeResetPasswordPage";

		public static final String RECAPTCHA_PRIVATE_KEY_PROPERTY = "recaptcha.privatekey.";
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

		public static final String COUNTRY_CODE_BR = "BR";
		public static final String DEF_B2B_UNIT_BR = "JnJMasterBR";
		public static final String DEF_B2B_UNIT_MX = "JnJMasterMX";
		public static final String REGISTER_LABEL = "register";
		public static final String REGISTER_EMAIL_FROM = "registerEmailFrom";
		public static final String REGISTER_EMAIL_TO = "register.email.to.";
		public static final String REGISTER_EMAIL_DISPLAY_NAME = "registerEmailFromDisplayName";


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
		public static final String ALLOWEDFRANCHISE = "allowedFranchise";
		public static final String ALLOWEDFRANCHISECONDITION = "allowedFranchiseCondition";
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
		public static final String ORDER_BY_NAME = " ORDER BY Upper({customer:name})";
		public static final String ORDER_BY_STATUS = " ORDER BY {customer:status}";
		public static final String ORDER_BY_ROLE = " ORDER BY {desiredgroups:locname}";
		public static final String LIKE_WILDCARD_CHARACTER = "%";
		public static final String SPACE = " ";
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
		public static final String PASSWORD_CHANGE_SUCCESS_MESSAGE = "editUser.passwordExpireSuccessMessage";
		public static final String PASSWORD_CHANGE_FAILURE_MESSAGE = "editUser.passwordExpireFailureMessage";
		public static final String FAIL = "FAIL";
	}
	
	public interface Order
	{
		public interface SortOption
		{
			public static final String DEFAULT_SORT_CODE = "Order Date - Newest to Oldest";
			public static final String ORDER_DATE_OLDEST_TO_NEWEST = "Order Date - oldest to newest";
			public static final String ORDER_NUMBER_INCREASING = "Order Number - increasing";
			public static final String ORDER_NUMBER_DECREASING = "Order Number - decreasing";
			public static final String ORDER_TOTAL_INCREASING = "Order Total - increasing";
			public static final String ORDER_TOTAL_DECREASING = "Order Total - decreasing";
		}

	}
	
	public interface Solr
	{
		public static final String RESTRICTED_FIELD = "restricted_field";
		public static final String POST_PROCESSOR = "queryPostProcessor";
		public static final String GET_RESTRICTED_VALUES = "getRestrictedValues";
		public static final String DEFAULTUG = "DEFAULTJNJGT";
		public static final String COLON = ":";
		public static final String ENABLE_PARTIAL_SEARCH = "search.enable.partial.search";
	}
	
	public interface HomePage
	{
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
		public static final String BASKET_PAGE_MESSAGE_QTY_ADJUSTED = "basket.page.message.qtyAdjusted";
		public static final String BASKET_PAGE_MESSAGE_MIN_QTY_ADDED = "basket.page.message.minQtyAdded";
		

	}
	
	public interface Report
	{
		
		// AAOL -4347 surabhi
		public static final String REPORT_CATEGORY= "reportCategory";
		public static final String REPORT_TYPE ="reportsType";
		public static final String CATEGORY_URL="categoryUrl";
		public static final String SUB_REPORT_URL="subReportUrl";
		
		
		public static final String FINANCIAL_CATEGORY = "financial";
		public static final String INVOICE_REPORT ="invoiceReport";
		public static final String INVOICE_DUE="invoicePastDue";
		public static final String FINANCIAL_SUMMARY="financialSummary";
		//added for AAOL-2426
		 public static final String INVOICE_CLEARING="invoiceClearing";
		//ended for AAOL-2426
		public static final String ORDER_CATEGORY = "order";
		public static final String DELIVERY_LIST = "deliveryList";
		public static final String SALES_REPORT ="salesReport";
		public static final String MULTI_PRODUCT_REPORT="multiProductReport";
		public static final String SINGLE_PRODUCT_REPORT="singleProductReport";
		
		
		public static final String INVENTORY_CATEGORY = "inventory";
		public static final String CONSIGNMENT_REPORT = "consignmentReport";
		public static final String CUT_REPORT ="cutReport";
		public static final String BO_REPORT ="boReport";
		public static final String INVENTORY_REPORT ="inventoryReport";
		
		
		// end
		
		public static final String FINANCIAL_ANALYSIS_LABEL_STRING = "category.financial.Analysis";
		public static final String INVENTORY_ANALYSIS_LABEL_STRING = "category.inventory.Analysis";
		public static final String ORDER_ANALYSIS_LABEL_STRING = "category.order.Analysis";
		
		public static final String BREADCRUMB_FINANCIAL_URL="category.url.financial";
		public static final String BREADCRUMB_INVENTORY_URL = "category.url.inventory";
		public static final String BREADCRUMB_ORDER_URL ="category.url.order";
		
		public static final String INVOICE_REPORT_LABEL_STRING = "invoiceReport.reportType";
		public static final String INVOICE_PAST_DUE_REPORT_LABEL_STRING ="invoicePastDue.reportType";
		public static final String INVOICE_CLEARING_LABEL_STRING ="invoiceClearing.reportType";
		public static final String FINANCIAL_SUMMARY_LABEL_STRING ="financialSummary.reportType";
		
		
		public static final String BO_REPORT_LABEL_STRING = "boReport.reportType";
		public static final String CUT_REPORT_LABEL_STRING ="cutReport.reportType";
		public static final String INVENTORY_REPORT_LABEL_STRING ="inventoryReport.reportType";
		public static final String CONSIGNMENT_REPORT_LABEL_STRING ="consignmentReport.reportType";

		public static final String SALES_REPORT_LABEL_STRING ="salesReport.reportType";
		public static final String DELIVERY_LIST_REPORT_LABEL_STRING ="deliveryList.reportType";
		public static final String SINGLE_PRODUCT_REPORT_LABEL_STRING ="singleProductReport.reportType";
		public static final String MULTI_PRODUCT_REPORT_LABEL_STRING ="multiProductReport.reportType";
		
		
	}


	// implement here constants used by this extension
}
