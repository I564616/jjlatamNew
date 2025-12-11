/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchPagePopulator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * This is used to create PageableData from SolrSearchRequest
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTSearchPagePopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		extends SearchPagePopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
{
	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		if (target.getPageableData() != null)
		{
			final int pageSize = target.getPageableData().getPageSize();
			if (pageSize > 0)
			{
				target.getSearchQuery().setPageSize(pageSize);
			}

			final int currentPage = target.getPageableData().getCurrentPage();
			if (currentPage < 0)
			{
				return;
			}
			target.getSearchQuery().setOffset(currentPage);
		}
		else
		{
			target.getSearchQuery().setOffset(0);
		}
	}
}
