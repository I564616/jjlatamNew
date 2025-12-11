/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;



/**
 * @author sakshi.kashiva
 * 
 */
public class JnjGTCartOrderConfirmationEvent extends AbstractCommerceUserEvent
{
	private String firstName;
	private String lastName;
	private String orderStatus;

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *           the orderStatus to set
	 */
	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}




}
