/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.services.order.impl.JnjLAOrderDataServiceImpl;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjLaUpdateOrderStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>  {

    private JnjLAOrderDataServiceImpl orderDataService;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel) {

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_ORDER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD, JnjLaUpdateOrderStatusJob.class);

        orderDataService.processOrderEntriesStatus(cronJobModel);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_ORDER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.END_OF_METHOD, JnjLaUpdateOrderStatusJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setOrderDataService(JnjLAOrderDataServiceImpl orderDataService) {
        this.orderDataService = orderDataService;
    }
}
