/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.controllers;

/**
 */
public interface JnjlacartandcheckoutaddonControllerConstants
{

	String ADDON_PREFIX = "addon:/jnjlacartandcheckoutaddon/";

	interface Views
	{
		interface Pages
		{
			interface Account
			{
				String Contract = "pages/account/contractPage";
				String ContractDetail = "pages/account/contractDetailPage";
			}

			interface Cart
			{
				String CartPage = "/cartPage";
				String CartValidationPage = "/cartValidationPage";
				String CartCheckoutPage = "/cartCheckoutPage";
				String ChangeOrderType = "pages/cart/MDD/delivered/changeOrderType";
				String quoteResultPage = "pages/cart/MDD/quote/quoteResultPage";
				String SavedTemplatePopup = "fragments/cart/saveToTemplate";
				String DeliveryAddressPage = "pages/cart/deliveryAddressPage";
				String DropShipAccountPage = "pages/cart/dropShipAccountPage";
				String SurgeonPopUp = "pages/cart/MDD/delivered/surgeonPopup";
				String SurgeryInfo = "pages/cart/MDD/delivered/surgeryInfo";
				String PriceOverridePopUp = "pages/cart/MDD/delivered/priceOverridePage";
				String ShippingPage = "pages/cart/MDD/standard/shippingPage";
				String PaymentContinuePage = "pages/cart/MDD/standard/paymentContinuePage";
				String OrderReviewPage = "pages/cart/MDD/standard/orderReviewPage";
				String CheckoutConfirmationPage = "/checkoutConfirmationPage";
			}
		}


	}
}
