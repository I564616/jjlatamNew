/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.List;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;

/**
 * Event to carry cronjob monitoring data and processed by JnjLaCronJobMonitoringReportEventListener
 */

public class JnjLaCronJobMonitoringReportEvent extends AbstractCommerceUserEvent
{
    private List<JnjLaCronJobMonitoringEmailDto> jnjLaCronJobMonitoringEmailDtoList;

    private String country;

    private String userEmailId;

    private String cmsEmailSite;


    /**
     * @return the userEmailId
     */
    public String getUserEmailId()
    {
        return userEmailId;
    }

    /**
     * @param userEmailId
     *           the userEmailId to set
     */
    public void setUserEmailId(final String userEmailId)
    {
        this.userEmailId = userEmailId;
    }

    /**
     * @return the cmsEmailSite
     */
    public String getCmsEmailSite()
    {
        return cmsEmailSite;
    }

    /**
     * @param cmsEmailSite
     *           the cmsEmailSite to set
     */
    public void setCmsEmailSite(final String cmsEmailSite)
    {
        this.cmsEmailSite = cmsEmailSite;
    }

    /**
     * @return the jnjLaCronJobMonitoringEmailDtoList
     */
    public List<JnjLaCronJobMonitoringEmailDto> getJnjLaCronJobMonitoringEmailDtoList()
    {
        return jnjLaCronJobMonitoringEmailDtoList;
    }

    /**
     * @param jnjLaCronJobMonitoringEmailDtoList
     *           the jnjLaCronJobMonitoringEmailDtoList to set
     */
    public void setJnjLaCronJobMonitoringEmailDtoList(
            final List<JnjLaCronJobMonitoringEmailDto> jnjLaCronJobMonitoringEmailDtoList)
    {
        this.jnjLaCronJobMonitoringEmailDtoList = jnjLaCronJobMonitoringEmailDtoList;
    }

    /**
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @param country
     *           the country to set
     */
    public void setCountry(final String country)
    {
        this.country = country;
    }
}
