/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto.order;

/**
 * TODO:<class level comments are missing>.
 *
 * @author mpanda3
 * @version 1.0
 */

public class JnjOrderSchLineDTO
{

	private String confirmedQuantity;

	private String roundedQuantity;

	private String orderNumber;

	private String lineNumber;

	private String orderLineNumber;

	private String deliveryDate;

	private String proofOfDeliveryDate;

	private String carrierExpectedDeliveryDate;

	/**
	 * @return the confirmedQuantity
	 */
	public String getConfirmedQuantity()
	{
		return confirmedQuantity;
	}

	/**
	 * @param confirmedQuantity
	 *           the confirmedQuantity to set
	 */
	public void setConfirmedQuantity(final String confirmedQuantity)
	{
		this.confirmedQuantity = confirmedQuantity;
	}

	/**
	 * @return the roundedQuantity
	 */
	public String getRoundedQuantity()
	{
		return roundedQuantity;
	}

	/**
	 * @param roundedQuantity
	 *           the roundedQuantity to set
	 */
	public void setRoundedQuantity(final String roundedQuantity)
	{
		this.roundedQuantity = roundedQuantity;
	}

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
	 * @return the lineNumber
	 */
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
	 * @return the orderLineNumber
	 */
	public String getOrderLineNumber()
	{
		return orderLineNumber;
	}

	/**
	 * @param orderLineNumber
	 *           the orderLineNumber to set
	 */
	public void setOrderLineNumber(final String orderLineNumber)
	{
		this.orderLineNumber = orderLineNumber;
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

	public String getProofOfDeliveryDate() {
		return proofOfDeliveryDate;
	}

	public void setProofOfDeliveryDate(final String proofOfDeliveryDate) {
		this.proofOfDeliveryDate = proofOfDeliveryDate;
	}

	public String getCarrierExpectedDeliveryDate() {
		return carrierExpectedDeliveryDate;
	}

	public void setCarrierExpectedDeliveryDate(final String carrierExpectedDeliveryDate) {
		this.carrierExpectedDeliveryDate = carrierExpectedDeliveryDate;
	}
}
