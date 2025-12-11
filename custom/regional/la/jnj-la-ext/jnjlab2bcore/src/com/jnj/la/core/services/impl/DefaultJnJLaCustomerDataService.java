/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.services.impl.DefaultJnJCustomerDataService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.daos.JnJLaCustomerDataDao;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;
import com.jnj.la.core.services.JnJLaCustomerDataService;



public class DefaultJnJLaCustomerDataService extends DefaultJnJCustomerDataService implements JnJLaCustomerDataService
{
	protected static final Class currentClass = DefaultJnJLaCustomerDataService.class;

	private static final Logger LOGGER = Logger.getLogger(currentClass);

	private static final String LATAM_CUSTOMER_DATA_SERVICE = "Latam Customer Data Service";

	private JnJLaCustomerDataDao jnJLaCustomerDataDao;

	public JnJLaCustomerDataDao getJnJLaCustomerDataDao()
	{
		return jnJLaCustomerDataDao;
	}

	public void setJnJLaCustomerDataDao(final JnJLaCustomerDataDao jnJLaCustomerDataDao)
	{
		this.jnJLaCustomerDataDao = jnJLaCustomerDataDao;
	}

	@Override
	public JnjIndirectCustomerModel getJnjIndirectCustomerByIDCountry(final String customerNumber, final String country)
	{

		return getJnJCustomerDataDao().getIndirectCustomerByIdCountry(customerNumber, country);
	}

	@Override
	public JnjIndirectPayerModel getJnjIndirectPayerByIDCountry(final String customerNumber, final String country)
	{

		return getJnJLaCustomerDataDao().getIndirectPayerByIdCountry(customerNumber, country);
	}

	@Override
	public List<String> getIndirectCustomer(final String country)
	{
		final String methodName = "getIndirectCustomer()";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		List<String> indirectCustomerList = null;
		final List<JnjIndirectCustomerModel> jnjIndCustModelList = getJnJCustomerDataDao().getIndirectCustomer(country);
		if (CollectionUtils.isNotEmpty(jnjIndCustModelList))
		{
			indirectCustomerList = new ArrayList<>(jnjIndCustModelList.size());
			buildIndirectCustomerStringList(indirectCustomerList, jnjIndCustModelList);

		}

		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);
		return indirectCustomerList;
	}



	@Override
	public List<String> getIndirectPayer(final String country)
	{
		final String methodName = "getIndirectPayer()";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		List<String> indirectPayerList = null;
		final List<JnjIndirectPayerModel> jnjIndirectPayerModelList = getJnJLaCustomerDataDao().getIndirectPayer(country);
		if (CollectionUtils.isNotEmpty(jnjIndirectPayerModelList))
		{
			indirectPayerList = new ArrayList<>(jnjIndirectPayerModelList.size());
			buildIndirectPayerStringList(indirectPayerList, jnjIndirectPayerModelList);

		}
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);
		return indirectPayerList;
	}


	@Override
	public List<JnjIndirectPayerModel> getJnjIndirectPayers(final String country)
	{
		return getJnJLaCustomerDataDao().getIndirectPayer(country);
	}

	@Override
	public List<String> getIndirectCustomer(final String siteDefaultCountry, final String term)
	{
		final String methodName = "getIndirectCustomer(country, term)";
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		List<String> indirectCustomerList = null;
		final List<JnjIndirectCustomerModel> jnjIndCustModelList = getJnJLaCustomerDataDao().getIndirectCustomer(siteDefaultCountry,
				term);
		if (CollectionUtils.isNotEmpty(jnjIndCustModelList))
		{
			indirectCustomerList = new ArrayList<>(jnjIndCustModelList.size());
			buildIndirectCustomerStringList(indirectCustomerList, jnjIndCustModelList);
		}
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);

		return indirectCustomerList;
	}

	@Override
	public List<String> getIndirectPayer(final String siteDefaultCountry, final String term)
	{
		final String methodName = "getIndirectPayer(country, term)";
		JnjGTCoreUtil.logDebugMessage("Indirect Payer", methodName, Logging.BEGIN_OF_METHOD, currentClass);

		List<String> indirectPayerList = null;
		final List<JnjIndirectPayerModel> jnjIndirectPayerModelList = getJnJLaCustomerDataDao().getIndirectPayer(siteDefaultCountry,
				term);
		if (CollectionUtils.isNotEmpty(jnjIndirectPayerModelList))
		{
			indirectPayerList = new ArrayList<>(jnjIndirectPayerModelList.size());
			buildIndirectPayerStringList(indirectPayerList, jnjIndirectPayerModelList);

		}
		JnjGTCoreUtil.logDebugMessage(LATAM_CUSTOMER_DATA_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);
		return indirectPayerList;
	}

	@Override
	public JnjIndirectCustomerModel getIndirectCustomerModel(final String country, final String indirectCustomer)
	{
		return getJnJLaCustomerDataDao().getIndirectCustomerModel(country, indirectCustomer);
	}

	@Override
	public JnjIndirectPayerModel getIndirectPayerModel(final String country, final String indirectPayer)
	{
		return getJnJLaCustomerDataDao().getIndirectPayerModel(country, indirectPayer);
	}
	
	@Override
	public List<JnJLaUserAccountPreferenceModel> getActiveUserNotificationPreference() {
		return getJnJLaCustomerDataDao().getActiveUsersNotification();
	}

	@Override
	public List<JnJB2bCustomerModel> getActiveUsersReport() {
		return getJnJLaCustomerDataDao().getActiveUsers();
	}

	private void buildIndirectPayerStringList(final List<String> indirectPayerList,
			final List<JnjIndirectPayerModel> jnjIndirectPayerModelList)
	{
		for (final JnjIndirectPayerModel jnjIndirectPayerModel : jnjIndirectPayerModelList)
		{
			if (null != jnjIndirectPayerModel)
			{
				indirectPayerList
						.add(jnjIndirectPayerModel.getIndirectPayer() + " - " + jnjIndirectPayerModel.getIndirectPayerName());
			}
		}
	}

	private void buildIndirectCustomerStringList(final List<String> indirectCustomerList,
			final List<JnjIndirectCustomerModel> jnjIndCustModelList)
	{
		for (final JnjIndirectCustomerModel jnjIndirectCustomerModel : jnjIndCustModelList)
		{
			if (null != jnjIndirectCustomerModel)
			{
				indirectCustomerList.add(
						jnjIndirectCustomerModel.getIndirectCustomer() + " - " + jnjIndirectCustomerModel.getIndirectCustomerName());
			}
		}

	}
		
}
