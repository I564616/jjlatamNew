/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.exceptions;

import com.jnj.commons.Severity;


/**
 * The Class SystemException.
 * 
 * 
 * @author Accenture
 * @version 1.0
 */
public class SystemException extends BaseException
{
	/**
	 * Instantiates a new system exception.
	 */
	public SystemException()
	{
		super();
	}

	/**
	 * Instantiates a new system exception.
	 * 
	 * @param message
	 *           the message
	 */
	public SystemException(final String message)
	{
		super(message);
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
	public SystemException(final String message, final String exceptionCode, final Severity severity)
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
	public SystemException(final String message, final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(message, cause, exceptionCode, severity);
	}

	/**
	 * parameterized constructor.
	 * 
	 * @param cause
	 *           the cause of exception
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public SystemException(final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(cause, exceptionCode, severity);
	}
}
