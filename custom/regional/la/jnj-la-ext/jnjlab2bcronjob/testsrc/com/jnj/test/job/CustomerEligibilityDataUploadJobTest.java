/**
 * 
 */
package com.jnj.test.job;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jnj.la.cronjobs.JnjCustomerEligibilityDataUploadJob;



/**
 * Test covering high level {@link CustomerEligibilityDataUploadJob} logic.
 * 
 * @author Manoj.K.Panda
 * 
 */
@UnitTest
public class CustomerEligibilityDataUploadJobTest
{
	@Mock
	private com.jnj.la.dataload.JnJCustomerEligiblityDataLoad customerEligiblityDataLoader;

	@Mock
	private JnjCustomerEligibilityDataUploadJob performableJob;
	
	@Mock
	private ModelService modelService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		performableJob = new JnjCustomerEligibilityDataUploadJob();
		performableJob.setCustomerEligiblityDataLoader(customerEligiblityDataLoader);
	}

	@Test
	public void test()
	{
		JnjIntegrationRSACronJobModel myCronJob=modelService.create(JnjIntegrationRSACronJobModel.class);
		performableJob.perform(myCronJob);
	}

}
