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
package com.jnj.la.dataload;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.mapper.JnjTranslationMapper;
import com.jnj.la.dataload.services.JnjLoadTranslationDataLoadService;


/**
 *
 */
public class JnjTranslationDataLoad
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjTranslationDataLoad.class);

	/** The Constant LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT. */
	private static final String LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA = "getTranslationData()";

	@Autowired
	protected JnjLoadTranslationDataLoadService jnjLoadTranslationDataLoadService;

	@Autowired
	private JnjTranslationMapper jnjTranslationMapper;


	public void getTranslationData(final JnjIntegrationRSACronJobModel cronJobModel) throws NumberFormatException, BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA,
				Logging.BEGIN_OF_METHOD, JnjTranslationDataLoad.class);


		LOGGER.info("In JnjTranslationDataLoad method############");


		jnjTranslationMapper.processTranslations(jnjLoadTranslationDataLoadService.getTranslationDataService(cronJobModel),
				cronJobModel);


		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA,
				Logging.END_OF_METHOD, JnjTranslationDataLoad.class);

	}

}
