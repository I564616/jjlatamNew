/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.solr.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.springframework.beans.factory.annotation.Autowired;


import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.services.MessageService;


/**
 * This is a generic Facet Value Display Name Provider. It picks the values to be displayed from configured values.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{
	@Autowired
	MessageService messageService;

	public MessageService getMessageService() {
		return messageService;
	}

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		return messageService.getMessageForCode(facetValue, null, Jnjb2bCoreConstants.EMPTY_STRING);
	}

}