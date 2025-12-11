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
import com.jnj.gt.mapper.b2bunit.JnjGTB2BUnitDataLoadMapper;
import com.jnj.gt.model.JnjGTIntB2BUnitLocalModel;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * The JnjGTIntB2BUnitLocalFacadeImpl class contains the methods which are used for clean the records and calls the
 * method for processing of intermediate records.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTIntB2BUnitLocalFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntB2BUnitLocalFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTB2BUnitDataLoadMapper jnjGTB2BUnitDataLoadMapper;

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
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status
		final List<JnjGTIntB2BUnitLocalModel> jnjGTIntB2BUnitLocalModels = (List<JnjGTIntB2BUnitLocalModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntB2BUnitLocalModel._TYPECODE, recordStatus, selectionDate);

		if (CollectionUtils.isNotEmpty(jnjGTIntB2BUnitLocalModels))
		{
			invalidRecords.addAll(jnjGTIntB2BUnitLocalModels);
			//delete all the records.
			jnjGTFeedService.invalidateRecords(invalidRecords);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
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
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "processIntermediaryRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// changing the status from loading to pending.
		jnjGTFeedService.updateIntRecordStatus(JnjGTIntB2BUnitLocalModel._TYPECODE);
		// Process the pending the records and set the data in hybris model.
		jnjGTB2BUnitDataLoadMapper.mapCustomerLocalDataWithB2BUnit();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
