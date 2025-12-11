/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.OrderModel;

import java.text.ParseException;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTCreateOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The jnjGTCreateOrderMapper interface contains the declaration of all the method of the jnjGTCreateOrderMapperImpl
 * class.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTCreateOrderMapper
{

	/**
	 * Map create order request and response by getting value from the OrderModel and set it in sap object.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param sapWsData YTODO
	 * @return the jnj na submit order response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public JnjGTCreateOrderResponseData mapCreateOrderRequestResponse(final OrderModel orderModel, JnjGTSapWsData sapWsData) throws IntegrationException,
			SystemException, ParseException, BusinessException;
}
