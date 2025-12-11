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
package com.jnj.facades.data;

/**
 *
 */
public class JnjLaCartModificationData extends JnjCartModificationData
{
	private boolean contractVoilate;

	/**
	 * @return the contractVoilate
	 */
	public boolean isContractVoilate()
	{
		return contractVoilate;
	}

	/**
	 * @param contractVoilate
	 *           the contractVoilate to set
	 */
	public void setContractVoilate(final boolean contractVoilate)
	{
		this.contractVoilate = contractVoilate;
	}
}
