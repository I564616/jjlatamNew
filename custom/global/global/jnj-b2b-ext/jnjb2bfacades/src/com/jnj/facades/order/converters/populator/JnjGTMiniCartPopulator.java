/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;


import de.hybris.platform.commercefacades.order.converters.populator.MiniCartPopulator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import org.apache.commons.collections4.CollectionUtils;


/**
 * @author Accenture
 * @version 1.0
 */
public class JnjGTMiniCartPopulator extends MiniCartPopulator
{
	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		if (null != source && CollectionUtils.isNotEmpty(source.getEntries()))
		{
			JnJGTCartPopulatorHelper.populateCartTotalWeightNVolume(source, target);
		}
	}
}
