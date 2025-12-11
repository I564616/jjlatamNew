/**
 * 
 */
package com.jnj.gt.service.surgeon.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.surgeon.JnjGTSurgeonDao;
import com.jnj.gt.service.surgeon.JnjGTSurgeonFeedService;


/**
 * @author sakshi.kashiva
 * 
 */
public class DefaultJnjGTSurgeonFeedService implements JnjGTSurgeonFeedService
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSurgeonFeedService.class);

	@Autowired
	private JnjGTSurgeonDao jnjGTSurgeonDao;

	@Autowired
	private ModelService modelService;

	/**
	 * Gets the surgeon model by id.
	 * 
	 * @param surgeonId
	 *           the surgeon id
	 * @return the surgeon model
	 */
	@Override
	public JnjGTSurgeonModel getJnjGTSurgeonModelById(final String surgeonId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTSurgeonModelById()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTSurgeonModelById()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTSurgeonDao.getJnjSurgeonModelById(surgeonId);
	}
}
