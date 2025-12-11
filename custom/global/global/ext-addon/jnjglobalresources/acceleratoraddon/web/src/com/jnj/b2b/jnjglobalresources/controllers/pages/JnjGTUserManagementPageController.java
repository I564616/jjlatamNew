package com.jnj.b2b.jnjglobalresources.controllers.pages;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.services.CMSSiteService;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.b2b.jnjglobalresources.controllers.JnjglobalresourcesControllerConstants;
import com.jnj.b2b.jnjglobalresources.form.JnjGTB2BCustomerForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSearchDTO;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.b2b.jnjglobalresources.form.JnjGTUserSearchForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.utils.CommonUtil;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.services.CMSSiteService;

/**
 * This Controller is used for Search Users and User Management Activites : Create and Edit
 *
 * @author Accenture
 * @version 1.0
 */
@Controller("jnjGTUserManagementPageController")
@Scope("tenant")
@RequestMapping("/resources/usermanagement")
public class JnjGTUserManagementPageController extends JnjGTMyCompanyPageController
{
	protected static final Logger LOG = Logger.getLogger(JnjGTUserManagementPageController.class);

	@Autowired
	protected SessionService sessionService;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjgtCustomerFacade;

	@Autowired
	protected MessageFacadeUtill messageFacade;

    @Resource(name = "GTOrderFacade")
    protected JnjGTOrderFacade orderFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	/** The Enumerator DOWNLOAD_TYPE. **/
	protected enum DOWNLOAD_TYPE
	{
		/** The PDF. **/
		PDF,
		/** The EXCEL. **/
		EXCEL, NONE;
	}
	
	@Autowired
	protected CMSSiteService cMSSiteService;
	@Autowired
	protected MediaService mediaService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjgtCustomerFacade;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	public JnjGTOrderFacade getOrderFacade() {
		return orderFacade;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	protected static final String SELECTED_LINK = "selectedLink";
	protected final String RESOURCE_LABEL = "resource.RESOURCE.text";
	protected final String USER_MANAGEMENT_LABEL = "userSearch.breadCrumb";
	protected static final String RESOURCES_USEFULLINKS = "/resources/usefullinks";

	protected static final String ACCOUNT_SEARCH_TERM = "accountSearchTerm";
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String ACCOUNT_PAGINATION_DATA = "accountPaginationData";
	protected static final String CURRENT_PAGE = "currentPage";
	protected static final String USERMANAGEMENT_FORM_NAME = "JnjGTUserSearchForm";
	protected static final String USER_MANAGEMENT_REPORT_PDF_VIEW = "JnJGTUserManagementReportPdfView";
	protected static final String USER_MANAGEMENT_REPORT_EXCEL_VIEW = "JnJGTUserManagementReportExcelView";

	
	/* This method is used for when the Admin User Search User as well as For UserManagment Build */
	@RequestMapping(method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String manageUsers(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.NAME) String sortCode, final Model model,
			JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException, BusinessException
    {
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		return manageUsersCommon(page, showMode, sortCode, model, userSearchForm);
	}
	protected String manageUsersCommon(final int page, final ShowMode showMode, String sortCode, final Model model, JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException, BusinessException 
	{
		final String METHOD_NAME = "manageUsers()";
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.USER_MANAGEMENT, METHOD_NAME, "Enter User Search Page", LOG);

		/** Checking if user is Admin / CSR **/
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}

		final Object existsUserSearchForm = sessionService.getAttribute("userSearchForm");
		if (existsUserSearchForm != null)
		{
			userSearchForm = (JnjGTUserSearchForm) existsUserSearchForm;
			sessionService.removeAttribute("userSearchForm");
		}
		if (userSearchForm.getSortBy() != null)
		{
			sortCode = userSearchForm.getSortBy();
		}

		final int finalPageSize = (userSearchForm.isShowMore()) ? userSearchForm.getPageSize()
				* userSearchForm.getShowMoreCounter() : userSearchForm.getPageSize();
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, sortCode, showMode);
		pageableData.setSearchUserFlag(userSearchForm.isSearchFlagUserSearch());
		pageableData.setSearchDtoList(setSearchDtoList(userSearchForm));
		final SearchPageData<CustomerData> searchPageData = jnjGTB2BCommerceUserFacade.searchCustomers(pageableData);
		populateModel(model, searchPageData, showMode);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE));
		model.addAttribute("pageSize", Long.valueOf(searchPageData.getPagination().getPageSize()));
		model.addAttribute("totalResults", Long.valueOf(searchPageData.getPagination().getTotalNumberOfResults()));
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		/*breadcrumbs.add(new Breadcrumb(RESOURCES_USEFULLINKS, messageFacade.getMessageTextForCode(RESOURCE_LABEL, getI18nService()
				.getCurrentLocale()), null));*/
		breadcrumbs.add(new Breadcrumb("/resources/usermanagement", messageFacade.getMessageTextForCode(USER_MANAGEMENT_LABEL,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("searchUserForm", userSearchForm);
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		//Setting the Search Form Only In Case This method Is Called By Search Button,
		if (userSearchForm.isSearchFlagUserSearch())
		{
			model.addAttribute("searchUserForm", userSearchForm);
		}
		else
		{
			final JnjGTUserSearchForm newForm = new JnjGTUserSearchForm();
			newForm.setPageSize(userSearchForm.getPageSize());
			newForm.setSortBy(userSearchForm.getSortBy());
			model.addAttribute("searchUserForm", newForm);
		}
		final JnjGTB2BCustomerForm jnjGTB2BCustomerForm = new JnjGTB2BCustomerForm();
		model.addAttribute("showmoreCount",userSearchForm.getShowMoreCounter());
		model.addAttribute("createUserForm", jnjGTB2BCustomerForm);
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsersPage);
	}
	
	
	/**
	 *
	 * This method returns the generated excel or PDF containing the results for the User Management report search query
	 *
	 * @param model
	 * @param JnjGTBackorderReportForm
	 * @return download view
	 */
	@PostMapping("/downloadReport")
	public String downloadUserManagementReport(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.NAME) String sortCode,final Model model,
			@ModelAttribute final JnjGTUserSearchForm jnjGTUserSearchForm) {
		final String METHOD_NAME = "downloadUserManagementReport()";

		model.addAttribute(USERMANAGEMENT_FORM_NAME, jnjGTUserSearchForm);
		//if(String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTUserSearchForm.getDownloadType())){
			 /*site log */
	    final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
	    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
	    //Send site logo
	    /* Excel image adding Started here */
		model.addAttribute("siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		 /* Excel image adding end here */
		
		/* PDF image adding Started here */
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
		 /* PDF image adding end here */
		/** Generate  User Management report Response and set it in model by passing the searchPageData by calling populateModel **/
		 final int finalPageSize = (jnjGTUserSearchForm.isShowMore()) ? jnjGTUserSearchForm.getPageSize()
					* jnjGTUserSearchForm.getShowMoreCounter() : jnjGTUserSearchForm.getPageSize();
		 final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, sortCode, showMode);
		pageableData.setSearchUserFlag(jnjGTUserSearchForm.isSearchFlagUserSearch());
		pageableData.setSearchDtoList(setSearchDtoList(jnjGTUserSearchForm));
		final SearchPageData<CustomerData> searchPageData = jnjGTB2BCommerceUserFacade.searchCustomers(pageableData);
		populateModel(model, searchPageData, showMode);
		/** Returning the view for PDF or excel **/
		return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTUserSearchForm.getDownloadType())) ? USER_MANAGEMENT_REPORT_PDF_VIEW
				: USER_MANAGEMENT_REPORT_EXCEL_VIEW;
	}
	

	
		//click on edit user link
	@RequestMapping(value = "/edit", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String editUser(@RequestParam("user") final String user, final Model model, final JnjGTUserSearchForm userSearchForm)
			throws CMSItemNotFoundException, BusinessException
	{
		sessionService.setAttribute("userSearchForm", userSearchForm);

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		/** Checking if user is Admin / CSR **/
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}
		model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);
		return super.editUser(user, model);
	}

	/**
	 * Edit User Functionality
	 *
	 * @throws BusinessException
	 */ //clicking save user for edit
	@Override
	@PostMapping("/editUser")
	public String editUser(final String user, final JnjGTB2BCustomerForm b2bCustomerForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request)
			throws CMSItemNotFoundException, BusinessException
	{
		
		/** Checking if user is Admin / CSR **/
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}

		return super.editUser(user, b2bCustomerForm, bindingResult, model, redirectModel, request);
	}

	@RequestMapping(value = "/create", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String createUser(final Model model, final JnjGTUserSearchForm userSearchForm) throws CMSItemNotFoundException,
			BusinessException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		/** Checking if user is Admin / CSR **/
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}
		model.addAttribute(SELECTED_LINK, Jnjb2bCoreConstants.Resources.USER_MANAGEMENT_LINK_COMPONENT_ID);
		model.addAttribute("action", "manageUsers");
		sessionService.setAttribute("userSearchForm", userSearchForm);
		return super.createUser(model);
	}

	@PostMapping("/createUser")
	@RequireHardLogIn
	public String createUser(@ModelAttribute("jnjGTB2BCustomerForm") final JnjGTB2BCustomerForm jnjGTB2BCurstomerForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectModel,final HttpServletRequest request)
			throws CMSItemNotFoundException, BusinessException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		/** Checking if user is Admin / CSR **/
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}

		model.addAttribute("action", "manageUsers");
		return super.createUser(jnjGTB2BCurstomerForm, bindingResult, model, redirectModel,request);
	}

	@Override
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
	 * @param userSearchForm
	 */
	protected List<JnjGTSearchDTO> setSearchDtoList(final JnjGTUserSearchForm userSearchForm)
	{

		final List<JnjGTSearchDTO> searchDtoList = new ArrayList<JnjGTSearchDTO>();

		if (StringUtils.isNotEmpty(userSearchForm.getPhone()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.PHONE);
			searchDTO.setSearchValue(userSearchForm.getPhone());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}
		if (StringUtils.isNotEmpty(userSearchForm.getSector()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.SECTOR);
			searchDTO.setSearchValue(userSearchForm.getSector());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getLastName()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.LASTNAME);
			searchDTO.setSearchValue(userSearchForm.getLastName());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getFirstName()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.FIRSTNAME);
			searchDTO.setSearchValue(userSearchForm.getFirstName());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getAccountName()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.ACCOUNTNAME);
			searchDTO.setSearchValue(userSearchForm.getAccountName());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getAccountNumber()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.ACCOUNTNUMBER);
			searchDTO.setSearchValue(userSearchForm.getAccountNumber());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}
		if (StringUtils.isNotEmpty(userSearchForm.getStatus()))
		{

			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.STATUS);
			searchDTO.setSearchValue(userSearchForm.getStatus());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getEmail()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.EMAIL);
			searchDTO.setSearchValue(userSearchForm.getEmail());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		if (StringUtils.isNotEmpty(userSearchForm.getRole()))
		{
			final JnjGTSearchDTO searchDTO = new JnjGTSearchDTO();
			searchDTO.setSearchBy(Jnjb2bCoreConstants.UserSearch.ROLE);
			searchDTO.setSearchValue(userSearchForm.getRole());
			searchDTO.setSearchType(Jnjb2bCoreConstants.UserSearch.LIKE);
			searchDtoList.add(searchDTO);
		}

		return searchDtoList;
	}

	@PostMapping("/isUidExists")
	@ResponseBody
	public boolean isUidExists(final Model model, @RequestParam("email") final String email)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		

		return jnjgtCustomerFacade.validateUid(email.toLowerCase());
	}

	@PostMapping("/accountsSelection")
	public String getAccountSelection(final Model model, @RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm)
	{
		/** Checking if user is Admin / CSR **/
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final String userTierType = String.valueOf(sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE));
		if (!(Jnjb2bCoreConstants.UserSearch.USER_TIER2.equalsIgnoreCase(userTierType) || Jnjb2bCoreConstants.UserSearch.USER_TIER1
				.equalsIgnoreCase(userTierType)))
		{
			return REDIRECT_PREFIX + RESOURCES_USEFULLINKS;
		}

		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

		final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm))
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}
		/** Calling facade layer **/
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, false, false, pageableData);

		model.addAttribute(Jnjb2bCoreConstants.Login.ACCOUNT_LIST, accountSelectionData.getAccountsMap());
		model.addAttribute("currentAccountId", orderFacade.getCurrentB2bUnitId());

		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		/** Returning view **/
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.Misc.AccountSelectionPageForUserManagement);
	}

	@PostMapping("/resetPassword")
	@ResponseBody
	public Map<String, String> resetPassword(final Model model, @RequestParam("email") final String email,
			final HttpServletRequest request) throws DuplicateUidException, UnsupportedEncodingException
	{
		final Map<String, String> responseMap = new HashMap<String, String>();
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final boolean isPwdReset = jnjgtCustomerFacade.resetUserPassword(email.toLowerCase());
		if (isPwdReset)
		{
			final String siteUrl = JnjWebUtil.getServerUrl(request);
			final String logoUrl = JnjWebUtil.getUnsecureServerUrl(request);
			jnjgtCustomerFacade.sentPasswordExpirymail(logoUrl, email, siteUrl);
			responseMap.put(Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS,
					jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.UserSearch.PASSWORD_CHANGE__SUCCESS_MESSAGE));
		}
		else
		{
			responseMap.put(Jnjb2bCoreConstants.UserSearch.FAIL,
					jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.UserSearch.PASSWORD_CHANGE_FAILURE_MESSAGE));
		}
		return responseMap;
	}

	@PostMapping("/addToNotes")
	@ResponseBody
	public String addToNotes(@RequestParam("existingNotes") final String existingNotes,final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		return jnjgtCustomerFacade.addToExistingNotes(existingNotes);

	}
	
	
	
	@PostMapping("/enableOrDisableUser")
	@ResponseBody
	public String enableOrDisableUser( @RequestParam("status") final boolean status,@RequestParam("emailAddress") final String emailAddress, final Model model)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : " + showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			LOG.info("entered condition...........showChangeAccountLink :" + showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjgtCustomerFacade.enableOrDisableUser(status, emailAddress);
	}
	
	public String getView(final String view){
        return JnjglobalresourcesControllerConstants.ADDON_PREFIX + view;
 }
	//for GT no need this delete func
	/*@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUser( @RequestParam("uid") final String uid, final Model model)	{
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : " + showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjgtCustomerFacade.deleteUser(uid);
	}*/
	
	@PostMapping("/selectAccount")
	public String getSelectAccountPopup(final Model model, final String[] selectedAccounts, final JnjGTB2BCustomerForm form,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm)
	{

		
		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

		final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm))
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}
		/** Calling facade layer **/
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, false, false, pageableData);

		model.addAttribute("popUpType","SELECT_ACCOUNTS");
		model.addAttribute("accounts", accountSelectionData.getAccountsMap());
		model.addAttribute("currentAccountId", orderFacade.getCurrentB2bUnitId());
		model.addAttribute("selectedAccounts", selectedAccounts);
		model.addAttribute("jnjGTB2BCustomerForm", form);

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
	/*	LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);*/
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			/*LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);*/
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.SelectsAccountPopups);
		
	}
	@PostMapping("/editUserselectAccount")
	public String getSelectAccountEditPopup(final Model model, final String[] selectedAccounts, final JnjGTB2BCustomerForm form,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "user", defaultValue = "") final String user,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm)
	{

		
		final JnjGTCustomerData customerData = jnjgtCustomerFacade.getCustomerForUid(user);
		final List<B2BUnitData> b2bUnits = customerData.getB2bUnits();

		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

		final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm))
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}
		/** Calling facade layer **/
		
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(true,false,false, pageableData);

		model.addAttribute("popUpType","SELECT_ACCOUNTS");
		model.addAttribute("accounts", accountSelectionData.getAccountsMap());
		model.addAttribute("currentAccountId",orderFacade.getCurrentB2bUnitId());
		model.addAttribute("accountToAdded",b2bUnits);
		model.addAttribute("selectedAccounts", selectedAccounts);
		model.addAttribute("jnjGTB2BCustomerForm", form);

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		return getView(JnjglobalresourcesControllerConstants.Views.Pages.MyCompany.SelectsEditAccountPopups);
	}
	
}
