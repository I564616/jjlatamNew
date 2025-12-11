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
package com.jnj.la.core.services.ordersplit;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface JnjLatamOrderSplitService<S, L>
{

	final String SPLIT_SERVICE = "Order Split Service";

	public Map<S, List<L>> splitOrder(AbstractOrderModel abstOrderModel);

}
