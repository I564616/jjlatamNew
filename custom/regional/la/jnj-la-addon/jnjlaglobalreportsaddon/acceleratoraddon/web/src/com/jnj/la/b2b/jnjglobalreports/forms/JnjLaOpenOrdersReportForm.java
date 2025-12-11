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
package com.jnj.la.b2b.jnjglobalreports.forms;

import java.util.List;

public class JnjLaOpenOrdersReportForm {
	private List<String> accountIds;
    private String fromDate;
    private String toDate;
    private String orderType;
    private String orderNumber;
    private String downloadType;
    private String productCode;
    private String shipTo;
    private String reportColumns;
    private String quickSelection;
    
    public String getQuickSelection()
	{
		return quickSelection;
	}

	public void setQuickSelection(final String quickSelection)
	{
		this.quickSelection = quickSelection;
	}

    
	public List<String> getAccountIds()
	{
		return accountIds;
	}

	public void setAccountIds(final List<String> accountIds)
	{
		this.accountIds = accountIds;
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(final String fromDate)
	{
		this.fromDate = fromDate;
	}

	
	public String getToDate()
	{
		return toDate;
	}
	
	public void setToDate(final String toDate)
	{
		this.toDate = toDate;
	}
	
	public String getDownloadType()
	{
		return downloadType;
	}

	public void setDownloadType(String downloadType)
	{
		this.downloadType = downloadType;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public String getShipTo()
	{
		return shipTo;
	}

	public void setShipTo(String shipTo)
	{
		this.shipTo = shipTo;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getReportColumns()
	{
		return reportColumns;
	}

	public void setReportColumns(String reportColumns)
	{
		this.reportColumns = reportColumns;
	}


}