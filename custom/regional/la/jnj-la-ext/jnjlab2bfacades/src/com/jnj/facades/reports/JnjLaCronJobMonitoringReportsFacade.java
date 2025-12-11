/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.facades.reports;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.facades.data.JnjLaCronJobMonitoringReportData;

/**
 * The Interface JnjLaCronJobMonitoringReportsFacade.
 */
public interface JnjLaCronJobMonitoringReportsFacade
{
    /**
     * To fetch cronitor data
     *
     * @return Map
     */
    public Map<String, JnjLaCronJobMonitoringReportData> fetchJobMonitoringData();

    /**
     * To fetch and process cron job static data
     *
     * @param monitoringCode
     * the monitoringCode to be used
     * @return List
     */
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final Set<String> monitoringCode);

    /**
     * To fetch and process cron job data
     *
     * @param dtoList
     *           the dtoList to be used
     * @param monitoringReportData
     *           the monitoringReportData to be used
     */
    public void updateJobMonitoringData(final List<JnjLaCronJobMonitoringEmailDto> dtoList,
                                        final Map<String, JnjLaCronJobMonitoringReportData> monitoringReportData);

    /**
     * To send Job Monitoring Report by mail
     *
     * @param dtoList
     *           the dtoList to be used
     */
    public void sendJobMonitoringReport(final List<JnjLaCronJobMonitoringEmailDto> dtoList);
}
