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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.MessageFacadeUtill;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTApproveUserEmailProcessModel;
import com.jnj.utils.CommonUtil;



/**
 * This class represents the event listener for the Approve User Email Process
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTApprovedUserEventListener extends AbstractSiteEventListener<JnjGTApprovedUserEvent>
{

	protected static final Logger LOG = Logger.getLogger(JnjGTApprovedUserEventListener.class);
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
	protected static final String HELP_URL = "helpUrl";
	protected static final String SITE_LOGO_PATH = "siteLogoPath";
	protected static final String PASSWORD_RESET_URL = "passwordResetUrl";
	protected static final String CUSTOMER_SERVICES_NUMBER = "customerServiceNumber";
	protected BusinessProcessService businessProcessService;

	/**
	 * This method is triggered when an object of the event is caught.
	 *
	 * @param JnjGTApprovedCompleteEvent
	 *           event
	 */
	@Override
	protected void onSiteEvent(final JnjGTApprovedUserEvent event)
	{

		JnjGTApproveUserEmailProcessModel jnjGTApproveUserEmailProcessModel = null;
		if (event.isCompleteProfileFlag())
		{
			jnjGTApproveUserEmailProcessModel = (JnjGTApproveUserEmailProcessModel) getBusinessProcessService().createProcess(
					"" + "-" + System.currentTimeMillis(), "jnjGTApprovedCompleteProfile");
		}
		else
		{
			jnjGTApproveUserEmailProcessModel = (JnjGTApproveUserEmailProcessModel) getBusinessProcessService().createProcess(
					"" + "-" + System.currentTimeMillis(), "jnjGTApprovedInCompleteProfile");
		}
		Map<String, String> approvedUserEmailDataMap = null;
		/** Populating the Registration Data Map **/
		approvedUserEmailDataMap = populateApprovedEmailDataMap(event);
		/** Setting the registration data map in the process model **/
		jnjGTApproveUserEmailProcessModel.setJnjGTApprovedUserEmailDetails(approvedUserEmailDataMap);
		populateProcessModel(event, jnjGTApproveUserEmailProcessModel);

	}

	/**
	 * This method populates the ApprovedUser Email Data Map
	 *
	 * @param JnjGTApprovedCompleteEvent
	 * @return approvedUserEmailDataMap
	 */
	protected Map<String, String> populateApprovedEmailDataMap(final JnjGTApprovedUserEvent event)
	{
		final String METHOD_NAME = "populateApprovedEmailDataMap()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		final Map<String, String> approvedUserEmailDataMap = new HashMap<String, String>();
		/** Setting essential data in the map **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting Essential data", LOG);
		/** Setting Business Email in the map **/
		if (null != event.getCustomer())
		{
			approvedUserEmailDataMap.put("userFullName", event.getCustomer().getName());
		}
		approvedUserEmailDataMap.put(USER_EMAIL_ADDRESS, event.getBuisnessEmail());
		/** Setting Site Url in the map **/
		approvedUserEmailDataMap.put(SITE_URL, event.getBaseUrl());
		/** Setting the site logo URL **/
		approvedUserEmailDataMap.put(SITE_LOGO_PATH, event.getLogoURL());
		approvedUserEmailDataMap.put(HELP_URL, event.getHelpUrl());
		approvedUserEmailDataMap.put(PASSWORD_RESET_URL, event.getPasswordResetUrl());
		approvedUserEmailDataMap.put(CUSTOMER_SERVICES_NUMBER, event.getCustomerServiceNumber());
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, "Essential data set!", LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.APPROVED_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return approvedUserEmailDataMap;
	}



	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTApproveUserEmailProcessModel
	 */
	protected void populateProcessModel(final JnjGTApprovedUserEvent event, final JnjGTApproveUserEmailProcessModel processModel)
	{
		CommonUtil.logMethodStartOrEnd(Logging.APPROVED_USER_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.APPROVED_USER_EMAIL, "sendApprovedEmailToUser()", "Populating process model", LOG);
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		CommonUtil.logDebugMessage(Logging.APPROVED_USER_EMAIL, "sendApprovedEmailToUser()", "Starting the process", LOG);
		getBusinessProcessService().startProcess(processModel);
		CommonUtil.logMethodStartOrEnd(Logging.APPROVED_USER_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD, LOG);
	}


	@Override
	protected boolean shouldHandleEvent(final JnjGTApprovedUserEvent paramT)
	{
		CommonUtil.logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD, LOG);
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
