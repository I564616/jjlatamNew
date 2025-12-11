/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.reports.impl.DefaultJnjGTReportsDao;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.la.core.daos.JnjLaReportsDao;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnjOrderTypeModel;
import com.jnj.la.core.model.JnjLaOpenOrdersReportTemplateModel;
import com.jnj.core.enums.JnjOrderTypesEnum;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;


public class DefaultJnjLaReportsDao extends DefaultJnjGTReportsDao implements JnjLaReportsDao  {
	protected static final String ORDER_TYPES = "backorder.restrict.order.types.";
	protected static final String ORDER_ENTRY_STATUS = "backorder.order.entry.statuses";
	protected static final String QUERY_PARAM_STATUS = "status";
	protected static final String DEFAULT_ENTRY_STATUS = "INVOICED,SHIPPED,DELIVERED,CANCELLED";	
	protected static final String QUERY_PARAMS_ORDER_NO="orderNumber";
	protected static final String QUERY_PARAM_ENTRY_STATUS_INVOICED="statusInvoiced";
	protected static final String QUERY_PARAM_ENTRY_STATUS_DELIVERED="statusDelivered";
	protected static final String BACKORDER_REPORT_DATES_QUERY = " AND {order:creationtime} BETWEEN CONVERT(DATETIME,?startDate) AND  CONVERT(DATETIME,?endDate)";
	protected static final String BACKORDER_REPORT_ENTRY_STATUS_QUERY = " {oe:status} NOT IN (?status) AND ";
	protected static final String BACKORDER_REPORT_UNIT_QUERY = "{order:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN (?accountNumbers)}})";
	protected static final String BACKORDER_REPORT_ORDER_STATUS_QUERY = " {order:orderType} NOT IN ({{SELECT {PK} FROM {JnjOrderTypesEnum} WHERE {CODE} IN (?orderTypes)}}) AND ";
	protected static final String ORDER_NUMBER_QUERY = "  {o:sapOrderNumber} = ?orderNumber AND ";
	protected static final String OPEN_ORDERS_REPORT_UNIT_QUERY = "  {o:unit} IN ({{SELECT {pk} from {JnjB2BUnit} where {uid} IN (?accountNumbers)}}) AND ";
	protected static final String OPEN_ORDERS_REPORT_SHIP_TO_ACCOUNT_QUERY = " {o:deliveryAddress} in ({{select {pk} from {Address} where {jnJAddressId} = ?shipTo or {streetname} = ?shipTo or {streetnumber} = ?shipTo  or {postalcode} = ?shipTo or {town} = ?shipTo}}) AND ";
	protected static final String OPEN_ORDERS_REPORT_DATES_QUERY = " {o:creationtime} BETWEEN CONVERT(DATETIME,?startDate) AND  CONVERT(DATETIME,?endDate)";
	protected static final String ORDER_TYPES_QUERY = "select {PK} from {JnjOrderType}";
	protected static final String OPEN_ORDERS_REPORT_TEMPLATE_QUERY = "select {template:PK} from {JnjLaOpenOrdersReportTemplate as template JOIN JnJB2bCustomer as user on {template:user} = {user:PK}} where {user:uid} = ?userId order by {modifiedtime} desc";
	protected static final String OPEN_ORDERS_REPORT_TEMPLATE_QUERY_BY_NAME = "select {template:PK} from {JnjLaOpenOrdersReportTemplate as template JOIN JnJB2bCustomer as user on {template:user} = {user:PK}} where {user:uid} = ?userId and {template:templateName} = ?name";
	protected static final String OPEN_ORDERS_REPORT_ENTRY_STATUS_QUERY = " {oe:status} NOT IN (?statusInvoiced, ?statusDelivered) AND ";

	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	protected ConfigurationService configurationService;

	@Override
	public List<OrderEntryModel> fetchBackOrderReport(final JnjGTPageableData jnjGTPageableData)
	{
		List<OrderEntryModel> reportsResult = new ArrayList<>();

		final Map<String,Object> queryParams = populateLaBackorderQueryParams(jnjGTPageableData);
		StringBuilder query = new StringBuilder("SELECT distinct {oe.PK}");
		query.append("FROM {").append(OrderModel._TYPECODE).append(" AS order JOIN ")
				.append(JnJLaB2BUnitModel._TYPECODE).append(" AS unit ON {order:unit} = {unit:pk} JOIN ")
				.append(OrderEntryModel._TYPECODE).append(" AS oe ON {order.pk}={oe.order} JOIN ")
				.append(JnJLaProductModel._TYPECODE).append(" as product on {product:pk}={oe:product}} where");
		final String country = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		final String orderType = configurationService.getConfiguration().getString(ORDER_TYPES+country);
		if (StringUtils.isNotEmpty(orderType)) {
			query.append(BACKORDER_REPORT_ORDER_STATUS_QUERY);
			queryParams.put(QUERY_PARAM_ORDER_TYPES, Arrays.asList(orderType.split(",")));
		}
		
		query.append(BACKORDER_REPORT_ENTRY_STATUS_QUERY).append(BACKORDER_REPORT_UNIT_QUERY);
		query.append(BACKORDER_REPORT_DATES_QUERY);

		return getOrderEntries(reportsResult, queryParams, query);
	}

	private List<OrderEntryModel> getOrderEntries(List<OrderEntryModel> reportsResult,
												  final Map<String, Object> queryParams, final StringBuilder query) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		final List<OrderEntryModel> result1 = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public List<OrderEntryModel> execute()
			{
				return getFlexibleSearchService().<OrderEntryModel> search(fQuery).getResult();
			}
		}, userService.getAdminUser());

		if (result1 != null)
		{
			reportsResult.addAll(result1);
		}

		return reportsResult;
	}

	private final Map<String, Object> populateLaBackorderQueryParams(final JnjGTPageableData jnjGTPageableData) {
		final Map<String, Object> queryParams = new HashMap<>();
		if(StringUtils.isNotBlank(jnjGTPageableData.getFromDate())){
			final String dateFrom=jnjGTPageableData.getFromDate();
			queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
				Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT, Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT,
					-1));
			 
		}
		if(StringUtils.isNotBlank(jnjGTPageableData.getToDate())) {
			final String toDate=jnjGTPageableData.getToDate();
			queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT,
					Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
			 
		}
		queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
		final List<String> entryStatus = Arrays.asList((configurationService.getConfiguration()
				.getString(ORDER_ENTRY_STATUS, DEFAULT_ENTRY_STATUS).split(",")));
		queryParams.put(QUERY_PARAM_STATUS, entryStatus);
		return queryParams;
	}

	private static final Map<String, Object> populateLaOpenOrdersReportQueryParams(
			final JnjGTPageableData jnjGTPageableData) {
		final Map<String, Object> queryParams = new HashMap<>();
		if (StringUtils.isNotBlank(jnjGTPageableData.getFromDate())) {
			final String dateFrom = jnjGTPageableData.getFromDate();
			queryParams.put(QUERY_PARAM_START_DATE, JnjGTCoreUtil.convertDateFormat(dateFrom,
					Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT,
					Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, -1));
		}
		if (StringUtils.isNotBlank(jnjGTPageableData.getToDate())) {
			final String toDate = jnjGTPageableData.getToDate();
			queryParams.put(QUERY_PARAM_END_DATE, JnjGTCoreUtil.convertDateFormat(toDate,
											Jnjb2bCoreConstants.Reports.OBTAINED_DATE_FORMAT,
											Jnjb2bCoreConstants.HYBRIS_UNDERSTANDABLE_DATE_FORMAT, 1));
		}

		return queryParams;
	}
	
	@Override
	public List<JnjOrderTypeModel> getOrderTypes() {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(ORDER_TYPES_QUERY);
		return getFlexibleSearchService().<JnjOrderTypeModel> search(fQuery).getResult();
	}

	@Override
	public List<JnjLaOpenOrdersReportTemplateModel> getOpenOrdersReportTemplate(final String userId) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(OPEN_ORDERS_REPORT_TEMPLATE_QUERY);
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("userId", userId);
		fQuery.addQueryParameters(queryParams);
		return getFlexibleSearchService().<JnjLaOpenOrdersReportTemplateModel>search(fQuery).getResult();
	}
	
	@Override
    public JnjLaOpenOrdersReportTemplateModel getOpenOrdersReportTemplate(final String userId,
																		  final String templateName) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(OPEN_ORDERS_REPORT_TEMPLATE_QUERY_BY_NAME);
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("userId", userId);
		queryParams.put("name", templateName);
		fQuery.addQueryParameters(queryParams);
		final List<JnjLaOpenOrdersReportTemplateModel> result1 = getFlexibleSearchService()
		.<JnjLaOpenOrdersReportTemplateModel> search(fQuery).getResult();
		if (CollectionUtils.isNotEmpty(result1)) {
		   return result1.get(0);
		} else {
			return null;
		}
	}

	public List<OrderEntryModel> fetchOpenOrdersReport(final JnjGTPageableData jnjGTPageableData) {

		List<OrderEntryModel> reportsResult = new ArrayList<>();

		final Map<String, Object> queryParams = populateLaOpenOrdersReportQueryParams(jnjGTPageableData);
		StringBuilder query = new StringBuilder("SELECT distinct {oe.PK}");
		query.append("FROM {").append(OrderModel._TYPECODE)
				.append(" AS o JOIN ").append(JnJLaB2BUnitModel._TYPECODE)
				.append(" AS unit ON {o:unit} = {unit:pk} JOIN ")
				.append(OrderEntryModel._TYPECODE)
				.append(" AS oe ON {o.pk}={oe.order} JOIN ")
				.append(JnJLaProductModel._TYPECODE)
				.append(" as product on {product:pk}={oe:product} JOIN ")
				.append(JnjOrderTypesEnum._TYPECODE)
				.append(" AS status ON {o:orderType}={status:pk}} WHERE");
		if (null != jnjGTPageableData.getOrderType() && StringUtils.isNotEmpty(jnjGTPageableData.getOrderType())
				&& !"ALL".equalsIgnoreCase(jnjGTPageableData.getOrderType())) {
			LOG.debug("OrderType is " + jnjGTPageableData.getOrderType());
			String orderType = jnjGTPageableData.getOrderType();
			queryParams.put("orderTypes", orderType);
			query.append(" {status.code} IN (?orderTypes) AND ");
		}
		if (CollectionUtils.isNotEmpty(jnjGTPageableData.getSearchParamsList())) {
			queryParams.put(QUERY_PARAM_ACCOUNT_NUMBERS, jnjGTPageableData.getSearchParamsList());
			query.append(OPEN_ORDERS_REPORT_UNIT_QUERY);
		}

		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchBy())) {
			query.append(" ({oe:product}= ?productCode or {oe:referencedVariant}= ?productCode)  AND ");
			queryParams.put(PRODUCT, jnjGTPageableData.getSearchBy());
		}

		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText())) {
			query.append(ORDER_NUMBER_QUERY);
			queryParams.put(QUERY_PARAMS_ORDER_NO, jnjGTPageableData.getSearchText());
		}

		if (StringUtils.isNotEmpty(jnjGTPageableData.getAdditionalSearchText())) {
			query.append(OPEN_ORDERS_REPORT_SHIP_TO_ACCOUNT_QUERY);
			queryParams.put(SHIP_TO, jnjGTPageableData.getAdditionalSearchText());
		}

		query.append(OPEN_ORDERS_REPORT_ENTRY_STATUS_QUERY);
		queryParams.put(QUERY_PARAM_ENTRY_STATUS_INVOICED, OrderEntryStatus.INVOICED.getCode());
		queryParams.put(QUERY_PARAM_ENTRY_STATUS_DELIVERED, OrderEntryStatus.DELIVERED.getCode());

		query.append(OPEN_ORDERS_REPORT_DATES_QUERY);

		return getOrderEntries(reportsResult, queryParams, query);
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	public void setJnjGetCurrentDefaultB2BUnitUtil(
			final JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil) {
		this.jnjGetCurrentDefaultB2BUnitUtil = jnjGetCurrentDefaultB2BUnitUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(
			final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}