/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.facades.vtex.order;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import com.jnj.facades.data.JnjLaCartData;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.facades.data.JnjLatamOrderRequestData;
import com.jnj.facades.data.JnjLatamOrdersListData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.order.InvalidCartException;

/**
 * interface JnjLatamVtexOrderFacade.
 *
 */
public interface JnjLatamVtexOrderFacade
{

	/**
	 * Creates the vtex cart.
	 *
	 * @param orderRequestData the order request data
	 */
	public void createVtexCart(final JnjLatamOrderRequestData orderRequestData) throws BusinessException;
	/**
	 * sets the vtex cart.
	 *
	 * @param orderRequestData the order request data
	 */
	public void setVtexCart(final JnjLatamOrderRequestData orderRequestData) throws BusinessException;
	/**
	 * validates cart
	 * @return latamCartData
	 */
	public JnjLaCartData validateCart() throws BusinessException, SystemException, IntegrationException, TimeoutException;

	/**
	 * Place order in hybris.
	 *
	 * @return OrderCode the string
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public String placeOrderInHybris() throws InvalidCartException;
	/**
	 * creates order in sap
	 * @param orderCode
	 * @return SalesOrderCreationResponse
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws IntegrationException
	 * @throws ParseException
	 */
	public SalesOrderCreationResponse createOrderInSAP(final String orderCode) throws BusinessException, SystemException, IntegrationException, ParseException;
	/**
	 * Gets order place
	 * @param orderCode
	 * @return orderData
	 */
	public OrderData getOrderPlaced(final String orderCode);

	/**
	 * Get Order Histories Data
	 * @param ordersListData
	 * @return ordersData
	 */
	public OrderHistoriesData getOrderHistoriesData(final JnjLatamOrdersListData ordersListData);
	
	/**
	 * Gets the b 2 b unit for account id.
	 *
	 * @param accountId the account id
	 * @return the b 2 b unit for account id
	 */
	public JnJB2BUnitModel getB2bUnitForAccountId(final String accountId);
	
	/**
	 * Update cart entry sales org.
	 */
	public void updateCartEntrySalesOrg();

}
