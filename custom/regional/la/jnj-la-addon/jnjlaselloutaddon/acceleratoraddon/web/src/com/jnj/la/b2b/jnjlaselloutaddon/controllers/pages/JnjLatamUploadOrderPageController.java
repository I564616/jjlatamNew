package com.jnj.la.b2b.jnjlaselloutaddon.controllers.pages;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jnj.b2b.jnjselloutaddon.form.JnjSellOutReportsForm;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.account.JnjSellOutReportsFacade;
import com.jnj.facades.data.JnjLatamUploadOrderData;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.b2b.jnjlaselloutaddon.controllers.JnjlaselloutaddonControllerConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


@Controller
@Scope("tenant")
@RequestMapping(value = "/my-account/uploadorders")
public class JnjLatamUploadOrderPageController extends AbstractSearchPageController {

	private String sortflag = Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_DESC;
	private String filterFlag = Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_CUST;
	public static final String REDIRECT_PREFIX = "redirect:";
	/** The Constant ORDER_HISTORY. */
	private static final String UPLOAD_ORDERS = "/my-account/uploadorders";
	String pageSize = "0";
	int updatedPageSize = 0;
	String uploadStatus = StringUtils.EMPTY;
	boolean sizeError = false;

	private enum DOWNLOAD_TYPE {
		PDF, EXCEL;
	}

	public static final String RESULT_PDF = "jnJUploadOrderPdfView";

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	protected JnjSellOutReportsFacade jnjSellOutReportsFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Autowired
	protected JnjLatamOrderFacade jnjLatamOrderFacade;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
	MediaService mediaService;

	@Autowired
	protected CatalogVersionService catalogVersionService;

	@Autowired
	protected JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	/**
	 *
	 * This method fetches the data for Sell Out Reports to be dislayed on the
	 * front end.
	 *
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping
	public String getUploadOrderData(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) String sortCode,
			@RequestParam(value = "filterBy", required = false) String filterCode,
			@RequestParam(value = "isNewRequest", defaultValue = "true") final String isNewRequest,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "currentPageSize", defaultValue = "0") int currentPageSize, final Model model)
			throws CMSItemNotFoundException {

		final String methodName = "getUploadOrderData()";
		pageSize = "0";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		if (!pageSize.equalsIgnoreCase("0")) {
			updatedPageSize = Integer.parseInt(pageSize);
		}
		currentPageSize = 1000;
		if (updatedPageSize != 0) {
			currentPageSize = updatedPageSize;
		}
		updatedPageSize = 0;
		SearchPageData<JnjLatamUploadOrderData> searchPageData = null;
		if (isNewRequest.equalsIgnoreCase("false")) {
			if (sessionService.getAttribute("currentPageSize") != null
					&& !sessionService.getAttribute("currentPageSize").toString().isEmpty()) {
				currentPageSize = Integer.parseInt(sessionService.getAttribute("currentPageSize").toString());
			}
			if (sessionService.getAttribute("sortCode") != null
					&& !sessionService.getAttribute("sortCode").toString().isEmpty()) {
				sortCode = sessionService.getAttribute("sortCode").toString();
				sortflag = sortCode;
			}
			if (StringUtils.isNotBlank((String) sessionService.getAttribute("filterCode"))) {
				filterCode = sessionService.getAttribute("filterCode").toString();
				filterFlag = filterCode;
			}
			if (sessionService.getAttribute("page") != null
					&& !sessionService.getAttribute("page").toString().isEmpty()) {
				page = Integer.parseInt(sessionService.getAttribute("page").toString());
			}
		} else {
			sessionService.removeAttribute("currentPageSize");
			sessionService.removeAttribute("sortCode");
			sessionService.removeAttribute("filterCode");
			sessionService.removeAttribute("page");
		}
		final PageableData pageableData = createPageableData(page, currentPageSize, sortCode, showMode);

		try {
			// Calling facade layer
			searchPageData = jnjLatamOrderFacade.getUploadOrder(sortflag, filterFlag, pageableData);
		}

		catch (final Exception e) {

			JnjGTCoreUtil.logErrorMessage("UPLOADED FILES", methodName, "ERROR while calling facade class : ", e,
					JnjLatamUploadOrderPageController.class);
		}

		// Setting attributes
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.DATA_LIST, searchPageData);
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.SORT_FLAG, sortflag);
		model.addAttribute(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_FLAG, filterFlag);
		model.addAttribute("defaultPageSize", Integer.valueOf(10));
		model.addAttribute("uploadStatus", uploadStatus);
		model.addAttribute("sizeError", Boolean.valueOf(sizeError));
		// Resetting all flags as default after setting in model
		sortflag = Jnjb2bCoreConstants.SellOutReports.SORT_ORDER_DESC;
		filterFlag = Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_CUST;
		uploadStatus = "";
		sizeError = false;
		if (sessionService.getAttribute("scrollPos") != null
				&& !StringUtils.isEmpty(sessionService.getAttribute("scrollPos").toString())) {
			model.addAttribute("scrollPos", sessionService.getAttribute("scrollPos").toString());
			sessionService.removeAttribute("scrollPos");
		}
		// UX changes for pegination start
		if (form != null) {
			form.setPageSize(String.valueOf(pageableData.getPageSize()));
		}
		model.addAttribute("sellOutReportsForm", form);
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.header"), null));
		model.addAttribute(Jnjlab2bcoreConstants.Forms.BREADCRUMBS, breadcrumbs);
		// UX changes for pegination end
		return JnjlaselloutaddonControllerConstants.ADDON_PREFIX
				+ JnjlaselloutaddonControllerConstants.Views.Pages.Account.UploadOrders;
	}

	@PostMapping("/loadMore")
	public String loadMore(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			final Model model) throws CMSItemNotFoundException {

		final String methodName = "loadMore()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the sort field as per users choice
		pageSize = form.getPageSize();
		sessionService.setAttribute("currentPageSize", pageSize);

		JnjGTCoreUtil.logDebugMessage("UPLOADED FILES", methodName, "Redirecting to get method",
				JnjLatamUploadOrderPageController.class);

		sessionService.setAttribute("scrollPos", scrollPos);
		return REDIRECT_PREFIX + Jnjlab2bcoreConstants.UploadOrder.REDIRECT_TO_MAIN + "?isNewRequest=false";
	}

	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException {
		return getContentPageForLabelOrId(Jnjlab2bcoreConstants.UploadOrder.UPLOAD_ORDERS);
	}

	/**
	 * This method is used for returning the view for the pop-up that will open
	 * when user attempts to upload a file on sell out reports page.
	 *
	 *
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping("/showUploadPopUp")
	public String showUploadPopUp(final Model model) throws CMSItemNotFoundException {
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		return JnjlaselloutaddonControllerConstants.ADDON_PREFIX
				+ JnjlaselloutaddonControllerConstants.Views.Pages.Account.UploadOrderPopUp;
	}

	/**
	 * This method is called when user clicks on the Sort By selection box and
	 * chooses the sort order for the Sell Out reports data.
	 *
	 *
	 * @param form
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("/sort")
	public String sortSellOutReportsData(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form,
			final Model model) throws CMSItemNotFoundException {

		final String methodName = "sortSellOutReportsData()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the sort field as per users choice
		sortflag = form.getSortType();
		sessionService.setAttribute("sortCode", sortflag);

		JnjGTCoreUtil.logDebugMessage("UPLOADED FILES", methodName, "Redirecting to get method",
				JnjLatamUploadOrderPageController.class);

		return REDIRECT_PREFIX + Jnjlab2bcoreConstants.UploadOrder.REDIRECT_TO_MAIN + "?isNewRequest=false";
	}

	/**
	 * This method is called when user clicks on the Filter By selection box and
	 * chooses the customer/user filter for the Sell Out reports data.
	 *
	 *
	 * @param form
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @author RBatich
	 */
	@PostMapping("/filterBy")
	public String filterSellOutReportsData(@ModelAttribute("sortOutReportsForm") final JnjSellOutReportsForm form,
			final Model model) throws CMSItemNotFoundException {

		final String methodName = "filterSellOutReportsData()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the filter field as per users choice
		filterFlag = form.getFilterType();
		sessionService.setAttribute("filterCode", filterFlag);

		JnjGTCoreUtil.logDebugMessage("UPLOADED FILES", methodName, "Redirecting to get method",
				JnjLatamUploadOrderPageController.class);

		return REDIRECT_PREFIX + Jnjlab2bcoreConstants.UploadOrder.REDIRECT_TO_MAIN + "?isNewRequest=false";
	}

	@PostMapping("/showMore")
	public String showMore(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form, final Model model)
			throws CMSItemNotFoundException {

		final String methodName = "showMore()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.header"), null));
		model.addAttribute(Jnjlab2bcoreConstants.Forms.BREADCRUMBS, breadcrumbs);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the sort field as per users choice
		pageSize = form.getPageSize();
		sessionService.setAttribute("currentPageSize", pageSize);

		JnjGTCoreUtil.logDebugMessage("UPLOADED FILES", methodName, "Redirecting to get method",
				JnjLatamUploadOrderPageController.class);

		return REDIRECT_PREFIX + Jnjlab2bcoreConstants.UploadOrder.REDIRECT_TO_MAIN + "?isNewRequest=false";
	}

	@PostMapping("/downloadData")
	public String downloadUploadOrderData(@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) String sortCode,
			@RequestParam(value = "filterBy", required = false) String filterCode,
			@RequestParam(value = "isNewRequest", defaultValue = "false") final String isNewRequest,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "currentPageSize", defaultValue = "10") int currentPageSize,
			@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form, final Model model)
			throws CMSItemNotFoundException {

		final String methodName = "downloadUploadOrderData()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		final List<Breadcrumb> breadcrumbs = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.header"), null));
		model.addAttribute(Jnjlab2bcoreConstants.Forms.BREADCRUMBS, breadcrumbs);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		if (!pageSize.equalsIgnoreCase("0")) {
			updatedPageSize = Integer.parseInt(pageSize);
		}
		currentPageSize = 10;
		if (updatedPageSize != 0) {
			currentPageSize = updatedPageSize;
		}
		updatedPageSize = 0;
		pageSize = "0";

		SearchPageData<JnjLatamUploadOrderData> searchPageData = null;
		if (isNewRequest.equalsIgnoreCase("false")) {
			if (sessionService.getAttribute("currentPageSize") != null
					&& !sessionService.getAttribute("currentPageSize").toString().isEmpty()) {
				currentPageSize = Integer.parseInt(sessionService.getAttribute("currentPageSize").toString());
			}
			if (sessionService.getAttribute("sortCode") != null
					&& !sessionService.getAttribute("sortCode").toString().isEmpty()) {
				sortCode = sessionService.getAttribute("sortCode").toString();
				sortflag = sortCode;
			}
			if (StringUtils.isNotBlank((String) sessionService.getAttribute("filterCode"))) {
				filterCode = sessionService.getAttribute("filterCode").toString();
				filterFlag = filterCode;
			}
			if (sessionService.getAttribute("page") != null
					&& !sessionService.getAttribute("page").toString().isEmpty()) {
				page = Integer.parseInt(sessionService.getAttribute("page").toString());
			}
		} else {
			sessionService.removeAttribute("currentPageSize");
			sessionService.removeAttribute("sortCode");
			sessionService.removeAttribute("filterCode");
			sessionService.removeAttribute("page");
		}
		final PageableData pageableData = createPageableData(page, currentPageSize, sortCode, showMode);

		try {
			// Calling facade layer
			searchPageData = jnjLatamOrderFacade.getUploadOrder(sortflag, filterFlag, pageableData);
		}

		catch (final Exception e) {

			JnjGTCoreUtil.logErrorMessage("UPLOADED FILES", methodName, "ERROR while calling facade class : ", e,
					JnjLatamUploadOrderPageController.class);
		}

		// Setting attributes
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.DATA_LIST, searchPageData);
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.SORT_FLAG, sortflag);
		model.addAttribute(Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_FLAG, filterFlag);
		model.addAttribute("defaultPageSize", Integer.valueOf(10));
		model.addAttribute("uploadStatus", uploadStatus);
		model.addAttribute("sizeError", Boolean.valueOf(sizeError));
		// Resetting all flags as default after setting in model
		sortflag = Jnjb2bCoreConstants.SellOutReports.SORT_ORDER_DESC;
		filterFlag = Jnjlab2bcoreConstants.UploadEdiFiles.FILTER_BY_CUST;
		uploadStatus = "";
		sizeError = false;
		if (sessionService.getAttribute("scrollPos") != null
				&& !StringUtils.isEmpty(sessionService.getAttribute("scrollPos").toString())) {
			model.addAttribute("scrollPos", sessionService.getAttribute("scrollPos").toString());
			sessionService.removeAttribute("scrollPos");
		}

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

		return DOWNLOAD_TYPE.PDF.toString().equals(downloadType) ? RESULT_PDF
				: REDIRECT_PREFIX + Jnjlab2bcoreConstants.UploadOrder.REDIRECT_TO_MAIN + "?isNewRequest=false";
	}

	/**
	 *
	 * This method fetches the data for uploadedOrderId to be displayed on the
	 * front end.
	 *
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	// Added for EDI changes
	@GetMapping("/uplodedOrderDetails")
	public String getUploadOrderDataDetails(@RequestParam(value = "fileNameId") final String fileNameId,
			final Model model) throws CMSItemNotFoundException {

		final String methodName = "getUploadOrderDataDetails()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		JnjLatamUploadOrderData searchPageData = null;
		final List<Breadcrumb> listOfBreadCrumb = resourceBreadcrumbBuilder.getBreadcrumbs(null);
		listOfBreadCrumb.add(new Breadcrumb(UPLOAD_ORDERS,
				jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.header"), null));
		final Breadcrumb breadCrumb = new Breadcrumb(null,
				jnjCommonFacadeUtil.getMessageFromImpex("text.uploadOrder.orderDetails"), null);
		listOfBreadCrumb.add(breadCrumb);
		try {
			// Calling facade layer
			searchPageData = jnjLatamOrderFacade.getUploadOrderDetails(fileNameId);
		}

		catch (final Exception e) {

			JnjGTCoreUtil.logErrorMessage("UPLOADED FILES", methodName, "ERROR while calling facade class : ", e,
					JnjLatamUploadOrderPageController.class);
		}

		// Setting attributes
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.DATA_LIST, searchPageData);
		model.addAttribute("breadcrumbs", listOfBreadCrumb);
		return JnjlaselloutaddonControllerConstants.ADDON_PREFIX
				+ JnjlaselloutaddonControllerConstants.Views.Pages.Account.UploadOrderDetails;
	}

}
