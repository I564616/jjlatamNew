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

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjContractDTO;
import com.jnj.la.dataload.dao.JnjContractDataLoadDao;
import com.jnj.la.dataload.services.JnjContractDataLoadService;


public class JnjContractDataLoadServiceImpl implements JnjContractDataLoadService {

	@Autowired
	protected JnjContractDataLoadDao jnjContractDataLoadDao;

	@Override
	public List<JnjContractDTO> fetchLoadContractRecords(final JnjIntegrationRSACronJobModel jobmodel) throws BusinessException {
		final String METHOD_NAME = "fetchLoadContractRecords()";
		try {
			return jnjContractDataLoadDao.fetchLoadContractRecords(jobmodel);
		}
		catch (final SQLException ex) {
			JnjGTCoreUtil.logErrorMessage("JnjContractDataLoadServiceImpl" + Logging.HYPHEN, METHOD_NAME,
					Logging.HYPHEN + "Exception Occurred" + ex, JnjContractDataLoadServiceImpl.class);
			throw new BusinessException(ex.getMessage());
		}
	}

}
