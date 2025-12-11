/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.daos.JnjSellOutReportsDao;
import com.jnj.la.core.model.JnjSellOutReportModel;


/**
 * This class converts the sell out reports model into list object.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSellOutReportsDaoImpl extends AbstractItemDao implements JnjSellOutReportsDao
{
	private static final Logger LOG = Logger.getLogger(JnjSellOutReportsDaoImpl.class);

	private final String SORT_ORDER_ASC = "Oldest to newest";
	private final String SORT_ORDER_DESC = "Newest to oldest";

	@Autowired
	protected DefaultCompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;
	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	private static final Class<JnjSellOutReportsDaoImpl> currentClass = JnjSellOutReportsDaoImpl.class;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<JnjSellOutReportModel> getSellOutReportData(final String sortflag, final PageableData pageableData)
	{
		final String methodName = "getSellOutReportData()";
		SearchPageData<JnjSellOutReportModel> result = null;
		String customerId = null;
		try
		{
			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
			customerId = jnjB2BUnitModel.getUid();
			final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName, "value of unit model"
					+ unit, currentClass);

			final Map queryParams = new HashMap();

			String query = "";
			List<SortQueryData> sortQueries = null;

			if (sortflag.equalsIgnoreCase(SORT_ORDER_DESC))
			{
				query = "select {pk} from {JnjSellOutReport} where {b2bUnitId}=?unit ORDER BY {date} desc";
			}
			else if (sortflag.equalsIgnoreCase(SORT_ORDER_ASC))
			{
				query = "select {pk} from {JnjSellOutReport} where {b2bUnitId}=?unit ORDER BY {date}";

			}
			else
			{
				query = "select {pk} from {JnjSellOutReport} where {b2bUnitId}=?unit";
			}
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName,
					"sortflag : " + sortflag, currentClass);

			if (("").equals(sortflag))
			{

				sortQueries = Arrays.asList(createSortQueryData("byDate", query));
			}
			else
			{
				sortQueries = Arrays.asList(createSortQueryData(sortflag, query));
			}
			queryParams.put(Jnjb2bCoreConstants.OrderHistory.B2BUNIT, unit);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName,
					"Executing query for obtaining sell out reports records.", currentClass);

			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName, "unit : " + unit,
					currentClass);

			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName, "fQuery : " + fQuery,
					currentClass);

			result = pagedFlexibleSearchService.<JnjSellOutReportModel> search(fQuery, pageableData);
		}
		catch (final Exception exp)
		{
			JnjGTCoreUtil.logErrorMessage(
					Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS,
					methodName,
					"Error while executing query to get Cross Reference Table records in JnJCrossReferenceDao class - "
							+ exp.getMessage(), currentClass);
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS, methodName,
					"Error while executing query to get Cross Reference Table records in JnJCrossReferenceDao class - " + exp,
					currentClass);
		}
		return result;

	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}
}