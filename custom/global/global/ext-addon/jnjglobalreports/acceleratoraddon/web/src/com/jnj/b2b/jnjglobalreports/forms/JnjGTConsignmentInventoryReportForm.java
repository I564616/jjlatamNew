/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;

import java.util.List;

/**
 * This class holds the data for the inventory report search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTConsignmentInventoryReportForm
{
	private List<String> franchiseDescription;
	private String productCode;
	private String stockLocationAcc;
	private String downloadType;
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
	public  List<String> getFranchiseDescription() {
		return franchiseDescription;
	}
	public void setFranchiseDescription( List<String> franchiseDescription) {
		this.franchiseDescription = franchiseDescription;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getStockLocationAcc() {
		return stockLocationAcc;
	}
	public void setStockLocationAcc(String stockLocationAcc) {
		this.stockLocationAcc = stockLocationAcc;
	}
	public String getDownloadType() {
		return downloadType;
	}
	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

}