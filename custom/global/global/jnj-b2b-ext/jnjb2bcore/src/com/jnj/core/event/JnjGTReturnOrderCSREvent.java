/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

/**
 * @author nsinha7
 *
 */
public class JnjGTReturnOrderCSREvent extends AbstractCommerceUserEvent {


	/**
	 * JnJ Order Number.
	 */
	private String jnjOrderNumber;

	/**
	 * SAP based/originated Order Number.
	 */
	private String sapOrderNumber;

	/**
	 * Base URL for the BR/MX Site.
	 */
	private String baseUrl;
	
	/**
	 * Indicates the media URL for Logo
	 */

	private String mediaUrl;

	/**
	 * Indicates the Order code
	 */
	private String orderCode;
	

	/**
	 * Indicates the To Email
	 */
	private String toEmail;
	
	/**
	 * Indicates if the mail is to be sent to user or customer service.
	 */
	private boolean isCustomer;
	
	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}
	
	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *           the mediaUrl to set
	 */
	public void setMediaUrl(final String mediaUrl)
	{
		this.mediaUrl = mediaUrl;
	}
	
	/**
	 * @return the jnjOrderNumber
	 */
	public String getJnjOrderNumber()
	{
		return jnjOrderNumber;
	}

	/**
	 * @param jnjOrderNumber
	 *           the jnjOrderNumber to set
	 */
	public void setJnjOrderNumber(final String jnjOrderNumber)
	{
		this.jnjOrderNumber = jnjOrderNumber;
	}

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
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
	 * @return the toEmail
	 */
	/**
	 * @return
	 */
	public String getToEmail() {
		return toEmail;
	}

	/**
	 * @param toEmail
	 * 			the toEmail to set
	 */		
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	
	/**
	 * @return the isCustomer
	 */
	public boolean isCustomer() {
		return isCustomer;
	}

	/**
	 * @param isCustomer the isCustomer to set
	 */
	public void setCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

}
