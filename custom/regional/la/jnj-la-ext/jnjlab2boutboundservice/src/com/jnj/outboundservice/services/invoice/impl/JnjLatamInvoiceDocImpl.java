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
package com.jnj.outboundservice.services.invoice.impl;

import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants;
import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants.Logging;
import de.hybris.platform.util.Config;

import java.io.StringWriter;

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

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.outboundservice.constants.Jnjlab2boutboundserviceConstants;
import com.jnj.outboundservice.invoice.ElectronicBillingRequest;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;
import com.jnj.outboundservice.invoice.ObjectFactory;
import com.jnj.outboundservice.invoice.ReceiveElectronicBillingFromHybrisWrapper;
import com.jnj.outboundservice.invoice.ReceiveElectronicBillingFromHybrisWrapperResponse;
import com.jnj.outboundservice.services.invoice.JnjLatamInvoiceDoc;


/**
 *
 */
public class JnjLatamInvoiceDocImpl extends WebServiceTemplate implements JnjLatamInvoiceDoc
{

	private static final Logger LOGGER = Logger.getLogger(JnjLatamInvoiceDocImpl.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected WebServiceTemplate webserviceTemplateForInvoice;

	public WebServiceTemplate getWebserviceTemplateForInvoice()
	{
		return webserviceTemplateForInvoice;
	}




	@Override
	public ElectronicBillingResponse receiveElectronicBillingFromHybrisWrapper(
			final ElectronicBillingRequest electronicBillingRequest) throws IntegrationException
	{


		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "receiveElectronicBillingFromHybrisWrapper()",
				Logging.BEGIN_OF_METHOD, JnjLatamInvoiceDocImpl.class);


		ReceiveElectronicBillingFromHybrisWrapperResponse responseWrapper = null;
		ElectronicBillingResponse electronicBillingResponse = null;
		try
		{
			final ReceiveElectronicBillingFromHybrisWrapper receiveElectronBillingFromHybrisWrapper = new ReceiveElectronicBillingFromHybrisWrapper();
			receiveElectronBillingFromHybrisWrapper.setElectronicBillingRequest(electronicBillingRequest);

			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller marshaller;
			final StringWriter stringWriter = new StringWriter();

			jaxbContext = JAXBContext.newInstance(ReceiveElectronicBillingFromHybrisWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(receiveElectronBillingFromHybrisWrapper, stringWriter);
			LOGGER.info("INVOICE DOCUMENT  - Request XML:######################### " + stringWriter.toString());
			loadConnectionParamsFromProp(webserviceTemplateForInvoice);
			responseWrapper = (ReceiveElectronicBillingFromHybrisWrapperResponse) ((JAXBElement) webserviceTemplateForInvoice
					.marshalSendAndReceive(receiveElectronBillingFromHybrisWrapper, new WebServiceMessageCallback()
					{

						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{

							((SoapMessage) arg0).setSoapAction(Jnjlab2boutboundserviceConstants.SOAP_ACTION_INVOICE_DOC);

						}
					})).getValue();
			jaxbContext = JAXBContext.newInstance(ReceiveElectronicBillingFromHybrisWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(responseWrapper, stringWriter);
			LOGGER.info("INVOICE DOCUMENT - Response XML:######################### " + stringWriter.toString());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{

			JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "receiveElectronicBillingFromHybrisWrapper()",
					"Illegal Argument Exception Occured in JnjInvoiceImpl class " + illegalArgumentException
							+ illegalArgumentException.getMessage(),
					JnjLatamInvoiceDocImpl.class);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{

			JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "receiveElectronicBillingFromHybrisWrapper()",
					"Web Service Client Exception Occured in JnjInvoiceImpl class " + webServiceClientException
							+ webServiceClientException.getMessage(),
					JnjLatamInvoiceDocImpl.class);

			throw new IntegrationException();
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "receiveElectronicBillingFromHybrisWrapper()",
					"Exception caught in Throwable block of JnjLatamInvoiceImpl class " + throwable + throwable.getMessage(),
					JnjLatamInvoiceDocImpl.class);


			throw new IntegrationException();
		}
		if (null != responseWrapper)
		{
			electronicBillingResponse = responseWrapper.getElectronicBillingResponse();
		}

		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "receiveElectronicBillingFromHybrisWrapper()",
				Logging.END_OF_METHOD, JnjLatamInvoiceDocImpl.class);
		return electronicBillingResponse;
	}


	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForInvoice) throws Exception
	{

		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "loadConnectionParamsFromProp()", Logging.BEGIN_OF_METHOD,
				JnjLatamInvoiceDocImpl.class);
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug(
				"User= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_USER));
		LOGGER.debug(
				"Password= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_USER),
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForInvoice
				.setDefaultUri(Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForInvoice.setMessageSender(messageSender);
		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "loadConnectionParamsFromProp()", Logging.END_OF_METHOD,
				JnjLatamInvoiceDocImpl.class);
	}


}
