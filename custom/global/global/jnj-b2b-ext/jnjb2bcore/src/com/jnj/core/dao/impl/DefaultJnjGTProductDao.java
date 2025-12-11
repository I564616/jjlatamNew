/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.dao.impl;


import com.jnj.core.util.Interval;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.impl.DefaultJnjProductDao;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;
import com.jnj.utils.CommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjGTModStatus;

/**
 * This class represents the product DAO layer
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTProductDao extends DefaultJnjProductDao implements JnjGTProductDao
{
	@Autowired
	SessionService sessionService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	

	public SessionService getSessionService() {
		return sessionService;
	}



	public UserService getUserService() {
		return userService;
	}



	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}



	protected static final String DATE = "date";
	protected static final String PRD_CODE = "prdCode";
	protected static final String PCM_NEW_PRODUCTS = "PCM New Products";
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductDao.class);
	protected static final String VARIANT_BASE_QUERY = "SELECT {vp.pk} FROM	{JnjGTVariantProduct as vp JOIN JnJProduct as p ON {p:PK} = {vp:baseproduct}} ";
	protected static final String PRODUCT_BASE_QUERY = "SELECT {p.pk} FROM	{JnJProduct as p} where";
	protected static final String PRODUCT_MODEL_BY_CODE = "Select {pk} from {JnJProduct} where {code}=?code and {catalogVersion} IN (?catalogVersion)";
	protected static final String PRODUCT_MODELS_BY_UPC_CODE = "Select {pk} from {JnJProduct} where {upccode}=?upcCode and {catalogVersion} IN (?catalogVersion)";
	protected static final String SELECT_MOD_PRODUCT = "SELECT {PK} FROM {JnJProduct} WHERE {MATERIALBASEPRODUCT}=?productPk OR {CODE}=?productCode";
	protected static final String RESOURCES_CPSIA = "Resources - CPSIA";
	protected static final String SELECT_CPSIA_DETAILS = "SELECT {CPSIA:PK} FROM {JnjGTProductCpscDetail AS CPSIA}";
	protected static final String SELECT_NEW_US_ACTIVE_PRODUCTS = "SELECT {prod:pk} FROM {JnJProduct as prod} WHERE {prod:invalidInPrcieList}=0 AND {prod:catalogversion}=?catalogVersion and {prod:newProductStartDate}<=CONVERT(DATETIME,?endDate) and {prod:newProductStartDate}>=CONVERT(DATETIME,?startDate) AND {prod:productStatus} IN ({{select {pk} from {JnjGTModStatus} where {code} = 'ACTIVE'}}) AND {prod:pcmModStatus} IN ({{select {pk} from {JnjGTModStatus} where {code} = 'ACTIVE'}}) ";
	protected static final String SELECT_NEW_CA_ACTIVE_PRODUCTS = "SELECT {prod:pk} FROM {JnJProduct as prod} WHERE {prod:invalidInPrcieList}=0 AND {prod:catalogversion}=?catalogVersion and {prod:firstShipEffectDate}<=CONVERT(DATETIME,?endDate) and {prod:firstShipEffectDate}>=CONVERT(DATETIME,?startDate) AND {prod:productStatus} IN ({{select {pk} from {JnjGTModStatus} where {code} = 'ACTIVE'}}) AND {prod:pcmModStatus} IN ({{select {pk} from {JnjGTModStatus} where {code} = 'ACTIVE'}}) ";
	protected static final String SELECT_VARIANTS_ORDER_BY_PKG_LVL_CODE = "SELECT {PK}, {VAR:PACKAGINGLVLCODE} FROM  {JNJGTVARIANTPRODUCT AS VAR JOIN JnJProduct "
			+ "AS PROD ON {VAR:BASEPRODUCT}={PROD:PK}} WHERE {PROD:PK} = ?pk AND ({VAR:SHIPUNITIND}=1 OR {VAR:SELLUNITIND} = 1) ORDER BY {VAR:PACKAGINGLVLCODE} DESC";




	protected static final String OUTERCASE = "outercase";
	protected static final String UPC = "upc";
	protected static final String LOT = "lot";
	protected static final String ORDER_BY_CPSIA_UPC_CODE = " ORDER BY {CPSIA:upcCode}";
	protected static final String ORDER_BY_CPSIA_LOT_NUMBER = " ORDER BY length({CPSIA:lotNumber}) ";
	protected static final String ORDER_BY_CPSIA_LOT_NUMBER_2 = ",{CPSIA:lotNumber} ";
	protected static final String ORDER_BY_CPSIA_PRODUCT_CODE = " ORDER BY {CPSIA:productCode}";
	protected static final String ORDER_BY_CPSIA_MODIFIED_DATE = " ORDER BY {CPSIA:modifiedDate}";
	protected static final String ORDER_BY_CPSIA_OUTERCASE = " ORDER BY {CPSIA:outercase}";
	protected static final String LIKE_WILDCARD_CHARACTER = "%";

	/**
	 * @param typecode
	 */
	public DefaultJnjGTProductDao(final String typecode)
	{
		super(typecode);
	}



	@Override
	public ProductModel getProductByPartialValue(final String code, final boolean ignoreUpc, final String currentSite,
			final boolean admin)
	{

		/*if (currentSite.equals(JnjPCMCoreConstants.PCM))
		{
			return getConsProductByPartialValue(code, ignoreUpc, currentSite, admin);
		}*/
		if (currentSite.equals(Jnjb2bCoreConstants.MDD))
		{
			return getMDDProductByValue(code, ignoreUpc, admin);
		}
		// If site is CONS, variant needs to be searched upon UPC and Base Material Number
		else
		{
			return getConsProductByPartialValue(code, false, currentSite, admin);
		}

	}

	@Override
	public List<JnjGTVariantProductModel> getProductByValue(String code, final boolean ignoreUpc)
	{
		code = StringUtils.upperCase(code);
		// From session get the current site i.e. MDD/CONS
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		//final String checkUpc = sessionService.getAttribute(JnjPCMCoreConstants.Cart.IGNORE_UPC);
		final StringBuilder query = new StringBuilder(VARIANT_BASE_QUERY);
		final Map<String, Object> params = new HashMap<String, Object>();
		// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
		if (currentSite.equals(Jnjb2bCoreConstants.MDD))
		{
			query.append("where");
			/*if (!ignoreUpc || !StringUtils.equals(checkUpc, JnjPCMCoreConstants.Cart.IGNORE_UPC_VALUE))
			{
				query.append(" {vp:EAN}=?code or");
			}*/
			query.append(" ({p:materialBaseProduct} is null and {p:code}=?code )");
		}
		// If site is CONS, variant needs to be searched upon UPC and Base Material Number
		else
		{
			final JnJProductModel baseProduct = getProductModelByCode(code, null);
			// Query has the fieldName placed at runtime depending upon the current site
			query.append("where ");

			/*if (!StringUtils.equals(checkUpc, JnjPCMCoreConstants.Cart.IGNORE_UPC_VALUE))
			{
				query.append("{vp:UPC}=?code or ");
			}*/
			query.append("(({p:materialBaseProduct}is null and {p:code}=?code )");

			if (baseProduct != null)
			{
				query.append("OR({p:materialBaseProduct} is not null and {p:materialBaseProduct} =?baseProduct)");
				params.put("baseProduct", baseProduct.getPk());
			}

			query.append(")");
		}
		// Query has the fieldName placed at runtime depending upon the current site
		params.put("code", code);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("JnjGTProductDaoImpl : Executing query for quick add to cart ---" + query);
		}

		final Date startTime = new Date();
		final List<JnjGTVariantProductModel> products = doSearch(query.toString(), params, null);
		final Date endTime = new Date();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("-------->Time taken to execute JnjGTProductDaoImpl-getProductByValue() Query : "
					+ (endTime.getTime() - startTime.getTime()) / 1000 + "seconds");
		}

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
	public JnJProductModel getProductModelByCode(final String code, final CatalogVersionModel catalogVersionModel)
	{

		return getProductModelByCode(code, catalogVersionModel, false);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public JnJProductModel getProductModelByCode(String code, final CatalogVersionModel catalogVersionModel,
			final boolean partial)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelByCode()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		code = StringUtils.upperCase(code);
		String query = null;
		JnJProductModel JnJProductModel = null;
		try
		{
			ServicesUtil.validateParameterNotNull(code, " processed Indentfier must not be null");
			final Map queryParams = new HashMap();
			if (partial)
			{
				/* 1-26-15 mzeman, Added query for testing (disabled) by default. ** */
				if (Config.getBoolean("useRegExpLike1", false))
				{
					queryParams.put("trimmedCode", code.replaceAll("^0+", ""));
					query = Config
							.getString("searchQuery1",
									"Select {pk} from {JnJProduct} where  (LTRIM({code},'0')=?trimmedCode) and {catalogVersion} IN (?catalogVersion)");
				}
				else
				{
					query = "Select {pk} from {JnJProduct} where  REGEXP_LIKE({code},'^[0]*" + code
							+ "$') and {catalogVersion} IN (?catalogVersion)";
				}
			}
			else
			{
				query = PRODUCT_MODEL_BY_CODE;
				queryParams.put("code", code);
			}
			if (null != catalogVersionModel)
			{
				queryParams.put("catalogVersion", catalogVersionModel);
			}
			else
			{
				queryParams.put("catalogVersion", catalogVersionService.getSessionCatalogVersions());
			}
			final String finalQuery = query;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getProductModelByCode()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJProductModel Query " + fQuery);
			}
			final List<JnJProductModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnJProductModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(finalQuery);
					fQuery.addQueryParameters(queryParams);
					final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());
			if (CollectionUtils.isNotEmpty(result))
			{
				JnJProductModel = result.get(0);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error("getProductModelByCode()" + Logging.HYPHEN + "model is not found in hybris database for the code - " + code
					+ " - " + modelNotFoundException.getMessage(), modelNotFoundException);
			return null;
		}

		return JnJProductModel;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelByUpcCode(final String upcCode, final CatalogVersionModel catalogVersionModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelByUpcCode()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
		List<JnJProductModel> result = null;
		try
		{
			ServicesUtil.validateParameterNotNull(upcCode, " processed Indentfier must not be null");
			final Map queryParams = new HashMap();
			final String query = PRODUCT_MODELS_BY_UPC_CODE;
			queryParams.put("upcCode", upcCode);
			queryParams.put("catalogVersion", catalogVersionModel);
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getProductModelByUpcCode()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJProductModel Query " + fQuery);
			}
			result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnJProductModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());
			if (CollectionUtils.isEmpty(result))
			{
				return null;
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error("getProductModelByUpcCode()" + Logging.HYPHEN + "model is not found in hybris database for the code - "
					+ upcCode + " - " + modelNotFoundException.getMessage(), modelNotFoundException);
			return null;
		}
		return result;
	}

	@Override
	public JnJProductModel getModProductByBase(final String baseProductCode, final String baseProductPk)
	{
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("productPk", baseProductPk);
		queryParams.put("productCode", baseProductCode);

		final List<JnJProductModel> products = doSearch(SELECT_MOD_PRODUCT, queryParams, null);
		return products.size() > 0 ? products.get(0) : null;
	}


	@Override
	public JnJProductModel getProductForOrderTemplate(String code, final String sourceId)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelByCode()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		code = StringUtils.upperCase(code);
		JnJProductModel JnJProductModel = null;
		try
		{
			ServicesUtil.validateParameterNotNull(code, " processed Indentfier must not be null");
			final Map queryParams = new HashMap();
			final String query = PRODUCT_MODEL_BY_CODE;
			queryParams.put("code", code);
			if (sourceId.equals(Jnjb2bCoreConstants.SalesAlignment.MDD))
			{
				queryParams.put("catalogVersion",
						catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE));
			}
			else if (sourceId.equals(Jnjb2bCoreConstants.SalesAlignment.CONSUMER))
			{
				queryParams.put("catalogVersion", catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_CATALOG_ID,
						Jnjb2bCoreConstants.ONLINE));
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

			fQuery.addQueryParameters(queryParams);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getProductModelByCode()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnJProductModel Query " + fQuery);
			}
			final List<JnJProductModel> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnJProductModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());
			if (CollectionUtils.isNotEmpty(result))
			{
				JnJProductModel = result.get(0);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error("getProductModelByCode()" + Logging.HYPHEN + "model is not found in hybris database for the code - " + code
					+ " - " + modelNotFoundException.getMessage(), modelNotFoundException);
			return null;
		}
		return JnJProductModel;
	}

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 * 
	 * @param sortBy
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	@Override
	public Collection<JnjGTProductCpscDetailModel> getConsumerProductsCpsia(final String sortBy)
	{
		final String METHOD_NAME = "getConsumerProductsCpsia";
		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOGGER);

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("catalogVersion",
				catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_CATALOG_ID, Jnjb2bCoreConstants.ONLINE));

		final StringBuilder queryString = new StringBuilder();
		queryString.append(SELECT_CPSIA_DETAILS);

		addSortToCPSIAQuery(sortBy, queryString);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.addQueryParameters(queryParams);

		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Going to hit DB for the CPSIA data :: QUERY :: " + queryString,
				LOGGER);
		final List<JnjGTProductCpscDetailModel> result = getFlexibleSearchService().<JnjGTProductCpscDetailModel> search(fQuery)
				.getResult();
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "DB returned CPSIA data :: " + result, LOGGER);

		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return result;
	}

	/**
	 * This method adds the sorting string to the query for CPSIA
	 * 
	 * @param sortBy
	 * @param queryString
	 */
	protected void addSortToCPSIAQuery(final String sortBy, final StringBuilder queryString)
	{
		final String METHOD_NAME = "addSortToCPSIAQuery";
		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOGGER);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Adding sort query string for :: " + sortBy, LOGGER);

		String sortColumn = "";
		String sortOrder = "";
		if (StringUtils.isNotEmpty(sortBy))
		{
			sortColumn = sortBy.split(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL)[0];
			sortOrder = sortBy.split(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL)[1];
		}

		/** Adding sort query as per the sorting requested **/
		if (StringUtils.isEmpty(sortColumn) || PRD_CODE.equalsIgnoreCase(sortColumn))
		{
			queryString.append(ORDER_BY_CPSIA_PRODUCT_CODE);
		}
		else if (LOT.equalsIgnoreCase(sortColumn))
		{
			queryString.append(ORDER_BY_CPSIA_LOT_NUMBER);
			queryString.append(Jnjb2bCoreConstants.SPACE + sortOrder);
			queryString.append(ORDER_BY_CPSIA_LOT_NUMBER_2);
		}
		else if (OUTERCASE.equalsIgnoreCase(sortColumn))
		{
			queryString.append(ORDER_BY_CPSIA_OUTERCASE);
		}
		else if (UPC.equalsIgnoreCase(sortColumn))
		{
			queryString.append(ORDER_BY_CPSIA_UPC_CODE);
		}
		else
		{
			queryString.append(ORDER_BY_CPSIA_MODIFIED_DATE);
		}
		queryString.append(Jnjb2bCoreConstants.SPACE + sortOrder);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Sorting string added", LOGGER);
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
	}

	@Override
	public Collection<JnjGTVariantProductModel> getVariantsOrderedByPkgLvlCode(final String productPk)
	{
		CommonUtil.logMethodStartOrEnd("Update Delivery/Sales GTIN", "getVariantsOrderedByPkgLvlCode()", Logging.START_TIME, LOGGER);

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(ItemModel.PK, productPk);

		final StringBuilder queryString = new StringBuilder();
		queryString.append(SELECT_VARIANTS_ORDER_BY_PKG_LVL_CODE);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.addQueryParameters(queryParams);

		CommonUtil.logDebugMessage("Update Delivery/Sales GTIN", "getVariantsOrderedByPkgLvlCode()", "QUERY :: " + queryString,
				LOGGER);

		final List<JnjGTVariantProductModel> result = getFlexibleSearchService().<JnjGTVariantProductModel> search(fQuery)
				.getResult();

		CommonUtil.logDebugMessage("Update Delivery/Sales GTIN", "getVariantsOrderedByPkgLvlCode()", "Results returned: " + result,
				LOGGER);

		CommonUtil.logMethodStartOrEnd("Update Delivery/Sales GTIN", "getVariantsOrderedByPkgLvlCode()", Logging.END_OF_METHOD,
				LOGGER);
		return result;
	}

	/**
	 * Retrieves all new products which have their first ship effective date or new product start date lying between 7
	 * days thirty days from the current date.
	 * 
	 * @return List<JnJProductModel>
	 */
	@Override
	public List<JnJProductModel> getNewActiveProducts(final CatalogVersionModel catalogVersionModel, final String endDate,
			final String startDate)
	{
		final String METHOD_NAME = "getNewActiveProducts";
		CommonUtil.logMethodStartOrEnd(PCM_NEW_PRODUCTS, METHOD_NAME, Logging.START_TIME, LOGGER);

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("catalogVersion", catalogVersionModel);
		queryParams.put("endDate", endDate);
		queryParams.put("startDate", startDate);
		StringBuilder queryString = new StringBuilder();
		queryString = (catalogVersionModel.getCatalog().getId().equals(Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID)) ? queryString
				.append(SELECT_NEW_CA_ACTIVE_PRODUCTS) : queryString.append(SELECT_NEW_US_ACTIVE_PRODUCTS);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.addQueryParameters(queryParams);

		CommonUtil.logDebugMessage(PCM_NEW_PRODUCTS, METHOD_NAME, "Going to hit DB for newly activated products :: QUERY :: "
				+ queryString, LOGGER);
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
		CommonUtil.logDebugMessage(PCM_NEW_PRODUCTS, METHOD_NAME, "DB returned newly activated products :: " + result, LOGGER);

		CommonUtil.logMethodStartOrEnd(PCM_NEW_PRODUCTS, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return result;
	}


	protected JnJProductModel getConsProductByPartialValue(String code, final boolean ignoreUpc, final String currentSite,
			final boolean admin)
	{
		code = StringUtils.trim(StringUtils.upperCase(code));
		final StringBuilder query = new StringBuilder(PRODUCT_BASE_QUERY);
		query.append(" {p:code}='" + code + "'");
		/*if (currentSite.equals(JnjPCMCoreConstants.PCM))
		{
			 2-1-15 mzeman, Added query for testing (disabled) by default. ** 
			if (Config.getBoolean("useCustomQueryPCM", true) && !ignoreUpc)
			{
				query.append(Config.getString("customQueryPCM",
						" LTRIM({p:materialBaseNum},'0') like ?trimmedCode || '%' or LTRIM({p:upcCode},'0') like ?trimmedCode || '%'"));
			}
			else
			{
				if (ignoreUpc)
				{
					query.append("{p:materialBaseNum}=?code");
				}
				else
				{
					code = LIKE_WILDCARD_CHARACTER + code + LIKE_WILDCARD_CHARACTER;
					query.append("{p:materialBaseNum} like ?code or {p:upcCode} like ?code");
				}
			}
		}*/
		
			/*if (admin)
			{
				if (ignoreUpc)
				{
					 1-26-15 mzeman, Added query for testing (disabled) by default. ** 
					if (Config.getBoolean("useRegExpLike2", false))
					{
						query.append(Config.getString("searchQuery2", " (LTRIM({code},'0')=?trimmedCode)"));
					}
					else
					{
						query.append(" REGEXP_LIKE({p:code},'^[0]*" + code + "$')");
					}
				}
				else
				{
					
					 * 1-26-15 mzeman, Added query for testing (disabled) by default. 1-28-15 Remove ')' **
					 
					if (Config.getBoolean("useRegExpLike3", false))
					{
						query.append(Config
								.getString("searchQuery3",
										" ((LTRIM({code},'0')=?trimmedCode) or (LTRIM({upcCode},'0')=?trimmedCode)) and {p:materialBaseProduct} is null"));
					}
					else
					{
						query.append(" (REGEXP_LIKE({p:code},'^[0]*" + code + "$')" + " or (REGEXP_LIKE({p:upcCode},'^[0]*" + code
								+ "$')) and {p:materialBaseProduct} is null) ");
					}
				}


			}
			else
			{
				 1-28-15 mzeman, Added query for testing (disabled) by default. ** 
				if (Config.getBoolean("useRegExpLike4", true))
				{
					query.append(Config.getString("searchQuery4",
							" ((LTRIM({p:materialBaseNum},'0')=?trimmedCode) or (LTRIM({p:upcCode},'0')=?trimmedCode))"));
				}
				else
				{
					query.append(" (REGEXP_LIKE({p:materialBaseNum},'^[0]*" + code + "$')" + " or REGEXP_LIKE({p:upcCode},'^[0]*"
							+ code + "$'))");
				}
			}*/
		query.append(" or ({p:productCode}='" + code +"')" + "  or ({p:upcCode}='"
				+ code + "')");
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("JnjGTProductDaoImpl-getConsProductByPartialValue() : Executing query for quick add to cart ---" + query);
		}
		final Date startTime = new Date();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		/* 1-26-15 mzeman, Added query for testing (disabled) by default. */
		/*if (Config.getBoolean("trimCode", true))
		{
			queryParams.put("trimmedCode", code.replaceAll("^0+", ""));
		}*/

		queryParams.put("code", code);

		final List<JnJProductModel> products = executeProductQueryByAdmin(query, queryParams, null, admin);
		final Date endTime = new Date();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("-------->Time taken to execute JnjGTProductDaoImpl-getConsProductByPartialValue() Query : "
					+ (endTime.getTime() - startTime.getTime()) / 1000 + "seconds");
		}

		if (CollectionUtils.isNotEmpty(products))
		{
			if (products.size() == 1)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("JnjGTProductDaoImpl-getConsProductByPartialValue() : One match found for query for quick add to cart ---"
							+ query);
				}
				return products.get(0);
			}
			else if (products != null && products.size() == 2)
			{

				
				JnJProductModel activeProduct = null;
				boolean discontinueFlag = false;
				for (final JnJProductModel product : products)
				{
					 String s = product.getModStatus().getCode();
					 System.out.println(s);
					 
					if (JnjGTModStatus.DISCONTINUED.equals(product.getModStatus()))
					{
						discontinueFlag = true;
					}
					else
					{
						activeProduct = product;
					}
				}

				if (activeProduct != null)
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug("JnjGTProductDaoImpl-getConsProductByPartialValue() : Returning Active material when more than one products have the same UPC --- "
								+ activeProduct.getUpcCode());
					}
					return activeProduct;
				}
				else
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug("JnjGTProductDaoImpl-getConsProductByPartialValue() : Multiple matches for same UPC but either both active or discontinued --- "
								+ activeProduct.getUpcCode());
					}
					return null;
				}

			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("JnjGTProductDaoImpl-getConsProductByPartialValue() : No match found for query for quick add to cart ---"
					+ query);
		}
		return null;

	}

	protected ProductModel getMDDProductByValue(String code, final boolean ignoreUpc, final boolean admin)
	{
		code = StringUtils.upperCase(code);
		StringBuilder query = null;
		ProductModel product = null;
		if (admin)
		{

			query = new StringBuilder(PRODUCT_BASE_QUERY);
			query.append("{p:code}=?code ");
			final Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("code", code);
			/*Start 5401 and 5403 restriction type*/
			String allowedFranchise = sessionService.getAttribute("allowedFranchise");
			if(StringUtils.isNotEmpty(allowedFranchise)){
				LOGGER.debug("Allowed Franchises"+ allowedFranchise);
				query.append(" AND {p.franchiseName} IN (" + allowedFranchise + ")");
			
			}
			/*END 5401 and 5403 restriction type*/
			final List<JnJProductModel> products = executeProductQueryByAdmin(query, queryParams, null, admin);
			if (CollectionUtils.isNotEmpty(products))
			{
				product = products.get(0);
			}
			if (product == null && !ignoreUpc)
			{
				query = new StringBuilder(VARIANT_BASE_QUERY);
				query = query.append("where ({p:materialBaseProduct} is null and {vp:EAN}=?code )");
				final List<JnjGTVariantProductModel> variants = executeProductQueryByAdmin(query, queryParams, null, admin);
				if (CollectionUtils.isNotEmpty(variants))
				{
					product = variants.get(0).getBaseProduct();
				}
			}
		}
		else
		{
			query = new StringBuilder(VARIANT_BASE_QUERY);
			query = query.append("where ({p:materialBaseProduct} is null and {p:code}=?code ) and {vp:deliveryGtinInd}=1");
			String allowedFranchise = sessionService.getAttribute("allowedFranchise");
			/*Start 5401 and 5403 restriction type*/
			if(StringUtils.isNotEmpty(allowedFranchise)){
				LOGGER.debug("Allowed Franchises"+ allowedFranchise);
				query.append(" AND {p.franchiseName} IN (" + allowedFranchise + ")");
			
			}
			/*END 5401 and 5403 restriction type*/
			final Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("code", code);
			product = getMDDVariantProduct(query,queryParams);
			if (product == null && !ignoreUpc)
			{
				StringBuilder eanQuery = new StringBuilder(VARIANT_BASE_QUERY);
				eanQuery = eanQuery.append("where {vp:EAN}=?code ");

				product = getMDDVariantProduct(eanQuery,queryParams);
			}
		}
		return product;
	}


	/**
	 * @param query
	 * @return
	 */
	protected JnjGTVariantProductModel getMDDVariantProduct(final StringBuilder query)
	{
		final Date startTime = new Date();
		final List<JnjGTVariantProductModel> variantProducts = doSearch(query.toString(), null, null);
		final Date endTime = new Date();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("-------->Time taken to execute JnjGTProductDaoImpl-getMDDVariantProduct() Query : "
					+ (endTime.getTime() - startTime.getTime()) / 1000 + "seconds");
		}
		JnjGTVariantProductModel variantProduct = null;
		if (CollectionUtils.isNotEmpty(variantProducts))
		{
			if (variantProducts.size() == 1)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : One match found for query for quick add to cart ---"
							+ query);
				}
				variantProduct = variantProducts.get(0);
			}
			else
			{
				ProductModel baseProduct = null;
				for (final JnjGTVariantProductModel variant : variantProducts)
				{
					if (baseProduct != null)
					{
						if (!baseProduct.equals(variant.getBaseProduct()))
						{
							if (LOGGER.isDebugEnabled())
							{
								LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : More than one match found for query for quick add to cart ---"
										+ query);
							}
							return null;
						}
					}
					else
					{
						baseProduct = variant.getBaseProduct();
					}
				}
				variantProduct = variantProducts.get(0);
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : No match found for query for quick add to cart ---"
						+ query);
			}
		}
		return variantProduct;
	}
	private JnjGTVariantProductModel getMDDVariantProduct(final StringBuilder query, Map<String, Object> queryParams)
	{
		final Date startTime = new Date();
		final List<JnjGTVariantProductModel> variantProducts = doSearch(query.toString(), queryParams, null);
		final Date endTime = new Date();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("-------->Time taken to execute JnjGTProductDaoImpl-getMDDVariantProduct() Query : "
					+ (endTime.getTime() - startTime.getTime()) / 1000 + "seconds");
		}
		JnjGTVariantProductModel variantProduct = null;
		if (CollectionUtils.isNotEmpty(variantProducts))
		{
			if (variantProducts.size() == 1)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : One match found for query for quick add to cart ---"
							+ query);
				}
				variantProduct = variantProducts.get(0);
			}
			else
			{
				ProductModel baseProduct = null;
				for (final JnjGTVariantProductModel variant : variantProducts)
				{
					if (baseProduct != null)
					{
						if (!baseProduct.equals(variant.getBaseProduct()))
						{
							if (LOGGER.isDebugEnabled())
							{
								LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : More than one match found for query for quick add to cart ---"
										+ query);
							}
							return null;
						}
					}
					else
					{
						baseProduct = variant.getBaseProduct();
					}
				}
				variantProduct = variantProducts.get(0);
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("JnjGTProductDaoImpl-getMDDVariantProduct() : No match found for query for quick add to cart ---"
						+ query);
			}
		}
		return variantProduct;
	}

	protected <T> List<T> executeProductQueryByAdmin(final StringBuilder query, final Map<String, Object> queryParams,
			final List<Class> resultClasses, final boolean isAdmin)
	{
		if (isAdmin)
		{
			query.append("and {catalogVersion} IN (?catalogVersion)");
			LOGGER.debug("query="+query);
			LOGGER.debug("catalogVersionService.getSessionCatalogVersions()="+catalogVersionService.getSessionCatalogVersions());
			queryParams.put("catalogVersion", catalogVersionService.getSessionCatalogVersions());			
			//queryParams.put("catalogVersion",queryParams.get("catalogVersion"));
			final List<T> result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<T> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
					fQuery.addQueryParameters(queryParams);
					final List<T> result = getFlexibleSearchService().<T> search(fQuery).getResult();
					return result;
				}
			}, userService.getAdminUser());
			return result;
		}
		else
		{
			return doSearch(query.toString(), queryParams, resultClasses);
		}
	}

	@Override
	public List<JnJProductModel> getAllProductsForSite(final String currentSite, final String accountPK)
	{
		StringBuilder query = null;
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		final String MDD_PRODUCT_QUERY = "SELECT {product:pk} FROM {JnJProduct  as product JOIN ArticleApprovalStatus AS approval on {approval:pk}={product:approvalStatus} JOIN CategoryProductRelation as cat_rel ON {cat_rel:target} = {product:pk} JOIN Category as cat ON {cat_rel:source}={cat:pk}} WHERE  {cat:catalogVersion} IN (?catalogVersion) AND {product:modStatus} NOT IN ({{select {pk} from {JnjGTModStatus} where {code} = 'NOTAPPLICABLE'}}) AND {product:jnjPortalInd} = 1  AND {product:materialBaseProduct} IS NULL  AND ( {product:offlineDate} IS NULL OR {product:offlineDate} >= CONVERT(DATETIME,?currentDate) ) AND ({product:onlineDate} IS NULL OR {product:onlineDate} <=CONVERT(DATETIME,?currentDate)) AND {cat:code} != 'DefaultCategory' AND {approval:code} ='approved' ";
		final String CONS_PRODUCT_QUERY = "SELECT {product:pk} FROM {JnJProduct  as product JOIN ArticleApprovalStatus AS approval on {approval:pk}={product:approvalStatus} JOIN CategoryProductRelation as cat_rel ON {cat_rel:target} = {product:pk} JOIN Category as cat ON {cat_rel:source}={cat:pk} JOIN JnjB2bUnitToIncludedProducts as pg ON {pg:target}={product:pk}} WHERE  {cat:catalogVersion} IN (?catalogVersion) AND {pg:source}=?accountPK AND {product:modStatus} NOT IN ({{select {pk} from {JnjGTModStatus} where {code} = 'NOTAPPLICABLE'}}) AND	( {product:offlineDate} IS NULL OR {product:offlineDate} >= CONVERT(DATETIME,?currentDate)) AND	({product:onlineDate} IS NULL OR {product:onlineDate} <=CONVERT(DATETIME,?currentDate)) AND {cat:code} != 'DefaultCategory' AND {approval:code} = 'approved' ";
		final Calendar currentDateCalendar = Calendar.getInstance();
		currentDateCalendar.setTime(new Date());
		final String currentDate = currentDateCalendar.get(Calendar.YEAR) + "-" + (currentDateCalendar.get(Calendar.MONTH) + 1)
				+ "-" + currentDateCalendar.get(Calendar.DATE);
		queryParams.put("currentDate", currentDate);
		if (currentSite.equals(Jnjb2bCoreConstants.MDD))
		{
			query = new StringBuilder(MDD_PRODUCT_QUERY);

		}
		else
		{
			query = new StringBuilder(CONS_PRODUCT_QUERY);
			queryParams.put("accountPK", accountPK);
		}
		query.append(" and {cat:catalogVersion} IN (?catalogVersion)");
		queryParams.put("catalogVersion", catalogVersionService.getSessionCatalogVersions());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (Integer.valueOf(Config.getParameter("test.limit.for.MDD.catalog.export")).intValue() > 0)
		{
			fQuery.setCount(Integer.valueOf(Config.getParameter("test.limit.for.MDD.catalog.export")).intValue());
		}
		fQuery.addQueryParameters(queryParams);
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
		return result;
	}



	/**
	 * This is the method to fetch the list of all products in the PCM application.
	 */
	@Override
	public List<JnJProductModel> getAllProductsForPCM(final CatalogModel catalog)
	{
		final String PCM_PRODUCT_QUERY = "SELECT {product:pk} FROM {JnJProduct  as product } WHERE  {product:catalogVersion} NOT IN (?catalogVersion)";
		final StringBuilder query = new StringBuilder(PCM_PRODUCT_QUERY);
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("catalogVersion", catalog.getCatalogVersions());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
		return result;
	}
	
	public List<JnJProductModel> getMDDProductByMODValue(String code, final boolean admin)
	{
		code = StringUtils.upperCase(code);
		StringBuilder query = null;
		List<JnJProductModel> products = null;
		query = new StringBuilder(PRODUCT_BASE_QUERY);
		query.append(" {p:code} = '" + code + "'");
		query.append(" OR ");
		query.append(" {p:code} like '" + code + "-%'");
		
		/*Commenting for defect GTUX_147 For Base Product*/ 

		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("catalogVersion",
				catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE));

		products = executeProductQueryByAdmin(query, queryParams, null, admin);
		if (LOGGER.isDebugEnabled())
		{
			if (products.size() > 0)
			{
				LOGGER.debug("JnjGTProductDaoImpl-getMDDProductByMODValue() : return products size is : " + products.size()
						+ " and the query is:" + query + " with requested code/ean:" + code + "  Catalog version "
						+ queryParams.get("catalogVersion"));
			}
			else
			{
				LOGGER.debug("JnjGTProductDaoImpl-getMDDProductByMODValue() : no product found for  requested code/ean:" + code
						+ " and the query is:" + query);
			}
		}
		return products;
	}

	@Override
	public List<JnJProductModel> getProductsBoughtTogether(final List<String> allowedFranchisesCode, final String startProductPK, final Interval<Calendar> dates, final int quantity) {
		final String methodName = "getProductsBoughtTogether()";
		final Map<String, Object> queryParams = new HashMap<>();

		queryParams.put("startProductPK", startProductPK);
		queryParams.put("startDate", getDbFormattedDate(dates.getStart()));
		queryParams.put("endDate", getDbFormattedDate(dates.getEnd()));
		queryParams.put("quantity", quantity);

		if(CollectionUtils.isNotEmpty(allowedFranchisesCode)) {
			queryParams.put("allowedFranchisesCode", allowedFranchisesCode);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryGetAllProductsClientAlsoBought(allowedFranchisesCode));
		fQuery.addQueryParameters(queryParams);
		fQuery.setDisableSearchRestrictions(true);
		LOGGER.debug(methodName + Jnjb2bCoreConstants.Logging.HYPHEN + " Query " + fQuery);
		return getFlexibleSearchService().<JnJProductModel>search(fQuery).getResult();
	}
	private String getDbFormattedDate(final Calendar date) {
		return date.get(Calendar.YEAR)
				+ "-" + (date.get(Calendar.MONTH) + 1)
				+ "-" + date.get(Calendar.DATE);
	}
	private String queryGetAllProductsClientAlsoBought(final List<String> allowedFranchisesCode) {
		StringBuilder query = new StringBuilder();

		query.append("SELECT TOP (?quantity) {product:PK}");
		query.append("FROM {JnJProduct as product");
		query.append("    JOIN OrderEntry as orderEntry");
		query.append("        ON {product:PK} = {orderEntry:product}}");
		query.append("WHERE EXISTS ({{");
		query.append("    SELECT 1");
		query.append("    FROM {OrderEntry as iOrderEntry}");
		query.append("    WHERE {iOrderEntry:product} = ?startProductPK");
		query.append("        AND {iOrderEntry:creationTime} >= CONVERT(DATETIME,?startDate)");
		query.append("        AND {iOrderEntry:creationTime} <= CONVERT(DATETIME,?endDate)");
		query.append("        AND EXISTS ({{");
		query.append("            SELECT 1");
		query.append("            FROM {Order as order}");
		query.append("            WHERE {order:pk} = {iOrderEntry:order}");
		query.append("                AND EXISTS ({{");
		query.append("                    SELECT 1");
		query.append("                    FROM {JnjOrderTypesEnum as orderTypes}");
		query.append("                    WHERE {orderTypes:code} = 'ZOR'");
		query.append("                        AND {order:ordertype} = {orderTypes:PK}");
		query.append("                }})");
		query.append("            }})");
		query.append("            AND {orderEntry:order} = {iOrderEntry:order}");
		query.append("    }})");
		query.append("    AND EXISTS ({{");
		query.append("        SELECT 1");
		query.append("        FROM {JnJGTModStatus as modStatus}");
		query.append("        WHERE {modStatus:code} = 'ACTIVE'");
		query.append("            AND {product:modstatus} = {modStatus:PK}");
		query.append("    }})");

		if(CollectionUtils.isNotEmpty(allowedFranchisesCode)){
			query.append("    	AND {product:franchiseName} IN (?allowedFranchisesCode)");
		}

		query.append("    AND {product:PK} != ?startProductPK");
		query.append(" GROUP BY {product:PK}, {product:code}");
		query.append(" ORDER BY COUNT({product:PK}) DESC, {product:code}");

		return query.toString();
	}
}