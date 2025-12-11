/**
 * 
 */
package com.jnj.facades.cronjobs;

/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.customer.JnjCustomerFacade;


/**
 * The Class JnjLoadContractsJob. The class is a cronJob to for LoadContracts Interface.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjUserLoginCronJob extends AbstractJobPerformable<CronJobModel>
{

	/** The jnj contract data load. */
	@Autowired
	private JnjCustomerFacade jnjCustomerFacade;

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjUserLoginCronJob.class);

	/**
	*
	*/
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_USER_LOGIN_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.JNJ_USER_LOGIN_JOB + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			jnjCustomerFacade.sendAccountActiveEmail();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.JNJ_USER_LOGIN_JOB + Logging.HYPHEN + Logging.USER_LOGIN_JOB_METHOD_NAME + Logging.HYPHEN
					+ throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_USER_LOGIN_JOB + Logging.HYPHEN + Logging.USER_LOGIN_JOB_METHOD_NAME + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
}
