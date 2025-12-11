/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.jwt.service.exception;

import java.io.Serial;

/**
 *
 */
public class JwtServiceException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = -8902795807754443499L;
	private IErrorCode errorCode;

	public JwtServiceException(final IErrorCode e, final Throwable t)
	{
        super(e.getMessage(), t);
        this.setErrorCode(e);
    }

	public JwtServiceException(final IErrorCode e, final String message)
	{
        super(message);
        this.setErrorCode(e);
    }

	public JwtServiceException(final IErrorCode e)
	{
        super(e.getMessage());
        this.setErrorCode(e);
    }

	public IErrorCode getErrorCode()
	{
		return errorCode;
	}

	public final void setErrorCode(final IErrorCode errorCode)
	{
		this.errorCode = errorCode;
	}


}
