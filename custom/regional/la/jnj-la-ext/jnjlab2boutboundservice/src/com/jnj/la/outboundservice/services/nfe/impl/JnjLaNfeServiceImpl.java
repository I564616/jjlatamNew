/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.outboundservice.services.nfe.impl;

import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants;
import de.hybris.platform.util.Config;

import java.io.IOException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.la.outboundservice.services.nfe.JnjLaNfeService;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalResponse;
import com.jnj.outboundservice.nfe.ObjectFactory;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapperResponse;



/**
 * The Serivce Interface Impl to get NFE data from external systems.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLaNfeServiceImpl extends WebServiceGatewaySupport implements JnjLaNfeService
{


	/** The Constant objectFactory. */
	public static final ObjectFactory objectFactory = new ObjectFactory();

	/** The Constant METHOD_NAME. */
	protected static final String METHOD_NAME = "getNfeWrapper()";



	/** The webservice template for nfe. */
	@Autowired
	protected WebServiceTemplate webserviceTemplateForLaNfe;

	public WebServiceTemplate getWebserviceTemplateForLaNfe()
	{
		return webserviceTemplateForLaNfe;
	}

	/**
	 * This methods hits the external system to get the NFE data.
	 */
	@Override
	public ElectronicNotaFiscalResponse getNfeWrapper(
			final ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper)
			throws IntegrationException
	{


		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaNfeServiceImpl.class);

		ElectronicNotaFiscalResponse electronicNotaFiscalResponse = null;
		ReceiveElectronicNotaFiscalFromHybrisWrapperResponse responseWrapper = null;
		// To be removed after testing
		JAXBContext jaxbContext;
		Marshaller marshaller;

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NAME, "Hitting the WerbSerivce to get the response.",
				JnjLaNfeServiceImpl.class);

		try
		{
			// Added to Out Request on the Console. Need to remove later.
			jaxbContext = JAXBContext.newInstance(ReceiveElectronicNotaFiscalFromHybrisWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(receiveElectronicNotaFiscalFromHybrisWrapper, System.out);
			loadConnectionParamsFromProp(webserviceTemplateForLaNfe);

			responseWrapper = (ReceiveElectronicNotaFiscalFromHybrisWrapperResponse) ((JAXBElement) webserviceTemplateForLaNfe
					.marshalSendAndReceive(receiveElectronicNotaFiscalFromHybrisWrapper, new WebServiceMessageCallback()
					{

						@Override
						public void doWithMessage(final WebServiceMessage arg0) throws IOException, TransformerException
						{
							((SoapMessage) arg0).setSoapAction(
									"SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1_webservices_receiveElectronicNotaFiscalWS_Binder_receiveElectronicNotaFiscalFromHybrisWrapper");

						}
					})).getValue();
			// Added to Out Response on the Console. Need to remove later.


		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.QUERY_NFE, METHOD_NAME,
					"Illegal Argument Exception error Occured: " + illegalArgumentException + illegalArgumentException.getMessage(),
					JnjLaNfeServiceImpl.class);

			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.QUERY_NFE, METHOD_NAME,
					"WebServiceClientException occured: " + webServiceClientException + webServiceClientException.getMessage(),
					JnjLaNfeServiceImpl.class);

			throw new IntegrationException();
		}
		catch (final JAXBException jAXBException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.QUERY_NFE, METHOD_NAME,
					"WebServiceClientException occured: " + "JAXBException occured: " + jAXBException + jAXBException.getMessage(),
					JnjLaNfeServiceImpl.class);

			throw new IntegrationException();
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.QUERY_NFE, METHOD_NAME,
					"Exception caught in Throwable block of JnjLaNfeServiceImpl class" + throwable + throwable.getMessage(),
					JnjLaNfeServiceImpl.class);

			throw new IntegrationException();
		}

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NAME, "Response recieved from WebService.",
				JnjLaNfeServiceImpl.class);

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaNfeServiceImpl.class);

		if (null != responseWrapper)
		{
			electronicNotaFiscalResponse = responseWrapper.getElectronicNotaFiscalResponse();
		}

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_NAME, Logging.END_OF_METHOD, JnjLaNfeServiceImpl.class);


		return electronicNotaFiscalResponse;
	}

	/**
	 * Load connection params from prop.
	 *
	 * @param webserviceTemplateForNfe
	 *           the webservice template for nfe
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForNfe) throws Exception
	{
		final String methodName = "loadConnectionParamsFromProp()";

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, methodName, Logging.BEGIN_OF_METHOD, JnjLaNfeServiceImpl.class);

		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, methodName,
				"User= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_USER),
				JnjLaNfeServiceImpl.class);
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, methodName,
				"Password= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_PWD),
				JnjLaNfeServiceImpl.class);
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, methodName,
				"URL= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_URL),
				JnjLaNfeServiceImpl.class);

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_USER),
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForNfe
				.setDefaultUri(Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForNfe.setMessageSender(messageSender);

		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, "loadConnectionParamsFromProp()", Logging.END_OF_METHOD,
				JnjLaNfeServiceImpl.class);
	}
}
