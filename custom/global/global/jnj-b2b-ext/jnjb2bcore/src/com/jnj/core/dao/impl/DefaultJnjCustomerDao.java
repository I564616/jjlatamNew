/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.dao.impl.DefaultPagedB2BCustomerDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.JnjCustomerDao;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnJCommonUtil;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCustomerDao extends DefaultPagedB2BCustomerDao implements JnjCustomerDao
{
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	/** The Constant LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjCustomerDao.class);


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}



	//CR 31439:User Management 
	protected static final String FIND_B2BCUSTOMERS_IN_DESIRED_GROUPS = " SELECT DISTINCT {b2bcustomer:pk}, {b2bcustomer:name} as CustomerName, {b2bunit:name} as UnitName"
			+ " FROM { JnjB2BCustomer 				AS b2bcustomer "
			+ " JOIN   PrincipalGroupRelation 	AS b2bunitrelation 		 	ON {b2bunitrelation:source} = {b2bcustomer:pk} "
			+ " JOIN   B2BUnit 	  				AS b2bunit	 				ON {b2bunit:pk} = {b2bunitrelation:target} "
			+ " JOIN   PrincipalGroupRelation 	AS desiredgrouprelations	ON {desiredgrouprelations:source} = {b2bcustomer:pk} "
			+ " JOIN   UserGroup 				AS desiredgroups      		ON {desiredgroups:pk} = {desiredgrouprelations:target}} "
			+ " WHERE {desiredgroups:uid} IN (?usergroups)  and {desiredgroups:pk} = {b2bcustomer:creatorB2BUnit}";

	protected static final String ORDERBY_UNIT_NAME = " ORDER BY UnitName ";
	protected static final String ORDERBY_CUSTOMER_NAME = " ORDER BY LOWER(CustomerName) ";
	protected static final String FIND_NEW_REGISTERED_USERS = " SELECT {pk} FROM {JnjB2BCUSTOMER} WHERE {ACTIVE}=1 AND {LOGINDISABLED}=0 AND "
			+ "{registrationEmailSent}=0";
	protected static final String FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL = "SELECT DISTINCT {b2bcustomer:pk},{b2bcustomer:uid},{b2bcustomer:passwordchangedate},{b2bcustomer:mailSentForInterval},{b2bcustomer:emailNotification} "
			+ " FROM {JnjB2BCustomer AS b2bcustomer} "
			+ "where {b2bcustomer:active} = 1 and {b2bcustomer:logindisabled} = 0 "
			+ " and {b2bcustomer:passwordchangedate} IS NOT NULL";


	public static final int MAX_PAGE_LIMIT = 100;

	/**
	 * @param typeCode
	 */
	public DefaultJnjCustomerDao(final String typeCode)
	{
		super(typeCode);
		// YTODO Auto-generated constructor stub
	}

	@Override
	public SearchPageData<B2BCustomerModel> findPagedCustomersCaseInsenitiveByGroupMembership(final String sortCode,
			final PageableData pageableData, final String... userGroupid)
	{

		final List<SortQueryData> sortQueries = Arrays.asList(
				createSortQueryData("byName", FIND_B2BCUSTOMERS_IN_DESIRED_GROUPS + ORDERBY_CUSTOMER_NAME),
				createSortQueryData("byUnit", FIND_B2BCUSTOMERS_IN_DESIRED_GROUPS + ORDERBY_UNIT_NAME));

		return getPagedFlexibleSearchService().search(sortQueries, sortCode,
				Collections.singletonMap("usergroups", Arrays.asList(userGroupid)), pageableData);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.JnjCustomerDao#getNewlyRegisteredUsers()
	 */
	@Override
	public List<JnJB2bCustomerModel> getNewlyRegisteredUsers()
	{

		List<JnJB2bCustomerModel> newlyRegisteredUsers = new ArrayList<JnJB2bCustomerModel>();
		final String query = FIND_NEW_REGISTERED_USERS;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final SearchResult<JnJB2bCustomerModel> result = flexibleSearchService.search(fQuery);
		newlyRegisteredUsers = result.getResult();
		return newlyRegisteredUsers;

	}


	@Override
	public List<JnJB2bCustomerModel> getAllJnjUsers()
	{
		final List<JnJB2bCustomerModel> listOfJnjUser = new ArrayList<JnJB2bCustomerModel>();

		final List<JnJB2bCustomerModel> results;
		try
		{
			final String query = FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL;
			LOGGER.debug("qry for FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL in getAllJnjUsers.. "+query);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			results = getFlexibleSearchService().<JnJB2bCustomerModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnJB2bCustomerModel jnjUser : results)
				{
					listOfJnjUser.add(jnjUser);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("User list size (pwd expiry job mail) is " + listOfJnjUser.size());
		}
		return listOfJnjUser;
	}


}
