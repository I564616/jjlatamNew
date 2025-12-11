/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.la.b2b.occ.filter;
import com.jnj.la.b2b.occ.context.JnjLatamB2BContextInformationLoader;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter sets essential data in current session which requires for service layer.
 *
 */
public class JnjLatamB2BCustomFilter extends OncePerRequestFilter {
    private JnjLatamB2BContextInformationLoader jnjLatamB2BContextInformationLoader;
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filter)
            throws ServletException, IOException
    {
        getJnjLatamB2BContextInformationLoader().setSessionAttributes();
        filter.doFilter(request, response);
    }
    public JnjLatamB2BContextInformationLoader getJnjLatamB2BContextInformationLoader() {
        return jnjLatamB2BContextInformationLoader;
    }

    public void setJnjLatamB2BContextInformationLoader(JnjLatamB2BContextInformationLoader jnjLatamB2BContextInformationLoader) {
        this.jnjLatamB2BContextInformationLoader = jnjLatamB2BContextInformationLoader;
    }
}
