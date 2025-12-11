package com.jnj.b2b.storefront.interceptors;

import java.util.Collections;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.jnj.core.annotations.AuthorizedUserGroup;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * A Spring MVC interceptor that intercepts the applicable requests and verifies if the user has
 * access to the API requested by validating the required user group against the ones the logged in
 * user is part of.
 *
 */
public class AuthorizedUserHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOG = Logger.getLogger(AuthorizedUserHandlerInterceptor.class);
    private static final String STORE_PATH = "/store";

    private UserService userService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside the AuthorizedUserHandlerInterceptor preHandle method");
        }
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final AuthorizedUserGroup authorizedUserGroupAnnotation = handlerMethod.getMethod().getAnnotation(AuthorizedUserGroup.class);
            if (authorizedUserGroupAnnotation != null
                    && StringUtils.isNotBlank(authorizedUserGroupAnnotation.value())) {
                final String authorizedUserGroup = authorizedUserGroupAnnotation.value();
                if (!isAuthorizedUser(authorizedUserGroup)) {
                    LOG.error("Current user does not have access to the requested resource.. redirecting to base url");
                    response.sendRedirect(STORE_PATH);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Iterates through the current user's user groups and verifies if they are part of the provided
     * group.
     * 
     * @param authorizedUserGroup To be verified.
     * @return true if the user group is present, false otherwise.
     */
    private boolean isAuthorizedUser(final String authorizedUserGroup) {
        final UserModel currentUser = userService.getCurrentUser();
        return Optional.ofNullable(currentUser.getAllGroups()).orElseGet(Collections::emptySet)
                .stream().anyMatch(group -> authorizedUserGroup.equals(group.getUid()));
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
