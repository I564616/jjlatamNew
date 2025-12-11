/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;

import java.io.File;
import java.util.List;

import com.jnj.core.dto.JnjCheckoutDTO;
import com.jnj.core.dto.OrderHistoryDTO;


/**
 * 
 * The JnjSubmitOrder interface invokes the save method of ModelService to save the Order Model in hybris data.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjOrderService
{

	/**
	 * The getSubmitOrderDataList method retrieves the list of Order Model for which SAP order number doesn't exist in
	 * the hybris database.
	 * 
	 * @return List of OrderModel
	 */
	public List<OrderModel> getSubmitOrderDataList();


	/**
	 * This method is used to get all order details from OrderModel
	 * 
	 * @return OrderModel
	 */
	public List<OrderModel> getAllOrderDetails(final String purchaseOrderNumber, final String orderNumber);

	/**
	 * Update sap fileds.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param jnjCheckoutDTO
	 *           the jnj checkout dto
	 * @return true, if successful
	 */
	public boolean updateSAPFileds(OrderModel orderModel, final JnjCheckoutDTO jnjCheckoutDTO);


	/**
	 * Gets the order list.
	 * 
	 * @param statuses
	 *           the statuses
	 * @param pageableData
	 *           the pageable data
	 * @param orderHistoryDTO
	 *           the order history dto
	 * @return the order list
	 */
	public SearchPageData<OrderModel> getOrderList(OrderStatus[] statuses, PageableData pageableData,
			OrderHistoryDTO orderHistoryDTO);

	/**
	 * Gets the product codes.
	 * 
	 * @param orderId
	 *           the order id
	 * @return the product codes
	 */
	public List<String> getProductCodes(String orderId);

	/**
	 * This method is used to call the getOrderStatus method of Jnj Order Dao Impl class.
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
	public OrderStatus getOrderStatus(final String overAllStatus, final String rejectionStatus, final String creditStatus,
			final String deliveryStatus, final String invoiceStatus);

	/**
	 * This method is used to call the getOrderEntryStatus method of Jnj Order Dao Impl class.
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
	public OrderEntryStatus getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus);

	/**
	 * Gets the latest order.
	 * 
	 * @return the latest order
	 */
	public OrderModel getLatestOrder();

	/**
	 * The sftpCallAndMoveFileToZipFolder performs the SFTP call to move the file in the SFTP folder and on the basis of
	 * success/failure message,file will be moved to archive folder/error folder.
	 * 
	 * @param fileListForSAP
	 *           the file list for sap
	 * @return true, if successful
	 */
	public boolean sftpCallAndMoveFileToZipFolder(final List<File> fileListForSAP);


}
