/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("tenant")
@RequireHardLogIn
@RequestMapping("/chat")
public class JnjLatamChatController extends AbstractJnjLAChatController {

    private static final String JNJ_LATAM_CHAT_GROUPS = "jnj.latam.chat.groups.";
    private static final String JNJ_LATAM_CHAT_SECTORS = "jnj.latam.chat.sectors.";
    private static final String SECTOR_TOKEN = "$SECTOR";
    private static final String UNDERLINE = "_";

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView redirectToExternalChat(@RequestParam(required = false, defaultValue = "") String sector) {
        return createModelAndViewToRedirect(createChatUrl(sector));
    }

    private String createChatUrl(String sector) {
        final UserModel user = userService.getCurrentUser();
        final String group = getGroup(user, getCountryIsoCode(), sector);
        return buildUrl(group, user.getName());
    }

    private String getGroup(UserModel user, String isoCode, String sector) {
        String result = getGroupByCountryOrLanguage(JnjLaCommonUtil.getCountryOrCenca(isoCode), user.getSessionLanguage().getIsocode());
        if (result != null) {
            return result.replace(SECTOR_TOKEN, getSectorToReplace(sector));
        }
        return "";
    }

    private String getSectorToReplace(String sectorCode) {
        if (sectorCode != null) {
            String sector = JnJCommonUtil.getValue(JNJ_LATAM_CHAT_SECTORS + sectorCode);
            if (StringUtils.isNotBlank(sector)) {
                return UNDERLINE + sector;
            }
        }
        return "";
    }

    String getChatGroupProperty() {
        return JNJ_LATAM_CHAT_GROUPS;
    }

}