/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product;

import com.jnj.facades.data.JnjProductCarouselData;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Collection;
import java.util.List;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTCpsiaData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;


/**
 * The Interface JnjGTProductFacade.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTProductFacade extends JnjProductFacade
{
	/**
	 * Returns true/ false if excel of all Products from the active Catalog Version for the full catalog download are
	 * emailed successfully.
	 * 
	 */
	public boolean sendEmailForAllProductsOfCatalog();

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 * 
	 * @param sortBy
	 *           the sort by
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	public List<JnjGTCpsiaData> getConsumerProductsCpsia(final String sortBy);

	/**
	 * This method fetches the CPSIA data for the supplied product.
	 * 
	 * @param productId
	 *           the product id
	 * @return cpsiaData
	 */
	public JnjGTCpsiaData getCpsiaDataForProduct(final String productId);

	/**
	 * Retrieves product based on product code, and send along with quantity to trigger contract price outbound call.
	 * 
	 * @param productCode
	 *           the product code
	 * @return contract price
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public String getProductContractPrice(final String productCode) throws SystemException, IntegrationException;

	/**
	 * Returns boolean to indicate mail has been sent successfully or not.
	 * 
	 * @param productData
	 *           the product data
	 * @param contactDetails
	 *           the contact details
	 * @return boolean
	 */
	public boolean sendProductDetailsEmail(final ProductData productData, String contactDetails);

	/**
	 * Gets the product by code. Current session data (catalog versions, user: admin) is used, to return a valid product.
	 * 
	 * This method replicates the functionality of an OOTB method "getProductForCodeAndOptions" written in
	 * "DefaultProductFacade", and, by-passes all restrictions applied on the OOTB method.
	 * 
	 * @param String
	 *           the code
	 * @param Collection
	 *           <ProductOption> the options
	 * @return ProductData
	 */
	public ProductData getProductAsAdmin(final String code, final Collection<ProductOption> options);

	/**
	 * Converts LanguageModel to LanguageData
	 * 
	 * 
	 * @param LanguageModel
	 *           the language Model
	 * @return LanguageData
	 */
	public LanguageData getLanguageData(final LanguageModel languageModel);
	

	/**
	 * Filter the list with the obsolete produtcs
	 * @param selectedProductIds
	 * @return
	 */
	public StringBuilder getObsoleteProductList(String[] selectedProductIds);

	/**
	 * AFFG-1
	 * Changes for Products also bought Together Carousel
	 * @param productCode
	 * @param catalogVersion
	 * @return
	 */
	public List<JnjProductCarouselData> getProductsBoughtTogether(final String productCode, final CatalogVersionModel catalogVersion);
}
