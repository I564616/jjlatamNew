/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;


import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import com.jnj.facades.data.JnjCartData;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjCartPopulator extends CartPopulator<CartData>
{

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		if (target instanceof JnjCartData)
		{
			final JnjCartData jnjCartData = (JnjCartData) target;

			// Changes Start for CR CP003
			jnjCartData.setTaxTotal(createPrice(source, source.getTotalTax()));
			jnjCartData.setTotalNetValue(createPrice(source, source.getTotalNetValue()));
			jnjCartData.setTotalDropShipFee(createPrice(source, source.getTotalDropShipFee()));
			jnjCartData.setTotalExpeditedFees(createPrice(source, source.getTotalExpeditedFees()));
			jnjCartData.setTotalFreightFees(createPrice(source, source.getTotalFreightFees()));
			jnjCartData.setTotalHandlingFee(createPrice(source, source.getTotalHandlingFee()));
			jnjCartData.setTotalminimumOrderFee(createPrice(source, source.getTotalminimumOrderFee()));
			jnjCartData.setDiscountTotal(createPrice(source, Double.valueOf(Math.abs(source.getTotalDiscounts().doubleValue()))));
			jnjCartData.setTotalInsurance(createPrice(source, source.getTotalInsurance()));
			if (null != source.getTotalGrossPrice())
			{
				jnjCartData.setTotalGrossPrice(createPrice(source, source.getTotalGrossPrice()));
			}
			else
			{
				jnjCartData.setTotalGrossPrice(createPrice(source, source.getTotalPrice()));
			}

			// Changes Done for CR CP003
		}
	}
}
