/**
 *
 */
package com.jnj.b2b.cartandcheckoutaddon.controllers.pages;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.jnj.core.util.JnjGTCoreUtil;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jnj.facades.cart.JnjGTCartFacade;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonConstants;
import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.controllers.AbstractController;
import com.jnj.b2b.storefront.controllers.misc.AddToCartController;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.b2b.storefront.forms.AddToCartForm;
import com.jnj.b2b.storefront.forms.BatchDetailsForm;
import com.jnj.b2b.storefront.forms.ConsignmentChargeForm;
import com.jnj.b2b.storefront.forms.ConsignmentReturnForm;
import com.jnj.b2b.storefront.forms.AddToCartForm;
import com.jnj.b2b.storefront.forms.ConsignmentFillupForm;
import com.jnj.b2b.storefront.forms.UpdateQuantityForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.data.JnjGTCommonFormIOData;
import com.jnj.b2b.cartandcheckoutaddon.forms.JnjAddToCartForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.ProposedLineItemForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.JnjAddToCartCartForm;
import com.jnj.b2b.cartandcheckoutaddon.forms.UpdateMultipleEntriesInCartForm;


/**
 * JnJ Controller for Add to Cart functionality which is not specific to a certain page.
 */
@Controller
@Scope("tenant")
public class JnjGTAddToCartController extends AddToCartController
{
	protected static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	protected static final String ERROR_MSG_TYPE = "errorMsg";
	protected static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";
	protected static final String CART_MOD_DATA = "cartModData";
	protected static final String PRODUCT_INVALID_ERROR_MESSAGE = "productValidationErrorMsg";
	protected static final Logger LOG = Logger.getLogger(JnjGTAddToCartController.class);
	protected static final String LINES = "lines";
	protected static final String ADD_STATUS = "addStatus";
	protected static final String RESULT_EXCEL = "JnjGTCartEntriesExportToExcelView";

	@Autowired
	protected SessionService sessionService;

	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;

	/*@Autowired
	JnjNACartFacade jnjNACartFacade;*/
	@Resource(name="GTCartFacade")
	JnjGTCartFacade jnjGTCartFacade;

	@Autowired
	JnjGTProductFacade jnjGTProductFacade;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;
	

	@Autowired
	protected B2BOrderService b2bOrderService;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	public SessionService getSessionService() {
		return sessionService;
	}

	

	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}

	public JnjGTProductFacade getJnjGTProductFacade() {
		return jnjGTProductFacade;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}
	
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	@PostMapping("/cart/add")
	public String addToCart(final JnjAddToCartForm addToCartForm, final Model model)
	{
		validateParameterNotNull(addToCartForm, "Jnj Add to Cart form is Null.");
		final Map<String, String> selectedProducts = new HashMap<String, String>();
		String[] productCodeAndQty = null;
      
		for (final String selectedProd : addToCartForm.getProductCodeAndQty())
		{
			 
			productCodeAndQty = selectedProd.split(":", 2);
			final String qty = "0".equals(productCodeAndQty[1]) ? "0" : productCodeAndQty[1];
			selectedProducts.put(productCodeAndQty[0], qty);
		}
		//	final List<ProductData> addedProducts = new ArrayList<ProductData>(selectedProducts.size());

		final String currentSite = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		JnjCartModificationData jnjCartModificationData = null;
		jnjCartModificationData = jnjGTCartFacade.addToCartGT(selectedProducts, true, false,false,0);
		model.addAttribute(ADD_STATUS, selectedProducts);
		if (!CollectionUtils.isEmpty(jnjCartModificationData.getCartModifications()))
		{
			
			final CartModificationData cartModificationData = jnjCartModificationData.getCartModifications().get(0);
			if (null != cartModificationData.getEntry())
			{
				
				model.addAttribute("entry", cartModificationData.getEntry());
				model.addAttribute("product", cartModificationData.getEntry().getProduct());
				model.addAttribute(LINES, String.valueOf(jnjCartModificationData.getTotalUnitCount()));
				model.addAttribute(CART_MOD_DATA, jnjCartModificationData);
			}
			if (cartModificationData.getQuantityAdded() == 0L)
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModificationData.getStatusCode());
			}
		} 
			return getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.AddToCartPopup);
	 
	}
	
	/*
	 * This method is called when products are added to cart from a Template.
	 */
	@PostMapping("/cart/addTemplateToCart")
	public String addToCartTemplate(@RequestParam("prodIds") final String prodIds, final Model model)
	{
//		validateParameterNotNull(addToCartForm, "Jnj Add to Cart form is Null.");
		final Map<String, String> selectedProducts = new HashMap<String, String>();
		String[] productCodeAndQty = null;
		if (StringUtils.isNotEmpty(prodIds))
		{
			// Create product code array
			final String[] products = prodIds.replaceAll("\n", "").split(LoginaddonConstants.SYMBOl_COMMA);
		
		for (final String selectedProd : products)
		{
			 
			productCodeAndQty = selectedProd.split(":", 2);
			final String qty = "0".equals(productCodeAndQty[1]) ? "0" : productCodeAndQty[1];
			selectedProducts.put(productCodeAndQty[0], qty);
		}
		//	final List<ProductData> addedProducts = new ArrayList<ProductData>(selectedProducts.size());
		}
		final String currentSite = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		JnjCartModificationData jnjCartModificationData = null;
		jnjCartModificationData = jnjGTCartFacade.addToCartGT(selectedProducts, true, false,false,0);
		model.addAttribute(ADD_STATUS, selectedProducts);
		if (!CollectionUtils.isEmpty(jnjCartModificationData.getCartModifications()))
		{
			
			final CartModificationData cartModificationData = jnjCartModificationData.getCartModifications().get(0);
			if (null != cartModificationData.getEntry())
			{
				
				model.addAttribute("entry", cartModificationData.getEntry());
				model.addAttribute("product", cartModificationData.getEntry().getProduct());
				model.addAttribute(LINES, String.valueOf(jnjCartModificationData.getTotalUnitCount()));
				model.addAttribute(CART_MOD_DATA, jnjCartModificationData);
			}
			if (cartModificationData.getQuantityAdded() == 0L)
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModificationData.getStatusCode());
			}
		} 
			return getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.AddToCartPopup);
	 
	}

	public String addToCart(final String code, final Model model, @Valid final AddToCartForm form,
			final BindingResult bindingErrors, final List<ProductData> addedProducts)
	{
		if (bindingErrors.hasErrors())
		{
			return getViewWithBindingErrorMessages(model, bindingErrors);
		}

		final long qty = form.getQty();

		if (qty <= 0)
		{
			model.addAttribute(ERROR_MSG_TYPE, "basket.error.quantity.invalid");
			model.addAttribute("quantity", Long.valueOf(0L));
		}
		else
		{
			try
			{
				final JnjCartModificationData cartModificationData = jnjGTCartFacade.addToCart(code, String.valueOf(qty));
				//final CartModificationData cartModification = cartFacade.addToCart(code, qty);
				final CartModificationData cartModification = cartModificationData.getCartModifications().get(0);
				model.addAttribute("quantity", Long.valueOf(cartModification.getQuantityAdded()));
				model.addAttribute("entry", cartModification.getEntry());
				if (!CollectionUtils.isEmpty(cartModificationData.getCartModifications())
						&& null != cartModificationData.getCartModifications().get(0).getEntry())
				{
					model.addAttribute(CART_MOD_DATA, cartModificationData);
				}
				if (cartModification.getQuantityAdded() == 0L)
				{
					model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				}
				else if (cartModification.getQuantityAdded() < qty)
				{
					model.addAttribute(ERROR_MSG_TYPE,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				}
			}
			catch (final CommerceCartModificationException ex)
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.error.occurred");
				model.addAttribute("quantity", Long.valueOf(0L));
			}
		}
		final ProductData productData = jnjGTProductFacade.getProductAsAdmin(code, Arrays.asList(ProductOption.BASIC));
		addedProducts.add(productData);
		model.addAttribute("product", productData);
		// Added to get Updated Count on Minicartcomponent
		final CartData cartData = jnjGTCartFacade.getSessionCart();
		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		//return ControllerConstants.Views.Fragments.Cart.AddToCartPopup;
		return "";
	}

	protected void addMultipleProds(final Map<String, String> productCodes, final Model model)
	{
		//
	}

	protected String getViewWithBindingErrorMessages(final Model model, final BindingResult bindingErrors)
	{
		for (final ObjectError error : bindingErrors.getAllErrors())
		{
			if (isTypeMismatchError(error))
			{
				model.addAttribute(ERROR_MSG_TYPE, QUANTITY_INVALID_BINDING_MESSAGE_KEY);
			}
			else
			{
				model.addAttribute(ERROR_MSG_TYPE, error.getDefaultMessage());
			}
		}
		//return ControllerConstants.Views.Fragments.Cart.AddToCartPopup;
		return "";
		
	}

	protected boolean isTypeMismatchError(final ObjectError error)
	{
		return error.getCode().equals(TYPE_MISMATCH_ERROR_CODE);
	}

	@RequestMapping(value = "/cart/addToCart")
	public String addToCart(final JnjAddToCartCartForm addToCartForm, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		try
		{
			if (StringUtils.isEmpty(addToCartForm.getQty()))
			{
				//Set to 0 for showing minimum qty message on FE
				addToCartForm.setQty("0");
			}
			LOG.info("Start:Controller Add to Cart Starts");
			final JnjCartModificationData cartModificationData = jnjGTCartFacade.addToCart(addToCartForm.getProductId(),
					addToCartForm.getQty());
			LOG.info("End>>:Add to Cart Done");

			// Set add to cart status in flash message to show on reveiw cart page
			if (!cartModificationData.getCartModifications().get(0).isError())
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, cartModificationData
						.getCartModifications().get(0).getStatusCode());
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectModel, PRODUCT_INVALID_ERROR_MESSAGE, cartModificationData
						.getCartModifications().get(0).getStatusCode());
			}

		}
		catch (final CommerceCartModificationException exeption)
		{
			LOG.error("Not able to add products" + addToCartForm.getProductId() + " Excption trace" + exeption);
		}
		catch (final NumberFormatException exeption)
		{
			LOG.error("Not able to add products as the entered value for the quantity is not valid");
			return REDIRECT_PREFIX + "/home";
		}
		return REDIRECT_PREFIX + "/cart";
	}

	@PostMapping("cart/update")
	@RequireHardLogIn
	public String updateCartQuantities(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@Valid final UpdateQuantityForm form, final BindingResult bindingResult, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		
		//Changes for Bonus Item
		final Map<Integer, Boolean> quantityUpdateStatus = new HashMap<Integer, Boolean>();

		if (bindingResult.hasErrors())
		{
			for (final ObjectError error : bindingResult.getAllErrors())
			{
				if (error.getCode().equals("typeMismatch"))
				{
					GlobalMessages.addErrorMessage(model, "basket.error.quantity.invalid");
				}
				else
				{
					GlobalMessages.addErrorMessage(model, error.getDefaultMessage());
				}
			}
		}
		else if (cartFacade.getSessionCart().getEntries() != null)
		{
			try
			{
//				final CartData cartData = cartFacade.getSessionCart();
				if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
				{
					for (final OrderEntryData entry : cartData.getEntries())
					{
						if (entry != null && entry.getEntryNumber() == entryNumber)
						{
							final String productCode = entry.getProduct().getCode();

							final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
							if (freeGoodsMap != null)
							{
								final Map<String, JnjGTOutOrderLine> freeGoodsMapModifiable = new HashMap<String, JnjGTOutOrderLine>(
										freeGoodsMap);
								freeGoodsMapModifiable.remove(productCode);
								sessionService.setAttribute("freeGoodsMap", freeGoodsMapModifiable);
							}
						}
					}
				}
				final CartModificationData cartModification = cartFacade.updateCartEntry(entryNumber, form.getQuantity().longValue());
				if (cartModification.getQuantity() == 0)
				{
					// Success in removing entry
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "basket.page.message.remove");
				}
				else
				{
					// Success in update quantity
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "basket.page.message.update");
					quantityUpdateStatus.put(Integer.valueOf(((int) entryNumber)), Boolean.TRUE);
				}
				
				//Changes for Bonus Item
				sessionService.setAttribute("quantityUpdateFlag", quantityUpdateStatus.get(Integer.valueOf((int) entryNumber)));

				// Redirect to the cart page on update success so that the browser doesn't re-post again
				return REDIRECT_PREFIX + "/cart";
			}
			catch (final CommerceCartModificationException ex)
			{
				LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
				//Start GTR-1720
				if (cartData.getOrderType().equalsIgnoreCase("ZKB"))
				{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ex.getMessage());
				}
				//End GTR-1720
			}
		}
		return REDIRECT_PREFIX + "/cart";
	}


/*	@RequestMapping(value = "cart/pcmCart/removeProducts", method = RequestMethod.POST)
	public String removePCMCartProducts(@Valid final RemovePCMCartProductForm removePCMCartProductForm, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (cartFacade.getSessionCart().getEntries() != null)
		{

			final List<String> entryNumbers = removePCMCartProductForm.getEntryNumber();
			for (final String entryNumber : entryNumbers)
			{
				try
				{
					final CartModificationData cartModification = cartFacade.updateCartEntry(Long.parseLong(entryNumber), 0L);
				}
				catch (final NumberFormatException ex)
				{
					LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
				}
				catch (final CommerceCartModificationException ex)
				{
					LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
				}
			}
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "basket.page.message.remove");
		}

		return REDIRECT_PREFIX + "/cart/pcmCart";
	}*/


	/*@RequestMapping(value = "cart/pcmCart/addToPCMCart", method = RequestMethod.POST)
	public String addToPCMCart(final AddToCartCartForm addToCartForm, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		final Map<String, String> responseMap = new HashMap<>();
		try
		{
			if (StringUtils.isEmpty(addToCartForm.getQty()))
			{
				//Set to 0 for showing minimum qty message on FE
				addToCartForm.setQty("1");
			}
			final String productCode = addToCartForm.getProductId();
			responseMap.put(productCode, addToCartForm.getQty());
			final JnjCartModificationData cartModificationData = jnjNACartFacade.addToCartPCM(responseMap, false,
					JnjPCMCoreConstants.PCM, false);
			if (!CollectionUtils.isEmpty(cartModificationData.getCartModifications())
					&& null != cartModificationData.getCartModifications().get(0).getEntry())
			{
				final JnjGTProductData productData = (JnjGTProductData) cartModificationData.getCartModifications()
						.get(CartandcheckoutaddonConstants.NUMBER_ZERO).getEntry().getProduct();
				redirectModel.addFlashAttribute("addedProduct", productData);
			}
			// Set add to cart status in flash message to show on review cart page
			if (responseMap.containsKey(productCode)
					&& StringUtils.equals(responseMap.get(productCode), LoginaddonConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS))
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						JnjPCMCoreConstants.Cart.ADD_TO_CART_SUCCESSFUL);
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectModel, PRODUCT_INVALID_ERROR_MESSAGE, responseMap.get(productCode));
			}
		}
		catch (final NumberFormatException exeption)
		{
			LOG.error("Not able to add products as the entered value for the quantity is not valid");
			return REDIRECT_PREFIX + "/home";
		}
		return REDIRECT_PREFIX + "/cart/pcmCart";
	}*/

	@RequestMapping(value = "/cart/addQuoteToCart")
	public String addQuoteToCart() throws CMSItemNotFoundException
	{
		jnjGTCartFacade.addQuoteToCart();
		return REDIRECT_PREFIX + "/cart";
	}
	
	public String getView(final String view){
        return CartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
 }
	/**
	 * Added as part of NA GT Project
	 * 
	 * @param redirectModel
	 * @param form
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@PostMapping("cart/updateAll")
	public String updateMultipleCartQuantities(final RedirectAttributes redirectModel, final UpdateMultipleEntriesInCartForm form)
			throws CMSItemNotFoundException
	{
		int removedEntriesCount = 0;
		int updatedEntriesCount = 0;
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		if (!CollectionUtils.isEmpty(form.getEntryQuantityList()) && form.getEntryQuantityList() != null)
		{
			for (final String entryQty : form.getEntryQuantityList())
			{
				final String[] entryQtyCombo = StringUtils.split(entryQty, ":");

				if (cartFacade.getSessionCart().getEntries() != null)
				{
					try
					{
						final CartModificationData cartModification = cartFacade.updateCartEntry(Long.valueOf(entryQtyCombo[0]),
								Long.valueOf(entryQtyCombo[1]));
						if (cartModification.getQuantity() == 0)
						{
							// Success in removing entry
							removedEntriesCount++;
							//Changes for Bonus Item
							sessionService.removeAttribute("freeGoodsMap");
						}
						else
						{
							// Success in update quantity
							updatedEntriesCount++;
							//Changes for Bonus Item
							sessionService.removeAttribute("freeGoodsMap");
						}
					}
					catch (final CommerceCartModificationException ex)
					{
						LOG.warn("Couldn't update product with the entry number: " + entryQtyCombo[0] + ".", ex);
						//Start GTR-1720
				if (cartData.getOrderType().equalsIgnoreCase("ZKB"))
				{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ex.getMessage());
				}
				//End GTR-1720
						return REDIRECT_PREFIX + "/cart";
					}
				}
			}
			if (removedEntriesCount > 0)
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						jnjCommonFacadeUtil.getMessageFromImpex("basket.page.message.cartRemove.multipleEntries")
								+ removedEntriesCount);
			}
			if (updatedEntriesCount > 0)
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "basket.page.message.update");


			}
		}
		return REDIRECT_PREFIX + "/cart";
	}

	@GetMapping("/cart/addToCartFromQuote")
	public String addToCartFromQuote(@RequestParam("orderNumber") final String orderNumber, final Model model)
	{
		
		final OrderModel orderModel = b2bOrderService.getOrderForCode(orderNumber);
		
		for(AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			try
			{
				final CartModificationData cartModificationData = jnjGTCartFacade.addToCart(entry.getProduct().getCode(),entry.getQuantity());
				
			}
			catch (final CommerceCartModificationException exeption)
			{
				//LOGGER.error("Not able to add products"  + exeption);	
			}
		}
		return REDIRECT_PREFIX + "/cart";
	}
	
	//AAOL-2405 start
	/**
	 * This method is used to remove a item from the cart using ajax call
	 * @param request 
	 * @param model the model
	 * @param strEntryQuantityList 
	 * @return the string
	 * @throws CMSItemNotFoundException 
	 */ 
	@ResponseBody
	@GetMapping(value = "cart/removeCartItem", produces = "application/json")
	public String removeCartItem(final Model model,final HttpServletRequest request,
			@RequestParam(value = "entryQuantityList", required = true) String strEntryQuantityList) throws CMSItemNotFoundException {
		 
		LOG.info(Jnjb2bCoreConstants.Logging.ADDTOCARTCONT + " - " + Jnjb2bCoreConstants.Logging.ADDTOCARTCONT_REMOVEITEMINCART
					+ "-"  + JnJCommonUtil.getCurrentDateTime());
		return removeCartItemPage(model, request, strEntryQuantityList);
		 
	}
	
	/**
	 * @param model
	 * @param request
	 * @param strEntryQuantityList
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	public String removeCartItemPage(final Model model, final HttpServletRequest request, String strEntryQuantityList) throws CMSItemNotFoundException {
		LOG.info("invoked removeCartItemPage method");
		int removedEntriesCount = 0;
		int updatedEntriesCount = 0;
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		StringBuilder returnMessage = new StringBuilder();
   	if (StringUtils.isNotEmpty(strEntryQuantityList)) {
   		String[] removeProductList = StringUtils.split(strEntryQuantityList, ",");
   		for (final String entryQty : removeProductList) {
   				final String[] entryQtyCombo = StringUtils.split(entryQty, ":");
   				
   				if (!CollectionUtils.isEmpty(cartFacade.getSessionCart().getEntries())) {
   					try {
   							final CartModificationData cartModification = cartFacade.updateCartEntry(Long.valueOf(entryQtyCombo[0]), Long.valueOf(entryQtyCombo[1]));
      						if (cartModification.getQuantity() == 0) {
      							removedEntriesCount++; // Success in removing entry
      							sessionService.removeAttribute("freeGoodsMap"); //Changes for Bonus Item
      						} else {
      							updatedEntriesCount++; // Success in update quantity
      							sessionService.removeAttribute("freeGoodsMap"); //Changes for Bonus Item
      						}
   					}
   					catch (final CommerceCartModificationException ex) {
   						LOG.warn("Couldn't update product with the entry number: " + entryQtyCombo[0] + ".", ex);
         				//Start GTR-1720
         				if (cartData.getOrderType().equalsIgnoreCase("ZKB")) {
         					//GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ex.getMessage());
         				}
         				//End GTR-1720
         				//return REDIRECT_PREFIX + "/cart";
         				returnMessage.append(",").append(GlobalMessages.ERROR_MESSAGES_HOLDER).append(":").append(ex.getMessage());
   					}
   				}
   			}
   			if (removedEntriesCount > 0) {
   				returnMessage.append(",").append( GlobalMessages.CONF_MESSAGES_HOLDER).append(":").append(
   				jnjCommonFacadeUtil.getMessageFromImpex("basket.page.message.cartRemove.multipleEntries")).append(removedEntriesCount);
   			}
   			if (updatedEntriesCount > 0) {
   				returnMessage.append(",").append( GlobalMessages.CONF_MESSAGES_HOLDER ).append(":").append("basket.page.message.update");
   			}
   		}
   	LOG.info(Jnjb2bCoreConstants.Logging.ADDTOCARTCONT + " - " + Jnjb2bCoreConstants.Logging.ADDTOCARTCONT_REMOVEITEMINCART
				+ "-" + JnJCommonUtil.getCurrentDateTime());
		return returnMessage.toString();
	}

	@PostMapping(value = "cart/updateProductLineItem", produces = "application/json")
	public @ResponseBody ProposedLineItemForm updateProductLineItem(final Model model, final HttpServletRequest request, 
			@RequestBody ProposedLineItemForm proposedLineItemForm ) throws CMSItemNotFoundException {
		LOG.info("updateProductLineItem method invoked");
		LOG.info(Jnjb2bCoreConstants.Logging.ADDTOCARTCONT + " - " + "updateProductLineItem"
					+ "-" + JnJCommonUtil.getCurrentDateTime());
		
		return updateProductLineItemPage(model, request, proposedLineItemForm);
	}
	
	public ProposedLineItemForm updateProductLineItemPage(final Model model, final HttpServletRequest request, ProposedLineItemForm proposedLineItemForm) throws CMSItemNotFoundException {
		JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = new JnjGTProposedOrderResponseData();
		List<JnjGTCommonFormIOData> proposedOutOrderItemList = new ArrayList<JnjGTCommonFormIOData>();
		for (JnjAddToCartCartForm jnjAddToCartCartForm : proposedLineItemForm.getJnjAddToCartCartFormList()) {
			LOG.info("getOriginalItemNo : "+jnjAddToCartCartForm.getOriginalItemNo());
			JnjGTCommonFormIOData proposedItenData = new JnjGTCommonFormIOData();
			proposedItenData.setHybrisLineItemNo(jnjAddToCartCartForm.getHybrisLineItemNo());
			proposedItenData.setProposedItemCd(jnjAddToCartCartForm.getProposedItemNo());
			proposedItenData.setOrderItemCd(jnjAddToCartCartForm.getOriginalItemNo());
			proposedOutOrderItemList.add(proposedItenData);
		}
		jnjGTProposedOrderResponseData.setJnjGTCommonFormIODataList(proposedOutOrderItemList);
		jnjGTCartFacade.updateProposedAndOriginalItem(jnjGTProposedOrderResponseData);
		return proposedLineItemForm;
	}
		
	//AAOL-2405 end

@PostMapping(value = "/cart/consigmentReturnAddToCartUrl", produces = "application/json")
public @ResponseBody ConsignmentReturnForm updateCartConsignmentReturn(final Model model, final HttpServletRequest request, 
		@RequestBody ConsignmentReturnForm consignmentReturnForm ) throws CMSItemNotFoundException {
	
	final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
	
	cartData.setPurchaseOrderNumber(consignmentReturnForm.getCustomerPONo());
	cartData.setPoDate(new Date(consignmentReturnForm.getPoDate()));
	cartData.setReturnCreatedDate(new Date(consignmentReturnForm.getReturnCreatedDate()));
	cartData.setShippingInstructions(consignmentReturnForm.getComment());
	cartData.setStockUser(consignmentReturnForm.getStockUser());
	cartData.setEndUser(consignmentReturnForm.getEndUser());
	
	jnjGTCartFacade.updateCartModelReturn(cartData);
	return consignmentReturnForm;
	//return REDIRECT_PREFIX + "/cart";
}

@PostMapping(value = "/cart/consigmentFillUpAddToCartUrl", produces = "application/json")
public @ResponseBody ConsignmentFillupForm updateCartConsignmentFillUp(final Model model, final HttpServletRequest request, 
		@RequestBody ConsignmentFillupForm consignmentFillupForm ) throws CMSItemNotFoundException {

	
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		
		cartData.setPurchaseOrderNumber(consignmentFillupForm.getCustomerPONo());
		cartData.setPoDate(new Date(consignmentFillupForm.getPoDate()));
		cartData.setExpectedShipDate(new Date(consignmentFillupForm.getRequestDelDate()));
		cartData.setShippingInstructions(consignmentFillupForm.getComment());
		cartData.setStockUser(consignmentFillupForm.getStockUser());
		cartData.setEndUser(consignmentFillupForm.getEndUser());
		
		jnjGTCartFacade.updateCartModelReturn(cartData);
		return consignmentFillupForm;
		
	
	}

@PostMapping(value = "/cart/consigmentChargeAddToCartUrl", produces = "application/json")
public @ResponseBody ConsignmentChargeForm updateCartConsignmentCharge(final Model model, final HttpServletRequest request, 
		@RequestBody ConsignmentChargeForm consignmentChargeForm) throws CMSItemNotFoundException {
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		
		cartData.setPurchaseOrderNumber(consignmentChargeForm.getCustomerPONo());
		cartData.setPoDate(new Date(consignmentChargeForm.getPoDate()));
		cartData.setExpectedShipDate(new Date(consignmentChargeForm.getRequestDelDate()));
		cartData.setShippingInstructions(consignmentChargeForm.getComment());
		cartData.setStockUser(consignmentChargeForm.getStockUser());
		cartData.setEndUser(consignmentChargeForm.getEndUser());
		
		if(updateBatchDetails(consignmentChargeForm.getBatchDetails())){
			jnjGTCartFacade.updateCartModelReturn(cartData);
		}
		return consignmentChargeForm;
	}



private boolean updateBatchDetails(List<BatchDetailsForm> batchDetails) {
	boolean isSuccess=false;
	BatchDetailsForm batchDetail;
	for (Iterator iterator = batchDetails.iterator(); iterator.hasNext();) {
		batchDetail = (BatchDetailsForm) iterator.next();
		if(!jnjGTCartFacade.updateBatchDetailsForEntry(Integer.parseInt(batchDetail.getEntryNumber()), batchDetail.getBatchNumber() , batchDetail.getSerialNumber()))
		{
			return false;
		}
		else
		{
			isSuccess= true;
		}
	}
	return isSuccess;
}

	@GetMapping("/cart/exportToExcel")
	public String consigmentFillUpExportToExcel(final ConsignmentFillupForm form, final RedirectAttributes redirectModel,final Model model ,  final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final JnjGTCartData cartData =(JnjGTCartData) jnjGTCartFacade.getSessionCart();
		model.addAttribute("cartData", cartData);
		return  RESULT_EXCEL;
	}
	
	@GetMapping(value = "/cart/isObsoleteProduct", produces = "application/json")
	public @ResponseBody JnjGTCommonFormIOData  isObsoleteProduct(JnjGTCommonFormIOData jnjGTCommonFormIOData, final Model model, 
			@RequestParam(value = "selectedProducts", required = false) String[] selectedProductIds, final HttpServletRequest request) throws CMSItemNotFoundException {
		JnjGTCoreUtil.logInfoMessage("isObsoleteProduct", "isObsoleteProduct", Logging.BEGIN_OF_METHOD, JnjGTAddToCartController.class);
		StringBuilder strProductList = jnjGTProductFacade.getObsoleteProductList(selectedProductIds);
		jnjGTCommonFormIOData.setObsoleteProductList(strProductList.toString());
		JnjGTCoreUtil.logInfoMessage("isObsoleteProduct", "isObsoleteProduct", Logging.END_OF_METHOD, JnjGTAddToCartController.class);
		return jnjGTCommonFormIOData;
	}

	/*
	 * This method used to identify the replacement product is available or not form SAP. If available then need to show the popup to the user selection
	 */
	/*@RequestMapping(value = "/cart/checkReplacementProduct", method =  RequestMethod.GET , produces = "application/json")
	public String checkReplacementProduct( JnjGTCommonFormIOData jnjGTCommonFormIOData, JnjGTSapWsData wsData,  final Model model, 
			final HttpServletRequest request, final RedirectAttributes redirectModel) throws CMSItemNotFoundException {	
		LOG.info("checkReplacementProductPage method invoked");
		ConsignmentChargeForm consignmentChargeForm = new ConsignmentChargeForm();
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue()) {
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("consignmentChargeForm", consignmentChargeForm);	
		return checkReplacementProductPage(jnjGTCommonFormIOData, model,wsData,redirectModel);
	}	*/ 
	
	
	@GetMapping(value = "/cart/checkReplacementProduct", produces = "application/json")
	public String checkReplacementProduct(final Model model, final HttpServletRequest request,
			@RequestParam(value = "obsoleteProductList", required = true) String strObsoleteProductList) throws CMSItemNotFoundException {
		 
		LOG.info(Jnjb2bCoreConstants.Logging.ADDTOCARTCONT + " - " + Jnjb2bCoreConstants.Logging.ADDTOCARTCONT_REMOVEITEMINCART
					+ "-"  + JnJCommonUtil.getCurrentDateTime());
		//return removeCartItemPage(model, request, strObsoleteProductList);
		return checkReplacementProductPage(strObsoleteProductList, model );
		 
	}
	
	/**
	 * @param model
	 * @param request
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	 public String checkReplacementProductPage(String strObsoleteProductList, final Model model ) throws CMSItemNotFoundException {
		  JnjGTSapWsData wsData = new JnjGTSapWsData();
		  // till clarification is received for quantity we are setting quantity for the user to know the quantity which was ordered in the popup.
	      JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = jnjGTCartFacade.checkReplacemenItemForProduct(strObsoleteProductList, wsData);
	      if(!CollectionUtils.isEmpty(jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList())){
	    	  model.addAttribute("replacementItemList", jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList());
	      }
	      return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ReplacementItemPopups);
	}
	 
	 @GetMapping(value = "/cart/checkReplacementProductWithQty", produces = "application/json")
		public String checkReplacementProductWithQty(final Model model, final HttpServletRequest request,
				@RequestParam(value = "obsoleteProductList", required = true) String strObsoleteProductList) throws CMSItemNotFoundException {
			 
			LOG.info(Jnjb2bCoreConstants.Logging.ADDTOCARTCONT + " - " + Jnjb2bCoreConstants.Logging.ADDTOCARTCONT_REMOVEITEMINCART
						+ "-"  + JnJCommonUtil.getCurrentDateTime());
			//return removeCartItemPage(model, request, strObsoleteProductList);
			return checkReplacementProductPageWithQty(strObsoleteProductList, model );
			 
		}
		
		/**
		 * @param model
		 * @param request
		 * @return
		 * @throws CMSItemNotFoundException
		 */
		 public String checkReplacementProductPageWithQty(String strObsoleteProductList, final Model model ) throws CMSItemNotFoundException {
			  JnjGTSapWsData wsData = new JnjGTSapWsData();
			  // till clarification is received for quantity we are setting quantity for the user to know the quantity which was ordered in the popup.
			  long qty= Long.parseLong(strObsoleteProductList.split(":")[1], 10);
			  strObsoleteProductList=strObsoleteProductList.split(":")[0];
		      JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData = jnjGTCartFacade.checkReplacemenItemForProduct(strObsoleteProductList, wsData);
		      jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList().get(0).setOrderItemQty(qty);
		      jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList().get(0).setProposedItemQty(qty);
		      if(!CollectionUtils.isEmpty(jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList())){
		    	  model.addAttribute("replacementItemList", jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList());
		      }
		      return getView(CartandcheckoutaddonControllerConstants.Views.Pages.Cart.ReplacementItemPopups);
				 
		}
}
