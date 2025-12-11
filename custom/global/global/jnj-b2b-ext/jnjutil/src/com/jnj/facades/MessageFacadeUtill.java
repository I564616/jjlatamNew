/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades;

import de.hybris.platform.core.model.c2l.CountryModel;

import java.util.Locale;

import com.jnj.exceptions.BusinessException;


/**
 * The Interface MessageFacade.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface MessageFacadeUtill
{

	/**
	 * Gets the message text for code.
	 * 
	 * @param code
	 *           the code
	 * @param locale
	 *           the locale
	 * @return the message text for code
	 * @throws BusinessException
	 *            the business exception
	 */
	String getMessageTextForCode(String code, Locale locale) throws BusinessException;

	/**
	 * Gets the message text for code.
	 * 
	 * @param code
	 *           the code
	 * @return the message text for code
	 * @throws BusinessException
	 *            the business exception
	 */
	String getMessageTextForCode(String code) throws BusinessException;

	/**
	 * Gets the message text for code for cs.
	 * 
	 * @param code
	 *           the code
	 * @param countryModel
	 *           country
	 * @return the message text for code
	 * @throws BusinessException
	 *            , CMSItemNotFoundException
	 */
	String getCsMessageTextForCode(final String code, final CountryModel countryModel) throws BusinessException;

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
	 * Gets the message text for code.
	 * 
	 * @param code
	 *           the code
	 * @param locale
	 *           the locale
	 * @return the message text for code
	 * @throws BusinessException
	 *            the business exception
	 */
	String getMessageTextForCode(String code, String locale) throws BusinessException;
}
