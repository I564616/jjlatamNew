/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.core.service.vtex.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The interface JnjLatamVtexOrderService.
 *
 */
public interface JnjLatamVtexOrderService
{
	public Optional<AbstractOrderModel> getCartForVtexReferenceNumber(final String vtexReferenceNumber);
	public void removeVtexCart(final AbstractOrderModel vtexCart);

	public List<OrderModel> getOrdersList(final Set<String> orderNumbersSet);
	public Optional<OrderModel> getOrdersWithCartRefNum(final String vtexReferenceNumber);
}
