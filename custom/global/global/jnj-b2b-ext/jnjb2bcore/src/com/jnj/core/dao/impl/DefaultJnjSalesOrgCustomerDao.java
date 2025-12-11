/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjSalesOrgCustomerDao;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.util.JnJCommonUtil;


/**
 * This class is used to get the Sales Org Customer Details
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjSalesOrgCustomerDao implements JnjSalesOrgCustomerDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjSalesOrgCustomerDao.class);
	@Autowired
	FlexibleSearchService flexibleSearchService;
	@Autowired
	SessionService sessionService;
	@Autowired
	UserService userService;
	

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}


	public SessionService getSessionService() {
		return sessionService;
	}


	public UserService getUserService() {
		return userService;
	}


	/**
	 * This method is used to get the customer ID for customer id and sector if it is exist in data base .
	 * 
	 * @param customerId
	 *           String
	 * @param sector
	 *           String
	 */
	@Override
	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeById(final String customerId, final String sector)
	{
		final String METHOD_NAME = "getJnJSalesOrgCustomerModeById()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnJSalesOrgCustomerModel customerModel = null;
		try
		{

			ServicesUtil.validateParameterNotNull(customerId, "Type must not be null");
			ServicesUtil.validateParameterNotNull(sector, " processed Indentfier must not be null");
			final Map queryParams = new HashMap();
			final String query = Jnjb2bCoreConstants.SalesOrgCustomer.FETCH_SALES_ORG_CUSTOMER_MODEL;
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Query  : " + query);
			}
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

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + " - " + METHOD_NAME + Logging.END_OF_METHOD
						+ JnJCommonUtil.getCurrentDateTime());
			}
		}
		catch (final UnknownIdentifierException unknownIdentifierException)
		{
			LOGGER.warn(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + "-" + METHOD_NAME
					+ "key for which you are performing flexi search , identifier not found" + "-"
					+ unknownIdentifierException.getMessage() + JnJCommonUtil.getCurrentDateTime());
			throw unknownIdentifierException;
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + "-" + METHOD_NAME + "model not found for given code" + "-"
					+ modelNotFoundException.getMessage() + JnJCommonUtil.getCurrentDateTime());
			throw modelNotFoundException;

		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + "-" + METHOD_NAME
					+ "Query is not executing successfully , there are some issue in data passed " + "-" + throwable.getMessage()
					+ JnJCommonUtil.getCurrentDateTime());
			throw throwable;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return customerModel;
	}

}
