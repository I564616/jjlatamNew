/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved. 
 */
package com.jnj.facades.reports;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTCutReportOrderData;
import com.jnj.facades.data.JnjGTCutReportOrderEntryData;
import com.jnj.facades.data.JnjGTFinancePurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.facades.data.JnjGTSinglePurchaseAnalysisEntriesData;
import com.jnj.facades.data.JnjGTSinglePurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTSalesReportResponseData; 
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;

/**
 * This class represents the facade layer for the reports flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTReportsFacade
{
	/**
	 * This method fetches back-order report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTBackorderReportResponseData>
	 */
	public List<JnjGTBackorderReportResponseData> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData);

	
	/**
	 * AAOL-2410: This method fetches sales report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTBackorderReportResponseData>
	 */
	public TreeMap<String, List<JnjGTSalesReportResponseData>> fetchSalesReport(final JnjGTPageableData jnjGTPageableData);
	
	
	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	public List<JnjGTInventoryReportResponseData> fetchInventoryReport(final JnjGTPageableData jnjGTPageableData);
	
	
	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	//public List<JnjGTFinancialSummaryReportData> fetchFinancialSummaryReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method fetches single purchase analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData>
	 */
	public TreeMap<String, JnjGTSinglePurchaseOrderReportResponseData> fetchSinglePurchaseAnalysisReport(
			final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method is used to fetch the product PK for the product code entered in jnjGTPageableData's search by field
	 * 
	 * @param jnjGTPageableData
	 * @return product Name + GTIN
	 */
	public String convertProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo);

	/**
	 * This method fetches multi purchase analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTMultiPurchaseOrderReportResponseData>
	 */
	Map<String, Object> fetchMultiPurchaseAnalysisReport(JnjGTPageableData jnjGTPageableData);

	/**
	 * AAOL-4603:  This method fetches multi purchase analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTMultiPurchaseOrderReportResponseData>
	 */
	Map<String, Object> fetchOADeliveryListReport(JnjGTPageableData jnjGTPageableData);

	/**
	 * 
	 * @param accountNumbers
	 * @return List<JnjGTAddressData>
	 */
	List<JnjGTAddressData> getShipTOAddressesForAccount(final String accountNumbers);

	/**
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTCutReportOrderData>
	 */
	public List<JnjGTCutReportOrderData> fetchCutReport(final JnjGTPageableData jnjGTPageableData);

	List<JnjGTCutReportOrderEntryData> fetchCutReportOrderEntryData(final String orderNumber);

	/**
	 * This method is used to get the Franchise or Division code.
	 * 
	 * @param operatingCompany
	 * @return Map<String, String>
	 */
	Map<String, String> getFranchiseOrDivCode(String operatingCompany);

	/**
	 * AAOL-4603: This method is used to get ALL the Franchise or Division code.
	 * 
	 * @param operatingCompany
	 * @return Map<String, String>
	 */
	Map<String, String> getFranchiseOrDivCode();

	
	/**
	 * 
	 * This method is used to fetch the values for the territory dropdown.
	 * 
	 * @param currentSite
	 * @return List<String>
	 */
	List<String> getTerritoryForSalesRep(String currentSite);


	/**
	 * 
	 * This method is used to fetch the static dropdown data from the config table in a map.
	 * 
	 * @param id
	 *           - the id based on which the value is stored in the ConfigTable
	 * @return Map<String,String>
	 */
	Map<String, String> getDropdownValuesInMap(String id);

	/**
	 * 
	 * This method is used to fetch the list of static dropdown data from the config table.
	 * 
	 * @param id
	 *           - the id based on which the value is stored in the ConfigTable
	 * @return List<String>
	 */
	List<String> getDropdownValuesInList(String id);

	/**
	 * 
	 * To fetch the operating company for the purchase analysis report
	 * 
	 * @param currentSite
	 *           - the value of the source system id of the current b2b unit of the current user
	 * @return Map<String, String>
	 */
	Map<String, String> getOperatingCompanyDropdown(String currentSite);

	/**
	 * This method fetches the entries for the particular line in the purchase analysis table
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTSinglePurchaseAnalysisEntriesData>
	 */
	public List<JnjGTSinglePurchaseAnalysisEntriesData> fetchSinglePurchaseAnalysisOrderEntries(JnjGTPageableData jnjGTPageableData);

	/**
	 * This method fetches financial analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData>
	 */
	public TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> fetchFinancialAnalysisReport(
			final JnjGTPageableData jnjGTPageableData);

	
	/**
	 * This method fetches InvoicePastDue report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInvoicePastDueReportResponseData>
	 */
	public List<JnjGTInvoicePastDueReportResponseData> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method fetches Inventory report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInventoryReportResponseData>
	 */
	public List<JnjGTInventoryReportResponseData> fetchInventoryDataReport(final JnjGTPageableData jnjGTPageableData);
	/**
	 * 
	 * This method returns the ordered from codes
	 * 
	 * @param key
	 * @return codes
	 */
	public String fetchValuesFromConfig(String id, String key);
	
	public Map<String,String> getCartegoryDropdownValuesInMap();

	public Map<String,String> getFinancialReportsTypeDropdownVaulesInMap();
	
	public Map<String,String> getInventoryReportsTypeDropdownVaulesInMap();
	
	public Map<String,String> getOrderReportsTypeDropdownVaulesInMap();

	public List<JnjGTConsInventoryData> simulateConsInventoryReport(JnjGTPageableData jnjGTPageableData);
	
	public List<JnjGTFinancialAccountAgingReportData> simulateAccountAgingReport(JnjGTPageableData jnjGTPageableData);
	
	public List<JnjGTFinancialBalanceSummaryReportData> simulateBalanceSummaryReport(JnjGTPageableData jnjGTPageableData);
	
	public List<JnjGTFinancialPaymentSummaryReportData> simulatePaymentSummaryReport(JnjGTPageableData jnjGTPageableData);
	
	public List<JnjGTFinancialCreditSummaryReportData> simulateCreditSummaryReport(JnjGTPageableData jnjGTPageableData);
	
	List<String> getDropdownFranchiseCode(String productCode);	
	
	
	List<String> getPayerIdValuesBasedPayFromAddress(String accountid);
	
	public List<JnjGTInvoiceClearingReportResponseData> fetchInvoiceClearingReport(final JnjGTPageableData jnjGTPageableData);


	public List<String> fetchStockLocationAccount(String accountid);

}
