package com.jnj.la.core.dto;

public class JnjProductSalesOrgDTO
{

	private String deliveryUnit;

	private String salesUnitOfMeasure;

	private String unit;

	private String coldChainProduct;

	private String salesOrganization;

	private String status;

	private Boolean ecommerceFlag;

	/**
	 * @return the deliveryUnit
	 */
	public String getDeliveryUnit()
	{
		return deliveryUnit;
	}

	/**
	 * @param deliveryUnit
	 *           the deliveryUnit to set
	 */
	public void setDeliveryUnit(final String deliveryUnit)
	{
		this.deliveryUnit = deliveryUnit;
	}

	/**
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *           the unit to set
	 */
	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	/**
	 * @return the coldChainProduct
	 */
	public String getColdChainProduct()
	{
		return coldChainProduct;
	}

	/**
	 * @param coldChainProduct
	 *           the coldChainProduct to set
	 */
	public void setColdChainProduct(final String coldChainProduct)
	{
		this.coldChainProduct = coldChainProduct;
	}

	/**
	 * @return the salesOrganization
	 */
	public String getSalesOrganization()
	{
		return salesOrganization;
	}

	/**
	 * @param salesOrganization
	 *           the salesOrganization to set
	 */
	public void setSalesOrganization(final String salesOrganization)
	{
		this.salesOrganization = salesOrganization;
	}

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
	 * @return the ecommerceFlag
	 */
	public Boolean getEcommerceFlag()
	{
		return ecommerceFlag;
	}

	/**
	 * @param ecommerceFlag
	 *           the ecommerceFlag to set
	 */
	public void setEcommerceFlag(final Boolean ecommerceFlag)
	{
		this.ecommerceFlag = ecommerceFlag;
	}

	/**
	 * @return the salesUnitOfMeasure
	 */
	public String getSalesUnitOfMeasure()
	{
		return salesUnitOfMeasure;
	}

	/**
	 * @param salesUnitOfMeasure
	 *           the salesUnitOfMeasure to set
	 */
	public void setSalesUnitOfMeasure(final String salesUnitOfMeasure)
	{
		this.salesUnitOfMeasure = salesUnitOfMeasure;
	}
}