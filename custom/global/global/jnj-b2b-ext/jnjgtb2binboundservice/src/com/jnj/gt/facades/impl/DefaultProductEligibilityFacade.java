/**
 *
 */
package com.jnj.gt.facades.impl;

import org.apache.log4j.Logger;

import com.jnj.gt.core.model.JnjInterfaceCronJobModel;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.facades.ProductEligibilityFacade;


/**
 * The JnjGTIntProductExclusionFacadeImpl class contains the methods which are used for clean the records and calls the
 * method for processing of intermediate records.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultProductEligibilityFacade extends JnjGTIntFacade implements ProductEligibilityFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultProductEligibilityFacade.class);

	/**
	 * Start interface feed.
	 * 
	 * Note: we've made it final because this method must be used as it is in all the feed.
	 * 
	 * @param impexFileName
	 *           the impex file name
	 */


	@Override
	public void processProductEligibilityFeed(final String impexFileName, final JnjInterfaceCronJobModel interfaceCronJob)
	{
		LOGGER.debug("START - Running cronjob from Staging to Hybris for Product Eligibility");

		getJnjGTStgSerivce().updateReadStatusForStgTables(interfaceCronJob.getAssociatedStgTables(), REC_STATUS_PENDING,
				REC_STATUS_LOADING);

		readImpexFile(impexFileName, interfaceCronJob.getCode());
		// Setting read status to processed
		getJnjGTStgSerivce().updateReadStatusForStgTables(interfaceCronJob.getAssociatedStgTables(), REC_STATUS_PROCESSED,
				REC_STATUS_PENDING);

		LOGGER.debug("END - Running cronjob from Staging to Hybris for Product Eligibility");
	}

}
