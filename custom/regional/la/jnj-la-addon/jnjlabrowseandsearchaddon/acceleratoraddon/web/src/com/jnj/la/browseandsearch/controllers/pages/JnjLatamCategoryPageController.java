package com.jnj.la.browseandsearch.controllers.pages;

import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.la.browseandsearch.wrapper.JnjLatamCategoryDataWrapper;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import com.jnj.core.util.JnjObjectComparator;
import com.jnj.la.browseandsearch.controllers.JnjlabrowseandsearchaddonControllerConstants;
import com.jnj.services.CMSSiteService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.browseandsearch.controllers.pages.JnjGTCategoryPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import de.hybris.platform.cms2.model.site.CMSSiteModel;

import com.jnj.core.util.JnjGTCoreUtil;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.b2b.storefront.util.MetaSanitizerUtil;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;

import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController.ShowMode;

/**
 * @author sbaner48
 *
 */
public class JnjLatamCategoryPageController extends JnjGTCategoryPageController {

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<JnjLaProductData> productSearchFacade;

	@Autowired
	private MediaService mediaService;

	@Resource(name="GTCustomerFacade")
	private JnjGTCustomerFacade jnjGTCustomerFacade;

	@Resource(name = "GTB2BUnitFacade")
	private JnjGTB2BUnitFacade jnjGTB2BUnitFacade;

	private static final Class<JnjLatamCategoryPageController> currentClass = JnjLatamCategoryPageController.class;

	private static final JnjObjectComparator jnjObjectComparator = new JnjObjectComparator(
			CategoryData.class, "getName", true, true);

	private static final JnjObjectComparator jnjCategoryWrapperComparator = new JnjObjectComparator(
			JnjLatamCategoryDataWrapper.class, "getCategoryData", true, true);

	private static final int LATAM_CATALOG_PAGE_SIZE = 10000;
	protected static final String CATEGORY_SEARCH_RESULT_PDF = "latamproductSearchResultsPdf";
	protected static final String CATEGORY_SEARCH_RESULT_EXCEL = "latamproductSearchResultsExcel";

	@Override
	protected String displayCategoriesAndProducts(final String categoryCode,
			final ShowMode showMode, final String searchQuery,
			final JnjGTSearchSortForm form, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException, CMSItemNotFoundException {
		final String methodName = "Latam displayCategoriesAndProducts()";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.CATEGORIES,
				methodName, Logging.BEGIN_OF_METHOD, currentClass);
		
		CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.CATEGORIES,
				methodName, "Fetching categories from :: "
						+ productCatalogs.get(0).getId() + " "
						+ Jnjb2bCoreConstants.ONLINE, currentClass);
		
		CatalogVersionModel catalogVersionModel = catalogService
				.getCatalogVersion(productCatalogs.get(0).getId(),
						Jnjb2bCoreConstants.ONLINE);

		final CategoryModel category = categoryService.getCategoryForCode(
				catalogVersionModel, categoryCode);

		model.addAttribute("categoryCode", categoryCode);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		final Boolean displayProducts = category.getDisplayProducts();
		String view;
		model.addAttribute(
				IS_ELIGIBLE_FOR_NEW_ORDER,
				sessionService
						.getAttribute(Jnjb2bbrowseandsearchConstants.Login.ORDERING_RIGHTS));
		
		final String currentSite = sessionService
				.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);	
		setExportData(model, productCatalogs, currentSite);
			
		final ProductCategorySearchPageData<SearchStateData, JnjLaProductData, CategoryData> searchPageData;
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		
		final String downloadType = form.getDownloadType();
				
		final PageableData pageableData = createPageableData(DEFAULT_PAGE, LATAM_CATALOG_PAGE_SIZE, form.getSortCode(), ShowMode.Page);

		searchPageData = productSearchFacade.categorySearch(categoryCode, searchState, pageableData);
		
		if (displayProducts.booleanValue()) {
			boolean resultLimitExceeded = false;
			
			if (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)
					|| DOWNLOAD_TYPE.PDF.toString().equals(downloadType) && StringUtils.isNotEmpty(form.getTotalNumberOfResults())) {
					int totalResults = Integer.parseInt(form.getTotalNumberOfResults());
					final int resultLimit = Integer
							.parseInt(Config
									.getParameter(Jnjb2bbrowseandsearchConstants.PLP.PLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD));
					if (totalResults > resultLimit) {
						resultLimitExceeded = true;
					}
			}

			populateModel(model, searchPageData, showMode);

			if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType)) {
				
				setDataForNoneDownloadTypeToModel(model, categoryCode, searchPageData, form, currentSite, request, category, searchQueryData);
				
				view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.ProductListPage;
				
			} else {
				model.addAttribute("currentAccount", (jnjGTCustomerFacade
						.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);

				model.addAttribute(
						Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG,
						Boolean.FALSE);
				model.addAttribute("resultLimitExceeded", resultLimitExceeded);

				setExcelImageToModel(model);
				
				setMediastoModel(model);

				return DOWNLOAD_TYPE.PDF.toString().equals(downloadType) ? CATEGORY_SEARCH_RESULT_PDF
						: CATEGORY_SEARCH_RESULT_EXCEL;
			}
		} else {
			model.addAttribute(
					Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb(null,
							jnjCommonFacadeUtil.getMessageFromImpex("category.catalog"), null)));
			populateModel(model, searchPageData, showMode);
			setUpCategoryLevelData(model, category);
			view = Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Category.CategoryPage;
			updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
		}
		storeCmsPageInModel(model, categoryPage);
		storeContinueUrl(request);
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category
				.getKeywords());
		final String metaDescription = MetaSanitizerUtil
				.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		setFalgForSpecialityCustomer(model);
		final Object showChangeAccountLink = sessionService
				.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink
				&& ((Boolean) showChangeAccountLink).booleanValue()) {
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.CATEGORIES,
				methodName, Logging.END_OF_METHOD, currentClass);
		return getView(view);
	}
	
	private void setExcelImageToModel(Model model){
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog =
			cmsSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog,
						"epicEmailLogoImageOne")));
	}
	
	private void setMediastoModel(Model model){
		List<ContentCatalogModel> catologLst = getCmsSiteService()
				.getCurrentSite().getContentCatalogs();
		if (catologLst != null && !catologLst.isEmpty()) {
			MediaModel mediaModel1 = mediaService
					.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(),
							Jnjb2bCoreConstants.ONLINE),
							Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService
									.getCatalogVersion(catologLst.get(0).getId(),
											Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
			if (mediaModel1 != null) {
				model.addAttribute("jnjConnectLogoURL",
						mediaService.getStreamFromMedia(mediaModel1));
			}
			if (mediaModel2 != null) {
				model.addAttribute("jnjConnectLogoURL2",
						mediaService.getStreamFromMedia(mediaModel2));
			}
		}
		
	}
	
	private void setExportData(final Model model, List<CatalogModel> productCatalogs, String currentSite){
		model.addAttribute(
				"mddExportFileUrl",
				getMediaURLForMDDExport(
						Jnjb2bCoreConstants.MDD_EXPORT_FILE_MEDIA_KEY,
						productCatalogs.get(0).getId()));
		model.addAttribute(
				"mddPdfExportFileUrl",
				getMediaURLForMDDExport(
						Jnjb2bCoreConstants.MDD_PDF_EXPORT_FILE_MEDIA_KEY,
						productCatalogs.get(0).getId()));
		model.addAttribute("isMddSite", Boolean
				.valueOf(Jnjb2bbrowseandsearchConstants.MDD
						.equals(currentSite)));
	}
	
	private void setDataForNoneDownloadTypeToModel(Model model, String categoryCode, final ProductCategorySearchPageData<SearchStateData, JnjLaProductData, CategoryData> searchPageData, JnjGTSearchSortForm form, String currentSite, HttpServletRequest request, CategoryModel category, SearchQueryData searchQueryData){

		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> tempSearchPageData = new ProductCategorySearchPageData<>();
		tempSearchPageData.setBreadcrumbs(searchPageData.getBreadcrumbs());
		tempSearchPageData.setFreeTextSearch(searchPageData.getFreeTextSearch());

		model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY,
				searchBreadcrumbBuilder.getBreadcrumbs(categoryCode, tempSearchPageData));
		model.addAttribute("pageType", PageType.CATEGORY.name());
		model.addAttribute("searchSortForm", form);
		model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
		model.addAttribute("showMoreCounter", "1");

		model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));

		updatePageTitle(category, searchPageData.getBreadcrumbs(),
				model);
		final RequestContextData requestContextData = getRequestContextData(request);
		requestContextData.setCategory(category);
		requestContextData.setSearch(searchPageData);

		if (searchQueryData.getValue() != null) {
			model.addAttribute("metaRobots", "no-index,follow");
		}
			model.addAttribute("groups", Config.getParameter(Jnjb2bbrowseandsearchConstants.PLP.SEARCH_GROUPS).split(","));
	}
	

	public String getView(final String view) {
		return JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + view;
	}

	protected void setUpCategoryLevelData(final Model model,
			final CategoryModel selectedCategory) {

		final Map<String, Set<CategoryData>> subCategories = new LinkedHashMap<>();
		final Map<String, Set<JnjLatamCategoryDataWrapper>> highCategoriesLevel = new LinkedHashMap<>();
		buildCategoryData(selectedCategory, subCategories, highCategoriesLevel);

		final CategoryModel rootCategory = commerceCategoryService
				.getCategoryForCode(Jnjb2bbrowseandsearchConstants.PLP.ROOT_CATEGORY_CODE);
		if (rootCategory.getCategories().contains(selectedCategory)) {
			subCategories.remove(selectedCategory.getName());
		}

		model.addAttribute("currentAccount", (jnjGTCustomerFacade
				.getCurrentB2bUnit() != null) ? jnjGTCustomerFacade
				.getCurrentB2bUnit().getUid() : StringUtils.EMPTY);
		model.addAttribute("userEmail", (jnjGTCustomerFacade
				.getCurrentCustomer() != null) ? jnjGTCustomerFacade
				.getCurrentCustomer().getEmail() : StringUtils.EMPTY);

		model.addAttribute("subCategoriesData", subCategories);
		model.addAttribute("highCategoriesLevel", highCategoriesLevel);
	}


	private void buildCategoryData(final CategoryModel currentCategory,
								   final Map<String, Set<CategoryData>> subCategories,
								   final Map<String, Set<JnjLatamCategoryDataWrapper>> highCategoriesLevel){
		buildSubCategoriesData(currentCategory, subCategories);
		buildHighLevelCategoryData(currentCategory,highCategoriesLevel);
	}


	private void buildSubCategoriesData(final CategoryModel currentCategory,
			final Map<String, Set<CategoryData>> subCategories) {

		for (final CategoryModel subCategory : currentCategory.getCategories()) {
			final Set<CategoryData> subCategoriesData = new TreeSet<>(jnjObjectComparator);
			if(isProductPresentForTheCategory(subCategory)){
				checkSecondLevelSubCategory(subCategory, subCategories, subCategoriesData);
			}
		}
	}


	private void buildHighLevelCategoryData(final CategoryModel currentCategory,
											final Map<String, Set<JnjLatamCategoryDataWrapper>> highCategoriesLevel) {
		for (final CategoryModel subCategory : currentCategory.getCategories()) {
			final Set<JnjLatamCategoryDataWrapper> highCategoryLevelData = new TreeSet<>(jnjCategoryWrapperComparator);
			final CategoryData highCategoryData = new CategoryData();

			if (isProductPresentForTheCategory(subCategory)) {
				categoryConverter.convert(subCategory,highCategoryData);

				final JnjLatamCategoryDataWrapper jnjLatamCategoryDataWrapper =
						new JnjLatamCategoryDataWrapper(highCategoryData, isDisplayProductsEnabled(subCategory));
				if (highCategoriesLevel.keySet().contains(subCategory.getName())) {
					highCategoriesLevel.get(subCategory.getName()).add(jnjLatamCategoryDataWrapper);
				} else {
					highCategoryLevelData.add(jnjLatamCategoryDataWrapper);
					highCategoriesLevel.put(subCategory.getName(), highCategoryLevelData);
				}
			}
		}
	}


	private void checkSecondLevelSubCategory(CategoryModel subCategory, final Map<String, Set<CategoryData>> subCategories,
											 final Set<CategoryData> subCategoriesData){
		for (final CategoryModel category : subCategory.getCategories()) {
			if (isProductPresentForTheCategory(category)) {
				final CategoryData subCategoryData = new CategoryData();
				categoryConverter.convert(category, subCategoryData);
				if (subCategories.keySet().contains(subCategory.getName())) {
					subCategories.get(subCategory.getName()).add(subCategoryData);
				} else {
					subCategoriesData.add(subCategoryData);
					subCategories.put(subCategory.getName(), subCategoriesData);
				}
			}
		}
	}

	private boolean isProductPresentForTheCategory(CategoryModel category) {
		return (category.getProductPresentForTheCategory() != null && category
				.getProductPresentForTheCategory().booleanValue()) ? true : false;
	}

	@Override
	protected void setFalgForSpecialityCustomer(final Model model) {
		boolean spclCustomer = false;
		model.addAttribute("specialCustomer", Boolean.toString(spclCustomer));
	}

	public String getMediaURLForMDDExport(String mediaCode, String catalogId) {
		final String methodName = "getMediaURLForMDDExport()";
		MediaModel mediaModel = modelService.create(MediaModel.class);
		mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(catalogId, Jnjb2bCoreConstants.ONLINE));

		mediaModel.setCode(mediaCode);
		try {
			final MediaModel existingMedia = flexibleSearchService.getModelByExample(mediaModel);
			if (existingMedia != null) {
				return existingMedia.getURL();
			}
		} catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.CATEGORIES,
					methodName, "Media with code Not Found :: " + mediaCode, exception, currentClass);
		}
		return null;
	}

    public boolean isDisplayProductsEnabled(CategoryModel category) {
        return (category.getDisplayProducts() != null &&
                category.getDisplayProducts().booleanValue()) ? Boolean.TRUE : Boolean.FALSE;
    }

}
