/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.controllers.pages;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel; 
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;
import java.io.File;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceClearingForm;
import com.jnj.b2b.jnjglobalprofile.controllers.JnjglobalprofileControllerConstants;
import com.jnj.b2b.jnjglobalreports.constants.JnjglobalreportsConstants;
import com.jnj.b2b.jnjglobalreports.controllers.JnjglobalreportsControllerConstants;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTAccountAgingForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTConsignmentInventoryReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTCutReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInventoryReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceDueReportDueForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTMultiPurchaseAnalysisReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTOADeliveryListReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTSinglePurchaseAnalysisReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTSalesReportAnalysisForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialSummaryReportForm;
import com.jnj.b2b.jnjglobalreports.forms.OrderTypesForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTCustomerBasicData;
import com.jnj.facades.data.JnjGTCutReportOrderData;
import com.jnj.facades.data.JnjGTCutReportOrderEntryData;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.facades.data.JnjGTMultiPurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTOADeliveryListReportResponseData;
import com.jnj.facades.data.JnjGTSinglePurchaseAnalysisEntriesData;
import com.jnj.facades.data.JnjGTSinglePurchaseOrderReportResponseData;
import com.jnj.facades.reports.JnjGTReportsFacade;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.services.CMSSiteService;
import com.jnj.facades.data.JnjGTSalesReportResponseData;
import com.jnj.core.dto.JnjGTSearchDTO;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.media.MediaService;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.catalog.CatalogVersionService;

import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.facades.data.JnjGTFinancePurchaseOrderReportResponseData;

/**
 * This class handles the Back-Order Report Page.
 *
 * @author Accenture
 * @version 1.0
 */
@Controller("myReportsPageController")
@RequestMapping(value ="/reports")
@RequireHardLogIn
public class JnjGTReportsPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(JnjGTReportsPageController.class);
	protected static final String DISPLAY_UCN_SELECTION = "displayUCNSelection";
	protected static final String IS_INVENTORY_ACCESSIBLE = "isInventoryAccessible";
	protected static final String REPORTS_ROOT_PAGE_URL = "#";
	protected static final String FRANCHISE_CODES = "franchiseCodes";
	protected static final String CATEGORYDATA = "categoryData";
	protected static final String SEARCH_TOTAL = "searchTotal";
	protected static final String CMS_BACKORDER_REPORT_PAGE = "backorderReportPage";
	protected static final String CMS_INVENTORY_REPORT_PAGE = "inventoryReportPage";
	protected static final String CMS_PURCHASE_ANALYSIS_REPORT_PAGE = "purchaseAnalysisReportPage";
	protected static final String CMS_OA_DELIVERY_LIST_REPORT_PAGE = "oaDeliveryListReportPage";//AAOL-4603 
	protected static final String CMS_CUTORDER_REPORT_PAGE = "cutReportPage";
	protected static final String BACKORDER_RESPONSE_DATA_LIST = "jnjGTBackorderReportResponseDataList";
	protected static final String INVENTORY_RESPONSE_DATA_LIST = "jnjGTInventoryReportResponseDataList";
	protected static final String SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP = "jnjGTSinglePurchaseOrderReportResponseDataMap";
	protected static final String FINANCIAL_SUMMARY_RESPONSE_DATA_LIST = "jnjGTFinancialSummaryReportResponseDataList";
	protected static final String FINACIAL_ANALYSIS_RESPONSE_DATA_MAP = "jnjGTFinancialAnalysisOrderReportResponseDataMap";
	protected static final String SINGLE_PURCHASE_ANALYSIS_ENTRIES_LIST = "jnjGTSinglePurchaseAnalysisEntriesList";
	protected static final String MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST = "jnjGTMultiPurchaseOrderReportResponseDataList";
	protected static final String OA_DELIVERY_LIST_RESPONSE_DATA_LIST = "jnjGTOADeliveryListReportResponseDataList"; //AAOL-4603
	protected static final String NO_DATA_FOUND_FLAG = "noDataFound";
	protected static final String BACKORDER_FORM_NAME = "jnjGlobalBackorderReportForm";
	protected static final String INVENTORY_FORM_NAME = "jnjGlobalInventoryReportForm";
	protected static final String CONSIGNMENT_INV_FORM_NAME = "jnjGlobalConsignmentInventoryReportForm";
	protected static final String SINGLE_PURCHASE_ANALYSIS_FORM_NAME = "jnjGlobalSinglePurchaseAnalysisReportForm";
	protected static final String MULTI_PURCHASE_ANALYSIS_FORM_NAME = "jnjGlobalMultiPurchaseAnalysisReportForm";
	protected static final String FINANCIAL_ANALYSIS_FORM_NAME = "JnjGlobalFinancialAnalysisReportForm";
	protected static final String INVOICE_DUE_FINANCIAL_ANALYSIS_FORM_NAME = "jnjGTInvoiceDueReportDueForm";
	protected static final String FINANCIAL_SUMMARY_FORM_NAME = "JnjGlobalFinancialSummaryReportForm";
	protected static final String OA_DELIVERY_LIST_FORM_NAME = "jnjGlobalOADeliveryListReportForm"; //AAOL-4603
	protected static final String REPORTS_LABEL_STRING = "reports.report.header";
	protected static final String BACKORDER_LABEL_STRING = "reports.backorder.header";
	protected static final String INVENTORY_LABEL_STRING = "reports.inventory.header";
	protected static final String CUT_REPORT_LABEL_STRING = "cutReport.label";
	protected static final String PURCHASE_ANALYSIS_LABEL_STRING = "reports.purchase.analysis.header";
	protected static final String OA_DELIVERY_LIST_LABEL_STRING = "reports.oa.delivery.list.header"; //AAOL-4603
	protected static final String INVOICE_DUE_LIST_LABEL_STRING = "reports.invoicedue.header";
	protected static final String OPERATING_COMPANY = "operatingCompany";
	protected static final String ANALYSIS_VARIABLE = "analysisVariable";
	protected static final String PRODUCT_DISPLAY = "productDisplay";
	protected static final String INVALID_PRODUCT = "invalidProduct";
	protected static final String PRODUCT_INFO = "productInfo";
	protected static final String ORDERED_FROM = "orderedFrom";
	protected static final String ORDER_TYPES = "orderTypes";
	protected static final String TERRITORIES = "territories";
	
	/*START AAOL-4603 for ASPAC*/
	protected static final String ORDER_TYPE= "orderType"; //- By default it will be Standard Order
	protected static final String SALES_DOC_NUM = "salesDocNum";
	protected static final String CUST_PO_NUM = "custPONum";
	protected static final String DELIVERY_NUM = "deliveryNum";
	protected static final String START_DATE = "startDate"; // - Actual Shipment Date - Default to Past 1 month date from todays date
	protected static final String END_DATE = "endDate";
	protected static final String ACTUAL_SHIPMENT_DATE = "actualShipmentDate";
	protected static final String FRANCHISE_DESC = "franchiseDesc";
	protected static final String PRODUCT_CODE = "productCode";
	/*END AAOL-4603 for ASPAC*/
	
	protected static final String RESPONSE_DATA = "responseData";
	protected static final String TOTAL_SPENDING_MAP = "totalSpendingMap";
	protected static final String ACCOUNT = "account";
	protected static final String PERIOD = "period";
	protected static final String ALL = "All";
	private static final String CUTREPORT_FORM_NAME = "jnjGTCutReportForm";
    protected static final String CMS_CONSIGNMENT_INVENTORY_REPORT_PAGE = "consignmentReportPage";
    protected static final String CONSIGNMENT_INVENTORY_FORM_NAME = "jnjGlobalConsignmentInventoryReportForm";
    
    protected static final String CMS_INVOICE_REPORT = "invoiceReportPage";

	/** AAOL-2401: Order Analysis  **/
	protected static final String ORDER_ANALYSIS_LABEL_STRING = "reports.order.analysis.header";
	protected static final String SALES_REPORT_ORDER_ANALYSIS_FORM ="JnjGTSalesReportAnalysisForm";
	protected static final String SALES_ORDER_ANALYSIS_RESPONSE_DATA_LIST = "jnjGTSalesReportResponseDataList";

	protected static final String SELECTED_REPORT = "selectedReport";

	/** Pagination + Account selection Constants **/
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String ACCOUNT_SELECTION_SHOW_ALL_RESULTS = "account.selection.showAllResults";
	protected static final String ACCOUNT_PAGINATION_DATA = "accountPaginationData";
	protected static final String ACCOUNT_SEARCH_TERM = "accountSearchTerm";
	protected static final String CURRENT_PAGE = "currentPage";
	protected static final String FETCH_ALL = "fetchAll";
	public static final int MAX_PAGE_LIMIT = 50;
	//Added for AAOL-2426
	protected static final String INVOICE_CLEARING_FINANCIAL_ANALYSIS_FORM_NAME = "jnjGTInvoiceClearingForm";

	/** The customer facade **/
	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	/** The reports facade **/
	@Resource(name="GTReportsFacade")
	protected JnjGTReportsFacade jnjGTReportsFacade;

	/** The bread-crumb builder **/
	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	/** The common facade utility **/
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The cart facade **/
	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;
	
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "jnjGTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTUnitFacade;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected CMSSiteService cMSSiteService;
	@Autowired
	protected MediaService mediaService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	/**
	 * @return the cMSSiteService
	 */
	public CMSSiteService getcMSSiteService() {
	    return cMSSiteService;
	}
	public CatalogVersionService getCatalogVersionService() {
	    return catalogVersionService;
	}
	/**
	 * @return the mediaService
	 */
	public MediaService getMediaService() {
	    return mediaService;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public JnjGTReportsFacade getJnjGTReportsFacade() {
		return jnjGTReportsFacade;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}

	public JnjGTB2BUnitFacade getJnjGTUnitFacade() {
		return jnjGTUnitFacade;
	}
	
	public EnumerationService getEnumerationService() {
		return enumerationService;
	}


	public SessionService getSessionService() {
		return sessionService;
	}
	/** The Enumerator DOWNLOAD_TYPE. **/
	protected enum DOWNLOAD_TYPE
	{
		/** The PDF. **/
		PDF,
		/** The EXCEL. **/
		EXCEL, NONE;
	}

	/** Constants for PDF / Excel Views for reports - **/
	protected static final String CUT_REPORT_PDF_VIEW = "jnjGTCutReportPdfView";
	protected static final String CUT_REPORT_EXCEL_VIEW = "jnjGTCutReportExcelView";
	protected static final String BACKORDER_REPORT_PDF_VIEW = "jnjGTBackorderReportPdfView";
	protected static final String BACKORDER_REPORT_EXCEL_VIEW = "jnjGTBackorderReportExcelView";
	protected static final String INVENTORY_REPORT_PDF_VIEW = "jnjGTInventoryReportPdfView";
	protected static final String INVENTORY_REPORT_EXCEL_VIEW = "jnjGTInventoryReportExcelView";
	protected static final String SINGLE_PURCHASE_ANALYSIS_REPORT_PDF_VIEW = "jnjGTSinglePurchaseAnalysisReportPdfView";
	protected static final String SINGLE_PURCHASE_ANALYSIS_REPORT_EXCEL_VIEW = "jnjGTSinglePurchaseAnalysisReportExcelView";
	protected static final String MULTI_PURCHASE_ANALYSIS_REPORT_PDF_VIEW = "jnjGTMultiPurchaseAnalysisReportPdfView";
	protected static final String MULTI_PURCHASE_ANALYSIS_REPORT_EXCEL_VIEW = "jnjGTMultiPurchaseAnalysisReportExcelView";
	protected static final String SALES_REPORT_ANALYSIS_REPORT_PDF_VIEW = "jnjGTSalesReportPdfView";//AAOL-2410
	protected static final String SALES_REPORT_ANALYSIS_REPORT_EXCEL_VIEW = "jnjGTSalesReportExcelView";//AAOL-2410
	protected static final String OA_DELIVERY_LIST_REPORT_PDF_VIEW = "jnjGTOADeliveryListReportPdfView";//AAOL-4603
	protected static final String OA_DELIVERY_LIST_REPORT_EXCEL_VIEW = "jnjGTOADeliveryListReportExcelView";//AAOL-4603
	protected static final String FINANCIAL_ANALYSIS_INVOICE_REPORT_PDF_VIEW = "JnJGTFnancialInvoiceReportPdfView";
	protected static final String FINANCIAL_ANALYSIS_INVOICE_REPORT_EXCEL_VIEW = "JnJGTFnancialInvoiceReportExcelView";
	protected static final String FINANCIAL_SUMMARY_REPORT_EXCEL_VIEW = "JnJGTFnancialSummaryReportExcelView";
	protected static final String FINANCIAL_SUMMARY_REPORT_PDF_VIEW = "JnJGTFinancialSummaryReportPdfView";
	protected static final String FINANCIAL_ANALYSIS_INVOICE_DUE_REPORT_PDF_VIEW = "JnJGTFinancialInvoiceDueReportPdfView";
	protected static final String FINANCIAL_ANALYSIS_INVOICE_DUE_REPORT_EXCEL_VIEW = "JnJGTFinancialInvoiceDueReportExcelView";
	protected static final String CONSIGNMENT_INVENTORY_REPORT_PDF_VIEW = "jnjGTConsIventoryReportPdfView";
	protected static final String CONSIGNMENT_INVENTORY_EXCEL_VIEW = "jnjGTConsIventoryReportExcelView";
	//AAOL-2426 chanegs
	protected static final String FINANCIAL_ANALYSIS_INVOICE_CLEARING_REPORT_PDF_VIEW = "JnJGTFinancialInvoiceClearingReportPdfView";
	protected static final String FINANCIAL_ANALYSIS_INVOICE_CLEARING_EXCEL_VIEW = "JnJGTFinancialInvoiceClearingReportExcelView";
	//AAOL-5794 start
	protected static final String BACKORDER_ALL_REPORTS = "allreports";
	//AAOL-5794 end
	/**
	 * This method is used to fetch all the accounts and send the respective details to the user
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/accountSelection")
	public String getAccountSelection(final Model model,
			@RequestParam(value = "ucnFlag", defaultValue = "false", required = false) final boolean ucnFlag,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = FETCH_ALL, defaultValue = "false", required = false) final boolean fetchAll)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getbackOrderReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final int pageSizeFromConfig;
		if (fetchAll)
		{
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_SHOW_ALL_RESULTS, 1000);
		}
		else
		{
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
		}

		final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm) && !fetchAll)
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}
		/** Calling facade layer **/
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Calling facade to fetch accounts!");
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade
				.getAccountsMap(true, true, ucnFlag, pageableData);

		/** Setting all the accounts by calling the facade layer method getAccountsMap **/
		model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountSelectionData.getAccountsMap());
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		
		/* *
		 * 	Fetch all flag for account selection from popup 
		 * */
		if (fetchAll)
		{
			model.addAttribute(FETCH_ALL, Boolean.valueOf(fetchAll));
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Misc.AccountSelectionPage);
	}

	/**
	 * This method is responsible for handling the Back-Order Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/inventoryAnalysis/backorder")
	public String getBackOrderReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getBackOrderReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.BO_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Setting the form in the model **/
		model.addAttribute(BACKORDER_FORM_NAME, new JnjGTBackorderReportForm());

		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		//setupModel(model, CMS_BACKORDER_REPORT_PAGE);
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.BO_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.BO_REPORT);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
	}
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Back-Order Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/inventoryAnalysis/backorder")
	public String fetchBackOrderReport(final Model model, @ModelAttribute final JnjGTBackorderReportForm jnjGlobalBackorderReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		/** Creating bread-crumbs by surabhi **/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.BO_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Setting the form in the model **/
		model.addAttribute(BACKORDER_FORM_NAME, jnjGlobalBackorderReportForm);

		/** Generate Back-Order Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateBackorderResponse(model, populatePageableData(jnjGlobalBackorderReportForm));

		/** Setting up the model **/
		//setupModel(model, CMS_BACKORDER_REPORT_PAGE);
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.BO_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.BO_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGlobalBackorderReportForm.getAccountsSelectedValue());
		//AAOL-5794 start
		model.addAttribute(BACKORDER_ALL_REPORTS,jnjGlobalBackorderReportForm.getAllreports());
		//AAOL-5794 end
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
	}
	
	/**
	 * This method is responsible for handling the Financial Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	/*AAOL #2419*/
	@GetMapping("/financialAnalysis/invoiceReport")
	public String getFinancialAnalysisReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getFinancialAnalysisReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_FINANCIAL_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		
		/** In case of single purchase analysis report **/
		model.addAttribute(FINANCIAL_ANALYSIS_FORM_NAME, new JnjGlobalFinancialAnalysisReportForm());
		
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"SINGLE PA REPORT - JnjGTSinglePurchaseAnalysisReportForm has been set in the model!");
		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		//final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		setCurrentB2BUnitIdInModel(model);
		setupModel(model, CMS_INVOICE_REPORT);
		
		//model.addAttribute(FRANCHISE_CODES,jnjGTCustomerFacade.getAllowedFranchise());
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_REPORT);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);	
	}

	/**
	 * This method is responsible for handling the Financial Summer Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	/*AAOL #2419*/
	@GetMapping("/financialAnalysis/financialSummary")
	public String getFinancialSummaryReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getFinancialSummaryReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		List<String> payersids= null;
		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_FINANCIAL_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_SUMMARY_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		

		/** In case offinancial Summary report **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, new JnjGlobalFinancialSummaryReportForm());
		
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Financial Summery REPORT - JnjGlobalFinancialSummaryReportForm has been set in the model!");
		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		//final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		setCurrentB2BUnitIdInModel(model);
		setupModel(model, CMS_INVOICE_REPORT);
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		String acountid = jnjGTCustomerFacade.getCurrentB2bUnit().getUid();
		List<String> payerid = getPayerIds(acountid);
		model.addAttribute("payerid",payerid);
		model.addAttribute("showAccounts", true);		
		model.addAttribute("showCurrency", true);
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);	
	}
	
	/**
	 *
	 * This method is responsible for getting all the Payer information based on account id
	 *
	 * @param accountid
	 * @return List<String>
	 */	@ResponseBody
	@GetMapping("/financialAnalysis/getPayer")
	public List<String> getPayerIds(@RequestParam(value = "accountid") final String accountid){
		
		/*** Populate Order types only those listed in requirements and not all. ***/
		List<String> payerIds = jnjGTReportsFacade.getPayerIdValuesBasedPayFromAddress(accountid);
		return payerIds;
	}
	
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Financial Summer Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/accountAging")
	public String fetchFinancialSummaryAccountAgingReport(final Model model, @ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm, 
			@RequestParam(value = "payer") final String payer, @RequestParam(value = "accountid") final String accountid)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		List<String> searchParams = new ArrayList<>();
		searchParams.add(payer);
		searchParams.add(accountid);
		//add additional parameters here
		jnjGTPageableData.setSearchParamsList(searchParams);

		List<JnjGTFinancialAccountAgingReportData> consInventoryReportList = jnjGTReportsFacade.simulateAccountAgingReport(jnjGTPageableData);
		/** Checking if not MDD, redirect to root node of Reports **/
		if (!(LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + REPORTS_ROOT_PAGE_URL;
		}

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(INVENTORY_LABEL_STRING),null,null);
		/** Generate Financial Summer Response and set it in model by passing the page-able data by calling populatePageableData **/
	
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, new JnjGlobalFinancialSummaryReportForm());
		
		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVOICE_REPORT);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute("accountAgingReportData", consInventoryReportList);
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.AccountAgingReportPage);
		//return accountAgingReportDatas;
		
	}
	
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Financial Summer Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/balanceSummary")
	public String fetchFinancialBalanceSummaryReport(final Model model, @ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm, 
			@RequestParam(value = "payer") final String payer, @RequestParam(value = "accountid") final String accountid)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		
		JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		List<String> searchParams = new ArrayList<>();
		searchParams.add(payer);
		searchParams.add(accountid);
		//add additional parameters here
		jnjGTPageableData.setSearchParamsList(searchParams);
		
		List<JnjGTFinancialBalanceSummaryReportData> consBalanceSummaryReportList = jnjGTReportsFacade.simulateBalanceSummaryReport(jnjGTPageableData);
		/** Checking if not MDD, redirect to root node of Reports **/
		if (!(LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + REPORTS_ROOT_PAGE_URL;
		}

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(INVENTORY_LABEL_STRING),null,null);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, new JnjGlobalFinancialSummaryReportForm());

		/** Generate Financial Summer Response and set it in model by passing the page-able data by calling populatePageableData **/
		
		final List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportDatas = new ArrayList<>(); 
		
		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVOICE_REPORT);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute("balanceSummaryReportData", consBalanceSummaryReportList);
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.BalanceSummaryReportPage);
	}
	
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Financial Summer Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/paymentSummary")
	public String fetchFinancialPaymentSummaryReport(final Model model, @ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm, 
			@RequestParam(value = "payer") final String payer, @RequestParam(value = "accountid") final String accountid)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** Generate Financial Summer Response and set it in model by passing the page-able data by calling populatePageableData **/
		JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		List<String> searchParams = new ArrayList<>();
		searchParams.add(payer);
		searchParams.add(accountid);
		//add additional parameters here
		jnjGTPageableData.setSearchParamsList(searchParams);
		
		List<JnjGTFinancialPaymentSummaryReportData> consPaymentSummaryReportList = jnjGTReportsFacade.simulatePaymentSummaryReport(jnjGTPageableData);
		
		/** Checking if not MDD, redirect to root node of Reports **/
		if (!(LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + REPORTS_ROOT_PAGE_URL;
		}

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(INVENTORY_LABEL_STRING),null,null);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, new JnjGlobalFinancialSummaryReportForm());
		
		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVOICE_REPORT);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute("paymentSummaryReportData", consPaymentSummaryReportList);
		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.PaymentSummaryReportPage);
	}
	
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Financial Summer Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/creditSummary")
	public String fetchFinancialCreditSummaryReport(final Model model, @ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm, 
			@RequestParam(value = "payer") final String payer, @RequestParam(value = "accountid") final String accountid)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		
		JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		List<String> searchParams = new ArrayList<>();
		searchParams.add(payer);
		searchParams.add(accountid);
		//add additional parameters here
		jnjGTPageableData.setSearchParamsList(searchParams);
		
		/** Generate Financial Summer Response and set it in model by passing the page-able data by calling populatePageableData **/
		List<JnjGTFinancialCreditSummaryReportData> consCreditSummaryReportList = jnjGTReportsFacade.simulateCreditSummaryReport(jnjGTPageableData);
		
		/** Checking if not MDD, redirect to root node of Reports **/
		if (!(LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + REPORTS_ROOT_PAGE_URL;
		}

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(INVENTORY_LABEL_STRING),null,null);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, new JnjGlobalFinancialSummaryReportForm());

		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVOICE_REPORT);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute("creditSummaryReportData", consCreditSummaryReportList);
		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.CreditSummaryReportPage);
		
	}
	
	/**
	 *
	 * This method is responsible for handling the Search feature on FinancialAnalysis Reports Page.
	 *
	 * @param model
	 * @param JnjGlobalFinancialAnalysisReportForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	/*AAOL #2419*/
	@PostMapping("/financialAnalysis/invoiceReport")
	public String fetchFinancialAnalysisReport(final Model model,
			@ModelAttribute final JnjGlobalFinancialAnalysisReportForm jnjGlobalFinancialAnalysisReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialAnalysisReportReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_FINANCIAL_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
	    final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
				 
		model.addAttribute(FINANCIAL_ANALYSIS_FORM_NAME, jnjGlobalFinancialAnalysisReportForm);

		generateFinancePurchaseAnalysisResponse(model, populatePageableData(jnjGlobalFinancialAnalysisReportForm));

		setupModel(model, CMS_INVOICE_REPORT);
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_REPORT);
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGlobalFinancialAnalysisReportForm.getAccountsSelectedValue());
		model.addAttribute("startDate",jnjGlobalFinancialAnalysisReportForm.getStartDate());
		model.addAttribute("endDate",jnjGlobalFinancialAnalysisReportForm.getEndDate());
		model.addAttribute("financialStatus",jnjGlobalFinancialAnalysisReportForm.getFinancialStatus());
		model.addAttribute("customerPONumber",jnjGlobalFinancialAnalysisReportForm.getCustomerPONumber());
		model.addAttribute("salesDocumentNumber",jnjGlobalFinancialAnalysisReportForm.getSalesDocumentNumber());
		model.addAttribute("invoiceNumber",jnjGlobalFinancialAnalysisReportForm.getInvoiceNumber());
		model.addAttribute("franchiseDesc",jnjGlobalFinancialAnalysisReportForm.getFranchiseDesc());
		model.addAttribute("orderType",jnjGlobalFinancialAnalysisReportForm.getOrderType());

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}
	
	
	/**
	 * THis method generates the Single Purchase Analysis response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	/*AAOL #2419*/
	protected void generateFinancePurchaseAnalysisResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateFinancePurchaseAnalysisResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)	{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Single Purchase Analysis report.");
	 
			final TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> jnjGTFinancialAnalysisOrderReportResponseDataMap = jnjGTReportsFacade.fetchFinancialAnalysisReport(jnjGTPageableData);
 
			if (null != jnjGTFinancialAnalysisOrderReportResponseDataMap && jnjGTFinancialAnalysisOrderReportResponseDataMap.size() != 0) {
				if (jnjGTFinancialAnalysisOrderReportResponseDataMap.containsKey(INVALID_PRODUCT)) {
					model.addAttribute(INVALID_PRODUCT, Boolean.TRUE);
				}
				else {
					/** Setting the jnjGTFinancialAnalysisOrderReportResponseDataMap in the model **/
					model.addAttribute(FINACIAL_ANALYSIS_RESPONSE_DATA_MAP, jnjGTFinancialAnalysisOrderReportResponseDataMap);
					logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
							"jnjGTFinancialAnalysisOrderReportResponseDataMap has been set in the model!");
				}
			}
			else {
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTFinancialAnalysisOrderReportResponseDataMap was found to be null or empty! Moving to regular inventory page load!");
			}
		}
		else {
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular Single Purchase Analysis page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	
	/**
	 * This method populates the page-able data for the financial Summary search
	 *
	 * JnjGlobalFinancialSummaryReportForm
	 * @return jnjGTPageableData
	 */
	/*AAOL #2419*/
	protected JnjGTPageableData populatePageableData(final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check jnjGlobalFinancialAnalysisReportForm **/
		if (null != jnjGlobalFinancialSummaryReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			if (null != jnjGlobalFinancialSummaryReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(jnjGlobalFinancialSummaryReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				
				jnjGTPageableData.setFromDate(jnjGlobalFinancialSummaryReportForm.getPayer());
				
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTFinanceBackorderReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTFinanceBackorderReport Form was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}
	
	/**
	 * This method populates the page-able data for the financial analysis order entries search
	 *
	 * JnjGTFinanceBackorderReportForm
	 * @return jnjGTPageableData
	 */
	/*AAOL #2419*/
	protected JnjGTPageableData populatePageableData(final JnjGlobalFinancialAnalysisReportForm jnjGlobalFinancialAnalysisReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check jnjGlobalFinancialAnalysisReportForm **/
		if (null != jnjGlobalFinancialAnalysisReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			if (null != jnjGlobalFinancialAnalysisReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(jnjGlobalFinancialAnalysisReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				
				jnjGTPageableData.setFromDate(jnjGlobalFinancialAnalysisReportForm.getStartDate());
				jnjGTPageableData.setToDate(jnjGlobalFinancialAnalysisReportForm.getEndDate());
				jnjGTPageableData.setOrderType(jnjGlobalFinancialAnalysisReportForm.getOrderType());
				jnjGTPageableData.setFinancialsDescription(jnjGlobalFinancialAnalysisReportForm.getFranchiseDesc());
				jnjGTPageableData.setFinancialStatus(jnjGlobalFinancialAnalysisReportForm.getFinancialStatus());
				jnjGTPageableData.setCustomerPONumber(jnjGlobalFinancialAnalysisReportForm.getCustomerPONumber());
				jnjGTPageableData.setSalesDocumentNumber(jnjGlobalFinancialAnalysisReportForm.getSalesDocumentNumber());
				jnjGTPageableData.setInvoiceNumber(jnjGlobalFinancialAnalysisReportForm.getInvoiceNumber());
				
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTFinanceBackorderReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTFinanceBackorderReport Form was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 * This method is responsible for handling the Financial Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/financialAnalysis/invoicePastDue")
	public String getinvoicePastDuefinancialAnalysisReportPage(final Model model) throws CMSItemNotFoundException	{
	 
		final String METHOD_NAME = "getinvoicePastDue()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_FINANCIAL_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_PAST_DUE_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);


		/** In case of multiple purchase analysis report **/
		model.addAttribute(INVOICE_DUE_FINANCIAL_ANALYSIS_FORM_NAME,new JnjGTInvoiceDueReportDueForm() );
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"MULTI PA REPORT - invoicePastDuefinancialAnalysisReport has been set in the model!");
		
		/*inventry report check*/

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		//Check which type of site is used to fetch the territory dropdown
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		// AAOL - 4347
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_DUE);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_DUE);
		//AAOL-4347 
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
			
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}
	
	/**
	 *
	 * This method is responsible for handling the Search feature on InvoicePastDue Reports Page.
	 *
	 * @param model
	 * @param jnjGTInvoiceDueReportDueForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/invoicePastDue")
	public String fetchInvoicePastDueReport(final Model model, @ModelAttribute("jnjGTInvoiceDueReportDueForm") JnjGTInvoiceDueReportDueForm jnjGTInvoiceDueReportDueForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchInvoicePastDueReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_FINANCIAL_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_PAST_DUE_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Setting the form in the model **/
		model.addAttribute(INVOICE_DUE_FINANCIAL_ANALYSIS_FORM_NAME, jnjGTInvoiceDueReportDueForm);

		/** Generate Back-Order Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateInvoicePastDueResponse(model, populatePageableData(jnjGTInvoiceDueReportDueForm));

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		// AAOL - 4347
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_DUE);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_DUE);
		//AAOL-4347
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGTInvoiceDueReportDueForm.getAccountsSelectedValue());
		model.addAttribute("invoiceDueDate", jnjGTInvoiceDueReportDueForm.getInvoiceDueDate());

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}
	
	/**
	 * This method generates the InvoicePastDue response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateInvoicePastDueResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateInvoicePastDueResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Invoice Past Due report.");

			/** calling facade layer to fetch the back-order report data **/
			final List<JnjGTInvoicePastDueReportResponseData> jnjGTInvoicePastDueReportResponseDataList = jnjGTReportsFacade
					.fetchInvoicePastDueReport(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTInvoicePastDueReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTInvoicePastDueReportResponseDataList))
			{
				/** Setting the jnjGTInvoicePastDueReportResponseDataList in the model **/
				model.addAttribute("invoicePastDueResponse", jnjGTInvoicePastDueReportResponseDataList);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInvoicePastDueReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInvoicePastDueReportResponseDataList was found to be null or empty! Moving to regular backorder page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular invoice due load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	
	@GetMapping("/financialAnalysis/invoiceClearing")
	public String getinvoiceCleaningfinancialAnalysisReportPage(final Model model) throws CMSItemNotFoundException	{
	 
		final String METHOD_NAME = "getinvoicePastDue()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		/* fix for AAOL-5191*/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_CLEARING_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		/** In case of multiple purchase analysis report **/
		model.addAttribute(INVOICE_CLEARING_FINANCIAL_ANALYSIS_FORM_NAME,new JnjGTInvoiceClearingForm() );
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"MULTI PA REPORT - invoicePastDuefinancialAnalysisReport has been set in the model!");
		
		/*inventry report check*/

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		//Check which type of site is used to fetch the territory dropdown
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		// AAOL - 4347
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_CLEARING);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_CLEARING);
		//AAOL-4347 
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
			
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}
	
	/**
	 *
	 * This method is responsible for handling the Search feature on InvoicePastDue Reports Page.
	 *
	 * @param model
	 * @param jnjGTInvoiceDueReportDueForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialAnalysis/invoiceClearing")
	public String fetchInvoiceClearingReport(final Model model, @ModelAttribute("jnjGTInvoiceDueReportDueForm") JnjGTInvoiceClearingForm jnjGTInvoiceClearingForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchInvoiceClearingReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		/* fix for AAOL-5191*/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.FINANCIAL_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVOICE_CLEARING_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		
		/** Setting the form in the model **/
		model.addAttribute(INVOICE_CLEARING_FINANCIAL_ANALYSIS_FORM_NAME, jnjGTInvoiceClearingForm);

		/** Generate Back-Order Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateInvoiceClearingResponse(model, populatePageableData(jnjGTInvoiceClearingForm));

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		// AAOL - 4347
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVOICE_CLEARING);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVOICE_CLEARING);
		//AAOL-4347
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGTInvoiceClearingForm.getAccountsSelectedValue());


		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}
	
	
	
	//AAOL-2426 changes
	protected void generateInvoiceClearingResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateInvoicePastDueResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Invoice Past Due report.");

			/** calling facade layer to fetch the back-order report data **/
			final List<JnjGTInvoiceClearingReportResponseData> jnjGTInvoiceClearingReportResponseDataList = jnjGTReportsFacade
					.fetchInvoiceClearingReport(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTInvoiceClearingReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTInvoiceClearingReportResponseDataList))
			{
				/** Setting the jnjGTInvoicePastDueReportResponseDataList in the model **/
				model.addAttribute("invoiceClearingResponse", jnjGTInvoiceClearingReportResponseDataList);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInvoicePastDueReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInvoicePastDueReportResponseDataList was found to be null or empty! Moving to regular backorder page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular invoice due load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	//AAOL-2426 changes
	
	
	
	protected JnjGTPageableData populatePageableData(final JnjGTInvoiceClearingForm jnjGTInvoiceClearingForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTInvoiceDueReportForm **/
		if (null != jnjGTInvoiceClearingForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			if(null != jnjGTInvoiceClearingForm.getStartDate() && null != jnjGTInvoiceClearingForm.getEndDate())
			{/*if (null != JnjGTBackorderReportForm.getAccountIds() && null != JnjGTBackorderReportForm.getFromDate()
				&& null != JnjGTBackorderReportForm.getToDate())*/
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchBy(jnjGTInvoiceClearingForm.getInvoiceNumber());
				jnjGTPageableData.setFromDate(jnjGTInvoiceClearingForm.getStartDate());
				jnjGTPageableData.setToDate(jnjGTInvoiceClearingForm.getEndDate());
				jnjGTPageableData.setStatus(jnjGTInvoiceClearingForm.getStatus());
				jnjGTPageableData.setReceiptNumber(jnjGTInvoiceClearingForm.getReceiptNumber());
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				//** One or more form elements were null **//*
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTBackorderReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTBackorderReportForm was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}
	//AAOL-2426 changes
	/**	
	 * This method populates the jnjGTPageableData for JnjGTInvoiceDueReportForm
	 *
	 * JnjGTInvoiceDueReportDueForm
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTInvoiceDueReportDueForm JnjGTInvoiceDueReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTInvoiceDueReportForm **/
		if (null != JnjGTInvoiceDueReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			if(null != JnjGTInvoiceDueReportForm.getInvoiceDueDate() && null != JnjGTInvoiceDueReportForm.getInvoiceNumber() && null !=JnjGTInvoiceDueReportForm.getStatus())
			{/*if (null != JnjGTBackorderReportForm.getAccountIds() && null != JnjGTBackorderReportForm.getFromDate()
				&& null != JnjGTBackorderReportForm.getToDate())*/
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchBy(JnjGTInvoiceDueReportForm.getInvoiceNumber());
				jnjGTPageableData.setFromDate(JnjGTInvoiceDueReportForm.getInvoiceDueDate());
				jnjGTPageableData.setStatus(JnjGTInvoiceDueReportForm.getStatus());
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				//** One or more form elements were null **//*
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTBackorderReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTBackorderReportForm was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	
	/**
	 * This method is responsible for handling the Inventory Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/inventoryAnalysis/inventoryReport")
	public String getInventoryReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getInventoryReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Fetching the division data to check if CODMAN or MITEK **/
		//inventry report check
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(null!= divisionData &&( divisionData.isIsMitek() || divisionData.isIsCodman())){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		
		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting the form in the model **/
		model.addAttribute(INVENTORY_FORM_NAME, new JnjGTInventoryReportForm());

		/** Setting up the model **/
		setupModel(model, CMS_INVENTORY_REPORT_PAGE);
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//Report categorization
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVENTORY_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVENTORY_REPORT);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
		//return "";
	}

	
	/**
	 * This method is responsible for handling the Single Purchase Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/orderAnalysis/single")
	public String getSinglePurchaseAnalysisReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getSinglePurchaseAnalysisReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(PURCHASE_ANALYSIS_LABEL_STRING),null,null);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		
		/** In case of single purchase analysis report **/
		model.addAttribute(SINGLE_PURCHASE_ANALYSIS_FORM_NAME, new JnjGTSinglePurchaseAnalysisReportForm());
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"SINGLE PA REPORT - JnjGTSinglePurchaseAnalysisReportForm has been set in the model!");
		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		
		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));

		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
		//return "";
	}

	/**
	 * This method is responsible for handling the Multiple Purchase Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/orderAnalysis/multi")
	public String getMultiPurchaseAnalysisReportPage(final Model model) throws CMSItemNotFoundException
	{
	 
		final String METHOD_NAME = "getMultiPurchaseAnalysisReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.MULTI_PRODUCT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** In case of multiple purchase analysis report **/
		model.addAttribute(MULTI_PURCHASE_ANALYSIS_FORM_NAME, new JnjGTMultiPurchaseAnalysisReportForm());
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"MULTI PA REPORT - JnjGTMultiPurchaseAnalysisReportForm has been set in the model!");
		
		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));
		model.addAttribute(ANALYSIS_VARIABLE,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.ANALYSIS_VARIABLE));
		model.addAttribute(PRODUCT_DISPLAY,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY));
		//Check which type of site is used to fetch the territory dropdown
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();
		//model.addAttribute(OPERATING_COMPANY, jnjGTCustomerFacade.getAllowedFranchise());
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		model.addAttribute(TERRITORIES, jnjGTReportsFacade.getTerritoryForSalesRep(currentSite));
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.MULTI_PRODUCT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.MULTI_PRODUCT_REPORT);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
	}
	
	/**
	 * AAOL-2401: This method is responsible for handling the salesreport feature on reports page.
	 *  
	 * 
	 */
	@GetMapping("/orderAnalysis/salesreport")
	public String getsalesReportOrderAnalysisPage(final Model model) throws CMSItemNotFoundException
	{
	 
		final String METHOD_NAME = "getsalesReportOrderAnalysisPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.SALES_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);


		/** In case of multiple purchase analysis report **/
		model.addAttribute(SALES_REPORT_ORDER_ANALYSIS_FORM, new JnjGTSalesReportAnalysisForm());
		
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"SALESREPORT PA REPORT - JnjGTSalesReportAnalysisForm has been set in the model!");
		

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));
		model.addAttribute(ANALYSIS_VARIABLE,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.ANALYSIS_VARIABLE));
		model.addAttribute(PRODUCT_DISPLAY,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY));
		
		final List<OrderTypesForm> orderTypes = new ArrayList<>();
		final List<SelectOption> orderStatus = new ArrayList<>();
		/*** Populate Order types only those listed in requirements and not all. ***/
		populateOrderTypes(orderTypes);
		populateOrderStatus(orderStatus);
		model.addAttribute("orderTypes",orderTypes);
		model.addAttribute("orderStatus",orderStatus);
		//model.addAttribute(FRANCHISE_CODES,jnjGTCustomerFacade.getAllowedFranchise());
		
		
		//Check which type of site is used to fetch the territory dropdown
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();
		if(Objects.nonNull(currentSite))
		{
			model.addAttribute(OPERATING_COMPANY, jnjGTReportsFacade.getOperatingCompanyDropdown(currentSite));
			model.addAttribute(TERRITORIES, jnjGTReportsFacade.getTerritoryForSalesRep(currentSite));
		}
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		model.addAttribute("selectedReport",LoginaddonConstants.Report.SALES_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.SALES_REPORT);
		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
	}
	
		/**
	 * AAOL-4603 for ASPAC
	 * 
	 * This method is responsible for handling the Order Analysis - Delivery List Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/orderAnalysis/deliveryList")
	public String getOADeliveryListReportPage(final Model model) throws CMSItemNotFoundException
	{
	 
		final String METHOD_NAME = "getOADeliveryListReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.DELIVERY_LIST_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		final List<OrderTypesForm> orderTypes = new ArrayList<>();
		/*** Populate Order types only those listed in requirements and not all. ***/
		populateOrderTypes(orderTypes);
		model.addAttribute(ORDER_TYPES,orderTypes);
		
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();		
		/** Setting franchice drop down values **/
		/*model.addAttribute(FRANCHISE_CODES,
				jnjGTCustomerFacade.getAllowedFranchise());*/
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		
		JnjGTOADeliveryListReportForm jnjGTOADeliveryListReportForm = new JnjGTOADeliveryListReportForm();
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		
		if(StringUtils.isEmpty(jnjGTOADeliveryListReportForm.getStartDate()) || StringUtils.isEmpty(jnjGTOADeliveryListReportForm.getEndDate())){
			Date defaultDate= DateUtils.addMonths(new Date(), -1);		
			String formattedDate = dateFormat.format(defaultDate).toString();
			LOG.info("startdate::"+formattedDate);
			jnjGTOADeliveryListReportForm.setStartDate(formattedDate);
		
			defaultDate= new Date();
			formattedDate = dateFormat.format(defaultDate).toString();
			LOG.info("enddate::"+formattedDate);
			jnjGTOADeliveryListReportForm.setEndDate(formattedDate);
		}
		
		/*model.addAttribute("startDate", jnjGTOADeliveryListReportForm.getStartDate());
		model.addAttribute("endDate", jnjGTOADeliveryListReportForm.getEndDate());*/
		
		/** In case of multiple purchase analysis report **/
		model.addAttribute(OA_DELIVERY_LIST_FORM_NAME, jnjGTOADeliveryListReportForm);
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"OA Delivery List REPORT - JnjGTOADeliveryListReportForm has been set in the model!");
		
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);

		/** Setting up the model **/
		setupModel(model, CMS_OA_DELIVERY_LIST_REPORT_PAGE);//TODO 
		
		//TODO - account change - All or specific
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		// 4603 by surabhi
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.DELIVERY_LIST);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.DELIVERY_LIST);
		//end
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
	}

	
	/**
	 * This method is responsible for handling the Search feature on Inventory Reports Page.
	 *
	 * @param model
	 * @param JnjGTSinglePurchaseAnalysisReportForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/orderAnalysis/single")
	public String fetchSinglePurchaseAnalysisReport(final Model model,
			@ModelAttribute final JnjGTSinglePurchaseAnalysisReportForm JnjGTSinglePurchaseAnalysisReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		/** Setting the ordered from drop-down values **/
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));

		/** Setting the form in the model **/
		model.addAttribute(SINGLE_PURCHASE_ANALYSIS_FORM_NAME, JnjGTSinglePurchaseAnalysisReportForm);

		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		/**
		 * Generate Single Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		generateSinglePurchaseAnalysisResponse(model, populatePageableData(JnjGTSinglePurchaseAnalysisReportForm));

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.SINGLE_PRODUCT_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, JnjGTSinglePurchaseAnalysisReportForm.getAccountsSelectedValue());

		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
		//return "";
	}

	/**
	 *
	 * This method shows the entries for the selected period for the single purchase analysis report.
	 *
	 * @param model
	 * @param periodFrom
	 * @param periodTo
	 * @param accountId
	 * @param productCode
	 * @return view
	 */
	@PostMapping("/purchaseAnalysis/single/entries")
	public String showEntriesForSinglePurchaseAnalysisReport(final Model model,
			@RequestParam("periodFrom") final String periodFrom, @RequestParam("periodTo") final String periodTo,
			@RequestParam("accountId") final String accountId, @RequestParam("productCode") final String productCode,
			@RequestParam("orderedFrom") final String orderedFrom, @RequestParam("lotNumber") final String lotNumber)
	{
		final String METHOD_NAME = "showEntriesForSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		model.addAttribute(PERIOD, periodFrom + JnjglobalreportsConstants.SPACE + Logging.HYPHEN + JnjglobalreportsConstants.SPACE + periodTo);
		model.addAttribute(ACCOUNT, accountId);

		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		/** Generate entries Response and set it in model by passing the page-able data by calling populatePageableData **/
		generatePurchaseAnalysisEntriesResponse(model,
				populatePageableData(periodFrom, periodTo, accountId, productCode, orderedFrom, lotNumber, model));

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		//return ControllerConstants.Views.Pages.Reports.PurchaseAnalysisOrderEntriesPage;
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.PurchaseAnalysisOrderEntriesPage);
	}

	/**
	 * This method generates the response for the purchase analysis entries
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generatePurchaseAnalysisEntriesResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generatePurchaseAnalysisEntriesResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade fetchSinglePurchaseAnalysisOrderEntries.");

			/** calling facade layer to fetch the entries **/
			final List<JnjGTSinglePurchaseAnalysisEntriesData> jnjGTSinglePurchaseAnalysisEntriesDataList = jnjGTReportsFacade
					.fetchSinglePurchaseAnalysisOrderEntries(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTSinglePurchaseAnalysisEntriesDataList
					&& CollectionUtils.isNotEmpty(jnjGTSinglePurchaseAnalysisEntriesDataList))
			{
				/** Setting the jnjGTBackorderReportResponseDataList in the model **/
				model.addAttribute(SINGLE_PURCHASE_ANALYSIS_ENTRIES_LIST, jnjGTSinglePurchaseAnalysisEntriesDataList);

				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTSinglePurchaseAnalysisEntriesDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTSinglePurchaseAnalysisEntriesDataList was found to be null or empty!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method populates the page-able data for the single purchase analysis order entries search
	 *
	 * @param periodFrom
	 * @param periodTo
	 * @param accountId
	 * @param productCode
	 * @param orderedFrom
	 * @param lotNumber
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final String periodFrom, final String periodTo, final String accountId,
			final String productCode, final String orderedFrom, final String lotNumber, final Model model)
	{
		final String METHOD_NAME = "showEntriesForSinglePurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		JnjGTPageableData jnjGTPageableData = null;

		/** Populating the jnjGTPageableData **/
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
		/** Mandatory Form elements null check **/
		if (null != periodFrom && null != periodTo && null != accountId && null != productCode)
		{
			jnjGTPageableData = new JnjGTPageableData();
			/** Splitting accounts by comma and converting into array list **/
			jnjGTPageableData.setSearchParamsList(Arrays.asList(accountId.split(JnjglobalreportsConstants.CONST_COMMA)));
			jnjGTPageableData.setSearchBy(productCode);
			jnjGTPageableData.setSearchText(lotNumber);
			jnjGTPageableData.setFromDate(periodFrom);
			jnjGTPageableData.setToDate(periodTo);
			/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
			final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
			final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
					: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
			jnjGTPageableData.setStatus(jnjGTReportsFacade.fetchValuesFromConfig(orderedFromId, orderedFrom));

			/** Converting the product code to the corresponding PK and retrieving the product info as well **/
			model.addAttribute(PRODUCT_INFO, jnjGTReportsFacade.convertProductCodeToPK(jnjGTPageableData, true));
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData has been populated and the product info is also set in the model");
		}
		else
		{
			/** One or more form elements were null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData not populated as one or more form elements were null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 *
	 * This method is responsible for handling the Search feature on multi purchase Reports Page.
	 *
	 * @param model
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/orderAnalysis/multi")
	public String fetchMultiPurchaseAnalysisReport(final Model model,
			@ModelAttribute final JnjGTMultiPurchaseAnalysisReportForm JnjGTMultiPurchaseAnalysisReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchMultiPurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.MULTI_PRODUCT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);


		/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		/** Setting the ordered from drop-down values **/
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));
		model.addAttribute(ANALYSIS_VARIABLE,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.ANALYSIS_VARIABLE));
		model.addAttribute(PRODUCT_DISPLAY,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY));
		//Check which type of site is used to fetch the territory dropdown
		final String currentSite = jnjGTUnitFacade.getSourceSystemIdForUnit();
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		model.addAttribute(OPERATING_COMPANY, jnjGTReportsFacade.getOperatingCompanyDropdown(currentSite));
		model.addAttribute(TERRITORIES, jnjGTReportsFacade.getTerritoryForSalesRep(currentSite));
		/*inventry report check*/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(divisionData.isIsMitek() || divisionData.isIsCodman()){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}

		if (StringUtils.isNotEmpty(JnjGTMultiPurchaseAnalysisReportForm.getFranchiseDivCode()))
		{
			model.addAttribute(FRANCHISE_CODES,
					jnjGTReportsFacade.getFranchiseOrDivCode(JnjGTMultiPurchaseAnalysisReportForm.getOperatingCompany()));
		}
		/** Setting the form in the model **/
		model.addAttribute(MULTI_PURCHASE_ANALYSIS_FORM_NAME, JnjGTMultiPurchaseAnalysisReportForm);

		/**
		 * Generate Multi Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		generateMultiPurchaseAnalysisResponse(model, populatePageableData(JnjGTMultiPurchaseAnalysisReportForm));

		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("startDate", JnjGTMultiPurchaseAnalysisReportForm.getStartDate());
		model.addAttribute("endDate", JnjGTMultiPurchaseAnalysisReportForm.getEndDate());
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.MULTI_PRODUCT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.MULTI_PRODUCT_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, JnjGTMultiPurchaseAnalysisReportForm.getAccountsSelectedValue());

		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
		//return "";
	}
	
	
	/**
	 *
	 * AAOL-2401: This method is responsible for handling the Search feature on sales Reports Page.
	 *
	 * @param model
	 * @param JnjGTSalesReportAnalysisForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/orderAnalysis/salesreport")
	public String fetchSalesReportPageReport(final Model model,
			@ModelAttribute final JnjGTSalesReportAnalysisForm JnjGTSalesReportAnalysisForm) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchSalesReportPageReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.SALES_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		
		/** Setting the form in the model **/
		model.addAttribute(SALES_REPORT_ORDER_ANALYSIS_FORM, JnjGTSalesReportAnalysisForm);

		/**
		 * Generate Multi Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		generateSalesReportPageResponse(model, populatePageableData(JnjGTSalesReportAnalysisForm));
		
		final List<OrderTypesForm> orderTypes = new ArrayList<>();
		final List<SelectOption> orderStatus = new ArrayList<>();
		/*** Populate Order types only those listed in requirements and not all. ***/
		populateOrderTypes(orderTypes);
		populateOrderStatus(orderStatus);
		model.addAttribute("orderTypes",orderTypes);
		model.addAttribute("orderStatus",orderStatus);
		model.addAttribute(FRANCHISE_CODES,jnjGTReportsFacade.getFranchiseOrDivCode());
		
		/** Setting up the model **/
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("startDate", JnjGTSalesReportAnalysisForm.getStartDate());
		model.addAttribute("endDate", JnjGTSalesReportAnalysisForm.getEndDate());
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.SALES_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.SALES_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, JnjGTSalesReportAnalysisForm.getAccountsSelectedValue());
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
		//return "";
	}
	
	
	
	/**
	 * AAOL-2410: This method populates the jnjGTPageableData for JnjGTMultiPurchaseAnalysisReportForm
	 *
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return JnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTSalesReportAnalysisForm JnjGTSalesReportAnalysisForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;
		JnjGTSearchDTO searchDTO = null;
		List<JnjGTSearchDTO> searchList = null;
		
		/** Form class null check JnjGTSinglePurchaseAnalysisReportForm **/
		if (null != JnjGTSalesReportAnalysisForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the JnjGTSalesReportAnalysisForm!");
			/** Mandatory Form elements null check **/
			if ( null != JnjGTSalesReportAnalysisForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				 searchList = new ArrayList<JnjGTSearchDTO>();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTSalesReportAnalysisForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				if(null != JnjGTSalesReportAnalysisForm.getStartDate()){
					jnjGTPageableData.setFromDate(JnjGTSalesReportAnalysisForm.getStartDate());
				}
				if(null != JnjGTSalesReportAnalysisForm.getEndDate()){
					jnjGTPageableData.setToDate(JnjGTSalesReportAnalysisForm.getEndDate());
				}

			//	jnjGTPageableData.setSearchBy(JnjGTSalesReportAnalysisForm.getProductCode()); 
				jnjGTPageableData.setStatus(JnjGTSalesReportAnalysisForm.getStatus());
				jnjGTPageableData.setSearchText(JnjGTSalesReportAnalysisForm.getOrderType());
				jnjGTPageableData.setAdditionalSearchText(JnjGTSalesReportAnalysisForm.getFranchiseDesc());
				
				if(JnjGTSalesReportAnalysisForm.getCustomerPONo() != null){
					searchDTO = new JnjGTSearchDTO();
					searchDTO.setSearchBy("CustomerPONO");
					searchDTO.setSearchValue(JnjGTSalesReportAnalysisForm.getCustomerPONo().trim());
					searchList.add(searchDTO);
				}
				
				if(JnjGTSalesReportAnalysisForm.getDeliveryNo() != null){
					searchDTO = new JnjGTSearchDTO();
					searchDTO.setSearchBy("DeliveryNo");
					searchDTO.setSearchValue(JnjGTSalesReportAnalysisForm.getDeliveryNo().trim());
					searchList.add(searchDTO);
				}
				if(JnjGTSalesReportAnalysisForm.getSalesDocNo() != null){
					searchDTO = new JnjGTSearchDTO();
					searchDTO.setSearchBy("SalesDocNo");
					searchDTO.setSearchValue(JnjGTSalesReportAnalysisForm.getSalesDocNo().trim());
					searchList.add(searchDTO);
				}
				
				if(JnjGTSalesReportAnalysisForm.getInvoiceNo() != null){
					searchDTO = new JnjGTSearchDTO();
					searchDTO.setSearchBy("InvoiceNo");
					searchDTO.setSearchValue(JnjGTSalesReportAnalysisForm.getInvoiceNo().trim());
					searchList.add(searchDTO);
				}
				if(JnjGTSalesReportAnalysisForm.getProductCode() != null){
					searchDTO = new JnjGTSearchDTO();
					searchDTO.setSearchBy("ProductCode");
					searchDTO.setSearchValue(JnjGTSalesReportAnalysisForm.getProductCode().trim());
					searchList.add(searchDTO);
				}
				
				
				jnjGTPageableData.setSearchDtoList(searchList);				
		
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more mandatory form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTMultiPurchaseAnalysisReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTMultiPurchaseAnalysisReportForm was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 *
	 * AAOL-4603: This method is responsible for handling the Search feature on Order Analysis Delivery List Reports Page.
	 *
	 * @param model
	 * @param JnjGTOADeliveryListReportForm
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/orderAnalysis/deliveryList")
	public String fetchOADeliveryReport(final Model model,
			@ModelAttribute final JnjGTOADeliveryListReportForm jnjGTOADeliveryListReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchOADeliveryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_ORDER_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.ORDER_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.DELIVERY_LIST_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());		
		if(StringUtils.isEmpty(jnjGTOADeliveryListReportForm.getStartDate()) || StringUtils.isEmpty(jnjGTOADeliveryListReportForm.getEndDate())){
			Date defaultDate= DateUtils.addMonths(new Date(), -1);		
			String formattedDate = dateFormat.format(defaultDate).toString();
			LOG.info("startdate::"+formattedDate);
			jnjGTOADeliveryListReportForm.setStartDate(formattedDate);
		
			defaultDate= new Date();
			formattedDate = dateFormat.format(defaultDate).toString();
			LOG.info("enddate::"+formattedDate);
			jnjGTOADeliveryListReportForm.setEndDate(formattedDate);
		}

		generateOADeliveryListResponse(model, populatePageableData(jnjGTOADeliveryListReportForm));

		final List<OrderTypesForm> orderTypes = new ArrayList<>();
		/*** Populate Order types only those listed in requirements and not all. ***/
		populateOrderTypes(orderTypes);
		model.addAttribute(ORDER_TYPES,orderTypes);
		
		/** Setting franchise drop down values **/		
		model.addAttribute(FRANCHISE_CODES,
					jnjGTReportsFacade.getFranchiseOrDivCode());
		model.addAttribute(CATEGORYDATA,jnjGTCustomerFacade.getUserFranchise());
		/** Setting the form in the model **/
		model.addAttribute(OA_DELIVERY_LIST_FORM_NAME, jnjGTOADeliveryListReportForm);

		/** Setting up the model **/
		setupModel(model, CMS_OA_DELIVERY_LIST_REPORT_PAGE);//TODO - CMS_OA_DELIVERY_LIST_REPORT_PAGE
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		/*model.addAttribute("startDate", jnjGTOADeliveryListReportForm.getStartDate());
		model.addAttribute("endDate", jnjGTOADeliveryListReportForm.getEndDate());*/
		
		// 4603 by surabhi
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.DELIVERY_LIST);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getOrderReportsTypeDropdownVaulesInMap());
		
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.ORDER_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.DELIVERY_LIST);
		//end
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGTOADeliveryListReportForm.getAccountsSelectedValue());

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.OrderAnalysisReportPage);
		//return "";
	}


	/**
	 *
	 * This method is responsible for handling the Search feature on Single Purchase Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/inventoryAnalysis/inventoryReport")
	public String fetchInventoryReport(final Model model, @ModelAttribute final JnjGTInventoryReportForm JnjGTInventoryReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchInventoryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		//inventry report check
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if(null!= divisionData &&( divisionData.isIsMitek() || divisionData.isIsCodman())){
		
			model.addAttribute("inventry", Boolean.TRUE);	
		}else{
			model.addAttribute("inventry", Boolean.FALSE);	
			
		}
		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);

		/** Setting the form in the model **/
		model.addAttribute(INVENTORY_FORM_NAME, JnjGTInventoryReportForm);

		model.addAttribute("selectedAccounts", JnjGTInventoryReportForm.getRepUCNs());

		/** Generate Inventory Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateInventoryResponse(model, populatePageableData(JnjGTInventoryReportForm));

		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVENTORY_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//Report categorization
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVENTORY_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVENTORY_REPORT);

		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, JnjGTInventoryReportForm.getAccountsSelectedValue());

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
		//return "";
	}

	
	/**
	 *
	 * This method is responsible for handling the Search feature on Single Purchase Analysis Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/financialSummary")
	public String fetchFinancialSummaryReport(final Model model, @ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialSummaryReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		
		/** Checking if not MDD, redirect to root node of Reports **/
		if (!(LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			return REDIRECT_PREFIX + REPORTS_ROOT_PAGE_URL;
		}

		/** Creating bread-crumbs **/
		createBreadCrumbsForReports(null, model, jnjCommonFacadeUtil.getMessageFromImpex(INVENTORY_LABEL_STRING),null,null);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, jnjGlobalFinancialSummaryReportForm);

		//model.addAttribute("selectedAccounts", jnjGlobalFinancialSummaryReportForm.getRepUCNs());

		/** Generate Financial Summer Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateInventoryResponse(model, populatePageableData(jnjGlobalFinancialSummaryReportForm));

		/** Fetching UCNs to check if the change link should be displayed **/
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			/** Setting the flag to display the change link **/
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}

		/** Setting up the model **/
		setupModel(model, CMS_INVENTORY_REPORT_PAGE);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getFinancialReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.FINANCIAL_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.FINANCIAL_SUMMARY);
		
		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.FinancialAnalysisReportPage);
	}

	/**
	 * THis method generates the Back-Order response by calling the facade layer and sets the same in the model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateBackorderResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateBackorderResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch backorder report.");

			/** calling facade layer to fetch the back-order report data **/
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = jnjGTReportsFacade
					.fetchBackOrderReport(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTBackorderReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTBackorderReportResponseDataList))
			{
				/** Setting the jnjGTBackorderReportResponseDataList in the model **/
				model.addAttribute(BACKORDER_RESPONSE_DATA_LIST, jnjGTBackorderReportResponseDataList);

				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTBackorderReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTBackorderReportResponseDataList was found to be null or empty! Moving to regular backorder page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular backorder page load!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method generates the Inventory response by calling the facade layer and sets the same in the model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateInventoryResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateInventoryResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch inventory report.");

			/** calling facade layer to fetch the back-order report data **/
			final List<JnjGTInventoryReportResponseData> jnjGTInventoryReportResponseDataList = jnjGTReportsFacade
					.fetchInventoryDataReport(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTInventoryReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTInventoryReportResponseDataList))
			{
				/** Setting the jnjGTInventoryReportResponseDataList in the model **/
				model.addAttribute(INVENTORY_RESPONSE_DATA_LIST, jnjGTInventoryReportResponseDataList);

				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInventoryReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInventoryReportResponseDataList was found to be null or empty! Moving to regular inventory page load!");
				/** Set current B2B Unit Id in Model **/
				//setCurrentB2BUnitIdInModel(model);
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular inventory page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	/**
	 * This method generates the Inventory response by calling the facade layer and sets the same in the model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateFinancialSummaryResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateFinancialSummaryResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch inventory report.");

			/** calling facade layer to fetch the back-order report data **/
			/*final List<JnjGTFinancialSummaryReportData> jnjGTFinancialSummaryReportResponseDataList = null;
					//jnjGTReportsFacade.fetchFinancialSummaryReport(jnjGTPageableData);

			*//** Checking if the facade layer returned null or empty order report data **//*
			if (null != jnjGTFinancialSummaryReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTFinancialSummaryReportResponseDataList))
			{
				*//** Setting the jnjGTFinancialSummaryReportResponseDataList in the model **//*
				model.addAttribute(FINANCIAL_SUMMARY_RESPONSE_DATA_LIST, jnjGTFinancialSummaryReportResponseDataList);

				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTInventoryReportResponseDataList has been set in the model!");
			}
			else
			{
				*//** Adding no data flag in the model **//*
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTFinancialSummaryReportResponseDataList was found to be null or empty! Moving to regular inventory page load!");
				*//** Set current B2B Unit Id in Model **//*
				setCurrentB2BUnitIdInModel(model);
			}*/
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular inventory page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * THis method generates the Single Purchase Analysis response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateSinglePurchaseAnalysisResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateSinglePurchaseAnalysisResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Single Purchase Analysis report.");

			/** calling facade layer to fetch the back-order report data **/
			final TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> jnjGTSinglePurchaseOrderReportResponseDataMap = jnjGTReportsFacade
					.fetchSinglePurchaseAnalysisReport(jnjGTPageableData);

			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTSinglePurchaseOrderReportResponseDataMap && jnjGTSinglePurchaseOrderReportResponseDataMap.size() != 0)
			{
				if (jnjGTSinglePurchaseOrderReportResponseDataMap.containsKey(INVALID_PRODUCT))
				{
					model.addAttribute(INVALID_PRODUCT, Boolean.TRUE);
				}
				else
				{
					/** Setting the jnjGTSinglePurchaseOrderReportResponseDataMap in the model **/
					model.addAttribute(SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP, jnjGTSinglePurchaseOrderReportResponseDataMap);

					logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
							"jnjGTSinglePurchaseOrderReportResponseDataMap has been set in the model!");
				}
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTSinglePurchaseOrderReportResponseDataMap was found to be null or empty! Moving to regular inventory page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular Single Purchase Analysis page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	
	
	

	/**
	 * THis method generates the Single Purchase Analysis response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateMultiPurchaseAnalysisResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateMultiPurchaseAnalysisResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Multi Purchase Analysis report.");

			/** calling facade layer to fetch the back-order report data **/
			//List<JnjGTMultiPurchaseOrderReportResponseData>
			final Map<String, Object> outputMap = jnjGTReportsFacade.fetchMultiPurchaseAnalysisReport(jnjGTPageableData);
			final List<JnjGTMultiPurchaseOrderReportResponseData> jnjGTMultiPurchaseOrderReportResponseDataList = (List<JnjGTMultiPurchaseOrderReportResponseData>) outputMap
					.get(RESPONSE_DATA);
			final Map<String, Double> totalSpendingMap = (Map<String, Double>) outputMap.get(TOTAL_SPENDING_MAP);
			final PriceData totalAmountSpend = (PriceData) outputMap.get(SEARCH_TOTAL);
			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTMultiPurchaseOrderReportResponseDataList
					&& CollectionUtils.isNotEmpty(jnjGTMultiPurchaseOrderReportResponseDataList))
			{
				/** Setting the jnjGTMultiPurchaseOrderReportResponseDataList in the model **/
				model.addAttribute(MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST, jnjGTMultiPurchaseOrderReportResponseDataList);
				model.addAttribute("size", jnjGTMultiPurchaseOrderReportResponseDataList.size());
				/** Setting the account based total spending map in the model **/
				model.addAttribute("size", jnjGTMultiPurchaseOrderReportResponseDataList.size());
				model.addAttribute(TOTAL_SPENDING_MAP, totalSpendingMap);
				model.addAttribute(SEARCH_TOTAL, totalAmountSpend);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTMultiPurchaseOrderReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(
						LoginaddonConstants.Logging.REPORTS_NAME,
						METHOD_NAME,
						"jnjGTMultiPurchaseOrderReportResponseDataList was found to be null or empty! Moving to regular multi-product PA report page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular Multi Purchase Analysis page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	/**
	 * AAOL-2410: This method generates the Single Purchase Analysis response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateSalesReportPageResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateSalesReportPageResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Multi Purchase Analysis report.");

			/** calling facade layer to fetch the back-order report data **/
			//List<JnjGTMultiPurchaseOrderReportResponseData>
			final TreeMap<String, List<JnjGTSalesReportResponseData>> outputMap = jnjGTReportsFacade.fetchSalesReport(jnjGTPageableData);
			
			
			
			final List<JnjGTSalesReportResponseData> jnjGTSalesReportResponseDataList = (List<JnjGTSalesReportResponseData>) outputMap
					.get(RESPONSE_DATA);
			//** Checking if the facade layer returned null or empty order report data **//*
			if (null != jnjGTSalesReportResponseDataList
					&& CollectionUtils.isNotEmpty(jnjGTSalesReportResponseDataList))
			{
				//** Setting the jnjGTMultiPurchaseOrderReportResponseDataList in the model **//*
				model.addAttribute(SALES_ORDER_ANALYSIS_RESPONSE_DATA_LIST, jnjGTSalesReportResponseDataList);
				//** Setting the account based total spending map in the model **//*
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTSalesReportResponseDataList has been set in the model!");
			}
			else
			{
				//** Adding no data flag in the model **//*
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(
						LoginaddonConstants.Logging.REPORTS_NAME,
						METHOD_NAME,
						"jnjGTSalesReportResponseDataList was found to be null or empty! Moving to regular multi-product PA report page load!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular Multi Purchase Analysis page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}


	/**
	 * AAOL-4603 for ASPAC
	 * THis method generates the Order Analysis for Delivery List response by calling the facade layer and sets the same in the
	 * model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateOADeliveryListResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateOADeliveryListResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** jnjGTPageableData null check **/
		/*if (null != jnjGTPageableData)
		{*/
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch Order Analysis for Delivery List report.");

			/** calling facade layer to fetch the order analysis for delivery list report data **/
			final Map<String, Object> outputMap = jnjGTReportsFacade.fetchOADeliveryListReport(jnjGTPageableData);
			//TODO JnjGTOADeliveryListReportResponseData
			final List<JnjGTOADeliveryListReportResponseData> jnjGTOADeliveryListReportResponseDataList = (List<JnjGTOADeliveryListReportResponseData>) outputMap
					.get(RESPONSE_DATA); //TODO RESPONSE DATA
			
			//TODO - find the search result
			/*final Map<String, Double> totalSpendingMap = (Map<String, Double>) outputMap.get(TOTAL_SPENDING_MAP);
			final PriceData totalAmountSpend = (PriceData) outputMap.get(SEARCH_TOTAL);
			*/
			/** Checking if the facade layer returned null or empty order report data **/
			if (null != jnjGTOADeliveryListReportResponseDataList
					&& CollectionUtils.isNotEmpty(jnjGTOADeliveryListReportResponseDataList))
			{
				/** Setting the jnjGTOADeliveryListReportResponseDataList in the model **/
				model.addAttribute(OA_DELIVERY_LIST_RESPONSE_DATA_LIST, jnjGTOADeliveryListReportResponseDataList);
				/** Setting the account based total spending map in the model **/
				/*model.addAttribute(TOTAL_SPENDING_MAP, totalSpendingMap);
				model.addAttribute(SEARCH_TOTAL, totalAmountSpend);
				*/logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTOADeliveryListReportResponseDataList has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(
						LoginaddonConstants.Logging.REPORTS_NAME,
						METHOD_NAME,
						"jnjGTOADeliveryListReportResponseDataList was found to be null or empty! Moving to regular Order Analysis for Delivery Report page load!");
			}
		/*}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular Order Analysis for Delivery Report page load!");*/
			/** Set current B2B Unit Id in Model **/
			/*setCurrentB2BUnitIdInModel(model);
		}*/
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}


	/**	
	 * This method populates the jnjGTPageableData for JnjGTBackorderReportForm
	 *
	 * @param JnjGTBackorderReportForm
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTBackorderReportForm JnjGTBackorderReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTBackorderReportForm **/
		if (null != JnjGTBackorderReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			/*if (null != JnjGTBackorderReportForm.getAccountIds() && null != JnjGTBackorderReportForm.getFromDate()
					&& null != JnjGTBackorderReportForm.getToDate())*/
			if (null != JnjGTBackorderReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTBackorderReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				jnjGTPageableData.setFromDate(JnjGTBackorderReportForm.getFromDate());
				jnjGTPageableData.setToDate(JnjGTBackorderReportForm.getToDate());
				jnjGTPageableData.setSearchBy(JnjGTBackorderReportForm.getProductCode());
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTBackorderReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTBackorderReportForm was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 * This method populates the jnjGTPageableData for JnjGTInventoryReportForm
	 *
	 * @param JnjGTInventoryReportForm
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTInventoryReportForm JnjGTInventoryReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTInventoryReportForm **/
		if (null != JnjGTInventoryReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Form elements null check **/
			if (null != JnjGTInventoryReportForm.getRepUCNs() && null != JnjGTInventoryReportForm.getProductCode()
					&& null != JnjGTInventoryReportForm.getLotNumber())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTInventoryReportForm.getRepUCNs().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				jnjGTPageableData.setSearchBy(JnjGTInventoryReportForm.getProductCode());
				jnjGTPageableData.setSearchText(JnjGTInventoryReportForm.getLotNumber());
				jnjGTPageableData.setSearchFlag(JnjGTInventoryReportForm.isDisplayZeroStocks());
				jnjGTPageableData.setFromDate(JnjGTInventoryReportForm.getStartDate());
				jnjGTPageableData.setToDate(JnjGTInventoryReportForm.getEndDate());
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTInventoryReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTInventoryReportForm was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 * This method populates the jnjGTPageableData for JnjGTSinglePurchaseAnalysisReportForm
	 *
	 * @param JnjGTSinglePurchaseAnalysisReportForm
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(
			final JnjGTSinglePurchaseAnalysisReportForm JnjGTSinglePurchaseAnalysisReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTSinglePurchaseAnalysisReportForm **/
		if (null != JnjGTSinglePurchaseAnalysisReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Mandatory Form elements null check **/
			if (null != JnjGTSinglePurchaseAnalysisReportForm.getProductCode()
					&& null != JnjGTSinglePurchaseAnalysisReportForm.getStartDate()
					&& null != JnjGTSinglePurchaseAnalysisReportForm.getEndDate()
					&& null != JnjGTSinglePurchaseAnalysisReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTSinglePurchaseAnalysisReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				jnjGTPageableData.setSearchBy(JnjGTSinglePurchaseAnalysisReportForm.getProductCode());
				jnjGTPageableData.setSearchText(JnjGTSinglePurchaseAnalysisReportForm.getLotNumber());
				jnjGTPageableData.setFromDate(JnjGTSinglePurchaseAnalysisReportForm.getStartDate());
				jnjGTPageableData.setToDate(JnjGTSinglePurchaseAnalysisReportForm.getEndDate());
				jnjGTPageableData.setAdditionalSearchText(JnjGTSinglePurchaseAnalysisReportForm.getPeriodBreakdown());

				if (ALL.equalsIgnoreCase(JnjGTSinglePurchaseAnalysisReportForm.getOrderedFrom()))
				{
					jnjGTPageableData.setStatus(null);
				}
				else
				{
					/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
					final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
					final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
							: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
					/** Fetching the ordered from channel codes for the key specified **/
					jnjGTPageableData.setStatus(jnjGTReportsFacade.fetchValuesFromConfig(orderedFromId,
							JnjGTSinglePurchaseAnalysisReportForm.getOrderedFrom()));
				}
				final boolean isOthersOrderedFrom = JnjGTSinglePurchaseAnalysisReportForm.getOrderedFrom().equalsIgnoreCase(
						"consOrderedFrom4");
				jnjGTPageableData.setSearchFlag(isOthersOrderedFrom);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");
			}
			else
			{
				/** One or more form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTSinglePurchaseAnalysisReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTSinglePurchaseAnalysisReportForm was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 * This method populates the jnjGTPageableData for JnjGTMultiPurchaseAnalysisReportForm
	 *
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return JnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTMultiPurchaseAnalysisReportForm JnjGTMultiPurchaseAnalysisReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTSinglePurchaseAnalysisReportForm **/
		if (null != JnjGTMultiPurchaseAnalysisReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
			/** Mandatory Form elements null check **/
			if (null != JnjGTMultiPurchaseAnalysisReportForm.getStartDate()
					&& null != JnjGTMultiPurchaseAnalysisReportForm.getEndDate()
					&& null != JnjGTMultiPurchaseAnalysisReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTMultiPurchaseAnalysisReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				jnjGTPageableData.setFromDate(JnjGTMultiPurchaseAnalysisReportForm.getStartDate());
				jnjGTPageableData.setToDate(JnjGTMultiPurchaseAnalysisReportForm.getEndDate());
				if (ALL.equalsIgnoreCase(JnjGTMultiPurchaseAnalysisReportForm.getOrderedFrom()))
				{
					jnjGTPageableData.setSearchBy(null);
				}
				else
				{
					/** Checking if current site is MDD or not, and fetching the ordered from list accordingly **/
					final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
					final String orderedFromId = siteName.equals(LoginaddonConstants.MDD) ? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
							: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
					/** Fetching the ordered from channel codes for the key specified **/
					jnjGTPageableData.setSearchBy(jnjGTReportsFacade.fetchValuesFromConfig(orderedFromId,
							JnjGTMultiPurchaseAnalysisReportForm.getOrderedFrom()));
				}
				final boolean isOthersOrderedFrom = JnjGTMultiPurchaseAnalysisReportForm.getOrderedFrom().equalsIgnoreCase(
						"consOrderedFrom4");
				jnjGTPageableData.setSearchFlag(isOthersOrderedFrom);
				final String operatingCompany = JnjGTMultiPurchaseAnalysisReportForm.getOperatingCompany();
				/*final String operatingCompany = JnjGTMultiPurchaseAnalysisReportForm.getOperatingCompany().equalsIgnoreCase(ALL) ? null
						: JnjGTMultiPurchaseAnalysisReportForm.getOperatingCompany();*/
				jnjGTPageableData.setSearchText(operatingCompany);
				final String franchiseCode = JnjGTMultiPurchaseAnalysisReportForm.getFranchiseDivCode().equals(ALL) ? null
						: JnjGTMultiPurchaseAnalysisReportForm.getFranchiseDivCode();
				jnjGTPageableData.setAdditionalSearchText(franchiseCode);
				if(JnjGTMultiPurchaseAnalysisReportForm.getTerritory()!=null)
				{
				final String territory = JnjGTMultiPurchaseAnalysisReportForm.getTerritory().equals(ALL) ? null
						: JnjGTMultiPurchaseAnalysisReportForm.getTerritory();
				jnjGTPageableData.setStatus(territory);
				}
				String analysisVariable = null;
				switch (JnjGTMultiPurchaseAnalysisReportForm.getAnalysisVariable())
				{
					case "analysisVariable2":
						analysisVariable = "quantity";
						break;
					case "analysisVariable3":
						analysisVariable = "frequency";
						break;
					default:
						analysisVariable = "amount";
						break;
				}
				jnjGTPageableData.setSort(analysisVariable);
				int productsCount = 0;
				if (!ALL.equalsIgnoreCase(JnjGTMultiPurchaseAnalysisReportForm.getProductsToDisplay()))
				{
					final String productCountStr = jnjGTReportsFacade.fetchValuesFromConfig(
							JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY,
							JnjGTMultiPurchaseAnalysisReportForm.getProductsToDisplay());
					productsCount = Integer.parseInt(productCountStr);
				}
				jnjGTPageableData.setPageSize(productsCount);
			}
			else
			{
				/** One or more mandatory form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			/** JnjGTMultiPurchaseAnalysisReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTMultiPurchaseAnalysisReportForm was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	
	/**
	 * AAOL-4603: This method populates the jnjGTPageableData for JnjGTOADeliveryListReportForm
	 *
	 * @param JnjGTOADeliveryListReportForm
	 * @return JnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTOADeliveryListReportForm JnjGTOADeliveryListReportForm)
	{
		final String METHOD_NAME = "populatePageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;

		/** Form class null check JnjGTSinglePurchaseAnalysisReportForm **/
		if (null != JnjGTOADeliveryListReportForm)
		{
			/** Populating the jnjGTPageableData **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
				jnjGTPageableData = new JnjGTPageableData();
				/** Splitting accounts by comma and converting into array list **/
				jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTOADeliveryListReportForm.getAccountIds().split(
						JnjglobalreportsConstants.CONST_COMMA)));
				jnjGTPageableData.setFromDate(JnjGTOADeliveryListReportForm.getStartDate());
				jnjGTPageableData.setToDate(JnjGTOADeliveryListReportForm.getEndDate());
				
				List<JnjGTSearchDTO> searchDtoList = new ArrayList();
				JnjGTSearchDTO dldto = null; 
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("pcode");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getProductCode());
				searchDtoList.add(dldto);
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("custPONum");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getCustPONum());
				searchDtoList.add(dldto);
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("salesDocNum");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getSalesDocNum());
				searchDtoList.add(dldto);
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("deliveryNum");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getDeliveryNum());
				searchDtoList.add(dldto);
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("orderType");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getOrderType());
				searchDtoList.add(dldto);
				
				dldto= new JnjGTSearchDTO();
				dldto.setSearchBy("franchiseDesc");
				dldto.setSearchValue(JnjGTOADeliveryListReportForm.getFranchiseDesc());
				searchDtoList.add(dldto);

				jnjGTPageableData.setSearchDtoList(searchDtoList);
				/** One or more mandatory form elements were null **/
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
		}
		else
		{
			/** JnjGTOADeliveryListReportForm found to be null **/
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTOADeliveryListReportForm was found to be null!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the back-order report search query
	 *
	 * @param model
	 * @param JnjGTBackorderReportForm
	 * @return download view
	 */
	@PostMapping("/backorder/downloadReport")
	public String downloadBackorderReport(final Model model,
			@ModelAttribute final JnjGTBackorderReportForm JnjGTBackorderReportForm)
	{
		final String METHOD_NAME = "downloadBackorderReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the form in the model **/
		model.addAttribute(BACKORDER_FORM_NAME, JnjGTBackorderReportForm);

		/** Generate Back-Order Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateBackorderResponse(model, populatePageableData(JnjGTBackorderReportForm));
		if (null != JnjGTBackorderReportForm.getAccountIds() && JnjGTBackorderReportForm.getAccountIds().length() > 0) {
			final String selectedAccountsString = "";
		final JnJB2BUnitModel accountNam=jnjGTCustomerFacade.getCurrentB2bUnit();			
		model.addAttribute("accountreportname",accountNam.getDisplayName());	
		}
		
		if(String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTBackorderReportForm.getDownloadType())){
		 List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
        if (mediaModel1 != null) {
               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
        }
        if (mediaModel2 != null) {
           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
        }
		}  
		}
		 /*site log */
	    final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
	    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
	    //Send site logo
	    /* Excel image adding Started here */
		model.addAttribute("siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		 /* Excel image adding end here */
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download.");

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTBackorderReportForm.getDownloadType())) ? BACKORDER_REPORT_PDF_VIEW
				: BACKORDER_REPORT_EXCEL_VIEW;
	}

	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the inventory report search query
	 *
	 * @param model
	 * @param JnjGTInventoryReportForm
	 * @return download view
	 */
	@PostMapping("/inventory/downloadReport")
	public String downloadInventoryReport(final Model model,
			@ModelAttribute final JnjGTInventoryReportForm JnjGTInventoryReportForm)
	{
		final String METHOD_NAME = "downloadInventoryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		model.addAttribute(INVENTORY_FORM_NAME, JnjGTInventoryReportForm);

		/** Generate Inventory Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateInventoryResponse(model, populatePageableData(JnjGTInventoryReportForm));
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download.");
		/* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
 	        if (mediaModel1 != null) {
 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
 	        }
 	        if (mediaModel2 != null) {
 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
 	        }
		}
		 /* Pdf image adding Started here */
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTInventoryReportForm.getDownloadType())) ? INVENTORY_REPORT_PDF_VIEW
				: INVENTORY_REPORT_EXCEL_VIEW;
	}

	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the back-order report search query
	 *
	 * @param model
	 * @param JnjGTBackorderReportForm
	 * @return download view
	 */
	@PostMapping("/financial/downloadReport")
	public String downloadFinancialReport(final Model model,
			@ModelAttribute final JnjGlobalFinancialAnalysisReportForm jnjGlobalFinancialAnalysisReportForm)
	{
		final String METHOD_NAME = "downloadFinancialReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_ANALYSIS_FORM_NAME, jnjGlobalFinancialAnalysisReportForm);

		 /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
 	        if (mediaModel1 != null) {
 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
 	        }
 	        if (mediaModel2 != null) {
 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
 	        }
		}
		 /* Pdf image adding Started here */
	
		 	/**
			 * Generate Financial Analysis Invoice Response and set it in model by passing the page-able data by calling
			 * populatePageableData
			 **/
		 generateFinancePurchaseAnalysisResponse(model, populatePageableData(jnjGlobalFinancialAnalysisReportForm));

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		System.out.println("Value of the string" + String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGlobalFinancialAnalysisReportForm.getDownloadType()));
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGlobalFinancialAnalysisReportForm.getDownloadType())) ? FINANCIAL_ANALYSIS_INVOICE_REPORT_PDF_VIEW
				: FINANCIAL_ANALYSIS_INVOICE_REPORT_EXCEL_VIEW;
	}
	
	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the back-order report search query
	 *
	 * @param model
	 * @param JnjGTBackorderReportForm
	 * @return download view
	 */
	@PostMapping("/financialSummary/downloadReport")
	public String downloadFinancialSummaryReport(final Model model,
			@ModelAttribute final JnjGlobalFinancialSummaryReportForm jnjGlobalFinancialAnalysisReportForm)
	{
		final String METHOD_NAME = "downloadFinancialSummaryReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_SUMMARY_FORM_NAME, jnjGlobalFinancialAnalysisReportForm);

		 /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		
		//System.out.println("downloadFinancialSummaryReport  ::::::: Payer Value Passed in Account Aging ::: " + payer);
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	        if (mediaModel1 != null) {
	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
	        }
	        if (mediaModel2 != null) {
	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
	        }
		}
		 /* Pdf image adding Started here */
	
		 	/**
			 * Generate Financial Summary Response and set it in model by passing the page-able data
			 * 
			 **/
		 
		 if(null != jnjGlobalFinancialAnalysisReportForm.getAccountAgingPayerID()){
			 JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
				List<String> searchParams = new ArrayList<>();
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getAccountAgingPayerID());
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getAccountIds());
				//add additional parameters here
				jnjGTPageableData.setSearchParamsList(searchParams);
			 List<JnjGTFinancialAccountAgingReportData> accountAgingReportDatasList = jnjGTReportsFacade.simulateAccountAgingReport(jnjGTPageableData);
			 if (null != accountAgingReportDatasList && CollectionUtils.isNotEmpty(accountAgingReportDatasList)) {
			 model.addAttribute("accountAgingPayerId", jnjGlobalFinancialAnalysisReportForm.getAccountAgingPayerID());
	 			model.addAttribute("accountAgingReportDatasList", accountAgingReportDatasList);
			 }
		 }
		 if(null != jnjGlobalFinancialAnalysisReportForm.getBalanceSummaryPayerID()){
			 JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
				List<String> searchParams = new ArrayList<>();
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getBalanceSummaryPayerID());
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getAccountIds());
				//add additional parameters here
				jnjGTPageableData.setSearchParamsList(searchParams);
			 List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportDatasList = jnjGTReportsFacade.simulateBalanceSummaryReport(jnjGTPageableData);
			 if (null != balanceSummaryReportDatasList && CollectionUtils.isNotEmpty(balanceSummaryReportDatasList)) {
			 model.addAttribute("balanceSummaryPayerID", jnjGlobalFinancialAnalysisReportForm.getBalanceSummaryPayerID());
				model.addAttribute("balanceSummaryReportDatasList", balanceSummaryReportDatasList);
			 }
		 }
		 if(null != jnjGlobalFinancialAnalysisReportForm.getPaymentSummaryPayerID()){
			 JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
				List<String> searchParams = new ArrayList<>();
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getPaymentSummaryPayerID());
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getAccountIds());
				//add additional parameters here
				jnjGTPageableData.setSearchParamsList(searchParams);
			 List<JnjGTFinancialPaymentSummaryReportData> paymentSummaryReportDatasList = jnjGTReportsFacade.simulatePaymentSummaryReport(jnjGTPageableData);
			 if (null != paymentSummaryReportDatasList && CollectionUtils.isNotEmpty(paymentSummaryReportDatasList)) {
			 model.addAttribute("paymentSummaryPayerID", jnjGlobalFinancialAnalysisReportForm.getPaymentSummaryPayerID());
				model.addAttribute("paymentSummaryReportDatasList", paymentSummaryReportDatasList);
			 }
		 }
		 if(null != jnjGlobalFinancialAnalysisReportForm.getCreditSummaryPayerID()){
			 JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
				List<String> searchParams = new ArrayList<>();
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getCreditSummaryPayerID());
				searchParams.add(jnjGlobalFinancialAnalysisReportForm.getAccountIds());
				//add additional parameters here
				jnjGTPageableData.setSearchParamsList(searchParams);
			 List<JnjGTFinancialCreditSummaryReportData> creditSummaryReportDatasList = jnjGTReportsFacade.simulateCreditSummaryReport(jnjGTPageableData);
			 if (null != creditSummaryReportDatasList && CollectionUtils.isNotEmpty(creditSummaryReportDatasList)) {
			 model.addAttribute("creditSummaryPayerID", jnjGlobalFinancialAnalysisReportForm.getCreditSummaryPayerID());
				model.addAttribute("creditSummaryReportDatasList", creditSummaryReportDatasList);
			 }
		 }
					
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		System.out.println("Value of the string" + String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGlobalFinancialAnalysisReportForm.getDownloadType()));
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGlobalFinancialAnalysisReportForm.getDownloadType())) ? FINANCIAL_SUMMARY_REPORT_PDF_VIEW
				: FINANCIAL_SUMMARY_REPORT_EXCEL_VIEW;
	}
	
	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the Invoice PastDue report search query
	 *
	 * @param model
	 * @param JnjGTBackorderReportForm
	 * @return download view
	 */	
	@PostMapping("/financial/invoicedownloadReport")
	public String downloadInvoiceDueReport(final Model model,
			@ModelAttribute final JnjGTInvoiceDueReportDueForm jnjGTInvoiceDueReportDueForm)
	{
		final String METHOD_NAME = "downloadInvoiceDueReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the form in the model **/
		model.addAttribute(INVOICE_DUE_FINANCIAL_ANALYSIS_FORM_NAME, jnjGTInvoiceDueReportDueForm);

		 /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
 	        if (mediaModel1 != null) {
 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
 	        }
 	        if (mediaModel2 != null) {
 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
 	        }
		}
		 /* Pdf image adding Started here */
	
		 	/**
			 * Generate Financial Analysis Invoice Response and set it in model by passing the page-able data by calling
			 * populatePageableData
			 **/
		 generateInvoicePastDueResponse(model, populatePageableData(jnjGTInvoiceDueReportDueForm));

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		System.out.println("Value of the string" + String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTInvoiceDueReportDueForm.getDownloadType()));
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTInvoiceDueReportDueForm.getDownloadType())) ? FINANCIAL_ANALYSIS_INVOICE_DUE_REPORT_PDF_VIEW
				: FINANCIAL_ANALYSIS_INVOICE_DUE_REPORT_EXCEL_VIEW;
	}
	
	//AAOL-2426
		@PostMapping("/financial/invoiceClearingdownloadReport")
	public String downloadInvoiceClearingReport(final Model model,
			@ModelAttribute final JnjGTInvoiceClearingForm jnjGTInvoiceClearingForm)
	{
		final String METHOD_NAME = "downloadInvoiceDueReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the form in the model **/
		model.addAttribute(FINANCIAL_ANALYSIS_FORM_NAME, jnjGTInvoiceClearingForm);

		 /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
 	        if (mediaModel1 != null) {
 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
 	        }
 	        if (mediaModel2 != null) {
 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
 	        }
		}
		 /* Pdf image adding Started here */
	
		 	/**
			 * Generate Financial Analysis Invoice Response and set it in model by passing the page-able data by calling
			 * populatePageableData
			 **/
		 generateInvoiceClearingResponse(model, populatePageableData(jnjGTInvoiceClearingForm));

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		System.out.println("Value of the string" + String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTInvoiceClearingForm.getDownloadType()));
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTInvoiceClearingForm.getDownloadType())) ? FINANCIAL_ANALYSIS_INVOICE_CLEARING_REPORT_PDF_VIEW
				: FINANCIAL_ANALYSIS_INVOICE_CLEARING_EXCEL_VIEW;
	}
	//AAOL-2426
	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the single purchase analysis report
	 * search query
	 *
	 * @param model
	 * @param JnjGTSinglePurchaseAnalysisReportForm
	 * @return download view
	 */
	@PostMapping("/purchaseAnalysis/single/downloadReport")
	public String downloadPurchaseAnalysisReport(final Model model,
			@ModelAttribute final JnjGTSinglePurchaseAnalysisReportForm JnjGTSinglePurchaseAnalysisReportForm)
	{
		final String METHOD_NAME = "downloadPurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		model.addAttribute(SINGLE_PURCHASE_ANALYSIS_FORM_NAME, JnjGTSinglePurchaseAnalysisReportForm);
		   /* Excel image adding Started here */
				final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
				final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
							.getActiveCatalogVersion();
				model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
					+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
					 /* Excel image adding end here */
				
				List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
				 if (catologLst != null && catologLst.size() > 0) {
					 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
							 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
					 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
							 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
		 	        if (mediaModel1 != null) {
		 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
		 	        }
		 	        if (mediaModel2 != null) {
		 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
		 	        }
				}
				 /* Pdf image adding Started here */

		/**
		 * Generate Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		generateSinglePurchaseAnalysisResponse(model, populatePageableData(JnjGTSinglePurchaseAnalysisReportForm));
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download::"
				+ SINGLE_PURCHASE_ANALYSIS_RESPONSE_DATA_MAP);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTSinglePurchaseAnalysisReportForm.getDownloadType())) ? SINGLE_PURCHASE_ANALYSIS_REPORT_PDF_VIEW
				: SINGLE_PURCHASE_ANALYSIS_REPORT_EXCEL_VIEW;
	}

	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the multiple purchase analysis report
	 * search query
	 *
	 * @param model
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return download view
	 */
	@PostMapping("/purchaseAnalysis/multi/downloadReport")
	public String downloadPurchaseAnalysisReport(final Model model,
			@ModelAttribute final JnjGTMultiPurchaseAnalysisReportForm JnjGTMultiPurchaseAnalysisReportForm)
	{
		final String METHOD_NAME = "downloadPurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/**
		 * Generate Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		model.addAttribute(MULTI_PURCHASE_ANALYSIS_FORM_NAME, JnjGTMultiPurchaseAnalysisReportForm);
		

		   /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
 	        if (mediaModel1 != null) {
 	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
 	        }
 	        if (mediaModel2 != null) {
 	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
 	        }
		}
		 /* Pdf image adding Started here */
		
		
		generateMultiPurchaseAnalysisResponse(model, populatePageableData(JnjGTMultiPurchaseAnalysisReportForm));
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download::"
				+ MULTI_PURCHASE_ANALYSIS_RESPONSE_DATA_LIST);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTMultiPurchaseAnalysisReportForm.getDownloadType())) ? MULTI_PURCHASE_ANALYSIS_REPORT_PDF_VIEW
				: MULTI_PURCHASE_ANALYSIS_REPORT_EXCEL_VIEW;
	}

	/**
	 *
	 * AAOL-4603: This method returns the generated excel or PDF containing the results for the multiple purchase analysis report
	 * search query
	 *
	 * @param model
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return download view
	 */
	@PostMapping("/orderAnalysis/dl/downloadReport")
	public String downloadoaDeliveryListReport(final Model model,
			@ModelAttribute final JnjGTOADeliveryListReportForm JnjGTOADeliveryListReportForm)
	{
		final String METHOD_NAME = "downloadPurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/**
		 * Generate Order Analysis for Delivery List Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **/
		model.addAttribute(OA_DELIVERY_LIST_FORM_NAME, JnjGTOADeliveryListReportForm);
		

		   /* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	        if (mediaModel1 != null) {
	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
	        }
	        if (mediaModel2 != null) {
	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
	        }
		}
		 /* Pdf image adding Started here */
		
		
		generateOADeliveryListResponse(model, populatePageableData(JnjGTOADeliveryListReportForm));
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download::"
				+ OA_DELIVERY_LIST_RESPONSE_DATA_LIST);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTOADeliveryListReportForm.getDownloadType())) ? OA_DELIVERY_LIST_REPORT_PDF_VIEW
				: OA_DELIVERY_LIST_REPORT_EXCEL_VIEW;
	}	
	
	


	/**
	 * @param model
	 * @param jnjGTPageableData
	 */
	protected void generateCutReport(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateCutReport()";
		if (null != jnjGTPageableData)
		{
			LOG.info(LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch cut report.");

			model.addAttribute("sortOptions", jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.SORT_OPTIONS));
			/** calling facade layer to fetch the cut report data **/
			final List<JnjGTCutReportOrderData> cutReportOrders = jnjGTReportsFacade.fetchCutReport(jnjGTPageableData);
			
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "cutReportOrders="+ cutReportOrders);
			
			if (null != cutReportOrders && CollectionUtils.isNotEmpty(cutReportOrders))
			{
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "cutReportOrders.size()="+ cutReportOrders.size() );
				/** Setting the cutReportOrders in the model **/
				model.addAttribute("cutReportOrders", cutReportOrders);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "cutReportOrders has been set in the model!");
			}
			else
			{
				/** Adding no data flag in the model **/
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"cutReportOrders was found to be null or empty! Moving to regular cut report page load!");
				/** Set current B2B Unit Id in Model **/
				//setCurrentB2BUnitIdInModel(model);
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular cut report page load!");
			/** Set current B2B Unit Id in Model **/
			//setCurrentB2BUnitIdInModel(model);
		}
	}

	/**
	 * This method populates the jnjGTPageableData
	 *
	 * @param JnjGTCutReportForm
	 * @return jnjGTPageableData
	 */
	protected JnjGTPageableData populatePageableData(final JnjGTCutReportForm JnjGTCutReportForm)
	{
		final String METHOD_NAME = "populateBackorderReportData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Populating the jnjGTPageableData **/
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
		final JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		jnjGTPageableData.setFromDate(JnjGTCutReportForm.getPostartDate());
		jnjGTPageableData.setToDate(JnjGTCutReportForm.getPoendDate());
		if (StringUtils.isNotEmpty(JnjGTCutReportForm.getProductCode()))
		{
			jnjGTPageableData.setSearchBy(JnjGTCutReportForm.getProductCode());
		}
		if (StringUtils.isNotEmpty(JnjGTCutReportForm.getPoNumber()))
		{
			jnjGTPageableData.setSearchText(JnjGTCutReportForm.getPoNumber());
		}
		if (StringUtils.isNotEmpty(JnjGTCutReportForm.getShipTo()) && !ALL.equals(JnjGTCutReportForm.getShipTo()))
		{
			jnjGTPageableData.setAdditionalSearchText((JnjGTCutReportForm.getShipTo()));
		}
		if (StringUtils.isNotEmpty(JnjGTCutReportForm.getOrderBy()))
		{
			jnjGTPageableData.setStatus(jnjGTReportsFacade.fetchValuesFromConfig(JnjglobalreportsConstants.Reports.SORT_OPTIONS,
					JnjGTCutReportForm.getOrderBy()));
		}
		if (StringUtils.isNotEmpty(JnjGTCutReportForm.getAccountIdsList()))
		{
			jnjGTPageableData.setSearchParamsList(Arrays.asList(JnjGTCutReportForm.getAccountIdsList().split(",")));
		}
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	/**
	 * This method sets the current b2b unit in the model
	 *
	 * @param model
	 */
	protected void setCurrentB2BUnitIdInModel(final Model model)
	{
		final String METHOD_NAME = "setCurrentB2BUnitIdInModel";
		/** Fetching current NA customer data **/
		if (null != jnjGTCustomerFacade.getCurrentGTCustomer()
				&& jnjGTCustomerFacade.getCurrentGTCustomer() instanceof JnjGTCustomerBasicData)
		{
			/** Set current account UID in model **/
			model.addAttribute(JnjglobalreportsConstants.Reports.CURRENT_ACCOUNT_ID,
					((JnjGTCustomerBasicData) jnjGTCustomerFacade.getCurrentGTCustomer()).getCurrentB2BUnitID());
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Current B2B Unit Id set in the model.");
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Current NA customer not found!");
		}
	}

	/**
	 * This method sets the CMS page association in the model.
	 *
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	protected void setupModel(final Model model, final String pageLabelOrId) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "setupModel()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Fetching the division data to check if CODMAN or MITEK **/
		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
		if (null != divisionData
				&& (divisionData.isIsMitek() || divisionData.isIsCodman())
				&& (LoginaddonConstants.MDD).equalsIgnoreCase(String.valueOf(sessionService
						.getAttribute(LoginaddonConstants.SITE_NAME))))
		{
			model.addAttribute(IS_INVENTORY_ACCESSIBLE, Boolean.TRUE);
		}
		/** Fetching the page for request **/
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(pageLabelOrId);

		/** Setting up the model **/
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "CMS Page loaded to the model");
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}

	@RequestMapping(value = "/shipToDropdown")
	public @ResponseBody List<JnjGTAddressData> getShipToDropdown(@RequestParam(value = "accounts") final String accounts)
	{
		return jnjGTReportsFacade.getShipTOAddressesForAccount(accounts);
	}

	@RequestMapping(value = "/franchiseCode")
	public @ResponseBody Map<String, String> getFranchiseCode(
			@RequestParam(value = OPERATING_COMPANY) final String operatingCompany)
	{
		final Map<String, String> map = jnjGTReportsFacade.getFranchiseOrDivCode(operatingCompany);
		return map;
	}

	@RequestMapping(value = "/cutorder/downloadReport")
	public String downloadCutReport(final Model model,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@ModelAttribute(value = "Form") final JnjGTCutReportForm JnjGTCutReportForm)
	{
		final String METHOD_NAME = "downloadCutReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** Generate Cut Response and set it in model by passing the page-able data by calling populatePageableData **/
		generateCutReport(model, populatePageableData(JnjGTCutReportForm));
		
		model.addAttribute(CUTREPORT_FORM_NAME, JnjGTCutReportForm);

		if (null != JnjGTCutReportForm.getAccountIds() && JnjGTCutReportForm.getAccountIds().length() > 0) {
			final String selectedAccountsString = "";
		final JnJB2BUnitModel accountNam=jnjGTCustomerFacade.getCurrentB2bUnit();			
		model.addAttribute("accountreportname",accountNam.getDisplayName());	
		}
		
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		
		if(String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTCutReportForm.getDownloadType())){
			 List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
			 if (catologLst != null && catologLst.size() > 0) {
				 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
						 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
				 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
						 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	        if (mediaModel1 != null) {
	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
	        }
	        if (mediaModel2 != null) {
	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
	        }
			}  
			}
		
		
		final List<JnjGTCutReportOrderData> cutReportOrders = (List<JnjGTCutReportOrderData>) model.asMap().get("cutReportOrders");
		final Map<String, List<JnjGTCutReportOrderEntryData>> orderEntryMap = new HashMap<String, List<JnjGTCutReportOrderEntryData>>();
	/*	if (null != cutReportOrders)
		{
			for (final JnjGTCutReportOrderData jnjGTCutReportOrderData : cutReportOrders)
			{
				final List<JnjGTCutReportOrderEntryData> orderEntryDatas = jnjGTReportsFacade
						.fetchCutReportOrderEntryData(jnjGTCutReportOrderData.getOrderNumber());
				orderEntryMap.put(jnjGTCutReportOrderData.getOrderNumber(), orderEntryDatas);
			}
		}*/
		model.addAllAttributes(orderEntryMap);
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download.");
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning the view for PDF or excel **/
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? CUT_REPORT_PDF_VIEW : CUT_REPORT_EXCEL_VIEW;
	}

	@RequestMapping(value = "/viewDetails")
	public String viewOrderEntryDetails(final Model model, @RequestParam(value = "orderNumber") final String orderNumber)
	{
		final List<JnjGTCutReportOrderEntryData> orderEntryDatas = jnjGTReportsFacade.fetchCutReportOrderEntryData(orderNumber);
		model.addAttribute("orderEntryDatas", orderEntryDatas);
		//return JnjglobalreportsControllerConstants.Views.Pages.Reports.CutReportOrderEntryPanel;
		//return "";
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.CutReportOrderEntryPanel);
	}

	/**
	 * This method creates the bread-crumbs for the reports page.
	 *
	 * @param formURI
	 * @param model
	 * @param formSpecificKey
	 */
	protected void createBreadCrumbsForReports(final String reportCategoryUrl, final Model model, final String reportCategory,final String reportType,String reportTypeUrl)
	{
		final String METHOD_NAME = "createBreadCrumbsForReports()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Building bread-crumbs **/
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);

		/** reports root node bread-crumb **/
		breadcrumbs.add(new Breadcrumb(REPORTS_ROOT_PAGE_URL, jnjCommonFacadeUtil.getMessageFromImpex(REPORTS_LABEL_STRING), null));

		/** reports current page bread-crumb **/
		breadcrumbs.add(new Breadcrumb(reportCategoryUrl, reportCategory, null));
		breadcrumbs.add(new Breadcrumb(reportTypeUrl, reportType, null));

		/** Adding bread-crumbs to model **/
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Breadcrumbs have been created for :: "
				+ reportCategory);

		logMethodStartOrEnd(LoginaddonConstants.Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
	}
	/**
	 * AAOL-2410: This method creates the bread-crumbs for the orderanalysis page.
	 *
	 * @param formURI
	 * @param model
	 * @param formSpecificKey
	 */
	protected void createBreadCrumbsForOrderAnalysis(final String formURI, final Model model, final String formSpecificKey)
	{
		final String METHOD_NAME = "createBreadCrumbsForReports()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Building bread-crumbs **/
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);

		/** reports root node bread-crumb **/
		breadcrumbs.add(new Breadcrumb(REPORTS_ROOT_PAGE_URL, jnjCommonFacadeUtil.getMessageFromImpex(REPORTS_LABEL_STRING), null));

		/** reports current page bread-crumb **/
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));

		/** Adding bread-crumbs to model **/
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Breadcrumbs have been created for :: "
				+ formSpecificKey);

		logMethodStartOrEnd(LoginaddonConstants.Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method creates pageable data
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortCode
	 * @param showMode
	 * @return pageableData
	 */
	protected JnjGTPageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final JnjGTPageableData pageableData = new JnjGTPageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
	public String getView(final String view){
        return JnjglobalreportsControllerConstants.ADDON_PREFIX + view;
	}
	
	@GetMapping("/inventoryAnalysis/consignmentInventory")
    public String getconsignmentInventoryReportPage(final Model model) throws CMSItemNotFoundException
    {
           final String METHOD_NAME = "getconsignmentInventoryReportPage()";
           logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
           JnjGTConsignmentInventoryReportForm jnjGTConsignmentInventoryReportForm = new JnjGTConsignmentInventoryReportForm();
           List<String> franchiseCode = jnjGTReportsFacade.getDropdownFranchiseCode(null);
           jnjGTConsignmentInventoryReportForm.setFranchiseDescription(franchiseCode);
           
           String acountid = jnjGTCustomerFacade.getCurrentB2bUnit().getUid();
           List<String> stockLocationAccounts = jnjGTReportsFacade.fetchStockLocationAccount(acountid);
           model.addAttribute("stockLocationAccounts", stockLocationAccounts);
	        
           /*inventry report check*/
	   		final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
	   		if(null!= divisionData &&( divisionData.isIsMitek() || divisionData.isIsCodman())){
	   		
	   			model.addAttribute("inventry", Boolean.TRUE);	
	   		}else{
	   			model.addAttribute("inventry", Boolean.FALSE);	
	   			
	   		}


			/** Creating bread-crumbs by surabhi**/
			createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
					getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
					getMessageSource().getMessage(LoginaddonConstants.Report.CONSIGNMENT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
					,null);

           /** Set current B2B Unit Id in Model **/
          setCurrentB2BUnitIdInModel(model);

           /** Fetching UCNs to check if the change link should be displayed **/
           final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
           if (ucnMap != null && ucnMap.size() > 1)
           {
                  /** Setting the flag to display the change link **/
                  model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
           }
           
           final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
           LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
           if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
           		{
   			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
   			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
           		}

           /** Setting the form in the model **/
           model.addAttribute(CONSIGNMENT_INVENTORY_FORM_NAME, jnjGTConsignmentInventoryReportForm);

           /** Setting up the model **/
           setupModel(model, CMS_CONSIGNMENT_INVENTORY_REPORT_PAGE);
           
	       model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.CONSIGNMENT_REPORT);
	   	   model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
	   	   model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
	   	   model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
	   	   model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.CONSIGNMENT_REPORT);
	   	   logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);

	       logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	       return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
	           //return "";
    }
	
	
	@PostMapping("/inventoryAnalysis/consignmentInventory")
    public String fetchConsignmentInventoryReportPage(final Model model, @ModelAttribute final JnjGTConsignmentInventoryReportForm jnjGlobalConsignmentInventoryReportForm) throws CMSItemNotFoundException
    {
			JnjGTPageableData jnjGTPageableData = populateConsPageableData(jnjGlobalConsignmentInventoryReportForm);
           final String METHOD_NAME = "postconsignmentInventoryReportPage()";
           logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
           List<JnjGTConsInventoryData> consInventoryReportList = jnjGTReportsFacade.simulateConsInventoryReport(jnjGTPageableData);

			/** Creating bread-crumbs by surabhi**/
			createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
					getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
					getMessageSource().getMessage(LoginaddonConstants.Report.CONSIGNMENT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
					,null);
           

          /*inventry report check*/
   			final JnjGTDivisonData divisionData = jnjGTCartFacade.getPopulatedDivisionData();
	   		if(null!= divisionData &&( divisionData.isIsMitek() || divisionData.isIsCodman())){
	   		
	   			model.addAttribute("inventry", Boolean.TRUE);	
	   		}else{
	   			model.addAttribute("inventry", Boolean.FALSE);	
	   			
	   		}

           /** Fetching UCNs to check if the change link should be displayed **/
           final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
           if (ucnMap != null && ucnMap.size() > 1)
           {
                  /** Setting the flag to display the change link **/
                  model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
           }
           
           final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
           LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
           if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
           		{
   			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
   			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
           		}
           
           /** Setting the form in the model **/
           model.addAttribute(CONSIGNMENT_INVENTORY_FORM_NAME,jnjGlobalConsignmentInventoryReportForm);

           /** Setting up the model **/
           setupModel(model, CMS_CONSIGNMENT_INVENTORY_REPORT_PAGE);
           
           model.addAttribute("consInventoryReportList", consInventoryReportList);
           model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.CONSIGNMENT_REPORT);
	   	   model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
	   	   model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
	   	   model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
	   	   model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.CONSIGNMENT_REPORT);
	   	   
			model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGlobalConsignmentInventoryReportForm.getAccountsSelectedValue());

	       logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	       return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
          
    }
	
	
	@PostMapping("/inventoryAnalysis/consignmentInventory/downloadReport")
	public String downloadConsignmentInventoryReport(final Model model,
			@ModelAttribute final JnjGTConsignmentInventoryReportForm jnjGlobalConsignmentInventoryReportForm,@RequestParam(value = "downloadType")  String downloadType)
	{
		JnjGTPageableData jnjGTPageableData = populateConsPageableData(jnjGlobalConsignmentInventoryReportForm);
        final String METHOD_NAME = "downloadConsignmentInventoryReport()";
        logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
        List<JnjGTConsInventoryData> consInventoryReportList = jnjGTReportsFacade.simulateConsInventoryReport(jnjGTPageableData);
        model.addAttribute("consInventoryReportList", consInventoryReportList);
        /*site log */
	    final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
	    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
	    //Send site logo
	    /* Excel image adding Started here */
		model.addAttribute("siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		 /* Excel image adding end here */
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download.");
        
        
		model.addAttribute(CONSIGNMENT_INV_FORM_NAME, jnjGlobalConsignmentInventoryReportForm);
        logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(downloadType)) ? CONSIGNMENT_INVENTORY_REPORT_PDF_VIEW
				: CONSIGNMENT_INVENTORY_EXCEL_VIEW;
       
	}
	/**
	 * This method is responsible for handling the Cut Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/inventoryAnalysis/cutorder")
	public String getCutOrderReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getcutOrderReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.CUT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		/** Set current B2B Unit Id in Model **/
		setCurrentB2BUnitIdInModel(model);
		/** Setting up the model **/
		setupModel(model, CMS_CUTORDER_REPORT_PAGE);
		model.addAttribute("sortOptions", jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.SORT_OPTIONS));
		model.addAttribute("cutReportForm", new JnjGTCutReportForm());
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.CUT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.CUT_REPORT);

		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		//return "pages/reports/cutReportPage";
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
		//return "";
	}
	
		
	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the multiple purchase analysis report
	 * search query
	 *
	 * @param model
	 * @param JnjGTMultiPurchaseAnalysisReportForm
	 * @return download view
	 */
@PostMapping("/salesReport/downloadReport")
	public String downloadsalesReport(final Model model,
			@ModelAttribute final JnjGTSalesReportAnalysisForm JnjGTSalesReportAnalysisForm)
	{
		final String METHOD_NAME = "downloadSalesReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/**//**
		 * Generate Purchase Analysis Response and set it in model by passing the page-able data by calling
		 * populatePageableData
		 **//*
*/		
		model.addAttribute(SALES_REPORT_ORDER_ANALYSIS_FORM, JnjGTSalesReportAnalysisForm);
	
		  //  Excel image adding Started here 
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			//  Excel image adding end here 
		
		List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		 if (catologLst != null && catologLst.size() > 0) {
			 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
					 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
	        if (mediaModel1 != null) {
	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
	        }
	        if (mediaModel2 != null) {
	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
	        }
		}
		 // Pdf image adding Started here 
		
		
		 generateSalesReportPageResponse(model, populatePageableData(JnjGTSalesReportAnalysisForm));
		
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Response ready for download::"
				+ SALES_ORDER_ANALYSIS_RESPONSE_DATA_LIST);

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		// Returning the view for PDF or excel 
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(JnjGTSalesReportAnalysisForm.getDownloadType())) ? SALES_REPORT_ANALYSIS_REPORT_PDF_VIEW
				: SALES_REPORT_ANALYSIS_REPORT_EXCEL_VIEW;
	}
	
	
	
	
	

	/**
	 *
	 * This method is responsible for handling the Search feature on Cut Reports Page.
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/inventoryAnalysis/cutorder")
	public String fetchCutReportData(final Model model, @ModelAttribute final JnjGTCutReportForm JnjGTCutReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchCutReportData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		/** Creating bread-crumbs by surabhi**/
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.CUT_REPORT_LABEL_STRING, null, getI18nService().getCurrentLocale())
				,null);
		
		model.addAttribute("cutReportForm", JnjGTCutReportForm);
		/** Populating the jnjGTPageableData **/
		generateCutReport(model, populatePageableData(JnjGTCutReportForm));
		/** Setting up the model **/
		setupModel(model, CMS_CUTORDER_REPORT_PAGE);
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.CUT_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjGTReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.CUT_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, JnjGTCutReportForm.getAccountsSelectedValue());

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
		//return "";
	}

	
	
	/** AAOL-2410: Sales Report **/
		protected List<OrderTypesForm> populateOrderTypes(List<OrderTypesForm> orderTypes){		
		
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZOR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZOR)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZDEL.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZDEL)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZLZ.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZLZ)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZNC.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZNC)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZKB.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZKB)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZEX.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZEX)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZRE.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZRE)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.ZQT.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZQT)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.KA.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KA)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.KB.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KB)));
	orderTypes.add(new OrderTypesForm(JnjOrderTypesEnum.KE.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KE)));
	//Added by Pradeep Chandupatla new order types Credit Memo and debit memo
	/*orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZCR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZCR)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZDR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZDR)));*/

	/*commented by tharun to hide credit and debit memos*/
	
	return orderTypes;
	}
	
	protected JnjGTPageableData populateConsPageableData(final JnjGTConsignmentInventoryReportForm jnjGTConsignmentInventoryReportForm)
	{
		final String METHOD_NAME = "populateBackorderReportData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Populating the jnjGTPageableData **/
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the jnjGTPageableData!");
		final JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		
		if (StringUtils.isNotEmpty(jnjGTConsignmentInventoryReportForm.getProductCode()))
		{
			jnjGTPageableData.setSearchBy(jnjGTConsignmentInventoryReportForm.getProductCode());
		}
		/*if (CollectionUtils.isNotEmpty(jnjGTConsignmentInventoryReportForm.getFranchiseDescription()))
		{
			jnjGTPageableData.setSearchText(jnjGTConsignmentInventoryReportForm.getFranchiseDescription());
		}*/
		if (StringUtils.isNotEmpty(jnjGTConsignmentInventoryReportForm.getStockLocationAcc()))
		{
			jnjGTPageableData.setAdditionalSearchText(jnjGTConsignmentInventoryReportForm.getStockLocationAcc());
		}
		
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, "jnjGTPageableData has been populated!");

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}
	//AAOL-2410
	protected List<SelectOption> populateOrderStatus(List<SelectOption> orderStatus){
		
			orderStatus.add(new SelectOption(OrderStatus.PENDING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PENDING)));
			orderStatus.add(new SelectOption(OrderStatus.SHIPPED.toString(), enumerationService
					.getEnumerationName(OrderStatus.SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CANCELLED)));
			orderStatus.add(new SelectOption(OrderStatus.PARTIALLY_SHIPPED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PARTIALLY_SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.INCOMPLETE.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INCOMPLETE)));
			orderStatus.add(new SelectOption(OrderStatus.ACCEPTED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.ACCEPTED)));
			orderStatus.add(new SelectOption(OrderStatus.BACKORDERED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BACKORDERED)));
			orderStatus.add(new SelectOption(OrderStatus.INVOICED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INVOICED)));
			orderStatus.add(new SelectOption(OrderStatus.IN_PROCESS.getCode(), enumerationService
					.getEnumerationName(OrderStatus.IN_PROCESS)));
			orderStatus.add(new SelectOption(OrderStatus.DELIVERED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.DELIVERED)));
			orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BEING_PROCESSED)));
			orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
					.getEnumerationName(OrderStatus.CREATED)));
			orderStatus.add(new SelectOption(OrderStatus.CREDIT_HOLD.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CREDIT_HOLD)));
			orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.IN_PICKING)));
			orderStatus.add(new SelectOption(OrderStatus.COMPLETED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.COMPLETED)));
			
		return orderStatus;
		
		
	}
	
	//AAOL-2410
		/**
	 * Data class used to hold drop down select options and respective values. Holds the code identifier as well as the
	 * display name.
	 */
	public static class SelectOption
	{
		protected final String code;
		protected final String name;

		public SelectOption(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}
	}
	
}
