/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.email.notification.JnjEmailNotificationService;
import com.jnj.core.services.customer.JnjGTCustomerService;


/**
 * This class checks the last login for every user and disable login id when last login is more than a configurable no
 * of days.It will send a warning mail before ten days of disabling the user.
 * 
 */
public class JnjGTDisableUserloginJob extends AbstractJobPerformable<CronJobModel>
{
	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTUserService;


	@Autowired
	private JnjEmailNotificationService jnjEmailNotificationService;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		jnjGTUserService.setUserDiableLogin();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


}
