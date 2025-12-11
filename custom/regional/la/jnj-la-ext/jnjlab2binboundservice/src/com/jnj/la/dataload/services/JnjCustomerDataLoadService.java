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
package com.jnj.la.dataload.services;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.List;
import java.util.Map;

import com.jnj.la.core.dto.JnJLaCustomerDTO;


/**
 *
 */
public interface JnjCustomerDataLoadService
{

	public Map<Integer, List<JnJLaCustomerDTO>> fetchCustomerRecords(JnjIntegrationRSACronJobModel jobModel);

}
