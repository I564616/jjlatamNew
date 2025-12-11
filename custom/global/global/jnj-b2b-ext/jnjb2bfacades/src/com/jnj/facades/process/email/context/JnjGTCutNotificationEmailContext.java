/*
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
import com.jnj.core.model.JnjGTCutNotificationEmailProcessModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.utils.CommonUtil;


/**
 * This class represents the context of the Cut Notification email
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCutNotificationEmailContext extends CustomerEmailContext
{

	private static final String CUT_NOTIFICATION_EMAIL = "Cut Notification Email";

	private Map<String, String> jnjGTCutNotificationEmailDetails = new HashMap<String, String>();

	private static final Logger LOG = Logger.getLogger(JnjGTCutNotificationEmailContext.class);
	
	private String emailSecureUrl;

	public String getEmailSecureUrl() {
		return emailSecureUrl;
	}

	public void setEmailSecureUrl(String emailSecureUrl) {
		this.emailSecureUrl = emailSecureUrl;
	}

	/**
	 *
	 * This method initializes the context for the cut notification email
	 */
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "START : Default initialization of email context...", LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME,
				"START : Custom initialization of email context. Now setting the email addresses...", LOG);
		JnjGTCutNotificationEmailProcessModel cutNotificationEmailProcessModel = null;

		if (storeFrontCustomerProcessModel instanceof JnjGTCutNotificationEmailProcessModel)
		{
			CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTCutNotificationEmailProcessModel", LOG);

			/** Type-casting the storeFrontCustomerProcessModel to JnjGTCutNotificationEmailProcessModel **/
			cutNotificationEmailProcessModel = (JnjGTCutNotificationEmailProcessModel) storeFrontCustomerProcessModel;
			/** Setting cut notification details **/
			setJnjGTCutNotificationEmailDetails(cutNotificationEmailProcessModel.getJnjGTCutNotificationEmailDetails());

			/** Setting "from" details **/
			put(FROM_DISPLAY_NAME, Config.getString(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_NAME,
					Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_NAME_DEFAULT));
			put(FROM_EMAIL, Config.getString(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_ADDRESS,
					Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_ADDRESS_DEFAULT));
			
			setEmailSecureUrl(JnJCommonUtil.getSecureSiteUrl());
			put(SECURE_BASE_URL, getEmailSecureUrl());
			CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "From address has been set for Cut Notification Email",
					LOG);

			/** Setting the To Email address **/
			put(EMAIL, cutNotificationEmailProcessModel.getJnjGTCutNotificationEmailDetails().get(EMAIL));
			CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "To address has been set for Cut Notification Email",
					LOG);

			CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		}
	}

	/**
	 * @return the jnjGTCutNotificationEmailDetails
	 */
	public Map<String, String> getJnjGTCutNotificationEmailDetails()
	{
		return jnjGTCutNotificationEmailDetails;
	}

	/**
	 * @param jnjGTCutNotificationEmailDetails
	 *           the jnjGTCutNotificationEmailDetails to set
	 */
	public void setJnjGTCutNotificationEmailDetails(final Map<String, String> jnjGTCutNotificationEmailDetails)
	{
		this.jnjGTCutNotificationEmailDetails = jnjGTCutNotificationEmailDetails;
	}
}
