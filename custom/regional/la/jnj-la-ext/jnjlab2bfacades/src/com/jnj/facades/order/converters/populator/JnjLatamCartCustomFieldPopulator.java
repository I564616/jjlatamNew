/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import com.jnj.facades.data.JnjLaCartData;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;

/**
* JnjLatamCartCustomFieldPopulator
*/
public class JnjLatamCartCustomFieldPopulator implements Populator<CartModel, CartData> {

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		if (target instanceof JnjLaCartData jnjLaCartData)
		{
			jnjLaCartData.setCartReferenceNumber(source.getExternalOrderRefNumber());
			jnjLaCartData.setShipToAccount(source.getShipToAccount());
			jnjLaCartData.setSoldToAccount(source.getUnit().getUid());
		}	
	}
}
