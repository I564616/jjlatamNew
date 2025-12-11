package com.jnj.b2b.storefront.filters.basicauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    private String username;
    private String password;

    public BasicAuthenticationFilter(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authenticationHeader = httpRequest.getHeader("Authorization");

        if (authenticationHeader == null) {
            sendUnauthorizedResponse(httpResponse, "Missing Authorization header");
            return;
        }

        if (!isAuthorized(authenticationHeader)) {
            sendUnauthorizedResponse(httpResponse, "Invalid username or password");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthorized(String authenticationHeader) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(authenticationHeader);
            String authorizationType = tokenizer.nextToken();
            if (!"Basic".equalsIgnoreCase(authorizationType)) {
                return false;
            }

            String encodedCredentials = tokenizer.nextToken();
            String credentials = new String(Base64.getDecoder().decode(encodedCredentials));
            tokenizer = new StringTokenizer(credentials, ":");

            String providedUsername = tokenizer.nextToken();
            String providedPassword = tokenizer.nextToken();

            return username.equals(providedUsername) && password.equals(providedPassword);

        } catch (Exception e) {
            LOG.info("Auth parsing failed: {}", e.getMessage());
            return false;
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"JnJStorefront\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
