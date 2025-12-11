/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
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

//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTCutNotificationEmailProcessModel;
import com.jnj.utils.CommonUtil;


/**
 * This class is the event listener for the cut notification email.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCutNotificationEmailEventListener extends AbstractSiteEventListener<JnjGTCutNotificationEmailEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTCutNotificationEmailEventListener.class);

	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected ModelService modelService;
	
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public UserService getUserService() {
		return userService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	protected BusinessProcessService businessProcessService;

	protected static final String CUT_NOTIFICATION_EMAIL = "Cut Notification Email";

	/**
	 * This method performs the email flow for the cut line notification
	 */
	@Override
	protected void onSiteEvent(final JnjGTCutNotificationEmailEvent event)
	{
		final String METHOD_NAME = "onSiteEvent()";
		final String EMAIL = "email";
		final String HYBRIS_ORDER_NUMBER = "hybrisOrderNumber";
		final String DISPLAY_ORDER_NUMBER = "displayOrderNumber";
		final String USER_FULL_NAME = "userFullName";

		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnjGTCutNotificationEmailProcessModel cutNotificationEmailProcessModel = (JnjGTCutNotificationEmailProcessModel) getBusinessProcessService()
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjGTCutNotificationEmailProcess");

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Populating CutNotificationEmail data map", LOG);
		/** Setting the cut notification data map in the process model **/

		final Map<String, String> cutNotificationEmailDetails = new HashMap<String, String>();

		cutNotificationEmailDetails.put(EMAIL, event.getUserEmailAddress());
		cutNotificationEmailDetails.put(DISPLAY_ORDER_NUMBER, event.getDisplayOrderNumber());
		cutNotificationEmailDetails.put(USER_FULL_NAME, event.getUserFullName());
		LOG.debug("Order Num ::" + event.getDisplayOrderNumber());
		cutNotificationEmailDetails.put(HYBRIS_ORDER_NUMBER, event.getHybrisOrderNumber());

		cutNotificationEmailProcessModel.setJnjGTCutNotificationEmailDetails(cutNotificationEmailDetails);

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Populating CutNotificationEmail Process Model", LOG);
		/** Populating the process model and then starting the process for the cut notification email **/
		populateAndStartProcess(event, cutNotificationEmailProcessModel);

		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	protected void populateAndStartProcess(final JnjGTCutNotificationEmailEvent event,
			final JnjGTCutNotificationEmailProcessModel processModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Populating CutNotificationEmail process model", LOG);

		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Saving the CutNotificationEmail process model", LOG);
		modelService.save(processModel);

		CommonUtil.logDebugMessage(CUT_NOTIFICATION_EMAIL, METHOD_NAME, "Starting the CutNotificationEmail process", LOG);
		getBusinessProcessService().startProcess(processModel);

		//CommonUtil.logMethodStartOrEnd(CUT_NOTIFICATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	@Override
	protected boolean shouldHandleEvent(final JnjGTCutNotificationEmailEvent arg0)
	{
		return true;
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
