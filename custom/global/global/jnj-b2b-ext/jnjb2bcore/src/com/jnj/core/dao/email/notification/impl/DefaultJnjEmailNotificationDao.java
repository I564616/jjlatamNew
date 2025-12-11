/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.email.notification.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.email.notification.JnjEmailNotificationDao;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjEmailNotificationDao implements JnjEmailNotificationDao
{

	/** The Constant LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjEmailNotificationDao.class);
	protected static final int EMAIL_NOTIFICATION_SENT_FLAG = 0;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	@Override
	public Collection<JnjReadOperationDashboardModel> getReadDashboardRecordsForNotification()
	{
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjReadOperationDashboardModel._TYPECODE)
				.append("}").append(" WHERE {").append(JnjReadOperationDashboardModel.EMAILNOTIFICATIONSENT)
				.append("} = ?emailNotificationSent").append(" ORDER BY {").append(ItemModel.PK).append("}");

		final Map queryParams = new HashMap();
		queryParams.put(JnjReadOperationDashboardModel.EMAILNOTIFICATIONSENT, EMAIL_NOTIFICATION_SENT_FLAG);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);
		final List<JnjReadOperationDashboardModel> result = flexibleSearchService.<JnjReadOperationDashboardModel> search(fQuery)
				.getResult();
		return result;
	}

	@Override
	public Collection<JnjWriteOperationDashboardModel> getWriteDashboardRecordsForNotification()
	{
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjWriteOperationDashboardModel._TYPECODE)
				.append("} WHERE {").append(JnjWriteOperationDashboardModel.EMAILNOTIFICATIONSENT)
				.append("} = ?emailNotificationSent").append(" ORDER BY {").append(ItemModel.PK).append("}");

		final Map queryParams = new HashMap();
		queryParams.put(JnjWriteOperationDashboardModel.EMAILNOTIFICATIONSENT, EMAIL_NOTIFICATION_SENT_FLAG);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);
		final List<JnjWriteOperationDashboardModel> result = flexibleSearchService.<JnjWriteOperationDashboardModel> search(fQuery)
				.getResult();
		return result;
	}
}
