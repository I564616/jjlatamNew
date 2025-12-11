/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.controllers.pages;

import com.jnj.core.util.JnjLatamGetCurrentDefaultB2BUnitUtil;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.cartandcheckoutaddon.controllers.pages.JnjGTCartPageController;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.b2b.storefront.forms.UpdateQuantityForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.dataload.mapper.UserRolesData;
import com.jnj.core.dto.FileUploadDTO;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjFileUploadToSharedFolderUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.JnjLatamSAPErrorMessageFacade;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.order.JnjLatamCheckoutFacade;
import com.jnj.facades.company.JnjB2BUnitFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.la.b2b.cartandcheckoutaddon.controllers.JnjlacartandcheckoutaddonControllerConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjIndirectPayerModel;
import com.jnj.la.core.util.JnjLaCoreUtil;

import com.jnj.la.core.data.ReplacementProductData;
import com.jnj.la.b2b.cartandcheckoutaddon.forms.JnjLaProductReplacementForm;
import org.springframework.util.ObjectUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * @author nbanerj1
 *
 */

public class JnjLatamCartPageController extends JnjGTCartPageController
{
	private static final Class currentClass = JnjLatamCartPageController.class;

	private static final String ORDER_VALIDATION_PAGE = "orderValidationPage";

	private static final String CART_VALIDATION_PAGE = "cartValidationPage";

	private static final String CART_CMS_PAGE = "cartPage";

	private static final String REVIEW_ORDER_PAGE = "Review Order Page";

	private static final String CART_PAGE_URL = "/cart";

	private static final String CART_DATA = "cartData";

	private static final String SHOW_ATP_FLAG_MAP = "showATPFlagMap";

	private static final String PRODUCT = "product";

	private static final String ADD_STATUS = "addStatus";

	private static final String IS_TEMPLATE_ADD_FLAG = "isTemplateAdd";

	private static final String ADD_STATUS_FOR_SESSION = "previousExceutionIsTemplateAdd";

	private static final String IS_TEMPLATE_ADD_FLAG_FOR_SESSION = "previousExceutionAddTemplateStatus";

	private static final String LINES = "lines";

	private static final String VALIDATE_CART = "cart.review.validateOrder";

	private static final String ORDER_REVIEW = "cart.review.orderReview";

	private static final String IS_VALIDATE_PAGE = "isValidatePage";

	private static final String DROPSHIMENT_ERROR = "dropshipmentError";

	private static final String DROPSHIMENT_ERROR_PRODUCT_CODE_MAP = "dropshipmentErrorProductMap";

	private static final String HIDE_CHECKOUT = "hideCheckOut";

	private static final String CartValidationPage = "pages/cart/cartValidationPage";

	private static final String INDIRECT_CUSTOMER_HEADER = "displayIndirectCustomerHeader";

	private static final String INDIRECT_PAYER_HEADER = "displayIndirectPayerHeader";

	private static final String INDIRECT_CUSTOMER_LINE = "displayIndirectCustomerLine";

	private static final String INDIRECT_PAYER_LINE = "displayIndirectPayerLine";

	private static final String USER_ROLE = "userRolesData";

	private static final String INDIRECT_CUSTOMER = "indirectCustomer";

	private static final String INDIRECT_PAYER = "indirectPayer";

	private static final String _1 = "1";

	private static final String _0 = "0";

	private static final String UPDATE_NAMED_EXPECTED_DELIVERY_DATE_FAILED = "Method updateNamedExpectedDeliveryDate() returned 'false' for Entry Number:::::::::::::::::";

	@Autowired
	protected ConfigurationService configurationService;
	@Autowired
	private JnjLatamSAPErrorMessageFacade jnjSAPErrorMessageFacade;

	@Autowired
	private JnjLatamCartFacade jnjLatamCartFacade;
	
	@Autowired
	private JnjLatamCheckoutFacade jnjLatamCheckoutFacade;

	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
	private JnjLatamGetCurrentDefaultB2BUnitUtil jnjLatamGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	private JnjB2BUnitFacade jnjB2BUnitFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelService modelService;

	@Autowired
	protected ProductFacade productFacade;

	@Autowired
	private JnjFileUploadToSharedFolderUtil jnjFileUploadToSharedFolderUtil;

	@Autowired
	protected JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	@Autowired
	private CMSSiteService cmsSiteService;
	
	private JnjConfigService jnjConfigService;

	@Override
	protected String showCartPage(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final String methodName = "showCartPage()";
		LOGGER.info("Start: Cart Page Loading");

		try
		{
			super.prepareDataForCartPage(model, request);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(CART_CMS_PAGE, methodName,
					"An exception has occured in the GT scope when calling the method: prepareDataForCartPage(model, request)",
					exception, JnjLatamCartPageController.class);
		}
		this.prepareDataForPage(model, request.getSession(false));
		LOGGER.info("End>>: Cart Page Loading");

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final Object allProdQuantityValue = sessionService.getAttribute("allProdQuantity");
		final Object fileAllProdQuantityValue = sessionService.getAttribute("fileAllProdQuantity");

		final String responseStatus = (String) model.asMap().get(ADD_STATUS);
		final Boolean isTemplateAdd = (Boolean) model.asMap().get(IS_TEMPLATE_ADD_FLAG);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "inside showCartPage :: " + responseStatus + " :: " + isTemplateAdd,
				JnjLatamCartPageController.class);

		if (isTemplateAdd == Boolean.TRUE)
		{
			sessionService.setAttribute(ADD_STATUS_FOR_SESSION, responseStatus);
			sessionService.setAttribute(IS_TEMPLATE_ADD_FLAG_FOR_SESSION, isTemplateAdd);
		}
		else
		{
			final String responseStatusForDisplay = (String) sessionService.getAttribute(ADD_STATUS_FOR_SESSION);
			final Boolean isTemplateAddForDisplay = (Boolean) sessionService.getAttribute(IS_TEMPLATE_ADD_FLAG_FOR_SESSION);
			if (responseStatusForDisplay != null && isTemplateAddForDisplay != null)
			{
				model.addAttribute(ADD_STATUS, responseStatusForDisplay);
				model.addAttribute(IS_TEMPLATE_ADD_FLAG, isTemplateAddForDisplay);
			}
			sessionService.removeAttribute(IS_TEMPLATE_ADD_FLAG_FOR_SESSION);
			sessionService.removeAttribute(ADD_STATUS_FOR_SESSION);
		}

		if (null != allProdQuantityValue && ((Boolean) allProdQuantityValue).booleanValue())
		{
			model.addAttribute("allProdQuantityStatus", allProdQuantityValue);
			sessionService.removeAttribute("allProdQuantity");
		}
		if (null != fileAllProdQuantityValue && ((Boolean) fileAllProdQuantityValue).booleanValue())
		{
			model.addAttribute("fileAllProdQuantityStatus", fileAllProdQuantityValue);
			sessionService.removeAttribute("fileAllProdQuantity");
		}

		final String viewPath = jnjLatamCartFacade
				.getPathForView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartPage, null);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "cart page path : " + viewPath, JnjLatamCartPageController.class);
		return getView(viewPath);
	}

	@PostMapping("/laAddTocart")
	public String laAddQuickTocart(@RequestParam("prodId_Qtys") final String prodId_Qtys, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final String methodName = "laAddQuickTocart()";
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "showChangeAccountLink-value : " + showChangeAccountLink,
				JnjLatamCartPageController.class);

		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName,
					"Entered condition...........showChangeAccountLink: " + showChangeAccountLink, JnjLatamCartPageController.class);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final Map<String, String> responseMap = new HashMap<>();
		final String[] products = prodId_Qtys.split(LoginaddonConstants.SYMBOl_COMMA);

		for (final String product : products)
		{
			final String[] product_Qq = product.split(LoginaddonConstants.Solr.COLON);
			final String productCode = product_Qq[0].trim();
			final String productQuantity = product_Qq[1].trim();

			if (StringUtils.isNotBlank(productCode))
			{
				if (responseMap.containsKey(productCode))
				{
					final int currentQuantity = Integer.parseInt(responseMap.get(productCode));
					final int newQuantity = Integer.parseInt(productQuantity);
					responseMap.put(product_Qq[0].trim(), String.valueOf(currentQuantity + newQuantity));
				}
				else
				{
					responseMap.put(productCode, productQuantity);
				}
			}
		}
		// Add all products to cart
		jnjLatamCartFacade.addMultipleProds(responseMap, model, false, false, 0);

		model.addAttribute(ADD_STATUS, responseMap);
		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjLatamCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));

		final HttpSession session = request.getSession(false);
		prepareDataForPage(model, session);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);

		return REDIRECT_PREFIX + "/cart/reviseOrder";
	}

	@GetMapping("/laReviseOrder")
	public String reviseCart(final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		redirectModel.addFlashAttribute(ADD_STATUS, model.asMap().get(ADD_STATUS));
		redirectModel.addFlashAttribute(IS_TEMPLATE_ADD_FLAG, model.asMap().get(IS_TEMPLATE_ADD_FLAG));
		return REDIRECT_PREFIX + "/cart";
	}


	@PostMapping("/laAddToCartOrderTemplate")
	public String laAddToCartOrderTemplate(@RequestParam("prodId_Qtys") final String prodId_Qtys,
			@RequestParam(value = "isTemplateAdd", required = false) final String isTemplateAdd, final Model model,
			final HttpServletRequest request, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final String methodName = "laAddQuickTocart()";
		final String PRODUCT_ADD_STATUS = "SUCCESS";
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "showChangeAccountLink value: " + showChangeAccountLink,
				JnjLatamCartPageController.class);

		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName,
					"Entered condition...........showChangeAccountLink: " + showChangeAccountLink, JnjLatamCartPageController.class);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final Map<String, String> responseMap = new HashMap<>();
		final String[] products = prodId_Qtys.split(LoginaddonConstants.SYMBOl_COMMA);

		for (final String product : products)
		{
			final String[] productQuantity = product.split(LoginaddonConstants.Solr.COLON);
			final String productCode = productQuantity[0].trim();
			final String productQty = productQuantity[1].trim();

			if (StringUtils.isNotBlank(productCode))
			{
				if (responseMap.containsKey(productCode))
				{
					final int currentQuantity = Integer.parseInt(responseMap.get(productCode));
					final int newQuantity = Integer.parseInt(productQty);
					responseMap.put(productCode.trim(), String.valueOf(currentQuantity + newQuantity));
				}
				else
				{
					responseMap.put(productCode, productQty);
				}
			}
		}
		// Add all products to cart
		jnjLatamCartFacade.addMultipleProds(responseMap, model, false, false, 0);

		model.addAttribute(ADD_STATUS, responseMap);
		StringBuilder errorProducts = null;
		if (isTemplateAdd != null && Boolean.parseBoolean(isTemplateAdd))
		{
			for (final Map.Entry<String, String> entry : responseMap.entrySet())
			{
				final String key = entry.getKey();
				final String value = entry.getValue();

				if (errorProducts == null && !value.equals(PRODUCT_ADD_STATUS))
				{
					errorProducts = new StringBuilder(key);
				}
				else if (errorProducts != null && !value.equals(PRODUCT_ADD_STATUS))
				{
					errorProducts.append("," + key);
				}
			}
			if (errorProducts != null)
			{
				redirectModel.addFlashAttribute(ADD_STATUS, errorProducts.toString());
			}
			redirectModel.addFlashAttribute(IS_TEMPLATE_ADD_FLAG, Boolean.valueOf(isTemplateAdd));
		}
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "errorProducts :: " + errorProducts,
				JnjLatamCartPageController.class);

		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjLatamCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));

		final HttpSession session = request.getSession(false);
		prepareDataForPage(model, session);
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);
		return REDIRECT_PREFIX + "/cart/laReviseOrder";
	}

	@Override
	public String validateCart(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel)
	{
		final String methodName = "validateCart()";
		JnjGTCoreUtil.logInfoMessage("Validate", methodName, "Latam", currentClass);

		JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName, "Cart validation starts", currentClass);

		JnjValidateOrderData validateOrderData = null;
		setHideCheckoutFlag(model, Boolean.FALSE);
		model.addAttribute(DROPSHIMENT_ERROR, Boolean.FALSE);
		boolean isException = false;

		/* headerErrorMessageList will containt the SAP error Messages comming at header level. */
		final List<String> headerErrorMessageList = new ArrayList<>();
		/* lineErrorMessageMap<K,V> K - ProductCode, V - Error Message List */
		final Map<String, List<String>> lineErrorMessageMap = new HashMap<>();

		sessionService.removeAttribute("contractEntryList");

		try
		{
			final String countryInfo = jnjLatamGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
			JnjGTCoreUtil.logInfoMessage("Validate", methodName, "Latam validateOrder method try", currentClass);

			if (Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA.equalsIgnoreCase(countryInfo))
			{
				final Map<String, String> productShippingMap = jnjLatamCartFacade.getShippingDetails();
				if (productShippingMap != null)
				{
					model.addAttribute("productShippingMap", productShippingMap);
				}
			}
			addFreightTypeInModel(model, countryInfo);

			validateOrderData = jnjLatamCartFacade.validateOrder(wsData);
			
			model.addAttribute("country", countryInfo);
		}
		catch (final SystemException systemException)
		{
			isException = true;
			JnjGTCoreUtil.logErrorMessage("Validate", methodName,
					Logging.CREATE_ORDER + Logging.HYPHEN + "System Exception occurred " + systemException.getMessage(),
					systemException, currentClass);
		}
		catch (final IntegrationException integrationException)
		{
			isException = true;
			JnjGTCoreUtil.logErrorMessage("Validate", methodName,
					Logging.CREATE_ORDER + Logging.HYPHEN + "Integration Exception occurred " + integrationException.getMessage(),
					integrationException, currentClass);
		}

		catch (final Throwable throwable)
		{
			isException = true;
			JnjGTCoreUtil.logErrorMessage("Validate", methodName, Logging.CREATE_ORDER + Logging.HYPHEN
					+ "Exception occurred other then integration exception " + throwable.getMessage(), throwable, currentClass);
		}

		if (isException)
		{
			setHideCheckoutFlag(model, Boolean.TRUE);
		}

		try
		{
			if (null != validateOrderData && validateOrderData.getSapErrorResponse() != null
					&& validateOrderData.getSapErrorResponse().booleanValue() && !isException)
			{
				setHideCheckoutFlag(model, Boolean.TRUE);
				/* Generate Error Header Error Message Lists and Line Error Message Map */
				jnjSAPErrorMessageFacade.populateSapErrorDetails(headerErrorMessageList, lineErrorMessageMap, validateOrderData);
				model.addAttribute("headerErrorMessageList", headerErrorMessageList);
				model.addAttribute("lineErrorMessageMap", lineErrorMessageMap);
			}

			sessionService.setAttribute("validationFlag", Boolean.TRUE);

			// Changes for Dropshipment validation
			final Map<String, String> codesNotFound = sessionService.getAttribute("codesNotFound");
			if (codesNotFound != null && !codesNotFound.isEmpty())
			{
				setHideCheckoutFlag(model, Boolean.TRUE);
				model.addAttribute(DROPSHIMENT_ERROR, Boolean.TRUE);
				model.addAttribute(DROPSHIMENT_ERROR_PRODUCT_CODE_MAP, codesNotFound);
				model.addAttribute("headerErrorMessageList", headerErrorMessageList);
				model.addAttribute("lineErrorMessageMap", lineErrorMessageMap);
				sessionService.setAttribute("validationFlag", Boolean.FALSE);
				if (validateOrderData != null)
				{
					validateOrderData.setSapErrorResponse(Boolean.TRUE);
				}
			}
			prepareDataForPage(model, false);

			boolean splitCart = false;
			if (sessionService.getAttribute("populateSpliCart") != null)
			{
				splitCart = sessionService.getAttribute("populateSpliCart");
			}
			if (splitCart)
			{
				populateSplitCarts(model);
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_VALIDATION_PAGE));
		}
		catch (final CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage("Validate", methodName,
					"Cart ValidationPage page not found in current content" + exception, currentClass);
		}
		model.addAttribute("validateOrderData", validateOrderData);
		model.addAttribute("displaySubstitutes", jnjLatamCartFacade.displaySubstitutes());
		populateFreeItemFromSession(model);

		final List<Breadcrumb> breadCrumb = new ArrayList<>();
		breadCrumb.add(new Breadcrumb("/cart", jnjCommonFacadeUtil.getMessageFromImpex(ORDER_REVIEW), null));
		breadCrumb.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex(VALIDATE_CART), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadCrumb);
		model.addAttribute(IS_VALIDATE_PAGE, Boolean.TRUE);
		model.addAttribute(SHOW_ATP_FLAG_MAP, jnjLatamCommonFacadeUtil.buildShowATPFlagMap(model, "cartData"));

		jnjLatamCommonFacadeUtil.checkoutCurrentSiteNotNull();

		final String ViewPath = jnjGTCartFacade
				.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartValidationPage, null);
		sessionService.setAttribute("validatedone", Boolean.TRUE);

		JnjGTCoreUtil.logInfoMessage("Validate", methodName,
				"Latam validate order method getView(ViewPath) :: " + getView(ViewPath), currentClass);
		return getView(ViewPath);
	}

	private void addFreightTypeInModel(Model model, String countryInfo) {
		if (StringUtils.isNotEmpty(countryInfo) && countryInfo.equals(Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL)) {
			JnJLaB2BUnitModel currentB2bUnit = (JnJLaB2BUnitModel) jnjLatamGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

			if (StringUtils.isNotEmpty(currentB2bUnit.getIndustryCode1()) && currentB2bUnit.getIndustryCode1().equals(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.DISTRIBUTOR_INDUSTRY_CODE))) {
				model.addAttribute("freightType", currentB2bUnit.getCustomerFreightType());
			}
		}
	}

	/**
	 * @param model
	 * @param value
	 *           to be set: true or false
	 */
	private void setHideCheckoutFlag(final Model model, final Boolean value)
	{
		sessionService.setAttribute(HIDE_CHECKOUT, value);
		model.addAttribute(HIDE_CHECKOUT, value);
	}

	public Long updateQuantity(final OrderEntryData entry, final Long entryQuantity)
	{
		Long quantity = null;

		if (sessionService != null)
		{
			final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			if (freeGoodsMap != null && entry != null && freeGoodsMap.containsKey(entry.getProduct().getCode()))
			{
				final String freeItemsQuantity = freeGoodsMap.get(entry.getProduct().getCode()).getMaterialQuantity();
				final JnjOutOrderLine outOrderLine = freeGoodsMap.get(entry.getProduct().getCode());
				final double outOrderLineQty = Double.parseDouble(outOrderLine.getOrderedQuantity());
				final long orderedQuantity = (long) outOrderLineQty;
				final long chargedQuantity = entry.getQuantity();
				final long totalQuantity = Long.parseLong(freeItemsQuantity) + chargedQuantity;
				if (chargedQuantity == entryQuantity)
				{
					if (totalQuantity == orderedQuantity)
					{
						quantity = orderedQuantity;
					}
					else if (totalQuantity != orderedQuantity)
					{
						quantity = chargedQuantity;
					}
					else
					{
						quantity = totalQuantity;
					}
				}
				else
				{
					quantity = entryQuantity;
				}
			}
			else
			{
				quantity = entryQuantity;
			}
		}
		else
		{
			quantity = entryQuantity;
		}
		return quantity;
	}

	@Override
	protected void createProductList(final Model model) throws CMSItemNotFoundException
	{
		final CartData cartData = cartFacade.getSessionCart();
		final String methodName = "createProductList()";
		JnjGTProductData lastBaseProduct;
		boolean checkIsMitekProduct;
		boolean isMitekProduct = false;
		Long updateQuantity;

		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				lastBaseProduct = (JnjGTProductData) entry.getProduct();
				checkIsMitekProduct = StringUtils.equals(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK),
						lastBaseProduct.getSalesOrgCode());
				if (checkIsMitekProduct)
				{
					isMitekProduct = true;
				}
				final UpdateQuantityForm updateQuantityForm = new UpdateQuantityForm();
				updateQuantity = this.updateQuantity(entry, entry.getQuantity());
				entry.setQuantity(updateQuantity);
				updateQuantityForm.setQuantity(updateQuantity);
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), updateQuantityForm);
			}
		}
		jnjGTCartFacade.setShippingMethodOnOrderType((JnjGTCartData) cartData);

		model.addAttribute("cartData", cartData);
		sessionService.setAttribute("jnjCartData", cartData);
		model.addAttribute("isMitekProduct", isMitekProduct);
		model.addAttribute("overridePriceContractFlg",
				Config.getParameter(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_CONTRACTNUM_FLG));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		JnjGTCoreUtil.logInfoMessage("Change account", methodName, "showChangeAccountLink value :: " + showChangeAccountLink,
				currentClass);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			JnjGTCoreUtil.logInfoMessage("Entered condition", methodName, "showChangeAccountLink :: " + showChangeAccountLink,
					currentClass);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (cartData instanceof JnjGTCartData && ((JnjGTCartData) cartData).isContainsOCDProduct())
		{
			GlobalMessages.addInfoMessage(model, "ocd.product.alert.message");
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE));
	}

	@Override
	public String shipping(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel, final String freightType)
			throws CMSItemNotFoundException
	{
		LOGGER.info("Entered shipping() in JnjLatamCartPageController.java");
		boolean splitCart = false;
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		
		if (StringUtils.isNotEmpty(freightType)) {
		  jnjLatamCartFacade.setCustomerFreightType(freightType);
		}
						
		prepareDataForPage(model, true);
		populateFreeItemFromSession(model);

		/** Changes for Dropshipment and Order Split */
		if (sessionService.getAttribute("populateSpliCart") != null)
		{
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if (splitCart)
		{
			populateSplitCarts(model);
		}

		final CartModel sessionCart = getCartService().getSessionCart();

		LOGGER.info("shipping address" + sessionCart.getDeliveryAddress().getLine1());
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());

		final List<AddressData> shippingAddressess = jnjGTUnitFacade.getShippingAddresses();
		LOGGER.info("before removal" + shippingAddressess.size());

		for (int i = 0; i < shippingAddressess.size(); i++)
		{
			LOGGER.info("jnjid  " + ((JnjGTAddressData) shippingAddressess.get(i)).getJnjAddressId());
			if (shippingAddressess.get(i) instanceof JnjGTAddressData)
			{
				if (((JnjGTAddressData) shippingAddressess.get(i)).getJnjAddressId() == sessionCart.getDeliveryAddress()
						.getJnJAddressId())
				{
					shippingAddressess.remove(i);
				}
			}
		}


		LOGGER.info("after removal" + shippingAddressess.size());
		model.addAttribute("shippingAddressess", shippingAddressess);


		final String orderTypes = ((JnjLaCartData) jnjGTCartFacade.getSessionCart()).getOrderType();

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		final JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		model.addAttribute("checkoutoption", Config.getParameter(BYEPASS_CHECKOUT_OPTION));
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex("Shopping Cart"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("Shipping"), null));

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

		model.addAttribute("sessionlanguagePattern", jnjLatamCartFacade.getLanguageSpecificDatePattern(super.getCurrentLanguage()));

		LOGGER.info("Exiting shipping() in JnjLatamCartPageController.java");

		return getView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Cart.ShippingPage);
	}

	@Override
	public String checkoutCart(final Model model, final JnjGTSapWsData wsData) throws CMSItemNotFoundException
	{
		LOGGER.info("in the latam checkout method");

		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		prepareDataForPage(model, false);

		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());


		final String orderTypes = ((JnjLaCartData) cartFacade.getSessionCart()).getOrderType();

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final String ViewPath = jnjGTCartFacade
				.getPathForView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.CartCheckoutPage, null);

		if (cartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}

		// to remove cart on clicking back button
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if (sessionService.getAttribute("validatedone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();

			return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if (sessionService.getAttribute("confirmationdone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();
			sessionService.setAttribute("confirmationdone", Boolean.FALSE);
			return REDIRECT_PREFIX + "/cart";
		}


		boolean splitCart = false;
		if (sessionService.getAttribute("populateSpliCart") != null)
		{
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if (splitCart)
		{
			populateSplitCarts(model);
		}
		return getView(ViewPath);

	}

	@Override
	public String getView(final String view)
	{
		return JnjlacartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;

	}

	@Override
	public String paymentContinue(final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);		
		final CartModel cartModel = getCartService().getSessionCart();
						
		LOGGER.info("In Latam processPaymentContinue()");
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			LOGGER.error("Order Checkout not found in current content", exeption);
		}
		prepareDataForPage(model, false);

		populateFreeItemFromSession(model);

		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		
		JnJLaB2BUnitModel b2bUnit = (JnJLaB2BUnitModel) jnjLatamGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		model.addAttribute("b2bUnitFileUploadFlag", b2bUnit.getStdOrderFileUpload());
		model.addAttribute("fileTypes",jnjConfigService.getConfigValueById("standOrderFileTypes"));
		model.addAttribute("fileSize",jnjConfigService.getConfigValueById("standOrderFileSize"));


		final String orderTypes = ((JnjLaCartData) jnjGTCartFacade.getSessionCart()).getOrderType();
		

		final Boolean isContractCart = Boolean.valueOf(((JnjLaCartData) jnjGTCartFacade.getSessionCart()).isIsContractCart());

		LOGGER.info("isContractCart :: " + isContractCart);

		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		prepareDataForCartPage(model, request);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		model.addAttribute("isContractCart", isContractCart);
		final JnJB2BUnitModel currentB2bUnit = (JnJB2BUnitModel) sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		
		JnJLaB2BUnitModel jnjB2bUnitModel = (JnJLaB2BUnitModel) cartModel.getUnit();
		LOGGER.debug("current site for commercial user " + currentSite);
		if (!ObjectUtils.isEmpty(jnjB2bUnitModel) && StringUtils.isNotBlank(jnjB2bUnitModel.getIndustryCode1())
				&& configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.ELIGIBLE_INDUSTRY_CODES_FOR_COMMERCIAL_USER, StringUtils.EMPTY).contains(jnjB2bUnitModel.getIndustryCode1())
				&& jnjLatamCommonFacadeUtil.isCommercialUserEnabledForCurrentSite()) {
			model.addAttribute("govtCodeIndicator",  Boolean.TRUE);
			LOGGER.debug("commercial user - govtCodeIndicator : TRUE ");
		} else {
			model.addAttribute("govtCodeIndicator",  Boolean.FALSE);
			LOGGER.debug("commercial user - govtCodeIndicator : FALSE ");
		}

		if (jnjGTCartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}

		// to remove cart on clicking back button
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if (sessionService.getAttribute("validatedone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();

			return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if (sessionService.getAttribute("confirmationdone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();
			sessionService.setAttribute("confirmationdone", Boolean.FALSE);
			return REDIRECT_PREFIX + "/cart";
		}

		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex("Shopping Cart"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("Payment"), null));

		setPaymetricField(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

		boolean splitCart = false;
		if (sessionService.getAttribute("populateSpliCart") != null)
		{
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if (splitCart)
		{
			populateSplitCarts(model);
		}

		model.addAttribute("sessionlanguagePattern", jnjLatamCartFacade.getLanguageSpecificDatePattern(super.getCurrentLanguage()));

		return getView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Cart.PaymentContinuePage);
	}

	private void populateFreeItemFromSession(final Model model)
	{
		if (sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP) != null)
		{
			final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			sessionService.setAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP, freeGoodsMap);
			model.addAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP, freeGoodsMap);
		}
	}

	@PostMapping("/reviewOrder")
	public String reviewOrder(@RequestParam(value = "deliveredOrderDoc", required = false) final CommonsMultipartFile[] files,
			final Model model, final JnjGTSapWsData wsData, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final String methodName = "reviewOrder()";
		JnjGTCoreUtil.logDebugMessage(REVIEW_ORDER_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		boolean isUploadSuccess = false;
		try
		{
			if (null != files && files.length > 0 && StringUtils.isNotEmpty(files[0].getFileItem().getName()))
			{
				final List<String> empenhoFilesFullPathList = new ArrayList<>();
				for (final CommonsMultipartFile file : files)
				{
					final FileUploadDTO fileUploadDTO = new FileUploadDTO();
					fileUploadDTO.setFile(file);
					fileUploadDTO.setRenameFileTo(file.getFileItem().getName());

					final String destainationDir = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
							+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
							+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SHARED_FOLDER_LOCATION_ORDER);
					fileUploadDTO.setFileDirInShareFolder(destainationDir);
					isUploadSuccess = jnjFileUploadToSharedFolderUtil.uploadFileToSharedFolder(fileUploadDTO, destainationDir);

					JnjGTCoreUtil.logInfoMessage(REVIEW_ORDER_PAGE, methodName,
							"File Upload to local folder status :" + isUploadSuccess, JnjLatamCartPageController.class);

					final String fileFullPath = destainationDir.concat(File.separator).concat(file.getOriginalFilename());

					addUplodedFileToEmpenhoList(isUploadSuccess, fileFullPath, empenhoFilesFullPathList);
				}
				sessionService.setAttribute(Jnjlab2bcoreConstants.EMPENHO_FILES_FULL_PATH, empenhoFilesFullPathList);
				jnjLatamCheckoutFacade.setEmpenhoFilesFullPath(empenhoFilesFullPathList);
				JnjGTCoreUtil.logInfoMessage(REVIEW_ORDER_PAGE, methodName, "File Uploaded total:" + empenhoFilesFullPathList.size(),
						JnjLatamCartPageController.class);
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage("Review", methodName, "No files to be Upload:Peform Normall Checkout", currentClass);
			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CHECKOUT_PAGE));

		}
		catch (final CMSItemNotFoundException exeption)
		{
			JnjGTCoreUtil.logErrorMessage(REVIEW_ORDER_PAGE, methodName, "Order Checkout not found in current content", exeption,
					currentClass);
		}
		catch (final Exception exeption)
		{
			JnjGTCoreUtil.logErrorMessage(REVIEW_ORDER_PAGE, methodName,
					"An exception ocurred while preparing the data for the review order page.", exeption, currentClass);
		}
		prepareDataForPage(model, false);
		populateFreeItemFromSession(model);

		LOGGER.info("isUploadSuccess :: " + isUploadSuccess);
		model.addAttribute("shippingRouteMap", jnjGTCartFacade.getShippingRouteAndNameMap());
		model.addAttribute("isUploadSuccess", Boolean.valueOf(isUploadSuccess));

		final String orderTypes = ((JnjLaCartData) jnjGTCartFacade.getSessionCart()).getOrderType();
		
		/** Checking if the order type is delivered order "ZDEL" **/
		if (orderTypes != null && orderTypes.equalsIgnoreCase(JnjOrderTypesEnum.ZDEL.getCode()))
		{
			/** Adding the show signature flag **/
			model.addAttribute(SHOW_SIGNATURE, Boolean.TRUE);
		}
		setFalgForcaribPuertCustomer(model);
		model.addAttribute("timeOutExtended", Boolean.valueOf(wsData.isTimeOutExtended()));
		final JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		model.addAttribute("indicator", currentB2bUnit.getIndicator());

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		if (jnjGTCartFacade.getSessionCart().getEntries().isEmpty())
		{
			return REDIRECT_PREFIX + "/cart";
		}

		// to remove cart on clicking back button
		sessionService.setAttribute("validatedone", Boolean.FALSE);
		if (sessionService.getAttribute("validatedone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();

			return REDIRECT_PREFIX + "/cart";
		}
		sessionService.setAttribute("validatedone", Boolean.TRUE);
		if (sessionService.getAttribute("confirmationdone") == Boolean.TRUE)
		{
			jnjGTCartFacade.removeSessionCart();
			sessionService.setAttribute("confirmationdone", Boolean.FALSE);
			return REDIRECT_PREFIX + "/cart";
		}

		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("/cart/validate", jnjCommonFacadeUtil.getMessageFromImpex("Shopping Cart"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("Order Review"), null));


		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

		boolean splitCart = false;
		if (sessionService.getAttribute("populateSpliCart") != null)
		{
			splitCart = sessionService.getAttribute("populateSpliCart");
		}
		if (splitCart)
		{
			populateSplitCarts(model);
		}


		JnjGTCoreUtil.logDebugMessage(REVIEW_ORDER_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);
		model.addAttribute("sessionlanguagePattern", jnjLatamCartFacade.getLanguageSpecificDatePattern(super.getCurrentLanguage()));

		return getView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Cart.OrderReviewPage);
	}

	private void addUplodedFileToEmpenhoList(final boolean isUploadSuccess, final String fileFullPath,
			final List<String> empenhoFilesFullPathList)
	{
		final String methodName = "addUplodedFileToEmpenhoList()";
		if (isUploadSuccess)
		{
			JnjGTCoreUtil.logInfoMessage(REVIEW_ORDER_PAGE, methodName, "File Uploaded full path:" + fileFullPath,
					JnjLatamCartPageController.class);
			empenhoFilesFullPathList.add(fileFullPath);
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(REVIEW_ORDER_PAGE, methodName,
					"An error occured while trying to upload the file: " + fileFullPath + " to the shared folder",
					JnjLatamCartPageController.class);
		}
	}

	/**
	 * This method is used to Split Cart data and validate required details.
	 *
	 * @param model
	 */
	@Override
	protected void populateSplitCarts(final Model model)
	{
		final List<JnjLaCartData> jnjCartDataList = sessionService.getAttribute("jnjCartDataList");
		// Stops split population if there is none or only one item in the cart
		if (jnjCartDataList != null && jnjCartDataList.size() == 1)
		{
			final List<OutOrderLines3> outOrderLinesList = sessionService.getAttribute("outOrderLinesList");
			OutOrderLines3 outOrderLine = null;
			for (final OrderEntryData entry : jnjCartDataList.get(0).getEntries())
			{
				outOrderLine = jnjGTCartFacade.getOutOrderLinesResult(entry.getProduct().getCode(), outOrderLinesList);
			}
			model.addAttribute("splitCart", Boolean.FALSE);
		}

		final JnjLaCartData jnjCartData = sessionService.getAttribute("jnjCartData");
		List<OrderEntryData> orderEntryDataList;

		// Loop all split carts
		if (jnjCartDataList != null && jnjCartDataList.size() > 1)
		{
			for (final JnjLaCartData splitCartData : jnjCartDataList)
			{
				//Finds product from split cart in the original cart and adds the whole entry to the split cart
				orderEntryDataList = new ArrayList<>();

				if (CollectionUtils.isNotEmpty(splitCartData.getEntries()))
				{
					for (final OrderEntryData entry : splitCartData.getEntries())
					{
						if (entry != null && entry.getProduct() != null)
						{
							final String productCode = entry.getProduct().getCode();
							orderEntryDataList.add(getOrderDataEntry(productCode, jnjCartData));
							splitCartData.setContractId(jnjCartData.getContractId());

							splitCartData.setEntries(orderEntryDataList);
							jnjGTCartFacade.calculateAllTotals(splitCartData);
						}
					}
				}
			}
			model.addAttribute("splitCart", Boolean.TRUE);
			model.addAttribute("jnjCartDataList", jnjCartDataList);
		}
		else
		{
			model.addAttribute("splitCart", Boolean.FALSE);
		}
	}

	/**
	 * Finds an order entry given a product code
	 *
	 * @param productCode
	 * @param cartData
	 * @return
	 */
	@Override
	protected OrderEntryData getOrderDataEntry(final String productCode, final CartData cartData)
	{
		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			if (orderEntryData.getProduct().getCode().equalsIgnoreCase(productCode))
			{
				return orderEntryData;
			}
		}
		return null;
	}

	@Override
	protected String clearCart()
	{
		getCartService().removeSessionCart();
		sessionService.removeAttribute("splitMap");
		sessionService.removeAttribute("contractEntryList");
		sessionService.removeAttribute("freeGoodsMap");
		return REDIRECT_PREFIX + "/cart";
	}

	@GetMapping(value = "/getIndirectCustomerName", produces = "text/plain")
	@ResponseBody
	@RequireHardLogIn
	public String getIndirectCustomerName(@RequestParam("indirectCustomer") final String indirectCustomer,
			@RequestParam(value = "cartEntryId", required = false) final int cartEntryId)
	{
		final String methodName = "getIndirectCustomerName()";
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		String indirectCustName = "";

		final CountryModel siteDefaultCountry = cmsSiteService.getCurrentSite().getDefaultCountry();
		if (null != siteDefaultCountry && indirectCustomer != null && !indirectCustomer.isEmpty())
		{
			indirectCustName = jnjLatamCartFacade.getIndirectCustomerName(siteDefaultCountry.getIsocode(), indirectCustomer);
		}

		if (!indirectCustName.isEmpty())
		{
			jnjLatamCartFacade.updateIndirectCustomer(indirectCustomer, indirectCustName, cartEntryId);
		}
		else
		{
			jnjLatamCartFacade.updateIndirectCustomer(Jnjlab2bcoreConstants.EMPTY_STRING, Jnjlab2bcoreConstants.EMPTY_STRING,
					cartEntryId);
		}

		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);

		return indirectCustName;
	}

	@GetMapping(value = "/getIndirectPayerName", produces = "text/plain")
	@ResponseBody
	@RequireHardLogIn
	public String getIndirectPayerName(@RequestParam("indirectPayer") final String indirectPayer,
			@RequestParam(value = "cartEntryId", defaultValue = "-1", required = false) final int cartEntryId)
	{
		final String methodName = "getIndirectPayerName()";
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		String indirectPayerName = "";
		final CountryModel siteDefaultCountry = cmsSiteService.getCurrentSite().getDefaultCountry();

		if (null != siteDefaultCountry && indirectPayer != null && !indirectPayer.isEmpty())
		{
			indirectPayerName = jnjLatamCartFacade.getIndirectPayerName(siteDefaultCountry.getIsocode(), indirectPayer);
		}

		if (!indirectPayerName.isEmpty())
		{
			jnjLatamCartFacade.updateIndirectPayer(indirectPayer, indirectPayerName, cartEntryId);
		}
		else
		{
			jnjLatamCartFacade.updateIndirectPayer(Jnjlab2bcoreConstants.EMPTY_STRING, Jnjlab2bcoreConstants.EMPTY_STRING,
					cartEntryId);
		}

		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);

		return indirectPayerName;
	}

	@GetMapping("/getIndirectCustomerOrPayerData")
	@ResponseBody
	public List<String> getIndirectCustomerOrPayerData(@RequestParam("indirectType") final String indirectType,
			@RequestParam("term") final String term)
	{
		final String methodName = "getIndirectCustomerOrPayerData()";

		final CountryModel siteDefaultCountry = cmsSiteService.getCurrentSite().getDefaultCountry();
		List<String> indirectDataList = null;
		if (indirectType != null && siteDefaultCountry != null && term != null)
		{
			JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, "Country: " + siteDefaultCountry.getIsocode(),
					JnjLatamCartPageController.class);
			final String termUpperCase = term.toUpperCase();
			if (indirectType.equals(INDIRECT_CUSTOMER))
			{
				indirectDataList = jnjLatamCartFacade.getIndirectCustomer(siteDefaultCountry.getIsocode(), termUpperCase);
			}
			else if (indirectType.equals(INDIRECT_PAYER))
			{
				indirectDataList = jnjLatamCartFacade.getIndirectPayer(siteDefaultCountry.getIsocode(), termUpperCase);
			}
		}
		return indirectDataList;
	}

	@GetMapping(value = "/getIndirectNameForModal", produces = "text/plain")
	@ResponseBody
	public String getIndirectNameForModal(@RequestParam("indirectNumber") final String indirectNumber,
			@RequestParam(value = "searchType", required = false) final String searchType)
	{
		final String methodName = "getIndirectNameForModal()";
		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		if (indirectNumber == null || searchType == null)
		{
			return StringUtils.EMPTY;
		}

		String indirectName = StringUtils.EMPTY;
		final CountryModel siteDefaultCountry = cmsSiteService.getCurrentSite().getDefaultCountry();

		if (null != siteDefaultCountry)
		{
			if ("Customer".equals(searchType))
			{
				indirectName = jnjLatamCartFacade.getIndirectCustomerName(siteDefaultCountry.getIsocode(), indirectNumber);
			}
			else if ("Payer".equals(searchType))
			{
				indirectName = jnjLatamCartFacade.getIndirectPayerName(siteDefaultCountry.getIsocode(), indirectNumber);
			}
		}

		JnjGTCoreUtil.logInfoMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);

		return indirectName;
	}

	private String getIndirectPayerName(final JnjIndirectPayerModel jnjIndirectPayerModel)
	{
		if (jnjIndirectPayerModel != null)
		{
			return jnjIndirectPayerModel.getIndirectPayerName();
		}
		return null;
	}

	private String getIndirectCustomerName(final JnjIndirectCustomerModel jnjIndirectCustomerModel)
	{
		if (jnjIndirectCustomerModel != null)
		{
			return jnjIndirectCustomerModel.getIndirectCustomerName();
		}
		return null;
	}

	protected void prepareDataForPage(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		final String methodName = "prepareDataForPage()";
		try
		{
			super.prepareDataForPage(model, false);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(CART_CMS_PAGE, methodName,
					"An exception has occured in the GT scope when calling the method: prepareDataForPage(model, false)", exception,
					JnjLatamCartPageController.class);
		}

		JnjGTCoreUtil.logDebugMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		prepateIndirectCustomerAndPayerData(session);
		setUpProductListFlags(session);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		JnjGTCoreUtil.logDebugMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);
	}


	private void setUpProductListFlags(final HttpSession session) throws CMSItemNotFoundException
	{
		final CartData cartData = jnjLatamCartFacade.getSessionCart();

		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			int entryNumer = 0;
			for (final OrderEntryData orderEntryData : cartData.getEntries())
			{
				setupIndirectCustomerAndPayerHeaderAndLineFlags(entryNumer, orderEntryData, session);
				entryNumer++;
			}
		}
	}

	private void setupIndirectCustomerAndPayerHeaderAndLineFlags(final int entryNumber, final OrderEntryData entry,
			final HttpSession session)
	{

		final ProductData product = entry.getProduct();

		List<Boolean> displayIndirectCustomerLine = (List<Boolean>) session.getAttribute(INDIRECT_CUSTOMER_LINE);
		if (displayIndirectCustomerLine == null)
		{
			displayIndirectCustomerLine = new ArrayList<>();
		}

		List<Boolean> displayIndirectPayerLine = (List<Boolean>) session.getAttribute(INDIRECT_PAYER_LINE);
		if (displayIndirectPayerLine == null)
		{
			displayIndirectPayerLine = new ArrayList<>();
		}

		UserRolesData userRolesData = (UserRolesData) session.getAttribute(USER_ROLE);
		if (userRolesData == null)
		{
			userRolesData = jnjLatamGetCurrentDefaultB2BUnitUtil.getUserRoles();
		}

		if (product instanceof JnjLaProductData)
		{
			final String sector = ((JnjLaProductData) product).getSector();
			if (sector != null && sector.equalsIgnoreCase(Jnjb2bCoreConstants.MDD) && userRolesData.isIndirectCustomer())
			{
				// Shows header for indirect customer if at least one product in the cart is from MDD
				session.setAttribute(INDIRECT_CUSTOMER_HEADER, Boolean.TRUE);
				addOrUpdateLineLevelFlag(displayIndirectCustomerLine, entryNumber, Boolean.TRUE);

				// Show header for indirect payer if at least one product in the cart is from MDD and the customer is from Argentina
				final CountryModel countryModel = jnjLatamGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
				final List<String> indirectPayerEnabledCountriesList = JnjLaCoreUtil
						.getCountriesList(Jnjlab2bcoreConstants.KEY_INDIRECT_PAYER_VALID_COUNTRIES);

				if (JnjLaCoreUtil.isCountryValidForACountriesList(countryModel.getIsocode(), indirectPayerEnabledCountriesList))
				{
					session.setAttribute(INDIRECT_PAYER_HEADER, Boolean.TRUE);
					addOrUpdateLineLevelFlag(displayIndirectPayerLine, entryNumber, Boolean.TRUE);
				}
				else
				{
					addOrUpdateLineLevelFlag(displayIndirectPayerLine, entryNumber, Boolean.FALSE);
				}
			}
			else
			{
				addOrUpdateLineLevelFlag(displayIndirectCustomerLine, entryNumber, Boolean.FALSE);
				addOrUpdateLineLevelFlag(displayIndirectPayerLine, entryNumber, Boolean.FALSE);
			}

			setIndirectLineToSession(displayIndirectCustomerLine, session, INDIRECT_CUSTOMER_LINE);
			setIndirectLineToSession(displayIndirectPayerLine, session, INDIRECT_PAYER_LINE);
		}
	}


	private void setIndirectLineToSession(final List<Boolean> displayIndirectLine, final HttpSession session, final String string)
	{
		if (!displayIndirectLine.isEmpty())
		{
			session.setAttribute(string, displayIndirectLine);
		}
	}


	private void addOrUpdateLineLevelFlag(final List<Boolean> indirectListLineFlags, final int entryNumber, final Boolean value)
	{
		if (indirectListLineFlags.size() <= entryNumber)
		{
			indirectListLineFlags.add(value);
		}
		else
		{
			indirectListLineFlags.set(entryNumber, value);
		}
	}


	/**
	 * @param session
	 */
	private void prepateIndirectCustomerAndPayerData(final HttpSession session)
	{
		final String methodName = "prepateIndirectCustomerAndPayerData()";

		JnjGTCoreUtil.logDebugMessage(CART_CMS_PAGE, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCartPageController.class);

		final Boolean indirectCustomerHeader = (Boolean) session.getAttribute(INDIRECT_CUSTOMER_HEADER);
		if (indirectCustomerHeader != null)
		{
			session.removeAttribute(INDIRECT_CUSTOMER_HEADER);
		}

		final Boolean indirectPayerHeader = (Boolean) session.getAttribute(INDIRECT_PAYER_HEADER);
		if (indirectPayerHeader != null)
		{
			session.removeAttribute(INDIRECT_PAYER_HEADER);
		}

		final UserRolesData userRolesData = jnjLatamGetCurrentDefaultB2BUnitUtil.getUserRoles();

		session.setAttribute(USER_ROLE, userRolesData);

		JnjGTCoreUtil.logDebugMessage(CART_CMS_PAGE, methodName, Logging.END_OF_METHOD, JnjLatamCartPageController.class);
	}

	@PostMapping("/replaceProducts")	
	public String replaceProducts(final Model model, final HttpServletRequest request, final JnjLaProductReplacementForm jnjLaProductReplacementForm)
			throws CMSItemNotFoundException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final List<ReplacementProductData> replacementProductsDataList = new ArrayList<>();
		ReplacementProductData replacementProductData = null;
		String[] originalCartEntryNumbers = null;
		String[] originalProductCodes = null;
		String[] replacementProductCodes = null;
		String[] replacementProductQuantities = null;

		if (jnjLaProductReplacementForm != null) {
			originalCartEntryNumbers = jnjLaProductReplacementForm.getOriginalCartEntryNumber().split(LoginaddonConstants.SYMBOl_COMMA);
			originalProductCodes = jnjLaProductReplacementForm.getOriginalProductCode().split(LoginaddonConstants.SYMBOl_COMMA);
			replacementProductCodes = jnjLaProductReplacementForm.getReplacementProductCode().split(LoginaddonConstants.SYMBOl_COMMA);
			replacementProductQuantities = jnjLaProductReplacementForm.getReplacementProductQuantity().split(LoginaddonConstants.SYMBOl_COMMA);
		}

		if (null != originalCartEntryNumbers) {
		for (int i=0; i < originalCartEntryNumbers.length; i++) {
			replacementProductData = new ReplacementProductData();
			replacementProductData.setEntryNumber(Integer.valueOf(originalCartEntryNumbers[i].trim()));
			replacementProductData.setOriginalProductCode(originalProductCodes[i].trim());
			replacementProductData.setQuantity(Long.valueOf(replacementProductQuantities[i].trim()));
			if(!originalProductCodes[i].equals(replacementProductCodes[i])){
				replacementProductData.setReplacementProductCode(replacementProductCodes[i].trim());
			}
			replacementProductsDataList.add(replacementProductData);
		}
		}

		if (CollectionUtils.isNotEmpty(cartFacade.getSessionCart().getEntries()) && cartModel != null) {
			jnjLatamCartFacade.replaceProducts(cartModel, replacementProductsDataList);
		}
		return REDIRECT_PREFIX + CART_PAGE_URL;
	}

	@Override
	public boolean updateShippingDate(final Model model, @RequestParam("expectedShipDate") final String expectedShipDate,

			@RequestParam("entryNumber") final String entryNumber)

			throws CMSItemNotFoundException
	{
		boolean success = false;

		String language = null;

		if (super.getCurrentLanguage() != null)
		{
			language = super.getCurrentLanguage().getIsocode();
		}

		if (StringUtils.isNotEmpty(expectedShipDate) && StringUtils.isNumeric(entryNumber))
		{
			LOGGER.info("LATAM set expectedShipDate for single cart entry ::::::::::::::::::::" + expectedShipDate);
			success = jnjLatamCartFacade.updateNamedExpectedDeliveryDate(expectedShipDate, Integer.valueOf(entryNumber), language);
		}
		else if (StringUtils.isNotEmpty(expectedShipDate) && !StringUtils.isNumeric(entryNumber)) {
			final CartData cartData = jnjLatamCartFacade.getSessionCart();
			success = hasSuccessUpdatingShippingDate(expectedShipDate, success, language, cartData);
		}

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		return success;
	}

	private boolean hasSuccessUpdatingShippingDate (@RequestParam ("expectedShipDate") String expectedShipDate, boolean success, String language, CartData cartData) {
		LOGGER.info("LATAM set expectedShipDate for  all cart entries :::::::::::::::::" + expectedShipDate);
		if(null != cartData.getEntries()) {
			for (final OrderEntryData entry : cartData.getEntries()) {
				success = jnjLatamCartFacade.updateNamedExpectedDeliveryDate(expectedShipDate, entry.getEntryNumber(), language);
				if (!success && cartData.getEntries().size() > 1) {
					LOGGER.warn(UPDATE_NAMED_EXPECTED_DELIVERY_DATE_FAILED + entry.getEntryNumber());
				}
			}
		}
		return success;
	}

	protected String updateShippingAddressInCart(final Model model, final String shippingAddrId, final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		session.setAttribute("updateShippingAddressClicked", Boolean.FALSE);
		if (null != shippingAddrId && (!shippingAddrId.isEmpty())) {

			final AddressData shippingAddData = jnjLatamCartFacade.chnageShippingAddress(shippingAddrId, request);

			model.addAttribute("deliveryAddress", shippingAddData);
			session.setAttribute("defaultChekAddid", shippingAddrId);
		}
		if (sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME) != null && Jnjb2bCoreConstants.MDD
				.equalsIgnoreCase(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME).toString())) {
			session.setAttribute("skipping", "billing");
			return REDIRECT_PREFIX + "/cart";
		} else {
			/*Added the below changes for consumer ECPH-2154*/
			return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.DeliveryAddressPage);
		}
	}

	@ResponseBody
	@GetMapping("/updateComplementaryInfo")
	public boolean updateComplementaryInfo(final Model model, @RequestParam("complementaryInfo") final String complementaryInfo)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);

		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return jnjLatamCartFacade.updateComplementaryInfo(complementaryInfo);
	}

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public void setJnjConfigService(JnjConfigService jnjConfigService) {
		this.jnjConfigService = jnjConfigService;
	}

}
