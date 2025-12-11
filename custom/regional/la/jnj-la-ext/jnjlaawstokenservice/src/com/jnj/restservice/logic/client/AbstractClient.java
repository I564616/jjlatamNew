/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.jnj.restservice.logic.Request;
import com.jnj.restservice.logic.Request.REQUEST_TYPE;
import com.jnj.restservice.logic.Response;
import com.jnj.restservice.logic.dispatcher.DispatcherImpl;
import com.jnj.restservice.logic.dispatcher.IDispatcher;
import com.jnj.restservice.util.RestServiceConstant;

public abstract class AbstractClient {

    public abstract <T> IDispatcher<T> getDispatcher(Class<T> clazz);

	private static final Logger LOG = Logger.getLogger(DispatcherImpl.class);

    /**
     * Default getting headers for any services. Please override this method to make a custom headers
     *
     * @param servletRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> buildHeaders(final HttpServletRequest servletRequest) {
        final Map<String, Object> headers = new HashMap<String, Object>();

        final Enumeration<String> eHeader = servletRequest.getHeaderNames();
        while (eHeader.hasMoreElements()) {
            final String key = eHeader.nextElement();
            headers.put(key, servletRequest.getHeader(key));
        }

        return headers;
    }

    public String getHeader(final String headerKey) {
        final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (RequestContextHolder.getRequestAttributes() != null) {
            final HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();

            return request.getHeader(headerKey);
        }

        return null;
    }


    public Request getRestRequest(final Map<String, Object> headers, final String endpoint, final REQUEST_TYPE requestType) {
        return getRestRequest(headers, endpoint, requestType, null);
    }

    public Request getRestRequest(final Map<String, Object> headers, final String endpoint, final REQUEST_TYPE requestType, final JsonNode jsonBody) {
        final Request.Builder requestBuilder = new Request.Builder()
                .endpoint(endpoint)
                .requestType(requestType);

        for (final String key : headers.keySet()) {
            requestBuilder.addHeader(key, headers.get(key));
        }

        if (jsonBody != null) {
            requestBuilder.jsonBody(jsonBody);
        }

        try {
            URL url;
            url = URI.create(endpoint).toURL();
            final String userInfo = url.getUserInfo();
            if (userInfo != null) {
                final String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(userInfo.getBytes());
                requestBuilder.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
            }
        } catch (final MalformedURLException e) {
			LOG.error(e);
        }
        final Request restRequest = requestBuilder.build();

        return restRequest;
    }

    public  <T> Response<T>  submit(final Class<T> clazz
            , final REQUEST_TYPE requestType
            , final String endpoint
            , final JsonNode jsonBody
            , final Map<String, Object> headers) {
        final Response<T> restResponse = submitAndGetResponse(clazz, requestType, endpoint, jsonBody, headers);

        return restResponse;
    }

    public <T> Response<T> submitAndGetResponse(final Class<T> clazz
            , final REQUEST_TYPE requestType
            , final String endpoint
            , final JsonNode jsonBody
            , Map<String, Object> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (RequestContextHolder.getRequestAttributes() != null) {
            final HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            headers.put(RestServiceConstant.JNJ_API_KEY, request.getHeader(RestServiceConstant.JNJ_API_KEY));
        }

        final Request request = this.getRestRequest(headers, endpoint, requestType, jsonBody);

        return this.getDispatcher(clazz).dispatch(request);
    }

}
