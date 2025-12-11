/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.ImpexTransformerTask;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjImpexTransformerTask extends ImpexTransformerTask
{
	protected static final Logger LOGGER = Logger.getLogger(JnjImpexTransformerTask.class);
	protected JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;
	protected static final Integer FILE_NAME_POSITION = Integer.valueOf(26);
	protected static final String DATE_SEPARATOR = "_";

	protected static final String CONVERT_FILE_METHOD_NAME = "convertFIle()";


	@Override
	public BatchHeader execute(final BatchHeader header) throws UnsupportedEncodingException, FileNotFoundException
	{
		getCleanupHelper().setInitialTimeStamp();
		return super.execute(header);
	}

	@Override
	protected boolean convertFile(final BatchHeader header, final File file, final File impexFile, final ImpexConverter converter)
			throws UnsupportedEncodingException, FileNotFoundException
	{
		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + CONVERT_FILE_METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis());

		boolean result = false;
		CSVReader csvReader = null;
		PrintWriter writer = null;
		PrintWriter errorWriter = null;
		try
		{
			csvReader = createCsvReader(file);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(impexFile), getEncoding())));
			writer.println(getReplacedHeader(header, converter));
			while (csvReader.readNextLine())
			{
				final Map<Integer, String> row = csvReader.getLine();
				if (converter.filter(row))
				{
					try
					{
						final String fileName = row.get(FILE_NAME_POSITION);
						if (fileName == null || fileName.isEmpty())
						{
							row.put(FILE_NAME_POSITION, file.getName() + DATE_SEPARATOR + getCleanupHelper().getInitialTimeStamp());
						}

						writer.println(converter.convert(row, header.getSequenceId()));
						result = true;
					}
					catch (final IllegalArgumentException exc)
					{
						LOGGER.error("IMPEX Transformation for the file: " + file.getName() + ", has failed. Error Message: "
								+ exc.getMessage());
						getCleanupHelper().cleanup(header, true);
						handleIntefaceReadException(impexFile, exc.getMessage());
						errorWriter = writeErrorLine(file, csvReader, errorWriter, exc);
					}
				}
			}
		}
		finally
		{
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(errorWriter);
			closeQuietly(csvReader);
		}

		LOGGER.debug(Logging.IMPEX_IMPORT + Logging.HYPHEN + CONVERT_FILE_METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis());

		return result;
	}

	/**
	 * Handles the error scenario while transforming CSV to Impex, by:
	 * <ul>
	 * <li>Appending file name with the current system time stamp.</li>
	 * <li>Update Read Dashboard sending all required parameters.</li>
	 * </ul>
	 * 
	 * @param fileName
	 * @param errorMessage
	 */
	protected void handleIntefaceReadException(final File file, final String errorMessage)
	{
		final File errorFile = getCleanupHelper().getDestFile(file, false);

		jnjInterfaceOperationArchUtility.updateReadDashboard(Logging.IMPEX_IMPORT, errorFile.getName(),
				RecordStatus.ERROR.toString(), false, errorMessage);
	}

	@Override
	protected JnjCleanupHelper getCleanupHelper()
	{
		return (JnjCleanupHelper) super.getCleanupHelper();
	}

	public JnjInterfaceOperationArchUtility getJnjInterfaceOperationArchUtility()
	{
		return jnjInterfaceOperationArchUtility;
	}

	public void setJnjInterfaceOperationArchUtility(final JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility)
	{
		this.jnjInterfaceOperationArchUtility = jnjInterfaceOperationArchUtility;
	}
}
