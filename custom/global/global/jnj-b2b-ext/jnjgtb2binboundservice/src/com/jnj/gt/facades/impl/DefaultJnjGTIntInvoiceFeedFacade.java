/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.facades.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.invoicedata.JnjGTLoadInvoiceDataLoadMapper;
import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoiceModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.invoice.JnjGTInvoiceFeedService;


/**
 * YTODO <<Replace this line with the class description>>
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTIntInvoiceFeedFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntProductListPriceFacade.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTInvoiceFeedService jnjGTInvoiceFeedService;

	@Autowired
	private JnjGTLoadInvoiceDataLoadMapper jnjGTInvoiceDataLoadMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.INVOICE + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final List<ItemModel> intermediateInvoiceModels = new ArrayList<>();

		final List<JnjGTIntInvoiceModel> jnjGTIntInvoiceModels = (List<JnjGTIntInvoiceModel>) jnjGTFeedService.getRecordsByStatus(
				JnjGTIntInvoiceModel._TYPECODE, recordStatus, selectionDate);

		intermediateInvoiceModels.addAll(jnjGTIntInvoiceModels);

		for (final JnjGTIntInvoiceModel jnjGTIntInvoiceModel : jnjGTIntInvoiceModels)
		{
			intermediateInvoiceModels.addAll(getIntermediateInvoiceRecords(jnjGTIntInvoiceModel.getInvoiceDocNum()));
		}

		jnjGTFeedService.invalidateRecords(intermediateInvoiceModels);


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.INVOICE + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.INVOICE + Logging.HYPHEN + "processIntermediaryRecords()" +
		 * Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 *//** After cleanup, mark the valid records status to PENDING **/
		/*
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntInvoiceModel._TYPECODE);
		 * 
		 * jnjGTInvoiceDataLoadMapper.processIntermediateRecords();
		 * 
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.INVOICE + Logging.HYPHEN + "cleanInvalidRecords()" +
		 * Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}


	private List<ItemModel> getIntermediateInvoiceRecords(final String invoiceDocNum)
	{
		final List<ItemModel> intermediateInvoiceRecords = new ArrayList<>();

		final List<JnjGTIntInvoiceEntryModel> IntInvoiceEntry = jnjGTInvoiceFeedService.getInvoiceEntry(invoiceDocNum);
		intermediateInvoiceRecords.addAll(IntInvoiceEntry);

		if (IntInvoiceEntry != null && (!IntInvoiceEntry.isEmpty()))
		{
			for (final JnjGTIntInvoiceEntryModel jnjGTIntInvoiceEntryModel : IntInvoiceEntry)
			{
				final List<JnjGTIntInvoiceEntryLotModel> IntInvoiceEntryLots = jnjGTInvoiceFeedService.getInvoiceEntryLot(
						invoiceDocNum, jnjGTIntInvoiceEntryModel.getInvoiceNum());
				intermediateInvoiceRecords.addAll(IntInvoiceEntryLots);

			}
		}

		final List<JnjGTIntInvoicePriceModel> invoicePrices = jnjGTInvoiceFeedService.getInvoicePrice(invoiceDocNum);
		intermediateInvoiceRecords.addAll(invoicePrices);

		return intermediateInvoiceRecords;
	}

}
