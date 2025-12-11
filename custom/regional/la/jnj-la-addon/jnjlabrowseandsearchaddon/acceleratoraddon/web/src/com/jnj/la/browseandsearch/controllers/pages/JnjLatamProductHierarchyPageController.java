/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.la.browseandsearch.controllers.pages;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.browseandsearch.controllers.pages.JnjGTProductHierarchyPageController;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.facades.customer.impl.JnjLatamCustomerFacadeImpl;
import com.jnj.la.browseandsearch.controllers.JnjlabrowseandsearchaddonControllerConstants;
import com.jnj.la.browseandsearch.util.JnjLatamBreadcrumbUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class JnjLatamProductHierarchyPageController extends JnjGTProductHierarchyPageController {

    protected static final String LATAM_CATEGORY_SEARCH_RESULT_PDF = "latamproductSearchResultsPdf";
    protected static final String LATAM_CATEGORY_SEARCH_RESULT_EXCEL = "latamproductSearchResultsExcel";
    private static final String ROOT_CATEGORY_CODE = "Categories";
    @Autowired
    private MediaService mediaService;
    @Autowired
    private JnjLatamCustomerFacadeImpl jnjLatamCustomerFacadeImpl;

    /**
     * Custom sort method to sort by category name
     *
     * @param userSelectedFranchiseMap
     * @return
     */
    private static Map<CategoryData, List<CategoryData>> customSortCategoryByName(
            Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap) {
        final List<Map.Entry<CategoryData, List<CategoryData>>> entries =
                new ArrayList<>(userSelectedFranchiseMap.entrySet());

        Collections.sort(entries, Comparator.comparing(a -> a.getKey().getName()));
        final Map<CategoryData, List<CategoryData>> sortedMap = new LinkedHashMap<>();
        for (final Map.Entry<CategoryData, List<CategoryData>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Custom filtering logic for the subcategories data
     *
     * @param subCategoriesData
     * @param franchise
     * @param userSelectedFranchise
     * @param selectedCategory
     * @param userSelectedFranchiseMap
     */
    private static void applyFilterLogic(final Map.Entry<CategoryData, List<CategoryData>> subCategoriesData, final String franchise, final List<String> userSelectedFranchise, final CategoryModel selectedCategory, final Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap) {
        for (final String string : userSelectedFranchise) {
            if (selectedCategory.getCode().equalsIgnoreCase(ROOT_CATEGORY_CODE)) {
                if (subCategoriesData.getKey().getName().equalsIgnoreCase(string)) {
                    userSelectedFranchiseMap.put(subCategoriesData.getKey(), subCategoriesData.getValue());
                }
            } else if (franchise.equalsIgnoreCase(string)) {
                userSelectedFranchiseMap.put(subCategoriesData.getKey(), subCategoriesData.getValue());
            }
        }
    }

    /**
     * Custom sort method to sort by sequence
     *
     * @param userSelectedFranchiseMap
     * @return
     */
    private static Map<CategoryData, List<CategoryData>> customSortCategoryData(
            Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap) {
        final List<Map.Entry<CategoryData, List<CategoryData>>> entries =
                new ArrayList<>(userSelectedFranchiseMap.entrySet());

        Collections.sort(entries, (a, b) -> (a.getKey().getSequence() > b.getKey().getSequence() ? 1 : -1));
        final Map<CategoryData, List<CategoryData>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<CategoryData, List<CategoryData>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Returns boolean value true if a category has products under it
     *
     * @param category
     * @return
     */
    private static boolean isProductPresentForTheCategory(CategoryModel category) {
        return category.getProductPresentForTheCategory() != null && category
                .getProductPresentForTheCategory().booleanValue();
    }

    /**
     * Filter child level category data based of franchise
     *
     * @param dataObject
     * @param userSelectedFranchise
     * @param selectedCategory
     * @param franchise
     * @return
     */
    private static Map<CategoryData, Map<CategoryData, List<CategoryData>>> filterChildData(
            Map<CategoryData, Map<CategoryData, List<CategoryData>>> dataObject, List<String> userSelectedFranchise, CategoryModel selectedCategory, String franchise) {

        Map<CategoryData, Map<CategoryData, List<CategoryData>>> filteredMap = new LinkedHashMap<>();

        for (Map.Entry<CategoryData, Map<CategoryData, List<CategoryData>>> level0 : dataObject.entrySet()) {
            Map<CategoryData, List<CategoryData>> userSelectedFranchiseMap = new LinkedHashMap<>();
            for (final Map.Entry<CategoryData, List<CategoryData>> subCategoriesData : level0.getValue().entrySet()) {
                if ((null != subCategoriesData) && (null != subCategoriesData.getKey() && null != subCategoriesData.getKey().getCode())) {
                    applyFilterLogic(subCategoriesData, franchise, userSelectedFranchise, selectedCategory, userSelectedFranchiseMap);
                }
            }
            filteredMap.put(level0.getKey(), userSelectedFranchiseMap);
        }
        return filteredMap;
    }

    private static Boolean isShowAllProducts(final CategoryModel category, final HttpServletRequest request) {
        Boolean showAllProducts = false;
        if (null != request.getParameter("showAllProducts")) {
            showAllProducts = Boolean.valueOf(request.getParameter("showAllProducts"));
        } else {
            showAllProducts = category.getDisplayProducts();
            if (CollectionUtils.isNotEmpty(category.getCategories())) {
                showAllProducts = false;
            }
        }
        return showAllProducts;
    }

    /**
     * Prepare page data for PLP
     *
     * @param form
     * @param model
     * @param category
     * @param searchQueryData
     * @param searchPageData
     * @param request
     * @return
     */
    private String getPLPView(final JnjGTSearchSortForm form, final Model model, final CategoryModel category, final SearchQueryData searchQueryData, final ProductCategorySearchPageData<SearchStateData,
            ProductData, CategoryData> searchPageData, final HttpServletRequest request) {
        final List<Breadcrumb> breadCrumbList = JnjLatamBreadcrumbUtil.customizeBreadCrumb(hierarchyBreadcrumbBuilder.getBreadcrumbs(category.getCode(), searchPageData));
        model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadCrumbList);
        model.addAttribute(Jnjlab2bcoreConstants.PAGE_TYPE, PageType.CATEGORY.name());

        //Changes for AAOL-7654
        setDisplayResult(form,searchPageData.getPagination().getTotalNumberOfResults(),searchPageData.getPagination().getNumberOfPages());

        model.addAttribute(Jnjlab2bcoreConstants.SEARCH_SORT_FORM, form);
        model.addAttribute(Jnjlab2bcoreConstants.SHOW_MORE_COUNTER, "1");
        model.addAttribute(Jnjlab2bcoreConstants.IS_INT_AFF, jnjGTB2BUnitFacade.isCustomerInternationalAff());

        updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
        final RequestContextData requestContextData = getRequestContextData(request);
        requestContextData.setCategory(category);
        requestContextData.setSearch(searchPageData);

        if (searchQueryData.getValue() != null) {
            model.addAttribute(Jnjlab2bcoreConstants.META_ROBOTS, Jnjlab2bcoreConstants.META_ROBOTS_VALUE);
        }
        model.addAttribute(Jnjlab2bcoreConstants.GROUPS,
                configurationService.getConfiguration().getString(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
        return Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductListPage;
    }

    /**
     * Prepare page data for PDF/XML view
     *
     * @param model
     * @param resultLimitExceeded
     * @param downloadType
     * @return
     */
    private String getPDFView(final Model model, final boolean resultLimitExceeded, final String downloadType) {

        model.addAttribute(Jnjlab2bcoreConstants.CURRENT_ACCOUNT,
                (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

        model.addAttribute(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG, Boolean.FALSE);
        model.addAttribute(Jnjlab2bcoreConstants.RESULT_EXCEEDED, resultLimitExceeded);

        final String mediaDirBase =
                configurationService.getConfiguration().getString(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
        final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite()
                .getContentCatalogs().get(0).getActiveCatalogVersion();

        model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, Jnjlab2bcoreConstants.EPIC_EMAIL_LOGO)));

        final List<ContentCatalogModel> catalogList = getCmsSiteService().getCurrentSite().getContentCatalogs();
        if (CollectionUtils.isNotEmpty(catalogList)) {
            final MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                    Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
            final MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogList.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
                    Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
            if (mediaModel1 != null) {
                model.addAttribute(Jnjlab2bcoreConstants.LOGO_URL, mediaService.getStreamFromMedia(mediaModel1));
            }
            if (mediaModel2 != null) {
                model.addAttribute(Jnjlab2bcoreConstants.LOGO_URL2, mediaService.getStreamFromMedia(mediaModel2));
            }
        }

        return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? LATAM_CATEGORY_SEARCH_RESULT_PDF : LATAM_CATEGORY_SEARCH_RESULT_EXCEL;
    }

    /**
     * Prepare page data for Product Hierarchy Page
     *
     * @param model
     * @param category
     * @param franchise
     * @param showMode
     * @param searchQuery
     * @param form
     * @return
     */
    private String getCategoryHierarchyView(final Model model, final CategoryModel category, final String franchise, final ShowMode showMode, final String searchQuery, final JnjGTSearchSortForm form) {
        final SearchQueryData searchQueryData = new SearchQueryData();
        searchQueryData.setValue(searchQuery);

        final ProductCategorySearchPageData<SearchStateData,
                ProductData, CategoryData> searchPageData = populateSearchPageData(form, model, showMode, category.getCode(), searchQueryData);

        final List<Breadcrumb> breadCrumbList = JnjLatamBreadcrumbUtil.customizeBreadCrumb(hierarchyBreadcrumbBuilder.getBreadcrumbs(category.getCode(), searchPageData));
        model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadCrumbList);

        setUpCategoryLevelData(model, category, false, franchise);
        updatePageTitle(category, null, model);
        return Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductHierarchyPage;
    }

    /**
     * Set site specific data on Model
     *
     * @param model
     */
    private void setSiteData(final Model model) {
        final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);

        if (null != showChangeAccountLink && (Boolean) showChangeAccountLink) {
            model.addAttribute(Jnjlab2bcoreConstants.SHOW_CHANGE_ACC_LINK, Boolean.TRUE);
        }
        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
        if (Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite)) {
            model.addAttribute(Jnjlab2bcoreConstants.IS_MDD_SITE, Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite));
        }
        model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
    }

    /**
     * populate child category data
     *
     * @param category
     * @param subCategories
     */
    private void checkAndPopulateData(final CategoryModel category, List<CategoryData> subCategories) {
        for (final CategoryModel categoryModel : category.getCategories()) {
            if (isProductPresentForTheCategory(categoryModel)) {
                final CategoryData subCategoryData = jnjGTProductCategoryConverter.convert(categoryModel);
                subCategories.add(subCategoryData);
                subCategories.sort(Comparator.comparing(CategoryData::getName,
                        Comparator.nullsFirst(Comparator.naturalOrder())));
            }
        }
    }

    /**
     * Method to populate data based on category code - either plp or product hierarchy page
     *
     * @param categoryCode
     * @param showMode
     * @param searchQuery
     * @param form
     * @param model
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     * @throws CMSItemNotFoundException
     */
    @Override
    protected String displayCategoriesAndProducts(final String categoryCode, final ShowMode showMode, final String searchQuery, final JnjGTSearchSortForm form,
                                                  final Model model, final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException, CMSItemNotFoundException {
        final CategoryModel category = commerceCategoryService.getCategoryForCode(categoryCode);
        Boolean showAllProducts = isShowAllProducts(category, request);
        final String redirection = checkRequestUrl(request, response, hierarchyModelUrlResolver.resolve(category));
        if (StringUtils.isNotEmpty(redirection)) {
            return redirection;
        }

        final String hierarchyTree = request.getServletPath();
        final String[] categoryTree = hierarchyTree.substring(1).split("/");
        final String franchise = categoryTree[2].replace("-", " ");

        model.addAttribute(Jnjlab2bcoreConstants.CATEGORY_CODE, categoryCode);
        final CategoryPageModel categoryPage = getCategoryPage(category);
        String view = null;
        model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
        model.addAttribute(USER_TYPE, sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE));


        if (showAllProducts) {
            boolean resultLimitExceeded = false;
            final String downloadType = form.getDownloadType();
            final SearchQueryData searchQueryData = new SearchQueryData();
            searchQueryData.setValue(searchQuery);

            if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()) && isExcelOrPDF(downloadType)) {
                final int totalResults = Integer.parseInt(form.getTotalNumberOfResults());
                final int resultLimit =
                        Integer.parseInt(configurationService.getConfiguration()
                                .getString(Jnjb2bbrowseandsearchConstants.PLP.PLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));
                if (resultLimit > 0 && totalResults >= resultLimit) {
                    resultLimitExceeded = true;
                }
            }
            //Changes for AAOL-7654
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                form.setPageNumber(0);
                form.setPageSize(configurationService.getConfiguration().getInt(Jnjlab2bcoreConstants.MediaFolder.LATAM_CATALOG_RESULT_PAGE_LIMIT, 10));
            }
            final ProductCategorySearchPageData<SearchStateData,
                    ProductData, CategoryData> searchPageData = populateSearchPageData(form, model, showMode, categoryCode, searchQueryData);


            if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType)) {
                view = getPLPView(form, model, category, searchQueryData, searchPageData, request);
            } else {
                return getPDFView(model, resultLimitExceeded, downloadType);
            }
        } else {

            view = getCategoryHierarchyView(model, category, franchise, showMode, searchQuery, form);
        }
        storeCmsPageInModel(model, categoryPage);
        storeContinueUrl(request);
        final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
        final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
        setUpMetaData(model, metaKeywords, metaDescription);
        setFalgForSpecialityCustomer(model);
        setSiteData(model);
        return getView(view);
    }

    /**
     * This method sets the category level data
     *
     * @param model
     * @param selectedCategory
     * @param isPCM
     * @param franchise
     */
    @Override
    protected void setUpCategoryLevelData(final Model model, final CategoryModel selectedCategory, final boolean isPCM, String franchise) {
        final Map<String, List<CategoryData>> subCategories = new LinkedHashMap<>();
        buildGTCategoriesData(selectedCategory, model, franchise);
        final CategoryModel rootCategory = commerceCategoryService.getCategoryForCode(Jnjb2bbrowseandsearchConstants.PLP.ROOT_CATEGORY_CODE);
        if (rootCategory.getCategories().contains(selectedCategory)) {
            subCategories.remove(selectedCategory.getName());
        }

        model.addAttribute(Jnjlab2bcoreConstants.CURRENT_ACCOUNT, (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
                .getCurrentB2bUnit().getUid() : StringUtils.EMPTY);
        model.addAttribute(Jnjlab2bcoreConstants.USER_EMAIL, (jnjGTCustomerFacade.getCurrentCustomer() != null) ? jnjGTCustomerFacade
                .getCurrentCustomer().getEmail() : StringUtils.EMPTY);
    }

    /**
     * This method builds the level wise category structure
     *
     * @param selectedCategory
     * @param model
     * @param franchise
     */
    @Override
    protected void buildGTCategoriesData(CategoryModel selectedCategory,
                                         Model model, String franchise) {

        Map<CategoryData, Map<CategoryData, List<CategoryData>>> dataObject = new LinkedHashMap<>();
        Map<CategoryData, Map<CategoryData, List<CategoryData>>> userSelectedFranchiseMap = null;
        final List<String> userSelectedFranchise = new ArrayList<>();
        final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
        List<CategoryModel> allowedFranchise = null;
        if (CollectionUtils.isNotEmpty(currentCustomer.getAllowedFranchise())) {
            allowedFranchise = currentCustomer.getAllowedFranchise();
        } else {
            allowedFranchise = jnjLatamCustomerFacadeImpl.getAllFranchise();
        }
        for (final CategoryModel categoryModel : allowedFranchise) {
            userSelectedFranchise.add(categoryModel.getName());
        }

        if (ROOT_CATEGORY_CODE.equalsIgnoreCase(selectedCategory.getCode())) {
            for (final CategoryModel subCategory : selectedCategory.getCategories()) {
                if (isProductPresentForTheCategory(subCategory)) {
                    final CategoryModel categoryL0 = commerceCategoryService.getCategoryForCode(subCategory.getCode());
                    final CategoryData categoryData = jnjGTProductCategoryConverter.convert(categoryL0);
                    dataObject.put(categoryData, populateLatamChildCategoriesData(categoryL0));
                }
            }
        } else {
            final CategoryModel categoryL0 = commerceCategoryService.getCategoryForCode(selectedCategory.getCode());
            final CategoryData categoryData = jnjGTProductCategoryConverter.convert(categoryL0);
            dataObject.put(categoryData, populateLatamChildCategoriesData(categoryL0));
        }

        userSelectedFranchiseMap = filterChildData(dataObject, userSelectedFranchise, selectedCategory, franchise);
        //Changes for AAOL-7606
        Map<CategoryData, Map<CategoryData, List<CategoryData>>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<CategoryData, Map<CategoryData, List<CategoryData>>> mapEntry : userSelectedFranchiseMap.entrySet()) {
            if ((ROOT_CATEGORY_CODE).equalsIgnoreCase(selectedCategory.getCode())) {
                sortedMap.put(mapEntry.getKey(), customSortCategoryData(mapEntry.getValue()));
            } else {
                sortedMap.put(mapEntry.getKey(), customSortCategoryByName(mapEntry.getValue()));
            }
        }
        model.addAttribute(Jnjlab2bcoreConstants.SUB_CATEGORIES_DATAS, sortedMap);
    }

    @Override
    public String getView(final String view) {
        return JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + view;
    }

    /**
     * This method returns search page data
     *
     * @param form
     * @param model
     * @param showMode
     * @param categoryCode
     * @param searchQueryData
     * @return
     */
    private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> populateSearchPageData(
            JnjGTSearchSortForm form, Model model, ShowMode showMode, String categoryCode, SearchQueryData searchQueryData) {
        ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;

        final SearchStateData searchState = new SearchStateData();
        searchState.setQuery(searchQueryData);
        //Changes for AAOL-7654
        final String downloadType = form.getDownloadType();
        JnjPageableData pageableData = null;

        if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType)) {
            pageableData = createJnjPageableData(form.getPageNumber(), form.getPageSize(), form.getSortCode(), ShowMode.Page);
            searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
        } else {
            pageableData = createJnjPageableData(form.getPageNumber(), form.getFullCount(), form.getSortCode(), ShowMode.Page);
            searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
        }


        refineFacetList(searchPageData);

        populateModel(model, searchPageData, showMode);
        return searchPageData;
    }

    /**
     * Prepare Child level data for selected category
     *
     * @param selectedCategory
     * @return
     */
    private Map<CategoryData, List<CategoryData>> populateLatamChildCategoriesData(CategoryModel selectedCategory) {
        final Map<CategoryData, List<CategoryData>> dataObject = new LinkedHashMap<>();
        List<CategoryData> subCategories = null;
        for (final CategoryModel subCategory : selectedCategory.getCategories()) {
            if (isProductPresentForTheCategory(subCategory)) {
                final CategoryModel category = commerceCategoryService.getCategoryForCode(subCategory.getCode());
                final CategoryData parentCatData = jnjGTProductCategoryConverter.convert(category);
                subCategories = new ArrayList<>();
                checkAndPopulateData(category, subCategories);
                dataObject.put(parentCatData, subCategories);
            }
        }
        return dataObject;
    }


}