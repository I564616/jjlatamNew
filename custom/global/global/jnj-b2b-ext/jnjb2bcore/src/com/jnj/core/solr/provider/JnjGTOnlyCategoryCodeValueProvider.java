/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.category.model.CategoryModel;
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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;


/**
 * This is the handler used to export caregories codes
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOnlyCategoryCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	/** The field name provider. */
	@Autowired
	private FieldNameProvider fieldNameProvider;
	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	/** This is to get category level for which subcategories will be indexed.Possible values - are 1 or 2 */
	private Integer categoryLevel;
	/** The categories qualifier. */
	private String categoriesQualifier;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection<CategoryModel> categories = new HashSet<CategoryModel>();
		if (model instanceof JnJProductModel)
		{
			categories.addAll((Collection) modelService.getAttributeValue(model, categoriesQualifier));
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate category code value for non-JnJProductModel item");
		}
		if (CollectionUtils.isNotEmpty(categories))
		{
			final int maxLevel = categoryLevel.intValue();
			final Collection<CategoryModel> allCategories = new HashSet<CategoryModel>();

			final Collection fieldValues = new ArrayList();

			if (maxLevel == 1)
			{
				allCategories.addAll(categories);
			}
			else
			{
				getSuperCategories(categories, allCategories, 1, maxLevel);
			}

			createFieldValueForCategories(indexedProperty, fieldValues, allCategories);
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @param indexedProperty
	 * @param fieldValues
	 * @param categories
	 */
	private void createFieldValueForCategories(final IndexedProperty indexedProperty, final Collection fieldValues,
			final Collection<CategoryModel> categories)
	{
		for (final CategoryModel superCategory : categories)
		{
			fieldValues.addAll(createFieldValue(superCategory, indexedProperty));
		}
	}

	/**
	 * Creates the field value.
	 * 
	 * @param category
	 *           the category
	 * @param indexedProperty
	 *           the indexed property
	 * @return the list
	 */
	private List<FieldValue> createFieldValue(final CategoryModel category, final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final Object value = getPropertyValue(category, "code");
		final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	private void getSuperCategories(final Collection<CategoryModel> categories, final Collection<CategoryModel> allCategories,
			int currentLevel, final int maxLevel)
	{
		// If its level 1 do nothing
		if (currentLevel < maxLevel)
		{
			for (final CategoryModel category : categories)
			{
				final Collection<CategoryModel> superCategories = category.getSupercategories();
				allCategories.addAll(superCategories);
				currentLevel++;
				this.getSuperCategories(superCategories, allCategories, currentLevel, maxLevel);
			}
		}
	}

	/**
	 * Gets the property value.
	 * 
	 * @param model
	 *           the model
	 * @param propertyName
	 *           the property name
	 * @return the property value
	 */
	private Object getPropertyValue(final Object model, final String propertyName)
	{
		return this.modelService.getAttributeValue(model, propertyName);
	}

	/**
	 * Sets the categories qualifier.
	 * 
	 * @param categoriesQualifier
	 *           the new categories qualifier
	 */
	public void setCategoriesQualifier(final String categoriesQualifier)
	{
		this.categoriesQualifier = categoriesQualifier;
	}


	/**
	 * @return the categoryLevel
	 */
	public Integer getCategoryLevel()
	{
		return categoryLevel;
	}

	/**
	 * @param categoryLevel
	 *           the categoryLevel to set
	 */
	public void setCategoryLevel(final Integer categoryLevel)
	{
		this.categoryLevel = categoryLevel;
	}


}
