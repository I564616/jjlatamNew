/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.la.browseandsearch.controllers;

/**
 */
public interface JnjlabrowseandsearchaddonControllerConstants
{
	String ADDON_PREFIX = "addon:/jnjlabrowseandsearchaddon/";

	interface Views
	{
		interface Pages
		{
			interface Category
			{
				String CategoryPage = "pages/category/categoryPage";
				String ProductListPage = "pages/category/productListPage";
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
}
