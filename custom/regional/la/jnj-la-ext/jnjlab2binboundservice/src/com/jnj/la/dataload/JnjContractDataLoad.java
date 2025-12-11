/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;

import java.util.ArrayList;
import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.email.notification.JnjEmailNotificationService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjContractDTO;
import com.jnj.la.core.services.contract.JnjContractService;
import com.jnj.la.dataload.mapper.JnjContractMapper;
import com.jnj.la.dataload.services.JnjContractDataLoadService;


/**
 * The Class JnjContractDataLoad is used to read and parse Contracts received from SAP.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjContractDataLoad
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjContractDataLoad.class);

	/** The Constant LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT. */
	private static final String LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT = "getLoadContract()";

	/** The jnj contract mapper. */
	@Autowired
	private JnjContractMapper jnjContractMapper;

	/** The jnj contract service. */
	@Autowired
	private JnjContractService jnjContractService;

	/** The jnj email notification service. */
	@Autowired
	private JnjEmailNotificationService jnjEmailNotificationService;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JNJRSADBConnector rsaDBConnector;

	@Autowired
	protected JnjContractDataLoadService jnjContractDataLoadService;

	/**
	 * Gets the load contract. The method gets a list of all the Load Contacts Files.
	 *
	 * @throws BusinessException
	 *
	 */
	public void getLoadContract(final JnjIntegrationRSACronJobModel jobmodel) throws BusinessException
	{
		final String METHOD_NAME = "getLoadContract()";
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT,
				Logging.BEGIN_OF_METHOD, JnjContractDataLoad.class);
		List<JnjContractDTO> listOfContracts = new ArrayList<JnjContractDTO>();

		try
		{
			listOfContracts = jnjContractDataLoadService.fetchLoadContractRecords(jobmodel);
		}
		catch (final Exception ex)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_CONTRACTS_INTERFACE, METHOD_NAME,
					"Exception occured while executing load contract cron job" + "-" + ex.getLocalizedMessage() + ". Exception: "
							+ ex.getMessage(),
					ex, JnjContractDataLoad.class);

			throw new BusinessException(ex.getMessage());
		}
		if (!listOfContracts.isEmpty())
		{
			jnjContractMapper.processContracts(listOfContracts, jobmodel);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT,
					Logging.LOAD_CONTRACTS_INTERFACE_NO_RECORD_FOUND, JnjContractDataLoad.class);
		}


		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_GET_CONTRACT,
				Logging.END_OF_METHOD, JnjContractDataLoad.class);
	}

}
