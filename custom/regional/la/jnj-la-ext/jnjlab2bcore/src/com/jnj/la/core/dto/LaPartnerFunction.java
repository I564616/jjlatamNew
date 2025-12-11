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

import com.jnj.core.dto.PartnerFunction;


/**
 *
 */
public class LaPartnerFunction extends PartnerFunction
{

	private String customerNumber = "";

	/**
	 * @return the customerNumber
	 */

	public String getCustomerNumber()
	{
		return customerNumber;
	}

	/**
	 * @param customerNumber
	 *           the customerNumber to set
	 */

	public void setCustomerNumber(final String customerNumber)
	{
		this.customerNumber = customerNumber;
	}
}
