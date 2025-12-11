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

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTGTINValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;

	public JnJGTProductService getJnjGTProductService() {
		return jnjGTProductService;
	}

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

		// Get Delivery GTIN
		final JnjGTVariantProductModel variantModel = jnjGTProductService.getDeliveryGTIN(naProductModel);

		if (variantModel != null && variantModel.getUnit() != null)
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			fieldValues.addAll(createFieldValue(variantModel, indexedProperty));
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(final JnjGTVariantProductModel variantModel, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final StringBuilder value = new StringBuilder();

		if (StringUtils.isNotBlank(variantModel.getEan()))
		{
			value.append(variantModel.getEan());
		}

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
