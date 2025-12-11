/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.util;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.operationarchitecture.impl.DefaultJnjInterfaceOperationArchUtility;


/**
 * Updates the <code>JnjWriteOperationDashboard</code> for a failed/successful XMl being parsed, with parameterized
 * details received.
 *
 * @author mpamda3
 * @version 1.0
 */
public class DefaultJnjLaInterfaceOperationArchUtility extends DefaultJnjInterfaceOperationArchUtility
		implements JnjLaInterfaceOperationArchUtility
{
	/**
	 * Constant LOGGER.
	 */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjLaInterfaceOperationArchUtility.class);
	protected static final String BUILD_RSA_QUERY="builRSAdQueryLastUpdatedDate()";
	protected static final String AND_CONDITION=" AND ";

	/**
	 * Constant for DATE FORMAT.
	 */
	protected static final String RSA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	protected ModelService modelService;

	private static final String LATAM_INTERFACE_OPERATION = "Jnj Latam Interface Operation Utility";

	@Override
	public String getLastSuccesfulStartTimeForJob(final JnjIntegrationRSACronJobModel arg0)
	{
		Date returnDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		String retString = null;
		try
		{
			if (arg0.getLastSuccessFulRecordProcessTime() == null)
			{
				return retString;
			}
			returnDate = arg0.getLastSuccessFulRecordProcessTime();
			retString = sdf.format(returnDate);
		}
		catch (final Exception e)
		{
			LOGGER.error(e);
		}
		return retString;
	}

	@Override
	public void setLastSuccesfulStartTimeForJob(final String lastUpdatedate, final JnjIntegrationRSACronJobModel arg0)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		try
		{
			arg0.setLastSuccessFulRecordProcessTime(sdf.parse(lastUpdatedate));
			modelService.save(arg0);
		}
		catch (final Exception e)
		{
			LOGGER.error(e);
		}
	}

	@Override
	public String builRSAQueryLastUpdatedDate(final JnjIntegrationRSACronJobModel jobModel, final String queryFragmentAndColumnName)
	{
		String queryConstraints = " ";
		final String lastLogDateTime = getLastSuccesfulStartTimeForJob(jobModel);
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, BUILD_RSA_QUERY,
				"Last Successful record process time  :SATRT: " + lastLogDateTime, DefaultJnjLaInterfaceOperationArchUtility.class);
		if (lastLogDateTime != null)
		{
			queryConstraints += queryFragmentAndColumnName + " > '" + lastLogDateTime + "'";
		}
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, BUILD_RSA_QUERY,
				"Last Successful record process time  :END: " + lastLogDateTime, DefaultJnjLaInterfaceOperationArchUtility.class);
		return queryConstraints;
	}
	
	@Override
	public String buildRSAQueryOrderLastUpdatedDate(final JnjIntegrationRSACronJobModel jobModel, final String queryFragmentAndColumnName)
	{
		String queryConstraints = " ";
		final String lastLogDateTime = getLastSuccesfulStartTimeForJob(jobModel);
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, BUILD_RSA_QUERY,
				"Last Successful record process time  ::: " + lastLogDateTime, DefaultJnjLaInterfaceOperationArchUtility.class);
		if (lastLogDateTime != null)
		{
			queryConstraints += queryFragmentAndColumnName + " > '" + lastLogDateTime + "' OR OL.LAST_UPDATED_DATE > '"+lastLogDateTime+"')";
		}
		return queryConstraints;
	}

	@Override
	public String buildRSAQueryLastUpdatedDateForProduct(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel jobModel, final String queryFragmentAndColumnName)
	{
		String queryConstraints = " ";
		final String lastLogDateTime = getLastSuccesfulStartTimeForProductsJob(lowerDate);
		final String upperDateTime = getUpperDate(upperDate);
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, "buildRSAQueryLastUpdatedDateForProduct()",
				"Last Successful record process time  :BEGIN: " + lastLogDateTime, DefaultJnjLaInterfaceOperationArchUtility.class);
		if (lastLogDateTime != null)
		{
			queryConstraints += queryFragmentAndColumnName + " > '" + lastLogDateTime + "'" +  " AND LAST_UPDATED_DATE" + " <='" + upperDateTime + "'";
		}
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, "buildRSAQueryLastUpdatedDateForProduct()",
				"Last Successful record process time  :END: " + lastLogDateTime, DefaultJnjLaInterfaceOperationArchUtility.class);
		return queryConstraints;
	}

	private String getUpperDate(Date upperDate) {
		Date returnDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		String retString = null;
		try
		{
			if (upperDate == null)
			{
				return retString;
			}
			returnDate = upperDate;
			retString = sdf.format(returnDate);
		}
		catch (final Exception e)
		{
			LOGGER.error("Error in formatting the Date into String: " + e);
		}
		return retString;
	}

	@Override
	public String buildRSAQueryLastUpdatedEndDate(final String queryConstraints, final String lastUpdatedEndDateProperty,
			final String columnName)
	{
		final String methodName = "buildRSAQueryLastUpdatedEndDate()";
		final String lastUpdatedEndDate = Config.getParameter(lastUpdatedEndDateProperty);

		if (Strings.isNullOrEmpty(lastUpdatedEndDate))
		{
			JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, methodName,
					"Last Successful record process time  ::: " + lastUpdatedEndDate, DefaultJnjLaInterfaceOperationArchUtility.class);
			return queryConstraints;
		}
		return queryConstraints + AND_CONDITION + columnName + " < '" + lastUpdatedEndDate + "'";
	}

	@Override
	public String buildRSAQueryOrderCreationDate(final String queryConstraints, final String columnName)
	{
		final String methodName = "buildRSAQueryOrderCreationDate()";
		final Calendar currentDate = Calendar.getInstance();
		final int days = Config.getInt(Jnjlab2bcoreConstants.Order.SYNC_ORDER_CREATION_DATE_FOR_ORDERS, 7);
		currentDate.add(Calendar.DAY_OF_MONTH, -days);
		final Date orderCreationMinimumDate = currentDate.getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		String orderCreationDateString = null;
		try
		{
			orderCreationDateString = sdf.format(orderCreationMinimumDate);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(LATAM_INTERFACE_OPERATION, methodName,
					"Error while formating the Order Creation date to the RSA format: " + RSA_DATE_FORMAT, exception,
					DefaultJnjLaInterfaceOperationArchUtility.class);
		}

		return queryConstraints + AND_CONDITION + columnName + " > '" + orderCreationDateString + "'";
	}

	@Override
	public String buildRSAQuerySalesOrgList(final String queryConstraints, final String salesOrgListProperty,
			final String columnName)
	{
		final String methodName = "buildRSAQuerySalesOrgList()";
		final String salesOrgsList = JnjLaCoreUtil.getCommaSeparatedValuesForQueryConditions(salesOrgListProperty);

		if (salesOrgsList == null || salesOrgsList.length() < 4) // Sales org ex.: 'BR01'. Length=4
		{
			JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, methodName,
					"No sales org list was specified in the query so all sales orgs will be considered.",
					DefaultJnjLaInterfaceOperationArchUtility.class);
			return queryConstraints;
		}
		return queryConstraints + AND_CONDITION + columnName + " IN (" + salesOrgsList + ")";
	}

	@Override
	public String buildRSAQueryForLastUpdatedDate(final Date lowerDate, final Date upperDate, final String queryFragmentAndColumnName)
	{
		final String methodName = "buildRSAQueryForLastUpdatedDate()";
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, methodName,
				"Last Successful record process time  :Start: " + lowerDate, DefaultJnjLaInterfaceOperationArchUtility.class);
		String queryConstraint = null;
		final String lastLogDateTime = getLastSuccessfulTimeForJob(lowerDate);
		final String upperDateTime = getLastSuccessfulTimeForJob(upperDate);
		if (lastLogDateTime != null)
		{
			queryConstraint = queryFragmentAndColumnName + " > '" + lastLogDateTime + "'" + queryFragmentAndColumnName + " <= '" + upperDateTime + "'";
		}
		JnjGTCoreUtil.logInfoMessage(LATAM_INTERFACE_OPERATION, methodName,
				"Last Successful record process time  :End: " + lowerDate, DefaultJnjLaInterfaceOperationArchUtility.class);
		return queryConstraint;
	}

	private String getLastSuccessfulTimeForJob(Date lastSuccessfulTime) {
		Date returnDate;
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		String retString = null;
		try
		{
			if (lastSuccessfulTime == null)
			{
				return retString;
			}
			returnDate = lastSuccessfulTime;
			retString = sdf.format(returnDate);
		}
		catch (final Exception e)
		{
			LOGGER.error("Error in formatting the last successful Date into String:: " + e);
		}
		return retString;
	}


	public String getLastSuccesfulStartTimeForProductsJob(final Date lowerDate)
	{
		Date returnDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		String retString = null;
		try
		{
			if (lowerDate == null)
			{
				return retString;
			}
			returnDate = lowerDate;
			retString = sdf.format(returnDate);
		}
		catch (final Exception e)
		{
			LOGGER.error("Error in formatting the lowerDate into String::: " + e);
		}
		return retString;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
