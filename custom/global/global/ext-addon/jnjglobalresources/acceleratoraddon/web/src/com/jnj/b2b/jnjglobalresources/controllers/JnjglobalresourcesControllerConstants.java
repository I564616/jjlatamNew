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
package com.jnj.b2b.jnjglobalresources.controllers;

/**
 */
public interface JnjglobalresourcesControllerConstants
{
	// implement here controller constants used by this extension

	String ADDON_PREFIX = "addon:/jnjglobalresources/";

	interface Views
	{
		interface Pages
		{
			interface Resources
			{
				String ResourcesPage = "pages/resource/resource";
				String PolicyFees = "pages/resource/policiesandfees";
				String Termsofsale = "pages/resource/termsofsale";

			}
			interface MyCompany
			{
				String MyCompanyHomePage =  "pages/company/myCompanyHomePage";
				String MyCompanyManageUserEditPage = "pages/company/myCompanyManageUserEditPage";
				String MyCompanyManageUsersPage = "pages/company/myCompanyManageUsersPage";
				String MyCompanyManageUserAddEditFormPage ="pages/company/myCompanyManageUserAddEditFormPage";
				String SelectsAccountPopups = "pages/company/selectsAccountPopup";
				String SelectsEditAccountPopups = "pages/company/selectsAccountEditPopup";
			}
			interface Misc
			{	
			String AccountSelectionPageForUserManagement = "pages/misc/accountsSelectionForUserManagement";
			}
		}
	}
}
