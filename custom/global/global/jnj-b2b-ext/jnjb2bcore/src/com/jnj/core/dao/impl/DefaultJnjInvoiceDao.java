/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.dao.JnjInvoiceDao;
import com.jnj.core.model.JnJInvoiceOrderModel;


/**
 * This class converts the sell out reports model into list object.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjInvoiceDao extends AbstractItemDao implements JnjInvoiceDao
{
	private static final Logger LOG = Logger.getLogger(DefaultJnjInvoiceDao.class);


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJInvoiceOrderModel> getInvoiceOrderData()
	{

		List<JnJInvoiceOrderModel> result = null;
		try
		{
			final Map queryParams = new HashMap();

			String query = "";

			query = "select {pk} from {JnJInvoiceOrder}";


			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameters(queryParams);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Executing query for obtaining JnJInvoiceOrderModel records.");
			}
			result = getFlexibleSearchService().<JnJInvoiceOrderModel> search(fQuery).getResult();

		}
		catch (final Exception exp)
		{
			LOG.error("Error while executing query to get JnJInvoiceOrderModel records in JnjInvoiceDaoImpl class - "
					+ exp.getMessage());
		}
		return result;

	}



}
