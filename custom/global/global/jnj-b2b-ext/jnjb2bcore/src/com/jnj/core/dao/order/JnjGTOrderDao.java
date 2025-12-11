/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dao.order;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;


/**
 * @author Accenture
 * @version 1.0
 */

public interface JnjGTOrderDao
{

	/**
	 * Gets the paged order history for statuses.
	 * 
	 * @param pageableData
	 *           the pageable data
	 * @param form
	 *           the form
	 * @return the paged order history for statuses
	 */
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData, JnjGTOrderHistoryForm form);

	/**
	 * Gets the delivery blocks for entries.
	 * 
	 * @param orderPK
	 *           the order pk
	 * @return the delivery blocks for entries
	 */
	public List<String> getDeliveryBlocksForEntries(final String orderPK);

	/**
	 * Retrieve <code>JnjGTTerritoryDivison</code> record based on the shipToNumber found in onr of the associated
	 * addresses to it.
	 * 
	 * @param shipToNumber
	 * @return String
	 */
	public JnjGTTerritoryDivisonModel getSalesTerritoryByShipToNum(String shipToNumber);

	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> which have <code>sendOrderShipmentEmail</code> flag
	 * set and are eligible for shipment email notification for its users.
	 */
	public Collection<OrderModel> getOrdersEligibleForShipmentNotification();

	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> which have atleast a line with 'BACKORDERED' status
	 * and has Available date.
	 */
	public Collection<OrderEntryModel> getBackordersEligibleForEmailNotification();

	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> which have 'QUOTE' order type and created the one
	 * day before current date.
	 */
	public List<OrderModel> getOrdersByTypeAndCreationTime(final String orderType, final Date date);
	
	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> which are eligible for email notification for its users.
	 */
	public Collection<OrderModel> getOrdersEligibleForEmailNotification();
	
	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> for a particular user going back orderHistoryDays
	 * days
	 */
	public Collection<OrderEntryModel> getBackordersAndOpenLinesEligibleForEmailNotification(List<String> b2bUnitListPerUser,
			int orderHistoryDays);
	
	/**
	 * Gets the order.
	 *
	 * @param orderCoden
	 *           the order coden
	 * @param salesOrgCodes
	 *           the sales org codes
	 * @param salesRep
	 *           the sales rep
	 * @return the order
	 */
	public OrderModel getOrder(final String orderCoden, final List<String> salesOrgCodes, final Boolean salesRep);
	
	public Collection<OrderEntryModel> getShippedOrdersEligibleForEmailNotification(List<String> b2bUnits, int days);
	
	public Map<JnJB2bCustomerModel, List<OrderEntryModel>> getCancelOrderLineItems();
}
