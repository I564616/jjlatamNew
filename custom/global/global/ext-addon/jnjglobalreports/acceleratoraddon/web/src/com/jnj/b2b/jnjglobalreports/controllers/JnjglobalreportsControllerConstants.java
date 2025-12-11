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
package com.jnj.b2b.jnjglobalreports.controllers;

/**
 */
public interface JnjglobalreportsControllerConstants
{
	// implement here controller constants used by this extension

	String ADDON_PREFIX = "addon:/jnjglobalreports/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{

		interface Pages
		{
			interface Reports
			{
				//String PurchaseAnalysisReportPage = "pages/reports/purchaseAnalysisReportPage";
				String PurchaseAnalysisOrderEntriesPage = "pages/reports/purchaseAnalysisOrderEntriesPage";
				//String BackorderReportPage = "pages/reports/backorderReportPage";
				//String InventoryReportPage ="pages/reports/inventoryReportPage";
				//String CutReportPage ="pages/reports/cutReportPage";
				String CutReportOrderEntryPanel = "fragments/reports/cutReportBody";
				String OrderAnalysisReportPage = "pages/reports/orderAnalysisReportPage";//AAOL-4603
				String InventoryAnalysisReportPage = "pages/reports/inventoryAnalysisReportPage";
				String FinancialAnalysisReportPage="pages/reports/financialAnalysisReportPage";
				String AccountAgingReportPage = "pages/reports/accountAging";
				String PaymentSummaryReportPage = "pages/reports/paymentSummary";
				String BalanceSummaryReportPage = "pages/reports/balanceSummary";
				String CreditSummaryReportPage = "pages/reports/creditSummary";
				//String consignmentInventoryReportPage = "pages/reports/consignmentInventoryReportPage";
			}
			
			interface Misc
			{
				String AccountSelectionPage = "pages/misc/accountSelection";
			}
		}
	}


}
