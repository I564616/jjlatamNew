/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.gt.service.common.impl;

import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjGTStgSerivce
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTStgSerivce.class);

	@Autowired
	private SetupImpexService setupImpexService;

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * Load feed data as per given impex file.
	 * 
	 * @param impexFilePath
	 *           the impex file path
	 */
	public void loadFeedData(final String impexFilePath, final String interfaceName)
	{
		boolean readSuccesful = true;
		String errorMessage = null;
		try
		{
			setupImpexService.importImpexFile(impexFilePath, true);
		}
		catch (final Exception e)
		{
			readSuccesful = false;
			errorMessage = e.getMessage();
		}

		jnjGTFeedService.updateReadDashboard(impexFilePath, "", readSuccesful, errorMessage, interfaceName);
	}

	/**
	 * This method is used to update read status while fetching data from STG Tables
	 * 
	 * @param tableNames
	 *           the table names
	 * @param newStatus
	 *           the status
	 * @return true, if successful
	 */
	public boolean updateReadStatusForStgTables(final List<String> tableNames, final int newStatus, final int existingStatus)
	{
		boolean result = false;
		Connection dbConnection = null;
		Statement statement = null;
		try
		{
			dbConnection = getDBConnection();
			if (null == dbConnection)
			{
				throw new SQLException("Not able to connect DataBase");
			}
			dbConnection.setAutoCommit(false);//commit transaction manually
			statement = dbConnection.createStatement();
			LOGGER.info("Updating STG Tables: " + tableNames + " for New Status: " + newStatus);
			for (final String tableName : tableNames)
			{
				statement.addBatch("UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus="
						+ existingStatus);
			}
			statement.executeBatch();
			dbConnection.commit();
			result = true;
			LOGGER.debug("MIGRATION STATUS SET SUCESSFULLY!");
		}
		catch (final SQLException e)
		{
			LOGGER.error("ERROR WHILE UPDTING STG RECS ", e);
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
				if (dbConnection != null)
				{
					dbConnection.close();
				}
			}
			catch (final SQLException e)
			{
				LOGGER.error("Not able to close connection or statement", e);
			}
		}
		return result;
	}
	
	
	public boolean updateReadStatusForStgTables(final List<String> tableNames, final int newStatus, final int existingStatus, Map<String,Object> values)
	{
		boolean result = false;
		Connection dbConnection = null;
		Statement statement = null;
		try
		{
			dbConnection = getDBConnection();
			if (null == dbConnection)
			{
				throw new SQLException("Not able to connect DataBase");
			}
			dbConnection.setAutoCommit(false);//commit transaction manually
			statement = dbConnection.createStatement();
			LOGGER.info("Updating STG Tables: " + tableNames + " for New Status: " + newStatus);
			String sqlStatement_STG_ORDER_HEADER = null;
			String sqlStatement_STG_ORDER_LINE = null;
			String sqlStatement_STG_ORDER_LINE_SCH = null;
			String sqlStatement_STG_SHIPMENT_TRACKING = null;
			for (final String tableName : tableNames)
			{
				if(tableName.equalsIgnoreCase("STG_ORDER_HEADER")) {
					sqlStatement_STG_ORDER_HEADER = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus=" + existingStatus + " AND "+ values.keySet().iterator().next()+" like '%"+values.values().iterator().next()+"%'";
					LOGGER.info("SQL Update statement for "+tableName+" :: "+sqlStatement_STG_ORDER_HEADER);
					//statement.addBatch(sqlStatement_STG_ORDER_HEADER);
				}
				else if(tableName.equalsIgnoreCase("STG_ORDER_LINE")) {
					sqlStatement_STG_ORDER_LINE = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus=" + existingStatus + " AND ORDER_NUM IN (SELECT ORDER_NUM FROM STG_ORDER_HEADER WHERE MigrationStatus=" + existingStatus +
							" AND "+ values.keySet().iterator().next()+" like '%"+values.values().iterator().next()+"%')";
					LOGGER.info("SQL Update statement for "+tableName+" :: "+sqlStatement_STG_ORDER_LINE);
					//statement.addBatch(sqlStatement_STG_ORDER_LINE);
				}
				else if(tableName.equalsIgnoreCase("STG_ORDER_LINE_SCH")) {
					sqlStatement_STG_ORDER_LINE_SCH = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus=" + existingStatus + " AND ORDER_NUM IN (SELECT ORDER_NUM FROM STG_ORDER_HEADER WHERE MigrationStatus=" + existingStatus +
							" AND "+ values.keySet().iterator().next()+" like '%"+values.values().iterator().next()+"%')";
					LOGGER.info("SQL Update statement for "+tableName+" :: "+sqlStatement_STG_ORDER_LINE_SCH);
					//statement.addBatch(sqlStatement_STG_ORDER_LINE_SCH);
				}
				else if(tableName.equalsIgnoreCase("STG_SHIPMENT_TRACKING")) {
					sqlStatement_STG_SHIPMENT_TRACKING = "UPDATE " + tableName + " SET MigrationStatus=" + newStatus + " WHERE MigrationStatus=" + existingStatus + " AND ORDER_NUM IN (SELECT ORDER_NUM FROM STG_ORDER_HEADER WHERE MigrationStatus=" + existingStatus +
							" AND "+ values.keySet().iterator().next()+" like '%"+values.values().iterator().next()+"%')";
					LOGGER.info("SQL Update statement for "+tableName+" :: "+sqlStatement_STG_SHIPMENT_TRACKING);
					//statement.addBatch(sqlStatement_STG_SHIPMENT_TRACKING);
				}
			}
			statement.addBatch(sqlStatement_STG_SHIPMENT_TRACKING);
			statement.addBatch(sqlStatement_STG_ORDER_LINE_SCH);
			statement.addBatch(sqlStatement_STG_ORDER_LINE);
			statement.addBatch(sqlStatement_STG_ORDER_HEADER);
			
			statement.executeBatch();
			dbConnection.commit();
			result = true;
			LOGGER.debug("MIGRATION STATUS SET SUCESSFULLY!");
		}
		catch (final SQLException e)
		{
			LOGGER.error("ERROR WHILE UPDTING STG RECS ", e);
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
				if (dbConnection != null)
				{
					dbConnection.close();
				}
			}
			catch (final SQLException e)
			{
				LOGGER.error("Not able to close connection or statement", e);
			}
		}
		return result;
	}
	

	private static Connection getDBConnection()
	{
		final String driverName = Config.getParameter("rsa.db.driver").replace("\"", StringUtils.EMPTY);
		final String connectionString = Config.getParameter("rsa.db.url").replace("\"", StringUtils.EMPTY);
		final String dbUserName = Config.getParameter("rsa.db.username").replace("\"", StringUtils.EMPTY);
		final String dbPassword = Config.getParameter("rsa.db.password").replace("\"", StringUtils.EMPTY);
		Connection connection = null;
		try
		{
			Class.forName(driverName);
		}
		catch (final ClassNotFoundException e)
		{
			return connection;
		}
		LOGGER.debug("Oracle JDBC Driver Registered!");

		try
		{
			connection = DriverManager.getConnection(connectionString, dbUserName, dbPassword);
		}
		catch (final SQLException e)
		{
			LOGGER.error("Connection Failed! Check output console", e);
			return connection;
		}
		return connection;
	}

	public boolean executeStgOrdersProcedures()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "updateReadStatusForStgTables()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean result = false;
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		try
		{
			dbConnection = getDBConnection();
			if (null == dbConnection)
			{
				throw new SQLException("Not able to connect DataBase");
			}
			dbConnection.setAutoCommit(false);//commit transaction manually
			callableStatement = dbConnection.prepareCall(Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_START_STRING
					+ Config.getParameter(Jnjgtb2binboundserviceConstants.Order.EXECUTE_ORDER_PROCEDURE_NAME)
					+ Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_END_STRING);
			callableStatement.executeQuery();
			dbConnection.commit();
			result = true;
			LOGGER.debug("MIGRATION STATUS SET SUCESSFULLY!");
		}
		catch (final SQLException e)
		{
			LOGGER.error("ERROR WHILE UPDTING STG RECS ", e);
		}
		finally
		{
			try
			{
				if (null != callableStatement)
				{
					callableStatement.close();
				}
				if (dbConnection != null)
				{
					dbConnection.close();
				}
			}
			catch (final SQLException e)
			{
				LOGGER.error("Not able to close connection or statement", e);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "updateReadStatusForStgTables()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}
	
	// Newly added method
		public boolean populateStageTempOrderTable()
		{

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "populateStageTempOrderTable()" + Logging.HYPHEN
						+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			boolean result = false;
			Connection dbConnection = null;
			CallableStatement callableStatement = null;
			try
			{
				dbConnection = getDBConnection();
				if (null == dbConnection)
				{
					throw new SQLException("Not able to connect DataBase");
				}
				dbConnection.setAutoCommit(false);//commit transaction manually
				callableStatement = dbConnection.prepareCall(Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_START_STRING
						+ Config.getParameter(Jnjgtb2binboundserviceConstants.Order.EXECUTE_ORDER_PROCEDURE_NAME_TUNED)
						+ Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_END_STRING);
				callableStatement.executeQuery();
				dbConnection.commit();
				result = true;
				LOGGER.debug("STG TEMP TABLE POPULATED SUCCESSFULLY!");
			}
			catch (final SQLException e)
			{
				LOGGER.error("ERROR WHILE POPULATING STG TEMP TABLE", e);
			}
			finally
			{
				try
				{
					if (null != callableStatement)
					{
						callableStatement.close();
					}
					if (dbConnection != null)
					{
						dbConnection.close();
					}
				}
				catch (final SQLException e)
				{
					LOGGER.error("Not able to close connection or statement", e);
				}
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "populateStageTempOrderTable()" + Logging.HYPHEN
						+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			return result;
		}

		
		// Newly added Shipment method
	public boolean populateStageTempShipmentTable()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "populateStageTempShipmentTable()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean result = false;
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		try
		{
			dbConnection = getDBConnection();
			if (null == dbConnection)
			{
				throw new SQLException("Not able to connect DataBase");
			}
			dbConnection.setAutoCommit(false);//commit transaction manually
			callableStatement = dbConnection.prepareCall(Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_START_STRING
					+ Config.getParameter(Jnjgtb2binboundserviceConstants.Order.EXECUTE_ORDER_PROCEDURE_NAME_TUNED)
					+ Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_END_STRING);
			callableStatement.executeQuery();
			dbConnection.commit();
			result = true;
			LOGGER.debug("STG TEMP TABLE POPULATED SUCCESSFULLY!");
		}
		catch (final SQLException e)
		{
			LOGGER.error("ERROR WHILE POPULATING STG TEMP TABLE", e);
		}
		finally
		{
			try
			{
				if (null != callableStatement)
				{
					callableStatement.close();
				}
				if (dbConnection != null)
				{
					dbConnection.close();
				}
			}
			catch (final SQLException e)
			{
				LOGGER.error("Not able to close connection or statement", e);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "populateStageTempShipmentTable()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

		
		
		public boolean updateReadStatusForStgTables()
		{

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "updateReadStatusForStgTables()" + Logging.HYPHEN
						+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			boolean result = false;
			Connection dbConnection = null;
			CallableStatement callableStatement = null;
			try
			{
				dbConnection = getDBConnection();
				if (null == dbConnection)
				{
					throw new SQLException("Not able to connect DataBase");
				}
				dbConnection.setAutoCommit(false);//commit transaction manually
				callableStatement = dbConnection.prepareCall(Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_START_STRING
						+ Config.getParameter(Jnjgtb2binboundserviceConstants.Order.EXECUTE_ORDER_PROCEDURE_NAME_TRUNCATE_TUNED)
						+ Jnjgtb2binboundserviceConstants.PROCEDURE_EXECUTION_END_STRING);
				callableStatement.executeQuery();
				dbConnection.commit();
				result = true;
				LOGGER.debug("STG TEMP TRUNCATED SUCCESSFULLY!");
			}
			catch (final SQLException e)
			{
				LOGGER.error("ERROR WHILE TRUNCATING STG TEMP TABLE", e);
			}
			finally
			{
				try
				{
					if (null != callableStatement)
					{
						callableStatement.close();
					}
					if (dbConnection != null)
					{
						dbConnection.close();
					}
				}
				catch (final SQLException e)
				{
					LOGGER.error("Not able to close connection or statement", e);
				}
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "updateReadStatusForStgTables()" + Logging.HYPHEN
						+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			return result;
		}
		
		public class StgToIntInboundImpexThread implements Runnable
		{

			private final String tenant;
			private final String impexFilePath;
			
			public StgToIntInboundImpexThread(final String tenant, final String impexFilePath)
			{
				super();
				this.impexFilePath = impexFilePath;
				this.tenant = tenant;
			}

			@Override
			public void run()
			{
				Registry.setCurrentTenant(Registry.getTenantByID(tenant));
				try
				{
					setupImpexService.importImpexFile(impexFilePath, true,true);
				}
				finally
				{
					Registry.unsetCurrentTenant();
				}
			}
		}
		
		/**
		 * Load feed data as per given impex file.
		 *
		 * @param impexFilePath
		 *           the impex file path
		 * @param interfaceName
		 * @param orderTypes
		 *           the list of order types	                     
		 */
		public void loadFeedData(final List<String> impexFilesPath, final String interfaceName, final List<String> orderTypes)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "loadFeedData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
						+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
			}

			boolean readSuccesful = true;
			String errorMessage = null;
			try
			{
				
				final List<Thread> impexThreads = new ArrayList<Thread>();
						
				for (final String impexFile : impexFilesPath)
				{
					final Thread stgToIntInboundImpexThread = new Thread(new StgToIntInboundImpexThread(Registry.getCurrentTenant().getTenantID(), impexFile));
					stgToIntInboundImpexThread.start();
					impexThreads.add(stgToIntInboundImpexThread);
				}
				for (final Thread stgToIntInboundImpexThread : impexThreads)
				{
					stgToIntInboundImpexThread.join();
				}
				

			}
			catch (final Exception e)
			{
				readSuccesful = false;
				errorMessage = e.getMessage();
			}

			
			for (final String impexFile : impexFilesPath)
			{
				jnjGTFeedService.updateReadDashboard(impexFile, "", readSuccesful, errorMessage, interfaceName);
			}
			
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "loadFeedData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
						+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}

		}

}
