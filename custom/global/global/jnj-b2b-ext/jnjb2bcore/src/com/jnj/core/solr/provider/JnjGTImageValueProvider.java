/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ImageValueProvider;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.Collection;

import com.jnj.core.model.JnJProductModel;


/**
 * This is a Image Value Provider. It checks if product has materialBaseProduct as not null fetch images from
 * materialBaseProduct.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTImageValueProvider extends ImageValueProvider
{
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, Object model)
			throws FieldValueProviderException
	{
		if (model instanceof JnJProductModel)
		{
			final JnJProductModel naProductModel = (JnJProductModel) model;

			// If materialBaseProduct is null, index the product code
			if (naProductModel.getMaterialBaseProduct() != null)
			{
				model = naProductModel.getMaterialBaseProduct();
			}
		}
		return super.getFieldValues(indexConfig, indexedProperty, model);
	}
}
