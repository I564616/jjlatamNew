/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dto;

/**
 *
 */
public class JnjTranslationDTO
{

	private String customerNum;
	private String materialNum;

	private String custMaterialNum;

	private String baseUom;

	private String custUom;

	private String denConversion;

	private String numConversion;

	private String roundingProfile;

	private String lastUpdatedDate;

	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate
	 *           the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(final String lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * @return the customerNum
	 */
	public String getCustomerNum()
	{
		return customerNum;
	}

	/**
	 * @return the custUom
	 */
	public String getCustUom()
	{
		return custUom;
	}

	/**
	 * @param custUom
	 *           the custUom to set
	 */
	public void setCustUom(final String custUom)
	{
		this.custUom = custUom;
	}

	/**
	 * @param customerNum
	 *           the customerNum to set
	 */
	public void setCustomerNum(final String customerNum)
	{
		this.customerNum = customerNum;
	}

	/**
	 * @return the materialNum
	 */
	public String getMaterialNum()
	{
		return materialNum;
	}

	/**
	 * @param materialNum
	 *           the materialNum to set
	 */
	public void setMaterialNum(final String materialNum)
	{
		this.materialNum = materialNum;
	}

	/**
	 * @return the custMaterialNum
	 */
	public String getCustMaterialNum()
	{
		return custMaterialNum;
	}

	/**
	 * @param custMaterialNum
	 *           the custMaterialNum to set
	 */
	public void setCustMaterialNum(final String custMaterialNum)
	{
		this.custMaterialNum = custMaterialNum;
	}

	/**
	 * @return the baseUom
	 */
	public String getBaseUom()
	{
		return baseUom;
	}

	/**
	 * @param baseUom
	 *           the baseUom to set
	 */
	public void setBaseUom(final String baseUom)
	{
		this.baseUom = baseUom;
	}

	/**
	 * @return the denConversion
	 */
	public String getDenConversion()
	{
		return denConversion;
	}

	/**
	 * @param denConversion
	 *           the denConversion to set
	 */
	public void setDenConversion(final String denConversion)
	{
		this.denConversion = denConversion;
	}

	/**
	 * @return the numConversion
	 */
	public String getNumConversion()
	{
		return numConversion;
	}

	/**
	 * @param numConversion
	 *           the numConversion to set
	 */
	public void setNumConversion(final String numConversion)
	{
		this.numConversion = numConversion;
	}

	/**
	 * @return the roundingProfile
	 */
	public String getRoundingProfile()
	{
		return roundingProfile;
	}

	/**
	 * @param roundingProfile
	 *           the roundingProfile to set
	 */
	public void setRoundingProfile(final String roundingProfile)
	{
		this.roundingProfile = roundingProfile;
	}







}
