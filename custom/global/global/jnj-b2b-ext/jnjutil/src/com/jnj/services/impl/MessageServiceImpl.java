/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.services.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.accenture.model.MessageItemModel;
import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.constants.JnjutilConstants;
import com.jnj.dao.MessageDao;
import com.jnj.exceptions.BusinessException;
import com.jnj.services.MessageService;


/**
 * <p>
 * Implements the business logic to get the message text from the message item of CMS
 * </p>
 * .
 *
 * @author Accenture
 * @version 1.0
 */
public class MessageServiceImpl extends AbstractService implements MessageService
{
	protected static final Logger LOG = Logger.getLogger(MessageServiceImpl.class);
	/** The message dao. */
	@Autowired
	private MessageDao messageDao;

	/** The message dao. */
	@Autowired
	private CatalogService catalogService;

	@Autowired
	private I18NService i18nService;

	/** The message dao. */
	@Autowired
	private CatalogVersionService catalogVersionService;

	public MessageDao getMessageDao() {
		return messageDao;
	}


	public CatalogService getCatalogService() {
		return catalogService;
	}


	public I18NService getI18nService() {
		return i18nService;
	}


	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}


	/**
	 * Gets the message for codefrom the current catalog version.
	 *
	 * @param messageCode
	 *           the message code
	 * @param locale
	 *           the locale
	 * @return the message for code
	 * @throws BusinessException
	 *            the business exception
	 * @see com.jnj.services.MessageService#getMessageForCode(String, Locale)
	 */
	@Override
	public String getMessageForCode(final String messageCode, final Locale locale) throws BusinessException
	{
		final List<MessageItemModel> messageModels = messageDao.findJNJMessages(messageCode,
				catalogVersionService.getSessionCatalogVersions());
		if (CollectionUtils.isEmpty(messageModels))
		{
			LOG.error("No message found for the message code: " + messageCode + " and locale " + locale);
			return messageCode;
		}
		return getMessage(messageModels, locale);
	}


	@Override
	public String getMessageForCode(final String messageCode, final Locale locale, final String... params)
	{
		// If no locale is passed as argument, select the current locale
		Locale currentLocale;
		if (locale == null)
		{
			currentLocale = i18nService.getCurrentLocale();
		}
		else
		{
			currentLocale = locale;
		}

		// Get the localized message
		final List<MessageItemModel> messageModels = messageDao.findJNJMessages(messageCode,
					catalogVersionService.getSessionCatalogVersions());
		if (CollectionUtils.isEmpty(messageModels))
		{
			return messageCode;
		}

		String message = getMessage(messageModels, currentLocale);

		// Replace all the placeholders with actual values
		StringBuilder stringToBeSearched;
		//final StringBuilder parameter = new StringBuilder(JnjutilConstants.PARAMS); // moved inside loop otherwise parameter appending i values
		for (int i = 1; i <= params.length; i++)
		{
			final StringBuilder parameter = new StringBuilder(JnjutilConstants.PARAMS);
			stringToBeSearched = parameter.insert(6, i);
			if (message.contains(stringToBeSearched))
			{
				message = message.replace(stringToBeSearched, params[i - 1]);
			}
		}
		return message;
	}

	/**
	 * Gets the message for code from the current active catalog version.
	 *
	 * @param messageCode
	 *           the message code
	 * @param locale
	 *           the locale
	 * @param activeVersions
	 *           the active versions
	 * @return the message for code
	 * @throws BusinessException
	 *            the business exception
	 * @see com.jnj.services.MessageService#getCsMessageForCode(String, Locale, Set)
	 */
	@Override
	public String getCsMessageForCode(final String messageCode, final Locale locale, final Set<CatalogVersionModel> activeVersions)
			throws BusinessException
	{
		final List<MessageItemModel> messageModels = messageDao.findJNJMessages(messageCode, activeVersions);
		if (CollectionUtils.isEmpty(messageModels) || messageModels.get(0).getMessageText(locale) == null)
		{

			throw new BusinessException("No message found for the message code: " + messageCode, MessageCode.NO_RECORDS_FOUND,
					Severity.BUSINESS_EXCEPTION);
		}
		return messageModels.get(0).getMessageText(locale);
	}

	/**
	 * Gets all messages from current catalog version.
	 *
	 * @return the all messages
	 * @throws BusinessException
	 *            the business exception
	 * @see com.jnj.services.MessageService#getAllMessages()
	 */
	@Override
	public List<String> getAllMessages() throws BusinessException
	{
		final List<MessageItemModel> messageModels = messageDao.findAllJNJMessages((Set<CatalogVersionModel>) catalogVersionService.getSessionCatalogVersions());
		if (messageModels == null || messageModels.isEmpty())
		{
			throw new BusinessException("No messages found.", MessageCode.NO_RECORDS_FOUND, Severity.BUSINESS_EXCEPTION);
		}
		final List<String> messages = new ArrayList<String>();
		for (final MessageItemModel model : messageModels)
		{
			messages.add(model.getMessageText());
		}
		if (messages.isEmpty())
		{
			throw new BusinessException("No messages found.", MessageCode.NO_RECORDS_FOUND, Severity.BUSINESS_EXCEPTION);
		}
		return messages;
	}

	/**
	 * Gets the all messages.
	 *
	 * @param locale
	 *           the locale
	 * @return the all messages
	 * @throws BusinessException
	 *            the business exception
	 * @see com.jnj.services.MessageService#getAllMessages(Locale)
	 */
	@Override
	public List<String> getAllMessages(final Locale locale) throws BusinessException
	{
		final List<MessageItemModel> messageModels = messageDao.findAllJNJMessages((Set<CatalogVersionModel>) catalogVersionService.getSessionCatalogVersions());
		if (messageModels == null || messageModels.isEmpty())
		{
			throw new BusinessException("No messages found for locale: " + locale, MessageCode.NO_RECORDS_FOUND,
					Severity.BUSINESS_EXCEPTION);
		}
		final List<String> messages = new ArrayList<String>();
		for (final MessageItemModel model : messageModels)
		{
			if (model.getMessageText(locale) != null)
			{
				messages.add(model.getMessageText());
			}
		}
		if (messages.isEmpty())
		{
			throw new BusinessException("No messages found for locale: " + locale, MessageCode.NO_RECORDS_FOUND,
					Severity.BUSINESS_EXCEPTION);
		}
		return messages;
	}

	/**
	 * Retrieves the message text corresponding to the primary channel type. If not found, the text from fallbackChannel
	 * type is used.
	 *
	 * @param messages
	 *           messages
	 * @param locale
	 *           locale
	 * @return message
	 */
	private String getMessage(final List<MessageItemModel> messages, final Locale locale)
	{
		String message = "";
		final Locale[] fallBackLocale = i18nService.getFallbackLocales(locale);

		for (final MessageItemModel messageItem : messages)
		{
			message = messageItem.getMessageText(locale);
			if (StringUtils.isNotBlank(message))
			{
				break;
			}
			else if (ArrayUtils.isNotEmpty(fallBackLocale))
			{
				message = messageItem.getMessageText(fallBackLocale[0]);
			}
		}
		return message;
	}
}
