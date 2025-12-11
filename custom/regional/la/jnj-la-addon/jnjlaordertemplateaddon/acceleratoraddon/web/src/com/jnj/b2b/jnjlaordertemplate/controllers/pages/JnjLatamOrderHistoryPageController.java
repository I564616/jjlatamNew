package com.jnj.b2b.jnjlaordertemplate.controllers.pages;

import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Order;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Order.Invoice;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.controllers.pages.JnjGTOrderHistoryPageController;
import com.jnj.b2b.jnjlaordertemplate.constants.JnjlaordertemplateaddonConstants;
import com.jnj.b2b.jnjlaordertemplate.controllers.JnjlaordertemplateaddonControllerConstants;
import com.jnj.b2b.jnjlaordertemplate.controllers.utils.JnjLatamInvoiceControllerUtil;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.enums.JnjGTPageType;
import com.jnj.core.enums.SelectedAccountType;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.invoice.JnjInvoiceFacade;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.facades.order.JnjLatamOrderFacade;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.model.JnjOrderChannelModel;
import com.jnj.la.core.model.JnjOrderTypeModel;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.facade.nfe.JnjNfeFacade;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jnj.facades.services.JnjLatamCommonService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.PDF_EXCEL_MAX_PAGE_SIZE;

@RequireHardLogIn
public class JnjLatamOrderHistoryPageController extends JnjGTOrderHistoryPageController {

    private static final String ADD_TO_CART_ERROR = "addToCartError";
    @Autowired
	private JnjLatamCartFacade jnjLatamCartFacade;

	@Autowired
	private JnjLAOrderService jnjOrderService;

	@Autowired
	private UserService userService;

	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Resource(name = "GTCartFacade")
	protected JnjGTCartFacade jnJGTCartFacade;

	@Autowired
	private JnjLatamOrderFacade jnjlatamCustomOrderFacade;

	@Autowired
	private JnjInvoiceFacade jnjInvoiceFacade;

	@Autowired
	private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	private final Class<JnjLatamOrderHistoryPageController> currentClass = JnjLatamOrderHistoryPageController.class;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjLatamInvoiceFacade jnjLatamInvoiceFacade;

	@Autowired
	private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	@Autowired
	protected CMSSiteService cmsSiteService;

	@Autowired
	private CartService cartService;

    @Autowired
    protected MediaService mediaService;

    @Autowired
    protected JnjNfeFacade jnjNfeFacade;

    @Autowired
    private JnjLatamInvoiceControllerUtil invoiceUtil;
    
    private JnjLatamCommonService jnjLatamCommonService;

	public static final String ORDER_DETAIL_PAGE_SIZE = "orderDetailPageSize";

	public static final String ORDER_DATA = "orderData";

	private static final String INVOICE_PDF_PROPERTY = "download.invoice.pdf.countries";

	private static final String INVOICE_XML_PROPERTY = "download.invoice.xml.countries";

	protected static final String ORDER_HISTORY_PDF_VIEW = "jnjLAOrderHistoryPdfView";

	protected static final String ORDER_HISTORY_EXCEL_VIEW = "jnjLAOrderHistoryExcelView";

	protected static final String ORDER_DETAIL_PDF = "jnjLatamOrderDetailPdfView";

	protected static final String ORDER_DETAIL_EXCEL = "jnjLatamOrderDetailExcelView";

	private static final String SHOW_ATP_FLAG_MAP = "showATPFlagMap";

	public static final String REDIRECT_PREFIX = "redirect:";

	private static final String INVOICE_DETAIL_PAGE_REL_PATH = "/order-history/order/invoiceDetail/";

	private static final String ASC = "asc";

	private static final Integer PAGE_SIZE_DEFAULT = 10;

	private static final Integer PAGE_NUMBER_DEFAULT = 0;

	//FIXME:MERGED super class does not have a default constructor any more.
	public JnjLatamOrderHistoryPageController() throws NoSuchAlgorithmException {
		super();
	}

	private enum DOWNLOAD_TYPE {
		PDF, EXCEL, NONE;
	}

	protected enum POP_UP_TYPE {
		SELECT_ACCOUNTS, UPDATE_PO, UPDATE_SURGEON, DISPUTE_ORDER, DISPUTE_ITEM, SURGEON_INFORMATION;
	}

	@Override
	public String refineSearchResult(@RequestParam("orderCode") final String orderCode, final JnjGTSearchSortForm form,
			final Model model) throws CMSItemNotFoundException {
		final String methodName = "Latam processRefineSearchResult()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.BEGIN_OF_METHOD,
				currentClass);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		boolean resultLimitExceeded = false;
		final String downloadType = form.getDownloadType();
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		int finalPageSize = form.isShowMore() ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
		if (DOWNLOAD_TYPE.PDF.toString().equals(downloadType) || DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)) {
			if (StringUtils.isNotEmpty(form.getTotalNumberOfResults())) {
				finalPageSize = Integer.parseInt(Config.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE));
				final int resultLimit = Integer.parseInt(Config.getParameter(
						Jnjb2bglobalordertemplateConstants.Order.ORDER_DETAIL_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD));
				if (resultLimit > 0) {
					if (finalPageSize >= resultLimit) {
						finalPageSize = resultLimit;
						resultLimitExceeded = true;
					}
				}
			}
		}
		sessionService.removeAttribute(ORDER_DETAIL_PAGE_SIZE);
		sessionService.setAttribute(ORDER_DETAIL_PAGE_SIZE, Integer.parseInt(Config.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE)));
		sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE, JnjGTPageType.ORDER_DETAIL);
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);

		model.addAttribute(ORDER_DATA, orderData);
		model.addAttribute("searchSortForm", form);

		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
				"downloadType : " + downloadType, currentClass);
		if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType)) {
			storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
			setOrderDetailBreadCrumb(model);
			final String returnPage = getView(
					Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailPage);
			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
					"returnPage : " + returnPage, currentClass);
			return returnPage;
		} else {
			/* Excel image adding Started here */
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
			final CatalogVersionModel currentCatalog = cmsSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();

			// Send site logo
			model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));
			/* Excel image adding end here */

			/* Pdf image adding Started here */
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
			
			final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
			if (Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA.equalsIgnoreCase(countryInfo) && (jnjLatamCartFacade.getShippingDetails() != null))
			{
				final Map<String, String> productShippingMap = jnjLatamCartFacade.getShippingDetails();
				model.addAttribute("productShippingMap", productShippingMap);
			}
			model.addAttribute("country", countryInfo);
			final Object flagMap= jnjLatamCommonFacadeUtil.buildShowATPFlagMap(model, "orderData");
			sessionService.setAttribute(SHOW_ATP_FLAG_MAP, flagMap);
	
			model.addAttribute("orderDataForPDFExcel", orderData);
			model.addAttribute("resultLimitExceeded", resultLimitExceeded);
			final String returnPage = DOWNLOAD_TYPE.PDF.toString().equals(downloadType) ? ORDER_DETAIL_PDF
					: ORDER_DETAIL_EXCEL;

			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
					"returnPage : " + returnPage, currentClass);
			JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, methodName, Logging.END_OF_METHOD,
					currentClass);
			return returnPage;
		}

	}

	@Override
	protected void populateDropDownOptions(final Model model, final String currentSite,
			final SelectedAccountType accountType) {
		final List<SelectOption> orderTypes = new ArrayList<>();
		populateOrderTypes(orderTypes);

		final List<SelectOption> orderStatus = new ArrayList<>();
		latamPopulateOrderStatus(orderStatus);

		final List<SelectOption> lineStatus = new ArrayList<>();
		latamPopulateLineStatus(lineStatus);

		/*** Populate Sort By options. ***/
		final List<String> sortOptions = new ArrayList<>();
		latamPopulateSortOptions(sortOptions);

		/*** Populate Search By options. ***/
		final List<SelectOption> searchOptions = new ArrayList<>();
		latamPopulateSearchOptions(searchOptions, model);

        final String poNumberVal = searchOptions.get(2).getName();

        model.addAttribute("poNumberVal", poNumberVal);

        /***
         * Populate Order Channel options based on the code, and corresponding
         * label from message inventory.
         ***/
        final List<SelectOption> channelOptions = new ArrayList<>();
        populateChannelOptions(channelOptions);

		model.addAttribute("orderTypes", orderTypes);
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("lineStatus", lineStatus);
		model.addAttribute("channels", channelOptions);
		model.addAttribute("sortOptions", sortOptions);
		model.addAttribute("searchOptions", searchOptions);
	}

	private void latamPopulateSortOptions(final List<String> sortOptions) {
		sortOptions.add(Order.SortOption.DEFAULT_SORT_CODE);
		sortOptions.add(Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST);
		sortOptions.add(Order.SortOption.ORDER_NUMBER_DECREASING);
		sortOptions.add(Order.SortOption.ORDER_NUMBER_INCREASING);
		sortOptions.add(Order.SortOption.ORDER_TOTAL_DECREASING);
		sortOptions.add(Order.SortOption.ORDER_TOTAL_INCREASING);
	}

	private List<SelectOption> latamPopulateOrderStatus(final List<SelectOption> orderStatus) {

		orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(),
				enumerationService.getEnumerationName(OrderStatus.CREATED)));
		orderStatus.add(new SelectOption(OrderStatus.UNDER_ANALYSIS.toString(),
				enumerationService.getEnumerationName(OrderStatus.UNDER_ANALYSIS)));
		orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.BEING_PROCESSED)));
		orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(),
				enumerationService.getEnumerationName(OrderStatus.IN_PICKING)));
		orderStatus.add(new SelectOption(OrderStatus.INVOICED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.INVOICED)));
		orderStatus.add(new SelectOption(OrderStatus.SHIPPED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.SHIPPED)));
		orderStatus.add(new SelectOption(OrderStatus.DELIVERED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.DELIVERED)));
		orderStatus.add(new SelectOption(OrderStatus.PENDING.getCode(),
				enumerationService.getEnumerationName(OrderStatus.PENDING)));
		orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.CANCELLED)));
		orderStatus.add(new SelectOption(OrderStatus.PARTIALLY_DELIVERED.getCode(),
				enumerationService.getEnumerationName(OrderStatus.PARTIALLY_DELIVERED)));

		return orderStatus;
	}

	private List<SelectOption> latamPopulateLineStatus(final List<SelectOption> lineStatus) {

		lineStatus.add(new SelectOption(OrderEntryStatus.CREATED.toString(),
				enumerationService.getEnumerationName(OrderEntryStatus.CREATED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.UNDER_ANALYSIS.toString(),
				enumerationService.getEnumerationName(OrderEntryStatus.UNDER_ANALYSIS)));
		lineStatus.add(new SelectOption(OrderEntryStatus.BEING_PROCESSED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.BEING_PROCESSED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.IN_PICKING.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.IN_PICKING)));
		lineStatus.add(new SelectOption(OrderEntryStatus.INVOICED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.INVOICED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.SHIPPED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.SHIPPED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.DELIVERED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.DELIVERED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.PENDING.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.PENDING)));
		lineStatus.add(new SelectOption(OrderEntryStatus.CANCELLED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.CANCELLED)));
		lineStatus.add(new SelectOption(OrderEntryStatus.PARTIALLY_DELIVERED.getCode(),
				enumerationService.getEnumerationName(OrderEntryStatus.PARTIALLY_DELIVERED)));

		return lineStatus;
	}

	@Override
	public String getOrderHistoryDetails(JnjGTOrderHistoryForm form, final Model model,
			final HttpServletRequest request, final boolean resultLimitExceeded) throws CMSItemNotFoundException {

		String language = "en";
		if (null != commonI18NService.getCurrentLanguage()) {
			language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
		}
		getSessionService().getCurrentSession().setAttribute("sessionLanguage", language);
        final String valToCompare = sessionService.getAttribute("valToCompare");
        if (StringUtils.isNotEmpty(valToCompare)) {
            model.addAttribute("valToCompare", valToCompare);
        }

		form = form.isResetSelection() ? new JnjGTOrderHistoryForm() : form;

		if (request.getMethod().equalsIgnoreCase("GET")) {
			final JnjGTOrderHistoryForm sessionForm = sessionService.getAttribute("orderHistoryForm");
			if (null != sessionForm) {
				form = sessionForm;
			}
		}

		final int finalPageSize = form.isShowMore() ? form.getPageSize() * form.getShowMoreCounter()
				: form.getPageSize();

		Integer pageSize = getPageSize(request, form);
		Integer pageNumber = getPageNumber(request);
		String sortOption =  getSortOption(request,form.getSortCode());

		final PageableData pageableData = createPageableData(pageNumber, pageSize, sortOption, ShowMode.Page);
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		if (null != form.getAccounts() && form.getAccounts().length > 0) {
			String selectedAccountsString = "";
			for (final String account : form.getAccounts()) {
				selectedAccountsString += "," + account;
			}
			if (selectedAccountsString.indexOf(",") == 0) {
				model.addAttribute("accountsSelectedList", selectedAccountsString.split(",", 2)[1]);
			} else {
				model.addAttribute("accountsSelectedList", selectedAccountsString);
			}
		}
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		// Set counter for ascending or descending sort
		setAscDescCounter(model, form);

		/**
		 * Checking permissions - start
		 */
		jnjLatamCommonFacadeUtil.addPermissionsFlags(request, model);
		
		/**
		 * Retrieving the user groups to check permissions - end
		 */

        if (DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType())) {
            final SearchPageData<OrderHistoryData> searchPageData = orderFacade
                    .getPagedOrderHistoryForStatuses(pageableData, form);

            final SearchPageData<OrderHistoryData> searchPageDataUpdated = searchPageData;
            final List<OrderHistoryData> list = new ArrayList();

            if (StringUtils.isNotEmpty(valToCompare)) {
                for (int i = 0; i < searchPageData.getResults().size(); i++) {
                    if (valToCompare.equals(searchPageData.getResults().get(i).getPurchaseOrderNumber())) {
                        list.add(searchPageData.getResults().get(i));
                        searchPageDataUpdated.setResults(list);
                    }
                }

                populateModel(model, searchPageDataUpdated, ShowMode.Page);

            }

            else {
                populateModel(model, searchPageData, ShowMode.Page);
            }

            populateDropDownOptions(model, currentSite, form.getSelectedAccountType());
            model.addAttribute("orderHistoryForm", form);
            if (request.getMethod().equalsIgnoreCase("POST")) {
                sessionService.setAttribute("orderHistoryForm", form);
            }
            /** Setting the start date **/
            if (null != form.getStartDate()) {
                model.addAttribute("startDate", form.getStartDate());
            }
            /** Setting the end date **/
            if (null != form.getEndDate()) {
                model.addAttribute("endDate", form.getEndDate());
            }

			final String totalAccountsSelected = Integer.toString(form.getAccounts().length);
			model.addAttribute("totalAccountsSelected", totalAccountsSelected);

			model.addAttribute("selectedaccounts",
					StringUtils.join(form.getAccounts(), Jnjb2bglobalordertemplateConstants.SYMBOl_COMMA));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, Collections.singletonList(
					new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null)));

			model.addAttribute("metaRobots", "no-index,no-follow");

			model.addAttribute("isMddSite",Boolean.TRUE);

            sessionService.removeAttribute("valToCompare");

            setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
            storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));

            return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderHistoryPage);
        }
        else {
            /* Excel image adding Started here */
            final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
            final CatalogVersionModel currentCatalog = cmsSiteService.getCurrentSite().getContentCatalogs().get(0)
                    .getActiveCatalogVersion();

			final SearchPageData<OrderHistoryData> searchPageData = orderFacade
					.getPagedOrderHistoryForStatuses(pageableData, form);
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("resultLimitExceeded", resultLimitExceeded);

			// Send site logo
			model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));
			/* Excel image adding end here */

			/* Pdf image adding Started here */
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
			return DOWNLOAD_TYPE.PDF.toString().equals(form.getDownloadType()) ? ORDER_HISTORY_PDF_VIEW
					: ORDER_HISTORY_EXCEL_VIEW;
		}
	}

	protected List<SelectOption> latamPopulateSearchOptions(final List<SelectOption> searchOptions, final Model model) {

		SelectOption orderNumber = new SelectOption(Order.SearchOption.ORDER_NUMBER, Order.SearchOption.ORDER_NUMBER);
		SelectOption productNumber = new SelectOption(Order.SearchOption.PRODUCT_NUMBER,
				Order.SearchOption.PRODUCT_NUMBER);
		SelectOption poNumber = new SelectOption(Order.SearchOption.PO_NUMBER, Order.SearchOption.PO_NUMBER);
		SelectOption invoiceNumber = new SelectOption(Order.SearchOption.INVOICE_NUMBER,
				Order.SearchOption.INVOICE_NUMBER);

		final String sessionLanguage = getSessionService().getCurrentSession().getAttribute("sessionLanguage");

		if (null != sessionLanguage && "pt".equalsIgnoreCase(sessionLanguage)) {
			orderNumber = new SelectOption(Order.SearchOption.ORDER_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.ORDER_NUMBER_PT);
			productNumber = new SelectOption(Order.SearchOption.PRODUCT_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.PRODUCT_NUMBER_PT);
			poNumber = new SelectOption(Order.SearchOption.PO_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.PO_NUMBER_PT);
			invoiceNumber = new SelectOption(Order.SearchOption.INVOICE_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.INVOICE_NUMBER_PT);
		} else if (null != sessionLanguage && "es".equalsIgnoreCase(sessionLanguage)) {
			orderNumber = new SelectOption(Order.SearchOption.ORDER_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.ORDER_NUMBER_ES);
			productNumber = new SelectOption(Order.SearchOption.PRODUCT_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.PRODUCT_NUMBER_ES);
			poNumber = new SelectOption(Order.SearchOption.PO_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.PO_NUMBER_ES);
			invoiceNumber = new SelectOption(Order.SearchOption.INVOICE_NUMBER,
					JnjlaordertemplateaddonConstants.Order.SearchOption.INVOICE_NUMBER_ES);
		}

		searchOptions.add(orderNumber);
		searchOptions.add(productNumber);
		searchOptions.add(poNumber);
		searchOptions.add(invoiceNumber);

		return searchOptions;
	}

	@Override
	protected List<SelectOption> populateChannelOptions(final List<SelectOption> channelOptions) {

		final List<String> orderTypeChannelCodes = new ArrayList<String>();
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_DFUE);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_E001);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_E003);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_E004);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_E006);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_E008);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_M002);
		orderTypeChannelCodes.add(JnjlaordertemplateaddonConstants.Order.OrderChannel.ORDET_CHANNEL_M004);
		for (final String jnjOrderChannelCode : orderTypeChannelCodes) {
			try {
				final JnjOrderChannelModel jnjOrderChannel = jnjOrderService.getOrderChannel(jnjOrderChannelCode);
				if (null != jnjOrderChannel) {
					/*
					 * JnjOrderChannelModel is fetched form Hybris. Use the Name
					 */
					final String jnjOrderChannelName = jnjOrderService.getOrderChannel(jnjOrderChannelCode).getName();
					channelOptions.add(new SelectOption(jnjOrderChannelCode,
							null != jnjOrderChannelName ? jnjOrderChannelName : jnjOrderChannelCode));
				} else {
					/*
					 * if JnjOrderChannelModel is not fetched form Hybris then
					 * Use the code
					 */
					channelOptions.add(new SelectOption(jnjOrderChannelCode, jnjOrderChannelCode));
				}
			} catch (final BusinessException businessException) {
				/*
				 * if JnjOrderChannelModel is not fetched form Hybris then Use
				 * the code
				 */
				channelOptions.add(new SelectOption(jnjOrderChannelCode, jnjOrderChannelCode));
			}
		}

		return channelOptions;
	}

	@Override
	protected List<SelectOption> populateOrderTypes(final List<SelectOption> orderTypes) {

		final List<String> orderTypeCodeList = new ArrayList<String>();
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_9SE1);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZCT);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZEX);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZFR);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZINS);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKA);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKB);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKE);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKEI);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKR);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZLZ);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZMIS);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZOCR);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZODR);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZOR);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZOR1);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZORD);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZQT);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZRCT);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZREC);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZRED);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZRER);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZRO);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZSER);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZSM);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZSER2);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZKGW);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZOS);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZSIN);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZIND);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_9SES);
		orderTypeCodeList.add(JnjlaordertemplateaddonConstants.Order.OrderType.ORDET_TYPE_ZSES);
		
		for (final String jnjOrderTypeCode : orderTypeCodeList) {
			try {
				final JnjOrderTypeModel jnjOrderTypeModel = jnjOrderService.getOrderType(jnjOrderTypeCode);
				if (null != jnjOrderTypeModel) {
					/* JnjOrderTypeModel is fetched form Hybris. Use the Name */
					final String jnjOrderTypeName = jnjOrderTypeModel.getName();
					orderTypes.add(new SelectOption(jnjOrderTypeCode,
							null != jnjOrderTypeName ? jnjOrderTypeName : jnjOrderTypeCode));
				} else {
					/*
					 * JnjOrderTypeModel is not fetched form Hybris. Use the
					 * Code in Place of Name
					 */
					orderTypes.add(new SelectOption(jnjOrderTypeCode, jnjOrderTypeCode));
				}
			} catch (final BusinessException businessException) {
				/*
				 * JnjOrderTypeModel is not fetched form Hybris. Use the Code in
				 * Place of Name
				 */
				orderTypes.add(new SelectOption(jnjOrderTypeCode, jnjOrderTypeCode));
			}
		}
		return orderTypes;
	}

	@GetMapping("/laAddToCart")
	public String addToCart(@RequestParam("orderCode") final String orderCode, final Model model, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException {

		cartService.removeSessionCart();
		sessionService.removeAttribute("splitMap");
		sessionService.removeAttribute("contractEntryList");

		final ProductData productData = jnjLatamCartFacade.createCartWithOrder(orderCode);

		if (productData instanceof JnjGTProductData && StringUtils.isNotBlank(((JnjGTProductData)productData).getInvalidProductCodes())) {
            GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.ERROR_MESSAGES_HOLDER, "orderHistory.addToCart.invalidProducts");
			return REDIRECT_PREFIX + "/order-history/order/" + orderCode;
		}

		model.addAttribute("product", productData);
		final CartData cartData = jnJGTCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		return REDIRECT_PREFIX + "/cart";
	}

	@Override
	public String getView(final String view) {
		return JnjlaordertemplateaddonControllerConstants.ADDON_PREFIX + view;
	}

	@Override
	public String orderDetail(@PathVariable("orderCode") final String orderCode, final JnjGTSearchSortForm form,
			final Model model, final HttpServletRequest request, final boolean ignoreRestriction)
			throws CMSItemNotFoundException {
		final String methodName = "Latam orderDetail()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
				Logging.BEGIN_OF_METHOD, currentClass);
		if (form.getPageSize()<=100) {
			form.setPageSize(Integer.parseInt(Config.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE)));
			}
		try {
			sessionService.setAttribute(ORDER_DETAIL_PAGE_SIZE, Integer.valueOf(form.getPageSize()));
			sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE,
					JnjGTPageType.ORDER_DETAIL);

			final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
			if (Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA.equalsIgnoreCase(countryInfo))
			{
				if (jnjLatamCartFacade.getShippingDetails() != null){
					final Map<String, String> productShippingMap = jnjLatamCartFacade.getShippingDetails();
					model.addAttribute("productShippingMap", productShippingMap);
				}
			}

			final JnjLaOrderData orderData = (JnjLaOrderData) jnjlatamCustomOrderFacade
					.getOrderDetailsForCode(orderCode, ignoreRestriction);
			sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);

			final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);

			if (null != orderData.getActualDeliveryDate()) {
				final int packingListExpiryDate = Days
						.daysBetween(new DateTime(orderData.getActualDeliveryDate()), new DateTime(new Date()))
						.getDays();
				model.addAttribute("packingListExpiryDate", packingListExpiryDate);
			}
			List<JnJInvoiceOrderData> orderInvoices = new ArrayList<>();
			try {
				final String sapOrderNumber = orderData.getSapOrderNumber();

				if (sapOrderNumber != null && !sapOrderNumber.isEmpty()) {
					orderInvoices = jnjLatamInvoiceFacade.getInvoices(sapOrderNumber);
				}

				JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.LoadInvoices.LOAD_INVOICES_FILE_NAME, methodName,
						"no of invoice :: " + orderInvoices.size() + " for order no :: " + orderCode, currentClass);
				if (!orderInvoices.isEmpty()) {
					model.addAttribute("hasInvoice", Boolean.TRUE);
				} else {
					model.addAttribute("hasInvoice", Boolean.FALSE);
				}
			} catch (final Exception ex) {
				JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.LoadInvoices.LOAD_INVOICES_FILE_NAME, methodName,
						"Some error occurred while fetching invoice", ex, currentClass);
				model.addAttribute("hasInvoice", Boolean.FALSE);
			}
			model.addAttribute("orderData", orderData);
			model.addAttribute("country", countryInfo);
			model.addAttribute(SHOW_ATP_FLAG_MAP, jnjLatamCommonService.showATPFlagMap(model, "orderData"));
			model.addAttribute("searchSortForm", form);
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_URL, JnjWebUtil.getSiteUrl(request));
			setOrderDetailBreadCrumb(model);
		} catch (final UnknownIdentifierException exception) {
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
					"Attempted to load a order that does not exist or is not visible", exception, currentClass);
			return REDIRECT_HOME;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		
		/**
		 * Checking permissions - start
		 */
		jnjLatamCommonFacadeUtil.addPermissionsFlags(request, model);
		
		/**
		 * Retrieving the user groups to check permissions - end
		 */

		return getView(JnjlaordertemplateaddonControllerConstants.Views.Pages.Order.OrderDetailPage);
	}

	@Override
	public String invoiceDetails(@PathVariable("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request, final boolean ignoreRestriction) throws CMSItemNotFoundException {
		final String methodName = "Latam invoiceDetails()";

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		try {
			sessionService.setAttribute(ORDER_DETAIL_PAGE_SIZE, Integer.valueOf(10));
			sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE,
					JnjGTPageType.INVOICE_DETAIL);
			JnjLaOrderData orderData = null;
			try {
				orderData = (JnjLaOrderData) jnjlatamCustomOrderFacade.getOrderDetailsForCode(orderCode,
						ignoreRestriction);

			} catch (final Exception e) {

				JnjGTCoreUtil.logWarnMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
						"invoiceDetails : " + e, currentClass);
				return REDIRECT_HOME;
			}
			if (null == orderData) {
				throw new UnknownIdentifierException(
						orderCode + " does not exists or it is not valid order for current user");
			}
			sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);
			sessionService.removeAttribute(ORDER_DETAIL_PAGE_SIZE);

			// jjepic - 773 - packing list expiry
			if (null != orderData.getActualDeliveryDate()) {
				final int packingListExpiryDate = Days
						.daysBetween(new DateTime(orderData.getActualDeliveryDate()), new DateTime(new Date()))
						.getDays();
				model.addAttribute("packingListExpiryDate", packingListExpiryDate);
			}
			model.addAttribute("orderData", orderData);
			final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
			List<JnJInvoiceOrderData> orderInvoices = new ArrayList<JnJInvoiceOrderData>();
			try {
				final String sapOrderNumber = orderData.getSapOrderNumber();

				if (sapOrderNumber != null && !sapOrderNumber.isEmpty()) {
					orderInvoices = jnjInvoiceFacade.getInvoices(sapOrderNumber);
				}
				if (orderInvoices != null) {
					JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.LoadInvoices.LOAD_INVOICES_FILE_NAME, methodName,
							"no of invoice :: " + orderInvoices.size() + " for order no :: " + orderCode, currentClass);
				}
			} catch (final Exception ex) {
				JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.LoadInvoices.LOAD_INVOICES_FILE_NAME, methodName,
						"Some error occurred while fetching invoice", ex, currentClass);
			}
			final String countryInfo = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
			model.addAttribute("orderInvoices", orderInvoices);
			model.addAttribute("countryInfo", countryInfo);
			model.addAttribute("errorInvoiceMessage", Boolean.FALSE);
			final List<Breadcrumb> breadcrumbs = new ArrayList<>();
			breadcrumbs.add(new Breadcrumb("/order-history",
					jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
			breadcrumbs.add(new Breadcrumb("/order-history/order/" + orderCode,
					jnjCommonFacadeUtil.getMessageFromImpex("orderDetailPage.heading"), null));
			breadcrumbs.add(
					new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("invoiceDetailPage.heading"), null));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

			model.addAttribute("proofOfDelTestInd", Config.getParameter(Invoice.PROOF_OF_DELIVERY_TEST_IND));
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_URL, JnjWebUtil.getSiteUrl(request));
			model.addAttribute("sessionLanguage", sessionService.getCurrentSession().getAttribute("sessionLanguage"));
			storeCmsPageInModel(model, getContentPageForLabelOrId(INVOICE_DETAIL_CMS_PAGE));

			setPdfXmlDownloadFlag(model);

			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Invoice.InvoiceDetailPage);
		} catch (final UnknownIdentifierException exception) {
			JnjGTCoreUtil.logWarnMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName,
					"invoiceDetails : " + exception, currentClass);

			return REDIRECT_ORDER_HISTORY;
		}
	}

	public void setPdfXmlDownloadFlag(final Model model) {
		final CountryModel country = cmsSiteService.getCurrentSite().getDefaultCountry();
		if (country != null) {
			final String countryIso = country.getIsocode();

			final List<String> pdfCountriesList = JnjLaCoreUtil.getCountriesList(INVOICE_PDF_PROPERTY);
			if (pdfCountriesList != null && pdfCountriesList.contains(countryIso)) {
				model.addAttribute("pfdInvoiceLinkFlag", Boolean.TRUE);
			}

            final List<String> xmlCountriesList = JnjLaCoreUtil.getCountriesList(INVOICE_XML_PROPERTY);
            if (xmlCountriesList != null && xmlCountriesList.contains(countryIso))
            {
                if ((Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL).equalsIgnoreCase(countryIso))
                {
                    model.addAttribute("brXmlInvoiceLinkFlag", Boolean.TRUE);
                }
                else
                {
                    model.addAttribute("xmlInvoiceLinkFlag", Boolean.TRUE);
                }
            }
        }

	}

	@Override
	public String invoiceDetailsPOST(@PathVariable("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException {

		return invoiceDetails(orderCode, model, request, true);
	}

	@Override
	public String getSelectAccountPopup(final Model model, final String[] selectedAccounts,
			final JnjGTOrderHistoryForm form, @RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm) {

		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

		final int finalPageSize = Boolean.valueOf(showMore) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm)) {
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting
																	// search
																	// term
		}
		/** Calling facade layer **/
		final JnjGTAccountSelectionData accountSelectionData = jnjLatamCustomerFacadeImpl.getAccountsMap(true, true,
				false, pageableData);

		model.addAttribute("popUpType", POP_UP_TYPE.SELECT_ACCOUNTS);
		model.addAttribute("accounts", accountSelectionData.getAccountsMap());
		model.addAttribute("currentAccountId", orderFacade.getCurrentB2bUnitId());
		model.addAttribute("selectedAccounts", selectedAccounts);
		model.addAttribute("orderHistoryForm", form);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting
																								// pagination
																								// data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.OrderHistoryPopups);
	}

    @GetMapping("/nfeFile")
    public String nfeFile(
    		@RequestParam(value = "invoiceNumber") final String invoiceNumber,
            @RequestParam(value = "orderCode") final String orderCode,
            final HttpServletResponse httpServletResponse,
			final RedirectAttributes redirectAttributes) {

		invoiceUtil.prepareNfeFile(invoiceNumber, httpServletResponse, redirectAttributes);

		return REDIRECT_PREFIX + INVOICE_DETAIL_PAGE_REL_PATH + orderCode;
    }

    @RequestMapping(value = "/invoiceDocFile")
    public String invoiceDocFile(
    		@RequestParam(value = "downloadType") final String downloadType,
			@RequestParam(value = "orderCode") final String orderCode,
			@RequestParam(value = "invoiceNumber") final String invoiceNumber,
			final HttpServletResponse httpServletResponse,
			final RedirectAttributes redirectAttributes) {

		invoiceUtil.prepareInvoiceDocFile(downloadType, invoiceNumber, httpServletResponse, redirectAttributes);

		return REDIRECT_PREFIX + INVOICE_DETAIL_PAGE_REL_PATH + orderCode;
    }


	private Integer getPageSize(final HttpServletRequest request, JnjGTOrderHistoryForm form){
		if(DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType())) {
			Integer pageSize = PAGE_SIZE_DEFAULT;
			if (request.getParameter("pageSize") != null){
				pageSize = Integer.valueOf(request.getParameter("pageSize"));
			}
			return pageSize;
		}
		return PDF_EXCEL_MAX_PAGE_SIZE;
	}

	private Integer getPageNumber(final HttpServletRequest request){
		Integer pageNumber = PAGE_NUMBER_DEFAULT;

		if(request.getParameter("currentPage") != null) {
			pageNumber = Integer.valueOf(request.getParameter("currentPage"));
		}

		return pageNumber;
	}

	private String getSortOption(final HttpServletRequest request, final String sortParam){
		final String sortColumn = request.getParameter("sortColumn");
		final String sortDirection = request.getParameter("sortDirection");
		String sortOption = sortParam;

		/*Sort Column & Direction*/
		if(sortColumn != null && sortDirection != null){

			switch (sortColumn){
				case "order":
				case "orderNumber":
					if(sortDirection.equals(ASC)){
						sortOption = Jnjlab2bcoreConstants.Order.SortOption.ORDER_NUMBER_INCREASING;
						//sortOption = Jnjlab2bcoreConstants.Order.SortOption.SAP_NUMBER_INCREASING;
					}else{
						sortOption = Jnjlab2bcoreConstants.Order.SortOption.ORDER_NUMBER_DECREASING;
						//sortOption = Jnjlab2bcoreConstants.Order.SortOption.SAP_NUMBER_DECREASING;
					}
					break;

				case "orderDate":
					if(sortDirection.equals(ASC)){
						sortOption = Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST;
					}else{
						sortOption = Order.SortOption.DEFAULT_SORT_CODE;
					}
					break;

				case"total":
					if(sortDirection.equals(ASC)){
						sortOption = Order.SortOption.ORDER_TOTAL_INCREASING;
					}else{
						sortOption = Order.SortOption.ORDER_TOTAL_DECREASING;
					}
					break;
			}
		}

		return  sortOption;
	}
	
	public JnjLatamCommonService getJnjLatamCommonService() {
		return jnjLatamCommonService;
	}

	public void setJnjLatamCommonService(JnjLatamCommonService jnjLatamCommonService) {
		this.jnjLatamCommonService = jnjLatamCommonService;
	}

}