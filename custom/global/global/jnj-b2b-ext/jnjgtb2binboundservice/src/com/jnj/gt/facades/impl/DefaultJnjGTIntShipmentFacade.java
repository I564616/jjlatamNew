/**
 *
 */
package com.jnj.gt.facades.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.shipment.JnjGTShipmentDataLoadMapper;
import com.jnj.gt.model.JnjGTIntShipTrckHdrModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.shipment.JnjGTShipmentFeedService;


/**
 * The JnjGTIntShipmentFacadeImpl class contains the methods which are used for clean the records and calls the method
 * for processing of intermediate records.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTIntShipmentFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntShipmentFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTShipmentDataLoadMapper jnjGTShipmentDataLoadMapper;
	@Autowired
	private JnjGTShipmentFeedService jnjGTB2BUnitFeedService;


	/**
	 * The cleanInvalidRecords method is used to delete all those records which has loading status.
	 *
	 * @param record
	 *           status
	 */
	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status
		final List<JnjGTIntShipTrckHdrModel> jnjGTIntShipTrckHdrModels = (List<JnjGTIntShipTrckHdrModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntShipTrckHdrModel._TYPECODE, recordStatus, selectionDate);
		// Iterates each record one by one and get the child related with it.
		for (final JnjGTIntShipTrckHdrModel jnjGTIntShipTrckHdrModel : jnjGTIntShipTrckHdrModels)
		{
			if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCorrelationId()))
			{
				invalidRecords.addAll(jnjGTB2BUnitFeedService.getJnjGTIntShipTrckLineModel(
						jnjGTIntShipTrckHdrModel.getCorrelationId(), null, null));
			}
			if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getDeliveryNum()))
			{
				invalidRecords.addAll(jnjGTB2BUnitFeedService.getJnjGTIntShipTrckLineModel(null,
						jnjGTIntShipTrckHdrModel.getDeliveryNum(), null));
			}
		}

		if (CollectionUtils.isNotEmpty(jnjGTIntShipTrckHdrModels))
		{
			invalidRecords.addAll(jnjGTIntShipTrckHdrModels);
		}
		if (CollectionUtils.isNotEmpty(invalidRecords))
		{
			//delete all the records.
			jnjGTFeedService.invalidateRecords(invalidRecords);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
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
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntShipTrckHdrModel._TYPECODE); // Process the pending the records
		 * and set the data in hybris model. jnjGTShipmentDataLoadMapper.processIntermediateRecords(); if
		 * (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN +
		 * "processIntermediateRecords()" + Logging.HYPHEN + Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

}
