/**
 *
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjOrderService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.constants.Jnjgtb2bcronjobConstants.Logging;
import com.jnj.gt.mapper.zipcode.JnjGTEarlyZipCodeMapper;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * The JnjGTEarlyZipCodeCronJob class is a cron job which runs on specific time at daily basis to download the txt &
 * excel file from the specified url location and after processing the file contents, that has been saved in hybris
 * model.
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjGTEarlyZipCodeCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTEarlyZipCodeCronJob.class);

	@Autowired
	JnjOrderService jnjOrderService;

	@Autowired
	JnjGTEarlyZipCodeMapper jnjGTEarlyZipCodeMapper;

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * This method is responsible for calling the JnjGTCreateMapperImpl, JnjGTCreateDeliveredMapperImpl class for
	 * retrieving those orders for which SAP order Number doesn't exist in the hybris database.
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		try
		{
			jnjGTEarlyZipCodeMapper.fetchAndSaveDataInHybris();

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("CronJob runs successfully");
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.START_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final SystemException systemException)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "System Exception occured " + systemException.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final FileNotFoundException fileNotFoundException)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "File Not Found Exception occured " + fileNotFoundException.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final IOException integrationException)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Integration Exception occured " + integrationException.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Throwable Exception occured " + throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}
}
