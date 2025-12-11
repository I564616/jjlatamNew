/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataimport.batch.task;


import de.hybris.platform.acceleratorservices.dataimport.batch.task.CleanupHelper;
import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCleanupHelper extends CleanupHelper
{
	protected static final Logger LOGGER = Logger.getLogger(JnjCleanupHelper.class);
	protected static final String DATE_SEPARATOR = "_";
	protected static final String SET_INITIAL_TIME_STAMP_MESSAGE = "Initial Time Stamp has been set as: ";
	protected static final String SET_TIME_STAMP_METHOD_NAME = "setInitialTimeStamp()";

	protected static String initialTimeStamp;

	@Override
	protected File getDestFile(final File file, final boolean error)
	{
		final StringBuilder builder = new StringBuilder(file.getName());
		if (!StringUtils.isBlank(getTimeStampFormat()))
		{
			builder.append(DATE_SEPARATOR);
			builder.append(getInitialTimeStamp());
		}
		return new File(error ? BatchDirectoryUtils.getRelativeErrorDirectory(file)
				: BatchDirectoryUtils.getRelativeArchiveDirectory(file), builder.toString());
	}

	/**
	 * @return the initialTimeStamp
	 */
	public String getInitialTimeStamp()
	{
		return initialTimeStamp;
	}

	public void setInitialTimeStamp()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + SET_TIME_STAMP_METHOD_NAME + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final SimpleDateFormat sdf = new SimpleDateFormat(getTimeStampFormat(), Locale.getDefault());
		initialTimeStamp = sdf.format(new Date());
		LOGGER.info(Logging.IMPEX_IMPORT + SET_INITIAL_TIME_STAMP_MESSAGE + initialTimeStamp);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + SET_TIME_STAMP_METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}
}
