/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dao.order.impl;

import com.jnj.core.dao.synchronizeOrders.impl.DefaultJnjSAPOrdersDao;
import com.jnj.la.core.dao.order.JnjLaSAPOrdersDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJnjLaSAPOrdersDao extends DefaultJnjSAPOrdersDao implements JnjLaSAPOrdersDao
{

    @Override
	public OrderModel getExistingOrderByHybrisOrderNumber(final String orderNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map<String, String> queryParams = new HashMap<>();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(OrderModel._TYPECODE).append("} WHERE {")
				.append(OrderModel.CODE).append("} = ?orderNumber");
		queryParams.put("orderNumber", orderNumber);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(flexibleSearchQuery).getResult();
		return (!result.isEmpty() ? result.get(0) : null);
	}

}
