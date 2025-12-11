/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.math.BigDecimal;

import com.jnj.facades.data.JnjOrderHistoryData;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderHistoryPopulator extends OrderHistoryPopulator
{
	private PriceDataFactory priceDataFactory;

	@Override
	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Override
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);
		if (target instanceof JnjOrderHistoryData)
		{
			final JnjOrderHistoryData jnjHistoryData = (JnjOrderHistoryData) target;
			if (source.getPurchaseOrderNumber() != null)
			{
				jnjHistoryData.setClientNumber(source.getPurchaseOrderNumber());
			}
			else
			{
				jnjHistoryData.setClientNumber(source.getCode());
			}
			jnjHistoryData.setJnJOrderNumber(source.getSapOrderNumber());
			jnjHistoryData.setDate(source.getDate());
			jnjHistoryData.setTotalOrder(source.getTotalPrice());
			if (null != source.getStatus())
			{
				jnjHistoryData.setOrderStatus((OrderStatus.valueOf(source.getStatus().getCode())).toString());
			}

			jnjHistoryData.setSapOrderNumber(source.getSapOrderNumber());
			jnjHistoryData.setCustomerPONumber(source.getPurchaseOrderNumber());
			jnjHistoryData.setOrderNumber(source.getCode());

			if (null != source.getUnit())
			{
				jnjHistoryData.setSoldToNumber(source.getUnit().getId());
			}
			if (null != source.getUnit() && null != source.getUnit().getShippingAddress())
			{
				jnjHistoryData.setShipToNumber(source.getUnit().getShippingAddress().getJnJAddressId());
			}

			jnjHistoryData.setPayFromNumber(source.getPayFromNumber());
			jnjHistoryData.setNamedDeliveryDate(source.getDate().toString());
			jnjHistoryData.setTotalNetPrice(String.valueOf(source.getTotalPrice()));
			jnjHistoryData.setStartDate(source.getCreationtime().toString());
			jnjHistoryData.setForbiddenSales(source.getForbiddenSales());
			jnjHistoryData.setCompleteDelivery(source.getCompleteDelivery());
			jnjHistoryData.setSalesOrganizationCode(source.getSalesOrganizationCode());
			jnjHistoryData.setDistributionChannel(source.getDistributionChannel());
			jnjHistoryData.setDivision(source.getDivision());
			jnjHistoryData.setSalesOrderOverallStatus(source.getSalesOrderOverallStatus());
			jnjHistoryData.setSalesOrderDeliveryStatus(source.getSalesOrderDeliveryStatus());
			jnjHistoryData.setSalesOrderRejectionStatus(source.getSalesOrderRejectionStatus());
			jnjHistoryData.setSalesOrderCreditStatus(source.getSalesOrderCreditStatus());
			jnjHistoryData.setSalesOrderDataCompleteness(source.getSalesOrderDataCompleteness());
			jnjHistoryData.setHeaderDeliveryBlock(source.getHeaderDeliveryBlock());
			jnjHistoryData.setPoType(source.getPoType());
			jnjHistoryData.setInvoiceStatus(source.getInvoiceStatus());

			if (null != source.getTotalGrossPrice())
			{
				jnjHistoryData.setTotalGrossPrice(createPrice(source, source.getTotalGrossPrice()));
			}
			else
			{
				jnjHistoryData.setTotalGrossPrice(createPrice(source, source.getTotalPrice()));
			}
		}

	}



	private PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

}
