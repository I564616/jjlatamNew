/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.daos.impl;


import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.impl.DefaultJnjGTProductDao;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.daos.JnjLaProductDao;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.model.JnjCommercialUomConversionModel;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 */
public class JnjLaProductDaoImpl extends DefaultJnjGTProductDao implements JnjLaProductDao
{

	private static final Logger LOGGER = Logger.getLogger(JnjLaProductDaoImpl.class);
	private static final String SELECT="SELECT {";
	private static final String FROM="} FROM {";
	private static final String WHERE=" WHERE {";
	private static final String QUESTION_MARK="} = ?";
	private static final String EXCEPTION="Exception occured. Cascading to Business Exception. ";
	private static final String EXCEPTION_01="No [catalogId] given. Cascading to Business Exception. ";
	private static final String EXCEPTION_02="No [catalogId] given.";
	private static final String EXCEPTION_03="No [catalogVersion] given. Cascading to Business Exception. ";
	private static final String EXCEPTION_04="No [catalogVersion] given.";
	private static final String CATALOG_VERSION="catalogVersion";
	private static final String MODEL_NOT_FOUND_EXCEPTION="ModelNotFoundException occured. Cascading to Business Exception.";
	private static final String AND=" AND {";

	protected static final String PRODUCTS_FOR_CATALOG_SECTOR = "SELECT {prod.pk} FROM {JnJProduct as prod " +
			"join catalogversion as vers on {prod.catalogversion}={vers.pk} join catalog as cat " +
			"on {vers.catalog}={cat.pk}} WHERE {prod.modifiedtime} >=CONVERT(DATETIME,?fromDate) AND {cat.id} " +
			"IN (?catalogid) AND {vers.version}=(?catalogversion) AND {prod.sector} = (?sector)";

	protected static final String PRODUCTS_FOR_CATALOG_AND_PRODUCT_CODES = "Select {pk} from {JnJProduct} where {code} IN (?productCodes) and {catalogVersion} IN (?catalogVersion)";

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	protected ModelService modelService;

	public JnjLaProductDaoImpl(final String typecode)
	{
		super(typecode);
	}

	/**
	 * This method is used to fetch the JnjCommercialUomConversionModel on the basis of given iso code
	 *
	 *
	 * @param isoCode
	 * @return listJnjCommercialUomConversionModel
	 */
	@Override
	public List<JnjCommercialUomConversionModel> getCommercialUom(final String isoCode)
	{
		final String METHOD_NAME = "getCommercialUom()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		List<JnjCommercialUomConversionModel> result = null;
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnjCommercialUomConversionModel._TYPECODE)
				.append("}").append(WHERE).append(JnjCommercialUomConversionModel.ISOCODE).append(QUESTION_MARK)
				.append(JnjCommercialUomConversionModel.ISOCODE);

		final Map parameters = new HashMap();
		parameters.put(JnjCommercialUomConversionModel.ISOCODE, isoCode);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		try
		{
			result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public List<JnjCommercialUomConversionModel> execute()
				{
					final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
					fQuery.addQueryParameters(parameters);
					final List<JnjCommercialUomConversionModel> result = getFlexibleSearchService()
							.<JnjCommercialUomConversionModel> search(fQuery).getResult();
					return result;

				}
			}, userService.getAdminUser());
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{

			throw new ModelNotFoundException(modelNotFoundException);
		}
		catch (final Exception exception)
		{
			throw new IllegalArgumentException(exception);
		}
		return result;
	}

	/**
	 * Get the Product sales org model on the basis of product code and sales org code.
	 *
	 * @param code
	 *           String
	 * @param salesOrgCode
	 *           String
	 * @param activeFlag
	 *           the active flag
	 * @return JnJProductSalesOrgModel
	 */
	@Override
	public JnJProductSalesOrgModel getJnjProductSalesOrgModel(final String code, final String salesOrgCode,
			final boolean activeFlag)
	{
		final String METHOD_NAME = "getJnjProductSalesOrgModel ()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		final String query = "select {pk} from {JnjProductSalesOrg} where {productCode}=?code and {salesOrg}=?salesOrgCode and {active}=?active";
		JnJProductSalesOrgModel result = null;
		final Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("salesOrgCode", salesOrgCode);
		params.put("active", activeFlag ? "1" : "0");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		try {
			result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnJProductSalesOrgModel execute()
				{
					return getFlexibleSearchService().searchUnique(fQuery);
				}
			}, userService.getAdminUser());
		} catch (final ModelNotFoundException e) {
			String message = "model not found exception for given ID" + "-" + e.getLocalizedMessage();
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, message, JnjLaProductDaoImpl.class);
		} catch (final AmbiguousIdentifierException e) {
			String message = "ambiguous identifier exception for given ID" + "-" + e.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime();
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, message, e, JnjLaProductDaoImpl.class);
		} catch (final Exception e) {
			String message = "generic exception for given ID" + "-" + e.getLocalizedMessage();
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, message, e, JnjLaProductDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);

		return result;
	}

	@Override
	public Collection<String> getActiveProductCodes(final String catalogId) throws BusinessException
	{
		final String METHOD_NAME = "getActiveProductCodes()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		final Set<String> activeProductCodesSet = new HashSet<>();
		List<JnJLaProductModel> activeProducts = null;
		final List<String> activeProductCodesList = new ArrayList<>();
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnJLaProductModel._TYPECODE)
				.append("}").append(WHERE).append(JnJProductModel.CATALOGID).append(QUESTION_MARK)
				.append(JnJProductModel.CATALOGID);

		final Map parameters = new HashMap();
		parameters.put(JnJLaProductModel.CATALOGID, catalogId.toUpperCase());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		try
		{
			final SearchResult<JnJLaProductModel> result = getFlexibleSearchService().search(fQuery);
			activeProducts = result.getResult();
			if (CollectionUtils.isNotEmpty(activeProducts))
			{
				for (final JnJLaProductModel jnJProductModel : activeProducts)
				{
					activeProductCodesSet.add(jnJProductModel.getCode());
				}
			}
			activeProductCodesList.addAll(activeProductCodesSet);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"ModelNotFoundException occured. Cascading to Business Exception. ", modelNotFoundException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());

		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"AmbiguousIdentifierException occured. Cascading to Business Exception. ", ambiguousIdentifierException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION, exception, JnjLaProductDaoImpl.class);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);

		return activeProductCodesList;
	}

	@Override
	public List<JnJProductModel> getActiveProducts(final String catalogId, final CatalogVersionModel catalogVersion)
			throws BusinessException
	{
		final String METHOD_NAME = "getActiveProducts()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		final List<JnJProductModel> jnJTempProductModelList = new ArrayList<>();

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnJLaProductModel._TYPECODE).append("}")
				.append(WHERE).append(ProductModel.CATALOGVERSION).append(QUESTION_MARK).append(ProductModel.CATALOGVERSION)
				.append(AND).append(JnJProductModel.CATALOGID).append(QUESTION_MARK).append(JnJProductModel.CATALOGID);

		final Map parameters = new HashMap();
		parameters.put(JnJLaProductModel.CATALOGVERSION, catalogVersion);
		parameters.put(JnJLaProductModel.CATALOGID, catalogId.toUpperCase());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		try
		{
			final SearchResult<JnJLaProductModel> result = getFlexibleSearchService().search(fQuery);
			result.getResult();
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"ModelNotFoundException occured. Cascading to Business Exception. ", modelNotFoundException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());

		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"AmbiguousIdentifierException occured. Cascading to Business Exception. ", ambiguousIdentifierException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION, exception, JnjLaProductDaoImpl.class);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		//return jnJProductModelList;
		return jnJTempProductModelList;
	}

	@Override
	public JnJProductModel getProduct(final CatalogVersionModel catalogVersion, final String productCode, final String catalogId)
			throws BusinessException
	{
		final String METHOD_NAME = "getProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		if (StringUtils.isEmpty(productCode))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"No [productCode] given. Cascading to Business Exception. ", JnjLaProductDaoImpl.class);
			throw new BusinessException("No [productCode] given.");
		}
		else if (StringUtils.isEmpty(catalogId))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION_01, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_02);
		}
		else if (null == catalogVersion)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION_03, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_04);
		}

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
				"Fetching Product for Code [" + productCode + "] , Catalog ID [" + catalogId.toUpperCase() + "] adn Catalog Version ["
						+ catalogVersion.getCatalog().getName() + Logging.HYPHEN + catalogVersion.getVersion() + "]. ",
				JnjLaProductDaoImpl.class);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnJLaProductModel._TYPECODE).append("}")
				.append(WHERE).append(ProductModel.CODE).append(QUESTION_MARK).append(ProductModel.CODE).append(AND)
				.append(ProductModel.CATALOGVERSION).append(QUESTION_MARK).append(ProductModel.CATALOGVERSION).append(AND)
				.append(JnJProductModel.CATALOGID).append(QUESTION_MARK).append(JnJProductModel.CATALOGID);

		final Map parameters = new HashMap();
		parameters.put(JnJLaProductModel.CODE, productCode);
		parameters.put(JnJLaProductModel.CATALOGVERSION, catalogVersion);
		parameters.put(JnJLaProductModel.CATALOGID, catalogId.toUpperCase());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		JnJLaProductModel JnJLaProductModel = null;

		try
		{
			JnJLaProductModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnJLaProductModel execute()
				{
					return getFlexibleSearchService().searchUnique(fQuery);
				}
			}, userService.getAnonymousUser());
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					MODEL_NOT_FOUND_EXCEPTION + modelNotFoundException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());
		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"AmbiguousIdentifierException occured. Cascading to Business Exception.", ambiguousIdentifierException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION, exception, JnjLaProductDaoImpl.class);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		return JnJLaProductModel;
	}

	/**
	 * @param country
	 * @return countryModel
	 */
	@Override
	public CountryModel getCountryModelByIsoOrPk(final String country)
	{
		ServicesUtil.validateParameterNotNull(country, "Country Id/Isocode must not be null");

		final Map queryParams = new HashMap();
		String query = null;
		if (country.length() > 3)
		{
			query = "select {pk} from {Country} where {pk}=?pk";
			queryParams.put("pk", country);
		}
		else
		{
			query = "select {pk} from {Country} where {isocode}=?isocode";
			queryParams.put("isocode", country);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);

		final CountryModel countryModel = (CountryModel) getFlexibleSearchService().searchUnique(fQuery);

		return countryModel;
	}

	@Override
	public List<JnJProductModel> getMDDProductByMODValue(final String code, final boolean admin)
	{
		StringUtils.upperCase(code);
		StringBuilder query = null;
		List<JnJProductModel> products = null;
		query = new StringBuilder(PRODUCT_BASE_QUERY);
		query.append(" {p:code} = '" + code + "'");
		query.append(" OR ");
		query.append(" {p:code} like '" + code + "-%'");

		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CATALOG_VERSION, catalogVersionService
				.getCatalogVersion(cmsSiteService.getCurrentSite().getDefaultCatalog().getId(), Jnjb2bCoreConstants.ONLINE));

		products = executeProductQueryByAdmin(query, queryParams, null, admin);
		if (LOGGER.isDebugEnabled())
		{
			if (!products.isEmpty())
			{
				LOGGER.debug("JnjGTProductDaoImpl-getMDDProductByMODValue() : return products size is : " + products.size()
						+ " and the query is:" + query + " with requested code/ean:" + code + "  Catalog version "
						+ queryParams.get(CATALOG_VERSION));
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
	public JnJProductModel getProductbyCatalogId(final CatalogVersionModel catalogVersion, final String catalogId)
			throws BusinessException
	{
		final String METHOD_NAME = "getProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);


		if (StringUtils.isEmpty(catalogId))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION_01, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_02);
		}
		else if (null == catalogVersion)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION_03, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_04);
		}


		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnJLaProductModel._TYPECODE).append("}")
				.append(WHERE).append(ProductModel.CATALOGVERSION).append(QUESTION_MARK).append(ProductModel.CATALOGVERSION)
				.append(AND).append(JnJProductModel.CATALOGID).append(QUESTION_MARK).append(JnJProductModel.CATALOGID)
				.append(AND).append(JnJProductModel.ECOMMERCEFLAG).append(QUESTION_MARK).append(JnJProductModel.ECOMMERCEFLAG);

		final Map parameters = new HashMap();
		parameters.put(JnJLaProductModel.CATALOGVERSION, catalogVersion);
		parameters.put(JnJLaProductModel.CATALOGID, catalogId.toUpperCase());
		parameters.put(JnJLaProductModel.ECOMMERCEFLAG, Jnjlab2bcoreConstants.NUMBER_ONE);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		JnJLaProductModel JnJLaProductModel = null;

		try
		{
			JnJLaProductModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnJLaProductModel execute()
				{
					return getFlexibleSearchService().searchUnique(fQuery);
				}
			}, userService.getAnonymousUser());
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					MODEL_NOT_FOUND_EXCEPTION + modelNotFoundException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());
		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"AmbiguousIdentifierException occured. Cascading to Business Exception.", ambiguousIdentifierException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					EXCEPTION, exception, JnjLaProductDaoImpl.class);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		return JnJLaProductModel;
	}

	@Override
	public JnJProductModel getProductByCatalogIdWithoutSearchRestriction(final String catalogId) throws BusinessException
	{
		final String methodName = "getProductByCatalogIdWithoutSearchRestriction()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);
		final CatalogVersionModel catalogVersion = catalogVersionService
				.getCatalogVersion(cmsSiteService.getCurrentSite().getDefaultCatalog().getId(), Jnjb2bCoreConstants.ONLINE);

		if (StringUtils.isEmpty(catalogId))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					EXCEPTION_01, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_02);
		}
		else if (null == catalogVersion)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					EXCEPTION_03, JnjLaProductDaoImpl.class);
			throw new BusinessException(EXCEPTION_04);
		}


		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT).append(ItemModel.PK).append(FROM).append(JnJLaProductModel._TYPECODE).append("}")
				.append(WHERE).append(ProductModel.CATALOGVERSION).append(QUESTION_MARK).append(ProductModel.CATALOGVERSION)
				.append(AND).append(JnJProductModel.CATALOGID).append(QUESTION_MARK).append(JnJProductModel.CATALOGID)
				.append(AND).append(ProductModel.CODE).append(QUESTION_MARK).append(ProductModel.CODE)
				.append(AND).append(JnJProductModel.PRODUCTSTATUSCODE).append("} IN (?")
				.append(JnJProductModel.PRODUCTSTATUSCODE).append(")")
				.append(AND).append(JnJProductModel.ECOMMERCEFLAG).append(QUESTION_MARK).append(JnJProductModel.ECOMMERCEFLAG);

		final Map parameters = new HashMap();
		parameters.put(JnJLaProductModel.CATALOGVERSION, catalogVersion);
		parameters.put(JnJLaProductModel.CATALOGID, catalogId.toUpperCase());
		parameters.put(JnJLaProductModel.CODE, catalogId.toUpperCase());
		final List<String> validProducStatusList = Arrays.asList("D2", "D3");
		parameters.put(JnJLaProductModel.PRODUCTSTATUSCODE, validProducStatusList);
		parameters.put(JnJLaProductModel.ECOMMERCEFLAG, Jnjlab2bcoreConstants.NUMBER_ONE);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(parameters);
		JnJLaProductModel JnJLaProductModel = null;
		LOGGER.debug("Flexible query for AddTo cart from contract page ##########################:::::::::"+fQuery);
		try
		{
			JnJLaProductModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public JnJLaProductModel execute()
				{
					return getFlexibleSearchService().searchUnique(fQuery);
				}
			}, userService.getAdminUser());
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					MODEL_NOT_FOUND_EXCEPTION + modelNotFoundException,
					JnjLaProductDaoImpl.class);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());
		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"There are two products with the same catalog id: " + catalogId
							+ ". Fetching product considering the Search restriction for ecommerceflag=1",
					ambiguousIdentifierException, JnjLaProductDaoImpl.class);
			return getProductbyCatalogId(catalogVersion, catalogId);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					EXCEPTION, exception, JnjLaProductDaoImpl.class);
			throw new BusinessException(exception.getLocalizedMessage());
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		return JnJLaProductModel;
	}

	@Override
	public boolean removeAllProductSalesOrgForProduct(final String productCode)
	{
		final String methodName = "removeAllProductSalesOrgForProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Removing old sales orgs for product: " + productCode,
				JnjLaProductDaoImpl.class);

		List<JnJProductSalesOrgModel> productSalesOrgList = null;

		final String query = "select {pk} from {JnjProductSalesOrg} where {productCode}=?code";
		final Map<String, Object> params = new HashMap<>();
		params.put("code", productCode);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		try
		{
			productSalesOrgList = getFlexibleSearchService().<JnJProductSalesOrgModel> search(fQuery).getResult();

			if (CollectionUtils.isNotEmpty(productSalesOrgList))
			{
				JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"Found " + productSalesOrgList.size() + " old product sales orgs. Removing them all.",
						JnjLaProductDaoImpl.class);
				modelService.removeAll(productSalesOrgList);
			}
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName, exception.getMessage(), exception,
					JnjLaProductDaoImpl.class);

			return false;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		return true;
	}

	@Override
	public List<JnJProductModel> getProductsForSector(String startDate, String catalogID, String catalogVersion, String sector) {
		final String methodName = "getProductsForSector()";
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(Jnjlab2bcoreConstants.FROM_DATE, startDate);
		queryParams.put(Jnjb2bCoreConstants.CATALOG_ID, Arrays.asList(catalogID));
		queryParams.put(Jnjb2bCoreConstants.CATALOG_VERSION, catalogVersion);
		queryParams.put(Jnjlab2bcoreConstants.SECTOR, sector);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(PRODUCTS_FOR_CATALOG_SECTOR);
		fQuery.addQueryParameters(queryParams);
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(fQuery).getResult();
		LOGGER.debug(methodName+" Result Count:"+result.size());
		return result;
	}

	@Override
	public List<JnJProductModel> getProductsForCatalogVersion(CatalogVersionModel catalogVersion, List<String> productCodes) {
		final String methodName = "getProductsForCatalogVersion()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductDaoImpl.class);
		final Map<String, Object> queryParams = new HashMap<>();
		List<JnJProductModel> result = null;
		queryParams.put(CATALOG_VERSION, catalogVersion);
		queryParams.put("productCodes", productCodes);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(PRODUCTS_FOR_CATALOG_AND_PRODUCT_CODES);
		fQuery.addQueryParameters(queryParams);
		try {
			result = getFlexibleSearchService().<JnJProductModel>search(fQuery).getResult();
			LOGGER.debug(methodName + " Products Result Count: " + result.size());
		} catch (final Exception ex) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName, ex.getMessage(), ex,
					JnjLaProductDaoImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjLaProductDaoImpl.class);
		return result;
	}

}
