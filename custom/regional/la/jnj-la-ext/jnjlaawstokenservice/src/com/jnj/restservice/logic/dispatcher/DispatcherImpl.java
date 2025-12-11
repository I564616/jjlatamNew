/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.dispatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jnj.restservice.exception.ErrorCode;
import com.jnj.restservice.exception.RestServiceException;
import com.jnj.restservice.logic.Request;
import com.jnj.restservice.logic.Request.REQUEST_TYPE;
import com.jnj.restservice.logic.Response;
import com.jnj.restservice.logic.handler.AbstractMasterResponseHandler;
import org.apache.log4j.Logger;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import java.util.Map;

public class DispatcherImpl<T> implements IDispatcher<T> {
     private static final Logger LOG = Logger.getLogger(DispatcherImpl.class);

    private ClientBuilder builder = ClientBuilder.newBuilder().register(new JacksonJsonProvider());
    
    private AbstractMasterResponseHandler masterHandler;
    
    private Class<T> genericType;
    
    public DispatcherImpl(AbstractMasterResponseHandler masterHandler, Class<T> genericType) {
        this.masterHandler = masterHandler;
        this.genericType = genericType;
    }

    private Client getClient() {
         return builder.build();
    }

    @Override
    public Response<T> dispatch(Request restRequest) throws RestServiceException {
        LOG.debug("Request:"+ restRequest);
        final Client client = getClient();
        final Builder request = client.target(restRequest.getEndpoint()).request();

        Map<String, Object> headerMap = restRequest.getHeaders();
        headerMap.entrySet().forEach(hEntry -> request.header(hEntry.getKey(), hEntry.getValue()));

        jakarta.ws.rs.core.Response rawResponse = null;
        try {
            if(restRequest.getRequestType() == REQUEST_TYPE.POST) {
                JsonNode postBody = restRequest.getJsonBody();
                rawResponse = request.post(Entity.json(postBody));
            } else if (restRequest.getRequestType() == REQUEST_TYPE.PUT) {
                JsonNode postBody = restRequest.getJsonBody();
                rawResponse = request.put(Entity.json(postBody));
            } else if (restRequest.getRequestType() == REQUEST_TYPE.PATCH) {
                rawResponse = request.method("PATCH", Entity.json(restRequest.getJsonBody()));
            } else {
                rawResponse = request.get();
            }
        } catch (ProcessingException e) {
            LOG.error("Error when submitting request: "+ e);
            if (client != null) {
                client.close();
            }
            throw new RestServiceException(ErrorCode.GENERIC_ERROR, e);
        }

        Response<T> restResponse = null;
        try {
            restResponse = Response.parse(rawResponse, genericType, masterHandler);
            LOG.debug("Response: "+ restResponse);
        } catch (RestServiceException e) {
            LOG.error(e.getMessage());
            throw e;
        }finally {
        	if(rawResponse != null){
        		rawResponse.close();
        	}
            if (client != null) {
            	client.close();
            }

         }
        return restResponse;
    }

   

}
