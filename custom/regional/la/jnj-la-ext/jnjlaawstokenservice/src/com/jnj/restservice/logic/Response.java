/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.client.ResponseProcessingException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.jnj.restservice.exception.ErrorCode;
import com.jnj.restservice.exception.RestServiceException;
import com.jnj.restservice.logic.handler.AbstractMasterResponseHandler;

public class Response<T> {
    private static final Logger LOG = Logger.getLogger(Response.class);

    private List<T> results;

    private int responseCode;

    private String responseMessage;
    private String token;
    private boolean validToken;

    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //enforce the creation of this Response object via parser
    private Response(){}

    public static <T> Response<T> parse(final jakarta.ws.rs.core.Response rawResponse
                                      , final Class<T> genericType
                                      , final AbstractMasterResponseHandler masterHandler) throws RestServiceException {
        LOG.debug("Parsing raw response...");
        final Response<T> restResponse = new Response<T>();

        final int responseCode = rawResponse==null ? 500 : rawResponse.getStatus();
        restResponse.setResponseCode(responseCode);

        final String responseMessage = rawResponse==null
                                ? "Cannot reachout endpoints"
                                : rawResponse.getStatusInfo().getReasonPhrase();
        restResponse.setResponseMessage(responseMessage);

        if(rawResponse!=null) {
            try {
                masterHandler.handle(rawResponse, restResponse, genericType);
            } catch(final ResponseProcessingException e){
                final ErrorCode ec = ErrorCode.READ_AWS_RESPONSE;
                LOG.error(ec.toString() + e.getMessage());
                throw new RestServiceException(ec, e);
            } catch(final IllegalStateException e){
                final ErrorCode ec = ErrorCode.READ_AWS_RESPONSE;
                LOG.error(ec.toString() + e.getMessage());
                throw new RestServiceException(ec, e);
            }
        } else {
            final ErrorCode ec = ErrorCode.EMPTY_AWS_RESPONSE;
            LOG.error(ec.toString());
            throw new RestServiceException(ec);
        }

        LOG.debug("Response Parsing completed!");
        return restResponse;
    }

    public List<T> getResult() {
        return results;
    }

    public void setResult(final List<T> results) {
        this.results = results;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(final int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @param responseMessage the responseMessage to set
     */
    public void setResponseMessage(final String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }

    public Object getAdditionalProperty(final String name) {
        return additionalProperties.get(name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("\n")
                .append("responseCode", responseCode).append("\n")
                .append("responseMessage", responseMessage).append("\n")
                .append("results", results)
                .build();
    }

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public boolean isValidToken() {
		return validToken;
	}

	public void setValidToken(final boolean validToken) {
		this.validToken = validToken;
	}
}
