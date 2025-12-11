/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.mapper.order.JnjGTOrderSyncDataLoadMapper;
import com.jnj.gt.model.JnjGTIntOrderHeaderModel;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * Cron-job responsbile to perform write operation/processing of the pending <code>JnjGTIntOrderHeader</code> records.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTProcessOrderStatusInboundJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTProcessOrderStatusInboundJob.class);

	/**
	 * The Constant Instance of <code>JnjGTSurgeonDataLoadMapper</code>.
	 */
	@Autowired
	private JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper;

	/**
	 * The Constant Instance of <code>JnjGTFeedService</code>.
	 */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROCESS_ORDER_STATUS_INT_RECORDS + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			/** After clean up and read operation, mark the valid records status to PENDING **/
			jnjGTFeedService.updateIntRecordStatus(JnjGTIntOrderHeaderModel._TYPECODE);

			/*
			 * Start processing of valid records.
			 */
			jnjGTOrderSyncDataLoadMapper.processIntermediateRecords();
		}
		catch (final Exception exception)
		{
			LOGGER.error(Logging.PROCESS_SURGEON_INT_RECORDS + Logging.HYPHEN + "Renaming files has caused an exception: "
					+ exception.getMessage());

			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROCESS_ORDER_STATUS_INT_RECORDS + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}
}
