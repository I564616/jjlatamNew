/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjProductDao;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;



/**
 * TODO:<Neeraj-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJProductService extends DefaultProductService implements JnJProductService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnJProductService.class);
	protected static final String DEFAULT_CATEGORY_CODE = "DefaultCategory";

	@Autowired
	ModelService modelService;

	@Autowired
	CatalogService catalogService;

	@Autowired
	JnjProductDao jnjProductDao;

	@Autowired
	private CatalogVersionService catalogVersionService;


	@Autowired
	FlexibleSearchService flexibleSearchService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	UserService userService;

	@Autowired
	private JnjCustomerEligibilityService customerEligibilityService;
	
	/** The jnj get current default b2 b unit util. */
	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	public ModelService getModelService() {
		return modelService;
	}


	public CatalogService getCatalogService() {
		return catalogService;
	}


	public JnjProductDao getJnjProductDao() {
		return jnjProductDao;
	}


	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}


	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}


	public SessionService getSessionService() {
		return sessionService;
	}


	public UserService getUserService() {
		return userService;
	}


	public JnjCustomerEligibilityService getCustomerEligibilityService() {
		return customerEligibilityService;
	}


	@Override
	public List<ProductModel> getDiscountinedProducts(final CatalogVersionModel catalogVersion, final String countryIso)
	{
		return jnjProductDao.getDiscountinedProducts(catalogVersion, countryIso);
	}


	@Override
	public boolean updateInvalidProduct(final CatalogVersionModel catalogVersion, final String countryIso)
	{
		Boolean updated = Boolean.FALSE;
		final List<ProductModel> products = getDiscountinedProducts(catalogVersion, countryIso);
		for (final ProductModel productModel : products)
		{
			final Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -2);
			final Date date = c.getTime();
			productModel.setOfflineDate(date);
			updated = saveProduct(productModel);
		}
		return updated.booleanValue();
	}

	/*
	 * This method is used to create Product Model
	 */

	protected Boolean saveProduct(final ProductModel productModel)
	{
		final String METHOD_NAME = "saveProduct";
		Boolean saved = Boolean.FALSE;
		try
		{
			modelService.save(productModel);
			saved = Boolean.TRUE;
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error(Logging.UPSERT_PRODUCT_NAME + "-" + METHOD_NAME + "product model not saved into Hybris data base" + "-"
					+ modelSavingException.getLocalizedMessage());
		}
		return saved;
	}

	/*
	 * This method is used to save JnjProduct Model
	 */
	@Override
	public boolean saveProduct(final ItemModel itemModel) throws ModelSavingException
	{

		final String METHOD_NAME = "saveProduct ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean success = false;
		try
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + "BEFORE SAVE CALL"
						+ JnJCommonUtil.getCurrentDateTime());
			}

			modelService.saveAll(itemModel);
			success = true;
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + "AFTER SUCCESSFULLY SAVE THE JNJ PRODUCT MODEL"
						+ JnJCommonUtil.getCurrentDateTime());
			}
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error(Logging.UPSERT_PRODUCT_NAME + "-" + METHOD_NAME + "model not saved into Hybris data base" + "-"
					+ modelSavingException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
			success = false;
			throw modelSavingException;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return success;

	}

	/*
	 * This method is used to create JnjProductModel
	 */
	@Override
	public JnJProductModel createModel()
	{
		final JnJProductModel jnjProductModel = modelService.create(JnJProductModel.class);
		return jnjProductModel;
	}


	/*
	 * This method is used to create a catalog model
	 */
	@Override
	public CatalogModel createModelForCatalog()
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		return catalogModel;
	}

	/*
	 * This mehtod is used to get a catalog model for given ID
	 */
	@Override
	public CatalogModel getJnJCatalogForId()
	{
		final CatalogModel catalogModel = catalogService.getCatalogForId(Jnjb2bCoreConstants.UpsertProduct.CATALOG_MASTER);
		return catalogModel;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}



	/*
	 * this method is used to create a category model
	 */
	@Override
	public CategoryModel createCategoryModel()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		return categoryModel;
	}

	/*
	 * This method is used to create a model for unit id
	 */
	@Override
	public UnitModel createModelForUnit()
	{
		final UnitModel unitModel = modelService.create(UnitModel.class);
		return unitModel;
	}


	/**
	 * Get Product Method gets the JnjProduct with respective to a particular catalog version.
	 */
	@Override
	public JnJProductModel getProduct(final B2BUnitModel b2bUnitModel, final String productCode)
	{
		final String METHOD_NAME = "getProduct()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final String catalogId;
		JnJProductModel jnJProductModel = null;
		if (null != b2bUnitModel)
		{
			final String cuntryISO = b2bUnitModel.getCountry().getIsocode();
			catalogId = cuntryISO.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogId,
					Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE);
			if (StringUtils.isNotEmpty(productCode))
			{
				jnJProductModel = (JnJProductModel) getProductForCode(catalogVersionModel, productCode);
			}
			else
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME
							+ "Recieved NULL prodcut Code. Returnig NUll Product" + JnJCommonUtil.getCurrentDateTime());
				}
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.UPSERT_PRODUCT_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
						+ JnJCommonUtil.getCurrentDateTime());
			}
		}
		return jnJProductModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelByEanProductNumber(final String eanProductNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelByEanProductNumber()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		JnJProductModel jnJProductModel = null;
		List<JnJProductModel> jnjProductModelList = null;
		if (StringUtils.isNotEmpty(eanProductNumber))
		{
			jnJProductModel = new JnJProductModel();
			jnJProductModel.setEan(eanProductNumber);
			// Get the list of Jnj Product Model object by calling the getModelsByExample method of Flexible Search Service.
			jnjProductModelList = flexibleSearchService.getModelsByExample(jnJProductModel);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelByEanProductNumber()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjProductModelList;
	}

	@Override
	public JnJProductModel getProductCodeOrEAN(final String code)
	{
		JnJProductModel product = null;
		try
		{
			product = (JnJProductModel) getProductForCode(code.trim().toUpperCase());
		}
		catch (final UnknownIdentifierException itemNotFoundExp)
		{
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
	public JnJProductModel getProductForCatalogId(final String productCatalogId) throws BusinessException
	{
		final String METHOD_NAME = "getProductForCatalogId()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		/* Check if productCatalogId is null */
		if (StringUtils.isEmpty(productCatalogId))
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME
					+ "Cannot Fetch Product as no [productCatalogId] given. Cascading to Business Exception. ");
			throw new BusinessException("Cannot Fetch Product as no [productCatalogId] given.");
		}

		/* Fetch the Current Product Catalog */
		final CatalogVersionModel catalogVersionModel = getCurrentProductCatalogVersion();

		/* Fetch the Product From the DAO Layer */
		final List<JnJProductModel> jnJProductModelList = jnjProductDao.getActiveProducts(productCatalogId, catalogVersionModel);

		JnJProductModel jnJProductModel = null;
		if (CollectionUtils.isNotEmpty(jnJProductModelList))
		{
			jnJProductModel = jnjProductDao.getActiveProducts(productCatalogId, catalogVersionModel).get(0);
		}
		else
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Cannot Fetch Product for CatalogID ["
					+ productCatalogId + "]. Cascading to Business Exception. ");
			throw new BusinessException("Cannot Fetch Product for CatalogID [" + productCatalogId + "].");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnJProductModel;
	}
	
	@Override
	public JnJProductModel getActiveProduct(final String productCode) throws BusinessException
	{
		final String METHOD_NAME = "getActiveProduct()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		/* Check if productCatalogId is null */
		if (StringUtils.isEmpty(productCode))
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME
					+ "Cannot Fetch Active Product as no [productCode] given. Cascading to Business Exception. ");
			throw new BusinessException("Cannot Fetch Active Product as no [productCode] given.");
		}

		/* Fetch the Product From the DAO Layer with the Material Entered */
		final JnJProductModel jnJProductModel = (JnJProductModel) getProductForCode(productCode);
		if (null == jnJProductModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Cannot Product for Code[" + productCode
					+ "]. Cascading to Business Exception. ");
			throw new BusinessException("Cannot Product for Code[" + productCode + "]");
		}

		JnJProductModel activeProductModel = null;

		/* Getting the Active Product in the CatalogId Pool */
		if (jnJProductModel.getEcommerceFlag() == null || !jnJProductModel.getEcommerceFlag().booleanValue())
		{
			LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Current product with Product Code[" + productCode
					+ "] not active. Fetching other active Product in Catalog Id pool. ");
			activeProductModel = getProductForCatalogId(jnJProductModel.getCatalogId());
		}
		else
		{
			LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Current product with Product Code[" + productCode
					+ "] is active in the Catalog Id pool. ");
			activeProductModel = jnJProductModel;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return activeProductModel;
	}
	
	@Override
	public CatalogVersionModel getCurrentProductCatalogVersion() throws BusinessException
	{
		final String METHOD_NAME = "getCurrentProductCatalogVersion()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		/* Fetching the B2BUnit for the Logged in User */
		final JnJB2BUnitModel jnJB2BUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		if (null == jnJB2BUnitModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME
					+ "Cannot Fetch Product as no [B2bUnit] found for logged in user.Cascading to Business Exception;");
			throw new BusinessException("Cannot Fetch Product as no [B2bUnit] found for logged in user.");
		}

		String cuntryIso = null;
		final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();

		if (Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA.equalsIgnoreCase(loggedInSite))
		{
			cuntryIso = loggedInSite;
		}
		else
		{
			cuntryIso = jnJB2BUnitModel.getCountry().getIsocode();
		}

		/* Creating the catalogUid with Country ISO fetched from B2BUnit */
		final String catalogUid = cuntryIso.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;

		/* Fetching the Online CatalogVersionModel based on CatalogUid */
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogUid,
				Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE);

		if (null == catalogVersionModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME
					+ "Cannot Fetch Product as no cuurent [CatalogVersion] found for logged in user.Cascading to Business Exception;");
			throw new BusinessException("Cannot Fetch Product as no current [CatalogVersion] found for logged in user.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return catalogVersionModel;
	}


	@Override
	public JnJProductModel getActiveProduct(final JnJProductModel jnJProductModel) throws BusinessException
	{
		final String METHOD_NAME = "getActiveProduct()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		/* Check if productCatalogId is null */
		if (null == jnJProductModel)
		{
			LOGGER.warn(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME
					+ "Cannot Fetch Active Product as Product given is null. Cascading to Business Exception. ");
			throw new BusinessException("Cannot Fetch Active Product as Product given is null.");
		}

		JnJProductModel activeProductModel = null;
		/* Getting the Active Product in the CatalogId Pool */
		if (!jnJProductModel.getEcommerceFlag().booleanValue())
		{
			LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Current product with Product Code["
					+ jnJProductModel.getCode() + "] not active. Fetching other active Product in Catalog Id pool. ");
			activeProductModel = getProductForCatalogId(jnJProductModel.getCatalogId());
		}
		else
		{
			LOGGER.info(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + "Current product with Product Code["
					+ jnJProductModel.getCode() + "] is active in the Catalog Id pool. ");
			activeProductModel = jnJProductModel;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return activeProductModel;
	}
	
	
}
