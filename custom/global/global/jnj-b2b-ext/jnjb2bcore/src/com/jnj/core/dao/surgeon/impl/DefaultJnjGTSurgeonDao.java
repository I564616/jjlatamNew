/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dao.surgeon.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.impl.DefaultJnjConfigDao;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.surgeon.JnjGTSurgeonDao;
import com.jnj.core.model.JnjGTSurgeonModel;


/**
 * Interacts with the database gets the surgeon model and
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTSurgeonDao implements JnjGTSurgeonDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjConfigDao.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;
	

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	/**
	 * Returns the surgeon model by id.
	 *
	 * @param id
	 *           the id
	 * @return the surgeon model
	 */
	@Override
	public JnjGTSurgeonModel getJnjSurgeonModelById(final String id)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjSurgeonModelById()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjGTSurgeonModel result = null;
		final Map queryParams = new HashMap();
		final String query = "select {pk} from {JnjGTIntSurgeon} where {surgeonId}=?surgeonId";
		queryParams.put("surgeonId", id);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjSurgeonModelById()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Surgeon Model Query " + fQuery);
		}
		result = flexibleSearchService.searchUnique(fQuery);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjSurgeonModelById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public Collection<JnjGTSurgeonModel> getAllSurgeonRecords()
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTSurgeonModel._TYPECODE).append("}").append("ORDER BY").append("{").append(JnjGTSurgeonModel.FIRSTNAME).append("}");
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(stringBuilder.toString());
		final List<JnjGTSurgeonModel> result = flexibleSearchService.<JnjGTSurgeonModel> search(flexibleSearchQuery).getResult();

		return result;
	}

	@Override
	public SearchPageData<JnjGTSurgeonModel> getSurgeonRecords(final PageableData pageableData, final String searchPattern)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(ItemModel.PK).append("} FROM {")
				.append(JnjGTSurgeonModel._TYPECODE).append("}");

		final String pattrenMatchCond = " WHERE UPPER({firstName}) LIKE ?pattern OR UPPER({lastName}) LIKE ?pattern  OR UPPER({middleName}) LIKE ?pattern OR UPPER({surgeonId}) LIKE ?pattern";

		final Map<String, Object> queryParams = new HashMap<String, Object>(1);
		if (StringUtils.isNotEmpty(searchPattern))
		{
			query.append(pattrenMatchCond);
			queryParams.put("pattern", "%" + searchPattern.toUpperCase() + "%");
		}
		final SearchPageData<JnjGTSurgeonModel> result = pagedFlexibleSearchService.search(query.toString(), queryParams,
				pageableData);
		return result;
	}
}
