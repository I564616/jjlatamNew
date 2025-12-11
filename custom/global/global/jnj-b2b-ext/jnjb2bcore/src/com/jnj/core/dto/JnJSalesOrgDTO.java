/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;

import java.util.List;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */


public class JnJSalesOrgDTO
{
	private String salesOrganization = "";
	private String distributionChannel = "";
	private String division = "";
	private String productAttribute1 = "";
	private String productAttribute2 = "";
	private String productAttribute3 = "";
	private List<PartnerFunction> partnerFunction;


	/**
	 * @return the partnerFunction
	 */
	public List<PartnerFunction> getPartnerFunction()
	{
		return partnerFunction;
	}

	/**
	 * @param partnerFunction
	 *           the partnerFunction to set
	 */
	public void setPartnerFunction(final List<PartnerFunction> partnerFunction)
	{
		this.partnerFunction = partnerFunction;
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

	/**
	 * @return the distributionChannel
	 */
	public String getDistributionChannel()
	{
		return distributionChannel;
	}

	/**
	 * @param distributionChannel
	 *           the distributionChannel to set
	 */
	public void setDistributionChannel(final String distributionChannel)
	{
		this.distributionChannel = distributionChannel;
	}

	/**
	 * @return the division
	 */
	public String getDivision()
	{
		return division;
	}

	/**
	 * @param division
	 *           the division to set
	 */
	public void setDivision(final String division)
	{
		this.division = division;
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

}
