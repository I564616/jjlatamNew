/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.converters.populator;


import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.math.BigDecimal;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnjOrderEntryData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderEntryPopulator extends OrderEntryPopulator
{

	@Resource(name = "priceDataFactory")
	private JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	JnjScheduleLinePopulator jnjScheduleLinePopulator;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		super.populate(source, target);
		if (target instanceof JnjOrderEntryData)
		{
			final JnjOrderEntryData jnjOrderEntryData = (JnjOrderEntryData) target;

			if (StringUtils.isNotEmpty(source.getPriceOverride())
					&& !StringUtils.equals(source.getPriceOverride(), Jnjb2bFacadesConstants.ZERO_IN_DOUBLE_VALUE_STRING))
			{
				jnjOrderEntryData.setPriceOverride(jnjPriceDataFactory.formatPrice(BigDecimal.valueOf(Double.parseDouble(source
						.getPriceOverride()))));
			}
			jnjOrderEntryData.setPriceOverrideReason(source.getPriceOverrideReason());

			jnjOrderEntryData.setExpectedDeliveryDate(source.getExpectedDeliveryDate());
			jnjOrderEntryData.setSapOrderlineNumber(source.getSapOrderlineNumber());
			jnjOrderEntryData.setReasonForRejection(source.getReasonForRejection());
			jnjOrderEntryData.setSalesOrg(source.getSalesOrg());
			jnjOrderEntryData.setSplProdType(source.getSapOrderType());

			jnjOrderEntryData.setNewPrice(jnjPriceDataFactory.formatPrice(BigDecimal.valueOf(Double.valueOf(
					source.getPriceOverride()).doubleValue())));
			if (null != source.getDefaultPrice()
					&& !Double.valueOf(Jnjb2bFacadesConstants.ZERO_IN_FLOAT).equals(source.getDefaultPrice()))
			{
				jnjOrderEntryData.setDefaultPrice(jnjPriceDataFactory.formatPrice(BigDecimal.valueOf(source.getDefaultPrice()
						.doubleValue())));
			}
			// Check not null for the sales uom  then check for reminder when the quantity divide by sales uom value. 
			// If the value is not equal to zero then set the sales Uom calculated flag as false.
			if (null != source.getSalesUOM()
					&& source.getQuantity().intValue() % source.getSalesUOM().intValue() == Jnjb2bFacadesConstants.Order.ZERO_AS_INT)
			{
				jnjOrderEntryData.setSalesUomCalculated(Boolean.TRUE);
			}
			else
			{
				jnjOrderEntryData.setSalesUomCalculated(Boolean.FALSE);
			}
			// call the populate method to set the schedule line data in the jnj order entry data object.
			if (null != source.getOrder()
					&& (StringUtils.isNotEmpty(source.getOrder().getSapOrderNumber()) || BooleanUtils.isTrue(source.getOrder()
							.getSapValidated())))
			{
				jnjScheduleLinePopulator.populate(source, jnjOrderEntryData);
			}
		}
	}

}
