/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.services.CMSSiteService;
import com.jnj.services.MessageService;


/**
 * This Facade class implements the logic to get the message text for a message code which is stored in CMS database.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultMessageFacade implements MessageFacadeUtill
{

	/**
	 * Logging instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(DefaultMessageFacade.class);

	/** The message service. */
	@Autowired
	private MessageService messageService;

	/** I18NService to retrieve the current locale. */
	@Autowired
	private I18NService i18nService;

	/** The cms site service. */
	@Autowired
	private CMSSiteService cmsSiteService;


	public I18NService getI18nService() {
		return i18nService;
	}


	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageTextForCode(final String code, final Locale locale) throws BusinessException
	{

		return messageService.getMessageForCode(code, locale);

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCsMessageTextForCode(final String code, final CountryModel countryModel) throws BusinessException
	{
		String returnCode = StringUtils.EMPTY;
		CMSSiteModel site = null;
		try
		{
			site = cmsSiteService.getSiteForCountry(countryModel.getIsocode());
		}
		catch (final CMSItemNotFoundException cmsE)
		{
			LOGGER.error("CMSSite not found, " + cmsE.getMessage());
		}
		final Set<CatalogVersionModel> activeVersions = new HashSet<CatalogVersionModel>();
		if (site != null)
		{
			for (final ContentCatalogModel contentCat : site.getContentCatalogs())
			{
				activeVersions.add(contentCat.getActiveCatalogVersion());
			}

		}
		if (!activeVersions.isEmpty())
		{
			returnCode = messageService.getCsMessageForCode(code, i18nService.getCurrentLocale(), activeVersions);
		}
		else
		{
			returnCode = code;
		}
		return returnCode;

	}

	/**
	 * Sets the message service.
	 * 
	 * @param messageService
	 *           the messageService to set
	 */
	public void setMessageService(final MessageService messageService)
	{
		this.messageService = messageService;
	}

	/**
	 * Gets the message service.
	 * 
	 * @return the messageService
	 */
	public MessageService getMessageService()
	{
		return messageService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageTextForCode(final String code) throws BusinessException
	{

		return messageService.getMessageForCode(code, i18nService.getCurrentLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageForCode(final String messageCode, final Locale locale, final String... params)
	{
		return messageService.getMessageForCode(messageCode, locale, params);
	}

	@Override
	public String getMessageTextForCode(final String code, final String locale) throws BusinessException
	{
		Locale currentLocale = null;
		if (StringUtils.isNotBlank(locale))
		{
			currentLocale = LocaleUtils.toLocale(locale);
		}
		else
		{
			currentLocale = i18nService.getCurrentLocale();
		}
		return messageService.getMessageForCode(code, currentLocale);
	}

}
