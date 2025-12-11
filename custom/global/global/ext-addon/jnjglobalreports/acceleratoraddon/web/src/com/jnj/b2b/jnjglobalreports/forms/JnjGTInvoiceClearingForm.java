package com.jnj.b2b.jnjglobalreports.forms;

public class JnjGTInvoiceClearingForm extends JnjGTPurchaseAnalysisReportForm{
	
	private String startDate;
	private String endDate;
	private String invoiceNumber;
	private String status;
	private String accountsSelectedValue;
	private String accountIds;
	private String receiptNumber;
	
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
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
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
