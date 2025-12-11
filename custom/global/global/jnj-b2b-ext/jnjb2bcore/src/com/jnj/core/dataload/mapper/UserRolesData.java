/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataload.mapper;

/**
 * The UserRolesData class contains the different user roles.
 * 
 * @author Accenture sumit.y.kumar
 * @version 1.0
 */
public class UserRolesData
{
	protected boolean indirectCustomer;
	protected boolean expectedPrice;
	protected boolean priceChange;
	protected boolean distributor;

	/**
	 * @return the indirectCustomer
	 */
	public boolean isIndirectCustomer()
	{
		return indirectCustomer;
	}

	/**
	 * @param indirectCustomer
	 *           the indirectCustomer to set
	 */
	public void setIndirectCustomer(final boolean indirectCustomer)
	{
		this.indirectCustomer = indirectCustomer;
	}

	/**
	 * @return the expectedPrice
	 */
	public boolean isExpectedPrice()
	{
		return expectedPrice;
	}

	/**
	 * @param expectedPrice
	 *           the expectedPrice to set
	 */
	public void setExpectedPrice(final boolean expectedPrice)
	{
		this.expectedPrice = expectedPrice;
	}

	/**
	 * @return the priceChange
	 */
	public boolean isPriceChange()
	{
		return priceChange;
	}

	/**
	 * @param priceChange
	 *           the priceChange to set
	 */
	public void setPriceChange(final boolean priceChange)
	{
		this.priceChange = priceChange;
	}

	/**
	 * @return the distributor
	 */
	public boolean isDistributor()
	{
		return distributor;
	}

	/**
	 * @param distributor
	 *           the distributor to set
	 */
	public void setDistributor(final boolean distributor)
	{
		this.distributor = distributor;
	}
}
