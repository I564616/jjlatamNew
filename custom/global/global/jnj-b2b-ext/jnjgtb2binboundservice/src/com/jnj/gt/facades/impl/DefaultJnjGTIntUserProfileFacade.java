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
import com.jnj.gt.mapper.user.JnjGTUserProfileDataLoadMapper;
import com.jnj.gt.model.JnjGTIntB2bCustomerModel;
import com.jnj.gt.model.JnjGTIntUserB2bUnitModel;
import com.jnj.gt.model.JnjGTIntUserPermissionModel;
import com.jnj.gt.model.JnjGTIntUserSalesOrgModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.user.JnjGTUserFeedService;


/**
 * @author sakshi.kashiva
 *
 */
public class DefaultJnjGTIntUserProfileFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntB2BUnitFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;
	@Autowired
	private JnjGTUserFeedService jnjGTUserFeedService;
	@Autowired
	private JnjGTUserProfileDataLoadMapper jnjGTUserProfileDataLoadMapper;

	/**
	 * The processIntermediaryRecords method process all those records which has been put in intermediate tables and
	 * populates the data in hybris tables.
	 */
	@Override
	public void processIntermediaryRecords()
	{
		
		  if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN +
		  "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		  + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		  jnjGTFeedService.updateIntRecordStatus(JnjGTIntB2bCustomerModel._TYPECODE);
		  
		  // Process the pending the records and set the data in hybris model.
		  jnjGTUserProfileDataLoadMapper.processIntermediateRecords(); if (LOGGER.isDebugEnabled()) {
		  LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN +
		  Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime()); }
		 
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.JnjGTIntFacade#cleanInvalidRecords(com.jnj.core.enums.RecordStatus)
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
		// Fetch all those records which has loading status For First Sales  Alignment Table
		final List<JnjGTIntB2bCustomerModel> jnjGTIntB2bCustomerModelList = (List<JnjGTIntB2bCustomerModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntB2bCustomerModel._TYPECODE, recordStatus, selectionDate);
		// Adding the List to the invalidRecords.
		invalidRecords.addAll(jnjGTIntB2bCustomerModelList);
		// Fetch all those records which has loading status For Second Sales  Alignment Table
		for (final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel : jnjGTIntB2bCustomerModelList)
		{
			final List<JnjGTIntUserPermissionModel> jnjGTIntUserPermissionModellist = jnjGTUserFeedService
					.getIntUserPermissionIdForCustomer(jnjGTIntB2bCustomerModel);
			// Adding the List to the invalidRecords.
			invalidRecords.addAll(jnjGTIntUserPermissionModellist);

		}
		for (final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel : jnjGTIntB2bCustomerModelList)
		{
			// Fetch all those records which has loading status For Second Sales  Alignment Table
			final List<JnjGTIntUserB2bUnitModel> jnjGTIntB2BUnitModelList = jnjGTUserFeedService
					.getIntB2BUnitForEmail(jnjGTIntB2bCustomerModel);
			// Adding the List to the invalidRecords.
			invalidRecords.addAll(jnjGTIntB2BUnitModelList);
		}
		for (final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel : jnjGTIntB2bCustomerModelList)
		{
			// Fetch all those records which has loading status For Third Sales  Alignment Table
			final List<JnjGTIntUserSalesOrgModel> jnjGTIntUserSalesOrgModelList = jnjGTUserFeedService
					.getIntUserSalesOrgForCustomer(jnjGTIntB2bCustomerModel);
			// Adding the List to the invalidRecords.
			if (CollectionUtils.isNotEmpty(jnjGTIntUserSalesOrgModelList))
			{
				invalidRecords.addAll(jnjGTIntUserSalesOrgModelList);
			}
		}
		//delete all the records.
		jnjGTFeedService.invalidateRecords(invalidRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}
}
