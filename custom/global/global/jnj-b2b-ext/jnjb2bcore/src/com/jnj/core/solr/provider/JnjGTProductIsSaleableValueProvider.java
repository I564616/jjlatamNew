/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.servicelayer.session.SessionService;
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

import org.springframework.beans.factory.annotation.Autowired;



//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTProductIsSaleableValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;

	@Autowired
	SessionService sessionService;

	public JnJGTProductService getJnjGTProductService() {
		return jnjGTProductService;
	}

	public SessionService getSessionService() {
		return sessionService;
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

		// save the current site
		final String sessionSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		// If current indexing is being done for MDD
		if (indexConfig.getBaseSite().getUid().equals(Jnjb2bCoreConstants.MDD_SITE_ID))
		{
			// set the current site to MDD temporarily
			sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, Jnjb2bCoreConstants.MDD);
		}
		// If current indexing is being done for CONS
		else if (indexConfig.getBaseSite().getJnjWebSiteType().getCode().equals(Jnjb2bCoreConstants.CONSUMER_SITE_ID))
		{
			// set the current site to CONS temporarily
			sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, Jnjb2bCoreConstants.CONS);
		}

		final boolean isProdSaleable = jnjGTProductService.isProductSaleable(naProductModel, true);

		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

		fieldValues.addAll(createFieldValue(isProdSaleable, indexedProperty));

		// revert the session site back to stored session site
		sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, sessionSite);

		return fieldValues;

	}

	protected List<FieldValue> createFieldValue(final boolean isProdSaleable, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, Boolean.valueOf(isProdSaleable)));
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
