package com.jnj.b2b.jnjglobalreports.forms;

public class JnjGTAccountAgingForm {

	private String payerId;
	private String daysInArrears;
	private String dueItem;
	private String NotDue;
	
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getDaysInArrears() {
		return daysInArrears;
	}
	public void setDaysInArrears(String daysInArrears) {
		this.daysInArrears = daysInArrears;
	}
	public String getDueItem() {
		return dueItem;
	}
	public void setDueItem(String dueItem) {
		this.dueItem = dueItem;
	}
	public String getNotDue() {
		return NotDue;
	}
	public void setNotDue(String notDue) {
		NotDue = notDue;
	}
}
