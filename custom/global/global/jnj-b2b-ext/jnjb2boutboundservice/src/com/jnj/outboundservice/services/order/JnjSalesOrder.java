/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.outboundservice.services.order;

import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjSalesOrder
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
	public SalesOrderPricingResponse salesOrderPricingWrapper(SalesOrderPricingRequest salesOrderPricingRequest)
			throws IntegrationException;

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
			throws IntegrationException;

}
