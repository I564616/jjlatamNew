/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.security;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.loginaddon.security.JnJStorefrontAuthenticationSuccessHandler;
import com.jnj.b2b.storefront.security.RequestUtility;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class JnJLatamStorefrontAuthenticationSuccessHandler extends JnJStorefrontAuthenticationSuccessHandler {

    private static final Class CURRENT_CLASS = JnJLatamStorefrontAuthenticationSuccessHandler.class;
    private static final String CHECK_PASS_EXPIRY = "checkPasswordExpiry";
    private static final String ON_AUTHENTICATION_SUCCESS = "onAuthenticationSuccess";
    private static final int DEFAULT_EXPIRES_DAYS = 90;

    private BaseSiteService baseSiteService;

    private RequestUtility requestUtil;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {

        JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.LOGIN, ON_AUTHENTICATION_SUCCESS, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
        jnjGTCustomerFacade.loginSuccess();

        final UserModel currentUserModel = userService.getCurrentUser();
        final String userId = currentUserModel.getUid();
        if (currentUserModel instanceof JnJB2bCustomerModel) {

            sessionService.setAttribute(LoginaddonConstants.Login.FIRST_LOGIN_CHECK, Boolean.TRUE);

            final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) currentUserModel;

            final boolean passwordExpiry = checkPasswordExpiry(currentUser);

            if (passwordExpiry) {
                JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.LOGIN, ON_AUTHENTICATION_SUCCESS, "REDIRECTING TO LOGIN :: User's password is expired!", CURRENT_CLASS);
                sessionService.setAttribute(LoginaddonConstants.Login.EXPIRED_PASSWORD_PARAM, userId);
                getRedirectStrategy().sendRedirect(request, response, LoginaddonConstants.Login.EXPIRED_PASSWORD_LOGIN_URL + userId);
            }

            final Set<PrincipalGroupModel> allGroups = currentUser.getAllGroups();
            setOrderingRightsInSession(allGroups);
            setAdminRightsInSession(allGroups);
            currentUser.setLastLogin(Calendar.getInstance().getTime());
            modelService.save(currentUser);

            if (!passwordExpiry) {
                getBruteForceAttackCounter().resetUserCounter(userId);
                jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().remove(userId);
                sessionService.setAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN, Boolean.TRUE);
                JnjGTCoreUtil.logDebugMessage(LoginaddonConstants.Logging.LOGIN, ON_AUTHENTICATION_SUCCESS, Logging.END_OF_METHOD, CURRENT_CLASS);

                final Object ssoLogin = sessionService.getAttribute("ssoLogin");
                if (ssoLogin == null || !((boolean) sessionService.getAttribute("ssoLogin"))) {
                    savedOnAuthenticationSuccess(request, response, authentication);
                }

                final JnJB2BUnitModel currentB2bUnit = jnjGTCustomerFacade.getCurrentB2bUnit();
                if (currentB2bUnit != null && CollectionUtils.isNotEmpty(currentB2bUnit.getJnjContracts())) {
                    sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.TRUE);
                } else {
                    sessionService.setAttribute(LoginaddonConstants.SHOW_CONTRACT_LINK, LoginaddonConstants.FALSE);
                }
            }
        }
    }

    public void savedOnAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String targetUrlParameter = getTargetUrlParameter();
        if ((isAlwaysUseDefaultTargetUrl()) || ((targetUrlParameter != null) && (StringUtils.hasText(request.getParameter(targetUrlParameter))))) {
            this.requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        clearAuthenticationAttributes(request);
        String savedUrl = requestUtil.getSavedRedirectUrl(request, response);

        String targetUrl;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(savedUrl)) {
            targetUrl = calculateRelativeRedirectUrl(request.getContextPath(), savedUrl);
        } else {
            targetUrl = calculateRelativeRedirectUrl(request.getContextPath(), "/login");
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(targetUrl)) {
            String temp = targetUrl.substring(1);
            BaseSiteModel baseSiteModel = baseSiteService.getBaseSiteForUID(temp);
            if (null != baseSiteModel) {
                targetUrl = "/home";
            }
        }

        this.logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    public boolean checkPasswordExpiry(final JnJB2bCustomerModel jnJB2bCustomerModel) {
        logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, CHECK_PASS_EXPIRY, Logging.BEGIN_OF_METHOD);

        if (jnJB2bCustomerModel.getForcefulExpired()) {
            return true;
        }

        boolean expiry = false;
        final Date passwordChangeDate = jnJB2bCustomerModel.getPasswordChangeDate();
        if (null != passwordChangeDate) {
            expiry = checkExpiryDate(passwordChangeDate);
        }

        logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, CHECK_PASS_EXPIRY, Logging.END_OF_METHOD);
        return expiry;
    }

    private boolean checkExpiryDate(final Date passwordChangeDate) {
        logDebugMessage(Logging.LOGIN, CHECK_PASS_EXPIRY, "User's last password change date :: " + passwordChangeDate);

        final LocalDate dateOfExpiry = getExpiryDate();
        final LocalDate passwordChangeDateCal = passwordChangeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (passwordChangeDateCal.isBefore(dateOfExpiry)) {
            LOG.info(Logging.LOGIN + Logging.HYPHEN + CHECK_PASS_EXPIRY + Logging.HYPHEN + "User's password is expired!");
            return true;
        } else {
            LOG.info(Logging.LOGIN + Logging.HYPHEN + CHECK_PASS_EXPIRY + Logging.HYPHEN + "User's password has not yet expired. User fit for login");
            return false;
        }
    }

    private LocalDate getExpiryDate() {
        return LocalDate.now().minus(Config.getInt(LoginaddonConstants.Login.DAYS_BEFORE_PASSWORD_EXPIRES_KEY, DEFAULT_EXPIRES_DAYS), ChronoUnit.DAYS);
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public void setRequestUtil(RequestUtility requestUtil) {
        this.requestUtil = requestUtil;
    }

}
