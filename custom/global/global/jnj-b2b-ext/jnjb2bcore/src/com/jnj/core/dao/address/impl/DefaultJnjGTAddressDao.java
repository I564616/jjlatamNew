/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.address.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.address.JnjGTAddressDao;


/**
 * The JnjGTAddressDaoImpl class contains definition all those method which are dealing with na address model and
 * defined in JnjGTAddressDao.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTAddressDao extends AbstractItemDao implements JnjGTAddressDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTAddressDao.class);
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected UserService userService;

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<AddressModel> getAddressByIdandSourceSysId(final String jnjId, final String sourceSysId)
	{
		final List<AddressModel> listAdress = new ArrayList<AddressModel>();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getAddressByIdandSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<AddressModel> results;

		ServicesUtil.validateParameterNotNull(jnjId, "Id must not be null");
		ServicesUtil.validateParameterNotNull(sourceSysId, "Source System Id must not be null");
		final Map queryParams = new HashMap();
		final String query = "select {pk} from {Address} where {owner} in ({{ select {pk} from {JnJB2BUnit} where {pk} in ({{ select {owner} from {Address} where {jnjaddressId}=?jnjAddressId and {sourceSysId}=?sourceSysId}}) and {sourceSysId}=?sourceSysId}}) and {jnjaddressId}=?jnjAddressId";
		queryParams.put("jnjAddressId", jnjId);
		queryParams.put("sourceSysId", sourceSysId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		results = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
		if (!results.isEmpty())
		{
			for (final AddressModel address : results)
			{
				listAdress.add(address);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getAddressByIdandSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return listAdress;

	}
	//3088 start
	public List getSearchShippingAddress(String searchitem, final String jnjaddressId)
	{
		final List<AddressModel> results;
		final List<AddressModel> listAdress = new ArrayList<AddressModel>();

		ServicesUtil.validateParameterNotNull(jnjaddressId, "Id must not be null");
		ServicesUtil.validateParameterNotNull(jnjaddressId, "searchitem  must not be null");
		final Map queryParams = new HashMap();
		LOGGER.debug("jnjAddressId :: " + jnjaddressId + " and searchitem :: " + searchitem);
		final String st = "select {a:pk} from {Address as a left join JnjB2bUnit as b on {a:owner} = {b:pk} join Country as c ON {a:country} = {c:pk}} where {b:uid}=?jnjAddressId and {a:active}='1' and {a:ShippingAddress} ='1'";


		final StringBuilder selectquery = new StringBuilder(st);
		searchitem = searchitem.toUpperCase();
		searchitem = "%" + searchitem + "%";
		selectquery
				.append("and (upper({a:company}) like upper(?searchitem) or upper({a:postalCode}) like upper(?searchitem)  or upper({a:streetName}) like upper(?searchitem) or upper({a:streetNumber}) like upper(?searchitem) or upper({a:town}) like upper(?searchitem) or upper({c:name}) like upper(?searchitem) or upper({a:jnjAddressId}) like upper(?searchitem))");
		queryParams.put("jnjAddressId", jnjaddressId);
		queryParams.put("searchitem", searchitem);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(selectquery);
		fQuery.addQueryParameters(queryParams);
		results = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public List<AddressModel> execute()
			{
				final List<AddressModel> addresses = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
				return addresses;
			}
		}, userService.getAdminUser());
		//results = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
		if (!results.isEmpty())
		{
			for (final AddressModel address : results)
			{
				listAdress.add(address);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getAddressByIdandSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return results;
	}
	
	public List getSearchBillingAddress(String searchitem, final String jnjaddressId)
	{
		final List<AddressModel> results;
		final List<AddressModel> listAdress = new ArrayList<AddressModel>();

		ServicesUtil.validateParameterNotNull(jnjaddressId, "Id must not be null");
		ServicesUtil.validateParameterNotNull(jnjaddressId, "searchitem  must not be null");
		final Map queryParams = new HashMap();
		LOGGER.debug("jnjAddressId :: " + jnjaddressId + " and searchitem :: " + searchitem);
		final String st = "select {a:pk} from {Address as a left join JnjB2bUnit as b on {a:owner} = {b:pk} join Country as c ON {a:country} = {c:pk}} where {b:uid}=?jnjAddressId and {a:active}='1' and {a:billingaddress} ='1'";


		final StringBuilder selectquery = new StringBuilder(st);
		searchitem = searchitem.toUpperCase();
		searchitem = "%" + searchitem + "%";
		selectquery
				.append("and (upper({a:company}) like upper(?searchitem) or upper({a:postalCode}) like upper(?searchitem)  or upper({a:streetName}) like upper(?searchitem) or upper({a:streetNumber}) like upper(?searchitem) or upper({a:town}) like upper(?searchitem) or upper({c:name}) like upper(?searchitem) or upper({a:jnjAddressId}) like upper(?searchitem))");
		queryParams.put("jnjAddressId", jnjaddressId);
		queryParams.put("searchitem", searchitem);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(selectquery);
		fQuery.addQueryParameters(queryParams);
		results = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public List<AddressModel> execute()
			{
				final List<AddressModel> addresses = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
				return addresses;
			}
		}, userService.getAdminUser());
		//results = getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
		if (!results.isEmpty())
		{
			for (final AddressModel address : results)
			{
				listAdress.add(address);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getAddressByIdandSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return results;
	}
  //3088 end
}
