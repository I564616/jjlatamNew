/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.singlesignon;

import de.hybris.platform.samlsinglesignon.RedirectionControllerBase;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

import jakarta.annotation.Resource;
import org.apache.log4j.Logger;
import com.jnj.la.sso.service.JnJLABackofficeSSOService;
import de.hybris.platform.samlsinglesignon.SAMLService;

public class JnjLaRedirectionControllerBase extends RedirectionControllerBase {

	private static final Logger LOGGER = Logger.getLogger(JnjLaRedirectionControllerBase.class);
    private static final String BACKOFFICE_URL = "/backoffice";
    private static final String HOME_URL = "/home";
    private static final String ERROR="/error";
    public static final String SSO_REDIRECT_URL = "sso.redirect.url";
    private static final String ACCESS_DENIED_URL = "/login.zul?login_error=1";
    protected static final String REDIRECT_PREFIX = "redirect:";
    private static final String HTTPS_SLASH = "https://";
    private static final String SLASH = "/";
    private static final String STORE_FOR_LOCAL = ":9002/store";
    private static final String STORE_FOR_ENVIRONMENT = "/store";
    private static final String LOCAL = "local";
    private static final String HAC_URL = "/samlsinglesignon/saml/hac/";
    private static final String HAC_SSO_REDIRECT_URL = "hac.sso.redirect.url";
	
    @Resource
    private JnJLABackofficeSSOService jnjLaBackofficeSSOService;
    @Resource
    private SAMLService samlService;

    @Override
    public String getRedirectUrl(HttpServletResponse response, HttpServletRequest request) {
        String configuredRedirectUrl = getLaRedirectUrl(request);
        return !ERROR.equals(configuredRedirectUrl) ? configuredRedirectUrl : (String) Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).filter((auth) -> {
            return auth.getCredentials() instanceof Saml2AuthenticatedPrincipal;
        }).map((auth) -> {
            return (Saml2AuthenticatedPrincipal)auth.getCredentials();
        }).map(Saml2AuthenticatedPrincipal::getRelyingPartyRegistrationId).filter(StringUtils::isNotBlank).orElseGet(() -> {
            return configuredRedirectUrl;
        });
    }

    private String getLaRedirectUrl(HttpServletRequest request) {
        String returnString ;
        LOGGER.info("request.getRequestURI(): " + request.getRequestURI());
        if (request.getRequestURI().endsWith(HOME_URL))
        {
            String referenceURL = StringUtils.substringAfter(request.getServletPath(), "/saml/");
            String country = referenceURL.split(SLASH)[0];
            referenceURL = StringUtils.substringAfter(referenceURL, country + SLASH);
            referenceURL = getRedirectReferenceURL(request, referenceURL);
            try {
                String redirectURL = Config.getString("sso.login.redirect.url.pattern", (String) null);
                if (redirectURL != null) {
                    return String.format(redirectURL, country) + referenceURL;
                }
                return ERROR;
            } catch (IllegalStateException var5) {
                return ERROR;
            }
        }
        else if (request.getRequestURI().contains(BACKOFFICE_URL))
        {
            final String redirectionUrl=Config.getParameter(SSO_REDIRECT_URL);
            LOGGER.debug("Redirection Controller Inside Backoffice === ");
            final Saml2AuthenticatedPrincipal credential = (Saml2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(null != this.jnjLaBackofficeSSOService.getOrCreateSSOUser(this.samlService.getUserId(credential),
                    this.samlService.getUserName(credential), null))
            {
                LOGGER.info("getLaRedirectUrl Backoffice- Inside If" + redirectionUrl);
                return redirectionUrl;
            }
            LOGGER.info("getLaRedirectUrl Backoffice- Inside Else" + redirectionUrl + ACCESS_DENIED_URL);
            return redirectionUrl + ACCESS_DENIED_URL;
        }
        else if(request.getRequestURI().endsWith(HAC_URL)){
            final String hacRedirectionUrl=Config.getParameter(HAC_SSO_REDIRECT_URL);
            LOGGER.debug("Redirection Controller Inside HAC === ");
            final Saml2AuthenticatedPrincipal credential = (Saml2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(null != this.jnjLaBackofficeSSOService.getOrCreateSSOUser(this.samlService.getUserId(credential),
                    this.samlService.getUserName(credential), null))
            {
                LOGGER.info("getLaRedirectUrl HAC- Inside If" + hacRedirectionUrl);
                return hacRedirectionUrl;
            }
            LOGGER.info("getLaRedirectUrl HAC- Inside Else" + hacRedirectionUrl + ACCESS_DENIED_URL);
            return hacRedirectionUrl + ACCESS_DENIED_URL;
        }
        else
        {
            returnString = redirectToLoginPage(request);
        }
        LOGGER.info("returnString:::::::: " + returnString);
        return REDIRECT_PREFIX + returnString;
    }

    private static String getRedirectReferenceURL(HttpServletRequest request, String referenceURL) {
        if (!StringUtils.isEmpty(request.getQueryString())) {
            referenceURL = referenceURL + (request.getQueryString().isEmpty() ? "" : "?" + request.getQueryString());
            LOGGER.info("referenceURL::::::: " + referenceURL);
        }
        return referenceURL;
    }

    private String redirectToLoginPage(final HttpServletRequest request)
    {
        String url = HTTPS_SLASH + request.getServerName();
        LOGGER.info("URL : " + url);
        final String env = Config.getParameter("deployment.env");
        LOGGER.info("Environment : " + env);
        if (LOCAL.equals(env))
        {
            url = url + STORE_FOR_LOCAL;
        }
        else
        {
            url = url + STORE_FOR_ENVIRONMENT;
        }
        return url;
    }
}
