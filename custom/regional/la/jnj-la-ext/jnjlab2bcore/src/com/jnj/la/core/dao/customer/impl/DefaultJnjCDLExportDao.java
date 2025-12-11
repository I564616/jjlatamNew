/**
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 **/
package com.jnj.la.core.dao.customer.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;
import com.jnj.la.core.dao.customer.JnjCDLExportDao;


public class DefaultJnjCDLExportDao implements JnjCDLExportDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCDLExportDao.class);

	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_STATEMENT = "SELECT {PK} FROM {";
	private static final String SELECT_END = "} ";
	private static final String DATE_CLAUSE = "where {modifiedtime} >= ?date";
	private static final String BETWEEN_DATE_CLAUSE = "where {modifiedtime} BETWEEN CONVERT(DATETIME, ?startDate) AND CONVERT(DATETIME, ?endDate)";

	@Override
	public List<Object> getCDLExportForType(String type, Date cronjoblastSuccessfulRunDate)
	{
		LOGGER.info("Getting value for " + type + " during JnjCDLExportJob run");
		StringBuilder query = new StringBuilder(SELECT_STATEMENT);
		query.append(type).append(SELECT_END);
		Map<String, Object> params = new HashMap<>();
		if(Objects.nonNull(cronjoblastSuccessfulRunDate)) {
			query.append(DATE_CLAUSE);
			params.put("date", cronjoblastSuccessfulRunDate);
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if(MapUtils.isNotEmpty(params)){
			fQuery.addQueryParameters(params);
		}
		SearchResult<Object> searchResult = flexibleSearchService.search(fQuery);
		return searchResult.getResult();
	}
	
	@Override
	public List<Object> getCDLExportForTypeForDate(final String type, final Date startDate, final Date endDate)
	{
		LOGGER.info("Getting value for " + type + " during JnjCDLExportJob run");
		StringBuilder query = new StringBuilder(SELECT_STATEMENT);
		query.append(type).append(SELECT_END);
		query.append(BETWEEN_DATE_CLAUSE);
		Map<String, Object> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);		
		SearchResult<Object> searchResult = flexibleSearchService.search(fQuery);
		return searchResult.getResult();
		
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService(){
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService){
		this.flexibleSearchService = flexibleSearchService;
	}



}