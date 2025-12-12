/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.JnJ.common.logging.MessageLoggerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.FileUploadDTO;
import java.util.function.Predicate;




/**
 * This is a util class which is used for uploading a file in shared Folder.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjFileUploadToSharedFolderUtil
{
	protected static final String CLASS_NAME = JnjFileUploadToSharedFolderUtil.class.getName();
	private static final Logger LOG = Logger.getLogger(JnjFileUploadToSharedFolderUtil.class);

	/**
	 * Upload file to shared folder.
	 * 
	 * @param fileUploadDTO
	 *           the file upload dto
	 * @param sharedfolder
	 *           the sharedfolder
	 * @return true, if successful
	 */
	public boolean uploadFileToSharedFolder(final FileUploadDTO fileUploadDTO, final String sharedfolder)
	{
		final String METHOD_NAME = Jnjb2bCoreConstants.DocumenTransfer.METHOD_NAME_UPLOAD_FILE;
		BufferedOutputStream buffOut = null;
		FileOutputStream outputStream = null;

		final String finalsharedfolder = sharedfolder + Jnjb2bCoreConstants.DocumenTransfer.DELIMITER;
		final String fileName = StringUtils.isNotEmpty(fileUploadDTO.getName()) ? fileUploadDTO.getName() : fileUploadDTO.getFile()
				.getName();
		final String filePath = (fileUploadDTO.getRenameFileTo() != null ? finalsharedfolder + fileUploadDTO.getRenameFileTo()
				: finalsharedfolder + fileName);
		fileUploadDTO.setFilePathInShareFolder(filePath);
		fileUploadDTO.setFileNameInShareFolder(fileUploadDTO.getRenameFileTo());
		try
		{
			outputStream = new FileOutputStream(new File(filePath));
			buffOut = new BufferedOutputStream(outputStream);
			buffOut.write(fileUploadDTO.getFile().getBytes());
		}
		catch (final IOException e)
		{
			LOG.error(MessageLoggerHelper.buildErrorMessage("userId", "transactionId", "code", " File Could Not Be  Uploaded",
					CLASS_NAME, METHOD_NAME));
			return false;
		}
		finally
		{
			try
			{
				if (null != buffOut)
				{
					buffOut.close();
				}
				if (null != outputStream)
				{
					outputStream.close();
				}
			}
			catch (final IOException e)
			{
				LOG.error(MessageLoggerHelper.buildErrorMessage("userId", "transactionId", "code", " File Could Not Be  Uploaded",
						CLASS_NAME, METHOD_NAME));
				return false;
			}
		}
		return true;
	}

	public static String changeFileName(final String existingName, final String newName)
	{
		final String fileExtension = getFileExtension(existingName);
		if (null != fileExtension)
		{
			return newName.concat(fileExtension);
		}
		else
		{
			return newName;
		}
	}

	/**
	 * Gets the file extension.
	 * 
	 * @param fileName
	 *           the file name
	 * @return the file extension
	 */
	public static String getFileExtension(final String fileName)
	{
		String extension = null;
		if (null != fileName && fileName.contains(Jnjb2bCoreConstants.CONST_DOT))
		{
			extension = fileName.substring(fileName.lastIndexOf(Jnjb2bCoreConstants.CONST_DOT));
		}
		return extension;
	}

	/**
	 * Rename file.
	 * 
	 * @param oldName
	 *           the old name
	 * @param newName
	 *           the new name
	 */
	public static void renameFile(final String oldName, final String newName)
	{
		final String METHOD_NAME = "renameFile ()";
		final File srcFile = new File(oldName);
		boolean bSucceeded = false;
		try
		{
			final File destFile = new File(newName);
			if (!destFile.getParentFile().exists())
			{
				destFile.getParentFile().mkdirs();
			}
			if (destFile.exists())
			{
				if (!destFile.delete())
				{
					LOG.error("File Could Not Be  deleted," + CLASS_NAME + " : " + METHOD_NAME);
				}
			}
			if (!srcFile.renameTo(destFile))
			{
				LOG.error("File was not successfully final renamed to" + CLASS_NAME + " : " + METHOD_NAME);
			}
			else
			{
				bSucceeded = true;
			}
		}
		finally
		{
			if (bSucceeded)
			{
				srcFile.delete();
			}
		}
	}
}
