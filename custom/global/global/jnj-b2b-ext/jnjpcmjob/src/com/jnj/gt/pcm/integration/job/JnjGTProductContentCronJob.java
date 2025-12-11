/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.facade.JnjGTProductContentFacade;


/**
 * This class is the executable file for FetchImage Job
 */
public class JnjGTProductContentCronJob extends AbstractJobPerformable<JnjGTPCMIntegrationCronJobModel>
{


	private static final Logger LOG = Logger.getLogger(JnjGTProductContentCronJob.class);
	private JnjGTProductContentFacade jnjGTProductContentFacade;
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel)
	 */
	@Override
	public PerformResult perform(final JnjGTPCMIntegrationCronJobModel jobModel)
	{


		PerformResult result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		try
		{
			if (jnjGTProductContentFacade.getProductData(jobModel))
			{
				result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while running productContentCronjob, error message: {}", e);
		}
		return result;

	}

	public JnjGTProductContentFacade getJnjGTProductContentFacade()
	{
		return jnjGTProductContentFacade;
	}

	public void setJnjGTProductContentFacade(final JnjGTProductContentFacade jnjGTProductContentFacade)
	{
		this.jnjGTProductContentFacade = jnjGTProductContentFacade;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
