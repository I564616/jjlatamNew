/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.factory.order;

import jakarta.annotation.Resource;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.impl.DefaultCartFactory;
import de.hybris.platform.site.BaseSiteService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;



//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;

import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

/**
 * @author Accenture
 * @version 1.0
 */

public class JnjGTCartFactory extends DefaultCartFactory
{
	protected static final Logger LOGGER = Logger.getLogger(JnjGTCartFactory.class);
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;
	@Autowired
	protected BaseSiteService baseSiteService;
	
	@Resource(name = "commerceCartService")
	protected JnjGTCartService jnjGTCartService;
	
	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	protected BaseStoreService baseStoreService;
	
	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}


	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}


	public JnjGTCartService getJnjGTCartService() {
		return jnjGTCartService;
	}


	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}


	@Override
	protected CartModel createCartInternal()
	{
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		LOGGER.info("Initializing empty Cart for Unit :" + currentB2BUnit + " Site:" + baseSiteService.getCurrentBaseSite());
		final CartModel cart = super.createCartInternal();
		cart.setUnit(currentB2BUnit);
		cart.setSite(baseSiteService.getCurrentBaseSite());
		cart.setStore(baseStoreService.getCurrentBaseStore());
		cart.setOrderType(jnjGTCartService.getDefaultOrderType());
		final JnjGTDivisonData divisionData = jnjGTCustomerService.getPopulatedDivisionData(null);
		if (null != divisionData && divisionData.isIsMitek()
				&& jnjGTCartService.getDefaultOrderType().equals(JnjOrderTypesEnum.ZDEL))
		{
			// changes as per the defect no. 22971
			cart.setDeliveryCost(Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.MITEK_FREIGHT_CHARGES)));
			cart.setTotalFees(Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.MITEK_FREIGHT_CHARGES)));
		}
		cart.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress(currentB2BUnit));
		cart.setPaymentAddress(jnjGTB2BUnitService.getBillingAddress(currentB2BUnit));
		LOGGER.info("Cart initiliztion done Cart Type is :" + cart.getOrderType().getCode());
		return cart;
	}
}