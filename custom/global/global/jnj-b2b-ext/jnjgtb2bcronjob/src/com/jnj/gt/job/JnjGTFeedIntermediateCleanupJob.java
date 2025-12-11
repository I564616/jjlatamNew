package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jnj.gt.core.model.JnjInterfaceIntermediateCleanupCronJobModel;
import com.jnj.gt.facades.JnjGTIntFacade;


/**
 * The Class JnjGTFeedJob.
 */
public class JnjGTFeedIntermediateCleanupJob extends AbstractJobPerformable<JnjInterfaceIntermediateCleanupCronJobModel>
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjGTFeedIntermediateCleanupJob.class);

	private ArrayList<JnjGTIntFacade> cleanupFacades;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final JnjInterfaceIntermediateCleanupCronJobModel interfaceCronJob)
	{
		LOG.debug("Start Inside perform JnjInterfaceIntermediateCleanupCronJobModel Job");

		Date selectionDate = null;
		final Date retentionDate = interfaceCronJob.getDATE_RETENTION();
		final Integer retainHours = interfaceCronJob.getRETENTION();

		if (retentionDate != null)
		{
			selectionDate = retentionDate;
		}
		else if (retainHours != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, retainHours.intValue() * -1);
			selectionDate = cal.getTime();
		}
		else
		{
			LOG.error("JnjInterfaceIntermediateCleanupCronJob is not fully configured.  Please specify retention criteria in the form of hours or a specific date");
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		for (final JnjGTIntFacade currentFacade : cleanupFacades)
		{
			LOG.debug("Cleaning up with: " + currentFacade.getClass().toString());
			currentFacade.cleanInvalidRecords(null, selectionDate);
		}


		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @return the cleanupFacades
	 */
	public ArrayList<JnjGTIntFacade> getCleanupFacades()
	{
		return cleanupFacades;
	}

	/**
	 * @param cleanupFacades
	 *           the cleanupFacades to set
	 */
	public void setCleanupFacades(final ArrayList<JnjGTIntFacade> cleanupFacades)
	{
		this.cleanupFacades = cleanupFacades;
	}

}