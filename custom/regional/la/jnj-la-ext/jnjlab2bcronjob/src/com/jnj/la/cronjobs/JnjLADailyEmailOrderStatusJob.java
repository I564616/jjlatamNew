/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.services.JnjLAEmailOrderStatusService;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.END_OF_METHOD;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.IMMEDIATE_ORDER_EMAIL;

public class JnjLADailyEmailOrderStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel> {

    private static final Map<String, String> JOB_STORE_MAP;
    static {
        final Map<String, String> map = new HashMap<>();
        map.put("jnjLAARDailyEmailOrderStatusJob", "arCMSite");
        map.put("jnjLABRDailyEmailOrderStatusJob", "brCMSsite");
        map.put("jnjLACENCADailyEmailOrderStatusJob", "cencaCMSsite");
        map.put("jnjLACLDailyEmailOrderStatusJob", "clCMSsite");
        map.put("jnjLACODailyEmailOrderStatusJob", "coCMSsite");
        map.put("jnjLAECDailyEmailOrderStatusJob", "ecCMSsite");
        map.put("jnjLAMXDailyEmailOrderStatusJob", "mxCMSsite");
        map.put("jnjLAPEDailyEmailOrderStatusJob", "peCMSsite");
        map.put("jnjLAPRDailyEmailOrderStatusJob", "prCMSsite");
        map.put("jnjLAUYDailyEmailOrderStatusJob", "uyCMSsite");
        JOB_STORE_MAP = Collections.unmodifiableMap(map);
    }

    private JnjLAEmailOrderStatusService service;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel model) {
        JnjGTCoreUtil.logDebugMessage(IMMEDIATE_ORDER_EMAIL, "perform()", BEGIN_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        service.processPendingEmailsForDaily(JnJEmailPeriodicity.DAILY, JOB_STORE_MAP.get(model.getCode()));

        JnjGTCoreUtil.logDebugMessage(IMMEDIATE_ORDER_EMAIL, "perform()", END_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setService(final JnjLAEmailOrderStatusService service) {
        this.service = service;
    }
}