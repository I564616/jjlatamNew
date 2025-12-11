package com.jnj.la.browseandsearch.controllers.pages;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchWebConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.browseandsearch.controllers.pages.JnjGTProductPageController;
import com.jnj.b2b.storefront.breadcrumb.impl.ProductBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.forms.ReviewForm;
import com.jnj.b2b.storefront.util.MetaSanitizerUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.JnjProductCarouselData;
import com.jnj.facades.product.JnjLatamProductFacade;
import com.jnj.la.browseandsearch.controllers.JnjlabrowseandsearchaddonControllerConstants;
import com.jnj.la.browseandsearch.util.JnjLatamBreadcrumbUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;


/**
 *
 */

/**
 * @author sbaner48
 *
 */
@Controller
public class JnjLatamProductPageController extends JnjGTProductPageController {

    protected static final String NUMBER_OF_PRODUCT_PER_SLIDE = "numberOfProductPerSlide";
    protected static final String CAROUSEL_PRODUCTS_EACH_SLIDE = "carousel.products.each.slide";
    protected static final String DEFAULT_CAROUSEL_PRODUCTS_EACH_SLIDE = "4";
    protected static final String PDP_RELATED_PRODUCTS_CAROUSEL_HIDE_FLAG = "pdp.rp.carousel.hide.lang.";
    protected static final String PDP_CAB_PRODUCTS_CAROUSEL_HIDE_FLAG = "pdp.cab.carousel.hide.lang.";
    protected static final String PDP_TABS_HIDE_FLAG = "pdp.info.tabs.hide.lang.";
    protected static final String PDP_DESC_HIDE_FLAG = "pdp.desc.hide.lang.";
    protected static final String JNJ_PRODUCT_MAIN_IMAGE_URL_PROPERTY = "productMainImage";
    protected static final String JNJ_PRODUCT_MAIN_IMAGE_FILE_PROPERTY = "productMainImageFile";
    protected static final String JNJ_FRANCHISE_LOGO_PDP_URL_PROPERTY = "franchiseLogo";
    protected static final String JNJ_FRANCHISE_LOGO_PDP_FILE_PROPERTY = "franchiseLogoFile";
    protected static final String JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY = "productDocumentsList";
    protected static final String SITE_LOGO_PATH_PROPERTY = "siteLogoPath";
    protected static final String SITE_LOGO_PATH1_PROPERTY = "siteLogoPath1";
    protected static final String PRODUCT_DETAIL_PDF_VIEW = "laProductDetailPdfView";
    protected static final String PRODUCT_GALERY_IMAGE_PIXEL_SIZE = "300x300";
    protected static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
    private static final String FILE_NAME_PATH_VARIABLE_PATTERN = "/{mediaCode:.*}";
    private static final String EXCEPTION = "Exception";
    private final Class<JnjLatamProductPageController> currentClass = JnjLatamProductPageController.class;
    @Autowired
    protected JnjLatamProductFacade jnjLatamProductFacade;

    @Resource(name = "productService")
    protected JnJGTProductService jnJGTProductService;
    @Resource(name = "GTB2BUnitFacade")
    protected JnjGTB2BUnitFacade jnjGTUnitFacade;
    @Resource(name = "productBreadcrumbBuilder")
    protected ProductBreadcrumbBuilder productBreadcrumbBuilder;
    @Autowired
    protected MediaService mediaService;
    @Autowired
    protected ConfigurationService configurationService;
    @Autowired
    private CMSSiteService cmsSiteService;
    @Autowired
    private CatalogService catalogService;

    protected static void setAdditionalInformation(final JnJProductModel product, final Model model) {
        model.addAttribute(JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY, product.getProductDocumentlist());
    }

    protected static void setMDDProductMainImage(final JnJProductModel product, final JnjLaProductData laProductData, final Model model) {
        final MediaModel picture = product.getPicture();

        if (picture == null) {
            model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL_PROPERTY, StringUtils.EMPTY);
            laProductData.setMainProductImgUrl(StringUtils.EMPTY);
            return;
        }

        final String downloadURL = picture.getDownloadURL();
        model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_URL_PROPERTY, StringUtils.isNotEmpty(downloadURL) ? downloadURL : picture.getURL());
        laProductData.setMainProductImgUrl(StringUtils.isNotEmpty(downloadURL) ? downloadURL : picture.getURL());
    }

    /**
     * This method sets the gallery index for image data
     *
     * @param image
     *           the image to be used
     */
    private static void setGalleryIndexForImage(final ImageData image) {
        if (image.getGalleryIndex() == null) {
            image.setGalleryIndex(0);
        }
    }

    @Override
    public String getView(final String view) {
        final String METHOD_NAME = "getView()";
        JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.PRODUCT_CATALOG, METHOD_NAME,
                "In JnjLatamProductPageController getView() :: "
                        + JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + view,
                currentClass);
        return JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + view;
    }

    @Override
    protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model,
                                                   final HttpServletRequest request) throws CMSItemNotFoundException {
        final String METHOD_NAME = "Latam populateProductDetailForDisplay()";

        JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.PRODUCT_CATALOG, METHOD_NAME, Logging.BEGIN_OF_METHOD,
                currentClass);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");
        final JnjLaProductData laProductData = jnjLatamProductFacade.getLatamProductData(productModel);
        if (productModel instanceof JnJLaProductModel) {
            final JnJLaProductModel laProductModel = (JnJLaProductModel) productModel;
            getRequestContextData(request).setProduct(laProductModel);
            storeCmsPageInModel(model, getPageForProduct(laProductModel));
            setMDDProductMainImage(laProductModel, laProductData, model);
        }

        model.addAttribute("galleryImages", getGalleryImages(laProductData));
        model.addAttribute("product", laProductData);
        final String numberOfProductPerSlide = getConfigurationService().getConfiguration()
                .getString(CAROUSEL_PRODUCTS_EACH_SLIDE, DEFAULT_CAROUSEL_PRODUCTS_EACH_SLIDE);
        model.addAttribute(NUMBER_OF_PRODUCT_PER_SLIDE, numberOfProductPerSlide);

        model.addAttribute(Jnjb2bbrowseandsearchWebConstants.BREADCRUMBS_KEY,
                JnjLatamBreadcrumbUtil.customizeBreadCrumb(hierarchyProdBreadcrumbBuilder.getBreadcrumbs(laProductData)));
        JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.PRODUCT_CATALOG, METHOD_NAME, Logging.END_OF_METHOD,
                currentClass);

        Boolean relatedProductsCarouselHideFlag = configurationService.getConfiguration().getBoolean(PDP_RELATED_PRODUCTS_CAROUSEL_HIDE_FLAG + commonI18NService.getCurrentLanguage().getIsocode(), Boolean.FALSE);
        Boolean customersAlsoBoughtCarouselHideFlag = configurationService.getConfiguration().getBoolean(PDP_CAB_PRODUCTS_CAROUSEL_HIDE_FLAG + commonI18NService.getCurrentLanguage().getIsocode(), Boolean.FALSE);
        //Flags to hide Related Products Carousel and Customer also bought Caqrousel for English language
        model.addAttribute("rpCarouselHideFlag", relatedProductsCarouselHideFlag);
        model.addAttribute("cabCarouselHideFlag", customersAlsoBoughtCarouselHideFlag);


    }

    @Override
    public String productDetail(@PathVariable("productCode") final String productCode, final Model model,
                                final HttpServletRequest request, final HttpServletResponse response)
            throws CMSItemNotFoundException, UnsupportedEncodingException {
        final Object showChangeAccountLink = sessionService
                .getAttribute(Jnjb2bbrowseandsearchConstants.Login.SHOW_CHANGE_ACCOUNT);
        LOG.info("showChangeAccountLink value : " + showChangeAccountLink);
        if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
            LOG.info("entered condition...........showChangeAccountLink :" + showChangeAccountLink);
            model.addAttribute("showChangeAccountLink", Boolean.TRUE);
        }
        final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
        final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
        final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
                Jnjb2bCoreConstants.ONLINE);
        JnJProductModel product = null;
        try {
            product = (JnJProductModel) jnJGTProductService.getProduct(catalogVersionModel, productCode);
        } catch (final UnknownIdentifierException exception) {
            //
            JnjGTCoreUtil.logErrorMessage("Fetching Product Model", "productDetail()", EXCEPTION, exception,
                    JnjLatamProductPageController.class);
        }

        if (product == null) {
            storeCmsPageInModel(model, getContentPageForLabelOrId(NO_PRODUCT_CMS_PAGE_ID));
            return getViewForPage(model);
        }

        final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(product));
        if (StringUtils.isNotEmpty(redirection)) {
            return redirection;
        }

        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
        model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);

        populateLanguages(model);
        setFranchiseLogo(product, model);
        setAdditionalInformation(product, model);

        Boolean pdpTabsHideFlag = configurationService.getConfiguration().getBoolean(PDP_TABS_HIDE_FLAG + commonI18NService.getCurrentLanguage().getIsocode(), Boolean.FALSE);
        Boolean pdpDescHideFlag = configurationService.getConfiguration().getBoolean(PDP_DESC_HIDE_FLAG + commonI18NService.getCurrentLanguage().getIsocode(), Boolean.FALSE);
        updatePageTitle(product, model);
        populateProductDetailForDisplay(product, model, request);
        model.addAttribute(new ReviewForm());
        model.addAttribute("pageType", PageType.PRODUCT.name());
        model.addAttribute("isinternationalAff", Boolean.valueOf(jnjGTUnitFacade.isCustomerInternationalAff()));
        model.addAttribute("pdpTabsHideFlag", pdpTabsHideFlag);
        model.addAttribute("pdpDescHideFlag", pdpDescHideFlag);

        final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(product.getKeywords());
        final String metaDescription = MetaSanitizerUtil.sanitizeDescription(product.getDescription());
        setUpMetaData(model, metaKeywords, metaDescription);

        return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Product.ProductLayout1);
    }

    @Override
    public String productDetailPopUP(@RequestParam("productCode") final String productCode, final Model model,
                                     final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException,
            UnsupportedEncodingException {
        final JnJProductModel product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);

        final String currentSite = sessionService.getAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME);
        model.addAttribute(Jnjb2bbrowseandsearchConstants.SITE_NAME, currentSite);
        populateProductDetailForDisplay(product, model, request);

        return getView(Jnjb2bbrowseandsearchControllerConstants.Views.Pages.Cart.productDetailPopUp);
    }

    @GetMapping(PRODUCT_CODE_PATH_VARIABLE_PATTERN
            + "/downloadProductDetails" + FILE_NAME_PATH_VARIABLE_PATTERN)
    public void getDetailsFile(
            @PathVariable("productCode") final String productCode,
            @PathVariable("mediaCode") final String mediaCode,
            final HttpServletResponse httpServletResponse) throws IOException,
            BusinessException {

        final String METHOD_GET_DETAILS_FILE = "getDetailsFileForProduct()";

        // Retrieving Details Media for the product
        final MediaModel detailsFileMedia = jnjLatamProductFacade
                .getDetailsFileForProduct(productCode, mediaCode);

        if (null != detailsFileMedia) {
            JnjGTCoreUtil.logInfoMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN,
                    METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
                    "Media fetched for Product[" + productCode
                            + "] with MediaCode[" + detailsFileMedia.getCode() + "]",
                    currentClass);

            final String mediaDirBase = Config
                    .getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
            final String mediaPath = mediaDirBase
                    + File.separator
                    + Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_ROOT_FOLDER_KEY)
                    + detailsFileMedia.getLocation();

            final File detailsFile = new File(mediaPath);
            if (detailsFile.exists()) {

                JnjGTCoreUtil.logInfoMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN,
                        METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
                        "File found.", currentClass);
                final FileInputStream fileInputStream = new FileInputStream(
                        detailsFile);
                httpServletResponse.setContentType(detailsFileMedia.getMime());
                httpServletResponse.setContentLength(Long.valueOf(detailsFile
						.length()).intValue());
                httpServletResponse.setHeader(
                        WebConstants.InvoiceDetails.HEADER_PARAM,
                        WebConstants.InvoiceDetails.HEADER_PARAM_VALUE
                                + detailsFileMedia.getRealFileName());
                FileCopyUtils.copy(fileInputStream,
                        httpServletResponse.getOutputStream());
                fileInputStream.close();
                httpServletResponse.getOutputStream().close();
                httpServletResponse.getOutputStream().flush();
            } else {
                JnjGTCoreUtil.logInfoMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN,
                        METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
                        "File does not exists.", currentClass);
            }
        } else {
            JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_DISPLAY_PAGE + Logging.HYPHEN,
                    METHOD_GET_DETAILS_FILE + Logging.HYPHEN,
                    "No Media fetched for Product[" + productCode + "]", currentClass);
            throw new BusinessException("No Media fetched for Product["
                    + productCode + "]");
        }
    }

    @Override
    public List<JnjProductCarouselData> productsBoughtTogether(@RequestParam("productCode") final String productCode) {
        return jnjLatamProductFacade.getProductsBoughtTogether(productCode);
    }

    protected void setFranchiseLogo(final JnJProductModel product, final Model model) {
        String franchiseMediaPath = StringUtils.EMPTY;
        String franchiseName = product.getFranchiseName();
        if (StringUtils.isNotEmpty(franchiseName)) {
            try {
                final MediaModel picture = mediaService.getMedia(franchiseName);
                franchiseMediaPath=getUrlFromMedia(picture);
            }
            catch (final UnknownIdentifierException e)
            {
                JnjGTCoreUtil.logErrorMessage("Fetching Media Model", "setFranchiseLogo()", EXCEPTION, e,
                        JnjLatamProductPageController.class);
            }
        }
        if (StringUtils.isEmpty(franchiseMediaPath)) {
            final CategoryModel superCategory = product.getSupercategories().iterator().next();
            if (null != superCategory) {
                final MediaModel picture = superCategory.getPicture();
                franchiseMediaPath=getUrlFromMedia(picture);
            }
        }
        model.addAttribute(JNJ_FRANCHISE_LOGO_PDP_URL_PROPERTY, franchiseMediaPath);
    }

    private static String getUrlFromMedia(final MediaModel picture){
        if (null != picture) {
            return (StringUtils.isNotEmpty(picture.getDownloadURL()) ? picture.getDownloadURL() : picture.getURL());
        }
        return StringUtils.EMPTY;
    }

    @Override
    protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData) {
        String mainProductImgUrl = "";
        if (productData instanceof JnjLaProductData) {
            mainProductImgUrl = ((JnjLaProductData) productData).getMainProductImgUrl();
        }
        final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productData.getImages())) {
            final List<ImageData> images = new ArrayList<>();
            for (final ImageData image : productData.getImages()) {
                if (StringUtils.isNotEmpty(image.getUrl()) && image.getUrl().contains(PRODUCT_GALERY_IMAGE_PIXEL_SIZE) && !mainProductImgUrl.equalsIgnoreCase(image.getUrl())) {
                    images.add(image);
                    setGalleryIndexForImage(image);
                }
            }
            Collections.sort(images, Comparator.comparing(ImageData::getGalleryIndex));
            setImageFormats(images,galleryImages);
        }
        return galleryImages;
    }

    /**
     * Method to set image formats
     *
     * @param images
     * @param galleryImages
     */
    private static void setImageFormats(final List<ImageData> images, final List<Map<String, ImageData>> galleryImages){
        if (CollectionUtils.isNotEmpty(images)) {
            int currentIndex = images.get(0).getGalleryIndex().intValue();
            Map<String, ImageData> formats = new HashMap<>();
            for (final ImageData image : images) {
                if (currentIndex != image.getGalleryIndex().intValue()) {
                    galleryImages.add(formats);
                    formats = new HashMap<>();
                    currentIndex = image.getGalleryIndex().intValue();
                }
                formats.put(image.getFormat(), image);
            }
            if (!formats.isEmpty()) {
                galleryImages.add(formats);
            }
        }
    }

    @Override
    public String generateProductDetailsPDF(@PathVariable("productCode") final String productCode, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException {
        final JnJProductModel product = (JnJProductModel) jnJGTProductService.getProductForCode(productCode);

        populateProductDetailForDisplay(product, model, request);
        setPDFJnJLogos(model);
        setPDFMDDProductMainImage(product, model);
        setPDFFranchiseLogo(product, model);
        setAdditionalInformation(product, model);
        Boolean pdpTabsHideFlag = configurationService.getConfiguration().getBoolean(PDP_TABS_HIDE_FLAG + commonI18NService.getCurrentLanguage().getIsocode(), Boolean.FALSE);
        model.addAttribute("pdpTabsHideFlag", pdpTabsHideFlag);

        return PRODUCT_DETAIL_PDF_VIEW;
    }

    protected void setPDFMDDProductMainImage(final JnJProductModel product, final Model model) {
        final MediaModel mediaModel = product.getPicture();

        if (mediaModel == null) {
            model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_FILE_PROPERTY, StringUtils.EMPTY);
            return;
        }

        final String productUrl = mediaModel.getDownloadURL();

        if (productUrl == null) {
            model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_FILE_PROPERTY, StringUtils.EMPTY);
            return;
        }

        model.addAttribute(JNJ_PRODUCT_MAIN_IMAGE_FILE_PROPERTY, productUrl);
    }

    protected void setPDFFranchiseLogo(final JnJProductModel product, final Model model) {
        String franchiseMediaPath = StringUtils.EMPTY;
        String franchiseName = product.getFranchiseName();
        if (StringUtils.isNotEmpty(franchiseName)) {
            try{
                final MediaModel picture = mediaService.getMedia(franchiseName);
                if (null != picture && StringUtils.isNotEmpty(picture.getLocation())) {
                    franchiseMediaPath = JnjLaCommonUtil.getMediaPath(configurationService, picture);
                }
            }
            catch (final UnknownIdentifierException e)
            {
                JnjGTCoreUtil.logErrorMessage("Fetching Media Model", "setPDFFranchiseLogo()", EXCEPTION, e,
                        JnjLatamProductPageController.class);
            }
        }
        if (StringUtils.isEmpty(franchiseMediaPath)) {
            final CategoryModel superCategory = product.getSupercategories().iterator().next();
            if (null != superCategory) {
                final MediaModel picture = superCategory.getPicture();
                if (null != picture && StringUtils.isNotEmpty(picture.getLocation())) {
                    franchiseMediaPath = JnjLaCommonUtil.getMediaPath(configurationService, picture);
                }
            }
        }
        model.addAttribute(JNJ_FRANCHISE_LOGO_PDP_FILE_PROPERTY, franchiseMediaPath);
    }

    protected void setPDFJnJLogos(final Model model) {
        final CatalogVersionModel currentCatalog = cmsSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();

        final MediaModel logo = mediaService.getMedia(currentCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
        model.addAttribute(SITE_LOGO_PATH_PROPERTY, logo.getURL());
        final MediaModel logo1 = mediaService.getMedia(currentCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
        model.addAttribute(SITE_LOGO_PATH1_PROPERTY, logo1.getURL());
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public JnJGTProductService getJnJGTProductService() {
        return jnJGTProductService;
    }

    public JnjGTB2BUnitFacade getJnjGTUnitFacade() {
        return jnjGTUnitFacade;
    }

    public void setJnjGTUnitFacade(final JnjGTB2BUnitFacade jnjGTUnitFacade) {
        this.jnjGTUnitFacade = jnjGTUnitFacade;
    }

}
