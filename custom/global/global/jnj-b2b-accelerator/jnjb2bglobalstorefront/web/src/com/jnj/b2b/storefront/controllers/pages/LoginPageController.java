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
package com.jnj.b2b.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;


import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.CookieGenerator;

import com.jnj.b2b.storefront.controllers.ControllerConstants;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{
	public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";

	@Resource(name = "guidCookieGenerator")
	private CookieGenerator cookieGenerator;

	@Resource(name = "httpSessionRequestCache")
	private HttpSessionRequestCache httpSessionRequestCache;

	public CookieGenerator getCookieGenerator()
	{
		return cookieGenerator;
	}


	public HttpSessionRequestCache getHttpSessionRequestCache()
	{
		return httpSessionRequestCache;
	}


	public UserService getUserService()
	{
		return userService;
	}

	@Resource
	private UserService userService;

	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}


	@GetMapping
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			@RequestParam(value = "passwordExpireToken", required = false) final String passwordExpireToken,
			@RequestParam(value = "email", required = false) final String email,
			@RequestParam(value = "contactUs", required = false) final String helpFlag, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException, IOException
	{

		final UserModel user = userService.getCurrentUser();
		final boolean isUserAnonymous = user == null || userService.isAnonymousUser(user);
		final String guid = (String) request.getSession().getAttribute(SECURE_GUID_SESSION_KEY);

		if (!doRedirect(request, response, isUserAnonymous, guid))
		{
			return REDIRECT_PREFIX + ROOT;
		}

		if (!loginError)
		{
			storeReferer(referer, request, response);
		}
		return getDefaultLoginPage(loginError, session, model);
	}


	protected boolean doRedirect(final HttpServletRequest request, final HttpServletResponse response,
			final boolean isUserAnonymous, final String guid)
	{
		boolean redirect = true;

		if (!isUserAnonymous && guid != null && request.getCookies() != null)
		{
			final String guidCookieName = cookieGenerator.getCookieName();
			if (guidCookieName != null)
			{
				for (final Cookie cookie : request.getCookies())
				{
					if (guidCookieName.equals(cookie.getName()))
					{
						if (guid.equals(cookie.getValue()))
						{
							redirect = false;
							break;
						}
						else
						{
							cookieGenerator.removeCookie(response);
						}
					}
				}
			}
		}
		return redirect;
	}

	@Override
	protected String getLoginView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}

		return "/my-account";
	}

	@Override
	protected AbstractPageModel getLoginCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

}
