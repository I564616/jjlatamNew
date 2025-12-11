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
package com.jnj.b2b.loginaddon.security;

import com.jnj.b2b.storefront.security.GUIDCookieStrategy;
import com.jnj.b2b.storefront.util.JnjSSOUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.core.util.JnjGTCoreUtil;
import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class JnjStorefrontLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{
	protected GUIDCookieStrategy guidCookieStrategy;
	protected List<String> restrictedPages;

	@Autowired
	protected B2BCartService b2bCartService;

	@Autowired
	protected JnjGTOperationsService jnjGTOperationsService;

	@Autowired
	private SessionService sessionService;


	@Autowired
	protected UserService userService;
	public UserService getUserService() {
		return userService;
	}
	public ModelService getModelService() {
		return modelService;
	}
	@Autowired
	protected ModelService modelService;
	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
								final Authentication authentication) throws IOException, ServletException
	{
		getGuidCookieStrategy().deleteCookie(request, response);
		JaloSession.getCurrentSession().close();
		JnjSSOUtil.eraseSamlCookie(response);
		request.getSession().invalidate();

		this.logAuditData("logout", "User Logout Event.session expires.", JnjGTCoreUtil.getIpAddress(request), true, true,
			new Date());
		if(null != authentication)
		{
			String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
			UserModel currentUserModel = userService.getUserForUID(username);
			if(currentUserModel instanceof JnJB2bCustomerModel)
			{
				final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) currentUserModel;
				if(currentUser.getSsoLogin()!= null && currentUser.getSsoLogin()==true)
				{
					//Idp logout call
					currentUser.setSsoLogin(Boolean.FALSE);
					modelService.save(currentUser);
				}
			}
		}
		super.onLogoutSuccess(request, response, authentication);
	}


	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		String targetUrl = super.determineTargetUrl(request, response);


		targetUrl = targetUrl + Jnjb2bCoreConstants.Login.LOGOUT_URL;

		System.out.println("===================TARGET URL IN LOGINADDON=================" + targetUrl);
		for (final String restrictedPage : getRestrictedPages())
		{
			// When logging out from a restricted page, return user to homepage.
			if (targetUrl.contains(restrictedPage))
			{
				targetUrl = super.getDefaultTargetUrl();
			}
		}

		System.out.println("=========loginaddon target url is=========" + targetUrl);
		return targetUrl;

	}

	protected void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
								final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent,
			null);
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

}