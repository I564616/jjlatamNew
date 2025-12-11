/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.pcm.integration.core.job.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;


/**
 * This Interface s used by the integration jobs for DB calls
 */
public interface JnjCCP360IntegrationDAO
{
	/**
	 * This method is used to return Category model for given category code and catalogversion
	 *
	 * @param categoryCode
	 *           Input parameter
	 * @param catalogVersionModel
	 *           Input parameter
	 * @return CategoryModel return category Model if available
	 */
	public CategoryModel getCategoryModel(final String categoryCode, final CatalogVersionModel catalogVersionModel);

	/**
	 * This method is used to fetch Category JnJProductModel deltas for given start dtae
	 *
	 * @param startDate
	 *           Input field to fetch deltas on or after this date
	 * @return List<JnJProductModel> List of Deltas
	 */
	public List<JnJProductModel> getProductDetailList(final String startDate);

	/**
	 * This method returns ProductDocumentsModel for updating Product
	 *
	 * @param name
	 *           ProductDocumentsModel ID
	 * @param url
	 *           ProductDocumentsModel URL
	 * @return ProductDocumentsModel returns ProductDocumentsModel if available in DB
	 */
	public ProductDocumentsModel getProductDocumentByNameAndUrl(final String name, final String url);

	/**
	 * This method returns a related product model if available
	 *
	 * @param jnjProductModel
	 *           source of the relation type
	 * @param targetModel
	 *           target of the relation type
	 * @return List<ProductReferenceModel> returns related Products
	 */
	public List<ProductReferenceModel> getProductReferenceModelList(final JnJProductModel jnjProductModel,
			final JnJProductModel targetModel);
}
