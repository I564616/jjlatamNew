/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.pac.aera.job.dao;

import com.gt.pac.aera.JnJPacAeraResponse;
import com.gt.pac.aera.PacHiveException;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import jakarta.annotation.Nonnull;


/**
 * @author PRajago4
 */
public interface JnjPacAeraDao
{

	/**
	 * Finds {@link JnjPacHiveEntryModel} corresponding to the given {@link JnJPacAeraResponse} inside the provided
	 * {@link AbstractOrderEntryModel}.
	 *
	 * @param jnJPacAeraResponse {@link JnJPacAeraResponse} defining search criteria.
	 * @param orderEntryModel    {@link AbstractOrderEntryModel} to search in.
	 * @return {@link JnjPacHiveEntryModel} corresponding to the given {@link JnJPacAeraResponse}.
	 * @throws PacHiveException if nothing is found.
	 */
	@Nonnull
	JnjPacHiveEntryModel findJnjPacHiveEntryModel(
			@Nonnull JnJPacAeraResponse jnJPacAeraResponse,
			@Nonnull AbstractOrderEntryModel orderEntryModel
	) throws PacHiveException;

	/**
	 * Finds {@link AbstractOrderEntryModel} corresponding to the given {@link com.gt.pac.aera.JnJPacAeraResponse}.
	 * To understand why it is working like this see com.jnj.la.core.services.order.impl.OrderEntryCloneHelper#createCloneKey(de.hybris.platform.core.model.order.AbstractOrderEntryModel)
	 * and
	 * https://jira.jnj.com/browse/AALH-2552?focusedCommentId=2629795&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-2629795
	 *
	 * @param jnJPacAeraResponse {@link JnJPacAeraResponse} defining search criteria.
	 * @return {@link AbstractOrderEntryModel} corresponding to the given {@link com.gt.pac.aera.JnJPacAeraResponse}.
	 * @throws PacHiveException if nothing is found.
	 */
	@Nonnull
	AbstractOrderEntryModel findCorrespondingOrderEntryModel(@Nonnull JnJPacAeraResponse jnJPacAeraResponse)
	throws Exception;

}
