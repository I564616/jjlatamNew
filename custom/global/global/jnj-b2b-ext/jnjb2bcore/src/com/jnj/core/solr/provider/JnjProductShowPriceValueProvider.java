/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;


/**
 * To index show price
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjProductShowPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{

	@Autowired
	protected FieldNameProvider fieldNameProvider;

	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof JnJProductModel)
		{
			final JnJProductModel productModel = (JnJProductModel) model;
			final Collection fieldValues = new ArrayList();
			final Collection<CategoryModel> categories = productModel.getSupercategories();
			Boolean showPrice = Boolean.TRUE;
			if (CollectionUtils.isNotEmpty(categories))
			{
				for (final CategoryModel category : categories)
				{
					if (null != category.getShowProductPrice())
					{
						showPrice = category.getShowProductPrice();
						break;
					}

				}

			}

			fieldValues.addAll(createFieldValue(showPrice, indexedProperty, null));

			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate units for non-JnJProductModel item");
		}


	}



	/**
	 * Creates the field value.
	 * 
	 * @param value
	 *           {@link String}
	 * @param indexedProperty
	 *           {@link IndexedProperty}
	 * @param language
	 *           {@link LanguageModel}
	 * @return the list {@link List}
	 */
	protected List<FieldValue> createFieldValue(final Boolean value, final IndexedProperty indexedProperty,
			final LanguageModel language)
	{
		final List fieldValues = new ArrayList();
		final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, (language == null) ? null
				: language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}




}
