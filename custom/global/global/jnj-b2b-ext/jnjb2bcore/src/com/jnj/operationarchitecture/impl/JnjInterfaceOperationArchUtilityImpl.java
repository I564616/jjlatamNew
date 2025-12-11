/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.operationarchitecture.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;
import com.jnj.core.services.email.notification.JnjEmailNotificationService;
import com.jnj.core.services.synchronizeOrders.JnjSAPOrdersService;
import com.jnj.exceptions.BusinessException;


/**
 * Updates the <code>JnjWriteOperationDashboard</code> for a failed/successful XMl being parsed, with parameterized
 * details received.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjInterfaceOperationArchUtilityImpl implements JnjInterfaceOperationArchUtility
{

	/**
	 * Constant LOGGER.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjInterfaceOperationArchUtilityImpl.class);

	/**
	 * Constant for updateReadDashboard() method.
	 */
	protected static final String READ_DASHBOARD = "updateReadDashboard()";

	/**
	 * Constant for updateWriteDashboard() method.
	 */
	protected static final String WRITE_DASHBOARD = "updateWriteDashboard()";

	/**
	 * Constant for removeIntermediaryTableRecords() method.
	 */
	protected static final String REMOVE_INVALID_RECORDS = "removeIntermediaryTableRecords()";

	/**
	 * Constant for cleanProcessedRecords() method name.
	 */
	protected static final String CLEAN_PROCESSED_RECORDS_METHOD = "cleanProcessedRecords()";

	protected static final String LOAD_INTERFACE_OPERATION_ARCHITECTURE = "Load Interface Operation Architecture";
	/**
	 * Constant for None Message.
	 */
	protected static final String NO_ERROR_MESSAGE = "None";

	/**
	 * Instance of <code>ModelService</code>
	 */
	private ModelService modelService;


	/** The Jnj product service. */
	@Autowired
	protected JnJProductService jnjProductService;

	/** The jn j customer data service. */
	@Autowired
	protected JnJCustomerDataService jnJCustomerDataService;

	@Autowired
	JnjSalesOrgCustService jnJSalesOrgCustService;

	/**
	 * Instance of <code>JnjEmailNotificationService</code>
	 */
	@Autowired
	protected JnjEmailNotificationService jnjEmailNotificationService;

	@Autowired
	protected JnjInvoiceService jnjInvoiceService;



	/**
	 * Instance of <code>JnjSAPOrdersService</code>
	 */
	@Autowired
	protected JnjSAPOrdersService ordersService;

	/**
	 * Instance of <code>JnjCustomerEligibilityService</code>
	 */
	@Autowired
	protected JnjCustomerEligibilityService customerEligibilityService;

	
	public JnJProductService getJnjProductService() {
		return jnjProductService;
	}

	public JnJCustomerDataService getJnJCustomerDataService() {
		return jnJCustomerDataService;
	}

	public JnjSalesOrgCustService getJnJSalesOrgCustService() {
		return jnJSalesOrgCustService;
	}

	public JnjEmailNotificationService getJnjEmailNotificationService() {
		return jnjEmailNotificationService;
	}

	public JnjInvoiceService getJnjInvoiceService() {
		return jnjInvoiceService;
	}

	public JnjSAPOrdersService getOrdersService() {
		return ordersService;
	}

	public JnjCustomerEligibilityService getCustomerEligibilityService() {
		return customerEligibilityService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateReadDashboard(final String interfaceName, final String fileName, final String status,
			final boolean isSuccess, final String errorMessage)
	{
		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, READ_DASHBOARD, Logging.BEGIN_OF_METHOD);

		final JnjReadOperationDashboardModel newReadDashboardEntry = modelService.create(JnjReadOperationDashboardModel.class);
		newReadDashboardEntry.setInterfaceName(interfaceName);
		newReadDashboardEntry.setFileName(fileName);
		newReadDashboardEntry.setStatus(isSuccess ? RecordStatus.SUCCESS.toString() : RecordStatus.ERROR.toString());
		newReadDashboardEntry.setErrorMessage(isSuccess ? NO_ERROR_MESSAGE : errorMessage);
		newReadDashboardEntry.setDate(new Date());
		newReadDashboardEntry.setEmailNotificationSent(Boolean.FALSE);
		saveDashboard(newReadDashboardEntry);

		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, READ_DASHBOARD, Logging.END_OF_METHOD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateWriteDashboard(final String interfaceName, final String fileName, final String idocNumber,
			final String errorMessage)
	{
		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, WRITE_DASHBOARD, Logging.BEGIN_OF_METHOD);
		final JnjWriteOperationDashboardModel newWriteDashboardEntry = modelService.create(JnjWriteOperationDashboardModel.class);
		newWriteDashboardEntry.setInterfaceName(interfaceName);
		newWriteDashboardEntry.setFileName(fileName);
		newWriteDashboardEntry.setIdocNumber(idocNumber);
		newWriteDashboardEntry.setDate(new Date());
		newWriteDashboardEntry.setErrorMessage(errorMessage);
		newWriteDashboardEntry.setEmailNotificationSent(Boolean.FALSE);
		final boolean dashboardUpdated = saveDashboard(newWriteDashboardEntry);

		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, WRITE_DASHBOARD, Logging.END_OF_METHOD);
		return dashboardUpdated;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeIntermediaryTableRecords(Collection<? extends ItemModel> invalidRecords, final String interfaceName)
	{
		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, REMOVE_INVALID_RECORDS, Logging.BEGIN_OF_METHOD);
		final Collection<ItemModel> invalidRecordsEmptyCollection = new ArrayList<ItemModel>();
		try
		{
			modelService.removeAll(invalidRecords);
			LOGGER.info(interfaceName + "SUCCESSFULL Deletion/Removal of records from Intermediary table.");
		}
		catch (final ModelRemovalException e)
		{
			LOGGER.error(interfaceName + "Deletion/Removal of records from Intermediary table has cuased an error: "
					+ e.getMessage());
		}
		invalidRecords = invalidRecordsEmptyCollection;
		//logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, REMOVE_INVALID_RECORDS, Logging.END_OF_METHOD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveDashboard(final ItemModel dashboard)
	{
		try
		{
			modelService.save(dashboard);
		}
		catch (final ModelSavingException e)
		{
			if (dashboard instanceof JnjReadOperationDashboardModel)
			{
				LOGGER.error("Saving READ OPERATION DASHBOARD failed. Error Message: " + e.getMessage());
			}
			else
			{
				LOGGER.error("Saving WRITE OPERATION DASHBOARD failed. Error Message: " + e.getMessage());
			}
			return true;
		}
		return true;
	}


	/**
	 * Utility method used for logging entry into / exit from any method in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cleanIntermediaryTableRecords() throws BusinessException
	{
		final Collection<ItemModel> intermediaryTableRecords = new ArrayList<ItemModel>();


		removeIntermediaryTableRecords(intermediaryTableRecords, Logging.LOAD_CONTRACTS_INTERFACE);

		removeIntermediaryTableRecords(intermediaryTableRecords, Logging.LOAD_CONTRACTS_INTERFACE);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeIntermediaryTableRecords(Collection<? extends ItemModel> invalidRecords, final boolean errorRecords)
	{
		logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, REMOVE_INVALID_RECORDS, Logging.BEGIN_OF_METHOD);
		final Collection<ItemModel> invalidRecordsEmptyCollection = new ArrayList<ItemModel>();
		try
		{
			modelService.removeAll(invalidRecords);
			LOGGER.info("SUCCESSFULL Deletion/Removal of " + (errorRecords ? "Error" : "Success")
					+ " records from Intermediary table.");
		}
		catch (final ModelRemovalException e)
		{
			LOGGER.error("Deletion/Removal of records from Intermediary table has cuased an error: " + e.getMessage());
		}
		invalidRecords = invalidRecordsEmptyCollection;
		logMethodStartOrEnd(Logging.INTERFACE_OPERATION_ARCHITECTURE_UTIL, REMOVE_INVALID_RECORDS, Logging.END_OF_METHOD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendEmailNotification()
	{
		jnjEmailNotificationService.sendEmailNotification();
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
