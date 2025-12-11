package com.jnj.core.solr;

import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import de.hybris.platform.solrfacetsearch.search.impl.AbstractFacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
public class JnjFacetSearchStrategy extends DefaultFacetSearchStrategy{
	
	
	private static final Logger LOG = Logger.getLogger(DefaultFacetSearchStrategy.class);
	private FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory;
	private Converter<SearchQueryConverterData, SolrQuery> facetSearchQueryConverter;
	private Converter<SearchResultConverterData, SearchResult> facetSearchResultConverter;

	@Autowired
	private FieldNameProvider fieldNameProvider;
	
	@Autowired
	private CMSSiteService cmsSiteService;
	
	
	
	public I18NService getI18NService() {
		return this.getI18NService();
	}

	
	public FacetSearchContextFactory<FacetSearchContext> getFacetSearchContextFactory() {
		return this.facetSearchContextFactory;
	}

	public void setFacetSearchContextFactory(FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory) {
		this.facetSearchContextFactory = facetSearchContextFactory;
	}

	public Converter<SearchQueryConverterData, SolrQuery> getFacetSearchQueryConverter() {
		return this.facetSearchQueryConverter;
	}

	public void setFacetSearchQueryConverter(Converter<SearchQueryConverterData, SolrQuery> facetSearchQueryConverter) {
		this.facetSearchQueryConverter = facetSearchQueryConverter;
	}

	public Converter<SearchResultConverterData, SearchResult> getFacetSearchResultConverter() {
		return this.facetSearchResultConverter;
	}

	public void setFacetSearchResultConverter(
			Converter<SearchResultConverterData, SearchResult> facetSearchResultConverter) {
		this.facetSearchResultConverter = facetSearchResultConverter;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public SearchResult search(SearchQuery searchQuery, Map<String, String> searchHints) throws FacetSearchException {
		
		
		
		CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		if(cmsSiteModel!=null){
			String solrNotSupportedLangList = Config.getParameter("solr.lang.notsupported.cmssite.ids");
			if(StringUtils.isNotBlank(solrNotSupportedLangList)){
				if(solrNotSupportedLangList.contains(cmsSiteModel.getUid())){
					searchQuery.setLanguage("en");
				}
			}
		}

		this.checkQuery(searchQuery);
		SolrClient solrClient = null;
		
		SearchResult var17;
		try {
			
			/*if (facetSearchConfig.getSearchConfig().getGroupingProperty() != null)
			{
				
				
				final String fieldName = fieldNameProvider.getFieldName(
						facetSearchConfig.getSearchConfig().getGroupingProperty(), null, FieldNameProvider.FieldType.INDEX);
				searchQuery.addSolrParams("group", new String[]
				{ "true" });
				searchQuery.addSolrParams("group.field", new String[]
				{ fieldName });
				searchQuery.addSolrParams("group.truncate", new String[]
				{ "true" });
				searchQuery.addSolrParams("group.ngroups", new String[]
				{ "true" });
				searchQuery.addSolrParams("group.limit", new String[]
				{ "1" });
			}*/
		/*	
			FacetSearchContext facetSearchContext = this.facetSearchContextFactory.createContext(facetSearchConfig, indexedType,
					searchQuery);*/
			FacetSearchConfig facetSearchConfig = searchQuery
					.getFacetSearchConfig();
			IndexedType indexedType = searchQuery.getIndexedType();
			FacetSearchContext facetSearchContext = this.getFacetSearchContextFactory()
					.createContext(facetSearchConfig, indexedType, searchQuery);
			facetSearchContext.getSearchHints().putAll(searchHints);
			this.getFacetSearchContextFactory().initializeContext();
			this.checkContext(facetSearchContext);
			SolrSearchProvider solrSearchProvider = this.getSolrSearchProviderFactory()
					.getSearchProvider(facetSearchConfig, indexedType);
			SolrIndexModel activeIndex = this.getSolrIndexService().getActiveIndex(
					facetSearchConfig.getName(), indexedType.getIdentifier());
			Index index = solrSearchProvider.resolveIndex(facetSearchConfig,
					indexedType, activeIndex.getQualifier());
			solrClient = solrSearchProvider.getClient(index);
			SearchQueryConverterData searchQueryConverterData = new SearchQueryConverterData();
			searchQueryConverterData.setFacetSearchContext(facetSearchContext);
			
				       
			searchQueryConverterData.setSearchQuery(searchQuery);
			
			SolrQuery solrQuery = (SolrQuery) this.getFacetSearchQueryConverter()
					.convert(searchQueryConverterData);
			if (LOG.isDebugEnabled()) {
				LOG.debug(solrQuery);
			}

			METHOD method = this.resolveQueryMethod(facetSearchConfig);
			QueryResponse queryResponse = solrClient.query(index.getName(),
					solrQuery, method);
			SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
			searchResultConverterData.setFacetSearchContext(facetSearchContext);
			searchResultConverterData.setQueryResponse(queryResponse);
			SearchResult searchResult = (SearchResult) this.facetSearchResultConverter
					.convert(searchResultConverterData);
			
			this.getFacetSearchContextFactory().getContext().setSearchResult(
					searchResult);
			this.getFacetSearchContextFactory().destroyContext();
			var17 = searchResult;
		} catch (SolrServerException | IOException | RuntimeException
				| SolrServiceException var20) {
			this.getFacetSearchContextFactory().destroyContext(var20);
			throw new FacetSearchException(var20.getMessage(), var20);
		} finally {
			IOUtils.closeQuietly(solrClient);
			
		}
		return var17;
	}	

	private boolean canUseFallbackLanguage(final SolrServerException solrServerException)
	{
		return ((solrServerException.getMessage().contains("undefined field")) && (getI18NService().isLocalizationFallbackEnabled()));
	}


	private SearchResult queryUsingFallbackLanguage(final SearchQuery query, final SolrClient solrServer,
			final IndexedType indexedType, final SolrServerException solrServerException) throws FacetSearchException, IOException
	{
		final LanguageModel language = getI18NService().getLanguage(query.getLanguage());
		final List<LanguageModel> languages = language.getFallbackLanguages();
	
		for (final LanguageModel lang : languages)
		{
			query.setLanguage(lang.getIsocode());
			
		}
	
		throw new FacetSearchException("Cannot query using fallback languages: " + languages, solrServerException);
	}


	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}


	public void setFieldNameProvider(FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}


	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}


	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

	
	
	/*private SearchResult queryInternal(final SearchQuery query, final SolrClient solrServer, final IndexedType indexedType)
			throws FacetSearchException, SolrServerException, IOException
	{
		//final SolrQuery solrSearchQuery = translateSearchQuery(query);
		final SolrQuery solrSearchQuery = getSolrQueryConverter().convertSolrQuery(query);
		if (LOG.isDebugEnabled())
		{
			try
			{
				LOG.debug("Solr Query: \n" + URLDecoder.decode(solrSearchQuery.toString(), "UTF-8"));
			}
			catch (final UnsupportedEncodingException localUnsupportedEncodingException)
			{
				LOG.error(localUnsupportedEncodingException.getMessage());
			}
		}
		if (null != solrSearchQuery.getParams("spellcheck.q") && solrSearchQuery.getParams("spellcheck.q")[0] == null)
		{
			solrSearchQuery.remove("spellcheck.q");
		}
		
		final QueryResponse queryResponse = solrServer.query(solrSearchQuery, SolrRequest.METHOD.POST);
		
		final SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
		searchResultConverterData.setFacetSearchContext(getFacetSearchContextFactory().createContext(query.getFacetSearchConfig(), indexedType, query));
		searchResultConverterData.setQueryResponse(queryResponse);
		
		
		final SearchResult searchResult = getFacetSearchResultConverter().convert(searchResultConverterData);
		return applySearchResultsPostProcessors(searchResult);
	}*/

}
