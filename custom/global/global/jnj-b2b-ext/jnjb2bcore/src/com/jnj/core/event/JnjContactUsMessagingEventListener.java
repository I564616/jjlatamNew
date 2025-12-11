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
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.jnj.core.model.ContactUsProcessModel;


/**
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjContactUsMessagingEventListener extends AbstractSiteEventListener<JnjContactUsMessagingEvent>
{
	protected BusinessProcessService businessProcessService;
	protected ModelService modelService;
	protected static final Logger LOG = Logger.getLogger(JnjContactUsMessagingEventListener.class);

	/**
	 * This method is called when it sites the mentioned event getting published
	 * 
	 * @param jnjContactUsMessagingEvent
	 */
	@Override
	protected void onSiteEvent(final JnjContactUsMessagingEvent jnjContactUsMessagingEvent)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Contact Us Email Event Sited");
		}
		final ContactUsProcessModel contactUsProcessModel = (ContactUsProcessModel) businessProcessService.createProcess(
				"ContactUsEmailProcess" + System.currentTimeMillis(), "ContactUsEmailProcess");
		populateProcessModel(contactUsProcessModel, jnjContactUsMessagingEvent);
		modelService.save(contactUsProcessModel);
		businessProcessService.startProcess(contactUsProcessModel);
		LOG.info("Contact Us Email Process Started");
	}

	/**
	 * This method populates the process model with values from the event
	 * 
	 * @param contactUsProcessModel
	 * @param jnjContactUsMessagingEvent
	 */
	protected void populateProcessModel(final ContactUsProcessModel contactUsProcessModel,
			final JnjContactUsMessagingEvent jnjContactUsMessagingEvent)
	{
		contactUsProcessModel.setToEmailAddress(jnjContactUsMessagingEvent.getToEmailAddress());
		contactUsProcessModel.setEmailSubject(jnjContactUsMessagingEvent.getEmailSubject());
		contactUsProcessModel.setEmailOrderId(jnjContactUsMessagingEvent.getOrderNumber());
		contactUsProcessModel.setEmailBody(String.valueOf(jnjContactUsMessagingEvent.getEmailBody()));
		contactUsProcessModel.setLanguage(jnjContactUsMessagingEvent.getLanguage());
		contactUsProcessModel.setCurrency(jnjContactUsMessagingEvent.getCurrency());
		contactUsProcessModel.setCustomer(jnjContactUsMessagingEvent.getCustomer());
		contactUsProcessModel.setSite(jnjContactUsMessagingEvent.getSite());
		contactUsProcessModel.setStore(jnjContactUsMessagingEvent.getBaseStore());
	}

	/**
	 * Default return true, as there is no logic required to check if the event should be handeld by the listener or not.
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjContactUsMessagingEvent arg0)
	{
		return true;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 * 
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

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}
}
