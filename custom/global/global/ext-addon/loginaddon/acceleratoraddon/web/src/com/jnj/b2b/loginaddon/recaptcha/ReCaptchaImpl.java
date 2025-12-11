/*
 * Copyright 2007 Soren Davidsen, Tanesha Networks
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jnj.b2b.loginaddon.recaptcha;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;


public class ReCaptchaImpl implements ReCaptcha
{

	public static final String PROPERTY_THEME = "theme";
	public static final String PROPERTY_TABINDEX = "tabindex";

	public static final String HTTP_SERVER = "http://api.recaptcha.net";
	public static final String HTTPS_SERVER = "https://api-secure.recaptcha.net";
	public static final String VERIFY_URL = "http://api-verify.recaptcha.net/verify";

	protected String privateKey;
	protected String publicKey;
	protected String recaptchaServer = HTTP_SERVER;
	protected boolean includeNoscript = false;
	protected HttpLoader httpLoader = new SimpleHttpLoader();

	public void setPrivateKey(final String privateKey)
	{
		this.privateKey = privateKey;
	}

	public void setPublicKey(final String publicKey)
	{
		this.publicKey = publicKey;
	}

	public void setRecaptchaServer(final String recaptchaServer)
	{
		this.recaptchaServer = recaptchaServer;
	}

	public void setIncludeNoscript(final boolean includeNoscript)
	{
		this.includeNoscript = includeNoscript;
	}

	public void setHttpLoader(final HttpLoader httpLoader)
	{
		this.httpLoader = httpLoader;
	}

	public ReCaptchaResponse checkAnswer(final String remoteAddr, final String challenge, final String response)
	{

		final String postParameters = "privatekey=" + URLEncoder.encode(privateKey, StandardCharsets.UTF_8) + "&remoteip=" + URLEncoder.encode(remoteAddr, StandardCharsets.UTF_8)
				+ "&challenge=" + URLEncoder.encode(challenge, StandardCharsets.UTF_8) + "&response=" + URLEncoder.encode(response, StandardCharsets.UTF_8);

		final String message = httpLoader.httpPost(VERIFY_URL, postParameters);

		if (message == null)
		{
			return new ReCaptchaResponse(false, "Null read from server.");
		}

		final String[] a = message.split("\r?\n");
		if (a.length < 1)
		{
			return new ReCaptchaResponse(false, "No answer returned from recaptcha: " + message);
		}
		final boolean valid = "true".equals(a[0]);
		String errorMessage = null;
		if (!valid)
		{
			if (a.length > 1)
			{
				errorMessage = a[1];
			}
			else
			{
				errorMessage = "recaptcha4j-missing-error-message";
			}
		}

		return new ReCaptchaResponse(valid, errorMessage);
	}

	public String createRecaptchaHtml(final String errorMessage, final Properties options)
	{

		final String errorPart = (errorMessage == null ? "" : "&amp;error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));

		String message = fetchJSOptions(options);

		message += "<script type=\"text/javascript\" src=\"" + recaptchaServer + "/challenge?k=" + publicKey + errorPart
				+ "\"></script>\r\n";

		if (includeNoscript)
		{
			final String noscript = "<noscript>\r\n" + "	<iframe src=\"" + recaptchaServer + "/noscript?k=" + publicKey + errorPart
					+ "\" height=\"300\" width=\"500\" frameborder=\"0\"></iframe><br>\r\n"
					+ "	<textarea name=\"recaptcha_challenge_field\" rows=\"3\" cols=\"40\"></textarea>\r\n"
					+ "	<input type=\"hidden\" name=\"recaptcha_response_field\" value=\"manual_challenge\">\r\n" + "</noscript>";
			message += noscript;
		}

		return message;
	}

	public String createRecaptchaHtml(final String errorMessage, final String theme, final Integer tabindex)
	{

		final Properties options = new Properties();

		if (theme != null)
		{
			options.setProperty(PROPERTY_THEME, theme);
		}
		if (tabindex != null)
		{
			options.setProperty(PROPERTY_TABINDEX, String.valueOf(tabindex));
		}

		return createRecaptchaHtml(errorMessage, options);
	}

	/**
	 * Produces javascript array with the RecaptchaOptions encoded.
	 * 
	 * @param properties
	 * @return
	 */
	protected String fetchJSOptions(final Properties properties)
	{

		if (properties == null || properties.size() == 0)
		{
			return "";
		}

		String jsOptions = "<script type=\"text/javascript\">\r\n" + "var RecaptchaOptions = {";

		for (final Enumeration e = properties.keys(); e.hasMoreElements();)
		{
			final String property = (String) e.nextElement();

			jsOptions += property + ":'" + properties.getProperty(property) + "'";

			if (e.hasMoreElements())
			{
				jsOptions += ",";
			}

		}

		jsOptions += "};\r\n</script>\r\n";

		return jsOptions;
	}
}
