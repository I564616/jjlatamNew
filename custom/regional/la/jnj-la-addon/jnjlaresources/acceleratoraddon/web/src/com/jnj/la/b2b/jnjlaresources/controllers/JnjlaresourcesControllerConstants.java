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
package com.jnj.la.b2b.jnjlaresources.controllers;

/**
 */
public interface JnjlaresourcesControllerConstants
{
	// implement here controller constants used by this extension



	String ADDON_PREFIX = "addon:/jnjlaresources/";

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
				String MyCompanyHomePage = "pages/company/myCompanyHomePage";
				String MyCompanyManageUserEditPage = "pages/company/myCompanyManageUserEditPage";
				String MyCompanyManageUsersPage = "pages/company/myCompanyManageUsersPage";
				String MyCompanyManageUserAddEditFormPage = "pages/company/myCompanyManageUserAddEditFormPage";
			}

			interface Misc
			{
				String AccountSelectionPageForUserManagement = "pages/misc/accountsSelectionForUserManagement";
			}
		}
	}
}
