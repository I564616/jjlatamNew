/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.facades.services.impl;

import com.jnj.facades.services.JnjLaCronJobReportService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.core.event.JnjLaCronJobMonitoringReportEvent;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.daos.JnjLaCronJobDao;
import com.jnj.facades.services.JnjLaCronJobReportService;
import com.jnj.la.core.services.JnjLaCronJobMonitoringExcelAttachment;

/**
 * Service to process job monitoring data
 */
public class JnjLaCronJobReportServiceImpl implements JnjLaCronJobReportService
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobReportServiceImpl.class);
    private static final String CRONITOR_URL = "jnj.cronitor.monitoring.url";
    private static final String CRONITOR_URL_PARAMETERS = "jnj.cronitor.monitoring.url.parameters";
    private static final String AUTHENTICATION_CODE = "jnj.cronitor.monitoring.url.authCode";
    private static final String JOB_MONITORING_REPORT_DATA_LIST = "jobMonitoringData";
    private static final String FILE_NAME_PREFIX = "LATAMJobMonitoring_";
    private static final String TIME_ZONE = "EST5EDT";
    private static final String TIME_ZONE_EXTN = "EST";
    private static final String DATE_FORMAT = "ddMMMyy_hha_";
    private static final String EMAIL_LIST = "jnj.la.cronjob.monitoring.emaillist";
    private static final String COUNTRY = "BR";
    private static final String FINISHED = "FINISHED";
    private static final String REMARKS_NOT_STARTED = "Job did not start";
    protected static final String SITE_LOGO_PATH = "siteLogoPath";
    protected static final String EPIC_LOGO_IMAGE_ONE = "epicEmailLogoImageOne";

    private JnjLaCronJobDao jnjLaCronJobDao;

    private CommonI18NService commonI18NService;

    private EventService eventService;

    private BaseStoreService baseStoreService;

    private BaseSiteService baseSiteService;

    private ConfigurationService configurationService;

    /**
     * To fetch and process cronitor data
     */
    @Override
    public String fetchCronJobCronitorData(final int page)
    {
        String jsonString = StringUtils.EMPTY;
        try
        {
            final String cronitorUrl = configurationService.getConfiguration().getString(CRONITOR_URL);
            final String cronitorUrlParam = configurationService.getConfiguration().getString(CRONITOR_URL_PARAMETERS);
            final String authCode = configurationService.getConfiguration().getString(AUTHENTICATION_CODE);
            final String base64Creds = Base64.getEncoder().encodeToString(authCode.getBytes());
            HttpHeaders headers = new HttpHeaders();

            headers.add("Authorization", "Basic " + base64Creds);
            final HttpEntity request = new HttpEntity(headers);
            final ResponseEntity<String> response = new RestTemplate().exchange(cronitorUrl + cronitorUrlParam + page,
                    HttpMethod.GET, request, String.class);
            jsonString = response.getBody();

        }
        catch (final HttpClientErrorException ex)
        {
            LOGGER.error("Error while pulling data from cronitor", ex);
        }
        return jsonString;
    }

    /**
     * To fetch cron job static data
     */
    @Override
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final List<String> monitoringCode)
    {
        final List<JnjLaCronJobMonitoringEmailDto> emailDtoList = jnjLaCronJobDao.fetchJobMonitoringStaticData(monitoringCode);
        if (CollectionUtils.isNotEmpty(emailDtoList))
        {
            LOGGER.info("JnjLaCronJobReportServiceImpl :: getCronJobModelByCode :: class of emailDtoList :: "
                    + emailDtoList.get(0).getClass());
        }
        return emailDtoList;
    }

    /**
     * To fetch cronj job model by job codes
     */
    @Override
    public List<CronJobModel> getCronJobModelByCode(final List<String> jobCodes)
    {
        return jnjLaCronJobDao.getCronJobModelByCode(jobCodes);
    }

    /**
     * To send Job Monitoring Report by mail
     */
    @Override
    public void sendCronJobMonitoringData(final List<JnjLaCronJobMonitoringEmailDto> dtoList)
    {
        LOGGER.info("sendCronJobMonitoringData dtoList size:" + dtoList.size());
        JnjLaCronJobMonitoringExcelAttachment jnjLaCronJobMonitoringExcelAttachment = new JnjLaCronJobMonitoringExcelAttachment();
        final Map<String, Object> outputMap = new HashMap<>();
        outputMap.put(JOB_MONITORING_REPORT_DATA_LIST, dtoList);
        final HSSFWorkbook excelFile = new HSSFWorkbook();

        final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        final String date = df.format(new Date());
        final String fileName = FILE_NAME_PREFIX + date + TIME_ZONE_EXTN;
        jnjLaCronJobMonitoringExcelAttachment.buildJobMonitoringReportExcel(outputMap, excelFile, fileName);

        final List<JnjLaCronJobMonitoringEmailDto> mailContentDtoList = getMailContentDtoList(dtoList);
        final String emailList = configurationService.getConfiguration().getString(EMAIL_LIST);

        if (StringUtils.isNotEmpty(emailList))
        {
            sendCronJobMonitoringReportEmail(mailContentDtoList, COUNTRY, emailList);
        }
        else
        {
            LOGGER.info("Email list not configured to send cronjob monitoring report");
        }
    }

    private static List<JnjLaCronJobMonitoringEmailDto> getMailContentDtoList(final List<JnjLaCronJobMonitoringEmailDto> dtoList)
    {
        List<JnjLaCronJobMonitoringEmailDto> mailContentDtoList = new ArrayList<>();
        for (JnjLaCronJobMonitoringEmailDto dto : dtoList)
        {
            if (!dto.isPaused() && (!FINISHED.equalsIgnoreCase(dto.getCurrentStatus()) || REMARKS_NOT_STARTED.equalsIgnoreCase(dto.getRemarks())))
            {
                mailContentDtoList.add(dto);
            }
        }
        LOGGER.info("mailContentDtoList size :" + mailContentDtoList.size());
        return mailContentDtoList;
    }

    private void sendCronJobMonitoringReportEmail(final List<JnjLaCronJobMonitoringEmailDto> dtoList, final String countries,
                                                  final String senderList)
    {
        final JnjLaCronJobMonitoringReportEvent event = new JnjLaCronJobMonitoringReportEvent();
        event.setCustomer(null);
        event.setSite(baseSiteService.getBaseSiteForUID(JnjLaCommonUtil.getValue(Jnjlab2bcoreConstants.CURRENT_BASE_SITE)));
        event.setLanguage(commonI18NService.getCurrentLanguage());
        event.setCurrency(commonI18NService.getCurrentCurrency());
        event.setJnjLaCronJobMonitoringEmailDtoList(dtoList);
        event.setCountry(countries);
        event.setUserEmailId(senderList);
        eventService.publishEvent(event);

    }

    /**
     * @return the jnjLaCronJobDao
     */
    public JnjLaCronJobDao getJnjLaCronJobDao()
    {
        return jnjLaCronJobDao;
    }

    /**
     * @param jnjLaCronJobDao
     *           the jnjLaCronJobDao to set
     */
    public void setJnjLaCronJobDao(final JnjLaCronJobDao jnjLaCronJobDao)
    {
        this.jnjLaCronJobDao = jnjLaCronJobDao;
    }

    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }

    public EventService getEventService()
    {
        return eventService;
    }

    public void setEventService(final EventService eventService)
    {
        this.eventService = eventService;
    }

    public BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }

    public void setBaseStoreService(final BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }

    public BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }

}
