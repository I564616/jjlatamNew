/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.jnj.restservice.logic.Request.REQUEST_TYPE;
import com.jnj.restservice.logic.Response;
import com.jnj.restservice.logic.client.AbstractClient;
import com.jnj.restservice.logic.dispatcher.DispatcherImpl;
import com.jnj.restservice.logic.dispatcher.IDispatcher;
import com.jnj.restservice.logic.handler.MasterHandlerImpl;
import com.jnj.restservice.util.RestServiceConstant;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;


@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RestAPIClient extends AbstractClient {

	protected ConfigurationService configurationService;


	public enum HEADER_SET {
		EMPTY_HEADER,
		AWS_HEADER
	}

	public  <T> Response<T>  post(Class<T> clazz, final String endpoint, final JsonNode jsonBody, final HEADER_SET headerSet) {
		return submit(clazz, REQUEST_TYPE.POST, endpoint, jsonBody, getHeadersByHeaderSet(headerSet));
	}

	public  <T> Response<T>  get(final Class<T> clazz, final String endpoint, final HEADER_SET headerSet) {
		return submit(clazz, REQUEST_TYPE.GET, endpoint, null, getHeadersByHeaderSet(headerSet));
	}

	public  <T> Response<T>  patch(Class<T> clazz, final String endpoint, final JsonNode jsonBody, final HEADER_SET headerSet) {
		return submit(clazz, REQUEST_TYPE.PATCH, endpoint, jsonBody, getHeadersByHeaderSet(headerSet));
	}

	public  <T> Response<T>  patch(Class<T> clazz, final String endpoint, final JsonNode jsonBody, final Map<String, Object> headers) {
		return submit(clazz, REQUEST_TYPE.PATCH, endpoint, jsonBody, headers);
	}
	public <T> Response<T>  submit(final Class<T> clazz
			, final REQUEST_TYPE requestType
			, final String endpoint
			, final JsonNode jsonBody
			, final Map<String, Object> headers) {
		Response<T> restResponse = submitAndGetResponse(
				clazz, requestType, endpoint
				, jsonBody, headers);

		return restResponse;
	}

	private Map<String, Object> getHeadersByHeaderSet(final HEADER_SET headerSet) {
		Map<String, Object> headers = new HashMap<String, Object>();

		if (headerSet == HEADER_SET.AWS_HEADER) {
			headers.put(RestServiceConstant.APIGEE_X_API_KEY_HEADER,getAPIKEY(RestServiceConstant.JNJ_API_KEY));
			headers.put(RestServiceConstant.ACCEPT_HEADER, MediaType.APPLICATION_JSON);
			headers.put(RestServiceConstant.CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON);
		} 
		return headers;
	}

	private String getAPIKEY(final String keyFromConfigFile) {
		return getConfigurationService().getConfiguration().getString(RestServiceConstant.AWS_API_KEY);
	}

	@Override
	public <T> IDispatcher<T> getDispatcher(final Class<T> clazz) {
		return new DispatcherImpl<T>(MasterHandlerImpl.getInstance(), clazz);
	}

	public  ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}



}
