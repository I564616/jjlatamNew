/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.job;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.JnjContractDataLoad;

public class JnjLoadContractsJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	/** The jnj contract data load. */
	@Autowired
	private JnjContractDataLoad jnjContractDataLoad;

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjLoadContractsJob.class);

	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel arg0)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.FEED_CONTRACTS_JOB_METHOD_NAME + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			jnjContractDataLoad.getLoadContract(arg0);
		}
		catch (final Exception throwable)
		{
			LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION + throwable
					+ Logging.HYPHEN + throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB_METHOD_NAME + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
