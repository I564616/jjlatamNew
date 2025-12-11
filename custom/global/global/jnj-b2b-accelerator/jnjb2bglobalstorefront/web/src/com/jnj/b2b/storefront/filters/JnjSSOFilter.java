package com.jnj.b2b.storefront.filters;

import com.jnj.b2b.storefront.security.impl.JnjGTAutoLoginStrategy;
import com.jnj.b2b.storefront.util.JnjSSOUtil;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JnjSSOFilter extends GenericFilterBean {

    private static final Logger LOG = Logger.getLogger(JnjSSOFilter.class);

    private JnjGTAutoLoginStrategy jnjAutoLoginStrategy;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ModelService modelService;

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpservletrequest = (HttpServletRequest) request;
        final HttpServletResponse httpservletresponse = (HttpServletResponse) response;
        boolean isAuthenticationSuccess = true;
        final Cookie cookie = JnjSSOUtil.getSamlCookie(httpservletrequest);
        if (cookie != null) {
            isAuthenticationSuccess = isAuthenticationSuccess(httpservletrequest, httpservletresponse, cookie);
        }

        if (sessionService.getAttribute("ssoLogin") == null) {
            sessionService.setAttribute("ssoLogin", false);
        }

        if (isAuthenticationSuccess) {
            chain.doFilter(request, response);
        }
    }

    private boolean isAuthenticationSuccess(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse, final Cookie cookie) {
        boolean isAuthenticationSuccess = true;

        try {
            final LoginToken token = new CookieBasedLoginToken(cookie);

            final UserModel currentUser = userService.getCurrentUser();

            if (!sameAsToken(token, currentUser)) {
                isAuthenticationSuccess = loginTokenUser(httpservletrequest, httpservletresponse, token);
            } else if (isLoginURL(httpservletrequest)) {
                redirectToHome(httpservletrequest, httpservletresponse);
            }

        } catch (final Exception e) {
            LOG.error("Error while retrieving cookie", e);
            JnjSSOUtil.eraseSamlCookie(httpservletresponse);
        }

        return isAuthenticationSuccess;
    }

    private static boolean sameAsToken(final LoginToken token, final UserModel currentUser) {
        return currentUser != null && token.getUser() != null && Objects.equals(currentUser.getUid(), token.getUser().getUid());
    }

    private static void redirectToHome(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse) throws IOException {
        httpservletresponse.sendRedirect(httpservletrequest.getContextPath() + httpservletrequest.getServletPath().replace("login", "home"));
    }

    private static boolean isLoginURL(final HttpServletRequest request) {
        return (request.getServletPath().endsWith("/login"));
    }

    private boolean loginTokenUser(final HttpServletRequest request, final HttpServletResponse response, final LoginToken token) {
        boolean isAuthenticationSuccess = true;

        try {
            final UserModel user = getUserService().getUserForUID(token.getUser().getUid());
            if (user.getEncodedPassword().equals(token.getPassword())) {
                isAuthenticationSuccess = jnjAutoLoginStrategy.login(token, request, response);
                LOG.debug(String.format("User [%s] has been loged in using SSO", user.getUid()));
            }
        } catch (final UnknownIdentifierException | ClassMismatchException e) {
            LOG.warn(e.getMessage(), e);
            throw new BadCredentialsException("Unknown user id." + token.getUser().getUid());
        }

        return isAuthenticationSuccess;
    }

    public JnjGTAutoLoginStrategy getJnjAutoLoginStrategy() {
        return jnjAutoLoginStrategy;
    }

    public void setJnjAutoLoginStrategy(final JnjGTAutoLoginStrategy jnjAutoLoginStrategy) {
        this.jnjAutoLoginStrategy = jnjAutoLoginStrategy;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

}
