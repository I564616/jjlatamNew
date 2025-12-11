/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPrimaryImagePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.jnj.core.model.JnJProductModel;


/**
 * Populate the product data with the product's primary image. It checks if product has materialBaseProduct as not null
 * fetch images from materialBaseProduct.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTProductPrimaryImagePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductPrimaryImagePopulator<SOURCE, TARGET>
{
	@Override
	public void populate(SOURCE productModel, final TARGET productData) throws ConversionException
	{
		if (productModel instanceof JnJProductModel)
		{
			final JnJProductModel naProductModel = (JnJProductModel) productModel;

			// If materialBaseProduct is null, index the product code
			if (naProductModel.getMaterialBaseProduct() != null)
			{
				productModel = (SOURCE) naProductModel.getMaterialBaseProduct();
			}
		}
		super.populate(productModel, productData);
	}

}
