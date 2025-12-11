/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.exceptions;

import com.jnj.commons.Severity;


/**
 * The Class BusinessException.
 * 
 * @author Accenture
 * @version 1.0
 */
public class BusinessException extends BaseException
{
	/**
	 * Instantiates a new business exception.
	 */
	public BusinessException()
	{
		super();
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param message
	 *           the message
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public BusinessException(final String message, final String exceptionCode, final Severity severity)
	{
		super(message, exceptionCode, severity);
	}

	/**
	 * Instantiates a new business exception.
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
	public BusinessException(final String message, final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(message, cause, exceptionCode, severity);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param cause
	 *           the cause
	 * @param exceptionCode
	 *           the exception code
	 * @param severity
	 *           the severity
	 */
	public BusinessException(final Throwable cause, final String exceptionCode, final Severity severity)
	{
		super(cause, exceptionCode, severity);
	}

	/**
	 * Instantiates a new business exception.
	 * 
	 * @param message
	 *           the message
	 */
	public BusinessException(final String message)
	{
		super(message);
	}
}
