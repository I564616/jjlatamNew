/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author himanshi.batra
 * 
 */
public class JnjGTProfilePasswordExpiryEvent extends AbstractCommerceUserEvent
{
	private String buisnessEmail;
	private String PasswordExpiryUrl;
	private String logoUrl;
	private String forPCMFlag;


	public String getForPCMFlag()
	{
		return forPCMFlag;
	}


	public void setForPCMFlag(final String forPCMFlag)
	{
		this.forPCMFlag = forPCMFlag;
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
	 * @return the passwordExpiryUrl
	 */
	public String getPasswordExpiryUrl()
	{
		return PasswordExpiryUrl;
	}

	/**
	 * @param passwordExpiryUrl
	 *           the passwordExpiryUrl to set
	 */
	public void setPasswordExpiryUrl(final String passwordExpiryUrl)
	{
		PasswordExpiryUrl = passwordExpiryUrl;
	}

	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl()
	{
		return logoUrl;
	}

	/**
	 * @param logoUrl
	 *           the logoUrl to set
	 */
	public void setLogoUrl(final String logoUrl)
	{
		this.logoUrl = logoUrl;
	}


}
