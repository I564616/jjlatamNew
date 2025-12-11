/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.customerEligibility.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jnj.core.dao.customerEligibility.JnjCustomerEligiblityDao;


/**
 * Customer Eligibility Interface Data Load - Dao layer class, responsible for carrying out all DB retrieval operations.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCustomerEligibilityDao implements JnjCustomerEligiblityDao
{
	/**
	 * The Constant LOGGER.
	 */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjCustomerEligibilityDao.class);

	/**
	 * Instance of <code>FlexibleSearchService</code>
	 */
	protected FlexibleSearchService flexibleSearchService;

	/**
	 * Instance of <code>UserService</code>
	 */
	protected UserService userService;

	/**
	 * Instance of <code>SessionService</code>
	 */
	protected SessionService sessionService;

	/**
	 * Constant for relationship table name.
	 */
	protected static final String CUSTOMER_ELIGIBILITY_RELATION = "CustomerEligibilityRelation";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRestrictedCategories(final B2BUnitModel b2bUnit)
	{
		ServicesUtil.validateParameterNotNull(b2bUnit, "B2b Unit cannot be null");

		final StringBuilder searchQuery = new StringBuilder();
		final Set<String> restrictedCategoryCodes = new HashSet<String>();

		searchQuery.append("SELECT DISTINCT {cat:").append(Link.PK).append("} FROM {").append(CategoryModel._TYPECODE)
				.append(" AS cat JOIN ").append(CUSTOMER_ELIGIBILITY_RELATION).append(" AS rel ").append("ON {rel:")
				.append(Link.TARGET).append("} = ").append("{cat:").append(Link.PK).append("} AND {rel:").append(Link.SOURCE)
				.append("} = ").append(b2bUnit.getPk()).append("}");

		final List<CategoryModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public List<CategoryModel> execute()
			{
				final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
				final List<CategoryModel> result = getFlexibleSearchService().<CategoryModel> search(fQuery).getResult();

				return result;
			}
		}, userService.getAdminUser());

		if (result != null)
		{
			for (final CategoryModel category : result)
			{
				restrictedCategoryCodes.add(category.getCode());
			}
		}
		return restrictedCategoryCodes;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}
