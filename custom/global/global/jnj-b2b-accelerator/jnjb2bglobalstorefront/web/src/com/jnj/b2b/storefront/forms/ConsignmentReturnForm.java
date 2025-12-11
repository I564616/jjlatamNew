
package com.jnj.b2b.storefront.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Pojo for 'consignment return order' form.
 */

public class ConsignmentReturnForm
{
	private String customerPONo;
	private String endUser;
	private String stockUser;
	private String poDate;
	private String returnCreatedDate;
	
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
	public String getReturnCreatedDate() {
		return returnCreatedDate;
	}
	public void setReturnCreatedDate(String returnCreatedDate) {
		this.returnCreatedDate = returnCreatedDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	private String comment;
	 
}
	