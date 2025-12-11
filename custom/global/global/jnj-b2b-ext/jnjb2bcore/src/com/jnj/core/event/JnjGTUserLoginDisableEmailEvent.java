/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author labanya.saha
 * 
 */
public class JnjGTUserLoginDisableEmailEvent extends AbstractCommerceUserEvent
{


	private String firstName;
	private String email;
	private int daysBeforeDisable;

	public JnjGTUserLoginDisableEmailEvent(final String firstName, final String email, final int daysBeforeDisable)
	{
		this.firstName = firstName;
		this.email = email;
		this.daysBeforeDisable = daysBeforeDisable;

	}


	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

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
	 * @return the daysbeforeDisable
	 */
	public int getDaysBeforeDisable() {
		return daysBeforeDisable;
	}


	/**
	 * @param daysbeforeDisable the daysbeforeDisable to set
	 */
	public void setDaysBeforeDisable(int daysbeforeDisable) {
		this.daysBeforeDisable = daysbeforeDisable;
	}


}
