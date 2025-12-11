/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2bcronjobConstants.OrderStatusInbound;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Order.StatusInbound;
import com.jnj.gt.model.JnjGTIntOrderHeaderModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.order.JnjGTOrderFeedService;



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
public class JnjGTReadOrderStatusInboundJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTReadOrderStatusInboundJob.class);

	/**
	 * The Constant for .csv
	 */
	private static final String CSV_FILE_FORMAT = ".csv";

	/**
	 * The Constant value for Order Header csv file prefix.
	 */
	private static final String ORDER_HEADER_INBOUND_CSV_FILE_PREFIX = "/sapOrderHeader";

	/**
	 * The Constant value for Order Line csv file prefix.
	 */
	private static final String ORDER_LINE_INBOUND_CSV_FILE_PREFIX = "/sapOrderLine";

	/**
	 * The Constant value for Order Schedule Line csv file prefix.
	 */
	private static final String ORDER_SCH_LINE_INBOUND_CSV_FILE_PREFIX = "/sapOrderSchLine";

	/**
	 * The Constant for hybphen symbol.
	 */
	private static final String SYMBOL_HYPHEN = "-";

	/**
	 * The counter used for renaming Order Header in-bound files.
	 */
	private int orderHeaderCsvCounter;

	/**
	 * The counter used for renaming Order Line in-bound files.
	 */
	private int orderLineCsvCounter;

	/**
	 * The counter used for renaming Order Schedule Line in-bound files.
	 */
	private int orderSchLineCsvCounter;

	/**
	 * The private Instance of <code>JnjConfigServiceImpl</code>.
	 */
	@Autowired
	private JnjConfigServiceImpl jnjConfigService;

	/**
	 * The private Instance of <code>JnjGTFeedService</code>.
	 */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * The private Instance of <code>JnjGTOrderFeedService</code>.
	 */
	@Autowired
	private JnjGTOrderFeedService jnjGTOrderFeedService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}

		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		orderHeaderCsvCounter = Integer.parseInt(jnjConfigService
				.getConfigValueById(StatusInbound.ORDER_HEADER_CSV_FILE_COUNTER_KEY));

		orderLineCsvCounter = Integer.parseInt(jnjConfigService.getConfigValueById(StatusInbound.ORDER_lINE_CSV_FILE_COUNTER_KEY));

		orderSchLineCsvCounter = Integer.parseInt(jnjConfigService
				.getConfigValueById(StatusInbound.ORDER_SCH_LINE_CSV_FILE_COUNTER_KEY));

		final Map<String, String> fileNameAndCounts = new HashMap<String, String>();
		final List<String> orderStatusInboundFileNames = new ArrayList<>();

		try
		{
			/*
			 * Delete existing obsolete records (CONSUMER ONLY) from the Header, Line and Schedule Line intermediate
			 * tables.
			 */
			final List<String> consumerSourceSystems = JnJCommonUtil.getValues(
					Jnjgtb2binboundserviceConstants.CONSUMER_USA_SOURCE_SYS_ID, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<JnjGTIntOrderHeaderModel> invalidIntOrderHeaderRecords = (List<JnjGTIntOrderHeaderModel>) jnjGTFeedService
					.getRecordsByStatusAndSourceSys(JnjGTIntOrderHeaderModel._TYPECODE, RecordStatus.LOADING, consumerSourceSystems);

			final Collection<ItemModel> invalidIntRecords = getIntermediateRecordsForCleanup(invalidIntOrderHeaderRecords);
			invalidIntRecords.addAll(invalidIntOrderHeaderRecords);
			jnjGTFeedService.invalidateRecords(invalidIntRecords);

			/*
			 * Rename the in-bound files present at the specified location so that would get picked by Hot-folder after
			 * rename.
			 */
			final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.HOT_FOLDER_ROOT_PATH);
			final List<File> fileList = JnJXMLFilePicker.pickAndSortFilesForHotFolder(hotFolderBaseLocation);

			final String headerPrefix = Config.getParameter(StatusInbound.ORDER_HEADER_INBOUND_FILE_PREFIX_KEY);
			final String linePrefix = Config.getParameter(StatusInbound.ORDER_LINE_INBOUND_FILE_PREFIX_KEY);
			final String schLinePrefix = Config.getParameter(StatusInbound.ORDER_SCH_LINE_INBOUND_FILE_PREFIX_KEY);

			for (final File file : fileList)
			{
				if (file.getName().startsWith(headerPrefix))
				{
					checkLinesCount(file, orderStatusInboundFileNames, fileNameAndCounts);
					LOGGER.info(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: "
							+ file.getName());
					file.renameTo(new File(hotFolderBaseLocation + ORDER_HEADER_INBOUND_CSV_FILE_PREFIX + SYMBOL_HYPHEN
							+ (orderHeaderCsvCounter++) + CSV_FILE_FORMAT));
				}
				else if (file.getName().startsWith(schLinePrefix))
				{
					checkLinesCount(file, orderStatusInboundFileNames, fileNameAndCounts);
					LOGGER.info(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: "
							+ file.getName());
					file.renameTo(new File(hotFolderBaseLocation + ORDER_SCH_LINE_INBOUND_CSV_FILE_PREFIX + SYMBOL_HYPHEN
							+ (orderSchLineCsvCounter++) + CSV_FILE_FORMAT));
				}
				else if (file.getName().startsWith(linePrefix))
				{
					checkLinesCount(file, orderStatusInboundFileNames, fileNameAndCounts);
					LOGGER.info(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: "
							+ file.getName());
					file.renameTo(new File(hotFolderBaseLocation + ORDER_LINE_INBOUND_CSV_FILE_PREFIX + SYMBOL_HYPHEN
							+ (orderLineCsvCounter++) + CSV_FILE_FORMAT));
				}
			}

			if (!orderStatusInboundFileNames.isEmpty())
			{
				LOGGER.info("Initiating Order Status File VAlidation Email Report for files: " + orderStatusInboundFileNames);
				jnjGTOrderFeedService.sendOrderStatusFileValidationEmail(orderStatusInboundFileNames, fileNameAndCounts);
			}
		}
		catch (final Exception exception)
		{
			LOGGER.error(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + "Renaming files has caused an exception: "
					+ exception.getMessage());

			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		/*
		 * Persist back the incremented/updated value of csv counters.
		 */
		jnjConfigService.saveConfigValues(StatusInbound.ORDER_HEADER_CSV_FILE_COUNTER_KEY, String.valueOf(orderHeaderCsvCounter));

		jnjConfigService.saveConfigValues(StatusInbound.ORDER_lINE_CSV_FILE_COUNTER_KEY, String.valueOf(orderLineCsvCounter));

		jnjConfigService
				.saveConfigValues(StatusInbound.ORDER_SCH_LINE_CSV_FILE_COUNTER_KEY, String.valueOf(orderSchLineCsvCounter));


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * 
	 * @param invalidIntOrderHeaderRecords
	 * @return Collection<ItemModel>
	 */
	private Collection<ItemModel> getIntermediateRecordsForCleanup(
			final List<JnjGTIntOrderHeaderModel> invalidIntOrderHeaderRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();
		String sapOrderNumber = null;
		String sourceSysId = null;

		for (final JnjGTIntOrderHeaderModel intOrderHeaderModel : invalidIntOrderHeaderRecords)
		{
			sapOrderNumber = intOrderHeaderModel.getSapOrderNumber();
			sourceSysId = intOrderHeaderModel.getSourceSystemId();

			/** Fetch Intermediate Sap Order Line Records. **/
			final Collection<JnjGTIntOrderLineModel> jnjGTIntOrderLines = jnjGTOrderFeedService.getJnjGTIntOrderLineModel(
					sapOrderNumber, sourceSysId,
					JnJCommonUtil.getValues(Jnjgtb2bCONSConstants.OrderFeed.ITEM_CATEGORY, Jnjb2bCoreConstants.SYMBOl_COMMA), null);

			if (jnjGTIntOrderLines != null && !jnjGTIntOrderLines.isEmpty())
			{
				invalidIntRecords.addAll(jnjGTIntOrderLines);

				for (final JnjGTIntOrderLineModel intOrderLineModel : jnjGTIntOrderLines)
				{
					/** Fetch Intermediate Sap Schedule Line Records. **/
					final Collection<JnjGTIntOrderSchLineModel> intOrderSchLines = jnjGTOrderFeedService.getJnjGTIntOrderSchLineModel(
							sapOrderNumber, sourceSysId, intOrderLineModel.getSapOrderLineNumber());

					if (intOrderSchLines != null && !intOrderSchLines.isEmpty())
					{
						invalidIntRecords.addAll(intOrderSchLines);
					}
				}
			}
		}
		return invalidIntRecords;
	}

	/**
	 * 
	 * @param file
	 * @param orderStatusInboundFileNames
	 * @param fileNamesAndCount
	 */
	private void checkLinesCount(final File file, final List<String> orderStatusInboundFileNames,
			final Map<String, String> fileNamesAndCount)
	{
		String currentLine;
		FileInputStream inputStream = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferReader = null;

		try
		{
			inputStream = new FileInputStream(file);
			dataInputStream = new DataInputStream(inputStream);
			bufferReader = new BufferedReader(new InputStreamReader(dataInputStream));

			int currentLineNumber = 0;
			int avaialbleRecordCount = 0;
			int mentionedRecordCount = 0;

			final LineNumberReader lineNumberReader = new LineNumberReader(bufferReader);
			lineNumberReader.skip(Long.MAX_VALUE);
			final int lineNumber = lineNumberReader.getLineNumber();

			final String line32 = (String) FileUtils.readLines(file).get(lineNumber - 1);
			LOGGER.info("line32   :" + line32);

			while ((currentLine = bufferReader.readLine()) != null)
			{
				currentLineNumber++;
				if (currentLineNumber > OrderStatusInbound.HEADER_ROWS_COUNT)
				{
					if (currentLine.trim().startsWith(OrderStatusInbound.FOOTER_STARTING_CHARACTER))
					{
						final String[] parts = currentLine.split(",");
						if (parts.length > 1)
						{
							try
							{
								mentionedRecordCount = (parts[1] != null) ? Integer.valueOf(parts[1].trim()).intValue() : 0;
							}
							catch (final NumberFormatException exception)
							{
								LOGGER.error("ERROR WHILE PARSING MENTIONED RECORDS COUNT IN THE FILE: " + file.getName()
										+ ". Exception: " + exception);
							}
						}
					}
					else
					{
						avaialbleRecordCount++;
					}
				}
			}

			/*** If records count mismatch, update the data strctures with required info to be sent as email data. ***/
			if (avaialbleRecordCount != mentionedRecordCount)
			{
				final String fileName = file.getName();

				LOGGER.info("Mismatch occured on available and mentioned records count for the file: " + fileName);
				orderStatusInboundFileNames.add(fileName);
				fileNamesAndCount.put(fileName, avaialbleRecordCount + OrderStatusInbound.RECORD_COUNT_DELIMETER
						+ mentionedRecordCount);
			}
			bufferReader.close();
		}
		catch (final IOException exception)
		{
			LOGGER.error(exception.getMessage());
		}
		finally
		{
			try
			{
				if (bufferReader != null)
				{
					bufferReader.close();
				}

				if (inputStream != null)
				{
					inputStream.close();
				}

				if (dataInputStream != null)
				{
					dataInputStream.close();
				}
			}
			catch (final IOException exception)
			{
				LOGGER.error("Exception while closing connections", exception);
			}
		}
	}
}
