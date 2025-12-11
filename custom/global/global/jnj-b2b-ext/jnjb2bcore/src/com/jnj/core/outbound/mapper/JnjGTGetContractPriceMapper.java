/**
 * 
 */
package com.jnj.core.outbound.mapper;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTGetContractPriceResponseData;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * The JnjGTGetContractPriceMapper interface contains the declaration of all the method of the
 * JnjGTGetContractPriceMapperImpl class
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTGetContractPriceMapper
{
	/**
	 * Map get contract price mapper request response.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @return the jnj na get contract price response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public JnjGTGetContractPriceResponseData mapGetContractPriceMapperRequestResponse(final CartModel cartModel,
			final CartEntryModel cartEntryModel)

	throws IntegrationException, SystemException;

	public JnjGTGetContractPriceResponseData mapGetContractPrice(final JnjGTVariantProductModel JnjGTVariantProductModel,
			final Long quantity, final String orderType) throws IntegrationException, SystemException;

}
