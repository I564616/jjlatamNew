/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.JnJCrossReferenceDao;
import com.jnj.core.services.JnjCrossReferenceService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.LoadTranslationModel;


/**
 * This class contains the methods for getting the Cross Reference Table data and also for specific search feature.
 *
 * @author skant3
 * @version 1.0
 */
public class JnjCrossReferenceServiceImpl implements JnjCrossReferenceService
{
	@Autowired
	protected JnJCrossReferenceDao jnjCrossReferenceDao;

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<LoadTranslationModel> getCrossReferenceTable(final PageableData pageableData)
	{
		final String methodName = "getCrossReferenceTable()";
		SearchPageData<LoadTranslationModel> loadTranslationModel = null;
		try
		{
			loadTranslationModel = jnjCrossReferenceDao.getCrossReferenceTable(pageableData);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
					"Error while calling getCrossReferenceTable of JnJCrossReferenceDao class - " + e.getMessage(), e,
					JnjCrossReferenceServiceImpl.class);
		}

		return loadTranslationModel;

	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<LoadTranslationModel> getCrossReferenceSearch(final String searchTerm, final PageableData pageableData)
	{
		final String methodName = "getCrossReferenceSearch()";
		SearchPageData<LoadTranslationModel> loadTranslationModel = null;
		try
		{
			loadTranslationModel = jnjCrossReferenceDao.getCrossReferenceSearch(searchTerm, pageableData);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
					"Error while calling getCrossReferenceSearch of JnJCrossReferenceDao class - " + e.getMessage(), e,
					JnjCrossReferenceServiceImpl.class);
		}

		return loadTranslationModel;
	}
}
