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
package com.jnj.b2b.storefront.servlets;


import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;


/**
 * Filter which allows us to bypass all of the spring filters for requests to a given path.
 */
public class ResourceFilter implements Filter
{
	protected static final String COMMON_DEFAULT_SERVLET_NAME = "default";
	protected static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";
	protected static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";
	protected static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";
	protected static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";

	private RequestDispatcher defaultRequestDispatcher;

	protected RequestDispatcher getDefaultRequestDispatcher()
	{
		return defaultRequestDispatcher;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		final ServletContext servletContext = filterConfig.getServletContext();

		if (servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null)
		{
			this.defaultRequestDispatcher = servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME);
		}
		else if (servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null)
		{
			this.defaultRequestDispatcher = servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME);
		}
		else if (servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME) != null)
		{
			this.defaultRequestDispatcher = servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME);
		}
		else if (servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null)
		{
			this.defaultRequestDispatcher = servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME);
		}
		else if (servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null)
		{
			this.defaultRequestDispatcher = servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME);
		}
		else
		{
			throw new IllegalStateException(
					"Unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.");
		}
	}

	@Override
	public void destroy()
	{
		// No implementation
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		getDefaultRequestDispatcher().forward(request, response);
	}
}