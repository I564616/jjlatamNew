package com.jnj.la.cronjobs;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.jnj.la.core.services.order.JnjLAOrderService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

/**
 * Job which executes when sap failed order cronjob is triggered.
 *
 */
public class JnjLaSAPFailedOrdersReportJob extends AbstractJobPerformable<CronJobModel> {

	private static final Logger LOGGER = Logger.getLogger(JnjLaSAPFailedOrdersReportJob.class);
	private JnjLAOrderService jnjLAOrderService;

	/**
	 * Overridden method which will be executed when corresponding cronjob
	 * triggered.
	 * 
	 * @param cronJob - holds cronjob model.
	 */
	@Override
	public PerformResult perform(CronJobModel cronJob) {

		boolean status = getJnjLAOrderService().sendSAPFailedOrdersReportEmail();
		if (status) {
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		} else {
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

	}

	public JnjLAOrderService getJnjLAOrderService() {
		return jnjLAOrderService;
	}

	public void setJnjLAOrderService(JnjLAOrderService jnjLAOrderService) {
		this.jnjLAOrderService = jnjLAOrderService;
	}

}