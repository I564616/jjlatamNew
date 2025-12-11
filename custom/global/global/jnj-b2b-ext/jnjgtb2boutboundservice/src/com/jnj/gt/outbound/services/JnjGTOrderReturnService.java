/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPInput;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPOutput;


/**
 * The JnjNAOrderReturnService interface contains the declaration of all the methods of the JnjNAOrderReturnServiceImpl
 * class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTOrderReturnService
{
	/**
	 * Order Return methods calls the sap method by passing the input object and gets the sap response as per the input
	 * data.
	 * 
	 * @param orderReturnInSAPInput
	 *           the order return in sap input
	 * @return the order change in sap output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public OrderReturnInSAPOutput orderReturnInSAP(final OrderReturnInSAPInput orderReturnInSAPInput) throws IntegrationException;
}
