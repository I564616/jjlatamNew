/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved. 
 */
package com.jnj.core.dao.reports;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrderChannelModel;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
/**
 * This class represents the DAO layer for the reports functionality.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTReportsDao
{
	/**
	 * This method is used to query the database and fetch the data for back-order reports based on the query parameters
	 * supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method is used to query the database and fetch the data for single purchase analysis reports based on the
	 * query parameters supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchSinglePurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method fetches the order channel model for the desired order type code
	 * 
	 * @param code
	 * @return List<JnjGTOrderChannelModel>
	 */
	public List<JnjGTOrderChannelModel> fetchOrderChannelForCode(final String code);

	/**
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderModel>
	 */
	List<OrderModel> fetchCutReport(final JnjGTPageableData jnjGTPageableData);

	/**
	 * This method is used to query the database and fetch the data for multi-product purchase analysis report based on
	 * the query parameters supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	List<OrderEntryModel> fetchMultiPurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData);
	List<JnjGTInvoiceModel> fetchFinancialAnalysisReport(final JnjGTPageableData jnjGTPageableData);
	List<JnjGTInvoiceModel> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData);
	
	/**
	 * AAOL-4603: This method is used to query the database and fetch the data for delivery list in the order analysis reports based on the
	 * query parameters supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	/*TODO - REMOVE List<OrderEntryModel> fetchOADeliveryListReport(
			JnjGTPageableData jnjGTPageableData);*/

	/*AAOL-4603*/
	SearchResult<List<Object>> fetchOADeliveryListReport(
			JnjGTPageableData jnjGTPageableData);

	
	List<String> fetchFranchiseCode(final String code);
	 
	/*AAOL-4603*/
	public List<String> getFranchiseDesc();
	
	public List<String> fetchPayerId(String accountid);

	/*AAOL-2410*/
	List<OrderModel> fetchSalesReport(JnjGTPageableData jnjGTPageableData);

	List<String> fetchStockLocationAccount(String accountid);
}
