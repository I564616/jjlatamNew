/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.nfe.mapper;

import de.hybris.platform.servicelayer.user.UserService;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.log4j.Logger;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.BTBControlArea;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.NFERequestData;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.services.nfe.JnjNfeService;


/**
 * 
 * This class maps the Invoice Data to NFERequest to be sent to SAP and returnd with the response.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjNfeMapper
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjNfeMapper.class);

	/** The Constant METHOD_MAP_NFE_REQUEST_DATA. */
	private static final String METHOD_MAP_NFE_REQUEST_DATA = "mapNfeRequestData()";

	/** The Constant METHOD_NFE_GENERATED_ACCESS_KEY. */
	private static final String METHOD_NFE_GENERATED_ACCESS_KEY = "generateNFEAcessKey()";

	/** The jnj nfe service. */
	@Autowired
	private JnjNfeService jnjNfeService;

	@Autowired
	private UserService userService;


	/**
	 * Map nfe request data.
	 * 
	 * @param jnJInvoiceOrderModel
	 *           the jnj invoice order model
	 * 
	 *           the nfe request data
	 * @param receiveElectronicNotaFiscalFromHybrisWrapper
	 *           the receive electronic nota fiscal from hybris wrapper
	 * @return the electronic nota fiscal response
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws BusinessException
	 *            the business exception
	 */
	public ElectronicNotaFiscalResponse mapNfeRequestData(final JnJInvoiceOrderModel jnJInvoiceOrderModel,
			final ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper)
			throws IntegrationException, BusinessException
	{
		logMethodStartOrEnd(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA, Logging.BEGIN_OF_METHOD);
		ElectronicNotaFiscalResponse electronicNotaFiscalResponse = null;
		final NFERequestData nFERequestData = new NFERequestData();
		final BTBControlArea btbControlArea = new BTBControlArea();
		final ElectronicNotaFiscalRequest electronicNotaFiscalRequest = new ElectronicNotaFiscalRequest();
		LOGGER.info(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_MAP_NFE_REQUEST_DATA + Logging.HYPHEN + "Generating NFE Access Key");
		final String nFEAcessKey = generateNFEAcessKey(jnJInvoiceOrderModel);
		if (null != nFEAcessKey)
		{
			// CR CP015 Changes Start
			final Date date = new Date();
			final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
			final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);
			final String formatedSecondTime = secondformatter.format(date);
			final String formatedMiliSecondTime = miliSecondformatter.format(date);
			btbControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.QUERY_NFE + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
					+ jnJInvoiceOrderModel.getInvDocNo() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
			btbControlArea.setTransactionDateTime(formatedMiliSecondTime);
			electronicNotaFiscalRequest.setHeader(btbControlArea);
			nFERequestData.setNFeKey(nFEAcessKey);
			final String languageIsoCode = userService.getCurrentUser().getSessionLanguage().getIsocode();
			if (StringUtils.isNotEmpty(languageIsoCode))
			{
				nFERequestData.setLanguage(languageIsoCode.toUpperCase());
				electronicNotaFiscalRequest.setPayLoad(nFERequestData);
				receiveElectronicNotaFiscalFromHybrisWrapper.setElectronicNotaFiscalRequest(electronicNotaFiscalRequest);
				LOGGER.info(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_MAP_NFE_REQUEST_DATA + Logging.HYPHEN
						+ "Calling the Serive to Get ElectronicNotaFiscalResponse");

				electronicNotaFiscalResponse = jnjNfeService.getNfeWrapper(receiveElectronicNotaFiscalFromHybrisWrapper);

				LOGGER.info(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_MAP_NFE_REQUEST_DATA + Logging.HYPHEN
						+ "ElectronicNotaFiscalResponse Recieved");
			}
			else
			{

				logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA,
						"Language ISO Code cannot Be Null. Initiating Business Exception");
				throw new BusinessException("Language ISO Code is Null.");
			}

		}
		else
		{
			logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA,
					"Generated NFEAccess Key cannot Be Null. Initiating Business Exception");
			throw new BusinessException("Generated NFEAccess is Null.");
		}

		logMethodStartOrEnd(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA, Logging.END_OF_METHOD);
		return electronicNotaFiscalResponse;
	}


	/**
	 * This Method Generates the NFE Acess Key based on the Invoice Details.
	 * 
	 * @param jnJInvoiceOrderModel
	 *           the jn j invoice order model
	 * @return NFEAcessKey(String)
	 * @throws BusinessException
	 *            the business exception
	 */
	private String generateNFEAcessKey(final JnJInvoiceOrderModel jnJInvoiceOrderModel) throws BusinessException
	{
		logMethodStartOrEnd(Logging.QUERY_NFE, METHOD_NFE_GENERATED_ACCESS_KEY, Logging.BEGIN_OF_METHOD);

		StringBuffer nfeKey = new StringBuffer();
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getRegion().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfYear().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfMonth().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getStcd().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getModel().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getSeries().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfNumber().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getDocNumber().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getCdv().trim());

		/*
		 * nfeKey = jnJInvoiceOrderModel.getRegion() + jnJInvoiceOrderModel.getNfYear() +
		 * jnJInvoiceOrderModel.getNfMonth() + jnJInvoiceOrderModel.getStcd() + jnJInvoiceOrderModel.getModel() +
		 * jnJInvoiceOrderModel.getSeries() + jnJInvoiceOrderModel.getNfNumber() + jnJInvoiceOrderModel.getDocNumber() +
		 * jnJInvoiceOrderModel.getCdv();
		 */

		logMethodStartOrEnd(Logging.QUERY_NFE, METHOD_NFE_GENERATED_ACCESS_KEY, Logging.END_OF_METHOD);
		return nfeKey.toString();
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
			LOGGER.debug(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit
					+ Logging.HYPHEN + System.currentTimeMillis());
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
