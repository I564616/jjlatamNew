/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.la.core.daos.impl;

import com.jnj.la.core.daos.JnjLaCronJobDao;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.la.core.daos.JnjLaCronJobDao;
import com.jnj.la.core.model.JnjLaCronJobMonitoringModel;

/**
 * To fetch cron job static data by monitoringCodes
 */

public class JnjLaCronJobDaoImpl  extends AbstractItemDao implements JnjLaCronJobDao
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobDaoImpl.class);

    protected static final String MONITORING_CODE = "monitoringCode";
    protected static final String JOB_CODE = "jobCode";
    private static final String MONITORING_MODEL_QUERY = "SELECT {PK} FROM {JnjLaCronJobMonitoring} WHERE {monitored} = '1' and {monitoringCode} IN (?monitoringCode)";
    private static final String JOB_QUERY = "SELECT {PK} FROM {CRONJOB} WHERE {CODE} IN (?jobCode)";

    /**
     * To fetch cron job static data by monitoringCodes
     */
    @Override
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final List<String> monitoringCode)
    {
        LOGGER.info("fetchJobMonitoringStaticData monitrongCode :" + monitoringCode);
        final String METHOD_NAME = "fetchJobMonitoringStaticData()";
        List<JnjLaCronJobMonitoringModel> jnjLaCronJobMonitoringModelList = new ArrayList<>();
        try
        {

            final Map queryParams = new HashMap();
            queryParams.put(MONITORING_CODE, monitoringCode);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MONITORING_MODEL_QUERY);
            fQuery.addQueryParameters(queryParams);
            jnjLaCronJobMonitoringModelList = getFlexibleSearchService().<JnjLaCronJobMonitoringModel> search(fQuery).getResult();
        }
        catch (final ModelNotFoundException modelNotFoundException)
        {
            LOGGER.warn(
                    Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME + "model not found for given monitrongCode"
                            + Logging.HYPHEN + modelNotFoundException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
        }
        return populateMonitoringStaticData(jnjLaCronJobMonitoringModelList);
    }

    /**
     * To fetch cronj job model by job codes
     */
    @Override
    public List<CronJobModel> getCronJobModelByCode(final List<String> jobCode)
    {
        LOGGER.info("getCronJobModelByCode JobCode :" + jobCode);
        final String METHOD_NAME = "getCronJobModelByCode()";
        List<CronJobModel> cronJobModel = new ArrayList<>();
        try
        {

            final Map queryParams = new HashMap();
            queryParams.put(JOB_CODE, jobCode);
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(JOB_QUERY);
            fQuery.addQueryParameters(queryParams);
            cronJobModel = getFlexibleSearchService().<CronJobModel> search(fQuery).getResult();
        }
        catch (final ModelNotFoundException modelNotFoundException)
        {
            LOGGER.warn(Jnjb2bCoreConstants.Logging.REPORTS_NAME + Logging.HYPHEN + METHOD_NAME
                    + "model not found exception for given jobCode" + Logging.HYPHEN + modelNotFoundException.getLocalizedMessage()
                    + JnJCommonUtil.getCurrentDateTime());
        }
        return cronJobModel;
    }

    private static List<JnjLaCronJobMonitoringEmailDto> populateMonitoringStaticData(
            final List<JnjLaCronJobMonitoringModel> jnjLaCronJobMonitoringModelList)
    {
        LOGGER.info("jnjLaCronJobMonitoringModelList size: " + jnjLaCronJobMonitoringModelList.size());

        final List<JnjLaCronJobMonitoringEmailDto> dtoList = new ArrayList<>();
        JnjLaCronJobMonitoringModel jnjLaCronJobMonitoringModel = null;
        if (CollectionUtils.isNotEmpty(jnjLaCronJobMonitoringModelList))
        {
            LOGGER.info("In populateMonitoringStaticData class of jnjLaCronJobMonitoringModelList item :: "
                    + jnjLaCronJobMonitoringModelList.get(0).getClass());
            for (int count = 0; count < jnjLaCronJobMonitoringModelList.size(); count++)
            {
                final Object obj = jnjLaCronJobMonitoringModelList.get(count);
                if (obj instanceof JnjLaCronJobMonitoringModel)
                {
                    jnjLaCronJobMonitoringModel = (JnjLaCronJobMonitoringModel) obj;
                    final JnjLaCronJobMonitoringEmailDto emailDto = new JnjLaCronJobMonitoringEmailDto();
                    emailDto.setJobCode(jnjLaCronJobMonitoringModel.getJobCode());
                    emailDto.setCountry(jnjLaCronJobMonitoringModel.getCountry());
                    emailDto.setFrequency(jnjLaCronJobMonitoringModel.getFrequency());
                    emailDto.setServiceLevelStartTime(jnjLaCronJobMonitoringModel.getServiceLevelStartTime());
                    emailDto.setServiceLevelEndTime(jnjLaCronJobMonitoringModel.getServiceLevelEndTime());
                    emailDto.setAverageRunningDuration(jnjLaCronJobMonitoringModel.getAverageRunningDuration());
                    emailDto.setMonitoringCode(jnjLaCronJobMonitoringModel.getMonitoringCode());
                    emailDto.setMonitored(jnjLaCronJobMonitoringModel.isMonitored());
                    dtoList.add(emailDto);
                }
            }
        }
        return dtoList;
    }
}
