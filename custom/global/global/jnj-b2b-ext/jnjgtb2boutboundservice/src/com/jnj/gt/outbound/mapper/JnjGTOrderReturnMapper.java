/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.core.model.order.CartModel;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.core.data.JnjGTOrderReturnResponseData;


/**
 * @author somya.sinha
 * 
 */
public interface JnjGTOrderReturnMapper
{
	public JnjGTOrderReturnResponseData mapOrderReturnRequestResponse(final CartModel cartModel) throws IntegrationException,
			SystemException, BusinessException;
}
