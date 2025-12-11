package com.jnj.facades.order;

/**
 * 
 */
import java.util.concurrent.TimeoutException;

import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;

import de.hybris.platform.core.model.order.CartModel;

/**
 * @author nsinha7
 *
 */
public interface JnjGTSalesOrderMapper {
	
	/**
	 * This method populates the Cart Model into SalesOrderSimulationRequest object by getting value from the Cart Model
	 * and setting into List of Simulation Object. Then it will call the JnjSalesOrderImpl class to validate the request
	 * object and return the boolean flag.
	 *
	 * @param cartModel
	 *           the cart model
	 * @param isCallfromGetPrice 
	 * @return boolean flag
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException 
	 */
	//	Change the return type of the Method for CP-002
	public JnjGTOutboundStatusData mapSalesOrderSimulationWrapper(final CartModel cartModel, boolean isCallfromGetPrice) throws IntegrationException,
			SystemException, TimeoutException, BusinessException;

}
