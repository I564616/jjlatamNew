/**
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
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.MessageFacadeUtill;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTRejectUserEmailProcessModel;
import com.jnj.utils.CommonUtil;



/**
 * This class represents the event listener for the Approve User Email Process
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTRejectUserEventListener extends AbstractSiteEventListener<JnjGTRejectUserEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTRejectUserEventListener.class);
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected MessageFacadeUtill messageFacade;
	
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public UserService getUserService() {
		return userService;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	/** Model service **/
	protected ModelService modelService;
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	protected static final String SITE_URL = "siteUrl";
	protected static final String SITE_LOGO_PATH = "siteLogoPath";
	protected static final String CUSTOMER_SERVICES_NUMBER = "customerServiceNumber";
	//Constant added to populate user name for rejected mail - Hotfix.
	protected static final String USER_FULL_NAME = "userFullName";
	protected BusinessProcessService businessProcessService;

	/**
	 * This method is triggered when an object of the event is caught.
	 * 
	 * @param JnjGTRejectUserEvent
	 *           event
	 */
	@Override
	protected void onSiteEvent(final JnjGTRejectUserEvent event)
	{

		JnjGTRejectUserEmailProcessModel JnjGTRejectUserEmailProcessModel = null;
		JnjGTRejectUserEmailProcessModel = (JnjGTRejectUserEmailProcessModel) getBusinessProcessService().createProcess(
				"" + "-" + System.currentTimeMillis(), "jnjGTRejectUserEmailProcess");
		Map<String, String> rejectUserEmailDataMap = null;
		/** Populating the Registration Data Map **/
		rejectUserEmailDataMap = populateRejectUserDataMap(event);
		/** Setting the registration data map in the process model **/
		JnjGTRejectUserEmailProcessModel.setJnjGTRejectUserEmailDetails(rejectUserEmailDataMap);
		populateProcessModel(event, JnjGTRejectUserEmailProcessModel);

	}

	/**
	 * This method populates the ApprovedUser Email Data Map
	 * 
	 * @param JnjGTRejectUserEvent
	 * @return rejectUserEmailDataMap
	 */
	protected Map<String, String> populateRejectUserDataMap(final JnjGTRejectUserEvent event)
	{
		final String METHOD_NAME = "populateApprovedEmailDataMap()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		final Map<String, String> rejectUserEmailDataMap = new HashMap<String, String>();
		/** Setting essential data in the map **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, "Setting Essential data", LOG);
		/** Setting Business Email in the map **/
		rejectUserEmailDataMap.put(USER_EMAIL_ADDRESS, event.getBuisnessEmail());
		/** Setting Site Url in the map **/
		rejectUserEmailDataMap.put(SITE_URL, event.getBaseUrl());
		/** Setting the site logo URL **/
		rejectUserEmailDataMap.put(SITE_LOGO_PATH, event.getLogoURL());
		/** Setting the site logo URL **/
		rejectUserEmailDataMap.put(CUSTOMER_SERVICES_NUMBER, event.getCustomerServiceNumber());
		/** Setting the User Name - Hotfix**/
		rejectUserEmailDataMap.put(USER_FULL_NAME, event.getCustomer().getName());
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, "Essential data set!", LOG);
		//CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REJECT_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return rejectUserEmailDataMap;
	}


	/**
	 * 
	 * This method populates the process model with essential data from the event
	 * 
	 * @param event
	 * @param JnjGTRejectUserEmailProcessModel
	 */
	protected void populateProcessModel(final JnjGTRejectUserEvent event, final JnjGTRejectUserEmailProcessModel processModel)
	{
		CommonUtil.logMethodStartOrEnd(Logging.REJECT_USER_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.REJECT_USER_EMAIL, "sendApprovedEmailToUser()", "Populating process model", LOG);
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		CommonUtil.logDebugMessage(Logging.REJECT_USER_EMAIL, "sendApprovedEmailToUser()", "Starting the process", LOG);
		getBusinessProcessService().startProcess(processModel);
		CommonUtil.logMethodStartOrEnd(Logging.REJECT_USER_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD, LOG);
	}


	@Override
	protected boolean shouldHandleEvent(final JnjGTRejectUserEvent paramT)
	{
		CommonUtil.logMethodStartOrEnd(Logging.REJECT_USER_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Logging.REJECT_USER_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD, LOG);
		return true;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
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
}
