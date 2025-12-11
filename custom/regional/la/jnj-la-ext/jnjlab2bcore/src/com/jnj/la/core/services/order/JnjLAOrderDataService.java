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
package com.jnj.la.core.services.order;

import java.util.List;

import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;


public interface JnjLAOrderDataService
{
	List<JnjOrderDTO> pullOrdersFromRSA(JnjIntegrationRSACronJobModel cronJob);

	void saveOrdersToHybris(List<JnjOrderDTO> orders, final JnjIntegrationRSACronJobModel cronJob);

	void processSAPOrdersList(final List<JnjOrderDTO> sapOrders, final JnjIntegrationRSACronJobModel cronJob);

    void processOrdersHeaderStatus(JnjIntegrationRSACronJobModel cronJobModel);
}
