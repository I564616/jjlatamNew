/*
    * This code contains copyright information which is the proprietary property
    * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
    * transmitted in any form without the prior written permission of JNJ Companies Ltd.
    * Copyright (C) JNJ Companies Ltd 2020
    * All rights reserved.
*/

package com.jnj.facades.reports.impl;

import com.jnj.facades.converters.populator.JnjLaCronJobCronitorDataPopulator;
import com.jnj.facades.reports.JnjLaCronJobMonitoringReportsFacade;
import de.hybris.platform.cronjob.model.CronJobModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.facades.data.JnjLaCronJobMonitoringReportData;
import com.jnj.facades.reports.JnjLaCronJobMonitoringReportsFacade;
import com.jnj.facades.converters.populator.JnjLaCronJobCronitorDataPopulator;
import com.jnj.facades.services.JnjLaCronJobReportService;

/*
 * Facade to process job monitoring data
 */

public class JnjLaCronJobMonitoringReportsFacadeImpl implements JnjLaCronJobMonitoringReportsFacade
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobMonitoringReportsFacadeImpl.class);
    private static final String RUNNING = "RUNNING";
    private static final String FINISHED = "FINISHED";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
    private static final String ABORTED = "ABORTED";
    private static final String ERROR = "ERROR";
    private static final String CONTINUED = "CONTINUED";
    private static final String NOT_STARTED = "NOT STARTED";

    private static final String REMARKS_RUNNING_SUCCESS = "Running as per schedule;Last run success";
    private static final String REMARKS_RUNNING_WRONGSTATUS_SUCCESS = "Running as per schedule, wrong status in cronitor;Last run success";
    private static final String REMARKS_RUNNING_DELAY = "Job is running delay";
    private static final String REMARKS_RUNNING_ERROR = "Running as per schedule;Last run error";
    private static final String REMARKS_FINISHED_WRONGSTATUS_SUCCESS = "Finished successfully, wrong status in cronitor; Last run success";
    private static final String REMARKS_FINISHED_SUCCESS = "Finished successfully; Last run success";
    private static final String REMARKS_FINISHED_DELAY = "Job finished with delay";
    private static final String REMARKS_NOT_STARTED = "Job did not start";
    private static final String REMARKS_FAILED = "Job failed";
    private static final String CRONITOR_STATUS_FAILED = "Failed";
    private static final String CRONITOR_MONITORING_PAUSED = "Monitoring paused in cronitor";
    private static final String CRONITOR_TOTAL_MONITOR_COUNT = "total_monitor_count";
    private static final String CRONITOR_PAGE_SIZE = "page_size";

    private JnjLaCronJobCronitorDataPopulator jnjLaCronJobCronitorDataPopulator;

    private JnjLaCronJobReportService jnjLaCronJobReportService;


    public JnjLaCronJobReportService getJnjLaCronJobReportService()
    {
        return jnjLaCronJobReportService;
    }

    public void setJnjLaCronJobReportService(final JnjLaCronJobReportService jnjLaCronJobReportService)
    {
        this.jnjLaCronJobReportService = jnjLaCronJobReportService;
    }

    /**
     * To fetch and process cronitor data
     */
    @Override
    public Map<String, JnjLaCronJobMonitoringReportData> fetchJobMonitoringData()
    {
        final Map<String, JnjLaCronJobMonitoringReportData> jobMonitoringReportData = new HashMap<>();
        int pageCount = 1;
        String jsonString = jnjLaCronJobReportService.fetchCronJobCronitorData(pageCount);
        if (StringUtils.isNotEmpty(jsonString))
        {
            jnjLaCronJobCronitorDataPopulator.populate(jsonString, jobMonitoringReportData);
            pageCount = getCronitorPageCount(jsonString);
        }

        if (pageCount > 1)
        {
            for (int page = 2; page <= pageCount; page++)
            {
                jsonString = jnjLaCronJobReportService.fetchCronJobCronitorData(page);
                if (StringUtils.isNotEmpty(jsonString))
                {
                    jnjLaCronJobCronitorDataPopulator.populate(jsonString, jobMonitoringReportData);
                }
            }
        }
        LOGGER.info("Cronitor jobMonitoringReportData size: " + jobMonitoringReportData.size());
        return jobMonitoringReportData;
    }

    /**
     * To fetch and process cron job static data
     */
    @Override
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final Set<String> monitoringCode)
    {
        final List<String> monitoringCodeList = new ArrayList<>(monitoringCode);
        final List<JnjLaCronJobMonitoringEmailDto> JnjLaCronJobMonitoringEmailDtoList = jnjLaCronJobReportService
                .fetchJobMonitoringStaticData(monitoringCodeList);
        if (CollectionUtils.isNotEmpty(JnjLaCronJobMonitoringEmailDtoList))
        {
            LOGGER.info(
                    "DefaultJnjLaCronJobMonitoringReportsFacade :: fetchJobMonitoringStaticData :: class of jnjLaCronJobMonitoringEmailDtoList :: "
                            + JnjLaCronJobMonitoringEmailDtoList.get(0).getClass());
            LOGGER.info("jnjLaCronJobMonitoringEmailDtoList size: " + JnjLaCronJobMonitoringEmailDtoList.size());
        }
        return JnjLaCronJobMonitoringEmailDtoList;
    }

    /**
     * To fetch and process cron job data
     */
    @Override
    public void updateJobMonitoringData(final List<JnjLaCronJobMonitoringEmailDto> dtoList,
                                        final Map<String, JnjLaCronJobMonitoringReportData> monitoringReportDataMap)
    {
        LOGGER.info("updateJobMonitoringData service :: dtoList :: " + dtoList.size());
        Map<String, JnjLaCronJobMonitoringEmailDto> emailDtoMap = new HashMap<>();
        List<String> jobCodes = new ArrayList<>();
        for (int i = 0; i < dtoList.size(); i++)
        {
            emailDtoMap.put(dtoList.get(i).getJobCode(), dtoList.get(i));
            jobCodes.add(dtoList.get(i).getJobCode());
        }
        final List<CronJobModel> cronJobModelList = jnjLaCronJobReportService.getCronJobModelByCode(jobCodes);
        if (CollectionUtils.isNotEmpty(cronJobModelList))
        {
            LOGGER.info("DefaultJnjLaCronJobMonitoringReportsFacade :: updateJobMonitoringData :: class of cronJobModelList :: "
                    + cronJobModelList.get(0).getClass());
            LOGGER.info("cronJobModelList size: " + cronJobModelList.size());
        }

        for (CronJobModel cronJobModel : cronJobModelList)
        {
            if (cronJobModel != null)
            {
                JnjLaCronJobMonitoringEmailDto emailDto = emailDtoMap.get(cronJobModel.getCode());
                final JnjLaCronJobMonitoringReportData monitoringReportData = monitoringReportDataMap.get(emailDto.getMonitoringCode());
                updateCronJobModelData(cronJobModel, emailDto);

                //status and remarks
                if (emailDto.getCurrentStatus().equalsIgnoreCase(RUNNING))
                {
                    updateRunningJobRemarks(emailDto, monitoringReportData);
                }
                else if (emailDto.getCurrentStatus().equalsIgnoreCase(FINISHED))
                {
                    updateFinishedJobRemarks(emailDto, monitoringReportData);
                }
                else
                {
                    updateOtherJobStatusRemarks(emailDto, monitoringReportData);
                }
                updateDelayRemarks(cronJobModel, emailDto);

                if (monitoringReportData.isPaused())
                {
                    emailDto.setPaused(monitoringReportData.isPaused());
                    emailDto.setRemarks(CRONITOR_MONITORING_PAUSED);
                }
            }
        }
    }

    private static void updateDelayRemarks(final CronJobModel cronJobModel, final JnjLaCronJobMonitoringEmailDto emailDto)
    {
        final Date startTime = cronJobModel.getStartTime();
        Date endTime = null;

        if (emailDto.getCurrentStatus().equalsIgnoreCase(RUNNING))
        {
            endTime = new Date();
        }
        else if (emailDto.getCurrentStatus().equalsIgnoreCase(FINISHED) && emailDto.getLastStatus().equalsIgnoreCase(SUCCESS))
        {
            endTime = cronJobModel.getEndTime();
        }

        if (startTime != null && endTime != null && StringUtils.isNotEmpty(emailDto.getAverageRunningDuration()))
        {
            final long actualRunTime = endTime.getTime() - startTime.getTime();
            final long averageRunTime = Long.valueOf(emailDto.getAverageRunningDuration()).longValue() * 60 * 1000;
            if (actualRunTime > averageRunTime)
            {
                if (emailDto.getCurrentStatus().equalsIgnoreCase(RUNNING))
                {
                    emailDto.setRemarks(REMARKS_RUNNING_DELAY);
                }
                else if (emailDto.getCurrentStatus().equalsIgnoreCase(FINISHED) && emailDto.getLastStatus().equalsIgnoreCase(SUCCESS))
                {
                    emailDto.setRemarks(REMARKS_FINISHED_DELAY);
                }
            }
        }
    }

    /**
     * To send Job Monitoring Report by mail
     */
    @Override
    public void sendJobMonitoringReport(final List<JnjLaCronJobMonitoringEmailDto> dtoList)
    {
        jnjLaCronJobReportService.sendCronJobMonitoringData(dtoList);
    }

    private static void updateCronJobModelData(final CronJobModel cronJobModel, final JnjLaCronJobMonitoringEmailDto emailDto)
    {
        final DateFormat df = DateFormat.getInstance();
        String endTime = StringUtils.EMPTY;
        String startTime = StringUtils.EMPTY;
        if (cronJobModel.getStatus() != null)
        {
            emailDto.setCurrentStatus(cronJobModel.getStatus().getCode());
        }
        else
        {
            emailDto.setCurrentStatus(StringUtils.EMPTY);
        }

        if (cronJobModel.getStartTime() != null)
        {
            startTime = df.format(cronJobModel.getStartTime());
        }
        emailDto.setActualStartTime(startTime);
        if (cronJobModel.getEndTime() != null)
        {
            endTime = df.format(cronJobModel.getEndTime());
        }
        emailDto.setActualEndTime(endTime);
        if (cronJobModel.getResult() != null)
        {
            emailDto.setLastStatus(cronJobModel.getResult().getCode());
        }
        else
        {
            emailDto.setLastStatus(StringUtils.EMPTY);
        }
    }

    private static void updateOtherJobStatusRemarks(final JnjLaCronJobMonitoringEmailDto emailDto,
                                                    final JnjLaCronJobMonitoringReportData monitoringReportData)
    {
        if (emailDto.getCurrentStatus().equalsIgnoreCase(ABORTED))
        {
            if (StringUtils.isNotEmpty(monitoringReportData.getStatus())
                    && monitoringReportData.getStatus().startsWith(CRONITOR_STATUS_FAILED))
            {
                emailDto.setRemarks(monitoringReportData.getStatus());
            }
            else
            {
                emailDto.setRemarks(REMARKS_FAILED);
            }
            emailDto.setCurrentStatus(FAILED);
        }
        else if (emailDto.getCurrentStatus().equalsIgnoreCase(CONTINUED) && emailDto.getLastStatus().equalsIgnoreCase(ERROR)
                && monitoringReportData.isRunning())
        {
            emailDto.setRemarks(REMARKS_RUNNING_ERROR);
        }
    }

    private static void updateFinishedJobRemarks(final JnjLaCronJobMonitoringEmailDto emailDto,
                                                 final JnjLaCronJobMonitoringReportData monitoringReportData)
    {
        if (emailDto.getLastStatus().equalsIgnoreCase(SUCCESS))
        {
            if (monitoringReportData.isRunning())
            {
                emailDto.setRemarks(REMARKS_FINISHED_WRONGSTATUS_SUCCESS);
            }
            else if (!monitoringReportData.isRunning() && monitoringReportData.isPassing())
            {
                emailDto.setRemarks(REMARKS_FINISHED_SUCCESS);
            }
            else if (!monitoringReportData.isRunning() && !monitoringReportData.isPassing()
                    && !monitoringReportData.getStatus().startsWith(CRONITOR_STATUS_FAILED))
            {
                emailDto.setRemarks(REMARKS_NOT_STARTED);
                emailDto.setCurrentStatus(NOT_STARTED);
            }
        }
    }

    private static void updateRunningJobRemarks(final JnjLaCronJobMonitoringEmailDto emailDto,
                                                final JnjLaCronJobMonitoringReportData monitoringReportData)
    {
        if (emailDto.getLastStatus().equalsIgnoreCase(SUCCESS))
        {
            if (monitoringReportData.isRunning())
            {
                if (!monitoringReportData.isPassing() & !monitoringReportData.getStatus().startsWith(CRONITOR_STATUS_FAILED))
                {
                    emailDto.setRemarks(REMARKS_RUNNING_DELAY);
                }
                else
                {
                    emailDto.setRemarks(REMARKS_RUNNING_SUCCESS);
                }
            }
            else
            {
                emailDto.setRemarks(REMARKS_RUNNING_WRONGSTATUS_SUCCESS);
            }
        }
        else if (emailDto.getLastStatus().equalsIgnoreCase(ERROR) && monitoringReportData.isRunning()
                && !monitoringReportData.isPassing())
        {
            emailDto.setRemarks(REMARKS_RUNNING_ERROR);
        }
    }

    private static int getCronitorPageCount(final String jsonString)
    {
        int pageCount = 1;

        try
        {
            final JSONParser parse = new JSONParser();
            final JSONObject jobj = (JSONObject) parse.parse(jsonString);
            final int totalMonitorCount = ((Long) jobj.get(CRONITOR_TOTAL_MONITOR_COUNT)).intValue();
            final int pageSize = ((Long) jobj.get(CRONITOR_PAGE_SIZE)).intValue();
            pageCount = totalMonitorCount / pageSize;
            if (totalMonitorCount % pageSize > 0)
            {
                pageCount++;
            }
        }
        catch (ParseException e)
        {
            LOGGER.error("Exception is" + e);
        }
        return pageCount;
    }


    /**
     * @return the jnjLaCronJobCronitorDataPopulator
     */
    public JnjLaCronJobCronitorDataPopulator getJnjLaCronJobCronitorDataPopulator()
    {
        return jnjLaCronJobCronitorDataPopulator;
    }

    /**
     * @param jnjLaCronJobCronitorDataPopulator
     *           the JnjLaCronJobCronitorDataPopulator to set
     */
    public void setJnjLaCronJobCronitorDataPopulator(final JnjLaCronJobCronitorDataPopulator jnjLaCronJobCronitorDataPopulator) {
        this.jnjLaCronJobCronitorDataPopulator = jnjLaCronJobCronitorDataPopulator;
    }
}
