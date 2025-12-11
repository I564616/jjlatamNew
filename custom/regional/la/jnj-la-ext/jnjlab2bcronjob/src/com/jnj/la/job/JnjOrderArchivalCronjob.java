/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.job;

import com.jnj.la.core.data.JnjArchiveOrderData;
import com.jnj.la.core.model.JnjLaOrderArchivalCronJobModel;
import com.jnj.la.core.model.JnjOrderArchivalJobConfigModel;
import com.jnj.la.facades.order.JnjOrderArchivalFacade;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JnjOrderArchivalCronjob extends AbstractJobPerformable<JnjLaOrderArchivalCronJobModel> {

    private static final Logger LOG = LoggerFactory.getLogger(JnjOrderArchivalCronjob.class);
    private JnjOrderArchivalFacade jnjOrderArchivalFacade;
    private ConfigurationService configurationService;

    @Override
    public PerformResult perform(final JnjLaOrderArchivalCronJobModel cronjobModel) {

        final List<JnjArchiveOrderData> jnjArchiveOrderDataList = new ArrayList<>();
        List<JnjOrderArchivalJobConfigModel> countryConfigList = cronjobModel.getCountryConfigList();

        if (CollectionUtils.isNotEmpty(countryConfigList)) {
            countryConfigList.stream().forEach(countryConfig -> {
                if(BooleanUtils.isTrue(countryConfig.getActive())){
                    JnjArchiveOrderData jnjArchiveOrderData = getJnjOrderArchivalFacade().archiveOrdersByCountry(countryConfig);
                    if(null!= jnjArchiveOrderData.getRecordsDeleted() && jnjArchiveOrderData.getRecordsDeleted() > 0)  {
                        jnjArchiveOrderDataList.add(jnjArchiveOrderData);
                    }
                }
            });
        } else {
            LOG.error("Order List size is 0.Please check the input parameters for sectors and countries");
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }

        if (clearAbortRequestedIfNeeded(cronjobModel)) {
            saveArchivalResult(jnjArchiveOrderDataList);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }

        saveArchivalResult(jnjArchiveOrderDataList);
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private void saveArchivalResult(final List<JnjArchiveOrderData> jnjArchiveOrderDataList) {
        for(JnjArchiveOrderData jnjArchiveOrderData:jnjArchiveOrderDataList){
            getJnjOrderArchivalFacade().saveArchiveResult(jnjArchiveOrderData);
        }
    }


    @Override
    public boolean isAbortable() {
        return true;
    }

    /**
     * @return the JnjOrderArchivalFacade
     */
    public JnjOrderArchivalFacade getJnjOrderArchivalFacade() {
        return jnjOrderArchivalFacade;
    }

    /**
     * @param jnjOrderArchivalFacade the JnjOrderArchivalFacade to set
     */
    public void setJnjOrderArchivalFacade(final JnjOrderArchivalFacade jnjOrderArchivalFacade) {
        this.jnjOrderArchivalFacade = jnjOrderArchivalFacade;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}