/**
 * 
 */
package com.jnj.b2b.loginaddon.forms;

import java.util.Map;


/**
 * @author ujjwal.negi
 * 
 */
public class ProfileValidationData
{
	protected String status;
	protected String message;
	protected String typeOfProfile;
	protected Map<String, String> businessTypeDropdown;
	protected Map<String, String> estimatedAmountDropdown;
	protected Map<String, String> purchaseProducts;
	protected Map<String, String> departmentsDropdown;

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}

	/**
	 * @return the businessTypeDropdown
	 */
	public Map<String, String> getBusinessTypeDropdown()
	{
		return businessTypeDropdown;
	}

	/**
	 * @param businessTypeDropdown
	 *           the businessTypeDropdown to set
	 */
	public void setBusinessTypeDropdown(final Map<String, String> businessTypeDropdown)
	{
		this.businessTypeDropdown = businessTypeDropdown;
	}

	/**
	 * @return the estimatedAmountDropdown
	 */
	public Map<String, String> getEstimatedAmountDropdown()
	{
		return estimatedAmountDropdown;
	}

	/**
	 * @param estimatedAmountDropdown
	 *           the estimatedAmountDropdown to set
	 */
	public void setEstimatedAmountDropdown(final Map<String, String> estimatedAmountDropdown)
	{
		this.estimatedAmountDropdown = estimatedAmountDropdown;
	}

	/**
	 * @return the purchaseProducts
	 */
	public Map<String, String> getPurchaseProducts()
	{
		return purchaseProducts;
	}

	/**
	 * @param purchaseProducts
	 *           the purchaseProducts to set
	 */
	public void setPurchaseProducts(final Map<String, String> purchaseProducts)
	{
		this.purchaseProducts = purchaseProducts;
	}

	/**
	 * @return the departmentsDropdown
	 */
	public Map<String, String> getDepartmentsDropdown()
	{
		return departmentsDropdown;
	}

	/**
	 * @param departmentsDropdown
	 *           the departmentsDropdown to set
	 */
	public void setDepartmentsDropdown(final Map<String, String> departmentsDropdown)
	{
		this.departmentsDropdown = departmentsDropdown;
	}

	/**
	 * @return the typeOfProfile
	 */
	public String getTypeOfProfile()
	{
		return typeOfProfile;
	}

	/**
	 * @param typeOfProfile
	 *           the typeOfProfile to set
	 */
	public void setTypeOfProfile(final String typeOfProfile)
	{
		this.typeOfProfile = typeOfProfile;
	}


}
