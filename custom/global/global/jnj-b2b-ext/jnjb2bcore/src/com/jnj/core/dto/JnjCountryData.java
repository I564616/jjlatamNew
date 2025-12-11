/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjCountryData implements java.io.Serializable
{


	private String name;
	private String isocode;
	private String phoneCode;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the isocode
	 */
	public String getIsocode()
	{
		return isocode;
	}

	/**
	 * @param isocode
	 *           the isocode to set
	 */
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

	/**
	 * @return the phoneCode
	 */
	public String getPhoneCode()
	{
		return phoneCode;
	}

	/**
	 * @param phoneCode
	 *           the phoneCode to set
	 */
	public void setPhoneCode(final String phoneCode)
	{
		this.phoneCode = phoneCode;
	}




}