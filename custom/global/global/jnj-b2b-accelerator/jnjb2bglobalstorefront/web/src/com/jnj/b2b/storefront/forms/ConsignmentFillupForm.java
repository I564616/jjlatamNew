/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.storefront.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * Pojo for 'payments details' form.
 */
public class ConsignmentFillupForm
{
	private String customerPONo;
	private String endUser;
	private String stockUser;
	private String poDate;
	private String requestDelDate;
	private String comment;
	public String getCustomerPONo() {
		return customerPONo;
	}
	public void setCustomerPONo(String customerPONo) {
		this.customerPONo = customerPONo;
	}
	public String getEndUser() {
		return endUser;
	}
	public void setEndUser(String endUser) {
		this.endUser = endUser;
	}
	public String getStockUser() {
		return stockUser;
	}
	public void setStockUser(String stockUser) {
		this.stockUser = stockUser;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public String getRequestDelDate() {
		return requestDelDate;
	}
	public void setRequestDelDate(String requestDelDate) {
		this.requestDelDate = requestDelDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}


}
