/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTRegistrationConfirmationEmailProcessModel;


/**
 * This class represents the email context for the registration confirmation flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTRegistrationConfirmationEmailContext extends CustomerEmailContext
{
	protected Map<String, String> registrationDataMap = new HashMap<String, String>();

	protected final String USER_EMAIL_ADDRESS = "userEmailAddress";

	protected static final Logger LOG = Logger.getLogger(JnjGTSuccessfulRegistrationEmailContext.class);

	/**
	 * This method initializes the email context
	 * 
	 */
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		JnjGTRegistrationConfirmationEmailProcessModel jnjGTRegistrationConfirmationProcessModel = null;

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting the email addresses...");

		if (storeFrontCustomerProcessModel instanceof JnjGTRegistrationConfirmationEmailProcessModel)
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Process Model is an instance of jnjGTRegistrationConfirmationProcessModel");

			/** Type-casting the storeFrontCustomerProcessModel to jnjGTRegistrationConfirmationProcessModel **/
			jnjGTRegistrationConfirmationProcessModel = (JnjGTRegistrationConfirmationEmailProcessModel) storeFrontCustomerProcessModel;

			/** Setting the Registration data map in the context **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Setting the Registration data map in the context...");
			setRegistrationDataMap(jnjGTRegistrationConfirmationProcessModel.getJnjGTRegistrationDetails());
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Registration data map has been set!");

			/** Setting the To Email address **/
			put(EMAIL, jnjGTRegistrationConfirmationProcessModel.getJnjGTRegistrationDetails().get(USER_EMAIL_ADDRESS));

			/** Setting the From Email address **/
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS)); // TODO

			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Email addresses have been set!");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * @return the registrationDataMap
	 */
	public Map<String, String> getRegistrationDataMap()
	{
		return registrationDataMap;
	}

	/**
	 * @param registrationDataMap
	 *           the registrationDataMap to set
	 */
	public void setRegistrationDataMap(final Map<String, String> registrationDataMap)
	{
		this.registrationDataMap = registrationDataMap;
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
