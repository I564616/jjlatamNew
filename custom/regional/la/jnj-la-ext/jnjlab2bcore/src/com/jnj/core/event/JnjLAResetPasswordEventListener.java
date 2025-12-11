/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.la.core.model.JnjLAResetPasswordEmailProcessModel;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JnjLAResetPasswordEventListener extends AbstractSiteEventListener<JnjLAResetPasswordEvent> {
	
    private static final Logger LOG = Logger.getLogger(JnjGTTemporaryPasswordEventListener.class);
	private static final String RESET_TOKEN_EMAIL = "sentPasswordResetEmail()";
	protected static final String CUSTOMER_TEMPORARY_TOKEN = "customerTemporaryPassword";
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	protected static final String USER_FULL_NAME  = "userFullName";
	protected static final String TEMP_TOKEN_EMAILBODY  = "tempPwdEmailBody";
	protected static final String RESET_EMAILBODY_CONT  = "email.resetPassword.EmailBody";
	
	protected static final String TEMP_TOKEN_EMAILSUBJECT  = "tempPwdEmailSubject";
	protected static final String RESET_EMAILBODY_SUBJECT  = "email.resetPassword.EmailSubject";
	protected static final String MAIL_SUBJECT = "JJCC Password Recovery";
	
	protected BusinessProcessService businessProcessService;
	
	/** Model service **/
	protected ModelService modelService;
	
	/** I18NService to retrieve the current locale. */
	private I18NService i18nService;
	
	protected MessageFacadeUtill messageFacade;
	

	//@Override
	protected void onSiteEvent(final JnjLAResetPasswordEvent event) {

		JnjLAResetPasswordEmailProcessModel jnjLAResetPasswordEmailProcessModel = null;
		
		jnjLAResetPasswordEmailProcessModel = getBusinessProcessService().createProcess(
				"" + "-" + System.currentTimeMillis(), "jnjLAResetPasswordEmailProcess");
		
		Map<String, String> resetPasswordEmaiDataMap = null;
		/* Populating the Reset Password Data Map **/
		resetPasswordEmaiDataMap = populateResetPasswordEmaiDataMapDataMap(event);
		/** Setting the Temporary Password map in the process model */
		jnjLAResetPasswordEmailProcessModel.setJnjLAResetPasswordEmailDetails(resetPasswordEmaiDataMap);
		populateProcessModel(event, jnjLAResetPasswordEmailProcessModel);
		
	}
	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param processModel
	 */
	protected void populateProcessModel(final JnjLAResetPasswordEvent event, final JnjLAResetPasswordEmailProcessModel processModel)
	{
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(RESET_TOKEN_EMAIL, "sentResetPwdEmailToUser()", "Populating process model", LOG);
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		CommonUtil.logDebugMessage(RESET_TOKEN_EMAIL, "sentResetPwdEmailToUser()", "Starting the process", LOG);
		getBusinessProcessService().startProcess(processModel);
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD, LOG);
	}

	/**
	 * This method populates the ApprovedUser Email Data Map
	 *
	 * @param event
	 * @return approvedUserEmailDataMap
	 */
	protected Map<String, String> populateResetPasswordEmaiDataMapDataMap(final JnjLAResetPasswordEvent event)
	{
	    final String methodName = "populateResetPwdEmailDataMap()";
		String languageIsoCode = null;
		String mailContent = null;
		String mailSubject = null;
		Locale currentLocale = null;
		String mailBody = null;
		ArrayList<String> inpValues= new ArrayList<>();
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, methodName, Logging.END_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(RESET_TOKEN_EMAIL, methodName, "Setting Essential data", LOG);
		final Map<String, String> resetPasswordEmailDataMap = new HashMap<>();
		if (null != event.getCustomer())
		{
			resetPasswordEmailDataMap.put(USER_FULL_NAME, event.getCustomer().getName());
			inpValues.add(event.getCustomer().getName());
			if(event.getCustomer().getSessionLanguage() != null){
				languageIsoCode = event.getCustomer().getSessionLanguage().getIsocode();
			}else{
				languageIsoCode = event.getLanguage().getIsocode();
			}
		
			if (StringUtils.isNotBlank(languageIsoCode)) {
				currentLocale = LocaleUtils.toLocale(languageIsoCode);
			} else {
				currentLocale = i18nService.getCurrentLocale();
			}
		}
		resetPasswordEmailDataMap.put(USER_EMAIL_ADDRESS, event.getBusinessEmail());
		LOG.info("populateCreateEmailDataMap currentLocale ************************************************* : "+currentLocale);
		try {
			mailBody = messageFacade.getMessageTextForCode(RESET_EMAILBODY_CONT, currentLocale);
		} catch (BusinessException e) {
			LOG.error(e);
		}
		mailContent = String.format(mailBody, event.getCustomer().getName(), event.getResetPwdURL());
		mailSubject = MAIL_SUBJECT;
		LOG.info("populateCreateEmailDataMap mailContent ************************************************* : "+mailContent);
		LOG.info("populateCreateEmailDataMap mailSubject ************************************************* : "+mailSubject);
		resetPasswordEmailDataMap.put( TEMP_TOKEN_EMAILBODY,mailContent); 
		resetPasswordEmailDataMap.put( TEMP_TOKEN_EMAILSUBJECT,mailSubject);
		CommonUtil.logDebugMessage(RESET_TOKEN_EMAIL, methodName, "Essential data set!", LOG);
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, methodName, Logging.END_OF_METHOD, LOG);
		return resetPasswordEmailDataMap;
	}

	@Override
	protected boolean shouldHandleEvent( final JnjLAResetPasswordEvent event) {
		
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD, LOG);
		return true;
	}

	/**
	 * @return businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService
	 */
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	
	public void setI18nService(final I18NService i18nService) {
		this.i18nService = i18nService;
	}

	/**
	 * @return the messageFacade
	 */
	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

	/**
	 * @param messageFacade the messageFacade to set
	 */
	public void setMessageFacade(MessageFacadeUtill messageFacade) {
		this.messageFacade = messageFacade;
	}

}
