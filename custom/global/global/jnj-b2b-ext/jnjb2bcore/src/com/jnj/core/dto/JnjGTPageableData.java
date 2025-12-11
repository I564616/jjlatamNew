/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;

import com.jnj.core.dto.JnjPageableData;


/**
 * This class is used for generic data transfer objects across various modules
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTPageableData extends JnjPageableData
{
	private List<String> searchParamsList; // Added for Reports

	private List<JnjGTSearchDTO> searchDtoList; //Added for User Search 

	boolean searchUserFlag;

	boolean searchFlag; // Added for Reports
	
	private String customerPONumber;
	private String salesDocumentNumber;
	private String invoiceNumber;
	private String orderType;
	private String financialsDescription;
	private String financialStatus;
	private String categoryCode;
	private String receiptNumber;
	
	
	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCustomerPONumber() {
		return customerPONumber;
	}

	public void setCustomerPONumber(String customerPONumber) {
		this.customerPONumber = customerPONumber;
	}

	public String getSalesDocumentNumber() {
		return salesDocumentNumber;
	}

	public void setSalesDocumentNumber(String salesDocumentNumber) {
		this.salesDocumentNumber = salesDocumentNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getFinancialStatus() {
		return financialStatus;
	}

	public void setFinancialStatus(String financialStatus) {
		this.financialStatus = financialStatus;
	}

	public String getFinancialsDescription() {
		return financialsDescription;
	}

	public void setFinancialsDescription(String financialsDescription) {
		this.financialsDescription = financialsDescription;
	}

	/**
	 * @return the searchParamsList
	 */
	public List<String> getSearchParamsList()
	{
		return searchParamsList;
	}

	/**
	 * @return the searchUserFlag
	 */
	public boolean isSearchUserFlag()
	{
		return searchUserFlag;
	}

	/**
	 * @param searchUserFlag
	 *           the searchUserFlag to set
	 */
	public void setSearchUserFlag(final boolean searchUserFlag)
	{
		this.searchUserFlag = searchUserFlag;
	}

	/**
	 * @return the searchDtoList
	 */
	public List<JnjGTSearchDTO> getSearchDtoList()
	{
		return searchDtoList;
	}

	/**
	 * @param searchDtoList
	 *           the searchDtoList to set
	 */
	public void setSearchDtoList(final List<JnjGTSearchDTO> searchDtoList)
	{
		this.searchDtoList = searchDtoList;
	}

	/**
	 * @param searchParamsList
	 *           the searchParamsList to set
	 */
	public void setSearchParamsList(final List<String> searchParamsList)
	{
		this.searchParamsList = searchParamsList;
	}


	/**
	 * @return the searchFlag
	 */
	public boolean isSearchFlag()
	{
		return searchFlag;
	}

	/**
	 * @param searchFlag
	 *           the searchFlag to set
	 */
	public void setSearchFlag(final boolean searchFlag)
	{
		this.searchFlag = searchFlag;
	}


}
