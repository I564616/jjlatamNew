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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.order.JnjGTOrderSyncDataLoadMapper;
import com.jnj.gt.model.JnjGTIntOrdHdrNoteModel;
import com.jnj.gt.model.JnjGTIntOrdLineHoldLocalModel;
import com.jnj.gt.model.JnjGTIntOrdLinePriceLocalModel;
import com.jnj.gt.model.JnjGTIntOrderHeaderModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderLinePartModel;
import com.jnj.gt.model.JnjGTIntOrderLineTxtModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.order.JnjGTOrderFeedService;


/**
 * This class contains the methods which are used to clean the records and calls the method for processing of
 * intermediate records.
 * 
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjGTIntOrderFeedFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntOrderFeedFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper;
	@Autowired
	private JnjGTOrderFeedService jnjGTOrderFeedService;

	/**
	 * The cleanInvalidRecords method is used to delete all those records which has loading status.
	 * 
	 * @param recordStatus
	 *           status
	 */
	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		/*
		 * Delete existing obsolete records (MDD ONLY) from the Header, Line, Note, Price, Line Text, Line Part, Line Hold
		 * and Schedule Line intermediate tables.
		 */
		final List<String> mddSourceSystems = JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<JnjGTIntOrderHeaderModel> invalidIntOrderHeaderRecords = (List<JnjGTIntOrderHeaderModel>) jnjGTFeedService
				.getRecordsByStatusAndSourceSys(JnjGTIntOrderHeaderModel._TYPECODE, recordStatus, mddSourceSystems, selectionDate);

		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(invalidIntOrderHeaderRecords))
		{
			invalidIntRecords.addAll(invalidIntOrderHeaderRecords);
			invalidIntRecords.addAll(getIntermediateRecordsForCleanup(invalidIntOrderHeaderRecords));
		}

		jnjGTFeedService.invalidateRecords(invalidIntRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * The processIntermediaryRecords method process all those records which has been put in intermediate tables and
	 * populates the data in hybris tables.
	 */
	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntOrderHeaderModel._TYPECODE);
		 * 
		 * // Process the pending the records and set the data in hybris model.
		 * jnjGTOrderSyncDataLoadMapper.processIntermediateRecords(); if (LOGGER.isDebugEnabled()) {
		 * LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN +
		 * Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

	/**
	 * Retrieves all associated Intermediate table records based on the intermediate orders passed.
	 * 
	 * @param invalidIntOrderHeaderRecords
	 * @return Collection<ItemModel>
	 */
	private Collection<ItemModel> getIntermediateRecordsForCleanup(
			final List<JnjGTIntOrderHeaderModel> invalidIntOrderHeaderRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();
		String sapOrderNumber = null;
		String sourceSysId = null;
		final Date date = new Date();
		for (final JnjGTIntOrderHeaderModel intOrderHeaderModel : invalidIntOrderHeaderRecords)
		{
			sapOrderNumber = intOrderHeaderModel.getSapOrderNumber();
			sourceSysId = intOrderHeaderModel.getSourceSystemId();

			/** Fetch Intermediate Order Header Notes Records. **/
			final Collection<JnjGTIntOrdHdrNoteModel> intOrderHeaderNotesRecords = jnjGTOrderFeedService.getJnjGTIntOrdHdrNoteModel(
					sapOrderNumber, sourceSysId);

			if (!CollectionUtils.isEmpty(intOrderHeaderNotesRecords))
			{
				invalidIntRecords.addAll(intOrderHeaderNotesRecords);
			}


			/** Fetch Intermediate Sap Order Line Records. **/
			final Collection<JnjGTIntOrderLineModel> jnjGTIntOrderLines = jnjGTOrderFeedService.getJnjGTIntOrderLineModel(
					sapOrderNumber, sourceSysId,
					JnJCommonUtil.getValues(Jnjgtb2bCONSConstants.OrderFeed.ITEM_CATEGORY, Jnjb2bCoreConstants.SYMBOl_COMMA), null);

			if (!CollectionUtils.isEmpty(jnjGTIntOrderLines))
			{
				invalidIntRecords.addAll(jnjGTIntOrderLines);

				for (final JnjGTIntOrderLineModel intOrderLineModel : jnjGTIntOrderLines)
				{
					/** Fetch Intermediate Sap Schedule Line Records. **/
					final Collection<JnjGTIntOrderSchLineModel> intOrderSchLines = jnjGTOrderFeedService.getJnjGTIntOrderSchLineModel(
							sapOrderNumber, sourceSysId, intOrderLineModel.getSapOrderLineNumber());

					if (!CollectionUtils.isEmpty(intOrderSchLines))
					{
						invalidIntRecords.addAll(intOrderSchLines);
					}

					/** Fetch Intermediate Order Line Hold Local Records. **/
					final Collection<JnjGTIntOrdLineHoldLocalModel> intOrdLineHoldLocalRecords = jnjGTOrderFeedService
							.getJnjGTIntOrdLineHoldLocalModel(sapOrderNumber, sourceSysId, null, null);

					if (!CollectionUtils.isEmpty(intOrdLineHoldLocalRecords))
					{
						invalidIntRecords.addAll(intOrdLineHoldLocalRecords);
					}

					/** Fetch Intermediate Order Line Price Local Records. **/
					final Collection<JnjGTIntOrdLinePriceLocalModel> intOrdLinePriceLocalRecords = jnjGTOrderFeedService
							.getJnjGTIntOrdLinePriceLocalModel(sapOrderNumber, sourceSysId, null);

					if (!CollectionUtils.isEmpty(intOrdLinePriceLocalRecords))
					{
						invalidIntRecords.addAll(intOrdLinePriceLocalRecords);
					}
				}
			}

			/** Fetch Intermediate Order Line Part Records. **/
			final Collection<JnjGTIntOrderLinePartModel> intOrderLinePartRecords = jnjGTOrderFeedService
					.getJnjGTIntOrderLinePartModel(sapOrderNumber, sourceSysId);

			if (!CollectionUtils.isEmpty(intOrderLinePartRecords))
			{
				invalidIntRecords.addAll(intOrderLinePartRecords);
			}

			/** Fetch Intermediate Order Line Text Records. **/
			final Collection<JnjGTIntOrderLineTxtModel> intOrderLineTextRecords = jnjGTOrderFeedService
					.getJnjGTIntOrderLineTxtModel(sapOrderNumber, sourceSysId);

			if (!CollectionUtils.isEmpty(intOrderLineTextRecords))
			{
				invalidIntRecords.addAll(intOrderLineTextRecords);
			}
		}
		return invalidIntRecords;
	}
}
