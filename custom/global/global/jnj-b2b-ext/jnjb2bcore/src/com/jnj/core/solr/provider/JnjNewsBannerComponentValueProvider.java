/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCmsUtil;


/**
 * This is used to get all the field values related to news Page
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjNewsBannerComponentValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	@Autowired
	protected FieldNameProvider fieldNameProvider;
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	JnJCmsUtil jnjCmsUtil;
	
	

	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public JnJCmsUtil getJnjCmsUtil() {
		return jnjCmsUtil;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexconfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		Collection fieldValues = Collections.emptyList();

		if (model instanceof ContentPageModel)
		{
			fieldValues = new ArrayList();
			fieldValues.addAll(createFieldValue((ContentPageModel) model, indexedProperty, indexconfig));
		}
		return fieldValues;
	}

	/**
	 * This is used to get list of FieldValue
	 * 
	 * @param isBase
	 * @param indexedProperty
	 * @return List
	 */
	protected List createFieldValue(final ContentPageModel model, final IndexedProperty indexedProperty,
			final IndexConfig indexconfig) throws FieldValueProviderException
	{
		final List fieldValues = new ArrayList();
		Iterator iterator;

		if (!indexedProperty.isLocalized())
		{
			fieldValues.addAll(extractFieldValues(indexedProperty, null, model));
		}
		else
		{
			final LanguageModel currentLanguage = commonI18NService.getCurrentLanguage();
			iterator = indexconfig.getLanguages().iterator();
			while (iterator.hasNext())
			{
				final LanguageModel language = (LanguageModel) iterator.next();
				commonI18NService.setCurrentLanguage(language);
				fieldValues.addAll(extractFieldValues(indexedProperty, language, model));
				continue;
			}
			commonI18NService.setCurrentLanguage(currentLanguage);
		}

		return fieldValues;
	}

	protected List extractFieldValues(final IndexedProperty indexedProperty, final LanguageModel language,
			final ContentPageModel model) throws FieldValueProviderException
	{

		String value = null;
		if (indexedProperty.getName().equals("content"))
		{
			value = jnjCmsUtil.getContentForPage(model);
		}
		else if (indexedProperty.getName().equals("urlLink"))
		{
			value = jnjCmsUtil.getNewsUrlLink(model);
		}
		else if (indexedProperty.getName().equals("headline"))
		{
			value = jnjCmsUtil.getHeadLines(model);
		}
		else if (indexedProperty.getName().equals("newsPublishDate"))
		{
			value = jnjCmsUtil.getNewsPublicationDate(model);
		}
		else if (indexedProperty.getName().equals("thumbnailUrl"))
		{
			value = jnjCmsUtil.getNewsThumbnailUrl(model);
		}
		else if (indexedProperty.getName().equals("businessCenter"))
		{
			value = jnjCmsUtil.getNewsBusinessCenter(model);
		}
		else if (indexedProperty.getName().equals("year"))
		{
			value = jnjCmsUtil.getNewsPublicationYear(model);
		}
		else if (indexedProperty.getName().equals("longDate"))
		{
			value = jnjCmsUtil.getNewsPublicationDateInLong(model);
		}


		final List fieldValues = new ArrayList();
		if (StringUtils.isNotEmpty(value))
		{
			final Collection fieldNames = fieldNameProvider.getFieldNames(indexedProperty, language != null ? language.getIsocode()
					: null);
			String fieldName;
			for (final Iterator iterator = fieldNames.iterator(); iterator.hasNext(); fieldValues.add(new FieldValue(fieldName,
					value)))
			{
				fieldName = (String) iterator.next();
			}
		}
		return fieldValues;
	}
}
