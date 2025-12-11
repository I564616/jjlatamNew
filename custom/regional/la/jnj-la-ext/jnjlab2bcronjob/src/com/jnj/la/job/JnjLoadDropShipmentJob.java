/**
 *
 */
package com.jnj.la.job;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.JnjDropshipmentDataLoad;

public class JnjLoadDropShipmentJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	@Autowired
	protected JnjDropshipmentDataLoad jnjDropshipmentDataLoad;

	private static final Logger LOG = Logger.getLogger(JnjLoadDropShipmentJob.class);

	/**
	 * This is the Main class for Dropshipment Cronjob.
	 */
	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.FEED_DROPSHIPMENT_JOB_METHOD_NAME + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		try
		{
			//Calling the method for loading xml data into DTO objects
			jnjDropshipmentDataLoad.getLoadDropshipment(cronJobModel);

		}
		catch (final Exception exception)
		{

			LOG.error(
					Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN + "Error occured while calling JnjDropshipmentDataLoad class - "
							+ exception + exception.getMessage() + Logging.HYPHEN + Logging.FEED_DROPSHIPMENT_JOB_METHOD_NAME
							+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN + Logging.FEED_DROPSHIPMENT_JOB_METHOD_NAME + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
