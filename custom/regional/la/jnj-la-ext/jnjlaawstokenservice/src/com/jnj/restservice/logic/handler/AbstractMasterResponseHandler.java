/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import java.util.LinkedList;
import java.util.List;
import jakarta.ws.rs.core.Response;


public abstract class AbstractMasterResponseHandler implements IResponseHandler {
    private List<IResponseHandler> responseHandlers = new LinkedList<IResponseHandler>();
    
 
    @Override
    public <T> void handle(final Response rawResponse, com.jnj.restservice.logic.Response<T> response,
                                final Class<T> genericType) {
        for(IResponseHandler handler : getResponseHandlers()) {
            if(handler.canHandle(rawResponse, response)) {
                handler.handle(rawResponse, response, genericType);
                break;
            }
        }
    }

    
    @Override
    public <T> boolean canHandle(final Response rawResponse,final com.jnj.restservice.logic.Response<T> response) {
        return true;
    }

    /**
     * @return the responseHandlers
     */
    public List<IResponseHandler> getResponseHandlers() {
        return responseHandlers;
    }

    /**
     * @param responseHandlers the responseHandlers to set
     */
    public void setResponseHandlers(final List<IResponseHandler> lResponseHandler) {
        this.responseHandlers = lResponseHandler;
    }

}
