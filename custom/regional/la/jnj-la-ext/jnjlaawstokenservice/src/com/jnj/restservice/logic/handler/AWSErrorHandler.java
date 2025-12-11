/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import org.apache.log4j.Logger;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.jnj.restservice.logic.Response;


/**
 * To retrieve the error messages from the rest response
 */
public class AWSErrorHandler implements IResponseHandler {
    private static final Logger LOG = Logger.getLogger(Response.class);

    @Override
    public <T> void handle(final jakarta.ws.rs.core.Response rawResponse, final Response<T> restResponse, final Class<T> genericType) {
        try{
            final JsonNode responseNode = rawResponse.readEntity(JsonNode.class);
            final JsonNode messageNode = responseNode.path("message");

	        if(messageNode!=null && !(messageNode instanceof MissingNode) ) {
	            String message ="";
	            if(messageNode!=null  && !(messageNode instanceof MissingNode)){
	                message = messageNode.asText();
	                restResponse.setResponseMessage(message);
	            }
	        }
        }catch(final MessageBodyProviderNotFoundException e){
			LOG.error(e);
        }
    }

    @Override
    public <T> boolean canHandle(final jakarta.ws.rs.core.Response rawResponse, final Response<T> restResponse) {
        return (rawResponse.getStatus()!= 201 && rawResponse.getStatus()!=204 );
    }

}
