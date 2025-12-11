/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataload.mapper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * The LineItem class is user defined class using for storing the xml data.
 * 
 * @author Accenture sumit.y.kumar
 * @version 1.0
 */
@XmlRootElement(name = "LineItem")
public class LineItem
{
	protected String lineNumber;
	protected String productNumber = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String eanProductNumber = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String customerProductNumber = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String notes = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String quantity;
	protected String expectedPrice = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String expectedDeliveryDate = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String uom = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String customerUOM = Jnjb2bCoreConstants.Order.EMPTY_STRING;

	/**
	 * @return the lineNumber
	 */
	@XmlElement(name = "LineNumber")
	public String getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 *           the lineNumber to set
	 */
	public void setLineNumber(final String lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the productNumber
	 */
	@XmlElement(name = "ProductNumber")
	public String getProductNumber()
	{
		return productNumber;
	}

	/**
	 * @param productNumber
	 *           the productNumber to set
	 */
	public void setProductNumber(final String productNumber)
	{
		this.productNumber = productNumber;
	}

	/**
	 * @return the eanProductNumber
	 */
	@XmlElement(name = "EANProductNumber")
	public String getEanProductNumber()
	{
		return eanProductNumber;
	}

	/**
	 * @param eanProductNumber
	 *           the eanProductNumber to set
	 */
	public void setEanProductNumber(final String eanProductNumber)
	{
		this.eanProductNumber = eanProductNumber;
	}

	/**
	 * @return the customerProductNumber
	 */
	@XmlElement(name = "CustomerProductNumber")
	public String getCustomerProductNumber()
	{
		return customerProductNumber;
	}

	/**
	 * @param customerProductNumber
	 *           the customerProductNumber to set
	 */
	public void setCustomerProductNumber(final String customerProductNumber)
	{
		this.customerProductNumber = customerProductNumber;
	}

	/**
	 * @return the quantity
	 */
	@XmlElement(name = "Quantity")
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
	 * @return the expectedPrice
	 */
	@XmlElement(name = "ExpectedPrice")
	public String getExpectedPrice()
	{
		return expectedPrice;
	}

	/**
	 * @param expectedPrice
	 *           the expectedPrice to set
	 */
	public void setExpectedPrice(final String expectedPrice)
	{
		this.expectedPrice = expectedPrice;
	}

	/**
	 * @return the expectedDeliveryDate
	 */
	@XmlElement(name = "ExpectedDeliveryDate")
	public String getExpectedDeliveryDate()
	{
		return expectedDeliveryDate;
	}

	/**
	 * @param expectedDeliveryDate
	 *           the expectedDeliveryDate to set
	 */
	public void setExpectedDeliveryDate(final String expectedDeliveryDate)
	{
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	/**
	 * @return the uom
	 */
	@XmlElement(name = "UOM")
	public String getUom()
	{
		return uom;
	}

	/**
	 * @param uom
	 *           the uom to set
	 */
	public void setUom(final String uom)
	{
		this.uom = uom;
	}

	/**
	 * @return the customerUOM
	 */
	@XmlElement(name = "CustomerUOM")
	public String getCustomerUOM()
	{
		return customerUOM;
	}

	/**
	 * @param customerUOM
	 *           the customerUOM to set
	 */
	public void setCustomerUOM(final String customerUOM)
	{
		this.customerUOM = customerUOM;
	}

	/**
	 * @return the notes
	 */
	@XmlElement(name = "Notes")
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes
	 *           the notes to set
	 */
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}
}