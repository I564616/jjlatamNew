/**
 * 
 */
package com.jnj.core.dto;

/**
 * POJO class responsible to hold Shipped-Order Email Notification data.
 * 
 */
public class JnjGTShippedorderEmailDto
{
	/**
	 * SAP based order number.
	 */
	private String orderNumber;

	/**
	 * Line level present product name.
	 */
	private String productName;

	/**
	 * Line level present product code.
	 */
	private String productCode;

	/**
	 * Operating company.
	 *
	 */
	/* below fields added for JJEPIC -825 for backorder email notification */
	private String operatingCompany;

	/**
	 * Order Date
	 */
	private String orderDate;

	/**
	 * @return the orderDate
	 */
	public String getOrderDate()
	{
		return orderDate;
	}

	/**
	 * @param orderDate
	 *           the orderDate to set
	 */
	public void setOrderDate(String orderDate)
	{
		this.orderDate = orderDate;
	}

	/**
	 * @return the operatingCompany
	 */
	public String getOperatingCompany()
	{
		return operatingCompany;
	}

	/**
	 * @param operatingCompany
	 *           the operatingCompany to set
	 */
	public void setOperatingCompany(final String operatingCompany)
	{
		this.operatingCompany = operatingCompany;
	}

	/**
	 * @return the customerPO
	 */
	public String getCustomerPO()
	{
		return customerPO;
	}

	/**
	 * @param customerPO
	 *           the customerPO to set
	 */
	public void setCustomerPO(final String customerPO)
	{
		this.customerPO = customerPO;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * @return the itemPrice
	 */
	public String getItemPrice()
	{
		return itemPrice;
	}

	/**
	 * @param itemPrice
	 *           the itemPrice to set
	 */
	public void setItemPrice(final String itemPrice)
	{
		this.itemPrice = itemPrice;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *           the accountNumber to set
	 */
	public void setAccountNumber(final String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the extendedPrice
	 */
	public String getExtendedPrice()
	{
		return extendedPrice;
	}

	/**
	 * @param extendedPrice
	 *           the extendedPrice to set
	 */
	public void setExtendedPrice(final String extendedPrice)
	{
		this.extendedPrice = extendedPrice;
	}

	/**
	 * @return the shipDate
	 */
	public String getShipDate()
	{
		return shipDate;
	}

	/**
	 * @param shipDate
	 *           the shipDate to set
	 */
	public void setShipDate(final String shipDate)
	{
		this.shipDate = shipDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate()
	{
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *           the deliveryDate to set
	 */
	public void setDeliveryDate(final String deliveryDate)
	{
		this.deliveryDate = deliveryDate;
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
	 * @return the additionComments
	 */
	public String getAdditionComments()
	{
		return additionComments;
	}

	/**
	 * @param additionComments
	 *           the additionComments to set
	 */
	public void setAdditionComments(final String additionComments)
	{
		this.additionComments = additionComments;
	}

	/**
	 * Customer PO.
	 */
	private String customerPO;



	/**
	 * unit.
	 */
	private String unit;

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
	 * quantity.
	 */
	private String quantity;

	/**
	 * Item Price.
	 */
	private String itemPrice;
	/**
	 * Account Number.
	 */
	private String accountNumber;
	/**
	 * Extended Price.
	 */
	private String extendedPrice;

	/**
	 * shipDate.
	 */
	private String shipDate;


	/**
	 * Delivery Date.
	 */
	private String deliveryDate;

	/**
	 * Status.
	 */
	private String status;
	/**
	 * additionalComments.
	 */
	private String additionComments;
	/*
	 * Products' available Backorder date.
	 */
	private String availabilityDate;

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	public void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the productName
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * @param productName
	 *           the productName to set
	 */
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the availabilityDate
	 */
	public String getAvailabilityDate()
	{
		return availabilityDate;
	}

	/**
	 * @param availabilityDate
	 *           the availabilityDate to set
	 */
	public void setAvailabilityDate(final String availabilityDate)
	{
		this.availabilityDate = availabilityDate;
	}
}
