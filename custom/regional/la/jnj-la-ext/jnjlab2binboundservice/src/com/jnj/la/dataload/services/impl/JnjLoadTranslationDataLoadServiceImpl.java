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

import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjTranslationDTO;
import com.jnj.la.dataload.dao.JnjLoadTranslationDao;
import com.jnj.la.dataload.services.JnjLoadTranslationDataLoadService;


/**
 *
 */
public class JnjLoadTranslationDataLoadServiceImpl implements JnjLoadTranslationDataLoadService
{

	/** The Constant LOAD_TRANSLATION_INTERFACE_METHOD_GET_CONTRACT. */
	private static final String LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA = "getTranslationDataService()";

	@Autowired
	private JnjLoadTranslationDao jnjLoadTranslationDao;

	@Override
	public List<JnjTranslationDTO> getTranslationDataService(final JnjIntegrationRSACronJobModel cronJobModel)
			throws NumberFormatException, BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA,
				Logging.BEGIN_OF_METHOD, JnjLoadTranslationDataLoadServiceImpl.class);


		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA,
				Logging.END_OF_METHOD, JnjLoadTranslationDataLoadServiceImpl.class);

		return (jnjLoadTranslationDao.getTranslationDataFetch(cronJobModel));
	}

}
