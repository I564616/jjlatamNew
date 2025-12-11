/**
 *
 */
package com.jnj.la.cronjobs;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.jnj.la.dataload.JnJCustomerEligiblityDataLoad;


/**
 * Job responsible to perform Customer Eligibility Data load.
 *
 * @author Manoj.K.Panda
 */
public class JnjCustomerEligibilityDataUploadJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{
	private static final Logger LOG = Logger.getLogger(JnjCustomerEligibilityDataUploadJob.class);
	private JnJCustomerEligiblityDataLoad customerEligiblityDataLoader;

	/**
	 * @return the customerEligiblityDataLoader
	 */
	public JnJCustomerEligiblityDataLoad getCustomerEligiblityDataLoader()
	{
		return customerEligiblityDataLoader;
	}

	public void setCustomerEligiblityDataLoader(final JnJCustomerEligiblityDataLoad customerEligiblityDataLoader)
	{
		this.customerEligiblityDataLoader = customerEligiblityDataLoader;
	}

	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel arg0)
	{
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			customerEligiblityDataLoader.loadCustomerEligiblityData(arg0);
		}
		catch (final Exception e)
		{
			LOG.error("Customer Eligiblity Cronjob computation caused an exception : " + e);
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return result;
	}


}
