/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.operations.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.impl.DefaultJnjCustomerDao;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.operations.JnjGTAuditTrailDao;
import com.jnj.core.model.JnJGTAuditTrailModel;


/**
 * This class implements the functionality of Audit Trail
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTAuditTrailDao implements JnjGTAuditTrailDao
{


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCustomerDao.class);


	/*
	 * private static final String FETCH_ALL_USERS_FOR_PASSWORD_EXPIRY_EMAIL = "SELECT {pk} " +
	 * " FROM {JnjB2BCustomer AS b2bcustomer} " + "where {b2bcustomer:active} = 1 and {b2bcustomer:logindisabled} = 0 ";
	 */
	protected static final String FETCH_ALL_DATA_FROM_AUDITTRAIL = "SELECT {pk} " + " FROM {JNJGTAuditTrail AS audittrail} ";

	public List<JnJGTAuditTrailModel> getAllData()
	{
		final List<JnJGTAuditTrailModel> listOfJnjUser = new ArrayList<JnJGTAuditTrailModel>();

		final List<JnJGTAuditTrailModel> results;
		try
		{
			final String query = FETCH_ALL_DATA_FROM_AUDITTRAIL;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			results = flexibleSearchService.<JnJGTAuditTrailModel> search(fQuery).getResult();
			if (!results.isEmpty())
			{
				for (final JnJGTAuditTrailModel jnjUser : results)
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
			LOGGER.debug("Error fetching User " + JnJCommonUtil.getCurrentDateTime());
		}
		return listOfJnjUser;

	}


}
