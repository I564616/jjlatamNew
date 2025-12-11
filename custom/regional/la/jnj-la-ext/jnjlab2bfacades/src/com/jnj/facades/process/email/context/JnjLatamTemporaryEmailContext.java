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

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.la.core.model.JnjLAResetPasswordEmailProcessModel;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class JnjLatamTemporaryEmailContext extends CustomerEmailContext
{

	private static final Logger LOG = Logger.getLogger(JnjLatamTemporaryEmailContext.class);
	private static final String USER_EMAIL_ADDRESS = "userEmailAddress";

	protected ConfigurationService configurationService;

	private Map<String, String> temporaryPwdEmailDetail = new HashMap<>();
	public static final String TEMPORARY_PSWD_EMAIL = "sentTemporaryPwdEmail()";
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String methodName = "init()";
		CommonUtil
				.logMethodStartOrEnd(TEMPORARY_PSWD_EMAIL, methodName, Logging.BEGIN_OF_METHOD, LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(TEMPORARY_PSWD_EMAIL, methodName, "Setting the email addresses...", LOG);

		if (storeFrontCustomerProcessModel instanceof JnjLAResetPasswordEmailProcessModel)
		{

			CommonUtil.logDebugMessage(TEMPORARY_PSWD_EMAIL, methodName, "Process Model is an instance of JnjLAResetPasswordEmailProcessModel", LOG);

			setTemporaryPwdEmailDetail(((JnjLAResetPasswordEmailProcessModel) storeFrontCustomerProcessModel).getJnjLAResetPasswordEmailDetails());

			/** Setting the Create User Email data map in the context **/
			CommonUtil.logDebugMessage(TEMPORARY_PSWD_EMAIL, methodName, "Setting the Create User Email data map in the context...", LOG);
			CommonUtil.logDebugMessage(TEMPORARY_PSWD_EMAIL, methodName, " Create User Email data map has been set!", LOG);
			/** Setting the To Email address **/
			put(EMAIL, ((JnjLAResetPasswordEmailProcessModel) storeFrontCustomerProcessModel).getJnjLAResetPasswordEmailDetails().get(USER_EMAIL_ADDRESS));
			/** Setting the From Email address **/
			put(FROM_EMAIL, getConfigurationService().getConfiguration().getString(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, "Johnson & Johnson");
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, methodName, "Email addresses have been set!", LOG);

		}
		/*else if (storeFrontCustomerProcessModel instanceof JnjGTResetPasswordEmailProcessModel)
		{

			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.RESET_TOKEN_EMAIL, methodName, "Process Model is an instance of JnjGTResetPasswordEmailProcessModel", LOG);

			setTemporaryPwdEmailDetail(((JnjGTResetPasswordEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTResetPasswordEmailDetails());

			*//** Setting the Reset User Email data map in the context **//*
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.RESET_TOKEN_EMAIL, methodName, "Setting the Create User Email data map in the context...", LOG);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.RESET_TOKEN_EMAIL, methodName, " Create User Email data map has been set!", LOG);
			*//** Setting the To Email address **//*
			put(EMAIL, ((JnjGTResetPasswordEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTResetPasswordEmailDetails().get(USER_EMAIL_ADDRESS));
			*//** Setting the From Email address **//*
			put(FROM_EMAIL, getConfigurationService().getConfiguration().getString(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
			*//** Setting the From User display name **//*
			put(FROM_DISPLAY_NAME, "Johnson & Johnson");
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.RESET_TOKEN_EMAIL, methodName, "Email addresses have been set!", LOG);

		}*/

		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, methodName, Logging.END_OF_METHOD, LOG);
	}

	public Map<String, String> getTemporaryPwdEmailDetail() {
		return temporaryPwdEmailDetail;
	}

	public void setTemporaryPwdEmailDetail(Map<String, String> temporaryPwdEmailDetail) {
		this.temporaryPwdEmailDetail = temporaryPwdEmailDetail;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
