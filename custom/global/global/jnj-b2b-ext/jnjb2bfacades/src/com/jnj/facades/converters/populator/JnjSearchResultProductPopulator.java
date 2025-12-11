/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.jnj.facades.data.JnjProductData;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSearchResultProductPopulator extends SearchResultProductPopulator
{
	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		// Pull the values directly from the SearchResult object
		target.setCode(this.<String> getValue(source, "code"));
		target.setName(this.<String> getValue(source, "name"));
		target.setDescription(this.<String> getValue(source, "description"));

		populatePrices(source, target);

		final List<ImageData> images = createImageData(source);
		if (CollectionUtils.isNotEmpty(images))
		{
			target.setImages(images);
		}
		populateUrl(source, target);
		if (target instanceof JnjProductData)
		{
			final JnjProductData jnjProductData = (JnjProductData) target;
			final Boolean discontinue = this.<Boolean> getValue(source, "disContinue");
			final Boolean displayPrice = this.<Boolean> getValue(source, "showPrice");
			if (null != discontinue)
			{
				jnjProductData.setDiscontinue(discontinue);
			}
			if (null != displayPrice)
			{
				jnjProductData.setDisplayPrice(displayPrice);
			}
			setUnits(source, jnjProductData);
		}

	}

	/**
	 * @param source
	 * @param jnjProductData
	 */
	private void setUnits(final SearchResultValueData source, final JnjProductData jnjProductData)
	{
		final String unitOfMeasure = this.<String> getValue(source, "unitOfMeasure");
		if (StringUtils.isNotEmpty(unitOfMeasure))
		{
			final String[] units = unitOfMeasure.split("_");
			//unitOfMeasure pattern  is - salesUnit_saleItemQuantity_deliveryUnit
			if (units.length == 3)
			{
				jnjProductData.setSalesUnit(units[0]);
				jnjProductData.setQuantity(units[1]);
				jnjProductData.setDeliveryUnit(units[2]);
			}
		}

	}
}
