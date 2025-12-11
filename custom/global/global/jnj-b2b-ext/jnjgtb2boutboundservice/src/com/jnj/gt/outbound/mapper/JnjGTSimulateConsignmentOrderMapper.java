/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.CartModel;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTSimulateConsignmentResponseData;
import com.jnj.core.data.JnjGTSimulateDelOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNASimulateDeliveredOrderMapper interface contains the declaration of all the method of the
 * JnjNASimulateDeliveredOrderMapperImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTSimulateConsignmentOrderMapper
{

	/**
	 * Map simulate order request and response by getting value from the CartModel and set it in sap object.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param wsData YTODO
	 * @return the jnj na simulate order response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTSimulateConsignmentResponseData mapConsignmentSimulateOrderRequestResponse(final CartModel cartModel, JnjGTSapWsData wsData)
			throws IntegrationException, SystemException, BusinessException;
}
