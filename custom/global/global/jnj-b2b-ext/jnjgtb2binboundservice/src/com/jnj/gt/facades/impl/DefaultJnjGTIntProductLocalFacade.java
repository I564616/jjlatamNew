/**
 *
 */
package com.jnj.gt.facades.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.product.JnjGTProductLocalMapper;
import com.jnj.gt.model.JnJGTIntProductLocalModel;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;


public class DefaultJnjGTIntProductLocalFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntProductLocalFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	@Autowired
	private JnjGTProductLocalMapper jnjGTProductLocalMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_LOCAL + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final List<JnJGTIntProductLocalModel> invalidIntProdLocalRecords = (List<JnJGTIntProductLocalModel>) jnjGTFeedService
				.getRecordsByStatus(JnJGTIntProductLocalModel._TYPECODE, recordStatus, selectionDate);

		final List<JnJGTIntProductLocalPlantModel> invalidIntProdLocalPlantRecords = (List<JnJGTIntProductLocalPlantModel>) jnjGTFeedService
				.getRecordsByStatus(JnJGTIntProductLocalPlantModel._TYPECODE, recordStatus, selectionDate);

		jnjGTFeedService.invalidateRecords(invalidIntProdLocalRecords);
		jnjGTFeedService.invalidateRecords(invalidIntProdLocalPlantRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_LOCAL + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_LOCAL + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); }
		 *//** After cleanup, mark the valid records status to PENDING **/
		/*
		 * jnjGTFeedService.updateIntRecordStatus(JnJNAIntProductLocalModel._TYPECODE);
		 * jnjGTFeedService.updateIntRecordStatus(JnJNAIntProductLocalPlantModel._TYPECODE);
		 * 
		 * jnjGTProductLocalMapper.processIntermediateRecords();
		 * 
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_LOCAL + Logging.HYPHEN + "cleanInvalidRecords()" +
		 * Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

}
