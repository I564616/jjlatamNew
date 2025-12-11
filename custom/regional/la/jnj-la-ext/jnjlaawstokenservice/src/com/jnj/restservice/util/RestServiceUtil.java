/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jnj.restservice.logic.Response;
import com.jnj.restservice.logic.client.RestAPIClient;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import net.minidev.json.JSONObject;

public class RestServiceUtil {

	protected RestAPIClient restApiClient;
	protected ConfigurationService configurationService;

	public  Response<String> getPasswordResetToken(final String email){
		final String tokenEndPoint =getConfigurationService().getConfiguration().getString(RestServiceConstant.AWS_PSWD_TOKEN_GENERATION_URL);
		JsonNode node = JsonNodeFactory.instance.objectNode();
		((ObjectNode) node).put(RestServiceConstant.AWS_METHOD_KEY, RestServiceConstant.AWS_METHOD_VALUE_EMAIL);
		((ObjectNode) node).put(RestServiceConstant.AWS_EMAIL_KEY,email);
		Response<String> response = getRestApiClient().post(String.class,  tokenEndPoint, node, RestAPIClient.HEADER_SET.AWS_HEADER);
		return response;
	}

	public Response<String> verifyPasswordToken(final String email,final String token,final boolean deleteFlag ){
		final String tokenVaildationEndPoint =getConfigurationService().getConfiguration().getString(RestServiceConstant.AWS_PSWD_TOKEN_VERIFICATION_URL);
		JsonNode node = JsonNodeFactory.instance.objectNode();
		((ObjectNode) node).put(RestServiceConstant.AWS_EMAIL_KEY, email);
		((ObjectNode) node).put(RestServiceConstant.AWS_TOKEN_KEY,token);
		((ObjectNode) node).put(RestServiceConstant.AWS_DELETE_TOKEN,deleteFlag);
		Response<String> response = getRestApiClient().post(String.class,  tokenVaildationEndPoint, node, RestAPIClient.HEADER_SET.AWS_HEADER);
		return response;
	}

	public String getMultimodeJsonResponse(final boolean response , final int code, final String message){
		return getMultimodeJsonResponse(response,String.valueOf(code),message);
	}
	
	public String getMultimodeJsonResponse(final boolean response , final String code, final String message){
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response", String.valueOf(response));
		responseMap.put("responseCode", code);
		responseMap.put("responseMessage", message);
		JSONObject jOBJ = new JSONObject(responseMap);
		return jOBJ.toJSONString();
	}
	public RestAPIClient getRestApiClient() {
		return restApiClient;
	}

	public void setRestApiClient(final RestAPIClient restApiClient) {
		this.restApiClient = restApiClient;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
