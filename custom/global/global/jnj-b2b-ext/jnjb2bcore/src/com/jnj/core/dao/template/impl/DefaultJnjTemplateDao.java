/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.template.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.template.JnjTemplateDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjTemplateDao extends AbstractItemDao implements JnjTemplateDao
{


	protected static final String FIND_ORDER_TEMPLATE_B2BUNIT = "select {pk} from {JnjOrderTemplate} WHERE  {unit}=?unit ";

	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;
	
	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	

	protected static final String SEARCH_TEMPLATE_BY_NUMBER = "select {pk} from {JnjOrderTemplate} where {unit}=?unit and  {code} like ?searchByCriteria ";

	protected static final String SORT_TEMPLATES_BY_DATE = " ORDER BY {JnjOrderTemplate.CREATIONTIME}";

	protected static final String SORT_TEMPLATES_BY_CODE = " ORDER BY {JnjOrderTemplate.CODE}";

	protected static final String SORT_ORDER = " DESC";

	protected static final String SORT_TEMPLATES_BY_SKU_DATE = "ORDER BY {template.CREATIONTIME}";
	protected static final String SORT_TEMPLATES_BY_SKU_CODE = "ORDER BY {template.CODE}";

	protected static final String SORT_TEMPLATES_BY_SKU = "select {template:pk} from {JnjOrderTemplate As template JOIN JnjTemplateEntry As entry on "
			+ "{entry:orderTemplate}={template:pk} JOIN JnjProduct As prod on {entry:product}={prod:pk}} "
			+ "where {prod:code} like ?searchByCriteria and {template:unit}=?unit ";

	protected final String LIKE_CHARACTER = "%";


	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}


	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}


	@Override
	public List<JnjOrderTemplateModel> searchOrderTemplate(final String searchByCriteria, final String searchParameter)
	{

		List<JnjOrderTemplateModel> templateModelList = new ArrayList<JnjOrderTemplateModel>();
		ServicesUtil.validateParameterNotNull(searchByCriteria, "Criteria should not be null");

		if (searchParameter.equalsIgnoreCase("Template Number"))
		{
			final JnjOrderTemplateModel templateModel = getJnjTemplateByCode(searchByCriteria);
			templateModelList.add(templateModel);
		}
		else if (searchParameter.equalsIgnoreCase("SKU"))
		{
			final Map queryParams = new HashMap();
			final String query = "select {template:pk} from {JnjOrderTemplate As template JOIN JnjTemplateEntry As entry on {entry:orderTemplate}={template:pk} JOIN JnjProduct "
					+ "As prod on {entry:product}={prod:pk}} where {prod:code}=?searchByCriteria";
			queryParams.put("searchByCriteria", searchByCriteria);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			final SearchResult<JnjOrderTemplateModel> result = getFlexibleSearchService().search(fQuery);
			templateModelList = result.getResult();

		}

		return templateModelList;

	}


	/**
	 * @param searchByCriteria
	 * @return
	 */



	public JnjOrderTemplateModel getJnjTemplateByCode(final String searchByCriteria)
	{
		JnjOrderTemplateModel templateModel = null;
		try
		{
			final Map queryParams = new HashMap();
			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
			queryParams.put("unit", jnjB2BUnitModel.getPk().toString());
			queryParams.put("searchByCriteria", searchByCriteria);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SEARCH_TEMPLATE_BY_NUMBER);
			fQuery.addQueryParameters(queryParams);
			templateModel = (JnjOrderTemplateModel) getFlexibleSearchService().searchUnique(fQuery);
		}
		catch (final ModelNotFoundException e)
		{
			templateModel = null;

		}
		return templateModel;
	}


	public List<JnjOrderTemplateModel> getRecenlyUsedTemplates()
	{

		List<JnjOrderTemplateModel> templateModelList = new ArrayList<JnjOrderTemplateModel>();
		final Map queryParams = new HashMap();
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		queryParams.put("unit", jnjB2BUnitModel.getPk().toString());
		final String query = FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE + SORT_ORDER;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setCount(3);
		fQuery.addQueryParameters(queryParams);
		final SearchResult<JnjOrderTemplateModel> result = getFlexibleSearchService().search(fQuery);
		templateModelList = result.getResult();
		return templateModelList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.dao.template.JnjTemplateDao#getPagedOrderTemplates(de.hybris.platform.commerceservices.search.pagedata
	 * .PageableData, java.lang.String, java.lang.String)
	 */
	@Override
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(final PageableData pageableData,
			final String searchByCriteria, final String searchParameter, String sortByCriteria)
	{
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("unit", jnjB2BUnitModel.getPk().toString());


		if (sortByCriteria == null)
		{

			sortByCriteria = "Template Number - increasing";
		}
		List<SortQueryData> sortQueries = null;

		if (searchParameter == null || searchByCriteria.isEmpty())
		{
			sortQueries = Arrays.asList(
					createSortQueryData("Template Number - increasing", FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_CODE),
					createSortQueryData("Template Number - decreasing", FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_CODE
							+ SORT_ORDER),
					createSortQueryData("Date - newest to oldest", FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE + SORT_ORDER),
					createSortQueryData("Date - oldest to newest", FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE));

		}
		else if (searchParameter.equalsIgnoreCase("Template Number") && !searchByCriteria.isEmpty())
		{
			queryParams.put("searchByCriteria", LIKE_CHARACTER + searchByCriteria + LIKE_CHARACTER);
			sortQueries = Arrays.asList(
					createSortQueryData("Template Number - increasing", SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_CODE),
					createSortQueryData("Template Number - decreasing", SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_CODE
							+ SORT_ORDER),
					createSortQueryData("Date - newest to oldest", SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_DATE + SORT_ORDER),
					createSortQueryData("Date - oldest to newest", SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_DATE));
		}
		else if (searchParameter.equalsIgnoreCase("SKU") && !searchByCriteria.isEmpty())
		{

			queryParams.put("searchByCriteria", LIKE_CHARACTER + searchByCriteria + LIKE_CHARACTER);
			sortQueries = Arrays.asList(
					createSortQueryData("Template Number - increasing", SORT_TEMPLATES_BY_SKU + SORT_TEMPLATES_BY_SKU_CODE),
					createSortQueryData("Template Number - decreasing", SORT_TEMPLATES_BY_SKU + SORT_TEMPLATES_BY_SKU_CODE
							+ SORT_ORDER),
					createSortQueryData("Date - newest to oldest", SORT_TEMPLATES_BY_SKU + SORT_TEMPLATES_BY_SKU_DATE + SORT_ORDER),
					createSortQueryData("Date - oldest to newest", SORT_TEMPLATES_BY_SKU + SORT_TEMPLATES_BY_SKU_DATE));


		}
		return pagedFlexibleSearchService.search(sortQueries, sortByCriteria, queryParams, pageableData);

	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}
}
