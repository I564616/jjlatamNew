/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.search.solrfacetsearch.impl;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.solr.search.solrfacetsearch.service.JnjContentSearchService;
import com.jnj.facades.data.JnjContentData;
import com.jnj.facades.search.JnjContentSearchFacade;


/**
 * Content search facade interface impl. Used to retrieve Content of type {@link JnjContentData} (or subclasses of).
 * 
 * @param <ITEM>
 *           The type of the content result items
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSolrContentSearchFacade<ITEM extends JnjContentData> implements JnjContentSearchFacade<ITEM>
{
	@Autowired
	private JnjContentSearchService<SolrSearchQueryData, SearchResultValueData, FacetSearchPageData<SolrSearchQueryData, SearchResultValueData>> jnjSolrContentSearchService;
	@Autowired
	ThreadContextService threadContextService;
	@Autowired
	private Converter<SearchQueryData, SolrSearchQueryData> solrSearchQueryDecoder;
	@Autowired
	private Converter<FacetSearchPageData<SolrSearchQueryData, SearchResultValueData>, FacetSearchPageData<SearchStateData, ITEM>> jnjContentSearchPageConverter;


	protected SolrSearchQueryData decodeState(final SearchStateData searchState)
	{
		final SolrSearchQueryData searchQueryData = solrSearchQueryDecoder.convert(searchState.getQuery());

		return searchQueryData;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public FacetSearchPageData<SearchStateData, ITEM> contentSearch(final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");

		return threadContextService
				.executeInContext(new ThreadContextService.Executor<FacetSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
				{
					@Override
					public FacetSearchPageData<SearchStateData, ITEM> execute()
					{
						return jnjContentSearchPageConverter.convert(jnjSolrContentSearchService.searchAgain(decodeState(searchState),
								pageableData));
					}
				});
	}




}
