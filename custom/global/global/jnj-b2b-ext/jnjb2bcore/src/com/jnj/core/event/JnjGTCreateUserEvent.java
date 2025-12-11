package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjGTCreateUserEvent extends AbstractCommerceUserEvent {


	protected String baseUrl;
	protected String buisnessEmail;
	
	/**
	 * 
	 * @return BaseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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
