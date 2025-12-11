/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTRejectUserEvent extends AbstractCommerceUserEvent
{
	private String logoURL;
	private String baseUrl;
	private String buisnessEmail;
	private String customerServiceNumber;
	private String rejectionReason;

	/**
	 * @return the logoURL
	 */
	public String getLogoURL()
	{
		return logoURL;
	}

	/**
	 * @param logoURL
	 *           the logoURL to set
	 */
	public void setLogoURL(final String logoURL)
	{
		this.logoURL = logoURL;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the buisnessEmail
	 */
	public String getBuisnessEmail()
	{
		return buisnessEmail;
	}

	/**
	 * @param buisnessEmail
	 *           the buisnessEmail to set
	 */
	public void setBuisnessEmail(final String buisnessEmail)
	{
		this.buisnessEmail = buisnessEmail;
	}

	/**
	 * @return the customerServiceNumber
	 */
	public String getCustomerServiceNumber()
	{
		return customerServiceNumber;
	}

	/**
	 * @param customerServiceNumber
	 *           the customerServiceNumber to set
	 */
	public void setCustomerServiceNumber(final String customerServiceNumber)
	{
		this.customerServiceNumber = customerServiceNumber;
	}

	/**
	 * @return the rejectionReason
	 */
	public String getRejectionReason()
	{
		return rejectionReason;
	}

	/**
	 * @param rejectionReason
	 *           the rejectionReason to set
	 */
	public void setRejectionReason(final String rejectionReason)
	{
		this.rejectionReason = rejectionReason;
	}

}
