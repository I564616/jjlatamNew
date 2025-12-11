/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jnj.core.model.JnJProductModel;


/**
 * This is a Image Available Indicator Value Provider. It checks if product has materialBaseProduct as not null fetch
 * number from materialBaseProduct.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTImageAvlIndValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final JnJProductModel naProductModel = getGTProductModel(model);
		if (naProductModel == null)
		{
			return Collections.emptyList();
		}

		final Integer imageAvailableInd;

		// If materialBaseProduct is null, index the product code
		if (naProductModel.getMaterialBaseProduct() == null)
		{
			imageAvailableInd = naProductModel.getImageAvailableInd();
		}
		// If materialBaseProduct is not null, index the materialBaseProduct's code
		else
		{
			imageAvailableInd = naProductModel.getMaterialBaseProduct().getImageAvailableInd();
		}
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

		fieldValues.addAll(createFieldValue(imageAvailableInd, indexedProperty));

		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final Integer value, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	protected JnJProductModel getGTProductModel(final Object model)
	{

		if (model instanceof JnJProductModel)
		{
			return (JnJProductModel) model;
		}

		else
		{
			return null;
		}
	}

	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
