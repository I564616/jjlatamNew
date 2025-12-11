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
package com.jnj.b2b.storefront.controllers.misc;

import de.hybris.platform.servicelayer.i18n.I18NService;
import com.jnj.b2b.storefront.controllers.AbstractController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ThemeResolver;


/**
 * Controller for evil clients that go for the favicon.ico directly in the root, redirect them to the real location
 */
@Controller
@Scope("tenant")
public class FavIconController extends AbstractController
{
	protected static final String FAVICON_THEME_CODE = "img.favIcon";
	protected static final String ORIGINAL_CONTEXT = "originalContextPath";

	@Resource(name = "themeResolver")
	private ThemeResolver themeResolver;

	public ThemeResolver getThemeResolver()
	{
		return themeResolver;
	}


	public ThemeSource getThemeSource()
	{
		return themeSource;
	}


	public I18NService getI18nService()
	{
		return i18nService;
	}


	@Resource(name = "themeSource")
	private ThemeSource themeSource;

	@Resource(name = "i18nService")
	private I18NService i18nService;


	@GetMapping("/favicon.ico")
	public String getFavIcon(final HttpServletRequest request)
	{
		final String themeName = themeResolver.resolveThemeName(request);
		String iconPath = themeSource.getTheme(themeName).getMessageSource()
				.getMessage(FAVICON_THEME_CODE, new Object[]{}, i18nService.getCurrentLocale());
		final String originalContextPath = (String) request.getAttribute(ORIGINAL_CONTEXT);

		if (originalContextPath != null)
		{
			final String requestUrl = String.valueOf(request.getRequestURL());
			iconPath = requestUrl.substring(0, requestUrl.indexOf(originalContextPath)
					+ originalContextPath.length()) + "/" + iconPath;
		}

		return REDIRECT_PREFIX + iconPath;
	}
}
