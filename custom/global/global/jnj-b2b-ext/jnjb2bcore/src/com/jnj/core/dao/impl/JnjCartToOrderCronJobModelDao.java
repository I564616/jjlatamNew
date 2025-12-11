/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.b2bacceleratorservices.dao.impl.DefaultB2BAcceleratorCartToOrderCronJobModelDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.dto.JnjPageableData;


/**
 * The Class JnjCartToOrderCronJobModelDao for overrides OOTB API.
 * 
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjCartToOrderCronJobModelDao extends DefaultB2BAcceleratorCartToOrderCronJobModelDao
{
	protected static final String SKU = "SKU";
	protected static final String REPLENISHMENT_NO = "ReplenishmentNo";

	protected static final String FIND_CARTTOORDERCRONJOB_BY_USER_QUERY = "SELECT {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.PK + "} FROM { " + CartToOrderCronJobModel._TYPECODE + " as "
			+ CartToOrderCronJobModel._TYPECODE + " JOIN " + CartModel._TYPECODE + " as " + CartModel._TYPECODE + " ON {"
			+ CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.CART + "} = {" + CartModel._TYPECODE + ":"
			+ CartModel.PK + "}} WHERE {" + CartModel._TYPECODE + ":" + CartModel.USER + "} = ?user";

	protected static final String FIND_CARTTOORDERCRONJOB_BY_USER_AND_SKU_QUERY = " SELECT {CartToOrderCronJob:pk} FROM "
			+ "{ CartToOrderCronJob as CartToOrderCronJob JOIN Cart as Cart ON {CartToOrderCronJob:cart} = "
			+ "{Cart:pk}JOIN CartEntry as entry on {entry:order}={Cart:pk} "
			+ "JOIN JnjProduct As Prod on {entry:product}={Prod:pk} } WHERE UPPER({Prod:code}) like ?code and {Cart:user} = ?user ";

	protected static final String FIND_CARTTOORDERCRONJOB_BY_USER_AND_REPLINSH_CODE_QUERY = FIND_CARTTOORDERCRONJOB_BY_USER_QUERY
			+ " AND UPPER({" + CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.CODE + "}) like ?code";

	protected static final String FIND_CARTTOORDERCRONJOB_BY_USER_AND_STATUS_CODE_QUERY = " AND {"
			+ CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.ACTIVE + "} = ?status";


	protected static final String SORT_JOBS_BY_DATE_DESC = " ORDER BY {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.CREATIONTIME + "} DESC, {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.PK + "}";

	protected static final String SORT_JOBS_BY_CODE_DESC = " ORDER BY {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.CODE + "} DESC";

	protected static final String SORT_JOBS_BY_DATE = " ORDER BY {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.CREATIONTIME + "} , {" + CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.PK
			+ "}";

	protected static final String SORT_JOBS_BY_CODE = " ORDER BY {" + CartToOrderCronJobModel._TYPECODE + ":"
			+ CartToOrderCronJobModel.CODE + "}, {" + CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.CREATIONTIME
			+ "} , {" + CartToOrderCronJobModel._TYPECODE + ":" + CartToOrderCronJobModel.PK + "}";


	protected static final String SORT_DESC = " DESC";

	protected static final String SORT_JOBS_BY_PRICE = " ORDER BY {" + CartModel._TYPECODE + ":" + CartModel.TOTALPRICE + "}";

	protected final String LIKE_CHARACTER = "%";


	/**
	 * {@inheritDoc}
	 * 
	 * Added sort by option on top of OOTB
	 */
	/*
	 * This method is override for providing more options of sorting
	 */
	@Override
	public SearchPageData<CartToOrderCronJobModel> findPagedCartToOrderCronJobsByUser(final UserModel user,
			final PageableData pageableData)
	{
		String mainQuery = FIND_CARTTOORDERCRONJOB_BY_USER_QUERY;
		final Map<String, Object> queryParams = new HashMap<String, Object>(1);
		queryParams.put(OrderModel.USER, user);
		if (pageableData instanceof JnjPageableData)
		{
			final String searchBy = ((JnjPageableData) pageableData).getSearchBy();
			final String searchText = ((JnjPageableData) pageableData).getSearchText();
			final String status = ((JnjPageableData) pageableData).getStatus();
			if (StringUtils.isNotEmpty(searchText))
			{
				queryParams.put("code", LIKE_CHARACTER + searchText.trim().toUpperCase() + LIKE_CHARACTER); //Where clause in select is with upper case
				if (REPLENISHMENT_NO.equals(searchBy))
				{
					mainQuery = FIND_CARTTOORDERCRONJOB_BY_USER_AND_REPLINSH_CODE_QUERY;
				}
				else if (SKU.equalsIgnoreCase(searchBy))
				{
					mainQuery = FIND_CARTTOORDERCRONJOB_BY_USER_AND_SKU_QUERY;
				}
			}
			if (StringUtils.isNotEmpty(status) && !"all".equalsIgnoreCase(status))
			{
				queryParams.put("status", status);
				mainQuery = mainQuery + FIND_CARTTOORDERCRONJOB_BY_USER_AND_STATUS_CODE_QUERY;
			}
		}

		final List<SortQueryData> sortQueries = Arrays.asList(
				createSortQueryData("byDateDesc", mainQuery + SORT_JOBS_BY_DATE_DESC),
				createSortQueryData("byReplenishmentNumberDesc", mainQuery + SORT_JOBS_BY_CODE_DESC),
				createSortQueryData("byPriceDesc", mainQuery + SORT_JOBS_BY_PRICE + SORT_DESC),
				createSortQueryData("byDate", mainQuery + SORT_JOBS_BY_DATE),
				createSortQueryData("byReplenishmentNumber", mainQuery + SORT_JOBS_BY_CODE),
				createSortQueryData("byPrice", mainQuery + SORT_JOBS_BY_PRICE));

		return getPagedFlexibleSearchService().search(sortQueries, "byReplenishmentNumberDesc", queryParams, pageableData);
	}
}
