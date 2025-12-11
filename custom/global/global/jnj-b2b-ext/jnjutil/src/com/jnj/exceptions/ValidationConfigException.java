/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.exceptions;

import com.jnj.commons.Severity;


/**
 * The Class ValidationConfigException.
 * 
 * 
 * @author Accenture
 * @version 1.0
 */
public class ValidationConfigException extends SystemException
{
	/**
	 * Instantiates a new validation config exception.
	 */
	public ValidationConfigException()
	{
		super();
	}

	/**
	 * parameterized constructor.
	 * 
	 * @param message
	 *           the message
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public ValidationConfigException(final String message, final String exceptionCode, final Severity severity)
	{
		super(message, exceptionCode, severity);
	}

	/**
	 * parameterized constructor.
	 * 
	 * @param message
	 *           the message
	 * @param cause
	 *           the cause
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public ValidationConfigException(final String message, final Throwable cause, final String exceptionCode,
			final Severity severity)
	{
		super(message, cause, exceptionCode, severity);
	}

	/**
	 * parameterized constructor.
	 * 
	 * @param cause
	 *           the cause
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public ValidationConfigException(final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(cause, exceptionCode, severity);
	}
}
