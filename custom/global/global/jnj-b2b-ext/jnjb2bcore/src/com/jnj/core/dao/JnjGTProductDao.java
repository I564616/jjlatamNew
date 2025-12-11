/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 *
 */
package com.jnj.core.dao;

import com.jnj.core.util.Interval;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.jnj.core.dao.JnjProductDao;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * TODO:<class level comments are missing>.
 *
 * @author Accenture
 * @version 1.0
 *
 */

public interface JnjGTProductDao extends JnjProductDao
{

	/**
	 * Get the product variant for Product code, GTIN or UPC
	 *
	 * @param code
	 *           String
	 * @return JnjGTVariantProductModel
	 */
	public List<JnjGTVariantProductModel> getProductByValue(final String code, final boolean ignoreUpc);

	/**
	 * Gets the jnj na product model by code by querying the hybris database with admin.
	 *
	 * @param code
	 *           the code
	 * @param catalogVersionModel
	 * @return the product model by code
	 */
	public JnJProductModel getProductModelByCode(final String code, CatalogVersionModel catalogVersionModel);

	/**
	 * Get the product variant for values of Product code, GTIN or UPC without leading 0s
	 *
	 * @param code
	 *           String
	 * @return JnjGTVariantProductModel
	 */
	public ProductModel getProductByPartialValue(final String code, final boolean ignoreUpc, final String site, final boolean admin);

	/**
	 * Gets the jnj na product model by upcCode by querying the hybris database with admin.
	 *
	 * @param code
	 *           the code
	 * @param catalogVersionModel
	 * @return the product model by code
	 */
	public List<JnJProductModel> getProductModelByUpcCode(final String upcCode, CatalogVersionModel catalogVersionModel);

	/**
	 * Finds the Mod product available corresponding to the base Product.
	 *
	 * @param baseProductCode
	 * @return JnJProductModel
	 */
	public JnJProductModel getModProductByBase(final String baseProductCode, final String baseProductPk);

	/**
	 * @param code
	 * @param sourceId
	 * @return
	 */
	public JnJProductModel getProductForOrderTemplate(String code, String sourceId);

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 *
	 * @param sortBy
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	public Collection<JnjGTProductCpscDetailModel> getConsumerProductsCpsia(final String sortBy);

	/**
	 * Retrieves all variants of a product ordered by their packaging level code and having Ship/Sales Unit indicator
	 * set.
	 *
	 * @param productPk
	 * @return Collection<JnjGTVariantProductModel>
	 */
	public Collection<JnjGTVariantProductModel> getVariantsOrderedByPkgLvlCode(final String productPk);

	/**
	 * Retrieves all new products which have their first ship effective date or new product start date lying between 7
	 * days thirty days from the current date.
	 *
	 * @param catalogVersionModel
	 * @param startDate
	 * @param endDate
	 * @return List<JnJProductModel>
	 */
	public List<JnJProductModel> getNewActiveProducts(CatalogVersionModel catalogVersionModel, final String endDate,
			final String startDate);

	/**
	 * @param code
	 * @param catalogVersionModel
	 * @param partial
	 * @return
	 */
	public JnJProductModel getProductModelByCode(String code, CatalogVersionModel catalogVersionModel, boolean partial);

	/**
	 * This method is used to get all the products for a catalog to be downloaded from EPIC category page.
	 *
	 * @param currentSite
	 * @param accountNo
	 * @return
	 */
	public List<JnJProductModel> getAllProductsForSite(String currentSite, String accountNo);

	/**
	 * This method is used to get all the products for a catalog to be downloaded from EPIC category page.
	 *
	 * @param catalog
	 * @return
	 */
	public List<JnJProductModel> getAllProductsForPCM(CatalogModel catalog);

		
	public List<JnJProductModel> getMDDProductByMODValue(String code, final boolean admin);

	List<JnJProductModel> getProductsBoughtTogether(final List<String> allowedFranchisesCode, final String productCode, final Interval<Calendar> dates, final int quantity);
}
