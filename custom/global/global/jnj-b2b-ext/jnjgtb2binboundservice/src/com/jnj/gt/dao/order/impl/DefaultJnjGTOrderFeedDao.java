/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.dao.order.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.order.JnjGTOrderFeedDao;
import com.jnj.gt.model.JnjGTIntOrdLinePriceLocalModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;


/**
 * The JnjGTB2BUnitFeedDaoImpl class contains all those methods which are dealing with customer related intermediate
 * model and it has definition of all the methods which are defined in the JnjGTB2BUnitFeedDao interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOrderFeedDao extends AbstractItemDao implements JnjGTOrderFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderFeedDao.class);
	private static final String FIND_ORDER_LINE_PRICE = " SELECT {pk}"
			+ " FROM { JnjGTIntOrdLinePriceLocal} "
			+ " WHERE {pricingConditionType} IN (?pricingConditionTypes) AND {sapOrderNumber} = ?sapOrderNumber AND {sourceSystemId} = ?sourceSystemId AND "
			+ "{sapOrderLineNumber} = ?sapOrderLineNumber";
	private static final String FIND_ORDER_LINE = " SELECT {pk}" + " FROM {JnjGTIntOrderLine} "
			+ " WHERE {sapOrderNumber} = ?sapOrderNumber ";
	private static final String AND_ITEM_CATEGORY_EQUAL = "AND {itemCategory} IN (?itemCategory)";
	private static final String AND_SOURCE_SYS_ID_EQUAL = " AND {sourceSystemId} = ?sourceSystemId";
	private static final String AND_HIGHER_LVL_CD_EQUAL = " AND {highLevelItemNumber} = ?parentItemNumber";
	private static final String SELECT_INT_SCH_LINES = "SELECT {PK} FROM {JnjGTIntOrderSchLine} WHERE {sourceSystemId}=?sourceSystemId AND {sapOrderNumber}="
			+ "?sapOrderNumber AND {sapOrderLineNumber}=?sapOrderLineNumber";

	private static final String SELECT_CURRENCY_FROM_LINE_PRICE_LOCL = "SELECT {PK} FROM {JNJGTINTORDLINEPRICELOCAL} WHERE {CURRENCYFORCV} IS NOT NULL AND "
			+ "{SAPORDERNUMBER}=?sapOrderNumber AND {SOURCESYSTEMID}=?sourceSystemId";

	@Override
	public List<JnjGTIntOrdLinePriceLocalModel> getPriceJnjGTIntOrdLineHoldLocal(final String sapOrderNumber,
			final String sapOrderLineNumber, final String sourceSystemId, final String[] priceConditionType)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getPriceJnjGTIntOrdLineHoldLocal()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final Map queryParams = new HashMap();
		queryParams.put("pricingConditionTypes", Arrays.asList(priceConditionType));
		queryParams.put("sapOrderNumber", sapOrderNumber);
		queryParams.put("sourceSystemId", sourceSystemId);
		queryParams.put("sapOrderLineNumber", sapOrderLineNumber);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_ORDER_LINE_PRICE);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getPriceJnjGTIntOrdLineHoldLocal()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Price Sum Query " + fQuery);
		}

		final List<JnjGTIntOrdLinePriceLocalModel> result = getFlexibleSearchService().<JnjGTIntOrdLinePriceLocalModel> search(
				fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getPriceJnjGTIntOrdLineHoldLocal()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public List<JnjGTIntOrderLineModel> getJnjGTIntOrderLineModel(final String sapOrderNumber, final String sourceSystemId,
			final List<String> itemCategory, final String parentItemNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderLineModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final Map queryParams = new HashMap();
		String query = FIND_ORDER_LINE;

		// If item category is not empty then only search using item category
		if (CollectionUtils.isNotEmpty(itemCategory))
		{
			queryParams.put("itemCategory", itemCategory);
			query = query + AND_ITEM_CATEGORY_EQUAL;
		}
		// If highLevelItemNumber is not blank, query using highLevelItemNumber
		if (StringUtils.isNotBlank(parentItemNumber))
		{
			queryParams.put("parentItemNumber", parentItemNumber);
			query = query + AND_HIGHER_LVL_CD_EQUAL;
		}

		query = query + AND_SOURCE_SYS_ID_EQUAL;
		queryParams.put("sapOrderNumber", sapOrderNumber);
		queryParams.put("sourceSystemId", sourceSystemId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderLineModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Order line query " + fQuery);
		}

		final List<JnjGTIntOrderLineModel> result = getFlexibleSearchService().<JnjGTIntOrderLineModel> search(fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderLineModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public List<JnjGTIntOrderSchLineModel> getJnjGTIntOrderSchLineModel(final String orderNumber, final String sourceSystemId,
			final String orderLineNumber)
	{
		final Map queryParams = new HashMap();
		queryParams.put("sapOrderNumber", orderNumber);
		queryParams.put("sapOrderLineNumber", orderLineNumber);
		queryParams.put("sourceSystemId", sourceSystemId);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_INT_SCH_LINES);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderSchLineModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Order Sch. line query " + fQuery);
		}

		final List<JnjGTIntOrderSchLineModel> result = getFlexibleSearchService().<JnjGTIntOrderSchLineModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderSchLineModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public String getCurrencyFromOrdLinePriceLocal(final String sapOrderNumber, final String sourceSysid)
	{
		final Map queryParams = new HashMap();
		queryParams.put(JnjGTIntOrdLinePriceLocalModel.SAPORDERNUMBER, sapOrderNumber);
		queryParams.put(JnjGTIntOrdLinePriceLocalModel.SOURCESYSTEMID, sourceSysid);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_CURRENCY_FROM_LINE_PRICE_LOCL);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderSchLineModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query: " + fQuery);
		}

		final List<JnjGTIntOrdLinePriceLocalModel> result = getFlexibleSearchService().<JnjGTIntOrdLinePriceLocalModel> search(
				fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderSchLineModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return (result != null && result.size() > 0) ? result.get(0).getCurrencyForCv() : null;
	}
}
