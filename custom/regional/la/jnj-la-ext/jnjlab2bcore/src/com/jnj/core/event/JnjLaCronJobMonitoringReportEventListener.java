/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;
import com.jnj.la.core.model.JnjLaCronJobMonitoingEmailBusinessProcessModel;


/**
 * Event Listener class responsible publish the <code>JnjLaCronJobMonitoringReportEvent</code> along with setting
 * required data from event to the business process model.
 */

public class JnjLaCronJobMonitoringReportEventListener extends AbstractSiteEventListener<JnjLaCronJobMonitoringReportEvent>
{
    /**
     * Constant instance of <codeLogger></code>.
     */
    private static final Logger LOGGER = Logger.getLogger(JnjLaCronJobMonitoringReportEventListener.class);

    /**
     * Instance of <code>ModelService</code>.
     */
    protected ModelService modelService;

    /** Business process services required to create process **/
    protected BusinessProcessService businessProcessService;

    /**
     * Setting and processing JnjLaCronJobMonitoingEmailBusinessProcessModel
     */
    @Override
    protected void onSiteEvent(final JnjLaCronJobMonitoringReportEvent event)
    {
        final JnjLaCronJobMonitoingEmailBusinessProcessModel jnjLaCronJobMonitoingEmailBusinessProcessModel = businessProcessService
                .createProcess("JnjLaCronJobMonitoingEmailBusinessProcess" + "-" + System.currentTimeMillis(),
                        "jnjLaCronJobMonitoingEmailBusinessProcess");


        /*** Populate BPM with all necessary required values. ***/

        jnjLaCronJobMonitoingEmailBusinessProcessModel.setSite(event.getSite());
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setCustomer(event.getCustomer());
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setLanguage(event.getLanguage());
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setCurrency(event.getCurrency());
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setCountry(event.getCountry());
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setUserEmailList(event.getUserEmailId());

        final List<String> cronJobMonitoringEmailData = new ArrayList<>();
        final StringBuilder stringBuilder = new StringBuilder();
        for (final JnjLaCronJobMonitoringEmailDto emailDto : event.getJnjLaCronJobMonitoringEmailDtoList())
        {
            stringBuilder.append(emailDto.getCountry()).append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(emailDto.getJobCode())
                    .append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(emailDto.getCurrentStatus())
                    .append(Jnjb2bCoreConstants.SYMBOl_PIPE)
                    .append(StringUtils.isNotEmpty(emailDto.getActualStartTime()) ? emailDto.getActualStartTime() : "null")
                    .append(Jnjb2bCoreConstants.SYMBOl_PIPE)
                    .append(StringUtils.isNotEmpty(emailDto.getActualEndTime()) ? emailDto.getActualEndTime() : "null")
                    .append(Jnjb2bCoreConstants.SYMBOl_PIPE)
                    .append(StringUtils.isNotEmpty(emailDto.getRemarks()) ? emailDto.getRemarks() : "null");

            cronJobMonitoringEmailData.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        jnjLaCronJobMonitoingEmailBusinessProcessModel.setCronJobMonitoringEmailData(cronJobMonitoringEmailData);

        try
        {
            modelService.save(jnjLaCronJobMonitoingEmailBusinessProcessModel);
        }
        catch (final ModelSavingException e)
        {
            LOGGER.error("Saving 'JnjLaCronJobMonitoingEmailBusinessProcessModel' has caused an exception: " + e);
        }
        businessProcessService.startProcess(jnjLaCronJobMonitoingEmailBusinessProcessModel);
    }

    @Override
    protected boolean shouldHandleEvent(final JnjLaCronJobMonitoringReportEvent event)
    {
        return true;
    }

    /**
     * @return the modelService
     */
    public ModelService getModelService()
    {
        return modelService;
    }

    /**
     * @param modelService
     *           the modelService to set
     */
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    /**
     * @return the businessProcessService
     */
    public BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }

    /**
     * @param businessProcessService
     *           the businessProcessService to set
     */
    public void setBusinessProcessService(final BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }
}
