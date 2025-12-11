/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
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
import java.util.Set;

import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;


public class JnjGTIncludedB2BUnitsValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
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

		final JnjGTModStatus productStatus = naProductModel.getModStatus();
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

		// If product status is active index the b2bunit
		if (productStatus != null && productStatus.equals(JnjGTModStatus.ACTIVE))
		{
			final Set<JnJB2BUnitModel> includedUnits = naProductModel.getIncludedUnits();

			if (includedUnits != null && !includedUnits.isEmpty())
			{
				for (final JnJB2BUnitModel includedUnit : includedUnits)
				{
					fieldValues.addAll(createFieldValue(includedUnit, indexedProperty));
				}
			}
		}
		// If product status is discontinued index the default b2bunit
		else if (productStatus != null && productStatus.equals(JnjGTModStatus.DISCONTINUED))
		{
			final JnJB2BUnitModel allB2Bunits = new JnJB2BUnitModel();
			allB2Bunits.setUid(Jnjgtb2bCONSConstants.ALLB2BUNIT);
			fieldValues.addAll(createFieldValue(allB2Bunits, indexedProperty));
		}
		else
		{
			return Collections.emptyList();
		}
		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final JnJB2BUnitModel includedUnit, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		//As Uid for each of the B2B unit will be unique
		final Object value = includedUnit.getUid();
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
