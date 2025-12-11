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
package com.jnj.la.dataload.dao;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;

import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.dto.JnJLaCustomerDTO;


/**
 *
 */
public interface JnjCustomerDataLoadDao
{
	public List<JnJLaCustomerDTO> fetchCustomerRecords(JnjIntegrationRSACronJobModel jobModel);

}
