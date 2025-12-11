package com.jnj.gt.job;

import com.jnj.core.services.customer.JnjGTCustomerService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;

public class JnjGTActiveUserListExtractJob  extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = Logger.getLogger(JnjGTActiveUserListExtractJob.class);
    protected JnjGTCustomerService jnjGTCustomerService;

    @Override
    public PerformResult perform(final CronJobModel cronJob) {

        final Boolean status = getJnjGTCustomerService().extractActiveUserDetailList();
        if (Boolean.TRUE.equals(status)) {
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        } else {
            LOG.error("Error performing acknowledgement Updation Job");
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }

    }

    public JnjGTCustomerService getJnjGTCustomerService() {
        return jnjGTCustomerService;
    }

    public void setJnjGTCustomerService(final JnjGTCustomerService jnjGTCustomerService) {
        this.jnjGTCustomerService = jnjGTCustomerService;
    }

}
