/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.dataload.JnjInvoiceDataLoad;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;


/**
 * This is the cronjob class for triggering the Load Invoices process
 *
 * @author Manoj.K.Panda
 *
 */
public class JnjLoadInvoicesJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	@Autowired
	private JnjInvoiceDataLoad jnjInvoiceDataLoad;

	/**
	 * This method is responsible for calling the JnjInvoiceDataLoad class for the Load Invoices process, whenever the
	 * cronjob is triggered.
	 */
	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String methodName = "perform()";
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_INVOICES_JOB, methodName, Logging.BEGIN_OF_METHOD, JnjLoadInvoicesJob.class);

		try {
			//Calling the method for loading invoice data into DTO objects
			jnjInvoiceDataLoad.getLoadInvoices(cronJobModel);
		} catch (final EmptyResultDataAccessException e) {
            JnjGTCoreUtil.logInfoMessage(Logging.LOAD_INVOICES_JOB, methodName, "No orders returned from RSA", JnjLoadInvoicesJob.class);
        } catch (final Exception e) {
			String message = "Error occurred while calling JnjInvoiceDataLoad class" + "-" + e.getLocalizedMessage() + ". Exception: " + e.getMessage();
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_INVOICES_JOB, methodName, message, e, JnjLoadInvoicesJob.class);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB_METHOD_NAME, methodName, Logging.END_OF_METHOD, JnjLoadInvoicesJob.class);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
