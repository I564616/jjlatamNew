/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.service.common.impl.DefaultJnjGTStgSerivce;
import com.jnj.gt.service.product.JnjGTUsPcmPrdExclService;


/**
 * The JnjGTUsPcmProdExclJob class is a cron job which runs on specific time at daily basis to update the pcm mod status
 * field and also implements the multi mod logic.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTUsPcmProdExclJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTUsPcmProdExclJob.class);


	@Autowired
	private DefaultJnjGTStgSerivce jnjGTStgSerivce;

	@Autowired
	private JnjGTUsPcmPrdExclService jnjGTUsPcmPrdExclService;

	/**
	 * This method is responsible for getting the impex and also for executing the code which updates pcm mod status
	 * field and also implements the multi mod logic.
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		try
		{
			// To make the pcm mod status not applicable which has first effective ship date greater than current date plus 30 days.
			jnjGTUsPcmPrdExclService.getExProductAttributeModels();

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("CronJob runs successfully");
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.START_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Business Exception occured " + businessException.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Throwable Exception occured " + throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}
}
