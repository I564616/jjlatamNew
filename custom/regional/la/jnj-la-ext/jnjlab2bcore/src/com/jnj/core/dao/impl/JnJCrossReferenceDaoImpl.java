/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnJCrossReferenceDao;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnjIntLoadTranslationModel;
import com.jnj.la.core.model.LoadTranslationModel;



/**
 * This class is responsible for querying the databse to obtain the data for cross reference table.
 *
 * @author Accenture
 *
 */
public class JnJCrossReferenceDaoImpl extends AbstractItemDao implements JnJCrossReferenceDao
{
	private static final Logger LOG = Logger.getLogger(JnJCrossReferenceDaoImpl.class);

	private ProductService productService;

	@Autowired
	private DefaultCompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	@Autowired
	private JnJProductService jnjGTProductService;

	@Autowired
	private ModelService modelService;

	private final String LIKE_CHARACTER = "%";

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<LoadTranslationModel> getCrossReferenceTable(final PageableData pageableData)
	{
		List<SortQueryData> sortQueries = null;
		SearchPageData<LoadTranslationModel> result = null;
		try
		{
			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

			final String customerId = jnjB2BUnitModel.getUid();

			final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);

			ServicesUtil.validateParameterNotNull(unit, "unit must not be null");

			final Map queryParams = new HashMap();

			final String query = "select {pk} from {LoadTranslation} where {b2bunit}=?unit and {custMaterialNum} != 'null'";
			sortQueries = Arrays.asList(createSortQueryData("byDate", query));

			queryParams.put("unit", unit);

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);

			if (pageableData.getPageSize() == 0) // fetch whole data
			{
				pageableData.setPageSize(10);
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
				pageableData.setPageSize((int) result.getPagination().getTotalNumberOfResults());
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
			}
			else
			{
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
			}
		}
		catch (final Exception exp)
		{
			LOG.error("Error while executing query to get Cross Reference Table records in JnJCrossReferenceDao class - "
					+ exp.getMessage());
			LOG.error(exp);
		}
		return result;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<LoadTranslationModel> getCrossReferenceSearch(final String searchTerm, final PageableData pageableData)
	{
		List<SortQueryData> sortQueries = null;
		SearchPageData<LoadTranslationModel> result = null;
		try
		{
			final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();

			final String customerId = jnjB2BUnitModel.getUid();

			final B2BUnitModel unit = companyB2BCommerceService.getUnitForUid(customerId);

			ServicesUtil.validateParameterNotNull(unit, "unit must not be null");
			ServicesUtil.validateParameterNotNull(searchTerm, "searchTerm must not be null");


			final Map queryParams = new HashMap();

			queryParams.put("unit", unit);
			queryParams.put("searchTerm", LIKE_CHARACTER + searchTerm + LIKE_CHARACTER);
			String query = null;

			JnJProductModel jnJProductModel = null;
			try
			{
				jnJProductModel = jnjGTProductService.getProductForCatalogId(searchTerm);
			}
			catch (final BusinessException businessException)
			{
				LOG.info("Given Catalog ID is not Valid." + businessException.getLocalizedMessage());
				LOG.info(businessException);
			}
			if (null != jnJProductModel)
			{
				queryParams.put("catalogId", searchTerm.toUpperCase());
				query = "select {pk} from {LoadTranslation} where {b2bunit}=?unit and {catalogId}=?catalogId";
			}
			else
			{
				query = "select {pk} from {LoadTranslation} where {b2bunit}=?unit and {CUSTMATERIALNUM} like ?searchTerm";
			}
			sortQueries = Arrays.asList(createSortQueryData("byDate", query));
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (pageableData.getPageSize() == 0) // fetch whole data
			{
				pageableData.setPageSize(10);
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
				pageableData.setPageSize((int) result.getPagination().getTotalNumberOfResults());
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
			}
			else
			{
				result = pagedFlexibleSearchService.<LoadTranslationModel> search(sortQueries, "byDate", queryParams, pageableData);
			}
		}
		catch (final Exception exp)
		{
			LOG.error("Error while executing query to get searched Cross Reference Table record in JnJCrossReferenceDao class - "
					+ exp.getMessage());
			LOG.error(exp);
		}
		return result;
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}


	private boolean isProductCodeValid(final String productId)
	{

		boolean isProductCodeValid = false;
		final ProductModel product = jnjGTProductService.getProductCodeOrEAN(productId);
		if (product != null)
		{
			isProductCodeValid = true;
		}
		return isProductCodeValid;
	}



	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/*
	 * This method is used to fetch the record from intermediate table for remove
	 *
	 * @see com.jnj.core.dao.JnJCrossReferenceDao#getJnjIntLoadTranslatiomModelForRemove(com.jnj.core.enums.RecordStatus)
	 */
	@Override
	public List<JnjIntLoadTranslationModel> getJnjIntLoadTranslatiomModelForRemove(final RecordStatus recordStatus)
	{
		final String METHOD_NAME = "getJnjIntLoadTranslatiomModelForRemove ()";
		final int start = 0;
		final int range = Config.getInt(Jnjb2bCoreConstants.FILE_SIZE_LIMIT, 5);
		int total = 0;
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_TRANSLATION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		final String query = "select {pk} from {JnjIntLoadTranslation as p} where {p:recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?recordStatus}})";
		List<JnjIntLoadTranslationModel> searchResult = null;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("recordStatus", recordStatus.getCode());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setCount(range);
		fQuery.setNeedTotal(true);
		fQuery.addQueryParameters(params);
		do
		{
			final SearchResult<JnjIntLoadTranslationModel> result = getFlexibleSearchService().search(fQuery);
			total = result.getTotalCount();
			searchResult = result.getResult();
			fQuery.setStart(start);
			modelService.removeAll(searchResult);
		}
		while (start < total);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_TRANSLATION + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return searchResult;
	}

	public JnJProductService getJnjGTProductService()
	{
		return jnjGTProductService;
	}

	public void setJnjGTProductService(final JnJProductService jnjGTProductService)
	{
		this.jnjGTProductService = jnjGTProductService;
	}


}
