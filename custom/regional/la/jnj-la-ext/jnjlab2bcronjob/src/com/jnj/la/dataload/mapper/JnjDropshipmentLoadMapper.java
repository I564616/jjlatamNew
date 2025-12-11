/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload.mapper;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import com.jnj.la.core.services.dropshipment.JnjDropshipmentService;


public class JnjDropshipmentLoadMapper
{
	private static final String INSERT_ACTION = "I";
	private static final String MODIFY_ACTION = "M";
	private static final String DELETE_ACTION = "D";

	@Autowired
	protected JnjDropshipmentService jnjDropshipmentService;


	public boolean actOnDropshipmentProduct(final JnjDropshipmentDTO jnjDropshipmentDTO)
			throws BusinessException
	{
		boolean recordStatus = false;
		final String METHOD_NAME = "actOnDropshipmentProduct";

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
				Logging.HYPHEN+ Logging.BEGIN_OF_METHOD, JnjDropshipmentLoadMapper.class);

		if (jnjDropshipmentDTO.getOperation() != null && jnjDropshipmentDTO != null)
		{
			if (INSERT_ACTION.equalsIgnoreCase(jnjDropshipmentDTO.getOperation()) || MODIFY_ACTION.equalsIgnoreCase(jnjDropshipmentDTO.getOperation()))
			{
				recordStatus = jnjDropshipmentService.insertUpdateDropShipmentDetails(jnjDropshipmentDTO);
			}
			else if (DELETE_ACTION.equalsIgnoreCase(jnjDropshipmentDTO.getOperation()))
			{
				recordStatus = jnjDropshipmentService.deleteDropshipmentProductDetails(jnjDropshipmentDTO);
			} else {
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
						"Drop Shipment Record with ECARE ID:" + jnjDropshipmentDTO.getEcareId()
								+ " has operation equal to: " +jnjDropshipmentDTO.getOperation() + " not valid!",
						JnjDropshipmentLoadMapper.class);
			}
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
					"Drop Shipment Record with ECARE ID:" + jnjDropshipmentDTO.getEcareId()
							+ " has operation equals to NULL!",	JnjDropshipmentLoadMapper.class);
		}

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
				Logging.END_OF_METHOD,	JnjDropshipmentLoadMapper.class);

		return recordStatus;
	}


	/**
	 *
	 * @param jnjDropshipmentDTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateMainTbl(final JnjDropshipmentDTO jnjDropshipmentDTO) throws BusinessException
	{
		boolean recordStatus;
		final String METHOD_NAME  = "updateMainTbl";

		try
		{
				recordStatus = actOnDropshipmentProduct(jnjDropshipmentDTO);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
					"Error occurred while saving Drop Shipment Model with ECARE ID: " + jnjDropshipmentDTO.getEcareId()
					+ exception,
					JnjDropshipmentLoadMapper.class);
			recordStatus = false;
		}

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB, METHOD_NAME,
				Logging.END_OF_METHOD,	JnjDropshipmentLoadMapper.class);

		return recordStatus;
	}

}
