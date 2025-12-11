/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTCreateDelOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNACreateDeliveredOrderMapper interface contains the declaration of all the method of the
 * JnjNACreateDeliveredOrderMapperImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTCreateDeliveredOrderMapper
{

	/**
	 * Map create order request and response by getting value from the OrderModel and set it in sap object.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param sapWsData YTODO
	 * @return the jnj na create delivered order response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTCreateDelOrderResponseData mapCreateDelOrderRequestResponse(final OrderModel orderModel, JnjGTSapWsData sapWsData)
			throws IntegrationException, SystemException, BusinessException;
}
