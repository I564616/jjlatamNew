/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.core.service.vtex.order.impl;

import com.jnj.core.dao.vtex.order.JnjLatamVtexOrderDao;
import com.jnj.core.service.vtex.order.JnjLatamVtexOrderService;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfAnyResult;

/**
 * The implementation class DefaultJnjLatamVtexOrderService.
 *
 */
public class DefaultJnjLatamVtexOrderService implements JnjLatamVtexOrderService
{
	private static final int BATCH_SIZE = 1000;
	private static final String ORDERNUMBERSET_MUST_NOT_BE_EMPTY_OR_NULL = "orderNumberSet must not be empty or null";
	private JnjLatamVtexOrderDao jnjLatamVtexOrderDao;
	private ModelService modelService;

	/**
	 * Gets the cart for vtex reference number.
	 *
	 * @param vtexReferenceNumber the vtex reference number
	 * @return the cart for vtex reference number
	 */
	@Override
	public Optional<AbstractOrderModel> getCartForVtexReferenceNumber(final String vtexReferenceNumber)
	{
		return jnjLatamVtexOrderDao.getCartForVtexReferenceNumber(vtexReferenceNumber);
	}

	/**
	 * Removes the vtex cart.
	 *
	 * @param vtexCart the vtex cart
	 */
	@Override
	public void removeVtexCart(final AbstractOrderModel vtexCart)
	{
		modelService.remove(vtexCart);
	}

	/**
	 * Gets the Orders List
	 * @param orderNumbersSet
	 * @return List of Orders
	 */
    @Override
	public List<OrderModel> getOrdersList(final Set<String> orderNumbersSet)
	{
		validateIfAnyResult(orderNumbersSet,ORDERNUMBERSET_MUST_NOT_BE_EMPTY_OR_NULL);
		if(orderNumbersSet.size() > BATCH_SIZE)
		{
			return processInBatch(orderNumbersSet);
		}
		else
		{
			return jnjLatamVtexOrderDao.getOrdersList(new ArrayList<>(orderNumbersSet));
		}
	}

	private List<OrderModel> processInBatch(final Set<String> orderNumbersSet)
	{
		final List<OrderModel> globalOrdersList = new ArrayList<>();
		List<String> orderNumbersList = new ArrayList<>(orderNumbersSet);
		for(int index = 0; index < orderNumbersList.size(); index += BATCH_SIZE)
		{
			int endIndex = Math.min(index + BATCH_SIZE, orderNumbersList.size());
			List<String> batchSubList = orderNumbersList.subList(index,endIndex);
			globalOrdersList.addAll(jnjLatamVtexOrderDao.getOrdersList(batchSubList));
		}
		return globalOrdersList;
	}

	/**
	 * Get the Orders With Cart Reference Number
	 * @param vtexReferenceNumber
	 * @return the vtex reference number
	 */
	@Override
	public Optional<OrderModel> getOrdersWithCartRefNum(final String vtexReferenceNumber)
	{
		return jnjLatamVtexOrderDao.getOrdersWithCartRefNum(vtexReferenceNumber);
	}

	protected JnjLatamVtexOrderDao getJnjLatamVtexOrderDao()
	{
		return jnjLatamVtexOrderDao;
	}
	public void setJnjLatamVtexOrderDao(final JnjLatamVtexOrderDao jnjLatamVtexOrderDao)
	{
		this.jnjLatamVtexOrderDao = jnjLatamVtexOrderDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
