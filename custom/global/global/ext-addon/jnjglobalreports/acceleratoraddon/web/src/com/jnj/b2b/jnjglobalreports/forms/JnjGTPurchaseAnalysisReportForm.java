/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;

/**
 * This class holds the common data for the PA reports search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTPurchaseAnalysisReportForm
{
	private String accountIds;
	private String oldAccountIds;
	private String startDate;
	private String endDate;
	private String orderedFrom;
	private String downloadType;
	private String accountsSelectedValue;
	private boolean allAccountsFlag;
	
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
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return the orderedFrom
	 */
	public String getOrderedFrom()
	{
		return orderedFrom;
	}

	/**
	 * @param orderedFrom
	 *           the orderedFrom to set
	 */
	public void setOrderedFrom(final String orderedFrom)
	{
		this.orderedFrom = orderedFrom;
	}

	/**
	 * @return the downloadType
	 */
	public String getDownloadType()
	{
		return downloadType;
	}

	/**
	 * @param downloadType
	 *           the downloadType to set
	 */
	public void setDownloadType(final String downloadType)
	{
		this.downloadType = downloadType;
	}

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

	public String getOldAccountIds() {
		return oldAccountIds;
	}

	public void setOldAccountIds(String oldAccountIds) {
		this.oldAccountIds = oldAccountIds;
	}
	/**
	 * @return the allAccountsFlag
	 */
	public boolean isAllAccountsFlag()
	{
		return allAccountsFlag;
	}

	/**
	 * @param allAccountsFlag
	 *           the allAccountsFlag to set
	 */
	public void setAllAccountsFlag(final boolean allAccountsFlag)
	{
		this.allAccountsFlag = allAccountsFlag;
	}
}
