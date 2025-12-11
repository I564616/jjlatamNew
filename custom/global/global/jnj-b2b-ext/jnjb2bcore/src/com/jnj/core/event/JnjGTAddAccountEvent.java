/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import com.jnj.core.dto.JnjCompanyInfoData;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTAddAccountEvent extends AbstractCommerceUserEvent
{

	private JnjCompanyInfoData companyData;
	private String siteLogoURL;

	/**
	 * @param companyData
	 */
	public JnjGTAddAccountEvent(final JnjCompanyInfoData companyData)
	{
		super();
		this.companyData = companyData;
	}

	/**
	 * @return the siteLogoURL
	 */
	public String getSiteLogoURL()
	{
		return siteLogoURL;
	}

	/**
	 * @param siteLogoURL
	 *           the siteLogoURL to set
	 */
	public void setSiteLogoURL(final String siteLogoURL)
	{
		this.siteLogoURL = siteLogoURL;
	}

	/**
	 * @return the companyData
	 */
	public JnjCompanyInfoData getCompanyData()
	{
		return companyData;
	}

	/**
	 * @param companyData
	 *           the companyData to set
	 */
	public void setCompanyData(final JnjCompanyInfoData companyData)
	{
		this.companyData = companyData;
	}


}
