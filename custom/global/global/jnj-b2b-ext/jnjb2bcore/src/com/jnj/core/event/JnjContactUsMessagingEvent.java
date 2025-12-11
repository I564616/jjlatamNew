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
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjContactUsMessagingEvent extends AbstractCommerceUserEvent
{
	private String toEmailAddress;
	private String emailBody;
	private String emailSubject;
	private String customerEmail;
	private String orderNumber;

	/**
	 * @return the toEmailAddress
	 */
	public String getToEmailAddress()
	{
		return toEmailAddress;
	}

	/**
	 * @param toEmailAddress
	 *           the toEmailAddress to set
	 */
	public void setToEmailAddress(final String toEmailAddress)
	{
		this.toEmailAddress = toEmailAddress;
	}

	/**
	 * @return the emailBody
	 */
	public String getEmailBody()
	{
		return emailBody;
	}

	/**
	 * @param emailBody
	 *           the emailBody to set
	 */
	public void setEmailBody(final String emailBody)
	{
		this.emailBody = emailBody;
	}

	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject()
	{
		return emailSubject;
	}

	/**
	 * @param emailSubject
	 *           the emailSubject to set
	 */
	public void setEmailSubject(final String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail()
	{
		return customerEmail;
	}

	/**
	 * @param customerEmail
	 *           the customerEmail to set
	 */
	public void setCustomerEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	public void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}
}
