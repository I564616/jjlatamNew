/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013 
 * All rights reserved.
 */
package com.jnj.core.services.reports;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;
import java.util.Map;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrderChannelModel;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;


/**
 * This class represents the service layer for the reports functionality.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTReportsService
{
	/**
	 * This method fetches back-order report based on search criteria.
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * 
	 * This method fetches cut report based on search criteria.
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderModel>
	 */
	public List<OrderModel> fetchCutReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method fetches Single Purchase Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchSinglePurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * AAOL-2410: This method fetches sales report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderModel>
	 */
	public List<OrderModel> fetchSalesReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method fetches the entries for the particular line in the purchase analysis table
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchSinglePurchaseAnalysisOrderEntries(JnjGTPageableData jnjGTPageableData);

	/**
	 * This method is used to fetch the product PK for the product code entered in jnjGTPageableData's search by field
	 * 
	 * @param jnjGTPageableData
	 * @param isReturnProductInfo
	 * @return product Name + GTIN
	 */
	public String convertProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo);

	/**
	 * This method fetches the order channel from the map provided if its present in it, else it fetches the order
	 * channel model from the DAO layer and sets the channel values accordingly
	 * 
	 * @param orderChannelsMap
	 * @param poType
	 */
	public String convertBackorderProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo);
	
	public String fetchOrderChannel(final Map<String, JnjGTOrderChannelModel> orderChannelsMap, final String poType);

	/**
	 * 
	 * This method fetches Multi Purchase Analysis report based on search criteria
	 * 
	 * @param orderNumber
	 * @return List<OrderEntryModel>
	 */
	List<OrderEntryModel> fetchEntryDataForCutReport(String orderNumber);

	/**
	 * This method fetches Multi Purchase Analysis report based on search criteria.
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	List<OrderEntryModel> fetchMultiPurchaseAnalysisReport(JnjGTPageableData jnjGTPageableData);
	
	/**
	 * This method fetches Financial Analysis report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<JnjGTInvoiceModel> fetchFinancialAnalysisReport(final JnjGTPageableData jnjGTPageableData);
	
		/**
	 * AAOL-4603: This method fetches Multi Purchase Analysis report based on search criteria.
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	SearchResult<List<Object>> fetchOADeliveryListReport(JnjGTPageableData jnjGTPageableData);

	/**
	 * This method fetches InvoicePastDue report based on search criteria
	 * 
	 * @param jnjGTPageableData
	 * @return List<JnjGTInvoicePastDueReportResponseData>
	 */
	public List<JnjGTInvoiceModel> fetchInvoicePastDueReport(JnjGTPageableData jnjGTPageableData);
	
	List<String> fetchFranchiseCode(final String productCode);

	/*AAOL-4603*/
	public List<String> getFranchiseDesc();
	
	public List<String> fetchPayerId(String accountid);

	public List<String> fetchStockLocationAccount(String accountid);
}
