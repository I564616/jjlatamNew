/**
 *
 */
package com.jnj.gt.facades.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.b2bunit.JnjGTTerritoryDataLoadMapper;
import com.jnj.gt.model.JnjGTIntTerritoryModel;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * @author komal.sehgal
 *
 */
public class DefaultJnjGTIntTerritoryFacade extends JnjGTIntFacade
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntTerritoryFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTTerritoryDataLoadMapper jnjGTTerritoryDataLoadMapper;


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
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status For First Sales  Alignment Table
		final List<JnjGTIntTerritoryModel> jnjGTIntTerritoryModelList = (List<JnjGTIntTerritoryModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntTerritoryModel._TYPECODE, recordStatus, selectionDate);
		// Adding the List to the invalidRecords.
		invalidRecords.addAll(jnjGTIntTerritoryModelList);

		//delete all the records.
		jnjGTFeedService.invalidateRecords(invalidRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
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
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntTerritoryModel._TYPECODE); // Process the pending the records
		 * and set the data in hybris model. jnjGTTerritoryDataLoadMapper.processIntermediateRecords(); if
		 * (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN +
		 * "processIntermediateRecords()" + Logging.HYPHEN + Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

}
