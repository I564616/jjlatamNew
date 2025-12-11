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

public class JnjProductDetailsDTO
{
	private String productAttribute1;
	private String productAttribute2;
	private String productAttribute3;
	private String salesOrganization;
	private String Sector;
	private String country;
	private String status;
	private String vrkmeUnitMeasure;
	private String diliveryUnitMeasure;
	private String coldChainProduct;


	/**
	 * @return the coldChainProduct
	 */
	public String getColdChainProduct()
	{
		return coldChainProduct;
	}

	/**
	 * @param coldChainProduct
	 *           the coldChainProduct to set
	 */
	public void setColdChainProduct(final String coldChainProduct)
	{
		this.coldChainProduct = coldChainProduct;
	}

	/**
	 * @return the diliveryUnitMeasure
	 */
	public String getDiliveryUnitMeasure()
	{
		return diliveryUnitMeasure;
	}

	/**
	 * @param diliveryUnitMeasure
	 *           the diliveryUnitMeasure to set
	 */
	public void setDiliveryUnitMeasure(final String diliveryUnitMeasure)
	{
		this.diliveryUnitMeasure = diliveryUnitMeasure;
	}

	/**
	 * @return the vrkmeUnitMeasure
	 */
	public String getVrkmeUnitMeasure()
	{
		return vrkmeUnitMeasure;
	}

	/**
	 * @param vrkmeUnitMeasure
	 *           the vrkmeUnitMeasure to set
	 */
	public void setVrkmeUnitMeasure(final String vrkmeUnitMeasure)
	{
		this.vrkmeUnitMeasure = vrkmeUnitMeasure;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
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
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return Sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		Sector = sector;
	}

	/**
	 * @return the productAttribute1
	 */
	public String getProductAttribute1()
	{
		return productAttribute1;
	}

	/**
	 * @param productAttribute1
	 *           the productAttribute1 to set
	 */
	public void setProductAttribute1(final String productAttribute1)
	{
		this.productAttribute1 = productAttribute1;
	}

	/**
	 * @return the productAttribute2
	 */
	public String getProductAttribute2()
	{
		return productAttribute2;
	}

	/**
	 * @param productAttribute2
	 *           the productAttribute2 to set
	 */
	public void setProductAttribute2(final String productAttribute2)
	{
		this.productAttribute2 = productAttribute2;
	}

	/**
	 * @return the productAttribute3
	 */
	public String getProductAttribute3()
	{
		return productAttribute3;
	}

	/**
	 * @param productAttribute3
	 *           the productAttribute3 to set
	 */
	public void setProductAttribute3(final String productAttribute3)
	{
		this.productAttribute3 = productAttribute3;
	}

	/**
	 * @return the salesOrganization
	 */
	public String getSalesOrganization()
	{
		return salesOrganization;
	}

	/**
	 * @param salesOrganization
	 *           the salesOrganization to set
	 */
	public void setSalesOrganization(final String salesOrganization)
	{
		this.salesOrganization = salesOrganization;
	}

}
