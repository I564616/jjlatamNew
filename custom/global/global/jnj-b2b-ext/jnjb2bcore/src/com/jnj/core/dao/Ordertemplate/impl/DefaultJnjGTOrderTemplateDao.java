/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.Ordertemplate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.template.impl.DefaultJnjTemplateDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjOrderTemplateModel;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.dao.Ordertemplate.JnjGTOrderTemplateDao;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;


/**
 * TODO:<Balinder-Class level comments missing>
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOrderTemplateDao extends DefaultJnjTemplateDao implements JnjGTOrderTemplateDao
{
	@Autowired
	protected PagedFlexibleSearchService pagedFlexibleSearchService;
	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Resource(name = "productDao")
	protected JnjGTProductDao jnjGTProductDao;


	@Autowired
	protected SessionService sessionService;

	
	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	protected static final String FIND_ORDER_TEMPLATE_B2BUNIT = Jnjb2bCoreConstants.TemplateSearch.FIND_ORDER_TEMPLATE_B2BUNIT;
	protected static final String FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR = Jnjb2bCoreConstants.TemplateSearch.FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR;
	protected static final String FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS = Jnjb2bCoreConstants.TemplateSearch.FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS;
	protected static final String SORT_TEMPLATES_BY_NUMBER = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_NUMBER;
	protected static final String SORT_TEMPLATES_BY_NAME = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_NAME;
	protected static final String SORT_TEMPLATES_BY_LINE = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_LINE;
	protected static final String SORT_TEMPLATES_BY_DATE = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_DATE;
	protected static final String SORT_TEMPLATES_BY_AUTOR = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_AUTOR;
	protected static final String SORT_TEMPLATES_BY_STATUS = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_STATUS;
	protected static final String SEARCH_TEMPLATE_BY_NUMBER = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_NUMBER;
	protected static final String SEARCH_TEMPLATE_BY_NAME = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_NAME;
	protected static final String SEARCH_TEMPLATE_BY_NAME_SORT_BY_AUTHOR = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_NAME_SORT_BY_AUTHOR;
	protected static final String SEARCH_TEMPLATE_BY_NAME_SORT_BY_STATUS = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_NAME_SORT_BY_STATUS;
	protected static final String SEARCH_TEMPLATE_BY_NUMBER_SORT_BY_STATUS = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_NUMBER_SORT_BY_STATUS;
	protected static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_STATUS = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_STATUS;
	protected static final String SORT_TEMPLATE_NAME_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_ASC;
	protected static final String SORT_TEMPLATE_NUMBER_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_ASC;
	protected static final String SORT_AUTHOR_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_ASC;
	protected static final String SORT_LINES_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_ASC;
	protected static final String SORT_SHARE_STATUS_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_ASC;
	protected static final String SORT_CREATION_DATE_ASC = Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_ASC;
	protected static final String SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_AUTHOR = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_AUTHOR;
	public static final String SORT_TEMPLATES_BY_DATE_PRODUCTCODE = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_DATE_PRODUCTCODE;
	public static final String SORT_TEMPLATES_BY_NUMBER_PRODUCTCODE = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_NUMBER_PRODUCTCODE;
	public static final String SORT_TEMPLATES_BY_NAME_PRODUCTCODE = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATES_BY_NAME_PRODUCTCODE;
	public static final String SEARCH_TEMPLATE_DETAIL = Jnjb2bCoreConstants.TemplateSearch.SEARCH_TEMPLATE_DETAIL;
	protected static final String LIKE_CHARACTER = Jnjb2bCoreConstants.TemplateSearch.LIKE_CHARACTER;
    protected static final String SORT_TEMPLATE_NAME_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NAME_DESC;
	protected static final String SORT_TEMPLATE_NUMBER_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_TEMPLATE_NUMBER_DESC;
	protected static final String SORT_AUTHOR_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_AUTHOR_DESC;
	protected static final String SORT_LINES_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_LINES_DESC;
	protected static final String SORT_SHARE_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_SHARE_STATUS_DESC;
	protected static final String SORT_CREATION_DATE_DESC = Jnjb2bCoreConstants.TemplateSearch.SORT_CREATION_DATE_DESC;
	protected static final String SORT_DESCENDING = "DESC";

	@Override
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(final PageableData pageableData, final String searchBy,
			final String searchText, final String sortBy, final String unit)
	{

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("unit", unit);
		queryParams.put("userpk", jnjGTCustomerService.getCurrentUser().getPk());
		List<SortQueryData> sortQueries = null;
		if (StringUtils.isEmpty(searchBy) || StringUtils.isEmpty(searchText)
				|| searchText.trim().equalsIgnoreCase(Jnjb2bCoreConstants.TemplateSearch.SEARCH))
		{

			sortQueries = Arrays.asList(
					createSortQueryData(SORT_TEMPLATE_NAME_ASC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NAME),
					createSortQueryData(SORT_TEMPLATE_NUMBER_ASC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NUMBER),
					createSortQueryData(SORT_AUTHOR_ASC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR),
					createSortQueryData(SORT_LINES_ASC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_LINE),
					createSortQueryData(SORT_SHARE_STATUS_ASC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS),
					createSortQueryData(SORT_CREATION_DATE_ASC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE),
		            createSortQueryData(SORT_TEMPLATE_NAME_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NAME 
		            		+ SORT_DESCENDING),
					createSortQueryData(SORT_TEMPLATE_NUMBER_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NUMBER
							+ SORT_DESCENDING),
					createSortQueryData(SORT_AUTHOR_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR + SORT_DESCENDING),
					createSortQueryData(SORT_LINES_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_LINE + SORT_DESCENDING),
					createSortQueryData(SORT_SHARE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS + SORT_DESCENDING),
					createSortQueryData(SORT_CREATION_DATE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE
							+ SORT_DESCENDING));
	
	
		}
		else if (searchBy.equalsIgnoreCase("SearchTemplateName") && StringUtils.isNotEmpty(searchText))
		{
			queryParams.put("searchText", LIKE_CHARACTER + searchText.toLowerCase() + LIKE_CHARACTER);
			sortQueries = Arrays.asList(
					createSortQueryData(SORT_TEMPLATE_NAME_ASC, SEARCH_TEMPLATE_BY_NAME + SORT_TEMPLATES_BY_NAME),
					createSortQueryData(SORT_TEMPLATE_NUMBER_ASC, SEARCH_TEMPLATE_BY_NAME + SORT_TEMPLATES_BY_NUMBER),
					createSortQueryData(SORT_AUTHOR_ASC, SEARCH_TEMPLATE_BY_NAME_SORT_BY_AUTHOR + SORT_TEMPLATES_BY_AUTOR),
					createSortQueryData(SORT_LINES_ASC, SEARCH_TEMPLATE_BY_NAME + SORT_TEMPLATES_BY_LINE),
					createSortQueryData(SORT_SHARE_STATUS_ASC, SEARCH_TEMPLATE_BY_NAME_SORT_BY_STATUS + SORT_TEMPLATES_BY_STATUS),
					createSortQueryData(SORT_CREATION_DATE_ASC, SEARCH_TEMPLATE_BY_NAME + SORT_TEMPLATES_BY_DATE),
			
		            createSortQueryData(SORT_TEMPLATE_NAME_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NAME
							+ SORT_DESCENDING),
					createSortQueryData(SORT_TEMPLATE_NUMBER_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NUMBER
							+ SORT_DESCENDING),
					createSortQueryData(SORT_AUTHOR_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR + SORT_DESCENDING),
					createSortQueryData(SORT_LINES_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_LINE + SORT_DESCENDING),
					createSortQueryData(SORT_SHARE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS + SORT_DESCENDING),
					createSortQueryData(SORT_CREATION_DATE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE
							+ SORT_DESCENDING));
		
		
		
		
		}
		else if (searchBy.equalsIgnoreCase("SearchCode") && StringUtils.isNotEmpty(searchText))
		{

			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			final ProductModel product = jnjGTProductDao.getProductByPartialValue(searchText, false, currentSite, false);
			if (product != null)
			{
				queryParams.put("searchText", product.getPk());
				sortQueries = Arrays
						.asList(
								createSortQueryData(SORT_TEMPLATE_NAME_ASC, SEARCH_TEMPLATE_BY_NUMBER
										+ SORT_TEMPLATES_BY_NAME_PRODUCTCODE),
								createSortQueryData(SORT_TEMPLATE_NUMBER_ASC, SEARCH_TEMPLATE_BY_NUMBER
										+ SORT_TEMPLATES_BY_NUMBER_PRODUCTCODE),
								createSortQueryData(SORT_AUTHOR_ASC, SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_AUTHOR + SORT_TEMPLATES_BY_AUTOR),
								createSortQueryData(SORT_LINES_ASC, SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_LINE),
								createSortQueryData(SORT_SHARE_STATUS_ASC, SEARCH_TEMPLATE_BY_PRODUCT_SORT_BY_STATUS
										+ SORT_TEMPLATES_BY_STATUS),
								createSortQueryData(SORT_CREATION_DATE_ASC, SEARCH_TEMPLATE_BY_NUMBER + SORT_TEMPLATES_BY_DATE),
				createSortQueryData(SORT_TEMPLATE_NAME_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NAME
							+ SORT_DESCENDING),
					createSortQueryData(SORT_TEMPLATE_NUMBER_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_NUMBER
							+ SORT_DESCENDING),
					createSortQueryData(SORT_AUTHOR_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_AUTHOR + SORT_DESCENDING),
					createSortQueryData(SORT_LINES_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_LINE + SORT_DESCENDING),
					createSortQueryData(SORT_SHARE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT_SORT_BY_STATUS + SORT_DESCENDING),
					createSortQueryData(SORT_CREATION_DATE_DESC, FIND_ORDER_TEMPLATE_B2BUNIT + SORT_TEMPLATES_BY_DATE
							+ SORT_DESCENDING));
			
			
			
			
			
			}
			else
			{
				return null;
			}
		}
		return pagedFlexibleSearchService.search(sortQueries, sortBy, queryParams, pageableData);
	}

	@Override
	public JnjOrderTemplateModel getOrderTemplateDetails(final String templateCode, final String unit)
	{
		JnjOrderTemplateModel templateModel = null;
		try
		{
			final Map queryParams = new HashMap();
			queryParams.put("unit", unit);
			queryParams.put("searchText", templateCode);
			queryParams.put("userpk", jnjGTCustomerService.getCurrentUser().getPk());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SEARCH_TEMPLATE_DETAIL);
			fQuery.addQueryParameters(queryParams);
			templateModel = (JnjOrderTemplateModel) getFlexibleSearchService().searchUnique(fQuery);
		}
		catch (final ModelNotFoundException e)
		{
			templateModel = null;
		}
		return templateModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.Ordertemplate.JnjGTOrderTemplateDao#getOrderTemplatesForHome()
	 */
	/*@Override
	public Map<String, String> getOrderTemplatesForHome()
	{
		final String query = "select {code}, {name} from {JnjOrderTemplate} WHERE  {unit}=?unit and ({visibleto} =?userpk or {visibleto} =?unit )";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		resultClassList.add(String.class);
		fQuery.setResultClassList(resultClassList);
		final Map<String, String> queryParams = new HashMap<String, String>();
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		queryParams.put("unit", currentB2BUnit.getPk().toString());
		queryParams.put("userpk", jnjGTCustomerService.getCurrentUser().getPk().toString());
		fQuery.addQueryParameters(queryParams);
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		final List<List<String>> resultList = result.getResult();
		final Map<String, String> resultMap = new HashMap<String, String>();
		for (final List<String> lists : resultList)
		{
			resultMap.put(lists.get(0), lists.get(1));
		}
		return resultMap;
	}*/
	
	@Override
	public Map<String, String> getOrderTemplatesForHome()
	{
		final String query = "select {temp:code},{temp:name},{temp:author},{temp:sharestatus},code from {JnjOrderTemplate as temp JOIN ShareStatus as status on {status:pk} = {temp:sharestatus}} where {temp:unit}=?unit  and ({temp:visibleto} =?userpk or {temp:visibleto} =?unit )  order by  {status.CODE}";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		resultClassList.add(String.class);
		resultClassList.add(String.class);
		resultClassList.add(String.class);
		resultClassList.add(String.class);
		fQuery.setResultClassList(resultClassList);
		final Map<String, String> queryParams = new HashMap<String, String>();
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		queryParams.put("unit", currentB2BUnit.getPk().toString());
		queryParams.put("userpk", jnjGTCustomerService.getCurrentUser().getPk().toString());
		fQuery.addQueryParameters(queryParams);
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		final List<List<String>> resultList = result.getResult();
		final Map<String, String> resultMap = new HashMap<String, String>();
		for (final List<String> lists : resultList)
		{
			String code = lists.get(0);
			String name = lists.get(1);
			String author = lists.get(2);
			String shareStatusPK = lists.get(3);
			String shareStatusCode = lists.get(4);
			if(author.equalsIgnoreCase(jnjGTCustomerService.getCurrentUser().getPk().toString()) == true) {
				resultMap.put(lists.get(0), lists.get(1));
			} else {
				if(shareStatusCode.equalsIgnoreCase("SHARED") == true) {
					resultMap.put(lists.get(0), lists.get(1));
				}
			}
			
		}
		return resultMap;
	}


}
