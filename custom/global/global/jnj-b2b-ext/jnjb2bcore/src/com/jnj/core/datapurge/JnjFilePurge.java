/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.datapurge;

import de.hybris.platform.util.Config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjFilePurge
{
	/** The Constant LOG. */
	protected static final Logger LOGGER = Logger.getLogger(JnjFilePurge.class);

	@Autowired
	JnJCommonUtil jnjCommonUtil;

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * This method is the entry method which starts the purging of files which are no longer needed
	 * 
	 * @author balinder.singh
	 */
	public Boolean doFilePurge()
	{
		final Boolean SAP_file_purge = doFilePurgeForSAPOutput();
		final Boolean Hybris_file_purge = doFilePurgeForHybrisOutput();
		final Boolean Log_file_purge = doFilePurgeForLog();
		return (SAP_file_purge && Hybris_file_purge && Log_file_purge);
	}

	/**
	 * This method will purge files from the specified folder
	 * 
	 * @param folderName
	 */
	protected Boolean doFilePurgeByFolderName(final String folderName, final String filePrefixPattern)
	{
		final String METHOD_NAME = "doFilePurgeByFolderName ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.BEGIN_OF_METHOD + METHOD_NAME + "- purging folder - "
					+ folderName + " - " + JnJCommonUtil.getCurrentDateTime());
		}

		if (StringUtils.isNotEmpty(folderName))
		{
			try
			{
				jnjCommonUtil.purgeDirectory(new File(folderName), filePrefixPattern);
			}
			catch (final IOException e)
			{
				LOGGER.debug(Logging.FILE_PURGING_SAP + " - Error deleting files from folder " + folderName + " - "
						+ " doFilePurgeByFolderName() " + JnJCommonUtil.getCurrentDateTime());
				return Boolean.FALSE;
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.END_OF_METHOD + METHOD_NAME + JnJCommonUtil.getCurrentDateTime());
		}
		return Boolean.TRUE;
	}

	/**
	 * This method will purge SAP files
	 * 
	 */
	protected Boolean doFilePurgeForSAPOutput()
	{
		final String METHOD_NAME = "doFilePurgeForSAPOutput ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.BEGIN_OF_METHOD + METHOD_NAME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		Boolean returnVal = Boolean.TRUE;
		final String folderPrefix = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING_SAP_PURGE_PREFIX);
		final String sapFolders = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING_SAP_PURGE_FOLDERS);
		if (StringUtils.isNotEmpty(folderPrefix) && StringUtils.isNotEmpty(sapFolders))
		{
			for (final String folderName : StringUtils.split(sapFolders, Jnjb2bCoreConstants.CONST_COMMA))
			{
				if (!doFilePurgeByFolderName(folderPrefix + folderName, null).booleanValue())
				{
					returnVal = Boolean.FALSE;
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.END_OF_METHOD + METHOD_NAME + JnJCommonUtil.getCurrentDateTime());
		}
		return returnVal;
	}

	/**
	 * This method will purge Hybris files
	 * 
	 */
	protected Boolean doFilePurgeForHybrisOutput()
	{
		final String METHOD_NAME = "doFilePurgeForHybrisOutput ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.BEGIN_OF_METHOD + METHOD_NAME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		Boolean returnVal = Boolean.TRUE;
		final String folderPrefix = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_PREFIX);
		final String hybrisFolders = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING_HYBRIS_PURGE_FOLDERS);

		if (StringUtils.isNotEmpty(folderPrefix) && StringUtils.isNotEmpty(hybrisFolders))
		{
			for (final String folderName : StringUtils.split(hybrisFolders, Jnjb2bCoreConstants.CONST_COMMA))
			{
				if (!doFilePurgeByFolderName(folderPrefix + folderName, null).booleanValue())
				{
					returnVal = Boolean.FALSE;
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.END_OF_METHOD + METHOD_NAME + JnJCommonUtil.getCurrentDateTime());
		}
		return returnVal;
	}

	/**
	 * This method will purge Log files
	 * 
	 */
	protected Boolean doFilePurgeForLog()
	{
		final String METHOD_NAME = "doFilePurgeForLog ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.BEGIN_OF_METHOD + METHOD_NAME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		Boolean returnVal = Boolean.TRUE;
		final String hybrisLogFolders = Config.getParameter(Jnjb2bCoreConstants.FILEPATH_HYBRIS_LOG_PURGE_FOLDERS);
		final String jnjLogFolder = Config.getParameter(Jnjb2bCoreConstants.FILEPATH_JNJ_LOG_PURGE_FOLDER);
		final String jnjLogFilePattern = Config.getParameter(Jnjb2bCoreConstants.FILEPATH_JNJ_LOG_PURGE_FILE_PATTERN);
		if (StringUtils.isNotEmpty(hybrisLogFolders))
		{
			for (final String folderName : StringUtils.split(hybrisLogFolders, Jnjb2bCoreConstants.CONST_COMMA))
			{
				if (!doFilePurgeByFolderName(folderName, null).booleanValue())
				{
					returnVal = Boolean.FALSE;
				}
			}
		}

		if (StringUtils.isNotEmpty(jnjLogFolder))
		{
			if (!doFilePurgeByFolderName(jnjLogFolder, jnjLogFilePattern).booleanValue())
			{
				returnVal = Boolean.FALSE;
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PURGING_SAP + " - " + Logging.END_OF_METHOD + METHOD_NAME + JnJCommonUtil.getCurrentDateTime());
		}
		return returnVal;
	}

}
