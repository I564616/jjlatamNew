package com.jnj.b2b.browseandsearch.controllers.pages;

import com.jnj.b2b.storefront.breadcrumb.impl.HierarchyProductBreadCrumbBuilder;
import com.jnj.facades.data.JnjProductCarouselData;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.UnsupportedEncodingException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.impl.ProductBreadcrumbBuilder;
/*import com.jnj.hybris.storefront.constants.WebConstants;
import com.jnj.hybris.storefront.controllers.ControllerConstants;
import com.jnj.hybris.storefront.controllers.PcmControllerConstants;*/
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.b2b.storefront.controllers.pages.ProductPageController;
import com.jnj.b2b.storefront.forms.ReviewForm;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
/*import com.jnj.core.constants.Jnjb2bcoreConstants;*/
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;

/*import com.jnj.pcm.constants.JnjPCMCoreConstants;*/



/**
 * Controller class responsible for handling requests for Product Detail Page.
 *
 * @author Accenture
 * @version 1.0
 *
 */

public class JnjGTProductPageController extends ProductPageController
{
	private static final String PRODUCT_DETAIL_PDF="productDetailPdf";
	private static final String JNJ_PRODUCT_MAIN_IMAGE_URL="productMainImage";
	private static final String SYS_MASTER = "sys_master";
	private static final String EXCEPTION_MESSAGE="No Page With ID Found:::::::::::: Class  JnjGTProductPageController ::::::::: Method Name ::::: productDetail :";

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	protected static final Logger LOG = Logger.getLogger(JnjGTProductPageController.class);

	/**
	 * View Id for the Empty Product Page.
	 */
	protected static final String NO_PRODUCT_CMS_PAGE_ID = "noProductFoundPage";

	protected static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";

	/**
	 * Constant enum to store site name.
	 */
	public enum SITE_NAME
	{
		MDD, CONS;
	}

	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;

	/**
	 * The Instance of <code>UrlResolver</code>.
	 */
	@Resource(name = "productModelUrlResolver")
	protected UrlResolver<ProductModel> productModelUrlResolver;

	/**
	 * The Instance of <code>JnjGetCurrentDefaultB2BUnitUtil</code>.
	 */
	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	/**
	 * The Instance of <code>UserService</code>.
	 */
	@Resource(name = "userService")
	UserService userService;

	/**
	 * The Instance of <code>SessionService</code>.
	 */
	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	protected I18NService i18nService;

	@Autowired
	protected CommonI18NService commonI18NService;

	protected CMSSiteService cMSSiteService;


	protected MediaService mediaService;

	/**
	 * The Instance of <code>JnjProductFacade</code>.
	 */
	@Autowired
	protected JnjGTProductFacade jnjGTProductFacade;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade customerFacade;

	@Resource(name = "GTB2BUnitFacade")
	JnjGTB2BUnitFacade jnjGTB2BUnitFacade;

	protected ConfigurationService configurationService;

	@Resource(name = "productBreadcrumbBuilder")
	protected ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "hierarchyProdBreadcrumbBuilder")
	protected HierarchyProductBreadCrumbBuilder hierarchyProdBreadcrumbBuilder;

	@Override
	@RequireHardLogIn
	@GetMapping(PRODUCT_CODE_PATH_VARIABLE_PATTERN)
	public String productDetail(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}


		JnJProductModel product = null;
		try
		{
			product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);
		}
		catch (final UnknownIdentifierException exception)
		{
			//
		}

		if (product == null)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_PRODUCT_CMS_PAGE_ID));
			return getViewForPage(model);
		}

		final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(product));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}

		final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
		model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);

		populateLanguages(model);
		//model.addAttribute("currentLocale", i18nService.getCurrentLocale().getLanguage());

		updatePageTitle(product, model);
		populateProductDetailForDisplay(product, model, request);
		model.addAttribute(new ReviewForm());
		model.addAttribute("pageType", PageType.PRODUCT.name());
                model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTB2BUnitFacade.isCustomerInternationalAff()));


		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(product.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(product.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);

		//return getViewForPage(model);
		if (product == null)
		{
			return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Product.noProductFoundPage);
		}
		else
		{
			return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Product.ProductLayout1);
		}
	}

	/**
	 * Adds languages specific to current country to model so that they can be displayed on PDP.
	 *
	 * @param model
	 *           the model
	 */
	protected void populateLanguages(final Model model)
	{
		final List<LanguageData> languages = new ArrayList<LanguageData>();
		if (getCurrentCountry() != null)
		{
			for (final String languageCode : JnJCommonUtil.getValues(
					Jnjb2bbrowseandsearchWebConstants.LANGUAGES_KEY.concat(getCurrentCountry().getIsocode()), ","))
			{
				languages.add(jnjGTProductFacade.getLanguageData(commonI18NService.getLanguage(languageCode)));
			}
		}
		model.addAttribute("languages", languages);
	}

	@GetMapping("/productDetailPopUp")
	public String productDetailPopUP(@RequestParam("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{
		final JnJProductModel product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);

		final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
		model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
		populateProductDetailForDisplay(product, model, request);

		return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Cart.productDetailPopUp);
	}


	protected void updatePageTitle(final ProductModel productModel, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveProductPageTitle(productModel));
	}

	protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Collections.singleton(ProductOption.GALLERY));
		getRequestContextData(request).setProduct(productModel);
		storeCmsPageInModel(model, getPageForProduct(productModel));
		model.addAttribute("galleryImages", getGalleryImages(productData));
		model.addAttribute("product", productData);
		//rama
		//model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productModel));
		model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY, hierarchyProdBreadcrumbBuilder.getBreadcrumbs(productData));
	}

	protected AbstractPageModel getPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		return getCmsPageService().getPageForProduct(product);
	}

	protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData)
	{
		final List<Map<String, ImageData>> galleryImages = new ArrayList<Map<String, ImageData>>();
		if (CollectionUtils.isNotEmpty(productData.getImages()))
		{
			final List<ImageData> images = new ArrayList<ImageData>();
			for (final ImageData image : productData.getImages())
			{
				if (ImageDataType.GALLERY.equals(image.getImageType()))
				{
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>()
			{
				@Override
				public int compare(final ImageData image1, final ImageData image2)
				{
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images))
			{
				int currentIndex = images.get(0).getGalleryIndex().intValue();
				Map<String, ImageData> formats = new HashMap<String, ImageData>();
				for (final ImageData image : images)
				{
					if (currentIndex != image.getGalleryIndex().intValue())
					{
						galleryImages.add(formats);
						formats = new HashMap<String, ImageData>();
						currentIndex = image.getGalleryIndex().intValue();
					}
					formats.put(image.getFormat(), image);
				}
				if (!formats.isEmpty())
				{
					galleryImages.add(formats);
				}
			}
		}
		return galleryImages;
	}

	@GetMapping(PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/zoomImage")
	public String showZoomImages(@PathVariable("productCode") final String productCode,
			@RequestParam(value = "galleryPosition", required = false) final String galleryPosition, final Model model)
	{
		final ProductModel productModel = jnJGTProductService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Collections.singleton(ProductOption.GALLERY));
		final List<Map<String, ImageData>> images = getGalleryImages(productData);
		model.addAttribute("galleryImages", images);
		model.addAttribute("product", productData);
		if (galleryPosition != null)
		{
			try
			{
				model.addAttribute("zoomImageUrl", images.get(Integer.parseInt(galleryPosition)).get("zoom").getUrl());
			}
			catch (final IndexOutOfBoundsException ignore)
			{
				model.addAttribute("zoomImageUrl", "");
			}
			catch (final NumberFormatException ignore)
			{
				model.addAttribute("zoomImageUrl", "");
			}
		}
		return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Product.ZoomImagesPopup);
	}

	/**
	 * Returns product details section populated with localized data.
	 *
	 * @param model
	 *           the model
	 * @param productCode
	 *           the product code
	 * @param locale
	 *           the locale in which data is intended
	 * @return the string
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/getLocaleSpecific/Content")
	public String getLocaleSpecificCont(@RequestParam("productCode") final String productCode, final Model model,
			@RequestParam("locale") final String locale, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final JnJProductModel product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);

		// Save the current locale in temporary variable
		final Locale currentLocale = i18nService.getCurrentLocale();

		// Change the current locale and fetch data as per the requested locale
		i18nService.setCurrentLocale(LocaleUtils.toLocale(locale));
		populateProductDetailForDisplay(product, model, request);

		// Set the locale back to original locale
		i18nService.setCurrentLocale(currentLocale);
		model.addAttribute("currentLocale", locale);
		populateLanguages(model);

		return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Product.ProductDetails);
	}

	@PostMapping("/sendMultipleEmail/PDP")
	public @ResponseBody
	boolean sendMultipleEmail(final HttpServletRequest request)
	{
		final String productCode = request.getParameter("prodCode");
		final String locale = request.getParameter("locale");
		final String emailDetails = request.getParameter("emailDetails");

		final ProductModel productModel = jnJGTProductService.getProductForCode(productCode);

		// Save the current locale in temporary variable
		final Locale currentLocale = i18nService.getCurrentLocale();

		// Change the current locale and fetch data as per the requested locale
		i18nService.setCurrentLocale(LocaleUtils.toLocale(locale));

		final ProductData productData = productFacade.getProductForOptions(productModel, null);

		// Set the locale back to original locale
		i18nService.setCurrentLocale(currentLocale);

		return jnjGTProductFacade.sendProductDetailsEmail(productData, emailDetails);
	}

	@ModelAttribute("currentCountry")
	public CountryData getCurrentCountry()
	{
		return customerFacade.getCurrentCountry();
	}

	public String getView(final String view){
        return Jnjb2bbrowseandsearchControllerConstants.ADDON_PREFIX + view;
    }

	@ResponseBody
	@GetMapping(value = "/getTogetherBoughtProducts",produces = "application/json")
	public List<JnjProductCarouselData> productsBoughtTogether(@RequestParam("productCode") final String productCode){
		return jnjGTProductFacade.getProductsBoughtTogether(productCode, getCurrentCatalog());
	}
	
	@GetMapping(PRODUCT_CODE_PATH_VARIABLE_PATTERN+"/generateProductDetailsPDF")
	@RequireHardLogIn
	public String generateProductDetailsPDF(@PathVariable("productCode") final String productCode, final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
	{
		JnJProductModel product=null;

		product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);

		populateProductDetailForDisplay(product, model, request);

		final String mediaDirBase = getConfigurationService().getConfiguration().getString(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		model.addAttribute(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY,mediaDirBase + File.separator + SYS_MASTER + File.separator
				+ mediaService.getMedia(currentCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2).getLocation());
		model.addAttribute( Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH1_PROPERTY, mediaDirBase + File.separator + SYS_MASTER + File.separator
				+ mediaService.getMedia(currentCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_10).getLocation());

		try {
			final MediaModel mediaModelFranchiseLogo = mediaService.getMedia(product.getFranchiseName());
			if (mediaModelFranchiseLogo != null) {
				final String franchiseLogoUrl=mediaModelFranchiseLogo.getLocation();
				model.addAttribute(Jnjb2bbrowseandsearchConstants.PDP.FRANCHISE_LOGO_PROPERTY, mediaDirBase + File.separator + SYS_MASTER + File.separator+franchiseLogoUrl);

				if (LOG.isDebugEnabled()) {
					LOG.debug("Entered into mediaModelFranchiseLogo ::::Class  JnjGTProductPageController ::::::::: Method Name ::::: productDetail    ::::: " + mediaModelFranchiseLogo.getURL());
				}
			}
		}
		catch(ModelNotFoundException modelNotFoundException) {
			model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL, "");
			LOG.error(EXCEPTION_MESSAGE,modelNotFoundException);
		}
		catch(UnknownIdentifierException unknownIdentifierException) {
			model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL, "");
			LOG.error(EXCEPTION_MESSAGE,unknownIdentifierException);
		}
		try {
			final MediaModel mediaModel2 =product.getPicture();/*Getting the Product Image media */
			if(mediaModel2!=null) {
				final String productUrl=mediaModel2.getLocation();
				model.addAttribute(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_IMAGE_URL_PROPERTY, mediaDirBase + File.separator + SYS_MASTER + File.separator+productUrl);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Entered into mainImage ::::Class  JnjGTProductPageController ::::::::: Method Name ::::: productDetail ::::: ");
				}
			}
		}
		catch(ModelNotFoundException modelNotFoundException) {
			model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL, "");
			LOG.error(EXCEPTION_MESSAGE,modelNotFoundException);
		}
		catch(UnknownIdentifierException unknownIdentifierException) {
			model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL, "");
			LOG.error(EXCEPTION_MESSAGE,unknownIdentifierException);
		}


		final List<ProductDocumentsModel> productdodumentsList = product.getProductDocumentlist();
		model.addAttribute(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY,productdodumentsList);

		return PRODUCT_DETAIL_PDF;

	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}

	public UrlResolver<ProductModel> getProductModelUrlResolver() {
		return productModelUrlResolver;
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	public UserService getUserService() {
		return userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public JnjGTProductFacade getJnjGTProductFacade() {
		return jnjGTProductFacade;
	}

	public ProductBreadcrumbBuilder getProductBreadcrumbBuilder() {
		return productBreadcrumbBuilder;
	}

	public JnjGTCustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	public HierarchyProductBreadCrumbBuilder getHierarchyProdBreadcrumbBuilder() {
		return hierarchyProdBreadcrumbBuilder;
	}

}
