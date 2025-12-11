/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.exception;

public enum ErrorCode implements IErrorCode {
	AWS_ERROR_RESPONSE("AWS_001","Request has missing required properties"), 
	UBMIT_REQUEST("ESR_001","Error when submitting request"), 
	READ_AWS_RESPONSE("RCR_001","Unable to read response body from AWS"), 
	PARSE_AWS_RESPONSE("PCR_001","Unable to parse JSON response body from AWS"), 
	EMPTY_AWS_RESPONSE("ECR_001","Response is null or it has no entity"), 
	INVALID_JWT_RESPONSE("JWT_001","Response is null or it has no entity"), 
	PROCESSING_ERROR("401","Processing Error"), 
	GENERIC_ERROR("500", "Processing Error"),
	;

	private String code;
	private String message;

	private void setMessage(final String message)
	{
		this.message = message;
	}

	private void setCode(final String code)
	{
		this.code = code;
	}

	private ErrorCode(final String code)
	{
		this.code = code;
	}

	private ErrorCode(final String code, final String message)
	{
		this.code = code;
		this.message = message;
	}

	public String getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return code + ":" + message;
	}
}
