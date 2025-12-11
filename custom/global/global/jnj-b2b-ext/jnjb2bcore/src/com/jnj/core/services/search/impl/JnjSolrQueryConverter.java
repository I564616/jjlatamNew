/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.search.impl;

import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultSolrQueryConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.search.JnjSolrService;


/**
 * Product search restriction based on restricted categories
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSolrQueryConverter extends DefaultSolrQueryConverter
{
	@Autowired
	JnjSolrService jnjSolrService;

	public JnjSolrService getJnjSolrService() {
		return jnjSolrService;
	}

	@Override
	/**
	 * This is overridden to consider restricted field as fq 
	 */
	protected boolean isFilterQueryField(final QueryField queryField, final Map<String, IndexedFacetInfo> facetInfoMap)
	{
		final String field = queryField.getField();
		boolean isFilterQuery = ((Jnjb2bCoreConstants.CATALOG_ID.equals(field))
				|| (Jnjb2bCoreConstants.CATALOG_VERSION.equals(field)) || (facetInfoMap.containsKey(field)));

		if (StringUtils.isNotBlank(jnjSolrService.getRestrictedField()))
		{
			isFilterQuery = isFilterQuery || (jnjSolrService.getRestrictedField().equals(field));
		}
		return isFilterQuery;
	}

	@Override
	/**
	 * This is overridden to escape  field restricted field 
	 */
	protected String escape(final String text)
	{
		if (StringUtils.isNotBlank(jnjSolrService.getRestrictedField()) && jnjSolrService.getRestrictedField().equals(text))
		{
			return text;
		}
		return ClientUtils.escapeQueryChars(text);
	}


	/**
	 * This is overridden to set operator as 'and' for restricted field
	 */
	@Override
	protected String[] convertQueryFields(final List<QueryField> queryFields, final Map<String, IndexedFacetInfo> facetInfoMap)
	{
		final List joinedQueries = new ArrayList();

		for (final QueryField queryField : queryFields)

		{
			final IndexedFacetInfo indexedFacetInfo = (facetInfoMap == null) ? null : (IndexedFacetInfo) facetInfoMap.get(queryField
					.getField());
			String fieldPrefix = "";
			if (null != indexedFacetInfo)
			{
				fieldPrefix = "{!tag=" + indexedFacetInfo.getKey() + "}";
			}

			if (queryField.getValues().size() == 1)
			{
				if ("fulltext".equals(queryField.getField()))
				{
					joinedQueries.add(fieldPrefix + "(" + (queryField.getValues().iterator().next()) + ")");

				}
				else
				{
					joinedQueries.add(fieldPrefix + "(" + escape(queryField.getField()) + ":"
							+ (queryField.getValues().iterator().next()) + ")");

				}

			}
			else if ("fulltext".equals(queryField.getField()))
			{
				joinedQueries.add(fieldPrefix
						+ "("
						+ combine(queryField.getValues().toArray(new String[queryField.getValues().size()]), queryField.getOperator()
								.getName()) + "))");

			}
			else if (StringUtils.isNotBlank(jnjSolrService.getRestrictedField())
					&& jnjSolrService.getRestrictedField().equals(queryField.getField()))
			{

				joinedQueries.add(fieldPrefix
						+ "("
						+ escape(queryField.getField())
						+ ":("
						+ combine(queryField.getValues().toArray(new String[queryField.getValues().size()]), queryField.getOperator()
								.getName().toLowerCase()) + "))");


			}
			else
			{
				joinedQueries.add(fieldPrefix
						+ "("
						+ escape(queryField.getField())
						+ ":("
						+ combine(queryField.getValues().toArray(new String[queryField.getValues().size()]), queryField.getOperator()
								.getName()) + "))");

			}

		}

		return ((String[]) joinedQueries.toArray(new String[joinedQueries.size()]));
	}
}
