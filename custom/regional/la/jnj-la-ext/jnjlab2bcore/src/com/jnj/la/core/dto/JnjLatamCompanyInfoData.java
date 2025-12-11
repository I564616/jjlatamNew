/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dto;

import com.jnj.core.dto.JnjCompanyInfoData;


/**
 *
 */
public class JnjLatamCompanyInfoData extends JnjCompanyInfoData
{
	private String customerCode;
	private String soldTo;
	private String jnjAccountManager;

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode()
	{
		return customerCode;
	}

	/**
	 * @param customerCode
	 *           the customerCode to set
	 */
	public void setCustomerCode(final String customerCode)
	{
		this.customerCode = customerCode;
	}

	/**
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return soldTo;
	}

	/**
	 * @param soldTo
	 *           the soldTo to set
	 */
	public void setSoldTo(final String soldTo)
	{
		this.soldTo = soldTo;
	}

	/**
	 * @return the jnjAccountManager
	 */
	public String getJnjAccountManager()
	{
		return jnjAccountManager;
	}

	/**
	 * @param jnjAccountManager
	 *           the jnjAccountManager to set
	 */
	public void setJnjAccountManager(final String jnjAccountManager)
	{
		this.jnjAccountManager = jnjAccountManager;
	}


}
