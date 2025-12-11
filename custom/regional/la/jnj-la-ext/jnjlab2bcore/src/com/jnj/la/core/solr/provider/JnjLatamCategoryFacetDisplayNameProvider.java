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
package com.jnj.la.core.solr.provider;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.CommerceCategoryFacetDisplayNameProvider;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.services.CMSSiteService;


public class JnjLatamCategoryFacetDisplayNameProvider extends CommerceCategoryFacetDisplayNameProvider
{
	@Autowired
	protected transient CatalogService catalogService;

	@Autowired
	private transient CMSSiteService cmsSiteService;

	@SuppressWarnings("deprecation")
	@Override
	protected CategoryModel getCategory(final String code)
	{
		final String methodName = "getCategory(String)";

		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);
		CategoryModel category = null;
		try
		{
			category = getCategoryService().getCategoryForCode(catalogVersionModel, code);
		}
		catch (final UnknownIdentifierException e)
		{
			JnjGTCoreUtil.logErrorMessage("Latam Category Display Name Provider", methodName, e.getMessage(), e,
					JnjLatamCategoryFacetDisplayNameProvider.class);
		}
		return category;
	}
}
