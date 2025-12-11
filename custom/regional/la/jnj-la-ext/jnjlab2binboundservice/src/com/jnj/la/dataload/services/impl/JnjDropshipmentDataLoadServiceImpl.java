/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.dataload.services.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import com.jnj.la.dataload.dao.JnjDropshipmentDataLoadDao;
import com.jnj.la.dataload.services.JnjDropshipmentDataLoadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;


public class JnjDropshipmentDataLoadServiceImpl implements JnjDropshipmentDataLoadService
{
	@Autowired
	protected JnjDropshipmentDataLoadDao jnjDropshipmentDataLoadDao;

	@Override
	public List<JnjDropshipmentDTO> fetchDropShipmentRecords(final JnjIntegrationRSACronJobModel jobModel) throws BusinessException
	{
		final String METHOD_NAME = "fetchDropShipmentRecords()";
		List<JnjDropshipmentDTO> listOfDropshipmentDTO;
		try
		{
			listOfDropshipmentDTO = jnjDropshipmentDataLoadDao.fetchDropShipmentRecords(jobModel);
		}
		catch (final SQLException ex)
		{
			JnjGTCoreUtil.logErrorMessage("JnjDropshipmentDataLoadServiceImpl" + Logging.HYPHEN, METHOD_NAME,
					Logging.HYPHEN + "Exception Occurred" + ex, JnjDropshipmentDataLoadServiceImpl.class);
			throw new BusinessException(ex.getMessage());
		}
		return listOfDropshipmentDTO;
	}

}
