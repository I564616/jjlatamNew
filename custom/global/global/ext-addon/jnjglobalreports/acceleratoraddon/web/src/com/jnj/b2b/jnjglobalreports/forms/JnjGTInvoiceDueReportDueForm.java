package com.jnj.b2b.jnjglobalreports.forms;

public class JnjGTInvoiceDueReportDueForm extends JnjGTPurchaseAnalysisReportForm{
	
	private String invoiceDueDate;
	private String invoiceNumber;
	private String status;
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
	public String getInvoiceDueDate() {
		return invoiceDueDate;
	}
	public void setInvoiceDueDate(String invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
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
}
