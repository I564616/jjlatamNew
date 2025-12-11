/**
 * 
 */
package com.jnj.b2b.loginaddon.forms;

import java.util.List;

import com.jnj.facades.data.SecretQuestionData;


/**
 * @author ujjwal.negi
 * 
 */
public class JnjGTUserActivationForm
{
	protected String phone;
	protected String mobile;
	protected String fax;
	protected String country;
	protected String addressLine1;
	protected String addressLine2;
	protected String city;
	protected String state;
	protected String zip;
	protected List<SecretQuestionData> secretQuestionsAnswers;

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile()
	{
		return mobile;
	}

	/**
	 * @param mobile
	 *           the mobile to set
	 */
	public void setMobile(final String mobile)
	{
		this.mobile = mobile;
	}

	/**
	 * @return the fax
	 */
	public String getFax()
	{
		return fax;
	}

	/**
	 * @param fax
	 *           the fax to set
	 */
	public void setFax(final String fax)
	{
		this.fax = fax;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *           the addressLine1 to set
	 */
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *           the addressLine2 to set
	 */
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
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
	 * @return the zip
	 */
	public String getZip()
	{
		return zip;
	}

	/**
	 * @param zip
	 *           the zip to set
	 */
	public void setZip(final String zip)
	{
		this.zip = zip;
	}

	/**
	 * @return the secretQuestionsAnswers
	 */
	public List<SecretQuestionData> getSecretQuestionsAnswers()
	{
		return secretQuestionsAnswers;
	}

	/**
	 * @param secretQuestionsAnswers
	 *           the secretQuestionsAnswers to set
	 */
	public void setSecretQuestionsAnswers(final List<SecretQuestionData> secretQuestionsAnswers)
	{
		this.secretQuestionsAnswers = secretQuestionsAnswers;
	}


}
