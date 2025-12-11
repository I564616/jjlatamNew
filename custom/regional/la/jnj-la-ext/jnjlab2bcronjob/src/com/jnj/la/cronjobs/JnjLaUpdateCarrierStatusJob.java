/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.services.JnJUpdateCarrierStatusService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjLaUpdateCarrierStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel> {

    private JnJUpdateCarrierStatusService carrierStatusService;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel model) {
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_CARRIER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD, JnjLaUpdateCarrierStatusJob.class);

        carrierStatusService.processPendingOrderEntries(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_CARRIER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.END_OF_METHOD, JnjLaUpdateCarrierStatusJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setCarrierStatusService(final JnJUpdateCarrierStatusService carrierStatusService) {
        this.carrierStatusService = carrierStatusService;
    }
}
