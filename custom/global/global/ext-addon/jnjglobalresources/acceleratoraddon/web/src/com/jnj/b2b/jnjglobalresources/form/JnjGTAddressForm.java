/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalresources.form;

/**
 * YTODO <<Replace this line with the class description>>
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTAddressForm extends AddressForm
{

	private String mobileNumberPrefix;
	private String mobileNumber;
	private String faxNumberPrefix;
	private String faxNumber;
	private String state;

	/**
	 * @return the mobileNumberPrefix
	 */
	public String getMobileNumberPrefix()
	{
		return mobileNumberPrefix;
	}

	/**
	 * @param mobileNumberPrefix
	 *           the mobileNumberPrefix to set
	 */
	public void setMobileNumberPrefix(final String mobileNumberPrefix)
	{
		this.mobileNumberPrefix = mobileNumberPrefix;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber()
	{
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *           the mobileNumber to set
	 */
	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the faxNumberPrefix
	 */
	public String getFaxNumberPrefix()
	{
		return faxNumberPrefix;
	}

	/**
	 * @param faxNumberPrefix
	 *           the faxNumberPrefix to set
	 */
	public void setFaxNumberPrefix(final String faxNumberPrefix)
	{
		this.faxNumberPrefix = faxNumberPrefix;
	}

	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * @param faxNumber
	 *           the faxNumber to set
	 */
	public void setFaxNumber(final String faxNumber)
	{
		this.faxNumber = faxNumber;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}


}
