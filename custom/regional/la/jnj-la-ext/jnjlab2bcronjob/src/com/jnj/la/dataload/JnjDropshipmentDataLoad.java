/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.mapper.JnjDropshipmentLoadMapper;
import com.jnj.la.dataload.services.JnjDropshipmentDataLoadService;


public class JnjDropshipmentDataLoad
{

	private static final String METHOD_LOAD_DROPSHIPMENT = "getLoadDropshipment()";

	@Autowired
	private JnjDropshipmentLoadMapper jnjDropshipmentLoadMapper;

	@Autowired
	private JnjDropshipmentDataLoadService jnjDropshipmentDataLoadService;

	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;


	/**
	 * Gets the load dropshipment. The method gets a list of all the dropshipment Files.
	 *
	 * @throws BusinessException
	 *
	 */
	public void getLoadDropshipment(final JnjIntegrationRSACronJobModel jobModel) throws BusinessException
	{
		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_LOAD_DROPSHIPMENT, Logging.BEGIN_OF_METHOD,
				JnjDropshipmentDataLoad.class);

		final List<JnjDropshipmentDTO> listOfDropshipmentDTO = jnjDropshipmentDataLoadService.fetchDropShipmentRecords(jobModel);

		if (listOfDropshipmentDTO != null && !listOfDropshipmentDTO.isEmpty())
		{
			for (final JnjDropshipmentDTO record : listOfDropshipmentDTO)
			{
				if (jnjDropshipmentLoadMapper.updateMainTbl(record))
				{
					JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_LOAD_DROPSHIPMENT,
							" Record Processed Successfully, update timestamp in job", JnjDropshipmentDataLoad.class);
					interfaceOperationArchUtility.setLastSuccesfulStartTimeForJob(record.getLastUpdatedDate(), jobModel);
				}
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_LOAD_DROPSHIPMENT,
					"No Drop Shipment Records with status to be updated!",
					JnjDropshipmentDataLoad.class);
		}

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_LOAD_DROPSHIPMENT, Logging.END_OF_METHOD,
				JnjDropshipmentDataLoad.class);
	}

}
