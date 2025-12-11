/**
 * 
 */
package com.jnj.gt.util;

import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;


/**
 * JnjGTInboundUtil contains all the methods which are common to Inbound extension.
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjGTInboundUtil
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTInboundUtil.class);
	final static String MDD_SYS_ID = Config.getParameter(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID);
	final static String CONSUMER_USA_SYS_ID = Config.getParameter(Jnjgtb2binboundserviceConstants.CONSUMER_USA_SOURCE_SYS_ID);
	final static String CONSUMER_CANADA_SYS_ID = Config
			.getParameter(Jnjgtb2binboundserviceConstants.CONSUMER_CANADA_SOURCE_SYS_ID);

	/**
	 * Fetch valid source sys id on the basis of systemId.
	 * 
	 * @param systemId
	 *           the system id
	 * @return the string
	 */
	public static String fetchValidSourceSysId(final String systemId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("fetchValidSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		LOGGER.debug("Validating source system id.. " + systemId);
		String sourceSysId = null;
		if (MDD_SYS_ID.contains(systemId))
		{
			sourceSysId = JnjGTSourceSysId.MDD.toString();
		}
		
		  else if (CONSUMER_USA_SYS_ID.contains(systemId)) { sourceSysId = JnjGTSourceSysId.CONSUMER.toString(); }
		 
		else if (CONSUMER_CANADA_SYS_ID.contains(systemId))
		{
			sourceSysId = JnjGTSourceSysId.CONSUMER.toString();//TODO
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("fetchValidSourceSysId()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		LOGGER.debug("Source System is from " + sourceSysId);
		return sourceSysId;
	}

}
