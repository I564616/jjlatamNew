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
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;


/**
 * This Utility handles Operations Related to Zips.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjZipFileUtil
{

	private static final Logger LOGGER = Logger.getLogger(JnjZipFileUtil.class);

	/** The Constant BUFFER_SIZE. */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extract file.
	 * 
	 * @param in
	 *           the in
	 * @param outdir
	 *           the outdir
	 * @param name
	 *           the name
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	private static void extractFile(final ZipInputStream in, final File outdir, final String name) throws IOException
	{
		final byte[] buffer = new byte[BUFFER_SIZE];
		final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(outdir, name)));
		int len;
		while ((len = in.read(buffer)) > 0)
		{
			bufferedOutputStream.write(buffer, 0, len);
		}
		bufferedOutputStream.close();
	}

	/**
	 * This method is used to create the Directory structure.
	 * 
	 * @param outdir
	 *           the outdir
	 * @param path
	 *           the path
	 */
	private static void mkdirs(final File outdir, final String path)
	{
		final File file = new File(outdir, path);
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	/**
	 * Dirpart is used to if the the File entry is coming before the DIR entry.
	 * 
	 * @param name
	 *           the name
	 * @return the string
	 */
	private static String dirpart(final String name)
	{
		final int indexOfFileSeperator = name.lastIndexOf(File.separatorChar);
		return indexOfFileSeperator == -1 ? null : name.substring(0, indexOfFileSeperator);
	}

	/**
	 * * Extract zipfile to outdir with complete directory structure.
	 * 
	 * @param inputStream
	 *           the input stream
	 * @param outdir
	 *           Output directory
	 * @throws BusinessException
	 *            the business exception
	 */
	public static void extractZip(final InputStream inputStream, final File outdir) throws BusinessException
	{
		final String METHOD_EXTRACT_ZIP = "extractZip()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LAUDO + Logging.HYPHEN + METHOD_EXTRACT_ZIP + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		try
		{
			final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			ZipEntry zipEntry;
			String entryName, entryDir = null;
			while ((zipEntry = zipInputStream.getNextEntry()) != null)
			{
				entryName = zipEntry.getName();
				if (zipEntry.isDirectory())//Check if the Entry is a file or DIR, If a DIR create new DIR and continue.
				{
					mkdirs(outdir, entryName);
					continue;
				}

				//DIRPART is necessary as the files may come as entries before the directory. 
				entryDir = dirpart(entryName);
				if (entryDir != null)
				{
					mkdirs(outdir, entryDir);
				}
				extractFile(zipInputStream, outdir, entryName);//Extract entry to file.
			}
			zipInputStream.close();
		}
		catch (final IOException iOException)
		{
			LOGGER.error(Logging.LAUDO + Logging.HYPHEN + METHOD_EXTRACT_ZIP + Logging.HYPHEN
					+ "IOException while Unzipping the file. Cascading to BusinessException. " + iOException);
			throw new BusinessException("IOException while Unzipping the file.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LAUDO + Logging.HYPHEN + METHOD_EXTRACT_ZIP + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}
}
