/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.data;

import de.hybris.platform.commercefacades.order.data.CartModificationData;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the data class for add to cart.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjCartModificationData
{

	private List<CartModificationData> cartModifications;
	private List<String> productsWithMaxQty;
	private List<String> invalidProductCodes;
	boolean exceedMaxQty = false;
	boolean showQtyAdjustment = false;
	boolean showEachGTINMsg = false;
	private int totalUnitCount;

	public List<CartModificationData> getCartModifications()
	{
		return cartModifications;
	}
	public boolean isShowQtyAdjustment()

	{

	return showQtyAdjustment;

	}
	public void setShowQtyAdjustment(final boolean showQtyAdjustment)

	{
	this.showQtyAdjustment = showQtyAdjustment;

	}


	public void setCartModifications(final List<CartModificationData> cartModifications)
	{
		this.cartModifications = cartModifications;
	}

	public boolean isExceedMaxQty()
	{
		return exceedMaxQty;
	}

	/**
	 * @return the totalUnitCount
	 */
	public int getTotalUnitCount()
	{
		return totalUnitCount;
	}

	/**
	 * @param totalUnitCount
	 *           the totalUnitCount to set
	 */
	public void setTotalUnitCount(final int totalUnitCount)
	{
		this.totalUnitCount = totalUnitCount;
	}

	public void setExceedMaxQty(final boolean exceedMaxQty)
	{
		this.exceedMaxQty = exceedMaxQty;
	}

	public List<String> getProductsWithMaxQty()
	{
		if (null == productsWithMaxQty)
		{
			productsWithMaxQty = new ArrayList<String>();
		}
		return productsWithMaxQty;
	}

	public void setProductsWithMaxQty(final List<String> productsWithMaxQty)
	{
		this.productsWithMaxQty = productsWithMaxQty;
	}

	public List<String> getInvalidProductCodes()
	{
		if (null == invalidProductCodes)
		{
			invalidProductCodes = new ArrayList<String>();
		}
		return invalidProductCodes;
	}

	public void setInvalidProductCodes(final List<String> invalidProductCodes)
	{
		this.invalidProductCodes = invalidProductCodes;
	}

	/**
	 * @return the showEachGTINMsg
	 */
	public boolean isShowEachGTINMsg() {
		return showEachGTINMsg;
	}

	/**
	 * @param showEachGTINMsg the showEachGTINMsg to set
	 */
	public void setShowEachGTINMsg(boolean showEachGTINMsg) {
		this.showEachGTINMsg = showEachGTINMsg;
	}
	
}
