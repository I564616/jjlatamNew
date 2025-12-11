/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.la.core.util.JnjLaCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/p/consumer/chat")
public class JnjLAConsumerChatController extends AbstractJnjLAChatController {

    private static final Logger LOGGER = Logger.getLogger(JnjLAConsumerChatController.class);
    private static final String CONSUMER_CHAT_PAGE = "addon:/jnjlaloginaddon/pages/account/consumerChat";
    private static final String JNJ_LATAM_CONSUMER_CHAT_GROUPS = "jnj.latam.consumer.chat.groups.";
    private static final String INVALID_NAME = "Invalid name";
    private static final String CMS_PAGE = "login";
    private static final String CHAT_URL = "/p/consumer/chat";

    @GetMapping
    public String loadPage(final Model model) {
        storeCmsPageInModel(model, CMS_PAGE);
        return CONSUMER_CHAT_PAGE;
    }

    @GetMapping("/redirect")
    public ModelAndView redirectToChat(@RequestParam String name) {
        if (StringUtils.isBlank(name)) {
            LOGGER.warn(INVALID_NAME);
            return createModelAndViewToRedirect(CHAT_URL);
        }
        String group = getGroupByCountryOrLanguage(JnjLaCommonUtil.getCountryOrCenca(getCountryIsoCode()), getLanguageFromUrl());
        return createModelAndViewToRedirect(buildUrl(group, name));
    }

    String getChatGroupProperty() {
        return JNJ_LATAM_CONSUMER_CHAT_GROUPS;
    }

    @SuppressWarnings("WeakerAccess") // used in tests
    public String getLanguageFromUrl() {
        return getStoreSessionFacade().getCurrentLanguage().getIsocode();
    }

}