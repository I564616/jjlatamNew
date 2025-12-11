/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.io.Serializable;

import com.jnj.la.core.dto.JnjLatamRegistrationData;


/**
 *
 */
public class JnjLatamSuccessfulRegistrationEmailEvent extends AbstractCommerceUserEvent implements Serializable
{
	private final String siteLogoURL;
	private final transient JnjLatamRegistrationData jnjRegistrationData;

	/**
	 * Constructor for setting the site logo URL.
	 *
	 * @param siteLogoURLParam
	 * @param registrationData
	 */
	public JnjLatamSuccessfulRegistrationEmailEvent(final String siteLogoURLParam, final JnjLatamRegistrationData registrationData)

	{
		super();
		this.siteLogoURL = siteLogoURLParam;
		this.jnjRegistrationData = registrationData;
	}

	/**
	 * @return the siteLogoURL
	 */
	public String getSiteLogoURL()
	{
		return siteLogoURL;
	}

	/**
	 * @return the jnjRegistrationData
	 */
	public JnjLatamRegistrationData getJnjRegistrationData()
	{
		return jnjRegistrationData;
	}


}
