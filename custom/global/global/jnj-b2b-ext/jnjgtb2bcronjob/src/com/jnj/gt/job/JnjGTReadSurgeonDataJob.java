/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Surgeon;
import com.jnj.gt.model.JnjGTIntSurgeonModel;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * Cron-job responsible to:
 * 
 * <ul>
 * <li>Clean up existing obsolete 'LOADING' records from <code>JnjGTIntSurgeon</code></li>
 * <li>Pick up the Surgeon in-bound files based on a prefix at a specified location and rename them to hot-folder based
 * convention.</li>
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTReadSurgeonDataJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTReadSurgeonDataJob.class);

	/**
	 * The Constant for .csv
	 */
	private static final String CSV_FILE_FORMAT = ".csv";

	/**
	 * The Constant value for file prefix.
	 */
	private static final String SURGEON_INBOUND_FILE_PREFIX = "/surgeonInbound";

	/**
	 * The Constant for hybphen symbol.
	 */
	private static final String SYMBOL_HYPHEN = "-";

	/**
	 * The counter used for renaming in-bound files.
	 */
	private int csvFileCounter;

	/**
	 * The private Instance of <code>JnjConfigServiceImpl</code>.
	 */
	@Autowired
	protected JnjConfigServiceImpl jnjConfigService;

	/**
	 * The private Instance of <code>JnjGTFeedService</code>.
	 */
	@Autowired
	protected JnjGTFeedService jnjGTFeedService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}

		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		csvFileCounter = Integer.parseInt(jnjConfigService
				.getConfigValueById(Jnjb2bCoreConstants.Surgeon.SURGEON_CSV_FILE_COUNTER_KEY));

		try
		{
			/*
			 * Delete existing obsolete records from the intermediate table.
			 */
			final List<JnjGTIntSurgeonModel> invalidIntProdLocalRecords = (List<JnjGTIntSurgeonModel>) jnjGTFeedService
					.getRecordsByStatus(JnjGTIntSurgeonModel._TYPECODE, RecordStatus.LOADING);
			jnjGTFeedService.invalidateRecords(invalidIntProdLocalRecords);

			/*
			 * Rename the in-bound files present at the specified location so that would get picked by Hot-folder after
			 * rename.
			 */
			final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.HOT_FOLDER_SURGEON_INBOUND_ROOT_PATH);
			final List<File> fileList = JnJXMLFilePicker.pickAndSortFilesForHotFolder(hotFolderBaseLocation);
			final String prefix = Config.getParameter(Jnjb2bCoreConstants.Surgeon.SURGEON_INBOUND_FILE_PREFIX_KEY);

			for (final File file : fileList)
			{
				if (file.getName().startsWith(prefix))
				{
					LOGGER.info(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: " + file.getName());
					deleteFooterText(file);
					file.renameTo(new File(hotFolderBaseLocation + SURGEON_INBOUND_FILE_PREFIX + SYMBOL_HYPHEN + (csvFileCounter++)
							+ CSV_FILE_FORMAT));
				}

			}
		}
		catch (final Exception exception)
		{
			LOGGER.error(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + "Renaming files has caused an exception: "
					+ exception.getMessage());

			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		jnjConfigService.saveConfigValues(Jnjb2bCoreConstants.Surgeon.SURGEON_CSV_FILE_COUNTER_KEY, String.valueOf(csvFileCounter));

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	/**
	 * Deletes footer text if the last line starts with 'FILEFOOTER', to avoid any bad data import.
	 * 
	 * @param file
	 */
	protected void deleteFooterText(final File file)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + "deleteFooterText()" + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		LineNumberReader lineNumberReader = null;
		RandomAccessFile accessFile = null;
		FileReader fileReader = null;
		final String foterText = Config.getParameter(Surgeon.SURGEON_FILE_FOOTER_TEXT_KEY);

		try
		{
			fileReader = new FileReader(file);
			lineNumberReader = new LineNumberReader(fileReader);
			accessFile = new RandomAccessFile(file, "rw");
			final StringBuilder lastLine = new StringBuilder();

			long length = accessFile.length() - 1;
			byte currentByte;
			do
			{
				length -= 1;
				accessFile.seek(length);
				currentByte = accessFile.readByte();
				lastLine.append((char) currentByte);
			}
			while (currentByte != 10);

			if (lastLine.reverse().toString().trim().startsWith(foterText))
			{
				LOGGER.info("Last line matched with the footer text 'FILEFOOTER', deleting the last line.");
				accessFile.setLength(length + 1);
			}
			else
			{
				LOGGER.info("No last line matched with the footer text 'FILEFOOTER', continuing rename.");
			}

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.RENAME_SURGEON_INBOUND_FILES + Logging.HYPHEN + "deleteFooterText()" + Logging.END_OF_METHOD
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}

		}
		catch (final IOException exception)
		{
			LOGGER.error(exception.getMessage());
		}
		finally
		{
			try
			{
				if (fileReader != null)
				{
					fileReader.close();
				}

				if (lineNumberReader != null)
				{
					lineNumberReader.close();
				}

				if (accessFile != null)
				{
					accessFile.close();
				}
			}
			catch (final IOException exception)
			{
				LOGGER.error("Exception while closing connections", exception);
			}
		}
	}

}
