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
package com.jnj.core.checkout.flow.impl;

import com.jnj.core.checkout.flow.B2BCheckoutFlowStrategy;



import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;


/**
 * 
 * Uses fixed {@link B2BCheckoutFlowEnum} as result. Used most likely on the end of checkout flow strategy chain.
 * 
 * @since 4.6
 */
public class FixedB2BCheckoutFlowStrategy implements B2BCheckoutFlowStrategy
{
	private CheckoutFlowEnum checkoutFlow;

	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return checkoutFlow;
	}

	public void setCheckoutFlow(final CheckoutFlowEnum checkoutFlow)
	{
		this.checkoutFlow = checkoutFlow;
	}
}
