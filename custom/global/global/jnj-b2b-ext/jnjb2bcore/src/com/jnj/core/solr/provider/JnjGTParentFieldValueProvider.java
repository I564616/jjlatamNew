/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
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
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;


/**
 * This is a Parent Value Provider. It checks if product has materialBaseProduct as not null fetch field value from
 * materialBaseProduct.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTParentFieldValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	@Autowired
	private CommonI18NService commonI18NService;

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final JnJProductModel productModel = getGTProductModel(model);
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		String propertyValue;
		if (indexedProperty.isLocalized())
		{
			for (final LanguageModel language : indexConfig.getLanguages())
			{
				final Locale locale = this.i18nService.getCurrentLocale();
				try
				{
					i18nService.setCurrentLocale(commonI18NService.getLocaleForLanguage(language));
					propertyValue = getPropertyValue(productModel, indexedProperty);
					fieldValues.addAll(createFieldValue(propertyValue, indexedProperty, language));
				}
				finally
				{
					this.i18nService.setCurrentLocale(locale);
				}
			}
		}
		else
		{
			propertyValue = getPropertyValue(productModel, indexedProperty);
			fieldValues.addAll(createFieldValue(propertyValue, indexedProperty, null));
		}

		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final String value, final IndexedProperty indexedProperty,
			final LanguageModel language)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty,
				(language == null) ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	private String getPropertyValue(final JnJProductModel naProductModel, final IndexedProperty indexedProperty)
	{
		Object objectValue = null;
		String propertyValue = null;
		// If materialBaseProduct is null, index the product code
		if (naProductModel.getMaterialBaseProduct() == null)
		{
			objectValue = modelService.getAttributeValue(naProductModel, indexedProperty.getValueProviderParameter());
		}
		// If materialBaseProduct is not null, index the materialBaseProduct's code
		else
		{
			objectValue = modelService.getAttributeValue(naProductModel.getMaterialBaseProduct(),
					indexedProperty.getValueProviderParameter());
		}
		if (objectValue instanceof String)
		{
			propertyValue = (String) objectValue;
		}
		return propertyValue;
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
