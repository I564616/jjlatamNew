/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.data;

import de.hybris.platform.commercefacades.user.data.CustomerData;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCustomerData extends CustomerData
{
	private JnjAddressData defaultAddress;
	private String status;
	private Boolean emailNotification;
	private String siteLoginURL;
	private String logoURL;

	/**
	 * @return the logoURL
	 */
	public String getLogoURL()
	{
		return logoURL;
	}

	/**
	 * @param logoURL
	 *           the logoURL to set
	 */
	public void setLogoURL(final String logoURL)
	{
		this.logoURL = logoURL;
	}

	/**
	 * @return the siteLoginURL
	 */
	public String getSiteLoginURL()
	{
		return siteLoginURL;
	}

	/**
	 * @param siteLoginURL
	 *           the siteLoginURL to set
	 */
	public void setSiteLoginURL(final String siteLoginURL)
	{
		this.siteLoginURL = siteLoginURL;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	private Boolean showChangeAccount;


	/**
	 * @return the emailNotification
	 */
	public Boolean getEmailNotification()
	{
		return emailNotification;
	}

	/**
	 * @param emailNotification
	 *           the emailNotification to set
	 */
	public void setEmailNotification(final Boolean emailNotification)
	{
		this.emailNotification = emailNotification;
	}

	/**
	 * @return the status
	 */
	public String isStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the defaultAddress
	 */
	public JnjAddressData getDefaultAddress()
	{
		return defaultAddress;
	}

	/**
	 * @param defaultAddress
	 *           the defaultAddress to set
	 */
	public void setDefaultAddress(final JnjAddressData defaultAddress)
	{
		this.defaultAddress = defaultAddress;
	}


	/**
	 * @return the showChangeAccount
	 */
	public Boolean getShowChangeAccount()
	{
		return showChangeAccount;
	}

	/**
	 * @param showChangeAccount
	 *           the showChangeAccount to set
	 */
	public void setShowChangeAccount(final Boolean showChangeAccount)
	{
		this.showChangeAccount = showChangeAccount;
	}



}
