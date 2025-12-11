/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.CartModel;

import java.text.ParseException;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTGetPriceQuoteResponseData;


/**
 * The JnjNAGetPriceQuoteMapper interface contains the declaration of all the method of the JnjNAGetPriceQuoteMapperImpl
 * class.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTGetPriceQuoteMapper
{

	/**
	 * Map get price quote request response.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @return the jnj na get price quote mapper
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTGetPriceQuoteResponseData mapGetPriceQuoteRequestResponse(final CartModel cartModel) throws IntegrationException,
			SystemException, ParseException, BusinessException;
}
