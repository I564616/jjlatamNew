package com.jnj.b2b.storefront.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.jnj.b2b.storefront.security.GUIDCookieStrategy;

import de.hybris.platform.servicelayer.session.SessionService;

public class SSOAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	@Autowired
	private SessionService sessionService;

	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private GUIDCookieStrategy guidCookieStrategy;



	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication) throws IOException,ServletException
	{
		sessionService.setAttribute("ssoLogin", true);
		getGuidCookieStrategy().setCookie(request, response);
		getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);

	}

	public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}

	public void setAuthenticationSuccessHandler(
		AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public GUIDCookieStrategy getGuidCookieStrategy() {
		return guidCookieStrategy;
	}

	public void setGuidCookieStrategy(GUIDCookieStrategy guidCookieStrategy) {
		this.guidCookieStrategy = guidCookieStrategy;
	}

}