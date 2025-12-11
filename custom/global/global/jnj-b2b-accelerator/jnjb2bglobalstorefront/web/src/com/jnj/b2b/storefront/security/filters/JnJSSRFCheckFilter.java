/**
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.b2b.storefront.security.filters;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jnj.b2b.storefront.security.JnjReadHttpServletRequestWrapper;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * Request filter to detect malicious content in the API input request
 * parameters.
 */
public class JnJSSRFCheckFilter extends OncePerRequestFilter {

	private static final Logger LOG = Logger.getLogger(JnJSSRFCheckFilter.class);

	private static final String SPLIT_TOKEN_SLASH = "/";
	private static final String SPLIT_TOKEN_COMMA = ",";
	private static final String CHARACTER_ENCODING_UTF8 = "UTF-8";
	private static final String SSRF_FILTER_ENABLED_FLAG_KEY = "ssrf.filter.enabled";
	private static final String SSRF_FILTER_HTTP_METHODS_ALLOWED_KEY = "ssrf.filter.request.methods.allowed";
	private static final String SSRF_FILTER_URLS_ALLOWED_KEY = "ssrf.filter.request.urls.allowed";
	private static final String SSRF_REQUEST_BLACKLIST_CHARS_KEY = "ssrf.filter.request.characters.blacklist";
	private static final String SSRF_REQUEST_XML_CHARACTERS_BLACKLIST = "ssrf.filter.request.xml.characters.blacklist";

	@Resource
	private ConfigurationService configurationService;

	@Autowired
	protected SessionService sessionService;

	@Override
	protected void doFilterInternal(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final FilterChain filterChain)
			throws ServletException, IOException {

		LOG.debug("Inside doFilterInternal() in jnjLASSRFCheckFilter.java for request URI: "
				+ httpServletRequest.getRequestURI() + ".HTTP Method: " + httpServletRequest.getMethod());

		boolean isFilterEnabled = configurationService.getConfiguration().getBoolean(
				SSRF_FILTER_ENABLED_FLAG_KEY, false);

		if (isFilterEnabled && isAllowedHTTPMethod(httpServletRequest) && isAllowedURL(httpServletRequest)) {
			processRequest(httpServletRequest, httpServletResponse, filterChain);
		} else {
			LOG.debug("SSRF check filter is either not enabled or the http method is excluded from validation");
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private boolean isAllowedHTTPMethod(final HttpServletRequest httpServletRequest) {
		LOG.debug("Inside isAllowedHTTPMethod(). Request HTTP Method:" + httpServletRequest.getMethod());
		List<String> allowedHttpMethodsList = new ArrayList<>();
		String filterHttpMethodsStr = configurationService.getConfiguration().getString(
				SSRF_FILTER_HTTP_METHODS_ALLOWED_KEY, StringUtils.EMPTY);

		if (StringUtils.isNotEmpty(filterHttpMethodsStr)) {
			allowedHttpMethodsList = Arrays.asList(filterHttpMethodsStr.split(SPLIT_TOKEN_COMMA));
		}
		return CollectionUtils.isNotEmpty(allowedHttpMethodsList)
				&& allowedHttpMethodsList.contains(httpServletRequest.getMethod());
	}

	private boolean isAllowedURL(final HttpServletRequest httpServletRequest) {
		String requestURI = httpServletRequest.getRequestURI();

		LOG.info("Inside isAllowedURL() in jnjLASSRFCheckFilter.java for request URI: " + requestURI);
		List<String> allowedURLsList = new ArrayList<>();

		String finalURI = requestURI.contains(SPLIT_TOKEN_SLASH)
				? requestURI.substring(requestURI.lastIndexOf(SPLIT_TOKEN_SLASH)) : requestURI;
		String filterConfigURLsStr = configurationService.getConfiguration().getString(SSRF_FILTER_URLS_ALLOWED_KEY,
				StringUtils.EMPTY);

		if (StringUtils.isNotEmpty(filterConfigURLsStr)) {
			allowedURLsList = Arrays.asList(filterConfigURLsStr.split(SPLIT_TOKEN_COMMA));
		}

		return CollectionUtils.isNotEmpty(allowedURLsList) && allowedURLsList.contains(finalURI);
	}

	private void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
								FilterChain filterChain) throws IOException, ServletException {
		final String blacklistCharacterConfig = configurationService.getConfiguration()
				.getString(SSRF_REQUEST_BLACKLIST_CHARS_KEY);

		LOG.info("Begin validating data for : " + httpServletRequest.getRequestURI());

		JnjReadHttpServletRequestWrapper jnjHttpServletRequestWrapper = new JnjReadHttpServletRequestWrapper(
				httpServletRequest);
		String referrer = httpServletRequest.getHeader("referer");

		if (StringUtils.isNotBlank(blacklistCharacterConfig)) {
			List<String> blacklistCharInputList = Arrays.asList(
					blacklistCharacterConfig.replace("\"", StringUtils.EMPTY).split(SPLIT_TOKEN_COMMA));

			if (RequestMethod.POST.name().equalsIgnoreCase(httpServletRequest.getMethod())
					&& containsMaliciousCharsInRequestBody(httpServletRequest, httpServletResponse,
					blacklistCharInputList, jnjHttpServletRequestWrapper)) {
				LOG.debug("Validated POST request. Found malicious data in request body");
				if (referrer != null) {
					redirectBackToReferrer(referrer, httpServletResponse);
					return;
				}
			}
			if (containsMaliciousCharsInRequestURI(httpServletRequest, httpServletResponse, blacklistCharInputList)) {
				LOG.debug("Found malicious data in GET request URI");
				if (referrer != null) {
					redirectBackToReferrer(referrer, httpServletResponse);
					return;
				}
			}
			if (containsMaliciousCharsInRequestParameters(httpServletRequest, httpServletResponse,
					blacklistCharInputList)) {
				LOG.info("Found malicious data in GET request parameters");
				if (referrer != null) {
					redirectBackToReferrer(referrer, httpServletResponse);
					return;
				}
			}
		} else {
			LOG.warn("Malicious character list is not configured and hence skipping the " +
					"input request sanitization for the request");
		}

		LOG.debug("No malicious data found in the request");
		filterChain.doFilter(jnjHttpServletRequestWrapper, httpServletResponse);
	}

	private boolean containsMaliciousCharsInRequestURI(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final List<String> blacklistCharInputList)
			throws IOException {

		final List<String> requestParamsList = Arrays.asList(httpServletRequest.getRequestURI().split(SPLIT_TOKEN_SLASH));

		LOG.debug("Inside containsMaliciousCharsInRequestURI() in JnjSSRFCheckFilter.java with request params: "
				+ requestParamsList);

		if (CollectionUtils.isNotEmpty(requestParamsList)) {
			for (String requestParam : requestParamsList) {
				for (String blacklistChar : blacklistCharInputList) {
					requestParam = requestParam.replaceAll(Pattern.quote("%"), "<percentage>");
					if (!URLDecoder.decode(requestParam, CHARACTER_ENCODING_UTF8).isEmpty()
							&& (URLDecoder.decode(requestParam, CHARACTER_ENCODING_UTF8).contains(blacklistChar))
							&& StringUtils.isNotBlank(blacklistChar)) {
						httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						LOG.error("At least one of your URI input parameters contains bad characters. " +
								"Hence sending error response from filter:"
								+ URLDecoder.decode(requestParam, CHARACTER_ENCODING_UTF8) + ":" + blacklistChar);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean containsMaliciousCharsInRequestParameters(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final List<String> blacklistCharInputList)
			throws IOException {
		LOG.debug("Inside containsMaliciousCharsInRequestParameters() in JnjSSRFCheckFilter.java");
		final List<String> paramKeyList = Collections.list(httpServletRequest.getParameterNames());
		Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
		paramKeyList.removeIf(entry -> entry.contains("password"));

		if (CollectionUtils.isNotEmpty(paramKeyList)) {
			for (String paramKey : paramKeyList) {
				List<String> paramValueList = Arrays.asList(parameterMap.get(paramKey));
				for (String paramValue : paramValueList) {
					if (StringUtils.isNotBlank(paramValue)) {
						LOG.debug("paramKey:" + paramKey + " & paramValue:"
								+ URLDecoder.decode(paramValue, CHARACTER_ENCODING_UTF8));
						for (String blacklistChar : blacklistCharInputList) {
							paramValue = paramValue.replaceAll(Pattern.quote("%"), "<percentage>");
							if (!URLDecoder.decode(paramValue, CHARACTER_ENCODING_UTF8).isEmpty()
									&& (URLDecoder.decode(paramValue, CHARACTER_ENCODING_UTF8).contains(blacklistChar))
									&& StringUtils.isNotBlank(blacklistChar)) {
								httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								LOG.error("At least one of your input request parameters contains bad characters. "
										+ "Hence sending error response from filter");
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean containsMaliciousCharsInRequestBody(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final List<String> blacklistCharInputList,
			JnjReadHttpServletRequestWrapper jnjHttpServletRequestWrapper) throws IOException {

		String contentType = httpServletRequest.getContentType();
		String inputRequestData = jnjHttpServletRequestWrapper.getBody();

		LOG.info("Inside containsMaliciousCharsInRequestBody() in JnjSSRFCheckFilter.java with POST request body ### "
				+ inputRequestData);

		if (MediaType.APPLICATION_XML_VALUE.equalsIgnoreCase(contentType)) {
			return findMaliciousInXML(httpServletResponse, inputRequestData);
		} else {
			List<String> requestParamsList = Arrays.asList(inputRequestData.split(SPLIT_TOKEN_COMMA));
			for (String reqParam : requestParamsList) {
				reqParam = reqParam.replaceAll(Pattern.quote("%"), "<percentage>");
				LOG.info("Original request param : " + reqParam + "and decoded request param"
						+ URLDecoder.decode(reqParam, CHARACTER_ENCODING_UTF8));
				for (String maliciousInput : blacklistCharInputList) {
					if (!URLDecoder.decode(reqParam, CHARACTER_ENCODING_UTF8).isEmpty()
							&& (URLDecoder.decode(reqParam, CHARACTER_ENCODING_UTF8).contains(maliciousInput))
							&& StringUtils.isNotBlank(maliciousInput)) {
						httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						LOG.error("Input request value: " + reqParam + " contains blacklisted character: "
								+ maliciousInput + ". Hence sending error response from filter");
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean findMaliciousInXML(final HttpServletResponse httpServletResponse, final String inputRequest)
			throws IOException {
		LOG.debug("Inside findMaliciousInXML() in JnjSSRFCheckFilter.java");
		final String xmlBlacklistChars = configurationService.getConfiguration()
				.getString(SSRF_REQUEST_XML_CHARACTERS_BLACKLIST);
		final List<String> maliciousCharList = Arrays
				.asList(xmlBlacklistChars.replace("\"", StringUtils.EMPTY).split(SPLIT_TOKEN_COMMA));
		for (String maliciousInput : maliciousCharList) {
			if (!URLDecoder.decode(inputRequest, CHARACTER_ENCODING_UTF8).isEmpty()
					&& (URLDecoder.decode(inputRequest, CHARACTER_ENCODING_UTF8).contains(maliciousInput))) {
				httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				LOG.error("Input XML contains bad characters. Hence sending error response from filter");
				return true;
			}
		}
		return false;
	}

	private void redirectBackToReferrer(String referrer, HttpServletResponse httpServletResponse) {
		LOG.debug("Inside redirectBackToReferrer() in JnjSSRFCheckFilter.java. Referrer:" + referrer);
		try {
			sessionService.setAttribute("SSRFFailed", "SSRF check failed for your request. Please check input");
			httpServletResponse.sendRedirect(referrer);
		} catch (IOException exception) {
			LOG.error("Error redirecting back to the referrer after validation in JnJSSRFCheckFilter.java", exception);
		}
	}

}
