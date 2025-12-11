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
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.order.impl.JnjRSAOrderDaoImpl;
import com.jnj.la.core.dao.product.impl.JnjRSAProductDaoImpl;
import com.jnj.la.core.dto.JnjContractDTO;
import com.jnj.la.core.dto.JnjContractProductDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.JnjContractDataLoad;
import com.jnj.la.dataload.dao.JnjContractDataLoadDao;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.la.core.util.JnJLaCronjobUtil;	
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.math.BigDecimal;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.log4j.Logger;
import de.hybris.platform.util.Config;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;


public class JnjContractDataLoadDaoImpl implements JnjContractDataLoadDao
{

	private JNJRSADBConnector rsaDBConnector;
	
	protected JnJLaCronjobUtil jnjLaCronjobUtil;
	
	protected ConfigurationService configurationService;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	
	public JNJRSADBConnector getRsaDBConnector() {
		return rsaDBConnector;
	}

	public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector) {
		this.rsaDBConnector = rsaDBConnector;
	}


	public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}

	public void setJnjLaCronjobUtil(final JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}


	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	private static final String LOAD_CONTRACTS_INTERFACE_METHOD_FETCH_CONTRACT = "fetchLoadContractRecords()";

	private static String SQL_FETCH_CONTRACT_RECORDS = "SELECT SCH.CUSTOMER, SCH.CONTRACT_NUM_ECC, SCH.TENDER_NUM, SCH.START_DATE, SCH.END_DATE, SCH.STATUS AS HEADER_STATUS, SCH.ORDER_REASON, SCH.DOC_TYPE, SCH.iNDIRECT_CUST, "
			+ "SCH.TOTAL_AMT, SCH.BALANCE_AMT, SCH.LAST_UPDATED_DATE, SCL.CONTRACT_NUM_ECC, SCL.MATERIAL_NUM, SCL.STATUS AS LINE_STATUS, SCL.TENDER_LINE_NUM, SCL.NET_PRICE, SCL.LINE_NUM,"
			+ " SCL.CONTRACT_QTY, SCL.BALANCE_QTY, SCL.CONTRACT_UOM FROM HYBRIS.STG_CONTRACT_HEADER AS SCH INNER JOIN HYBRIS.STG_CONTRACT_LINE AS SCL ON SCH.CONTRACT_NUM_ECC=SCL.CONTRACT_NUM_ECC "
			+ "WHERE SCH.DOC_TYPE IN ";

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjContractDataLoadDaoImpl.class);
	private static final String CONNECTION_TIMEOUT = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection timed out";
	private static final String CONNECTION_RESET = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection reset";

	@Override
	public List<JnjContractDTO> fetchLoadContractRecords(final JnjIntegrationRSACronJobModel cronjobmodel)
	{

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_FETCH_CONTRACT,
				Logging.BEGIN_OF_METHOD, JnjContractDataLoadDaoImpl.class);

		final Map<String, JnjContractDTO> contractNumberAndContractDTOMap = new HashMap<>();
		final Map<String, List<JnjContractProductDTO>> contractNumberAndContractProductDTOMap = new HashMap<>();
		final String validDocTypes = JnjLaCoreUtil
				.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.LoadContract.LOAD_CONTRACT_VALID_DOCUMENT_TYPE);
		String FINAL_SQL_FETCH_CONTRACT_RECORDS = SQL_FETCH_CONTRACT_RECORDS + Jnjlab2bcoreConstants.OPEN_BRACKET + validDocTypes
				+ Jnjlab2bcoreConstants.CLOSE_BRACKET;

		FINAL_SQL_FETCH_CONTRACT_RECORDS = buildRSASQLQuery(FINAL_SQL_FETCH_CONTRACT_RECORDS, cronjobmodel);

		Map<String, List<?>> resultMap = new HashMap<>();
		List<Map<String, Object>> contractHeaderAndLineRecords = new ArrayList<>();

		int retryAttempt = 0;
		int connectionWaitPeriod = 0;

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));

		} catch (final NumberFormatException e) {
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, Logging.BEGIN_OF_METHOD,
					"Can not convert string to integer: " + e.getMessage(), e, JnjContractDataLoadDaoImpl.class);
		}

		try
		{
			for (int count = 0; count < retryAttempt; count++) {

				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				final boolean reconnectDB = getResultsFromDB(FINAL_SQL_FETCH_CONTRACT_RECORDS, contractHeaderAndLineRecords);
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

		LOGGER.info("List of contract records: " + contractHeaderAndLineRecords);

		if(contractHeaderAndLineRecords != null){
			if (contractHeaderAndLineRecords.isEmpty())
			{
				LOGGER.info("No Updated Data returned from view :STG_CONTRACT_LINE & STG_CONTRACT_HEADER #####");
			}

			for (final Map<String, Object> contractHeaderAndLineRecord : contractHeaderAndLineRecords)
			{
				LOGGER.info("row keys :: " + contractHeaderAndLineRecord.keySet());
				LOGGER.info("row values :: " + contractHeaderAndLineRecord.values());
				final JnjContractDTO contractDTO = new JnjContractDTO();
				contractDTO.setCustomerNum((String) contractHeaderAndLineRecord.get("CUSTOMER"));
				contractDTO.seteCCContractNum((String) contractHeaderAndLineRecord.get("CONTRACT_NUM_ECC"));
				contractDTO.setTenderNum((String) contractHeaderAndLineRecord.get("TENDER_NUM"));
				final Date startDate = (Date) contractHeaderAndLineRecord.get("START_DATE");
				if (startDate != null)
				{
					contractDTO.setStartDate(startDate.toString());
				}
				final Date endDate = (Date) contractHeaderAndLineRecord.get("END_DATE");
				if (endDate != null)
				{
					contractDTO.setEndDate(endDate.toString());
				}

				contractDTO.setStatus((String) contractHeaderAndLineRecord.get("HEADER_STATUS"));
				contractDTO.setContractOrderReason((String) contractHeaderAndLineRecord.get("ORDER_REASON"));
				contractDTO.setDocumentType((String) contractHeaderAndLineRecord.get("DOC_TYPE"));
				contractDTO.setIndirectCustomer((String) contractHeaderAndLineRecord.get("INDIRECT_CUST"));

				final BigDecimal totalAmount = (BigDecimal) contractHeaderAndLineRecord.get("TOTAL_AMT");
				if (totalAmount != null)
				{
					contractDTO.setTotalAmount(totalAmount.toString());
				}
				final BigDecimal balanceAmount = (BigDecimal) contractHeaderAndLineRecord.get("BALANCE_AMT");
				if (balanceAmount != null)
				{
					contractDTO.setBalanceAmount(balanceAmount.toString());
				}

				final Date lastUpdatedDate = (Date) contractHeaderAndLineRecord.get("LAST_UPDATED_DATE");
				if (lastUpdatedDate != null)
				{
					contractDTO.setLastUpdatedDate(lastUpdatedDate.toString());
				}
							
												
				contractNumberAndContractDTOMap.put(contractDTO.geteCCContractNum()+Jnjlab2bcoreConstants.HASH_SYMBOL+contractDTO.getCustomerNum(), contractDTO);
				
				final JnjContractProductDTO contractProductDTO = new JnjContractProductDTO();
				contractProductDTO.seteCCContractNum((String) contractHeaderAndLineRecord.get("CONTRACT_NUM_ECC"));
				contractProductDTO.setMaterialNo((String) contractHeaderAndLineRecord.get("MATERIAL_NUM"));
				contractProductDTO.setStatus((String) contractHeaderAndLineRecord.get("LINE_STATUS"));
				contractProductDTO.setTenderLineNum((String) contractHeaderAndLineRecord.get("TENDER_LINE_NUM"));
				final BigDecimal netPrice = (BigDecimal) contractHeaderAndLineRecord.get("NET_PRICE");
				if (netPrice != null)
				{
					contractProductDTO.setNetPrice(netPrice.toString());
				}
				final BigDecimal lineNum = (BigDecimal) contractHeaderAndLineRecord.get("LINE_NUM");
				if (lineNum != null)
				{
					contractProductDTO.setLineNum(lineNum.toString());
				}
				final BigDecimal contractQty = (BigDecimal) contractHeaderAndLineRecord.get("CONTRACT_QTY");
				if (contractQty != null)
				{
					contractProductDTO.setContractQty(contractQty.toString());
				}
				final BigDecimal contractBalanceQty = (BigDecimal) contractHeaderAndLineRecord.get("BALANCE_QTY");
				if (contractBalanceQty != null)
				{
					
					contractProductDTO.setContractBalanceQty(contractBalanceQty.toString());
				}
				contractProductDTO.setContractUnit((String) contractHeaderAndLineRecord.get("CONTRACT_UOM"));

				final List<JnjContractProductDTO> contractProductDTOList = new ArrayList<>();
				contractProductDTOList.add(contractProductDTO);

				if (!contractNumberAndContractProductDTOMap.containsKey(contractProductDTO.geteCCContractNum()))
				{
					contractNumberAndContractProductDTOMap.put(contractProductDTO.geteCCContractNum(), contractProductDTOList);
				}
				else
				{
					final List<JnjContractProductDTO> existingContractProductDTOList = contractNumberAndContractProductDTOMap
							.get(contractProductDTO.geteCCContractNum());
					final List<JnjContractProductDTO> finalContractProductDTOList = new ArrayList<>();
					for (JnjContractProductDTO contProduct : existingContractProductDTOList) {
						if (null != contProduct.getMaterialNo() && !contProduct.getMaterialNo().equals(contractProductDTO.getMaterialNo())){
							finalContractProductDTOList.add(contProduct);
						}
					}
					finalContractProductDTOList.add(contractProductDTO);
					contractNumberAndContractProductDTOMap.put(contractProductDTO.geteCCContractNum(), finalContractProductDTOList);
				}

			}
		}


		final List<JnjContractDTO> listOfContracts = new ArrayList<>();
		LOGGER.info("size of contractNumberAndContractDTOMap "+ contractNumberAndContractDTOMap.size());

		for (final Map.Entry<String, JnjContractDTO> entry : contractNumberAndContractDTOMap.entrySet())
		{
			final String contractKey = entry.getKey();			
			
			final JnjContractDTO contractDTO = contractNumberAndContractDTOMap.get(contractKey);
			final String contractNumber = (contractKey.split(Jnjlab2bcoreConstants.HASH_SYMBOL))[0];			
			final List<JnjContractProductDTO> contractProductDTOList = contractNumberAndContractProductDTOMap.get(contractNumber);
			contractDTO.setProductList(contractProductDTOList);
			listOfContracts.add(contractDTO);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_FETCH_CONTRACT,
				Logging.END_OF_METHOD, JnjContractDataLoadDaoImpl.class);
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_INTERFACE_METHOD_FETCH_CONTRACT,
				"Number Of Contracts :: " + listOfContracts.size(), JnjContractDataLoadDaoImpl.class);
		return listOfContracts;
	}

	
	public boolean getResultsFromDB(final String finalQuery, List<Map<String, Object>> contractHeaderAndLineRecords) {
		final String methodName = "getresultsFromDB()";
		boolean reconnectDB = false;
		try {

			Date timeBeforeConenction = new Date();
			final JdbcTemplate jdbcTemplate= rsaDBConnector.getJdbcTemplate();
			Date timeAfterConenction = new Date();
			final long timeDifference=timeAfterConenction.getTime()-timeBeforeConenction.getTime();

			JnjGTCoreUtil.logInfoMessage("Time in getting connection",methodName,"Time difference in milli seconds"+timeDifference,JnjContractDataLoadDaoImpl.class);
			if(jdbcTemplate!=null){
				
				final List<Map<String, Object>> contractsResults =jdbcTemplate.queryForList(finalQuery);
				contractHeaderAndLineRecords.addAll(contractsResults);
				LOGGER.info("List of contract records from DB: " + contractsResults);
				LOGGER.info("Contract list size "+ contractsResults.size());
			}
			else{
				LOGGER.info("Error while getting DB coneection: "+jdbcTemplate);
				reconnectDB = true;
			}
		}
		catch (final Exception e) {
			if (e.getMessage().contains(CONNECTION_TIMEOUT) || e.getMessage().contains(CONNECTION_RESET)) {
				LOGGER.error("Can not connect to RSA due to SQLServerException. : " + e.getMessage() + e);
				reconnectDB = true;
			} else {
				LOGGER.error("Can not connect to RSA view to perform query. Message:  " + e.getMessage() + e);
				reconnectDB = false;
			}
		}
		return reconnectDB;
	}

	private String buildRSASQLQuery(final String contractQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "buildRSASQLQuery";
		String finalQuery;
		final String isTestSetupRequired = Config
				.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.IS_TEST_SETUP_REQUIRED_FOR_CONTRACT);
		if (Boolean.parseBoolean(isTestSetupRequired))
		{
			final String testDataCriteria = JnjLaCoreUtil
					.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.UpsertCustomer.TEST_DATA_FOR_CONTRACT);

			JnjGTCoreUtil.logInfoMessage(Logging.FEED_CONTRACTS_JOB, methodName, "Contract Set up for testing: " + testDataCriteria,
					JnjRSAProductDaoImpl.class);

			finalQuery = contractQuery + " AND SCH.CONTRACT_NUM_ECC IN (" + testDataCriteria + ")";
			JnjGTCoreUtil.logInfoMessage(Logging.FEED_CONTRACTS_JOB, methodName, "Final Query formed: " + finalQuery,
					JnjRSAOrderDaoImpl.class);

			return finalQuery;
		}

		String queryConstraints = interfaceOperationArchUtility.builRSAQueryLastUpdatedDate(jobModel, "AND SCH.LAST_UPDATED_DATE");
		// Verify whether there is a initial date in order to set a end date.
		if (!queryConstraints.isEmpty())
		{
			queryConstraints = interfaceOperationArchUtility.buildRSAQueryLastUpdatedEndDate(queryConstraints,
					Jnjlab2bcoreConstants.Contract.CONTRACT_LAST_UPDATED_END_DATE, "SCH.LAST_UPDATED_DATE");
		}

		finalQuery = contractQuery + queryConstraints;

		JnjGTCoreUtil.logInfoMessage(Logging.FEED_CONTRACTS_JOB, methodName, "Final Query formed: " + finalQuery,
				JnjRSAProductDaoImpl.class);

		return finalQuery;
	}
}
