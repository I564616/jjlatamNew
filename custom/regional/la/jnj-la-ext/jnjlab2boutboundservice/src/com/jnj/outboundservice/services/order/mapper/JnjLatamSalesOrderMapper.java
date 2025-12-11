package com.jnj.outboundservice.services.order.mapper;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;


/*
 * package com.jnj.outboundservice.services.order.mapper;
 *
 * import de.hybris.platform.core.model.order.CartModel; import de.hybris.platform.core.model.order.OrderModel;
 *
 * import java.text.ParseException; import java.util.concurrent.TimeoutException;
 *
 * import com.jnj.exceptions.IntegrationException; import com.jnj.exceptions.SystemException; import
 * com.jnj.facades.data.JnjValidateOrderData; import com.jnj.outboundservice.order.SalesOrderCreationResponse; import
 * com.jnj.outboundservice.order.SalesOrderPricingResponse;
 *
 *
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP Hybris ("Confidential Information"). You shall
 * not disclose such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with SAP Hybris.
 *
 *
 *
 *
 *//**
	*
	*/
public interface JnjLatamSalesOrderMapper
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
			SystemException, ParseException;

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




	public JnjValidateOrderData mapSalesOrderSimulationWrapper(final CartModel cartModel) throws IntegrationException,
			SystemException, TimeoutException;

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
	/*
	 * public SalesOrderPricingResponse mapSalesOrderPricingWrapper(final CartModel cartModel, final String
	 * orderEntryNumber) throws IntegrationException, TimeoutException;
	 *
	 *
	 * }
	 */

	public SalesOrderPricingResponse mapSalesOrderPricingWrapper(final CartModel cartModel, final String orderEntryNumber)
			throws IntegrationException, TimeoutException;


	/**
	 * This method populates the Cart Model into SalesOrderSimulationRequest object by getting value from the Cart Model
	 * and setting into List of Simulation Object. Then it will call the JnjSalesOrderImpl class to validate the request
	 * object and return the boolean flag.
	 *
	 * @param cartModel
	 *           the cart model
	 * @param isCallfromGetPrice
	 * @return boolean flag
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */

	public JnjGTOutboundStatusData mapSalesOrderSimulationWrapper(CartModel cartModel, boolean isCallFromGetPrice)
			throws IntegrationException, SystemException, TimeoutException, BusinessException;

}