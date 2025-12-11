/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import com.jnj.core.dto.JnjRegistrationData;


/**
 * This class represents the event object for the successful registration email flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSuccessfulRegistrationEmailEvent extends AbstractCommerceUserEvent
{
	private final String siteLogoURL;
	private final JnjRegistrationData jnjRegistrationData;

	/**
	 * Constructor for setting the site logo URL.
	 * 
	 * @param siteLogoURLParam
	 * @param registrationData
	 */
	public JnjGTSuccessfulRegistrationEmailEvent(final String siteLogoURLParam, final JnjRegistrationData registrationData)
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
	public JnjRegistrationData getJnjRegistrationData()
	{
		return jnjRegistrationData;
	}
}
