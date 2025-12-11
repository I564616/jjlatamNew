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
package com.jnj.la.dataload;



import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;
import com.jnj.la.dataload.mapper.JnjLASalesOrgCustomerMapper;
import com.jnj.la.dataload.services.JnjLASalesOrgCustomerService;



/**
 *
 */
public class JnjLASalesOrgCustomerDataload
{

	@Autowired
	protected JnjLASalesOrgCustomerMapper jnjLASalesOrgCustomerMapper;

	@Autowired
	protected JnjLASalesOrgCustomerService jnjLASalesOrgCustomerService;


	public void getSalesOrgCustomerData(final JnjIntegrationRSACronJobModel cronJobModel) throws NumberFormatException, BusinessException
	{
		final String METHOD_NAME = "getSalesOrgCustomerData()";

		final List<JnjSalesOrgCustDTO> salesOrgs = jnjLASalesOrgCustomerService.pullDataFromRSA(cronJobModel);
		if (!salesOrgs.isEmpty())
		{
			jnjLASalesOrgCustomerMapper.saveDataToHybris(salesOrgs, cronJobModel);
		}

		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SALES_ORG_CUST, METHOD_NAME,
					"There is no new data to be saved in hybris. Terminating cronjob", JnjLASalesOrgCustomerDataload.class);

		}

	}
}
