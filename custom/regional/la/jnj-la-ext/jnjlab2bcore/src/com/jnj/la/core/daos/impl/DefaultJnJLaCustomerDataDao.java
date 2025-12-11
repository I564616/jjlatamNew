/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.daos.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.impl.DefaultJnJCustomerDataDao;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.UpsertCustomer;
import com.jnj.la.core.daos.JnJLaCustomerDataDao;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;


/**
 * @author mpanda3
 * @version 1.0
 */
public class DefaultJnJLaCustomerDataDao extends DefaultJnJCustomerDataDao implements JnJLaCustomerDataDao
{
	private static final Class currentClass = DefaultJnJLaCustomerDataDao.class;

	private static final String LATAM_CUSTOMER_DATA_DAO = "Latam Customer Data Dao";

	private static final String COUNTRY_ERROR_MESSAGE = "Country Id must not be null";
	public static final String INDIRECT_CUSTOMER_QUERY_FOR_COUNTRY_AND_TERM = "SELECT {pk} FROM {JnjIndirectCustomer} "
			+ "WHERE {country} IN (?countries) " + "AND ( {indirectCustomer} LIKE CONCAT( '%', CONCAT(?indirectCustomer, '%' ) ) "
			+ "OR {indirectCustomerName} LIKE CONCAT( '%', CONCAT(?indirectCustomerName,'%' ) ))";

	public static final String INDIRECT_PAYER_QUERY_FOR_COUNTRY_AND_TERM = "SELECT {pk} FROM {JnjIndirectPayer} "
			+ "WHERE {country} IN (?countries) AND ( {indirectPayer} LIKE CONCAT( '%', CONCAT(?indirectPayer, '%' ) ) "
			+ "OR {indirectPayerName} LIKE CONCAT( '%', CONCAT(?indirectPayerName,'%' ) ))";

	public static final String UNIQUE_INDIRECT_CUSTOMER_QUERY = "SELECT {pk} FROM {JnjIndirectCustomer} WHERE {country} IN (?countries) AND {indirectCustomer} = ?indirectCustomer";

	public static final String UNIQUE_INDIRECT_PAYER_QUERY = "SELECT {pk} FROM {JnjIndirectPayer} WHERE {country} IN (?countries) AND {indirectPayer} =?indirectPayer";

	public static final String INDIRECT_PAYER_QUERY = "select {pk} from {JnjIndirectPayer} where {country}=?country";

	public static final int INDIRECT_MAX_COUNT = 10;
	
	public static final String ACTIVE_USER_NOTIFICATION_PREFERENCE = "Select {pref.pk} from {JnJLaUserAccountPreference as pref JOIN JnJEmailPeriodicity as period on {period.pk}={pref.periodicity}"
	 + " JOIN JnJB2bCustomer as user on {user.pk} = {pref.user}} where {period.code} in ('CONSOLIDATED','IMMEDIATE','DAILY') order by {user.uid} asc";
	
	public static final String GET_CUSTOMERS_QUERY = "select {pk} from {JnJB2bCustomer}";

	@Override
	public JnjIndirectPayerModel getIndirectPayerByIdCountry(final String indirectPayer, final String country)
	{
		final String methodName = "getIndirectPayerByIdCountry()";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);

		JnjIndirectPayerModel jnjIndirectPayerModel = null;
		try
		{
			ServicesUtil.validateParameterNotNull(country, COUNTRY_ERROR_MESSAGE);
			final Map<String, Object> queryParams = new HashMap<>();
			final String query = UpsertCustomer.INDIRECT_PAYER_QUERY_FOR_ID_COUNTRY;
			queryParams.put(UpsertCustomer.COUNTRY_STRING, country);
			queryParams.put(UpsertCustomer.INDIRECT_PAYER_STRING, indirectPayer);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);

			JnjGTCoreUtil.logInfoMessage(LATAM_CUSTOMER_DATA_DAO, methodName, "Indirect Payer Query ", currentClass);

			jnjIndirectPayerModel = (JnjIndirectPayerModel) getFlexibleSearchService().searchUnique(fQuery);
		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(LATAM_CUSTOMER_DATA_DAO, methodName,
					"model not found or ambiguous identifier exception for given ID", exception, currentClass);
		}

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);

		return jnjIndirectPayerModel;

	}

	@Override
	public List<JnjIndirectPayerModel> getIndirectPayer(final String country)
	{
		final String methodName = "getIndirectPayer()";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);

		ServicesUtil.validateParameterNotNull(country, COUNTRY_ERROR_MESSAGE);

		final Map<String, Object> queryParams = new HashMap<>();
		final String query = UpsertCustomer.INDIRECT_PAYER_QUERY;

		queryParams.put(UpsertCustomer.COUNTRY_STRING, getCountryModelByIsoOrPk(country));

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName,
				Jnjb2bCoreConstants.Logging.HYPHEN + "Indirect Payer Query: " + fQuery, currentClass);

		final List<JnjIndirectPayerModel> jnjIndirectPayerList = getFlexibleSearchService().<JnjIndirectPayerModel> search(fQuery)
				.getResult();

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);
		return jnjIndirectPayerList;
	}

	@Override
	public List<JnjIndirectPayerModel> getIndirectPayer(final String siteDefaultCountry, final String term)
	{
		final String methodName = "getIndirectPayer()";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);
		ServicesUtil.validateParameterNotNull(siteDefaultCountry, COUNTRY_ERROR_MESSAGE);

		final String query = INDIRECT_PAYER_QUERY_FOR_COUNTRY_AND_TERM;

		final Map queryParams = new HashMap<>();
		final List<String> countries = getCountriesFromSite(siteDefaultCountry);
		queryParams.put(UpsertCustomer.COUNTRIES_STRING, countries);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setCount(INDIRECT_MAX_COUNT);
		queryParams.put(UpsertCustomer.INDIRECT_PAYER_STRING, term);
		queryParams.put(UpsertCustomer.INDIRECT_PAYER_NAME_STRING, term);
		fQuery.addQueryParameters(queryParams);

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName,
				Jnjb2bCoreConstants.Logging.HYPHEN + "Indirect Payer Query: " + fQuery, currentClass);

		final List<JnjIndirectPayerModel> jnjIndirectPayerList = getFlexibleSearchService().<JnjIndirectPayerModel> search(fQuery)
				.getResult();

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);
		return jnjIndirectPayerList;
	}

	@Override
	public List<JnjIndirectCustomerModel> getIndirectCustomer(final String siteDefaultCountry, final String term)
	{
		final String methodName = "getIndirectCustomer(country, term)";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);
		ServicesUtil.validateParameterNotNull(siteDefaultCountry, COUNTRY_ERROR_MESSAGE);

		final String query = INDIRECT_CUSTOMER_QUERY_FOR_COUNTRY_AND_TERM;

		final Map queryParams = new HashMap<>();
		final List<String> countries = getCountriesFromSite(siteDefaultCountry);
		queryParams.put(UpsertCustomer.COUNTRIES_STRING, countries);
		queryParams.put(UpsertCustomer.INDIRECT_CUSTOMER_STRING, term);
		queryParams.put(UpsertCustomer.INDIRECT_CUSTOMER_NAME_STRING, term);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setCount(INDIRECT_MAX_COUNT);
		fQuery.addQueryParameters(queryParams);

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);

		final List<JnjIndirectCustomerModel> jnjIndirectCustomerList = getFlexibleSearchService()
				.<JnjIndirectCustomerModel> search(fQuery).getResult();

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);
		return jnjIndirectCustomerList;
	}

	@Override
	public JnjIndirectCustomerModel getIndirectCustomerModel(final String country, final String indirectCustomer)
	{
		final String methodName = "getIndirectCustomerModel(country, indirectCustomer)";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);
		ServicesUtil.validateParameterNotNull(country, COUNTRY_ERROR_MESSAGE);

		final String query = UNIQUE_INDIRECT_CUSTOMER_QUERY;

		final Map queryParams = new HashMap<>();
		final List<String> countries = getCountriesFromSite(country);
		queryParams.put(UpsertCustomer.COUNTRIES_STRING, countries);
		queryParams.put(UpsertCustomer.INDIRECT_CUSTOMER_STRING, indirectCustomer);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);

		JnjIndirectCustomerModel jnjIndirectCustomerModel = null;
		try
		{
			jnjIndirectCustomerModel = getFlexibleSearchService().<JnjIndirectCustomerModel> searchUnique(fQuery);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(LATAM_CUSTOMER_DATA_DAO, methodName,
					"An exception occured while trying to fetch the indirect customer + " + indirectCustomer + " for countries: "
							+ countries.toString(),
					exception, currentClass);
		}

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);
		return jnjIndirectCustomerModel;
	}

	@Override
	public JnjIndirectPayerModel getIndirectPayerModel(final String country, final String indirectPayer)
	{
		final String methodName = "getIndirectPayerModel(country, indirectPayer)";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
				currentClass);
		ServicesUtil.validateParameterNotNull(country, COUNTRY_ERROR_MESSAGE);

		final String query = UNIQUE_INDIRECT_PAYER_QUERY;

		final Map queryParams = new HashMap<>();
		final List<String> countries = getCountriesFromSite(country);
		queryParams.put(UpsertCustomer.COUNTRIES_STRING, countries);
		queryParams.put(UpsertCustomer.INDIRECT_PAYER_STRING, indirectPayer);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);


		JnjIndirectPayerModel jnjIndirectPayerModel = null;
		try
		{
			jnjIndirectPayerModel = getFlexibleSearchService().<JnjIndirectPayerModel> searchUnique(fQuery);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(LATAM_CUSTOMER_DATA_DAO, methodName,
					"An exception occured while trying to fetch the indirect payer + " + indirectPayer + " for countries: "
							+ countries.toString(),
					exception, currentClass);
		}
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_DAO, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);
		return jnjIndirectPayerModel;
	}
	
	@Override
	public List<JnJB2bCustomerModel> getActiveUsers() {				
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_CUSTOMERS_QUERY);
		return getFlexibleSearchService().<JnJB2bCustomerModel> search(fQuery).getResult();
	}

	@Override
	public List<JnJLaUserAccountPreferenceModel> getActiveUsersNotification() {		
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(ACTIVE_USER_NOTIFICATION_PREFERENCE);		
		return getFlexibleSearchService().<JnJLaUserAccountPreferenceModel> search(fQuery).getResult();		
	}


	public CountryModel getCountryModelByIsoOrPk(final String country)
	{
		ServicesUtil.validateParameterNotNull(country, "Country Id/Isocode must not be null");

		final Map<String, String> queryParams = new HashMap<>();
		String query;
		if (country.length() > 3)
		{
			query = "select {pk} from {Country} where {pk}=?pk";
			queryParams.put(UpsertCustomer.COUNTRY_PK_STRING, country);
		}
		else
		{
			query = "select {pk} from {Country} where {isocode}=?isocode";
			queryParams.put(UpsertCustomer.ISOCODE_STRING, country);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		return (CountryModel) getFlexibleSearchService().searchUnique(fQuery);
	}

	private List<String> getCountriesFromSite(final String siteDefaultCountry)
	{
		final List<String> countries = new ArrayList<>();

		switch (siteDefaultCountry)
		{
			case Jnjlab2bcoreConstants.COUNTRY_ISO_ARGENTINA:
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_AR_VALID_COUNTRIES)));
				break;
			case Jnjlab2bcoreConstants.COUNTRY_ISO_URUGUAY:
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_UY_VALID_COUNTRIES)));
				break;
			case Jnjlab2bcoreConstants.COUNTRY_ISO_PERU:
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_PE_VALID_COUNTRIES)));
				break;
			case Jnjlab2bcoreConstants.COUNTRY_ISO_PUERTORICO:
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_PR_VALID_COUNTRIES)));
				break;
			case Jnjlab2bcoreConstants.COUNTRY_ISO_PANAMA:
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_CENCA_VALID_COUNTRIES)));
				countries.addAll(getStoreCountriesList(Config.getParameter(Jnjlab2bcoreConstants.KEY_PR_VALID_COUNTRIES)));
				break;
			default:
				countries.add(getCountryModelByIsoOrPk(siteDefaultCountry).getPk().toString());
				break;
		}

		return countries;
	}

	private Collection<? extends String> getStoreCountriesList(final String propertyName)
	{
		final ArrayList<String> finalContriesPkList = new ArrayList<>();
		if (propertyName != null)
		{
			final List<String> contriesList = Arrays.asList(propertyName.split(","));
			for (final String contryIsocode : contriesList)
			{
				finalContriesPkList.add(getCountryModelByIsoOrPk(contryIsocode).getPk().toString());
			}
		}
		return finalContriesPkList;
	}

}
