/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;

/**
 * DTO Class for the Contact Us Functionality
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjContactUsDTO
{
	private String customerEmail;
	private String subject;
	private String orderNumber;
	private String message;

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
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 *           the subject to set
	 */
	public void setSubject(final String subject)
	{
		this.subject = subject;
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

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}


}
