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
import com.jnj.gt.mapper.product.JnjGTProductLotMasterMapper;
import com.jnj.gt.model.JnjGTIntProductLotMasterModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;


public class DefaultJnjGTIntLotMasterFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntLotMasterFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	@Autowired
	private JnjGTProductLotMasterMapper JnjGTProductLotMasterMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final List<JnjGTIntProductLotMasterModel> invalidIntLotMasterRecords = (List<JnjGTIntProductLotMasterModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntProductLotMasterModel._TYPECODE, recordStatus, selectionDate);

		jnjGTFeedService.invalidateRecords(invalidIntLotMasterRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void processIntermediaryRecords()
	{		
		  if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.LOT_MASTER + Logging.HYPHEN +
		  "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		  + JnJCommonUtil.getCurrentDateTime()); }
		 /** After cleanup, mark the valid records status to PENDING **/
		
		 jnjGTFeedService.updateIntRecordStatus(JnjGTIntProductLotMasterModel._TYPECODE);
		  
		  JnjGTProductLotMasterMapper.processIntermediateRecords();
		  
		  if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.LOT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" +
		  Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME +
		  JnJCommonUtil.getCurrentDateTime()); }
		 
	}

}
