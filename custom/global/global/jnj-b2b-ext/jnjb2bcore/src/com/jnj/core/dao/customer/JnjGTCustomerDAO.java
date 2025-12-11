/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderEntryModel;

import java.util.List;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjIndirectCustomerModel;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;


/**
 * TODO:<Labanya-Class level comments missing>
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCustomerDAO
{
	public List<JnJB2bCustomerModel> getAllJnjGTUsers();

	/**
	 * This method validates the GLNs
	 *
	 * @param invalidCode
	 * @return String
	 */
	public String validateGLN(String invalidCode);
	
	
	/**
	 * This method validates the GLNs
	 *
	 * @param invalidCode
	 * @return String
	 */
	public String validateWwid(String wwid);

	/**
	 * This method is used to find all the PCM users who have their requestAccountIndicator set as true,meaning that the
	 * user is newly created and needs a password reset email to be sent for the same.
	 *
	 * @return List<String>
	 */
	public List<JnJB2bCustomerModel> getAllNewPCMUsers();

	/**
	 *
	 * This method fetches accounts for the admin user
	 *
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * @return accountsMaps
	 */
	public SearchPageData getAllAccountsMap(final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData);

	/**
	 * This method fetches accounts for the non-admin user
	 *
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * @return accountsMaps
	 */
	public SearchPageData<JnJB2BUnitModel> getAccountsMap(boolean isSectorSpecific, boolean isUCN, JnjGTPageableData pageableData);

	/**
	 * This method fetches the cut lines for sending notification email
	 *
	 * @return List<OrderEntryModel>
	 */
	public List<OrderEntryModel> fetchCutLineDetails();
	
	/**
	 *
	 * This method fetches b2bCustomer which contains he account
	 *
	 * @param accountNumber
	 */
	public List<JnJB2bCustomerModel> getUsersWithAccount(String accountNumber);

	/**
	 *
	 * This method fetches b2bCustomer which contains he account
	 *
	 * @param uid
	 */
	public List<JnJB2bCustomerModel> getUsersWithTerritoryWithB2bUnit(String uid);
	
	/**
	 * Generate list of all MDD Users
	 * 
	 */
	public List<JnJB2bCustomerModel> getAllMDDUsers();

	/**
	 * Generate unique list of account numbers for a given sales rep uid
	 * 
	 */
	public List<String> getUniqueB2BUnitCodesForRepUID(final String repUID);

	public JnJB2bCustomerModel getCustomerByUid(String uid);

	/**
	 * Generate sector specific account search query for admin.
	 *
	 * @param isSectorSpecific
	 *           the is sector specific
	 * @param isUCN
	 *           the is ucn
	 * @param pageableData
	 *           the pageable data
	 * @param queryParams
	 *           the query params
	 * @return the string
	 */
	public StringBuilder getAccountsMapQuery(final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData, final Map<String, String> queryParams);
	
	
	/**
	 * Fetch indirect customer name.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @return JnjIndirectCustomerModel
	 */
	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer);
	
	
	/**
	 * Fetch indirect customer name.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return JnjIndirectCustomerModel
	 */
	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer, CountryModel countryModel);
	
	
	/**
	 * Fetch indirect customers.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return the list
	 */
	List<JnjIndirectCustomerModel> fetchIndirectCustomers(String indirectCustomer, CountryModel countryModel);
	
	/**
	 * Returns a list of accounts for auto suggest search text
	 * @param searchText
	 * @param isAdminUser 
	 * @return
	 */
	public List<JnJB2BUnitModel> getAccountListForAutoSuggest(String searchText, boolean isAdminUser);


	/**
	 * This method gives the generated user list.
	 * @return
	 */
	public List<ArrayList<String>> extractActiveUserDetailList();
	

}
