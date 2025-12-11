/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013 
 * All rights reserved.
 */
package com.jnj.core.services.reports.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.JnjConfigService;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.dao.reports.JnjGTReportsDao;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.services.reports.JnjGTReportsService;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrderChannelModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTSalesReportResponseData;

/**
 * This class represents the service layer for the reports functionality.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTReportsService implements JnjGTReportsService
{
	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTReportsService.class);

	@Resource(name = "GTReportsDao")
	protected JnjGTReportsDao jnjGTReportsDao;
	@Autowired
	protected JnjConfigService jnjConfigService;
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	@Resource(name = "productDao")
	protected JnjGTProductDao jnjGTProductDao;
	@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected SessionService sessionService;

	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}

	public UserService getUserService() {
		return userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}
	/**
	 * This method fetches back-order report based on search criteria
	 *
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	@Override
	public List<OrderEntryModel> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling DAO layer to fetch back-order report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch backorder report ");
		final List<OrderEntryModel> backorderLines = getJnjGTReportsDao().fetchBackOrderReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + backorderLines);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return backorderLines;
	}

	/**
	 * This method fetches Single Purchase Analysis report based on search criteria
	 *
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	@Override
	public List<OrderEntryModel> fetchSinglePurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling DAO layer to fetch Single Purchase Analysis report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch Single Purchase Analysis report ");
		final List<OrderEntryModel> singlePurchaseAnalysisLines = getJnjGTReportsDao().fetchSinglePurchaseAnalysisReport(
				jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis lines obtained :: "
				+ singlePurchaseAnalysisLines);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return singlePurchaseAnalysisLines;
	}
	
	
	/**
	 * AAOL-2401: This method fetches SalesReport report based on search criteria
	 *
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	@Override
	public List<OrderModel> fetchSalesReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSalesReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling DAO layer to fetch Single Purchase Analysis report data based on search criteria **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch Single Purchase Analysis report ");
		final List<OrderModel> saleseReportAnalysisLines = getJnjGTReportsDao().fetchSalesReport(jnjGTPageableData);
		
		
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis lines obtained :: "
				+ saleseReportAnalysisLines);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return saleseReportAnalysisLines;
	}	
	
	/**
	 * This method fetches the entries for the particular line in the financial analysis table
	 *
	 * @param jnjGTPageableData
	 * @return List<JnjGTSinglePurchaseAnalysisEntriesData>
	 */
	/*AAOL #2419*/
	@Override
	public List<JnjGTInvoiceModel> fetchFinancialAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchFinancialAnalysisReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling DAO layer to fetch Single Purchase Analysis report entries for the line **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch Single Purchase Analysis entries ");
		final List<JnjGTInvoiceModel> orderEntries = getJnjGTReportsDao().fetchFinancialAnalysisReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Financial Analysis entries obtained :: "
				+ orderEntries);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return orderEntries;
	}

	/**
	 * This method fetches invoice-past-due report based on search criteria
	 *
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	/*@Override
	public List<JnjGTInvoicePastDueReportResponseData> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInvoicePastDueReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		*//** Calling DAO layer to fetch back-order report data based on search criteria **//*
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch backorder report ");
		final List<JnjGTInvoicePastDueReportResponseData> invoicepastdueLines = getJnjGTReportsDao().fetchInvoicePastDueReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "backorder lines obtained :: " + invoicepastdueLines);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return invoicepastdueLines;
	}*/

	/**
	 * This method is used to fetch the product PK for the product code entered in jnjGTPageableData's search by field
	 *
	 * @param jnjGTPageableData
	 * @param isReturnProductInfo
	 * @return product name + GTIN / null
	 */
	@Override
	public List<JnjGTInvoiceModel> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchInvoicePastDueReport()";
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch backorder report ");
		final List<JnjGTInvoiceModel> invoicepastdueLines = getJnjGTReportsDao().fetchInvoicePastDueReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "invoice due lines obtained :: " + invoicepastdueLines);
		return invoicepastdueLines;
	}

	/**
	 * This method is used to fetch the product PK for the product code entered in jnjGTPageableData's search by field
	 *
	 * @param jnjGTPageableData
	 * @param isReturnProductInfo
	 * @return product name + GTIN / null
	 */
	@Override
	public String convertProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo)
	{
		final String METHOD_NAME = "covertProductCodeToPK()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		String productInfo = null;

		/** Calling DAO layer to fetch product by code **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling products DAO to fetch product by code");
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final JnJProductModel product = (JnJProductModel) jnjGTProductDao.getProductByPartialValue(
				jnjGTPageableData.getSearchBy(), false, currentSite, true);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Product obtained for code :: "
				+ jnjGTPageableData.getSearchBy() + " :: " + product);

		/** Checking if the products found are not null or empty **/
		if (null != product)
		{
			/** Changing the value of search by to the PK of the base product **/
			jnjGTPageableData.setSearchBy(String.valueOf(product.getPk()));
		}
		else
		{
			jnjGTPageableData.setSearchBy(null);
		}
		/** Checking if the product information is requested **/
		if (isReturnProductInfo)
		{
			/** Getting the name and the GTIN **/
			final String productName = StringUtils.isNotEmpty(product.getName()) ? StringUtils.trim(product.getName()) : "";
			final String productGTIN = StringUtils.isNotEmpty(product.getEan()) ? StringUtils.trim(product.getEan()) : "";
			productInfo = productName + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + productGTIN;
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return productInfo;
	}
	
	/*rajesh*/
	public String convertBackorderProductCodeToPK(final JnjGTPageableData jnjGTPageableData, final boolean isReturnProductInfo)
	{
		final String METHOD_NAME = "convertBackorderProductCodeToPK()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		String productInfo = null;

		/** Calling DAO layer to fetch product by code **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling products DAO to fetch product by code");
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final JnJProductModel product = (JnJProductModel) jnjGTProductDao.getProductByPartialValue(
				jnjGTPageableData.getSearchBy(), false, currentSite, true);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Product obtained for code :: "
				+ jnjGTPageableData.getSearchBy() + " :: " + product);

		/** Checking if the products found are not null or empty **/
		if (null != product)
		{
			/** Changing the value of search by to the PK of the base product **/
			jnjGTPageableData.setSearchBy(String.valueOf(product.getPk()));
		}
		else
		{
			jnjGTPageableData.setSearchBy("");
		}
		/** Checking if the product information is requested **/
		if (isReturnProductInfo)
		{
			/** Getting the name and the GTIN **/
			final String productName = StringUtils.isNotEmpty(product.getName()) ? StringUtils.trim(product.getName()) : "";
			final String productGTIN = StringUtils.isNotEmpty(product.getEan()) ? StringUtils.trim(product.getEan()) : "";
			productInfo = productName + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + productGTIN;
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return productInfo;
	}
	/**
	 * This method fetches the order channel from the map provided if its present in it, else it fetches the order
	 * channel model from the DAO layer and sets the channel values accordingly
	 *
	 * @param orderChannelsMap
	 * @param poType
	 */
	@Override
	public String fetchOrderChannel(final Map<String, JnjGTOrderChannelModel> orderChannelsMap, final String poType)
	{
		final String METHOD_NAME = "fetchOrderChannel()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		String orderChannel = null;

		/** Checking if the PO type is already present in the map **/
		if (orderChannelsMap.containsKey(poType))
		{
			/** If it is found then no need to call the DAO to hit DB, simply set channel from map **/
			orderChannel = orderChannelsMap.get(poType).getType().getCode();
		}
		else
		{
			/** Hitting the DAO to fetch the oder channels for the provided PO Type **/
			final List<JnjGTOrderChannelModel> orderChannelModelList = jnjGTReportsDao.fetchOrderChannelForCode(poType);
			if (null != orderChannelModelList && CollectionUtils.isNotEmpty(orderChannelModelList))
			{
				/** Putting value fetched from DB in the map **/
				orderChannelsMap.put(poType, orderChannelModelList.get(0));

				/** Setting value of channel as value fetched from DB **/
				orderChannel = orderChannelModelList.get(0).getType().getCode();
			}
		}
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "order channel fetched as  ::" + orderChannel);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return orderChannel;
	}

	@Override
	public List<OrderEntryModel> fetchMultiPurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchMultiPurchaseAnalysisReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** filtered the accounts based on the selected territory send from the frontend **/
		filterAccountsBasedOnTerritory(jnjGTPageableData);
		/** Calling service layer to fetch multi-product report data based on search criteria **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Calling reports dao to fetch multi-product report");
		final List<OrderEntryModel> orderEntries = getJnjGTReportsDao().fetchMultiPurchaseAnalysisReport(jnjGTPageableData);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return orderEntries;
	}
	
	//AAOL-4603 - Order Analysis - Delivery List
	@Override
	public SearchResult<List<Object>> fetchOADeliveryListReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchOADeliveryListReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		/** filtered the accounts based on the selected territory send from the frontend **/
		//TODO - FETCH DATA FROM RESPONSE - filterAccountsBasedOnTerritory(jnjGTPageableData);
		/** Calling service layer to fetch multi-product report data based on search criteria **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Calling reports dao to fetch order analysis for delivery list report");
		//final List<OrderEntryModel> orders = getJnjGTReportsDao().fetchOADeliveryListReport(jnjGTPageableData);
		final SearchResult<List<Object>> orders = getJnjGTReportsDao().fetchOADeliveryListReport(jnjGTPageableData);
		//final List<OrderModel> orders = testData(jnjGTPageableData);//null; //TODO - FETCH DATA FROM RESPONSE 
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return orders;
	}
	
	/**
	 * This method is used to filter the accounts based on the territory as selected by the Sales Rep user.
	 *
	 * @param jnjGTPageableData
	 */
	protected void filterAccountsBasedOnTerritory(final JnjGTPageableData jnjGTPageableData)
	{
		final List<String> filteredAccounts = new ArrayList<String>();
		if (StringUtils.isNotEmpty(jnjGTPageableData.getStatus()))
		{
			for (final String uid : jnjGTPageableData.getSearchParamsList())
			{
				JnJB2BUnitModel JnJB2BUnitModel = null;
				final B2BUnitModel b2bUnitModel = companyB2BCommerceService.getUnitForUid(uid);
				if (b2bUnitModel instanceof JnJB2BUnitModel)
				{
					JnJB2BUnitModel = (JnJB2BUnitModel) b2bUnitModel;

					if (StringUtils.isNotEmpty(jnjGTPageableData.getStatus())
							&& JnJB2BUnitModel.getAllGroups().contains(userService.getUserGroupForUID(jnjGTPageableData.getStatus())))
					{
						filteredAccounts.add(uid);
					}
				}
			}
			jnjGTPageableData.setSearchParamsList(filteredAccounts);
		}

	}

	/**
	 * This method fetches cut report based on search criteria
	 *
	 * @param jnjGTPageableData
	 */
	@Override
	public List<OrderModel> fetchCutReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchCutReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch back-order report data based on search criteria **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Calling reports service to fetch cut report ");
		final List<OrderModel> cutOrders = getJnjGTReportsDao().fetchCutReport(jnjGTPageableData);
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch cut report ");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return cutOrders;
	}

	/**
	 * This method fetches cut report based on search criteria
	 *
	 * @param orderNumber
	 */
	@Override
	public List<OrderEntryModel> fetchEntryDataForCutReport(final String orderNumber)
	{
		final String METHOD_NAME = "fetchEntryDataForCutReport()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling service layer to fetch back-order report data based on search criteria **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Calling reports service to fetch cut report. Order Number="+orderNumber);
		OrderModel orderModel = new OrderModel();
		orderModel.setOrderNumber(orderNumber);
		orderModel = flexibleSearchService.getModelByExample(orderModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "orderModel="+orderModel);
		final OrderEntryModel orderEntryModel = new OrderEntryModel();
		orderEntryModel.setOrder(orderModel);
		orderEntryModel.setRejected(Boolean.TRUE);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "orderEntryModel="+orderEntryModel);
		final List<OrderEntryModel> cutOrderEntries = flexibleSearchService.getModelsByExample(orderEntryModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "cutOrderEntries.size()="+cutOrderEntries.size());
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports service to fetch cuts report ");
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return cutOrderEntries;
	}

	/**
	 * @return the jnjGTReportsDao
	 */
	public JnjGTReportsDao getJnjGTReportsDao()
	{
		return jnjGTReportsDao;
	}

	/**
	 * @param jnjGTReportsDao
	 *           the jnjGTReportsDao to set
	 */
	public void setJnjGTReportsDao(final JnjGTReportsDao jnjGTReportsDao)
	{
		this.jnjGTReportsDao = jnjGTReportsDao;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	/**
	 * This method fetches the entries for the particular line in the purchase analysis table
	 *
	 * @param jnjGTPageableData
	 * @return List<JnjGTSinglePurchaseAnalysisEntriesData>
	 */
	@Override
	public List<OrderEntryModel> fetchSinglePurchaseAnalysisOrderEntries(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisOrderEntries()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Calling DAO layer to fetch Single Purchase Analysis report entries for the line **/
		LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Calling reports DAO to fetch Single Purchase Analysis entries ");
		final List<OrderEntryModel> orderEntries = getJnjGTReportsDao().fetchSinglePurchaseAnalysisReport(jnjGTPageableData);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Single Purchase Analysis entries obtained :: "
				+ orderEntries);

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return orderEntries;
	}
	
		public List<String> fetchFranchiseCode(final String productCode)
	{
		return jnjGTReportsDao.fetchFranchiseCode(productCode);
		
	}
	
		/*AAOL-4603*/
		public List<String> getFranchiseDesc(){
			return jnjGTReportsDao.getFranchiseDesc();
		}
		
		public List<String> fetchPayerId(String accountid){
			return jnjGTReportsDao.fetchPayerId(accountid);
		}
		
		@Override
		public List<String> fetchStockLocationAccount(String accountid){
			return jnjGTReportsDao.fetchStockLocationAccount(accountid);
		}
}
