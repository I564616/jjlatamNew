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
package com.jnj.la.dataload.dao.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.impl.JnjRSAOrderDaoImpl;
import com.jnj.la.core.dao.product.impl.JnjRSAProductDaoImpl;
import com.jnj.la.core.daos.impl.CustomerRowMapper;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.dao.JnjCustomerDataLoadDao;


/**
 *
 */
public class JnjCustomerDataLoadDaoImpl implements JnjCustomerDataLoadDao
{

	private static final Logger LOGGER = Logger.getLogger(JnjCustomerDataLoadDaoImpl.class);

	@Autowired
	private JNJRSADBConnector rsaDBConnector;

	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	protected static Map<Integer, List<JnJLaCustomerDTO>> listOfAllCustomers = null;


	@Override
	public List<JnJLaCustomerDTO> fetchCustomerRecords(final JnjIntegrationRSACronJobModel jobModel)
	{

		final String queryForCustomerRecords = "SELECT SC.CUSTOMER_NUM,SC.NAME_1,SC.NAME_2,SC.ADDRESS_LINE_1,SC.CITY,SC.COUNTY,SC.COUNTRY_CD,SC.REGION_STATE_CD,SC.POSTAL_CODE,SC.ACCOUNT_TYPE,SC.TELEPHONE,SC.TAXID,SC.INDUSTRY_CODE_1,SC.INDUSTRY_CODE_2,SC.BOTH_INDICATOR,SCO.SALES_ORG,SCO.DIVISION,SCO.DIST_CHANNEL, SC.LAST_UPDATED_DATE,SC.CUSTOMER_FREIGHT_TYPE,"
				+ "SCO.CUSTOMER_NUM,SCO.MDD_PROD_ATTR1,SCO.PHR_PROD_ATTR2,SCO.CON_PROD_ATTR3,SCO.PARTIAL_DELIVERY,SPF.CUSTOMER_NUM,SPF.PARTNER_FUNC_TYPE,SPF.PARTNER_FUNC_CD "
				+ "FROM HYBRIS.STG_CUSTOMER AS SC INNER JOIN HYBRIS.STG_CUSTOMER_SORG AS SCO ON SC.CUSTOMER_NUM = SCO.CUSTOMER_NUM "
				+ "INNER JOIN HYBRIS.STG_PARTNER_FUNC AS SPF ON SCO.CUSTOMER_NUM = SPF.CUSTOMER_NUM ";

		final String upsertCustomerQuery = buildRSASQLQuery(queryForCustomerRecords, jobModel);

		List<JnJLaCustomerDTO> listOfCustomers = new ArrayList<>();

		try
		{
			listOfCustomers = rsaDBConnector.getJdbcTemplate().queryForObject(upsertCustomerQuery, new CustomerRowMapper());
			LOGGER.info("total number of customer :: " + listOfCustomers.size());
		}
		catch (final Exception ex)
		{
			LOGGER.info("Dao exception" + ex);
		}
		return listOfCustomers;



	}

	private String buildRSASQLQuery(final String upsertCustomerQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "buildRSASQLQuery";
		String finalQuery;
		final String isTestSetupRequired = Config.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.IS_TEST_SETUP_REQUIRED);
		if (Boolean.parseBoolean(isTestSetupRequired))
		{
			final String testDataCriteria = JnjLaCoreUtil
					.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.UpsertCustomer.TEST_DATA);

			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
					"Customers Set up for testing: " + testDataCriteria, JnjRSAProductDaoImpl.class);

			finalQuery = upsertCustomerQuery + " WHERE SC.CUSTOMER_NUM IN (" + testDataCriteria + ")";
			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, "Final Query formed: " + finalQuery,
					JnjRSAOrderDaoImpl.class);

			return finalQuery;
		}

		String queryConstraints = interfaceOperationArchUtility.builRSAQueryLastUpdatedDate(jobModel, "WHERE SC.LAST_UPDATED_DATE");
		// Verify whether there is a initial date in order to set a end date.
		if (!queryConstraints.isEmpty())
		{
			queryConstraints = interfaceOperationArchUtility.buildRSAQueryLastUpdatedEndDate(queryConstraints,
					Jnjlab2bcoreConstants.UpsertCustomer.CUSTOMER_LAST_UPDATED_END_DATE, "SC.LAST_UPDATED_DATE");
		}

		finalQuery = upsertCustomerQuery + queryConstraints;

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, "Final Query formed: " + finalQuery,
				JnjRSAProductDaoImpl.class);

		return finalQuery;
	}

}
