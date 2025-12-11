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
 * Global class for all Jnjb2bglobalordertemplate constants. You can add global constants for your extension into this
 * class.
 */
public final class Jnjb2bglobalordertemplateConstants extends GeneratedJnjb2bglobalordertemplateConstants
{
	/***added by Shamili*****/
	public static final String SITE_NAME = "JNJ_SITE_NAME";
	public static final String EXTENSIONNAME = "jnjb2bglobalordertemplate";
	public static final String SYMBOl_COMMA = ",";
	public static final String CURRENT_PAGE_TYPE = "currentPageType";
	public static final String EPIC_SITE_URL = "jnj.epic.site.url";
	public static final String SITE_URL = "JNJ_SITE_URL";
	public final static String PDF_EXTENSION = ".pdf";
	public static final String MDD = "MDD";

	private Jnjb2bglobalordertemplateConstants()
	{


		//empty to avoid instantiating this constant class
		super();
		assert false;
	}

	//empty to avoid instantiating this constant class


	// implement here constants used by this extension

	public interface DisputeInquiry
	{
		public static final String DISPUTE_INQUIRY_FILE_PREFIX = "taxexemptcertificate";
		public static final String DISPUTE_INQUIRY_CSR_EMAIL_KEY = "dispute.inquiry.csr.email";
		public static final String DISPUTE_INQUIRY_FILE_PATH_KEY = "dispute.filepath.root";
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

		public static final String SEARCH_TEMPLATE_BY_NUMBER = "select distinct{pk},{order:name},{order:templateNumber},{order:entryList},{order:creationTime} from {JnjOrderTemplate  as order join JnjOrderTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
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

		public static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_AUTHOR = "select distinct{pk},{order:name}, {order:templateNumber},{order:entryList},{order:creationTime},{customer.NAME} from {JnjOrderTemplate  as order join JnjOrderTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
				+ " JOIN Jnjb2bcustomer as customer on {customer:pk} = {order:author}}"
				+ " where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit) and ({entry:product}= ?searchText or {entry:referencedVariant}=?searchText)";

		public static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_STATUS = "select distinct{pk},{order:name}, {order:templateNumber},{order:entryList},{order:creationTime},{status.CODE} from {JnjOrderTemplate  as order join JnjOrderTemplateEntry as entry on {entry:orderTemplate}={order:pk}"
				+ " JOIN ShareStatus as status on {status:pk} = {order:sharestatus}}"
				+ " where {order:unit}=?unit  and ({order:visibleto} =?userpk or {order:visibleto} =?unit) and ({entry:product}= ?searchText or {entry:referencedVariant}=?searchText)";

		public static final String SEARCH_TEMPLATE_DETAIL = "select {pk} from {JnjOrderTemplate} where {unit}=?unit  and ({visibleto} =?userpk or {visibleto} =?unit ) and  {code} like ?searchText ";

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
	}

	public interface Logging
	{
		
		/*******/
		public static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
		
		/****************/
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
		public static final String FORGOT_PASSWORD_NAME = "Forgot Password";

	}
	/***added by Shamili*****/
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

		public interface SearchOption
		{
			public static final String ORDER_NUMBER = "Order Number";
			public static final String PRODUCT_NUMBER = "Product Number";
			public static final String PO_NUMBER = "PO Number";
			public static final String DEALER_PO_NUMBER = "Dealer PO Number";
			public static final String INVOICE_NUMBER = "Invoice Number";
			// Added by Pradeep chandupatla to display the credit memo and debit memo in search field for GTR-1797
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

		public static final String CONSUMER_VALID_ITEM_CATEGORY_KEY = "order.line.item.to.process";
		public static final String SCHEDULE_LINE_STATUS_UC = "UC";
		public static final String SCHEDULE_LINE_STATUS_CANCELLED = "RJ";
		public static final String PRODUCT_DIVISION = "OCD";
		public static final String CONFIRMED_SCHEDULE_LINE_STATUS = "jnj.na.schedule.line.confirmed.status";
		public static final String CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN = "consumer.order.line.item.category";
		public static final String CONSUMER_ORDER_LINE_HIGH_LEVEL_VALUE = "consumer.order.line.high.level.value";
		public static final String BACKORDER_EMAIL_AVAILABLILITY_DATE_FORMAT_KEY = "jnj.na.backorder.availabledate.format";
		public static final String CONSUMER_PRODUCT_LEVEL_ERROR_CODE = "consumer.product.level.error.code";
		public static final String CONSUMER_PRODUCT_EFFECTIVITY_ERROR_CODE = "consumer.product.effectivity.error.code";
		public static final String CONSUMER_PRODUCT_EXCLUSION_BLOCK_ERROR_CODE = "consumer.product.exclusion.block.error.code";
		public static final String CONSUMER_PRODUCT_FIRST_DEA_BLOCK_ERROR_CODE = "consumer.product.first.dea.block.error.code";
		public static final String CONSUMER_PRODUCT_SECOND_DEA_BLOCK_ERROR_CODE = "consumer.product.second.dea.block.error.code";
		public static final String CONSUMER_PRODUCT_MULTIPLE_BLOCK_ERROR_CODE = "consumer.product.multiple.block.error.code";
		public static final String CONSUMER_ORDER_TAPA_LINE_ITEM_CATEGORY = "consumer.order.tapa.line.item.category";
		public static final String ORDER_ENTRY_REASON_FOR_REJECTION_KEY = "order.entry.reasonOfRejection.";
		public static final String ORDER_HISTORY_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD = "orderHistory.pdfAndExcel.download.lineThreshold";
		public static final String ORDER_DETAIL_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD = "orderDetail.pdfAndExcel.download.lineThreshold";
		//Property added to remove the limit off 100 results.
		public static final String ORDER_HISTORY_PAGESIZE = "orderHistory.pagesize";
	}



}
