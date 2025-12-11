/*
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.facades.policies.JnjGTPoliciesFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class JnjLACookiePolicyController extends AbstractPageController {

    @Resource(name = "jnjPoliciesFacade")
    private JnjGTPoliciesFacade jnjGTPoliciesFacade;

    @GetMapping("/cookiePolicy")
    public String getLACookiePolicy(final HttpServletResponse response) throws CMSItemNotFoundException, IOException {

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath(null).build().toUriString();
        final String url = baseUrl + jnjGTPoliciesFacade.getCookiePolicies();

        response.setHeader("Location", url);
        response.setStatus(302);
        return null;
    }
}
