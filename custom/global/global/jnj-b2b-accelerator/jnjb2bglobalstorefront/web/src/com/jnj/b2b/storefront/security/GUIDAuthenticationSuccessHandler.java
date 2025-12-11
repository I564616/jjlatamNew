/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.storefront.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import de.hybris.platform.servicelayer.session.SessionService;

/**
 * Default implementation of {@link AuthenticationSuccessHandler}.
 */
public class GUIDAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private GUIDCookieStrategy guidCookieStrategy;
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private SessionService sessionService;
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		sessionService.setAttribute("ssoLogin", false);
		getGuidCookieStrategy().setCookie(request, response);
		getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
	}

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	/**
	 * @param guidCookieStrategy
	 *           the guidCookieStrategy to set
	 */
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		return authenticationSuccessHandler;
	}

	/**
	 * @param authenticationSuccessHandler
	 *           the authenticationSuccessHandler to set
	 */
	public void setAuthenticationSuccessHandler(final AuthenticationSuccessHandler authenticationSuccessHandler)
	{
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}
	public SessionService getSessionService()
	{
		return sessionService;
	}
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
