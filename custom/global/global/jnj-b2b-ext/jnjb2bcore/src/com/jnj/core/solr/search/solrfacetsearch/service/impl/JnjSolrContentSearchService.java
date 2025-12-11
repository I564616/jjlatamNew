/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.solr.search.solrfacetsearch.service.impl;

import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.solr.search.solrfacetsearch.service.JnjContentSearchService;
import com.jnj.core.util.JnJCommonUtil;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */



public class JnjSolrContentSearchService<ITEM> implements
		JnjContentSearchService<SolrSearchQueryData, ITEM, FacetSearchPageData<SolrSearchQueryData, ITEM>>
{
	private static final Logger LOG = Logger.getLogger(JnjSolrContentSearchService.class);

	@Autowired
	Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> contentSearchQueryPageableConverter;
	@Autowired
	Converter<SolrSearchRequest, SolrSearchResponse> commerceSolrSearchRequestConverter;
	@Autowired
	Converter<SolrSearchResponse, FacetSearchPageData<SolrSearchQueryData, ITEM>> contentSolrSearchResponseConverter;

	

	public Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getContentSearchQueryPageableConverter() {
		return contentSearchQueryPageableConverter;
	}


	public Converter<SolrSearchRequest, SolrSearchResponse> getCommerceSolrSearchRequestConverter() {
		return commerceSolrSearchRequestConverter;
	}


	public Converter<SolrSearchResponse, FacetSearchPageData<SolrSearchQueryData, ITEM>> getContentSolrSearchResponseConverter() {
		return contentSolrSearchResponseConverter;
	}


	protected FacetSearchPageData<SolrSearchQueryData, ITEM> doSearch(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final String METHOD_NAME = "doSearch ()";
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.SOLR_SEARCH + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}

		ServicesUtil.validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");


		final SearchQueryPageableData searchQueryPageableData = buildSearchQueryPageableData(searchQueryData, pageableData);


		final SolrSearchRequest solrSearchRequest = contentSearchQueryPageableConverter.convert(searchQueryPageableData);


		final SolrSearchResponse solrSearchResponse = commerceSolrSearchRequestConverter.convert(solrSearchRequest);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.SOLR_SEARCH + " - " + METHOD_NAME + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return (contentSolrSearchResponseConverter.convert(solrSearchResponse));
	}


	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}



	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData();
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.solr.search.solrfacetsearch.service.ContentSearchService#searchAgain(java.lang.Object,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public FacetSearchPageData<SolrSearchQueryData, ITEM> searchAgain(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}




}
