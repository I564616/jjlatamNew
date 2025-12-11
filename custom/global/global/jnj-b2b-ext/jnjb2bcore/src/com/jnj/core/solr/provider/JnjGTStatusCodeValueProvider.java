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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJProductModel;


/**
 * This is the handler used to populate launch status for active products
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTStatusCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	/**
	 * Private instance of <code>JnJGTProductService</code>
	 */
	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	/** The field name provider. */
	protected FieldNameProvider fieldNameProvider;

	/** The flag that indicates do we need to index only the active products. */
	protected Boolean indexOnlyActive;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final JnJProductModel naProductModel = getGTProductModel(model);
		if (naProductModel == null)
		{
			return Collections.emptyList();
		}

		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final String status = jnJGTProductService.getLaunchStatus(naProductModel, indexOnlyActive);

		if (StringUtils.isNotBlank(status))
		{
			fieldValues.addAll(createFieldValue(status, indexedProperty));
		}
		return fieldValues;
	}

	/**
	 * Creates the field value.
	 * 
	 * @param status
	 *           the status
	 * @param indexedProperty
	 *           the indexed property
	 * @return the list
	 */
	protected List<FieldValue> createFieldValue(final String status, final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();

		final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, status));
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

	/**
	 * @param indexOnlyActive
	 *           the indexOnlyActive to set
	 */
	public void setIndexOnlyActive(final Boolean indexOnlyActive)
	{
		this.indexOnlyActive = indexOnlyActive;
	}
}
