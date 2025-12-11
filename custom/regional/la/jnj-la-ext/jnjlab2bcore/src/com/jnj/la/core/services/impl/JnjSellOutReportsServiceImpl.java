/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.la.core.daos.JnjSellOutReportsDao;
import com.jnj.la.core.model.JnjSellOutReportModel;
import com.jnj.la.core.services.JnjSellOutReportsService;




/**
 * This class converts the sell out reports model into list object and also invokes the service layer to update this
 * model.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjSellOutReportsServiceImpl implements JnjSellOutReportsService
{
	private static final Logger LOG = Logger.getLogger(JnjSellOutReportsServiceImpl.class);
	@Autowired
	protected JnjSellOutReportsDao sellOutReportsDao;

	@Autowired
	protected ModelService modelService;

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<JnjSellOutReportModel> getSellOutReportData(final String sortflag, final PageableData pageableData)
	{
		return sellOutReportsDao.getSellOutReportData(sortflag, pageableData);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean updateUploadHistory(final JnjSellOutReportModel jnjSellOutReportModel)
	{
		boolean status;
		try
		{
			// Saving the jnjSellOutReportModel in database
			modelService.save(jnjSellOutReportModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("document details have been saved in jnjSellOutReportModel.");
			}
			status = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOG.error("Exception occurred while saving Document details in jnjSellOutReportModel:" + modelSavingException);
			status = false;
		}
		return status;
	}

	/**
	 *
	 * Setter for ModelService class
	 *
	 * @param modelService
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
