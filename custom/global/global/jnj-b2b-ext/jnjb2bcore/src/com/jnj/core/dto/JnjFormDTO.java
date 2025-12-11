/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;

/**
 * This class is responsible for setting or getting the form name flag
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjFormDTO
{
	public String formName;
	public String serverURL;
	public String fromEmail;
	public String fromDisplayName;

	/**
	 * @return the serverURL
	 */
	public String getServerURL()
	{
		return serverURL;
	}

	/**
	 * @param serverURL
	 *           the serverURL to set
	 */
	public void setServerURL(final String serverURL)
	{
		this.serverURL = serverURL;
	}

	/**
	 * @return the formName
	 */
	public String getFormName()
	{
		return formName;
	}

	/**
	 * @param formName
	 *           the formName to set
	 */
	public void setFormName(final String formName)
	{
		this.formName = formName;
	}

	/**
	 * @return the fromEmail
	 */
	public String getFromEmail()
	{
		return fromEmail;
	}

	/**
	 * @param fromEmail
	 *           the fromEmail to set
	 */
	public void setFromEmail(final String fromEmail)
	{
		this.fromEmail = fromEmail;
	}

	/**
	 * @return the fromDisplayName
	 */
	public String getFromDisplayName()
	{
		return fromDisplayName;
	}

	/**
	 * @param fromDisplayName
	 *           the fromDisplayName to set
	 */
	public void setFromDisplayName(final String fromDisplayName)
	{
		this.fromDisplayName = fromDisplayName;
	}
}
