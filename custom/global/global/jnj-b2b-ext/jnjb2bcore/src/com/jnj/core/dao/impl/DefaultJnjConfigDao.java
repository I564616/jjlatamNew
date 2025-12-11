/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dao.JnjConfigDao;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.util.JnJCommonUtil;


/**
 * The JnjConfigDaoImpl class interacts with the hybris data and retrieves the result set on the basis of requested id.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjConfigDao extends AbstractItemDao implements JnjConfigDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjConfigDao.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjConfigModel getConfigValueById(final String id)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//To validate the incoming parameter
		ServicesUtil.validateParameterNotNull(id, "Id must not be null");
		final Map queryParams = new HashMap();
		final String query = Order.CONFIG_QUERY;
		queryParams.put(Order.ID_STRING, id);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Config Model Query " + fQuery);
		}

		final List<JnjConfigModel> jnjConfigModelList = getFlexibleSearchService().<JnjConfigModel> search(fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (!jnjConfigModelList.isEmpty())
		{
			return jnjConfigModelList.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method retrieves data from hybris on the basis of the id pattern passed in the dynamic query.
	 * 
	 * @param idLike
	 * @return values
	 */
	@Override
	public List<String> getConfigValuesWhereIdLike(final String idLike)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(idLike, "IdLike must not be null");
		//"select {pk} from {jnjconfig} where {id} like ?text"
		final StringBuilder query = new StringBuilder("select {pk} from {jnjconfig} where {id} like '").append(idLike)
				.append(Jnjb2bCoreConstants.PERCENTAGE_SYMBOL).append("'");
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Config Model Query " + fQuery);
		}
		final List<JnjConfigModel> jnjConfigModelList = getFlexibleSearchService().<JnjConfigModel> search(fQuery).getResult();
		final List<String> values = new ArrayList<String>();
		if (!jnjConfigModelList.isEmpty())
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Config Model results " + jnjConfigModelList);
			}
			for (final JnjConfigModel jnjConfigModelValues : jnjConfigModelList)
			{
				values.add(jnjConfigModelValues.getValue());
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			return values;
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
						+ Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			return null;
		}
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjConfigModel> getConfigModelsByIdAndKey(final String id, final String key)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigModelsByIdAndKey()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//To validate the incoming parameter
		ServicesUtil.validateParameterNotNull(id, "Id must not be null");
		final Map queryParams = new HashMap();
		final StringBuilder query = new StringBuilder(Order.CONFIG_QUERY);
		queryParams.put(Order.ID_STRING, id);
		if (StringUtils.isNotEmpty(key))
		{
			query.append(Order.CONFIG_QUERY_FOR_KEY);
			queryParams.put(Order.KEY_STRING, key);
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		
		fQuery.addQueryParameters(queryParams);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigModelsByIdAndKey()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Config Model Query " + fQuery);
		}

		final List<JnjConfigModel> jnjConfigModelList = getFlexibleSearchService().<JnjConfigModel> search(fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigModelsByIdAndKey()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjConfigModelList;

	}
}
