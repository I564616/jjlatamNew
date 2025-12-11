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

import com.jnj.core.dto.JnjUomDTO;


/**
 *
 */
public class JnjLaUomDTO extends JnjUomDTO
{
	private String salesUnitDimension;

	private String salesUnitCode;

	private int baseUnitCount;

	private int finalUnitCount;

	/**
	 * @return the finalUnitCount
	 */
	public int getFinalUnitCount()
	{
		return finalUnitCount;
	}

	/**
	 * @param finalUnitCount
	 *           the finalUnitCount to set
	 */
	public void setFinalUnitCount(final int finalUnitCount)
	{
		this.finalUnitCount = finalUnitCount;
	}

	/**
	 * @return the baseUnitCount
	 */
	public int getBaseUnitCount()
	{
		return baseUnitCount;
	}

	/**
	 * @param baseUnitCount
	 *           the baseUnitCount to set
	 */
	public void setBaseUnitCount(final int baseUnitCount)
	{
		this.baseUnitCount = baseUnitCount;
	}

	/**
	 * @return the salesUnitDimension
	 */
	public String getSalesUnitDimension()
	{
		return salesUnitDimension;
	}

	/**
	 * @return the salesUnitCode
	 */
	public String getSalesUnitCode()
	{
		return salesUnitCode;
	}

	/**
	 * @param salesUnitCode
	 *           the salesUnitCode to set
	 */
	public void setSalesUnitCode(final String salesUnitCode)
	{
		this.salesUnitCode = salesUnitCode;
	}

	/**
	 * @param salesUnitDimension
	 *           the salesUnitDimension to set
	 */
	public void setSalesUnitDimension(final String salesUnitDimension)
	{
		this.salesUnitDimension = salesUnitDimension;
	}
}
