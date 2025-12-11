/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.search.querybuilder.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.impl.DefaultFreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;

//import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * This class is used to create query with Partial Match Enabled or Disabled depending upon the flag set in the
 * configuration.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTFreeTextQueryBuilder extends DefaultFreeTextQueryBuilder
{
	private static final Logger LOG = Logger.getLogger(JnjGTFreeTextQueryBuilder.class);

	@Override
	protected void addFreeTextQuery(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String value,
			final double boost)
	{
		final String field = indexedProperty.getName();
		if (!(indexedProperty.isFacet()))
		{
			if ("text".equalsIgnoreCase(indexedProperty.getType()))
			{
				addFreeTextQuery(searchQuery, field, value.toLowerCase(), "", boost);
				addFreeTextQuery(searchQuery, field, "*", value.toLowerCase(), "*", boost / 2.0D);
				if (Config.getBoolean(Jnjb2bCoreConstants.Solr.ENABLE_PARTIAL_SEARCH, true))
				{
					addFreeTextQuery(searchQuery, field, value.toLowerCase(), "~", boost / 4.0D);
				}
			}
			else
			{
				addFreeTextQuery(searchQuery, field, value, "", boost);
				addFreeTextQuery(searchQuery, field, value, "*", boost / 2.0D);
			}
		}
		else
		{
			LOG.warn("Not searching " + indexedProperty
					+ ". Free text search not available in facet property. Configure an additional text property for searching.");
		}
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final String field, final String prefixOp, final String value,
			final String suffixOp, final double boost)
	{
		final RawQuery rawQuery = new RawQuery(field, ClientUtils.escapeQueryChars(value) + suffixOp + "^" + boost, Operator.OR);
		searchQuery.addRawQuery(rawQuery);
	}
}
