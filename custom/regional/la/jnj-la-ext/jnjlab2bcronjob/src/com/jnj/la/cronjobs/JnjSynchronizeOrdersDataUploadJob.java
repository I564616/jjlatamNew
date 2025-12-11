/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.services.order.JnjLAOrderDataService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JnjSynchronizeOrdersDataUploadJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{
	@Autowired
	private JnjLAOrderDataService jnjLAOrderDataService;

	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJob)
	{
		final String methodName = "perform()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD,
				JnjSynchronizeOrdersDataUploadJob.class);
		try
		{
			final List<JnjOrderDTO> orders = jnjLAOrderDataService.pullOrdersFromRSA(cronJob);
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Data pulled successfully.",
					JnjSynchronizeOrdersDataUploadJob.class);

			if (orders != null && !orders.isEmpty())
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"There are " + orders.size() + " to be updated in Hybris.", JnjSynchronizeOrdersDataUploadJob.class);

				jnjLAOrderDataService.saveOrdersToHybris(orders, cronJob);

				JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Data saved successfully.",
						JnjSynchronizeOrdersDataUploadJob.class);
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"There is no new data to be saved into hybris. Ending cronjob.", JnjSynchronizeOrdersDataUploadJob.class);
			}
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Exception occured while executing upsert sync order cron job"
							+ "-" + exception.getLocalizedMessage() + ". Exception: " + exception.getMessage(),
					exception, JnjSynchronizeOrdersDataUploadJob.class);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD,
				JnjSynchronizeOrdersDataUploadJob.class);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
