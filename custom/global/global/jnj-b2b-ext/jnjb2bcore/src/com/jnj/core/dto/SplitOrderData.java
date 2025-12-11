/**
 * 
 */
package com.jnj.core.dto;


/**
 * The SpiltOrderData class contains the information related to split order fields.
 * 
 * NOTE: Equals and HashCode method of this class have been over ridden.
 * 
 * @author sumit.y.kumar
 * 
 */
public class SplitOrderData
{
	private String salesOrg;
	private String orderType;
	private String forbiddenSales;

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
	 * @return the orderType
	 */
	public String getOrderType()
	{
		return orderType;
	}

	/**
	 * @param orderType
	 *           the orderType to set
	 */
	public void setOrderType(final String orderType)
	{
		this.orderType = orderType;
	}

	/**
	 * @return the forbiddenSales
	 */
	public String getForbiddenSales()
	{
		return forbiddenSales;
	}

	/**
	 * @param forbiddenSales
	 *           the forbiddenSales to set
	 */
	public void setForbiddenSales(final String forbiddenSales)
	{
		this.forbiddenSales = forbiddenSales;
	}


	/**
	 * The equals method is over ridden with sales org and order type field of SplitOrderData class.
	 */
	@Override
	public boolean equals(final Object object)
	{
		boolean result = false;
		if (object == null || object.getClass() != getClass())
		{
			result = false;
		}
		else
		{
			final SplitOrderData splitOrderData = (SplitOrderData) object;
			if (this.salesOrg.equals(splitOrderData.getSalesOrg()) && this.orderType.equals(splitOrderData.getOrderType()))
			{
				result = true;
			}
		}
		return result;
	}

	/**
	 * The hashCode method is over ridden with sales org and order type field of SplitOrderData class.
	 */
	@Override
	/*
	 * public int hashCode() { int hash = 3; hash = 7 * hash + this.salesOrg.hashCode(); hash = 7 * hash +
	 * this.orderType.hashCode(); return hash; }
	 */
	public int hashCode()
	{
		final int hash = this.salesOrg.hashCode() * this.orderType.hashCode();
		return hash;
	}
}
