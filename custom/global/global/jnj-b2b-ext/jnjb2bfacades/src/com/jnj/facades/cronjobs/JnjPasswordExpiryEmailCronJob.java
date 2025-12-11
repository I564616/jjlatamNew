/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.cronjobs;

import jakarta.annotation.Resource;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.customer.JnjCustomerFacade;

/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPasswordExpiryEmailCronJob extends AbstractJobPerformable<CronJobModel>
{

	static final Logger LOGGER = Logger.getLogger(JnjPasswordExpiryEmailCronJob.class);

	@Resource(name="GTCustomerFacade")
	private JnjCustomerFacade jnjCustomerFacade;



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOGGER.debug("START: JnjPasswordExpiryEmailCronJob - sendPasswordExpiryMail");
		jnjCustomerFacade.sendPasswordExpiryMail();
		LOGGER.debug("END: JnjPasswordExpiryEmailCronJob - sendPasswordExpiryMail");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
}
