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
package com.jnj.b2b.core.checkout.pci.impl;

import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;

import com.jnj.b2b.core.checkout.pci.B2BCheckoutPciStrategy;


/**
 * Uses fixed {@link B2BCheckoutPciOptionEnum} as result. Used most likely on the end of checkout PCI option strategy
 * chain.
 * 
 * @Deprecated see {@link de.hybris.platform.b2bacceleratorservices.order.checkout.pci.impl.FixedB2BCheckoutPciStrategy}
 */
@Deprecated
public class FixedB2BCheckoutPciStrategy implements B2BCheckoutPciStrategy
{
	private CheckoutPciOptionEnum subscriptionPciOption;

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return this.subscriptionPciOption;
	}

	public void setSubscriptionPciOption(final CheckoutPciOptionEnum subscriptionPciOption)
	{
		this.subscriptionPciOption = subscriptionPciOption;
	}
}
