/**
 * 
 */
package com.jnj.core.cronjobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.datapurge.JnjFilePurge;
import com.jnj.core.util.JnJCommonUtil;


/**
 * This class is purge all the files which are no longer needed
 * 
 * @author balinder.singh
 * 
 */
public class JnjFilePurgeCronJob extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOGGER = Logger.getLogger(JnjFilePurgeCronJob.class);




	@Autowired
	JnjFilePurge jnjFilePurge;

	public JnjFilePurge getJnjFilePurge() {
		return jnjFilePurge;
	}


	/**
	 * This method is responsible for calling the File Purging process, whenever the cronjob is triggered.
	 * 
	 * @author balinder.singh
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		final String METHOD_NAME = "perform()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CRON_JOB_FILE_PURGE + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			jnjFilePurge.doFilePurge();
		}
		catch (final Throwable exception)
		{
			LOGGER.error(Logging.CRON_JOB_FILE_PURGE + "-" + METHOD_NAME + "Exception occured while executing File Purge cron job"
					+ "-" + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CRON_JOB_FILE_PURGE + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
}