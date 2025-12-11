/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.b2b.browseandsearch.controllers.pages;

import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.catalog.CatalogVersionService;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.impl.SearchBreadcrumbBuilder;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
//import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.controllers.pages.CategoryPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTExportCatalogEmailProcessModel;


/**
 * Controller for Category and Product List pages.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCategoryPageController extends CategoryPageController
{
	
    @Autowired
    protected ModelService modelService;
    @Autowired
	protected UserService userService;
    
    @Autowired
    protected FlexibleSearchService flexibleSearchService;
       
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
	
	protected Map<String, String> exportCatalogData = new HashMap<String, String>();
	/**
	 * The Instance of <code>SessionService</code>.
	 */
	@Resource(name = "sessionService")
	protected SessionService sessionService;
	
	@Autowired
	protected Converter<JnJProductModel, JnjGTProductData> productConverter;
	
	@Resource(name = "jnjGTProductFacade")
	protected JnjGTProductFacade jnjGTProductFacade;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name = "GTB2BUnitFacade")
	JnjGTB2BUnitFacade jnjGTB2BUnitFacade;

	@Resource(name="GTCustomerFacade")
	JnjGTCustomerFacade jnjGTCustomerFacade;
	
    @Resource(name = "productService")
    protected JnJGTProductService jnjGTProductService;

	@Resource(name = "productSearchFacade")
	protected ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;

	@Resource(name = "categoryConverter")
	protected Converter<CategoryModel, CategoryData> categoryConverter;

	@Resource(name = "searchBreadcrumbBuilder")
	protected SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "categoryModelUrlResolver")
	protected UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Resource(name = "customerLocationService")
	protected CustomerLocationService customerLocationService;

	@Resource(name = "customerFacade")
	protected CustomerFacade customerFacade;
	
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

	public SearchBreadcrumbBuilder getSearchBreadcrumbBuilder() {
		return searchBreadcrumbBuilder;
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

	public Converter<CategoryModel, CategoryData> getCategoryConverter() {
		return categoryConverter;
	}

	public UrlResolver<CategoryModel> getCategoryModelUrlResolver() {
		return categoryModelUrlResolver;
	}
	protected static final String TO_EMAIL = "toEmail";
	/**
	 * Default current page value.
	 */
	protected static final int DEFAULT_PAGE = 0;

	/**
	 * Enum to store download types.
	 */
	protected enum DOWNLOAD_TYPE
	{
		PDF, EXCEL, NONE;
	}
	@GetMapping(CATEGORY_CODE_PATH_VARIABLE_PATTERN)
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
		final String redirection = checkRequestUrl(request, response, categoryModelUrlResolver.resolve(category));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}
 
		model.addAttribute("categoryCode", categoryCode);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		final Boolean displayProducts = category.getDisplayProducts();
		String view = null;
		model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
		model.addAttribute(USER_TYPE, sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE));
		final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
		
		if (Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite))
		{
			model.addAttribute("mddExportFileUrl", jnjGTProductService.getMediaURLForMDDExport());
			model.addAttribute("mddPdfExportFileUrl", jnjGTProductService.getMediaURLForPDFMDDExport());
			model.addAttribute("isMddSite", Boolean.valueOf(Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite)));
		}
		if (displayProducts.booleanValue())
		{
			boolean resultLimitExceeded = false;
			final String downloadType = form.getDownloadType();
			final int finalPageSize = (form.isShowMore()) ? form.getPageSize(currentSite) * form.getShowMoreCounter() : form
					.getPageSize(currentSite);
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(searchQuery);
			final SearchStateData searchState = new SearchStateData();
			searchState.setQuery(searchQueryData);

		int totalResults = finalPageSize;
			if (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType) || DOWNLOAD_TYPE.PDF.toString().equals(downloadType))
			{
				if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()))
				{
					totalResults = Integer.parseInt(form.getTotalNumberOfResults());
					final int resultLimit = Integer.parseInt(Config
							.getParameter(Jnjb2bbrowseandsearchConstants.PLP.PLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));
					if (resultLimit > 0)
					{
						if (totalResults >= resultLimit)
						{
							totalResults = resultLimit;
							resultLimitExceeded = true;
						}
					}
				}
			}
			final PageableData pageableData = createPageableData(DEFAULT_PAGE, finalPageSize, form.getSortCode(), ShowMode.Page);
			
			searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
			
			refineFacetList(searchPageData);
			
			//model.addAttribute("isMddSite", Boolean.valueOf(Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite)));
			populateModel(model, searchPageData, showMode);
			
			if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType))
			{
				model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(categoryCode, searchPageData));
				model.addAttribute("pageType", PageType.CATEGORY.name());
				model.addAttribute("searchSortForm", form);
				model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
				model.addAttribute("showMoreCounter", "1");

				model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));

				updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
				final RequestContextData requestContextData = getRequestContextData(request);
				requestContextData.setCategory(category);
				requestContextData.setSearch(searchPageData);

				if (searchQueryData.getValue() != null)
				{
					model.addAttribute("metaRobots", "no-index,follow");
				}
				{
					model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
					view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductListPage;
				}
			}
			else
			{
				model.addAttribute("currentAccount", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
						.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

				model.addAttribute(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG, Boolean.FALSE);
				model.addAttribute("resultLimitExceeded", resultLimitExceeded);
				
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
				
				
				return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? CATEGORY_SEARCH_RESULT_PDF
						: CATEGORY_SEARCH_RESULT_EXCEL;
			}
		}
		else
		{
			model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("category.catalog"), null)));
		/*	if (currentSite.equals(JnjPCMCoreConstants.PCM))
			{
				setUpCategoryLevelData(model, category, true);
				view = PcmControllerConstants.Views.Pages.Category.CategoryPage;
			}
			else*/
			{
		
				setUpCategoryLevelData(model, category, false);
				view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.CategoryPage;
			}
			updatePageTitle(category, null, model);
		}
		storeCmsPageInModel(model, categoryPage);
		storeContinueUrl(request);
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		setFalgForSpecialityCustomer(model);
		 final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);
			//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
		
		return getView(view);
	}

	@PostMapping(CATEGORY_PRODUCT_LIST_DOWNLOAD)
	@ResponseBody
	public Boolean category()
	{
		final boolean result = jnjGTProductFacade.sendEmailForAllProductsOfCatalog();
		return Boolean.valueOf(result);
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
		try
		{
			return getCmsPageService().getPageForCategory(category);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			// Ignore
		}
		return null;
	}


	protected void setUpCategoryLevelData(final Model model, final CategoryModel selectedCategory, final boolean isPCM)
	{
		final Map<String, List<CategoryData>> subCategories = new LinkedHashMap<String, List<CategoryData>>();
        buildGTCategoriesData(selectedCategory,model);
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
			Model model) {
		// TODO Auto-generated method stub
		Map<String, List<CategoryData>> subCategories = null;
		//final List<CategoryData> subCategoriesData = null;
		Map<String,HashMap<String, List<CategoryData>>> dataObject = new LinkedHashMap<String,HashMap<String, List<CategoryData>>>();
		Map<String,HashMap<String, List<CategoryData>>> userSelectedFranchiseMap = new LinkedHashMap<String,HashMap<String, List<CategoryData>>>();
		List<String> userSelectedFranchise = new ArrayList<String>();
		/*Added for AAOL-5183 */
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		List<CategoryModel> allowedFranchise = new ArrayList<CategoryModel>();
		if(CollectionUtils.isNotEmpty(currentCustomer.getAllowedFranchise()))
		{
			 allowedFranchise = currentCustomer.getAllowedFranchise();
		}
		else
		{
			 allowedFranchise = jnjGTCustomerFacade.getAllFranchise();
		}
		for (CategoryModel categoryModel : allowedFranchise) {
			userSelectedFranchise.add(categoryModel.getName());
		}
		
		CategoryData subCategoryData = new CategoryData();
		for (final CategoryModel subCategory : selectedCategory.getCategories())
		{
			final CategoryModel category = commerceCategoryService.getCategoryForCode(subCategory.getCode());
			subCategories = new HashMap<String, List<CategoryData>>();
			for(final CategoryModel categoryModel : category.getCategories())
			{
				//buildCategoryMedia(categoryModel);
				List<CategoryData> subCategoriesData = new ArrayList<CategoryData>();
				subCategoryData = categoryConverter.convert(categoryModel);
				subCategoriesData.add(subCategoryData);
				subCategories.put(categoryModel.getName(), subCategoriesData);
			
				
			}
			
			dataObject.put(subCategory.getName(), (HashMap<String, List<CategoryData>>) subCategories);
		}
		
		/*Added for AAOL-5183*/
		for (Entry<String, HashMap<String, List<CategoryData>>> subCategoriesData : dataObject.entrySet()) {
			if ((null != subCategoriesData) && (null != subCategoriesData.getKey())){
				for (String string : userSelectedFranchise) {
					if (subCategoriesData.getKey().equalsIgnoreCase(string)) {
						userSelectedFranchiseMap.put(subCategoriesData.getKey(), subCategoriesData.getValue());
					}
				}
			}
		}
		
		for (final Map.Entry<String, List<CategoryData>> entry : subCategories.entrySet())
		{
			Collections.sort(entry.getValue(), new Comparator<CategoryData>()
			{
				@Override
				public int compare(final CategoryData paramT1, final CategoryData paramT2)
				{
					try{
						return paramT1.getName().compareTo(paramT2.getName());
					}catch (Exception e){
						System.out.println(paramT1 + "," + paramT2);
						if (paramT1 != null)
							System.out.println("paramT1.getName=" + paramT1.getName());	
						if (paramT2 != null)
							System.out.println("paramT2.getName=" + paramT2.getName());							
						return 0;
					}
				}
			});
		}
		
		//Map<String,HashMap<String, List<CategoryData>>> dataObject1 = new TreeMap<String,HashMap<String, List<CategoryData>>>(dataObject);
		/*Changed for AAOL-5183*/
		model.addAttribute("subCategoriesDatas", userSelectedFranchiseMap);
        
	}


	protected void buildCategoryMedia(CategoryModel categoryModel) {
		MediaModel media = new MediaModel();
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
			LOG.info("Media not found for Category with id =" + categoryModel.getCode());
		}
		
	}

	protected void buildSubCategoriesData(final CategoryModel currentCategory, final Map<String, List<CategoryData>> subCategories,
			final boolean isPCM)
	{
		final List<CategoryData> subCategoriesData = new LinkedList<CategoryData>();
		CategoryData subCategoryData = new CategoryData();

		for (final CategoryModel subCategory : currentCategory.getCategories())
		{
			subCategoryData = categoryConverter.convert(subCategory);

			// If the site is PCM then we need to add only the active categories i.e. categories with productPresentForTheCategory flag as true
			if (!isPCM
					|| (isPCM && subCategory.getProductPresentForTheCategory() != null && subCategory
							.getProductPresentForTheCategory().booleanValue()))
			{
				if (subCategories.keySet().contains(currentCategory.getName()))
				{
					subCategories.get(currentCategory.getName()).add(subCategoryData);
				}
				else
				{
					subCategoriesData.add(subCategoryData);
					subCategories.put(currentCategory.getName(), subCategoriesData);
				}
			} else {
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
		}

		// Sort the category data to show the categories in alphabetical order
		for (final Map.Entry<String, List<CategoryData>> entry : subCategories.entrySet())
		{
			Collections.sort(entry.getValue(), new Comparator<CategoryData>()
			{
				@Override
				public int compare(final CategoryData paramT1, final CategoryData paramT2)
				{
					try{
						return paramT1.getName().compareTo(paramT2.getName());
					}catch (Exception e){
						System.out.println(paramT1 + "," + paramT2);
						if (paramT1 != null)
							System.out.println("paramT1.getName=" + paramT1.getName());	
						if (paramT2 != null)
							System.out.println("paramT2.getName=" + paramT2.getName());							
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
		final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
		if (CollectionUtils.isNotEmpty(facets))
		{
			/*for (final FacetData facetData : facets)
			{
				if (facetData.getCode().equals(JnjPCMCoreConstants.PLP.LEVELONE_FACET))
				{
					facets.remove(facetData);
					break;
				}
			}*/
		}
	}
/*	public Converter<JnJProductModel, JnjGTProductData> getProductConverter()
	{
		return productConverter;
	}
	
	*//**
	 * @param exportCatalogData
	 *           the exportCatalogData to set
	 *//*
	public void setExportCatalogData(final Map<String, String> exportCatalogData)
	{
		this.exportCatalogData = exportCatalogData;
	}
	
	*//**
	 * This method is used to create the excel file which will be send as the attachment to the user.
	 * 
	 * @param jnjNAExportCatalogEmailProcessModel
	 * @param exportCatalogData
	 *//*
	protected void createExcelFile(
			final Map<String, String> exportCatalogData)
	{
		final JnjGTExportCatalogEmailProcessModel jnjNAExportCatalogEmailProcessModel = null;
		final DateFormat df1 = new SimpleDateFormat("ddMMMyy_HHmmss");
		df1.setTimeZone(TimeZone.getTimeZone("EST"));
		List<JnjGTProductData> catalogProductsData = null;
		String fileNameStr = null;
		
			final List<JnJProductModel> catalogproducts = jnjGTProductService.getProductsForCategory(Jnjb2bbrowseandsearchConstants.MDD,
					jnjGTCustomerFacade.getCurrentB2bUnit().getPk().toString());

			LOG.info("Total number of products models found for " + jnjGTCustomerFacade.getCurrentB2bUnit() + " are " + catalogproducts.size());
			fileNameStr = "Consumer_Product_Catalog";
			catalogProductsData = Converters.convertAll(catalogproducts, getProductConverter());
			LOG.info("Total number of products data found after conversion, for " + jnjGTCustomerFacade.getCurrentB2bUnit() + " are "
					+ catalogProductsData.size());
		
		final String filepath = Config.getParameter(Jnjb2bbrowseandsearchConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY);
		FileOutputStream fileOut = null;
		try
		{
			String fileName = null;
			if (jnjGTCustomerFacade.getCurrentB2bUnit().equals(Jnjb2bbrowseandsearchConstants.MDD))
			{
				final File[] files = (new File(filepath)).listFiles();
				Arrays.sort(files, new Comparator<File>()
				{
					@Override
					public int compare(final File f1, final File f2)
					{
						return Long.valueOf(f2.lastModified()).compareTo(Long.valueOf(f1.lastModified()));
					}
				});
				for (int index = 0; index < files.length; index++)
				{
					if (files[index].getName().contains(Jnjb2bbrowseandsearchConstants.MDD))
					{
						fileName = files[index].getName();
						break;
					}
				}
				exportCatalogData.put(Jnjb2bbrowseandsearchConstants.DELETE_FILE, Boolean.FALSE.toString());
			}
			else
			{
				try
				{
					fileName = fileNameStr + "-" + jnjNAExportCatalogEmailProcessModel.getExportCatalogData().get(TO_EMAIL) + "-"
							+ jnjGTCustomerFacade.getCurrentB2bUnit().getUid() + "-" + df1.format(new Date()) + ".xls";
					fileOut = new FileOutputStream(filepath + File.separator + fileName);
					jnjGTProductService.createCONSExportFile(catalogProductsData, jnjNAExportCatalogEmailProcessModel
							.getCurrentB2bUnit().getName(), fileName);
					catalogProductsData = null;
				}
				catch (final FileNotFoundException expection)
				{
					LOG.error("There was an error while trying to find the file", expection);
				}
				finally
				{
					try
					{
						fileOut.flush();
						fileOut.close();
					}
					catch (final Exception expection)
					{
						LOG.error("There was an error while trying to close the file output stream", expection);
					}
				}

			}

			exportCatalogData.put(Jnjb2bbrowseandsearchConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME, fileName);

		}
		catch (final Exception expection)
		{
			LOG.error("There was an error while trying to find the file", expection);
		}

	}
*/
	
	public String getView(final String view){
        return Jnjb2bbrowseandsearchControllerConstants.ADDON_PREFIX + view;
 }
	
	protected void setFalgForSpecialityCustomer(final Model model)
	{
		boolean spclCustomer = false;
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final String[] spclCustGroup = StringUtils.split(Config.getParameter(Jnjb2bCoreConstants.SPECIALITY_CUSTOMER_GROUP), ",");
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

}
