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

import com.jnj.core.jalo.JnjIntegrationRSACronJob;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjTranslationDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.dao.JnjLoadTranslationDao;
import com.jnj.la.core.util.JnJLaCronjobUtil;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 *
 */
public class JnjLoadTranslationDaoImpl implements JnjLoadTranslationDao
{
	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjLoadTranslationDaoImpl.class);

	/** The Constant LOAD_TRANSLATION_INTERFACE_METHOD_GET_CONTRACT. */
	private static final String LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA = "getTranslationDataFetch()";

	private static final String SELECT_QUERY = "SELECT CUSTOMER_NUM,MATERIAL_NUM,CUST_MATERIAL_NUM,BASE_UOM,CUST_UOM,DEN_CONVERSION,NUM_CONVERSION,ROUNDING_PROFILE,LAST_UPDATED_DATE FROM HYBRIS.STG_LOAD_TRANSLATION";

	private static final String ORDER_BY = " ORDER BY LAST_UPDATED_DATE";

	private JNJRSADBConnector rsaDBConnector;
	
	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;
	
	protected ConfigurationService configurationService;
	
	protected JnJLaCronjobUtil jnjLaCronjobUtil;
	

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

	public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}
	
	public void setJnjLaCronjobUtil(final JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}


	@Override
	public List<JnjTranslationDTO> getTranslationDataFetch(final JnjIntegrationRSACronJobModel cronJobModel)
			throws NumberFormatException, BusinessException
	{

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, LOAD_TRANSLATION_INTERFACE_METHOD_GET_LOAD_DATA,
				Logging.BEGIN_OF_METHOD, JnjLoadTranslationDaoImpl.class);

		LOGGER.info("IN LOAD TRANSLATION DAO ############");

		final String sql1 = SELECT_QUERY;

		final String sql2 = buildRSASQLQuery(sql1, cronJobModel);

		final String finalSql = sql2 + ORDER_BY;
		
		
		List<JnjTranslationDTO> translationRecords=null;
		
		Map<String,List<?>> resultMap= new HashMap<>();
		
		int retryAttempt = 0;
		int connectionWaitPeriod = 0;

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
			
		} catch (final NumberFormatException e) {
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, Logging.BEGIN_OF_METHOD,
					"Can not convert string to integer: " + e.getMessage(), e, JnjLoadTranslationDaoImpl.class);
		}
		
		try
		{
			for (int count = 0; count < retryAttempt; count++) {
				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				
				final boolean reconnectDB = jnjLaCronjobUtil.getResultsFromDB(finalSql, resultMap,JnjTranslationDTO.class);
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
		
		
		LOGGER.info("List of translation records: " +resultMap.get(JnjTranslationDTO.class.getSimpleName()));
		
		if(resultMap.get(JnjTranslationDTO.class.getSimpleName()) instanceof List<?>){
			translationRecords=(List<JnjTranslationDTO>)resultMap.get(JnjTranslationDTO.class.getSimpleName());
		}
		
		if(translationRecords!=null)
		{
			if (translationRecords.isEmpty())
			{
				LOGGER.info("No Updated Data returned from view :STG_LOAD_TRANSLATION#####");
			}


			for (int i = 0; i < translationRecords.size(); i++)
			{

				LOGGER.info("Values coming from RSA STG_TRANSLATION View" + "----BASE_UOM---" + translationRecords.get(i).getBaseUom()
						+ ",CUSTOMER_MATERIAL_NUM----" + translationRecords.get(i).getCustMaterialNum() + ",CUSTOMER_NUM----"
						+ translationRecords.get(i).getCustomerNum() + ",CUST_UOM----" + translationRecords.get(i).getCustUom()
						+ ",DEN_CONVERSION----" + translationRecords.get(i).getDenConversion() + ",MATERIAL_NUM-----"
						+ translationRecords.get(i).getMaterialNum() + ",NUM_CONVERSION-----"
						+ translationRecords.get(i).getNumConversion() + ",ROUNDING_PROFILE---"
						+ translationRecords.get(i).getRoundingProfile() + ",LAST_UPDATED_DATE-----"
						+ translationRecords.get(i).getLastUpdatedDate());

			}
		}

		return translationRecords;
	}


	private String buildRSASQLQuery(final String query, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String testDataCriteria = JnjLaCoreUtil
				.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.UpsertCustomer.TEST_DATA);

		final String isTestSetupRequired = Config.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.IS_TEST_SETUP_REQUIRED);

		LOGGER.info("isTestSetupRequired :: " + Boolean.parseBoolean(isTestSetupRequired));

		final String lastLogDateTime = interfaceOperationArchUtility.getLastSuccesfulStartTimeForJob(jobModel);

		LOGGER.info("Last Succesful record process time  ::: " + lastLogDateTime);

		String appendedQuery = query;

		if (lastLogDateTime != null)
		{
			appendedQuery = query + " where LAST_UPDATED_DATE > '" + lastLogDateTime + "'";
		}

		LOGGER.info("isTestSetupRequired :: " + Boolean.parseBoolean(isTestSetupRequired));

		if (isTestSetupRequired != null && Boolean.parseBoolean(isTestSetupRequired) == true && lastLogDateTime != null)
		{
			LOGGER.info("isTestSetupRequired :: ");
			appendedQuery = appendedQuery + " AND CUSTOMER_NUM IN " + Jnjlab2bcoreConstants.OPEN_BRACKET + testDataCriteria
					+ Jnjlab2bcoreConstants.CLOSE_BRACKET;
		}


		if (lastLogDateTime == null && isTestSetupRequired != null && Boolean.parseBoolean(isTestSetupRequired) == true)
		{
			LOGGER.info("isTestSetupRequired :: ");
			appendedQuery = appendedQuery + " where CUSTOMER_NUM IN " + Jnjlab2bcoreConstants.OPEN_BRACKET + testDataCriteria
					+ Jnjlab2bcoreConstants.CLOSE_BRACKET;
		}


		LOGGER.info("Final Query formed ::: " + appendedQuery);
		return appendedQuery;
	}


}
