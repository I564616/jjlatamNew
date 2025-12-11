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


import de.hybris.platform.acceleratorfacades.urlencoder.UrlEncoderFacade;
import de.hybris.platform.acceleratorfacades.urlencoder.data.UrlEncoderData;
import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlencoder.attributes.UrlEncodingAttributeManager;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.web.wrappers.UrlEncodeHttpRequestWrapper;


/**
 * This filter inspects the url and inject the url attributes if any for that CMSSite. Calls facades to fetch the list
 * of attributes and encode them in the URL.
 */
public class UrlEncoderFilter extends OncePerRequestFilter
{
	private static final Logger LOG = Logger.getLogger(UrlEncoderFilter.class.getName());

	private UrlEncoderFacade urlEncoderFacade;
	private SessionService sessionService;

	@Autowired(required = true)
	private UrlEncoderService urlEncoderService;



	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private StoreSessionFacade storeSessionFacade;

	//default value en
	private String langFromUrl = "en";

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" The incoming URL : [" + request.getRequestURL().toString() + "]");
		}
		final List<UrlEncoderData> currentUrlEncoderDatas = getCurrentUrlEncodingData();
		if (currentUrlEncoderDatas != null && !currentUrlEncoderDatas.isEmpty())
		{
			final String currentPattern = getSessionService().getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
			String newPattern = calculateAndUpdateUrlEncodingData(request.getRequestURI().toString(), request.getContextPath());
			String newPatternWithSlash = "/" + newPattern;
			if (!StringUtils.equalsIgnoreCase(currentPattern, newPatternWithSlash))
			{
				final String[] strSplit = newPattern.split("/");
				if (StringUtils.isNotBlank(strSplit[0]))
				{
					boolean flag = false;
					final BaseSiteModel baseSiteModel = baseSiteService.getCurrentBaseSite();
					final Set<LanguageModel> lgs = baseSiteModel.getLanguages();
					for (final LanguageModel lg : lgs)
					{
						if (lg.getIsocode().equalsIgnoreCase(strSplit[0]))
						{
							storeSessionFacade.setCurrentLanguage(strSplit[0]);
							flag = true;
						}
					}
					if (!flag)
					{
						storeSessionFacade.setCurrentLanguage(baseSiteModel.getDefaultLanguage().getIsocode());
						newPattern = newPattern.replace(strSplit[0], baseSiteModel.getDefaultLanguage().getIsocode());
						newPatternWithSlash = "/" + newPattern;

					}
					setLangFromUrl(strSplit[0]);
				}

				updateSiteFromUrlEncodingData();
				getSessionService().setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, newPatternWithSlash);
			}

			final UrlEncodeHttpRequestWrapper wrappedRequest = new UrlEncodeHttpRequestWrapper(request, newPattern);
			wrappedRequest.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, newPatternWithSlash);
			wrappedRequest.setAttribute("originalContextPath",
					StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath());

			if (LOG.isDebugEnabled())
			{
				LOG.debug("ContextPath=[" + wrappedRequest.getContextPath() + "]" + " Servlet Path= ["
						+ wrappedRequest.getServletPath() + "]" + " Request Url= [" + wrappedRequest.getRequestURL() + "]");
			}
			filterChain.doFilter(wrappedRequest, response);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(" No URL attributes defined");
			}
			request.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, "");
			filterChain.doFilter(request, response);
		}
	}

	private boolean isValid(final String attributeName, final String value)
	{
		final UrlEncodingAttributeManager attributeManager = getUrlEncoderService().getUrlEncodingAttrManagerMap()
				.get(attributeName);
		if (attributeManager != null)
		{
			return attributeManager.isValid(value);
		}
		return false;
	}

	private void updateSiteFromUrlEncodingData()
	{
		for (final UrlEncoderData urlEncoderData : getCurrentUrlEncodingData())
		{
			final UrlEncodingAttributeManager attributeManager = getUrlEncoderService().getUrlEncodingAttrManagerMap()
					.get(urlEncoderData.getAttributeName());
			if (attributeManager != null)
			{
				if (urlEncoderData.getAttributeName().contains("language"))
				{
					System.out.println("Language from URL -------------" + getLangFromUrl());
					attributeManager.updateAndSyncForAttrChange(getLangFromUrl());
				}
				else
				{
					attributeManager.updateAndSyncForAttrChange(urlEncoderData.getCurrentValue());
				}
			}
		}
	}

	protected UrlEncoderFacade getUrlEncoderFacade()
	{
		return urlEncoderFacade;
	}

	public void setUrlEncoderFacade(final UrlEncoderFacade urlEncoderFacade)
	{
		this.urlEncoderFacade = urlEncoderFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	private String calculateAndUpdateUrlEncodingData(final String uri, final String contextPath)
	{
		final List<UrlEncoderData> urlEncodingAttributes = getCurrentUrlEncodingData();
		final String[] splitUrl = StringUtils.split(uri, "/");
		int splitUrlCounter = (ArrayUtils.isNotEmpty(splitUrl) && (StringUtils.remove(contextPath, "/").equals(splitUrl[0]))) ? 1
				: 0;

		final StringBuilder patternSb = new StringBuilder();
		for (final UrlEncoderData urlEncoderData : urlEncodingAttributes)
		{
			String tempValue = urlEncoderData.getCurrentValue();
			if ((splitUrlCounter) < splitUrl.length)
			{
				tempValue = splitUrl[splitUrlCounter];
				if (!isValid(urlEncoderData.getAttributeName(), tempValue))
				{
					tempValue = urlEncoderData.getDefaultValue();
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Encoding attributes are absent. Injecting default value :  [" + tempValue + "]");
					}
				}
				urlEncoderData.setCurrentValue(tempValue);
				splitUrlCounter++;
			}

			if (patternSb.length() != 0)
			{
				patternSb.append('/');
			}
			patternSb.append(tempValue);

		}
		return patternSb.toString();
	}

	public List<UrlEncoderData> getCurrentUrlEncodingData()
	{
		//if (getSessionService().getAttribute("urlEncodingData") == null)
		//{
		final Collection<String> urlEncodingAttributes = getUrlEncoderService().getEncodingAttributesForSite();
		final List<UrlEncoderData> urlEncoderDataList = new ArrayList<UrlEncoderData>(urlEncodingAttributes.size());

		for (final String attribute : urlEncodingAttributes)
		{
			final UrlEncodingAttributeManager attributeManager = getUrlEncoderService().getUrlEncodingAttrManagerMap()
					.get(attribute);
			if (attributeManager != null)
			{
				final UrlEncoderData urlEncoderData = new UrlEncoderData();
				urlEncoderData.setAttributeName(attribute);
				urlEncoderData.setCurrentValue(attributeManager.getCurrentValue());
				urlEncoderData.setDefaultValue(attributeManager.getDefaultValue());
				urlEncoderDataList.add(urlEncoderData);
			}
		}
		getSessionService().setAttribute("urlEncodingData", urlEncoderDataList);
		//}

		return getSessionService().getAttribute("urlEncodingData");
	}

	public UrlEncoderService getUrlEncoderService()
	{
		return urlEncoderService;
	}

	public void setUrlEncoderService(final UrlEncoderService urlEncoderService)
	{
		this.urlEncoderService = urlEncoderService;
	}

	public String getLangFromUrl()
	{
		return langFromUrl;
	}

	public void setLangFromUrl(final String langFromUrl)
	{
		this.langFromUrl = langFromUrl;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}


}
