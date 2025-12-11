/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.browseandsearch.controllers;

/**
 */
public interface Jnjb2bbrowseandsearchControllerConstants
{
	String ADDON_PREFIX = "addon:/jnjb2bbrowseandsearch/";

	interface Views
	{
		interface Pages
		{
			interface Category
			{
				String CategoryPage = "pages/category/categoryPage";
				String ProductListPage = "pages/category/productListPage";
				public static final String ProductHierarchyPage = "pages/category/productHierarchyPage"; //AFFG-20804
			}

			interface Search
			{
				String searchPage = "pages/search/searchEmptyPage";
				String searchListPage = "pages/search/searchListPage";

			}
			interface Cart
			{	
			String productDetailPopUp = "pages/product/productPopup";
			}
			
			interface Product
			{
				String ZoomImagesPopup = "pcm/fragments/product/zoomImagesPopup";
				String ProductDetails = "pcm/fragments/product/productDescription";
				String ProductLayout1 = "pages/product/productLayout1Page";
				String noProductFoundPage = "pages/product/noProductFoundPage";
			}
		}
	}
	
	interface JnjGTExcelPdfViewLabels
	{
		interface ProductSearch
		{
			String PRICING_DISCLAIMER_MSG = "product.search.download.priceDisclaimer.pricing";
			String NO_PRICING_DISCLAIMER_MSG = "product.search.download.priceDisclaimer.nopricing";
		}
	}
}
