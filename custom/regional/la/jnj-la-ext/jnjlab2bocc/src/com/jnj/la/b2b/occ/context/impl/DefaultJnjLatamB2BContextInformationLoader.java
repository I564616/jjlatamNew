package com.jnj.la.b2b.occ.context.impl;
/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
import com.jnj.la.b2b.occ.context.JnjLatamB2BContextInformationLoader;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;


/**
 * Load default data into session context.
 */
public class DefaultJnjLatamB2BContextInformationLoader implements JnjLatamB2BContextInformationLoader
{
    private static final String SITE_NAME = "JNJ_SITE_NAME";

    private BaseSiteService baseSiteService;
    private SessionService sessionService;

    @Override
    public void setSessionAttributes()
    {
        setCountryAndBusinessSector();
    }

    protected void setCountryAndBusinessSector()
    {
        final BaseSiteModel site = getBaseSiteService().getCurrentBaseSite();
        if (site != null)
        {
            getSessionService().setAttribute(SITE_NAME, site.getJnjWebSiteType().getCode());
        }
    }

    protected BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    protected SessionService getSessionService()
    {
        return sessionService;
    }

    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
