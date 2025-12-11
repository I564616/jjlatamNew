/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.account.impl;

import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.account.JnjSellOutReportsFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjSellOutReportData;
import com.jnj.la.core.model.JnjSellOutReportModel;
import com.jnj.la.core.services.JnjSellOutReportsService;

import jakarta.annotation.Resource;


/**
 * This class converts the sell out reports model into list object and also invokes the service layer to update this
 * model.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjSellOutReportsFacadeImpl implements JnjSellOutReportsFacade
{
	@Autowired
	private JnjSellOutReportsService sellOutReportsService;

	@Autowired
	private ModelService modelService;

	@Resource(name = "defaultUserService")
	private UserService userservice;

	@Autowired
	private DefaultCompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	private static final Logger LOG = Logger.getLogger(JnjSellOutReportsFacadeImpl.class);


	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SearchPageData<JnjSellOutReportData> getSellOutReportData(final String sortflag, final PageableData pageableData)
	{
		//Instantiating list objects
		SearchPageData<JnjSellOutReportModel> list = null;
		try
		{
			list = sellOutReportsService.getSellOutReportData(sortflag, pageableData);
		}
		catch (final Exception exp)
		{

			LOG.error("Error while calling service class to obtain the list object of JnjSellOutReportModel type - " + exp);
		}



		final SearchPageData<JnjSellOutReportData> result = new SearchPageData<>();
		if (list != null)
		{
			final Collection<JnjSellOutReportModel> sellOutList = list.getResults();
			final List<JnjSellOutReportData> sellOutDataList = templateConverter(sellOutList);
			result.setResults(sellOutDataList);
			result.setPagination(list.getPagination());
		}
		return result;
	}

	/**
	 *
	 *
	 * @param sellOutList
	 * @return List
	 */
	private List<JnjSellOutReportData> templateConverter(final Collection<JnjSellOutReportModel> sellOutList)
	{
		final List<JnjSellOutReportData> sellOutDataList = new ArrayList<>();

		for (final JnjSellOutReportModel sellOutModel : sellOutList)
		{
			final JnjSellOutReportData templateData = createOrderTemplateData(sellOutModel);

			sellOutDataList.add(templateData);

		}
		return sellOutDataList;
	}

	/**
	 * @param jnjSellOutReportModel
	 * @return JnjSellOutReportData
	 */
	private JnjSellOutReportData createOrderTemplateData(final JnjSellOutReportModel jnjSellOutReportModel)
	{
		final JnjSellOutReportData sellOutReportData = new JnjSellOutReportData();
		try
		{
			sellOutReportData.setCompany(jnjSellOutReportModel.getCompany());
			if (null != jnjSellOutReportModel.getCustomer())
			{
				sellOutReportData.setCustomer(jnjSellOutReportModel.getCustomer());
			}
			final SimpleDateFormat enFormatter = new SimpleDateFormat(Jnjlab2bcoreConstants.SellOutReports.EN_DATE_FORMAT);
			final SimpleDateFormat ptFormatter = new SimpleDateFormat(Jnjlab2bcoreConstants.SellOutReports.PT_DATE_FORMAT);
			String language = null;
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) companyB2BCommerceService.getCurrentUser();
			if (null != currentUser.getSessionLanguage())
			{
				language = currentUser.getSessionLanguage().getIsocode();
			}
			if (null != language && ("en").equalsIgnoreCase(language))
			{
				sellOutReportData.setDate(enFormatter.format(jnjSellOutReportModel.getDate()));
			}
			else
			{
				sellOutReportData.setDate(ptFormatter.format(jnjSellOutReportModel.getDate()));
			}
			if (null != jnjSellOutReportModel.getDocName())
			{
				sellOutReportData.setDocName(jnjSellOutReportModel.getDocName());
			}
			if (null != jnjSellOutReportModel.getUser())
			{
				sellOutReportData.setUser(jnjSellOutReportModel.getUser());
			}
		}
		catch (final Exception exception)
		{
			LOG.warn("exception occured while updating model to data file - " + exception, exception);
		}

		return sellOutReportData;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean updateUploadHistory(final JnjSellOutReportData sellOutReportData)
	{

		final JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel) userservice.getCurrentUser();
		//Creating model object
		final JnjSellOutReportModel jnjSellOutReportModel = modelService.create(JnjSellOutReportModel.class);
		//storing values from dto class into the model
		jnjSellOutReportModel.setUser(customerModel.getName());
		jnjSellOutReportModel.setCompany(sellOutReportData.getCompany());
		jnjSellOutReportModel.setB2bUnitId(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit());
		final Date date = new Date();
		jnjSellOutReportModel.setDate(date);
		jnjSellOutReportModel.setCustomer(sellOutReportData.getCustomer());
		jnjSellOutReportModel.setDocName(sellOutReportData.getDocName());


		boolean statusflag = false;
		try
		{
			statusflag = sellOutReportsService.updateUploadHistory(jnjSellOutReportModel);
		}
		catch (final Exception exp)
		{
			LOG.error("Error while calling service class to save the model - " + exp);
		}
		return statusflag;
	}
}
