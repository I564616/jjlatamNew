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
package com.jnj.b2b.storefront.filters;

import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import com.jnj.b2b.storefront.history.BrowseHistory;
import com.jnj.b2b.storefront.history.BrowseHistoryEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.CookieGenerator;

import com.jnj.b2b.storefront.history.BrowseHistory;
import com.jnj.b2b.storefront.history.BrowseHistoryEntry;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;


/**
 * Filter that initializes the session for the jnjb2bglobalstorefront
 */
public class StorefrontFilter extends GenericFilterBean
{
	public static final String AJAX_REQUEST_HEADER_NAME = "X-Requested-With";
	public static final String ORIGINAL_REFERER = "originalReferer";

	private StoreSessionFacade storeSessionFacade;
	private BrowseHistory browseHistory;
	private CookieGenerator cookieGenerator;
	
	@Autowired(required=true)
	BaseSiteService baseSiteService;
	
	@Autowired(required=true)
	SessionService sessionService;
	
	@Autowired(required=true)
	UserService userService;
	
	@Autowired
	private RedirectStrategy redirectStrategy;

	
	
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final HttpSession session = httpRequest.getSession();
		final String queryString = httpRequest.getQueryString();
	    boolean siteChange = false;
	    
		if(null !=sessionService.getAttribute("changeSite"))
		{
			siteChange = (boolean) sessionService.getAttribute("changeSite");		
		}
		
		if (isSessionNotInitialized(session, queryString) || siteChange)
		{
			initDefaults(httpRequest);

			markSessionInitialized(session);
		}

		// For secure requests ensure that the JSESSIONID cookie is visible to insecure requests
		if (isRequestSecure(httpRequest))
		{
			fixSecureHttpJSessionIdCookie(httpRequest, (HttpServletResponse) response);
		}

	
		String incomingURL =  httpRequest.getRequestURL().toString();
		if (isGetMethod(httpRequest))
		{
			
			if (StringUtils.isBlank(httpRequest.getHeader(AJAX_REQUEST_HEADER_NAME)))
			{
				session.setAttribute(ORIGINAL_REFERER, httpRequest.getRequestURL().toString());
			}

			getBrowseHistory().addBrowseHistoryEntry(new BrowseHistoryEntry(httpRequest.getRequestURI(), null));
		}
		chain.doFilter(request, response);
	}



	protected boolean isGetMethod(final HttpServletRequest httpRequest)
	{
		return "GET".equalsIgnoreCase(httpRequest.getMethod());
	}

	protected boolean isRequestSecure(final HttpServletRequest httpRequest)
	{
		return httpRequest.isSecure();
	}

	protected boolean isSessionNotInitialized(final HttpSession session, final String queryString)
	{
		return session.isNew() || StringUtils.contains(queryString, CMSFilter.CLEAR_CMSSITE_PARAM)
				|| !isSessionInitialized(session);
	}

	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	public void setBrowseHistory(final BrowseHistory browseHistory)
	{
		this.browseHistory = browseHistory;
	}

	protected void initDefaults(final HttpServletRequest request)
	{
		final StoreSessionFacade storeSessionFacade = getStoreSessionFacade();
		BaseSiteModel site = baseSiteService.getCurrentBaseSite();
		if(null!=site && null!=site.getDefaultLanguage()){
			site.getDefaultLanguage().getIsocode();
			List<Locale> list = new ArrayList<Locale>(1);
			list.add(Locale.of(site.getDefaultLanguage().getIsocode()));
			storeSessionFacade.initializeSession(list);
			storeSessionFacade.setCurrentLanguage(site.getDefaultLanguage().getIsocode());
		}else{
		storeSessionFacade.initializeSession(Collections.list(request.getLocales()));
		}
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	protected BrowseHistory getBrowseHistory()
	{
		return browseHistory;
	}

	protected boolean isSessionInitialized(final HttpSession session)
	{
		return session.getAttribute(this.getClass().getName()) != null;
	}

	protected void markSessionInitialized(final HttpSession session)
	{
		session.setAttribute(this.getClass().getName(), "initialized");
	}

	protected void fixSecureHttpJSessionIdCookie(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse)
	{
		final HttpSession session = httpServletRequest.getSession(false);
		if (session != null)
		{
			getCookieGenerator().addCookie(httpServletResponse, session.getId());
		}

	}

	protected CookieGenerator getCookieGenerator()
	{
		return cookieGenerator;
	}

	public void setCookieGenerator(final CookieGenerator cookieGenerator)
	{
		this.cookieGenerator = cookieGenerator;
	}



	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}



	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}



	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}



	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}



	public UserService getUserService() {
		return userService;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
