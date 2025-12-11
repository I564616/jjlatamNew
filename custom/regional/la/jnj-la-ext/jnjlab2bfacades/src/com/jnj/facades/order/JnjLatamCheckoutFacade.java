/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;

import java.text.ParseException;
import java.util.List;

import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;


/**
 *
 */
public interface JnjLatamCheckoutFacade extends JnjCheckoutFacade
{


	/**
	 * Place order in hybris.
	 * 
	 * @param cartCleanUpRequired
	 *           the cart clean up required
	 * @return OrderCode the string
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public String placeOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException;

	/**
	 * Place Quote order.
	 * 
	 * @return boolean representing the order Model created or not
	 * @throws InvalidCartException
	 *            is thrown by underlying {@link CartValidator}
	 */
	public String placeQuoteOrder() throws InvalidCartException;

	/**
	 * Place Return order.
	 * 
	 * @return true, if successful
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public String placeReturnOrder() throws InvalidCartException;

	/**
	 * Creates the order in sap.
	 * 
	 * @param orderCode
	 *           the order code
	 * @param sapWsData
	 *           YTODO
	 * @return true, if successful
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public SalesOrderCreationResponse createOrderInSAP(final String orderCode, JnjGTSapWsData sapWsData) throws SystemException,
			IntegrationException, ParseException, BusinessException;


	public List<String> placeSplitOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException;
	
	public void setEmpenhoFilesFullPath(final List<String> empenhoFilesFullPath);
	
	public void createERPOrder(final List<OrderModel> orderList);

}
