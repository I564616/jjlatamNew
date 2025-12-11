/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.company.JnjCompanyService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnJCompanyModel;


/**
 * Company Service to deal operations related to JnjCompanyModel.
 *
 * @author mpanda3
 * @version 1.0
 */
public class JnjCompanyServiceImpl implements JnjCompanyService
{

	/** The Constant JNJ_COMPANY_SERVICE. */
	private static final String JNJ_COMPANY_SERVICE = "JnJ Company Service";
	private static final String MASTER_COMPANY_QUERY = "SELECT {PK} FROM {JnJCompany} WHERE {uid} IN (?uid) AND {masterCompany} = (?masterCompanyflag)";


	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjCompanyServiceImpl.class);

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The flexible search service. */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private final JnJCommonUtil jnjCommonUtil = new JnJCommonUtil();

	@Override
	public JnJCompanyModel getMasterCompanyForUid(final String companyUid) throws BusinessException
	{
		LOG.info("Comapny uid value: "+companyUid);
		final String METHOD_NAME = "getMasterCompany()";
		jnjCommonUtil.logMethodStartOrEnd(JNJ_COMPANY_SERVICE, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		if (StringUtils.isEmpty(companyUid))
		{
			LOG.error(JNJ_COMPANY_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Cannot find Company for ID [null]");
			throw new BusinessException("Cannot find Company for ID [null]");
		}

		JnJCompanyModel jnJCompanyModel = null;

		try
		{
			final FlexibleSearchQuery masterCompanyQuery = new FlexibleSearchQuery(MASTER_COMPANY_QUERY);
			masterCompanyQuery.addQueryParameter("uid", companyUid);
			masterCompanyQuery.addQueryParameter("masterCompanyflag", Boolean.TRUE);
			jnJCompanyModel = flexibleSearchService.searchUnique(masterCompanyQuery);
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{
			LOG.error(JNJ_COMPANY_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Master JnJCompanyModel with ID ["
					+ companyUid + "] not found in Hybris.", exception);
			throw new BusinessException(
					"Master JnJCompanyModel with ID [" + companyUid + "] not found in Hybris. " + exception.getLocalizedMessage());
		}
		jnjCommonUtil.logMethodStartOrEnd(JNJ_COMPANY_SERVICE, METHOD_NAME, Logging.END_OF_METHOD);
		return jnJCompanyModel;
	}

	@Override
	public Collection<JnJCompanyModel> getAllMasterCompanies() throws BusinessException
	{
		final String METHOD_NAME = "getAllMasterCompanies()";
		jnjCommonUtil.logMethodStartOrEnd(JNJ_COMPANY_SERVICE, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		List<JnJCompanyModel> masterJnJCompanyModelList = new ArrayList<JnJCompanyModel>();
		final JnJCompanyModel tempJnjCompayModel = modelService.create(JnJCompanyModel.class);
		tempJnjCompayModel.setMasterCompany(Boolean.TRUE);

		try
		{
			masterJnJCompanyModelList = flexibleSearchService.getModelsByExample(tempJnjCompayModel);
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{
			LOG.error(JNJ_COMPANY_SERVICE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Exeption occured while fetching Master Jnj Company from Hybris.", exception);
			throw new BusinessException(
					"Exeption occured while fetching All Master Jnj Companies from Hybris. " + exception.getLocalizedMessage());
		}
		jnjCommonUtil.logMethodStartOrEnd(JNJ_COMPANY_SERVICE, METHOD_NAME, Logging.END_OF_METHOD);
		return masterJnJCompanyModelList;
	}
}
