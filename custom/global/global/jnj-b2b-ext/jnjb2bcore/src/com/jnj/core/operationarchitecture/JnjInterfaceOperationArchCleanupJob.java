/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.operationarchitecture;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjInterfaceOperationArchCleanupJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjInterfaceOperationArchCleanupJob.class);

	private JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	/**
	 * 
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			jnjInterfaceOperationArchUtility.cleanIntermediaryTableRecords();
			jnjInterfaceOperationArchUtility.sendEmailNotification();
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage());
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return result;
	}

	public void setJnjInterfaceOperationArchUtility(final JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility)
	{
		this.jnjInterfaceOperationArchUtility = jnjInterfaceOperationArchUtility;
	}


}
