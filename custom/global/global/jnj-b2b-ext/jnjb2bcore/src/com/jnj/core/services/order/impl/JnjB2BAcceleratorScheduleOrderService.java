/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.order.impl;

import de.hybris.platform.b2bacceleratorservices.orderscheduling.impl.B2BAcceleratorScheduleOrderService;


/**
 * Custom Jnj Class for B2BAcceleratorScheduleOrderService. Created to override the bean "cartToOrderJobBeanId" to
 * invoke a custom JnjB2bAcceleratorCartToOrderJob.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjB2BAcceleratorScheduleOrderService extends B2BAcceleratorScheduleOrderService
{

	private String cartToOrderJobBeanId;

	/**
	 * @return the cartToOrderJobBeanId
	 */
	@Override
	public String getCartToOrderJobBeanId()
	{
		return cartToOrderJobBeanId;
	}

	/**
	 * @param cartToOrderJobBeanId
	 *           the cartToOrderJobBeanId to set
	 */
	@Override
	public void setCartToOrderJobBeanId(final String cartToOrderJobBeanId)
	{
		this.cartToOrderJobBeanId = cartToOrderJobBeanId;
	}

}
