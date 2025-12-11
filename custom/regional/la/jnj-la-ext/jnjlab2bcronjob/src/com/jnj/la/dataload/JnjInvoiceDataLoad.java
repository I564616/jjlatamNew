/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.impl.JnjRSAOrderDaoImpl;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.mapper.JnjInvoiceLoadMapper;


/**
 * This class reads the xml file from the IN folder and stores all the values to the DTO class. It then invokes the
 * mapper class to store these values in the model.
 *
 * @author Manoj.K.Panda
 *
 */
public class JnjInvoiceDataLoad
{
	private static final Logger LOG = Logger.getLogger(JnjInvoiceDataLoad.class);

	@Autowired
	private JnjInvoiceLoadMapper jnjInvoiceLoadMapper;

	@Autowired
	private JnjLaInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	private static final String SQL_QUERY_SELECT_PART = "SELECT A.INVOICE_NUM, A.BILLING_TYPE, A.NET_VALUE, A.PAYER_CUST_NUM, A.SOLD_TO_CUST_NUM, A.PO_NUM, "
			+ " A.MODEL, A.SERIES, A.NF_NUMBER, A.DOC_NUMBER, A.BILLING_DT, A.SHIP_TO_COUNTRY, A.YEAR_DOC_DATE, A.MONTH_DOC_DATE, A.CNPJ_SOLD_TO, A.CHECK_DIGIT, "
			+ " A.BILLING_DOC, A.CANCL_DOC_NUM,B.MATERIAL_NUM, B.LINE_ITEM_NUM, B.INVOICE_QTY, B.ORDER_REASON, B.SALES_ORDER_ITEM_NUM, B.LOT_NUM, "
			+ " (SELECT TOP 1 REF_SALES_ORDER FROM HYBRIS.STG_INVOICE_LINE WHERE INVOICE_NUM =A.INVOICE_NUM) AS REF_SALES_ORDER, "
			+ " LAST_UPDATED_DATE FROM ";

	private static final String SQL_QUERY_VIEW_PART = " HYBRIS.STG_INVOICE_HEADER A, HYBRIS.STG_INVOICE_LINE B ";


	private static final String SQL_QUERY_WHERE_PART = " WHERE A.INVOICE_NUM = B.INVOICE_NUM  ";


	/**
	 * This method iterates over a list of records from RSA view.
	 *
	 */
	public void getLoadInvoices(final JnjIntegrationRSACronJobModel job)
	{
		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
				+ Logging.LOAD_INVOICE_GET_INVOICES + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + " Calling the View to get data");

		final boolean hasInvalidRecords = readInvoiceDataFromRSA(job);

		if (hasInvalidRecords)
		{
			LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + " Issue with Data fetch and load.");
		}
		else
		{
			LOG.info(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + " Data fetched and loaded without issue.");
		}

		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
				+ Logging.LOAD_INVOICE_GET_INVOICES + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
	}

	/**
	 * This method invokes the mapper class to convert the invoice data from DTO object to invoice model and save.
	 *
	 */
	private boolean readInvoiceDataFromRSA(final JnjIntegrationRSACronJobModel jobModel)
	{
		final String query = SQL_QUERY_SELECT_PART + SQL_QUERY_VIEW_PART + SQL_QUERY_WHERE_PART;
		return jnjInvoiceLoadMapper.populateInvoiceRecords(buildRSASQLQuery(query, jobModel), jobModel);
	}

	private String buildRSASQLQuery(final String syncOrderQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "buildRSASQLQuery";

		String queryConstraints = jnjInterfaceOperationArchUtility.builRSAQueryLastUpdatedDate(jobModel, "AND LAST_UPDATED_DATE");
		// Verify whether there is a initial date in order to set a end date.
		if (!queryConstraints.isEmpty())
		{
			queryConstraints = jnjInterfaceOperationArchUtility.buildRSAQueryLastUpdatedEndDate(queryConstraints,
					Jnjlab2bcoreConstants.LoadInvoices.INVOICE_LAST_UPDATED_END_DATE, "LAST_UPDATED_DATE");
		}

		final String finalQuery = syncOrderQuery + queryConstraints;

		JnjGTCoreUtil.logInfoMessage(Logging.LOAD_INVOICES_JOB, methodName, "Final Query: " + finalQuery, JnjRSAOrderDaoImpl.class);

		return finalQuery;
	}


}
