/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import com.jnj.core.dto.JnjRegisterData;


/**
 * This is the event class for the forms email process
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjRegisterEvent extends AbstractCommerceUserEvent
{

	private JnjRegisterData registerData;
	private String siteLogoURL;

	/**
	 * @return the siteLogoURL
	 */
	public String getSiteLogoURL()
	{
		return siteLogoURL;
	}

	/**
	 * @param siteLogoURL
	 *           the siteLogoURL to set
	 */
	public void setSiteLogoURL(final String siteLogoURL)
	{
		this.siteLogoURL = siteLogoURL;
	}

	/**
	 * @return the registerData
	 */
	public JnjRegisterData getRegisterData()
	{
		return registerData;
	}

	/**
	 * @param registerData
	 *           the registerData to set
	 */
	public void setRegisterData(final JnjRegisterData registerData)
	{
		this.registerData = registerData;
	}

	/**
	 * @param registerData
	 */
	public JnjRegisterEvent(final JnjRegisterData registerData)
	{
		super();
		this.registerData = registerData;
	}

}
