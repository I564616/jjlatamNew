/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.controllers.pages;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.controllers.pages.JnjGTOrderTemplatePageController;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTTemplateDetailsForm;
import com.jnj.b2b.jnjlaordertemplate.controllers.JnjlaordertemplateaddonControllerConstants;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTOrderTemplateForm;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.facades.template.JnjGTOrderTemplateFacade;
import com.jnj.facades.template.impl.JnjLatamOrderTemplateFacadeImpl;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.services.CMSSiteService;

/**
 * @author sbaner48
 *
 */
public class JnjLatamOrderTemplatePageController extends JnjGTOrderTemplatePageController {

	@Autowired
	private JnjLatamOrderTemplateFacadeImpl jnjLatamOrderTemplateFacadeImpl;

	@Resource(name = "httpSessionRequestCache")
	protected HttpSessionRequestCache httpSessionRequestCache;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Resource(name = "jnjGTOrderTemplateFacade")
	protected JnjGTOrderTemplateFacade jnjGTOrderTemplateFacade;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Autowired
	protected UserFacade userFacade;

	@Autowired
	private CMSSiteService cMSSiteService;
	@Autowired
	private MediaService mediaService;

	@Autowired
	protected CatalogVersionService catalogVersionService;

	@Autowired
	JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	/**
	 * @return the catalogVersionService
	 */
	@Override
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	@Override
	public HttpSessionRequestCache getHttpSessionRequestCache() {
		return httpSessionRequestCache;
	}

	@Override
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	@Override
	public SessionService getSessionService() {
		return sessionService;
	}

	@Override
	public JnjGTOrderTemplateFacade getJnjGTOrderTemplateFacade() {
		return jnjGTOrderTemplateFacade;
	}

	@Override
	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	@Override
	public UserFacade getUserFacade() {
		return userFacade;
	}

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
	protected static final String SEARCH_CONFIG_ID_LATAM = "searchByTemplateLatam";
	protected static final String TOTAL_TEMPLATES = "totalTemplates";
	protected static final String SHOW_IN_GROUPS = "showinGroups";
	protected static final String ORDER_TEMPLATE_ID = "templateId";

	/** The Constant RESULT_PDF. */
	public static final String RESULT_PDF = "jnjLatamOrderTemplatePdfView";
	/** The Constant RESULT_EXCEL. */
	public static final String RESULT_EXCEL = "jnjLatamOrderTemplateExcelView";

	/** The Constant TEMPLATE_DETAILS_PDF. */
	public static final String TEMPLATE_DETAILS_PDF = "jnjLatamOrderTemplateDetailPdfView"; // jnjNAOrderTemplateDetailPdfView
	/** The Constant TEMPLATE_DETAILS_EXCEL. */
	public static final String TEMPLATE_DETAILS_EXCEL = "jnjLatamOrderTemplateDetailExcelView"; // jnjNAOrderTemplateDetailExcelView

	@PostMapping("/laProductValidate")
	@ResponseBody()
	public String laProductValidate(final Model model, @RequestParam("productCode") final String productCode,
			@RequestParam("qty") final String qtyString, final HttpSession session) {

		final String methodName = "laProductValidate()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SHOW_TEMPLATE, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamOrderTemplatePageController.class);

		final JnjGTTemplateDetailsData jnjGTTemplateDetailsForm = new JnjGTTemplateDetailsData();
		jnjGTTemplateDetailsForm.setProductCode(productCode);
		jnjGTTemplateDetailsForm.setQuantity(qtyString);

		if (StringUtils.isEmpty(qtyString) || "0".equals(qtyString)) {
			jnjGTTemplateDetailsForm.setQuantity("1");
		}
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SHOW_TEMPLATE, methodName, Logging.END_OF_METHOD,
				JnjLatamOrderTemplatePageController.class);
		return jnjLatamOrderTemplateFacadeImpl.updateProductAndQuantityForTemplate(jnjGTTemplateDetailsForm);

	}

	@Override
	public String getView(final String view) {
		final String methodName = "getView()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SHOW_TEMPLATE, methodName,
				JnjlaordertemplateaddonControllerConstants.ADDON_PREFIX + view,
				JnjLatamOrderTemplatePageController.class);
		return JnjlaordertemplateaddonControllerConstants.ADDON_PREFIX + view;
	}

	@Override
	public String editCreatedTemplate(@RequestParam("tempProdQtys") final String tempProdQtys,
			@RequestParam(value = "tempName") final String tempName,
			@RequestParam(value = "shareStatus") final Boolean shareStatus,
			@RequestParam(value = "tempCode") final String tempCode, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException {
		final String methodName = "editCreatedTemplate()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SHOW_TEMPLATE, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamOrderTemplatePageController.class);
		final boolean status = jnjLatamOrderTemplateFacadeImpl.editExistingTemplate(tempProdQtys, tempName, shareStatus,
				tempCode);
		if (status) {
			redirectModel.addFlashAttribute("successMsg", "text.template.edit.successmessage");
			return REDIRECT_PREFIX + "/templates";
		} else {
			redirectModel.addFlashAttribute("errorMsg", "Template couldn't be edited");
			return REDIRECT_PREFIX + "/editTemplate/" + tempCode;
		}
	}

	@Override
	public String addcreateTemplate(@RequestParam("tempProdQtys") final String tempProdQtys,
			@RequestParam(value = "tempName") final String tempName,
			@RequestParam(value = "shareStatus") final Boolean shareStatus, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException {

		final boolean status = jnjLatamOrderTemplateFacadeImpl.createNewTemplate(tempProdQtys, tempName, shareStatus);
		if (status) {
			redirectModel.addFlashAttribute("successMsg", "text.template.create.successmessage");
			return REDIRECT_PREFIX + "/templates";
		} else {
			redirectModel.addFlashAttribute("errorMsg", "Template couldn't be created");
			return REDIRECT_PREFIX + "/createTemplate";
		}
	}

	@Override
	public String downloadTemplatePDFPagePost(@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", required = false) String showinGroup,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final JnjGTOrderTemplateForm form) throws CMSItemNotFoundException {
		int showInGroupsOf = 0;
		if (StringUtils.isEmpty(showinGroup)) {
			showinGroup = jnjGTOrderTemplateFacade
					.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID)
					.get(0).toString();
		} else {
			showInGroupsOf = Integer.parseInt(showinGroup);
		}
		form.setShowinGroups(showinGroup);
		final PageableData pageableData = createPageableData(page, showInGroupsOf, sortCode, showMode);
		final SearchPageData<JnjGTOrderTemplateData> searchPageData = jnjGTOrderTemplateFacade
				.getPagedTemplateForStatuses(pageableData, form);
		model.addAttribute("orderTemplate", searchPageData.getResults());
		if (null == searchPageData.getResults() || searchPageData.getResults().size() == 0) {
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
		model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));
		/* Excel image adding end here */

		final List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		if (catologLst != null && catologLst.size() > 0) {
			final MediaModel mediaModel1 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(),
							Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			final MediaModel mediaModel2 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(),
							Jnjb2bCoreConstants.ONLINE),
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
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF : RESULT_EXCEL;
	}

	@Override
	public String downloadTemplateDetailData(@PathVariable("templateCode") final String templateCode,
			@RequestParam(value = PAGE_COUNT, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "All") final ShowMode showMode,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "showinGroups", defaultValue = "10") final String showinGroup,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException {
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		model.addAttribute(SHOW_IN_GROUPS, showinGroup);
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_LABEL);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_TEMPLATE_DETAIL_CMS_PAGE));
		createBreadCrumbsForTemplateDetail(null, model, templateLabel);
		model.addAttribute("orderTemplateDetail", jnjLatamOrderTemplateFacadeImpl
				.getSelectedOrderTemplateDetail(populatePageableData(templateCode, showMode, showinGroup, page)));

		/* Excel image adding Started here */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
				.getActiveCatalogVersion();
		model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));
		/* Excel image adding end here */

		final JnjGTTemplateDetailsForm templateEditForm = setupDetailsForm(
				jnjGTOrderTemplateFacade.getTemplateForCode(templateCode));
		model.addAttribute("templateEditForm", templateEditForm);

		final List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		if (catologLst != null && catologLst.size() > 0) {
			final MediaModel mediaModel1 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(),
							Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			final MediaModel mediaModel2 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(),
							Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
			if (mediaModel1 != null) {
				model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
			}
			if (mediaModel2 != null) {
				model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
			}
		}
		/* Pdf image adding Started here */
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? TEMPLATE_DETAILS_PDF : TEMPLATE_DETAILS_EXCEL;
	}

	@Override
	protected void populatePageDataTemplate(final Model model, final JnjGTOrderTemplateForm form) {
		model.addAttribute(SEARCH_DROP_DOWN, jnjGTOrderTemplateFacade.getDropDownList(SEARCH_CONFIG_ID_LATAM));
		model.addAttribute(SORT_DROP_DOWN, jnjGTOrderTemplateFacade.getDropDownList(SORT_CONFIG_ID));
		model.addAttribute(GROUP_BY_DROP_DOWN, jnjGTOrderTemplateFacade
				.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID));
		model.addAttribute(TOTAL_TEMPLATES, form.getTotalTemplates());
		model.addAttribute(SHOW_IN_GROUPS, form.getShowinGroups());
	}

	@Override
	public String deleteTemplateDetail(@PathVariable("templateCode") final String templateCode,
			final RedirectAttributes redirectModel) {
		jnjGTOrderTemplateFacade.deleteTemplate(templateCode);
		redirectModel.addFlashAttribute("deleteSuccessMsg", "text.template.delete.successmessage");
		return REDIRECT_PREFIX + "/templates";
	}

	@Override
	public String getTemplateDetailPage(@PathVariable("templateCode") final String templateCode,
			@RequestParam(value = PAGE_COUNT, defaultValue = "1") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", defaultValue = "10") String showinGroup,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException {
		final String methodName = "getTemplateDetailPage()";
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.BEGIN_OF_METHOD);
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		showinGroup = jnjGTOrderTemplateFacade
				.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID)
				.get(0).toString();
		final int showInGroupsOf = Integer.parseInt(showinGroup);
		final Object showChangeAccountLink = sessionService
				.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
		// LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			// LOGGER.info("entered condition...........showChangeAccountLink
			// :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(SHOW_IN_GROUPS, showInGroupsOf);
		model.addAttribute(PAGE_COUNT, Integer.valueOf(page));
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_DETAIL_LABEL);
		createBreadCrumbsForTemplateDetail(null, model, templateLabel);

		final List<JnjTemplateEntryData> orderTemplateDetails = jnjLatamOrderTemplateFacadeImpl
				.getSelectedOrderTemplateDetail(populatePageableData(templateCode, showMode, showinGroup, page));

		/**
		 * orderTemplateDetails can be null only if user lacks access to
		 * requested template details
		 **/
		if (null != orderTemplateDetails) {
			model.addAttribute("orderTemplateDetail", orderTemplateDetails);
		} else {
			/**
			 * Redirect to templates root page if the user lacks access -
			 * determined by null value in orderTemplateDetails
			 **/
			return REDIRECT_PREFIX + "/templates";
		}

		final JnjGTTemplateDetailsForm templateEditForm = setupDetailsForm(
				jnjGTOrderTemplateFacade.getTemplateForCode(templateCode));
		model.addAttribute("templateEditForm", templateEditForm);
		populatePageDataForTemplateDetail(model);
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.END_OF_METHOD);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplateDetailPage);
	}

	@Override
	public String editTemplate(@PathVariable("templateCode") final String templateCode,
			@RequestParam(value = PAGE_COUNT, defaultValue = "1") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showinGroups", defaultValue = "10") String showinGroup,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException {
		final String methodName = "getTemplateDetailPage()";
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.BEGIN_OF_METHOD);
		model.addAttribute(ORDER_TEMPLATE_ID, templateCode);
		showinGroup = jnjGTOrderTemplateFacade
				.getDropDownListForGroupBy(Jnjb2bglobalordertemplateConstants.TemplateSearch.GROUP_BY_DROP_DOWN_ID)
				.get(0).toString();
		final int showInGroupsOf = Integer.parseInt(showinGroup);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		model.addAttribute(PAGE_COUNT, Integer.valueOf(page));
		final String templateLabel = jnjCommonFacadeUtil.getMessageFromImpex(TEMPLATE_EDIT_LABEL);
		createBreadCrumbsForTemplateDetail(null, model, templateLabel);

		final List<JnjTemplateEntryData> orderTemplateDetails = jnjLatamOrderTemplateFacadeImpl
				.getSelectedOrderTemplateDetail(populatePageableData(templateCode, showMode, showinGroup, page));

		/**
		 * orderTemplateDetails can be null only if user lacks access to
		 * requested template details
		 **/
		if (null != orderTemplateDetails) {
			model.addAttribute("orderTemplateDetail", orderTemplateDetails);
		} else {
			/**
			 * Redirect to templates root page if the user lacks access -
			 * determined by null value in orderTemplateDetails
			 **/
			return REDIRECT_PREFIX + "/templates";
		}

		final JnjGTTemplateDetailsForm templateEditForm = setupDetailsForm(
				jnjGTOrderTemplateFacade.getTemplateForCode(templateCode));
		model.addAttribute("templateEditForm", templateEditForm);
		populatePageDataForTemplateDetail(model);
		logMethodStartOrEnd(TEMPLATE, methodName, Logging.END_OF_METHOD);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Template.TemplateEdit);
	}
}
