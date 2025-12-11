/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import java.util.Locale;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjOrderLineItemCancelEmailTiggerCronJobModel;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.dto.JnjGTOrderHistoryForm;


/**
 * The Interface JnjGTOrderService.
 * 
 * @author Accenture
 * @version 1.0
 */

public interface JnjGTOrderService extends JnjOrderService
{

	/**
	 * Checks if is pO number used.
	 * 
	 * @param poNumber
	 *           the po number
	 * @return true, if is pO number used
	 */
	public boolean isPONumberUsed(String poNumber);

	/**
	 * Retrieves Paged Order History Data using <code>JnjGTOrderDao</code> based on the criteria selected.
	 * 
	 * @param pageableData
	 *           the pageable data
	 * @param form
	 *           the form
	 * @return SearchPageData<OrderModel>
	 */
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData, JnjGTOrderHistoryForm form);

	/**
	 * Save order.
	 * 
	 * @param orderModel
	 *           the order model
	 * @return true, if successful
	 */
	public boolean saveOrder(final OrderModel orderModel);

	/**
	 * Checks if the current user in session eligible for Surgeon Details Update on the Order History page based on:
	 * 
	 * <ul>
	 * <li>a. If User is a DePuy Mitek, DePuy Codman, or DePuy Spine Sales Rep, and</li>
	 * <li>b. Order has no shipment record, and</li>
	 * <li>c. Order Type is Delivered.</li>
	 * </ul>
	 * 
	 * @param order
	 *           the order
	 * @return boolean
	 */
	public boolean isSurgeonUpdateEligible(final OrderModel order);

	/**
	 * Checks if the current user in session eligible for PO update on the Order History page based on:
	 * 
	 * <ul>
	 * <li>a. If Customer PO Number is blank, and</li>
	 * <li>b. User is a DePuy Mitek, DePuy Codman, or DePuy Spine Sales Rep, and</li>
	 * <li>c. Order Type is Delivered.</li>
	 * </ul>
	 * 
	 * @param order
	 *           the order
	 * @return boolean
	 */
	public boolean isPurchaseOrderUpdateEligible(final OrderModel order);

	/**
	 * Gets the order by sap order number.
	 * 
	 * @param sapOrderNumber
	 *           the sap order number
	 * @param baseSiteModel
	 *           the base site model
	 * @return the order by sap order number
	 */
	public OrderModel getOrderBySapOrderNumber(final String sapOrderNumber, final BaseSiteModel baseSiteModel);

	/**
	 * Gets the delivery blocks for entries.
	 * 
	 * @param orderPK
	 *           the order pk
	 * @return the delivery blocks for entries
	 */
	public List<String> getDeliveryBlocksForEntries(final String orderPK);

	/**
	 * Updates batchContentInd attribute in OrderModel
	 * 
	 * @param orderModel
	 * @return
	 */
	public boolean updateBatchContentInd(OrderModel orderModel);

	/**
	 * The set the order entry status by using the order entry model.
	 * 
	 * @param orderEntryModel
	 * 
	 */
	public void populateMddOrderEntryStatus(final AbstractOrderEntryModel orderEntryModel);

	/**
	 * Retrieve Sales Territory code from the <code>JnjGTTerritoryDivison</code> recourd found based on shipToNumber.
	 * 
	 * @param shipToNumber
	 * @return String
	 */
	public String getSalesTerritoryByShipToNum(String shipToNumber);

	/**
	 * Returns <code>Collection</code> of all <code>OrderModel</code> which are eligible for shipment email notification
	 * for its users.
	 * 
	 * @return Collection<OrderModel>
	 */
	public Collection<OrderModel> getOrdersEligibleForShipmentNotification();

	/**
	 * Instantiates and publish the even responsible to trigger Email Notification for a Shipped/Confirmed Order.
	 * 
	 */
	public void sendOrderShipEmailNotification();

	/**
	 * Instantiates and publish the even responsible to trigger Email Notification for all Back-orders having available
	 * date for each line..
	 * @throws DuplicateUidException 
	 * 
	 */
	public void sendBackOrderEmailNotification() throws DuplicateUidException;

	/**
	 * Remove quote orders careted the day before current date.
	 * 
	 */
	public void removeQuoteOrders();
	
	public JnjGTShippingMethodModel getShippingMethod(final String route, final String selectedRoute);
	public JnjGTShippingMethodModel getShippingMethodFromOrderEntry(final AbstractOrderEntryModel source);
	
	/**
	 * Gets the order code.
	 *
	 * @param orderCode
	 *           the order code
	 * @param ignoreRestriction
	 *           YTODO
	 * @return the order code
	 */
	public OrderModel getOrderDetailsForCode(final String orderCode, boolean ignoreRestriction);
	
	/**
	 * 
	 * @param jobModel
	 * @return
	 */
	public Map<JnJB2bCustomerModel,List<OrderEntryModel>> getCancelOrderLineItems(JnjOrderLineItemCancelEmailTiggerCronJobModel jobModel);
	
	public void sendCancelOrderLineStatusEmail(JnJB2bCustomerModel jnJB2bCustomerModel,List<OrderEntryModel> orderEntries);
	//AAOL-2420 changes
	public String getJnjOrderCreditLimitMsg(String code , Locale loc);
	//end of AAOL-2420 changes

}
