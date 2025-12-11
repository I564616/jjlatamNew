/**
 * 
 */
package com.jnj.gt.job;


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * @author himanshi.batra
 * 
 */
public class JnjGTAuditTrailCleanupDataJob extends AbstractJobPerformable<CronJobModel>
{

	@Autowired
	private JnjGTOperationsService jnjGTOperationsService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		//Business logic to clean the table.
		//List<JnJGTAuditTrailModel> b= cleanAllRecords();

		final boolean recordsDeleted = jnjGTOperationsService.cleanAllRecords();
		//System.out.println(records);

		if (recordsDeleted)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

	}



}
