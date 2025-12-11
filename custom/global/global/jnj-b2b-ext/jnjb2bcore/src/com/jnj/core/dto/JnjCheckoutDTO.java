/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;



/**
 * Pojo for 'Checkout' form.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCheckoutDTO
{
	private String poOrderNumber;
	private boolean salesForbidden;
	private boolean completOrder;
	private boolean deliveryAddressId;

	public String getPoOrderNumber()
	{
		return poOrderNumber;
	}

	public void setPoOrderNumber(final String poOrderNumber)
	{
		this.poOrderNumber = poOrderNumber;
	}

	public boolean isSalesForbidden()
	{
		return salesForbidden;
	}

	public void setSalesForbidden(final boolean salesForbidden)
	{
		this.salesForbidden = salesForbidden;
	}

	public boolean isCompletOrder()
	{
		return completOrder;
	}

	public void setCompletOrder(final boolean completOrder)
	{
		this.completOrder = completOrder;
	}

	public boolean isDeliveryAddressId()
	{
		return deliveryAddressId;
	}

	public void setDeliveryAddressId(final boolean deliveryAddressId)
	{
		this.deliveryAddressId = deliveryAddressId;
	}

}
