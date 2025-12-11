/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayOutput;
import com.jnj.core.data.ws.JnjGTSapWsData;



/**
 * The jnjGTSimulateOrderService interface contains the declaration of all the methods of the
 * jnjGTSimulateOrderServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTSimulateOrderService
{

	/**
	 * The orderSimulate method accepts the TestPricingFromGatewayInput object as its input parameters and passes the
	 * same to SAP service to validate it and receive the TestPricingFromGatewayOutput object.
	 * 
	 * @param testPricingFromGatewayInput
	 *           the test pricing from gateway input
	 * @param wsData YTODO
	 * @return the test pricing from gateway output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public TestPricingFromGatewayOutput orderSimulate(final TestPricingFromGatewayInput testPricingFromGatewayInput, JnjGTSapWsData wsData)
			throws IntegrationException;
}
