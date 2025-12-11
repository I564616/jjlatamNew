package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjGTTemporaryPasswordEvent extends AbstractCommerceUserEvent {

	private String temporaryPassword;
	private String buisnessEmail;
	/**
	 * 
	 * @return TemporaryPassword
	 */
	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	/**
	 * 
	 * @return BuisnessEmail
	 */
	public String getBuisnessEmail() {
		return buisnessEmail;
	}

	public void setBuisnessEmail(String buisnessEmail) {
		this.buisnessEmail = buisnessEmail;
	}
	
}
