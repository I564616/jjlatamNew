/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.core.job.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.ProductDocumentsModel;


/**
 * Interface for JJCC P360 Integration
 */
public interface JnjCCP360IntegrationService
{

	/**
	 * Fetches the CategoryModel based on Code & CatalogVersion
	 * 
	 * @param categoryCode
	 *           the categoryCode to be used
	 * @param catalogVersionModel
	 *           the catalogVersion to be used
	 * @return CategoryModel
	 */

	public CategoryModel getCategoryModel(final String categoryCode, final CatalogVersionModel catalogVersionModel);

	/**
	 * uploads the Product Detail File based on a selected date
	 * 
	 * @param startDate
	 *           the date to be used
	 * @return Boolean
	 */

	public boolean uploadProductDetailFile(final String startDate);

	/**
	 * Fetches the ProductDocumentsModel based on Name & Url
	 * 
	 * @param name
	 *           the name to be used
	 * @param url
	 *           the url to be used
	 * @return ProductDocumentsModel
	 */

	public ProductDocumentsModel getProductDocumentByNameAndUrl(final String name, final String url);


	/**
	 * Return list ProductReferenceModel based on Product & Related Product
	 * 
	 * @param jnjProductModel
	 *           the product to be used as source
	 * @param targetModel
	 *           the product to be used as target
	 * @return ProductReferenceModel
	 */
	public List<ProductReferenceModel> getProductReferenceModelList(final JnJProductModel jnjProductModel,
			final JnJProductModel targetModel);



}
