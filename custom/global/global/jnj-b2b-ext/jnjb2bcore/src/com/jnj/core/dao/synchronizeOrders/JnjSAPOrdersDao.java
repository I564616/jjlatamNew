/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.synchronizeOrders;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjSAPOrdersDao
{
	/**
	 * 
	 */
	public JnjDeliveryScheduleModel getExistingScheduleLineByLineNumber(String lineNumber);

	/**
	 * Finds existing order in Hybris based on SAP Order Number.
	 * 
	 * @param orderNumber
	 * @return <code>OrderModel</code>
	 * 
	 */
	public OrderModel getExistingOrderBySAPOrderNumber(final String orderNumber);

	/**
	 * Finds existing order Entry in Hybris based on SAP Order Entry Number.
	 * 
	 * @param entryNumber
	 * @return <code>OrderEntryModel</code>
	 */
	public OrderEntryModel getExistingOrderEntryByEntryNumber(final String entryNumber, final String orderNumber);

	/**
	 * Retrieves the Order Status from the Jnj Order Status table on the basis of incoming field.
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
	 * Retrieves the Order Entry Status from the Jnj Order Enty Status table on the basis of incoming field.
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
			final String deliveryStatus, final String invoiceStatus, final String gtsHold);

	UnitModel getUnitOfMeasurement(UnitModel unit) throws ModelNotFoundException, IllegalArgumentException;
}
