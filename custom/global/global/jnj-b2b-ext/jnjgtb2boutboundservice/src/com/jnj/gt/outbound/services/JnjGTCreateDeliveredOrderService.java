/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersInput;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersOutput;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNACreateDeliveredOrderService interface contains the declaration of all the methods of the
 * JnjNACreateDeliveredOrderServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTCreateDeliveredOrderService
{

	/**
	 * The createDeliveredOrders method accepts the OrderCreationInSAPOutput object as its input parameters and passes
	 * the same to SAP service to create an order in SAP and receive the OrderCreationInSAPOutput object.
	 * 
	 * @param createDeliveredOrdersInput
	 *           the order creation in sap input
	 * @param sapWsData YTODO
	 * @return the order creation in sap output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public CreateDeliveredOrdersOutput createDeliveredOrders(final CreateDeliveredOrdersInput createDeliveredOrdersInput, JnjGTSapWsData sapWsData)
			throws IntegrationException;
}
