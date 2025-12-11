/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import com.jnj.facades.data.JnjAddressData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjAddressPopulator extends AddressPopulator
{

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		super.populate(source, target);
		if (target instanceof JnjAddressData)
		{
			final JnjAddressData jnjAddressData = (JnjAddressData) target;
			jnjAddressData.setJnjAddressId(source.getJnJAddressId());
			jnjAddressData.setPhone2(source.getPhone2());
		}
	}

}
