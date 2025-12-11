package com.jnj.b2b.jnjglobalordertemplate.controllers.pages;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.catalog.CatalogVersionService;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.servicelayer.media.MediaService;


import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.core.dto.JnjGTOrderTemplateForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.jnj.facades.template.JnjGTOrderTemplateFacade;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTTemplateDetailsForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Logging;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;

/**
 * @author balinder.singh
 * 
 */
@Controller("JnjGTOrderTemplatePageController")
@RequestMapping("/templates")
public class JnjGTOrderTemplatePageController extends com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController
{
	/**
	 * 
	 */
	protected static final String TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PROD_DESC = "{productDesc:.*}";
	
	protected static final String PAGE_COUNT = "page";

	/**  */
	protected static final String SHARED = "shared";

	/**  */
	protected static final String GROUPBY_LABEL_VALUE = "dropdown.groupby.value.";

	@Resource(name = "httpSessionRequestCache")
	protected HttpSessionRequestCache httpSessionRequestCache;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Resource(name = "jnjGTOrderTemplateFacade")
	protected JnjGTOrderTemplateFacade jnjGTOrderTemplateFacade;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Autowired
	protected UserFacade userFacade;
	
	@Autowired
	CMSSiteService cMSSiteService;
	@Autowired
	MediaService mediaService;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService() {
	    return catalogVersionService;
	}

	
	public HttpSessionRequestCache getHttpSessionRequestCache() {
		return httpSessionRequestCache;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTOrderTemplateFacade getJnjGTOrderTemplateFacade() {
		return jnjGTOrderTemplateFacade;
	}

	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public UserFacade getUserFacade() {
		return userFacade;
	}

	protected static final String TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN = "{templateCode:.*}";

	/** The Constant for Template and Template detail Page. */
	protected static final String TEMPLATE = "Template";
	protected static final String TEMPLATE_LABEL = "template.page.label";
	protected static final String TEMPLATE_DETAIL_LABEL = "templatedetail.page.label";
	protected static final String TEMPLATE_CREATE_LABEL = "templateCreate.page.label";
	protected static final String TEMPLATE_EDIT_LABEL = "templateEdit.page.label";
	protected static final String ORDER_TEMPLATE_CMS_PAGE = "orderTemplatePage";
	protected static final String ORDER_TEMPLATE_DETAIL_CMS_PAGE = "orderTemplateDetailPage";
	protected static final String SORT_DROP_DOWN = "sortDropDown";
	protected static final String GROUP_BY_DROP_DOWN = "groupBy";
	protected static final String GROUP_BY_DROP_DOWN_ID = "groupBy";
	protected static final String SORT_CONFIG_ID = "sortByTemplate";
	protected static final String SEARCH_DROP_DOWN = "searchDropDown";
	protected static final String SEARCH_CONFIG_ID = "searchByTemplate";
	protected static final String TOTAL_TEMPLATES = "totalTemplates";
	protected static final String SHOW_IN_GROUPS = "showinGroups";
	protected static final String ORDER_TEMPLATE_ID = "templateId";
	/** The Constant RESULT_PDF. */
	public static final String RESULT_PDF = "jnjGlobalOrderTemplatePdfView";
	/** The Constant RESULT_EXCEL. */
	public static final String RESULT_EXCEL = "jnjGlobalOrderTemplateExcelView";

	/** The Constant TEMPLATE_DETAILS_PDF. */
	public static final String TEMPLATE_DETAILS_PDF = "jnjGTOrderTemplateDetailPdfView"; //jnjNAOrderTemplateDetailPdfView
	/** The Constant TEMPLATE_DETAILS_EXCEL. */
	public static final String TEMPLATE_DETAILS_EXCEL = "jnjGTOrderTemplateDetailExcelView";  //jnjNAOrderTemplateDetailExcelView

	/** Getting the LOGGER. */
	protected static final Logger LOG = Logger.getLogger(JnjGTOrderTemplatePageController.class);

	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

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

	/**
	 * 
	 * This method is used for fetching the template page by setting essential data in the model
	 * 
	 */
	@GetMapping
	public String getTemplatePage(@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", required = false) String showinGroup,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final JnjGTOrderTemplateForm form) throws CMSItemNotFoundException
	{
		logMethodStartOrEnd(TEMPLATE, "getTemplatePage()", Logging.BEGIN_OF_METHOD);
		if (StringUtils.isEmpty(showinGroup))
		{
			showinGroup = jnjGTOrderTemplateFacade
					.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID).get(0)
					.toString();
		}
		final int showInGroupsOf = Integer.parseInt(showinGroup);
		form.setShowinGroups(showinGroup);
		final PageableData pageableData = createPageableData(page, showInGroupsOf, sortCode, showMode);
		final SearchPageData<JnjGTOrderTemplateData> searchPageData = jnjGTOrderTemplateFacade.getPagedTemplateForStatuses(
				pageableData, form);
		model.addAttribute("orderTemplate", searchPageData.getResults());
		if (null == searchPageData.getResults() || searchPageData.getResults().size() == 0)
		{
			model.addAttribute("noResultsFlag", true);
		}
		model.addAttribute("JnjGTOrderTemplateForm", new JnjGTOrderTemplateForm());
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, createBreadCrumbsForTemplate(null, model, templateLabel));
		populatePageDataTemplate(model, form);
		//Set counter for ascending or descending sort
		setAscDescCounter(model, form);
		logMethodStartOrEnd(TEMPLATE, "getTemplatePage()", Logging.END_OF_METHOD);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplatePage);
	}

	@PostMapping
	public String getTemplatePagePost(@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", required = false) String showinGroup,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final JnjGTOrderTemplateForm form) throws CMSItemNotFoundException
	{
		logMethodStartOrEnd(TEMPLATE, "getTemplatePagePost()", Logging.BEGIN_OF_METHOD);
		int showInGroupsOf = 0;
		if (StringUtils.isEmpty(showinGroup))
		{
			showinGroup = jnjGTOrderTemplateFacade
					.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID).get(0)
					.toString();
		}
		else
		{
			if (StringUtils.indexOf(showinGroup, ",") < 0)
			{
				showInGroupsOf = Integer.parseInt(showinGroup);
			}
			else
			{
				showInGroupsOf = Integer.parseInt(showinGroup.split(",")[0]);
			}
		}
		form.setShowinGroups(String.valueOf(showInGroupsOf));
		if (StringUtils.isNotEmpty(form.getSearchText()))
		{
			form.setSearchText(form.getSearchText().trim());
		}
		final PageableData pageableData = createPageableData(page, showInGroupsOf, sortCode, showMode);
		final SearchPageData<JnjGTOrderTemplateData> searchPageData = jnjGTOrderTemplateFacade.getPagedTemplateForStatuses(
				pageableData, form);
		model.addAttribute("orderTemplate", searchPageData.getResults());
		if (null == searchPageData.getResults() || searchPageData.getResults().size() == 0)
		{
			model.addAttribute("noResultsFlag", true);
		}
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, createBreadCrumbsForTemplate(null, model, templateLabel));
		model.addAttribute("JnjGTOrderTemplateForm", form);
		model.addAttribute("searchText", form.getSearchText());
		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		populatePageDataTemplate(model, form);
		//Set counter for ascending or descending sort
				setAscDescCounter(model, form);
		logMethodStartOrEnd(TEMPLATE, "getTemplatePagePost()", Logging.END_OF_METHOD);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplatePage);
	}

	@PostMapping("/downloadData")
	public String downloadTemplatePDFPagePost(@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", required = false) String showinGroup,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final JnjGTOrderTemplateForm form) throws CMSItemNotFoundException
	{
		logMethodStartOrEnd(TEMPLATE, "getTemplatePage()", Logging.BEGIN_OF_METHOD);
		int showInGroupsOf = 0;
		if (StringUtils.isEmpty(showinGroup))
		{
			showinGroup = jnjGTOrderTemplateFacade
					.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID).get(0)
					.toString();
		}
		else
		{
			showInGroupsOf = Integer.parseInt(showinGroup);
		}
		form.setShowinGroups(showinGroup);
		final PageableData pageableData = createPageableData(page, showInGroupsOf, sortCode, showMode);
		final SearchPageData<JnjGTOrderTemplateData> searchPageData = jnjGTOrderTemplateFacade.getPagedTemplateForStatuses(
				pageableData, form);
		model.addAttribute("orderTemplate", searchPageData.getResults());
		if (null == searchPageData.getResults() || searchPageData.getResults().size() == 0)
		{
			model.addAttribute("noResultsFlag", true);
		}
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, createBreadCrumbsForTemplate(null, model, templateLabel));
		model.addAttribute("JnjGTOrderTemplateForm", form);
		
		
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
		
		
		
		
		
		
		
		populatePageDataTemplate(model, form);
		logMethodStartOrEnd(TEMPLATE, "getTemplatePage()", Logging.END_OF_METHOD);
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
	}


	/**
	 * Utility method used for logging entry into / exit from any method.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
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
	 * To create breadcrumb for the template page
	 * 
	 * @param formURI
	 * @param model
	 * @param formSpecificKey
	 */
	protected List<Breadcrumb> createBreadCrumbsForTemplate(final String formURI, final Model model, final String formSpecificKey)
	{
		final String METHOD_NAME = "createBreadCrumbsForTemplate()";
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
		return breadcrumbs;
	}

	//page population with required fields
	protected void populatePageDataTemplate(final Model model, final JnjGTOrderTemplateForm form)
	{
		model.addAttribute(SEARCH_DROP_DOWN, jnjGTOrderTemplateFacade.getDropDownList(SEARCH_CONFIG_ID));
		model.addAttribute(SORT_DROP_DOWN, jnjGTOrderTemplateFacade.getDropDownList(SORT_CONFIG_ID));
		model.addAttribute(GROUP_BY_DROP_DOWN, jnjGTOrderTemplateFacade
				.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID));
		model.addAttribute(TOTAL_TEMPLATES, form.getTotalTemplates());
		model.addAttribute(SHOW_IN_GROUPS, form.getShowinGroups());
	}


	@PostMapping("/templateDetail/editTemplate/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN)
	public @ResponseBody
	String editTemplateDetail(final JnjGTTemplateDetailsForm templateEditForm)
	{
		final JnjGTTemplateDetailsData templateDetailsData = createTemplateEditData(templateEditForm);
		if (jnjGTOrderTemplateFacade.updateTemplate(templateDetailsData))
		{
			return "true";
		}
		return null;
	}

	@GetMapping("/templateDetail/deleteTemplate/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN)
	public String getDeleteTemplateConfirmation(final Model model, @PathVariable("templateCode") final String templateCode)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getDeleteTemplateConfirmation()";
		logMethodStartOrEnd(TEMPLATE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		populatePageDataForTemplateDetail(model);
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		logMethodStartOrEnd(TEMPLATE, METHOD_NAME, Logging.END_OF_METHOD);
		/** Returning view **/
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.DeleteTemplatePopup);


	}

	@PostMapping("/templateDetail/deleteTemplate/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN)
	public String deleteTemplateDetail(@PathVariable("templateCode") final String templateCode,
			final RedirectAttributes redirectModel)
	{
		jnjGTOrderTemplateFacade.deleteTemplate(templateCode);
		redirectModel.addFlashAttribute("deleteSuccessMsg", "Your selected order template was successfully deleted");
		return REDIRECT_PREFIX + "/templates";
	}

	protected JnjGTTemplateDetailsData createTemplateEditData(final JnjGTTemplateDetailsForm templateDetailsForm)
	{
		final JnjGTTemplateDetailsData detailsData = new JnjGTTemplateDetailsData();
		detailsData.setShareWithAccountUsers(templateDetailsForm.isShareWithAccountUsers());
		detailsData.setTemplateName(templateDetailsForm.getTemplateName());
		detailsData.setTemplateNumber(templateDetailsForm.getTemplateNumber());
		return detailsData;
	}

	@RequestMapping(value = "/templateDetail/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN, method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String getTemplateDetailPage(@PathVariable("templateCode") final String templateCode,
			@RequestParam(value = PAGE_COUNT, defaultValue = "1") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", defaultValue = "10") String showinGroup, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		final String methodName = "getTemplateDetailPage()";
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.BEGIN_OF_METHOD);
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		  showinGroup = jnjGTOrderTemplateFacade
					.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID).get(0)
					.toString();
		 int showInGroupsOf = Integer.parseInt(showinGroup);
		 final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
			//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
		model.addAttribute(SHOW_IN_GROUPS, showInGroupsOf);
		model.addAttribute(PAGE_COUNT, Integer.valueOf(page));
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_DETAIL_LABEL);
		createBreadCrumbsForTemplateDetail(null, model, templateLabel);

		final List<JnjTemplateEntryData> orderTemplateDetails = jnjGTOrderTemplateFacade
				.getSelectedOrderTemplateDetail(populatePageableData(templateCode, showMode, showinGroup, page));

		/** orderTemplateDetails can be null only if user lacks access to requested template details **/
		if (null != orderTemplateDetails)
		{
			model.addAttribute("orderTemplateDetail", orderTemplateDetails);
		}
		else
		{
			/** Redirect to templates root page if the user lacks access - determined by null value in orderTemplateDetails **/
			return REDIRECT_PREFIX + "/templates";
		}

		final JnjGTTemplateDetailsForm templateEditForm = setupDetailsForm(jnjGTOrderTemplateFacade
				.getTemplateForCode(templateCode));
		model.addAttribute("templateEditForm", templateEditForm);
		populatePageDataForTemplateDetail(model);
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.END_OF_METHOD);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplateDetailPage);
	}

	/**
	 * Used to populate the data used to search for the template details
	 * 
	 * @param templateCode
	 * @param showMode
	 * @param showinGroup
	 */
	protected JnjGTPageableData populatePageableData(final String templateCode, final ShowMode showMode, final String showinGroup,
			final int page)
	{
		final JnjGTPageableData jnjGTPageableData = new JnjGTPageableData();
		jnjGTPageableData.setSearchText(templateCode);
		jnjGTPageableData.setPageSize(Integer.parseInt(showinGroup));
		jnjGTPageableData.setStatus(showMode.toString());
		jnjGTPageableData.setCurrentPage(page);
		return jnjGTPageableData;
	}

	/**
	 * This method is used to populate the order template detail form with values as present in the database
	 * 
	 * @param orderTemplateData
	 * @return JnjGTTemplateDetailsForm
	 */
	protected JnjGTTemplateDetailsForm setupDetailsForm(final JnjGTOrderTemplateData orderTemplateData)
	{
		final JnjGTTemplateDetailsForm jnjGTTemplateDetailsForm = new JnjGTTemplateDetailsForm();
		jnjGTTemplateDetailsForm.setTemplateName(orderTemplateData.getTemplateName());
		jnjGTTemplateDetailsForm.setShareWithAccountUsers(null != orderTemplateData.getShareStatus()
				&& orderTemplateData.getShareStatus().toLowerCase().indexOf(SHARED) != -1 ? true : false);
		jnjGTTemplateDetailsForm.setAuthor(orderTemplateData.getAuthorId());
		jnjGTTemplateDetailsForm.setTotalEntries(orderTemplateData.getLines().toString());
		return jnjGTTemplateDetailsForm;
	}

	@PostMapping("/templateDetail/download/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN)
	public String downloadTemplateDetailData(@PathVariable("templateCode") final String templateCode,
			@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "All") final ShowMode showMode,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "showinGroups", defaultValue = "10") final String showinGroup, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		final String methodName = "downloadTemplateDetailData()";
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.BEGIN_OF_METHOD);
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		model.addAttribute(SHOW_IN_GROUPS, showinGroup);
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
		createBreadCrumbsForTemplateDetail(null, model, templateLabel);
		model.addAttribute("orderTemplateDetail", jnjGTOrderTemplateFacade.getSelectedOrderTemplateDetail(populatePageableData(
				templateCode, showMode, showinGroup, page)));

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
		
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.END_OF_METHOD);
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? TEMPLATE_DETAILS_PDF : TEMPLATE_DETAILS_EXCEL;
	}

	@RequestMapping("/createTemplate")
	public String createTemplate(final Model model)throws CMSItemNotFoundException{
	storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_CMS_PAGE));
	final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_CREATE_LABEL);
	createBreadCrumbsForTemplateDetail(null, model, templateLabel);
	final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
	//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
	if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
	{
		//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
		model.addAttribute("showChangeAccountLink", Boolean.TRUE);
	}
	    return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplateCreate);    
	}
	
	
	@RequestMapping("/addcreateTemplate")
	public String addcreateTemplate(@RequestParam("tempProdQtys") final String tempProdQtys,
	@RequestParam(value = "tempName") final String tempName,@RequestParam(value = "shareStatus") final Boolean shareStatus, final RedirectAttributes redirectModel)throws CMSItemNotFoundException{
	
	boolean status = jnjGTOrderTemplateFacade.createNewTemplate(tempProdQtys,tempName,shareStatus);
	if(status)
	{
		redirectModel.addFlashAttribute("successMsg", "Template created successfully");
	return REDIRECT_PREFIX + "/templates";   
	}
	else
	{
		redirectModel.addFlashAttribute("errorMsg", "Template couldn't be created");
		return REDIRECT_PREFIX + "/createTemplate";  
	}
	}
	
	@RequestMapping("/editCreatedTemplate")
	public String editCreatedTemplate(@RequestParam("tempProdQtys") final String tempProdQtys,
	@RequestParam(value = "tempName") final String tempName,@RequestParam(value = "shareStatus") final Boolean shareStatus,@RequestParam(value = "tempCode") final String tempCode, final RedirectAttributes redirectModel)throws CMSItemNotFoundException{
		
		boolean status = jnjGTOrderTemplateFacade.editExistingTemplate(tempProdQtys,tempName,shareStatus,tempCode);
		if(status)
		{
			redirectModel.addFlashAttribute("successMsg", "Template edited successfully");
	return REDIRECT_PREFIX + "/templates";   
		}
		else
		{
			redirectModel.addFlashAttribute("errorMsg", "Template couldn't be edited");
			return REDIRECT_PREFIX + "/editTemplate/"+tempCode;  
		} 
	}
	
	
	
	@RequestMapping(value = "/editTemplate/" + TEMPLATE_DETAIL_CODE_PATH_VARIABLE_PATTERN, method =
		{ RequestMethod.POST, RequestMethod.GET })
		public String editTemplate(@PathVariable("templateCode") final String templateCode,
				@RequestParam(value = PAGE_COUNT, defaultValue = "1") final int page,
				@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
				@RequestParam(value = "showinGroups", defaultValue = "10") String showinGroup, final HttpServletRequest request,
				final Model model) throws CMSItemNotFoundException
		{
			final String methodName = "getTemplateDetailPage()";
			logMethodStartOrEnd(TEMPLATE, methodName, Logging.BEGIN_OF_METHOD);
			model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
			  showinGroup = jnjGTOrderTemplateFacade
						.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID).get(0)
						.toString();
			 int showInGroupsOf = Integer.parseInt(showinGroup);
			 final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
				//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
				if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
				{
					//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
					model.addAttribute("showChangeAccountLink", Boolean.TRUE);
				}
			model.addAttribute(SHOW_IN_GROUPS, showInGroupsOf);
			model.addAttribute(PAGE_COUNT, Integer.valueOf(page));
			final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_EDIT_LABEL);
			createBreadCrumbsForTemplateDetail(null, model, templateLabel);

			final List<JnjTemplateEntryData> orderTemplateDetails = jnjGTOrderTemplateFacade
					.getSelectedOrderTemplateDetail(populatePageableData(templateCode, showMode, showinGroup, page));

			/** orderTemplateDetails can be null only if user lacks access to requested template details **/
			if (null != orderTemplateDetails)
			{
				model.addAttribute("orderTemplateDetail", orderTemplateDetails);
			}
			else
			{
				/** Redirect to templates root page if the user lacks access - determined by null value in orderTemplateDetails **/
				return REDIRECT_PREFIX + "/templates";
			}

			final JnjGTTemplateDetailsForm templateEditForm = setupDetailsForm(jnjGTOrderTemplateFacade
					.getTemplateForCode(templateCode));
			model.addAttribute("templateEditForm", templateEditForm);
			populatePageDataForTemplateDetail(model);
			logMethodStartOrEnd(TEMPLATE, methodName, Logging.END_OF_METHOD);
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplateEdit);
		}
	
	
	
	
	
	
	
	
	
	
	
	
	//page population with required fields for template details
	protected void populatePageDataForTemplateDetail(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(GROUP_BY_DROP_DOWN, jnjGTOrderTemplateFacade
				.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DETAIL_DROP_DOWN_ID));
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
	}

	protected void createBreadCrumbsForTemplateDetail(final String formURI, final Model model, final String formSpecificKey)
	{
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		final String METHOD_NAME = "createBreadCrumbsForTemplate()";
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<Breadcrumb> breadcrumbs = createBreadCrumbsForTemplate("/templates", model, templateLabel);
		breadcrumbs.add(new Breadcrumb(formURI, formSpecificKey, null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		logMethodStartOrEnd(Logging.CREATE_BREADCRUMB, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	public void setAscDescCounter(final Model model, final JnjGTOrderTemplateForm form)
	{
		System.out.println("inside set asc desc counter");
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_ASC))
		{
			System.out.println("ascDescCounter_hcolumn11"+Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_DESC);
			model.addAttribute("ascDescCounter_hcolumn1", Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_DESC))
		{
			System.out.println("ascDescCounter_hcolumn12"+Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_ASC);
			model.addAttribute("ascDescCounter_hcolumn1", Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_ASC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_ASC))
		{
			System.out.println("ascDescCounter_hcolumn21"+Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_DESC);
			model.addAttribute("ascDescCounter_hcolumn2", Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_DESC))
		{
			System.out.println("ascDescCounter_hcolumn22"+Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_ASC);
			model.addAttribute("ascDescCounter_hcolumn2", Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_ASC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_ASC))
		{
			System.out.println("ascDescCounter_hcolumn31"+Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_DESC);
			model.addAttribute("ascDescCounter_hcolumn3", Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_DESC))
		{
			System.out.println("ascDescCounter_hcolumn32"+Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_ASC);
			model.addAttribute("ascDescCounter_hcolumn3", Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_ASC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_ASC))
		{
			System.out.println("ascDescCounter_hcolumn41"+Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_DESC);
			model.addAttribute("ascDescCounter_hcolumn4", Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_DESC))
		{
			System.out.println("ascDescCounter_hcolumn42"+Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_ASC);
			model.addAttribute("ascDescCounter_hcolumn4", Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_ASC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_ASC))
		{
			System.out.println("ascDescCounter_hcolumn51"+Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_DESC);
			model.addAttribute("ascDescCounter_hcolumn5", Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_DESC))
		{
			System.out.println("ascDescCounter_hcolumn52"+Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_ASC);
			model.addAttribute("ascDescCounter_hcolumn5", Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_ASC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_ASC))
		{
			System.out.println("ascDescCounter_hcolumn61"+Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_DESC);
			model.addAttribute("ascDescCounter_hcolumn6", Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_DESC);
		}
		if (form.getSortby() != null && form.getSortby().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_DESC))
		{
			System.out.println("ascDescCounter_hcolumn62"+Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_ASC);
			model.addAttribute("ascDescCounter_hcolumn6", Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_ASC);
		}
	}


	@PostMapping("/productValidate")
 	@ResponseBody()
	public String productValidate(final Model model, @RequestParam("productCode") final String productCode,@RequestParam("qty") final String qtyString,
			final HttpSession session){
	    final JnjGTTemplateDetailsData jnjGTTemplateDetailsForm = new JnjGTTemplateDetailsData();
	    jnjGTTemplateDetailsForm.setProductCode(productCode);
	    jnjGTTemplateDetailsForm.setQuantity(qtyString);
	    
	    
	    boolean minQtyFlag=false;
		if (StringUtils.isEmpty(qtyString) || qtyString.equals("0"))
		{
			//Set to 0 for showing minimum qty message on FE
			minQtyFlag=true;
			jnjGTTemplateDetailsForm.setQuantity("1");
		}
	    
		String status = jnjGTOrderTemplateFacade.updateProductAndQuantityForTemplate(jnjGTTemplateDetailsForm);
		return status;
       
 } 
	
	
	
	public String getView(final String view){
        return Jnjb2bglobalordertemplateControllerConstants.ADDON_PREFIX + view;
 }
}
