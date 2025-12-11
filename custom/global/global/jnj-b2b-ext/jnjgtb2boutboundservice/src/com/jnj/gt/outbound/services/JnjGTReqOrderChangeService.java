/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPOutput;


/**
 * The JnjNAReqOrderChangeService interface contains the declaration of all the methods of the
 * JnjNAReqOrderChangeServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTReqOrderChangeService
{

	/**
	 * Request order change methods calls the sap method by passing the input object and gets the sap response
	 * corresponding to surgeon update data.
	 * 
	 * @param orderChangeInSAPInput
	 *           the order change in sap input
	 * @return the order change in sap output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public OrderChangeInSAPOutput requestOrderChange(final OrderChangeInSAPInput orderChangeInSAPInput)
			throws IntegrationException;
}
