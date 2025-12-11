/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import java.util.List;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjPasswordExpiryEmailProcessModel;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPasswordExpiryEventListener extends AbstractSiteEventListener<JnjPasswordExpiryEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjPasswordExpiryEventListener.class);
	protected static final String USER_FIRST_NAME = "userFirstName";
	protected static final String USER_LAST_NAME = "userLastName";
	
	@Autowired
	protected ModelService modelService;
	@Autowired
	protected BusinessProcessService businessProcessService;
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected BaseSiteService baseSiteService;
	@Autowired
	protected BaseStoreService baseStoreService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final JnjPasswordExpiryEvent jnjPasswordExpiryEvent)
	{
		final JnjPasswordExpiryEmailProcessModel passwordExpiryEmailProcessModel = (JnjPasswordExpiryEmailProcessModel) getBusinessProcessService()
				.createProcess(
						"jnjPasswordExpiryNotificationEmailProcess" + "-" + jnjPasswordExpiryEvent.getCustomer().getUid() + "-"
								+ System.currentTimeMillis(), "jnjPasswordExpiryNotificationEmailProcess");
		//Values from various services
		passwordExpiryEmailProcessModel.setCurrency(commonI18NService.getCurrentCurrency());
		//passwordExpiryEmailProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		//passwordExpiryEmailProcessModel.setStore(baseStoreService.getCurrentBaseStore());
		passwordExpiryEmailProcessModel.setSite(jnjPasswordExpiryEvent.getSite());
		passwordExpiryEmailProcessModel.setCustomer(jnjPasswordExpiryEvent.getCustomer());
		passwordExpiryEmailProcessModel.setLanguage(jnjPasswordExpiryEvent.getLanguage());
		passwordExpiryEmailProcessModel.setStore(jnjPasswordExpiryEvent.getBaseStore());
		passwordExpiryEmailProcessModel.setEmailID(jnjPasswordExpiryEvent.getCustomer().getUid());
		passwordExpiryEmailProcessModel.setPortalName(jnjPasswordExpiryEvent.getPortalName());
		passwordExpiryEmailProcessModel.setLogoURL(jnjPasswordExpiryEvent.getLogoURL());
		passwordExpiryEmailProcessModel.setSecurityWindow(jnjPasswordExpiryEvent.getSecurityWindow());
		passwordExpiryEmailProcessModel.setDaysBeforePasswordExpiry(jnjPasswordExpiryEvent.getDaysBeforePasswordExpiry());
		passwordExpiryEmailProcessModel.setUserFirstName(jnjPasswordExpiryEvent.getUserFirstName());
		passwordExpiryEmailProcessModel.setUserLastName(jnjPasswordExpiryEvent.getUserLastName());
		passwordExpiryEmailProcessModel.setCustomerName(jnjPasswordExpiryEvent.getCustomerName());
		passwordExpiryEmailProcessModel.setUserRoles(getUserRolesAsString(jnjPasswordExpiryEvent.getUserRoles()));
		passwordExpiryEmailProcessModel.setEmailNotifications(jnjPasswordExpiryEvent.getEmailNotifications());
		LOG.debug("user first name.." + passwordExpiryEmailProcessModel.getUserFirstName());
		LOG.debug("email notif.." + passwordExpiryEmailProcessModel.getEmailNotifications());
		LOG.debug("user role size.." + passwordExpiryEmailProcessModel.getUserRoles()==null?"":passwordExpiryEmailProcessModel.getUserRoles());
		getModelService().save(passwordExpiryEmailProcessModel);
		LOG.debug("passwordExpiryEmailProcessModel saved successfully..........");
		getBusinessProcessService().startProcess(passwordExpiryEmailProcessModel);
		LOG.debug("passwordExpiryEmailProcessModel started process successfully..........");

	}
	
	protected String getUserRolesAsString(List<String> userRolesList){
		StringBuffer userRoles = new StringBuffer();
		for(String userRole: userRolesList){
			if(userRoles.length()==0){
				userRoles.append(userRole);
			}else{
				userRoles.append(",").append(userRole);
			}
		}
		return userRoles.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjPasswordExpiryEvent arg0)
	{
		// YTODO Auto-generated method stub
		return true;
	}

}
