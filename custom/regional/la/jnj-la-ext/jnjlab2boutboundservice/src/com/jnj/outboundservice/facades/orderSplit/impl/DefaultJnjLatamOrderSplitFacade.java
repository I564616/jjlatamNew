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
package com.jnj.outboundservice.facades.orderSplit.impl;

import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;
import com.jnj.outboundservice.facades.orderSplit.JnjLatamOrderSplitFacade;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class DefaultJnjLatamOrderSplitFacade<S, L> implements JnjLatamOrderSplitFacade<S, L>
{

	@Autowired
	protected Map<String, JnjLatamAbstractOrderSplitService> jnjLatamOrderSplitService;

	@Autowired
	protected SessionService sessionService;

	private static final String SPLIT_SERVICE_SUFIX = "OrderSplitService";
	private static final String DEFAULT_SPLIT_SERVICE_PREFIX = "defaultLatam";

	@Override
	public Map<S, List<L>> splitOrder(final AbstractOrderModel abstOrderModel, final String countryIsoCode)
	{

		final String splitServiceName = (countryIsoCode != null) ? countryIsoCode.toLowerCase() + SPLIT_SERVICE_SUFIX
				: DEFAULT_SPLIT_SERVICE_PREFIX + SPLIT_SERVICE_SUFIX;
		if (jnjLatamOrderSplitService.containsKey(splitServiceName))
		{
			return jnjLatamOrderSplitService.get(splitServiceName).splitOrder(abstOrderModel);
		}
		return jnjLatamOrderSplitService.get(DEFAULT_SPLIT_SERVICE_PREFIX + SPLIT_SERVICE_SUFIX).splitOrder(abstOrderModel);
	}
}


