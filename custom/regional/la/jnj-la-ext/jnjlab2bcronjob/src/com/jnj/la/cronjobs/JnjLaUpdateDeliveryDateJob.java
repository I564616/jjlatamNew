/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.services.JnJUpdateInvoiceService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjLaUpdateDeliveryDateJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>  {

    private JnJUpdateInvoiceService invoiceService;

    @Override
    public PerformResult perform(final JnjIntegrationRSACronJobModel model) {
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "perform()", Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD, JnjLaUpdateDeliveryDateJob.class);

        invoiceService.processUploadedFiles(model);

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPDATE_DELIVERY_DATE, "perform()", Jnjlab2bcoreConstants.Logging.END_OF_METHOD, JnjLaUpdateDeliveryDateJob.class);

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public void setInvoiceService(JnJUpdateInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
}
