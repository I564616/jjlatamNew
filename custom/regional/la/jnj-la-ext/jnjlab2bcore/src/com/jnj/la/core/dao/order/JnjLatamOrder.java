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
package com.jnj.la.core.dao.order;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;

import com.jnj.core.dao.order.JnjGTOrderDao;
import com.jnj.core.dto.JnjGTOrderHistoryForm;


/**
 *
 */
public interface JnjLatamOrder extends JnjGTOrderDao
{

	/**
	 * Gets the paged order history for statuses.
	 *
	 * @param pageableData
	 *           the pageable data
	 * @param form
	 *           the form
	 * @return the paged order history for statuses
	 */
	@Override
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData, JnjGTOrderHistoryForm form);

}
