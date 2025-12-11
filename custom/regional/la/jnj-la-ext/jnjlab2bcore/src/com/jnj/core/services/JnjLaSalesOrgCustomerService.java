/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.services;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;



public interface JnjLaSalesOrgCustomerService
{
	static final String LATAM_SALES_ORG_CUSTOMER = "Latam Sales Org Customer Interface";

	String getSalesOrgByProductAndB2bUnitModel(final JnJProductModel jnjProductModel, JnJB2BUnitModel unit);

	String getSalesOrgAndSapOrderType(JnJProductModel jnjProductModel, JnJB2BUnitModel unit);

	Map<String, String> getSectorAndSalesOrgMapping(JnJB2BUnitModel unit);

	List<JnJSalesOrgCustomerModel> getSalesOrgsForUnit(JnJB2BUnitModel unit);
}
