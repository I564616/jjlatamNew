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
package com.jnj.b2b.storefront.security.impl;

import de.hybris.platform.servicelayer.session.SessionService;

import java.io.Serial;
import java.io.Serializable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.jnj.b2b.storefront.constants.WebConstants;


/**
 * Extension of HttpSessionRequestCache that allows pass through of cookies from the current request. This is required
 * to allow the GUIDInterceptor to see the secure cookie written during authentication. The <tt>RequestCache</tt> stores
 * the <tt>SavedRequest</tt> in the HttpSession, this is then restored perfectly. Unfortunately the saved request also
 * hides new cookies that have been written since the saved request was created. This implementation allows the current
 * request's cookie values to override the cookies within the saved request.
 */
public class WebHttpSessionRequestCache extends HttpSessionRequestCache implements Serializable
{
	private static final String REFERER = "referer";

	static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
	static final String BACKUP_SAVED_REQUEST = "SPRING_SECURITY_BACKUP_SAVED_REQUEST";
	private PortResolver portResolver = new PortResolverImpl();
	private RequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;
	private boolean createSessionAllowed = true;
	private SessionService sessionService;

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Override
	public void setRequestMatcher(final RequestMatcher requestMatcher)
	{
		this.requestMatcher = requestMatcher;
		super.setRequestMatcher(requestMatcher);
	}


	@Override
	public void setPortResolver(final PortResolver portResolver)
	{
		this.portResolver = portResolver;
		super.setPortResolver(portResolver);
	}


	@Override
	public void setCreateSessionAllowed(final boolean createSessionAllowed)
	{
		this.createSessionAllowed = createSessionAllowed;
	}

	@Override
	public void saveRequest(final HttpServletRequest request, final HttpServletResponse response)
	{
		//this might be called while in ExceptionTranslationFilter#handleSpringSecurityException in this case base implementation
		if (SecurityContextHolder.getContext().getAuthentication() == null)
		{
			super.saveRequest(request, response);
		}
		else
		{
			final SavedRequest savedBefore = getRequest(request, response);
			if (savedBefore != null)//to not override request saved by ExceptionTranslationFilter#handleSpringSecurityException
			{
				return;
			}

			if (getRequestMatcher().matches(request))
			{
				 
				
				DefaultSavedRequest  savedRequest = new DefaultSavedRequest(request, getPortResolver())
				{
					/**
					 * 
					 */
					@Serial
					private static final long serialVersionUID = 990881L;
					private final String contextPath = request.getContextPath();

					@Override
					public String getRedirectUrl()
					{
						String referer = null;
						String originalReferer = null;
						final HttpSession session = request.getSession();
						System.out.println(session);
						if(request.getSession()!=null && request.getSession().getAttribute("originalReferer")!=null){
							originalReferer = (String)  request.getSession().getAttribute("originalReferer");
						}
						System.out.println("Original Referer --->"+originalReferer);
						if(request.getHeader(REFERER)!=null && !request.getHeader(REFERER).contains("login")){ 
							referer = request.getHeader(REFERER);
						}else if(originalReferer!=null){
							referer= originalReferer;
						}
						System.out.println("Original refererrefererreferer --->"+referer);

						if(StringUtils.isBlank(referer)){
							return null;
						}
						String endUrl = calculateRelativeRedirectUrl(contextPath, referer);
						if(StringUtils.isBlank(endUrl) || endUrl.equals("/")){
							endUrl = "/home";	
						}
						
						return endUrl;
					}
				};

				if (isCreateSessionAllowed() || request.getSession(false) != null)
				{
					request.getSession().setAttribute(SAVED_REQUEST, savedRequest);
					request.getSession().setAttribute(BACKUP_SAVED_REQUEST, savedRequest);
					logger.debug("DefaultSavedRequest added to Session: " + savedRequest);
				}
			}
			else
			{
				logger.debug("Request not saved as configured RequestMatcher did not match");
			}
		}
	}


	protected boolean isCreateSessionAllowed()
	{
		return createSessionAllowed;
	}

	protected PortResolver getPortResolver()
	{
		return portResolver;
	}

	protected RequestMatcher getRequestMatcher()
	{
		return requestMatcher;
	}

	@Override
	public HttpServletRequest getMatchingRequest(final HttpServletRequest request, final HttpServletResponse response)
	{
		HttpServletRequest result = super.getMatchingRequest(request, response);
		if (result != null)
		{
			result = new CookieMergingHttpServletRequestWrapper(result, request);
		}
		return result;
	}

	protected String calculateRelativeRedirectUrl(final String contextPath, final String url)
	{
		if (UrlUtils.isAbsoluteUrl(url))
		{
			String relUrl = url.substring(url.indexOf("://") + 3);
			String modifiedContextPath = contextPath;
			final String urlEncodingAttributes = getSessionService().getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
			if (urlEncodingAttributes != null && !url.contains(urlEncodingAttributes)
					&& modifiedContextPath.contains(urlEncodingAttributes))
			{
				modifiedContextPath = StringUtils.remove(modifiedContextPath, urlEncodingAttributes);
			}
			if (StringUtils.isEmpty(relUrl) || StringUtils.isEmpty(modifiedContextPath))
			{
				relUrl = "/";
			}
			else
			{
				relUrl = relUrl.substring(relUrl.indexOf(modifiedContextPath) + modifiedContextPath.length());
			}
			return relUrl;
		}
		else
		{
			return url;
		}
	}


}
