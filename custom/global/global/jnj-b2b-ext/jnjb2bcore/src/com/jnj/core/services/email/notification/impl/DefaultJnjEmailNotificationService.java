/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.email.notification.impl;

import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import com.jnj.core.dao.email.notification.JnjEmailNotificationDao;
import com.jnj.core.event.JnjInterfaceNotificationEvent;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;
import com.jnj.core.services.email.notification.JnjEmailNotificationService;


/**
 * Implementation class for JnjEmailNotificationService.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjEmailNotificationService implements JnjEmailNotificationService
{

	/** The Constant LOG. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjEmailNotificationService.class);

	/**
	 * 
	 */
	protected static final String METHOD_SEND_EMAIL_NOTIFICATION = "Service - sendEmailNotification()";

	protected static final String CURRENT_BASE_SIORE = "brBaseStore";

	protected static final String CURRENT_BASE_SITE = "brCMSite";

	/**
	 * Instance of <code>JnjEmailNotificationDao</code>.
	 */
	@Autowired
	protected JnjEmailNotificationDao jnjEmailNotificationDao;

	/** The event service. */
	@Autowired
	protected EventService eventService;

	@Autowired
	protected BaseStoreService baseStoreService;

	@Autowired
	protected BaseSiteService baseSiteService;

	@Autowired
	protected CommonI18NService commonI18NService;

	@Autowired
	UserService userService;

	
	public JnjEmailNotificationDao getJnjEmailNotificationDao() {
		return jnjEmailNotificationDao;
	}


	public EventService getEventService() {
		return eventService;
	}


	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}


	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}


	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}


	public UserService getUserService() {
		return userService;
	}


	@Override
	public void sendEmailNotification()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final JnjInterfaceNotificationEvent event = new JnjInterfaceNotificationEvent();

		if (jnjEmailNotificationDao.getReadDashboardRecordsForNotification().isEmpty()
				&& jnjEmailNotificationDao.getWriteDashboardRecordsForNotification().isEmpty())
		{
			LOGGER.info(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN
					+ "No eligible records present from READ/WRITE Dashboard to be sent for notification.");
		}
		else
		{

			event.setReadDashboardRecords(new HashSet<JnjReadOperationDashboardModel>(jnjEmailNotificationDao
					.getReadDashboardRecordsForNotification()));

			event.setWriteDashboardRecords(new HashSet<JnjWriteOperationDashboardModel>(jnjEmailNotificationDao
					.getWriteDashboardRecordsForNotification()));

			event.setBaseStore(baseStoreService.getBaseStoreForUid(CURRENT_BASE_SIORE));
			event.setSite(baseSiteService.getBaseSiteForUID(CURRENT_BASE_SITE));
			event.setLanguage(commonI18NService.getCurrentLanguage());
			event.setCurrency(commonI18NService.getCurrentCurrency());
			event.setCustomer(userService.getAnonymousUser());

			eventService.publishEvent(event);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}
}
