/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTProfilePasswordExpireEmailProcessModel;


/**
 * @author himanshi.batra
 * 
 */
public class JnjGTProfilePasswordExpiryEventListner extends AbstractSiteEventListener<JnjGTProfilePasswordExpiryEvent>
{


	protected static final Logger LOG = Logger.getLogger(JnjGTProfilePasswordExpiryEventListner.class);
	protected static final String LOGO_URL = "logoUrl";
	protected static final String PASSWORDEXPIRY_URL = "passwordexpiry";
	protected static final String BUISNESS_EMAIL = "buisnessemail";
	protected static final String FOR_PCM = "isFromPCM";
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected UserService userService;

	
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}



	public UserService getUserService() {
		return userService;
	}


	protected ModelService modelService;

	protected BusinessProcessService businessProcessService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final JnjGTProfilePasswordExpiryEvent event)
	{
		Map<String, String> PasswordexpiryDataMap = null;
		final JnjGTProfilePasswordExpireEmailProcessModel JnjGTProfilePasswordExpireEmailProcessModel = (JnjGTProfilePasswordExpireEmailProcessModel) getBusinessProcessService()
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjGTProfilePasswordExpireEmailProcess");

		/** Populating the Registration Data Map **/
		PasswordexpiryDataMap = populatePasswordExpiryDataMap(event);
		/** Setting the registration data map in the process model **/
		JnjGTProfilePasswordExpireEmailProcessModel.setJnjGTPasswordExpireEmailDetails(PasswordexpiryDataMap);
		/** Populating the process model and then starting the process for the successful registration email **/
		populateProcessModel(event, JnjGTProfilePasswordExpireEmailProcessModel);

	}



	protected Map<String, String> populatePasswordExpiryDataMap(final JnjGTProfilePasswordExpiryEvent event)
	{
		final String METHOD_NAME = "populatePasswordExpiryDataMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);

		final Map<String, String> passwordExpiryDataMap = new HashMap<String, String>();

		/** Setting essential data in the map **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, "Setting Essential data");
		passwordExpiryDataMap.put(LOGO_URL, event.getLogoUrl());
		passwordExpiryDataMap.put(BUISNESS_EMAIL, event.getBuisnessEmail());
		passwordExpiryDataMap.put(PASSWORDEXPIRY_URL, event.getPasswordExpiryUrl());
		passwordExpiryDataMap.put(FOR_PCM, event.getForPCMFlag());

		/** Setting the site logo URL **/

		logDebugMessage(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, "Essential data set!");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PASSWORD_EXPIRY_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return passwordExpiryDataMap;
	}

	protected void populateProcessModel(final JnjGTProfilePasswordExpiryEvent event,
			final JnjGTProfilePasswordExpireEmailProcessModel processModel)
	{
		//logMethodStartOrEnd(Logging.PASSWORD_EXPIRY_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.PASSWORD_EXPIRY_EMAIL, "sendEmailToUser()", "Populating process model");
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		logDebugMessage(Logging.PASSWORD_EXPIRY_EMAIL, "populateProcessModel()", "Starting the process");
		getBusinessProcessService().startProcess(processModel);
		//logMethodStartOrEnd(Logging.PASSWORD_EXPIRY_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTProfilePasswordExpiryEvent paramT)
	{
		//logMethodStartOrEnd(Logging.PASSWORD_EXPIRY_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Logging.PASSWORD_EXPIRY_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD);
		return true;
	}

	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}


	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}



}
