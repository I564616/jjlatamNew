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
package com.jnj.la.core.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import com.jnj.core.util.Interval;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.util.Config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.services.JnjLaSalesOrgCustomerService;
import com.jnj.core.services.impl.DefaultJnJGTProductService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.daos.JnjLaProductDao;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.model.JnjCommercialUomConversionModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.customer.JnjLaCustomerEligibilityService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.services.CMSSiteService;

public class JnjLaProductServiceImpl extends DefaultJnJGTProductService implements JnJLaProductService
{
	private static final Logger LOGGER = Logger.getLogger(JnjLaProductServiceImpl.class);

	@Autowired
	private JnjLaProductDao jnjLaProductDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ModelService modelService;

	/** The jnj get current default b2b unit util. */
	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private JnjLaCustomerEligibilityService customerEligibilityService;

	@Autowired
	protected CatalogService catalogService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private JnjLaSalesOrgCustomerService jnjLaSalesOrgCustomerService;

	@Override
	public List<JnjCommercialUomConversionModel> getCommercialUom(final String isoCode)
	{
		return jnjLaProductDao.getCommercialUom(isoCode);
	}

	@Override
	public boolean saveUomConversionModel(final JnjUomConversionModel jnjUomConversionModel) throws ModelSavingException
	{
		final String methodName = "saveUomConversionModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);
		boolean success = false;
		try
		{
			JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, "BEFORE SAVE CALL", JnjLaProductServiceImpl.class);

			modelService.saveAll(jnjUomConversionModel);
			success = true;
			JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, "AFTER SUCCESSFULLY SAVE JnjUomConversionModel MODEL",
					JnjLaProductServiceImpl.class);
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE, methodName,
					"model not saved into Hybris data base" + "-" + modelSavingException.getLocalizedMessage(), modelSavingException,
					JnjLaProductServiceImpl.class);
			success = false;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return success;
	}

	/**
	 * This method is used to get the JnJProductSalesOrgModel on the basis of product code and sales org code.
	 *
	 * @param code
	 *           String
	 * @param salesOrgCode
	 *           String
	 * @param activeFlag
	 *           the active flag
	 * @return JnJProductSalesOrgModel JnJProductSalesOrgModel
	 */
	@Override
	public JnJProductSalesOrgModel getJnjProductSalesOrgModel(final String code, final String salesOrgCode,
			final boolean activeFlag)
	{
		return jnjLaProductDao.getJnjProductSalesOrgModel(code, salesOrgCode, activeFlag);
	}

	@Override
	public boolean saveProductSalesOrgModel(final JnJProductSalesOrgModel jnjProductSalesOrgModel) throws ModelSavingException
	{

		final String methodName = "saveProductSalesOrgModel ()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		boolean success = false;
		try
		{
			JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, "BEFORE SAVE CALL", JnjLaProductServiceImpl.class);

			modelService.saveAll(jnjProductSalesOrgModel);
			success = true;

			JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, "AFTER SUCCESSFULLY SAVE THE JNJ PRODUCT MODEL",
					JnjLaProductServiceImpl.class);

		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE, methodName,
					"model not saved into Hybris data base" + "-" + modelSavingException.getLocalizedMessage(), modelSavingException,
					JnjLaProductServiceImpl.class);
			success = false;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return success;
	}

	@Override
	public List<JnJProductModel> getAllActiveProducts(final JnJLaProductModel jnJProductModel,
			final CatalogVersionModel catalogVersionModel) throws BusinessException
	{
		final String methodName = "getAllActiveProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		/* Check if Product is NULL */
		if (null == jnJProductModel)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.PRODUCT_SERVICE, methodName,
					"Cannot Fetch Active Product as Product given is NULL. Cascading to Business Exception.",
					JnjLaProductServiceImpl.class);
			throw new BusinessException("Cannot Fetch Active Product as Product given is NULL.");
		}

		final String productCatalogId = jnJProductModel.getCatalogId();
		/* Check if productCatalogId is NULL */
		if (StringUtils.isEmpty(productCatalogId))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.PRODUCT_SERVICE, methodName,
					"Cannot Fetch Active Products as no [productCatalogId] given. Cascading to Business Exception. ",
					JnjLaProductServiceImpl.class);
			throw new BusinessException("Cannot Active Products as no [productCatalogId] given.");
		}

		/* Check if catalogVersionModel is NULL */
		if (null == catalogVersionModel)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.PRODUCT_SERVICE, methodName,
					"Cannot Fetch Product as [CatalogVersion] is NULL.Cascading to Business Exception;",
					JnjLaProductServiceImpl.class);
			throw new BusinessException("Cannot Fetch Product as [CatalogVersion] is NULL");
		}

		/* Fetch the Active Products From the DAO Layer */
		final Collection<String> activeProductCodeList = jnjLaProductDao.getActiveProductCodes(productCatalogId);

		/* Fetch Products Corresponding to Product Codes */
		final List<JnJProductModel> activeProducts = new ArrayList<>();
		for (final String activeProductCode : activeProductCodeList)
		{
			activeProducts.add(getProductForCodeAndCatalogId(catalogVersionModel, activeProductCode, productCatalogId));
		}

		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return activeProducts;
	}

	@Override
	public JnJProductModel getProductForCodeAndCatalogId(final CatalogVersionModel catalogVersion, final String productCode,
			final String catalogId) throws BusinessException
	{
		final String methodName = "getProductForCodeAndCatalogId()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final JnJProductModel jnJproduct = jnjLaProductDao.getProduct(catalogVersion, productCode, catalogId);

		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnJproduct;
	}



	@Override
	public JnJProductModel getProductForCatalogId(final CatalogVersionModel catalogVersion, final String catalogId)
			throws BusinessException
	{
		final String methodName = "getProductForCodeAndCatalogId()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final JnJProductModel jnJproduct = jnjLaProductDao.getProductbyCatalogId(catalogVersion, catalogId);

		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnJproduct;
	}


	@Override
	public JnJLaProductModel createModel()
	{
		return modelService.create(JnJLaProductModel.class);
	}

	@Override
	public CountryModel getCountryModelByIsoOrPk(final String storeIso)
	{
		return jnjLaProductDao.getCountryModelByIsoOrPk(storeIso);
	}

	/**
	 * Fetch product based on a given b2b's country. Issues may occur for countries which buys from more than one store
	 */
	@Override
	public JnJProductModel getProduct(final B2BUnitModel b2bUnitModel, final String productCode)
	{
		final String methodName = "getProduct(b2bUnitModel, productCode)";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final String catalogId;
		JnJProductModel jnJProductModel = null;
		if (null != b2bUnitModel)
		{
			final String customerCountryIso = JnjLaCommonUtil.getBaseStoreCountryMaster(b2bUnitModel.getCountry().getIsocode());
			catalogId = JnjLaCommonUtil.getIdByCountry(customerCountryIso) + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogId,
					Jnjb2bCoreConstants.ONLINE);
			try
			{
				if (StringUtils.isNotEmpty(productCode))
				{
					jnJProductModel = (JnJProductModel) getProductForCode(catalogVersionModel, productCode);
				}
				else
				{
					JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName,
							"Received NULL product code. Returning NULL product", JnjLaProductServiceImpl.class);
				}
			}
			catch (ModelNotFoundException | IllegalArgumentException | UnknownIdentifierException exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE, methodName,
						"JnJProductModel with code: " + productCode + " not found in Hybris. Returning NULL - ", exception,
						JnjLaProductServiceImpl.class);
				jnJProductModel = null;
			}
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.PRODUCT_SERVICE, methodName, "Received NULL for B2BUnitModel",
					JnjLaProductServiceImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnJProductModel;
	}


	/**
	 * Fetch product based on a given customer' salesOrg country.
	 */
	@Override
	public JnJProductModel getProduct(final String customerSalesOrgCountryIsoCode, final String productCode)
	{
		final String methodName = "getProduct(customerSalesOrgCountryIsoCode, productCode)";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final String catalogId;
		JnJProductModel jnJProductModel = null;
		if (null != customerSalesOrgCountryIsoCode)
		{
			final String customerCountryIso = JnjLaCommonUtil.getBaseStoreCountryMaster(customerSalesOrgCountryIsoCode);
			catalogId = JnjLaCommonUtil.getIdByCountry(customerCountryIso) + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogId,
					Jnjb2bCoreConstants.ONLINE);
			try
			{
				if (StringUtils.isNotEmpty(productCode))
				{
					jnJProductModel = (JnJProductModel) getProductForCode(catalogVersionModel, productCode);
				}
				else
				{
					JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName,
							"Received NULL product code. Returning NULL product", JnjLaProductServiceImpl.class);
				}
			}
			catch (ModelNotFoundException | IllegalArgumentException | UnknownIdentifierException exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE, methodName,
						"JnJProductModel with code: " + productCode + " not found in Hybris. Returning NULL - ", exception,
						JnjLaProductServiceImpl.class);
				jnJProductModel = null;
			}
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.PRODUCT_SERVICE, methodName, "Received NULL for B2BUnitModel",
					JnjLaProductServiceImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnJProductModel;
	}

	@Override
	public Map<String, Boolean> checkProductCodes(final List<String> catalogIdList)
	{
		final String methodName = "checkProductCodes()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);
		final Map<String, Boolean> productCheckMap = new HashMap<>();
		/* Creating Active Product List from the catalogID List */
		for (String catalogId : catalogIdList)
		{
			catalogId = catalogId.toUpperCase();
			try
			{
				final JnJProductModel jnJProductModel = getProductForCatalogId(catalogId);
				if (null != jnJProductModel)
				{
					productCheckMap.put(catalogId, Boolean.TRUE);
				}
				else
				{
					productCheckMap.put(catalogId, Boolean.FALSE);
					JnjGTCoreUtil.logWarnMessage(Logging.PRODUCT_SERVICE, methodName,
							"Cannot find Active Product for CatalogId [" + catalogId + "] .", JnjLaProductServiceImpl.class);
				}
			}
			catch (final BusinessException businessException)
			{
				productCheckMap.put(catalogId, Boolean.FALSE);
				JnjGTCoreUtil.logWarnMessage(Logging.PRODUCT_SERVICE, methodName,
						"Cannot find Active Product for CatalogId [" + catalogId + "] ." + businessException,
						JnjLaProductServiceImpl.class);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return productCheckMap;
	}

	@Override
	public JnJProductModel getActiveProductWithRestrictionCheck(final String productCode)
	{
		final String methodName = "getActiveProductWithRestricationCheck()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		JnJProductModel activeProductModel = null;
		try
		{
			/* Check if productCode is null */
			if (StringUtils.isEmpty(productCode))
			{
				LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName
						+ "Cannot Fetch Active Product as no [productCode] given. Cascading to Business Exception. ");
				throw new BusinessException("Cannot Fetch Active Product as no [productCode] given.");
			}
			final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
			final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
			final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
					Jnjb2bCoreConstants.ONLINE);
			/* Fetch the Product From the DAO Layer with the Material Entered */
			final JnJProductModel jnJProductModel = (JnJProductModel) getProductForCode(catalogVersionModel, productCode);
			if (null == jnJProductModel)
			{
				LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + "Cannot Product for Code[" + productCode
						+ "]. Cascading to Business Exception. ");
				throw new BusinessException("Cannot Product for Code [" + productCode + "].");

			}

			/* Getting the Active Product in the CatalogId Pool */
			if (jnJProductModel.getEcommerceFlag() == null || !jnJProductModel.getEcommerceFlag().booleanValue())
			{
				LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + "Current product with Product Code[" + productCode
						+ "] not active. Fetching other active Product in Catalog Id pool. ");
				activeProductModel = getProductForCatalogId(jnJProductModel.getCatalogId());
			}
			else
			{
				LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + "Current product with Product Code[" + productCode
						+ "] is active in the Catalog Id pool. ");
				activeProductModel = jnJProductModel;
			}


			if (isProductAllignedToRestrictedCategory(activeProductModel))
			{
				LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + "Current Product with code[" + productCode
						+ "] is active in the Catalog Id pool but is alined with Restricted Category. ");
				throw new BusinessException("Current Product with code[" + productCode
						+ "] is active in the Catalog Id pool but is alined with Restricted Category. ");
			}
		}
		catch (final BusinessException businessException)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + "BusinessException occured." + businessException);

		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return activeProductModel;
	}

	@Override
	public boolean isProductAllignedToRestrictedCategory(final ProductModel product)
	{
		final Collection<CategoryModel> productSuperCategories = product.getSupercategories();
		final Set<CategoryModel> categories = new HashSet<>();
		Collection<CategoryModel> superCategories;

		for (final CategoryModel category : productSuperCategories)
		{
			categories.add(category);
			superCategories = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Collection<CategoryModel> execute()
				{
					return category.getAllSupercategories();
				}
			}, userService.getAdminUser());

			if (superCategories != null && !superCategories.isEmpty())
			{
				categories.addAll(superCategories);
			}
		}
		final B2BCustomerModel currentCustomer = (B2BCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel unit = (JnJB2BUnitModel) currentCustomer.getDefaultB2BUnit();
		final Set<String> restrictedCategoryCodes = customerEligibilityService.getRestrictedCategory(unit.getUid());
		restrictedCategoryCodes.add(DEFAULT_CATEGORY_CODE);

		if (categories.isEmpty())
		{
			return true;
		}
		else
		{

			for (final CategoryModel category : categories)
			{
				if (restrictedCategoryCodes.contains(category.getCode()))
				{
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public ProductModel getProductForCode(final String productCode, final String countryCode)
	{
		ProductModel product = null;
		final String methodName = "getProductForCode";

		List<String> countryList = null;
		final String list = Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES);
		if (list == null)
		{
			JnjGTCoreUtil.logWarnMessage("Get Product", methodName,
					"Properties: " + Jnjb2bCoreConstants.KEY_Valid_COUNTRIES + " is null.", JnjLaProductServiceImpl.class);
		}
		else
		{
			countryList = Arrays.asList(list.split(","));
		}

		if (null != countryList && countryList.contains(countryCode.toUpperCase()))
		{
			final String catalogId = countryCode.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			final CatalogModel catalogModel = catalogService.getCatalogForId(catalogId);
			final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogModel.getId(),
					Jnjb2bCoreConstants.ONLINE);

			product = getProductForCode(catalogVersion, productCode);
		}
		return product;
	}

	@Override
	public CatalogVersionModel getCurrentProductCatalogVersion() throws BusinessException
	{
		final String methodName = "getCurrentProductCatalogVersion()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		/* Fetching the B2BUnit for the Logged in User */
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		if (null == jnjB2BUnitModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName
					+ "Cannot Fetch Product as no [b2bUnit] found for logged in user.Cascading to Business Exception;");
			throw new BusinessException("Cannot Fetch Product as no [b2bUnit] found for logged in user.");
		}

		/* Creating the catalogUid with Country ISO fetched from B2BUnit */
		final String countryIso = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();
		final String catalogUid = JnjLaCommonUtil.getIdByCountry(countryIso) + Jnjb2bCoreConstants.PRODUCT_CATALOG;


		/* Fetching the Online CatalogVersionModel based on CatalogUid */
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogUid,
				Jnjb2bCoreConstants.ONLINE);

		if (null == catalogVersionModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName
					+ "Cannot Fetch Product as no current [CatalogVersion] found for logged in user.Cascading to Business Exception;");
			throw new BusinessException("Cannot Fetch Product as no current [CatalogVersion] found for logged in user.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return catalogVersionModel;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ProductModel getProductForCode(final String code)
	{
		ProductModel product = null;
		validateParameterNotNull(code, "Parameter code must not be null");
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);
		final List<ProductModel> products = productDao.findProductsByCode(catalogVersionModel, code);

		if (null != products)
		{
			validateIfSingleResult(products, format("Product with code '%s' not found!", code),
					format("Product code '%s' is not unique, %d products found!", code, Integer.valueOf(products.size())));
			if (!products.isEmpty())
			{
				product = products.get(0);
			}
		}
		return product;
	}

	@Override
	public JnJProductModel getProductbyCodeOrEAN(final String code)
	{
		JnJProductModel product = null;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);
		try
		{
			product = (JnJProductModel) getProductForCode(catalogVersionModel, code.trim().toUpperCase());
		}
		catch (final UnknownIdentifierException itemNotFoundExp)
		{
			JnjGTCoreUtil.logErrorMessage("Get Product", "getProductbyCodeOrEAN()", "Exception", itemNotFoundExp,
					JnjLaProductServiceImpl.class);
			LOGGER.info("Product Code-" + code + "not find while adding to cart, Search By EAN");
			final List<JnJProductModel> productList = getProductModelByEanProductNumber(code);
			if (null != productList && !productList.isEmpty())
			{
				product = getProductModelByEanProductNumber(code).get(0);
			}
		}
		return product;
	}

	@Override
	public JnJProductModel getProductByCatalogIdForEdi(final String catalogId, final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "getProductByCatalogIdWithoutCatalog()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final CatalogVersionModel catalogVersion = getCatalogId(jnJB2BUnitModel);

		List<JnJProductModel> jnjProductModelList = null;
		if (catalogId != null && StringUtils.isNotEmpty(catalogId))
		{
			final JnJProductModel jnjProductModel = new JnJProductModel();
			jnjProductModel.setCatalogId(catalogId);
			jnjProductModel.setCatalogVersion(catalogVersion);
			jnjProductModel.setEcommerceFlag(Boolean.TRUE);
			jnjProductModelList = flexibleSearchService.getModelsByExample(jnjProductModel);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);

		return CollectionUtils.isNotEmpty(jnjProductModelList) ? jnjProductModelList.get(0) : null;
	}

	@Override
	public JnJProductModel getProductByCatalogIdForAnyEcommerceFlag(final String catalogId, final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "getProductByCatalogIdForAnyEcommerceFlag()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);

		final CatalogVersionModel catalogVersion = getCatalogId(jnJB2BUnitModel);

		List<JnJProductModel> jnjProductModelList = null;
		if (catalogId != null && StringUtils.isNotEmpty(catalogId))
		{
			final JnJProductModel jnjProductModel = new JnJProductModel();
			jnjProductModel.setCatalogId(catalogId);
			jnjProductModel.setCatalogVersion(catalogVersion);
			jnjProductModelList = flexibleSearchService.getModelsByExample(jnjProductModel);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);

		return CollectionUtils.isNotEmpty(jnjProductModelList) ? jnjProductModelList.get(0) : null;
	}

	@Override
	public List<JnJProductModel> getProductModelByEanProductNumber(final String eanProductNumber,
			final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "getProductModelByEanProductNumber()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);

		final CatalogVersionModel catalogVersion = getCatalogId(jnJB2BUnitModel);

		List<JnJProductModel> jnjProductModelList = null;
		if (StringUtils.isNotEmpty(eanProductNumber))
		{
			final JnJProductModel jnjProductModel = new JnJProductModel();
			jnjProductModel.setEan(eanProductNumber);
			jnjProductModel.setCatalogVersion(catalogVersion);
			jnjProductModel.setEcommerceFlag(Boolean.TRUE);
			// Get the list of Jnj Product Model object by calling the getModelsByExample method of Flexible Search Service.
			jnjProductModelList = flexibleSearchService.getModelsByExample(jnjProductModel);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnjProductModelList;
	}

	@Override
	public JnJProductModel getProductByCatalogIdWithoutSearchRestriction(final String catalogId)
	{
		final String methodName = "getProductByCatalogIdWithoutSearchRestriction()";
		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);

		final CatalogVersionModel catalogVersion = catalogVersionService
				.getCatalogVersion(cmsSiteService.getCurrentSite().getDefaultCatalog().getId(), Jnjb2bCoreConstants.ONLINE);
		JnJProductModel jnjProductModel = null;

		if (StringUtils.isNotEmpty(catalogId) && null != catalogVersion)
		{
			try
			{
				// Get the list of Jnj Product Model object by calling the getModelsByExample method of Flexible Search Service.
				jnjProductModel = jnjLaProductDao.getProductByCatalogIdWithoutSearchRestriction(catalogId);
			}
			catch (final BusinessException ex)
			{
				jnjProductModel = null;
				JnjGTCoreUtil.logWarnMessage(Logging.CONTRACTS_SERVICE, methodName, "Model not found for catalogId: " + catalogId,
						JnjLaProductServiceImpl.class);
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return jnjProductModel;
	}

	@SuppressWarnings("deprecation")
	private CatalogVersionModel getCatalogId(final JnJB2BUnitModel jnJB2BUnitModel)
	{
		if (jnJB2BUnitModel == null)
		{
			return null;
		}

		if (jnJB2BUnitModel.getCountry() != null && jnJB2BUnitModel.getCountry().getIsocode() != null)
		{
			final String countryIsoCode = jnJB2BUnitModel.getCountry().getIsocode();
			final String productCatalogName = countryIsoCode.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			return catalogService.getCatalogVersion(productCatalogName, Jnjb2bCoreConstants.ONLINE);
		}

		return null;
	}

	@Override
	public String getProductSector(final String productCode)
	{
		@SuppressWarnings("deprecation")
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(
				Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_MASTER_CATALOG_ID, Jnjb2bCoreConstants.STAGED);

		final ProductModel productModel = getProductForCode(catalogVersionModel, productCode);
		if (productModel instanceof JnJProductModel)
		{
			return ((JnJProductModel) productModel).getSector();
		}
		return null;
	}

	@Override
	public boolean removeAllProductSalesOrgForProduct(final String productCode)
	{
		return jnjLaProductDao.removeAllProductSalesOrgForProduct(productCode);
	}

	@Override
	public List<JnJProductModel> getProductsForCatalogVersionId(final CatalogVersionModel catalogVersion, final List<String> productCodes) {
		final String methodName = "getProductsForCatalogVersionId()";
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjLaProductServiceImpl.class);
		final List<JnJProductModel> productModelList = jnjLaProductDao.getProductsForCatalogVersion(catalogVersion, productCodes);
		JnjGTCoreUtil.logDebugMessage(Logging.PRODUCT_SERVICE, methodName, Logging.END_OF_METHOD, JnjLaProductServiceImpl.class);
		return productModelList;
	}

	@Override
	public StringBuilder getObsoleteProductList(final String[] selectedProductIds) {
        StringBuilder strProductList = new StringBuilder();

        try {
            final CatalogModel defaultCatalog = cmsSiteService.getCurrentSite().getDefaultCatalog();
            if (defaultCatalog != null) {
                final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(defaultCatalog.getId(), Jnjb2bCoreConstants.ONLINE);
                for (final String productId : selectedProductIds) {
                    if(isObsoleteProduct(productId, catalogVersion)){
                        strProductList.append(productId).append(",");
                    }
                }
            } else {
                JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE,"getObsoleteProductList()", "No default catalog for current site", JnjLaProductServiceImpl.class);
            }
        } catch (UnknownIdentifierException e) {
            JnjGTCoreUtil.logErrorMessage(Logging.PRODUCT_SERVICE,"getObsoleteProductList()", "Unable to find catalogVersionModel", e, JnjLaProductServiceImpl.class);
        }

        return strProductList;
	}

    @Override
    public String getProductName(final JnJProductModel product) {
	    if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.CONS, sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME))
            && StringUtils.isNotEmpty(product.getMdmDescription())) {
            return product.getMdmDescription();
        } else {
            return product.getName();
        }
    }

	@Override
	public List<JnJProductModel> getProductsBoughtTogether(final String productCode, final CatalogVersionModel catalogVersion) {

		final int carouselQuantity = jnjCommonUtil.getInt(PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY, PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY_DEFAULT);

		final Interval<Calendar> dates = getDaysIntervalOfProductsBoughtTogether();
		final List<String> allowedFranchisesCode = getAllowedFranchiseCodeList();

		final String productPK = findProductPK(productCode, catalogVersion);
		if (StringUtils.isEmpty(productPK)) {
			return new ArrayList<>();
		}

		return jnjGTProductDao.getProductsBoughtTogether(allowedFranchisesCode, productPK, dates, carouselQuantity);
	}

	private Interval<Calendar> getDaysIntervalOfProductsBoughtTogether() {
		final Date currentDate = new Date();
		final LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		final int daysOfHistory = jnjCommonUtil.getInt(Jnjlab2bcoreConstants.DAYS_OF_HISTORY_PRODUCTS_BOUGHT_TOGETHER,Jnjlab2bcoreConstants.DAYS_OF_HISTORY_PRODUCTS_BOUGHT_TOGETHER_DEFAULT);
		final LocalDateTime startLocalDateTime = localDateTime.minusDays(daysOfHistory);
		final Calendar startDateCalendar = DateUtils.toCalendar(Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
		final LocalDateTime endLocalDateTime = localDateTime.plusDays(1);
		final Calendar endDateCalendar = DateUtils.toCalendar(Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));

		return new Interval<>(startDateCalendar, endDateCalendar);
	}
}
