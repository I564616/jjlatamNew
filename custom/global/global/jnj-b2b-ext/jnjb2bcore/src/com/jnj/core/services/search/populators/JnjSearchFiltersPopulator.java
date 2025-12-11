/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.search.populators;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchFiltersPopulator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.search.JnjSolrService;


/**
 * This is used to add the restriction to restrict product search
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSearchFiltersPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> extends
		SearchFiltersPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE>
{
	@Autowired
	JnjSolrService jnjSolrService;

	public JnjSolrService getJnjSolrService() {
		return jnjSolrService;
	}

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final Collection<String> restrictedValues = jnjSolrService.getRestrictedValues();
		final Collection<String> enabledManuAIDValues = jnjSolrService.getEnabledManufacturerAIDValues();

		//Check if the service returns a field that can needs to be restricted along with the restricted values
		if (StringUtils.isNotEmpty(jnjSolrService.getRestrictedField()) && CollectionUtils.isNotEmpty(restrictedValues))
		{
			final SearchQuery searchQuery = target.getSearchQuery();
			for (final String categorycode : restrictedValues)
			{
				searchQuery.addFacetValue(jnjSolrService.getRestrictedField(), ClientUtils.escapeQueryChars(categorycode));
			}
		}
		//Check if the service returns a field that can needs to be restricted along with the restricted values
		if (StringUtils.isNotEmpty(jnjSolrService.getRestrictedManufacturerField())
				&& CollectionUtils.isNotEmpty(enabledManuAIDValues))
		{
			final SearchQuery searchQuery = target.getSearchQuery();
			for (final String manufacturerAID : enabledManuAIDValues)
			{
				searchQuery.addFacetValue(jnjSolrService.getRestrictedManufacturerField(),
						ClientUtils.escapeQueryChars(manufacturerAID));
			}
		}
		super.populate(source, target);
	}
}
