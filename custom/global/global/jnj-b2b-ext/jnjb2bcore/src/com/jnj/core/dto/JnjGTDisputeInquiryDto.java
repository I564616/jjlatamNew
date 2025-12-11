/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;


/**
 * Class for holding generic Dispute Inquiry Form data captured from Dispute Order/Item inquiry page.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTDisputeInquiryDto
{
	/**
	 * Account number which relates with the dispute inquiry.
	 */
	private String accountNumber;

	/**
	 * Purchase Order number of the order for which dispute is raised.
	 */
	private String purchaseOrderNumber;

	/**
	 * Phone number of the user who raised the dispute over order/item.
	 */
	private String phoneNumber;

	/**
	 * Invoice Number associated with the disputed order/item.
	 */
	private List<String> invoiceNumber;
	
	private List<String> productCode;
	
	private List<String> contactNumber;
	/**
	 * Quantity associated with the disputed order/item.
	 */
	private List<String> quantity;


	public List<String> getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}

	public List<String> getQuantity() {
		return quantity;
	}

	public void setQuantity(List<String> quantity) {
		this.quantity = quantity;
	}

	public List<String> getProductCode() {
		return productCode;
	}

	public void setProductCode(List<String> productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *           the accountNumber to set
	 */
	public void setAccountNumber(final String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the purchaseOrderNumber
	 */
	public String getPurchaseOrderNumber()
	{
		return purchaseOrderNumber;
	}

	/**
	 * @param purchaseOrderNumber
	 *           the purchaseOrderNumber to set
	 */
	public void setPurchaseOrderNumber(final String purchaseOrderNumber)
	{
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *           the phoneNumber to set
	 */
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the invoiceNumber
	 */
	public List<String> getInvoiceNumber()
	{
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber
	 *           the invoiceNumber to set
	 */
	public void setInvoiceNumber(final List<String> invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

}
