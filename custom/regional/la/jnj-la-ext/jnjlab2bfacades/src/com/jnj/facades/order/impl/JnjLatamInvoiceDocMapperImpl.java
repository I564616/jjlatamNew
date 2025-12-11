/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
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
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Order;
import com.jnj.facades.order.JnjLatamInvoiceDocMapper;
import com.jnj.outboundservice.invoice.BTBControlArea;
import com.jnj.outboundservice.invoice.ElectronicBillingRequest;
import com.jnj.outboundservice.invoice.ElectronicBillingRequestIn;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;
import com.jnj.outboundservice.invoice.ObjectFactory;
import com.jnj.outboundservice.services.invoice.JnjLatamInvoiceDoc;


/**
 *
 */
public class JnjLatamInvoiceDocMapperImpl implements JnjLatamInvoiceDocMapper
{
	private static final Logger LOGGER = Logger.getLogger(JnjLatamInvoiceDocMapperImpl.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected JnjLatamInvoiceDoc jnjLatamInvoiceDoc;
	@Autowired
	protected UserService userService;

	@Override
	public ElectronicBillingResponse getInvoiceDocMapper(final String fileType, final String invoiceId)
			throws IntegrationException, BusinessException
	{

		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocMapper()", Logging.BEGIN_OF_METHOD,
				JnjLatamInvoiceDocMapperImpl.class);

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


			final Date date = new Date();
			final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
			final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);

			final String formatedSecondTime = secondformatter.format(date);
			final String formatedMiliSecondTime = miliSecondformatter.format(date);


			btbControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.GET_INVOICE + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + invoiceId
					+ Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
			btbControlArea.setTransactionDateTime(formatedMiliSecondTime);
			final JAXBElement<BTBControlArea> jaxbBTBControlArea = objectFactory
					.createElectronicBillingRequestHeader(btbControlArea);
			electronicBillingRequest.setHeader(jaxbBTBControlArea);

			electronicBillingRequest.setElectronicBillingRequestIn(ElectronicBillingRequestIn);

			electronicBillingResponse = jnjLatamInvoiceDoc.receiveElectronicBillingFromHybrisWrapper(electronicBillingRequest);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocMapper()", "Iso Code is empty or null ",
					JnjLatamInvoiceDocMapperImpl.class);
			throw new BusinessException("Iso Code is empty or null ", MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}


		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocMapper()", Logging.END_OF_METHOD,
				JnjLatamInvoiceDocMapperImpl.class);
		return electronicBillingResponse;
	}

}
