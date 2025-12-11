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
package com.jnj.b2b.cartandcheckoutaddon.controllers;

/**
 */
public interface CartandcheckoutaddonControllerConstants
{
	// implement here controller constants used by this extension

	String ADDON_PREFIX = "addon:/cartandcheckoutaddon/";

	interface Views
	{
		interface Fragments
		{
			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
				String ContractPopup = "fragments/cart/contractPopup";
			}
		}

		interface Pages
		{
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
				String ShippingPage = "/shippingPage";
				String PaymentContinuePage = "/paymentContinuePage";
				String OrderReviewPage = "/orderReviewPage";
				String ProposedItemPopups ="pages/cart/MDD/consignmentFillUp/proposedItemPopup";
				String ReplacementItemPopups ="pages/cart/MDD/consignmentFillUp/replacementItemPopup";
				//3088
				String changeAddressDiv = "/pages/cart/changeAddressDiv";
				String changeAddressDivForBill = "/pages/cart/changeAddressDivForBill";
			}

			interface Checkout
			{
				String CheckoutConfirmationPage = "/checkoutConfirmationPage";
				String CheckoutBatchConfirmationPage = "/checkoutBatchConfirmationPage";

			}
			
			interface Account
			{
				String ContractDetail = "pages/account/contractDetailPage";
				String Contract = "pages/account/contractPage";
			}
		}
	}
}
