/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;

import com.jnj.core.dto.JnjCheckoutDTO;


/**
 * The Facade Class for NFe to get and process the NFe Data.
 * 
 * @author Accenture
 * @version 1.0
 */

public interface JnjCheckoutFacade extends CheckoutFacade
{

	/**
	 * This method is uesd to create order from current cart. It may create more then one order from one cart depend upon
	 * the user and products inside the cart.
	 * 
	 * @param jnjCheckoutDTO
	 *           the jnj checkout dto
	 * @return List the list
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public List<String> placeOrder(JnjCheckoutDTO jnjCheckoutDTO) throws InvalidCartException;


	/**
	 * This method is used to create an order from cart obtained from Replenish Order Job.
	 * 
	 * @param cartModel
	 * @throws InvalidCartException
	 */
	public void placeOrderfromReplenishJob(final CartModel cartModel) throws InvalidCartException;

	boolean sendOrderStatusEmail(final String sapOrderNumber, final String clientOrderNumber, final String jnjOrderNumber,
			final OrderStatus currentStatus, final OrderStatus previousStatus, final String baseUrl, final Boolean isSyncOrder);

	public CustomerModel getCurrentUserForCheckoutForEmail();


	/**
	 * This method sends order status email
	 * 
	 * @param orderCode
	 * @param baseUrl
	 * @param isSyncOrder
	 * @return true/false
	 */
	boolean sendOrderStatusEmail(String orderCode, String baseUrl, Boolean isSyncOrder);
	
	/**
	 * AAOL-5163
	 * Method added to trigger mail for return order with attachments 
	 * @param orderCode
	 * @param baseUrl
	 */
	public boolean sendReturnOrderUserEmail(String orderCode, String baseUrl);
	
	/**
	 * AAOL-5163
	 * Method added to trigger mail for return order for CSR with attachments
	 * @param request
	 * @param orderCode
	 */
	public boolean sendReturnOrderCSREmail(String orderCode, String baseUrl);
}
