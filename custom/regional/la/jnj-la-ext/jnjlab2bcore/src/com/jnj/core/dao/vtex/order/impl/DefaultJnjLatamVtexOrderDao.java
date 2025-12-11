/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */

package com.jnj.core.dao.vtex.order.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.SearchResult;

import com.jnj.core.dao.vtex.order.JnjLatamVtexOrderDao;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.log4j.Logger;

/**
 * The implementation class JnjLatamVtexOrderDao.
 *
 */
public class DefaultJnjLatamVtexOrderDao implements JnjLatamVtexOrderDao
{
	private static final String GET_ABSTRACT_ORDER_FOR_VTEX_REFERENCE_NUMBER="Select {o.pk} from {AbstractOrder as o} where {o.externalOrderRefNumber}=?externalOrderRefNumber";
	private static final String GET_ORDER_LIST="SELECT {o.pk} FROM {Order as o} WHERE {o.sapOrderNumber} IN (?sapOrderNumbersList)";
	private static final String GET_ORDER_FOR_VTEX_REFERENCE_NUMBER= "SELECT {o.pk} FROM {Order as o} WHERE {o.externalOrderRefNumber}=?externalOrderRefNumber";
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjLatamVtexOrderDao.class);
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Gets the cart for vtex reference number.
	 *
	 * @param vtexReferenceNumber the vtex reference number
	 * @return the cart for vtex reference number
	 */
	@Override
	public Optional<AbstractOrderModel> getCartForVtexReferenceNumber(final String vtexReferenceNumber)
	{
		try {
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_ABSTRACT_ORDER_FOR_VTEX_REFERENCE_NUMBER);
			final Map<String, String> params = new HashMap<>();
			params.put("externalOrderRefNumber", vtexReferenceNumber);
			fQuery.addQueryParameters(params);
			return Optional.of(flexibleSearchService.<AbstractOrderModel>searchUnique(fQuery));
		}
		catch (final ModelNotFoundException e)
		{
			LOGGER.error(e.getMessage(),e);
			return Optional.empty();
		}
	}

	/**
	 * Gets the OrdersList
	 * @param orderNumbersList
	 * @return the order status
	 */
	@Override
	public List<OrderModel> getOrdersList(final List<String> orderNumbersList)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ORDER_LIST);
		final Map<String, Object> params = new HashMap<>();
		params.put("sapOrderNumbersList",orderNumbersList);
		query.addQueryParameters(params);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(query);
		return searchResult.getResult();
	}

	/**
	 *  Gets the OrdersWithCartRefNumber
	 * @param vtexReferenceNumber
	 * @return the vtex reference number
	 */
	@Override
	public Optional<OrderModel> getOrdersWithCartRefNum(final String vtexReferenceNumber)
	{
		try{
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_ORDER_FOR_VTEX_REFERENCE_NUMBER);
			final Map<String, String> params = new HashMap<>();
			params.put("externalOrderRefNumber", vtexReferenceNumber);
			fQuery.addQueryParameters(params);
			return Optional.of(flexibleSearchService.<OrderModel>searchUnique(fQuery));
		}
		catch (final ModelNotFoundException e)
		{
			LOGGER.error(e.getMessage(),e);
			return Optional.empty();
		}
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
