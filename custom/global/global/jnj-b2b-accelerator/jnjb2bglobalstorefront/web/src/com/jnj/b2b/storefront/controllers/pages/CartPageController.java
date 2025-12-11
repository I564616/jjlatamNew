/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.b2b.facades.flow.impl.SessionOverrideB2BCheckoutFlowFacade;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.b2b.storefront.controllers.ControllerConstants.Views;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.b2b.storefront.forms.UpdateQuantityForm;
import com.jnj.b2b.storefront.variants.VariantSortStrategy;


/**
 * Controller for cart page
 */
/*
 * @Controller
 * 
 * @Scope("tenant")
 * 
 * @RequestMapping(value = "/cart")
 */
public class CartPageController extends AbstractPageController
{
	protected static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	protected static final String ERROR_MSG_TYPE = "errorMsg";
	protected static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";

	protected static final Logger LOG = Logger.getLogger(CartPageController.class);

	protected static final String CART_CMS_PAGE = "cartPage";

	protected static final String CONTINUE_URL = "continueUrl";
	public static final String SUCCESSFUL_MODIFICATION_CODE = "success";

	@Deprecated
	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "cartFacade")
	private de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade b2bCartFacade;


	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "variantSortStrategy")
	private VariantSortStrategy variantSortStrategy;


	@Resource(name = "b2bProductFacade")
	private ProductFacade productFacade;

	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	public de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade getB2bCartFacade()
	{
		return b2bCartFacade;
	}

	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder()
	{
		return resourceBreadcrumbBuilder;
	}

	public VariantSortStrategy getVariantSortStrategy()
	{
		return variantSortStrategy;
	}

	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@GetMapping
	public String showCart(final Model model) throws CMSItemNotFoundException
	{
		prepareDataForPage(model);
		return Views.Pages.Cart.CartPage;
	}

	@GetMapping("/checkout")
	@RequireHardLogIn
	public String cartCheck(final Model model, final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		SessionOverrideB2BCheckoutFlowFacade.resetSessionOverrides();

		if (!cartFacade.hasSessionCart() || cartFacade.getSessionCart().getEntries().isEmpty())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + "/cart";
		}
		if (validateCart(redirectModel))
		{
			return REDIRECT_PREFIX + "/cart";
		}

		// Redirect to the start of the checkout flow to begin the checkout process
		// We just redirect to the generic '/checkout' page which will actually select the checkout flow
		// to use. The customer is not necessarily logged in on this request, but will be forced to login
		// when they arrive on the '/checkout' page.
		return REDIRECT_PREFIX + "/checkout";
	}

	@GetMapping("/getProductVariantMatrix")
	@RequireHardLogIn
	public String getProductVariantMatrix(@RequestParam("productCode") final String productCode, final Model model)
	{

		final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, Arrays.asList(ProductOption.BASIC,
				ProductOption.CATEGORIES, ProductOption.VARIANT_MATRIX_BASE, ProductOption.VARIANT_MATRIX_PRICE,
				ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.VARIANT_MATRIX_STOCK, ProductOption.VARIANT_MATRIX_URL));

		model.addAttribute("product", productData);

		return ControllerConstants.Views.Fragments.Cart.ExpandGridInCart;
	}

	protected boolean validateCart(final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		// Validate the cart
		final List<CartModificationData> modifications = cartFacade.validateCartData();
		if (!modifications.isEmpty())
		{
			redirectModel.addFlashAttribute("validationData", modifications);

			// Invalid cart. Bounce back to the cart page.
			return true;
		}
		return false;
	}

	@ResponseBody
	@PostMapping("/update")
	public CartData updateCartQuantities(@RequestParam("entryNumber") final Integer entryNumber,
			@RequestParam("productCode") final String productCode, final Model model, @Valid final UpdateQuantityForm form,
			final BindingResult bindingErrors) throws CMSItemNotFoundException
	{
		if (bindingErrors.hasErrors())
		{
			getViewWithBindingErrorMessages(model, bindingErrors);
		}
		else
		{
			final CartModificationData cartModification = b2bCartFacade.updateOrderEntry(getOrderEntryData(form.getQuantity(),
					productCode, entryNumber));

			if (cartModification.getStatusCode().equals(SUCCESSFUL_MODIFICATION_CODE))
			{
				GlobalMessages.addMessage(model, GlobalMessages.CONF_MESSAGES_HOLDER, cartModification.getStatusMessage(), null);
			}
			else if (!model.containsAttribute(ERROR_MSG_TYPE))
			{
				GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, cartModification.getStatusMessage(), null);
			}
		}

		return cartFacade.getSessionCart();
	}

	protected void createProductList(final Model model) throws CMSItemNotFoundException
	{
		final CartData cartData = cartFacade.getSessionCart();

		reverseCartProductsOrder(cartData.getEntries());
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{

			for (final OrderEntryData entry : cartData.getEntries())
			{
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entry.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
			}
		}

		model.addAttribute("cartData", cartData);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE));
	}

	protected void reverseCartProductsOrder(final List<OrderEntryData> entries)
	{
		if (entries != null)
		{
			Collections.reverse(entries);
		}
	}

	protected void prepareDataForPage(final Model model) throws CMSItemNotFoundException
	{
		final String continueUrl = (String) getSessionService().getAttribute(WebConstants.CONTINUE_URL);
		model.addAttribute(CONTINUE_URL, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

		final CartRestorationData restorationData = (CartRestorationData) sessionService
				.getAttribute(WebConstants.CART_RESTORATION);
		model.addAttribute("restorationData", restorationData);

		createProductList(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());
	}


	protected String getViewWithBindingErrorMessages(final Model model, final BindingResult bindingErrors)
	{
		for (final ObjectError error : bindingErrors.getAllErrors())
		{
			if (error.getCode().equals(TYPE_MISMATCH_ERROR_CODE))
			{
				model.addAttribute(ERROR_MSG_TYPE, QUANTITY_INVALID_BINDING_MESSAGE_KEY);
			}
			else
			{
				model.addAttribute(ERROR_MSG_TYPE, error.getDefaultMessage());
			}
		}
		return Views.Fragments.Cart.AddToCartPopup;
	}


	protected OrderEntryData getOrderEntryData(final long quantity, final String productCode, final Integer entryNumber)
	{

		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(entryNumber);

		return orderEntry;
	}

}
