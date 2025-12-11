/**
 *
 */
package com.jnj.b2b.cartandcheckoutaddon.controllers.pages;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.cartandcheckoutaddon.forms.ContractForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.contract.JnjGTContractFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjContractPriceData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.services.CMSSiteService;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;


/**
 * The Class JnjGTContractController.
 *
 * @author komal.sehgal
 *
 *
 *         This is used for check out functionality for Contracts
 */

@Controller("JnjGTContractController")
@Scope("tenant")
@RequestMapping(value = "/my-account/contract")
public class JnjGTContractController extends AbstractSearchPageController
{
	/**
	 * The Enum DOWNLOAD_TYPE.
	 */
	protected enum DOWNLOAD_TYPE
	{

		/** The pdf. */
		PDF,
		/** The excel. */
		EXCEL;
	}

	/** The Constant RESULT_PDF. */
	protected static final String RESULT_PDF = "JnjGTContractPdfView";
	protected static final String RESULT_EXCEL = "JnjGTContractExcelView";
	//for contract detail page download
	protected static final String RESULT_DETAIL_PDF = "JnjGTContractDetailPdfView";
	protected static final String RESULT_DETAIL_EXCEL = "JnjGTContractDetailExcelView";
	
	protected static final String CURRENT_PAGE = "currentPage";
	protected static final String ACCOUNT_SEARCH_TERM = "accountSearchTerm";
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String ACCOUNT_PAGINATION_DATA = "accountPaginationData";
	protected static final String EMPTY = "empty";
	protected static final String LINES = "lines";
	protected static final String CART_DATA = "cartData";
	protected static final String TEXT_HTML = "text/html";
	protected static final String PRODUCT = "product";
	protected static final String CMS_HOME_PAGE = "home";
	protected static final String CMS_SURVEY_PAGE = "surveyPage";
	protected static final int MAX_PAGE_LIMIT = 100;
	protected static final String CART_MODIFICATAION_DATA = "cartModificationData";
	protected static final String ADD_STATUS = "addStatus";
	protected static final String SHOW_CONTRACT ="showContractLink";

	/** The Constant LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTContractController.class);

	/** The jn j contract facade. */
	@Autowired
	protected JnjGTContractFacade jnjGTContractFacade;
	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;

	/** The jnj price data factory. */
	@Resource(name = "priceDataFactory")
	protected JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	protected SessionService sessionService;

	/** The account breadcrumb builder. */
	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/
	
	/** The user service. */
	@Autowired
	protected UserService userService;

	@Autowired
	protected MediaService mediaService;
	
	@Autowired
	protected CMSSiteService cMSSiteService;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	/** The Constant CONTRACT_NUMBER_PATH_VARIABLE_PATTERN. */
	protected static final String CONTRACT_NUMBER_PATH_VARIABLE_PATTERN = "{contractNumber:.*}";

	/** The Constant CONTRACT_CMS_PAGE. */
	protected static final String CONTRACT_CMS_PAGE = "contract";

	/** The Constant CONTRACT__DETAILS_CMS_PAGE. */
	protected static final String CONTRACT__DETAILS_CMS_PAGE = "contractDetail";

	

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @return the mediaService
	 */
	public MediaService getMediaService()
	{
		return mediaService;
	}



	/**
	 * Show contracts. This method is used for getting the contracts for the B2BUnit
	 *
	 * @param model
	 *           the model
	 * @return the string
	 */
	@GetMapping("/getContracts")
	public String showContracts(final Model model)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SHOW_CONTRACTS_METHOD
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}

		final List<JnjContractData> contractDataList = jnjGTContractFacade.getContracts();
		model.addAttribute("contractDataList", contractDataList);
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SHOW_CONTRACTS_METHOD
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.ContractPopup);


	}



	/**
	 * Get Contracts :This method is used to get the contracts For MY Contracts Page
	 *
	 * @param page
	 *           the page
	 * @param showMode
	 *           the show mode
	 * @param sortCode
	 *           the sort code
	 * @param model
	 *           the model
	 * @param form
	 *           the form
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	@GetMapping
	public String searchContracts(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form)
			throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_METHOD
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}

		try
		{
			final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
			final PageableData pageableData = createPageableData(page, finalPageSize, sortCode, showMode);
			final SearchPageData<JnjContractData> searchPageData = jnjGTContractFacade.getPagedContracts(pageableData,
					form.getSearchByCriteria(), form.getSearchParameter(), form.getSortByCriteria(), form.getSelectCriteria(), form.getFilterCriteria2());
			if (searchPageData.getResults().size() > 0)
			{
				model.addAttribute("contractList", searchPageData.getResults());
			}
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("searchPageData", searchPageData);
			final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
			preparePage(model);
		}
		catch (final CMSItemNotFoundException exception)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SEARCH_CONTRACT + "-"
					+ exception.getMessage() + JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.END_OF_METHOD + "-"
					+ Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		//return ControllerConstants.Views.Pages.Account.Contract;
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Account.Contract);
	}

	/**
	 * Prepare page: This method is used for preparing the page for Diaplay.
	 *
	 * @param model
	 *           the model
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	protected void preparePage(final Model model) throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.contracts"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		if (!model.containsAttribute("contractForm"))
		{
			model.addAttribute("contractForm", new ContractForm());
		}
	}




	/**
	 * Gets the Contract details : This method is used for getting contract details on various search Criteria.
	 *
	 * @param page
	 *           the page
	 * @param showMode
	 *           the show mode
	 * @param sortCode
	 *           the sort code
	 * @param model
	 *           the model
	 * @param form
	 *           the form
	 * @return the order details
	 *///search button click from contract page
	@PostMapping("/search")
	public String getContractDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form)
	{
		boolean noResultFlag = false;
		int noResult = 0;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SEARCH_CONTRACT + "-"
					+ Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			if (StringUtils.isNotEmpty(form.getLoadNoOfRecords()))
			{
				noResult = Integer.parseInt(form.getLoadNoOfRecords());
			}
			else
			{
			//	noResult = form.getNoOfRecords();
				noResult = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
			}

			final PageableData pageableData = createPageableData(page, noResult, sortCode, showMode);
			final SearchPageData<JnjContractData> searchPageData = jnjGTContractFacade.getPagedContracts(pageableData, form
					.getSearchByCriteria().trim(), form.getSearchParameter(), form.getSortByCriteria(), form.getSelectCriteria(),form.getFilterCriteria2());
			model.addAttribute("totalNoOfRecords", String.valueOf(searchPageData.getPagination().getTotalNumberOfResults()));
			model.addAttribute("contractList", searchPageData.getResults());

			if (searchPageData.getResults().size() > 0)
			{
				model.addAttribute("contractList", searchPageData.getResults());
			}
			else
			{
				noResultFlag = true;
				model.addAttribute("searchCriteria", form.getSearchByCriteria());
				model.addAttribute("searchParameter", form.getSearchParameter());
				model.addAttribute("noResultFlag", Boolean.valueOf(noResultFlag));
			}
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("contractForm", form);
			final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
			preparePage(model);
		}
		catch (final CMSItemNotFoundException exception)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SEARCH_CONTRACT + "-"
					+ "Contract page not found in current content" + exception.getMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_SEARCH_CONTRACT + "-"
					+ Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		model.addAttribute("scrollPos", scrollPos);
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Account.Contract);
	}


	/**
	 * This method is to download PDF for the list of Contracts
	 *
	 * @param model
	 * @param downloadType
	 * @return string
	 */
	@PostMapping("/downloadData")
	public String downloadContractsList(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType) {

		boolean noResultFlag = false;
		int noResult = 0;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_LIST + "-"
					+ Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		try {
			if (StringUtils.isNotEmpty(form.getLoadNoOfRecords())) {
				noResult = Integer.parseInt(form.getLoadNoOfRecords());
			} else {
				noResult = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
			}

			final PageableData pageableData = createPageableData(page, noResult, sortCode, showMode);
			final SearchPageData<JnjContractData> searchPageData = jnjGTContractFacade.getPagedContracts(pageableData, form
					.getSearchByCriteria().trim(), form.getSearchParameter(), form.getSortByCriteria(), form.getSelectCriteria(), form.getFilterCriteria2());
			model.addAttribute("totalNoOfRecords", String.valueOf(searchPageData.getPagination().getTotalNumberOfResults()));
			model.addAttribute("contractList", searchPageData.getResults());

			if (searchPageData.getResults().size() > 0) {
				model.addAttribute("contractList", searchPageData.getResults());
			} else {
				noResultFlag = true;
				model.addAttribute("searchCriteria", form.getSearchByCriteria());
				model.addAttribute("searchParameter", form.getSearchParameter());
				model.addAttribute("noResultFlag", Boolean.valueOf(noResultFlag));
			}
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("contractForm", form);
			preparePage(model);
		}
		catch (final CMSItemNotFoundException exception) {
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_LIST + "-"
					+ "Contract page not found in current content" + exception.getMessage() + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_LIST + "-"
					+ Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		
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
		 
		 /*site log */
	    final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
	    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
	  //Send site logo
		model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
				+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		model.addAttribute("scrollPos", scrollPos);
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)) ? RESULT_EXCEL
				: getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.ContractPopup);
	}


	/**
	 * Gets the contract details :This method is used for contract Detail for a given contract number.
	 *
	 * @param contractNumber
	 *           the contract number
	 * @param entryCount
	 *           the entry count
	 * @param model
	 *           the model
	 * @return the contract details
	 */
	@GetMapping("/getContractDetails/" + CONTRACT_NUMBER_PATH_VARIABLE_PATTERN)
	public String getContractDetails(@PathVariable("contractNumber") final String contractNumber,
			@RequestParam(value = "entryCount", defaultValue = "30") final int entryCount, final Model model)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_DETAILS
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			final JnjContractData contractData = jnjGTContractFacade.getContractDetailsById(contractNumber, entryCount);
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			//JCC - 13 change start
			final CurrencyModel currencyModel = currentUser.getSessionCurrency();
			final JnjContractPriceData cntrctPriceData = new JnjContractPriceData();
			cntrctPriceData.setTotalAmount(createPrice(currencyModel, contractData.getTotalAmount()));
			cntrctPriceData.setBalanceAmount(createPrice(currencyModel, contractData.getBalanceAmount()));
			cntrctPriceData.setConsumedAmount(createPrice(currencyModel, contractData.getConsumedAmount()));
			//final PriceData priceData = createPrice(currencyModel, contractData.getTotalAmount());
			final List<JnjContractEntryData> contractEntryList = contractData.getContractEntryList();
			final Map<String, JnjContractPriceData> cntrctEntryMap = new HashMap<String, JnjContractPriceData>();

			for (final JnjContractEntryData cntrctEntrData : contractEntryList)
			{

				final JnjContractPriceData cntractPriceData = new JnjContractPriceData();
				cntractPriceData.setBalanceAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractBalanceQty())));
				cntractPriceData.setTotalAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractQty())));
				cntractPriceData.setConsumedAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getConsumedQty())));

				cntrctEntryMap.put(cntrctEntrData.getProductCode(), cntractPriceData);
			}
			//JCC - 13 change end

			storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			String language = null;
			//final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			if (null != currentUser.getSessionLanguage())
			{
				language = currentUser.getSessionLanguage().getIsocode();
			}
			model.addAttribute("sessionlanguage", language);
			model.addAttribute("contractData", contractData);
			//
			model.addAttribute("cntrctPriceData", cntrctPriceData);
			model.addAttribute("cntrctEntryMap", cntrctEntryMap);
			model.addAttribute("contractEntryList", contractEntryList);
			//model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.contracts"));
			setContractDetailBreadCrumb(model);
			model.addAttribute("metaRobots", "no-index,no-follow");
			model.addAttribute("canCheckout", Boolean.valueOf(jnjGetCurrentDefaultB2BUnitUtil.validateCheckoutUser()));
			final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
		}
		catch (final CMSItemNotFoundException exception)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_DETAILS
					+ "-" + "Contract Detail page not found in current content" + exception.getMessage());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_GET_CONTRACTS_DETAILS
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Account.ContractDetail);
	}
	protected void setContractDetailBreadCrumb(final Model model) {
		final MessageSource messageSource = getMessageSource();
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("/my-account/contract",messageSource.getMessage("text.account.contracts", null, getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb(null, messageSource.getMessage("contract.detail.heading", null, getI18nService().getCurrentLocale()), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}

	//JCC - 13 change
	protected PriceData createPrice(final CurrencyModel currency, final Double val)
	{
		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;
		return jnjPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	/**
	 * @param view
	 * @return String
	 */
	public String getView(String view) {
		return CartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}

	/**
	 * @param form
	 * @param model
	 * @param selectedProductCatalogIds
	 * @return string
	 * @throws CommerceCartModificationException
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/addToCart", method = { RequestMethod.GET, RequestMethod.POST })
	public String addToCartFromContract(final ContractForm form, final Model model,
			@RequestParam(value = "selectedProducts", required = false) String[] selectedProductCatalogIds)
					throws CommerceCartModificationException, CMSItemNotFoundException {
		return addToCartFromContract(selectedProductCatalogIds, model, form);
	}

	protected String addToCartFromContract(final String[] selectedProductCatalogIds, final Model model,
			ContractForm form) throws CMSItemNotFoundException {

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : " + showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			//LOGGER.info("entered condition...........showChangeAccountLink :" + showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "addToCartFromContract()";
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		// Add to Cart from Contract
		responseMap = jnjGTContractFacade.addToCartFromContract(responseMap, selectedProductCatalogIds,form.geteCCContractNum());
		//End
		if (!responseMap.isEmpty() && responseMap.containsKey(CART_MODIFICATAION_DATA)) {
			final JnjCartModificationData cartData = (JnjCartModificationData) responseMap.get(CART_MODIFICATAION_DATA);
			model.addAttribute(PRODUCT, cartData.getCartModifications().get(0).getEntry().getProduct());
			model.addAttribute(CART_DATA, cartData);
			responseMap.remove(CART_MODIFICATAION_DATA);
		}
		model.addAttribute(ADD_STATUS, responseMap);
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		CommonUtil.logMethodStartOrEnd(Logging.HOME_QUICK_CART, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);
		return getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
	}
	/**
	 * This method is used to check contract products alone in the cart
	 * @param form 
	 * @param model the model
	 * @param selectedProductCatalogIds 
	 * @return the string
	 * @throws CommerceCartModificationException 
	 * @throws CMSItemNotFoundException 
	 */ 
	@ResponseBody
	@GetMapping(value = "/isNonContractProduct",produces = "application/json")
	public ContractForm isNonContractProduct(final ContractForm form, final Model model, 
			@RequestParam(value = "selectedProducts", required = false) String[] selectedProductCatalogIds,
			@RequestParam(value = "eCCContractNum", required = false) String contractNum) 
			throws CommerceCartModificationException, CMSItemNotFoundException {
   		if (LOGGER.isDebugEnabled()) {
   			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_CONTRACTS_PRODUCT
   					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
   		}
			JnjContractFormData formData = jnjGTCartFacade.validateIsNonContract(selectedProductCatalogIds,contractNum);
			form.setNonContractProductInCart(formData.getIsNonContractProductInCart());// generally bypass this condition to show popup because  on cart product / selected product is mixed
			form.setNonContractProductInSelectedList(formData.getIsNonContractProductInSelectedList());
			form.setMultiContractCount(formData.getMultiContractCount());
			form.setMultiProductCount(formData.getMultiProductCount());
 		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_GET_CONTRACTS_PRODUCT
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return form;
	}
	
	/**
	 * @param form
	 * @param model
	 * @return boolean
	 * @throws CommerceCartModificationException
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@GetMapping("/removeNonContractProduct")
	public boolean removeNonContractProduct(final ContractForm form, final Model model,
											@RequestParam(value = "contractNum", required = false) String contractNum)
			throws CommerceCartModificationException, CMSItemNotFoundException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_REMOVE_NON_CONTRACTS_PRODUCT
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		boolean flag = jnjGTCartFacade.removeNonContractProduct();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACT_REMOVE_NON_CONTRACTS_PRODUCT
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return flag;
	}
	
	/**
	 * This method is to download PDF for the list of Contracts
	 * @param page 
	 * @param showMode 
	 * @param scrollPos 
	 * @param sortCode 
	 * @param model
	 * @param form 
	 * @param downloadType
	 * @param contractNumber 
	 * @param entryCount 
	 * @param eCCContractNum 
	 * @return string
	 */
	@PostMapping("/downloadDetailData")
	public String downloadDetailData(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "eCCContractNum", required = true ) final String eCCContractNum,
			@RequestParam(value = "entryCount", defaultValue = "30") final int entryCount) {

		
      		if (LOGGER.isDebugEnabled()) {
      			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST
      					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
      		}
      		
      		try {
      			final JnjContractData contractData = jnjGTContractFacade.getContractDetailsById(eCCContractNum, entryCount);
      			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
      			//JCC - 13 change start
      			final CurrencyModel currencyModel = currentUser.getSessionCurrency();
      			final JnjContractPriceData cntrctPriceData = new JnjContractPriceData();
      			cntrctPriceData.setTotalAmount(createPrice(currencyModel, contractData.getTotalAmount()));
      			cntrctPriceData.setBalanceAmount(createPrice(currencyModel, contractData.getBalanceAmount()));
      			cntrctPriceData.setConsumedAmount(createPrice(currencyModel, contractData.getConsumedAmount()));
      			//final PriceData priceData = createPrice(currencyModel, contractData.getTotalAmount());
      			final List<JnjContractEntryData> contractEntryList = contractData.getContractEntryList();
      			final Map<String, JnjContractPriceData> cntrctEntryMap = new HashMap<String, JnjContractPriceData>();
      
      			for (final JnjContractEntryData cntrctEntrData : contractEntryList) {
      
      				final JnjContractPriceData cntractPriceData = new JnjContractPriceData();
      				cntractPriceData.setBalanceAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractBalanceQty())));
      				cntractPriceData.setTotalAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractQty())));
      				cntractPriceData.setConsumedAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getConsumedQty())));
      
      				cntrctEntryMap.put(cntrctEntrData.getProductCode(), cntractPriceData);
      			}
      			//JCC - 13 change end
      
      			storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
      			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
      			String language = null;
      			if (null != currentUser.getSessionLanguage()) {
      				language = currentUser.getSessionLanguage().getIsocode();
      			}
      			model.addAttribute("sessionlanguage", language);
      			model.addAttribute("contractData", contractData);
      			//
      			model.addAttribute("cntrctPriceData", cntrctPriceData);
      			model.addAttribute("cntrctEntryMap", cntrctEntryMap);
      			model.addAttribute("contractEntryList", contractEntryList);
      			setContractDetailBreadCrumb(model);
      			model.addAttribute("metaRobots", "no-index,no-follow");
      			model.addAttribute("canCheckout", Boolean.valueOf(jnjGetCurrentDefaultB2BUnitUtil.validateCheckoutUser()));
      			
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
      			 
      			/*site log */
      		   final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
      		   final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
      		   //Send site logo
      		   model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
					   + mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
      			 
      		} catch (final CMSItemNotFoundException exception) {
      			LOGGER.error(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST
      					+ "-" + "Contract Detail page not found in current content" + exception.getMessage());
      		}
      		
      		if (LOGGER.isDebugEnabled()) {
      			LOGGER.debug(Jnjb2bCoreConstants.Logging.CONTRACTS + " - " + Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST
      					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
      		}
      		
      		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_DETAIL_PDF : (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)) ? RESULT_DETAIL_EXCEL
      				: getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.ContractPopup);
      	
		}
}