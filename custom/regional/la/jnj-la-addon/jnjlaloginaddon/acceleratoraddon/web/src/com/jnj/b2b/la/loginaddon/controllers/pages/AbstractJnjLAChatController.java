/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.jnjlaordertemplate.controllers.pages.AbstractJnjLaBasePageController;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import jakarta.ws.rs.core.UriBuilder;

abstract class AbstractJnjLAChatController extends AbstractJnjLaBasePageController {

    private static final String CHAT_USERNAME = "chatUsername";
    private static final String REDIRECT_FORMAT = "redirect:%s";
    private static final String JNJ_LATAM_CHAT_URL = "jnj.latam.chat.url";

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

    @Autowired
    private ConfigurationService configurationService;

    abstract String getChatGroupProperty();

    String getCountryIsoCode() {
        String isoCode = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
        if (isoCode != null) {
            return isoCode.toLowerCase();
        }
        return null;
    }

    ModelAndView createModelAndViewToRedirect(String url) {
        return new ModelAndView(String.format(REDIRECT_FORMAT, url));
    }

    String buildUrl(final String path, final String name) {
        final String baseUrl = configurationService.getConfiguration().getString(JNJ_LATAM_CHAT_URL);
        final UriBuilder uriBuilder = UriBuilder.fromUri(baseUrl);
        if (StringUtils.isNotBlank(path)) {
            uriBuilder.path(path);
        }
        uriBuilder.queryParam(CHAT_USERNAME, name);
        return uriBuilder.build().toString();
    }

    String getGroupByCountryOrLanguage(final String country, final String language) {
        String group = JnJCommonUtil.getValue(getChatGroupProperty() + country + Jnjb2bFacadesConstants.Logging.DOT_STRING + language.toLowerCase());
        if (group == null) {
            group = JnJCommonUtil.getValue(getChatGroupProperty() + country);
        }
        return group;
    }

}