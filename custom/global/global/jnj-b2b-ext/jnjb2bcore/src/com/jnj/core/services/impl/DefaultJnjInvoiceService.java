/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjInvoiceDao;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;


/**
 * This class invokes the save method of ModelService class in order to save the model data into the database.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjInvoiceService implements JnjInvoiceService
{

	/** The model service. */
	@Autowired
	ModelService modelService;

	/** The flexible search service. */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	JnjInvoiceDao jnjInvoiceDao;

	@Autowired
	protected JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;
	
	

	public ModelService getModelService() {
		return modelService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjInvoiceDao getJnjInvoiceDao() {
		return jnjInvoiceDao;
	}

	public JnjInterfaceOperationArchUtility getJnjInterfaceOperationArchUtility() {
		return jnjInterfaceOperationArchUtility;
	}

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjInvoiceService.class);
	protected static final String METHOD_GET_INVOICE_BY_CODE = "getInvoicebyCode()";
	protected static final String METHOD_GET_INVOICE_BY_ORDER_CODE = "getInvoicebyOrderCode()";


	/**
	 * {@inheritDoc}
	 * 
	 * @throws BusinessException
	 */
	@Override
	public boolean saveInvoiceOrder(final JnJInvoiceOrderModel invoiceOrderModel) throws BusinessException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_SAVE_ORDER + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		boolean status;
		try
		{
			// Saving the JnJInvoiceOrderModel in database
			modelService.save(invoiceOrderModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "Invoice Orders have been saved in JnJInvoiceEntryModel."
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}
			status = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			//add order id in log
			LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "Exception occurred while saving Invoice Orders in Model:"
					+ modelSavingException.getMessage() + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			status = false;
			throw new BusinessException("Exception occurred while saving Invoice Orders in Model:"
					+ modelSavingException.getMessage(), MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_SAVE_ORDER + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveInvoiceEntry(final JnJInvoiceEntryModel invoiceEntryModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_SAVE_ENTRY + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		boolean status;
		try
		{
			// Saving the JnJInvoiceEntryModel in database
			modelService.save(invoiceEntryModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "Invoice Entries have been saved in JnJInvoiceEntryModel."
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}
			status = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOG.error((Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "Exception occurred while saving Invoice Entries in Model:" + modelSavingException
					.getMessage()) + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			status = false;
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_SAVE_ENTRY + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return status;
	}



	/**
	 * Setter for ModelService class.
	 * 
	 * @param modelService
	 *           the new model service
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * The methods returns with the Invoice Model for the given Invoice No.
	 * 
	 * @param invDocNo
	 *           the inv doc no
	 * @return the invoiceby code
	 */
	@Override
	public JnJInvoiceOrderModel getInvoicebyCode(final String invDocNo)
	{
		logMethodStartOrEnd(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_CODE, Logging.BEGIN_OF_METHOD);

		JnJInvoiceOrderModel jnJInvoiceOrderModel = null;
		final JnJInvoiceOrderModel tempJnJInvoiceOrderModel = modelService.create(JnJInvoiceOrderModel.class);
		tempJnJInvoiceOrderModel.setInvDocNo(invDocNo);

		try
		{
			jnJInvoiceOrderModel = flexibleSearchService.getModelByExample(tempJnJInvoiceOrderModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{

			LOG.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + METHOD_GET_INVOICE_BY_CODE + Logging.HYPHEN
					+ "Invoiec Model with invoiceDocNo: " + invDocNo + "Not Found. ModelNotFoundException occured. Returning Null."
					+ modelNotFoundException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOG.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + METHOD_GET_INVOICE_BY_CODE + Logging.HYPHEN
					+ "IllegalArgumentException Occured. Returning Null." + illegalArgumentException.getMessage());
		}

		logDebugMessage(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_CODE, "JnJInvoiceOrderModel with ID:" + invDocNo
				+ "found in Hybris. Returnig the same.");

		//logMethodStartOrEnd(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_CODE, Logging.END_OF_METHOD);
		return jnJInvoiceOrderModel;
	}

	/**
	 * This method returns a list of invoices for the given SAP Order No.
	 * 
	 * @param poNum
	 *           the po num
	 * @return the invoiceby order code
	 */
	@Override
	public List<JnJInvoiceOrderModel> getInvoicebyOrderCode(final String salesOrder)
	{
		logMethodStartOrEnd(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_ORDER_CODE, Logging.BEGIN_OF_METHOD);

		List<JnJInvoiceOrderModel> invoiceList = new ArrayList<JnJInvoiceOrderModel>();
		final JnJInvoiceOrderModel tempJnJInvoiceOrderModel = modelService.create(JnJInvoiceOrderModel.class);
		tempJnJInvoiceOrderModel.setSalesOrder(salesOrder);

		try
		{
			invoiceList = flexibleSearchService.getModelsByExample(tempJnJInvoiceOrderModel);
			logDebugMessage(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_ORDER_CODE, "JnJInvoiceOrderModels with Sales Order No."
					+ salesOrder + "found in Hybris. Returnig the same.");
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOG.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + METHOD_GET_INVOICE_BY_ORDER_CODE + Logging.HYPHEN
					+ "JnJInvoiceOrderModels with Sales Order No." + salesOrder + "NOT found in Hybris. Returnig NULL."
					+ modelNotFoundException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOG.error(Logging.INVOICE_SERVICE + Logging.HYPHEN + METHOD_GET_INVOICE_BY_CODE + Logging.HYPHEN
					+ "IllegalArgumentException Occured. Returning Null." + illegalArgumentException.getMessage());
		}

		logMethodStartOrEnd(Logging.INVOICE_SERVICE, METHOD_GET_INVOICE_BY_ORDER_CODE, Logging.END_OF_METHOD);
		return invoiceList;
	}



	@Override
	public List<JnJInvoiceOrderModel> getInvoiceOrderData()
	{
		return jnjInvoiceDao.getInvoiceOrderData();
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
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages in debug mode.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
