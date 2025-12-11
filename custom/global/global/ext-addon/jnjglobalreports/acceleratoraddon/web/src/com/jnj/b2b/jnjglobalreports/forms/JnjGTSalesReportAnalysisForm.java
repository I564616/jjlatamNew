package com.jnj.b2b.jnjglobalreports.forms;

public class JnjGTSalesReportAnalysisForm extends JnjGTPurchaseAnalysisReportForm{

	private String orderType;
	private String customerPONo;
	private String salesDocNo;
	private String status;
	private String productCode;
	private String deliveryNo;
	private String invoiceNo;
	private String franchiseDesc;
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
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getFranchiseDesc() {
		return franchiseDesc;
	}
	public void setFranchiseDesc(String franchiseDesc) {
		this.franchiseDesc = franchiseDesc;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCustomerPONo() {
		return customerPONo;
	}
	public void setCustomerPONo(String customerPONo) {
		this.customerPONo = customerPONo;
	}
	public String getSalesDocNo() {
		return salesDocNo;
	}
	public void setSalesDocNo(String salesDocNo) {
		this.salesDocNo = salesDocNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	
	
	
}
