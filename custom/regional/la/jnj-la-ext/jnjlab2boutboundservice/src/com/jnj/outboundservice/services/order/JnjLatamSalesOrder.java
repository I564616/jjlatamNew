package com.jnj.outboundservice.services.order;

import java.util.concurrent.TimeoutException;

import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;


/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP Hybris ("Confidential Information"). You shall
 * not disclose such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with SAP Hybris.
 *
 * package com.jnj.outboundservice.services.order;
 *
 * import java.util.concurrent.TimeoutException;
 *
 * import com.jnj.exceptions.IntegrationException; import com.jnj.outboundservice.order.SalesOrderCreationRequest;
 * import com.jnj.outboundservice.order.SalesOrderCreationResponse; import
 * com.jnj.outboundservice.order.SalesOrderPricingRequest; import
 * com.jnj.outboundservice.order.SalesOrderPricingResponse; import
 * com.jnj.outboundservice.order.SalesOrderSimulationRequest; import
 * com.jnj.outboundservice.order.SalesOrderSimulationResponse;
 *
 *
 *//**
	*
	*/

public interface JnjLatamSalesOrder
{
	/**
	 * The salesOrderSimulationWrapper method invoke the salesOrderSimulationWrapper method of the SAP Submit Order on
	 * the basis of the request parameters passed in request.
	 *
	 * @param salesOrderPricingRequest
	 *           the sales order pricing request
	 * @return salesOrderPricingResponse
	 * @throws IntegrationException
	 *            the integration exception
	 */

	public SalesOrderPricingResponse salesOrderPricingWrapper(final SalesOrderPricingRequest salesOrderPricingRequest)
			throws IntegrationException, TimeoutException;

	/**
	 * The salesOrderCreationWrapper method invoke the salesOrderCreationWrapper method of the SAP Submit Order on the
	 * basis of the request parameters passed in request.
	 *
	 * @param salesOrderCreationRequest
	 *           the sales order creation request
	 * @return salesOrderCreationResponse
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public SalesOrderCreationResponse salesOrderCreationWrapper(SalesOrderCreationRequest salesOrderCreationRequest)
			throws IntegrationException;

	/**
	 * The salesOrderSimulationWrapper method invoke the salesOrderSimulationWrapper method of the SAP Submit Order on
	 * the basis of the request parameters passed in request.
	 *
	 * @param salesOrderSimulationRequest
	 *           the sales order simulation request
	 * @return salesOrderSimulationResponse
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public SalesOrderSimulationResponse salesOrderSimulationWrapper(SalesOrderSimulationRequest salesOrderSimulationRequest)
			throws IntegrationException, TimeoutException;
}
