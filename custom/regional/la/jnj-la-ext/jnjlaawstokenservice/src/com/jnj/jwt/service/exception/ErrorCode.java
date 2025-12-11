/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.jwt.service.exception;

public enum ErrorCode implements IErrorCode {
    PROCESSING_ERROR("401", "Processing Error"),
	GENERIC_ERROR("500", "Processing Error")
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
    private ErrorCode(final String code) {
        this.code = code;
    }
    private ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }

    public String getMessage() { return message; }

    @Override
    public String toString() {
        return code + ":" + message;
    }
}
