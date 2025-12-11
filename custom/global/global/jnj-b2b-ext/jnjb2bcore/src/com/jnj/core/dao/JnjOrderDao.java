/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Sumit - class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 * 
 * 
 */
public interface JnjOrderDao
{
	/**
	 * The getOrderModel method retrieves the list of Order Model by querying the hybris data with order status and
	 * sapOrderNumber.
	 * 
	 * @return List of OrderModel
	 */

	public List<OrderModel> getOrderModel();

	/**
	 * This method is used to get all order details from order model
	 * 
	 * @return OrderModel
	 */
	public List<OrderModel> geAlltOrderDetails(final String purchaseOrderNumber, final String orderNumber);

	/**
	 * This method is used to get the order details
	 * 
	 * @param orderHistoryDTO
	 * @param statuses
	 * @param pageableData
	 * @return SearchPageData<OrderModel>
	 */
	public SearchPageData<OrderModel> getOrderDetails(OrderStatus[] statuses, PageableData pageableData,
			OrderHistoryDTO orderHistoryDTO);


	/**
	 * This method is used to get the oder code on the basis of saporder number
	 * 
	 * @param orderId
	 * @return List<String>
	 */
	public List<String> getProductCodes(String orderId);

	/**
	 * This method is used to retrieve the Order Status from the Jnj Order Status table on the basis of incoming field.
	 * 
	 * @param overAllStatus
	 *           the over all status
	 * @param rejectionStatus
	 *           the rejection status
	 * @param creditStatus
	 *           the credit status
	 * @param deliveryStatus
	 *           the delivery status
	 * @param invoiceStatus
	 *           the invoice status
	 * @return the order status
	 */
	public List<JnjOrdStsMappingModel> getOrderStatus(final String overAllStatus, final String rejectionStatus,
			final String creditStatus, final String deliveryStatus, final String invoiceStatus);

	/**
	 * This method is used to retrieve the Order Entry Status from the Jnj Order Enty Status table on the basis of
	 * incoming field.
	 * 
	 * @param overAllStatus
	 *           the over all status
	 * @param rejectionStatus
	 *           the rejection status
	 * @param deliveryStatus
	 *           the delivery status
	 * @param invoiceStatus
	 *           the invoice status
	 * @return the order entry status
	 */
	public List<JnjOrdEntStsMappingModel> getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus);

	public OrderModel geLatestOrderDetails(String unit) throws ModelNotFoundException, BusinessException;
}
