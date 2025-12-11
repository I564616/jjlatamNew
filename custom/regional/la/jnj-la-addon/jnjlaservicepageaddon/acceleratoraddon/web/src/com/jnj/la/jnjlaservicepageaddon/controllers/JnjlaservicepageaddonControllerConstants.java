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
package com.jnj.la.jnjlaservicepageaddon.controllers;

/**
 */

public interface JnjlaservicepageaddonControllerConstants {
	// implement here controller constants used by this extension
	String ADDON_PREFIX = "addon:/jnjlaservicepageaddon/";

	interface Views {
		interface Cms {
			String ComponentPrefix = "cms/";
		}

		interface Pages {
			interface Misc {
				String JnjServicesPage = "pages/misc/jnjServicesPage";
				String JnjAddIndirectCustomerFormPage = "pages/misc/addIndirectCustomerForm";
				String JnjAddIndirectPayerFormPage = "pages/misc/addIndirectPayerForm";
				String JnjConsignmentIssuePage = "pages/misc/consignmentIssuePage";
			}

			interface Laudo {
				String DownloadLaudo = "pages/misc/jnjDownloadLaudoPage";
				String ManageLaudo = "pages/misc/jnjManageLaudoPage";
			}

			interface CrossReferenceTable {
				String CrossReferenceTable = "pages/misc/crossReferenceTableView";
			}
		}

	}

}
