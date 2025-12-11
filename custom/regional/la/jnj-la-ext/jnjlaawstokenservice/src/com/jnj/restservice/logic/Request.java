/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Request {
    public enum REQUEST_TYPE {
        GET,
        POST, 
        PUT,
        PATCH
    }
    
    private REQUEST_TYPE requestType;
    
    private String endpoint;
    
    private Map<String, Object> headers;
    
    private JsonNode jsonBody;
    
    private Map<String, String> params;
    
    //enforce the use of Builder
    private Request() {}
    
    public static class Builder {
        private REQUEST_TYPE requestType;
        
        private String endpoint;
        
        private Map<String, Object> headers = new HashMap<String, Object>();
        
        private JsonNode jsonBody;
        
        private Map<String, String> params = new HashMap<String, String>();
        
        public Builder requestType(REQUEST_TYPE requestType) {
            this.requestType = requestType;
            return this;
        }
        
        public Builder endpoint(final String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        public Builder addHeader(final String key, final Object value) {
            this.headers.put(key, value);
            return this;
        }
        
        public Builder headers(final Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }
        
        public Builder addHeaders(final Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }
        
        public Builder jsonBody(final JsonNode jsonBody) {
            this.jsonBody = jsonBody;
            return this;
        }
        
        public Builder addParam(final String key, final String value) {
            this.params.put(key, value);
            return this;
        }
        
        public Builder params(final Map<String, String> params) {
            this.params = params;
            return this;
        }
        
        public Builder addParams(final Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }
        
        public Request build(){
            Request request = new Request();
            request.setRequestType(requestType);
            request.setEndpoint(endpoint);
            request.setHeaders(headers);
            request.setJsonBody(jsonBody);
            request.setParams(params);
            return request;
        }
    }

    /**
     * @return the requestType
     */
    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(final REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    private void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the headers
     */
    public Map<String, Object> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    private void setHeaders(final Map<String, Object> headers) {
        this.headers = headers;
    }

    /**
     * @return the jsonBody
     */
    public JsonNode getJsonBody() {
        return jsonBody;
    }

    /**
     * @param jsonBody the jsonBody to set
     */
    private void setJsonBody(final JsonNode jsonBody) {
        this.jsonBody = jsonBody;
    }

    /**
     * @return the params
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    private void setParams(final Map<String, String> params) {
        this.params = params;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("\n")
                .append("endpoint", endpoint).append("\n")
                .append("headers", headers).append("\n")
                .append("params", params).append("\n")
                .append("jsonBody", jsonBody)
                .build();
    }
}
