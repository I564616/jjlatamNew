package com.jnj.facades.order;


import java.text.ParseException;

import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author nsinha7
 */
public interface JnjGTCreateSalesOrderMapper {
	/**
	 * 
	 * @param orderModel
	 * @param sapWsData
	 * @return
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 * @throws ParseException 
	 */
	public JnjGTOutboundStatusData mapSalesOrderCreationWrapper(OrderModel orderModel, JnjGTSapWsData sapWsData)throws IntegrationException,
	SystemException, BusinessException,ParseException, ParseException;
}
