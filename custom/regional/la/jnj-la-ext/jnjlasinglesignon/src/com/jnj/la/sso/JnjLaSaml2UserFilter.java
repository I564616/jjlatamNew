/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.sso;

import com.jnj.la.sso.service.JnJLABackofficeSSOService;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.SAMLService;
import de.hybris.platform.samlsinglesignon.SSOUserService;
import de.hybris.platform.samlsinglesignon.SamlLoginService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class JnjLaSaml2UserFilter extends OncePerRequestFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JnjLaSaml2UserFilter.class);
    private static final String SSO_USERGROUP_KEY = "sso.usergroup.attribute.key";
    private static final String BACKOFFICE_URL = "/samlsinglesignon/saml/backoffice/";
    private static final String USER_GROUP = "usergroup";
    private static final String HAC_URL = "/samlsinglesignon/saml/hac/";
   
    private SSOUserService userService;
    private CommonI18NService commonI18NService;
    private SAMLService samlService;
    private SamlLoginService samlLoginService;
    private JnJLABackofficeSSOService jnjLaBackofficeSSOService;

    public JnjLaSaml2UserFilter(final SSOUserService userService, final CommonI18NService commonI18NService,
        final SAMLService samlService, final SamlLoginService samlLoginService, final JnJLABackofficeSSOService jnjLaBackofficeSSOService)
    {
        this.userService = userService;
        this.commonI18NService = commonI18NService;
        this.samlService = samlService;
        this.samlLoginService = samlLoginService;
        this.jnjLaBackofficeSSOService = jnjLaBackofficeSSOService;
    }

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException
    {
    	
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = null;

        final Saml2AuthenticatedPrincipal credential = (Saml2AuthenticatedPrincipal) auth.getPrincipal();
        final List<String> roles = this.samlService.getCustomAttributes(credential, Config.getString(SSO_USERGROUP_KEY, USER_GROUP));
        final String requestURI = request.getRequestURI();        
	
        try
        {
            if (requestURI.contains(BACKOFFICE_URL) || requestURI.contains(HAC_URL)) {
                user = this.jnjLaBackofficeSSOService.getOrCreateSSOUser(this.samlService.getUserId(credential),
                        this.samlService.getUserName(credential), roles);
			
            }
            else {
                user = this.userService.getOrCreateSSOUser(this.samlService.getUserId(credential),
                        this.samlService.getUserName(credential), roles);
			
            }
			
			this.samlLoginService.storeLoginToken(response, user,
					this.samlService.getLanguage(credential, request,
							this.commonI18NService));			

        }
        catch (final Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        chain.doFilter(request, response);
    }
}