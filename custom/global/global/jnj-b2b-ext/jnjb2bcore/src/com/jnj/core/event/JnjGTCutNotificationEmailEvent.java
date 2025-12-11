/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * This class is the event object for the cut notification email.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCutNotificationEmailEvent extends AbstractCommerceUserEvent
{
	String displayOrderNumber;
	String hybrisOrderNumber;
	String userEmailAddress;
	String userFullName;
	

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	/**
	 * @return the userEmailAddress
	 */
	public String getUserEmailAddress()
	{
		return userEmailAddress;
	}

	/**
	 * @param userEmailAddress
	 *           the userEmailAddress to set
	 */
	public void setUserEmailAddress(final String userEmailAddress)
	{
		this.userEmailAddress = userEmailAddress;
	}

	/**
	 * @return the displayOrderNumber
	 */
	public String getDisplayOrderNumber()
	{
		return displayOrderNumber;
	}

	/**
	 * @param displayOrderNumber
	 *           the displayOrderNumber to set
	 */
	public void setDisplayOrderNumber(final String displayOrderNumber)
	{
		this.displayOrderNumber = displayOrderNumber;
	}

	/**
	 * @return the hybrisOrderNumber
	 */
	public String getHybrisOrderNumber()
	{
		return hybrisOrderNumber;
	}

	/**
	 * @param hybrisOrderNumber
	 *           the hybrisOrderNumber to set
	 */
	public void setHybrisOrderNumber(final String hybrisOrderNumber)
	{
		this.hybrisOrderNumber = hybrisOrderNumber;
	}

}
