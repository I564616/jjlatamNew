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

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.dao.JnjDropshipmentDataLoadDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import com.jnj.la.core.util.JnJLaCronjobUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;

public class JnjDropshipmentDataLoadDaoImpl implements JnjDropshipmentDataLoadDao
{

	private static final Logger LOGGER = Logger.getLogger(JnjDropshipmentDataLoadDaoImpl.class);

	private JNJRSADBConnector rsaDBConnector;
	
	private JnJLaCronjobUtil jnjLaCronjobUtil;
	
	protected ConfigurationService configurationService;

	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}
	
	public void setJnjLaCronjobUtil(final JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	public JNJRSADBConnector getRsaDBConnector() {
		return rsaDBConnector;
	}

	public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector) {
		this.rsaDBConnector = rsaDBConnector;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public List<JnjDropshipmentDTO> fetchDropShipmentRecords(final JnjIntegrationRSACronJobModel jobModel)
	{
		final String METHOD_NAME = "fetchDropShipmentRecords()";

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN,
				Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME, Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime(),
				JnjDropshipmentDataLoadDaoImpl.class);

		List<JnjDropshipmentDTO> listOfDropshipment = null;

		final String dropShipmentQuery = "SELECT SDS.MATERIAL_NUM,SDS.PRINCIPAL,SDS.SHIPPER,SDS.SALES_ORG,SDS.DOC_TYPE,SDS.DEST_COUNTRY,SDS.OPERATION,SDS.SHIP_TO,SDS.SHIPPER_MD,"
				+ "SDS.SOLD_TO,SDS.ECARE_ID,SDS.LAST_UPDATED_DATE FROM HYBRIS.STG_DROP_SHIPMENT AS SDS";

		final String finalDropShipmentQuery = buildRSASQLQuery(dropShipmentQuery, jobModel);

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN, METHOD_NAME,
				"finalDropShipmentQuery :: " + finalDropShipmentQuery, JnjDropshipmentDataLoadDaoImpl.class);

		Map<String, List<?>> resultMap = new HashMap<>();

		int retryAttempt = 0;
		int connectionWaitPeriod = 0;

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));

		} catch (final NumberFormatException e) {
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, Logging.BEGIN_OF_METHOD,
					"Can not convert string to integer: " + e.getMessage(), e, JnjDropshipmentDataLoadDaoImpl.class);
		}

		try{
			for (int count = 0; count < retryAttempt; count++) {
				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				final boolean reconnectDB = jnjLaCronjobUtil.getResultsFromDB(finalDropShipmentQuery, resultMap,
						JnjDropshipmentDTO.class);
				LOGGER.info("ReconnectDB value: "+reconnectDB);

				if (!reconnectDB) {
					break;
				}

			}
		}
		catch(InterruptedException e)
		{
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("List of Dropshipment records: " + resultMap.get(JnjDropshipmentDTO.class.getSimpleName()));
		
		if (resultMap.get(JnjDropshipmentDTO.class.getSimpleName()) instanceof List<?>) {
			listOfDropshipment = (List<JnjDropshipmentDTO>) resultMap.get(JnjDropshipmentDTO.class.getSimpleName());
		}

		
		if(listOfDropshipment!=null){
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN, METHOD_NAME,
					"Total number of Drop Shipment records :: " + listOfDropshipment.size(), JnjDropshipmentDataLoadDaoImpl.class);
		}

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN,
				Logging.END_OF_METHOD + Logging.HYPHEN + METHOD_NAME, Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime(),
				JnjDropshipmentDataLoadDaoImpl.class);

		return listOfDropshipment;
	}

	private String buildRSASQLQuery(final String dropShipmentQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String METHOD_NAME = "buildRSASQLQuery()";

		final String lastLogDateTime = interfaceOperationArchUtility.getLastSuccesfulStartTimeForJob(jobModel);

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_DROP_SHIPMENT_JOB + Logging.HYPHEN, METHOD_NAME,
				"Last Successful record process time  ::: " + lastLogDateTime, JnjDropshipmentDataLoadDaoImpl.class);

		String query = dropShipmentQuery;

		if (lastLogDateTime != null)
		{
			query += " WHERE SDS.LAST_UPDATED_DATE > '" + lastLogDateTime + "'";
		}

		return query;
	}

}
