package com.jnj.core.integration.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjIntegrationCronJobModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.services.JnjMasterFeedService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class JnjGTIntegrationJobPerformable extends AbstractJobPerformable<JnjIntegrationCronJobModel> {


	/*
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */

	/** The jnj  data load. */
	
	
	@Autowired
	JnjMasterFeedService jnjGTMasterFeedService;
	

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjIntegrationCronJobModel.class);


	/**
	*
	*/
	@Override
	public PerformResult perform(final JnjIntegrationCronJobModel jobModel)
	{

		// Step 1 - Query Canonical Mapping Table with job code
		
		// Step 2 - Create map of source column to DTO (rest of the columns except jobCode and Source columns in Canonical mapping table)
		// Map<String, DTO>
		
		// Step 3 - Connect to Staging DB using JDBC Template
		
		// Step 4 - Query Staging DB with the query configured in Cronjob
		
		// Step 5 - Iterator through results set
		
		// Step 5(a) - Get target model instance (create new if does not exist)
		
		// Step 5(b) - Process each record
		
		// Step 5(c) - save target model
		
		jnjGTMasterFeedService.loadData(jobModel);
		
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	@Override
	public boolean isAbortable()
	{
		return true;
	}

}
