/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * This class represents the event object for the registration confirmation email flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTRegistrationConfirmationEmailEvent extends AbstractCommerceUserEvent
{
	private final String siteLogoURL;
	private String userEmail;
	private String userFirstName;
	private String userLastName;

	/**
	 * Constructor for setting the site logo URL.
	 * 
	 * @param siteLogoURLParam
	 */
	public JnjGTRegistrationConfirmationEmailEvent(final String siteLogoURLParam)
	{
		super();
		this.siteLogoURL = siteLogoURLParam;
	}

	/**
	 * @return the siteLogoURL
	 */
	public String getSiteLogoURL()
	{
		return siteLogoURL;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail()
	{
		return userEmail;
	}

	/**
	 * @param userEmail
	 *           the userEmail to set
	 */
	public void setUserEmail(final String userEmail)
	{
		this.userEmail = userEmail;
	}

	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName()
	{
		return userFirstName;
	}

	/**
	 * @param userFirstName
	 *           the userFirstName to set
	 */
	public void setUserFirstName(final String userFirstName)
	{
		this.userFirstName = userFirstName;
	}

	/**
	 * @return the userLastName
	 */
	public String getUserLastName()
	{
		return userLastName;
	}

	/**
	 * @param userLastName
	 *           the userLastName to set
	 */
	public void setUserLastName(final String userLastName)
	{
		this.userLastName = userLastName;
	}

}
