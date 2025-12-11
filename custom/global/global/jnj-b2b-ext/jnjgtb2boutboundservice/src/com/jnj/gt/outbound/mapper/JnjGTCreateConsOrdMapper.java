/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTCreateConsOrdResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNACreateConsOrdMapper interface contains the declaration of all the method of the JnjNACreateConsOrdMapperImpl
 * class
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCreateConsOrdMapper
{


	/**
	 * Map create cons ord request response.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param sapWsData YTODO
	 * @return the jnj na create cons ord response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTCreateConsOrdResponseData mapCreateConsOrdRequestResponse(final OrderModel orderModel, JnjGTSapWsData sapWsData)
			throws IntegrationException, SystemException;


	/**
	 * Map simulate cons ord request response.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param wsData YTODO
	 * @return the jnj na create cons ord response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTCreateConsOrdResponseData mapSimulateConsOrdRequestResponse(final CartModel cartModel, JnjGTSapWsData wsData)
			throws IntegrationException, SystemException;
}
