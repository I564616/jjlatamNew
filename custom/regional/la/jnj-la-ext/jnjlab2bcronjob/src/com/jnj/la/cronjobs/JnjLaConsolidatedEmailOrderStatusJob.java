/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.services.JnJLaConsolidatedEmailService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.CONSOLIDATED_ORDER_EMAIL;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.END_OF_METHOD;

public class JnjLaConsolidatedEmailOrderStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel> {

    private static final Map<String, String> JOB_STORE_MAP;
    static {
        final Map<String, String> map = new HashMap<>();
        map.put("jnjLaARConsolidatedEmailOrderStatusJob", "arCMSite");
        map.put("jnjLaBRConsolidatedEmailOrderStatusJob", "brCMSsite");
        map.put("jnjLaCENCAConsolidatedEmailOrderStatusJob", "cencaCMSsite");
        map.put("jnjLaCLConsolidatedEmailOrderStatusJob", "clCMSsite");
        map.put("jnjLaCOConsolidatedEmailOrderStatusJob", "coCMSsite");
        map.put("jnjLaECConsolidatedEmailOrderStatusJob", "ecCMSsite");
        map.put("jnjLaMXConsolidatedEmailOrderStatusJob", "mxCMSsite");
        map.put("jnjLaPEConsolidatedEmailOrderStatusJob", "peCMSsite");
        map.put("jnjLaPRConsolidatedEmailOrderStatusJob", "prCMSsite");
        map.put("jnjLaUYConsolidatedEmailOrderStatusJob", "uyCMSsite");
        JOB_STORE_MAP = Collections.unmodifiableMap(map);
    }

    private JnJLaConsolidatedEmailService service;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel model) {
        JnjGTCoreUtil.logDebugMessage(CONSOLIDATED_ORDER_EMAIL, "perform()", BEGIN_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        service.processPendingEmails(model, JOB_STORE_MAP.get(model.getCode()));

        JnjGTCoreUtil.logDebugMessage(CONSOLIDATED_ORDER_EMAIL, "perform()", END_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setService(final JnJLaConsolidatedEmailService service) {
        this.service = service;
    }
}
