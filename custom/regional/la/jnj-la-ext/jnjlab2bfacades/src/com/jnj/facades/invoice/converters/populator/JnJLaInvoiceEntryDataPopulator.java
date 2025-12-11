/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.invoice.converters.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnJLaInvoiceEntryData;
import com.jnj.facades.data.JnjLaProductData;


/**
 * Populator class to populate JnJInvoiceEntryData from JnJInvoiceEntryModel
 *
 * @author Accenture
 * @version 1.0
 */
public class JnJLaInvoiceEntryDataPopulator extends JnJInvoiceEntryDataPopulator
{

	/**
	 * Method to populate JnJInvoiceEntryData from JnJInvoiceEntryModel
	 */

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnJLaInvoiceEntryDataPopulator.class);

	private Converter<JnJProductModel, JnjLaProductData> jnjLaProductdataConverter;

	@Override
	public void populate(final JnJInvoiceEntryModel source, final JnJInvoiceEntryData target) throws ConversionException
	{

		if (target instanceof JnJLaInvoiceEntryData)
		{
			final JnJLaInvoiceEntryData latarget = (JnJLaInvoiceEntryData) target;
			LOGGER.info("Inside JnJLaInvoiceEntryDataPopulator--------------");

			Assert.notNull(source, "Parameter source cannot be null.");
			Assert.notNull(target, "Parameter target cannot be null.");

			latarget.setItemNo(source.getItemNo());
			latarget.setQty(source.getQty());
			latarget.setLotNo(source.getLotNo());
			latarget.setOrderReason(source.getOrderReason());
			latarget.setSalesOrderItemNo(source.getSalesOrderItemNo());

			final JnjLaProductData jnjLaProductData = new JnjLaProductData();
			latarget.setLamaterial(getJnjLaProductdataConverter().convert(source.getMaterial(), jnjLaProductData));
			latarget.getLamaterial().setDeliveryUnitCode(source.getMaterial().getDeliveryUnitOfMeasure().getCode());
			latarget.getLamaterial().setSalesUnit(source.getMaterial().getUnit().getCode());

			try
			{
				final int numeratorDUOM = Integer.parseInt(source.getMaterial().getNumeratorDUOM());
				final int numeratorSUOM = Integer.parseInt(source.getMaterial().getNumeratorSUOM());
				final int multipleof = numeratorDUOM / numeratorSUOM;
				latarget.getLamaterial().setNumeratorDUOM(String.valueOf(multipleof));

			}
			catch (final NumberFormatException exception)
			{
				latarget.getLamaterial().setNumeratorDUOM("1");
			}
			catch (final ArithmeticException exception)
			{
				LOGGER.error("Arithmetic Exception occured" + exception);
				latarget.getLamaterial().setNumeratorDUOM("1");
			}
		}
	}

	/**
	 * @return the jnjLaProductdataConverter
	 */
	public Converter<JnJProductModel, JnjLaProductData> getJnjLaProductdataConverter()
	{
		return jnjLaProductdataConverter;
	}

	/**
	 * @param jnjLaProductdataConverter
	 *           the jnjLaProductdataConverter to set
	 */
	public void setJnjLaProductdataConverter(final Converter<JnJProductModel, JnjLaProductData> jnjLaProductdataConverter)
	{
		this.jnjLaProductdataConverter = jnjLaProductdataConverter;
	}

}
