/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.exception;

import java.io.Serial;

public class RestServiceException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -8902795807754443499L;
	private IErrorCode errorCode;

    public RestServiceException(final IErrorCode e, final Throwable t) {
        super(e.getMessage(), t);
        this.setErrorCode(e);
    }

    public RestServiceException(final IErrorCode e, final String message) {
        super(message);
        this.setErrorCode(e);
    }

    public RestServiceException(final IErrorCode e) {
        super(e.getMessage());
        this.setErrorCode(e);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

	public final void setErrorCode(final IErrorCode errorCode)
	{
        this.errorCode = errorCode;
    }

}
