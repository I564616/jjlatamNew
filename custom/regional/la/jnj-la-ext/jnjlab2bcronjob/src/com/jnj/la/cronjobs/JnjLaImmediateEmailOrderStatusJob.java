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

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.END_OF_METHOD;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging.IMMEDIATE_ORDER_EMAIL;

public class JnjLaImmediateEmailOrderStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel> {

    private JnjLAEmailOrderStatusService service;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel model) {
        JnjGTCoreUtil.logDebugMessage(IMMEDIATE_ORDER_EMAIL, "perform()", BEGIN_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        service.processPendingEmailsForImmediate(model, JnJEmailPeriodicity.IMMEDIATE);

        JnjGTCoreUtil.logDebugMessage(IMMEDIATE_ORDER_EMAIL, "perform()", END_OF_METHOD, JnjLaImmediateEmailOrderStatusJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setService(final JnjLAEmailOrderStatusService service) {
        this.service = service;
    }
}