/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * TODO:<Sandeep-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjSalesOrgCustDTO
{
	private String customerId;
	private String salesOrg;
	private String sector;
	private String fileName;
	private String idocNumber;


	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *           the fileName to set
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the idocNumber
	 */
	public String getIdocNumber()
	{
		return idocNumber;
	}

	/**
	 * @param idocNumber
	 *           the idocNumber to set
	 */
	public void setIdocNumber(final String idocNumber)
	{
		this.idocNumber = idocNumber;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId
	 *           the customerId to set
	 */
	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg()
	{
		return salesOrg;
	}

	/**
	 * @param salesOrg
	 *           the salesOrg to set
	 */
	public void setSalesOrg(final String salesOrg)
	{
		this.salesOrg = salesOrg;
	}

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

}
