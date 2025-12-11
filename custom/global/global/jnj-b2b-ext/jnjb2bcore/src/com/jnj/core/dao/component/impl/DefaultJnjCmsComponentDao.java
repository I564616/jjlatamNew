/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.component.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.component.JnjCmsComponentDao;
import com.jnj.core.model.JnjLinkComponentModel;
import com.jnj.core.util.JnJCmsUtil;
import com.jnj.exceptions.BusinessException;


/**
 * Impl class for JnjCmsComponentDao. Fetches the CMS components from Hybris
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCmsComponentDao implements JnjCmsComponentDao
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjCmsComponentDao.class);

	protected static final String METHOD_JNJ_LINK_COMPONENT = "getJnjLinkComponentForId()";


	/** The flexible search service. */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	@Autowired
	protected JnJCmsUtil jnJCmsUtil;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnJCmsUtil getJnJCmsUtil() {
		return jnJCmsUtil;
	}

	@Override
	public JnjLinkComponentModel getJnjLinkComponentForId(final String componentId) throws BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		ServicesUtil.validateParameterNotNull(componentId, "JnjLinkComponent UId must not be null");

		JnjLinkComponentModel jnjLinkComponentModel = null;
		final List<CatalogVersionModel> catalogVersions = (List<CatalogVersionModel>) jnJCmsUtil.getSessionContentCatalogVersions();
		final CatalogVersionModel catalogVersionModel;
		if (null != catalogVersions && !(catalogVersions.isEmpty()))
		{
			catalogVersionModel = catalogVersions.get(0);
		}
		else
		{
			LOGGER.error(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ "Cannot Retirve Content Catalog. Throwing Exception.");
			throw new BusinessException("Cannot Retirve Content Catalog");
		}

		try
		{
			final Map queryParams = new HashMap();
			final String query = "select {jLink:pk} from {JnjLinkComponent as jLink}  where {jLink:uid}=?uid AND {jLink:visible}='1' AND {jLink:CatalogVersion} IN"
					+ "({{"
					+ "select {cv:pk} from {CatalogVersion as cv} where {cv:catalog} IN"
					+ "({{"
					+ "select {c:pk} from {Catalog as c} where {c:id}=?catalog" + "}})" + "AND {cv:version} =?version" + "}})";
			queryParams.put("uid", componentId);
			queryParams.put("catalog", catalogVersionModel.getCatalog().getId());
			queryParams.put("version", catalogVersionModel.getVersion());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			jnjLinkComponentModel = flexibleSearchService.searchUnique(fQuery);

		}

		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ "No JnjLinkComponent Fetched for Uid - " + componentId + Logging.HYPHEN + modelNotFoundException);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_CMS_COMPONET_DAO + Logging.HYPHEN + METHOD_JNJ_LINK_COMPONENT + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return jnjLinkComponentModel;
	}
}
