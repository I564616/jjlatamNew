/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.suggestion;


import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import de.hybris.platform.solrfacetsearch.suggester.impl.DefaultSolrAutoSuggestService;

import java.net.URLDecoder;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;

import com.jnj.core.constants.Jnjb2bCoreConstants.solrConfig;




/**
 * This class is overridden to make autocomplete work for numeric values
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSolrAutoSuggestService extends DefaultSolrAutoSuggestService
{
	
	private static final Logger LOG = Logger
			.getLogger(JnjSolrAutoSuggestService.class);
	
	protected SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;

	public SolrIndexedTypeCodeResolver getSolrIndexedTypeCodeResolver() {
		return solrIndexedTypeCodeResolver;
	}
	public void setSolrIndexedTypeCodeResolver(SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver) {
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
	}

	/**
	 * this is used to get the suggestions from solr server.
	 * 
	 * This is overridden to make autocomplete work for numeric values. {@inheritDoc}
	 * 
	 * @param language
	 *           the language
	 * @param solrIndexedType
	 *           the solr indexed type
	 * @param queryInput
	 *           the query input
	 * @return the auto suggestions for query
	 * @throws SolrAutoSuggestException
	 *            the solr auto suggest exception
	 */
	@Override
	public SolrSuggestion getAutoSuggestionsForQuery(final LanguageModel language, final SolrIndexedTypeModel solrIndexedType,
			final String queryInput) throws SolrAutoSuggestException
	{
		SolrClient solrClient = null;
		Map<String, Collection<String>> resultSuggestionMap = new HashMap();
		Collection<String> resultCollations = new ArrayList();
		if (StringUtils.isNotBlank(queryInput)) {
			try {
				String configName = solrIndexedType.getSolrFacetSearchConfig()
						.getName();
				FacetSearchConfig facetSearchConfig = this.facetSearchConfigService
						.getConfiguration(configName);
				IndexedType indexedType = (IndexedType) facetSearchConfig
						.getIndexConfig()
						.getIndexedTypes()
						.get(this.solrIndexedTypeCodeResolver
								.resolveIndexedTypeCode(solrIndexedType));
				SolrSearchProvider solrSearchProvider = this.getSolrSearchProviderFactory()
						.getSearchProvider(facetSearchConfig, indexedType);
				SolrIndexModel solrIndex = this.getSolrIndexService()
						.getActiveIndex(facetSearchConfig.getName(),
								indexedType.getIdentifier());
				Index index = solrSearchProvider.resolveIndex(
						facetSearchConfig, indexedType,
						solrIndex.getQualifier());
				solrClient = solrSearchProvider.getClient(index);
				SolrQuery query = new SolrQuery();
				/*query.setQuery(queryInput);
				query.setRequestHandler("/suggest");
				query.set("spellcheck.dictionary",
						new String[]{language.getIsocode()});
				query.set("spellcheck.q", new String[]{queryInput});*/
				query.setQuery(URLEncoder.encode(queryInput, solrConfig.URL_ENCODING));
				query.setRequestHandler(solrConfig.SUGGEST);
				query.set(solrConfig.SPELLCHECK_DICTIONARY, new String[]
	
				{ language.getIsocode() });
				query.set(solrConfig.SPELLCHECK_Q, new String[]
				{ URLEncoder.encode(queryInput, solrConfig.URL_ENCODING) });
				if (LOG.isDebugEnabled()) {
					LOG.debug("Solr Suggest Query: \n"
							+ URLDecoder.decode(query.toString(), "UTF-8"));
				}

				QueryResponse response = solrClient.query(index.getName(),
						query);
				
				if (LOG.isDebugEnabled()) {
					LOG.debug("Solr Suggest Response: \n" + response);
				}
			

				SpellCheckResponse spellCheckResponse = response
						.getSpellCheckResponse();
				if (spellCheckResponse != null) {
					this.populateSuggestionsFromResponse(resultSuggestionMap,
							resultCollations, spellCheckResponse);
				}
			} catch (FacetConfigServiceException | SolrServerException
					| IOException | SolrServiceException var19) {
				throw new SolrAutoSuggestException(
						"Error issuing suggestion query", var19);
			} finally {
				IOUtils.closeQuietly(solrClient);
			}
		}

		return new SolrSuggestion(resultSuggestionMap, resultCollations);
	}
}