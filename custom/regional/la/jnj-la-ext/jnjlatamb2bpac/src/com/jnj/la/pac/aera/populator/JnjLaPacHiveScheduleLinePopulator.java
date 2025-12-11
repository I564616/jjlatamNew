/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.pac.aera.populator;

import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.order.converters.populator.JnjLaScheduleLinePopulator;
import com.pac.aera.job.service.JnjGTPacHiveConfigurationService;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Populates PAC HIVE data from {@link AbstractOrderModel} to {@link AbstractOrderData}.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class JnjLaPacHiveScheduleLinePopulator extends JnjLaScheduleLinePopulator
{
	private static final Logger LOG = LoggerFactory.getLogger(JnjLaPacHiveScheduleLinePopulator.class);
	private static final String DATE_FORMAT_TO_BE_DISCARDED= "jnj.la.deliveryDate.tobeDiscarded";

	private JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService;

	@Override
	public void populate(final AbstractOrderEntryModel sourceEntryModel, final JnjGTOrderEntryData targetEntryData)
	{
		super.populate(sourceEntryModel, targetEntryData);

		try
		{
			if (null == sourceEntryModel || null == targetEntryData)
			{
				LOG.warn("[PAC HIVE] Source or target is null. Exiting populator without doing anything." +
				         " Source: '{}', target: '{}'. Order: '{}'.",
				         sourceEntryModel, targetEntryData, this.getOrderCode(sourceEntryModel)
				);
				return;
			}

			if (this.jnjGTPacHiveConfigurationService.isPacHiveEnabledForOrderEntry(sourceEntryModel))
			{
				this.populatePacHiveEstimatedDeliveryDate(sourceEntryModel, targetEntryData);
			}
			else
			{
				LOG.debug(
						"[PAC HIVE] PAC HIVE is disabled." +
						" PAC HIVE data will was not populated for order: '{}' and entry PK: '{}'.",
						this.getOrderCode(sourceEntryModel), sourceEntryModel.getPk()
				);
			}
		} catch (Exception e)
		{
			if (null != sourceEntryModel) {
				LOG.error(
						"[PAC HIVE] Failed to populate PAC HIVE data for or Order Entry with PK: '{}'. Order PK: '{}'"
								+ " sapOrderNumber: '{}'.",
						sourceEntryModel.getPk(),
						sourceEntryModel.getOrder() == null ? null : sourceEntryModel.getOrder().getPk(),
						sourceEntryModel.getOrder() == null ? null : sourceEntryModel.getOrder().getSapOrderNumber(),
						e);
			}
		}
	}

	protected void populatePacHiveEstimatedDeliveryDate(@Nonnull final AbstractOrderEntryModel sourceEntryModel,
			@Nonnull final JnjGTOrderEntryData targetEntryData)
	{
		Validate.notNull(targetEntryData, "Target entry can not be null.");
		Validate.notNull(sourceEntryModel, "Source entry can not be null.");

		if (CollectionUtils.isEmpty(sourceEntryModel.getJnjPacHiveEntries()))
		{
			LOG.debug(
					"[PAC HIVE] Source entry does not have PAC HIVE entries to populate into schedule lines." +
					" Order: '{}', entry PK: '{}'. Number of schedule lines: '{}'.",
					this.getOrderCode(sourceEntryModel),
					sourceEntryModel.getPk(),
					null == sourceEntryModel.getDeliverySchedules() ? 0 : sourceEntryModel.getDeliverySchedules().size()
			);
			return;
		}

		if (CollectionUtils.isEmpty(targetEntryData.getScheduleLines()))
		{
			final List<JnjDeliveryScheduleData> dummyScheduleDataList = populateEDDInDummyScheduleLines(sourceEntryModel, targetEntryData);
			targetEntryData.setScheduleLines(dummyScheduleDataList);
		}
		else 
		{
			for (JnjPacHiveEntryModel sourceJnjPacHiveEntry : sourceEntryModel.getJnjPacHiveEntries())
			{
				populateScheduleEDDWithPacEDD(sourceEntryModel, targetEntryData, sourceJnjPacHiveEntry);
			}
		}
	}

	/**
	 * This method is to populate the schedule line EDD with PAC EDD
	 * @param sourceEntryModel the sourceEntryModel is the orderentry model
	 * @param targetEntryData the targetEntryData is the order entry data
	 * @param sourceJnjPacHiveEntry The sourceJnjPacHiveEntry is the pachive entry
	 */
	private void populateScheduleEDDWithPacEDD(final AbstractOrderEntryModel sourceEntryModel,
			final JnjGTOrderEntryData targetEntryData, JnjPacHiveEntryModel sourceJnjPacHiveEntry) {

		boolean foundLineToUpdate = false;
		for (JnjDeliveryScheduleData targetScheduleLine : targetEntryData.getScheduleLines())
		{
			//mapped with LineNo feild as per Data availability in PROD 
			final String scheduleLineNumber = stripLeadingZeros(targetScheduleLine.getLineNumber());
			final String pacHiveScheduleLineNumber = this.stripLeadingZeros(sourceJnjPacHiveEntry.getSchedLineNumber());
			
			if (StringUtils.equals(scheduleLineNumber, pacHiveScheduleLineNumber) && targetScheduleLine.getProofOfDeliveryDate() == null)
			{
				LOG.debug(
						"[PAC HIVE] Updated schedule line delivery date for order: '{}' entry PK '{}' from" +
								" PAC HIVE entry with PK: '{}' and SchedLineNumber: '{}'. Old value: '{}' new value: '{}'.",
								this.getOrderCode(sourceEntryModel),
								sourceEntryModel.getPk(),
								sourceJnjPacHiveEntry.getPk(),
								sourceJnjPacHiveEntry.getSchedLineNumber(),
								targetScheduleLine.getDeliveryDate(),
								sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
				final String dateListToBeDiscarded= getConfigurationService().getConfiguration().getString(DATE_FORMAT_TO_BE_DISCARDED);
				
				if(!isDateDiscarded(sourceJnjPacHiveEntry, dateListToBeDiscarded))
				{
					targetScheduleLine.setDeliveryDate(sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
				}
				else {
					targetScheduleLine.setDeliveryDate(null);
				}
				LOG.info("EDD : "+targetScheduleLine.getDeliveryDate());
				foundLineToUpdate = true;
				break;
			}
		}

		if (!foundLineToUpdate)
		{
			LOG.warn("[PAC HIVE] Could find a corresponding schedule line to update delivery date for order: '{}'" +
							" entry PK '{}' from PAC HIVE entry with PK: '{}' and SchedLineNumber: '{}'." +
							" RecommendedDeliveryDate: '{}'.",
							this.getOrderCode(sourceEntryModel),
							sourceEntryModel.getPk(),
							sourceJnjPacHiveEntry.getPk(),
							sourceJnjPacHiveEntry.getSchedLineNumber(),
							sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
		}
	}

	/**
	 * @param sourceEntryModel
	 * @param targetEntryData
	 * @return list of delivery schedule lines
	 */
	private List<JnjDeliveryScheduleData> populateEDDInDummyScheduleLines(
			final AbstractOrderEntryModel sourceEntryModel, final JnjGTOrderEntryData targetEntryData) {
		final List<JnjDeliveryScheduleData> dummyScheduleDataList =new ArrayList<>();
		for (JnjPacHiveEntryModel sourceJnjPacHiveEntry : sourceEntryModel.getJnjPacHiveEntries())
		{
			final JnjDeliveryScheduleData target = new JnjDeliveryScheduleData();
			target.setLineNumber(this.stripLeadingZeros(sourceJnjPacHiveEntry.getSchedLineNumber()));
			if(null!=sourceJnjPacHiveEntry.getConfirmedQuantity()) {
			target.setQuantity((sourceJnjPacHiveEntry.getConfirmedQuantity()).longValue());
			}
			final String dateListToBeDiscarded= getConfigurationService().getConfiguration().getString(DATE_FORMAT_TO_BE_DISCARDED);
			if(!isDateDiscarded(sourceJnjPacHiveEntry, dateListToBeDiscarded)){
				target.setDeliveryDate(sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
				dummyScheduleDataList.add(target);
				if (null != sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate()) {
				  targetEntryData.setExpectedDeliveryDate(sourceJnjPacHiveEntry.getConvertedRecommendedDeliveryDate());
				}
			}
			
			LOG.info("EDD : "+targetEntryData.getExpectedDeliveryDate());
		}
		
		return dummyScheduleDataList;
	}

	private static boolean isDateDiscarded(final JnjPacHiveEntryModel sourceJnjPacHiveEntry, final String dateListToBeDiscarded) {
		boolean isDateDiscarded = false;
		if(null != dateListToBeDiscarded )
		{
			for(final String dateToBeDiscarded: dateListToBeDiscarded.split(Jnjb2bCoreConstants.SYMBOl_COMMA))				
			{
				if(sourceJnjPacHiveEntry.getRecommendedDeliveryDate() != null
						&& sourceJnjPacHiveEntry.getRecommendedDeliveryDate().equals(dateToBeDiscarded))
				{
					isDateDiscarded = true;
					break;
				}
			}
		}
		return isDateDiscarded;
	}

	@Nullable
	protected String stripLeadingZeros(@Nullable final String sapOrderLineNumber)
	{
		return StringUtils.stripStart(sapOrderLineNumber, "0");
	}

	@Nullable
	protected String getOrderCode(@Nullable final AbstractOrderEntryModel source)
	{
		return (source != null && source.getOrder() != null) ? source.getOrder().getCode() : null;
	}

	public void setJnjGTPacHiveConfigurationService(final JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService)
	{
		this.jnjGTPacHiveConfigurationService = jnjGTPacHiveConfigurationService;
	}
}
