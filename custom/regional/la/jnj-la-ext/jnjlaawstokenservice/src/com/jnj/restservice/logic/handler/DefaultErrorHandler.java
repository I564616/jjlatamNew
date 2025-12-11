/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.jnj.restservice.exception.ErrorCode;
import com.jnj.restservice.exception.RestServiceException;
import com.jnj.restservice.logic.Response;

/**
 * Last node in the error handling to handle unexpected errors
 */
public class DefaultErrorHandler implements IResponseHandler {

    @Override
    public <T> void handle(final jakarta.ws.rs.core.Response rawResponse, final Response<T> restResponse, final Class<T> genericType) {
        JsonNode responseNode = rawResponse.readEntity(JsonNode.class);

        JsonNode messageNode = responseNode.path("message");

        if(messageNode!=null && !(messageNode instanceof MissingNode) ) {
            String message ="";
            if(messageNode!=null  && !(messageNode instanceof MissingNode)){
                message = messageNode.asText();
            }
            throw new RestServiceException(ErrorCode.AWS_ERROR_RESPONSE, message);
        }
    }

    @Override
    public <T> boolean canHandle(final jakarta.ws.rs.core.Response rawResponse, final Response<T> restResponse) {
        return true;
    }

}
