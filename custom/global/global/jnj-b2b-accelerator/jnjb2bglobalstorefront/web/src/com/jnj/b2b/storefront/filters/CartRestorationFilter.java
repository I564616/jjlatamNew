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

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.security.cookie.CartRestoreCookieGenerator;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

/**
 * Filter that the restores the user's cart. This is a spring configured filter that is executed by the
 * PlatformFilterChain.
 */
public class CartRestorationFilter extends OncePerRequestFilter
{
	private CartRestoreCookieGenerator cartRestoreCookieGenerator;
	private CartFacade cartFacade;
	private BaseSiteService baseSiteService;
	private UserService userService;
	private SessionService sessionService;
	@Autowired
	protected CMSSiteService cmsSiteService;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
								 final FilterChain filterChain) throws IOException, ServletException
	{
		final UserModel userModel=getUserService().getCurrentUser();
		if (getUserService().isAnonymousUser(userModel))
		{
			if (getCartFacade().hasSessionCart()
				&& getBaseSiteService().getCurrentBaseSite().equals(
				getBaseSiteService().getBaseSiteForUID(getCartFacade().getSessionCart().getSite())))
			{
				final String guid = getCartFacade().getSessionCart().getGuid();

				if (!StringUtils.isEmpty(guid))
				{
					getCartRestoreCookieGenerator().addCookie(response, guid);
				}
			}
			else if (request.getSession().isNew()
				|| (getCartFacade().hasSessionCart() && !getBaseSiteService().getCurrentBaseSite().equals(
				getBaseSiteService().getBaseSiteForUID(getCartFacade().getSessionCart().getSite()))))
			{
				String cartGuid = null;

				if (request.getCookies() != null)
				{
					final String anonymousCartCookieName = getCartRestoreCookieGenerator().getCookieName();

					for (final Cookie cookie : request.getCookies())
					{
						if (anonymousCartCookieName.equals(cookie.getName()))
						{
							cartGuid = cookie.getValue();
							break;
						}
					}
				}

				if (!StringUtils.isEmpty(cartGuid))
				{
					try
					{
						getSessionService().setAttribute(WebConstants.CART_RESTORATION, getCartFacade().restoreSavedCart(cartGuid));
					}
					catch (final CommerceCartRestorationException e)
					{
						getSessionService().setAttribute(WebConstants.CART_RESTORATION, "basket.restoration.errorMsg");
					}
				}
			}

		}
		else
		{
			//Oops UX fix
			final String currentSiteType = cmsSiteService.getCurrentSite().getJnjWebSiteType().getCode();
			//final String currentSiteType = cmsSiteService.getCurrentSite().getUid();
			final String siteName = currentSiteType.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD
				: LoginaddonConstants.CONS;
			sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);
			//Oops UX fix

			if ((!getCartFacade().hasSessionCart() && getSessionService().getAttribute(WebConstants.CART_RESTORATION) == null)
				|| (getCartFacade().hasSessionCart() && !getBaseSiteService().getCurrentBaseSite().equals(
				getBaseSiteService().getBaseSiteForUID(getCartFacade().getSessionCart().getSite()))))
			{
				try
				{
					getSessionService().setAttribute(WebConstants.CART_RESTORATION, getCartFacade().restoreSavedCart(null));
				}
				catch (final CommerceCartRestorationException e)
				{
					getSessionService().setAttribute(WebConstants.CART_RESTORATION, "basket.restoration.errorMsg");
				}
			}
		}

		HttpSession session = request.getSession(false);
		SecurityContext securityContext = sessionService.getAttribute(session.getId()+userModel.getUid());
		if(null!=securityContext){
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		}

		filterChain.doFilter(request, response);
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CartRestoreCookieGenerator getCartRestoreCookieGenerator()
	{
		return cartRestoreCookieGenerator;
	}

	public void setCartRestoreCookieGenerator(final CartRestoreCookieGenerator cartRestoreCookieGenerator)
	{
		this.cartRestoreCookieGenerator = cartRestoreCookieGenerator;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
