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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.ShipmentInbound;
import com.jnj.gt.model.JnjGTIntShipTrckHdrModel;
import com.jnj.gt.model.JnjGTIntShipTrckLineModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.shipment.JnjGTShipmentFeedService;


/**
 * Cron-job responsible to:
 * 
 * <ul>
 * <li>Clean up existing obsolete 'LOADING' records from <code>JnjGTIntShipTrckHdr</code></li>
 * <li>Pick up the Ship Tracking in-bound files based on a prefix at a specified location and rename them to hot-folder
 * based convention.</li>
 * 
 * 
 */
public class JnjGTReadShipTrackingInboundJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTReadShipTrackingInboundJob.class);

	/**
	 * The Constant for .csv
	 */
	private static final String CSV_FILE_FORMAT = ".csv";

	/**
	 * The Constant value for Order Header csv file prefix.
	 */
	private static final String SHIPMENT_HEADER_INBOUND_CSV_FILE_PREFIX = "/sapShipTrackHeader";

	/**
	 * The Constant value for Order Line csv file prefix.
	 */
	private static final String SHIPMENT_LINE_INBOUND_CSV_FILE_PREFIX = "/sapShipTrackLine";

	/**
	 * The Constant for hyphen symbol.
	 */
	private static final String SYMBOL_HYPHEN = "-";

	/**
	 * The counter used for renaming Order Header in-bound files.
	 */
	private int shipmentHeaderCsvCounter;

	/**
	 * The counter used for renaming Order Line in-bound files.
	 */
	private int shipmentLineCsvCounter;

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
	 * The private Instance of <code>JnjGTShipmentFeedService</code>.
	 */
	@Autowired
	private JnjGTShipmentFeedService jnjGTShipmentFeedService;


	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_SHIP_TRACK_INBOUND_FILES + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}

		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		shipmentHeaderCsvCounter = Integer.parseInt(jnjConfigService
				.getConfigValueById(ShipmentInbound.SHIPMENT_HEADER_CSV_FILE_COUNTER_KEY));

		shipmentLineCsvCounter = Integer.parseInt(jnjConfigService
				.getConfigValueById(ShipmentInbound.SHIPMENT_lINE_CSV_FILE_COUNTER_KEY));

		try
		{
			/*
			 * Delete existing obsolete records from the Header, Line and Schedule Line intermediate tables.
			 */
			final List<JnjGTIntShipTrckHdrModel> invalidIntShipTrackHeaderRecords = (List<JnjGTIntShipTrckHdrModel>) jnjGTFeedService
					.getRecordsByStatus(JnjGTIntShipTrckHdrModel._TYPECODE, RecordStatus.LOADING);

			final Collection<ItemModel> invalidIntRecords = getIntermediateRecordsForCleanup(invalidIntShipTrackHeaderRecords);
			invalidIntRecords.addAll(invalidIntShipTrackHeaderRecords);
			jnjGTFeedService.invalidateRecords(invalidIntRecords);

			/*
			 * Rename the in-bound files present at the specified location so that would get picked by Hot-folder after
			 * rename.
			 */
			final String hotFolderBaseLocation = Config.getParameter(Jnjb2bCoreConstants.HOT_FOLDER_ROOT_PATH);
			final List<File> fileList = JnJXMLFilePicker.pickAndSortFilesForHotFolder(hotFolderBaseLocation);

			final String headerPrefix = Config.getParameter(ShipmentInbound.SHIPMENT_HEADER_INBOUND_FILE_PREFIX_KEY);
			final String linePrefix = Config.getParameter(ShipmentInbound.SHIPMENT_LINE_INBOUND_FILE_PREFIX_KEY);

			for (final File file : fileList)
			{
				if (file.getName().startsWith(headerPrefix))
				{
					LOGGER.info(Logging.RENAME_SHIP_TRACK_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: " + file.getName());
					file.renameTo(new File(hotFolderBaseLocation + SHIPMENT_HEADER_INBOUND_CSV_FILE_PREFIX + SYMBOL_HYPHEN
							+ (shipmentHeaderCsvCounter++) + CSV_FILE_FORMAT));
				}
				else if (file.getName().startsWith(linePrefix))
				{
					LOGGER.info(Logging.RENAME_SHIP_TRACK_INBOUND_FILES + Logging.HYPHEN + "File picked for rename: " + file.getName());
					file.renameTo(new File(hotFolderBaseLocation + SHIPMENT_LINE_INBOUND_CSV_FILE_PREFIX + SYMBOL_HYPHEN
							+ (shipmentLineCsvCounter++) + CSV_FILE_FORMAT));
				}
			}
		}
		catch (final Exception exception)
		{
			LOGGER.error(Logging.RENAME_SHIP_TRACK_INBOUND_FILES + Logging.HYPHEN + "Renaming files has caused an exception: "
					+ exception.getMessage());

			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		/*
		 * Persist back the incremented/updated value of csv counters.
		 */
		jnjConfigService.saveConfigValues(ShipmentInbound.SHIPMENT_HEADER_CSV_FILE_COUNTER_KEY,
				String.valueOf(shipmentHeaderCsvCounter));

		jnjConfigService.saveConfigValues(ShipmentInbound.SHIPMENT_lINE_CSV_FILE_COUNTER_KEY,
				String.valueOf(shipmentLineCsvCounter));

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RENAME_ORDER_STATUS_INBOUND_FILES + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * 
	 * @param invalidIntShipTrackrHeaderRecords
	 * @return Collection<ItemModel>
	 */
	private Collection<ItemModel> getIntermediateRecordsForCleanup(
			final List<JnjGTIntShipTrckHdrModel> invalidIntShipTrackrHeaderRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();

		for (final JnjGTIntShipTrckHdrModel intOrderHeaderModel : invalidIntShipTrackrHeaderRecords)
		{
			/** Fetch Intermediate Shipment Line Records. **/
			final Collection<JnjGTIntShipTrckLineModel> jnjGTIntShipmentLines = jnjGTShipmentFeedService
					.getJnjGTIntShipTrckLineModel(null, intOrderHeaderModel.getDeliveryNum(), intOrderHeaderModel.getSourceSysId());

			if (jnjGTIntShipmentLines != null && !jnjGTIntShipmentLines.isEmpty())
			{
				invalidIntRecords.addAll(jnjGTIntShipmentLines);
			}
		}
		return invalidIntRecords;
	}
}
