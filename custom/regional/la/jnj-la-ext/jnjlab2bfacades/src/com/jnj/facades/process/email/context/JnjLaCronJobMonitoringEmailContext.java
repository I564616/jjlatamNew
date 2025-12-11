/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.la.constants.Jnjlab2binboundserviceConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.la.core.model.JnjLaCronJobMonitoingEmailBusinessProcessModel;


public class JnjLaCronJobMonitoringEmailContext extends AbstractEmailContext<BusinessProcessModel>
{
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobMonitoringEmailContext.class);
    private static final String TRUE = "true";
    private static final String COUNTRY = "country";
    private static final String NULL_VALUE = "null";
    private static final String FILENAMEPREFIX = "LATAMJobMonitoring_";
    private static final String FILE_EXTENSION = ".xls";
    private static final String TIME_ZONE = "EST5EDT";
    private static final String TIME_ZONE_EXTN = "EST";
    private static final String DATE_FORMAT = "ddMMMyy_hha_";
    private static final String REPORT_DATE = "reportDate";


    private List<JnjLaCronJobMonitoringEmailDto> jobMonitoringEmailDtoList;

    private ConfigurationService configurationService;

    @Override
    public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
    {

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + "Start of Method-init()" + Logging.HYPHEN
                    + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
        }

        super.init(businessProcessModel, emailPageModel);
        if (businessProcessModel instanceof JnjLaCronJobMonitoingEmailBusinessProcessModel)
        {
            final JnjLaCronJobMonitoingEmailBusinessProcessModel cronJobMonitoringEmailProcessModel = (JnjLaCronJobMonitoingEmailBusinessProcessModel) businessProcessModel;

            setJobMonitoringEmailDtoList(createEmailDto(cronJobMonitoringEmailProcessModel));

            put(EMAIL, cronJobMonitoringEmailProcessModel.getUserEmailList());// configured email address
            put(DISPLAY_NAME, "");// configured Display Names
            put(FROM_EMAIL, configurationService.getConfiguration()
                    .getString(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
            /** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
            put(FROM_DISPLAY_NAME, configurationService.getConfiguration().getString(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_NAME));
            put(COUNTRY, cronJobMonitoringEmailProcessModel.getCountry());

            final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            final String date = df.format(new Date());
            final String fileName = FILENAMEPREFIX + date + TIME_ZONE_EXTN + FILE_EXTENSION;
            final String filePath = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.JOB_MONITORING_EMAIL_ATTACHMENT_PATH);
            LOGGER.info(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH +" :" + filePath);
            LOGGER.info(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME + " :" + fileName);
            put(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH, filePath);
            put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME, fileName.trim());
            put(Jnjb2bCoreConstants.USE_DIRECT_PATH, TRUE);
            put(Jnjb2bCoreConstants.DELETE_FILE, Boolean.TRUE);
            put(REPORT_DATE, date + TIME_ZONE_EXTN);
        }
    }

    private static List<JnjLaCronJobMonitoringEmailDto> createEmailDto(
            final JnjLaCronJobMonitoingEmailBusinessProcessModel cronJobMonitoringEmailProcessModel)
    {
        final List<JnjLaCronJobMonitoringEmailDto> emailDtoList = new ArrayList<>();
        for (final String data : cronJobMonitoringEmailProcessModel.getCronJobMonitoringEmailData())
        {
            final JnjLaCronJobMonitoringEmailDto dto = new JnjLaCronJobMonitoringEmailDto();
            final String[] dataArray = StringUtils.split(data, Jnjb2bCoreConstants.SYMBOl_PIPE);
            dto.setCountry(dataArray[0]);
            dto.setJobCode(dataArray[1]);
            dto.setCurrentStatus(NULL_VALUE.equals(dataArray[2]) ? StringUtils.EMPTY : dataArray[2]);
            dto.setActualStartTime(NULL_VALUE.equals(dataArray[3]) ? StringUtils.EMPTY : dataArray[3]);
            dto.setActualEndTime(NULL_VALUE.equals(dataArray[4]) ? StringUtils.EMPTY : dataArray[4]);
            dto.setRemarks(NULL_VALUE.equals(dataArray[5]) ? StringUtils.EMPTY : dataArray[5]);
            emailDtoList.add(dto);
        }
        return emailDtoList;
    }


    @Override
    protected BaseSiteModel getSite(final BusinessProcessModel businessProcessModel)
    {
        // YTODO Auto-generated method stub
        return null;
    }

    @Override
    protected CustomerModel getCustomer(final BusinessProcessModel businessProcessModel)
    {
        // YTODO Auto-generated method stub
        return null;
    }

    @Override
    protected LanguageModel getEmailLanguage(final BusinessProcessModel businessProcessModel)
    {
        // YTODO Auto-generated method stub
        if(businessProcessModel instanceof  JnjLaCronJobMonitoingEmailBusinessProcessModel)
        {
            return ((JnjLaCronJobMonitoingEmailBusinessProcessModel) businessProcessModel).getLanguage();
        }
        return null;
    }

    /**
     * @return the jobMonitoringEmailDtoList
     */
    public List<JnjLaCronJobMonitoringEmailDto> getJobMonitoringEmailDtoList()
    {
        return jobMonitoringEmailDtoList;
    }

    /**
     * @param jobMonitoringEmailDtoList the jobMonitoringEmailDtoList to set
     */
    public void setJobMonitoringEmailDtoList(final List<JnjLaCronJobMonitoringEmailDto> jobMonitoringEmailDtoList)
    {
        this.jobMonitoringEmailDtoList = jobMonitoringEmailDtoList;
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
