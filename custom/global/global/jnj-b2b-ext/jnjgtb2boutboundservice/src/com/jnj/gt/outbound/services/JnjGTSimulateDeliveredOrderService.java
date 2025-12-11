/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_simulatedelorder_v1.simulatedeliveredorderswebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_simulatedelorder_v1.simulatedeliveredorderswebservice.TestPricingFromGatewayOutput;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNASimulateDeliveredOrderService interface contains the declaration of all the methods of the
 * JnjNASimulateDeliveredOrderServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTSimulateDeliveredOrderService
{

	/**
	 * The simulateDeliveredOrders method accepts the TestPricingFromGatewayInput object as its input parameters and
	 * passes the same to SAP service to validate the delivered order and receive the TestPricingFromGatewayOutput
	 * object.
	 * 
	 * @param testPricingFromGatewayInput
	 *           the test pricing from gateway input
	 * @param wsData YTODO
	 * @return the test pricing from gateway output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public TestPricingFromGatewayOutput simulateDeliveredOrders(final TestPricingFromGatewayInput testPricingFromGatewayInput, JnjGTSapWsData wsData)
			throws IntegrationException;
}
