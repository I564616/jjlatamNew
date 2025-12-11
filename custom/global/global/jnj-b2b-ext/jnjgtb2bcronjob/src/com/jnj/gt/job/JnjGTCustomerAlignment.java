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
import com.jnj.core.services.territory.JnjGTTerritoryService;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTCustomerAlignment extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTCustomerAlignment.class);

	@Autowired
	private JnjGTTerritoryService jnjGTTerritoryService;

	/**
	 * This method is responsible for aligning the New Customer To The Territory
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_ALIGNMENT + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		try
		{
			jnjGTTerritoryService.alignCustomerWithTerritory();
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.CUSTOMER_ALIGNMENT + Logging.HYPHEN + "perform()" + Logging.HYPHEN + "Throwable Exception occured "
					+ throwable.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}
}
