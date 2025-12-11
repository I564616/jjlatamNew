/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.versioning.services;

import com.jnj.core.services.JnjConfigService;
import com.jnj.la.versioning.enumeration.JnjLAVersionEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class JnJLaVersionService {

    private static final String JNJ_LA_VERSION_LABEL = "jnjLaVersionLabel";

    private JnjConfigService configService;

    public JnjLAVersionEnum getCurrentVersion() {
        final String version = configService.getConfigValueById(JNJ_LA_VERSION_LABEL);

        if (StringUtils.isBlank(version)) {
            return JnjLAVersionEnum.NONE;
        } else {
            return JnjLAVersionEnum.get(version);
        }
    }

    @Required
    public void setConfigService(final JnjConfigService configService) {
        this.configService = configService;
    }
}
