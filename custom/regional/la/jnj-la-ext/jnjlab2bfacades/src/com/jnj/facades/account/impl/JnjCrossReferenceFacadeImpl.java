/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.account.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjCrossReferenceTableDTO;
import com.jnj.core.services.JnjCrossReferenceService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.account.JnJCrossReferenceFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.LoadTranslationModel;


/**
 * This class contains the methods for getting the Cross Reference Table data and also for specific search feature.
 *
 * @author skant3
 */
public class JnjCrossReferenceFacadeImpl implements JnJCrossReferenceFacade
{

	@Autowired
	protected JnjCrossReferenceService jnjCrossReferenceService;

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<JnjCrossReferenceTableDTO> getCrossReferenceTable(final PageableData pageableData)
	{
		final String methodName = "getCrossReferenceTable()";
		SearchPageData<LoadTranslationModel> list = null;

		try
		{
			list = jnjCrossReferenceService.getCrossReferenceTable(pageableData);

		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
					"getCrossReferenceTable() - "
							+ " Error while calling getCrossReferenceTable of JnjCrossReferenceServiceImpl class - " + e.getMessage(),
					e, JnjCrossReferenceFacadeImpl.class);
		}

		if (null != list)
		{
			final Collection<LoadTranslationModel> crossReferenceList = list.getResults();

			final List<JnjCrossReferenceTableDTO> crossReferenceDataList = templateConverter(crossReferenceList);
			final SearchPageData<JnjCrossReferenceTableDTO> result = new SearchPageData<>();
			result.setResults(crossReferenceDataList);
			result.setPagination(list.getPagination());
			return result;
		}
		else
		{
			return null;
		}
	}

	private List<JnjCrossReferenceTableDTO> templateConverter(final Collection<LoadTranslationModel> sellOutList)
	{
		final List<JnjCrossReferenceTableDTO> crossRefDataList = new ArrayList<>();

		for (final LoadTranslationModel model : sellOutList)
		{
			final JnjCrossReferenceTableDTO crossRefData = createOrderTemplateData(model);

			crossRefDataList.add(crossRefData);

		}
		return crossRefDataList;
	}

	private JnjCrossReferenceTableDTO createOrderTemplateData(final LoadTranslationModel loadTranslationModel)
	{
		final JnjCrossReferenceTableDTO jnjCrossReferenceTableDTO = new JnjCrossReferenceTableDTO();


		jnjCrossReferenceTableDTO.setClientProductID(loadTranslationModel.getCustMaterialNum());
		jnjCrossReferenceTableDTO.setJnjProductID(loadTranslationModel.getCatalogId());
		jnjCrossReferenceTableDTO.setProductName(loadTranslationModel.getProductId().getName());

		return jnjCrossReferenceTableDTO;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<JnjCrossReferenceTableDTO> getCrossReferenceSearch(final String searchTerm,
			final PageableData pageableData)
	{
		final String methodName = "getCrossReferenceSearch()";
		SearchPageData<LoadTranslationModel> list = null;
		try
		{
			list = jnjCrossReferenceService.getCrossReferenceSearch(searchTerm, pageableData);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
					"Error while calling getCrossReferenceSearch of JnjCrossReferenceServiceImpl class - " + e.getMessage(), e,
					JnjCrossReferenceFacadeImpl.class);
		}
		if (null != list)
		{
			final Collection<LoadTranslationModel> crossReferenceList = list.getResults();

			final List<JnjCrossReferenceTableDTO> crossReferenceDataList = templateConverter(crossReferenceList);
			final SearchPageData<JnjCrossReferenceTableDTO> result = new SearchPageData<>();
			result.setResults(crossReferenceDataList);
			result.setPagination(list.getPagination());
			return result;
		}
		else
		{
			return null;
		}

	}
}