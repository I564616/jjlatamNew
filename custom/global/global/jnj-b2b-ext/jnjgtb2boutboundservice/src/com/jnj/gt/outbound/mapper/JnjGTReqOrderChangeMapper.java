/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTOrderChangeResponseData;



/**
 * The JnjNAReqOrderChangeMapper interface contains the declaration of all the method of the
 * JnjNAReqOrderChangeMapperImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTReqOrderChangeMapper
{

	/**
	 * Map change order request response by getting the value from the Order Model and set in the request object of Order
	 * Change. After getting response from the SAP set the value in JnjNAChangeOrderResponseData object.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param sapOrderNumber
	 * @param isCallAfterCreateOrder
	 * @return the jnj na order change response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTOrderChangeResponseData mapChangeOrderRequestResponse(final OrderModel orderModel, String sapOrderNumber,
			boolean isCallAfterCreateOrder) throws IntegrationException, SystemException;
}
