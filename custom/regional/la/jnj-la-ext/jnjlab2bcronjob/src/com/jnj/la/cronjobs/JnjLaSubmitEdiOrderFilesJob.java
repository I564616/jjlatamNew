/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.cronjobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.services.order.JnjLAOrderService;


/**
 * This is the cronjob class for sending received edi order files to SAP
 */
public class JnjLaSubmitEdiOrderFilesJob extends AbstractJobPerformable<CronJobModel>
{
	@Autowired
	private JnjLAOrderService jnjLaOrderService;

	private static final Class currentClass = JnjLaSubmitEdiOrderFilesJob.class;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		final String methodName = "perform()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_FILES_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		try
		{
			final List<File> uploadeFilesList = jnjLaOrderService.getUploadOrderFiles();
			if (uploadeFilesList != null && !uploadeFilesList.isEmpty())
			{
				jnjLaOrderService.processEdiFiles(uploadeFilesList);
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_FILES_INTERFACE, methodName,
						"There aren't new EDI files uploaded to be sent.", currentClass);
			}

		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_FILES_INTERFACE, methodName,
					"Error occurred while processing EDI files.", exception, currentClass);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_FILES_INTERFACE, methodName, Logging.END_OF_METHOD, currentClass);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
