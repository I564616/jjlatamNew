package com.jnj.la.job;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.JnjCustomerDataLoad;
import com.jnj.la.email.util.JnjLaEmailUtil;


/**
 * @author sandeep.y.kumar
 *
 */
public class JnjUpsertCustomerCronJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	/**
	 * This method is responsible for calling the JnjCustomerDataLoad class for the Upsert Customer process, whenever the
	 * cronjob is triggered.
	 */

	@Autowired
	private JnjCustomerDataLoad jnjCustomerDataLoad;

	@Autowired
	private JnjLaEmailUtil jnjLaEmailUtil;


	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String methodName = "perform()";
		String errorMessage = null;
		Map<Object, String> errorRecords = null;
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName + Logging.BEGIN_OF_METHOD,
				JnJCommonUtil.getCurrentDateTime(), JnjUpsertCustomerCronJob.class);

		try
		{
			jnjCustomerDataLoad.loadCustomerData(cronJobModel);
			errorRecords = jnjCustomerDataLoad.getListOfErroneousRecords();
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME, methodName, "Exception occurred while executing upsert Customer cron job" + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					exception, JnjUpsertCustomerCronJob.class);
			errorMessage = exception.getMessage();
			if (errorMessage != null || (null != errorRecords && !errorRecords.isEmpty()))
			{
				final String logFileName = "Upsert-Customer-Error-Records-";
				final Boolean isNotificationSentSuccessfully = jnjLaEmailUtil.sendNotification(cronJobModel, errorRecords,
						errorMessage, JnjUpsertCustomerCronJob.class, logFileName, Logging.GET_EMAIL_ERROR_VM);
				if (isNotificationSentSuccessfully.equals(Boolean.FALSE))
				{
					JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
							"Could not send email due to technical failure. Please login to HMC to check the details.",
							JnjUpsertCustomerCronJob.class);
				}
			}
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName + Logging.END_OF_METHOD,
				JnJCommonUtil.getCurrentDateTime(), JnjUpsertCustomerCronJob.class);

		if (errorMessage != null || (null != errorRecords && !errorRecords.isEmpty()))
		{
			final String logFileName = "Upsert-Customer-Error-Records-";
			final Boolean isNotificationSentSuccessfully = jnjLaEmailUtil.sendNotification(cronJobModel, errorRecords, errorMessage,
					JnjUpsertCustomerCronJob.class, logFileName, Logging.GET_EMAIL_ERROR_VM);
			if (isNotificationSentSuccessfully.equals(Boolean.FALSE))
			{
				JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
						"Could not send email due to technical failure. Please login to HMC to check the details.",
						JnjUpsertCustomerCronJob.class);
			}
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
