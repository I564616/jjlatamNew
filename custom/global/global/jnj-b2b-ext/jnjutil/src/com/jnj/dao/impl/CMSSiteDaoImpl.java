/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.dao.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.impl.DefaultCMSSiteDao;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.jnj.constants.JNJFlexiQueries;
import com.jnj.dao.CMSSiteDao;


/**
 * The extension of default CMSSiteDao.
 * 
 * 
 * @author Accenture
 * @version 1.0
 */
public class CMSSiteDaoImpl extends DefaultCMSSiteDao implements CMSSiteDao
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.dao.CMSSiteDao#findCMSSiteByCountry(java.lang.String ,java.lang.String)
	 */
	@Override
	public CMSSiteModel findCMSSiteByCountry(final CountryModel countryModel) throws CMSItemNotFoundException
	{
		CMSSiteModel cmsSiteModel = null;

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(JNJFlexiQueries.SITE_BY_COUNTRY);
		searchQuery.addQueryParameter("country", countryModel);

		final SearchResult<CMSSiteModel> result = search(searchQuery);
		if (result != null)
		{
			final List<CMSSiteModel> cmsSites = result.getResult();
			if (CollectionUtils.isNotEmpty(cmsSites) && cmsSites.size() == 1)
			{
				cmsSiteModel = cmsSites.get(0);
			}
		}
		else
		{
			throw new CMSItemNotFoundException("Error retrieving CMSSite for the country " + countryModel.getIsocode()
					+ ". No site is found or more than one site is retrieved.");
		}

		return cmsSiteModel;
	}
}
