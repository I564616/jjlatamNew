/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;

/**
 * AAOL-4603: This class holds the data for the OA Delivery List report search form
 * 
 * @author Cognizant
 * @version 1.0
 */
public class JnjGTOADeliveryListReportForm extends JnjGTPurchaseAnalysisReportForm
{
	private String orderType; //- By default it will be Standard Order
	private String salesDocNum;
	private String custPONum;
	private String deliveryNum;
	private String actualShipmentDate;
	private String franchiseDesc;
	private String productCode;
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getSalesDocNum() {
		return salesDocNum;
	}
	public void setSalesDocNum(String salesDocNum) {
		this.salesDocNum = salesDocNum;
	}
	public String getCustPONum() {
		return custPONum;
	}
	public void setCustPONum(String custPONum) {
		this.custPONum = custPONum;
	}
	public String getDeliveryNum() {
		return deliveryNum;
	}
	public void setDeliveryNum(String deliveryNum) {
		this.deliveryNum = deliveryNum;
	}
	public String getActualShipmentDate() {
		return actualShipmentDate;
	}
	public void setActualShipmentDate(String actualShipmentDate) {
		this.actualShipmentDate = actualShipmentDate;
	}
	public String getFranchiseDesc() {
		return franchiseDesc;
	}
	public void setFranchiseDesc(String franchiseDesc) {
		this.franchiseDesc = franchiseDesc;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
