package com.jnj.b2b.storefront.security.impl;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.storefront.security.GUIDCookieStrategy;
import com.jnj.b2b.storefront.util.JnjSSOUtil;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JnjGTAutoLoginStrategy {
    private static final Logger LOG = Logger.getLogger(JnjGTAutoLoginStrategy.class);
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private List<AuthenticationSuccessHandler> authenticationSuccessHandlers = Collections.emptyList();

    @Autowired(required = true)
    private UserService userService;

    @Autowired
    protected CMSSiteService cmsSiteService;

    @Autowired(required = true)
    private ModelService modelService;

    @Autowired(required = true)
    GUIDCookieStrategy guidCookieStrategy;

    @Autowired(required = true)
    WebHttpSessionRequestCache httpSessionRequestCache;

    @Autowired(required = true)
    private UserDetailsChecker preAuthenticationChecks;

    @Autowired(required = true)
    private CustomerFacade customerFacade;

    @Autowired(required = true)
    private SessionService sessionService;

    @Autowired(required = true)
    private RedirectStrategy redirectStrategy;

    private String defaultFailureUrl;

    private List<AuthenticationFailureHandler> authenticationFailureHandlers = Collections.emptyList();

    public boolean login(final LoginToken loginToken, final HttpServletRequest request, final HttpServletResponse response) {
        final String username = loginToken.getUser().getUid();
        final UserDetails userDetails = getUserDetailsService().loadUserByUsername(username);
        boolean isAuthenticationSuccess = true;
        final UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, loginToken, userDetails.getAuthorities());

        upToken.setDetails(new WebAuthenticationDetails(request));
        try {
            Authentication authentication = getAuthenticationManager().authenticate(upToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            // Create a new session and add the security context.
            HttpSession session = request.getSession(false);
            sessionService.setAttribute(session.getId() + userService.getCurrentUser().getUid(), securityContext);
            sessionService.setAttribute("ssoLogin", true);
            for (final AuthenticationSuccessHandler successHandler : getAuthenticationSuccessHandlers()) {
                try {
                    successHandler.onAuthenticationSuccess(request, response, authentication);
                } catch (final IOException | ServletException ioe) {
                    LOG.error("Auth Success Exception during authentication is...", ioe);
                }
            }


            final String currentSiteType = cmsSiteService.getCurrentSite().getJnjWebSiteType().getCode();
            final String siteName = currentSiteType.equalsIgnoreCase(LoginaddonConstants.MDD_SITE_ID) ? LoginaddonConstants.MDD : LoginaddonConstants.CONS;
            sessionService.setAttribute(LoginaddonConstants.SITE_NAME, siteName);

            getGuidCookieStrategy().setCookie(request, response);
            getRedirectStrategy().sendRedirect(request, response, "/home");

        } catch (final AuthenticationException ae) {
            SecurityContextHolder.getContext().setAuthentication(null);
            sessionService.setAttribute(request.getSession(false).getId() + userService.getCurrentUser().getUid(), null);
            sessionService.setAttribute("ssoLogin", false);
            LOG.info("Exception during authentication is..." + ae.getMessage());
            JnjSSOUtil.eraseSamlCookie(response);
            for (final AuthenticationFailureHandler failureHandler : getAuthenticationFailureHandlers()) {
                try {
                    failureHandler.onAuthenticationFailure(request, response, ae);
                } catch (final IOException | ServletException ioe) {
                    LOG.error("Auth Failure Exception during authentication is...", ioe);
                }
            }
            isAuthenticationSuccess = false;
        } catch (final Exception e) {
            LOG.error("Exception during authentication is...", e);
        }
        return isAuthenticationSuccess;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public List<AuthenticationSuccessHandler> getAuthenticationSuccessHandlers() {
        return authenticationSuccessHandlers;
    }

    public void setAuthenticationSuccessHandlers(List<AuthenticationSuccessHandler> authenticationSuccessHandlers) {
        this.authenticationSuccessHandlers = authenticationSuccessHandlers;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static Logger getLog() {
        return LOG;
    }

    public GUIDCookieStrategy getGuidCookieStrategy() {
        return guidCookieStrategy;
    }

    public void setGuidCookieStrategy(GUIDCookieStrategy guidCookieStrategy) {
        this.guidCookieStrategy = guidCookieStrategy;
    }

    public WebHttpSessionRequestCache getHttpSessionRequestCache() {
        return httpSessionRequestCache;
    }

    public void setHttpSessionRequestCache(WebHttpSessionRequestCache httpSessionRequestCache) {
        this.httpSessionRequestCache = httpSessionRequestCache;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    public CustomerFacade getCustomerFacade() {
        return customerFacade;
    }

    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public List<AuthenticationFailureHandler> getAuthenticationFailureHandlers() {
        return authenticationFailureHandlers;
    }

    public void setAuthenticationFailureHandlers(List<AuthenticationFailureHandler> authenticationFailureHandlers) {
        this.authenticationFailureHandlers = authenticationFailureHandlers;
    }

    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

}
