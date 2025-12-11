/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */

package com.jnj.facades.converters.populator;

import java.util.Map;

import de.hybris.platform.converters.Populator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jnj.facades.data.JnjLaCronJobMonitoringReportData;

/**
 * To populate cronitor data
 */
public class JnjLaCronJobCronitorDataPopulator implements Populator<String, Map<String, JnjLaCronJobMonitoringReportData>>
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobCronitorDataPopulator.class);

    private static final String FIELD_MONITORS = "monitors";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_RUNNING = "running";
    private static final String FIELD_PASSING = "passing";
    private static final String FIELD_PAUSED = "paused";
    private static final String FIELD_DISABLED = "disabled";
    private static final String FIELD_INITIALIZED = "initialized";


    /**
     * To parse and set cronitor data in map, contains monitor code as key and JnjLaCronJobMonitoringReportData as value
     */
    @Override
    public void populate(final String source, final Map<String, JnjLaCronJobMonitoringReportData> target)
    {
        try
        {
            JSONParser parse = new JSONParser();
            final JSONObject jobj = (JSONObject) parse.parse(source);
            final JSONArray jobs = (JSONArray) jobj.get(FIELD_MONITORS);

            for (int i = 0; i < jobs.size(); i++)
            {
                JnjLaCronJobMonitoringReportData jobReportData = new JnjLaCronJobMonitoringReportData();
                JSONObject job = (JSONObject) jobs.get(i);

                jobReportData.setJobCode((String) job.get(FIELD_CODE));
                jobReportData.setJobName((String) job.get(FIELD_NAME));
                jobReportData.setStatus((String) job.get(FIELD_STATUS));
                jobReportData.setRunning(((Boolean) job.get(FIELD_RUNNING)).booleanValue());
                jobReportData.setPassing(((Boolean) job.get(FIELD_PASSING)).booleanValue());
                jobReportData.setPaused(((Boolean) job.get(FIELD_PAUSED)).booleanValue());
                jobReportData.setDisabled(((Boolean) job.get(FIELD_DISABLED)).booleanValue());
                jobReportData.setInitialized(((Boolean) job.get(FIELD_INITIALIZED)).booleanValue());

                target.put(jobReportData.getJobCode(), jobReportData);
            }
        }
        catch (ParseException e)
        {
            LOGGER.error("Exception is" + e);
        }
    }
}
