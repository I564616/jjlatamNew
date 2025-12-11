/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.List;

import com.jnj.core.dto.JnjGTBackorderEmailDto;


/**
 * Event class responsible for carrying data for submitting Back-order Email Notification.
 * 
 * @author akash.rawat
 * 
 */
public class JnjGTBackorderEmailNotificationEvent extends AbstractCommerceUserEvent
{
	private String emailPreferenceOpenlinebackorder;
	/**
	 * Instance of <code>JnjGTBackorderEmailDto</code>
	 */
	private List<JnjGTBackorderEmailDto> emailData;

	/**
	 * @return the emailData
	 */
	public List<JnjGTBackorderEmailDto> getEmailData()
	{
		return emailData;
	}

	/**
	 * @param emailData
	 *           the emailData to set
	 */
	public void setEmailData(final List<JnjGTBackorderEmailDto> emailData)
	{
		this.emailData = emailData;
	}
	
	/**
	 * @return the emailPreferenceOpenlinebackorder
	 */
	public String getEmailPreferenceOpenlinebackorder()
	{
		return emailPreferenceOpenlinebackorder;
	}

	/**
	 * @param emailPreferenceOpenlinebackorder
	 *           the emailPreferenceOpenlinebackorder to set
	 */
	public void setEmailPreferenceOpenlinebackorder(String emailPreferenceOpenlinebackorder)
	{
		this.emailPreferenceOpenlinebackorder = emailPreferenceOpenlinebackorder;
	}
}
