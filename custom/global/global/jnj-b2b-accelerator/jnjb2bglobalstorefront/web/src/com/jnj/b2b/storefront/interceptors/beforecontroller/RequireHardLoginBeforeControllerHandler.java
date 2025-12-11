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
package com.jnj.b2b.storefront.interceptors.beforecontroller;

import de.hybris.platform.servicelayer.user.UserService;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.interceptors.BeforeControllerHandler;
import com.jnj.core.model.JnJB2bCustomerModel;

import java.lang.annotation.Annotation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.CookieGenerator;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;

public class RequireHardLoginBeforeControllerHandler implements BeforeControllerHandler {
    private static final Logger LOG = Logger.getLogger(RequireHardLoginBeforeControllerHandler.class);

    public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";
    private static final String REDIRECTED_TO_LOGIN = "redirectedToLogin";

    private String loginUrl;
    private String loginAndCheckoutUrl;
    private RedirectStrategy redirectStrategy;
    private CookieGenerator cookieGenerator;
    private UserService userService;

    @Autowired(required = true)
    private SessionService sessionService;

    @Override
    public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response, final HandlerMethod handler) throws Exception {


        if (request.isSecure()) {
            UserModel userModel = getUserService().getCurrentUser();

            final boolean anonymousUser = getUserService().isAnonymousUser(userModel);
            final RequireHardLogIn annotation = findAnnotation(handler, RequireHardLogIn.class);

            final Boolean redirectLogin = sessionService.getAttribute(REDIRECTED_TO_LOGIN);

            if (annotation != null && null != redirectLogin && !redirectLogin) {
                sessionService.setAttribute("firstSSOLogin", false);
                boolean redirect = true;
                final String guid = (String) request.getSession().getAttribute(SECURE_GUID_SESSION_KEY);

                if (!anonymousUser && guid != null && request.getCookies() != null) {
                    final String guidCookieName = getCookieGenerator().getCookieName();
                    if (guidCookieName != null) {
                        for (final Cookie cookie : request.getCookies()) {
                            if (guidCookieName.equals(cookie.getName())) {
                                if (guid.equals(cookie.getValue())) {
                                    redirect = false;
                                    break;
                                } else {
                                    LOG.info("Found secure cookie with invalid value. expected [" + guid + "] actual [" + cookie.getValue() + "]. removing.");
                                    getCookieGenerator().removeCookie(response);
                                }
                            }
                        }
                    }
                }

                if (redirect) {
                    LOG.warn((guid == null ? "missing secure token in session" : "no matching guid cookie") + ", redirecting");
                    sessionService.setAttribute(REDIRECTED_TO_LOGIN, true);
                    //getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(request));
                    return false;
                }
            }
        }

        sessionService.setAttribute(REDIRECTED_TO_LOGIN, false);
        return true;
    }

    protected String getRedirectUrl(final HttpServletRequest request) {
        if (request != null && request.getServletPath().contains("checkout")) {
            return getLoginAndCheckoutUrl();
        } else {
            return getLoginUrl();
        }
    }

    protected <T extends Annotation> T findAnnotation(final HandlerMethod handlerMethod, final Class<T> annotationType) {
        final T annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }

        return AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), annotationType);
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    protected String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected CookieGenerator getCookieGenerator() {
        return cookieGenerator;
    }

    public void setCookieGenerator(final CookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }

    protected UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public String getLoginAndCheckoutUrl() {
        return loginAndCheckoutUrl;
    }

    public void setLoginAndCheckoutUrl(final String loginAndCheckoutUrl) {
        this.loginAndCheckoutUrl = loginAndCheckoutUrl;
    }
}
