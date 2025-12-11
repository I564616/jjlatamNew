/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.singlesignon;

import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.singlesignon.constants.JnjlasinglesignonConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.SSOUserService;
import de.hybris.platform.samlsinglesignon.constants.SamlsinglesignonConstants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JnjLACustomSSOService extends AbstractService implements SSOUserService {

    private static final Logger LOG = Logger.getLogger(JnjLACustomSSOService.class);
    private static final String CENCA = "cenca";
    private static final String PANAMA_ISO_CODE = "pa";
    private static final String DEFAULT_LANGUAGE = "en";

    private ModelService modelService;

    private UserService userService;

    private CommonI18NService commonI18NService;

    @Override
    public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("User info must not be empty");
        }

        JnJB2bCustomerModel user = lookupExisting(id);
        if (user == null) {
            user = createUser(id, name, roles);
        }

        if (user != null) {
            user.setSsoLogin(Boolean.TRUE);
            try {
                modelService.save(user);
            } catch (final Exception e) {
                LOG.error("Error occurred while saving the user model..", e);
            }
        }

        return user;
    }

    private JnJB2bCustomerModel createUser(final String id, final String name, final Collection<String> roles) {
        final RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        final ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        final HttpServletRequest request = attributes.getRequest();

        final String countryISO = getCountryISOFromURL(request);
        final String defaultCountryIso = getDefaultCountryIso(countryISO);

        final Collection<String> newRoles = defineRoles(roles, countryISO);

        final JnJB2bCustomerModel user = createNewUser(id, name, defaultCountryIso, newRoles);
        if (user != null) {
            user.setAccessBy(AccessBy.WWID);
            user.setSessionLanguage(commonI18NService.getLanguage(DEFAULT_LANGUAGE));
        }
        return user;
    }

    private String getDefaultCountryIso(String countryISO) {
        return StringUtils.equalsIgnoreCase(countryISO, CENCA) ? PANAMA_ISO_CODE : countryISO;
    }

    private String getCountryISOFromURL(HttpServletRequest request) {
        return StringUtils.substringAfter(request.getRequestURL().toString(),"/saml/").split("/")[0];
    }

    private static Collection<String> defineRoles(final Collection<String> roles, final String countryISO) {
        final Collection<String> newRoles = new ArrayList<>();

        final String userGroup = countryISO + "UserGroup";

        if (CollectionUtils.isEmpty(roles)) {
            newRoles.add("JnJLaDummyUnit");
        } else {
            newRoles.addAll(roles);
        }

        newRoles.add("b2bcustomergroup");
        newRoles.add("viewOnlySalesRepGroup");
        newRoles.add(userGroup);

        return newRoles;
    }

    private JnJB2bCustomerModel createNewUser(final String id, final String name, final String iso, final Collection<String> roles) {
        JnJB2bCustomerModel user = null;
        try {
            user = newUser(id, name);

            defineTempPassword(user);
            defineAddress(iso, user);
            defineGroups(roles, user);

            user.setStatus(CustomerStatus.ACTIVE);
        } catch (final Exception e) {
            LOG.error("Error occurred while creating the user", e);
        }
        return user;
    }

    private JnJB2bCustomerModel newUser(final String id, final String name) {
        final JnJB2bCustomerModel user = modelService.create(JnJB2bCustomerModel.class);
        user.setUid(id);
        user.setName(name);
        user.setEmail(id);
        return user;
    }

    private void defineGroups(final Collection<String> roles, final JnJB2bCustomerModel user) {
        final Set<PrincipalGroupModel> groups = new HashSet<>();
        for (final String role : roles) {
            if (StringUtils.isNotBlank(role)) {
                final UserGroupModel group = userService.getUserGroupForUID(role);
                if (group != null) {
                    groups.add(group);
                }
            }
        }
        user.setGroups(groups);
    }

    private void defineAddress(final String iso, final JnJB2bCustomerModel user) {
        final AddressModel contactAddress = modelService.create(AddressModel.class);
        final CountryModel countryModel = commonI18NService.getCountry(iso.toUpperCase());
        contactAddress.setContactAddress(Boolean.TRUE);
        contactAddress.setOwner(user);
        contactAddress.setCountry(countryModel);
        user.setAddresses(Collections.singletonList(contactAddress));
    }

    private void defineTempPassword(final JnJB2bCustomerModel user) {
        final String passwordEncoder = Config.getString(JnjlasinglesignonConstants.SSO_PASSWORD_ENCODING,
                JnjlasinglesignonConstants.MD5_PASSWORD_ENCODING);
        userService.setPassword(user, UUID.randomUUID().toString(), passwordEncoder);
    }

    private JnJB2bCustomerModel lookupExisting(final String id) {
        try {
            return (JnJB2bCustomerModel) userService.getUserForUID(id.toLowerCase(), B2BCustomerModel.class);
        } catch (final UnknownIdentifierException e) {
            LOG.warn("Customer not found for UID: " + id, e);
            return null;
        }
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public void setCommonI18NService(final CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }
}