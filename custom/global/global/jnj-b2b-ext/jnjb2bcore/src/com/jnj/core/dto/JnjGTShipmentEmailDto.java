/**
 * 
 */
package com.jnj.core.dto;

/**
 * @author MKhan6
 * 
 *         POJO class responsible to hold Shipment Email Notification data.
 *
 */
public class JnjGTShipmentEmailDto
{

	/**
	 * SAP based order number.
	 */
	private String orderNumber;

	/**
	 * hybris order number
	 */
	private String hybrisOrder;

	/**
	 * @return the hybrisOrder
	 */
	public String getHybrisOrder()
	{
		return hybrisOrder;
	}

	/**
	 * @param hybrisOrder
	 *           the hybrisOrder to set
	 */
	public void setHybrisOrder(String hybrisOrder)
	{
		this.hybrisOrder = hybrisOrder;
	}

	/**
	 * Line level present product name.
	 */
	private String productName;

	/**
	 * Line level present product code.
	 */
	private String productCode;

	/**
	 * Order Type.
	 *
	 */
	private String orderType;

	private String billingName;

	private String billingAddress;

	private String shippingAddress;

	private String orderChannel;

	private String gtin;

	private String shipUOM;

	private String eachUOM;

	private String eachDefinition;

	private String shippingMethod;

	private String trackingNumber;

	private String bolNumber;

	/**
	 * @return the bolNumber
	 */
	public String getBolNumber()
	{
		return bolNumber;
	}

	/**
	 * @param bolNumber
	 *           the bolNumber to set
	 */
	public void setBolNumber(String bolNumber)
	{
		this.bolNumber = bolNumber;
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
	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	/**
	 * @return the billingName
	 */
	public String getBillingName()
	{
		return billingName;
	}

	/**
	 * @param billingName
	 *           the billingName to set
	 */
	public void setBillingName(String billingName)
	{
		this.billingName = billingName;
	}

	/**
	 * @return the billingAddress
	 */
	public String getBillingAddress()
	{
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *           the billingAddress to set
	 */
	public void setBillingAddress(String billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAddress
	 */
	public String getShippingAddress()
	{
		return shippingAddress;
	}

	/**
	 * @param shippingAddress
	 *           the shippingAddress to set
	 */
	public void setShippingAddress(String shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @return the orderChannel
	 */
	public String getOrderChannel()
	{
		return orderChannel;
	}

	/**
	 * @param orderChannel
	 *           the orderChannel to set
	 */
	public void setOrderChannel(String orderChannel)
	{
		this.orderChannel = orderChannel;
	}

	/**
	 * @return the gtin
	 */
	public String getGtin()
	{
		return gtin;
	}

	/**
	 * @param gtin
	 *           the gtin to set
	 */
	public void setGtin(String gtin)
	{
		this.gtin = gtin;
	}

	/**
	 * @return the shipUOM
	 */
	public String getShipUOM()
	{
		return shipUOM;
	}

	/**
	 * @param shipUOM
	 *           the shipUOM to set
	 */
	public void setShipUOM(String shipUOM)
	{
		this.shipUOM = shipUOM;
	}

	/**
	 * @return the eachUOM
	 */
	public String getEachUOM()
	{
		return eachUOM;
	}

	/**
	 * @param eachUOM
	 *           the eachUOM to set
	 */
	public void setEachUOM(String eachUOM)
	{
		this.eachUOM = eachUOM;
	}

	/**
	 * @return the eachDefinition
	 */
	public String getEachDefinition()
	{
		return eachDefinition;
	}

	/**
	 * @param eachDefinition
	 *           the eachDefinition to set
	 */
	public void setEachDefinition(String eachDefinition)
	{
		this.eachDefinition = eachDefinition;
	}

	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod()
	{
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *           the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod)
	{
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the trackingNumber
	 */
	public String getTrackingNumber()
	{
		return trackingNumber;
	}

	/**
	 * @param trackingNumber
	 *           the trackingNumber to set
	 */
	public void setTrackingNumber(String trackingNumber)
	{
		this.trackingNumber = trackingNumber;
	}

	/*
	 * 
	 * /** Order Date
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
