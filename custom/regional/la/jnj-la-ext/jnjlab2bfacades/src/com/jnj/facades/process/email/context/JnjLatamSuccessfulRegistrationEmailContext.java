/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTSuccessfulRegistrationEmailProcessModel;
import com.jnj.core.services.JnjConfigService;


/**
 *
 */
public class JnjLatamSuccessfulRegistrationEmailContext extends CustomerEmailContext
{

	protected Map<String, String> registrationDataMap = new HashMap<String, String>();

	protected final String USER_EMAIL_ADDRESS = "userEmailAddress";

	protected final String USER_FIRST_NAME = "userFirstName";

	protected final String USER_LAST_NAME = "userLastName";

	public static final String COMPANY_NAME = "CompanyName";

	public static final String TO_EMAIL = "toEmail";
	@Autowired
	protected UserService userService;

	/**
	 * @return the userService
	 */

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjConfigService jnjConfigService;


	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected static final Logger LOG = Logger.getLogger(JnjLatamSuccessfulRegistrationEmailContext.class);

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
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting the email addresses...");

		if (storeFrontCustomerProcessModel instanceof JnjGTSuccessfulRegistrationEmailProcessModel)
		{

			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Process Model is an instance of jnjGTSuccessfulRegistrationProcessModel");

			/** Type-casting the storeFrontCustomerProcessModel to JnjGTSuccessfulRegistrationProcessModel **/
			final JnjGTSuccessfulRegistrationEmailProcessModel jnjGTSuccessfulRegistrationProcessModel = (JnjGTSuccessfulRegistrationEmailProcessModel) storeFrontCustomerProcessModel;

			/** Setting the Registration data map in the context **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Setting the Registration data map in the context...");

			/*
			 * Map<String, String> registrationDataMap = null;
			 *
			 * registrationDataMap = jnjGTSuccessfulRegistrationProcessModel.getJnjGTRegistrationDetails();
			 *
			 * JnJB2bCustomerModel currentCustomer = null;
			 *
			 *
			 *
			 * currentCustomer = (JnJB2bCustomerModel) getUserService().getCurrentUser();
			 */
			/*
			 * String customercode = sessionService.getAttribute("CustomerCode"); registrationDataMap.put(COMPANY_NAME,
			 * customercode);
			 */



			setRegistrationDataMap(jnjGTSuccessfulRegistrationProcessModel.getJnjGTRegistrationDetails());
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Registration data map has been set!");



			put(EMAIL, registrationDataMap.get(TO_EMAIL));

			put(DISPLAY_NAME, registrationDataMap.get(TO_EMAIL));


			/** Setting the From Email Address **/
			// jjepic-601 put(FROM_EMAIL, jnjGTSuccessfulRegistrationProcessModel.getJnjGTRegistrationDetails().get(USER_EMAIL_ADDRESS));
			put(FROM_EMAIL, jnjConfigService.getConfigValueById("registerEmailFrom"));


			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, jnjConfigService.getConfigValueById("formsFromDisplayName"));
		}

		/** Setting the To Email address **/
		/*
		 * put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
		 * System.out.println("emaillllllll1" + EMAIL); put(EMAIL, "abcd@gh.com"); System.out.println("emaillllllll" +
		 * EMAIL);
		 */



		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Email addresses have been set!");
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
