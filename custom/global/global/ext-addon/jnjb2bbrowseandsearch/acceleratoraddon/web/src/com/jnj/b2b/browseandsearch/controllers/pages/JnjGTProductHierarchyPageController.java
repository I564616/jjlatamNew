/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.b2b.browseandsearch.controllers.pages;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.impl.HierarchyBreadCrumbBuilder;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.product.JnjGTProductFacade;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author nsinha7
 * @version 1.0
 */

@Controller
@Scope("tenant")
@RequireHardLogIn
// FRAMEWORK_UPDATE - TODO - AntPathMatcher was replaced with PathPatternParser as the new default path parser in Spring 6. Adjust this path to the new matching rules or re-enable deprecated AntPathMatcher. Consult "Adapting to PathPatternParser new default URL Matcher" JDK21 Upgrade Step in SAP Help documentation.
@RequestMapping(value = "/*/ProductHierarchy")
public class JnjGTProductHierarchyPageController extends AbstractSearchPageController{

    private static final Class<JnjGTProductHierarchyPageController> currentClass = JnjGTProductHierarchyPageController.class;
    public static final String EXCEPTION = "Exception :: ";



    /**
     * Constant to store Request param Category Code.
     */
    protected static final String CATEGORY_CODE_PATH_VARIABLE_PATTERN = "/{categoryCode:.*}";
    protected static final String IS_ELIGIBLE_FOR_NEW_ORDER = "isEligibleForNewOrder";
    protected static final String USER_TYPE = "userType";
    /**
     * Constant for request mapping for catalog download.
     */
    protected static final String CATEGORY_PRODUCT_LIST_DOWNLOAD = "/catalog-Download/{categoryCode:.*}";
    /**
     * View Id for the MDD Product Catalog download.s
     */
    protected static final String MDD_PRODUCT_CATALOG_EXCEL_VIEW = "mddProductCatalogExcelView";
    /**
     * View Id for the MDD Product Catalog download.s
     */
    protected static final String CONS_PRODUCT_CATALOG_EXCEL_VIEW = "consProductCatalogExcelView";
    /**
     * View Id for the PProduct List PDF Download.
     */
    protected static final String CATEGORY_SEARCH_RESULT_PDF = "productSearchResultsPdf";
    /**
     * View Id for the PProduct List Excel Download.
     */
    protected static final String CATEGORY_SEARCH_RESULT_EXCEL = "productSearchResultsExcel";

    protected static final String METHOD_COMPARE = "compare()";

    protected Map<String, String> exportCatalogData = new HashMap<>();
    protected ModelService modelService;
    protected UserService userService;
    protected FlexibleSearchService flexibleSearchService;
    protected SessionService sessionService;
    protected Converter<JnJProductModel, JnjGTProductData> productConverter;
    protected JnjGTProductFacade jnjGTProductFacade;
    protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
    protected JnjGTB2BUnitFacade jnjGTB2BUnitFacade;
    protected JnjGTCustomerFacade jnjGTCustomerFacade;
    protected JnJGTProductService jnjGTProductService;
    protected ProductSearchFacade<ProductData> productSearchFacade;
    protected CommerceCategoryService commerceCategoryService;
    protected Converter<CategoryModel, CategoryData> jnjGTProductCategoryConverter;
    protected HierarchyBreadCrumbBuilder hierarchyBreadcrumbBuilder;
    protected UrlResolver<CategoryModel> hierarchyModelUrlResolver;
    protected CustomerLocationService customerLocationService;
    protected CustomerFacade customerFacade;
    protected CMSSiteService cMSSiteService;
    MediaService mediaService;
    protected CatalogVersionService catalogVersionService;
    protected static final int DEFAULT_PAGE = 0;
    protected ConfigurationService configurationService;
    protected enum DOWNLOAD_TYPE
    {
        PDF, EXCEL, NONE
    }
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = {RequestMethod.GET,RequestMethod.POST})
    public String showViewForCatalog(@PathVariable("categoryCode") final String categoryCode,
                                     @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                     @RequestParam(value = "q", required = false) final String searchQuery, final JnjGTSearchSortForm form,
                                     final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws UnsupportedEncodingException, CMSItemNotFoundException
    {
        return displayCategoriesAndProducts(categoryCode, showMode, searchQuery, form, model, request, response);
    }

    protected String displayCategoriesAndProducts(final String categoryCode, final ShowMode showMode, final String searchQuery, final JnjGTSearchSortForm form,
                                                  final Model model, final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException, CMSItemNotFoundException {
        final CategoryModel category = commerceCategoryService.getCategoryForCode(categoryCode);
        final String redirection = checkRequestUrl(request, response, hierarchyModelUrlResolver.resolve(category));
        if (StringUtils.isNotEmpty(redirection))
        {
            return redirection;
        }

        final String hierarchyTree = request.getServletPath();
        final String[] categoryTree = hierarchyTree.substring(1).split("/");
        final String franchise = categoryTree[1].replace("-", " ");

        model.addAttribute("categoryCode", categoryCode);
        final CategoryPageModel categoryPage = getCategoryPage(category);
        Boolean displayProducts = category.getDisplayProducts();
        String view = null;
        model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
        model.addAttribute(USER_TYPE, sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE));
        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);

        if (Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite))
        {
            model.addAttribute("mddExportFileUrl", jnjGTProductService.getMediaURLForMDDExport());
            model.addAttribute("mddPdfExportFileUrl", jnjGTProductService.getMediaURLForPDFMDDExport());
            model.addAttribute("isMddSite", Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite));
        }

        if(category.getCategories().size()>0) {
            displayProducts = false;
        }

        if (displayProducts)
        {
            boolean resultLimitExceeded = false;
            final String downloadType = form.getDownloadType();
            final int finalPageSize = configurationService.getConfiguration().getInt(Jnjb2bCoreConstants.MediaFolder.CATALOG_RESULT_PAGE_LIMIT,1000);
            final SearchQueryData searchQueryData = new SearchQueryData();
            searchQueryData.setValue(searchQuery);

            if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()) && isExcelOrPDF(downloadType))
            {
                final int totalResults = Integer.parseInt(form.getTotalNumberOfResults());
                final int resultLimit =
                        Integer.parseInt(configurationService.getConfiguration()
                                .getString(Jnjb2bbrowseandsearchConstants.PLP.PLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));
                if (resultLimit > 0 && totalResults >= resultLimit)
                {
                    resultLimitExceeded = true;
                }
            }
            //Changes for AAOL-7654
            if("GET".equalsIgnoreCase(request.getMethod())) {
                form.setPageNumber(0);
                form.setPageSize(configurationService.getConfiguration().getInt(Jnjb2bCoreConstants.MediaFolder.CATALOG_RESULT_PAGE_LIMIT,10));
            }
            final ProductCategorySearchPageData<SearchStateData,
                    ProductData, CategoryData> searchPageData = populateSearchPageData(finalPageSize,form,model,showMode,categoryCode,searchQueryData);


            if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType))
            {
                final List<Breadcrumb> breadCrumbList = customizeBreadCrumb(hierarchyBreadcrumbBuilder.getBreadcrumbs(categoryCode, searchPageData));
                model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadCrumbList);
                model.addAttribute("pageType", PageType.CATEGORY.name());

                //Changes for AAOL-7654

                final long totalNumberOfResults = searchPageData.getPagination().getTotalNumberOfResults();
                form.setTotalNumberOfResults(String.valueOf(totalNumberOfResults));
                form.setLastPageNumber(searchPageData.getPagination().getNumberOfPages());
                final int resultStartNumber = (form.getPageNumber() * form.getPageSize() +1);
                if(resultStartNumber<=totalNumberOfResults) {
                    final int resultLastNumber = ((form.getPageNumber()+1)* form.getPageSize());
                    if(resultLastNumber>totalNumberOfResults) {
                        form.setDisplayResults(resultStartNumber +" - "+totalNumberOfResults );
                    }else {
                        form.setDisplayResults(resultStartNumber +" - "+resultLastNumber );
                    }
                }else {
                    form.setDisplayResults("");
                }
                model.addAttribute("searchSortForm", form);
                model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
                model.addAttribute("showMoreCounter", "1");
                model.addAttribute("isinternationalAff", jnjGTB2BUnitFacade.isCustomerInternationalAff());

                updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
                final RequestContextData requestContextData = getRequestContextData(request);
                requestContextData.setCategory(category);
                requestContextData.setSearch(searchPageData);

                if (searchQueryData.getValue() != null)
                {
                    model.addAttribute("metaRobots", "no-index,follow");
                }
                model.addAttribute("groups",
                        configurationService.getConfiguration().getString(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
                view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductListPage;
            }
            else
            {
                model.addAttribute("currentAccount",
                        (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

                model.addAttribute(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG, Boolean.FALSE);
                model.addAttribute("resultLimitExceeded", resultLimitExceeded);

                final String mediaDirBase =
                        configurationService.getConfiguration().getString(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
                final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite()
                        .getContentCatalogs().get(0).getActiveCatalogVersion();
                model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
                        + mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());

                final List<ContentCatalogModel> catalogList = getCmsSiteService().getCurrentSite().getContentCatalogs();
                if (CollectionUtils.isNotEmpty(catalogList)) {
                    final MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                            Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
                    final MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                            Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
                    if (mediaModel1 != null) {
                        model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
                    }
                    if (mediaModel2 != null) {
                        model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
                    }
                }

                return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? CATEGORY_SEARCH_RESULT_PDF : CATEGORY_SEARCH_RESULT_EXCEL;
            }
        }
        else
        {

            final int finalPageSize = (form.isShowMore()) ? form.getPageSize(currentSite) * form.getShowMoreCounter() : form
                    .getPageSize(currentSite);
            final SearchQueryData searchQueryData = new SearchQueryData();
            searchQueryData.setValue(searchQuery);

            final ProductCategorySearchPageData<SearchStateData,
                    ProductData, CategoryData> searchPageData = populateSearchPageData(finalPageSize,form,model,showMode,categoryCode,searchQueryData);

            final List<Breadcrumb> breadCrumbList = customizeBreadCrumb(hierarchyBreadcrumbBuilder.getBreadcrumbs(categoryCode, searchPageData));
            model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadCrumbList);

            setUpCategoryLevelData(model, category, false,franchise);
            view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductHierarchyPage;

            updatePageTitle(category, null, model);
        }
        storeCmsPageInModel(model, categoryPage);
        storeContinueUrl(request);
        final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
        final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
        setUpMetaData(model, metaKeywords, metaDescription);
        setFalgForSpecialityCustomer(model);

        final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);

        if (null != showChangeAccountLink && (Boolean) showChangeAccountLink)
        {
            model.addAttribute("showChangeAccountLink", Boolean.TRUE);
        }

        return getView(view);
    }

    private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> populateSearchPageData(
            int finalPageSize, JnjGTSearchSortForm form, Model model,ShowMode showMode, String categoryCode, SearchQueryData searchQueryData) {
        ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;

        final SearchStateData searchState = new SearchStateData();
        searchState.setQuery(searchQueryData);
        //Changes for AAOL-7654
        final String downloadType = form.getDownloadType();
        PageableData pageableData = null;

        if(DOWNLOAD_TYPE.NONE.toString().equals(downloadType)) {
            pageableData = createPageableData(form.getPageNumber(), form.getPageSize(), form.getSortCode(), ShowMode.Page);
            searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
        }
        else {
            pageableData = createPageableData(form.getPageNumber(), form.getFullCount(), form.getSortCode(), ShowMode.Page);
            searchPageData = getProductSearchFacade().categorySearch(categoryCode,searchState, pageableData);
        }


        refineFacetList(searchPageData);

        populateModel(model, searchPageData, showMode);
        return searchPageData;
    }

    @PostMapping(CATEGORY_PRODUCT_LIST_DOWNLOAD)
    @ResponseBody
    public Boolean category() {
        return jnjGTProductFacade.sendEmailForAllProductsOfCatalog();
    }

    @Override
    protected void storeContinueUrl(final HttpServletRequest request)
    {
        final StringBuilder url = new StringBuilder();
        url.append(request.getServletPath());
        final String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty())
        {
            url.append('?').append(queryString);
        }
        getSessionService().setAttribute(Jnjb2bbrowseandsearchWebConstants.CONTINUE_URL, url.toString());
    }

    /**
     * Performs Category search based on the provided params.
     *
     * @param categoryCode
     * @param searchQuery
     * @param page
     * @param showMode
     * @param sortCode
     * @param pageSize
     * @return ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>
     */
    protected ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> performSearch(final String categoryCode,
                                                                                                      final String searchQuery, final int page, final ShowMode showMode, final String sortCode, final int pageSize)
    {
        final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
        final SearchStateData searchState = new SearchStateData();
        final SearchQueryData searchQueryData = new SearchQueryData();
        searchQueryData.setValue(searchQuery);
        searchState.setQuery(searchQueryData);
        return productSearchFacade.categorySearch(categoryCode, searchState, pageableData);
    }

    /**
     * Updates page title based on the category and applied Facet.
     *
     * @param category
     * @param appliedFacets
     * @param model
     */
    protected <QUERY> void updatePageTitle(final CategoryModel category, final List<BreadcrumbData<QUERY>> appliedFacets,
                                           final Model model)
    {
        storeContentPageTitleInModel(model, getPageTitleResolver().resolveCategoryPageTitle(category, appliedFacets));
    }

    protected CategoryPageModel getCategoryPage(final CategoryModel category)
    {
        final String methodName = "getCategoryPage()";
        try
        {
            return getCmsPageService().getPageForCategory(category);
        }
        catch (final CMSItemNotFoundException ignore)
        {
            JnjGTCoreUtil.logErrorMessage("CMS item not found for category page", methodName, EXCEPTION + ignore, currentClass);
        }
        return null;
    }


    protected void setUpCategoryLevelData(final Model model, final CategoryModel selectedCategory, final boolean isPCM, String franchise)
    {
        final Map<String, List<CategoryData>> subCategories = new LinkedHashMap<>();
        buildGTCategoriesData(selectedCategory,model,franchise);
        buildSubCategoriesData(selectedCategory, subCategories, isPCM);
        final CategoryModel rootCategory = commerceCategoryService.getCategoryForCode(Jnjb2bbrowseandsearchConstants.PLP.ROOT_CATEGORY_CODE);
        if (rootCategory.getCategories().contains(selectedCategory))
        {
            subCategories.remove(selectedCategory.getName());
        }

        model.addAttribute("currentAccount", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
                .getCurrentB2bUnit().getUid() : StringUtils.EMPTY);
        model.addAttribute("userEmail", (jnjGTCustomerFacade.getCurrentCustomer() != null) ? jnjGTCustomerFacade
                .getCurrentCustomer().getEmail() : StringUtils.EMPTY);

        model.addAttribute("subCategoriesData", subCategories);
    }

    protected void buildGTCategoriesData(CategoryModel selectedCategory,
                                         Model model, String franchise) {

        Map<CategoryData,List<CategoryData>> dataObject =null;
        Map<CategoryData,List<CategoryData>> userSelectedFranchiseMap = null;
        final List<String> userSelectedFranchise = new ArrayList<>();
        final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
        List<CategoryModel> allowedFranchise = null;
        if(CollectionUtils.isNotEmpty(currentCustomer.getAllowedFranchise()))
        {
            allowedFranchise = currentCustomer.getAllowedFranchise();
        }
        else
        {
            allowedFranchise = jnjGTCustomerFacade.getAllNewFranchise();
        }
        for (final CategoryModel categoryModel : allowedFranchise) {
            userSelectedFranchise.add(categoryModel.getName());
        }

        dataObject = populateChildCategoriesData(selectedCategory);

        userSelectedFranchiseMap = filterChildData(dataObject,userSelectedFranchise,selectedCategory,franchise);
        //Changes for AAOL-7606
        Map<CategoryData,List<CategoryData>> sortedMap = null;
        if(("ProductCategories").equalsIgnoreCase(selectedCategory.getCode())) {
            sortedMap = customSortCategoryData(userSelectedFranchiseMap);
        }else {
            sortedMap = customSortCategoryByName(userSelectedFranchiseMap);
        }

        model.addAttribute("subCategoriesDatas", sortedMap);
    }


    private Map<CategoryData, List<CategoryData>> populateChildCategoriesData(
            CategoryModel selectedCategory) {

        final Map<CategoryData,List<CategoryData>> dataObject = new LinkedHashMap<>();
        CategoryData subCategoryData = null;
        List<CategoryData> subCategories = null;
        for (final CategoryModel subCategory : selectedCategory.getCategories())
        {
            final CategoryModel category = commerceCategoryService.getCategoryForCode(subCategory.getCode());
            final CategoryData parentCatData = jnjGTProductCategoryConverter.convert(category);
            subCategories = new ArrayList<>();
            for(final CategoryModel categoryModel : category.getCategories())
            {
                subCategoryData = jnjGTProductCategoryConverter.convert(categoryModel);
                subCategories.add(subCategoryData);

                //Changes for AAOL-7606
                Collections.sort(subCategories, new Comparator<CategoryData>()
                {
                    @Override
                    public int compare(final CategoryData paramT1, final CategoryData paramT2)
                    {
                        final String methodName = METHOD_COMPARE;
                        try{
                            return paramT1.getName().compareTo(paramT2.getName());
                        }catch (Exception e){
                            JnjGTCoreUtil.logErrorMessage("Build categories data", methodName, EXCEPTION + e, currentClass);
                            JnjGTCoreUtil.logInfoMessage("Compare categories params", methodName, paramT1 + "," + paramT2, currentClass);
                            return 0;
                        }
                    }
                });
            }

            dataObject.put(parentCatData,subCategories);
        }
        return dataObject;
    }

    private Map<CategoryData, List<CategoryData>> filterChildData(
            Map<CategoryData, List<CategoryData>> dataObject, List<String> userSelectedFranchise, CategoryModel selectedCategory, String franchise) {

        Map<CategoryData,List<CategoryData>> userSelectedFranchiseMap = new LinkedHashMap<>();

        for (final Entry<CategoryData, List<CategoryData>> subCategoriesData : dataObject.entrySet()) {
            if ((null != subCategoriesData) && (null != subCategoriesData.getKey() && null!=subCategoriesData.getKey().getCode())){
                for (final String string : userSelectedFranchise) {

                    if(selectedCategory.getCode().equalsIgnoreCase("ProductCategories")) {
                        if (subCategoriesData.getKey().getName().equalsIgnoreCase(string)) {
                            userSelectedFranchiseMap.put(subCategoriesData.getKey(), subCategoriesData.getValue());
                        }
                    }else if(franchise.equalsIgnoreCase(string)) {
                        userSelectedFranchiseMap.put(subCategoriesData.getKey(), subCategoriesData.getValue());
                    }

                }
            }
        }
        return userSelectedFranchiseMap;
    }

    private Map<CategoryData, List<CategoryData>> customSortCategoryData(
            Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap) {
        final List<Map.Entry<CategoryData,List<CategoryData>>> entries =
                new ArrayList<>(userSelectedFranchiseMap.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<CategoryData,List<CategoryData>>>() {
            public int compare(Map.Entry<CategoryData,List<CategoryData>> a, Map.Entry<CategoryData,List<CategoryData>> b){
                return (a.getKey().getSequence() > b.getKey().getSequence() ? 1:-1);
            }
        });
        final Map<CategoryData,List<CategoryData>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<CategoryData,List<CategoryData>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static Map<CategoryData, List<CategoryData>> customSortCategoryByName(
            Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap) {
        final List<Map.Entry<CategoryData,List<CategoryData>>> entries =
                new ArrayList<>(userSelectedFranchiseMap.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<CategoryData,List<CategoryData>>>() {
            public int compare(Map.Entry<CategoryData,List<CategoryData>> a, Map.Entry<CategoryData,List<CategoryData>> b){
                return (a.getKey().getName() .compareTo( b.getKey().getName()));
            }
        });
        final Map<CategoryData,List<CategoryData>> sortedMap = new LinkedHashMap<>();
        for (final Map.Entry<CategoryData,List<CategoryData>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    protected void buildCategoryMedia(CategoryModel categoryModel) {
        final String methodName = "buildCategoryMedia()";
        final MediaModel media = new MediaModel();
        media.setCode(categoryModel.getCode());
        media.setCatalogVersion(categoryModel.getCatalogVersion());
        try
        {
            final List<MediaModel> existingMediaList = flexibleSearchService.getModelsByExample(media);
            if(null != existingMediaList)
            {
                categoryModel.setMedias(existingMediaList);
                categoryModel.setLogo(existingMediaList);
                modelService.save(categoryModel);

            }

        }catch(Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage("Media not found for Category with id =" + categoryModel.getCode(),
                    methodName, EXCEPTION + exception, currentClass);
        }

    }

    protected void buildSubCategoriesData(final CategoryModel currentCategory, final Map<String, List<CategoryData>> subCategories,
                                          final boolean isPCM)
    {
        final List<CategoryData> subCategoriesData = new LinkedList<>();
        CategoryData subCategoryData = null;

        for (final CategoryModel subCategory : currentCategory.getCategories())
        {
            subCategoryData = jnjGTProductCategoryConverter.convert(subCategory);


            if (subCategories.keySet().contains(currentCategory.getName()))
            {
                subCategories.get(currentCategory.getName()).add(subCategoryData);
            }
            else
            {
                subCategoriesData.add(subCategoryData);
                subCategories.put(currentCategory.getName(), subCategoriesData);
            }

        }

        sortSubCategoryData(subCategories);
        // Sort the category data to show the categories in alphabetical order
        for (final Map.Entry<String, List<CategoryData>> entry : subCategories.entrySet())
        {
            Collections.sort(entry.getValue(), new Comparator<CategoryData>()
            {
                @Override
                public int compare(final CategoryData paramT1, final CategoryData paramT2)
                {
                    final String methodName = METHOD_COMPARE;
                    try{
                        return paramT1.getName().compareTo(paramT2.getName());
                    }catch (Exception e){
                        JnjGTCoreUtil.logErrorMessage("Build subcategories", methodName, EXCEPTION + e, currentClass);
                        JnjGTCoreUtil.logInfoMessage("Comparing categories params", methodName, paramT1 + "," + paramT2, currentClass);
                        return 0;
                    }
                }
            });
        }
    }


    private List<Breadcrumb> customizeBreadCrumb(List<Breadcrumb> breadCrumbList) {

        final List<Breadcrumb> custombreadCrumbList = new ArrayList<>();
        for(int i=0; i<breadCrumbList.size(); i++) {
            final Breadcrumb breadCrumnb = breadCrumbList.get(i);
            if(i==1) {
                breadCrumnb.setUrl(breadCrumbList.get(0).getUrl());
            }else if(i==3) {
                breadCrumnb.setUrl(breadCrumbList.get(2).getUrl());
            }else {
                breadCrumnb.setUrl(breadCrumbList.get(i).getUrl());
            }
            custombreadCrumbList.add(breadCrumnb);
        }
        return custombreadCrumbList;
    }

    private void sortSubCategoryData(Map<String, List<CategoryData>> subCategories) {

        for (final Map.Entry<String, List<CategoryData>> entry : subCategories.entrySet())
        {
            Collections.sort(entry.getValue(), new Comparator<CategoryData>()
            {
                @Override
                public int compare(final CategoryData paramT1, final CategoryData paramT2)
                {
                    final String methodName = METHOD_COMPARE;
                    try{
                        return paramT1.getName().compareTo(paramT2.getName());
                    }catch (Exception e){
                        JnjGTCoreUtil.logErrorMessage("Build subcategories", methodName, EXCEPTION + e, currentClass);
                        JnjGTCoreUtil.logInfoMessage("Comparing categories params", methodName, paramT1 + "," + paramT2, currentClass);
                        return 0;
                    }
                }
            });
        }

    }

    /**
     * This is used to refine facet's list
     *
     * @param searchPageData
     */
    protected void refineFacetList(final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
    {
        searchPageData.getFacets();
    }

    public String getView(final String view){
        return Jnjb2bbrowseandsearchControllerConstants.ADDON_PREFIX + view;
    }

    protected void setFalgForSpecialityCustomer(final Model model)
    {
        boolean spclCustomer = false;
        final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
        final String[] spclCustGroup =
                StringUtils.split(configurationService.getConfiguration().getString(Jnjb2bCoreConstants.SPECIALITY_CUSTOMER_GROUP), ",");
        if (currentSite.equals(Jnjb2bCoreConstants.CONS))
        {
            final JnJB2BUnitModel jnjB2BUnit = jnjGTCustomerFacade.getCurrentB2bUnit();
            if (Arrays.asList(spclCustGroup).contains(jnjB2BUnit.getCustomerGroup()))
            {
                spclCustomer = true;
            }
        }
        model.addAttribute("specialCustomer", Boolean.toString(spclCustomer));
    }

    protected Boolean isExcelOrPDF(final String downloadType){
        return (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType) || DOWNLOAD_TYPE.PDF.toString().equals(downloadType));
    }

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public Converter<JnJProductModel, JnjGTProductData> getProductConverter() {
        return productConverter;
    }

    public JnjGTProductFacade getJnjGTProductFacade() {
        return jnjGTProductFacade;
    }

    public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
        return jnjCommonFacadeUtil;
    }

    public JnjGTB2BUnitFacade getJnjGTB2BUnitFacade() {
        return jnjGTB2BUnitFacade;
    }

    public JnjGTCustomerFacade getJnjGTCustomerFacade() {
        return jnjGTCustomerFacade;
    }

    public JnJGTProductService getJnjGTProductService() {
        return jnjGTProductService;
    }

    public ProductSearchFacade<ProductData> getProductSearchFacade() {
        return productSearchFacade;
    }

    public CustomerLocationService getCustomerLocationService() {
        return customerLocationService;
    }

    public CustomerFacade getCustomerFacade() {
        return customerFacade;
    }

    public CommerceCategoryService getCommerceCategoryService() {
        return commerceCategoryService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    public void setProductConverter(Converter<JnJProductModel, JnjGTProductData> productConverter) {
        this.productConverter = productConverter;
    }

    public void setJnjCommonFacadeUtil(JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setcMSSiteService(CMSSiteService cMSSiteService) {
        this.cMSSiteService = cMSSiteService;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setJnjGTProductFacade(JnjGTProductFacade jnjGTProductFacade) {
        this.jnjGTProductFacade = jnjGTProductFacade;
    }

    public void setJnjGTB2BUnitFacade(JnjGTB2BUnitFacade jnjGTB2BUnitFacade) {
        this.jnjGTB2BUnitFacade = jnjGTB2BUnitFacade;
    }

    public void setJnjGTCustomerFacade(JnjGTCustomerFacade jnjGTCustomerFacade) {
        this.jnjGTCustomerFacade = jnjGTCustomerFacade;
    }

    public void setJnjGTProductService(JnJGTProductService jnjGTProductService) {
        this.jnjGTProductService = jnjGTProductService;
    }

    public void setProductSearchFacade(ProductSearchFacade<ProductData> productSearchFacade) {
        this.productSearchFacade = productSearchFacade;
    }

    public void setCommerceCategoryService(CommerceCategoryService commerceCategoryService) {
        this.commerceCategoryService = commerceCategoryService;
    }

    public UrlResolver<CategoryModel> getHierarchyModelUrlResolver() {
        return hierarchyModelUrlResolver;
    }

    public void setHierarchyModelUrlResolver(UrlResolver<CategoryModel> hierarchyModelUrlResolver) {
        this.hierarchyModelUrlResolver = hierarchyModelUrlResolver;
    }

    public void setCustomerLocationService(CustomerLocationService customerLocationService) {
        this.customerLocationService = customerLocationService;
    }

    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public Converter<CategoryModel, CategoryData> getJnjGTProductCategoryConverter() {
        return jnjGTProductCategoryConverter;
    }

    public void setJnjGTProductCategoryConverter(Converter<CategoryModel, CategoryData> jnjGTProductCategoryConverter) {
        this.jnjGTProductCategoryConverter = jnjGTProductCategoryConverter;
    }

    public HierarchyBreadCrumbBuilder getHierarchyBreadcrumbBuilder() {
        return hierarchyBreadcrumbBuilder;
    }

    public void setHierarchyBreadcrumbBuilder(HierarchyBreadCrumbBuilder hierarchyBreadcrumbBuilder) {
        this.hierarchyBreadcrumbBuilder = hierarchyBreadcrumbBuilder;
    }



}
