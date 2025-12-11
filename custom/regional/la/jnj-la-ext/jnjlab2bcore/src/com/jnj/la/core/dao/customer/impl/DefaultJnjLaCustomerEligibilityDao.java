/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.dao.customer.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.dao.customerEligibility.impl.DefaultJnjCustomerEligibilityDao;
import com.jnj.core.dto.JnjCustomerEligiblityDTO;
import com.jnj.la.core.dao.customer.JnjLaCustomerEligiblityDao;
import com.jnj.la.core.dto.JnjLaCustomerEligiblityDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.log4j.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import java.lang.IndexOutOfBoundsException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


/**
 * Customer Eligibility Interface Data Load - Dao layer class, responsible for carrying out all DB retrieval operations.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjLaCustomerEligibilityDao extends DefaultJnjCustomerEligibilityDao implements JnjLaCustomerEligiblityDao
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjLaCustomerEligibilityDao.class);
	private static final String CONNECTION_TIMEOUT = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection timed out";
	private static final String CONNECTION_RESET = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection reset";
	private JNJRSADBConnector rsaDBConnector;

	protected ConfigurationService configurationService;
	
	
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public JNJRSADBConnector getRsaDBConnector()
	{
		return rsaDBConnector;
	}
	
	

	public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector)
	{
		this.rsaDBConnector = rsaDBConnector;
	}


	@Override
	public Set<JnjCustomerEligiblityDTO> getCustomerEligibilityRecords(final String customerEligibilityQuery)
	{

		SqlRowSet set=null;
		int retryAttempt = 0;
		int connectionWaitPeriod = 0;
		List<SqlRowSet> listOfResults  = new ArrayList<>();

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.RETRY_ATTEMPT));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));

		} catch (final NumberFormatException e) {
			LOGGER.info("Can not convert string to integer: "+e.getMessage() +" "+ e);
		}

		try
		{
			for (int count = 0; count < retryAttempt; count++) {
				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				final boolean reconnectDB = getResultsFromDB(customerEligibilityQuery, listOfResults);
				if(listOfResults != null)
				{
					LOGGER.info("ReconnectDB value: "+reconnectDB);
					LOGGER.info("Contracts results: "+listOfResults.get(0));
					set = listOfResults.get(0);
					if (!reconnectDB) {
						break;
					}
				}
			}
		}
		catch(InterruptedException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		catch(IndexOutOfBoundsException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		final Set<JnjCustomerEligiblityDTO> returnSet = new HashSet<JnjCustomerEligiblityDTO>();
		JnjLaCustomerEligiblityDTO custDTO = null;
		if(set != null){
			while (set.next())
			{
				custDTO = new JnjLaCustomerEligiblityDTO();
				custDTO.setB2bAccountUnitId(set.getString(1));
				custDTO.setStatus(set.getString(2));
				custDTO.setFirstLevelCategoryId(set.getString(3));
				custDTO.setSecondLevelCategoryId(set.getString(4));
				custDTO.setThirdLevelCategoryId(set.getString(5));
				custDTO.setLastUpdateDate(set.getString(6));
				returnSet.add(custDTO);
			}
		}
		
		return returnSet;
	}

	public boolean getResultsFromDB(final String customerEligibilityQuery, List<SqlRowSet> listOfResults) {
		final String methodName = "getresultsFromDB()";
		boolean reconnectDB = false;
		try {

			Date timeBeforeConnection = new Date();
			final JdbcTemplate jdbcTemplate= rsaDBConnector.getJdbcTemplate();
			Date timeAfterConnection = new Date();
			final long timeDifference=timeAfterConnection.getTime()-timeBeforeConnection.getTime();

			JnjGTCoreUtil.logInfoMessage("Time in getting connection",methodName,"Time difference in milli seconds"+timeDifference,DefaultJnjLaCustomerEligibilityDao.class);
			if(jdbcTemplate!=null){
				final SqlRowSet set=jdbcTemplate.queryForRowSet(customerEligibilityQuery);
				listOfResults.add(set);
				LOGGER.info("Contracts results from DB: "+set);
			}
			else{
				LOGGER.info("Error while getting DB coneection: "+jdbcTemplate);
				reconnectDB=true;
			}
		}
		catch (final Exception e) {
			if (e.getMessage().contains(CONNECTION_TIMEOUT) || e.getMessage().contains(CONNECTION_RESET)) {
				LOGGER.error("Can not connect to RSA due to SQLServerException: " + e.getMessage() + e);
				reconnectDB = true;
			} else {
				LOGGER.error("Can not connect to RSA view to perform query. Message:  " + e.getMessage() + e);
				reconnectDB = false;
			}
		}
		return reconnectDB;
	}
}