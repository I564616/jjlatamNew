/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.invoice.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPricePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.facades.data.JnJInvoiceEntryData;
import com.jnj.facades.data.JnjProductData;


/**
 * Populator class to populate JnJInvoiceEntryData from JnJInvoiceEntryModel
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJInvoiceEntryDataPopulator implements Populator<JnJInvoiceEntryModel, JnJInvoiceEntryData>
{

	/**
	 * Method to populate JnJInvoiceEntryData from JnJInvoiceEntryModel
	 */

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnJInvoiceEntryDataPopulator.class);

	private Converter<JnJProductModel, JnjProductData> jnjProductdataConverter;
	private ProductPricePopulator productPricePopulator;


	@Override
	public void populate(final JnJInvoiceEntryModel source, final JnJInvoiceEntryData target) throws ConversionException
	{

		logMethodStartOrEnd(Logging.INVOICE_ENTRY_DATA_POPULATOR, Logging.METHOD_POPULATE, Logging.BEGIN_OF_METHOD);

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setItemNo(source.getItemNo());
		target.setQty(source.getQty());
		target.setLotNo(source.getLotNo());
		target.setOrderReason(source.getOrderReason());
		target.setSalesOrderItemNo(source.getSalesOrderItemNo());

		final JnjProductData jnjProductData = new JnjProductData();
		target.setMaterial(getJnjProductdataConverter().convert(source.getMaterial(), jnjProductData));
		productPricePopulator.populate(source.getMaterial(), jnjProductData);

		logMethodStartOrEnd(Logging.INVOICE_ENTRY_DATA_POPULATOR, Logging.METHOD_POPULATE, Logging.END_OF_METHOD);

	}

	/**
	 * @return the jnjProductdataConverter
	 */
	public Converter<JnJProductModel, JnjProductData> getJnjProductdataConverter()
	{
		return jnjProductdataConverter;
	}

	/**
	 * @param jnjProductdataConverter
	 *           the jnjProductdataConverter to set
	 */
	public void setJnjProductdataConverter(final Converter<JnJProductModel, JnjProductData> jnjProductdataConverter)
	{
		this.jnjProductdataConverter = jnjProductdataConverter;
	}

	/**
	 * @return the productPricePopulator
	 */
	public ProductPricePopulator getProductPricePopulator()
	{
		return productPricePopulator;
	}

	/**
	 * @param productPricePopulator
	 *           the productPricePopulator to set
	 */
	public void setProductPricePopulator(final ProductPricePopulator productPricePopulator)
	{
		this.productPricePopulator = productPricePopulator;
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

}
