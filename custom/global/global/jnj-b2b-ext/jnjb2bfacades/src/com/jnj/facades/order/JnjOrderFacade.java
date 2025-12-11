/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.jnj.facades.order;

import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjOrderHistoryData;
import com.jnj.facades.data.JnjValidateOrderData;



/**
 * The JnjOrderFacade interface is used to invoke the respective method of the JnjSalesOrderMapper and JnjSalesOrder
 * class and pass the request object to corresponding method.
 */
public interface JnjOrderFacade extends B2BOrderFacade
{

	/**
	 * This method will be invoke when the user click on the validation button and it will call the JnJSalesOrderMapper
	 * and JnJSalesOrder class.
	 * 
	 * @param cartModel
	 * @return boolean
	 */
	JnjValidateOrderData invokeSalesOrderSimulateWrapper(final CartModel cartModel) throws IntegrationException, SystemException;

	/**
	 * This method will be invoke all the order details at the time of page load..
	 * 
	 * 
	 * @return JnjOrderData
	 */
	List<JnjOrderHistoryData> getAllOrder(String purchaseOrderNumber, String orderNumber);



	/**
	 * This method will be invoke all the order details at the time of page load..
	 * 
	 * 
	 * @return JnjOrderData
	 */
	List<JnjOrderData> getOrderData(String purchaseOrderNumber, String orderNumber);

	/**
	 * This method is used to get product codes for given order id, this method is used to load more product on order
	 * details page
	 * 
	 * @param orderId
	 * @return List<String>
	 */
	List<String> getProcuctCodes(String orderId);

	/**
	 * This method is used to get the status information on order history page
	 * 
	 * @param orders
	 *           List<JnjOrderHistoryData>
	 * 
	 * @return Set of status
	 */
	Set<String> getAllStatus(final List<JnjOrderHistoryData> orders);

	/**
	 * The createOrderFromInterface method passes the inputStream to JnjOrderServiceImpl class.
	 * 
	 * @param submitOrderFileArray
	 *           the submit order file array
	 * @return true, if successful
	 */
	public Map<String, List<String>> createOrderFromInterface(CommonsMultipartFile[] submitOrderFileArray);


	public OrderData getLatestOrder(final int numberOfEntries);

	/**
	 * @return
	 */
	OrderHistoryDTO getDefaultData();

}
