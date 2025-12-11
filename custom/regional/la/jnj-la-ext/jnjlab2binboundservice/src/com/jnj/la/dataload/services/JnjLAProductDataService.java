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

import java.util.Date;
import java.util.List;

import com.jnj.la.core.dto.JnJLaProductDTO;
import de.hybris.platform.cronjob.model.CronJobModel;


/**
 * Defines the methods to pull data from RSA, map it into JnJLAProduct and save it.
 */
public interface JnjLAProductDataService
{
	List<JnJLaProductDTO> pullProductsFromRSA(Date lowerDate, Date upperDate, JnjIntegrationRSACronJobModel cronJob, String sector);

	void saveProductToHybris(List<JnJLaProductDTO> products, List<String> failedProducts);

	List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector);

	Date getLastUpdatedDateForLatestRecord();
}
