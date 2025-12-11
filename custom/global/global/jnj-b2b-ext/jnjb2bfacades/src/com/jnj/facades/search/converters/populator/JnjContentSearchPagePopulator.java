/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.search.converters.populator;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.data.JnjContentData;


/**
 * 
 * This is used to FacetSearchPageData<QUERY, RESULT> to FacetSearchPageData<STATE, ITEM>
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjContentSearchPagePopulator<QUERY, STATE, RESULT, ITEM extends JnjContentData> implements
		Populator<FacetSearchPageData<QUERY, RESULT>, FacetSearchPageData<STATE, ITEM>>
{
	@Autowired
	private Converter<QUERY, STATE> solrSearchStateConverter;
	@Autowired
	private Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter;
	@Autowired
	private Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter;

	@Autowired
	private Converter<RESULT, ITEM> jnjSearchResultContentConverter;




	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final FacetSearchPageData<QUERY, RESULT> source, final FacetSearchPageData<STATE, ITEM> target)
			throws ConversionException
	{

		if (source.getBreadcrumbs() != null)
		{
			target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), breadcrumbConverter));
		}

		target.setCurrentQuery(solrSearchStateConverter.convert(source.getCurrentQuery()));

		if (source.getFacets() != null)
		{
			target.setFacets(Converters.convertAll(source.getFacets(), facetConverter));
		}

		target.setPagination(source.getPagination());

		if (source.getResults() != null)
		{
			target.setResults(Converters.convertAll(source.getResults(), jnjSearchResultContentConverter));
		}

		target.setSorts(source.getSorts());
	}
}
