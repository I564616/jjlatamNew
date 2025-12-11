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
package com.jnj.b2b.storefront.controllers;

import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.acceleratorcms.model.components.PurchasedCategorySuggestionComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;


/**
 * Class with constants for controllers.
 */
public interface ControllerConstants
{
	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";

			/**
			 * Default CMS component controller
			 */
			String DefaultCMSComponent = _Prefix + "DefaultCMSComponentController";

			/**
			 * CMS components that have specific handlers
			 */
			String PurchasedCategorySuggestionComponent = _Prefix + PurchasedCategorySuggestionComponentModel._TYPECODE + _Suffix;
			String ProductReferencesComponent = _Prefix + ProductReferencesComponentModel._TYPECODE + _Suffix;
			String ProductCarouselComponent = _Prefix + ProductCarouselComponentModel._TYPECODE + _Suffix;
			String MiniCartComponent = _Prefix + MiniCartComponentModel._TYPECODE + _Suffix;
			String ProductFeatureComponent = _Prefix + ProductFeatureComponentModel._TYPECODE + _Suffix;
			String CategoryFeatureComponent = _Prefix + CategoryFeatureComponentModel._TYPECODE + _Suffix;
			String NavigationBarComponent = _Prefix + NavigationBarComponentModel._TYPECODE + _Suffix;
			String CMSLinkComponent = _Prefix + CMSLinkComponentModel._TYPECODE + _Suffix;
		}
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Cms
		{
			String ComponentPrefix = "cms/";
		}

		interface Pages
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountHomePage = "pages/account/accountHomePage";
				String AccountOrderHistoryPage = "pages/account/accountOrderHistoryPage";
				String AccountOrderPage = "pages/account/accountOrderPage";
				String AccountProfilePage = "pages/account/accountProfilePage";
				String AccountProfileEditPage = "pages/account/accountProfileEditPage";
				String AccountProfileEmailEditPage = "pages/account/accountProfileEmailEditPage";
				String AccountChangePasswordPage = "pages/account/accountChangePasswordPage";
				String AccountAddressBookPage = "pages/account/accountAddressBookPage";
				String AccountEditAddressPage = "pages/account/accountEditAddressPage";
				String AccountPaymentInfoPage = "pages/account/accountPaymentInfoPage";
				String AccountMyQuotesPage = "pages/account/accountMyQuotesPage";
				String AccountReplenishmentSchedule = "pages/account/accountReplenishmentSchedule";
				String AccountReplenishmentScheduleDetails = "pages/account/accountReplenishmentScheduleDetails";
				String AccountOrderApprovalDashboardPage = "pages/account/accountOrderApprovalDashboardPage";
				String AccountOrderApprovalDetailsPage = "pages/account/accountOrderApprovalDetailsPage";
				String AccountQuoteDetailPage = "pages/account/accountQuoteDetailPage";
				String AccountCancelActionConfirmationPage = "pages/account/accountCancelActionConfirmationPage";
				String AccountBuildOrderpage = "pages/account/accountbuildOrderPage";
			}
			
			interface home
			{
				String HomePage = "pages/home/home";
			}

			interface Checkout
			{
				String CheckoutLoginPage = "pages/checkout/checkoutLoginPage";
				String CheckoutConfirmationPage = "pages/checkout/checkoutConfirmationPage";
				String QuoteCheckoutConfirmationPage = "pages/checkout/quoteCheckoutConfirmationPage";
				String CheckoutReplenishmentConfirmationPage = "pages/checkout/checkoutReplenishmentConfirmationPage";
			}

			interface SingleStepCheckout
			{
				String CheckoutSummaryPage = "pages/checkout/single/checkoutSummaryPage";
			}

			interface MultiStepCheckout
			{
				String CheckoutSampleLandingPage = "pages/checkout/multi/checkoutSampleLandingPage";
			}

			interface Password
			{
				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
			}

			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
			}

			interface Cart
			{
				String CartPage = "pages/cart/cartPage";
			}

			interface StoreFinder
			{
				String StoreFinderSearchPage = "pages/storeFinder/storeFinderSearchPage";
				String StoreFinderDetailsPage = "pages/storeFinder/storeFinderDetailsPage";
			}

			interface Misc
			{
				String MiscRobotsPage = "pages/misc/miscRobotsPage";
			}

			interface MyCompany
			{
				String MyCompanyLoginPage = "pages/company/myCompanyLoginPage";
				String MyCompanyManageUnitsPage = "pages/company/myCompanyManageUnitsPage";
				String MyCompanyManageUnitEditPage = "pages/company/myCompanyManageUnitEditPage";
				String MyCompanyManageUnitDetailsPage = "pages/company/myCompanyManageUnitDetailsPage";
				String MyCompanyManageUnitCreatePage = "pages/company/myCompanyManageUnitCreatePage";
				String MyCompanyManageBudgetsPage = "pages/company/myCompanyManageBudgetsPage";
				String MyCompanyManageBudgetsViewPage = "pages/company/myCompanyManageBudgetsViewPage";
				String MyCompanyManageBudgetsEditPage = "pages/company/myCompanyManageBudgetsEditPage";
				String MyCompanyManageBudgetsAddPage = "pages/company/myCompanyManageBudgetsAddPage";
				String MyCompanyManageCostCentersPage = "pages/company/myCompanyManageCostCentersPage";
				String MyCompanyCostCenterViewPage = "pages/company/myCompanyCostCenterViewPage";
				String MyCompanyCostCenterEditPage = "pages/company/myCompanyCostCenterEditPage";
				String MyCompanyAddCostCenterPage = "pages/company/myCompanyAddCostCenterPage";
				String MyCompanyManagePermissionsPage = "pages/company/myCompanyManagePermissionsPage";
				String MyCompanyManageUnitUserListPage = "pages/company/myCompanyManageUnitUserListPage";
				String MyCompanyManageUnitApproverListPage = "pages/company/myCompanyManageUnitApproversListPage";
				String MyCompanyManageUserDetailPage = "pages/company/myCompanyManageUserDetailPage";
				String MyCompanyManageUserAddEditFormPage = "pages/company/myCompanyManageUserAddEditFormPage";
				String MyCompanyManageUsersPage = "pages/company/myCompanyManageUsersPage";
				String MyCompanyManageUserDisbaleConfirmPage = "pages/company/myCompanyManageUserDisableConfirmPage";
				String MyCompanyManageUnitDisablePage = "pages/company/myCompanyManageUnitDisablePage";
				String MyCompanySelectBudgetPage = "pages/company/myCompanySelectBudgetsPage";
				String MyCompanyCostCenterDisableConfirm = "pages/company/myCompanyDisableCostCenterConfirmPage";
				String MyCompanyManageUnitAddAddressPage = "pages/company/myCompanyManageUnitAddAddressPage";
				String MyCompanyManageUserPermissionsPage = "pages/company/myCompanyManageUserPermissionsPage";
				String MyCompanyManageUserResetPasswordPage = "pages/company/myCompanyManageUserPassword";
				String MyCompanyBudgetDisableConfirm = "pages/company/myCompanyDisableBudgetConfirmPage";
				String MyCompanyManageUserGroupsPage = "pages/company/myCompanyManageUserGroupsPage";
				String MyCompanyManageUsergroupViewPage = "pages/company/myCompanyManageUsergroupViewPage";
				String MyCompanyManageUsergroupEditPage = "pages/company/myCompanyManageUsergroupEditPage";
				String MyCompanyManageUsergroupCreatePage = "pages/company/myCompanyManageUsergroupCreatePage";
				String MyCompanyManageUsergroupDisableConfirmationPage = "pages/company/myCompanyManageUsergroupDisableConfirmationPage";
				String MyCompanyManagePermissionDisablePage = "pages/company/myCompanyManagePermissionDisablePage";
				String MyCompanyManagePermissionsViewPage = "pages/company/myCompanyManagePermissionsViewPage";
				String MyCompanyManagePermissionsEditPage = "pages/company/myCompanyManagePermissionsEditPage";
				String MyCompanyManagePermissionTypeSelectPage = "pages/company/myCompanyManagePermissionTypeSelectPage";
				String MyCompanyManagePermissionAddPage = "pages/company/myCompanyManagePermissionAddPage";
				String MyCompanyManageUserCustomersPage = "pages/company/myCompanyManageUserCustomersPage";
				String MyCompanyManageUserGroupPermissionsPage = "pages/company/myCompanyManageUserGroupPermissionsPage";
				String MyCompanyManageUserGroupMembersPage = "pages/company/myCompanyManageUserGroupMembersPage";
				String MyCompanyRemoveDisableConfirmationPage = "pages/company/myCompanyRemoveDisableConfirmationPage";
				String MyCompanyManageUserB2BUserGroupsPage = "pages/company/myCompanyManageUserB2BUserGroupsPage";
				String MyCompanyManageUsergroupRemoveConfirmationPage = "pages/company/myCompanyManageUsergroupRemoveConfirmationPage";
			}

			interface Product
			{
				String OrderForm = "pages/product/productOrderFormPage";
			}
		}

		interface Fragments
		{
			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
				String MiniCartPanel = "fragments/cart/miniCartPanel";
				String MiniCartErrorPanel = "fragments/cart/miniCartErrorPanel";
				String CartPopup = "fragments/cart/cartPopup";
				String ExpandGridInCart = "fragments/cart/expandGridInCart";
			}

			interface Checkout
			{
				String TermsAndConditionsPopup = "fragments/checkout/termsAndConditionsPopup";
                String ReadOnlyExpandedOrderForm = "fragments/checkout/readOnlyExpandedOrderForm";
			}

			interface SingleStepCheckout
			{
				String DeliveryAddressFormPopup = "fragments/checkout/single/deliveryAddressFormPopup";
				String PaymentDetailsFormPopup = "fragments/checkout/single/paymentDetailsFormPopup";
			}

			interface Password
			{
				String PasswordResetRequestPopup = "fragments/password/passwordResetRequestPopup";
				String ForgotPasswordValidationMessage = "fragments/password/forgotPasswordValidationMessage";
			}

			interface Product
			{
				String FutureStockPopup = "fragments/product/futureStockPopup";
				String QuickViewPopup = "fragments/product/quickViewPopup";
				String ZoomImagesPopup = "fragments/product/zoomImagesPopup";
				String ReviewsTab = "fragments/product/reviewsTab";
				String ProductLister = "fragments/product/productLister";
			}
		}
	}
       
      //GTR-1682 Starts
	
	interface JnjGTExcelPdfViewLabels
	{
		

		interface OrderDetail
		{
			String MANUAL_REVIEW = "Manual Review:";
			String CUSTOMER_PO = "Customer PO";
			String INVOICE_NUMBER = "Invoice Number";
			String REASON_FOR_REJECTION = "Reason for Rejection";
			String RETURNS_GOODS_AUTHORIZATION = "Return Goods Authorization:";
			String APPROVER = "Approver";
			String ORDER_NUMBER = "Order Number";
			String ACCOUNT_NUMBER = "Account Number:";
			String SHIP_TO_ADDRESS = "Ship-To Address";
			String ATTENTION = "Attention";
			String BILLING_NAME_ADDRESS = "Billing Name and Address";
			String DEALER_PO = "Dealer PO :";
			String DROP_SHIP_ACC = "Drop-Ship Account";
			String REQ_DELIVERY_DATE = "Requested Delivery Date";
			String EXPECTED_DELIVERY_DATE = "Expected Delivery Date";
			String ACTUAL_SHIP_DATE = "Actual Ship Date";
			String ACTUAL_DELIVERY_DATE = "Actual Delivery Date";
			String SHIPMENT_LOCATION = "Shipment Location";
			String LINE_STATUS = "Line Status";
			String PRODUCT_NAME = "Product Name";
			String PRODUCT_ID = "Product ID";
			String GTIN = "GTIN";
			String UNIT_OF_MEASURE = "Unit of Measure";
			String QUANTITY = "Quantity";
			String UNIT_PRICE = "Unit Item Price";
			String TOTAL_PRICE = "Total";
			String ORDER_CHANNEL = "order.channel.";
			String SURGEON_NAME = "Surgeon Name";
			String DISTRIBUTOR_PO_NUMBER = "Distributor PO:";
			String SHIP_TO_ACCOUNT = "Ship-To Account";
			String LOT_NUMBER = "Lot Number";
			String LOT_COMMENT = "Lot Comment";
			String CONTRACT_NUMBER = "Contract Number";
			String SPECIAL_STOCK_PARTNER = "Special Stock Partner";
			String EXTENDED_PRICE = "Extended Price";
			String ESTIMATED_DELIVERY = "Estimated Delivery Date";
			String SHIPPING_METHOD = "Shipping Method";
			String PRICE_OVERRIDE = "Price Override RC";
			String ORDER_INFORMATION = "Order Information";
			String BILLING_INFORMATION = "Billing Information";
			String SHIPPING_INFORMATION = "Shipping Information";
			String JNJ_ORDER_NUMBER = "J&J Order Number:";
			String PO_NUMBER = "Purchase Order:";
			String ORDER_DATE = "Order Date:";
			String ORDER_STATUS = "Status :";
			String ORDER_TYPE = "Order Type :";
			String ORDER_BY = "Ordered By :";
			String NUMBER_OF_LINES = "Number of Lines:";
			String SOLD_TO_ACC_NUMBER = "Sold-To Account Number :";
			String GLN = "GLN :";
			String PAYMENT_METHOD = "Payment method :";
			String CREDIT_CARD_TYPE = "Credit Card Type :";
			String CREDIT_CARD_NUMBER = "Credit Card Number :";
			String CREDIT_CARD_EXPIRY = "Credit Card Expiration Date :";
			String DELIVERY_INFORMATION = "Delivery Information";
			String PACKING_LIST = "Packing List";
			String CARRIER = "Carrier";
			String BILL_OF_LANDING = "Bill of Lading";
			String LINE = "Line";
			String PRODUCT_CODE = "Product Code and GTIN";
			String CONS_PRODUCT_CODE = "Product Code and UPC";
			String CONTRACT = "Contract";
			String QTY = "Qty";
			String UOM = "UOM";
			String STATUS = "Status";
			String ITEM_PRICE = "Item Price";
			String TOTAL = "Total";
			String SUB_TOTAL = "Sub-Total:";
			String FEES_FREIGHT = "Fees/Freight:";
			String TAXES = "Taxes:";
			String ORDER_TOTAL = "Order Total:";
			String TRACKING = "Tracking";
			String REASON_CODE = "Reason Code";
			String CORDIS_HOUSE_ACCOUNT = "Cordis House Account:";
			String SPINE_SALES_REP_UCN = "Spine Sales Rep UCN:";
			String SALES_TERRITORY = "Sales Territory:";
			String SSP = "SSP";
			String SHIPPING_DATE = "Ship Date";
			String DELIVERY_DATE = "Delivery Date";
			String SURGERY_INFORMATION = "Surgery Information";
			String TOTAL_WEIGHT = "Total Weight:";
			String TOTAL_VOLUME = "Total Cubic Volume:";

			String SURGEON = "Surgeon:";
			String CASE_DATE = "Case Date:";
			String SURGERY_SPECIALITY = "Surgery Specialty:";
			String LEVELS_INSTRUMENTED = "# Levels Instrumented:";
			String ORTHOBIOLOGICS = "Orthobiologics:";
			String PATHOLOGY = "Pathology:";
			String SURGICAL_APPROACH = "Surgical Approach:";
			String ZONE = "Zone:";
			String INTERBODY = "Interbody:";
			String FILE_CASE = "File/Case #:";

		}
		
		interface InvoiceDetails
		{

			String NOT_AVAILABLE = "Not Available";
			String INVOICE_INFORMATION = "Invoice Information";
			String BILLING_DATE = "Billing Date:";
			String SUMMARY_HEADER = "Invoice Details";
			String LINE_HEADER = "Invoice Line Details";
			String INVOICE_NUMBER = "Invoice Number:";
			String SHIP_DATE = "Ship Date";
			String BILL_OF_LADING = "Bill of Lading:";
			String CARRIER_NAME = "Carrier:";
			String PACKING_LIST = "Packing List:";
			String SHIPPING_METHOD = "Shipping Method";
			String ITEM_PRICE = "Item Price";
			String EXTENDED_PRICE = "Extended Price";
			String CONTRACT_NUMBER = "Contract Number";
			String ORDERED_QUANTITY = "Ordered Quantity";
			String INVOICED_QUANTITY = "Invoiced Quantity";
			String UPC = "UPC:";
			String SHIP_TO_ADDRESS = "Ship-To Address:";
			String TRACKING_COLON = "Tracking:";
			String SHIPPING = "Shipping";
			String TRACKING = "Tracking";
			String INVOICE_TOTAL = "Invoice Total:";
			String INVOICED_DISCLAIMER = "invoiced.disclaimer";
			String INVOICED_DISCLAIMER_MSG = "invoiced.disclaimer.message";

			String ORDER_NUMBER = "Order Number";
			String ACCOUNT_NUMBER = "Account Number";
			String ATTENTION = "Attention";
			String BILLING_NAME_ADDRESS = "Billing Name and Address:";
			String DEALER_PO = "Dealer PO Number";
			String DROP_SHIP_ACC = "Drop-Ship Account";
			String REQ_DELIVERY_DATE = "Requested Delivery Date";
			String EXPECTED_DELIVERY_DATE = "Expected Delivery Date";
			String ACTUAL_SHIP_DATE = "Actual Ship Date";
			String ACTUAL_DELIVERY_DATE = "Actual Delivery Date";
			String SHIPMENT_LOCATION = "Shipment Location";
			String LINE_STATUS = "Line Status";
			String PRODUCT_NAME = "Product Name";
			String PRODUCT_ID = "ID:";
			String GTIN = "GTIN:";
			String UNIT_OF_MEASURE = "Unit of Measure";
			String QUANTITY = "Quantity";
			String UNIT_PRICE = "Unit Item Price";
			String TOTAL_PRICE = "Total";
			String ORDER_CHANNEL = "order.channel.";
			String SURGEON_NAME = "Surgeon Name";
			String DISTRIBUTOR_PO_NUMBER = "Distributor PO Number";
			String SHIP_TO_ACCOUNT = "Ship-To Account";
			String LOT_NUMBER = "Lot Number";
			String LOT_COMMENT = "Lot Comment";
			String SPECIAL_STOCK_PARTNER = "Special Stock Partner";
			String ESTIMATED_DELIVERY = "Estimated Delivery Date";

			String ORDER_INFORMATION = "Order Information";
			String BILLING_INFORMATION = "Billing Information";
			String SHIPPING_INFORMATION = "Shipping Information";
			String JNJ_ORDER_NUMBER = "J&J Order Number:";
			String PO_NUMBER = "Purchase Order:";
			String ORDER_DATE = "Order Date:";
			String ORDER_STATUS = "Status:";
			String ORDER_TYPE = "Order Type:";
			String ORDER_BY = "Ordered By:";
			String NUMBER_OF_LINES = "Number of Lines:";
			String SOLD_TO_ACC_NUMBER = "Sold-To Account Number:";
			String GLN = "GLN:";
			String PAYMENT_METHOD = "Payment method:";
			String CREDIT_CARD_TYPE = "Credit Card Type:";
			String CREDIT_CARD_NUMBER = "Credit Card Number:";
			String CREDIT_CARD_EXPIRY = "Credit Card Expiration Date:";
			String DELIVERY_INFORMATION = "Delivery Information";
			String CARRIER = "Carrier";
			String BILL_OF_LANDING = "Bill of Lading";
			String LINE = "Line";
			String PRODUCT_CODE = "Product Code and GTIN";
			String CONTRACT = "Contract";
			String QTY = "Qty";
			String UOM = "UOM";
			String STATUS = "Status:";
			String TOTAL = "Total";
			String SUB_TOTAL = "Sub-Total:";
			String FEES_FREIGHT = "Fees/Freight:";
			String TAXES = "Taxes:";
			String ORDER_TOTAL = "Order Total:";
			String SPINE_SALES_REP_UCN = "Spine Sales Rep UCN:";
			String SALES_TERRITORY = "Sales Territory:";
			String SSP = "SSP";
			String SHIPPING_DATE = "Ship Date";
			String DELIVERY_DATE = "Delivery Date";
			String SURGERY_INFORMATION = "Surgery Information";
			String TOTAL_WEIGHT = "Total Weight:";
			String TOTAL_VOLUME = "Total Cubic Volume:";

			String SURGEON = "Surgeon:";
			String CASE_DATE = "Case Date:";
			String SURGERY_SPECIALITY = "Surgery Specialty:";
			String LEVELS_INSTRUMENTED = "# Levels Instrumented:";
			String ORTHOBIOLOGICS = "Orthobiologics:";
			String PATHOLOGY = "Pathology:";
			String SURGICAL_APPROACH = "Surgical Approach:";
			String ZONE = "Zone:";
			String INTERBODY = "Interbody:";
			String FILE_CASE = "File/Case #:";
		}
          }
	
	//GTR-1682 Ends
	
	
}
	   

