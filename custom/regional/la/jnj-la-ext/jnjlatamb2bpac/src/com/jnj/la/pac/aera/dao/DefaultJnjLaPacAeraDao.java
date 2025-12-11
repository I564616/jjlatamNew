/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.pac.aera.dao;

import java.util.List;

import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.pac.aera.job.dao.DefaultJnjPacAeraDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.Nonnull;

/**
 * This Class is used to override DefaultJnjPacAeraDao  to add functionality to get OrphanPacHiveEntries 
 * from DB using OrderEntryData. 
 * 
 */
public class DefaultJnjLaPacAeraDao  extends DefaultJnjPacAeraDao implements JnjLaPacAeraDao  {
	
	private static final Logger LOG  = LoggerFactory.getLogger(DefaultJnjLaPacAeraDao.class);


	/**
	 * This method is used to get OrphanPacHiveEntries from DB  using OrderEntryData.
	 * 
	 * @param orderEntryModel
	 * @return List of type JnjPacHiveEntryModel
	 */
	@Override
	public List<JnjPacHiveEntryModel> getOrphanPacHiveEntriesByOrderEntryData(@Nonnull final AbstractOrderEntryModel orderEntryModel)
	{
		Validate.notNull(orderEntryModel, "Parameter 'orderEntryModel' can not be null.");
		
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT max({");
		searchQuery.append(JnjPacHiveEntryModel.PK);
		searchQuery.append("}),{");
        searchQuery.append(JnjPacHiveEntryModel.ORDERNUMBER + "},{");
        searchQuery.append(JnjPacHiveEntryModel.LINENUMBER + "},{");
        searchQuery.append(JnjPacHiveEntryModel.SCHEDLINENUMBER);
        searchQuery.append("} FROM {");
		searchQuery.append(JnjPacHiveEntryModel._TYPECODE);
		searchQuery.append("} WHERE {");
		searchQuery.append(JnjPacHiveEntryModel.ORDERNUMBER);
		searchQuery.append("} = ?" + AbstractOrderModel.SAPORDERNUMBER);
		searchQuery.append(" AND {");
		searchQuery.append(JnjPacHiveEntryModel.CATALOGCODE);
		searchQuery.append("} = ?" + ProductModel.CODE);
		searchQuery.append(" AND TO_NUMBER({");
		searchQuery.append(JnjPacHiveEntryModel.LINENUMBER);
		searchQuery.append("}) = TO_NUMBER(?" + AbstractOrderEntryModel.SAPORDERLINENUMBER+") ");
        searchQuery.append("group by {");
        searchQuery.append(JnjPacHiveEntryModel.SCHEDLINENUMBER+ "},{");
        searchQuery.append(JnjPacHiveEntryModel.ORDERNUMBER + "},{");
        searchQuery.append(JnjPacHiveEntryModel.LINENUMBER + "}"); 
		
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameter(AbstractOrderModel.SAPORDERNUMBER, this.stripLeadingZeros(orderEntryModel.getOrder().getSapOrderNumber()));
		flexibleSearchQuery.addQueryParameter(AbstractOrderEntryModel.SAPORDERLINENUMBER, orderEntryModel.getSapOrderlineNumber());
		flexibleSearchQuery.addQueryParameter(ProductModel.CODE, orderEntryModel.getProduct().getCode());
		if(LOG.isDebugEnabled()) {
		LOG.debug(String.format("[DefaultJnjLaPacAeraDao: getOrphanPacHiveEntriesByOrderEntryData] flexibleSearchQuery: %s ", flexibleSearchQuery));
		}	
		
		final SearchResult<JnjPacHiveEntryModel> searchResult = this.getFlexibleSearchService().search(flexibleSearchQuery);
		final List<JnjPacHiveEntryModel> orphanPACEntries = searchResult.getResult();

		if (null != orderEntryModel.getOrder() && LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"No Orphan PacHiveEntries found for given OrderEntry of sapOrderNumber '%s' using strict search.",
					orderEntryModel.getOrder().getSapOrderNumber()));
		}

		return orphanPACEntries;
	}


}
