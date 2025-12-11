/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import java.util.LinkedList;
import java.util.List;

public class MasterHandlerImpl extends AbstractMasterResponseHandler{
private static MasterHandlerImpl instance = null;

    private MasterHandlerImpl() {
		setHandler();
	}

	public final void setHandler()
	{
		final List<IResponseHandler> lResponseHandler = new LinkedList<IResponseHandler>();
		lResponseHandler.add(new AWSTokenHandler());
		lResponseHandler.add(new AWSTokenVerificationHandler());
		lResponseHandler.add(new AWSErrorHandler());
		lResponseHandler.add(new DefaultErrorHandler());

		setResponseHandlers(lResponseHandler);
    }
    public static MasterHandlerImpl getInstance() {
        if(instance==null) {
            instance = new MasterHandlerImpl();
        }
        return instance;
    }
}
