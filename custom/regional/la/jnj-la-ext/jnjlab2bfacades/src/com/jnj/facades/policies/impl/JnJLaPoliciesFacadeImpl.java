/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.policies.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.policies.JnjGTPoliciesFacade;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class JnJLaPoliciesFacadeImpl extends JnjGTPoliciesFacadeImpl implements JnjGTPoliciesFacade {

    private static final Logger LOGGER = Logger.getLogger(JnJLaPoliciesFacadeImpl.class);

    private static final String COOKIE_POLICY_PATTERN = "CMSsite%sCookiePolicy";
    private static final String LEGAL_NOTICE_PATTERN = "CMSsite%sLegalNotice";
    private static final String PRIVACY_POLICY_PATTERN = "CMSsite%sPrivacyPolicy";
    private static final String USEFUL_LINKS_PATTERN = "CMSsite%sUsefulLinks";
    private static final String TERMS_AND_COND_PATTERN = "CMSsite%sTermsAndCondition";

    @Override
    public String getCookiePolicies() {
        return getMediaUrl(COOKIE_POLICY_PATTERN);
    }

    @Override
    public String getTermsAndConditionsPolicies() {
        return getMediaUrl(TERMS_AND_COND_PATTERN);
    }

    @Override
    public String getLegalNotices() {
        return getMediaUrl(LEGAL_NOTICE_PATTERN);
    }

    @Override
    public String getPrivacyPolicies() {
        return getMediaUrl(PRIVACY_POLICY_PATTERN);
    }

    @Override
    public String getUsefulLinks() {
        return getMediaUrl(USEFUL_LINKS_PATTERN);
    }

    private String getMediaUrl(final String mediaCodePattern) {
        try {
            final String language = getI18nService().getCurrentLocale().getLanguage();
            final MediaModel mediaModel = getMediaService().getMedia(getCatalogVersionForCountry(), String.format(mediaCodePattern, language));
            return mediaModel.getURL();
        } catch(final Exception e) {
            LOGGER.error("Exception has occurred while getting the media model for" + mediaCodePattern,e);
            return StringUtils.EMPTY;
        }
    }

    private CatalogVersionModel getCatalogVersionForCountry() {
        final String id = getCmsSiteService().getCurrentSite().getContentCatalogs().get(0).getId();
        return getCatalogVersionService().getCatalogVersion(id, Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE);
    }

}