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

import com.jnj.core.services.JnjGTOrderService;


/**
 * This cronjob deletes quote orders created one day before the current date.
 * 
 */
public class JnjGTQuoteOrderDeletionJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTQuoteOrderDeletionJob.class);
	@Autowired
	JnjGTOrderService jnjGTOrderService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		try
		{
			jnjGTOrderService.removeQuoteOrders();
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception exception)
		{
			LOGGER.error("Exception occured during removal of quote order");
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}
}
