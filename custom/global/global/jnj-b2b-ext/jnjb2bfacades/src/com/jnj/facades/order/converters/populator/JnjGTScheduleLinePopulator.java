/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.cart.impl.DefaultJnjCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.core.model.JnJProductModel;


/**
 * The JnjScheduleLinePopulator class is used to populate the data in the Schedule lines of JnjOrderEntryData from the
 * Delivery Schedule lines of the AbstractOrderEntry Model.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTScheduleLinePopulator implements Populator<AbstractOrderEntryModel, JnjGTOrderEntryData>
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjCartFacade.class);
	private final String available = "available";
	private final String notAvailable = "notAvailable";
	private final String partialyAvailable = "partialyAvailable";
	protected final static String CONFIRMED_LINE_STATUS_CODES = Config
			.getParameter(Jnjb2bCoreConstants.Order.CONFIRMED_SCHEDULE_LINE_STATUS);
	protected final static String DATE_FORMAT=Config.getParameter(Jnjb2bCoreConstants.Order.DATE_FORMAT);

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	/**
	 * Populate the data in the JnjOrderEntryData object from the AbstractOrderEntryModel object.
	 *
	 * @param source
	 *           the source
	 * @param target
	 *           the target
	 */
    @Override
	public void populate(final AbstractOrderEntryModel source, final JnjGTOrderEntryData target)
	{
    	if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		long confirmedQty = 0;
		if (CollectionUtils.isNotEmpty(source.getDeliverySchedules()))
		{
			Date expectedShipDate = null;
			Date expectedDelDate = null;
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			final List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList = new ArrayList<JnjDeliveryScheduleData>();

			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : source.getDeliverySchedules())
			{
				if (null != jnjDeliveryScheduleModel)
				{
					final JnjDeliveryScheduleData scheduleData = populateScheduleEntry(jnjDeliveryScheduleModel);
					/** Fixes For JJEPIC-703 **/
					if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD))
					{
						if (source.getDeliverySchedules().size() < 2)
						{
							expectedShipDate = scheduleData.getShippingDate();
							expectedDelDate = scheduleData.getDeliveryDate();
						}
						confirmedQty = confirmedQty + scheduleData.getQuantity().longValue();
					}
					else
					{
						if ((OrderStatus.BACKORDERED).equals(source.getOrder().getStatus())
								&& OrderEntryStatus.BACKORDERED.getCode().equalsIgnoreCase(scheduleData.getLineStatus()))
						{
							scheduleData.setShippingDate(((JnJProductModel) source.getProduct()).getBackOrderedDate());
						}
						confirmedQty = confirmedQty + scheduleData.getQuantity().longValue();

						if (expectedShipDate == null
								|| (scheduleData.getShippingDate() != null && expectedShipDate.after(scheduleData.getShippingDate())))
						{
							expectedShipDate = scheduleData.getShippingDate();
						}
						if (null == expectedDelDate)
						{
							expectedDelDate = scheduleData.getDeliveryDate();
						}
						else if (null != scheduleData.getDeliveryDate() && scheduleData.getDeliveryDate().after(expectedDelDate))
						{
							expectedDelDate = scheduleData.getDeliveryDate();
						}
					}
					/** Code Fixes For JJEPIC-657 Start **/
					jnjDeliveryScheduleDataList.add(scheduleData);
					/** Code Fixes For JJEPIC-657 End **/
				}
			}
			target.setScheduleLines(jnjDeliveryScheduleDataList);
			target.setConfirmedQty(confirmedQty);
			// To the set the max. expected delivery date at entry level, we compare each expected delivery date with each other and then set the maximum one.
			if (null != expectedDelDate)
			{
				target.setExpectedDeliveryDate(expectedDelDate);
			}
			else if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD) && null != target.getExpectedDeliveryDate())
			{
				target.setExpectedDeliveryDate(null);
			}

			if (source.getQuantity().longValue() == confirmedQty && jnjDeliveryScheduleDataList.size() == 1)
			{
				target.setAvailabilityStatus(available);
			}
			else if (confirmedQty == 0)
			{
				target.setAvailabilityStatus(notAvailable);
			}
			else
			{
				target.setAvailabilityStatus(partialyAvailable);
			}
			target.setExpectedShipDate(expectedShipDate);
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
		target.setLineNumber(source.getLineNumber());
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		if(source.getDeliveryDate()!=null)
		target.setFormattedDeliveryDate(simpleDateFormat.format(source.getDeliveryDate()));
		target.setQuantity(source.getQty());
		target.setShippingDate(source.getShipDate());
		//Display availability date from product model instead of Simulate response
		if (null != source.getOwnerEntry() && null != source.getOwnerEntry().getProduct())
		{
			target.setMaterialAvailabilityDate(((JnJProductModel) (source.getOwnerEntry().getProduct())).getBackOrderedDate());
		}
		final String scheduleLineCode = source.getLineStatus();
		String scheduleLineStatus = null;
		if (StringUtils.isNotEmpty(scheduleLineCode))
		{
			if (CONFIRMED_LINE_STATUS_CODES.contains(scheduleLineCode))
			{
				scheduleLineStatus = OrderEntryStatus.CONFIRMED.getCode();
			}
			else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_UC.equals(scheduleLineCode))
			{
				if (source.getOwnerEntry() != null
						&& Jnjb2bCoreConstants.Order.PRODUCT_DIVISION
								.equals(((JnJProductModel) source.getOwnerEntry().getProduct()).getSalesOrgCode()))
				{
					scheduleLineStatus = OrderEntryStatus.ITEM_ACCEPTED.getCode();
				}
				else
				{
					scheduleLineStatus = OrderEntryStatus.BACKORDERED.getCode();
				}
			}
			else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_CANCELLED.equals(scheduleLineCode))
			{
				scheduleLineStatus = OrderEntryStatus.CANCELLED.getCode();
			}
			target.setLineStatus(scheduleLineStatus);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("populate()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return target;
	}
}
