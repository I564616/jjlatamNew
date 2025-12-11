package com.jnj.la.browseandsearch.controllers.pages;


import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.browseandsearch.controllers.pages.JnjGTSearchPageController;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.impl.SearchBreadcrumbBuilder;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.b2b.storefront.util.XSSFilterUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.browseandsearch.controllers.JnjlabrowseandsearchaddonControllerConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.services.MessageService;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Locale;
/**
 *
 */

/**
 * @author sbaner48
 */
public class JnjLatamSearchPageController extends JnjGTSearchPageController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private CMSSiteService cmsSiteService;

    @Autowired
    private CatalogVersionService catalogVersionService;

    /**
     * Default current page value.
     */
    private static final int DEFAULT_PAGE = 0;

    /**
     * View Id for the Product Search Result Page.
     */
    protected static final String SEARCH_CMS_PAGE_ID = "search";

    /**
     * View Id for the Empty Result Page.
     */
    protected static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";

    /**
     * Constant - eligibility text
     */
    protected static final String IS_ELIGIBLE_FOR_NEW_ORDER = "isEligibleForNewOrder";

    protected static final String PAGE_TYPE = "search";
    
    protected static final String SEARCH_RESULT_PDF = "latamproductSearchResultsPdf";
	protected static final String SEARCH_RESULT_EXCEL = "latamproductSearchResultsExcel";

    @Resource(name = "customerLocationService")
    private CustomerLocationService customerLocationService;

    @Resource(name = "searchBreadcrumbBuilder")
    private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

    @Resource(name = "productSearchFacade")
    private ProductSearchFacade<JnjLaProductData> productSearchFacade;

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

    @Resource(name = "sessionService")
    private SessionService sessionService;

    @Resource(name = "customerFacade")
    private CustomerFacade customerFacade;

    @Autowired
    private JnjGTProductFacade jnjGTProductFacade;

    @Resource(name = "GTB2BUnitFacade")
    private JnjGTB2BUnitFacade jnjGTB2BUnitFacade;

    @Resource(name = "GTCustomerFacade")
    private JnjGTCustomerFacade jnjGTCustomerFacade;

    @Autowired
    private MessageService messageService;

    @Autowired
    private I18NService i18nService;

    @Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

    @Override
    public String getView(final String view) {
        return JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + view;
    }
    @Override
    protected int getSearchPageSize()
    {
        return 10;
    }

    @Override
    public String textSearch(@RequestParam(value = "text", defaultValue = "") String searchText, HttpServletRequest request, Model model) throws CMSItemNotFoundException {

        final String methodName = "textSearch()";

        ProductSearchPageData<SearchStateData, JnjLaProductData> searchPageData = null;
        if (StringUtils.isNotBlank(searchText)) {
            final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);
            final SearchStateData searchState = new SearchStateData();
            final SearchQueryData searchQueryData = new SearchQueryData();
            searchQueryData.setValue(XSSFilterUtil.filter(searchText));
            searchState.setQuery(searchQueryData);

            searchPageData = productSearchFacade.textSearch(searchState,
                    pageableData);

            latamRefineFacetList(PAGE_TYPE, searchPageData);
            model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));

            if (searchPageData == null) {
                storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
            } else if (searchPageData.getKeywordRedirectUrl() != null) {
                return "redirect:" + searchPageData.getKeywordRedirectUrl();
            } else if (searchPageData.getPagination().getTotalNumberOfResults() == 0) {
                model.addAttribute("searchPageData", searchPageData);
                storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
                updatePageTitle(searchText, model);
            } else {
                storeContinueUrl(request);
                populateModel(model, searchPageData, ShowMode.Page);
                model.addAttribute("isShowAllAllowed", Boolean.valueOf(isShowAllAllowed(searchPageData)));
                final JnjGTSearchSortForm form = new JnjGTSearchSortForm();
                form.setSearchText(searchText);
                if ("GET".equalsIgnoreCase(request.getMethod())) {
                    form.setPageNumber(0);
                    form.setPageSize(10);
                }
                setDisplayResult(form,searchPageData.getPagination().getTotalNumberOfResults(),searchPageData.getPagination().getNumberOfPages());

                model.addAttribute("searchSortForm", form);
                storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
                updatePageTitle(searchText, model);
            }
            getRequestContextData(request).setSearch(searchPageData);
        }
        else {
            storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
        }

        String pageName = StringUtils.EMPTY;
        try {
            pageName = messageService.getMessageForCode("search.result",
                    i18nService.getCurrentLocale());
        } catch (BusinessException businessException) {
            JnjGTCoreUtil.logErrorMessage(SEARCH_CMS_PAGE_ID, methodName, businessException.getMessage(),
                    businessException, JnjLatamSearchPageController.class);
        }

        if (searchPageData != null) {
            final List<Breadcrumb> breadcrums = searchBreadcrumbBuilder.getBreadcrumbs(null, searchText,
                    CollectionUtils.isEmpty(searchPageData.getBreadcrumbs()));
            breadcrums.get(breadcrums.size() - 1).setName(pageName);
            model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadcrums);
        }

        model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
        model.addAttribute("metaRobots", "no-index,follow");

        final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
                "search.meta.description.results", null, getI18nService().getCurrentLocale())
                + " "
                + searchText
                + " "
                + getMessageSource().getMessage("search.meta.description.on", null, getI18nService().getCurrentLocale())
                + " "
                + getSiteName());
        final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
        setUpMetaData(model, metaKeywords, metaDescription);
        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
        model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
        model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
        model.addAttribute("showMoreCounter", "1");
        setFalgForSpecialityCustomer(model);

        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

        model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
        if (searchPageData == null || searchPageData.getPagination().getTotalNumberOfResults() == 0) {
            return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchPage);
        } else {
            return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchListPage);
        }
    }

    @Override
    public String refineSearchDetails(final String searchQuery, final JnjGTSearchSortForm form,
                                      final String searchText, final HttpServletRequest request,
                                      final Model model) throws CMSItemNotFoundException {
        boolean resultLimitExceeded = false;
        final String downloadType = form.getDownloadType();
        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
        final int finalPageSize = form.isShowMore() ? form.getPageSize() * form.getShowMoreCounter() : form
                .getPageSize();
        int totalResults;

        if ( (DOWNLOAD_TYPE.PDF.toString().equals(form.getDownloadType())
                || DOWNLOAD_TYPE.EXCEL.toString().equals(form.getDownloadType()))
                && StringUtils.isNotEmpty(form.getTotalNumberOfResults()) ) {

            totalResults = Integer.parseInt(form.getTotalNumberOfResults());

            final int resultLimit = Integer.parseInt(Config
                    .getParameter(Jnjb2bbrowseandsearchConstants.PLP.SLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));

            resultLimitExceeded = resultLimit > 0 && totalResults >= resultLimit;
        }
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            form.setPageNumber(0);
            form.setPageSize(10);
        }
        final JnjPageableData pageableData = createJnjPageableData(form.getPageNumber(), form.getPageSize(), form.getSortCode(),
                ShowMode.Page);

        ProductSearchPageData<SearchStateData, JnjLaProductData> searchPageData;

        if (!DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType())) {
            searchPageData = latamPerformSearch(searchQuery, DEFAULT_PAGE,
                    ShowMode.Page, form.getSortCode(), form.getFullCount());
        } else {
            searchPageData = latamPerformSearch(searchQuery, form.getPageNumber(),
                    ShowMode.Page, form.getSortCode(), finalPageSize);
        }
        setDisplayResult(form,searchPageData.getPagination().getTotalNumberOfResults(),searchPageData.getPagination().getNumberOfPages());

        model.addAttribute("searchSortForm", form);
        model.addAttribute("searchPageData", searchPageData);
        model.addAttribute("pageableData", pageableData);
        model.addAttribute("showMoreCounter", String.valueOf(form.getShowMoreCounter()));
        model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));
        List<ContentCatalogModel> catalogList = cmsSiteService.getCurrentSite().getContentCatalogs();
        MediaModel mediaModel1 = null;
        MediaModel mediaModel2 = null;
        MediaModel mediaModel3 = null;
        final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
        if (catalogList != null && !catalogList.isEmpty()) {
            mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                    Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
            mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                    Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
            mediaModel3 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                    "epicEmailLogoImageOne");
        }
        if (mediaModel1 != null) {
           model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
        }
        if (mediaModel2 != null) {
            model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
        }

        if (mediaModel3 != null) {
            model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaModel3));
        }

        model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
        model.addAttribute("isMddSite", Boolean.valueOf(Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite)));

        model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));

        if (DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType())) {
            if (searchPageData.getPagination().getTotalNumberOfResults() == 0) {
                updatePageTitle(searchPageData.getFreeTextSearch(), model);
                storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
            } else {
                // modify breadcrums name to display 'Search Result' as text

                ProductSearchPageData<SearchStateData, ProductData> tempSearchPageData = new ProductSearchPageData<>();
                tempSearchPageData.setBreadcrumbs(searchPageData.getBreadcrumbs());
                tempSearchPageData.setFreeTextSearch(searchPageData.getFreeTextSearch());

                final List<Breadcrumb> breadcrums = searchBreadcrumbBuilder.getBreadcrumbs(null, tempSearchPageData);
                breadcrums.get(breadcrums.size() - 1).setName(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_RESULT);
                model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadcrums);
                populateModel(model, searchText);
                storeContinueUrl(request);
                updatePageTitle(searchPageData.getFreeTextSearch(), model);
                storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
            }
            model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS));

            if (null == searchPageData || searchPageData.getPagination().getTotalNumberOfResults() == 0) {
                return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchPage);
            } else {
                return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchListPage);
            }
        } else {
            model.addAttribute("currentAccount", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
                    .getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

            model.addAttribute("currentAccountName", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
                    .getCurrentB2bUnit().getLocName() : StringUtils.EMPTY);

            model.addAttribute(Jnjb2bCoreConstants.Product.SEARCH_RES_DOWNLOAD_FLAG, Boolean.TRUE);
            model.addAttribute("resultLimitExceeded", resultLimitExceeded);
            return DOWNLOAD_TYPE.PDF.toString().equals(downloadType) ? SEARCH_RESULT_PDF : SEARCH_RESULT_EXCEL;
        }
    }

    private void latamRefineFacetList(String pageType, ProductSearchPageData<SearchStateData, JnjLaProductData> searchPageData) {
        final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
        if (CollectionUtils.isNotEmpty(facets)) {
            for (final FacetData facetData : facets) {
                if ( ("plp".equals(pageType) && "searchCategory".equals(facetData.getCode())) ||
                        "plpCategory".equals(facetData.getCode()) ) {
                    facets.remove(facetData);
                    break;
                }
            }
        }
    }

    protected ProductSearchPageData<SearchStateData, JnjLaProductData> latamPerformSearch(final String searchQuery, final int page,
                                                                                final ShowMode showMode, final String sortCode, final int pageSize)
    {
        final JnjPageableData pageableData = createJnjPageableData(page, pageSize, sortCode, showMode);
        final SearchStateData searchState = new SearchStateData();
        final SearchQueryData searchQueryData = new SearchQueryData();
        searchQueryData.setValue(searchQuery);
        searchState.setQuery(searchQueryData);

        return productSearchFacade.textSearch(searchState, pageableData);
    }
}
