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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.product.JnjGTProductExclusionMapper;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * The JnjGTIntProductExclusionFacadeImpl class contains the methods which are used for clean the records and calls the
 * method for processing of intermediate records.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTIntProductExclusionFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntProductExclusionFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTProductExclusionMapper jnjGTProductExclusionMapper;

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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status
		final List<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModels = (List<JnjGTIntProductExclusionModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntProductExclusionModel._TYPECODE, recordStatus, selectionDate);

		if (CollectionUtils.isNotEmpty(jnjGTIntProductExclusionModels))
		{
			invalidRecords.addAll(jnjGTIntProductExclusionModels);
			//delete all the records.
			jnjGTFeedService.invalidateRecords(invalidRecords);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
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
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntProductExclusionModel._TYPECODE); // Process the pending the
		 * records and set the data in hybris model. jnjGTProductExclusionMapper.processIntermediateRecords(); if
		 * (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN +
		 * "processIntermediateRecords()" + Logging.HYPHEN + Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

}
