/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLaOrderHistoryData;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;


/**
 *
 */
public class JnjLaOrderHistoryPopulator extends JnjGTOrderHistoryPopulator
{

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);
		setCustomStatus(source, target);
		if (target instanceof JnjLaOrderHistoryData)
		{
			((JnjLaOrderHistoryData) target).setContractNumber(source.getContractNumber());
		}

		if (target instanceof JnjLaOrderHistoryData)
		{
			BigDecimal totalGrossPrice = BigDecimal.valueOf(source.getTotalGrossPrice());
			((JnjLaOrderHistoryData) target).setTotal(
					getPriceDataFactory().create(PriceDataType.BUY, totalGrossPrice, source.getCurrency())
			);
		}
	}
	
	private void setCustomStatus(final OrderModel source, final OrderHistoryData target) {
		if(source.getStatus() != null) {
			if(OrderStatus.ERP_EXPORT_ERROR.getCode().equalsIgnoreCase(source.getStatus().getCode())) {
				target.setStatusDisplay(enumerationService.getEnumerationName(OrderStatus.CREATED));
			}else {
				target.setStatusDisplay(enumerationService.getEnumerationName(source.getStatus()));
			}
		} else {
			target.setStatusDisplay(StringUtils.EMPTY);
		}
	}
}
