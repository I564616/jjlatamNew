/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.company.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
//import de.hybris.platform.b2bacceleratorservices.dao.impl.DefaultPagedB2BCustomerDao;
import de.hybris.platform.b2b.dao.impl.DefaultPagedB2BCustomerDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSearchDTO;
import com.jnj.core.dao.company.JnjGTPagedB2BCustomerDao;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;


/**
 * This class Handles DatBase level interaction : For User Search And Pagintaion
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTPagedB2BCustomerDao extends DefaultPagedB2BCustomerDao implements
		JnjGTPagedB2BCustomerDao<B2BCustomerModel>
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjGTPagedB2BCustomerDao.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;


	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public UserService getUserService() {
		return userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	/**
	 * @param typeCode
	 */
	public DefaultJnjGTPagedB2BCustomerDao(final String typeCode)
	{
		super(typeCode);
		// YTODO Auto-generated constructor stub
	}

	/* This method handles search for the Customer when we click on User Management Page and Reset Button */
	@Override
	public SearchPageData<B2BCustomerModel> findPagedCustomers(final JnjGTPageableData pageableData)
	{
LOG.error("user - find");
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		final String sortByCriteria = pageableData.getSort();
		final List<String> b2bUnitIds = new ArrayList<String>();
		List<SortQueryData> sortQueries = null;
		final String query = Jnjb2bCoreConstants.UserSearch.FIND_B2BCUSTOMERS_QUERY;
LOG.error("QUERY..."+query);		
		/***********  Comment the code to display all the users from Global account ***********/
		/*final String query = Jnjb2bCoreConstants.UserSearch.FIND_B2BCUSTOMERS_QUERY
				+ Jnjb2bCoreConstants.UserSearch.SEARCH_BY_SELECTED_B2B_UNIT;*/		
		
		/*
		 * Search for the Users which are in All the Groups Except Admin as well as the B2BUnit should be selected One
		 */
LOG.error("tier1"+JnJCommonUtil.getValues(
		Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
LOG.error("tier2"+JnJCommonUtil.getValues(
		Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));

if (sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE).equals(
				Jnjb2bCoreConstants.UserSearch.USER_TIER1))

		{
LOG.error("tier1");
			queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
			b2bUnitIds.add(jnjGTB2BUnitService.getCurrentB2BUnit().getUid());

		}
		/*
		 * Search for the Users which are in All the Groups as well as the B2BUnit should be selected One as well as
		 * Global Unit
		 */
		else
		{
LOG.error("tier2");			
			queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));			
			
			/***********  Comment the code to display all the users from Global account ***********/
			/*b2bUnitIds.add(jnjGTB2BUnitService.getCurrentB2BUnit().getUid());
			b2bUnitIds.add(Jnjb2bCoreConstants.UserSearch.GLOBAL_ACCOUNT_ID);
			queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));*/			
		}
		
		/***********  Comment the code to display all the users from Global account ***********/
		//queryParams.put(Jnjb2bCoreConstants.UserSearch.B2B_UNIT_IDS, b2bUnitIds);
		LOG.error("QUERY PARAM..."+queryParams);		
		sortQueries = Arrays.asList(
				createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_NAME, query
						+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_NAME),
				createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_ROLE, query
						+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_ROLE),
				createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_STATUS, query
						+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_STATUS));
		final List<SortQueryData> sortQueriesT = sortQueries;
		//Running the  Query As Admin
		return sessionService.executeInLocalView(new SessionExecutionBody()

		{
			@Override
			public SearchPageData execute()

			{
				return getPagedFlexibleSearchService().search(sortQueriesT, sortByCriteria, queryParams, pageableData);

			}

		}, userService.getAdminUser());

	}

	/* This method handles search for the Customer when we click on Search Button */

	@Override
	public SearchPageData<B2BCustomerModel> searchCustomers(final JnjGTPageableData pageableData)
	{
LOG.error("USER - SEARCH BY CRIERIA");		
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		final List<JnjGTSearchDTO> searchDtoList = pageableData.getSearchDtoList();
		final String sortByCriteria = pageableData.getSort();
		LOG.error("tier1"+JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
		LOG.error("tier2"+JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
		//Check Whether the user  is Tier1 or Tier2
		if (sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE).equals(
				Jnjb2bCoreConstants.UserSearch.USER_TIER2))
		{
LOG.error("tier2");
			//Search for the Users which are in All the Groups except Admin
			queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER2, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
		}
		else
		{
LOG.error("tier1");
			//Search for the Users which are in All the Groups as well as  Admin
			queryParams.put(Jnjb2bCoreConstants.UserSearch.USERGROUPS, JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.UserSearch.DESIRED_GROUPS_TIER1, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR));
		}

		List<SortQueryData> sortQueries = null;
		String query = Jnjb2bCoreConstants.UserSearch.FIND_B2BCUSTOMERS_QUERY;
LOG.error("QRY.. "+query);		
		if (CollectionUtils.isNotEmpty(searchDtoList))
		{
			for (final JnjGTSearchDTO searchDto : searchDtoList)
			{
				//This method search User Based On Sector
				query = searchBySectorQuery(query, searchDto);
				//This method search User Based On Phone
				query = searchByPhoneQuery(queryParams, query, searchDto);
				//This method search User Based On Name
				query = searchByNameQuery(queryParams, query, searchDto);
				//This method search User Based On Status
				query = searchByStatus(queryParams, query, searchDto);
				//This method search User Based On Email
				query = searchByEmail(queryParams, query, searchDto);
				//This method search User Based On Account
				query = searchByAccount(queryParams, query, searchDto);
				//This method search User Based On Role
				query = searchByRole(queryParams, query, searchDto);
			}
			//Creating Sort Queries 
			sortQueries = Arrays.asList(
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_NAME, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_NAME),
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_ROLE, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_ROLE),
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_STATUS, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_STATUS));
		}
		//Creating Sort Queries   for Default Search When there is no Search Criteria
		else
		{
			sortQueries = Arrays.asList(
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_NAME, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_NAME),
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_ROLE, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_ROLE),
					createSortQueryData(Jnjb2bCoreConstants.UserSearch.SORT_USER_BY_STATUS, query
							+ Jnjb2bCoreConstants.UserSearch.ORDER_BY_STATUS));

		}
		LOG.error("QRY(CRITERIA).. "+query);
		LOG.error("QRY PARAMS.. "+queryParams);
		//Running the  Query As Admin
		final List<SortQueryData> sortQueriesT = sortQueries;
		return sessionService.executeInLocalView(new SessionExecutionBody()

		{
			@Override
			public SearchPageData execute()

			{
				return getPagedFlexibleSearchService().search(sortQueriesT, sortByCriteria, queryParams, pageableData);

			}

		}, userService.getAdminUser());

	}



	/**
	 * This method search User Based On Account
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return
	 */
	protected String searchByAccount(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.ACCOUNTNUMBER))
		{
			queryParams.put("searchAccountNumber", Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ searchDto.getSearchValue().trim() + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_ACCOUNTNUMER;
		}
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.ACCOUNTNAME))
		{
			queryParams.put("searchAccountName", Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ searchDto.getSearchValue().trim().toLowerCase() + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_ACCOUNTNAME;
		}
		return query;
	}


	/**
	 * This method search User Based On Email
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return
	 */
	protected String searchByEmail(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.EMAIL))
		{
			queryParams.put("searchByEmail", Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ searchDto.getSearchValue().trim().toLowerCase() + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_EMAIL;
		}
		return query;
	}


	/**
	 * This method search User Based On Status
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return String
	 */
	protected String searchByStatus(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.STATUS)
				&& !searchDto.getSearchValue().equalsIgnoreCase("ALL"))
		{

			queryParams.put("searchStatus", searchDto.getSearchValue().trim());
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_STATUS;

		}
		return query;
	}


	/**
	 * This method search User Based On Name
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return String
	 */
	protected String searchByNameQuery(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.LASTNAME))
		{
			queryParams.put("searchLastName", Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ Jnjb2bCoreConstants.UserSearch.SPACE + searchDto.getSearchValue().trim().toLowerCase()
					+ Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_LASTNAME;

		}

		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.FIRSTNAME))
		{
			queryParams.put("searchFirstName", Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ searchDto.getSearchValue().trim().toLowerCase() + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_FIRSTNAME;

		}
		return query;
	}


	/**
	 * This method search User Based On Phone
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return String
	 */
	protected String searchByPhoneQuery(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.PHONE))
		{
			queryParams.put(Jnjb2bCoreConstants.UserSearch.SEARCH_PHONE, Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER
					+ searchDto.getSearchValue().trim() + Jnjb2bCoreConstants.UserSearch.LIKE_WILDCARD_CHARACTER);
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_PHONE;

		}
		return query;
	}


	/**
	 * This method search User Based On Sector
	 * 
	 * @param query
	 * @param searchDto
	 * @return String
	 */
	protected String searchBySectorQuery(String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.SECTOR))
		{
			if (searchDto.getSearchValue().trim().equals(Jnjb2bCoreConstants.SalesAlignment.MDD))
			{
				query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_MDD;
			}
			else if (searchDto.getSearchValue().trim().equals(Jnjb2bCoreConstants.SalesAlignment.CONSUMER))
			{
				query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_CONS;
			}
			else if (searchDto.getSearchValue().trim().equals(Jnjb2bCoreConstants.SalesAlignment.PHARMA))
			{
				query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_PHARMA;
			}

		}
		return query;
	}



	/**
	 * This method search User Based On Role
	 * 
	 * @param queryParams
	 * @param query
	 * @param searchDto
	 * @return String
	 */
	protected String searchByRole(final Map<String, Object> queryParams, String query, final JnjGTSearchDTO searchDto)
	{
		if (searchDto.getSearchBy().equalsIgnoreCase(Jnjb2bCoreConstants.UserSearch.ROLE)
				&& !searchDto.getSearchValue().equalsIgnoreCase("ALL"))
		{
			queryParams.put("searchRole", searchDto.getSearchValue().trim());
			query += Jnjb2bCoreConstants.UserSearch.SEARCH_BY_ROLE;

		}
		return query;
	}


}
