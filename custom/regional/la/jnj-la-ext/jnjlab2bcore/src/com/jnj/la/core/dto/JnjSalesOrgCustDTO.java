package com.jnj.la.core.dto;

public class JnjSalesOrgCustDTO
{

	private String customerNum;
	private String salesOrg;
	private String sector;
	private String lastUpdatedDate;

	/**
	 * @return the customerNum
	 */
	public String getCustomerNum()
	{
		return customerNum;
	}

	/**
	 * @param customerNum
	 *           the customerNum to set
	 */
	public void setCustomerNum(final String customerNum)
	{
		this.customerNum = customerNum;
	}

	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg()
	{
		return salesOrg;
	}

	/**
	 * @param salesOrg
	 *           the salesOrg to set
	 */
	public void setSalesOrg(final String salesOrg)
	{
		this.salesOrg = salesOrg;
	}

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate
	 *           the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(final String lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}
}
