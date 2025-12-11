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
import com.jnj.core.model.JnjGTApproveUserEmailProcessModel;
import com.jnj.utils.CommonUtil;



/**
 * This class represents the email context for the registration confirmation flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTApprovedUserEmailContext extends CustomerEmailContext
{
	private Map<String, String> approvedUserEmailDetail = new HashMap<String, String>();

	private static final Logger LOG = Logger.getLogger(JnjGTApprovedUserEmailContext.class);

	private static final String USER_EMAIL_ADDRESS = "userEmailAddress";

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		CommonUtil
				.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
				"Setting the email addresses...", LOG);

		if (storeFrontCustomerProcessModel instanceof JnjGTApproveUserEmailProcessModel)
		{

			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTApproveUserEmailProcessModel", LOG);
			setApprovedUserEmailDetail(((JnjGTApproveUserEmailProcessModel) storeFrontCustomerProcessModel)
					.getJnjGTApprovedUserEmailDetails());
			/** Setting the Approved Email data map in the context **/
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
					"Setting the Approved Email data map in the context...", LOG);
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
					" Approved Email data map has been set!", LOG);
			/** Setting the To Email address **/
			put(EMAIL,
					((JnjGTApproveUserEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTApprovedUserEmailDetails().get(
							USER_EMAIL_ADDRESS));
			/** Setting the From Email address **/
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS)); // TODO
			/** Setting the From User display name **/
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); // TODO
			CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME,
					"Email addresses have been set!", LOG);
		}
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	/**
	 * @return the approvedUserEmailDetail
	 */
	public Map<String, String> getApprovedUserEmailDetail()
	{
		return approvedUserEmailDetail;
	}

	/**
	 * @param approvedUserEmailDetail
	 *           the approvedUserEmailDetail to set
	 */
	public void setApprovedUserEmailDetail(final Map<String, String> approvedUserEmailDetail)
	{
		this.approvedUserEmailDetail = approvedUserEmailDetail;
	}


}
