/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.process.email.context;

import com.jnj.core.services.JnjConfigService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractJnjLaEmailContext<T extends StoreFrontCustomerProcessModel> extends AbstractEmailContext<T> {

    private static final String CMS = "CMS";
    private static final String JNJ_ROOT_SERVER_URL = "jnj.root.server.url";
    private static final String JNJ_ROOT_SERVER_URL_HTTPS = "jnj.root.server.url.https";
    private static final String AUTOMATIC_EMAIL_SENDER = "_automaticEmailSender";
    private static final String BCC = "bcc";
    
    private static final Logger LOG = Logger.getLogger(AbstractJnjLaEmailContext.class);

    private MediaService mediaService;

    private JnjConfigService jnjConfigService;

    private String siteLogoURL;

    @Override
    public void init(T businessProcessModel, EmailPageModel emailPageModel) {
        final CMSSiteModel site = (CMSSiteModel) getSite(businessProcessModel);

        emailPageModel.setFromEmail(getFromEmail(site));
        emailPageModel.setFromName(getFromName());

        siteLogoURL = getSiteLogoURL(businessProcessModel, site);

        super.init(businessProcessModel, emailPageModel);
    }

    String getSiteBaseUrlHttp(final StoreFrontCustomerProcessModel processModel) {
        return getSiteBaseUrl(processModel, JNJ_ROOT_SERVER_URL);
    }

    String getSiteBaseUrlHttps(final StoreFrontCustomerProcessModel processModel) {
        return getSiteBaseUrl(processModel, JNJ_ROOT_SERVER_URL_HTTPS);
    }

    private String getSiteBaseUrl(final StoreFrontCustomerProcessModel processModel, String property) {
        final String url = Config.getString(property, StringUtils.EMPTY);
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }

        final String store = processModel.getSite().getUid().split(CMS)[0];
        String baseSiteUrl = String.format(url, store);
        LOG.info("Base site URL::::::::::::::::::::::::::::: "+baseSiteUrl);
        return baseSiteUrl;
    }

    String getFromName() {
        return jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.Forms.FORMS_FROM_DISPLAY_NAME);
    }

    String getFromEmail(final CMSSiteModel site) {
        return jnjConfigService.getConfigValueById(site.getDefaultCountry().getIsocode().toUpperCase() + AUTOMATIC_EMAIL_SENDER);
    }

    static CatalogVersionModel getContentCatalogVersion(final CMSSiteModel site) {
    	final CatalogVersionModel catalogVersion= site.getContentCatalogs().get(0).getActiveCatalogVersion();
    	LOG.info("Catalog version#################### "+catalogVersion.getVersion());
        return catalogVersion;
    }

    private String getSiteLogoURL(final StoreFrontCustomerProcessModel processModel, final CMSSiteModel site) {
        return getSiteBaseUrlHttp(processModel) + mediaService.getMedia(getContentCatalogVersion(site), Jnjlab2bcoreConstants.SITE_LOGO_IMAGE_ID).getURL();
    }

    public List<EmailAttachmentModel> getAttachments() {
        return Collections.emptyList();
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public void setJnjConfigService(final JnjConfigService jnjConfigService) {
        this.jnjConfigService = jnjConfigService;
    }

    public MediaService getMediaService() {
        return mediaService;
    }

    public String getSiteLogoURL() {
        return siteLogoURL;
    }

    void setBcc(String bcc) {
        put(BCC, bcc);
    }

    public String getBcc() {
        return (String) get(BCC);
    }

    void setTo(String to) {
        put(EMAIL, to);
    }

}