/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.cart.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.acceleratorservices.order.impl.DefaultCartServiceForAccelerator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;


/**
 *
 */
public class JnjLatamCartServiceForAccelerator extends DefaultCartServiceForAccelerator
{

	private static Class currentClass = JnjLatamCartServiceForAccelerator.class;

	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final CartModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent)
	{
		final String METHOD_NAME = "Latam addNewEntry()";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.AddToCart.ADD_TO_CART_FROM_ORDER, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				currentClass);
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("order", order);
		checkQuantity(qty, number);
		UnitModel usedUnit = unit;
		if (usedUnit == null)
		{
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.AddToCart.ADD_TO_CART_FROM_ORDER, METHOD_NAME,
					"No unit passed, trying to get product unit", currentClass);
			usedUnit = product.getUnit();
			validateParameterNotNullStandardMessage("usedUnit", usedUnit);
		}

		AbstractOrderEntryModel ret = getAbstractOrderEntryModel(order, product, qty, number, addToPresent, usedUnit);

		if (ret == null)
		{
			ret = getAbstractOrderEntryService().createEntry(entryType, order);
			ret.setQuantity(Long.valueOf(qty));
			ret.setMaterialEntered(product.getCode());
			ret.setProduct(product);
			ret.setUnit(usedUnit);
			addEntryAtPosition(order, ret, number);
		}
		order.setCalculated(Boolean.FALSE);
		return ret;
	}

}
