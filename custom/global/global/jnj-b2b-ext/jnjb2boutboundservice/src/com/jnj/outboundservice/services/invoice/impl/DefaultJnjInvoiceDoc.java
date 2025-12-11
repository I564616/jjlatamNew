/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 * 
 */
package com.jnj.outboundservice.services.invoice.impl;

import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants;
import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants.Logging;
import de.hybris.platform.util.Config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;




import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ReceiveElectronicBillingFromHybrisWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ReceiveElectronicBillingFromHybrisWrapperResponse;
import com.jnj.outboundservice.services.invoice.JnjInvoiceDoc;


/**
 * The JnjInvoiceDocImpl class is the implementation class for the JnjInvoiceDoc interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjInvoiceDoc extends WebServiceTemplate implements JnjInvoiceDoc
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjInvoiceDoc.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	WebServiceTemplate webserviceTemplateForInvoice;

	public WebServiceTemplate getWebserviceTemplateForInvoice() {
		return webserviceTemplateForInvoice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElectronicBillingResponse receiveElectronicBillingFromHybrisWrapper(
			final ElectronicBillingRequest electronicBillingRequest) throws IntegrationException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "receiveElectronicBillingFromHybrisWrapper()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		ReceiveElectronicBillingFromHybrisWrapperResponse responseWrapper = null;
		ElectronicBillingResponse electronicBillingResponse = null;
		try
		{
			final ReceiveElectronicBillingFromHybrisWrapper receiveElectronBillingFromHybrisWrapper = new ReceiveElectronicBillingFromHybrisWrapper();
			receiveElectronBillingFromHybrisWrapper.setElectronicBillingRequest(electronicBillingRequest);

			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(ReceiveElectronicBillingFromHybrisWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(receiveElectronBillingFromHybrisWrapper, System.out);
			loadConnectionParamsFromProp(webserviceTemplateForInvoice);
			responseWrapper = (ReceiveElectronicBillingFromHybrisWrapperResponse) ((JAXBElement) webserviceTemplateForInvoice
					.marshalSendAndReceive(receiveElectronBillingFromHybrisWrapper, new WebServiceMessageCallback()
					{

						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{

							((SoapMessage) arg0)
									.setSoapAction("SG910_BtB_IN0504_ElectronicBilling_Hybris_Source_v1_webservices_receiveElectronicBillingWS_Binder_receiveElectronicBillingFromHybrisWrapper");

						}
					})).getValue();
			jaxbContext = JAXBContext.newInstance(ReceiveElectronicBillingFromHybrisWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(responseWrapper, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "receiveElectronicBillingFromHybrisWrapper;()"
					+ Logging.HYPHEN + "Illegal Argument Exception Occured in JnjInvoiceImpl class"
					+ illegalArgumentException.getMessage());
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "receiveElectronicBillingFromHybrisWrapper()"
					+ Logging.HYPHEN + "Web Service Client Exception Occured in JnjInvoiceImpl class"
					+ webServiceClientException.getMessage());
			//electronicBillingResponse = new ElectronicBillingResponse();
			//final JAXBElement<String> value = objectFactory.createElectronicBillingResponseERROR("Invoice Not Found");
			//electronicBillingResponse.setERROR(value);
			//electronicBillingResponse.getURL().add("http://www.jpl.nasa.gov/about_JPL/jpl101.pdf");
			//return electronicBillingResponse;
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "receiveElectronicBillingFromHybrisWrapper()"
					+ Logging.HYPHEN + "Exception caught in Throwable block of JnjInvoiceImpl class" + throwable.getMessage());
			throw new IntegrationException();
		}
		if (null != responseWrapper)
		{
			electronicBillingResponse = responseWrapper.getElectronicBillingResponse();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "receiveElectronicBillingFromHybrisWrapper()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return electronicBillingResponse;
	}

	/**
	 * Load connection params from prop.
	 * 
	 * @param webserviceTemplateForInvoice
	 *           the webservice template for invoice
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForInvoice) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= "
				+ Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_USER));
		LOGGER.debug("Password= "
				+ Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_USER),
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForInvoice.setDefaultUri(Config
				.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForInvoice.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
