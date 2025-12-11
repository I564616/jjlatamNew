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
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjInterfaceNotificationProcessModel;


/**
 * Event Listener For JnjInterfaceNotificationEvent used during Email Process for Interface Notifications.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjInterfaceNotificationEventListner extends AbstractSiteEventListener<JnjInterfaceNotificationEvent>
{
	protected static final Logger LOGGER = Logger.getLogger(JnjInterfaceNotificationEventListner.class.getName());

	protected static final String METHOD_ON_EVENT = "Event Listner - onEvent()";

	/** The model service. */
	@Autowired
	protected ModelService modelService;

	@Autowired
	protected BusinessProcessService businessProcessService;

	
	public ModelService getModelService() {
		return modelService;
	}


	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}


	@Override
	protected void onSiteEvent(final JnjInterfaceNotificationEvent jnjInterfaceNotificationEvent)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_ON_EVENT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final JnjInterfaceNotificationProcessModel jnjInterfaceNotificationProcessModel = (JnjInterfaceNotificationProcessModel) businessProcessService
				.createProcess("interfaceEmailNotification" + "-" + System.currentTimeMillis(), "jnjInterfaceEmailProcess");

		jnjInterfaceNotificationProcessModel.setSite(jnjInterfaceNotificationEvent.getSite());
		jnjInterfaceNotificationProcessModel.setCustomer(jnjInterfaceNotificationEvent.getCustomer());
		jnjInterfaceNotificationProcessModel.setLanguage(jnjInterfaceNotificationEvent.getLanguage());
		jnjInterfaceNotificationProcessModel.setCurrency(jnjInterfaceNotificationEvent.getCurrency());
		jnjInterfaceNotificationProcessModel.setStore(jnjInterfaceNotificationEvent.getBaseStore());

		//Setting Values in Process Model from the Event Fired from Service
		jnjInterfaceNotificationProcessModel.setReadDashboardRecords(jnjInterfaceNotificationEvent.getReadDashboardRecords());
		jnjInterfaceNotificationProcessModel.setWriteDashboardRecords(jnjInterfaceNotificationEvent.getWriteDashboardRecords());

		jnjInterfaceNotificationProcessModel.setCustomer(jnjInterfaceNotificationEvent.getCustomer());

		try
		{
			modelService.save(jnjInterfaceNotificationProcessModel);
		}
		catch (final ModelSavingException e)
		{
			LOGGER.error("Saving 'JnjInterfaceNotificationProcessModel' has caused exception: " + e.getMessage());
		}

		businessProcessService.startProcess(jnjInterfaceNotificationProcessModel);

		//jnjInterfaceNotificationProcessModel.
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_ON_EVENT + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}


	@Override
	protected boolean shouldHandleEvent(final JnjInterfaceNotificationEvent arg0)
	{
		return true;
	}
}
