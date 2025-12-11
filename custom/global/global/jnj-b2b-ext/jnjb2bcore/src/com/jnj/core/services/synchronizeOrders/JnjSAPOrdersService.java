/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.synchronizeOrders;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.Collection;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjSAPOrdersService
{
	/**
	 * Returns SAP Order searched by SAp Order Number.
	 * 
	 * @param orderNumber
	 * @return
	 */
	OrderModel getExistingOrderBySAPOrderNumber(String orderNumber);

	/**
	 * Returns SAP Order Entry searched by Entry Number.
	 * 
	 * @param orderEntryNumber
	 * @return
	 */
	OrderEntryModel getExistingOrderEntry(String orderEntryNumber, String ordderNumber);

	/**
	 * 
	 * @param lineNumber
	 * @return
	 */
	JnjDeliveryScheduleModel getExistingScheduleLineByLineNumber(String lineNumber);

	/**
	 * Creates new order in Hybris based on the SAP Order.
	 * 
	 * @return
	 */
	OrderModel createNewOrder();

	/**
	 * Saves a New/Updated Order in Hybris.
	 * 
	 * @param item
	 */
	String saveItem(final ItemModel item) throws ModelSavingException;

	OrderEntryModel createOrderEntry();

	ProductModel getProductForCode(String productCode, String countryCode);

	JnjDeliveryScheduleModel createJnjDeliverySchedule();

	B2BUnitModel getUnitForUid(String shipToNumber);

	AddressModel getAddressForCode(B2BUnitModel unit, String addressCode);

	DeliveryModeModel createDeliveryMode();

	CurrencyModel getCurrentCurrency(final JnJB2BUnitModel jnJB2BUnitModel);

	UserModel getUser(B2BUnitModel unit);


	/**
	 * Retrieves and return the SAP Order status based on the combination of several statuses received.
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param creditStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 * @return
	 */
	OrderStatus getOrderStatus(final String overAllStatus, final String rejectionStatus, final String creditStatus,
			final String deliveryStatus, final String invoiceStatus) throws BusinessException;

	/**
	 * Retrieves and return the SAP Order Entry status based on the combination of several statuses received.
	 * 
	 * @param overAllStatus
	 * @param rejectionStatus
	 * @param deliveryStatus
	 * @param invoiceStatus
	 * @return
	 */
	OrderEntryStatus getOrderEntryStatus(final String overAllStatus, final String rejectionStatus, final String deliveryStatus,
			final String invoiceStatu, final String gtsHold);

	UnitModel getUnitOfMeasurement(final String unitCode);

	/**
	 * Returns the clone of the address.
	 * 
	 * @param address
	 * @return
	 */
	AddressModel cloneAddress(AddressModel address);



	/**
	 * Initiates and triggers Email Notification event for on Order Status change.
	 * 
	 * @param customer
	 * @param currentStatus
	 * @param previousStatus
	 */
	void sendStatusChangeNotification(CustomerModel customer, final String sapOrderNumber, final String clientOrderNumber,
			final String jnjOrderNumber, OrderStatus currentStatus, OrderStatus previousStatus, final String baseUrl,
			final Boolean isSyncOrder, final String mediaLogoURL, final String toEmail);

	/**
	 * Removes <code>Collection</code> of items sent, from their respective types.
	 */
	String removeItems(Collection<? extends ItemModel> items);

	/**
	 * Initiates and triggers Email Notification event for on Order Status change.
	 * 
	 * @param customer
	 * @param orderCode
	 * @param baseUrl
	 * @param mediaLogoURL
	 */
	void sendStatusChangeNotification(CustomerModel customer, String orderCode, String baseUrl, String mediaLogoURL,String toEmail);
	
	/**
	 * AAOL-5163
	 * @param currentUserForCheckout
	 * @param orderCode
	 * @param baseUrl
	 * @param createMediaLogoURL
	 * @param email
	 */
	void sendReturnOrderUserEmail(CustomerModel currentUserForCheckout, String orderCode, String baseUrl,
			String createMediaLogoURL, String email);
	
	/**
	 * AAOL-5163
	 * @param currentUserForCheckout
	 * @param orderCode
	 * @param baseUrl
	 * @param createMediaLogoURL
	 * @param email
	 */
	void sendReturnOrderCSREmail(CustomerModel currentUserForCheckout, String orderCode, String baseUrl,
			String createMediaLogoURL, String email);

}
