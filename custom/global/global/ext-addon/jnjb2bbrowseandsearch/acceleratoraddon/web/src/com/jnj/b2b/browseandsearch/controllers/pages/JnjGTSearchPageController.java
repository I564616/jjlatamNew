/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.browseandsearch.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.catalog.CatalogVersionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
//import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.SearchPageController;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.impl.SearchBreadcrumbBuilder;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.b2b.storefront.util.XSSFilterUtil;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;


/**
 * Controller class responsible for handling requests for:
 * <ul>
 * <li>Search Auto-complete</li>
 * <li>Product Search</li>
 * <li>Search result: Sort, Show More and Download</li>
 * 
 * @author Accenture
 * @version 1.0
 */


public class JnjGTSearchPageController extends SearchPageController
{
	/**
	 * Constant enum to store document type for download.
	 */
	public enum DOWNLOAD_TYPE
	{
		PDF, EXCEL, NONE;
	}

	private static final Logger LOGGER = Logger.getLogger(JnjGTSearchPageController.class);

	/**
	 * View Id for the PProduct List PDF Download.
	 */
	protected static final String SEARCH_RESULT_PDF = "productSearchResultsPdf";

	/**
	 * View Id for the PProduct List Excel Download.
	 */
	protected static final String SEARCH_RESULT_EXCEL = "productSearchResultsExcel";

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
	protected static final String USER_TYPE = "userType";

	@Resource(name = "customerLocationService")
	protected CustomerLocationService customerLocationService;

	@Resource(name = "searchBreadcrumbBuilder")
	protected SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "productSearchFacade")
	protected ProductSearchFacade<ProductData> productSearchFacade;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Resource(name = "customerFacade")
	protected CustomerFacade customerFacade;

	@Autowired
	JnjGTProductFacade jnjGTProductFacade;

	@Resource(name = "GTB2BUnitFacade")
	JnjGTB2BUnitFacade jnjGTB2BUnitFacade;

	@Resource(name="GTCustomerFacade")
	JnjGTCustomerFacade jnjGTCustomerFacade;
	
	@Autowired
	CMSSiteService cMSSiteService;
	@Autowired
	MediaService mediaService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	public CatalogVersionService getCatalogVersionService() {
	    return catalogVersionService;
	}
	
	public CustomerLocationService getCustomerLocationService() {
		return customerLocationService;
	}


	public SearchBreadcrumbBuilder getSearchBreadcrumbBuilder() {
		return searchBreadcrumbBuilder;
	}


	public ProductSearchFacade<ProductData> getProductSearchFacade() {
		return productSearchFacade;
	}


	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}


	public SessionService getSessionService() {
		return sessionService;
	}


	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}


	public JnjGTProductFacade getJnjGTProductFacade() {
		return jnjGTProductFacade;
	}


	public JnjGTB2BUnitFacade getJnjGTB2BUnitFacade() {
		return jnjGTB2BUnitFacade;
	}


	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}


	@Override
	@GetMapping( params = "!q")
	public String textSearch(@RequestParam(value = "text", defaultValue = "") final String searchText,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException
	{
		//TO-DO [AR]: Check if the user is INTERNATIONAL AFF., set Ind/Flag in model to display 'Get Price' link or not.
		ProductSearchPageData<SearchStateData, ProductData> searchPageData = null;
		if (StringUtils.isNotBlank(searchText))
		{
			final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
//			searchQueryData.setValue(XSSFilterUtil.filter(searchText));
			searchQueryData.setValue(searchText);
			searchState.setQuery(searchQueryData);

			 searchPageData = productSearchFacade.textSearch(searchState,
					pageableData);

			refineFacetList(PAGE_TYPE, searchPageData);
			model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));

			if (searchPageData == null)
			{
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else if (searchPageData.getKeywordRedirectUrl() != null)
			{
				// if the search engine returns a redirect, just
				return "redirect:" + searchPageData.getKeywordRedirectUrl();
			}
			else if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(searchText, model);
			}
			else
			{
				storeContinueUrl(request);
				populateModel(model, searchPageData, ShowMode.Page);
				model.addAttribute("isShowAllAllowed", Boolean.valueOf(isShowAllAllowed(searchPageData)));
				final JnjGTSearchSortForm form = new JnjGTSearchSortForm();
				form.setSearchText(searchText);
				model.addAttribute("searchSortForm", form);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				updatePageTitle(searchText, model);
			}
			getRequestContextData(request).setSearch(searchPageData);

			// modify breadcrums name to display 'Search Result' as text
			final List<Breadcrumb> breadcrums = searchBreadcrumbBuilder.getBreadcrumbs(null, searchText,
					CollectionUtils.isEmpty(searchPageData.getBreadcrumbs()));
			breadcrums.get(breadcrums.size() - 1).setName(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_RESULT);
			model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadcrums);
			model.addAttribute(USER_TYPE, sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE));
		}
		else
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
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
		/*if (currentSite.equals(JnjPCMCoreConstants.PCM))
		{
			model.addAttribute("groups", JnJCommonUtil.getValues(JnjPCMCoreConstants.PLP.SEARCH_GROUPS, ","));
		}
		else*/
		{
			model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
		}
		model.addAttribute("showMoreCounter", "1");
		setFalgForSpecialityCustomer(model);
		/* showaccount link*/
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
	
		/** If the User is eligible for Start New Order **/
		model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
		//return getViewForPage(model);
		if (searchPageData == null || searchPageData.getPagination().getTotalNumberOfResults() == 0)
		{
		return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchPage);
		}
		else
		{
			return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchListPage);
		}
	}
	

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, params = "q")
	public String refineSearch(@RequestParam("q") final String searchQuery, final JnjGTSearchSortForm form,
			@RequestParam(value = "text", required = false) final String searchText, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
	
		return refineSearchDetails(searchQuery,form,searchText,request,model);
	}
	
		public String refineSearchDetails(@RequestParam("q") final String searchQuery, final JnjGTSearchSortForm form,
			@RequestParam(value = "text", required = false) final String searchText, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		boolean resultLimitExceeded = false;
		final String downloadType = form.getDownloadType();
		final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
		final int finalPageSize = (form.isShowMore()) ? form.getPageSize(currentSite) * form.getShowMoreCounter() : form
				.getPageSize(currentSite);
		int totalResults = finalPageSize;

		if (DOWNLOAD_TYPE.PDF.toString().equals(form.getDownloadType())
				|| DOWNLOAD_TYPE.EXCEL.toString().equals(form.getDownloadType()))
		{
			if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()))
			{
				totalResults = Integer.parseInt(form.getTotalNumberOfResults());

				final int resultLimit = Integer.parseInt(Config
						.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));
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

		final PageableData pageableData = createPageableData(DEFAULT_PAGE, form.getPageSize(currentSite), form.getSortCode(),
				ShowMode.Page);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("refineSearchResult method of JnjGTSearchPageController");
			LOGGER.debug("isShowMore: " + form.isShowMore());
			LOGGER.debug("Page Size: " + form.getPageSize(currentSite));
			LOGGER.debug("Show More Counter: " + form.getShowMoreCounter());
			LOGGER.debug("Final Page Size: " + finalPageSize);
		}

	 ProductSearchPageData<SearchStateData, ProductData> searchPageData = new ProductSearchPageData<>();

		
		if(!DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType())){
			searchPageData = performSearch(searchQuery, DEFAULT_PAGE,
					ShowMode.Page, form.getSortCode(), form.getFullCount());
		}
		else{
		 searchPageData = performSearch(searchQuery, DEFAULT_PAGE,
				ShowMode.Page, form.getSortCode(), finalPageSize);
		}
		model.addAttribute("searchSortForm", form);
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("pageableData", pageableData);
		model.addAttribute("showMoreCounter", String.valueOf(form.getShowMoreCounter()));
		model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));

		model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
		model.addAttribute("isMddSite", Boolean.valueOf(Jnjb2bbrowseandsearchConstants.MDD.equals(currentSite)));
		/*if (currentSite.equals(JnjPCMCoreConstants.PCM))
		{
			model.addAttribute("groups", JnJCommonUtil.getValues(JnjPCMCoreConstants.PLP.SEARCH_GROUPS, ","));
		}
		else*/
		{
			model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
		}
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
		if (DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType()))
		{
			if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else
			{
				// modify breadcrums name to display 'Search Result' as text
				final List<Breadcrumb> breadcrums = searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData);
				breadcrums.get(breadcrums.size() - 1).setName(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_RESULT);
				model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, breadcrums);
				populateModel(model, searchText);
				storeContinueUrl(request);
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
			}
			/** If the User is eligible for Start New Order **/
			model.addAttribute(IS_ELIGIBLE_FOR_NEW_ORDER, sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS));
		//	return getViewForPage(model);
			if (searchPageData == null || searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
			return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchPage);
			}
			else
			{
				return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Search.searchListPage);
			}
		}
		else
		{
			model.addAttribute("currentAccount", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
					.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

			
			//JJEPIC-551
			model.addAttribute("currentAccountName", (jnjGTCustomerFacade.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
					.getCurrentB2bUnit().getLocName() : StringUtils.EMPTY);
			
			model.addAttribute(Jnjb2bCoreConstants.Product.SEARCH_RES_DOWNLOAD_FLAG, Boolean.TRUE);
			model.addAttribute("resultLimitExceeded", resultLimitExceeded);
			return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? SEARCH_RESULT_PDF : SEARCH_RESULT_EXCEL;
		}
	}

	@GetMapping("/getPrice")
	@ResponseBody
	protected String getProductPrice(final String productCode) throws SystemException, IntegrationException
	{
		String contractPrice = null;
		try
		{
			contractPrice = jnjGTProductFacade.getProductContractPrice(productCode);
		}
		catch (final SystemException systemException)
		{
			contractPrice = "error";
		}
		catch (final IntegrationException integrationException)
		{
			contractPrice = "error";
		}
		return contractPrice;
	}

	@ResponseBody
	@GetMapping("/autocomplete")
	public List<String> getAutocompleteSuggestions(@RequestParam("term") final String term)
	{
		final List<String> terms = new ArrayList<String>();
		for (final AutocompleteSuggestionData termData : productSearchFacade.getAutocompleteSuggestions(term))
		{
			terms.add(termData.getTerm());
		}
		return terms;
	}

	@ResponseBody
	@GetMapping("/autocompleteSecure")
	public List<String> getAutocompleteSuggestionsSecure(@RequestParam("term") final String term)
	{
		return getAutocompleteSuggestions(term);
	}

	protected void updatePageTitle(final String searchText, final Model model)
	{
		if (!sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME).equals(Jnjb2bbrowseandsearchConstants.PCM))
		{
			storeContentPageTitleInModel(
					model,
					getPageTitleResolver().resolveContentPageTitle(
							getMessageSource().getMessage("search.meta.title", null, getI18nService().getCurrentLocale()) + " "
									+ searchText));
		}
	}

	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String searchQuery, final int page,
			final ShowMode showMode, final String sortCode, final int pageSize)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);

		return productSearchFacade.textSearch(searchState, pageableData);
	}

	protected void populateModel(final Model model, final String searchText)
	{
		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());

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
	}

	/**
	 * This is used to refine facet's list based on pageType(PLP/Search)
	 * 
	 * @param pageType
	 * @param searchPageData
	 */
	protected void refineFacetList(final String pageType, final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
		if (CollectionUtils.isNotEmpty(facets))
		{
			for (final FacetData facetData : facets)
			{
				if (pageType.equals("plp"))
				{
					if (facetData.getCode().equals("searchCategory"))
					{
						facets.remove(facetData);
						break;
					}
				}
				else
				{
					if (facetData.getCode().equals("plpCategory"))
					{
						facets.remove(facetData);
						break;
					}
				}
			}
		}

	}
	
	public String getView(final String view){
        return Jnjb2bbrowseandsearchControllerConstants.ADDON_PREFIX + view;
	}
	
	protected void setFalgForSpecialityCustomer(final Model model)
	{
		boolean spclCustomer = false;
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final String[] spclCustGroup = StringUtils.split(Config.getParameter(Jnjb2bCoreConstants.SPECIALITY_CUSTOMER_GROUP), ",");
		if (currentSite != null && currentSite.equals(Jnjb2bCoreConstants.CONS))
		{
			final JnJB2BUnitModel jnjB2BUnit = jnjGTCustomerFacade.getCurrentB2bUnit();
			if (spclCustGroup!=null && Arrays.asList(spclCustGroup).contains(jnjB2BUnit.getCustomerGroup()))
			{
				spclCustomer = true;
			}
		}
		model.addAttribute("specialCustomer", Boolean.toString(spclCustomer));
	}
	
}
