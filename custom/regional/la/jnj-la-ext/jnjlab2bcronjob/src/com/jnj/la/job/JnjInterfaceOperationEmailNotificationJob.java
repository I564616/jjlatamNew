/**
 *
 */
package com.jnj.la.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;


/**
 * The Class JnjInterfaceOperationEmailNotificationJob.
 */
public class JnjInterfaceOperationEmailNotificationJob extends AbstractJobPerformable<CronJobModel>
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjInterfaceOperationEmailNotificationJob.class);

	/** The jnj interface operation arch utility. */
	private JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			jnjInterfaceOperationArchUtility.sendEmailNotification();
		}
		catch (final Exception e)
		{
			LOGGER.error(e);
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return result;
	}

	/**
	 * Sets the jnj interface operation arch utility.
	 *
	 * @param jnjInterfaceOperationArchUtility
	 *           the new jnj interface operation arch utility
	 */
	public void setJnjInterfaceOperationArchUtility(final JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility)
	{
		this.jnjInterfaceOperationArchUtility = jnjInterfaceOperationArchUtility;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
