package com.jnj.b2b.storefront.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import de.hybris.platform.jalo.user.UserManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import de.hybris.platform.util.Config;

public final class JnjSSOUtil {

    public static final String SSO_COOKIE_NAME = "sso.cookie.name";

    private JnjSSOUtil() {
    }

    public static void eraseSamlCookie(final HttpServletResponse response) {
        final String cookieName = Config.getParameter(SSO_COOKIE_NAME);
        if (cookieName != null) {
            final Cookie cookie = new Cookie(cookieName, "");
            cookie.setMaxAge(0);
            cookie.setPath(Config.getString("sso.cookie.path", "/"));
            cookie.setDomain(Config.getString("sso.cookie.domain", null));
            response.addCookie(cookie);
        }
    }

    public static Cookie getSamlCookie(final HttpServletRequest request) {
        final String cookieName = Config.getParameter(SSO_COOKIE_NAME);
        return cookieName != null ? WebUtils.getCookie(request, cookieName) : null;
    }

}
