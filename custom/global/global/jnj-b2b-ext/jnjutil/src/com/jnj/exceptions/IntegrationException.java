/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.exceptions;

import java.io.Serial;

/**
 * The class <code>IntegrationException</code> and its subclasses are a form of <code>Throwable</code> that indicates
 * conditions that application might want to catch. Applications should throw instances of this class whenever any
 * exception captured in system.
 * 
 * @author Accenture
 * @version 1.0
 */
public class IntegrationException extends Exception
{
	/** The Constant serialVersionUID. */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Specific error code against the captured exception. For example, If there is any business exception captured in
	 * system then it will have an error code in required format like (INTG_BE_ERR_XXX_YYYY_ZZZZ)
	 */
	private String errCode;

	/**
	 * Constructs a new IntegrationException with <code>null</code> as its detail message. The cause is not initialized,
	 * and may subsequently be initialized by a call to initClause method defined in Throwable class.
	 */
	public IntegrationException()
	{
		super();
	}

	/**
	 * Constructs a new IntegrationException with the specified error code. The cause is not initialized, and may
	 * subsequently be initialized by a call to initClause method defined in Throwable class.
	 * 
	 * @param errorCode
	 *           , the specified error code. The detail error code is saved for later retrieval by the
	 *           {@link #getErrCode()} method.
	 */

	public IntegrationException(final String errorCode)
	{
		super(errorCode);
		this.errCode = errorCode;
	}

	/**
	 * Constructs a new IntegrationException with the specified error code and cause.
	 * 
	 * @param errorCode
	 *           , the specified error code. The detail error code is saved for later retrieval by the
	 * @param cause
	 *           , cause to be initialized and may be retrieved by a call to initClause() method defined in Throwable
	 *           class. {@link #getErrCode()} method.
	 */

	public IntegrationException(final String errorCode, final Throwable cause)
	{
		super(errorCode, cause);
		this.errCode = errorCode;
	}

	/**
	 * Constructs a new IntegrationException with the cause only.
	 * 
	 * @param cause
	 *           , cause to be initialized and may be retrieved by a call to initClause() method defined in Throwable
	 *           class.
	 */
	public IntegrationException(final Throwable cause)
	{
		super(cause);
	}

	/**
	 * Returns the error code string of this throwable.
	 * 
	 * @return errCode, the error code string of this Throwable instance
	 */
	public final String getErrCode()
	{
		return this.errCode;
	}

	/**
	 * Method sets the vlaue of error code.
	 * 
	 * @param errorCode
	 *           , the error code string of this Throwable instance
	 */
	public final void setErrCode(final String errorCode)
	{
		this.errCode = errorCode;
	}

}
