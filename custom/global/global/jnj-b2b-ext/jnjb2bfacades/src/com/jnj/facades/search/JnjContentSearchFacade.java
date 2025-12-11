/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.search;


import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import com.jnj.facades.data.JnjContentData;


/**
 * Content search facade interface. Used to retrieve Content of type {@link JnjContentData} (or subclasses of).
 * 
 * @param <ITEM>
 *           The type of the content result items
 * @author Accenture
 * @version 1.0
 */
public interface JnjContentSearchFacade<ITEM extends JnjContentData>
{


	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection.
	 * 
	 * @param searchState
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	FacetSearchPageData<SearchStateData, ITEM> contentSearch(SearchStateData searchState, PageableData pageableData);

}
