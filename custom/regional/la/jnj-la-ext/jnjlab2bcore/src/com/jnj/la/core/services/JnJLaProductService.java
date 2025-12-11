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
package com.jnj.la.core.services;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.model.JnjCommercialUomConversionModel;

public interface JnJLaProductService extends JnJGTProductService
{
	List<JnjCommercialUomConversionModel> getCommercialUom(String isoCode);

	boolean saveUomConversionModel(JnjUomConversionModel jnjUomConversionModel);

	JnJProductSalesOrgModel getJnjProductSalesOrgModel(String code, String salesOrganization, boolean b);

	boolean saveProductSalesOrgModel(JnJProductSalesOrgModel jnJProductSalesOrgModel);

	List<JnJProductModel> getAllActiveProducts(JnJLaProductModel product, CatalogVersionModel catalogVersionModel)
			throws BusinessException;

	JnJProductModel getProductForCodeAndCatalogId(CatalogVersionModel catalogVersion, String productCode, String catalogId)
			throws BusinessException;

	CountryModel getCountryModelByIsoOrPk(String storeIso);

	@Override
	public JnJProductModel getProduct(B2BUnitModel b2bUnitModel, String productCode);

	JnJProductModel getProduct(final String customerSalesOrgCountryIsoCode, String productCode);

	Map<String, Boolean> checkProductCodes(List<String> catalogIdList);


	public JnJProductModel getActiveProductWithRestrictionCheck(String productCode);

	public boolean isProductAllignedToRestrictedCategory(final ProductModel product);

	public ProductModel getProductForCode(final String productCode, final String countryCode);

	public JnJProductModel getProductbyCodeOrEAN(final String code);

	public JnJProductModel getProductForCatalogId(final CatalogVersionModel catalogVersion, final String catalogId)
			throws BusinessException;

	JnJProductModel getProductByCatalogIdForEdi(String eanProductNumber, JnJB2BUnitModel jnJB2BUnitModel);

	public List<JnJProductModel> getProductModelByEanProductNumber(final String eanProductNumber, JnJB2BUnitModel jnJB2BUnitModel);

	JnJProductModel getProductByCatalogIdForAnyEcommerceFlag(String catalogId, JnJB2BUnitModel jnJB2BUnitModel);

	JnJProductModel getProductByCatalogIdWithoutSearchRestriction(String catalogIdl);

	String getProductSector(String productCode);

	boolean removeAllProductSalesOrgForProduct(String productCode);

	List<JnJProductModel> getProductsForCatalogVersionId(CatalogVersionModel catalogVersion, List<String> productCodes);
}
