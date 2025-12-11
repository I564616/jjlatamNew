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
package com.jnj.la.b2b.jnjlaselloutaddon.controllers;

/**
 */
public interface JnjlaselloutaddonControllerConstants {
	// implement here controller constants used by this extension

	String ADDON_PREFIX = "addon:/jnjlaselloutaddon/";

	interface Views {
		interface Cms {
			String ComponentPrefix = "cms/";
		}

		interface Pages {
			interface Account {
				String SellOutReports = "pages/account/sellOutReportPage";
				String UploadOrders = "pages/account/uploadOrderPage";
				String UploadOrderDetails = "pages/account/uploadOrderPageDetails";
				String UploadOrderPopUp = "pages/account/uploadOrderPagePopUp";
			}

		}

	}

}
