package com.jnj.b2b.cartandcheckoutaddon.forms;

/**
 * Global class for all jnjb2bcore constants
 * 
 * @author Accenture
 * @version 1.0
 */


public class OneTimeShippingAddForm
{
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postalCode;
	private String attnLine; //AAOM-6890

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1()
	{
		return address1;
	}

	/**
	 * @param address1
	 *           the address1 to set
	 */
	public void setAddress1(final String address1)
	{
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2()
	{
		return address2;
	}

	/**
	 * @param address2
	 *           the address2 to set
	 */
	public void setAddress2(final String address2)
	{
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param postalCode
	 *           the postalCode to set
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}
	
	public String getAttnLine() {
		return attnLine;
	}

	public void setAttnLine(String attnLine) {
		this.attnLine = attnLine;
	}

}
