/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.jnj.core.model.JnjCreateUserProcessModel;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCreateUserEventListener extends AbstractSiteEventListener<JnjCreateUserEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final JnjCreateUserEvent createUserEvent)
	{
		final JnjCreateUserProcessModel storeFrontCustomerProcessModel = (JnjCreateUserProcessModel) getBusinessProcessService()
				.createProcess(
						"jnjCreateUserEmailProcess" + "-" + createUserEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
						"jnjCreateUserEmailProcess");
		storeFrontCustomerProcessModel.setSite(createUserEvent.getSite());
		storeFrontCustomerProcessModel.setCustomer(createUserEvent.getCustomer());
		storeFrontCustomerProcessModel.setPassword(createUserEvent.getPassword());
		storeFrontCustomerProcessModel.setLanguage(createUserEvent.getLanguage());
		storeFrontCustomerProcessModel.setCurrency(createUserEvent.getCurrency());
		storeFrontCustomerProcessModel.setStore(createUserEvent.getBaseStore());
		storeFrontCustomerProcessModel.setEmailID(createUserEvent.getCustomer().getUid());
		storeFrontCustomerProcessModel.setFirstName(createUserEvent.getFirstName());
		storeFrontCustomerProcessModel.setLastName(createUserEvent.getLastName());
		storeFrontCustomerProcessModel.setRoles(createUserEvent.getRoles());
		storeFrontCustomerProcessModel.setAdminUser(createUserEvent.getAdminUser());
		storeFrontCustomerProcessModel.setPortalName(createUserEvent.getPortalName());
		storeFrontCustomerProcessModel.setRegistrationEmailSent(createUserEvent.getRistrationEmailSent());
		storeFrontCustomerProcessModel.setLogoUrl(createUserEvent.getLogoURL());
		storeFrontCustomerProcessModel.setLoginUrl(createUserEvent.getLoginUrl());
		storeFrontCustomerProcessModel.setEmailNotification("");
		if (createUserEvent.getEmailNotification())
		{
			storeFrontCustomerProcessModel.setEmailNotification("checked='checked'");
		}
		storeFrontCustomerProcessModel.setFirstName(createUserEvent.getFirstName());

		getModelService().save(storeFrontCustomerProcessModel);
		getBusinessProcessService().startProcess(storeFrontCustomerProcessModel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjCreateUserEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2B.equals(site.getChannel());
	}

}
