/**
 *
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.CartModel;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTSimulateOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The jnjGTSimulateOrderMapper interface contains the declaration of all the method of the jnjGTSimulateOrderMapperImpl
 * class.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public interface JnjGTSimulateOrderMapper
{

	/**
	 * Map simulate order request and response by getting value from the CartModel and set it in sap object.
	 *
	 * @param cartModel
	 *           the cart model
	 * @param isCallFromGetPriceQuote
	 *           the is call from get price quote
	 * @param wsData
	 *           Params for Sap WS
	 * @return the jnj na simulate order response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTSimulateOrderResponseData mapSimulateOrderRequestResponse(final CartModel cartModel,
			final boolean isCallFromGetPriceQuote, JnjGTSapWsData wsData) throws IntegrationException, SystemException, BusinessException;
}
