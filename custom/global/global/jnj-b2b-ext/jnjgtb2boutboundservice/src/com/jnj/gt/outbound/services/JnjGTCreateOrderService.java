/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPOutput;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The jnjGTCreateOrderService interface contains the declaration of all the methods of the jnjGTCreateOrderServiceImpl
 * class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTCreateOrderService
{

	/**
	 * The orderCreationInSAP method accepts the OrderCreationInSAPOutput object as its input parameters and passes the
	 * same to SAP service to create an order in SAP and receive the OrderCreationInSAPOutput object.
	 * 
	 * @param orderCreationInSAPInput
	 *           the order creation in sap input
	 * @param sapWsData YTODO
	 * @return the order creation in sap output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public OrderCreationInSAPOutput orderCreationInSAP(final OrderCreationInSAPInput orderCreationInSAPInput, JnjGTSapWsData sapWsData)
			throws IntegrationException;
}
