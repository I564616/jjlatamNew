/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjProductDao;
import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * TODO:<Neeraj-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjProductDao extends DefaultProductDao implements JnjProductDao
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjProductDao.class);

	/**
	 * @param typecode
	 */
	public DefaultJnjProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<ProductModel> getAllProducts(final CatalogVersionModel catalogVersion)
	{
		//validateParameterNotNull(catalogVersion, "CatalogVersion must not be null!");
		final Map parameters = new HashMap();
		parameters.put(ProductModel.CATALOGVERSION, catalogVersion);
		return find(parameters);


	}

	/**
	 * Get the discontinued product from catalog version for given countryIso. All product which does not have country
	 * xyz are considered invalid for country xyz
	 * 
	 * @param catalogVersion
	 *           CatalogVersionModel
	 * @param countryIso
	 *           String
	 * @return List<ProductModel>
	 */

	@Override
	public List<ProductModel> getDiscountinedProducts(final CatalogVersionModel catalogVersion, final String countryIso)
	{
		final String query = "select {pk} from {Product} where {catalogversion}=?catalogversion and {offlinedate} is null and {pk} in"
				+ " ({{ select {source} from {Product2Countries} where {target} not in({{ select {pk} from {country} where {isocode}=?countryIso}}) "
				+ "and {source} not in ({{  select {source} from {Product2Countries} where {target} in({{ select {pk} from {country} where {isocode}=?countryIso}})   }})  }})";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("catalogVersion", catalogVersion.getPk());
		params.put("countryIso", countryIso);
		final List<ProductModel> products = doSearch(query, params, null);
		return products;
	}

	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final List<Class> resultClasses)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}
		if (resultClasses != null)
		{
			fQuery.setResultClassList(resultClasses);
		}
		final SearchResult<T> result = getFlexibleSearchService().search(fQuery);
		final List<T> elements = result.getResult();
		return elements;
	}
	
	
	@Override
	public List<JnJProductModel> getActiveProducts(final String catalogId, final CatalogVersionModel catalogVersion)
			throws BusinessException
	{
		final String METHOD_NAME = "getActiveProducts()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		List<JnJProductModel> jnJProductModelList = null;
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnJProductModel._TYPECODE).append("}")
				.append(" WHERE {").append(JnJProductModel.CATALOGVERSION).append("} = ?").append(JnJProductModel.CATALOGVERSION)
				.append(" AND {").append(JnJProductModel.CATALOGID).append("} = ?").append(JnJProductModel.CATALOGID);

		final Map parameters = new HashMap();
		parameters.put(JnJProductModel.CATALOGVERSION, catalogVersion);
		parameters.put(JnJProductModel.CATALOGID, catalogId.toUpperCase());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		try
		{
			final SearchResult<JnJProductModel> result = getFlexibleSearchService().search(fQuery);
			jnJProductModelList = result.getResult();
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(Logging.UPSERT_PRODUCT_NAME + Logging.HYPHEN + METHOD_NAME
					+ "ModelNotFoundException occured. Cascading to Business Exception. ");
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());

		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			LOGGER.warn(Logging.UPSERT_PRODUCT_NAME + Logging.HYPHEN + METHOD_NAME
					+ "AmbiguousIdentifierException occured. Cascading to Business Exception. ", ambiguousIdentifierException);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			LOGGER.warn(Logging.UPSERT_PRODUCT_NAME + Logging.HYPHEN + METHOD_NAME
					+ "Exception occured. Cascading to Business Exception. ", exception);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnJProductModelList;
	}
}