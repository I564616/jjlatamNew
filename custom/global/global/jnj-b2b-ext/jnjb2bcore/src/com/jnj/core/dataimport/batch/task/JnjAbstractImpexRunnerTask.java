/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public abstract class JnjAbstractImpexRunnerTask extends AbstractImpexRunnerTask
{
	protected static final Logger LOGGER = Logger.getLogger(JnjAbstractImpexRunnerTask.class);
	protected JnjCleanupHelper cleanupHelper;
	protected JnjInterfaceOperationArchUtility interfaceOperationArchUtility;
	protected static final String DATE_SEPARATOR = "_";

	/**
	 * 
	 */
	@Override
	public BatchHeader execute(final BatchHeader header) throws FileNotFoundException
	{
		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + "execute()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis());

		Assert.notNull(header, "must not be null");
		Assert.notNull(header.getEncoding(), "must not be null");
		boolean readSuccessful = false;
		if (CollectionUtils.isNotEmpty(header.getTransformedFiles()))
		{
			final Session localSession = getSessionService().createNewSession();
			try
			{
				for (final File file : header.getTransformedFiles())
				{
					LOGGER.info(Logging.IMPEX_IMPORT + Logging.HYPHEN + "Transformed file picked for import: " + file.getName());
					readSuccessful = processFile(header.getFile().getName(), file, header.getEncoding());
					cleanupHelper.cleanup(header, !readSuccessful);
				}
			}
			finally
			{
				getSessionService().closeSession(localSession);
			}
		}

		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + "execute()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
				+ System.currentTimeMillis());

		return header;
	}

	/**
	 * Process an impex file using the given encoding
	 * 
	 * @param file
	 * @param encoding
	 * @throws FileNotFoundException
	 */
	protected boolean processFile(final String csvFileName, final File file, final String encoding) throws FileNotFoundException
	{
		FileInputStream fis = null;
		boolean readSuccessful = true;
		try
		{
			fis = new FileInputStream(file);
			final ImportConfig config = getImportConfig();
			final ImpExResource resource = new StreamBasedImpExResource(fis, encoding);
			config.setScript(resource);
			final ImportResult importResult = getImportService().importData(config);
			if (importResult.isError() || importResult.hasUnresolvedLines())
			{
				readSuccessful = false;
				LOGGER.error(importResult.getUnresolvedLines().getPreview());
			}
		}
		catch (final Exception e)
		{
			LOGGER.error("Processing of transformed IMPEX file: " + csvFileName + " has failed. Error Message: " + e.getMessage());
			readSuccessful = false;
		}
		finally
		{
			updateReadDashboard(csvFileName + DATE_SEPARATOR + cleanupHelper.getInitialTimeStamp(), readSuccessful);
			IOUtils.closeQuietly(fis);
		}

		return readSuccessful;
	}

	protected void updateReadDashboard(final String fileName, final boolean readSuccessful)
	{
		getInterfaceOperationArchUtility().updateReadDashboard(Logging.IMPEX_IMPORT, fileName,
				(readSuccessful) ? RecordStatus.SUCCESS.toString() : RecordStatus.ERROR.toString(), readSuccessful, null);
	}

	public void setCleanupHelper(final JnjCleanupHelper cleanupHelper)
	{
		this.cleanupHelper = cleanupHelper;
	}

	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	public void setInterfaceOperationArchUtility(final JnjInterfaceOperationArchUtility interfaceOperationArchUtility)
	{
		this.interfaceOperationArchUtility = interfaceOperationArchUtility;
	}

	/**
	 * Lookup method to return the import config
	 * 
	 * @return import config
	 */
	@Override
	public abstract ImportConfig getImportConfig();


}
