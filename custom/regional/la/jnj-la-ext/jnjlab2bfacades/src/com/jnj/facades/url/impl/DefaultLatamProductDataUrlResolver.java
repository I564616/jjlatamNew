/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.url.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.url.impl.DefaultProductDataUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.services.CMSSiteService;


/**
 *
 */
public class DefaultLatamProductDataUrlResolver extends DefaultProductDataUrlResolver
{
	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private CatalogService catalogService;

	@Override
	protected String resolveInternal(final ProductData source)
	{
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);
		// Lookup the product
		final ProductModel product = getProductService().getProductForCode(catalogVersionModel, source.getCode());

		return getProductModelUrlResolver().resolve(product);
	}
}
