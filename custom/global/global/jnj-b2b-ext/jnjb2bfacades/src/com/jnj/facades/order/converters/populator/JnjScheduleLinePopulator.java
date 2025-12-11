/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.converters.populator;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.cart.impl.DefaultJnjCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjOrderEntryData;


/**
 * The JnjScheduleLinePopulator class is used to populate the data in the Schedule lines of JnjOrderEntryData from the
 * Delivery Schedule lines of the AbstractOrderEntry Model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjScheduleLinePopulator
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCartFacade.class);
	private final String available = "available";
	private final String notAvailable = "notAvailable";
	private final String partialyAvailable = "partialyAvailable";

	/**
	 * Populate the data in the JnjOrderEntryData object from the AbstractOrderEntryModel object.
	 * 
	 * @param source
	 *           the source
	 * @param target
	 *           the target
	 */
	public void populate(final AbstractOrderEntryModel source, final JnjOrderEntryData target)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		long confirmedQty = 0;
		if (CollectionUtils.isNotEmpty(source.getDeliverySchedules()))
		{
			final List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList = new ArrayList<JnjDeliveryScheduleData>();

			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : source.getDeliverySchedules())
			{
				if (null != jnjDeliveryScheduleModel)
				{
					final JnjDeliveryScheduleData scheduleData = populateScheduleEntry(jnjDeliveryScheduleModel);
					jnjDeliveryScheduleDataList.add(scheduleData);
					confirmedQty = confirmedQty + scheduleData.getQuantity().longValue();
				}
			}
			target.setScheduleLines(jnjDeliveryScheduleDataList);
			target.setConfirmedQty(confirmedQty);
			if (source.getQuantity().longValue() == confirmedQty && jnjDeliveryScheduleDataList.size() == 1)
			{
				target.setAvailabilityStatus(available);
				target.setExpectedDeliveryDate(jnjDeliveryScheduleDataList.get(0).getDeliveryDate());
			}
			else if (confirmedQty == 0)
			{
				target.setAvailabilityStatus(notAvailable);
			}
			else
			{
				target.setAvailabilityStatus(partialyAvailable);
			}
		}
		else
		//Delivery Schedule is not available
		{
			target.setAvailabilityStatus(notAvailable);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Populate schedule entry fields value in the JnjDeliveryScheduleData object fields.
	 * 
	 * @param source
	 *           the source
	 * @return the jnj delivery schedule data
	 */
	private JnjDeliveryScheduleData populateScheduleEntry(final JnjDeliveryScheduleModel source)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final JnjDeliveryScheduleData target = new JnjDeliveryScheduleData();
		target.setDeliveryDate(source.getDeliveryDate());
		target.setMaterialAvailabilityDate(source.getMaterialAvailabilityDate());
		target.setLineNumber(source.getLineNumber());
		target.setScheduledLineNumber(source.getScheduledLineNumber());
		target.setQuantity(source.getQty());

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return target;
	}
}
