/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.factory.order;

import de.hybris.platform.core.model.order.CartModel;

import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.factory.order.JnjGTCartFactory;
import com.jnj.core.model.JnJB2BUnitModel;
import org.apache.log4j.Logger;

public class JnjLatamCartFactory extends JnjGTCartFactory
{

	private static final Logger LOGGER = Logger.getLogger(JnjLatamCartFactory.class);

	@Override
	protected CartModel createCartInternal()
	{
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		LOGGER.info("Initializing Latam empty Cart for Unit :" + currentB2BUnit + " Site:" + baseSiteService.getCurrentBaseSite());
		final CartModel cart = super.createCartInternal();
		cart.setUnit(currentB2BUnit);
		cart.setSite(baseSiteService.getCurrentBaseSite());
		cart.setStore(baseStoreService.getCurrentBaseStore());
		cart.setOrderType(JnjOrderTypesEnum.ZOR);
		cart.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress(currentB2BUnit));
		cart.setPaymentAddress(jnjGTB2BUnitService.getBillingAddress(currentB2BUnit));
		LOGGER.info("Latam Cart initialization done Cart Type is :" + cart.getOrderType().getCode());
		return cart;
	}

}
