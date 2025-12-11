/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.dao.exclusions.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Exclusions;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTExProductAttributeModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.dao.exclusions.JnjGTUsPcmPrdExclDao;



/**
 * The JnjGTUsPcmPrdExclDaoImpl class contains all those methods which are dealing with US related product exclusions
 * models.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTUsPcmPrdExclDao extends AbstractItemDao implements JnjGTUsPcmPrdExclDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTUsPcmPrdExclDao.class);

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTExProductAttributeModel> getExProductAttributeModels()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getExProductAttributeModels()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final Map queryParams = new HashMap();
		final String query = Exclusions.US_PRD_EXCL_QUERY;

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getExProductAttributeModels()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "JnjGTExProductAttributeModels Query " + fQuery);
		}

		final List<JnjGTExProductAttributeModel> result = getFlexibleSearchService().<JnjGTExProductAttributeModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getExProductAttributeModels()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return result;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelForUSProducts(final CatalogVersionModel catalogVersionModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForUSProducts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(Exclusions.PORTAL_INDC_NOT_SET_US_PRD_QUERY);

		final Map queryParams = new HashMap();
		queryParams.put(JnJProductModel.CATALOGVERSION, catalogVersionModel.getPk());
		queryParams.put("productStatusCode", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE,
				Jnjgtb2binboundserviceConstants.COMMA_STRING));
		queryParams.put("code", JnjGTModStatus.NOTAPPLICABLE.getCode());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForUSProducts()" + Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(flexibleSearchQuery)
				.getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForUSProducts()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelHavingPublishIndFalse(final CatalogVersionModel catalogVersionModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelHavingPublishIndFalse()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(Exclusions.PRODUCTS_HAVE_TEMP_PUBLISH_IND);

		final Map queryParams = new HashMap();
		queryParams.put(JnJProductModel.CATALOGVERSION, catalogVersionModel.getPk());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelHavingPublishIndFalse()" + Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(flexibleSearchQuery)
				.getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelHavingPublishIndFalse()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}
}
