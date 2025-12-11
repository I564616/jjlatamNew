/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.ProductService;



/**
 * This class is created for adding new api for products or override existing one.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJProductService extends ProductService
{

	/**
	 * Used to get discontinued products for given catalog and countryIso.
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @param countryIso
	 *           the country iso
	 * @return List of discontinued Products
	 */
	List<ProductModel> getDiscountinedProducts(final CatalogVersionModel catalogVersion, final String countryIso);

	/**
	 * Used to mark Product off-line in case of not valid for given catalog and countryIso.
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @param countryIso
	 *           the country iso
	 * @return true if success
	 */
	boolean updateInvalidProduct(final CatalogVersionModel catalogVersion, final String countryIso);

	/**
	 * Save product.
	 * 
	 * @param itemModel
	 *           the ItemModel
	 * @return true, if successful
	 */
	public boolean saveProduct(ItemModel itemModel);

	/**
	 * Creates the model.
	 * 
	 * @return the jn j product model
	 */
	public JnJProductModel createModel();

	/**
	 * Creates the category model.
	 * 
	 * @return the category model
	 */
	public CategoryModel createCategoryModel();

	/**
	 * Creates the model for unit.
	 * 
	 * @return the unit model
	 */
	public UnitModel createModelForUnit();

	/**
	 * Creates the model for catalog.
	 * 
	 * @return the catalog model
	 */
	public CatalogModel createModelForCatalog();

	/**
	 * Gets the jn j catalog for id.
	 * 
	 * @return the jn j catalog for id
	 */
	public CatalogModel getJnJCatalogForId();


	/**
	 * Gets the product.
	 * 
	 * @param b2bUnitModel
	 *           the b2b unit model
	 * @param productCode
	 *           the product code
	 * @return the product
	 */
	public JnJProductModel getProduct(B2BUnitModel b2bUnitModel, String productCode);


	/**
	 * The getProductModelByEanProductNumber method gets the Jnj Product Model by using ean product number.
	 * 
	 * @param eanProductNumber
	 *           the ean product number
	 * @return the product by customer number
	 */
	public List<JnJProductModel> getProductModelByEanProductNumber(final String eanProductNumber);

	/**
	 * This method try to find the code with code, if not exist then search for EAN number
	 * 
	 * @param code
	 *           the product code or EAN number
	 * @return the JnJProductModel
	 */
	public JnJProductModel getProductCodeOrEAN(final String code);
	
	/**
	 * Gets the active product in the Catalog ID pool for the given product code. This method does not check whether the
	 * product is aligned to restricted category or not.
	 * 
	 * @param productCode
	 *           the product code
	 * @return the active product
	 * @throws BusinessException
	 *            the business exception
	 */
	JnJProductModel getActiveProduct(String productCode) throws BusinessException;
	
	/**
	 * Gets the Active Product From Current Product Catalog based and Catalog ID pool. This method takes into account the
	 * Active Product Restriction and Fetches Only the Active Product if present.
	 *
	 * @param productCatalogId
	 *           the product catalog id
	 * @return the product for catalog id
	 * @throws BusinessException
	 *            the business exception
	 */
	JnJProductModel getProductForCatalogId(String productCatalogId) throws BusinessException;
	
	/**
	 * Gets the current product catalog version only for the Logged in user.
	 *
	 * @return the current product catalog version
	 * @throws BusinessException
	 *            the business exception
	 */
	CatalogVersionModel getCurrentProductCatalogVersion() throws BusinessException;
	
	/**
	 * This method fetches the first active product in the current Product CatalogID pool.
	 *
	 * @param jnJProductModel
	 *           the jn j product model
	 * @return the active product
	 * @throws BusinessException
	 *            the business exception
	 */
	JnJProductModel getActiveProduct(JnJProductModel jnJProductModel) throws BusinessException;


}
