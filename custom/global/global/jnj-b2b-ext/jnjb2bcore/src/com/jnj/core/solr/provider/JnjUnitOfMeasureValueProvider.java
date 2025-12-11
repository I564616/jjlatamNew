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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;


/**
 * To index Unit of Measure
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjUnitOfMeasureValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{

	@Autowired
	protected FieldNameProvider fieldNameProvider;
	@Autowired
	protected CommonI18NService commonI18NService;

	
	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
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
			String deliveryUnit = null;
			String salesUnit = null;
			String unitOfMeasure = null;
			final String deliminator = "_";
			if (indexedProperty.isLocalized())
			{
				for (final LanguageModel language : indexConfig.getLanguages())
				{
					final Locale locale = this.i18nService.getCurrentLocale();
					try
					{
						this.i18nService.setCurrentLocale(commonI18NService.getLocaleForLanguage(language));
						//DeliveryUnit
						if (null != productModel.getDeliveryUnitOfMeasure())
						{
							deliveryUnit = productModel.getDeliveryUnitOfMeasure().getName();
						}
						else
						{
							deliveryUnit = "pieces";
						}
						//Sales Unit
						if (null != productModel.getUnit())
						{
							salesUnit = productModel.getUnit().getName();
						}

						else
						{
							deliveryUnit = "pieces";
						}
						final int saleItemQuantity = getSaleItems(productModel);
						//unitOfMeasure pattern  is - salesUnit_saleItemQuantity_deliveryUnit
						unitOfMeasure = salesUnit + deliminator + saleItemQuantity + deliminator + deliveryUnit;
						fieldValues.addAll(createFieldValue(unitOfMeasure, indexedProperty, language));

					}
					finally
					{
						this.i18nService.setCurrentLocale(locale);
					}
				}


			}
			else
			{
				fieldValues.addAll(createFieldValue(unitOfMeasure, indexedProperty, null));
			}

			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate units for non-JnJProductModel item");
		}


	}

	/**
	 * This is used to get number of delivered units
	 * 
	 * @param productModel
	 * @return int
	 */
	protected int getSaleItems(final JnJProductModel productModel)
	{
		try
		{
			final int numeratorDUOM = Integer.parseInt(productModel.getNumeratorDUOM());
			final int numeratorSUOM = Integer.parseInt(productModel.getNumeratorSUOM());
			return numeratorDUOM / numeratorSUOM;
		}
		catch (final NumberFormatException exception)
		{
			return 1;
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
	protected List<FieldValue> createFieldValue(final String value, final IndexedProperty indexedProperty,
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
