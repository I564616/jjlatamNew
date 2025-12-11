package com.jnj.b2b.loginaddon.recaptcha;

import jakarta.servlet.http.HttpServletRequest;


/**
 * This class holds common web methods
 * 
 * @author Accenture
 * 
 */
public class JnjWebUtil
{
	final static String HTTP = "http";
	final static String COLON = ":";
	final static String DOUBLE_SLASH = "//";
	final static String SINGLE_SLASH = "/";

	/**
	 * This method returns the current site's server URL.
	 * 
	 * @param request
	 * @return site URL
	 */
	public static String getServerUrl(final HttpServletRequest request)
	{
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
		{
			return request.getScheme() + COLON + DOUBLE_SLASH + request.getServerName() + request.getContextPath();
		}
		else
		{
			return request.getScheme() + COLON + DOUBLE_SLASH + request.getServerName() + COLON + request.getServerPort()
					+ request.getContextPath();
		}
	}

	/**
	 * This method returns the current site' un-secure server URL (Converts HTTPs to HTTP).
	 * 
	 * @param request
	 * @return site URL
	 */
	public static String getUnsecureServerUrl(final HttpServletRequest request)
	{
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
		{
			return HTTP + COLON + DOUBLE_SLASH + request.getServerName();
		}
		else
		{
			return HTTP + COLON + DOUBLE_SLASH + request.getServerName() + COLON + request.getServerPort();
		}
	}

	/**
	 * This method returns securedURL without context path
	 * 
	 * @author balinder.singh
	 * @param request
	 * @return
	 */
	public static String getSiteUrl(final HttpServletRequest request)
	{
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
		{
			return request.getScheme() + COLON + DOUBLE_SLASH + request.getServerName();
		}
		else
		{
			return request.getScheme() + COLON + DOUBLE_SLASH + request.getServerName() + COLON + request.getServerPort();
		}
	}


}
