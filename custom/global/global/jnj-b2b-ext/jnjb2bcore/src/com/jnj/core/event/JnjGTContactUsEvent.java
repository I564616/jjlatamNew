/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author balinder.singh
 * 
 */
public class JnjGTContactUsEvent extends AbstractCommerceUserEvent
{
	private String productID;
	private String toEmailID;
	private String logoURL;
	private String portalName;
	private String fromEmailID;
	private String emailSubject;
	private String orderID;
	private String detailIssue;
	private String fromName;
	private String contactNumber;
	

	/**
	 * @return the contactNumber
	 */
	
	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getContactNumber()
	{
		return contactNumber;
	}

	/**
	 * @param contactNumber
	 *           the contactNumber to set
	 */
	public void setContactNumber(final String contactNumber)
	{
		this.contactNumber = contactNumber;
	}

	/**
	 * @return the detailIssue
	 */
	public String getDetailIssue()
	{
		return detailIssue;
	}

	/**
	 * @param detailIssue
	 *           the detailIssue to set
	 */
	public void setDetailIssue(final String detailIssue)
	{
		this.detailIssue = detailIssue;
	}

	/**
	 * @return the toEmailID
	 */
	public String getToEmailID()
	{
		return toEmailID;
	}

	/**
	 * @param toEmailID
	 *           the toEmailID to set
	 */
	public void setToEmailID(final String toEmailID)
	{
		this.toEmailID = toEmailID;
	}

	/**
	 * @return the fromEmailID
	 */
	public String getFromEmailID()
	{
		return fromEmailID;
	}

	/**
	 * @param fromEmailID
	 *           the fromEmailID to set
	 */
	public void setFromEmailID(final String fromEmailID)
	{
		this.fromEmailID = fromEmailID;
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
	 * @return the portalName
	 */
	public String getPortalName()
	{
		return portalName;
	}

	/**
	 * @param portalName
	 *           the portalName to set
	 */
	public void setPortalName(final String portalName)
	{
		this.portalName = portalName;
	}

	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject()
	{
		return emailSubject;
	}

	/**
	 * @param emailSubject
	 *           the emailSubject to set
	 */
	public void setEmailSubject(final String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the orderID
	 */
	public String getOrderID()
	{
		return orderID;
	}

	/**
	 * @param orderID
	 *           the orderID to set
	 */
	public void setOrderID(final String orderID)
	{
		this.orderID = orderID;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName()
	{
		return fromName;
	}

	/**
	 * @param fromName
	 *           the fromName to set
	 */
	public void setFromName(final String fromName)
	{
		this.fromName = fromName;
	}
}
