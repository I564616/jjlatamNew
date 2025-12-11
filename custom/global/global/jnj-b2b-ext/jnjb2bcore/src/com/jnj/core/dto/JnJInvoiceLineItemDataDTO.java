/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * DTO class for line item level objects of Invoices.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJInvoiceLineItemDataDTO
{
	private String itemNumber;
	private String qty;
	private String lotNo;
	private String orderReason;
	private String salesOrderItemNo;
	private String material;


	/**
	 * @return the material
	 */
	public String getMaterial()
	{
		return material;
	}

	/**
	 * @param material
	 *           the material to set
	 */
	public void setMaterial(final String material)
	{
		this.material = material;
	}

	/**
	 * @return the itemNo
	 */
	public String getItemNumber()
	{
		return itemNumber;
	}

	/**
	 * @param itemNumber
	 *           the itemNo to set
	 */
	public void setItemNumber(final String itemNumber)
	{
		this.itemNumber = itemNumber;
	}

	/**
	 * @return the qty
	 */
	public String getQty()
	{
		return qty;
	}

	/**
	 * @param qty
	 *           the qty to set
	 */
	public void setQty(final String qty)
	{
		this.qty = qty;
	}

	/**
	 * @return the lotNo
	 */
	public String getLotNo()
	{
		return lotNo;
	}

	/**
	 * @param lotNo
	 *           the lotNo to set
	 */
	public void setLotNo(final String lotNo)
	{
		this.lotNo = lotNo;
	}

	/**
	 * @return the orderReason
	 */
	public String getOrderReason()
	{
		return orderReason;
	}

	/**
	 * @param orderReason
	 *           the orderReason to set
	 */
	public void setOrderReason(final String orderReason)
	{
		this.orderReason = orderReason;
	}


	/**
	 * @return the salesOrderItemNo
	 */
	public String getSalesOrderItemNo()
	{
		return salesOrderItemNo;
	}

	/**
	 * @param salesOrderItemNo
	 *           the salesOrderItemNo to set
	 */
	public void setSalesOrderItemNo(final String salesOrderItemNo)
	{
		this.salesOrderItemNo = salesOrderItemNo;
	}

}
