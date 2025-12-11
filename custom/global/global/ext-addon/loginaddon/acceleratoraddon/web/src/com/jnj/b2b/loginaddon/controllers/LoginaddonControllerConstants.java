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
package com.jnj.b2b.loginaddon.controllers;

import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;

/**
 */
public interface LoginaddonControllerConstants
{
	// implement here controller constants used by this extension
	String ADDON_PREFIX = "addon:/loginaddon/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{

		interface Pages
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountRegisterPage = "pages/account/customerRegisterPage";
				String EmailPreferencePage = "pages/account/EmailPreferencePage";
				String UserActivationPage ="pages/account/userActivationPage";
				String successfulRegistration = "pages/account/successfulRegistrationPage";
				String termsandconditionsPage = "pages/terms/termsAndConditionsPage";
				String privacyandPolicyPage = "pages/misc/privacyPolicyPage";
				String SurveyPage = "pages/misc/surveyPage";
				String updatePrivacyPolicyPage = "pages/misc/updatedPrivacyPolicyPage";
				String ChangeAccountPage = "pages/account/changeAccount";
			    String legalnoticepage = "pages/legal/legalNoticePage";
				String SuccessfulActivationPage = "pages/account/successfulActivationPage";
				String AccountBuildOrderpage = "pages/account/accountbuildOrderPage";
				String UseFulLinkPage = "pages/misc/useFulLinkPage";

			}
			
			interface Home
			{
				String HomePage = "pages/home/home";
				String CURRENT_ACCOUNT_ID = "currentB2BUnitID";
				String CURRENT_ACCOUNT_NAME = "currentB2BUnitName";
				String SITE_NAME = "jnjSiteName";
				String CURRENT_B2B_UNIT_GLN = "currentB2BUnitGLN";
				String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
				String ADMIN_USER = "adminUser";
				String FIRST_NAME = "firstName";
				String LAST_NAME = "lastName";

				/*Changes for Serialization AAOL-8670*/
				String SerialzationPage =  "pages/home/serializationPage";
			}

			interface Password
			{
				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
				String PasswordResetExpiredPage = "pages/password/passwordResetExpiryPage";
				String PasswordResetPage = "pages/password/passwordResetPage";
				String FirstTimeResetPassword = "pages/password/firstTimeResetPassword";
				
			

			}
			interface Help
			{
				String ContactUsPopUpPage =  "pages/help/contactUs";
				
			}
			
			interface Misc
			{
				String MiscRobotsPage = "pages/misc/miscRobotsPage";
				String SurveyPage = "pages/misc/surveyPage";
				String AccountSelectionPage = "pages/misc/accountSelection";
				String AccountSelectionPageForUserManagement = "pages/misc/accountsSelectionForUserManagement";
				String HelpContractUsPage = "pages/misc/helpPage";
				String ContractUsPopUpPage = "pages/misc/contactUs";
			}
			interface Order
			{
				String orderHistorySection = "pages/order/orderHistoryHome";
			}
		}
		
		interface Fragments

		{

		interface Cart

		{

		String AddToCartHomePopup ="fragments/cart/addToCartHomePopup";

		}
		
		}

		interface Cms
		{
			interface minicart
			{
				 
				String ComponentPrefix = "cms/minicart/minicartcomponent";
			}
			
		}

	}
	 
}
