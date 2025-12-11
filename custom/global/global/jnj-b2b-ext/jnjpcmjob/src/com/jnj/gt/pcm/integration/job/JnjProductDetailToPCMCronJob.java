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
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.pcm.integration.constants.JnjpcmjobConstants.Logging;
import com.jnj.gt.pcm.integration.facade.JnjCCP360IntegrationFacade;


/**
 * This class is the executable file for Generate and update CSV file for P360 Job
 */
public class JnjProductDetailToPCMCronJob extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOGGER = Logger.getLogger(JnjProductDetailToPCMCronJob.class);

	private static final String PERFORM_STR = "perform()";

	private JnjCCP360IntegrationFacade jnjCCP360IntegrationFacade;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_DETAIL_TO_PCM + Logging.HYPHEN + PERFORM_STR + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final boolean uploadfile = jnjCCP360IntegrationFacade.uploadProductDetailFile();

		if (uploadfile)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

	}

	public JnjCCP360IntegrationFacade getJnjCCP360IntegrationFacade()
	{
		return jnjCCP360IntegrationFacade;
	}

	public void setJnjCCP360IntegrationFacade(final JnjCCP360IntegrationFacade jnjCCP360IntegrationFacade)
	{
		this.jnjCCP360IntegrationFacade = jnjCCP360IntegrationFacade;
	}


}
