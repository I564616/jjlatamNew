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
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.service.b2bunit.JnjGTB2BUnitFeedService;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * The JnjGTIntB2BUnitFacadeImpl class contains the methods which are used for clean the records and calls the method
 * for processing of intermediate records.
 *
 * @author sumit.y.kumar
 *
 */
public class DefaultJnjGTIntB2BUnitFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntB2BUnitFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTB2BUnitDataLoadMapper jnjGTB2BUnitDataLoadMapper;
	@Autowired
	private JnjGTB2BUnitFeedService jnjGTB2BUnitFeedService;


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
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status
		final List<JnjGTIntB2BUnitModel> jnjGTIntB2BUnitModels = null;
		// Iterates each record one by one and get the child related with it.
		for (final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel : jnjGTIntB2BUnitModels)
		{
			invalidRecords.addAll(jnjGTB2BUnitFeedService.getJnjGTIntSalesOrgModel(jnjGTIntB2BUnitModel.getUid()));
			invalidRecords.addAll(jnjGTB2BUnitFeedService.getJnjGTIntPartnerFuncModel(jnjGTIntB2BUnitModel.getUid()));
		}
		// Fetch all those records which has loading status for intermediate affiliation models.
		final List<JnjGTIntAffiliationModel> jnjGTIntAffiliationModels = null;

		if (CollectionUtils.isNotEmpty(jnjGTIntAffiliationModels))
		{
			invalidRecords.addAll(jnjGTIntAffiliationModels);
		}
		if (CollectionUtils.isNotEmpty(jnjGTIntB2BUnitModels))
		{
			invalidRecords.addAll(jnjGTIntB2BUnitModels);
		}
		if (CollectionUtils.isNotEmpty(invalidRecords))
		{
			//delete all the records.
			jnjGTFeedService.invalidateRecords(invalidRecords);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
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
		System.out.println("JnjGTIntB2BUnitFacadeImpl processIntermediaryRecords() START");
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "processIntermediaryRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// changing the status from loading to pending.
		jnjGTFeedService.updateIntRecordStatus(JnjGTIntB2BUnitModel._TYPECODE);
		jnjGTFeedService.updateIntRecordStatus(JnjGTIntAffiliationModel._TYPECODE);
		// Process the pending the records and set the data in hybris model.
		jnjGTB2BUnitDataLoadMapper.processIntermediateRecords();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
