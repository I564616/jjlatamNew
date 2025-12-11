/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.facades.nfe.mapper;

import de.hybris.platform.servicelayer.user.UserService;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.la.outboundservice.services.nfe.JnjLaNfeService;
import com.jnj.outboundservice.nfe.BTBControlArea;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalRequest;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalResponse;
import com.jnj.outboundservice.nfe.NFERequestData;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapper;


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
	private JnjLaNfeService jnjLaNfeService;

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
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA, Logging.BEGIN_OF_METHOD, JnjNfeMapper.class);

		ElectronicNotaFiscalResponse electronicNotaFiscalResponse = null;
		final NFERequestData nFERequestData = new NFERequestData();
		final BTBControlArea btbControlArea = new BTBControlArea();
		final ElectronicNotaFiscalRequest electronicNotaFiscalRequest = new ElectronicNotaFiscalRequest();
		LOGGER.info(
				Logging.QUERY_NFE + Logging.HYPHEN + METHOD_MAP_NFE_REQUEST_DATA + Logging.HYPHEN + "Generating NFE Access Key");
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

				electronicNotaFiscalResponse = jnjLaNfeService.getNfeWrapper(receiveElectronicNotaFiscalFromHybrisWrapper);

				LOGGER.info(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_MAP_NFE_REQUEST_DATA + Logging.HYPHEN
						+ "ElectronicNotaFiscalResponse Recieved");
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA,
						"Language ISO Code cannot Be Null. Initiating Business Exception", JnjNfeMapper.class);
				throw new BusinessException("Language ISO Code is Null.");
			}

		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA,
					"Generated NFEAccess Key cannot Be Null. Initiating Business Exception", JnjNfeMapper.class);
			throw new BusinessException("Generated NFEAccess is Null.");
		}
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_MAP_NFE_REQUEST_DATA, Logging.END_OF_METHOD, JnjNfeMapper.class);
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
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NFE_GENERATED_ACCESS_KEY, Logging.BEGIN_OF_METHOD,
				JnjNfeMapper.class);
		StringBuilder nfeKey = new StringBuilder();
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getRegion().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfYear().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfMonth().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getStcd().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getModel().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getSeries().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getNfNumber().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getDocNumber().trim());
		nfeKey = nfeKey.append(jnJInvoiceOrderModel.getCdv().trim());

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NFE_GENERATED_ACCESS_KEY, Logging.END_OF_METHOD,
				JnjNfeMapper.class);
		return nfeKey.toString();
	}



}
