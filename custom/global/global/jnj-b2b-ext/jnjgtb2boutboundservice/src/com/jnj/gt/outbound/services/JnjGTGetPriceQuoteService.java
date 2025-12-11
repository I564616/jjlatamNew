/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayOutput;


/**
 * The jnjGTSimulateOrderService interface contains the declaration of all the methods of the
 * jnjGTSimulateOrderServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTGetPriceQuoteService
{

	/**
	 * Gets the price quote in sap.
	 * 
	 * @param testPricingFromGatewayInput
	 *           the test pricing from gateway input
	 * @return the price quote in sap
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public TestPricingFromGatewayOutput getPriceQuoteInSAP(final TestPricingFromGatewayInput testPricingFromGatewayInput)
			throws IntegrationException;
}
