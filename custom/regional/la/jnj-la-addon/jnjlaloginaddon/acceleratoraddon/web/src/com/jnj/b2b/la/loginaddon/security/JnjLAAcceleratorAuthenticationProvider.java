/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.security;

import com.jnj.b2b.la.loginaddon.constants.JnjlaloginaddonConstants;
import com.jnj.b2b.la.loginaddon.filters.JnjLACMSSiteFilter;
import com.jnj.b2b.loginaddon.security.JnjAcceleratorAuthenticationProvider;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.impl.DefaultMessageFacade;
import com.jnj.la.core.services.customer.JnjLatamCustomerService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JnjLAAcceleratorAuthenticationProvider extends JnjAcceleratorAuthenticationProvider {

    private static final Logger LOG = Logger.getLogger(JnjLAAcceleratorAuthenticationProvider.class);
    private static final String VALID_CENCA_ISO_CODES = "jnj.valid.cenca.countries.isoCode";

    private CMSSiteService cmsSiteService;

    private DefaultMessageFacade messageFacade;

    private JnjLatamCustomerService customerService;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication) throws AuthenticationException {
        final UserModel user = getUserService().getUserForUID(details.getUsername());
        if (user == null) {
            throw new InsufficientAuthenticationException(getMessage(JnjlaloginaddonConstants.Login.USER_ERROR));
        }

        validateAccessToRegion(user);

        validateAccountOnURL(user);

        super.additionalAuthenticationChecks(details, authentication);
    }

    private void validateAccessToRegion(final UserModel user) {
        if (CollectionUtils.isEmpty(user.getAllGroups()) || !hasAccessToCountry(user)) {
            throw new InsufficientAuthenticationException(getMessage(JnjlaloginaddonConstants.Login.REGION_ERROR));
        }
    }

    private boolean hasAccessToCountry(final UserModel user) {
        final CountryModel country = cmsSiteService.getCurrentSite().getDefaultCountry();

        if (country != null && country.getIsocode() != null) {
            String countryGroup = getCountryGroup(country);

            for (PrincipalGroupModel group : user.getAllGroups()) {
                if (countryGroup.equalsIgnoreCase(group.getUid())) {
                    return true;
                }
            }
        }

        return false;
    }

    private String getMessage(final String code) {
        try {
            return messageFacade.getMessageTextForCode(code);
        } catch (final BusinessException e) {
            LOG.error("No message found for: " + code, e);
            return null;
        }
    }

    private static String getCountryGroup(final CountryModel country) {
        if (isCencaCountry(country)) {
            return JnjlaloginaddonConstants.Login.CENCA_USER_GROUP;
        }

        return country.getIsocode().toLowerCase() + JnjlaloginaddonConstants.Login.USER_GROUP;
    }

    private static boolean isCencaCountry(final CountryModel country) {
        return Config.getParameter(VALID_CENCA_ISO_CODES).contains(country.getIsocode());
    }

    private void validateAccountOnURL(final UserModel userModel) throws InsufficientAuthenticationException {
        final String tmpTargetUrl = getSessionService().getAttribute(JnjLACMSSiteFilter.JNJ_LA_TARGET_URL);
        if (StringUtils.isNotBlank(tmpTargetUrl) && tmpTargetUrl.contains(JnjLACMSSiteFilter.ACCOUNT_PARAM)) {
            final String[] accountString = tmpTargetUrl.contains(JnjLACMSSiteFilter.ACCOUNT_PARAM) ? tmpTargetUrl.split(JnjLACMSSiteFilter.EQUALS) : new String[]{};
            if (ArrayUtils.getLength(accountString) == JnjLACMSSiteFilter.ACCOUNT_PARAM_SIZE
                && StringUtils.isNotBlank(accountString[1])
                && userModel instanceof JnJB2bCustomerModel) {
                final JnJB2BUnitModel account = customerService.getAccountForCustomer(((JnJB2bCustomerModel) userModel), accountString[1]);
                if (account != null) {
                    getSessionService().setAttribute(JnjLACMSSiteFilter.SET_ACCOUNT_FROM_EXTERNAL_URL, account.getUid() + JnjLACMSSiteFilter.ACCOUNT_SEPARATOR + account.getLocName());
                } else {
                    LOG.warn(String.format("User %s has no access to account %s", userModel.getUid(), accountString[1]));
                    throwInsufficientAuth(getMessage(JnjlaloginaddonConstants.Login.ACCOUNT_ERROR));
                }
            } else {
                LOG.warn("Account parameter is incorrect");
                throwInsufficientAuth(getMessage(JnjlaloginaddonConstants.Login.ACCOUNT_ERROR));
            }
        }
    }

    private void throwInsufficientAuth(final String message) throws InsufficientAuthenticationException {
        try {
            getSessionService().removeAttribute(JnjLACMSSiteFilter.JNJ_LA_TARGET_URL);
        } catch (final Exception exp) {
            LOGGER.error("Cannot fetch ::", exp);
        }
        throw new InsufficientAuthenticationException(message);
    }

    public void setCmsSiteService(final CMSSiteService cmsSiteService) {
        this.cmsSiteService = cmsSiteService;
    }

    public void setMessageFacade(final DefaultMessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    public void setCustomerService(JnjLatamCustomerService customerService) {
        this.customerService = customerService;
    }
}