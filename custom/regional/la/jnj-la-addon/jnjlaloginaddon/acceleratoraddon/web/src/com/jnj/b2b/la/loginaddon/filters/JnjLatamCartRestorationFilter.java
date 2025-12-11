package com.jnj.b2b.la.loginaddon.filters;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;

import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.b2b.storefront.constants.WebConstants;

import de.hybris.platform.site.BaseSiteService;

import com.jnj.b2b.storefront.security.cookie.CartRestoreCookieGenerator;

/**
 * 
 */

/**
 * @author sbaner48
 * 
 */
public class JnjLatamCartRestorationFilter extends OncePerRequestFilter
{
	
	private CartRestoreCookieGenerator cartRestoreCookieGenerator;
	
	private CartFacade cartFacade;
	
	@Autowired
	private JnjLatamCartFacade jnJLatamCartFacade;
	
	private BaseSiteService baseSiteService;
	private UserService userService;
	private SessionService sessionService;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException
	{
		UserModel loggedInUser = getUserService().getCurrentUser();
		if (getUserService().isAnonymousUser(loggedInUser))
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
			if (loggedInUser instanceof CustomerModel && (!getCartFacade().hasSessionCart() && getSessionService().getAttribute(WebConstants.CART_RESTORATION) == null)
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
