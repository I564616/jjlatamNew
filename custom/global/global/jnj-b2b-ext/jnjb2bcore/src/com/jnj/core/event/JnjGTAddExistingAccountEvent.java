/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;



/**
 * @author sakshi.kashiva
 * 
 */
public class JnjGTAddExistingAccountEvent extends AbstractCommerceUserEvent
{
	private String firstName;
	private String lastName;
	private String email;
	private String[] accountNumbers;


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
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @return the accountNumbers
	 */
	public String[] getAccountNumbers()
	{
		return accountNumbers;
	}

	/**
	 * @param accountNumbers
	 *           the accountNumbers to set
	 */
	public void setAccountNumbers(final String[] accountNumbers)
	{
		this.accountNumbers = accountNumbers;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}




}
