/**
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.b2b.jnjglobalreports.controllers.pages;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import org.springframework.dao.DuplicateKeyException;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.b2b.jnjglobalreports.constants.JnjglobalreportsConstants;
import com.jnj.b2b.jnjglobalreports.controllers.JnjglobalreportsControllerConstants;
import com.jnj.la.b2b.jnjglobalreports.controllers.JnjlaglobalreportsaddonControllerConstants;
import com.jnj.b2b.jnjglobalreports.controllers.pages.JnjGTReportsPageController;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInventoryReportForm;
import com.jnj.la.b2b.jnjglobalreports.forms.JnjLaOpenOrdersReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTMultiPurchaseAnalysisReportForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.core.annotations.AuthorizedUserGroup;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.la.core.data.JnjLaOpenOrdersReportTemplateData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.facades.reports.JnjGTReportsFacade;
import com.jnj.facades.reports.JnjLatamReportsFacade;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjLaOpenOrdersReportReponseData;
import com.jnj.services.CMSSiteService;

/**
 * @author plahiri1
 *
 */
public class JnjLatamReportsPageController extends JnjGTReportsPageController {

	private static final Logger LOG = LoggerFactory.getLogger(JnjLatamReportsPageController.class);

	private static final String REPORTS_GROUP = "reportsGroup";
    private static final String CMS = "CMS";
    private static final String ACCOUNT_LOG_MSG = "showChangeAccountLink value {}: ";
    private static final String ACCOUNT_LOG_MSG1 = "Entered condition....showChangeAccountLink {} :" ;
	private static final String ADDON_PREFIX = "addon:/jnjlaglobalreportsaddon/";
	private static final String REPORT_ACCOUNT_NAME="reportAccountName";
	private static final String SITE_LOGO_PATH = "siteLogoPath";
	private static final String EPIC_EMAIL_LOGO_IMAGE_ONE="epicEmailLogoImageOne";
	private static final String RESPONSE_READY_DOWNLOAD="Response ready for download::";
	private static final String LATAM_BACKORDER_REPORT_PDF_VIEW = "jnjLABackorderReportPdfView";
	private static final String LATAM_BACKORDER_REPORT_EXCEL_VIEW = "jnjLABackorderReportExcelView";
	private static final String BACKORDER_REPORT_NAME ="boReport";
	private static final String BACKORDER_REPORT = "boReport.la.reportType";
	private static final String OPEN_ORDERS_REPORT = "Open Orders Report";
	private static final String OPEN_ORDER_REPORT_PDF_VIEW = "jnjLAOpenOrderReportPdfView";
	private static final String OPEN_ORDER_REPORT_EXCEL_VIEW = "jnjLAOpenOrderReportExcelView";
	private static final String BASE_URL = "jnj.root.server.url.https";
	private static final String SITE_LOGO_PROPERTY="siteLogo";
		
	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
	private SessionService sessionService;

	@Resource(name = "GTReportsFacade")
	private JnjGTReportsFacade jnjGTReportsFacade;

	@Resource(name = "jnjGTB2BUnitFacade")
	private JnjGTB2BUnitFacade jnjGTUnitFacade;

	@Autowired
	private JnjLatamReportsFacade jnjLatamReportsFacade;

	@Autowired
	private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;
	
	private ConfigurationService configurationService;

	private CMSSiteService cmsSiteService;

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Override
	public String getMultiPurchaseAnalysisReportPage(final Model model) throws CMSItemNotFoundException {

		final String methodName = "getMultiPurchaseAnalysisReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.BEGIN_OF_METHOD);

		createBreadCrumbsForOrderAnalysis(null, model,
				jnjCommonFacadeUtil.getMessageFromImpex(PURCHASE_ANALYSIS_LABEL_STRING));

		/** In case of multiple purchase analysis report **/
		model.addAttribute(MULTI_PURCHASE_ANALYSIS_FORM_NAME, new JnjGTMultiPurchaseAnalysisReportForm());
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, methodName,
				"MULTI PA REPORT - JnjGTMultiPurchaseAnalysisReportForm has been set in the model!");

		/**
		 * Checking if current site is MDD or not, and fetching the ordered from
		 * list accordingly
		 **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD)
				? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;
		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));
		model.addAttribute(ANALYSIS_VARIABLE,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.ANALYSIS_VARIABLE));
		model.addAttribute(PRODUCT_DISPLAY,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY));
		model.addAttribute(OPERATING_COMPANY, jnjLatamReportsFacade.getOperatingCompanyDropdown(siteName));
		model.addAttribute(TERRITORIES, jnjGTReportsFacade.getTerritoryForSalesRep(siteName));

		setCurrentB2BUnitIdInModel(model);
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info(ACCOUNT_LOG_MSG, showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			LOG.info(ACCOUNT_LOG_MSG1, showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.END_OF_METHOD);
		//FIXME:merge check if former PurchaseAnalysisReportPage is now PurchaseAnalysisOrderEntriesPage
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.PurchaseAnalysisOrderEntriesPage);
	}

	@Override
	public String fetchMultiPurchaseAnalysisReport(final Model model,
			@ModelAttribute final JnjGTMultiPurchaseAnalysisReportForm jnjGTMultiPurchaseAnalysisReportForm)
			throws CMSItemNotFoundException {
		final String methodName = "fetchMultiPurchaseAnalysisReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.BEGIN_OF_METHOD);

		createBreadCrumbsForOrderAnalysis(null, model,
				jnjCommonFacadeUtil.getMessageFromImpex(PURCHASE_ANALYSIS_LABEL_STRING));

		/**
		 * Checking if current site is MDD or not, and fetching the ordered from
		 * list accordingly
		 **/
		final String siteName = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		final String orderedFromId = siteName.equals(LoginaddonConstants.MDD)
				? JnjglobalreportsConstants.Reports.ORDERED_FROM_MDD
				: JnjglobalreportsConstants.Reports.ORDERED_FROM_CONS;

		model.addAttribute(ORDERED_FROM, jnjGTReportsFacade.getDropdownValuesInMap(orderedFromId));
		model.addAttribute(ANALYSIS_VARIABLE,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.ANALYSIS_VARIABLE));
		model.addAttribute(PRODUCT_DISPLAY,
				jnjGTReportsFacade.getDropdownValuesInMap(JnjglobalreportsConstants.Reports.PRODUCT_TO_DISPLAY));
		/**
		 * Check which type of site is used to fetch the territory drop-down
		 **/
		model.addAttribute(OPERATING_COMPANY, jnjLatamReportsFacade.getOperatingCompanyDropdown(siteName));
		model.addAttribute(TERRITORIES, jnjGTReportsFacade.getTerritoryForSalesRep(siteName));

		if (StringUtils.isNotEmpty(jnjGTMultiPurchaseAnalysisReportForm.getFranchiseDivCode())) {
			model.addAttribute(FRANCHISE_CODES, jnjGTReportsFacade
					.getFranchiseOrDivCode(jnjGTMultiPurchaseAnalysisReportForm.getOperatingCompany()));
		}

		model.addAttribute(MULTI_PURCHASE_ANALYSIS_FORM_NAME, jnjGTMultiPurchaseAnalysisReportForm);

		/**
		 * Generate Multi Purchase Analysis Response and set it in model by
		 * passing the page-able data by calling populatePageableData
		 **/
		generateMultiPurchaseAnalysisResponse(model, populatePageableData(jnjGTMultiPurchaseAnalysisReportForm));

		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info(ACCOUNT_LOG_MSG, showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			LOG.info(ACCOUNT_LOG_MSG1, showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("startDate", jnjGTMultiPurchaseAnalysisReportForm.getStartDate());
		model.addAttribute("endDate", jnjGTMultiPurchaseAnalysisReportForm.getEndDate());
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.END_OF_METHOD);
		//FIXME:merge check if former PurchaseAnalysisReportPage is now PurchaseAnalysisOrderEntriesPage
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.PurchaseAnalysisOrderEntriesPage);
	}

	@Override
	public String getView(String view) {
		return ADDON_PREFIX + view;
	}

	@Override
	public String getAccountSelection(final Model model,
			@RequestParam(value = "ucnFlag", defaultValue = "false", required = false) final boolean ucnFlag,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = FETCH_ALL, defaultValue = "false", required = false) final boolean fetchAll)
			throws CMSItemNotFoundException {
		final String methodName = "getBackOrderReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.BEGIN_OF_METHOD);

		final int pageSizeFromConfig;
		if (fetchAll) {
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_SHOW_ALL_RESULTS, 1000);
		} else {
			pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);
		}

		final int finalPageSize = Boolean.valueOf(showMore) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm) && !fetchAll) {
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm);
		}

		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, methodName, "Calling facade to fetch accounts!");
		final JnjGTAccountSelectionData accountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, true,
				ucnFlag, pageableData);

		model.addAttribute(LoginaddonConstants.Login.ACCOUNT_LIST, accountSelectionData.getAccountsMap());
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData());
		model.addAttribute(CURRENT_PAGE, showMoreCounter);

		if (fetchAll) {
			model.addAttribute(FETCH_ALL, Boolean.valueOf(fetchAll));
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Misc.AccountSelectionPage);
	}
	
	/**
	 * This method is responsible for handling the Back-Order Reports Page.
	 *
	 * @param model the view model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@AuthorizedUserGroup(value = REPORTS_GROUP)
	@Override
	public String getBackOrderReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getBackOrderReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(BACKORDER_REPORT, null, getI18nService().getCurrentLocale())
				,null);
		model.addAttribute(BACKORDER_FORM_NAME, new JnjGTBackorderReportForm());
		setCurrentB2BUnitIdInModel(model);
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info(ACCOUNT_LOG_MSG, showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info(ACCOUNT_LOG_MSG1, showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final Map<String, String> reportTypes = jnjLatamReportsFacade.getInventoryReportsTypeDropdownVaulesInMap();
		if (reportTypes.containsKey(BACKORDER_REPORT_NAME)) {
			model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.BO_REPORT);
			model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY,
					jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
			model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE,
					jnjLatamReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
			model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL, LoginaddonConstants.Report.INVENTORY_CATEGORY);
			model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL, LoginaddonConstants.Report.BO_REPORT);
			
			logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
			return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
		} else {
			return getInventoryReportPage(model);
		}
	}
	
	/**
	 *
	 * This method is responsible for handling the Search feature on Back-Order Reports Page.
	 *
	 * @param model object carrying data to front-end
	 * @return view UI file to display results
	 * @throws CMSItemNotFoundException
	 */
	@AuthorizedUserGroup(value = REPORTS_GROUP)
	@Override
	@PostMapping("/inventoryAnalysis/backorder")
	public String fetchBackOrderReport(final Model model, @ModelAttribute final JnjGTBackorderReportForm jnjGlobalBackorderReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(BACKORDER_REPORT, null, getI18nService().getCurrentLocale())
				,null);
		model.addAttribute(BACKORDER_FORM_NAME, jnjGlobalBackorderReportForm);
		generateBackorderResponse(model, populatePageableData(jnjGlobalBackorderReportForm));
		setupModel(model, CMS_PURCHASE_ANALYSIS_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.BO_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY, jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE, jnjLatamReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.BO_REPORT);
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE, jnjGlobalBackorderReportForm.getAccountsSelectedValue());
		model.addAttribute(BACKORDER_ALL_REPORTS,jnjGlobalBackorderReportForm.getAllreports());
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
	}
	
	/**
	 * THis method generates the Back-Order response by calling the facade layer and sets the same in the model
	 *
	 * @param model
	 * @param jnjGTPageableData
	 */
	@Override
	protected void generateBackorderResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateBackorderResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		if (null != jnjGTPageableData)
		{
			String msg = LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch backorder report.";
			LOG.info(msg);
			final List<JnjGTBackorderReportResponseData> jnjGTBackorderReportResponseDataList = jnjLatamReportsFacade
					.fetchBackOrderReport(jnjGTPageableData);
			if (null != jnjGTBackorderReportResponseDataList && CollectionUtils.isNotEmpty(jnjGTBackorderReportResponseDataList))
			{
				model.addAttribute(BACKORDER_RESPONSE_DATA_LIST, jnjGTBackorderReportResponseDataList);
			}
			else
			{
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
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
	 *
	 * This method returns the generated excel or PDF containing the results for the back-order report search query
	 *
	 * @param model
	 * @param jnjGTBackorderReportForm
	 * @return download view
	 */
	@Override
	@PostMapping("/backorder/downloadReport")
	public String downloadBackorderReport(final Model model,
			@ModelAttribute final JnjGTBackorderReportForm jnjGTBackorderReportForm)
	{
		final String methodName = "downloadBackorderReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.BEGIN_OF_METHOD);

		model.addAttribute(BACKORDER_FORM_NAME, jnjGTBackorderReportForm);

		generateBackorderResponse(model, populatePageableData(jnjGTBackorderReportForm));

		if (StringUtils.isNotEmpty(jnjGTBackorderReportForm.getAccountIds())) {
			final String[] accountIDs = jnjGTBackorderReportForm.getAccountIds().split(",");
			if(accountIDs.length == 1) {
				JnJB2BUnitModel selectedB2bUnit = jnjGTCustomerFacade.getB2BUnitForUid(
						jnjGTBackorderReportForm.getAccountIds());
				if(null == selectedB2bUnit) {
					selectedB2bUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
				}
				model.addAttribute(REPORT_ACCOUNT_NAME, (null != selectedB2bUnit)
						? selectedB2bUnit.getDisplayName() :"NA");
			}
		}

		CMSSiteModel cmsSiteModel = getCmsSiteService().getCurrentSite();

		final CatalogVersionModel currentCatalog = cmsSiteModel.getContentCatalogs().get(0).getActiveCatalogVersion();
		model.addAttribute(SITE_LOGO_PATH, mediaService.getMedia(currentCatalog,EPIC_EMAIL_LOGO_IMAGE_ONE).getURL());
		 
		final String url = mediaService.getMedia(currentCatalog,EPIC_EMAIL_LOGO_IMAGE_ONE).getURL();
		final String baseURL = String.format(getConfigurationService().getConfiguration().getString(BASE_URL),
				cmsSiteModel.getUid().split(CMS)[0]);

		model.addAttribute("siteLogoPathURL", baseURL+url);
	    
		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, methodName, RESPONSE_READY_DOWNLOAD);
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.END_OF_METHOD);

		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTBackorderReportForm.getDownloadType()))
				? LATAM_BACKORDER_REPORT_PDF_VIEW : LATAM_BACKORDER_REPORT_EXCEL_VIEW;
	}
	
	@Override
	public String getInventoryReportPage(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getInventoryReportPage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		createBreadCrumbsForReports(Config.getParameter(LoginaddonConstants.Report.BREADCRUMB_INVENTORY_URL), model,
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_ANALYSIS_LABEL_STRING, null,
						getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(LoginaddonConstants.Report.INVENTORY_REPORT_LABEL_STRING, null,
						getI18nService().getCurrentLocale())
				,null);
		setCurrentB2BUnitIdInModel(model);
		final Map<String, String> ucnMap = jnjGTCustomerFacade.getSectorSpecificAccountsMap(true, true);
		if (ucnMap != null && ucnMap.size() > 1)
		{
			model.addAttribute(DISPLAY_UCN_SELECTION, Boolean.TRUE);
		}
		model.addAttribute(INVENTORY_FORM_NAME, new JnjGTInventoryReportForm());
		setupModel(model, CMS_INVENTORY_REPORT_PAGE);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(SELECTED_REPORT,LoginaddonConstants.Report.INVENTORY_REPORT);
		model.addAttribute(LoginaddonConstants.Report.REPORT_CATEGORY,
				jnjGTReportsFacade.getCartegoryDropdownValuesInMap());
		model.addAttribute(LoginaddonConstants.Report.REPORT_TYPE,
				jnjLatamReportsFacade.getInventoryReportsTypeDropdownVaulesInMap());
		model.addAttribute(LoginaddonConstants.Report.CATEGORY_URL,LoginaddonConstants.Report.INVENTORY_CATEGORY);
		model.addAttribute(LoginaddonConstants.Report.SUB_REPORT_URL,LoginaddonConstants.Report.INVENTORY_REPORT);
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjglobalreportsControllerConstants.Views.Pages.Reports.InventoryAnalysisReportPage);
	}
	
	@AuthorizedUserGroup(value = REPORTS_GROUP)		
	@PostMapping("/openordersreport")
	public String fetchOpenOrdersReport(final Model model, @ModelAttribute final JnjLaOpenOrdersReportForm jnjLaOpenOrdersReportForm)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "fetchOpenOrdersReport()";
		logMethodStartOrEnd(OPEN_ORDERS_REPORT, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		createBreadCrumbsForReports(Config.getParameter(
				JnjlaglobalreportsaddonControllerConstants.BREADCRUMB_OPEN_ORDERS_REPORT_URL), model,
				getMessageSource().getMessage(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_STRING,
						null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_STRING,
						null, getI18nService().getCurrentLocale()),null);
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_FORM, jnjLaOpenOrdersReportForm);		

		generateOpenOrdersReportResponse(model, populateOpenOrdersPageableData(jnjLaOpenOrdersReportForm));
		setCurrentB2BUnitIdInModel(model);
		setupModel(model, JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_PAGE);
		
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.SHOW_CHANGE_ACCOUNT_LINK, Boolean.TRUE);
		}
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.ORDER_TYPE,
				jnjLatamReportsFacade.getOrderTypes());
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_TEMPLATE_DATA,
				jnjLatamReportsFacade.getOpenOrdersReportTemplate());
		model.addAttribute(JnjglobalreportsConstants.Reports.ACCOUNT_SELECTED_VALUE,
				jnjLaOpenOrdersReportForm.getAccountIds());
		
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return getView(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_PAGE_PATH);
	}
	
	@AuthorizedUserGroup(value = REPORTS_GROUP)		
	@PostMapping("/openordersreport/savetemplate")
	@ResponseBody
	private String saveOpenOrdersReportTemplate(final Model model,
												final JnjLaOpenOrdersReportForm jnjLaOpenOrdersReportForm,
												final String templateName) {
		JnjLaOpenOrdersReportTemplateData openOrdersReportData = new JnjLaOpenOrdersReportTemplateData();
		openOrdersReportData.setTemplateName(templateName);
		openOrdersReportData.setAccountIds(jnjLaOpenOrdersReportForm.getAccountIds());
		openOrdersReportData.setQuickSelection(jnjLaOpenOrdersReportForm.getQuickSelection());
		openOrdersReportData.setFromDate(jnjLaOpenOrdersReportForm.getFromDate());
		openOrdersReportData.setToDate(jnjLaOpenOrdersReportForm.getToDate());
		openOrdersReportData.setOrderType(jnjLaOpenOrdersReportForm.getOrderType());
		openOrdersReportData.setProductCode(jnjLaOpenOrdersReportForm.getProductCode());
		openOrdersReportData.setOrderNumber(jnjLaOpenOrdersReportForm.getOrderNumber());
		openOrdersReportData.setShipTo( jnjLaOpenOrdersReportForm.getShipTo());
		openOrdersReportData.setReportColumns(jnjLaOpenOrdersReportForm.getReportColumns());
		
		try {
			jnjLatamReportsFacade.saveOpenOrdersReportTemplate(openOrdersReportData, templateName);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.SUCCESS,
					JnjlaglobalreportsaddonControllerConstants.SUCCESS);
		} catch(DuplicateKeyException exe) {
			LOG.error("Template already exist with this name ", exe);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.ERROR,
					JnjlaglobalreportsaddonControllerConstants.DUPLICATE_TEMPLATE_ERROR);
			return JnjlaglobalreportsaddonControllerConstants.DUPLICATE_TEMPLATE_ERROR;
		} catch(Exception exe) {
			LOG.error("Error occurred while saving the template ", exe);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.ERROR,
					JnjlaglobalreportsaddonControllerConstants.ERROR);
			return JnjlaglobalreportsaddonControllerConstants.ERROR;
		}

		return JnjlaglobalreportsaddonControllerConstants.SUCCESS;
	}
	
	@AuthorizedUserGroup(value = REPORTS_GROUP)		
	@PostMapping("/openordersreport/deletetemplate")
	@ResponseBody
	public String deleteOpenOrdersReportTemplate(final Model model, final String templateName) {

		try {
			jnjLatamReportsFacade.deleteOpenOrdersReportTemplate(templateName);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.SUCCESS,
					JnjlaglobalreportsaddonControllerConstants.SUCCESS);
			return JnjlaglobalreportsaddonControllerConstants.SUCCESS;

		} catch (Exception exe) {
			LOG.error("Error occurred while deleting the template ", exe);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.ERROR,
					JnjlaglobalreportsaddonControllerConstants.ERROR);

			return JnjlaglobalreportsaddonControllerConstants.ERROR;
		}
	}
	
	private JnjGTPageableData populateOpenOrdersPageableData(final JnjLaOpenOrdersReportForm jnjLaOpenOrdersReportForm)
	{
		final String METHOD_NAME = "populateOpenOrdersPageableData()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		JnjGTPageableData jnjGTPageableData = null;
		
		if (null != jnjLaOpenOrdersReportForm)
		{			
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"Populating the jnjGTPageableData!");
			if (null != jnjLaOpenOrdersReportForm.getAccountIds())
			{
				jnjGTPageableData = new JnjGTPageableData();
				
				if (CollectionUtils.isNotEmpty(jnjLaOpenOrdersReportForm.getAccountIds())) {
				    jnjGTPageableData.setSearchParamsList(jnjLaOpenOrdersReportForm.getAccountIds());
				}
				jnjGTPageableData.setFromDate(jnjLaOpenOrdersReportForm.getFromDate());
				jnjGTPageableData.setToDate(jnjLaOpenOrdersReportForm.getToDate());
				jnjGTPageableData.setOrderType(jnjLaOpenOrdersReportForm.getOrderType());
				jnjGTPageableData.setSearchBy(jnjLaOpenOrdersReportForm.getProductCode());
				jnjGTPageableData.setSearchText(jnjLaOpenOrdersReportForm.getOrderNumber());
				jnjGTPageableData.setAdditionalSearchText(jnjLaOpenOrdersReportForm.getShipTo());
				
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData has been populated!");
			}
			else
			{
				logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
						"jnjGTPageableData not populated as one or more form elements were null!");
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"JnjGTBackorderReportForm was found to be null!");
		}

		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTPageableData;
	}

	private void generateOpenOrdersReportResponse(final Model model, final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "generateOpenOrdersReportResponse()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		if (null != jnjGTPageableData)
		{
			String msg = LoginaddonConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Calling reports facade to fetch open orders report.";
			LOG.info(msg);
			final List<JnjLaOpenOrdersReportReponseData> jnjLaOpenOrdersReportResponseDataList = jnjLatamReportsFacade
					.fetchOpenOrdersReport(jnjGTPageableData);
			if (CollectionUtils.isNotEmpty(jnjLaOpenOrdersReportResponseDataList))
			{
				model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_RESPONSE_DATA,
						jnjLaOpenOrdersReportResponseDataList);
			}
			else
			{
				model.addAttribute(NO_DATA_FOUND_FLAG, Boolean.TRUE);
			}
		}
		else
		{
			logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME,
					"jnjGTPageableData was found to be null! Moving to regular backorder page load!");
		}
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	
	@AuthorizedUserGroup(value = REPORTS_GROUP)	
	@GetMapping("/openordersreport")
	public String getOpenOrdersReport(final Model model) throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getOpenOrdersReportPage()";
		logMethodStartOrEnd(OPEN_ORDERS_REPORT, METHOD_NAME, Logging.BEGIN_OF_METHOD);		
		createBreadCrumbsForReports(Config.getParameter(
				JnjlaglobalreportsaddonControllerConstants.BREADCRUMB_OPEN_ORDERS_REPORT_URL), model,
				getMessageSource().getMessage(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_STRING,
				null, getI18nService().getCurrentLocale()),
				getMessageSource().getMessage(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_STRING,
				null, getI18nService().getCurrentLocale()),null);
		
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_FORM,
				new JnjLaOpenOrdersReportForm());
		
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.ORDER_TYPE, jnjLatamReportsFacade.getOrderTypes());
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_TEMPLATE_DATA,
				jnjLatamReportsFacade.getOpenOrdersReportTemplate());
		setCurrentB2BUnitIdInModel(model);
		
		setupModel(model, JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_PAGE_STRING);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info(ACCOUNT_LOG_MSG, showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info(ACCOUNT_LOG_MSG1, showChangeAccountLink);
			model.addAttribute(JnjlaglobalreportsaddonControllerConstants.SHOW_CHANGE_ACCOUNT_LINK, Boolean.TRUE);
		}
	
		logMethodStartOrEnd(OPEN_ORDERS_REPORT, METHOD_NAME, Logging.END_OF_METHOD);

		return getView(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_PAGE_PATH);
	}
	
	/**
	 * This method returns the generated excel or PDF containing the results for the open-order report search query
	 * @param model the view model used to store data
	 * @param jnjLaOpenOrdersReportForm the form object carrying search criteria
	 * @return download view
	 */	
	@AuthorizedUserGroup(value = REPORTS_GROUP)
	@PostMapping("/openordersreport/download")
	public String downloadOpenOrdersReport(final Model model, @ModelAttribute final JnjLaOpenOrdersReportForm jnjLaOpenOrdersReportForm)
	{
		final String methodName = "downloadOpenOrdersReport()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.BEGIN_OF_METHOD);		
		model.addAttribute(JnjlaglobalreportsaddonControllerConstants.OPEN_ORDERS_REPORT_FORM,
				jnjLaOpenOrdersReportForm);
		
		generateOpenOrdersReportResponse(model, populateOpenOrdersPageableData(jnjLaOpenOrdersReportForm));

		CMSSiteModel cmsSiteModel = getCmsSiteService().getCurrentSite();
		
		final CatalogVersionModel currentCatalog = cmsSiteModel.getContentCatalogs().get(0).getActiveCatalogVersion();
		model.addAttribute(SITE_LOGO_PATH, mediaService.getMedia(currentCatalog,EPIC_EMAIL_LOGO_IMAGE_ONE).getURL());

		final String url = mediaService.getMedia(currentCatalog,EPIC_EMAIL_LOGO_IMAGE_ONE).getURL();
		final String baseURL = String.format(getConfigurationService().getConfiguration().getString(BASE_URL),
				cmsSiteModel.getUid().split(CMS)[0]);
		model.addAttribute("siteLogoPathURL", baseURL+url);
		model.addAttribute(SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, EPIC_EMAIL_LOGO_IMAGE_ONE)));

		logDebugMessage(LoginaddonConstants.Logging.REPORTS_NAME, methodName, RESPONSE_READY_DOWNLOAD);
		logMethodStartOrEnd(LoginaddonConstants.Logging.REPORTS_NAME, methodName, Logging.END_OF_METHOD);
		
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjLaOpenOrdersReportForm.getDownloadType()))
				? OPEN_ORDER_REPORT_PDF_VIEW : OPEN_ORDER_REPORT_EXCEL_VIEW;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

}