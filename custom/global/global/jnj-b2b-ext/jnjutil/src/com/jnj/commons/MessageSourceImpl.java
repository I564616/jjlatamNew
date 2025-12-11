/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.commons;


import java.util.Locale;

import org.apache.poi.hpsf.NoSingleSectionException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;



/**
 * @author Accenture
 * 
 * @version 1.0
 */
public class MessageSourceImpl implements MessageSource
{

	/** The message facade. */
	private MessageFacadeUtill messageFacade;

	/**
	 * Gets the message.
	 * 
	 * @param resolvable
	 *           the resolvable
	 * @param locale
	 *           the locale
	 * @return the message
	 * @throws NoSuchMessageException
	 *            the no such message exception
	 * @see org.springframework.context.MessageSource#getMessage(org.springframework.context.MessageSourceResolvable,
	 *      java.util.Locale)
	 */
	@Override
	public String getMessage(final MessageSourceResolvable resolvable, final Locale locale) throws NoSuchMessageException
	{
		return resolvable.getDefaultMessage();
	}

	/**
	 * Gets the message.
	 * 
	 * @param code
	 *           the code
	 * @param arguments
	 *           the arguments
	 * @param locale
	 *           the locale
	 * @return the message
	 * @throws NoSuchMessageException
	 *            the no such message exception
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String, java.lang.Object[], java.util.Locale)
	 */
	@Override
	public String getMessage(final String code, final Object[] arguments, final Locale locale) throws NoSuchMessageException
	{
		String messageCode = code;
		if (code.contains("[") && code.contains("]"))
		{
			messageCode = code.substring(code.indexOf('[') + 1, code.indexOf(']'));
		}
		String message;
		try
		{
			message = messageFacade.getMessageTextForCode(messageCode);
		}
		catch (final BusinessException exception)
		{
			throw new NoSingleSectionException(exception.getMessage(), exception);
		}
		return message;
	}

	/**
	 * Gets the message.
	 * 
	 * @param code
	 *           the code
	 * @param arguments
	 *           the arguments
	 * @param arg2
	 *           the arg2
	 * @param locale
	 *           the locale
	 * @return the message
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.util.Locale)
	 */
	@Override
	public String getMessage(final String code, final Object[] arguments, final String arg2, final Locale locale)
	{
		String messageCode = null;
		if (code.contains("[") && code.contains("]"))
		{
			messageCode = code.substring(code.indexOf('[') + 1, code.indexOf(']'));
		}
		String message;
		try
		{
			message = messageFacade.getMessageTextForCode(messageCode);
		}
		catch (final BusinessException exception)
		{
			throw new NoSingleSectionException(exception.getMessage(), exception);
		}
		return message;
	}

	/**
	 * Sets the message facade.
	 * 
	 * @param messageFacade
	 *           the messageFacade to set
	 */
	public void setMessageFacade(final MessageFacadeUtill messageFacade)
	{
		this.messageFacade = messageFacade;
	}
}
