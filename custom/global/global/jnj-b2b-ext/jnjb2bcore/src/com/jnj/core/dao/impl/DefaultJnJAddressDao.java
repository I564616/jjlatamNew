/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultAddressDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnJAddressDao;
import com.jnj.core.util.JnJCommonUtil;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJAddressDao extends DefaultAddressDao implements JnJAddressDao
{

	/** The Constant LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnJAddressDao.class);

	@Autowired
	SessionService sessionService;
	@Autowired
	UserService userService;

	/** The paged flexible search service. */
	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;


	public SessionService getSessionService() {
		return sessionService;
	}

	public UserService getUserService() {
		return userService;
	}

	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	/**
	 * This method is used to get Address model on the basis of ID
	 */
	@Override
	public AddressModel getAddressById(final String jnjId)
	{
		return getAddressById(jnjId, false);
	}

	@Override
	public AddressModel getAddressById(final String jnjId, final boolean ignoreleadingZeros)
	{
		return getAddressById(jnjId, ignoreleadingZeros, false);
	}

	@Override
	public AddressModel getAddressById(final String jnjId, final boolean ignoreleadingZeros, final boolean checkActiveFlag)
	{
		final String METHOD_NAME = "METHOD_NAME";
		AddressModel result = null;
		String query = null;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final List<AddressModel> results;
		final String addressQueryIgnoreLeadingZeros = "select {pk} from {Address} where REGEXP_LIKE({JNJADDRESSID},'^[0]*" + jnjId
				+ "$')";
		final String addressQuery = "select {pk} from {Address} where {duplicate}='0' and {JNJADDRESSID}=?key";
		try
		{
			ServicesUtil.validateParameterNotNull(jnjId, "Id must not be null");
			final Map queryParams = new HashMap();
			if (ignoreleadingZeros)
			{
				query = addressQueryIgnoreLeadingZeros;
			}
			else
			{
				query = addressQuery;
				queryParams.put("key", jnjId);
			}
			if (checkActiveFlag)
			{
				query = query + " AND {active}=?activeFlag";
				queryParams.put("activeFlag", "1");
			}

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			results = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				result = results.get(0);
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + "model not found for given key -" + jnjId
					+ JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * This method is used to gets the list of address based on Id and owner's item type
	 */
	@Override
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjId)
	{
		return getAddressByIdandOnwerType(jnjId, false, false);
	}

	@Override
	public List<AddressModel> getAddressByIdandOnwerType(final String jnjId, final boolean ignoreleadingZeros,
			final boolean checkActiveFlag)
	{
		final String METHOD_NAME = "METHOD_NAME";
		final List<AddressModel> listAdress = new ArrayList<AddressModel>();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final String addressQueryLeadingZeros = "select top 1 {pk} from {address} where {owner} in ({{ select top 1 {unit:pk} from {jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner}}"
				+ "where REGEXP_LIKE({add:jnjaddressId},'^[0]*"
				+ jnjId
				+ "$')}}) and REGEXP_LIKE({JNJADDRESSID},'^[0]*" + jnjId + "$')";

		final String addressQuery = "select top 1 {pk} from {address} where {owner} in ({{ select top 1 {unit:pk} from {jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner}}  "
				+ "where {add:jnjaddressId}=?jnjAddressId}}) and {jnjaddressId}=?jnjAddressId";
		try
		{
			String query = null;
			ServicesUtil.validateParameterNotNull(jnjId, "Id must not be null");
			final Map queryParams = new HashMap();
			if (ignoreleadingZeros)
			{
				query = addressQueryLeadingZeros;
			}
			else
			{
				query = addressQuery;
				queryParams.put("jnjAddressId", jnjId);
			}
			if (checkActiveFlag)
			{
				query = query + " AND {active}=?activeFlag";
				queryParams.put("activeFlag", "1");
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getAddressByIdandOnwerType()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Get Addresses Query: " + fQuery);
			}

			final List<AddressModel> results = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<AddressModel> execute()
				{
					final List<AddressModel> results = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
					return results;
				}
			}, userService.getAdminUser());
			if (!results.isEmpty())
			{
				for (final AddressModel address : results)
				{
					listAdress.add(address);
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + "model not found for given key -" + jnjId
					+ JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return listAdress;
	}

	/**
	 * This method is used to get Address model on the basis of PK
	 */
	@Override
	public AddressModel getAddressByPK(final String pkString)
	{
		final String METHOD_NAME = "getAddressByPK";
		AddressModel result = null;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.isNotEmpty(pkString))
		{
			try
			{
				ServicesUtil.validateParameterNotNull(pkString, "pk must not be null");
				final Map queryParams = new HashMap();
				final String query = "select {pk} from {Address} where {pk}=?key";
				queryParams.put("key", pkString);
				final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
				fQuery.addQueryParameters(queryParams);
				result = (AddressModel) getFlexibleSearchService().searchUnique(fQuery);
			}
			catch (final ModelNotFoundException modelNotFound)
			{
				LOGGER.error("Fetching Address Using PK - " + METHOD_NAME + "model not found for given key -" + pkString);
			}
		}
		else
		{
			LOGGER.error("Recieved Null Address Id: " + METHOD_NAME + "- Returning Null without hitting the database.");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.dao.JnJAddressDao#findPagedAddresses(de.hybris.platform.commerceservices.search.pagedata.PageableData
	 * )
	 */
	/**
	 * This method is used to get address model on the basis of unitid and pagable data
	 */
	@Override
	public SearchPageData<AddressModel> findPagedAddresses(final PageableData pageableData, final String unitId)
	{
		final Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("unit", unitId);
		final String query = "SELECT {" + AddressModel.PK + "} FROM {" + AddressModel._TYPECODE + "} where {" + AddressModel.OWNER
				+ "}=?unit";

		return pagedFlexibleSearchService.search(query, queryParams, pageableData);
	}
}
