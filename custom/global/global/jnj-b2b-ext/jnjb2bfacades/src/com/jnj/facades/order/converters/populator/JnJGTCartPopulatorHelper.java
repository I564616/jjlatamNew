/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * @author Accenture
 * @version 1.0
 */
public class JnJGTCartPopulatorHelper
{
	public static void populateCartTotalWeightNVolume(final AbstractOrderModel source, final AbstractOrderData target)
	{
		double cartWeight = 0;
		double cartVolume = 0;
		String weightUOM = null;
		String volumeUOM = null;
		for (final AbstractOrderEntryModel orderEntry : source.getEntries())
		{
			if (orderEntry.getReferencedVariant() != null)
			{
				final JnjGTVariantProductModel referenceVariant = orderEntry.getReferencedVariant();
				if (referenceVariant.getWeightQty() != null && referenceVariant.getWeightUom() != null)
				{
					cartWeight = cartWeight + referenceVariant.getWeightQty().doubleValue() * orderEntry.getQuantity().longValue();
					weightUOM = referenceVariant.getWeightUom().getCode().toString();
				}
				if (referenceVariant.getVolumeQty() != null && referenceVariant.getVolumeUom() != null)
				{
					cartVolume = cartVolume + referenceVariant.getVolumeQty().doubleValue() * orderEntry.getQuantity().longValue();
					volumeUOM = referenceVariant.getVolumeUom().getCode().toString();
				}
			}
		}

		final DecimalFormat decimalFormat = new DecimalFormat("0.##");
		target.setOrderWeight(Double.valueOf(decimalFormat.format(cartWeight)).doubleValue());
		target.setOrderWeightUOM(weightUOM);

		target.setOrderVolume(Double.valueOf(decimalFormat.format(cartVolume)).doubleValue());
		target.setOrderVolumeUOM(volumeUOM);
	}

	public static boolean isExpediateOrder(final AbstractOrderModel source)
	{

		for (final AbstractOrderEntryModel orderEntry : source.getEntries())
		{
			if (StringUtils.isNotEmpty(orderEntry.getSelectedRoute()))
			{
				return true;
			}
		}
		return false;

	}
}
