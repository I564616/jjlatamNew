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
package com.jnj.b2b.facades.search.impl;

import de.hybris.platform.b2bacceleratorservices.search.B2BProductSearchService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import com.jnj.b2b.facades.search.AbstractB2BProductSearchFacade;

import java.util.Collection;
import java.util.List;


/**
 * B2B facade used for offering FlexibleSearch capabilities to the AdvancedSearch feature in B2B.
 * @deprecated see {@link de.hybris.platform.b2bacceleratorfacades.search.impl.DefaultB2BFlexibleSearchProductSearchFacade}
 */
@Deprecated
public class DefaultB2BFlexibleSearchProductSearchFacade<ITEM extends ProductData> extends
		AbstractB2BProductSearchFacade<ProductData>
{

	private B2BProductSearchService b2bFlexibleSearchProductSearchService;
	private ConfigurablePopulator<SearchPageData, ProductSearchPageData, ProductOption> flexibleSearchPopulator;

	@Override
	public ProductSearchPageData<SearchStateData, ProductData> searchForSkus(final Collection<String> skus,
			final PageableData pageableData, final Collection<ProductOption> options, final boolean populateMatrix)
	{
		final ProductSearchPageData productSearchPageData = new ProductSearchPageData();
		flexibleSearchPopulator.populate(b2bFlexibleSearchProductSearchService.findProductsBySkus(skus, pageableData),
				productSearchPageData, options);

		if (populateMatrix)
		{
			populateVariantProducts(productSearchPageData);
		}

		return productSearchPageData;
	}

	public void setFlexibleSearchPopulator(
			final ConfigurablePopulator<SearchPageData, ProductSearchPageData, ProductOption> flexibleSearchPopulator)
	{
		this.flexibleSearchPopulator = flexibleSearchPopulator;
	}

	public void setB2bFlexibleSearchProductSearchService(final B2BProductSearchService b2bFlexibleSearchProductSearchService)
	{
		this.b2bFlexibleSearchProductSearchService = b2bFlexibleSearchProductSearchService;
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductSearchPageData<SearchStateData, ProductData> textSearch(final String text)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductSearchPageData<SearchStateData, ProductData> textSearch(final SearchStateData searchState,
			final PageableData pageableData)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> categorySearch(final String categoryCode)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> categorySearch(final String categoryCode,
			final SearchStateData searchState, final PageableData pageableData)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public List<AutocompleteSuggestionData> getAutocompleteSuggestions(final String input)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductSearchPageData<SearchStateData, ProductData> textSearch(final String text, final boolean populateMatrix)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * This implementation always throws an {@code UnsupportedOperationException} because the operation is not supported
	 * by FlexibleSearch search.
	 * 
	 * @throws UnsupportedOperationException
	 *            {@inheritDoc}
	 */
	@Override
	public ProductSearchPageData<SearchStateData, ProductData> textSearch(final SearchStateData searchState,
			final PageableData pageableData, final boolean populateMatrix)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> categorySearch(String text, SearchQueryContext searchQueryContext)
	{
		return null;
	//	
	}
	@Override
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> textSearch(String text, SearchQueryContext searchQueryContext)
	{
		return null;
	//	
	}

}