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

import com.jnj.core.services.customer.JnjGTCustomerService;


/**
 * YTODO <<Replace this line with the class description>>
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCutNotificationEmailJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(JnjGTCutNotificationEmailJob.class);

	private JnjGTCustomerService customerService;

	/**
	 * This method performs the job for sending cut notification e-mails
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		LOG.debug("START : Performing JnjGTCutNotificationEmailJob");
		customerService.sendCutNotificationEmail();
		LOG.debug("END : JnjGTCutNotificationEmailJob Complete");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @return the customerService
	 */
	public JnjGTCustomerService getCustomerService()
	{
		return customerService;
	}

	/**
	 * @param customerService
	 *           the customerService to set
	 */
	public void setCustomerService(final JnjGTCustomerService customerService)
	{
		this.customerService = customerService;
	}
}
