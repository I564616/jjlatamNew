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
package com.jnj.la.job;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.JnjTranslationDataLoad;


/**
 *
 */
public class JnjLoadTranslationJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	@Autowired
	private JnjTranslationDataLoad jnjTranslationDataLoad;

	private static final Logger LOG = Logger.getLogger(JnjLoadTranslationJob.class);

	/**
	 * This method is responsible for calling the jnJLoadTranslationXmlToJavaObjConverter class for the Load translation
	 * process, whenever the cronjob is triggered.
	 */
	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_TRANSLATION + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		try
		{
			jnjTranslationDataLoad.getTranslationData(cronJobModel);
		}

		catch (final Exception throwable)
		{
			LOG.error(Logging.LOAD_TRANSLATION + Logging.HYPHEN + Logging.FEED_TRANSLATION_JOB__METHOD_EXCEPTION + throwable
					+ Logging.HYPHEN + throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_TRANSLATION + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}



		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

}
