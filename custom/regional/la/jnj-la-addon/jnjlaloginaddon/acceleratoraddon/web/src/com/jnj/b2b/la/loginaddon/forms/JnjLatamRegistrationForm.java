/**
 *
 */
package com.jnj.b2b.la.loginaddon.forms;

import com.jnj.loginaddon.forms.JnjGTRegistrationForm;

/**
 * @author skant3
 *
 */
public class JnjLatamRegistrationForm extends JnjGTRegistrationForm
{
	private String customerCode;
	private String soldTo;
	private String jnjAccountManager;
	private Boolean commercialUserFlag;
	private String commercialUserSector;	

	public Boolean getCommercialUserFlag() {
		return commercialUserFlag;
	}

	public void setCommercialUserFlag(Boolean commercialUserFlag) {
		this.commercialUserFlag = commercialUserFlag;
	}

	public String getCommercialUserSector() {
		return commercialUserSector;
	}

	public void setCommercialUserSector(String commercialUserSector) {
		this.commercialUserSector = commercialUserSector;
	}

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode()
	{
		return customerCode;
	}

	/**
	 * @param customerCode
	 *           the customerCode to set
	 */
	public void setCustomerCode(final String customerCode)
	{
		this.customerCode = customerCode;
	}

	/**
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return soldTo;
	}

	/**
	 * @param soldTo
	 *           the soldTo to set
	 */
	public void setSoldTo(final String soldTo)
	{
		this.soldTo = soldTo;
	}

	/**
	 * @return the jnjAccountManager
	 */
	public String getJnjAccountManager()
	{
		return jnjAccountManager;
	}

	/**
	 * @param jnjAccountManager
	 *           the jnjAccountManager to set
	 */
	public void setJnjAccountManager(final String jnjAccountManager)
	{
		this.jnjAccountManager = jnjAccountManager;
	}

}
