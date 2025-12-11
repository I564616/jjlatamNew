/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjOrderData;


/**
 * Extending OOTB OrderPopulator TODO:
 * 
 * <Sandeep Kumar-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderDataPopulator extends OrderPopulator
{
	@Autowired
	private SessionService sessionService;
	private OrderEntryData orderEntryData;
	@Resource(name = "priceDataFactory")
	private JnjPriceDataFactory jnjPriceDataFactory;

	static final String PREFIX_ORDER_STATUS = "order.status.";


	@Override
	public void populate(final OrderModel source, final OrderData target)
	{
		super.populate(source, target);

		if (target instanceof JnjOrderData)
		{
			final JnjOrderData jnjData = (JnjOrderData) target;
			jnjData.setPalcedInSap(Boolean.FALSE);
			if (StringUtils.isNotEmpty(source.getSapOrderNumber()))
			{
				jnjData.setSapOrderNumber(source.getSapOrderNumber());
				jnjData.setPalcedInSap(Boolean.TRUE);
			}

			if (source.getPurchaseOrderNumber() != null)
			{
				jnjData.setClientOrderNumber(source.getPurchaseOrderNumber());
			}
			else
			{
				jnjData.setClientOrderNumber(source.getCode());
			}
			jnjData.setDeliveryStatus(source.getDeliveryStatus());
			jnjData.setCompleteOrder(source.getCompleteDelivery());
			jnjData.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
			jnjData.setSalesForbidden(source.getForbiddenSales());
			// Changes Start for CR CP003
			jnjData.setTaxTotal(createPrice(source, source.getTotalTax()));
			if (null != source.getTotalNetValue())
			{
				jnjData.setTotalNetValue(createPrice(source, source.getTotalNetValue()));
			}
			jnjData.setTotalDropShipFee(createPrice(source, source.getTotalDropShipFee()));
			jnjData.setTotalExpeditedFees(createPrice(source, source.getTotalExpeditedFees()));
			jnjData.setTotalFreightFees(createPrice(source, source.getTotalFreightFees()));
			jnjData.setTotalHandlingFee(createPrice(source, source.getTotalHandlingFee()));
			jnjData.setTotalminimumOrderFee(createPrice(source, source.getTotalminimumOrderFee()));
			jnjData.setDiscountTotal(createPrice(source, Double.valueOf(Math.abs(source.getTotalDiscounts().doubleValue()))));
			jnjData.setTotalInsurance(createPrice(source, source.getTotalInsurance()));
			if (null != source.getTotalGrossPrice())
			{
				jnjData.setTotalGrossPrice(createPrice(source, source.getTotalGrossPrice()));
			}
			else
			{
				jnjData.setTotalGrossPrice(createPrice(source, source.getTotalPrice()));
			}
			if (jnjData.getEntries() != null)
			{
				for (final OrderEntryData orderEntry : jnjData.getEntries())
				{
					orderEntry.setBasePrice(createPrice(source, Double.valueOf(orderEntry.getBasePrice().getValue().doubleValue())));
					orderEntry.setTotalPrice(createPrice(source, Double.valueOf(orderEntry.getTotalPrice().getValue().doubleValue())));
				}
			}

			if (null != source.getUnit())
			{
				jnjData.setCustomerName(source.getUnit().getDisplayName());
			}
			if (null != source.getPaymentAddress() && null != jnjData.getPaymentInfo())
			{
				jnjData.getPaymentInfo().setAccountHolderName(
						source.getPaymentAddress().getFirstname() + source.getPaymentAddress().getMiddlename()
								+ source.getPaymentAddress().getLastname());
			}
			jnjData.setCode(source.getCode());
			if (null != source.getEntries())
			{
				jnjData.setEntrySize(Integer.valueOf(source.getEntries().size()));
			}
			if (null != source.getStatus())
			{
				jnjData.setStatusDisplay((OrderStatus.valueOf(source.getStatus().getCode())).toString());
			}

		}
	}


	/**
	 * This method is used to get the order Entries (Product details for particular order)
	 */
	@Override
	protected void addEntries(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		orderEntryData = null;
		String sessionObject = sessionService.getAttribute("jnjOrderDetailPageSize");
		if (null == sessionObject)
		{
			sessionObject = String.valueOf(source.getEntries().size());
		}

		int jnjOrderDetailPageSize = (Integer.valueOf(sessionObject)).intValue();
		if (jnjOrderDetailPageSize > source.getEntries().size())
		{
			jnjOrderDetailPageSize = source.getEntries().size();
		}

		//get page size from session service
		//if not exist 
		if (0 == jnjOrderDetailPageSize)
		{
			prototype.setEntries(Converters.convertAll(source.getEntries(), getOrderEntryConverter()));
		}


		else
		{
			final ArrayList<OrderEntryData> orderEntryList = new ArrayList<OrderEntryData>();
			if (prototype instanceof JnjOrderData)
			{
				((JnjOrderData) prototype).setOrderentrylistsize(Integer.valueOf(source.getEntries().size()));
			}

			for (int i = 0; i < jnjOrderDetailPageSize; i++)
			{
				orderEntryData = new OrderEntryData();
				if (null != source.getEntries())
				{
					orderEntryData = getOrderEntryConverter().convert(source.getEntries().get(i));
				}
				orderEntryList.add(orderEntryData);
				prototype.setEntries(orderEntryList);
			}
		}
	}

	@Override
	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
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

		return jnjPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}
}
