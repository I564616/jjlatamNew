/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */
package com.jnj.la.cronjobs;


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.facades.data.JnjLaCronJobMonitoringReportData;
import com.jnj.facades.reports.JnjLaCronJobMonitoringReportsFacade;


public class JnjLaCronJobMonitoringReportEmailJob extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(JnjLaCronJobMonitoringReportEmailJob.class);


    private JnjLaCronJobMonitoringReportsFacade jobMonitoringReportsFacade;

    /**
     * This method performs the job for sending job monitoring report e-mail
     */
    @Override
    public PerformResult perform(final CronJobModel cronJobModel)
    {
        LOG.debug("START : Performing JnjLaCronJobMonitoringReportEmailJob");

        final Map<String, JnjLaCronJobMonitoringReportData> jobMonitoringReportData = jobMonitoringReportsFacade
                .fetchJobMonitoringData();
        LOG.debug("jobMonitoringReportData size :" + jobMonitoringReportData.size());
        final List<JnjLaCronJobMonitoringEmailDto> dtoList = jobMonitoringReportsFacade
                .fetchJobMonitoringStaticData(jobMonitoringReportData.keySet());
        jobMonitoringReportsFacade.updateJobMonitoringData(dtoList, jobMonitoringReportData);

        jobMonitoringReportsFacade.sendJobMonitoringReport(dtoList);
        LOG.debug("END : JnjLaCronJobMonitoringReportEmailJob Complete");

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    /**
     * @return the jobMonitoringReportsFacade
     */
    public JnjLaCronJobMonitoringReportsFacade getJobMonitoringReportsFacade()
    {
        return jobMonitoringReportsFacade;
    }

    /**
     * @param jobMonitoringReportsFacade
     *           the jobMonitoringReportsFacade to set
     */
    public void setJobMonitoringReportsFacade(final JnjLaCronJobMonitoringReportsFacade jobMonitoringReportsFacade)
    {
        this.jobMonitoringReportsFacade = jobMonitoringReportsFacade;
    }
}
