/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.core.checkout.flow.impl;


import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;

import com.jnj.b2b.core.checkout.flow.B2BCheckoutFlowStrategy;


/**
 * 
 * Uses fixed {@link B2BCheckoutFlowEnum} as result. Used most likely on the end of checkout flow strategy chain.
 * 
 * @Deprecated use
 *             {@link de.hybris.platform.b2bacceleratorservices.order.checkout.flow.impl.FixedB2BCheckoutFlowStrategy}
 * @since 4.6
 */
@Deprecated
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
