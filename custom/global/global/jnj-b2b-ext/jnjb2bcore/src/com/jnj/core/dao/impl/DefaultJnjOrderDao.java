/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dao.JnjOrderDao;
import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;



/**
 * The JnjOrderDaoImpl class contains getOrderModel method which hits the Hybris database to retrieve the Order Model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjOrderDao extends DefaultOrderDao implements JnjOrderDao
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjOrderDao.class);

	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;
	@Autowired
	SessionService sessionService;
	@Autowired
	UserService userService;

	@Autowired
	DefaultCompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	public SessionService getSessionService() {
		return sessionService;
	}


	public UserService getUserService() {
		return userService;
	}


	public DefaultCompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}


	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	protected static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.PURCHASEORDERNUMBER + "}, {" + OrderModel.TOTALPRICE + "} FROM {"
			+ OrderModel._TYPECODE + "  AS order JOIN " + OrderStatus._TYPECODE
			+ " AS orderStatus ON {orderStatus:PK} = {order:status}} WHERE  {unit}=?unit AND {" + OrderModel.VERSIONID
			+ "} IS NULL ";

	protected static final String CONTRACT_SEARCH_QUERY = " and {contractnumber} like ?searchByCriteria ";
	protected static final String ORDER_NUMBER_SEARCH_QUERY = " and ( {purchaseOrderNumber} like ?searchByCriteria or {code} like ?searchByCriteria or {sapOrderNumber} like ?searchByCriteria )";
	protected static final String PRODUCT_SKU_SEARCH_QUERY = " and {jnjProduct:code} like ?searchByCriteria ";
	protected static final String INVOICE_NUMBER_SEARCH_QUERY = " and {jnjInvoiceOrder:invDocNo} like ?searchByCriteria ";
	protected static final String INDIRECT_CUSTOMER_SEARCH_QUERY = " and {indirectCustomer} like ?searchByCriteria ";

	protected static final String FIND_ORDERS_BY_PRODUCT_SKU_QUERY = "SELECT DISTINCT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.PURCHASEORDERNUMBER + "}, {" + OrderModel.TOTALPRICE + "} FROM {"
			+ OrderModel._TYPECODE + " AS order JOIN " + OrderEntryModel._TYPECODE
			+ " AS orderEntry ON {orderEntry:order}={order:pk} JOIN " + JnJProductModel._TYPECODE
			+ "  AS jnjProduct ON {jnjProduct:PK} = {orderEntry:PRODUCT} JOIN " + OrderStatus._TYPECODE
			+ " AS orderStatus ON {orderStatus:PK} = {order:status}} WHERE  {unit}=?unit AND {" + OrderModel.VERSIONID + "} IS NULL";

	protected static final String FIND_ORDERS_BY_INVOICE_NUMBER_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.PURCHASEORDERNUMBER + "}, {" + OrderModel.TOTALPRICE + "} FROM {"
			+ OrderModel._TYPECODE + " AS order JOIN " + JnJInvoiceOrderModel._TYPECODE
			+ " AS jnjInvoiceOrder ON {jnjInvoiceOrder:salesOrder}={order:sapOrderNumber} JOIN " + OrderStatus._TYPECODE
			+ " AS orderStatus ON {orderStatus:PK} = {order:status}} WHERE  {unit}=?unit AND {" + OrderModel.VERSIONID + "} IS NULL";

	protected static final String SORT_ORDERS_BY_DATE_DESC = " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK
			+ "}";
	protected static final String SORT_ORDERS_BY_DATE_ASC = " ORDER BY {" + OrderModel.CREATIONTIME + "} ASC, {" + OrderModel.PK
			+ "}";
	protected static final String SORT_ORDERS_BY_CODE_DESC = " ORDER BY {" + OrderModel.PURCHASEORDERNUMBER + "} DESC,{"
			+ OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	protected static final String SORT_ORDERS_BY_CODE_ASC = " ORDER BY {" + OrderModel.PURCHASEORDERNUMBER + "} ASC,{"
			+ OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	protected static final String SORT_ORDERS_BY_ITEM_PRICE_ASC = " ORDER BY {" + OrderModel.TOTALPRICE + "} ASC,{"
			+ OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	protected static final String SORT_ORDERS_BY_ITEM_PRICE_DESC = " ORDER BY {" + OrderModel.TOTALPRICE + "} DESC,{"
			+ OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	protected final String LIKE_CHARACTER = "%";
	protected String sortByCriteria;
	protected String searchByCriteria;
	protected String status;
	protected SortQueryData sortQueries;

	/**
	 * This method is used to get the order model from hybris Database
	 */
	@Override
	public List<OrderModel> getOrderModel()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final Map queryParams = new HashMap();
		final String query = Order.SUBMIT_ORDER_QUERY;
		queryParams.put(Order.CODE_STRING, Order.ORDER_STATUS);
		queryParams.put("excludedOrderType", JnJCommonUtil.getValues(Order.EXCLUDED_ORDER_TYPE, Jnjb2bCoreConstants.CONST_COMMA));
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Config Model Query " + fQuery);
		}

		final List<OrderModel> orderModelList = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (!orderModelList.isEmpty())
		{
			return orderModelList;
		}
		else
		{
			return null;
		}


	}


	/**
	 * this method is used to fetch the order status on the basis of passed status
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param creditStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 */

	@Override
	public List<JnjOrdStsMappingModel> getOrderStatus(final String overAllStatus, final String rejectionStatus,
			final String creditStatus, final String deliveryStatus, final String invoiceStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Map queryParams = new HashMap();
		final String query = Order.ORDER_STATUS_QUERY;
		fetchOrderStatus(overAllStatus, rejectionStatus, creditStatus, deliveryStatus, invoiceStatus, queryParams);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Staus Model Query " + fQuery);
		}

		final List<JnjOrdStsMappingModel> jnjOrderStatusModelList = getFlexibleSearchService().<JnjOrdStsMappingModel> search(
				fQuery).getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (!jnjOrderStatusModelList.isEmpty())
		{
			return jnjOrderStatusModelList;
		}
		else
		{
			return null;
		}
	}

	/**
	 * this method is used to fetch the order status on the basis of passed status
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param creditStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 * @param queryParams
	 */
	protected void fetchOrderStatus(final String overAllStatus, final String rejectionStatus, final String creditStatus,
			final String deliveryStatus, final String invoiceStatus, final Map queryParams)
	{
		if (null != overAllStatus)
		{
			queryParams.put(Order.OVER_ALL_STATUS, overAllStatus);
		}
		else
		{
			queryParams.put(Order.OVER_ALL_STATUS, Order.EMPTY_STRING);
		}
		if (null != rejectionStatus)
		{
			queryParams.put(Order.REJECTION_STATUS, rejectionStatus);
		}
		else
		{
			queryParams.put(Order.REJECTION_STATUS, Order.EMPTY_STRING);
		}
		if (null != creditStatus)
		{
			queryParams.put(Order.CREDIT_STATUS, creditStatus);
		}
		else
		{
			queryParams.put(Order.CREDIT_STATUS, Order.EMPTY_STRING);
		}
		if (null != deliveryStatus)
		{
			queryParams.put(Order.DELIVERY_STATUS, deliveryStatus);
		}
		else
		{
			queryParams.put(Order.DELIVERY_STATUS, Order.EMPTY_STRING);
		}
		if (null != invoiceStatus)
		{
			queryParams.put(Order.INVOICE_STATUS, invoiceStatus);
		}
		else
		{
			queryParams.put(Order.INVOICE_STATUS, Order.EMPTY_STRING);
		}
	}

	/**
	 * This method is used to fetch the order entry status on the basis of passed status
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 */
	@Override
	public List<JnjOrdEntStsMappingModel> getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Map queryParams = new HashMap();
		final String query = Order.ORDER_ENTRY_STATUS_QUERY;
		fetchOrderEntryStatus(overAllStatus, rejectionStatus, deliveryStatus, invoiceStatus, queryParams);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Entry Status Model Query " + fQuery);
		}

		final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = getFlexibleSearchService()
				.<JnjOrdEntStsMappingModel> search(fQuery).getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (!jnjOrderEntryStatusModelList.isEmpty())
		{
			return jnjOrderEntryStatusModelList;
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method is used to fetch the order entry status on the basis of passed status
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 * @param queryParams
	 */
	protected void fetchOrderEntryStatus(final String overAllStatus, final String rejectionStatus, final String deliveryStatus,
			final String invoiceStatus, final Map queryParams)
	{
		if (null != overAllStatus)
		{
			queryParams.put(Order.OVER_ALL_STATUS, overAllStatus);
		}
		else
		{
			queryParams.put(Order.OVER_ALL_STATUS, Order.EMPTY_STRING);
		}
		if (null != rejectionStatus)
		{
			queryParams.put(Order.REJECTION_STATUS, rejectionStatus);
		}
		else
		{
			queryParams.put(Order.REJECTION_STATUS, Order.EMPTY_STRING);
		}
		if (null != deliveryStatus)
		{
			queryParams.put(Order.DELIVERY_STATUS, deliveryStatus);
		}
		else
		{
			queryParams.put(Order.DELIVERY_STATUS, Order.EMPTY_STRING);
		}
		if (null != invoiceStatus)
		{
			queryParams.put(Order.INVOICE_STATUS, invoiceStatus);
		}
		else
		{
			queryParams.put(Order.INVOICE_STATUS, Order.EMPTY_STRING);
		}
	}

	/*
	 * This method is used to get all order details from Order model
	 * 
	 * @param orderNumber
	 * 
	 * @param purchaseOrderNumber
	 */
	@Override
	public List<OrderModel> geAlltOrderDetails(final String purchaseOrderNumber, String orderNumber)
	{
		String customerId = null;
		try
		{
			final Map queryParams = new HashMap();

			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

			customerId = jnjB2BUnitModel.getUid();

			final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
			LOGGER.debug("value of unit model" + unit);
			if (null == orderNumber || orderNumber.isEmpty())
			{
				orderNumber = purchaseOrderNumber;
			}

			final String query = "select {pk} from {order} where {unit}=?unit and {code} like ?orderNumber or {purchaseOrderNumber} like ?purchaseOrderNumber  ";
			queryParams.put(Jnjb2bCoreConstants.OrderHistory.B2BUNIT, unit);
			queryParams.put("purchaseOrderNumber", purchaseOrderNumber);
			queryParams.put("orderNumber", orderNumber);
			final List<OrderModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<OrderModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());
			return result;
		}
		catch (final ModelNotFoundException e)
		{
			return null;
		}

	}

	/**
	 * This method is used to get order details on search and sort criteria
	 * 
	 * @param orderHistoryDTO
	 * @param statuses
	 * @param pageableData
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.JnjOrderDao#getOrderDetails(de.hybris.platform.core.enums.OrderStatus[],
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData, com.jnj.core.dto.OrderHistoryDTO)
	 */
	@Override
	public SearchPageData<OrderModel> getOrderDetails(final OrderStatus[] statuses, final PageableData pageableData,
			final OrderHistoryDTO orderHistoryDTO)
	{
		String SEARCH_ORDERS_BY_STATUS = " and {orderStatus:code}=?statusDisplay";
		String SEARCH_ORDERS_BY_DATE = " and {creationtime} > CONVERT(DATETIME,?startDate) and {creationtime} < CONVERT(DATETIME,?toDate)";
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		String startDate = null;
		String toDate = null;
		final String customerId = jnjB2BUnitModel.getUid();

		final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
		sortByCriteria = orderHistoryDTO.getSortbynumber();
		status = orderHistoryDTO.getStatus();
		if ((null != orderHistoryDTO.getFromDate() && !StringUtils.isEmpty(orderHistoryDTO.getFromDate()) && (!("select a date")
				.equalsIgnoreCase(orderHistoryDTO.getFromDate())))
				&& (null != orderHistoryDTO.getToDate() && !StringUtils.isEmpty(orderHistoryDTO.getToDate()) && (!("select a date")
						.equalsIgnoreCase(orderHistoryDTO.getToDate()))))
		{
			startDate = getDateRange(orderHistoryDTO.getFromDate(), false);
			toDate = getDateRange(orderHistoryDTO.getToDate(), true);
		}

		final String statusDisplay = getStatusDisplay(status);
		if ((StringUtils.isEmpty(orderHistoryDTO.getToDate()) || StringUtils.isEmpty(orderHistoryDTO.getFromDate()))
				|| (orderHistoryDTO.getFromDate().equalsIgnoreCase("select a date") || orderHistoryDTO.getToDate().equalsIgnoreCase(
						"select a date")))
		{
			SEARCH_ORDERS_BY_DATE = StringUtils.EMPTY;
		}
		if (StringUtils.isEmpty(statusDisplay) || "All Status".equalsIgnoreCase(statusDisplay))
		{
			SEARCH_ORDERS_BY_STATUS = StringUtils.EMPTY;
		}
		if (sortByCriteria == null)
		{
			sortByCriteria = "Date - newest to oldest";
		}
		if (null != orderHistoryDTO.getFieldName() && !orderHistoryDTO.getFieldName().isEmpty()
				&& null != orderHistoryDTO.getCode() && !orderHistoryDTO.getCode().isEmpty())
		{
			final String searchParameter = orderHistoryDTO.getFieldName();
			searchByCriteria = orderHistoryDTO.getCode();

			sortQueries = null;

			searchWtihFieldValue(orderHistoryDTO, SEARCH_ORDERS_BY_STATUS, SEARCH_ORDERS_BY_DATE, searchParameter);

			if (null == sortQueries)
			{
				final SearchPageData<OrderModel> searchPageData = new SearchPageData<OrderModel>();
				searchPageData.setResults(new ArrayList<OrderModel>());
				searchPageData.setPagination(new PaginationData());
				searchPageData.setSorts(new ArrayList<SortData>());
				return searchPageData;
			}

			final Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("unit", unit);
			queryParams.put("searchByCriteria", LIKE_CHARACTER + searchByCriteria + LIKE_CHARACTER);
			{
				queryParams.put("statusDisplay", statusDisplay);
			}
			if ((!StringUtils.isEmpty(orderHistoryDTO.getToDate()) || !StringUtils.isEmpty(orderHistoryDTO.getFromDate()))
					&& (!orderHistoryDTO.getFromDate().equalsIgnoreCase("select a date") || !orderHistoryDTO.getToDate()
							.equalsIgnoreCase("select a date")))
			{
				queryParams.put("startDate", startDate);
				queryParams.put("toDate", toDate);
			}
			//}
			final SearchPageData<OrderModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public SearchPageData<OrderModel> execute()
				{
					final SearchPageData<OrderModel> result = search(sortQueries, sortByCriteria, queryParams, pageableData);
					return result;
				}
			}, userService.getAdminUser());
			return result;
		}
		else if (null != orderHistoryDTO.getFieldName() && !orderHistoryDTO.getFieldName().isEmpty())
		{
			final String searchParameter = orderHistoryDTO.getFieldName();
			searchByCriteria = orderHistoryDTO.getCode();

			sortQueries = null;

			searchWithBlankValue(orderHistoryDTO, SEARCH_ORDERS_BY_STATUS, SEARCH_ORDERS_BY_DATE, searchParameter);

			if (null == sortQueries)
			{
				final SearchPageData<OrderModel> searchPageData = new SearchPageData<OrderModel>();
				searchPageData.setResults(new ArrayList<OrderModel>());
				searchPageData.setPagination(new PaginationData());
				searchPageData.setSorts(new ArrayList<SortData>());
				return searchPageData;
			}

			final Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("unit", unit);
			queryParams.put("statusDisplay", statusDisplay);
			if ((!StringUtils.isEmpty(orderHistoryDTO.getToDate()))
					&& (!StringUtils.isEmpty(orderHistoryDTO.getFromDate()))
					&& (!orderHistoryDTO.getFromDate().equalsIgnoreCase("select a date") || !orderHistoryDTO.getToDate()
							.equalsIgnoreCase("select a date")))
			{
				queryParams.put("startDate", startDate);
				queryParams.put("toDate", toDate);
			}
			//}
			final SearchPageData<OrderModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public SearchPageData<OrderModel> execute()
				{
					final SearchPageData<OrderModel> result = search(sortQueries, sortByCriteria, queryParams, pageableData);
					return result;
				}
			}, userService.getAdminUser());
			return result;

		}
		else
		{
			final Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("unit", unit);
			SortQueryData sortQueries = null;
			final SortQueryData sortedQueries;
			if (statuses != null && statuses.length > 0)
			{
				queryParams.put("statusList", Arrays.asList(statuses));
				sortQueries = createQuery(sortQueries);
			}
			else
			{
				sortQueries = createQuery(sortQueries);
			}
			sortedQueries = sortQueries;
			if (null == sortedQueries)
			{
				final SearchPageData<OrderModel> searchPageData = new SearchPageData<OrderModel>();
				searchPageData.setResults(new ArrayList<OrderModel>());
				searchPageData.setPagination(new PaginationData());
				searchPageData.setSorts(new ArrayList<SortData>());
				return searchPageData;
			}

			final SearchPageData<OrderModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public SearchPageData<OrderModel> execute()
				{
					final SearchPageData<OrderModel> result = search(sortedQueries, sortByCriteria, queryParams, pageableData);
					return result;
				}
			}, userService.getAdminUser());
			return result;
		}
	}


	/**
	 * This method is used when page load or blank search is being called from front end
	 * 
	 * @param orderHistoryDTO
	 * @param SEARCH_ORDERS_BY_STATUS
	 * @param SEARCH_ORDERS_BY_DATE
	 * @param searchParameter
	 */
	protected void searchWithBlankValue(final OrderHistoryDTO orderHistoryDTO, final String SEARCH_ORDERS_BY_STATUS,
			final String SEARCH_ORDERS_BY_DATE, final String searchParameter)
	{
		if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.ORDER_NUMBER))
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (searchParameter.equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.CONTRACT_NUMBER))
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.PRODUCT_SKU))
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.INVOICE_NUMBER))
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.INDIRECT_CUSTOMER))
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
	}


	/**
	 * This method is being used when user search for perticular value from order history page
	 * 
	 * @param orderHistoryDTO
	 * @param SEARCH_ORDERS_BY_STATUS
	 * @param SEARCH_ORDERS_BY_DATE
	 * @param searchParameter
	 */
	protected void searchWtihFieldValue(final OrderHistoryDTO orderHistoryDTO, final String SEARCH_ORDERS_BY_STATUS,
			final String SEARCH_ORDERS_BY_DATE, final String searchParameter)
	{
		if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.ORDER_NUMBER)
				&& searchByCriteria.isEmpty())
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (searchParameter.equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.CONTRACT_NUMBER) && !searchByCriteria.isEmpty())
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE
					+ CONTRACT_SEARCH_QUERY;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.ORDER_NUMBER)
				&& !searchByCriteria.isEmpty())
		{

			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE
					+ ORDER_NUMBER_SEARCH_QUERY;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.PRODUCT_SKU)
				&& !searchByCriteria.isEmpty())
		{

			final String query = FIND_ORDERS_BY_PRODUCT_SKU_QUERY + SEARCH_ORDERS_BY_STATUS + PRODUCT_SKU_SEARCH_QUERY
					+ SEARCH_ORDERS_BY_DATE;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.INVOICE_NUMBER)
				&& !searchByCriteria.isEmpty())
		{
			final String query = FIND_ORDERS_BY_INVOICE_NUMBER_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE
					+ INVOICE_NUMBER_SEARCH_QUERY;
			createQueryForField(query);
		}
		else if (orderHistoryDTO.getFieldName().equalsIgnoreCase(Jnjb2bCoreConstants.OrderHistory.INDIRECT_CUSTOMER)
				&& !searchByCriteria.isEmpty())
		{
			final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SEARCH_ORDERS_BY_STATUS + SEARCH_ORDERS_BY_DATE
					+ INDIRECT_CUSTOMER_SEARCH_QUERY;
			createQueryForField(query);
		}
	}

	/**
	 * Thsi method is used to create query for field value
	 * 
	 * @param query
	 */
	protected void createQueryForField(final String query)
	{
		if (sortByCriteria.equalsIgnoreCase("Order Number - increasing"))
		{

			sortQueries = createSortQueryData("Order Number - increasing", query + SORT_ORDERS_BY_CODE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Order Number - decreasing"))
		{

			sortQueries = createSortQueryData("Order Number - decreasing", query + SORT_ORDERS_BY_CODE_DESC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Date - newest to oldest"))
		{

			sortQueries = createSortQueryData("Date - newest to oldest", query + SORT_ORDERS_BY_DATE_DESC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Date - oldest to newest"))
		{

			sortQueries = createSortQueryData("Date - oldest to newest", query + SORT_ORDERS_BY_DATE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Price - low to high"))
		{

			sortQueries = createSortQueryData("Price - low to high", query + SORT_ORDERS_BY_ITEM_PRICE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Price - high to low"))
		{

			sortQueries = createSortQueryData("Price - high to low", query + SORT_ORDERS_BY_ITEM_PRICE_DESC);
		}
	}

	/**
	 * This method is used to create the query
	 * 
	 * @param sortQueries
	 * @return SortQueryData sortQueries
	 */
	protected SortQueryData createQuery(SortQueryData sortQueries)
	{
		if (sortByCriteria.equalsIgnoreCase("Order Number - increasing"))
		{

			sortQueries = createSortQueryData("Order Number - increasing", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_CODE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Order Number - decreasing"))
		{

			sortQueries = createSortQueryData("Order Number - decreasing", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_CODE_DESC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Date - newest to oldest"))
		{

			sortQueries = createSortQueryData("Date - newest to oldest", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_DATE_DESC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Date - oldest to newest"))
		{

			sortQueries = createSortQueryData("Date - oldest to newest", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_DATE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Price - low to high"))
		{

			sortQueries = createSortQueryData("Price - low to high", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_ITEM_PRICE_ASC);
		}
		else if (sortByCriteria.equalsIgnoreCase("Price - high to low"))
		{

			sortQueries = createSortQueryData("Price - high to low", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY
					+ SORT_ORDERS_BY_ITEM_PRICE_DESC);
		}
		return sortQueries;
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	/**
	 * This method is used to get the Product codes on the basis of order ID
	 */
	@Override
	public List<String> getProductCodes(final String orderId)
	{

		String customerId = null;
		try
		{
			final Map queryParams = new HashMap();

			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

			customerId = jnjB2BUnitModel.getUid();

			final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
			LOGGER.debug("value of unit model" + unit);

			final String query = "select {pk} from {order} where {unit}=?unit and {sapOrderNumber}=?orderId ";
			queryParams.put(Jnjb2bCoreConstants.OrderHistory.B2BUNIT, unit);
			queryParams.put("orderId", orderId);
			final List<String> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<String> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<String> result = getFlexibleSearchService().<String> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());

			return result;
		}
		catch (final ModelNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 * this method is used to convert Status information on the the basis of selected field
	 */
	protected String getStatusDisplay(final String orderStatus)
	{
		String StatusDisplay = null;
		if (null != orderStatus)
		{
			if (orderStatus.equalsIgnoreCase("IN_PROCESS"))
			{
				StatusDisplay = "IN_PROCESS";
			}
			else if (orderStatus.equalsIgnoreCase("Open"))
			{
				StatusDisplay = "OPEN";
			}
			else if (orderStatus.equalsIgnoreCase("Approved"))
			{
				StatusDisplay = "APPROVED";
			}
			else if (orderStatus.equalsIgnoreCase("Created"))
			{
				StatusDisplay = "CREATED";
			}
			else if (orderStatus.equalsIgnoreCase("Rejected"))
			{
				StatusDisplay = "REJECTED";
			}
			else if (orderStatus.equalsIgnoreCase("Completed"))
			{
				StatusDisplay = "COMPLETED";
			}
			else if (orderStatus.equalsIgnoreCase("INVALID"))
			{
				StatusDisplay = "INVALID";
			}
			else
			{
				StatusDisplay = "All Status";
			}
		}
		return StatusDisplay;
	}


	/*
	 * This method is used to convert date range into hybris date format
	 */
	protected String getDateRange(final String date, final boolean isEndDate)
	{
		final SimpleDateFormat fromUser = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat myFormat = new SimpleDateFormat(jnjCommonUtil.getDBDateFormat());
		String reformattedStr = null;
		try
		{
			if (isEndDate)
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTime(fromUser.parse(date));
				cal.add(Calendar.DATE, 1);
				myFormat.setCalendar(cal);
				reformattedStr = myFormat.format(cal.getTime());
			}

			else
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTime(fromUser.parse(date));
				cal.add(Calendar.DATE, 0);
				myFormat.setCalendar(cal);
				reformattedStr = myFormat.format(cal.getTime());
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return reformattedStr;

	}

	@Override
	public OrderModel geLatestOrderDetails(final String unit) throws ModelNotFoundException, BusinessException
	{
		OrderModel orderModel = null;
		if (null != unit)
		{

			final Map queryParams = new HashMap();
			final String query = "select {pk} from {order} where {sapordernumber} is not null and {unit}=?unit order by {creationtime} desc";
			queryParams.put("unit", unit);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("geLatestOrderDetails()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Model Query " + fQuery);
			}

			final SearchResult<OrderModel> result = getFlexibleSearchService().search(fQuery);
			if (null != result && null != result.getResult() && !result.getResult().isEmpty())
			{
				orderModel = result.getResult().get(0);
			}

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("geLatestOrderDetails()" + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("geLatestOrderDetails()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME
						+ JnJCommonUtil.getCurrentDateTime() + "Unit is Null. Throwing Exception");
			}
			throw new BusinessException("Unit is Null");
		}
		return orderModel;
	}

	public <T> SearchPageData<T> search(final SortQueryData sortQueries, final String defaultSortCode,
			final Map<String, ?> queryParams, final PageableData pageableData)
	{
		ServicesUtil.validateParameterNotNull(sortQueries, "sortQueries cannot be null");
		ServicesUtil.validateParameterNotNull(defaultSortCode, "defaultSortCode cannot be null");
		ServicesUtil.validateParameterNotNull(pageableData, "pageableData cannot be null");
		Assert.isTrue(pageableData.getCurrentPage() >= 0, "pageableData current page must be zero or greater");
		Assert.isTrue(pageableData.getPageSize() > 0, "pageableData page size must be greater than zero");

		final SearchPageData searchPageData = search(sortQueries.getQuery(), queryParams, pageableData);
		searchPageData.getPagination().setSort(defaultSortCode);
		searchPageData.setSorts(createSorts(sortQueries, defaultSortCode));
		return searchPageData;
	}


	protected List<SortData> createSorts(final SortQueryData sortQueries, final String selectedSortCode)
	{
		final List result = new ArrayList();

		result.add(createSort(sortQueries, selectedSortCode));

		return result;
	}

	protected SortData createSort(final SortQueryData sortQuery, final String selectedSortCode)
	{
		final SortData sortData = createSortData();
		sortData.setCode(sortQuery.getSortCode());
		sortData.setName(sortQuery.getSortName());
		sortData.setSelected((selectedSortCode != null) && (selectedSortCode.equals(sortQuery.getSortCode())));
		return sortData;
	}

	protected SortData createSortData()
	{
		return new SortData();
	}

	public <T> SearchPageData<T> search(final String query, final Map<String, ?> queryParams, final PageableData pageableData)
	{
		ServicesUtil.validateParameterNotNull(query, "query cannot be null");
		ServicesUtil.validateParameterNotNull(pageableData, "pageableData cannot be null");
		Assert.isTrue(pageableData.getCurrentPage() >= 0, "pageableData current page must be zero or greater");
		Assert.isTrue(pageableData.getPageSize() > 0, "pageableData page size must be greater than zero");

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		if ((queryParams != null) && (!(queryParams.isEmpty())))
		{
			searchQuery.addQueryParameters(queryParams);
		}

		searchQuery.setNeedTotal(true);
		searchQuery.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
		searchQuery.setCount(pageableData.getPageSize());

		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);


		final SearchPageData result = createSearchPageData();
		result.setResults(searchResult.getResult());
		result.setPagination(createPagination(pageableData, searchResult));
		return result;
	}

	protected <T> SearchPageData<T> createSearchPageData()
	{
		return new SearchPageData();
	}

	protected SortQueryData findSortQueryData(final List<SortQueryData> sortQueries, final String requestedSortCode,
			final String defaultSortCode)
	{
		ServicesUtil.validateParameterNotNull(sortQueries, "sortQueries cannot be null");
		ServicesUtil.validateParameterNotNull(defaultSortCode, "defaultSortCode cannot be null");

		SortQueryData defaultQuery = null;
		SortQueryData requestedQuery = null;

		for (final SortQueryData sortQueryData : sortQueries)
		{
			if (defaultSortCode.equals(sortQueryData.getSortCode()))
			{
				defaultQuery = sortQueryData;
			}

			if ((requestedSortCode == null) || (!(requestedSortCode.equals(sortQueryData.getSortCode()))))
			{
				continue;
			}
			requestedQuery = sortQueryData;

		}

		return ((requestedQuery != null) ? requestedQuery : defaultQuery);
	}

	protected <T> PaginationData createPagination(final PageableData pageableData, final SearchResult<T> searchResult)
	{
		final PaginationData paginationData = createPaginationData();
		paginationData.setPageSize(pageableData.getPageSize());
		paginationData.setSort(pageableData.getSort());
		paginationData.setTotalNumberOfResults(searchResult.getTotalCount());


		paginationData.setNumberOfPages((int) Math.ceil(paginationData.getTotalNumberOfResults() / paginationData.getPageSize()));


		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		return paginationData;
	}

	protected PaginationData createPaginationData()
	{
		return new PaginationData();
	}

}
