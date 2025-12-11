package com.jnj.core.solr.search.solrfacetsearch.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;

import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.services.CMSSiteService;


public class ContentSearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	@Autowired
	private FacetSearchConfigService facetSearchConfigService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private BaseSiteService baseSiteService;

	

	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private CMSSiteService cmsSiteSerive;
	@Autowired
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;
	public FacetSearchConfigService getFacetSearchConfigService() {
		return facetSearchConfigService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public CMSSiteService getCmsSiteSerive() {
		return cmsSiteSerive;
	}


	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		target.setSearchQueryData(source.getSearchQueryData());
		target.setPageableData(source.getPageableData());

		final Collection<CatalogVersionModel> catalogVersions = getSessionContentCatalogVersions();
		if ((catalogVersions == null) || (catalogVersions.isEmpty()))
		{
			throw new ConversionException("Missing solr facet search indexed catalog versions");
		}

		target.setCatalogVersions(new ArrayList(catalogVersions));

		try
		{
			target.setFacetSearchConfig(getFacetSearchConfig());
		}
		catch (final FacetConfigServiceException ex)
		{
			throw new ConversionException("Exception looking up the SOLR search configuration", ex);
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new ConversionException("No valid solrFacetSearchConfig found for the current context", e);

		}

		target.setIndexedType(getIndexedType(target.getFacetSearchConfig()));


		target.setSearchQuery(createSearchQuery(target.getFacetSearchConfig(), target.getIndexedType()));
		target.getSearchQuery().setCatalogVersions(target.getCatalogVersions());
		target.getSearchQuery().setCurrency(commonI18NService.getCurrentCurrency().getIsocode());
		target.getSearchQuery().setLanguage(commonI18NService.getCurrentLanguage().getIsocode());
		target.getSearchQuery().setUserQuery(source.getSearchQueryData().getFreeTextSearch());
		target.getSearchQuery().setEnableSpellcheck(true);
	}







	protected Collection<CatalogVersionModel> getSessionContentCatalogVersions()
	{
		final List<ContentCatalogModel> contentCatalogs = cmsSiteSerive.getCurrentSite().getContentCatalogs();

		final Collection<CatalogVersionModel> sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();

		final Collection result = new ArrayList();
		for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
		{
			if (!(contentCatalogs.contains(sessionCatalogVersion.getCatalog())))
			{
				continue;
			}
			result.add(sessionCatalogVersion);
		}

		return result;
	}

	protected FacetSearchConfig getFacetSearchConfig() throws FacetConfigServiceException, NoValidSolrConfigException
	{
		final SolrFacetSearchConfigModel solrFacetSearchConfigModel = getContentSolrFacetSearchConfig();
		return facetSearchConfigService.getConfiguration(solrFacetSearchConfigModel.getName());
	}

	public SolrFacetSearchConfigModel getContentSolrFacetSearchConfig() throws NoValidSolrConfigException
	{
		final SolrFacetSearchConfigModel result = getSolrConfigForContentCatalogVersions();

		if (result == null)
		{
			throw new NoValidSolrConfigException(
					"No Valid SolrFacetSearchConfig configured neither for base site/base store/session product catalog versions.");
		}
		return result;
	}



	protected SolrFacetSearchConfigModel getSolrConfigForContentCatalogVersions()  throws NoValidSolrConfigException
	{
		final Collection sessionProductCatalogVersions = getSessionContentCatalogVersions();
		 SolrFacetSearchConfigModel solrConfigModel = solrFacetSearchConfigSelectionStrategy.getCurrentSolrFacetSearchConfig();
		
			if ((solrConfigModel.getCatalogVersions() != null)
					&& (solrConfigModel.getCatalogVersions().containsAll(sessionProductCatalogVersions)))
			{
				return solrConfigModel;
			}
		
		return null;
	}



	protected IndexedType getIndexedType(final FacetSearchConfig config)
	{
		final IndexConfig indexConfig = config.getIndexConfig();


		final Collection indexedTypes = indexConfig.getIndexedTypes().values();
		if ((indexedTypes != null) && (!(indexedTypes.isEmpty())))

		{
			return ((IndexedType) indexedTypes.iterator().next());

		}

		return null;
	}

	protected SearchQuery createSearchQuery(final FacetSearchConfig config, final IndexedType indexedType)
	{
		return new SearchQuery(config, indexedType);
	}


	public SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy() {
		return solrFacetSearchConfigSelectionStrategy;
	}

	public void setSolrFacetSearchConfigSelectionStrategy(
			SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy) {
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}


}
