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
package com.jnj.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.services.JnjLaSalesOrgCustomerService;
import com.jnj.core.util.JnjGTCoreUtil;



public class JnjLaSalesOrgCustomerServiceImpl extends DefaultJnjSalesOrgCustService implements JnjLaSalesOrgCustomerService
{
	private static final Class currentClass = JnjLaSalesOrgCustomerServiceImpl.class;

	@Override
	public String getSalesOrgByProductAndB2bUnitModel(final JnJProductModel jnjProductModel, final JnJB2BUnitModel unit)
	{
		final String methodName = "getSalesOrgByProductAndB2bUnitModel()";
		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final String salesOrgWithOrderType = getSalesOrgAndSapOrderType(jnjProductModel, unit);

		JnjGTCoreUtil.logInfoMessage(LATAM_SALES_ORG_CUSTOMER, methodName, "Sales Org With Order Type: " + salesOrgWithOrderType,
				currentClass);

		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.END_OF_METHOD, currentClass);
		return salesOrgWithOrderType;
	}

	@Override
	public String getSalesOrgAndSapOrderType(final JnJProductModel jnjProductModel, final JnJB2BUnitModel unit)
	{
		final String methodName = "getSalesOrgAndSapOrderType()";
		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final Map<String, String> sectorSalesOrgMapping = getSectorAndSalesOrgMapping(unit);
		String orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZOR;

		String dispatcherSalesOrg = StringUtils.EMPTY;
		final String productSector = jnjProductModel.getSector() == null ? StringUtils.EMPTY
				: jnjProductModel.getSector().toUpperCase();
		if (null != sectorSalesOrgMapping && sectorSalesOrgMapping.containsKey(productSector))
		{
			dispatcherSalesOrg = sectorSalesOrgMapping.get(productSector);
			//Check if product is cold Chain product and Sap order type will be ZORD if corresponding salesOrg does not have cold chain storage.
			if (jnjProductModel.getColdChainProduct().booleanValue() && !checkColdChainStorage(dispatcherSalesOrg))
			{
				orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZORD;
			}
		}

		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		return dispatcherSalesOrg.concat(Jnjb2bCoreConstants.Order.PIPE_STRING).concat(orderType);
	}

	@Override
	public Map<String, String> getSectorAndSalesOrgMapping(final JnJB2BUnitModel unit)
	{
		final String methodName = "getSectorAndSalesOrgMapping()";
		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final List<JnJSalesOrgCustomerModel> salesOrgs = getSalesOrgsForUnit(unit);

		final Map<String, String> salesOrgMap = new HashMap<>();
		for (final JnJSalesOrgCustomerModel salesOrg : salesOrgs)
		{
			if (null != salesOrg)
			{
				salesOrgMap.put(salesOrg.getSector().toUpperCase(), salesOrg.getSalesOrg());
			}
		}
		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.END_OF_METHOD, currentClass);
		return salesOrgMap;
	}

	@Override
	public List<JnJSalesOrgCustomerModel> getSalesOrgsForUnit(final JnJB2BUnitModel unit)
	{
		final String methodName = "getSalesOrgsForUnit()";
		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);
		final List<JnJSalesOrgCustomerModel> salesOrgList = getFlexibleSearchService().getModelsByExample(jnjSalesOrgCustomerModel);

		JnjGTCoreUtil.logDebugMessage(LATAM_SALES_ORG_CUSTOMER, methodName, Logging.END_OF_METHOD, currentClass);
		return salesOrgList;
	}

}
