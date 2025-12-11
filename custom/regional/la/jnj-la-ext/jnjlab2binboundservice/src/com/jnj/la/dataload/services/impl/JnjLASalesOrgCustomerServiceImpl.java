/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.dataload.services.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;
import com.jnj.la.dataload.services.JnjLASalesOrgCustomerService;
import com.jnj.la.dataload.services.JnjRSASalesOrgCustDataService;



/**
 *
 */
@SuppressWarnings("deprecation")
public class JnjLASalesOrgCustomerServiceImpl implements JnjLASalesOrgCustomerService
{

	@Autowired
	private JnjRSASalesOrgCustDataService jnjRSASalesOrgCustDataService;

	protected boolean recordStatus = false;

	/**
	 * Instance of <code>CompanyB2BCommerceService</code>
	 */
	@SuppressWarnings("deprecation")
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;


	@SuppressWarnings("deprecation")
	public CompanyB2BCommerceService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}


	public void setCompanyB2BCommerceService(
			@SuppressWarnings("deprecation") final CompanyB2BCommerceService companyB2BCommerceService)
	{
		this.companyB2BCommerceService = companyB2BCommerceService;
	}


	@Override
	public List<JnjSalesOrgCustDTO> pullDataFromRSA(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String METHOD_NAME = "pullDataFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjLASalesOrgCustomerServiceImpl.class);

		final List<JnjSalesOrgCustDTO> jnjSalesOrgCustDtoList = jnjRSASalesOrgCustDataService.pullDataFromRSA(cronJobModel);

		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.END_OF_METHOD,
				JnjLASalesOrgCustomerServiceImpl.class);
		return jnjSalesOrgCustDtoList;
	}

}
