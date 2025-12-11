/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;



/**
 * DTO class to store customer Eligibility relation data.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjCustomerEligiblityDTO
{
	/**
	 * <code>B2bUnit</code> to be restricted.
	 */
	private String b2bAccountUnitId;

	/**
	 * <code>Category</code> restricted for the <code>B2bUnit</code>.
	 */
	private String firstLevelCategoryId;

	/**
	 * 
	 */
	private String secondLevelCategoryId;

	/**
	 * 
	 */
	private String thirdLevelCategoryId;

	/**
	 * Represents Action status of the restricted Categories.
	 */
	private String status;

	/**
	 * 
	 */
	private String idocNumber;

	/**
	 * @return the b2bAccountUnitId
	 */
	public String getB2bAccountUnitId()
	{
		return b2bAccountUnitId;
	}

	/**
	 * @param b2bAccountUnitId
	 *           the b2bAccountUnitId to set
	 */
	public void setB2bAccountUnitId(final String b2bAccountUnitId)
	{
		this.b2bAccountUnitId = b2bAccountUnitId;
	}


	/**
	 * @return the firstLevelCategoryId
	 */
	public String getFirstLevelCategoryId()
	{
		return firstLevelCategoryId;
	}

	/**
	 * @param firstLevelCategoryId
	 *           the firstLevelCategoryId to set
	 */
	public void setFirstLevelCategoryId(final String firstLevelCategoryId)
	{
		this.firstLevelCategoryId = firstLevelCategoryId;
	}

	/**
	 * @return the secondLevelCategoryId
	 */
	public String getSecondLevelCategoryId()
	{
		return secondLevelCategoryId;
	}

	/**
	 * @param secondLevelCategoryId
	 *           the secondLevelCategoryId to set
	 */
	public void setSecondLevelCategoryId(final String secondLevelCategoryId)
	{
		this.secondLevelCategoryId = secondLevelCategoryId;
	}

	/**
	 * @return the thirdLevelCategoryId
	 */
	public String getThirdLevelCategoryId()
	{
		return thirdLevelCategoryId;
	}

	/**
	 * @param thirdLevelCategoryId
	 *           the thirdLevelCategoryId to set
	 */
	public void setThirdLevelCategoryId(final String thirdLevelCategoryId)
	{
		this.thirdLevelCategoryId = thirdLevelCategoryId;
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
}
