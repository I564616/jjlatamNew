/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.accenture.model.MessageItemModel;
import com.jnj.constants.JNJFlexiQueries;
import com.jnj.dao.MessageDao;


/**
 * The Class MessageDaoImpl.
 * 
 * @author Accenture
 * @version 1.0
 */
public class MessageDaoImpl extends AbstractItemDao implements MessageDao
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sapientnitro.dao.MessageDao#findAllCOMessages(java.util.Set)
	 */
	@Override
	public List<MessageItemModel> findAllJNJMessages(final Set<CatalogVersionModel> catalogVersions)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(JNJFlexiQueries.MESSAGES_BY_CATALOGVERSION);
		query.addQueryParameter("catalogVersions", catalogVersions);
		final SearchResult<MessageItemModel> searchResult = super.search(query);
		return searchResult.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sapientnitro.dao.MessageDao#findCOMessages(java.lang.String, java.util.Set)
	 */
	@Override
	public List<MessageItemModel> findJNJMessages(final String messageCode, final Collection<CatalogVersionModel> catalogVersions)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(JNJFlexiQueries.MESSAGE_BY_CODE);
		query.addQueryParameter("code", messageCode);
		query.addQueryParameter("catalogVersions", catalogVersions);
		final SearchResult<MessageItemModel> searchResult = getFlexibleSearchService().search(query);
		return searchResult.getResult();
	}
}
