	/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved. 
 */

package com.jnj.core.dao.reports.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import java.text.DateFormat;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;


import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.dao.reports.JnjGTReportsDao;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSearchDTO;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.enums.JnjOrderTypesEnum;


import com.jnj.core.jalo.JnjGTInvoice;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrderChannelModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * This class represents the DAO layer for the reports functionality.
 * 
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjGTReportsDao extends AbstractItemDao implements JnjGTReportsDao
{
	protected static final String REPORTS_ORDER_STATUS = "reportsOrderStatus";

	protected static final String ORDER_STATUS = "orderStatus";

	protected static final String SHIP_TO = "shipTo";

	protected static final String PO_NUMBER = "poNumber";

	protected static final String PO_NUMBER_QUERY = " AND {o:purchaseOrderNumber} = ?poNumber ";

	protected static final String SHIP_TO_ACCOUNT_QUERY = " AND {o:deliveryAddress} = ?shipTo ";

	protected static final String PRODUCT_QUERY = " AND ({oe:product}= ?productCode or {oe:referencedVariant}=?productCode)";

	protected static final String PRODUCT_INNER_QUERY = " AND {oe:product} IN  ({{SELECT {pk} from {JnJProduct} where {firstLevelCategory} =?operatingCompany ";

	protected static final String ADDITIONAL_INNER_QUERY = " AND {secondLevelCategory}= ?franchiseCode";
	
	protected static final String SELECTED_OPERATION_COMPANY_QUERY = " AND {product.franchiseName} = ?operatingCompany";
	protected static final String INNER_QUERY_END = " }}) ";

	protected static final String LOT_NUMBER_QUERY = " AND ({oe.lot}= ?lotNumber OR {oe.batchNum}= ?lotNumber) ";

	protected static final String SINGLE_PURCHASE_ANALYSIS_REPORT_SORT_QUERY = " ORDER BY {o:unit}, {o.date}";

	protected static final String ORDER_CHANNELS_QUERY_APPEND = " AND {ch.code} IN (?orderChannels) ";

	//added to search for the entries which do not have any sap code associated with them
	protected static final String BLANK_ORDER_CHANNELS_QUERY_APPEND = " AND ({ch.code} IN (?orderChannels) or {o:PoType} IS NULL ) ";

	protected static final String PRODUCT = "productCode";

	protected static final String CODE = "code";

	protected static final String CUT_REPORT_ORDER_BY = " ORDER BY ";

	protected static final String ORDER_BY_PO_NUMBER = " {o:purchaseOrderNumber} ";

	protected static final String ORDER_BY_ORDER_DATE = " {o:date} ";

	protected static final String ORDER_BY_ORDER_NUMBER = " {o:orderNumber} ";

	protected static final String ORDER_BY_REJECTION_REASON = " {oe:reasonForRejection} ";

	protected static final String ORDER_BY_SHIP_TO = " {add.streetname} ";
	protected static final String PURCHASE_ANALYSIS_ORDER_TYPES_APPEND = " AND {status:code} IN (?orderTypes) ";

	protected static final String PRUCHASE_ANALYSIS_ORDER_STATUS_QUERY = " AND {o.status} IN ({{SELECT {pk} FROM {OrderStatus} WHERE {code} IN (?orderStatus)}}) ";
	protected static final String INVOICE_DUE_REPORT_BASE_QYERY = "SELECT {i.pk} FROM {JnjGTInvoice AS i JOIN JnjGTInvoiceEntry AS ie ON {i.pk}={ie.invoice} JOIN Order as o ON {i:order}={o:pk}}";
	protected static final String INVOICE_DUE_REPORT_INVOICE_NUM = "where {i.invoiceNum} =(?invoiceNum)"; 
	protected static final String INVOICE_DUE_REPORT_INVOICE_DUE_DATE = "where {i.invoiceDueDate} < CONVERT(DATETIME,?invoiceDueDate)";
	protected static final String INVOICE_DUE_REPORT_INVOICE_DUE_STATUS = "where {i.invoiceDueDate} < CONVERT(DATETIME,?invoiceDueDate) AND {i.status} =(?invoiceStatus)";
	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTReportsDao.class);

	/** Enumeration Service **/
	@Autowired
	protected EnumerationService enumerationService;

	/** Flexible Search Service **/
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	

	@Autowired
	protected JnjConfigService jnjConfigService;
	

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}


	@Resource(name = "productDao")
	protected JnjGTProductDao jnjGTProductDao;


	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected UserService userService;

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}
    
	public String getFranchise(){
		return sessionService.getAttribute("allowedFranchise");
	}
	
	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	/** Back-order Report search query **/

	protected static final String BACKORDER_REPORT_SORT_QUERY = " ORDER BY {o.date} DESC";
	protected static final String BACKORDER_REPORT_QUERY = "SELECT Distinct{oe.pk},{o.date} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JNJProduct as product on {oe:product}={product:pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND {o.date} >= CONVERT(DATETIME,?startDate) AND {o.date} <= CONVERT(DATETIME,?endDate) AND {ds.lineStatus} = ?lineStatus ";
	//protected static final String BACKORDER_REPORT_QUERY = "SELECT Distinct{oe.pk} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND {o.date} >= CONVERT(DATETIME,?startDate) AND {o.date} <= CONVERT(DATETIME,?endDate) AND {ds.lineStatus} = ?lineStatus";
	
	protected static final String FINANCIAL_REPORT_INVOICE = "SELECT {i.pk} from {JnjGTInvoice as i JOIN Order as o ON {i:Order}={o:Pk} join Jnjb2bcustomer as cust ON {o:user}={cust:Pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk} JOIN OrderStatus AS os ON {o:status} = {os:pk} JOIN orderEntry AS oe ON {o.pk}={oe.order}  JOIN JnJProduct AS jp ON {oe:product}={jp:pk}} where {cust:uid}=?userID ";
	
	protected static final String PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_1 = "SELECT {oe.pk} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JNJProduct as product on {oe:product}={product:pk} JOIN JnjGTOrderChannel AS ch ON {ch:code}={o:PoType}";
  
	protected static final String PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_2 = "} WHERE {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN (?accountNumbers) }}) AND {o.date} >= CONVERT(DATETIME,?startDate) AND {o.date} <= CONVERT(DATETIME,?endDate)";
	protected static final String PURCHASE_ANALYSIS_ORDER_TYPES = " JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}";

	protected static final String ORDER_CHANNELS_QUERY = "SELECT {ch.pk} FROM {JnjGTOrderChannel AS ch} where {ch:code} = ?code";
	protected static final String QUERY_PARAM_START_DATE = "startDate";
	protected static final String QUERY_PARAM_END_DATE = "endDate";
	protected static final String QUERY_PARAM_ACCOUNT_NUMBERS = "accountNumbers";
	protected static final String QUERY_PARAM_ORDER_CHANNELS = "orderChannels";
	protected static final String QUERY_PARAM_ORDER_TYPES = "orderTypes";
	protected static final String QUERY_PARAM_FRANCHISE_DESC = "franchiseDesc";
	protected static final String QUERY_PARAM_STATUS="status";
	protected static final String QUERY_PARAM_LINE_STATUS = "lineStatus";
	protected static final Object QUERY_PARAM_LOT_NUMBER = "lotNumber";
	protected static final Object QUERY_PARAM_PRODUCT_PK = "productCode";
	protected static final String QUERY_PARAM_INVOICE_NUM = "invoiceNumber";
	protected static final String QUERY_PARAM_INVOICE_DUE_DATE = "invoiceDueDate";
	protected static final String UNCONFIRMED_STRING = "UC";
	protected static final String BACKORDER_REPORT_QUERY1="SELECT Distinct{oe.pk},{o.date} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JNJProduct as product on {oe:product}={product:pk} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND {ds.lineStatus} = ?lineStatus";
	protected static final String OPEN_LINE_AND_BACKORDER_REPORT_SORT_QUERY = " ORDER BY {ds.lineStatus}";
	protected static final String CANCELLED_LS_REPORT_QUERY = "SELECT Distinct{oe.pk},{ds.lineStatus} FROM {order AS o JOIN orderEntry AS oe ON {o.pk}={oe.order} JOIN JnjDeliverySchedule AS ds ON {ds.ownerEntry} = {oe.pk} JOIN JnjOrderTypesEnum AS status ON {o:orderType}={status:pk}} WHERE {status:code} IN (?orderTypes) AND {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) AND {o.date} >= CONVERT(DATETIME,?startDate) AND {o.date} <= CONVERT(DATETIME,?endDate) AND {o.date} >= CONVERT(DATETIME,?fourteenDays) AND {ds.lineStatus} ='RJ'";
	protected static final String QUERY_PARAM__FOURTEEN = "fourteenDays";
	
	protected static final String QUERY_PARAM_CUSTOMER_PO_NO="CustomerPONO";
	protected static final String QUERY_PARAM_DELIVERY_NO = "DeliveryNo";
	protected static final String QUERY_PARAM_SALES_DOC_NO="SalesDocNo";
	protected static final String QUERY_PARAM_INVOICE_NO="InvoiceNo";
	protected static final String QUERY_PARAM_PRODUCT_CODE="ProductCode";
	
	protected static final String SALES_REPORT_QUERY_FOR_ALL_FIELDS = "AND {sd.deliveryNum}=?deliveryNo AND {iv.invoiceNum} = ?invoiceNo AND {o.sapOrderNumber} = ?salesDocNo AND {o.purchaseOrderNumber} = ?customerPoNo AND {jp.code}=?productCode ";
	protected static final String SALES_REPORT_BASIC_QUERY = "select DISTINCT {o.pk} from {order AS o JOIN OrderEntry AS oe ON {oe:order}={o:pk} JOIN JnJProduct AS jp ON {oe:product}={jp:pk} LEFT JOIN JnjGTInvoice AS iv ON {o.pk}={iv.order} JOIN OrderStatus AS os ON {o:status}={os:pk} JOIN JnjOrderTypesEnum as ot ON {o:ordertype}={ot:pk} LEFT JOIN JnjGTShippingDetails AS sd ON {o.shippingDetails} LIKE CONCAT( '%', CONCAT( {sd.PK} , '%' ) ) JOIN JnjGTShippingLineDetails AS sld ON {sld.shippingDetail} = {sd.PK}  } WHERE {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN (?accountNumbers) }}) "; // FOR ALM - 696 B2B Issue
	protected static final String ORDER_BY_DATE = "order by {o.date}";
	protected static final String APPEND_SEARCH_ORDERTYPE ="AND {ot.code} = ?orderTypes ";
	protected static final String APPEND_SEARCH_PRODUCT="AND {jp.code}=?productCode ";
	//protected static final String APPEND_SEARCH_ORDER_TYPE="AND {ot.code}={?orderType}";
	protected static final String APPEND_SEARCH_CUSTOMER_PO_NO="AND {o.purchaseOrderNumber} = trim(?customerPoNo) ";
	protected static final String APPEND_SEARCH_SALES_DOC_NO="AND {o.sapOrderNumber} = trim(?salesDocNo) ";
	protected static final String APPEND_SEARCH_DELIVERY_NO="AND {sd.deliveryNum}=trim(?deliveryNo) ";
	protected static final String APPEND_SEARCH_INVOICE_NO="AND {iv.invoiceNum} = trim(?invoiceNo) ";
	protected static final String APPEND_SEARCH_STATUS="AND {os.code} = ?status ";
	protected static final String APPEND_SEARCH_FRANCHISE="AND {jp.franchiseName} = ?franchiseDesc ";
	protected static final String APPEND_SEARCH_START_DATE="AND {o.creationtime} >= CONVERT(DATETIME,?startDate) ";
	protected static final String APPEND_SEARCH_END_DATE="AND {o.creationtime} <= CONVERT(DATETIME,?endDate) ";
	protected static final String FRANCHISENAME="franchiseName";
	protected static final String ORDERTYPE="orderType";
	protected static final String STATUS="status";
	protected static final String CUSTOMERPONO="customerPoNo";
	protected static final String DELIVERYNO="deliveryNo";
	protected static final String SALESDOCNO="salesDocNo";
	protected static final String INVOICENO="invoiceNo";
	protected static final String PRODUCTCODE="productCode";
	
	
	/**
	 * This method is used to query the database and fetch the data for back-order reports based on the query parameters
	 * supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	@Override

	public List<OrderEntryModel> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchBackOrderReport()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		List<OrderEntryModel> result = null;
		//List<OrderEntryModel> cancelledLinesResult = null;
		List<OrderEntryModel> reportsResult = new ArrayList<OrderEntryModel>();

		/** Populating the query parameters **/
		final Map queryParams = populateBackorderQueryParams(jnjGTPageableData);
		/** Populate the query for Line status CANCELLED to stay on report for 14 days only **/
	//	final Map cancelledLineStatusQueryParams = populateCancelledLineStatusQueryParams(jnjGTPageableData);

		/** Creating new flexible search query with the query string BACKORDER_REPORT_QUERY **/
		//Hema- Added for JJEPIC-825
		//default sort is on the status
		StringBuilder query = new StringBuilder();
		String allowedFranchise = sessionService.getAttribute("allowedFranchise");
		if(jnjGTPageableData.getFromDate()==null || jnjGTPageableData.getFromDate()=="" || jnjGTPageableData.getToDate()==null || jnjGTPageableData.getToDate()==""){
		query.append(BACKORDER_REPORT_QUERY1);
		if(StringUtils.isNotEmpty(allowedFranchise)){
			query.append(" AND {product.franchiseName} IN (" + allowedFranchise + ")");
		}
		} else if(null!=jnjGTPageableData.getSearchBy() && jnjGTPageableData.getFromDate()==null && StringUtils.isNotEmpty(allowedFranchise) || jnjGTPageableData.getFromDate()=="" || jnjGTPageableData.getToDate()==null || jnjGTPageableData.getToDate()==""){
			LOG.debug("Allowed Franchises"+ allowedFranchise);
			query.append(BACKORDER_REPORT_QUERY1).append(PRODUCT_QUERY).append(" AND {product.franchiseName} IN (" + allowedFranchise + ")");
		}
		else{
			query.append(BACKORDER_REPORT_QUERY);
			if(StringUtils.isNotEmpty(allowedFranchise)){
				query.append(" AND {product.franchiseName} IN (" + allowedFranchise + ")");
			}
		}
	//	query.append(OPEN_LINE_AND_BACKORDER_REPORT_SORT_QUERY);
		query.append(BACKORDER_REPORT_SORT_QUERY);
		
		
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		//JJEPIC-930
//		StringBuilder cancelledQuery = new StringBuilder();
//		cancelledQuery = cancelledQuery.append(CANCELLED_LS_REPORT_QUERY);
//		final FlexibleSearchQuery cancellledFQuery = new FlexibleSearchQuery(cancelledQuery);
//		cancellledFQuery.addQueryParameters(cancelledLineStatusQueryParams);
		/*try
		{*/
		final List<OrderEntryModel> result1 = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			/*CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to perform flexible search"
					+ queryParams, LOG);
			*//** Fetching the type-casted result into OrderEntryModel type in order to obtain List<OrderEntryModel> **//*
			result = getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
			//JJEPIC-930 Cancelled lines should stay only for 14 days on the report
			//cancelledLinesResult = getFlexibleSearchService().<OrderEntryModel> search(cancellledFQuery).getResult();
			if (result != null)
			{*/
			@Override
			public List<OrderEntryModel> execute()
			{
				final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
				fQuery.addQueryParameters(queryParams);
				final List<OrderEntryModel> result = getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
				return result;
			}
		}, userService.getAdminUser());
		
		try
		{ 
			if (result1 != null)
			{
				reportsResult.addAll(result1);
			}
//			if (cancelledLinesResult != null)
//			{
//				reportsResult.addAll(cancelledLinesResult);
//			}

			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Result obtained :: " + result1);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOG.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return reportsResult;
	}

	/**
	 * This method is used to query the database and fetch the data for single purchase analysis reports based on the
	 * query parameters supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */
	@Override

	public List<OrderEntryModel> fetchSinglePurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchSinglePurchaseAnalysisReport()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		List<OrderEntryModel> result = null;

		/** Populating the query parameters **/
		final Map queryParams = populateSinglePurchaseAnalysisQueryParams(jnjGTPageableData);

		/** Creating new flexible search query with the query string SINGLE_PURCHASE_ANALYSIS_REPORT_QUERY **/
		StringBuilder query = new StringBuilder();
		String allowedFranchise = sessionService.getAttribute("allowedFranchise");
		if(StringUtils.isNotEmpty(allowedFranchise)){
			LOG.debug("Allowed Franchises"+ allowedFranchise);
			query = query.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_1).append(PURCHASE_ANALYSIS_ORDER_TYPES)
					.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_2).append(PRODUCT_QUERY).append(" AND {product.franchiseName} IN (" + allowedFranchise + ")");
		}else{
			query = query.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_1).append(PURCHASE_ANALYSIS_ORDER_TYPES)
					.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_2).append(PRODUCT_QUERY);
		}
		/** If ordered from parameter is not present, do not add the queryParam **/
		if (StringUtils.isNotEmpty(jnjGTPageableData.getStatus()))
		{
			/**
			 * If the user selects the "others" ordered from then the user can also see the orders which have the order
			 * channel field as blank
			 **/
			final String appendQuery = !jnjGTPageableData.isSearchFlag() ? ORDER_CHANNELS_QUERY_APPEND
					: BLANK_ORDER_CHANNELS_QUERY_APPEND;
			query.append(appendQuery);
		}
		/** If lot number parameter is not present, do not add the queryParam **/
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText()))
		{
			query.append(LOT_NUMBER_QUERY);
		}
		query.append(PURCHASE_ANALYSIS_ORDER_TYPES_APPEND);

		if ((Jnjb2bCoreConstants.CONS).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			query.append(PRUCHASE_ANALYSIS_ORDER_STATUS_QUERY);
		}

		query.append(SINGLE_PURCHASE_ANALYSIS_REPORT_SORT_QUERY);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		try
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to perform flexible search"
					+ queryParams, LOG);
			/** Fetching the type-casted result into OrderEntryModel type in order to obtain List<OrderEntryModel> **/
			result = getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Result obtained :: " + result);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOG.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return result;
	}

	
	/**
	 * This method is used to query the database and fetch the data for financial analysis reports based on the
	 * query parameters supplied in jnjGTPageableData
	 * 
	 * @param jnjGTPageableData
	 * @return List<OrderEntryModel>
	 */ 
	/*AAOL #2419*/
	@Override
	public List<JnjGTInvoiceModel> fetchFinancialAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchFinancialAnalysisReport()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
				List<JnjGTInvoiceModel> result = null;

				/** Populating the query parameters **/
				//

				/** Creating new flexible search query with the query string SINGLE_PURCHASE_ANALYSIS_REPORT_QUERY **/
				StringBuilder query = new StringBuilder();
				final Map queryParams = new HashMap();
 				boolean flag = false;
				
				query.append(FINANCIAL_REPORT_INVOICE);
				
				if(jnjGTPageableData != null){
				
				if(jnjGTPageableData.getCustomerPONumber() != null ||  jnjGTPageableData.getSalesDocumentNumber() != null || null != jnjGTPageableData.getInvoiceNumber()){
					
				if(jnjGTPageableData.getCustomerPONumber()!=""  ){
					LOG.debug("CustomerPONumber is "+jnjGTPageableData.getCustomerPONumber());	
					String customerPoNumber = jnjGTPageableData.getCustomerPONumber();
					queryParams.put("CustomerPONumber", customerPoNumber);
					query.append("and {o.purchaseOrderNumber}=(?CustomerPONumber) ");
					flag=true;
				}
				
				 if(jnjGTPageableData.getSalesDocumentNumber()!="" ){
					LOG.debug("SalesDocumentNumber is "+jnjGTPageableData.getSalesDocumentNumber());
					String salesDocumentNumber = jnjGTPageableData.getSalesDocumentNumber();
					queryParams.put("SalesDocumentNumber", salesDocumentNumber);
					query.append("and {o.sapOrderNumber} = (?SalesDocumentNumber) ");
					flag=true;
				}
				 if(jnjGTPageableData.getInvoiceNumber()!="" ){
					LOG.debug("InvoiceNumber is "+jnjGTPageableData.getInvoiceNumber());
					String invoiceNumber = jnjGTPageableData.getInvoiceNumber();
					queryParams.put("invoiceNumber", invoiceNumber);
					query.append("and {i.invoiceNum} = (?invoiceNumber) ");
					flag=true;
				}
				}
				
					if(flag == false){
					if(null != jnjGTPageableData.getOrderType() && StringUtils.isNotEmpty(jnjGTPageableData.getOrderType()) && !jnjGTPageableData.getOrderType().equalsIgnoreCase("ALL") ){
						LOG.debug("OrderType is "+jnjGTPageableData.getOrderType());
						final List<String> orderTypesList = new ArrayList<String>();
						String orderType = jnjGTPageableData.getOrderType();
						queryParams.put("orderTypes", orderType);
						query.append("AND {status.code} IN (?orderTypes) ");
					}
					if(null != jnjGTPageableData.getFinancialStatus() && StringUtils.isNotEmpty(jnjGTPageableData.getFinancialStatus()) && !jnjGTPageableData.getFinancialStatus().equalsIgnoreCase("ALL")){	
						LOG.debug("status is "+jnjGTPageableData.getFinancialStatus());
						String status = jnjGTPageableData.getFinancialStatus();
						queryParams.put("status", status);
						query.append(" AND {os.code} IN (?status) ");
					}
					
					if(null != jnjGTPageableData.getFromDate() && null != jnjGTPageableData.getToDate()){
						
						LOG.debug("start date is "+jnjGTPageableData.getFromDate());
						String dateFrom = jnjGTPageableData.getFromDate();
						LOG.debug("dateFrom "+ dateFrom);
						query.append(" AND {i.creationtime} >= CONVERT(DATETIME,?startDate) ");
						queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
							Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, -1));
						LOG.debug("queryParams Start Date "+ queryParams);
						
						
						LOG.debug("end date is "+jnjGTPageableData.getToDate());
						String toDate=jnjGTPageableData.getToDate();
						LOG.debug("toDate "+toDate);
						query.append(" AND {i.creationtime} <= CONVERT(DATETIME,?endDate) ");
						queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
								Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
							LOG.debug("queryParams end Date "+ queryParams);
					}
					
					if(jnjGTPageableData.getFinancialsDescription()!="" && null != jnjGTPageableData.getFinancialsDescription() && !jnjGTPageableData.getFinancialsDescription().equalsIgnoreCase("ALL")){
						LOG.debug("FinancialDescription is "+jnjGTPageableData.getFinancialsDescription());
						String financialDescription = jnjGTPageableData.getFinancialsDescription();
						queryParams.put("franchiseCode", financialDescription);
						LOG.debug("Selected Allowed Franchise"+ financialDescription);
						query.append(" AND {jp.franchiseName}= ?franchiseCode");
					}else{
						String allowedFranchise = sessionService.getAttribute("allowedFranchise");
						LOG.debug("Allowed Franchises"+ allowedFranchise);
						if(StringUtils.isNotEmpty(allowedFranchise)){
							LOG.debug("Allowed Franchises"+ allowedFranchise);
							query.append(" AND {jp.franchiseName} IN (" + allowedFranchise + ")");
						
						}
					}
				
				
				}
				
				}	
			
				//query = query.append("FINANCIAL_REPORT_MAIN_QUERY");
				
				String userid = userService.getCurrentUser().getUid();
				if(null != userid && userid != ""){
					LOG.debug("User id for invoice Report is "+userid);
					queryParams.put("userID",userid);
				}
							
				final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
				fQuery.addQueryParameters(queryParams);
				List<JnjGTInvoiceModel> invoiceList = null;
				try
				{
					CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to perform flexible search"
							+ queryParams, LOG);
					/** Fetching the type-casted result into OrderEntryModel type in order to obtain List<OrderEntryModel> **/
					
					invoiceList = sessionService.executeInLocalView(new SessionExecutionBody() {
                           @Override
                           public List<JnjGTInvoiceModel> execute() {
                                  final List<JnjGTInvoiceModel> result = flexibleSearchService.<JnjGTInvoiceModel>search(fQuery).getResult();
                                  return result;
                           }
                    }, userService.getAdminUser()); 
					
					//result = getFlexibleSearchService().<JnjGTInvoiceModel> search(fQuery).getResult();
					LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
							+ "Result obtained :: " + invoiceList);
				}
				catch (final ModelNotFoundException modelNotFoundException)
				{
					LOG.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
							+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
							+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
				}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return invoiceList;
	}
	
	
	
	/**
	 * This method populates the query parameters for the back-order report
	 * 
	 * @param jnjGTPageableData
	 * @return queryParams
	 */

	protected Map populateBackorderQueryParams(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateBackorderQueryParams()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		/** Populating the query parameters **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the query params.", LOG);
		final Map queryParams = new HashMap();

		/** Parsing the dates to Hybris understandable format and adding to query params **/
		if(jnjGTPageableData.getFromDate()!="" && jnjGTPageableData.getFromDate()!=null){
				LOG.debug("start date is "+jnjGTPageableData.getFromDate());
				String dateFrom=jnjGTPageableData.getFromDate();
				LOG.debug("dateFrom "+dateFrom);
			queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, -1));
				LOG.debug("queryParams Start Date "+ queryParams);
				 
			}
		if(jnjGTPageableData.getToDate()!="" && jnjGTPageableData.getToDate()!=null) {
			LOG.debug("end date is "+jnjGTPageableData.getToDate());
			String toDate=jnjGTPageableData.getToDate();
			LOG.debug("toDate "+toDate);
			queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
				LOG.debug("queryParams end Date "+ queryParams);
			 
		}

		/** Adding rest of the query parameters **/
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
		queryParams.put(QUERY_PARAM_ORDER_TYPES, createOrderTypesList(false));
		if(jnjGTPageableData.getSearchBy()!="" && jnjGTPageableData.getSearchBy()!=null) {
			queryParams.put(QUERY_PARAM_PRODUCT_PK, jnjGTPageableData.getSearchBy());
			}
		queryParams.put(QUERY_PARAM_LINE_STATUS, UNCONFIRMED_STRING); // Unconfirmed status for Back-order items
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
				+ queryParams, LOG);

		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return queryParams;
	}
	
	
	
	
	protected Map populateSalesReportQueryParams(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateSalesReportQueryParams()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		/** Populating the query parameters **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the query params.", LOG);
		final Map queryParams = new HashMap();
		
		if(jnjGTPageableData.getFromDate()!="" && jnjGTPageableData.getFromDate()!=null){
			LOG.debug("start date is "+jnjGTPageableData.getFromDate());
			String dateFrom=jnjGTPageableData.getFromDate();
			LOG.debug("dateFrom "+dateFrom);
			queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, -1));
			LOG.debug("queryParams Start Date "+ queryParams);
			 
		}
		if(jnjGTPageableData.getToDate()!="" && jnjGTPageableData.getToDate()!=null) {
			LOG.debug("end date is "+jnjGTPageableData.getToDate());
			String toDate=jnjGTPageableData.getToDate();
			LOG.debug("toDate "+toDate);
			queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
				LOG.debug("queryParams end Date "+ queryParams);
			 
		}
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
		queryParams.put(QUERY_PARAM_ORDER_TYPES,jnjGTPageableData.getSearchText());
		queryParams.put(QUERY_PARAM_FRANCHISE_DESC,jnjGTPageableData.getAdditionalSearchText());
		if(jnjGTPageableData.getStatus().equalsIgnoreCase("COMPLETED")){
		queryParams.put(QUERY_PARAM_STATUS,"ACCEPTED");
		}else{
			queryParams.put(QUERY_PARAM_STATUS,jnjGTPageableData.getStatus());
		}
		List<JnjGTSearchDTO> jnjGTSearchDTOList = new ArrayList<JnjGTSearchDTO>();
		
		for (JnjGTSearchDTO jnjGTSearchDTO:jnjGTPageableData.getSearchDtoList()) {
	
		/** Adding rest of the query parameters **/
			if(jnjGTSearchDTO.getSearchBy().equals(QUERY_PARAM_CUSTOMER_PO_NO) && jnjGTSearchDTO.getSearchValue() != ""){
			queryParams.put(CUSTOMERPONO, jnjGTSearchDTO.getSearchValue());}
		
			if(jnjGTSearchDTO.getSearchBy().equals(QUERY_PARAM_DELIVERY_NO) && jnjGTSearchDTO.getSearchValue() != ""){
				queryParams.put(DELIVERYNO, jnjGTSearchDTO.getSearchValue());}
			
			if(jnjGTSearchDTO.getSearchBy().equals(QUERY_PARAM_SALES_DOC_NO) && jnjGTSearchDTO.getSearchValue() != ""){
				queryParams.put(SALESDOCNO, jnjGTSearchDTO.getSearchValue());}
			
			if(jnjGTSearchDTO.getSearchBy().equals(QUERY_PARAM_INVOICE_NO) && jnjGTSearchDTO.getSearchValue() != ""){
				queryParams.put(INVOICENO, jnjGTSearchDTO.getSearchValue());
				}
			if(jnjGTSearchDTO.getSearchBy().equals(QUERY_PARAM_PRODUCT_CODE) && jnjGTSearchDTO.getSearchValue() != ""){
				queryParams.put(PRODUCTCODE, jnjGTSearchDTO.getSearchValue());}
		
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
				+ queryParams, LOG);
			
		}
		/** Parsing the dates to Hybris understandable format and adding to query params **/
		

		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return queryParams;
	}
	
	
	
	
	
	
	

	/**
	 * This method populates the query parameters for the back-order report
	 * 
	 * @param jnjGTPageableData
	 * @return queryParams
	 */

	protected Map populateSinglePurchaseAnalysisQueryParams(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateSinglePurchaseAnalysisQueryParams()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		/** Populating the query parameters **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the query params.", LOG);
		final Map queryParams = new HashMap();

		/** Parsing the dates to Hybris understandable format and adding to query params **/

		queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getFromDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0));

		queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getToDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));

		/** Adding rest of the query parameters **/
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
		queryParams.put(QUERY_PARAM_ORDER_TYPES, createOrderTypesList(true));

		/** If lot number parameter is not present, do not add the queryParam **/
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText()))
		{
			queryParams.put(QUERY_PARAM_LOT_NUMBER, jnjGTPageableData.getSearchText());
		}

		/** If ordered from parameter is not present, do not add the queryParam **/
		if (StringUtils.isNotEmpty(jnjGTPageableData.getStatus()))
		{
			queryParams.put(QUERY_PARAM_ORDER_CHANNELS,
					Arrays.asList(jnjGTPageableData.getStatus().split(Jnjb2bCoreConstants.CONST_COMMA)));
		}

		queryParams.put(QUERY_PARAM_PRODUCT_PK, jnjGTPageableData.getSearchBy());

		if ((Jnjb2bCoreConstants.CONS).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			queryParams.put(ORDER_STATUS, createOrderStatusQueryParam());
		}

		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
				+ queryParams, LOG);
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return queryParams;
	}

	/**
	 * This method fetches the order channel model for the desired order type code
	 * 
	 * @param code

	 * @return List<JnjGTOrderChannelModel>
	 */
	@Override

	public List<JnjGTOrderChannelModel> fetchOrderChannelForCode(final String code)
	{
		final String METHOD_NAME = "fetchOrderChannelForCode()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		List<JnjGTOrderChannelModel> result = null;

		/** Populating query parameters **/
		final Map queryParams = new HashMap();
		queryParams.put(CODE, code);

		/** Flexible search query **/
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(ORDER_CHANNELS_QUERY);
		fQuery.addQueryParameters(queryParams);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
				+ queryParams, LOG);

		try
		{
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to perform flexible search"
					+ queryParams, LOG);
			/** Fetching the Order Channels Model **/

			result = getFlexibleSearchService().<JnjGTOrderChannelModel> search(fQuery).getResult();
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Result obtained :: " + result);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOG.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
					+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return result;
	}

	/**
	 * This method creates a List of strings containing the various order types
	 * 
	 * @param purchaseAnalysisFlag
	 * @return orderTypesList
	 */
	protected List<String> createOrderTypesList(final boolean purchaseAnalysisFlag)
	{
		final String METHOD_NAME = "createOrderTypesList()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		final List<String> orderTypesList = new ArrayList<String>();
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Generating the order types List of Strings ::", LOG);

		/** Setting order types for purchase analysis **/
		if (purchaseAnalysisFlag)
		{
			/** Using the enumeration service to get list of all ENUM values **/
			final List<JnjOrderTypesEnum> jnjOrderTypesEnumList = enumerationService
					.getEnumerationValues(JnjOrderTypesEnum._TYPECODE);
			final List<String> orderTypesTempList = new ArrayList<String>();

			/** Iterating over the jnjOrderTypesEnumList **/
			for (final JnjOrderTypesEnum jnjOrderTypesEnum : jnjOrderTypesEnumList)
			{
				/** Adding string value of the ENUM in the order types list **/
				orderTypesList.add(jnjOrderTypesEnum.getCode());
			}

			/** Getting all the order types to be removed **/
			orderTypesTempList.add(JnjOrderTypesEnum.ZQT.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZQEP.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZQCP.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZQCE.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZQZZ.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZLZ.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZCA.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZCB.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZCN.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZCR.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZDA.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZDB.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZDN.getCode());
			orderTypesTempList.add(JnjOrderTypesEnum.ZDR.getCode());

			/** Removing all the above order types **/
			orderTypesList.removeAll(orderTypesTempList);
		}
		/** Setting order types for back order **/
		else
		{
			orderTypesList.add(JnjOrderTypesEnum.YKB.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZBRO.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZCQ.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZDEL.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZEX.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZKB.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZNC.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZOR.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZREC.getCode());
			orderTypesList.add(JnjOrderTypesEnum.ZVMI.getCode());
		}
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME,
				"Order types List of Strings generated!", LOG);

		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return orderTypesList;
	}

	/*
	 * (non-Javadoc)
	 * 

	 * @see com.jnj.core.dao.reports.JnjGTReportsDao#fetchCutReport(com.jnj.core.dto.JnjGTPageableData)
	 */
	@Override

	public List<OrderModel> fetchCutReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchCutReport()";
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final Map queryParams = new HashMap();
		final StringBuilder query = new StringBuilder(
				"SELECT DISTINCT {o:pk} FROM {order AS o JOIN orderEntry AS oe ON {o:pk}={oe:order} JOIN address AS add ON {o:deliveryAddress} = {add.pk}} where {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN (?accountNumbers) }}) AND {o:date} >= CONVERT(DATETIME,?startDate) AND {o:date} <= CONVERT(DATETIME,?endDate) AND {oe:reasonForRejection} IS NOT NULL AND {oe:Rejected}='1'");
		/** Parsing the dates to Hybris understandable format and adding to query params **/

		queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getFromDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0)); // adjusting by one day less for the start date

		queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getToDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1)); // adjusting by one day more for the end date
		/** Adding rest of the query parameters **/
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchBy()))
		{
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

			final ProductModel product = jnjGTProductDao.getProductByPartialValue(jnjGTPageableData.getSearchBy(), false,
					currentSite, false);
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + "Product=" + product);			
			if (product != null)
			{
				query.append(PRODUCT_QUERY);
				queryParams.put(PRODUCT, product.getPk());
			}
			else
			{
				return null;
			}
		}
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText()))
		{
			query.append(PO_NUMBER_QUERY);
			queryParams.put(PO_NUMBER, jnjGTPageableData.getSearchText());
		}
		if (StringUtils.isNotEmpty(jnjGTPageableData.getAdditionalSearchText()))
		{
			query.append(SHIP_TO_ACCOUNT_QUERY);
			queryParams.put(SHIP_TO, jnjGTPageableData.getAdditionalSearchText());
		}
	/*	query.append(CUT_REPORT_ORDER_BY);
		if (jnjGTPageableData.getStatus().equalsIgnoreCase("orderDate"))
		{
			query.append(ORDER_BY_ORDER_DATE);
		}
		else if (jnjGTPageableData.getStatus().equalsIgnoreCase("orderId"))
		{
			query.append(ORDER_BY_ORDER_NUMBER);
		}
		else if (jnjGTPageableData.getStatus().equalsIgnoreCase("poNumber"))
		{
			query.append(ORDER_BY_PO_NUMBER);
		}
		else if (jnjGTPageableData.getStatus().equalsIgnoreCase("cutReason"))
		{
			query.append(ORDER_BY_REJECTION_REASON);
		}
		else
		{
			query.append(ORDER_BY_SHIP_TO);
		}*/

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "fetchCutReport() Query " + fQuery);
		}

		final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (CollectionUtils.isNotEmpty(result))
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method is used to query the database and fetch the data for multi-product purchase analysis report based on
	 * the query parameters supplied in jnjGTPageableData
	 */
	@Override

	public List<OrderEntryModel> fetchMultiPurchaseAnalysisReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchMultiPurchaseAnalysisReport()";
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		/** If no filtered accounts were found associated with the territory, returning Null response **/
		if (CollectionUtils.isEmpty(jnjGTPageableData.getSearchParamsList()))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}
			return null;
		}

		final Map queryParams = new HashMap();
		final StringBuilder query = new StringBuilder();
		query.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_1).append(PURCHASE_ANALYSIS_ORDER_TYPES)
				.append(PURCHASE_ANALYSIS_REPORT_BASIC_QUERY_PART_2);
		/** Parsing the dates to Hybris understandable format and adding to query params **/

		queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getFromDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0)); // adjusting by one day less for the start date

		queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(jnjGTPageableData.getToDate(),
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1)); // adjusting by one day more for the end date
		/** Adding rest of the query parameters **/
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());

		/*
		 * For JJEPIC-574, same order type restrictions as Single Product
		 */
		query.append(PURCHASE_ANALYSIS_ORDER_TYPES_APPEND);
		queryParams.put(QUERY_PARAM_ORDER_TYPES, createOrderTypesList(true));
		/*
		 * End JJEPIC-574
		 */

		/** If ordered from parameter is not present, do not add the queryParam **/
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchBy()))
		{
			/**
			 * If the user selects the "others" ordered from then the user can also see the orders which have the order
			 * channel field as blank
			 **/
			final String appendQuery = !jnjGTPageableData.isSearchFlag() ? ORDER_CHANNELS_QUERY_APPEND
					: BLANK_ORDER_CHANNELS_QUERY_APPEND;
			query.append(appendQuery);
			queryParams.put(QUERY_PARAM_ORDER_CHANNELS,
					Arrays.asList(jnjGTPageableData.getSearchBy().split(Jnjb2bCoreConstants.CONST_COMMA)));
		}

		if (!jnjGTPageableData.getSearchText().equalsIgnoreCase("ALL"))
		{
		//query.append(PRODUCT_INNER_QUERY);
		
		queryParams.put("operatingCompany", jnjGTPageableData.getSearchText());
		query.append(SELECTED_OPERATION_COMPANY_QUERY);
		/*if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText()))
		{*/
			/*if(!jnjGTPageableData.getSearchText().equalsIgnoreCase("ALL")){*/
		/*}*/
		}
		else{
			String allowedFranchise = sessionService.getAttribute("allowedFranchise");
			LOG.debug("Allowed Franchises"+ allowedFranchise);
			if(StringUtils.isNotEmpty(allowedFranchise)){
				LOG.debug("Allowed Franchises"+ allowedFranchise);
				query.append(" AND {product.franchiseName} IN (" + allowedFranchise + ")");
			}
			/*}*/
			/*if (StringUtils.isNotEmpty(jnjGTPageableData.getAdditionalSearchText()))
			{
				query.append(ADDITIONAL_INNER_QUERY);
				queryParams.put("franchiseCode", jnjGTPageableData.getAdditionalSearchText());
			}*/
					//query.append(INNER_QUERY_END);
			}
		if ((Jnjb2bCoreConstants.CONS).equalsIgnoreCase(String.valueOf(sessionService
				.getAttribute(Jnjb2bCoreConstants.SITE_NAME))))
		{
			query.append(PRUCHASE_ANALYSIS_ORDER_STATUS_QUERY);
			queryParams.put(ORDER_STATUS, createOrderStatusQueryParam());
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "fetchMultiPurchaseAnalysisReport() Query " + fQuery);
		}
		LOG.info("######################"+fQuery);
		final List<OrderEntryModel> result = getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
		LOG.info("######################Result Set Size "+result.size());
		LOG.info("######################Result Set Size "+result);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (CollectionUtils.isNotEmpty(result))
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	/**
	 * AAOL-4603: This method is used to query the database and fetch the data for delivery list in order analysis report based on
	 * the query parameters supplied in jnjGTPageableData
	 */
	@Override

	public SearchResult<List<Object>> fetchOADeliveryListReport(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "fetchOADeliveryListReport()";
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		final Map queryParams = new HashMap();
		final StringBuilder query = new StringBuilder();

		String qry = "SELECT {otenum.code},{o.sapOrderNumber},{oe.sapOrderlineNumber},{o.purchaseOrderNumber},"
				+ "{sd.deliveryNum},{sld.deliveryLineNum},{sd.actualShipDate},"
				+ "{p.franchiseName},{p.code},{p.name},"
				+ "{sld.deliveryQty},{oe.quantity},"
				+" {sld.batchNum}, {sld.batchExpiryDate}, {sld.serialNum}, {sld.podDate}, {o.shipToAccount}, {sld.trackingNum},{b2b.name}, {u.name} "
		
		+ " FROM {"
				+ " Order as o JOIN OrderEntry AS oe ON {o.pk}={oe.order}"
				+ " JOIN JnJProduct AS p ON {oe:product} = {p:pk}"
				+ " JOIN JnjOrderTypesEnum AS otenum ON {o:orderType}={otenum:pk}"
				+ " JOIN JnjGTShippingDetails AS sd ON {o.shippingDetails} LIKE CONCAT('%', CONCAT({sd.PK}, '%'))"
				+ " JOIN JnjGTShippingLineDetails AS sld ON {sld.shippingDetail} = {sd.PK}"
				+ " JOIN JNJB2BUnit AS b2b ON {b2b.uid} ={o.shipToAccount}"
				+ " JOIN Unit as u ON {u.pk}={oe.unit}"
				+ " } ";
		query.append(qry);
		LOG.info("basic qry......." + qry);
		

		qry = " WHERE {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} WHERE {uid} IN (?accountNumbers)}}) "; 
		query.append(qry);		
		LOG.debug("qry with date....." + qry);		
		
		/** Adding rest of the query parameters **/
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
				
		String qPO="", qPCode="", qSDoc="", qDelNum="", qOType="", qFDesc="";
		List<JnjGTSearchDTO> searchDtoList = jnjGTPageableData.getSearchDtoList();
		for(JnjGTSearchDTO searchBy:searchDtoList){
			if (searchBy.getSearchBy().equals("custPONum") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {
				qPO = " AND {o.purchaseOrderNumber} = ?custPONum ";
				queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
				
				LOG.debug("custpo qry...." +qPO);
				LOG.debug("custpo queryparam....." + queryParams);
			}	
			if (searchBy.getSearchBy().equals("pcode") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {
				qPCode = " AND {p.code} = ?pcode ";
				queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
				
				LOG.debug("pcode qry...." +qPCode);
				LOG.debug("pcode queryparam....." + queryParams);
			}	
			if (searchBy.getSearchBy().equals("salesDocNum") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {
				qSDoc = " AND {o.sapOrderNumber} = ?salesDocNum ";
				queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
				
				LOG.debug("salesDocNum qry...." +qSDoc);
				LOG.debug("salesDocNum queryparam....." + queryParams);
			}
			if (searchBy.getSearchBy().equals("orderType") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {				
				if(searchBy.getSearchValue().trim().equalsIgnoreCase("ALL")){
					LOG.info("ALL order types are selected for search");
				}else{
					qOType = " AND {otenum:code} = ?orderType ";					
					queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
					
					LOG.debug("orderType qry...." +qOType);
					LOG.debug("orderType queryparam....." + queryParams);
				}
			}
			if (searchBy.getSearchBy().equals("franchiseDesc") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {
				if(searchBy.getSearchValue().trim().equalsIgnoreCase("ALL")){
					String allowedFranchise = sessionService.getAttribute("allowedFranchise");
					LOG.debug("Allowed Franchises"+ allowedFranchise);
					if(StringUtils.isNotEmpty(allowedFranchise)){
						LOG.debug("Allowed Franchises"+ allowedFranchise);
						qFDesc = " AND {p.franchiseName} IN (" + allowedFranchise + ")";
						LOG.debug("franchiseDesc queryparam....." + queryParams);
					}
				}else{
					LOG.info("ALL franchise descs are selected for search");
					qFDesc = " AND {p.franchiseName} = ?franchiseDesc ";					
					queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
					
					LOG.debug("franchiseDesc qry...." +qFDesc);
					LOG.debug("franchiseDesc queryparam....." + queryParams);
					/*qFDesc = " AND {p.franchiseName} = ?franchiseDesc ";					
					queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
					
					LOG.debug("franchiseDesc qry...." +qFDesc);
					LOG.debug("franchiseDesc queryparam....." + queryParams);*/
				}
			}
			if (searchBy.getSearchBy().equals("deliveryNum") && (searchBy.getSearchValue()==null || StringUtils.isNotEmpty(searchBy.getSearchValue().trim()))) {
				qDelNum = " AND {sd.deliveryNum} = ?deliveryNum ";				
				queryParams.put(searchBy.getSearchBy(), searchBy.getSearchValue().trim());
				
				LOG.debug("deliveryNum qry...." +qDelNum);				
				LOG.debug("deliveryNum queryparam....." + queryParams);
			}
		}
		
		/*Validate if any one CustPO, SalesDoc, DeliveryNum, ProdCode is given, then ignore all other validations*/
		if(StringUtils.isNotEmpty(qPO) || StringUtils.isNotEmpty(qPCode) || StringUtils.isNotEmpty(qSDoc) || StringUtils.isNotEmpty(qDelNum)){
			query.append(qPO).append(qPCode).append(qSDoc).append(qDelNum);
		}else{
			query.append(qOType).append(qFDesc);
			
			qry = " AND {sd.actualShipDate} >= CONVERT(DATETIME,?startDate) AND {sd.actualShipDate} <= CONVERT(DATETIME,?endDate) ";
			query.append(qry);		
			LOG.debug("qry with date....." + qry);
			
			/** Parsing the dates to Hybris understandable format and adding to query params **/
			if(jnjGTPageableData.getFromDate()!="" && jnjGTPageableData.getFromDate()!=null){
				LOG.debug("start date is "+jnjGTPageableData.getFromDate());
					String dateFrom=jnjGTPageableData.getFromDate();
					LOG.debug("dateFrom "+dateFrom);
				queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
						Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, -1));
					LOG.debug("queryParams Start Date "+ queryParams);
					 
				}
			if(jnjGTPageableData.getToDate()!="" && jnjGTPageableData.getToDate()!=null) {
				LOG.debug("end date is "+jnjGTPageableData.getToDate());
				String toDate=jnjGTPageableData.getToDate();
				LOG.debug("toDate "+toDate);
				queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
						Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
					LOG.debug("queryParams end Date "+ queryParams);
				 
			}
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		fQuery.setResultClassList(Arrays.asList(String.class, String.class, String.class,String.class, String.class, String.class,String.class, String.class, String.class,String.class, String.class, String.class,String.class, String.class, String.class,String.class, String.class, String.class, String.class, String.class));
		LOG.info("######################"+fQuery);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "fetchoaDeliverylistReport() Query " + fQuery);
		}
		
		
		
		final SearchResult<List<Object>> orderList = sessionService.executeInLocalView(new SessionExecutionBody()
        {
               @Override
               public SearchResult<List<Object>> execute()
               {
		final SearchResult<List<Object>> sresult = getFlexibleSearchService().search(fQuery);
		//finaL List<OrderEntryModel> result = getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
		LOG.info("######################Search Result Set "+sresult.getResult().size());
		return sresult;
               }
        }, userService.getAdminUser());
		
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return orderList;
	}


	/**
	 * This method fetches the order status query parameter value from config table
	 * 
	 * @return List<String>
	 */
	protected List<String> createOrderStatusQueryParam()
	{
		final String METHOD_NAME = "createOrderStatusQueryParam()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return jnjConfigService.getConfigValuesById(REPORTS_ORDER_STATUS, Jnjb2bCoreConstants.CONST_COMMA);
	}
	//Added for JJEPIC-825 Reports - Hema
		protected List<String> createLineStatusList()
		{
			final List<String> lineStatusList = new ArrayList<String>();
			lineStatusList.add(UNCONFIRMED_STRING);//backordered reports
			//lineStatusList.add("RJ");
			lineStatusList.add("CS");
			lineStatusList.add("CQ");
			lineStatusList.add("CC");
			return lineStatusList;

		}
		protected Map populateCancelledLineStatusQueryParams(final JnjGTPageableData JnjGTPageableData)
		{
			final String METHOD_NAME = "populateCancelledLineStatusQueryParams()";
			CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

			/** Populating the query parameters **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the query params.", LOG);
			final Map queryParams = new HashMap();

			/** Parsing the dates to Hybris understandable format and adding to query params **/
			queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(JnjGTPageableData.getFromDate(),
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0));
			queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(JnjGTPageableData.getToDate(),
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0));

			/** Adding rest of the query parameters **/
			queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, JnjGTPageableData.getSearchParamsList());
			queryParams.put(QUERY_PARAM_ORDER_TYPES, createOrderTypesList(false));
			DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -14);
			Date fourteenDaysPrior = cal.getTime();
			String fromdate = dateFormat.format(fourteenDaysPrior);
			/** pick records with line status CANCELLED within fourteen days **/
			queryParams.put(QUERY_PARAM__FOURTEEN, JnjGTCoreUtil.convertDateFormat(fromdate,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 0));

			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
					+ queryParams, LOG);

			CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return queryParams;
		}

		protected boolean checkIfAllValuesNull(JnjGTPageableData jnjGTPageableData){
			
			if(CollectionUtils.isNotEmpty(jnjGTPageableData.getSearchDtoList())){ 
				boolean isNull = false;
				for (JnjGTSearchDTO search: jnjGTPageableData.getSearchDtoList()) {
					  if (StringUtils.isBlank(search.getSearchValue())) {
						  isNull= true;
						  break;
					  }
					    
					  }
				return isNull;	
			}
			
			return true;
		}
		
		/**
		 * This method is used to query the database and fetch the data for invoice-past-due reports based on the query parameters
		 * supplied in jnjGTPageableData
		 * 
		 * @param jnjGTPageableData
		 * @return List<OrderEntryModel>
		 */
		@Override
		public List<JnjGTInvoiceModel> fetchInvoicePastDueReport(final JnjGTPageableData jnjGTPageableData)
		{
			final String METHOD_NAME = "fetchInvoicePastDueReport()";
			CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
			List<JnjGTInvoiceModel> result = new ArrayList<JnjGTInvoiceModel>();

			//** Populating the query parameters **//*
			final Map queryParams = populateInvoicePastDueQueryParams(jnjGTPageableData);
			//** Creating new flexible search query with the query string BACKORDER_REPORT_QUERY **//*
			StringBuilder query = new StringBuilder();
			query = query.append(INVOICE_DUE_REPORT_BASE_QYERY);
			if(jnjGTPageableData.getSearchBy() != null && !jnjGTPageableData.getSearchBy().equalsIgnoreCase("")){
				query = query.append(INVOICE_DUE_REPORT_INVOICE_NUM);
				}
				else if(jnjGTPageableData.getStatus() =="all"){
				query = query.append(INVOICE_DUE_REPORT_INVOICE_DUE_STATUS);
				}
				else{
					query = query.append(INVOICE_DUE_REPORT_INVOICE_DUE_DATE);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			try
			{
				CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Going to perform flexible search"
						+ queryParams, LOG);
				result = getFlexibleSearchService().<JnjGTInvoiceModel> search(fQuery).getResult();
				LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "Result obtained :: " + result);
			}
			catch (final ModelNotFoundException modelNotFoundException)
			{
				LOG.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
						+ "model not found oR ambiguous identifier exception for given ID" + Logging.HYPHEN
						+ modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
			}
			CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
			return result;
		}

		/**
	 * This method populates the query parameters for the back-order report
	 * 
	 * @param jnjGTPageableData
	 * @return queryParams
	 */

	protected Map populateInvoicePastDueQueryParams(final JnjGTPageableData jnjGTPageableData)
	{
		final String METHOD_NAME = "populateInvoicePastDueQueryParams()";
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		/** Populating the query parameters **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Populating the query params.", LOG);
		final Map queryParams = new HashMap();

		/** Parsing the dates to Hybris understandable format and adding to query params **/
 		if(jnjGTPageableData.getSearchBy()!="" && jnjGTPageableData.getSearchBy()!=null){
			LOG.debug("Invoice Number is "+jnjGTPageableData.getSearchBy());
			String invoiceNumber=jnjGTPageableData.getSearchBy();
			LOG.debug("invoiceNum "+invoiceNumber);
			queryParams.put(QUERY_PARAM_INVOICE_NUM, invoiceNumber);
				LOG.debug("queryParams Invoice Number"+ queryParams);
		}
		if(jnjGTPageableData.getFromDate()!="" && jnjGTPageableData.getFromDate()!=null){
			LOG.debug("Invoice Due Date is "+jnjGTPageableData.getFromDate());
			String invoiceDueDate=jnjGTPageableData.getFromDate();
			LOG.debug("dateFrom "+invoiceDueDate);
		queryParams.put(QUERY_PARAM_INVOICE_DUE_DATE, JnjGTCoreUtil.convertDateFormat(invoiceDueDate,
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
			LOG.debug("queryParams Invoice Due Date "+ queryParams);
			 
		}
		if(jnjGTPageableData.getStatus()!=null && jnjGTPageableData.getStatus()!="" && jnjGTPageableData.getStatus()=="all"){
			LOG.debug("Status is "+jnjGTPageableData.getStatus());
			String invoiceStatus=jnjGTPageableData.getStatus();
			LOG.debug("invoiceStatus "+invoiceStatus);
			queryParams.put(QUERY_PARAM_STATUS, invoiceStatus);
				LOG.debug("queryParams Invoice Status"+ queryParams);
		}
		if(jnjGTPageableData.getSearchBy()!="" && jnjGTPageableData.getSearchBy()!=null) {
			queryParams.put("invoiceNum", jnjGTPageableData.getSearchBy());
			}
		queryParams.put(QUERY_PARAM_LINE_STATUS, UNCONFIRMED_STRING); // Unconfirmed status for Back-order items
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Query params populated :: "
				+ queryParams, LOG);
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return queryParams;
	}
	
	
			@Override
		 public List<String> fetchFranchiseCode(final String code)
		 {
			 String query = "select distinct {globalBusinessUnit} from {JnJProduct} where {globalBusinessUnit} != ''";
			// String param = "where {productCode} = ?code";
			 final List resultClassList = new ArrayList();
			 resultClassList.add(String.class);
			 final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			 fQuery.setResultClassList(resultClassList);
			 final List<String> result = getFlexibleSearchService().<String>search(fQuery).getResult();
			return result;
					 
		 }
			
			/*AAOL-4603 AND AAOL 2410*/
		 @Override          
  		  public List<String> getFranchiseDesc()
    		{
           	final String METHOD_NAME = "getFranchiseDesc()";
           	CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
           	String query = "select distinct {FRANCHISENAME} from {JnJProduct}";
           	LOG.debug("query to pull franchisedesc......."+ query);
           	final List resultClassList = new ArrayList();
           	resultClassList.add(String.class);
           	final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
           	fQuery.setResultClassList(resultClassList);
           	
           	
            final List<String> result = sessionService.executeInLocalView(new SessionExecutionBody()
            {
                   @Override
                   public List<String> execute()
                   {
                         		List<String> fetchList = getFlexibleSearchService().<String>search(fQuery).getResult();
                         		return fetchList;
                   }
            }, userService.getAdminUser());
           	
        
           	if(result==null){
                 LOG.info("From DAO, franchiseDescList.."+result);
           }else{
                 LOG.info("FROM dao, franchiseDescList.."+result.size());
           }
   
           	CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD, LOG);
    return result;
                
}
			
	@Override
	public List<String> fetchPayerId(String accountid){
		try {
			final Map queryParams = new HashMap();
			
			final String query = "select {pk} from {address} where {owner} in ({{ select {unit:pk} from {jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner} and {unit:UID}= (?accountid) and {add:payFromAddress}='1'} }})";
			if(null != accountid){
				queryParams.put("accountid", accountid);
				LOG.debug("queryParams fetchPayerId"+ accountid);
			}
			final List resultClassList = new ArrayList();
			final List<String> addresses = new ArrayList<>();
			final List<AddressModel> result = sessionService.executeInLocalView(
					new SessionExecutionBody() {
						@Override
						public List<AddressModel> execute() {
							final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
									query);
							fQuery.addQueryParameters(queryParams);
							final List<AddressModel> result = getFlexibleSearchService()
									.<AddressModel> search(fQuery).getResult();
							if (result != null) {
								for (Iterator iterator = result
										.iterator(); iterator.hasNext();) {
									AddressModel addressModel = (AddressModel) iterator.next();
									addresses.add(addressModel.getJnJAddressId());
								}
								LOG.info("FROM dao, fetchPayerId."+result);
								return result;
								
							} else {
								LOG.info("FROM dao, fetchPayerId."+result.size());
								return null;
							}

						}
					}, userService.getAdminUser());
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + Logging.HYPHEN
					+ "Result obtained :: " + addresses);
			return addresses;
		} catch (final ModelNotFoundException modelNotFoundException) {
			LOG.error("fetchPayerId()" + Logging.HYPHEN
					+ "model is not found in hybris database for the code - "
					+ accountid + " - " + modelNotFoundException.getMessage(),
					modelNotFoundException);
			return null;
		}
	}
	
	/*AAOL-2410*/
	@Override
		public List<OrderModel> fetchSalesReport(final JnjGTPageableData jnjGTPageableData) {
			// TODO Auto-generated method stub
			final String METHOD_NAME = "fetchSalesReport()";
			if (LOG.isDebugEnabled())
			{
				LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}

			final Map queryParams = populateSalesReportQueryParams(jnjGTPageableData);
			final StringBuilder query = new StringBuilder();
			/** If no filtered accounts were found associated with the territory, returning Null response **/
			if (CollectionUtils.isEmpty(jnjGTPageableData.getSearchParamsList()))
			{
				
				if (LOG.isDebugEnabled())
				{
					LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
							+ JnJCommonUtil.getCurrentDateTime());
				}
				return null;
			}
			
			
			if(jnjGTPageableData != null){	
				query.append(SALES_REPORT_BASIC_QUERY);
				
				if(!checkIfAllValuesNull(jnjGTPageableData)){
					
					query.append(SALES_REPORT_QUERY_FOR_ALL_FIELDS);	
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					
					final List<OrderModel> orderList = sessionService.executeInLocalView(new SessionExecutionBody()
		             {
		                    @Override
		                    public List<OrderModel> execute()
		                    {
		                          	List<OrderModel> orders = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();
		                    			return orders;
		                    }
		             }, userService.getAdminUser());
					
		
					LOG.info("######################Result Set Size "+orderList.size());
					LOG.info("######################Result Set Size "+orderList);
					if (CollectionUtils.isNotEmpty(orderList))
					{
						return orderList;
					}
					else
					{
						return null;
					}
				}
					
				}
					
				if(CollectionUtils.isNotEmpty(jnjGTPageableData.getSearchDtoList())){
					int count= 0;
					for(JnjGTSearchDTO searchDto : jnjGTPageableData.getSearchDtoList()){
						
						if(StringUtils.isNotEmpty(searchDto.getSearchBy()) && searchDto.getSearchBy().equals("CustomerPONO") && !StringUtils.isBlank(searchDto.getSearchValue())){
							query.append(APPEND_SEARCH_CUSTOMER_PO_NO);
							count++;
							
						}else if(StringUtils.isNotEmpty(searchDto.getSearchBy()) && searchDto.getSearchBy().equals("SalesDocNo") && !StringUtils.isBlank(searchDto.getSearchValue())){
							query.append(APPEND_SEARCH_SALES_DOC_NO);
							count++;
							
						}else if(StringUtils.isNotEmpty(searchDto.getSearchBy()) && searchDto.getSearchBy().equals("DeliveryNo") && !StringUtils.isBlank(searchDto.getSearchValue())){
							
							query.append(APPEND_SEARCH_DELIVERY_NO);
							count++;
						}else if(StringUtils.isNotEmpty(searchDto.getSearchBy()) && searchDto.getSearchBy().equals("InvoiceNo") && !StringUtils.isBlank(searchDto.getSearchValue())) {
							
							query.append(APPEND_SEARCH_INVOICE_NO);
							count++;
						}else if(StringUtils.isNotEmpty(searchDto.getSearchBy()) && searchDto.getSearchBy().equals("ProductCode") && !StringUtils.isBlank(searchDto.getSearchValue())){
							query.append(APPEND_SEARCH_PRODUCT);
							count++;
						}
						
					}
					
					if(count == 0){
						
						if(!jnjGTPageableData.getStatus().equalsIgnoreCase("ALL")){
							query.append(APPEND_SEARCH_STATUS);
						}
						if(!jnjGTPageableData.getAdditionalSearchText().equalsIgnoreCase("ALL")){
							query.append(APPEND_SEARCH_FRANCHISE);
						}else{
							String allowedFranchise = sessionService.getAttribute("allowedFranchise");
							LOG.debug("Allowed Franchises"+ allowedFranchise);
							if(StringUtils.isNotEmpty(allowedFranchise)){
								LOG.debug("Allowed Franchises"+ allowedFranchise);
								query.append(" AND {jp.franchiseName} IN (" + allowedFranchise + ")");
							}
						}
						if(!jnjGTPageableData.getSearchText().equalsIgnoreCase("ALL")){
							query.append(APPEND_SEARCH_ORDERTYPE);
						}
						if(!StringUtils.isBlank(jnjGTPageableData.getFromDate())){
							query.append(APPEND_SEARCH_START_DATE);
						}
						if(!StringUtils.isBlank(jnjGTPageableData.getToDate())){
							query.append(APPEND_SEARCH_END_DATE);
						}
						
					}
				}
		
			//query.append("select DISTINCT {o.pk} from {order AS o JOIN OrderEntry AS oe ON {oe:order}={o:pk} JOIN JnJProduct AS jp ON {oe:product}={jp:pk} JOIN JnjGTInvoice AS iv ON {o.pk}={iv.order}  JOIN OrderStatus AS os ON {o:status}={os:pk} JOIN JnjOrderTypesEnum as ot ON {o:ordertype}={ot:pk} JOIN JnjGTShippingDetails AS sd ON {o.shippingDetails} LIKE CONCAT( '%', CONCAT( {sd.PK} , '%' ) )  } WHERE {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN ('00080891') }}) AND {sd.deliveryNum}='13415' AND {iv.invoiceNum} = '12341241267' AND {o.sapOrderNumber} = '00048000' AND {o.purchaseOrderNumber} = '123' AND {ot.code} = 'KB' AND {o.poDate} >= CONVERT(DATETIME,'2017-05-26') AND {o.poDate} <= CONVERT(DATETIME,'2017-06-30') AND {os.code} = 'ACCEPTED' AND {jp.code}='540137000' AND {jp.franchiseName} ='Jesson'");
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + "fetchMultiPurchaseAnalysisReport() Query " + fQuery);
			}
			LOG.info("######################"+fQuery);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}
			
			 final List<OrderModel> orderList = sessionService.executeInLocalView(new SessionExecutionBody()
             {
                    @Override
                    public List<OrderModel> execute()
                    {
                          
                   
                    			List<OrderModel> orders = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();
                    			return orders;
                    }
             }, userService.getAdminUser());
			
			//final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();
			LOG.info("######################Result Set Size "+orderList.size());
			LOG.info("######################Result Set Size "+orderList);
			if (CollectionUtils.isNotEmpty(orderList))
			{
				return orderList;
			}
			else
			{
				return null;
			}
			
			
			
		}
	@Override
	public List<String> fetchStockLocationAccount(String accountid){
		try {
			final Map queryParams = new HashMap();
			
			final String query = "select {pk} from {address} where {owner} in ({{ select {unit:pk} from {jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner} and {unit:UID}= (?accountid) and {add:payFromAddress}='1'} }})";
			if(null != accountid){
				queryParams.put("accountid", accountid);
				LOG.debug("queryParams fetchPayerId"+ accountid);
			}
			final List resultClassList = new ArrayList();
			final List<String> addresses = new ArrayList<>();
			final List<AddressModel> result = sessionService.executeInLocalView(
					new SessionExecutionBody() {
						@Override
						public List<AddressModel> execute() {
							final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
									query);
							fQuery.addQueryParameters(queryParams);
							final List<AddressModel> result = getFlexibleSearchService()
									.<AddressModel> search(fQuery).getResult();
							if (result != null) {
								for (Iterator iterator = result
										.iterator(); iterator.hasNext();) {
									AddressModel addressModel = (AddressModel) iterator.next();
									addresses.add(addressModel.getJnJAddressId());
								}
								LOG.info("FROM dao, fetchPayerId."+result);
								return result;
								
							} else {
								LOG.info("FROM dao, fetchPayerId."+result.size());
								return null;
							}

						}
					}, userService.getAdminUser());
			LOG.info(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + Logging.HYPHEN
					+ "Result obtained :: " + addresses);
			return addresses;
		} catch (final ModelNotFoundException modelNotFoundException) {
			LOG.error("fetchPayerId()" + Logging.HYPHEN
					+ "model is not found in hybris database for the code - "
					+ accountid + " - " + modelNotFoundException.getMessage(),
					modelNotFoundException);
			return null;
		}
	}
	
}
