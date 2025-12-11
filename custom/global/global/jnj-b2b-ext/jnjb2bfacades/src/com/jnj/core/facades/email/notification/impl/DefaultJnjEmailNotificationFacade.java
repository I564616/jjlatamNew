/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.facades.email.notification.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.email.notification.JnjEmailNotificationService;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.email.notification.JnjEmailNotificationFacade;



/**
 * Implementation class for JnjEmailNotification.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjEmailNotificationFacade implements JnjEmailNotificationFacade
{
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(DefaultJnjEmailNotificationFacade.class);

	/** The Constant METHOD_SEND_EMAIL_NOTIFICATION. */
	private static final String METHOD_SEND_EMAIL_NOTIFICATION = "Facade - sendEmailNotificationForInterface()";

	/** The jnj email notification service. */
	@Autowired
	private JnjEmailNotificationService jnjEmailNotificationService;


	@Override
	public void sendEmailNotificationForInterface(final String interfaceName, final List<String> errorFileList)
			throws BusinessException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		if (StringUtils.isNotEmpty(interfaceName) && null != errorFileList && !errorFileList.isEmpty())
		{
			jnjEmailNotificationService.sendEmailNotification();
		}
		else
		{
			LOG.error(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ "Not Suffecient Info For Email. Initiating Exception.");
			throw new BusinessException("Not Suffecient Info For Email.");
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}


	@Override
	public void sendEmailNotificationForInterface(final String interfaceName, final String errorFile) throws BusinessException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		if (StringUtils.isNotEmpty(interfaceName) && StringUtils.isNotEmpty(errorFile))
		{
			final List<String> errorFileList = new ArrayList<String>();
			errorFileList.add(errorFile);
			jnjEmailNotificationService.sendEmailNotification();
		}
		else
		{
			LOG.error(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ "Not Suffecient Info For Email. Initiating Exception.");
			throw new BusinessException("Not Suffecient Info For Email.");
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_SEND_EMAIL_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}


}
