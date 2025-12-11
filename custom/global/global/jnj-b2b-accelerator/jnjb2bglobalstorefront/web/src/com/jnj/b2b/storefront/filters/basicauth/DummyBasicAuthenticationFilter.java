package com.jnj.b2b.storefront.filters.basicauth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class DummyBasicAuthenticationFilter  extends BasicAuthenticationFilter{

    private static final String DUMMY_FILTER = "dummy";

    public DummyBasicAuthenticationFilter() {
        super(DUMMY_FILTER, DUMMY_FILTER);
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException
    {
        chain.doFilter(request, response);
    }
}
