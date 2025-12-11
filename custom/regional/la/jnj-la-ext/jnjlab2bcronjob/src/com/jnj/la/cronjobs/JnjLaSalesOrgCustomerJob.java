/**
 *
 */
package com.jnj.la.cronjobs;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.dataload.JnjLASalesOrgCustomerDataload;

public class JnjLaSalesOrgCustomerJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{

	@Autowired
	private JnjLASalesOrgCustomerDataload jnjLASalesOrgCustomerDataload;

	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String METHOD_NAME = "perform()";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaSalesOrgCustomerJob.class);
		try
		{
			jnjLASalesOrgCustomerDataload.getSalesOrgCustomerData(cronJobModel);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SALES_ORG_CUST,
					METHOD_NAME, "Exception occurred while executing the salesOrgCustomer cronjob" + "-"
							+ exception.getLocalizedMessage() + ". Exception: " + exception.getMessage(),
					exception, JnjLaSalesOrgCustomerJob.class);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
