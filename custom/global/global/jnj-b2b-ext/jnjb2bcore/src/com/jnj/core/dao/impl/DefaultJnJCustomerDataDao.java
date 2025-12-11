/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.UpsertCustomer;
import com.jnj.core.dao.JnJCustomerDataDao;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.model.OldPasswordModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.core.util.JnJCommonUtil;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJCustomerDataDao extends AbstractItemDao implements JnJCustomerDataDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnJCustomerDataDao.class);

	@Autowired
	SessionService sessionService;
	@Autowired
	UserService userService;



	public SessionService getSessionService() {
		return sessionService;
	}

	public UserService getUserService() {
		return userService;
	}

	/**
	 * this method is used to get Sales Org Customer Model model for given customer ID and sector
	 */

	@Override
	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeBylId(final String customerId, final String sector)
	{
		final String METHOD_NAME = "getJnJSalesOrgCustomerModeBylId";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnJSalesOrgCustomerModel customerModel = null;
		try
		{

			ServicesUtil.validateParameterNotNull(customerId, "Type must not be null");
			ServicesUtil.validateParameterNotNull(sector, " processed Indentfier must not be null");
			final Map queryParams = new HashMap();
			final String query = "select {pk} from {JnJSalesOrgCustomer} where {customerId}=?customerId and {sector}=?sector";
			queryParams.put("customerId", customerId);
			queryParams.put("sector", sector);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			//final JnJSalesOrgCustomerModel result = (JnJSalesOrgCustomerModel) flexibleSearchService.search(fQuery);
			final List<JnJSalesOrgCustomerModel> elements = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnJSalesOrgCustomerModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<JnJSalesOrgCustomerModel> result = flexibleSearchService.<JnJSalesOrgCustomerModel> search(fQuery)
							.getResult();
					return result;
				}
			}, userService.getAdminUser());


			if (CollectionUtils.isNotEmpty(elements))
			{
				customerModel = elements.get(0);
			}
		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
		{
			LOGGER.warn(Logging.UPSERT_CUSTOMER_NAME + "-" + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + "-" + exception.getLocalizedMessage()
					+ JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return customerModel;
	}

	/**
	 * this method is used to get Secret Question for given User ID.
	 * 
	 * @param userId
	 *           the user id
	 * @return the secret questions for user
	 */
	@Override
	public List<SecretQuestionsAndAnswersModel> getSecretQuestionsForUser(final String userId)
	{
		final String METHOD_NAME = "getSecretQuestionsForUser()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(userId, "Type must not be null");

		final Map queryParams = new HashMap();
		final String query = "select {" + SecretQuestionsAndAnswersModel.PK + "}" + "from {"
				+ SecretQuestionsAndAnswersModel._TYPECODE + "}" + "where {customerUid}=?userId";
		queryParams.put("userId", userId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

		fQuery.addQueryParameters(queryParams);
		final SearchResult<SecretQuestionsAndAnswersModel> searchResult = getFlexibleSearchService().search(fQuery);
		final List<SecretQuestionsAndAnswersModel> secretQuestions = searchResult.getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return secretQuestions;
	}

	@Override
	public List<CountryModel> getCountries()
	{
		List<CountryModel> result = null;
		String siteName = StringUtils.EMPTY;
		try
		{
			final Map queryParams = new HashMap();
			String query = null;
			if (ObjectUtils.isNotEmpty(getSessionService().getCurrentSession())) {
				siteName = getSessionService().getCurrentSession().getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			}

			if (Jnjb2bCoreConstants.MDD.equalsIgnoreCase(siteName) || Jnjb2bCoreConstants.CONS.equalsIgnoreCase(siteName))
			{
				query = "select {pk} from {country}";
			}
			else
			{
				query = "select {pk} from {country} where {active}=1";
			}

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Executing query for obtaining country records.");
			}

			result = getFlexibleSearchService().<CountryModel> search(fQuery).getResult();
		}
		catch (final Exception e)
		{
			LOGGER.error("Error getting countries", e);
		}
		return result;
	}

	@Override
	public List<CountryModel> getRegions(final String country)
	{
		List<CountryModel> result = null;
		try
		{
			final Map queryParams = new HashMap();

			ServicesUtil.validateParameterNotNull(country, "country must not be null");
			queryParams.put("country", country);

			final String query = "select {pk} from {country} where {isocode}=?country";

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Executing query for obtaining country records.");
			}

			result = getFlexibleSearchService().<CountryModel> search(fQuery).getResult();

		}

		catch (final Exception exp)
		{
			LOGGER.error("Error while executing query to get Cross Reference Table records in JnJCrossReferenceDao class - "
					+ exp.getMessage());
		}
		return result;
	}


	@Override
	public Date getLastPasswordChangeDate(final String loginId)
	{
		final String METHOD_NAME = "getLastPasswordChangeDate()";

		Date result = null;
		JnJB2bCustomerModel fetchedVal = null;
		try
		{
			final Map queryParams = new HashMap();
			final String query = "select {pk} from {jnjb2bcustomer} where {email}=?email";
			queryParams.put("email", loginId);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			fetchedVal = (JnJB2bCustomerModel) getFlexibleSearchService().searchUnique(fQuery);
			result = fetchedVal.getPasswordChangeDate();

		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
		{
			LOGGER.warn(this.getClass() + "-" + METHOD_NAME + " model not found or ambiguous identifier exception for given ID"
					+ "-" + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	@Override
	public List<OldPasswordModel> getOldPasswordList(final String loginId)
	{
		List<OldPasswordModel> result = null;
		try
		{
			final Map queryParams = new HashMap();

			ServicesUtil.validateParameterNotNull(loginId, "country must not be null");
			queryParams.put("email", loginId);

			final String query = "select {oldPasswords} from {jnjb2bcustomer} where {email}=?email";

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Executing query for obtaining country records.");
			}

			result = getFlexibleSearchService().<OldPasswordModel> search(fQuery).getResult();

		}

		catch (final Exception exp)
		{
			LOGGER.error("Error while executing query to get Old Password  in JnJCustomerDataDaoImpl class - " + exp.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.JnJCustomerDataDao#getJnJSalesOrgCustomerModeBylId(java.lang.String)
	 */
	@Override
	public List<JnJSalesOrgCustomerModel> getJnJSalesOrgCustomerModeBylId(final String customerNumber)
	{

		final String METHOD_NAME = "getJnJSalesOrgCustomerModeBylId";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		List<JnJSalesOrgCustomerModel> jnJSalesOrgCustomerModelList = null;
		try
		{

			ServicesUtil.validateParameterNotNull(customerNumber, "Type must not be null");
			final Map queryParams = new HashMap();
			final String query = "select {pk} from {JnJSalesOrgCustomer} where {customerId}=?customerId";
			queryParams.put("customerId", customerNumber);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			//final JnJSalesOrgCustomerModel result = (JnJSalesOrgCustomerModel) flexibleSearchService.search(fQuery);
			jnJSalesOrgCustomerModelList = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnJSalesOrgCustomerModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<JnJSalesOrgCustomerModel> result = flexibleSearchService.<JnJSalesOrgCustomerModel> search(fQuery)
							.getResult();
					return result;
				}
			}, userService.getAdminUser());


		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
		{
			LOGGER.warn(Logging.UPSERT_CUSTOMER_NAME + "-" + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + "-" + exception.getLocalizedMessage()
					+ JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return jnJSalesOrgCustomerModelList;

	}

	/**
	 * this method is used to get the indirect customer on the basis of Country
	 */
	@Override
	public List<JnjIndirectCustomerModel> getIndirectCustomer(final String country)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIndirectCustomer()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//To validate the incoming parameter
		ServicesUtil.validateParameterNotNull(country, "Country Id must not be null");
		final Map queryParams = new HashMap();
		final String query = UpsertCustomer.INDIRECT_CUSTOMER_QUERY;
		queryParams.put(UpsertCustomer.COUNTRY_STRING, country);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIndirectCustomer()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Indirect Customer Query " + fQuery);
		}

		final List<JnjIndirectCustomerModel> jnjConfigModelList = getFlexibleSearchService().<JnjIndirectCustomerModel> search(
				fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIndirectCustomer()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjConfigModelList;
	}



	/**
	 * this method is used to get the indirect customer on the basis of Country and indirect customer
	 */
	@Override
	public JnjIndirectCustomerModel getIndirectCustomerByIdCountry(final String indirectCustomer, final String country)
	{
		final String METHOD_NAME = "getIndirectCustomerByIdCountry ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIndirectCustomer()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}


		JnjIndirectCustomerModel jnjIndirectCustomerModel = null;
		try
		{
			ServicesUtil.validateParameterNotNull(country, "Country Id must not be null");
			final Map queryParams = new HashMap();
			final String query = UpsertCustomer.INDIRECT_CUSTOMER_QUERY_FOR_ID_COUNTRY;
			queryParams.put(UpsertCustomer.COUNTRY_STRING, country);
			queryParams.put(UpsertCustomer.INDIRECT_CUSTOMER_STRING, indirectCustomer);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getIndirectCustomer()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Indirect Customer Query " + fQuery);
			}
			jnjIndirectCustomerModel = (JnjIndirectCustomerModel) getFlexibleSearchService().searchUnique(fQuery);
		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException exception)
		{
			LOGGER.warn(Logging.UPSERT_CUSTOMER_NAME + "-" + METHOD_NAME
					+ "model not found oR ambiguous identifier exception for given ID" + "-" + exception.getLocalizedMessage()
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return jnjIndirectCustomerModel;
	}
	
	/**
	 * @param countryList
	 * @param cencaCountryList
	 */
	public static void filterCencaCountryList(final StringTokenizer countryList, final List<String> cencaCountryList)
	{
		ServicesUtil.validateParameterNotNull(countryList, "Country List  must not be null");
		ServicesUtil.validateParameterNotNull(cencaCountryList, "Cenca Country List reference must not be null");

		while (countryList.hasMoreElements())
		{
			final String token = countryList.nextToken();
			if (!(token.toString().equalsIgnoreCase(Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL)
					|| token.toString().equalsIgnoreCase(Jnjb2bCoreConstants.COUNTRY_ISO_MEXICO) || token.toString().equalsIgnoreCase(
					Jnjb2bCoreConstants.COUNTRY_ISO_EQUADOR)))
			{
				cencaCountryList.add(token);
			}
		}
	}

}
