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
package com.jnj.la.core.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;

import java.util.Collection;
import java.util.List;

import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.model.JnjCommercialUomConversionModel;



/**
 *
 */
public interface JnjLaProductDao extends JnjGTProductDao
{
	List<JnjCommercialUomConversionModel> getCommercialUom(String isoCode);

	/**
	 *
	 */
	JnJProductSalesOrgModel getJnjProductSalesOrgModel(String code, String salesOrgCode, boolean activeFlag);

	/**
	 * @throws BusinessException
	 *
	 */
	Collection<String> getActiveProductCodes(String productCatalogId) throws BusinessException;

	/**
	 *
	 */
	List<JnJProductModel> getActiveProducts(String catalogId, CatalogVersionModel catalogVersion) throws BusinessException;

	/**
	 * @throws BusinessException
	 *
	 */
	JnJProductModel getProduct(CatalogVersionModel catalogVersion, String productCode, String catalogId) throws BusinessException;


	public JnJProductModel getProductbyCatalogId(final CatalogVersionModel catalogVersion, final String catalogId)
			throws BusinessException;

	JnJProductModel getProductByCatalogIdWithoutSearchRestriction(final String catalogId) throws BusinessException;

	/**
	 *
	 */
	CountryModel getCountryModelByIsoOrPk(String country);

	List<JnJProductModel> getMDDProductByMODValue(String code, final boolean admin);

	boolean removeAllProductSalesOrgForProduct(String productCode);

	/**
	 * This method returns list of product model for the catalog and catalog version which are modified after the fromDate.
	 * @param fromDate The From Date
	 * @param catalogID The Catalog ID
	 * @param catalogVersion The Catalog Version
	 *  @param sector The product sector
	 * @return List<JnJProductModel> The List of Products
	 */
	public List<JnJProductModel> getProductsForSector(final String fromDate, final String catalogID, final String catalogVersion, final String sector);

    List<JnJProductModel> getProductsForCatalogVersion(final CatalogVersionModel catalogVersion, final List<String> productCodes);
}
