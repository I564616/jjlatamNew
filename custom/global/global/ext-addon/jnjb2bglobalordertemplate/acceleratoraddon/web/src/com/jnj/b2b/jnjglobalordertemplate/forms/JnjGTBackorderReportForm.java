/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalordertemplate.forms;



/**
 * This class holds the data for the back-order report search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackorderReportForm
{
	private String accountIds;
	private String fromDate;
	private String toDate;
	private String accountsSelectedValue;
	private String downloadType;

	/**
	 * @return the accountIds
	 */
	public String getAccountIds()
	{
		return accountIds;
	}

	/**
	 * @param accountIds
	 *           the accountIds to set
	 */
	public void setAccountIds(final String accountIds)
	{
		this.accountIds = accountIds;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate()
	{
		return fromDate;
	}

	/**
	 * @param fromDate
	 *           the fromDate to set
	 */
	public void setFromDate(final String fromDate)
	{
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate()
	{
		return toDate;
	}

	/**
	 * @param toDate
	 *           the toDate to set
	 */
	public void setToDate(final String toDate)
	{
		this.toDate = toDate;
	}

	/**
	 * @return the accountsSelectedValue
	 */
	public String getAccountsSelectedValue()
	{
		return accountsSelectedValue;
	}

	/**
	 * @param accountsSelectedValue
	 *           the accountsSelectedValue to set
	 */
	public void setAccountsSelectedValue(final String accountsSelectedValue)
	{
		this.accountsSelectedValue = accountsSelectedValue;
	}

	/** 
	 * @return the downloadType
	 */
	public String getDownloadType()
	{
		return downloadType;
	}

	/**
	 * @param downloadType the downloadType to set
	 */
	public void setDownloadType(String downloadType)
	{
		this.downloadType = downloadType;
	}
}
