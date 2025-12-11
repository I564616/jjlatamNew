/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * TODO:<Ujjwal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSectorTypeData
{
	private String wwid;
	private String division;
	private boolean consumerProductsSector;
	private boolean mddSector;
	private boolean pharmaSector;
	private String typeOfProfile;
	private String accountNumbers;
	private boolean unknownAccount;
	private String glnOrAccountNumber;

	/**
	 * @return the division
	 */
	public String getDivision()
	{
		return division;
	}

	/**
	 * @return the glnOrAccountNumber
	 */
	public String getGlnOrAccountNumber()
	{
		return glnOrAccountNumber;
	}

	/**
	 * @param glnOrAccountNumber
	 *           the glnOrAccountNumber to set
	 */
	public void setGlnOrAccountNumber(final String glnOrAccountNumber)
	{
		this.glnOrAccountNumber = glnOrAccountNumber;
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
	 * @return the consumerProductsSector
	 */
	public boolean isConsumerProductsSector()
	{
		return consumerProductsSector;
	}

	/**
	 * @param consumerProductsSector
	 *           the consumerProductsSector to set
	 */
	public void setConsumerProductsSector(final boolean consumerProductsSector)
	{
		this.consumerProductsSector = consumerProductsSector;
	}

	/**
	 * @return the mddSector
	 */
	public boolean isMddSector()
	{
		return mddSector;
	}

	/**
	 * @param mddSector
	 *           the mddSector to set
	 */
	public void setMddSector(final boolean mddSector)
	{
		this.mddSector = mddSector;
	}

	/**
	 * @return the typeOfProfile
	 */
	public String getTypeOfProfile()
	{
		return typeOfProfile;
	}

	/**
	 * @param typeOfProfile
	 *           the typeOfProfile to set
	 */
	public void setTypeOfProfile(final String typeOfProfile)
	{
		this.typeOfProfile = typeOfProfile;
	}

	/**
	 * @return the accountNumbers
	 */
	public String getAccountNumbers()
	{
		return accountNumbers;
	}

	/**
	 * @param accountNumbers
	 *           the accountNumbers to set
	 */
	public void setAccountNumbers(final String accountNumbers)
	{
		this.accountNumbers = accountNumbers;
	}

	/**
	 * @return the unknownAccount
	 */
	public boolean isUnknownAccount()
	{
		return unknownAccount;
	}

	/**
	 * @param unknownAccount
	 *           the unknownAccount to set
	 */
	public void setUnknownAccount(final boolean unknownAccount)
	{
		this.unknownAccount = unknownAccount;
	}

	/**
	 * @return the wwid
	 */
	public String getWwid()
	{
		return wwid;
	}

	/**
	 * @param wwid
	 *           the wwid to set
	 */
	public void setWwid(final String wwid)
	{
		this.wwid = wwid;
	}

	public boolean isPharmaSector() {
		return pharmaSector;
	}

	public void setPharmaSector(boolean pharmaSector) {
		this.pharmaSector = pharmaSector;
	}

}
