/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.invoice.converters.populator;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnJInvoiceOrderData;


/**
 * Populator class to populate JnJInvoiceOrderData from JnJInvoiceOrderModel.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJInvoiceOrderDataPopulator implements Populator<JnJInvoiceOrderModel, JnJInvoiceOrderData>
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnJInvoiceOrderDataPopulator.class);

	/** The jn j invoice entry data converter. */
	private Converter<JnJInvoiceEntryModel, JnJInvoiceEntryData> jnJInvoiceEntryDataConverter;

	/** The price data factory. */
	private PriceDataFactory priceDataFactory;

	/** The default commerce common i18 n service. */
	@Autowired
	private DefaultCommerceCommonI18NService defaultCommerceCommonI18NService;




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

		logMethodStartOrEnd(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE, Logging.BEGIN_OF_METHOD);

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setInvDocNo(source.getInvDocNo());
		target.setBillType(source.getBillType());

		target.setNetValue(getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(source.getNetValue()),
				defaultCommerceCommonI18NService.getCurrentCurrency()));
		target.setCreationDate(source.getCreationDate());
		if (null != source.getSoldTo())
		{
			target.setSoldTo(source.getSoldTo().getUid());
		}
		else
		{
			logDebugMessage(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE, "JnJInvoiceOrderModel has null soldTO");
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
		final List<JnJInvoiceEntryModel> jnJInvoiceEntryModelList = source.getEntries();
		final List<JnJInvoiceEntryData> jnJInvoiceEntryDataList = new ArrayList<JnJInvoiceEntryData>();
		for (final JnJInvoiceEntryModel jnJInvoiceEntryModel : jnJInvoiceEntryModelList)
		{

			jnJInvoiceEntryDataList.add(getJnJInvoiceEntryDataConverter().convert(jnJInvoiceEntryModel, new JnJInvoiceEntryData()));
		}
		target.setEntries(jnJInvoiceEntryDataList);
		logMethodStartOrEnd(Logging.INVOICE_ORDER_POPULATOR, Logging.METHOD_POPULATE, Logging.END_OF_METHOD);

	}


	/**
	 * Gets the jn j invoice entry data converter.
	 * 
	 * @return the jnJInvoiceEntryDataConverter
	 */
	public Converter<JnJInvoiceEntryModel, JnJInvoiceEntryData> getJnJInvoiceEntryDataConverter()
	{
		return jnJInvoiceEntryDataConverter;
	}

	/**
	 * Sets the jn j invoice entry data converter.
	 * 
	 * @param jnJInvoiceEntryDataConverter
	 *           the jnJInvoiceEntryDataConverter to set
	 */
	public void setJnJInvoiceEntryDataConverter(
			final Converter<JnJInvoiceEntryModel, JnJInvoiceEntryData> jnJInvoiceEntryDataConverter)
	{
		this.jnJInvoiceEntryDataConverter = jnJInvoiceEntryDataConverter;
	}


	/**
	 * Gets the price data factory.
	 * 
	 * @return the priceDataFactory
	 */
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
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * Utility method used for logging entry into / exit from any method in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}


}
