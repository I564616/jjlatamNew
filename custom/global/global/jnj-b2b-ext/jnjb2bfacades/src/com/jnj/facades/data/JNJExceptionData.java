package com.jnj.facades.data;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.io.Serializable;


/**
 * @author 711552
 * 
 */
public class JNJExceptionData implements Serializable
{
	private String exceptionString;
	private String exceptionTime;
	private CustomerData customerData;

	/**
	 * @return the exceptionString
	 */
	public String getExceptionString()
	{
		return exceptionString;
	}

	/**
	 * @param exceptionString
	 *           the exceptionString to set
	 */
	public void setExceptionString(final String exceptionString)
	{
		this.exceptionString = exceptionString;
	}

	/**
	 * @return the exceptionTime
	 */
	public String getExceptionTime()
	{
		return exceptionTime;
	}

	/**
	 * @param exceptionTime
	 *           the exceptionTime to set
	 */
	public void setExceptionTime(final String exceptionTime)
	{
		this.exceptionTime = exceptionTime;
	}

	/**
	 * @return the customerData
	 */
	public CustomerData getCustomerData()
	{
		return customerData;
	}

	/**
	 * @param customerData
	 *           the customerData to set
	 */
	public void setCustomerData(final CustomerData customerData)
	{
		this.customerData = customerData;
	}
}
