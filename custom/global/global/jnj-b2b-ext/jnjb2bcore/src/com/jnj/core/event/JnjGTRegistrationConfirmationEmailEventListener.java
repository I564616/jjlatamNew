/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTRegistrationConfirmationEmailProcessModel;


/**
 * This class represents the event listener for the registration email confirmation flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTRegistrationConfirmationEmailEventListener extends
		AbstractSiteEventListener<JnjGTRegistrationConfirmationEmailEvent>
{
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	protected static final String USER_FIRST_NAME = "userFirstName";
	protected static final String USER_LAST_NAME = "userLastName";
	protected static final String SITE_LOGO_PATH = "siteLogoPath";

	protected static final Logger LOG = Logger.getLogger(JnjGTRegistrationConfirmationEmailEventListener.class);

	/** Model service **/
	@Autowired
	protected ModelService modelService;

	/** Business process services required to create process **/
	@Autowired
	protected BusinessProcessService businessProcessService;
	

	public ModelService getModelService() {
		return modelService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	/**
	 * This method is triggered when an object of the event is caught.
	 * 
	 * @param JnjGTRegistrationConfirmationEmailEvent
	 */
	@Override
	protected void onSiteEvent(final JnjGTRegistrationConfirmationEmailEvent JnjGTRegistrationConfirmationEmailEvent)
	{
		final String METHOD_NAME = "onSiteEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating the process **/
		final JnjGTRegistrationConfirmationEmailProcessModel JnjGTRegistrationConfirmationProcessModel = (JnjGTRegistrationConfirmationEmailProcessModel) businessProcessService
				.createProcess("jnjGTRegistrationConfirmationProcess" + "-" + System.currentTimeMillis(),
						"jnjGTRegistrationConfirmationProcess");

		Map<String, String> registrationDataMap = null;

		/** Populating the Registration Data Map **/
		registrationDataMap = populateRegistrationDataMap(JnjGTRegistrationConfirmationEmailEvent);

		/** Setting the registration data map in the process model **/
		JnjGTRegistrationConfirmationProcessModel.setJnjGTRegistrationDetails(registrationDataMap);

		/** Populating the process model and then starting the process for the successful registration email **/
		populateProcessModel(JnjGTRegistrationConfirmationEmailEvent, JnjGTRegistrationConfirmationProcessModel);

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method populates the registration data map
	 * 
	 * @param JnjGTRegistrationConfirmationEmailEvent
	 * @return registrationDataMap
	 */
	protected Map<String, String> populateRegistrationDataMap(
			final JnjGTRegistrationConfirmationEmailEvent JnjGTRegistrationConfirmationEmailEvent)
	{
		final String METHOD_NAME = "populateRegistrationDataMap()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);

		final Map<String, String> registrationDataMap = new HashMap<String, String>();

		/** Setting essential data in the map **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting Essential data");

		registrationDataMap.put(USER_EMAIL_ADDRESS, JnjGTRegistrationConfirmationEmailEvent.getUserEmail());
		registrationDataMap.put(USER_FIRST_NAME, JnjGTRegistrationConfirmationEmailEvent.getUserFirstName());
		registrationDataMap.put(USER_LAST_NAME, JnjGTRegistrationConfirmationEmailEvent.getUserLastName());

		/** Setting the site logo URL **/
		registrationDataMap.put(SITE_LOGO_PATH, JnjGTRegistrationConfirmationEmailEvent.getSiteLogoURL());
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Essential data set!");

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return registrationDataMap;
	}

	/**
	 * 
	 * This method populates the process model with essential data from the event
	 * 
	 * @param event
	 * @param JnjGTRegistrationConfirmationProcessModel
	 */
	protected void populateProcessModel(final JnjGTRegistrationConfirmationEmailEvent event,
			final JnjGTRegistrationConfirmationEmailProcessModel JnjGTRegistrationConfirmationProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting essential data **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"Populating the JnjGTRegistrationConfirmationProcessModel");

		JnjGTRegistrationConfirmationProcessModel.setSite(event.getSite());
		JnjGTRegistrationConfirmationProcessModel.setCustomer(event.getCustomer());
		JnjGTRegistrationConfirmationProcessModel.setLanguage(event.getLanguage());
		JnjGTRegistrationConfirmationProcessModel.setCurrency(event.getCurrency());
		JnjGTRegistrationConfirmationProcessModel.setStore(event.getBaseStore());

		/** Saving the model **/
		modelService.save(JnjGTRegistrationConfirmationProcessModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"JnjGTRegistrationConfirmationProcessModel saved! Now starting the process - jnjGTRegistrationConfirmationProcess");

		/** Starting the process **/
		businessProcessService.startProcess(JnjGTRegistrationConfirmationProcessModel);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method simply enables this listener to handle the event when spotted. Hence default return is true.
	 * 
	 * @param JnjGTRegistrationConfirmationEmailEvent
	 * @return true
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTRegistrationConfirmationEmailEvent JnjGTRegistrationConfirmationEmailEvent)
	{
		final String METHOD_NAME = "shouldHandleEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return true;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
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
}
