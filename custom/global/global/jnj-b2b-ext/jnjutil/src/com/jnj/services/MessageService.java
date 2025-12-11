/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.jnj.exceptions.BusinessException;


/**
 * The Interface MessageService.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface MessageService
{
	/**
	 * Gets the message for code.
	 * 
	 * @param messageCode
	 *           the message code
	 * @param locale
	 *           the locale
	 * @return the message for code
	 * @throws BusinessException
	 *            the business exception
	 */
	String getMessageForCode(final String messageCode, final Locale locale) throws BusinessException;

	/**
	 * Gets the message for code.
	 * 
	 * @param messageCode
	 *           the message code
	 * @param locale
	 *           the locale, can be null, if null method considers the current locale
	 * @param params
	 *           Variable length array of Strings, that contain the values to be replaced for the placeholder
	 * @return the message for code
	 */
	String getMessageForCode(final String messageCode, final Locale locale, final String... params);

	/**
	 * Gets the all messages.
	 * 
	 * @return the all messages
	 * @throws BusinessException
	 *            the business exception
	 */
	List<String> getAllMessages() throws BusinessException;

	/**
	 * Gets the all messages.
	 * 
	 * @param locale
	 *           the locale
	 * @return the all messages
	 * @throws BusinessException
	 *            the business exception
	 */
	List<String> getAllMessages(final Locale locale) throws BusinessException;

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
	 */
	String getCsMessageForCode(String messageCode, Locale locale, Set<CatalogVersionModel> activeVersions)
			throws BusinessException;

}
