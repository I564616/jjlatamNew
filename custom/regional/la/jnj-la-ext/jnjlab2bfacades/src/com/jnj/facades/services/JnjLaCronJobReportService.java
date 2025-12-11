/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.facades.services;

import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.List;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;




/**
 * The JnjNACronitorService interface contains all those methods which are dealing with cronitor data and it has
 * declaration of all the methods which are defined in the DefaultJnjLaCronitorService class.
 */

public interface JnjLaCronJobReportService
{
    /**
     * To fetch cronitor data
     *
     * @param page
     *  the page to be used
     *
     * @return String
     */
    public String fetchCronJobCronitorData(final int page);

    /**
     * To fetch cron job static data
     *
     * @param monitoringCode
     *           the monitoringCode to be used
     * @return List
     */
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final List<String> monitoringCode);

    /**
     * To fetch cronj job model by job codes
     *
     * @param jobCodes
     *           the jobCodes to be used
     * @return List
     */
    public List<CronJobModel> getCronJobModelByCode(final List<String> jobCodes);

    /**
     * To send Job Monitoring Report by mail
     *
     * @param dtoList
     *           the dtoList to be used
     */
    public void sendCronJobMonitoringData(final List<JnjLaCronJobMonitoringEmailDto> dtoList);
}
