/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.accenture.model.MessageItemModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjLaMessageService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import com.jnj.services.impl.MessageServiceImpl;

public class JnjLaMessageServiceImpl extends MessageServiceImpl implements JnjLaMessageService
{

	@Autowired
	private CatalogVersionService catalogVersionService;

	private static final Class currentClass = JnjLaMessageServiceImpl.class;

	@Override
	public String getMessageFromImpex(final String messageCode, final Locale locale,
			final Collection<CatalogVersionModel> catalogVersions)
	{
		final String methodName = "getMessageFromImpex()";
		final List<MessageItemModel> messageModels = getMessageDao().findJNJMessages(messageCode, catalogVersions);
		if (CollectionUtils.isEmpty(messageModels))
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"No message found for the message code: " + messageCode + " and locale " + locale, currentClass);
			return messageCode;
		}
		return getMessage(messageModels, locale);
	}

	private String getMessage(final List<MessageItemModel> messages, final Locale locale)
	{
		String message = "";
		for (final MessageItemModel messageItem : messages)
		{
			message = messageItem.getMessageText(locale);
			if (StringUtils.isNotBlank(message))
			{
				break;
			}
		}
		return message;
	}

	@Override
	public String getMessageFromImpex(final String messageCode, final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String methodName = "getMessageFromImpex(messageCode, jnJB2bCustomerModel)";
		final Locale userLanguageLocale = JnjlatamOrderUtil.getUserLanguageLocale(jnJB2bCustomerModel);

		final List<CatalogVersionModel> catalogVersions = new ArrayList<>();
		final CatalogVersionModel catalogVersion = getB2bCatalogVersion(jnJB2bCustomerModel);

		if (catalogVersion != null)
		{
			catalogVersions.add(catalogVersion);
			return getMessageFromImpex(messageCode, userLanguageLocale, catalogVersions);
		}
		else
		{
			JnjGTCoreUtil
					.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"No catalogVersion coud be determined for message code: " + messageCode
									+ " Double check the current country for the b2bUnit logged whe the edi file was uploaded.",
							currentClass);
		}
		return messageCode;

	}

	public CatalogVersionModel getB2bCatalogVersion(final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String methodName = "getB2bCatalogVersion()";
		CatalogVersionModel catalogVersion = null;
		try
		{
			String countryIsoCode;
			String catalogVersionName;
			if (jnJB2bCustomerModel != null && jnJB2bCustomerModel.getCurrentCountry() != null)
			{
				countryIsoCode = jnJB2bCustomerModel.getCurrentCountry().getIsocode();
				catalogVersionName = countryIsoCode.toLowerCase() + Jnjlab2bcoreConstants.CONTENT_CATALOG;
				catalogVersion = catalogVersionService.getCatalogVersion(catalogVersionName,
						Jnjlab2bcoreConstants.CATALOG_VERSION_ONLINE);
			}
			else
			{
				countryIsoCode = Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL;
				catalogVersionName = countryIsoCode.toLowerCase() + Jnjlab2bcoreConstants.CONTENT_CATALOG;
				catalogVersion = catalogVersionService.getCatalogVersion(catalogVersionName,
						Jnjlab2bcoreConstants.CATALOG_VERSION_ONLINE);
			}
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, exception, currentClass);
		}

		return catalogVersion;
	}
}
