/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.services.order.JnjLAOrderDataService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjLaUpdateOrderHeaderStatusJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>  {

    private static final Class<JnjLaUpdateOrderHeaderStatusJob> THIS_CLASS = JnjLaUpdateOrderHeaderStatusJob.class;

    private JnjLAOrderDataService orderDataService;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel cronJobModel) {
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_ORDER_HEADER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

        orderDataService.processOrdersHeaderStatus(cronJobModel);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_ORDER_HEADER_STATUS, "perform()", Jnjlab2bcoreConstants.Logging.END_OF_METHOD, THIS_CLASS);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setOrderDataService(JnjLAOrderDataService orderDataService) {
        this.orderDataService = orderDataService;
    }

}
