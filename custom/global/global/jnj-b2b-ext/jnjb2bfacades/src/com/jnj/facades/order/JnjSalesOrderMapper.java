/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;


/**
 * The JnJSalesOrderMapper interface is used for mapping the CartModel object into respective sales order request for
 * creation, simulation and pricing.Then invoke the JnjSalesOrderImpl class to perform the respective operation to
 * retrieve the response from the SAP service.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjSalesOrderMapper
{

	/**
	 * This method populates the Cart Model into SalesOrderCreationRequest object by getting value from the Order Model
	 * and setting into Creation Object. Then it will call the JnjSalesOrderImpl class to create the Order in SAP and
	 * return the SalesOrderCreationResponse object to JnjOrderFacadeImpl.
	 * 
	 * @param orderModel
	 *           the order model
	 * @return salesOrderCreationResponse
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public SalesOrderCreationResponse mapSalesOrderCreationWrapper(final OrderModel orderModel) throws IntegrationException,
			SystemException;

	/**
	 * This method populates the Cart Model into SalesOrderSimulationRequest object by getting value from the Cart Model
	 * and setting into List of Simulation Object. Then it will call the JnjSalesOrderImpl class to validate the request
	 * object and return the boolean flag.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @return boolean flag
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	//	Change the return type of the Method for CP-002
	public JnjValidateOrderData mapSalesOrderSimulationWrapper(final CartModel cartModel) throws IntegrationException,
			SystemException;

	/**
	 * This method populates the Cart Model into SalesOrderPricingRequest object by getting value from the Cart Model and
	 * setting into Pricing Object. Then it will call JnjSalesOrderImpl class to retrieve the get Price and send the
	 * salesOrderPricingResponse object to JnjOrderFacadeImpl
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param orderEntryNumber
	 *           the order entry number
	 * @return salesOrderPricingResponse
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public SalesOrderPricingResponse mapSalesOrderPricingWrapper(final CartModel cartModel, final String orderEntryNumber)
			throws IntegrationException;
}
