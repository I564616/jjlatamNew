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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjRegisterData;
import com.jnj.core.model.JnjRegistrationProcessModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjRegisterEventListener extends AbstractSiteEventListener<JnjRegisterEvent>
{


	protected static final String CUSTOMER_CODE = "customerCode";
	protected static final String CUSTOMER_NAME = "customerName";
	protected static final String REGISTER_EMAIL_SUBJECT = "register.email.subject";
	protected static final String SUBJECT = "subject";
	protected static final String ACCOUNT_MANAGER = "accountManager";
	protected static final String COUNTRYNAME = "countryName";
	protected static final String EMAIL = "email";
	protected static final String COUNTRYISOCODE = "countryIsoCode";
	protected static final String SITE_LOGO_PATH = "siteLogoPath";
	protected static final Logger LOG = Logger.getLogger(JnjRegisterEventListener.class);
	protected ModelService modelService;
	protected BusinessProcessService businessProcessService;

	@Autowired
	protected CommonI18NService commonI18NService;

	@Autowired
	protected MessageFacadeUtill messageFacade;
	
	

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}

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
	protected void onSiteEvent(final JnjRegisterEvent event)
	{

		//logMethodStartOrEnd(Logging.REGISTER_EMAIL, "onSiteEvent()", Logging.BEGIN_OF_METHOD);

		logDebugMessage(Logging.REGISTER_EMAIL, "sendEmailToUser()", "Start setting the process Model");


		final JnjRegistrationProcessModel jnjRegistrationProcessModel = (JnjRegistrationProcessModel) getBusinessProcessService()
				.createProcess("s" + "-" + System.currentTimeMillis(), "jnjRegisterEmailProcess");

		final JnjRegistrationProcessModel jnjRegistrationUserProcessModel = (JnjRegistrationProcessModel) getBusinessProcessService()
				.createProcess("s" + "-" + System.currentTimeMillis(), "jnjRegisterUserEmailProcess");



		final JnjRegisterData jnjRegisterData = event.getRegisterData();
		final Map<String, Object> jnjRegisterCustomer = new HashMap<String, Object>();
		jnjRegisterCustomer.put(CUSTOMER_NAME, jnjRegisterData.getCustName());
		jnjRegisterCustomer.put(EMAIL, jnjRegisterData.getEmail());
		jnjRegisterCustomer.put(COUNTRYISOCODE, commonI18NService.getCountry(jnjRegisterData.getCountry()).getIsocode());
		jnjRegisterCustomer.put(COUNTRYNAME, commonI18NService.getCountry(jnjRegisterData.getCountry()).getName());
		jnjRegisterCustomer.put(SITE_LOGO_PATH, event.getSiteLogoURL());
		try
		{
			jnjRegisterCustomer.put(SUBJECT, messageFacade.getMessageTextForCode(REGISTER_EMAIL_SUBJECT));
		}
		catch (final BusinessException exp)
		{

			LOG.error("Unable to ftch subject message for ::" + REGISTER_EMAIL_SUBJECT);
		}

		jnjRegistrationProcessModel.setJnjCustomerFormMap(jnjRegisterCustomer);
		populateProcessModel(event, jnjRegistrationProcessModel);

		jnjRegistrationUserProcessModel.setJnjCustomerFormMap(jnjRegisterCustomer);
		populateProcessModel(event, jnjRegistrationUserProcessModel);

	}

	protected void populateProcessModel(final JnjRegisterEvent event, final JnjRegistrationProcessModel jnjRegistrationProcessModel)
	{
		//logMethodStartOrEnd(Logging.REGISTER_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.REGISTER_EMAIL, "sendEmailToUser()", "Populating process model");
		jnjRegistrationProcessModel.setSite(event.getSite());
		jnjRegistrationProcessModel.setCustomer(event.getCustomer());
		jnjRegistrationProcessModel.setLanguage(event.getLanguage());
		jnjRegistrationProcessModel.setCurrency(event.getCurrency());
		jnjRegistrationProcessModel.setStore(event.getBaseStore());
		getModelService().save(jnjRegistrationProcessModel);
		logDebugMessage(Logging.REGISTER_EMAIL, "sendEmailToUser()", "Starting the process");
		getBusinessProcessService().startProcess(jnjRegistrationProcessModel);
		//logMethodStartOrEnd(Logging.REGISTER_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjRegisterEvent paramT)
	{
		//logMethodStartOrEnd(Logging.REGISTER_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Logging.REGISTER_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD);
		return true;
	}

	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

}
