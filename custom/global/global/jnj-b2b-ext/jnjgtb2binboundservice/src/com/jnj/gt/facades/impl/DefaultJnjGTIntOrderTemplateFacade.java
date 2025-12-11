/**
 *
 */
package com.jnj.gt.facades.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.ordertemplate.JnjGTOrderTemplateDataLoadMapper;
import com.jnj.gt.model.JnjGTIntOrderTemplateEntryModel;
import com.jnj.gt.model.JnjGTIntOrderTemplateModel;
import com.jnj.core.services.Ordertemplate.JnjGTOrderTemplateService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.customer.JnjGTCustomerService;


/**
 * @author sakshi.kashiva
 *
 */
public class DefaultJnjGTIntOrderTemplateFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntB2BUnitFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTOrderTemplateService jnjGTOrderTemplateService;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjGTOrderTemplateDataLoadMapper jnjGTOrderTemplateDataLoadMapper;

	/**
	 * The processIntermediaryRecords method process all those records which has been put in intermediate tables and
	 * populates the data in hybris tables.
	 */
	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); } // changing the status from loading to pending.
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntOrderTemplateModel._TYPECODE);
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntOrderTemplateEntryModel._TYPECODE); // Process the pending the
		 * records and set the data in hybris model. jnjGTOrderTemplateDataLoadMapper.processIntermediateRecords(); if
		 * (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN +
		 * "processIntermediateRecords()" + Logging.HYPHEN + Logging.END_TIME + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}


	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<ItemModel> invalidRecords = new ArrayList<ItemModel>();
		// Fetch all those records which has loading status For First Sales  Alignment Table
		final List<JnjGTIntOrderTemplateModel> jnjGTIntOrderTemplateModelList = (List<JnjGTIntOrderTemplateModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntOrderTemplateModel._TYPECODE, recordStatus, selectionDate);
		// Adding the List to the invalidRecords.
		invalidRecords.addAll(jnjGTIntOrderTemplateModelList);
		// Fetch all those records which has loading status For Second Sales  Alignment Table
		final Collection<JnjGTIntOrderTemplateEntryModel> jnjIntOrderEntryModelList = (Collection<JnjGTIntOrderTemplateEntryModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntOrderTemplateEntryModel._TYPECODE, recordStatus, selectionDate);
		// Adding the List to the invalidRecords.
		invalidRecords.addAll(jnjIntOrderEntryModelList);
		//delete all the records.
		jnjGTFeedService.invalidateRecords(invalidRecords);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

}
