/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.impl;

import de.hybris.platform.servicelayer.user.UserService;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Order;
import com.jnj.facades.order.JnjInvoiceDocMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.BTBControlArea;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingRequestIn;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ObjectFactory;
import com.jnj.outboundservice.services.invoice.JnjInvoiceDoc;


/**
 * The JnjInvoiceDocMapperImpl class implements JnjInvoiceDocMapper interface and we set the request object.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjInvoiceDocMapper implements JnjInvoiceDocMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjInvoiceDocMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	JnjInvoiceDoc jnjInvoiceDoc;
	@Autowired
	UserService userService;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws BusinessException
	 */
	@Override
	public ElectronicBillingResponse getInvoiceDocMapper(final String fileType, final String invoiceId)
			throws IntegrationException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocMapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ElectronicBillingResponse electronicBillingResponse = null;
		final ElectronicBillingRequest electronicBillingRequest = new ElectronicBillingRequest();
		final ElectronicBillingRequestIn ElectronicBillingRequestIn = new ElectronicBillingRequestIn();
		final BTBControlArea btbControlArea = new BTBControlArea();

		// Getting the iso code from the user service.
		final String languageIsoCode = userService.getCurrentUser().getSessionLanguage().getIsocode();
		if (StringUtils.isNotEmpty(languageIsoCode))
		{

			if (StringUtils.isNotEmpty(invoiceId))
			{
				ElectronicBillingRequestIn.setBILLINGDOC(invoiceId);
			}
			else
			{
				ElectronicBillingRequestIn.setBILLINGDOC(Order.EMPTY_STRING);
			}
			if (StringUtils.isNotEmpty(fileType))
			{
				final JAXBElement<String> value = objectFactory.createElectronicBillingRequestInFILETYPE(fileType);
				ElectronicBillingRequestIn.setFILETYPE(value);
			}

			ElectronicBillingRequestIn.setLANGUAGE(languageIsoCode.toUpperCase());

			// CR CP015 Changes Start
			final Date date = new Date();
			final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
			final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);

			final String formatedSecondTime = secondformatter.format(date);
			final String formatedMiliSecondTime = miliSecondformatter.format(date);


			btbControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.GET_INVOICE + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
					+ invoiceId + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
			btbControlArea.setTransactionDateTime(formatedMiliSecondTime);
			final JAXBElement<BTBControlArea> jaxbBTBControlArea = objectFactory
					.createElectronicBillingRequestHeader(btbControlArea);
			electronicBillingRequest.setHeader(jaxbBTBControlArea);

			electronicBillingRequest.setElectronicBillingRequestIn(ElectronicBillingRequestIn);
			// CR CP015 Changes Done
			electronicBillingResponse = jnjInvoiceDoc.receiveElectronicBillingFromHybrisWrapper(electronicBillingRequest);
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocMapper()" + Logging.HYPHEN
						+ "Iso Code is empty or null ");
			}
			throw new BusinessException("Iso Code is empty or null ", MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocMapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return electronicBillingResponse;
	}
}
