package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.enums.ResetPwdJobAction;
import com.jnj.gt.model.job.JnjGTUserResetPwdCronJobModel;
import com.jnj.core.services.customer.JnjGTCustomerService;



/**
 * @author komal.sehgal
 * 
 */

public class JnjGTGenerateResetPasswordDetailsJob extends AbstractJobPerformable<JnjGTUserResetPwdCronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTGenerateResetPasswordDetailsJob.class);

	/**
	 * This method is responsible for generating token and URL for the resetting the password.
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final JnjGTUserResetPwdCronJobModel resetPasswordModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.RESET_PASSWORD_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			if (resetPasswordModel.getResetPwdJobAction().equals(ResetPwdJobAction.RESET_PASSWORD_URL))
			{
				jnjGTCustomerService.resetPasswordForUserWithGenerateTokenTrue();
			}
			else if (resetPasswordModel.getResetPwdJobAction().equals(ResetPwdJobAction.SEND_ACTIVATION_EMAIL))
			{
				jnjGTCustomerService.sendActivationEmail();
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.RESET_PASSWORD_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Throwable Exception occured " + throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}
}
