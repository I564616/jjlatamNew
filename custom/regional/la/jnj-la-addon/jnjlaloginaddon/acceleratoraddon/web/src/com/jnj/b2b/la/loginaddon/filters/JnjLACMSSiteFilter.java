/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.filters;

import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonConstants;
import com.jnj.b2b.storefront.filters.cms.CMSSiteFilter;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.util.config.ConfigIntf;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JnjLACMSSiteFilter extends CMSSiteFilter {

    private static final Logger LOG = Logger.getLogger(JnjLACMSSiteFilter.class);

    private static final String SLASH_ISO_CODE_EN = "/en";
    private static final String BLANK = "";
    private static final String STORE = "store";

    public static final String JNJ_LA_TARGET_URL = "JNJ_LA_TARGET_URL";
    public static final String ACCOUNT_PARAM = "account=";
    public static final String EQUALS = "=";
    public static final String SET_ACCOUNT_FROM_EXTERNAL_URL = "SET_ACCOUNT_FROM_EXTERNAL_URL";
    public static final String ACCOUNT_SEPARATOR = "##";
    private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    private static final String HEADER_PROPERTIES_CONTENT_SECURITY_POLICY =
            "storefront.staticResourceFilter.response.header.Content-Security-Policy";

    public static final int ACCOUNT_PARAM_SIZE = 2;

    private Set<String> includeUrls;

    private B2BCommerceUnitService b2bCommerceUnitService;
    private Map<String, String> headerParams = null;

    @Override
    protected void doFilterInternal(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse, final FilterChain filterChain) throws ServletException, IOException {
        checkForAccountParameter(httpRequest, httpResponse);
        readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
        super.doFilterInternal(httpRequest, httpResponse, filterChain);
    }

    private void checkForAccountParameter(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws IOException {
        if (getUserService().isAnonymousUser(getUserService().getCurrentUser())) {
            LOG.debug("User is anonymous");
            checkAnonymousUser(httpRequest);
        } else {
            LOG.debug("User is not anonymous");
            final UserModel currentUserModel = getUserService().getCurrentUser();
            if (currentUserModel instanceof JnJB2bCustomerModel) {
                final String queryString = httpRequest.getQueryString();
                if (hasAccountParam(httpRequest)) {
                    setAccountFromExternal(httpRequest, httpResponse, queryString);
                }
            }
        }
    }

    private static boolean hasAccountParam(HttpServletRequest httpRequest) {
        return StringUtils.isNotBlank(httpRequest.getQueryString()) && httpRequest.getQueryString().contains(ACCOUNT_PARAM);
    }

    private void setAccountFromExternal(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse, final String queryString) throws IOException {
        LOG.debug("Defining account on SET_ACCOUNT_FROM_EXTERNAL_URL");
        final String[] accountSplit = queryString.split(EQUALS);
        if (ArrayUtils.getLength(accountSplit) == ACCOUNT_PARAM_SIZE && StringUtils.isNotBlank(accountSplit[1])) {
            final JnJB2BUnitModel b2bUnit = (JnJB2BUnitModel) b2bCommerceUnitService.getUnitForUid(accountSplit[1]);
            if (b2bUnit != null) {
                getSessionService().setAttribute(SET_ACCOUNT_FROM_EXTERNAL_URL, b2bUnit.getUid() + ACCOUNT_SEPARATOR + b2bUnit.getLocName());
            } else {
                LOG.error("No b2bUnit found for: " + accountSplit[1]);
                redirectToLogout(httpRequest, httpResponse);
            }
        }  else {
            LOG.error("No b2bUnit found in the URL");
            redirectToLogout(httpRequest, httpResponse);
        }
    }

    private void redirectToLogout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        final Cookie cookie = new Cookie(JnjlaloginaddonConstants.Login.LOGOUT_REASON, JnjlaloginaddonConstants.Login.ACCOUNT_ERROR);
        cookie.setPath("/");
        cookie.setMaxAge(JnjlaloginaddonConstants.Login.LOGOUT_REASON_COOKIE_MAXAGE);
        httpResponse.addCookie(cookie);
        final String[] contextPathSplit = httpRequest.getContextPath().split("/");
        httpResponse.sendRedirect(contextPathSplit[0] + "/" + contextPathSplit[1] + "/logout");
    }

    private void checkAnonymousUser(final HttpServletRequest httpRequest) {
        final String uri = httpRequest.getRequestURI();
        if (StringUtils.isNotEmpty(uri) && hasAccountParam(httpRequest)) {
            includeUrls.stream().filter(uri::contains).findFirst().ifPresent(i -> prepareAccountRedirect(httpRequest));
        }
    }

    private void prepareAccountRedirect(final HttpServletRequest httpRequest) {
        LOG.debug("Preparing account redirect for anonymous user");
        final String uri = httpRequest.getRequestURI();
        final String contextPath = httpRequest.getContextPath();
        final String queryString = httpRequest.getQueryString();
        final String replaceLangCode = getLangCodeFromURL(uri);

        String redirectUrl = uri.replace(contextPath, BLANK);
        if (StringUtils.isNotBlank(replaceLangCode)) {
            redirectUrl = redirectUrl.replace(replaceLangCode, BLANK);
        } else {
            redirectUrl = redirectUrl.replace(SLASH_ISO_CODE_EN, BLANK);
        }

        if (StringUtils.isNotBlank(queryString)) {
            getSessionService().setAttribute(JNJ_LA_TARGET_URL, redirectUrl + "?" + httpRequest.getQueryString());
        } else {
            getSessionService().setAttribute(JNJ_LA_TARGET_URL, redirectUrl);
        }
    }

    private static String getLangCodeFromURL(final String uri) {
        if (StringUtils.isNotBlank(uri) && uri.contains(STORE)) {
            String[] langSplit = uri.split(STORE);
            if (StringUtils.isNotBlank(langSplit[1])) {
                String splitBy = "/";
                String[] data = langSplit[1].split(splitBy);
                return splitBy.concat(data[1]);
            }
        }
        return StringUtils.EMPTY;
    }

    private void readConfiguredHeaderParamsAndWriteToResponse(final HttpServletResponse httpResponse)
    {
        if (headerParams == null)
        {
            // Lazily build the headers from the configuration options
            headerParams = new ConcurrentHashMap<>();
            final ConfigIntf config = Registry.getMasterTenant().getConfig();
            String contentSecurityPolicy = config.getParameter(HEADER_PROPERTIES_CONTENT_SECURITY_POLICY);
            headerParams.put(CONTENT_SECURITY_POLICY, contentSecurityPolicy);
        }

        // Add any configured headers to the response
        for (final Map.Entry<String, String> param : headerParams.entrySet())
        {
            httpResponse.setHeader(param.getKey(), param.getValue());
        }
    }

    public void setIncludeUrls(Set<String> includeUrls) {
        this.includeUrls = includeUrls;
    }

    public void setB2bCommerceUnitService(final B2BCommerceUnitService b2bCommerceUnitService) {
        this.b2bCommerceUnitService = b2bCommerceUnitService;
    }

}
