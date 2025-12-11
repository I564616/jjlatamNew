/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

/**
 * Form for validating update field on cart page.
 */
public class JnjAddToCartForm
{
	private String[] productCodeAndQty;

	public String[] getProductCodeAndQty()
	{
		return productCodeAndQty;
	}

	public void setProductCodeAndQty(final String[] productCodeAndQty)
	{
		this.productCodeAndQty = productCodeAndQty;
	}

}
