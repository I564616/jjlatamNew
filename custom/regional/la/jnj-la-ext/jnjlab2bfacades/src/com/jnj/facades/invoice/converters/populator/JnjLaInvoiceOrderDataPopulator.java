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
package com.jnj.facades.invoice.converters.populator;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.data.JnJLaInvoiceEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class JnjLaInvoiceOrderDataPopulator extends JnJInvoiceOrderDataPopulator
{

	/** The jnj invoice entry data converter. */
	@Autowired
	private Converter<JnJInvoiceEntryModel, JnJInvoiceEntryData> jnJLaInvoiceEntryDataConverter;

	/** The price data factory. */
	private PriceDataFactory priceDataFactory;

	/** The default commerce common i18 n service. */
	@Autowired
	private DefaultCommerceCommonI18NService defaultCommerceCommonI18NService;

	private static final Class currentClass = JnjLaInvoiceOrderDataPopulator.class;




	/**
	 * Method to populate JnJInvoiceOrderData from JnJInvoiceOrderModel.
	 *
	 * @param source
	 *           the source
	 * @param target
	 *           the target
	 * @throws ConversionException
	 *            the conversion exception
	 */
	@Override
	public void populate(final JnJInvoiceOrderModel source, final JnJInvoiceOrderData target) throws ConversionException
	{

		JnjGTCoreUtil.logInfoMessage(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE, Logging.BEGIN_OF_METHOD,
				currentClass);

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setInvDocNo(source.getInvDocNo());
		target.setBillType(source.getBillType());

		final double netValue = source.getNetValue() != null ? source.getNetValue().doubleValue() : 0;
		target.setNetValue(getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(netValue),
				defaultCommerceCommonI18NService.getCurrentCurrency()));
		target.setCreationDate(source.getCreationDate());
		if (null != source.getSoldTo())
		{
			target.setSoldTo(source.getSoldTo().getUid());
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE,
					"JnJInvoiceOrderModel has null soldTo", currentClass);
		}
		target.setPayer(source.getPayer());
		target.setPoNumber(source.getPoNumber());
		target.setRegion(source.getRegion());
		target.setNfYear(source.getNfYear());
		target.setNfMonth(source.getNfMonth());
		target.setStcd(source.getStcd());
		target.setModel(source.getModel());
		target.setSeries(source.getSeries());
		target.setNfNumber(source.getNfNumber());
		target.setDocNumber(source.getDocNumber());
		target.setCdv(source.getCdv());
		target.setBillingDoc(source.getBillingDoc());
		target.setCancelledDocNo(source.getCancelledDocNo());
		target.setSalesOrder(source.getSalesOrder());
		target.setCarrierEstimateDeliveryDate(source.getCarrierEstimateDeliveryDate());
		target.setCarrierConfirmedDeliveryDate(source.getCarrierConfirmedDeliveryDate());

		final List<JnJInvoiceEntryModel> jnJInvoiceEntryModelList = source.getEntries();
		final List<JnJInvoiceEntryData> jnJInvoiceEntryDataList = new ArrayList<JnJInvoiceEntryData>();
		for (final JnJInvoiceEntryModel jnJInvoiceEntryModel : jnJInvoiceEntryModelList)
		{
			jnJInvoiceEntryDataList.add(jnJLaInvoiceEntryDataConverter.convert(jnJInvoiceEntryModel, new JnJLaInvoiceEntryData()));
		}
		target.setEntries(jnJInvoiceEntryDataList);
		JnjGTCoreUtil.logInfoMessage(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE, Logging.END_OF_METHOD, currentClass);

	}


	/**
	 * Gets the price data factory.
	 *
	 * @return the priceDataFactory
	 */
	@Override
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}


	/**
	 * Sets the price data factory.
	 *
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	@Override
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

}
