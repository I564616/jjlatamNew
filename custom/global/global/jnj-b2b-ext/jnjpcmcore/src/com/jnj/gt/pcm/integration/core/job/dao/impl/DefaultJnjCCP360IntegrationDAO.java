/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.pcm.integration.core.job.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.gt.pcm.integration.core.job.dao.JnjCCP360IntegrationDAO;
import com.jnj.utils.CommonUtil;


/**
 * Implementation class for JnjCCP360IntegrationDAO.
 */
public class DefaultJnjCCP360IntegrationDAO implements JnjCCP360IntegrationDAO
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjCCP360IntegrationDAO.class);
	protected static final String PRODUCT_BASE_QUERY = "SELECT {pk} FROM {JnJProduct} WHERE {modStatus} IN ({{select {pk} from {JnjGTModStatus} where {code} = 'ACTIVE'}}) AND {modifiedtime} >=CONVERT(DATETIME,?startDate) AND"
			+ "{catalogversion} IN ({{select {pk} from {catalogversion} where {version} = 'Staged'}})";
	private static final String JJCC_P360_PRODUCTSYNC = "JJCC To P360 Product Sync";

	private static final String CATEGORY_BASE_QUERY = "Select {pk} from {category} where {code} = ?code and {catalogversion} = ?catalogversion";
	private static final String SELECT_PRODUCT_DOCUMENT_BY_NAME_URL = "SELECT {PD:PK} FROM {PRODUCTDOCUMENTS AS PD} WHERE {PD:NAME} = ?name AND {PD:URLLINK} = ?urlLink";
	private static final String RELATED_PRODUCT_QUERY = "SELECT PK FROM {PRODUCTREFERENCE} WHERE {SOURCE}=?sourceProduct AND {TARGET}=?targetProduct";
	private static final String CODE = "code";
	private static final String CATALOGVERSION = "catalogversion";

	protected SessionService sessionService;
	protected UserService userService;
	protected FlexibleSearchService flexibleSearchService;

	@Override
	public List<JnJProductModel> getProductDetailList(final String startDate)
	{
		final String methodNameValue = "getNewActiveProducts";
		CommonUtil.logMethodStartOrEnd(JJCC_P360_PRODUCTSYNC, methodNameValue, Logging.START_TIME, LOGGER);
		final Map<String, Object> paramList = new HashMap<>();
		paramList.put("startDate", startDate);
		final String prodQuery = PRODUCT_BASE_QUERY;

		final FlexibleSearchQuery fQueryValue = new FlexibleSearchQuery(prodQuery);
		fQueryValue.addQueryParameters(paramList);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(methodNameValue + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJProductOrderTemplate Query " + fQueryValue);
		}
		return flexibleSearchService.<JnJProductModel> search(fQueryValue).getResult();
	}

	@Override
	public CategoryModel getCategoryModel(final String categoryCode, final CatalogVersionModel catalogVersionModel)
	{

		CategoryModel categoryModel = null;
		final String methodName = "getCategoryModel";
		CommonUtil.logMethodStartOrEnd(JJCC_P360_PRODUCTSYNC, methodName, Logging.START_TIME, LOGGER);
		try
		{
			final String query = CATEGORY_BASE_QUERY;
			final Map params = new HashMap();

			params.put(CODE, categoryCode);
			params.put(CATALOGVERSION, catalogVersionModel);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(params);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(methodName + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJProductOrderTemplate Query " + fQuery);
			}
			categoryModel = flexibleSearchService.<CategoryModel> searchUnique(fQuery);

		}
		catch (final ModelNotFoundException exception)
		{
			LOGGER.error("No Category Found for given code", exception);
		}

		return categoryModel;
	}

	@Override
	public ProductDocumentsModel getProductDocumentByNameAndUrl(final String name, final String url)
	{
		CommonUtil.logMethodStartOrEnd(JJCC_P360_PRODUCTSYNC, "getProductDocumentByNameAndUrl()", Logging.START_TIME, LOGGER);
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT_PRODUCT_DOCUMENT_BY_NAME_URL);

		final Map queryParams = new HashMap();
		queryParams.put(ProductDocumentsModel.NAME, name);
		queryParams.put(ProductDocumentsModel.URLLINK, url);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<ProductDocumentsModel> result = getFlexibleSearchService().<ProductDocumentsModel> search(flexibleSearchQuery)
				.getResult();
		return !result.isEmpty() ? result.get(0) : null;
	}

	@Override
	public List<ProductReferenceModel> getProductReferenceModelList(final JnJProductModel jnjProductModel,
			final JnJProductModel targetModel)
	{
		CommonUtil.logMethodStartOrEnd(JJCC_P360_PRODUCTSYNC, "getProductReferenceModelList()", Logging.START_TIME, LOGGER);
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(RELATED_PRODUCT_QUERY);

		final Map queryParams = new HashMap();
		queryParams.put("sourceProduct", jnjProductModel.getPk());
		queryParams.put("targetProduct", targetModel.getPk());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);


		return getFlexibleSearchService().<ProductReferenceModel> search(fQuery).getResult();
	}


	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
