/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao;

import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public interface JnjProductDao extends ProductDao
{
	List<ProductModel> getAllProducts(final CatalogVersionModel catalogVersion);

	/**
	 * Get the discontinued product from catalog version for given countryIso
	 * 
	 * @param catalogVersion
	 *           CatalogVersionModel
	 * @param countryIso
	 *           String
	 * @return List<ProductModel>
	 */
	List<ProductModel> getDiscountinedProducts(final CatalogVersionModel catalogVersion, final String countryIso);
	
	/**
	 * Gets the Active Products From Current Product Catalog based and Catalog ID pool.
	 * 
	 * @param catalogId
	 *           the catalog id
	 * @param catalogVersion
	 *           the catalog version
	 * @return the product
	 * @throws BusinessException
	 *            the business exception
	 */
	List<JnJProductModel> getActiveProducts(String catalogId, CatalogVersionModel catalogVersion) throws BusinessException;


}
