/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 **/
package com.jnj.la.cronitorjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobCrashAbortEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import io.cronitor.client.CronitorClient;

/**
 * This class is defined for Cronitor configuration for LATAM JJC cronjobs.
 *
 */
public class JnJLaCronjobToCronitorEventListener extends AbstractEventListener {

	private static final Logger LOG = Logger.getLogger(JnJLaCronjobToCronitorEventListener.class);
	private static final String RUN = "run";
	private static final String COMPLETE = "complete";
	private static final String FAIL = "fail";

	private ConfigurationService configurationService;

	private CronitorClient cronitorClient;

	@Override
	protected void onEvent(final AbstractEvent event) {
		
		String jobCode = null;
		String cronitorEvent = null;

		if (event instanceof BeforeCronJobStartEvent) {
			jobCode = ((AbstractCronJobEvent) event).getCronJob();
			cronitorEvent = RUN;
			LOG.info("Cronjob started | " + jobCode);
		}
		if (event instanceof AfterCronJobFinishedEvent) {
			final AfterCronJobFinishedEvent afterEvent = (AfterCronJobFinishedEvent) event;
			if (afterEvent.getResult() != null) {
				if (afterEvent.getResult().equals(CronJobResult.ERROR)
						|| afterEvent.getResult().equals(CronJobResult.FAILURE)) {
					jobCode = ((AbstractCronJobEvent) event).getCronJob();
					cronitorEvent = FAIL;
					LOG.debug("Cronjob Errored/Failed | " + jobCode);
				} else if (afterEvent.getResult().equals(CronJobResult.SUCCESS)) {
					jobCode = ((AbstractCronJobEvent) event).getCronJob();
					cronitorEvent = COMPLETE;
					LOG.debug("Cronjob finished | " + jobCode);
				}
			}
		}

		onCrashEvent(event,jobCode,cronitorEvent);
	}

	private void onCrashEvent(final AbstractEvent event,String jobCode,String cronitorEvent) {
		if (event instanceof AfterCronJobCrashAbortEvent) {
			jobCode = ((AbstractCronJobEvent) event).getCronJob();
			cronitorEvent = FAIL;
			LOG.debug("Cronjob Aborted/Crashed | " + jobCode);
		}
		if ("true".equalsIgnoreCase(getConfigurationService().getConfiguration().getString("jnj.cronitor.enabled"))) {
			jobCode = getConfigurationService().getConfiguration().getString("jnj.cronjob.jobName." + jobCode);

			if (StringUtils.isNotEmpty(jobCode) && StringUtils.isNotEmpty(cronitorEvent)) {
				jobCode = getConfigurationService().getConfiguration().getString("jnj.cronitor.monitorcode." + jobCode);
				notifyCronitor(jobCode, cronitorEvent);
			}
		}
	}

	public void notifyCronitor(final String monitorcode, final String cronitorEvent) {

		try {

			LOG.info(monitorcode + " --> " + cronitorEvent);
			final String httpEnabled = getConfigurationService().getConfiguration().getString("jnj.cronitor.http");
			if (StringUtils.isNotEmpty(cronitorEvent) && StringUtils.isNotEmpty(httpEnabled)
					&& "true".equalsIgnoreCase(httpEnabled)) {
				ping(monitorcode, cronitorEvent, CronitorClient.withoutHttps());
			} else if (StringUtils.isNotEmpty(cronitorEvent)) {
				ping(monitorcode, cronitorEvent, cronitorClient);
			}
		} catch (final IOException e) {
			LOG.error("IOException Failure during Cronitor Trigger - " + monitorcode + e);

		}
	}

	private void ping(final String monitorcode, final String cronitorEvent, final CronitorClient cronitorClient)
			throws IOException {
		if (StringUtils.equalsIgnoreCase(cronitorEvent, RUN)) {
			cronitorClient.run(monitorcode, "Job Started Successfully");
		} else if (StringUtils.equalsIgnoreCase(cronitorEvent, COMPLETE)) {
			cronitorClient.complete(monitorcode, "Job Completed Successfully");
		} else if (StringUtils.equalsIgnoreCase(cronitorEvent, FAIL)) {
			cronitorClient.fail(monitorcode, "Job Failed/Aborted Successfully");
		}
	}

	public ConfigurationService getConfigurationService() {

		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {

		this.configurationService = configurationService;
	}

	public CronitorClient getCronitorClient() {

		return cronitorClient;
	}

	public void setCronitorClient(final CronitorClient cronitorClient) {
		this.cronitorClient = cronitorClient;
	}

}
