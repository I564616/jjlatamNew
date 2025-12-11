/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.news.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.news.JnjNewsReleasesDao;
import com.jnj.core.enums.BusinessCenter;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.services.CMSSiteService;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjNewsReleasesDao implements JnjNewsReleasesDao
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjNewsReleasesDao.class);
	protected static final String METHOD_GET_NEWS_COMPONENTS = "getNewsComponents()";
	protected static final String METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS = "getSessionContentCatalogVersions()";



	/** The flexible search service. */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	/** The catalog version service. */
	@Autowired
	protected CatalogVersionService catalogVersionService;

	/** The cms site serive. */
	@Autowired
	protected CMSSiteService cmsSiteSerive;



	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public CMSSiteService getCmsSiteSerive() {
		return cmsSiteSerive;
	}

	@Override
	public List<JnjNewsBannerComponentModel> getNewsComponents(final BusinessCenter businessCenter) throws BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		ServicesUtil.validateParameterNotNull(businessCenter, "BusinessCenter must not be null");
		List<JnjNewsBannerComponentModel> jnjNewsBannerList = new ArrayList<JnjNewsBannerComponentModel>();

		final List<CatalogVersionModel> catalogVersions = (List<CatalogVersionModel>) getSessionContentCatalogVersions();
		final CatalogVersionModel catalogVersionModel;
		if (null != catalogVersions && !(catalogVersions.isEmpty()))
		{
			catalogVersionModel = catalogVersions.get(0);
		}
		else
		{
			LOGGER.error(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ "Cannot Retirve Content Catalog. Throwing Exception.");
			throw new BusinessException("Cannot Retirve Content Catalog");
		}
		try
		{
			final Map queryParams = new HashMap();
			final String query = "select {news:pk},{news:newsPublishDate} from {JnjNewsBannerComponent as news JOIN businessCenter as bc ON {news:businessCenter} ={bc:pk} }  where {bc:code}=?businessCenter AND {news:CatalogVersion} IN"
					+ "({{"
					+ "select {cv:pk} from {CatalogVersion as cv} where {cv:catalog} IN"
					+ "({{"
					+ "select {c:pk} from {Catalog as c} where {c:id}=?catalog"
					+ "}})"
					+ "AND {cv:version} =?version"
					+ "}})"
					+ "ORDER BY {news:newsPublishDate} DESC, {pk}";
			queryParams.put("businessCenter", businessCenter.getCode().toString());
			queryParams.put("catalog", catalogVersionModel.getCatalog().getId());
			queryParams.put("version", catalogVersionModel.getVersion());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			final SearchResult<JnjNewsBannerComponentModel> searchResult = flexibleSearchService.search(fQuery);
			jnjNewsBannerList = searchResult.getResult();
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ "No News Components Fetched for BusinessCenter - " + businessCenter.getCode().toString()
					+ modelNotFoundException);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_NEWS_COMPONENTS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return jnjNewsBannerList;
	}

	/**
	 * Gets the session content catalog versions.
	 * 
	 * @return the session content catalog versions
	 */
	protected Collection<CatalogVersionModel> getSessionContentCatalogVersions()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

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


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.NEWS_RELEASES + Logging.HYPHEN + METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return result;
	}
}
