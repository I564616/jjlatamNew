/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;


/**
 * This class holds the data for the inventory report search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTInventoryReportForm
{
	private String repUCNs;
	private String productCode;
	private String lotNumber;
	private boolean displayZeroStocks;
	private String downloadType;
	private String startDate;
	private String endDate;
	private String accountsSelectedValue;
	private String accountIds;

	public String getAccountsSelectedValue() {
		return accountsSelectedValue;
	}

	public void setAccountsSelectedValue(String accountsSelectedValue) {
		this.accountsSelectedValue = accountsSelectedValue;
	}

	public String getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(String accountIds) {
		this.accountIds = accountIds;
	}

	/**
	 * @return the repUCNs
	 */
	public String getRepUCNs()
	{
		return repUCNs;
	}

	/**
	 * @param repUCNs
	 *           the repUCNs to set
	 */
	public void setRepUCNs(final String repUCNs)
	{
		this.repUCNs = repUCNs;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the lotNumber
	 */
	public String getLotNumber()
	{
		return lotNumber;
	}

	/**
	 * @param lotNumber
	 *           the lotNumber to set
	 */
	public void setLotNumber(final String lotNumber)
	{
		this.lotNumber = lotNumber;
	}

	/**
	 * @return the displayZeroStocks
	 */
	public boolean isDisplayZeroStocks()
	{
		return displayZeroStocks;
	}

	/**
	 * @param displayZeroStocks
	 *           the displayZeroStocks to set
	 */
	public void setDisplayZeroStocks(final boolean displayZeroStocks)
	{
		this.displayZeroStocks = displayZeroStocks;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
