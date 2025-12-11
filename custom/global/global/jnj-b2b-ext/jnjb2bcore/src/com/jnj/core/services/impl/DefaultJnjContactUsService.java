/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjContactUsDTO;
import com.jnj.core.event.JnjContactUsMessagingEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjContactUsService;


/**
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjContactUsService implements JnjContactUsService
{
	@Autowired
	protected JnjConfigServiceImpl jnjConfigServiceImpl;
	
	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	protected EventService eventService;
	protected BaseStoreService baseStoreService;
	protected BaseSiteService baseSiteService;
	protected CommonI18NService commonI18NService;
	protected UserService userService;

	protected static final Logger LOG = Logger.getLogger(JnjConfigServiceImpl.class);
	protected static final Map<String, String> subjectsMap = new HashMap<String, String>()
	{
		{
			put(Jnjb2bCoreConstants.ContactUs.PLACE_ORDER, Jnjb2bCoreConstants.ContactUs.PLACE_ORDER_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.TRACKING_ORDER, Jnjb2bCoreConstants.ContactUs.TRACKING_ORDER_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.BID_OPPORTUNITY, Jnjb2bCoreConstants.ContactUs.BID_OPPORTUNITY_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.BID_ORDER, Jnjb2bCoreConstants.ContactUs.BID_ORDER_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.BID_UPDATE, Jnjb2bCoreConstants.ContactUs.BID_UPDATE_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.ACCOUNT_MANAGEMENT, Jnjb2bCoreConstants.ContactUs.ACCOUNT_MANAGEMENT_SHORT);
			put(Jnjb2bCoreConstants.ContactUs.OTHER, Jnjb2bCoreConstants.ContactUs.OTHER_SHORT);
		}
	};

	/**
	 * Method responsible for fetching the email address of the back-office user
	 * 
	 * @param subject
	 * 
	 * @return emailAddress
	 */
	@Override
	public String fetchEmailAddress(final String subject)
	{
		String toEmailAddress;

		// Fetching the B2B Unit
		final JnJB2bCustomerModel jnjB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel jnjB2BUnitModel = (JnJB2BUnitModel) jnjB2bCustomerModel.getDefaultB2BUnit();
		// Fetching the country
		final CountryModel country = jnjB2BUnitModel.getCountry();

		if (null != country)
		{
			final String countryCode = country.getIsocode();
			final String key = countryCode + Jnjb2bCoreConstants.ContactUs.UNDERSCORE_SYMBOL + subjectsMap.get(subject);

			LOG.info("Calling DAO Layer for fetching Contact Us email address :: key - " + key);
			toEmailAddress = jnjConfigServiceImpl.getConfigValueById(key);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("toEmailAddress fetched :: " + toEmailAddress);
			}
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Country was found to be null, returning a null email address");
			}
			toEmailAddress = null;
		}
		return toEmailAddress;
	}

	/**
	 * This method is responsible for sending the email for the contact us functionality.
	 * 
	 * @param jnjContactUsDTO
	 * 
	 * @return true/false
	 */
	@Override
	public boolean sendMessage(final JnjContactUsDTO jnjContactUsDTO)
	{
		boolean status = false;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Populating the event object");
		}
		final JnjContactUsMessagingEvent jnjContactUsMessagingEvent = new JnjContactUsMessagingEvent();

		// Populating the event 
		populateEvent(jnjContactUsMessagingEvent, jnjContactUsDTO);

		LOG.info("Contact Us Messaging Event populated with following data - ToEmailAddress :: "
				+ jnjContactUsMessagingEvent.getToEmailAddress() + "; EmailBody :: " + jnjContactUsMessagingEvent.getEmailBody()
				+ "; EmailSubject :: " + jnjContactUsMessagingEvent.getEmailSubject() + "; OrderNumber :: "
				+ jnjContactUsMessagingEvent.getOrderNumber() + "; CustomerEmailAddress :: "
				+ jnjContactUsMessagingEvent.getCustomerEmail());

		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Going to publish contact us email event");
			}
			// Publishing the event
			eventService.publishEvent(jnjContactUsMessagingEvent);

			LOG.info("Contact Us Messaging Event published successfully");
			status = true;
		}
		catch (final Exception e)
		{
			LOG.error("Error publishing event :: " + e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * This method populates the event with the values from the DTO and various services
	 * 
	 * @param jnjContactUsMessagingEvent
	 * @param jnjContactUsDTO
	 */
	protected void populateEvent(final JnjContactUsMessagingEvent jnjContactUsMessagingEvent, final JnjContactUsDTO jnjContactUsDTO)
	{
		//Values from DTO
		jnjContactUsMessagingEvent.setToEmailAddress(fetchEmailAddress(jnjContactUsDTO.getSubject()));
		jnjContactUsMessagingEvent.setEmailBody(jnjContactUsDTO.getMessage());
		jnjContactUsMessagingEvent.setEmailSubject(jnjContactUsDTO.getSubject());
		jnjContactUsMessagingEvent.setCustomerEmail(jnjContactUsDTO.getCustomerEmail());
		if (null != jnjContactUsDTO.getOrderNumber() && !jnjContactUsDTO.getOrderNumber().isEmpty())
		{
			jnjContactUsMessagingEvent.setOrderNumber("Order Number - " + jnjContactUsDTO.getOrderNumber());
		}
		else
		{
			jnjContactUsMessagingEvent.setOrderNumber("");
		}

		//Values from various services
		jnjContactUsMessagingEvent.setCustomer((CustomerModel) userService.getCurrentUser());
		jnjContactUsMessagingEvent.setLanguage(commonI18NService.getCurrentLanguage());
		jnjContactUsMessagingEvent.setCurrency(commonI18NService.getCurrentCurrency());
		jnjContactUsMessagingEvent.setSite(baseSiteService.getCurrentBaseSite());
		jnjContactUsMessagingEvent.setBaseStore(baseStoreService.getCurrentBaseStore());
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

}
