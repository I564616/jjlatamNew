/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.invoice.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.invoice.JnjInvoiceFacade;
import com.jnj.facades.order.JnjInvoiceDocMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingResponse;


/**
 * The facade for operations related to invoices.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjInvoiceFacade implements JnjInvoiceFacade
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjInvoiceFacade.class);
	private static final String METHOD_GET_INVOICES = "getInvoices()";

	/** The jnj invoice service. */
	@Autowired
	private JnjInvoiceService jnjInvoiceService;

	@Autowired
	private JnjInvoiceDocMapper jnjInvoiceDocMapper;

	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	private Converter<JnJInvoiceOrderModel, JnJInvoiceOrderData> jnJInvoiceOrderDataConverter;

	/**
	 * This method will fetch all the invoices related to particular Sap Order No.
	 * 
	 * @param salesOrder
	 * @return the invoices
	 */
	@Override
	public List<JnJInvoiceOrderData> getInvoices(final String salesOrder) throws BusinessException
	{
		logMethodStartOrEnd(Logging.ORDER_HISTORY_INVOICE_DETAILS, METHOD_GET_INVOICES, Logging.BEGIN_OF_METHOD);
		List<JnJInvoiceOrderModel> invoiceModelList = new ArrayList<JnJInvoiceOrderModel>();
		final List<JnJInvoiceOrderData> invoiceDataList = new ArrayList<JnJInvoiceOrderData>();
		if (null != salesOrder)
		{
			invoiceModelList = jnjInvoiceService.getInvoicebyOrderCode(salesOrder);
			logDebugMessage(Logging.ORDER_HISTORY_INVOICE_DETAILS, METHOD_GET_INVOICES, "JnJInvoiceOrderModels with Sales Order No:"
					+ salesOrder + "found in Hybris. Returnig the same.");
			for (final JnJInvoiceOrderModel jnJInvoiceOrderModel : invoiceModelList)
			{
				logDebugMessage(Logging.ORDER_HISTORY_INVOICE_DETAILS, METHOD_GET_INVOICES,
						"Converting JnJInvoiceOrderModel with PONum:" + salesOrder + "to JnJInvoiceOrderData");
				invoiceDataList.add(getJnJInvoiceOrderDataConverter().convert(jnJInvoiceOrderModel, new JnJInvoiceOrderData()));
			}
		}
		else
		{
			logDebugMessage(Logging.QUERY_NFE, METHOD_GET_INVOICES,
					"Empty SAP Order No received. Returning Null. Initiating Business Exception");
			throw new BusinessException("Empty SAP Order No received. Returning Null");

		}
		logMethodStartOrEnd(Logging.ORDER_HISTORY_INVOICE_DETAILS, METHOD_GET_INVOICES, Logging.END_OF_METHOD);
		return invoiceDataList;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getInvoiceDocFile(final String fileType, final String invoiceId) throws BusinessException, IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		File invoiceDocFile = null;
		URL website = null;
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;

		final ElectronicBillingResponse electronicBillingResponse = jnjInvoiceDocMapper.getInvoiceDocMapper(fileType, invoiceId);
		if (null != electronicBillingResponse && null != electronicBillingResponse.getElectronicBillingRequestOut().getURL()
				&& !electronicBillingResponse.getElectronicBillingRequestOut().getURL().isEmpty())
		{
			final String sharedFolder = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER);
			final List<String> urlList = electronicBillingResponse.getElectronicBillingRequestOut().getURL();
			final String urlOpenText = Config.getParameter(Jnjb2bCoreConstants.INVOICE_OPEN_TEXT_URL);
			final String hostOpenText = Config.getParameter(Jnjb2bCoreConstants.INVOICE_OPEN_TEXT_HOST);
			String newURLOpentext = "";
			for (final String url : urlList)
			{
				try
				{


					newURLOpentext = parseCdataValue(url.replace(hostOpenText, urlOpenText));
					LOGGER.debug(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
							+ Logging.HYPHEN + " Final OpenText URL: " + newURLOpentext);
					website = URI.create(newURLOpentext).toURL();
					rbc = Channels.newChannel(website.openStream());
					invoiceDocFile = new File(sharedFolder + invoiceId + Jnjb2bFacadesConstants.Logging.DOT_STRING + fileType);
					fos = new FileOutputStream(invoiceDocFile);
					fos.getChannel().transferFrom(rbc, 0, 15728640);
					fos.close();
				}
				catch (final FileNotFoundException fileNOteFoundException)
				{
					LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
							+ Logging.HYPHEN + "File Not Found Exception Occured " + fileNOteFoundException.getMessage());
					throw new BusinessException("File Not Found Exception Occured", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
				catch (final MalformedURLException malformedURLException)
				{
					LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
							+ Logging.HYPHEN + "Mal Formed URL Exception Occured " + malformedURLException.getMessage());
					throw new BusinessException("Mal Formed URL Exception Occured", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
				catch (final IOException ioException)
				{
					LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
							+ Logging.HYPHEN + "Input Out Exception Occured " + ioException.getMessage());
					throw new BusinessException("Input Out Exception Occured ", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
			}
		}
		// code for logging the error message which comes from the SAP response
		else if (null != electronicBillingResponse && null != electronicBillingResponse.getElectronicBillingRequestOut()
				&& null != electronicBillingResponse.getElectronicBillingRequestOut().getERROR()
				&& StringUtils.isNotEmpty(electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()))
		{
			if (electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()
					.equals(jnjCommonFacadeUtil.getMessageFromImpex("order.history.invoiceNotFound")))
			{
				LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
						+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
						+ "Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.invoiceNotFound"));

				throw new BusinessException(jnjCommonFacadeUtil.getMessageFromImpex("order.history.invoiceNotFound"),
						MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
			}
			else if (electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()
					.equals(jnjCommonFacadeUtil.getMessageFromImpex("order.history.fileNotFound")))
			{
				LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
						+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
						+ "Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.fileNotFound"));

				throw new BusinessException("Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.fileNotFound"), MessageCode.BUSINESS_EXCEPTION,
						Severity.BUSINESS_EXCEPTION);
			}
			else if (electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()
					.equals(jnjCommonFacadeUtil.getMessageFromImpex("order.history.errorDuringFileCreation")))
			{
				LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
						+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
						+ "Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.errorDuringFileCreation"));

				throw new BusinessException("Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.errorDuringFileCreation"),
						MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
			}
			else if (electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()
					.equals(jnjCommonFacadeUtil.getMessageFromImpex("order.history.notExpectedCountry")))
			{
				LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
						+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
						+ "Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.notExpectedCountry"));

				throw new BusinessException("Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.notExpectedCountry"), MessageCode.BUSINESS_EXCEPTION,
						Severity.BUSINESS_EXCEPTION);
			}
			else if (electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue()
					.equals(jnjCommonFacadeUtil.getMessageFromImpex("order.history.documentNotFound")))
			{
				LOGGER.error(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
						+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
						+ "Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.documentNotFound"));

				throw new BusinessException("Received error message from SAP and the error message is "
						+ jnjCommonFacadeUtil.getMessageFromImpex("order.history.documentNotFound"), MessageCode.BUSINESS_EXCEPTION,
						Severity.BUSINESS_EXCEPTION);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return invoiceDocFile;
	}


	private String parseCdataValue(final String value)
	{

		return (value.replace("<![CDATA[", "")).replace("]]>", "");
	}

	/**
	 * /**
	 * 
	 * @return the jnJInvoiceOrderDataConverter
	 */
	public Converter<JnJInvoiceOrderModel, JnJInvoiceOrderData> getJnJInvoiceOrderDataConverter()
	{
		return jnJInvoiceOrderDataConverter;
	}

	/**
	 * @param jnJInvoiceOrderDataConverter
	 *           the jnJInvoiceOrderDataConverter to set
	 */
	public void setJnJInvoiceOrderDataConverter(
			final Converter<JnJInvoiceOrderModel, JnJInvoiceOrderData> jnJInvoiceOrderDataConverter)
	{
		this.jnJInvoiceOrderDataConverter = jnJInvoiceOrderDataConverter;
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
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
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
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
