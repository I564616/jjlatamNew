/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.impex.impl;

import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.DefaultImportService;
import de.hybris.platform.servicelayer.impex.impl.ImportCronJobResult;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjSyncOrderImpExImportCronJobModel;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSyncOrderImportService extends DefaultImportService
{
	private static final Logger LOGGER = Logger.getLogger(JnjSyncOrderImportService.class);

	@Override
	public ImportResult importData(final ImportConfig config)
	{
		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + "importData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis());

		final ImpExImportCronJobModel cronJob = (JnjSyncOrderImpExImportCronJobModel) getModelService().create(
				"JnjSyncOrderImpExImportCronJob");

		try
		{
			getModelService().initDefaults(cronJob);
		}
		catch (final ModelInitializationException e)
		{
			throw new SystemException(e);
		}

		configureCronJob(cronJob, config);

		getModelService().saveAll(new Object[]
		{ cronJob.getJob(), cronJob });

		importData(cronJob, config.isSynchronous(), config.isRemoveOnSuccess());

		if (((Item) getModelService().getSource(cronJob)).isAlive())
		{
			return new ImportCronJobResult(cronJob);

		}

		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + "importData()" + Logging.HYPHEN + Logging.END_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis());

		return new ImportCronJobResult(null);
	}
}
