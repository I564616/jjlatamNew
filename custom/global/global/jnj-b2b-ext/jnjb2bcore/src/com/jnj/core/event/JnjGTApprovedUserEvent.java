/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTApprovedUserEvent extends AbstractCommerceUserEvent
{
	private String logoURL;
	private String baseUrl;
	private String helpUrl;
	private String buisnessEmail;
	private boolean completeProfileFlag;
	private String passwordResetUrl;
	private String customerServiceNumber;

	/**
	 * @return the completeProfileFlag
	 */
	public boolean isCompleteProfileFlag()
	{
		return completeProfileFlag;
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
	 * @param completeProfileFlag
	 *           the completeProfileFlag to set
	 */
	public void setCompleteProfileFlag(final boolean completeProfileFlag)
	{
		this.completeProfileFlag = completeProfileFlag;
	}

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
	 * @return the helpUrl
	 */
	public String getHelpUrl()
	{
		return helpUrl;
	}

	/**
	 * @param helpUrl
	 *           the helpUrl to set
	 */
	public void setHelpUrl(final String helpUrl)
	{
		this.helpUrl = helpUrl;
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
	 * @return the passwordResetUrl
	 */
	public String getPasswordResetUrl()
	{
		return passwordResetUrl;
	}

	/**
	 * @param passwordResetUrl
	 *           the passwordResetUrl to set
	 */
	public void setPasswordResetUrl(final String passwordResetUrl)
	{
		this.passwordResetUrl = passwordResetUrl;
	}



}
