/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.JnjGTOrderService;


/**
 * Cron-job responsible for sending Email Notification to the users of Orders having flag
 * <code>sendOrderShipmentEmail</code> set for any of the following status:
 * 
 * <ul>
 * <li>SHIPPED,or</li>
 * <li>PARTIALLY_SHIPPED,or</li>
 * <li>COMPLETED,</li>
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderShipEmailNotificationJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTOrderShipEmailNotificationJob.class);

	/**
	 * The private Instance of <code>JnjGTOrderFeedService</code>.
	 */
	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOGGER.debug(Logging.ORDER_SHIPMENT_EMAIL_NOTIFICATION + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
				+ JnJCommonUtil.getCurrentDateTime());

		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			jnjGTOrderService.sendOrderShipEmailNotification();
		}
		catch (final Exception e)
		{
			LOGGER.error("Exception while sending Order Shipment Notification mail, error message: " + e.getMessage());
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return result;
	}
}
