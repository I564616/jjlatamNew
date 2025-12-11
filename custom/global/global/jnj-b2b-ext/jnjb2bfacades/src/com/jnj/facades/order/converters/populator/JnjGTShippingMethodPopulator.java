/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.converters.Populator;

import com.jnj.facades.data.JnjGTShippingMethodData;
import com.jnj.core.model.JnjGTShippingMethodModel;


/**
 * @author Accenture
 * @version 1.0
 */

public class JnjGTShippingMethodPopulator implements Populator<JnjGTShippingMethodModel, JnjGTShippingMethodData>
{

	/**
	 * Populate ShippingMethodData From JnjGTShippingMethodModel.
	 * 
	 * @param source
	 *           JnjGTShippingMethodModel
	 * @param target
	 *           JnjShippingMethodData
	 */
	@Override
	public void populate(final JnjGTShippingMethodModel source, final JnjGTShippingMethodData target)
	{
		target.setRoute(source.getRoute());
		target.setExpidateRoute(source.getExpidateRoute());
		target.setRank(source.getRank());
		target.setDispName(source.getDispName());
		target.setSortBy(source.getSortBy());
	}
}