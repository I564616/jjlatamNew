/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.exceptions;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;


/**
 * The Class BaseException.
 * 
 * @author Accenture
 * @version 1.0
 */
public class BaseException extends Exception
{
	/** The exception code. */
	private final String exceptionCode;

	/** The severity. */
	private final Severity severity;

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param cause
	 *           the cause
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public BaseException(final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(cause);
		this.exceptionCode = exceptionCode;
		this.severity = severity;
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param message
	 *           the message
	 * @param messageCode
	 *           the message code
	 * @param exceptionSeverity
	 *           the exception severity
	 */
	public BaseException(final String message, final String messageCode, final Severity exceptionSeverity)
	{
		super(message);
		this.exceptionCode = messageCode;
		this.severity = exceptionSeverity;
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param message
	 *           the message
	 * @param cause
	 *           the cause
	 * @param messageCode
	 *           the message code
	 * @param exceptionSeverity
	 *           the exception severity
	 */
	public BaseException(final String message, final Throwable cause, final String messageCode, final Severity exceptionSeverity)
	{
		super(message, cause);
		this.exceptionCode = messageCode;
		this.severity = exceptionSeverity;
	}

	/**
	 * default constructor.
	 */
	public BaseException()
	{
		super();
		this.exceptionCode = MessageCode.NONE;
		this.severity = Severity.NONE;
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param message
	 *           the message
	 */
	public BaseException(final String message)
	{
		super(message);
		this.exceptionCode = MessageCode.NONE;
		this.severity = Severity.NONE;
	}

	/**
	 * Gets the exception code.
	 * 
	 * @return the exceptionCode
	 */
	public String getExceptionCode()
	{
		return this.exceptionCode;
	}

	/**
	 * Gets the severity.
	 * 
	 * @return the severity
	 */
	public Severity getSeverity()
	{
		return this.severity;
	}
}
