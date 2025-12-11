package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.gt.core.model.JnjInterfaceCronJobModel;
import com.jnj.gt.facades.JnjGTIntFacade;


/**
 * The Class JnjGTFeedJob.
 */
public class JnjGTFeedJob extends AbstractJobPerformable<JnjInterfaceCronJobModel>
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjGTFeedJob.class);



	/** The int facade map. */
	private final Map<String, JnjGTIntFacade> intFacadeMap = new HashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final JnjInterfaceCronJobModel interfaceCronJob)
	{
		LOG.debug("Strart Inside perform JnjGTFeedJob Job");

		final String impexFilePath = interfaceCronJob.getImpexFile();
		final String facadeBeanId = interfaceCronJob.getFacadeBean();

		LOG.debug("Inside perform JnjGTFeedJob Job facadeBeanId: " + facadeBeanId + "  impexFilePath:" + impexFilePath);

		final JnjGTIntFacade intFacade = getIntFacadeImpl(facadeBeanId);
		intFacade.startInterfaceFeed(impexFilePath, interfaceCronJob);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	/**
	 * This method is used to get the implementation "Feed Abst. facade".
	 * 
	 * @param facadeBeanId
	 *           beanId configured in the cronJob Model
	 * @return JnjGTIntFacade bean class w.r.t. bean Id.
	 */
	public JnjGTIntFacade getIntFacadeImpl(final String facadeBeanId)
	{
		return (this.intFacadeMap.get(facadeBeanId));
	}

	/**
	 * Setter method for property intFacadeMap
	 * 
	 * @param intFacadeMap
	 *           the int facade map
	 */
	public void setIntFacadeMap(final Map<String, JnjGTIntFacade> intFacadeMap)
	{
		this.intFacadeMap.clear();
		if ((intFacadeMap == null) || (intFacadeMap.isEmpty()))
		{
			return;
		}
		this.intFacadeMap.putAll(intFacadeMap);
	}

}