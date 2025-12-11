/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.outboundservice.services.nfe.impl;

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
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.services.nfe.JnjNfeService;


/**
 * The Serivce Interface Impl to get NFE data from external systems.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjNfeService extends WebServiceGatewaySupport implements JnjNfeService
{


	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjNfeService.class);

	/** The Constant objectFactory. */
	public static final ObjectFactory objectFactory = new ObjectFactory();

	/** The Constant METHOD_NAME. */
	protected static final String METHOD_NAME = "getNfeWrapper()";



	/** The webservice template for nfe. */
	@Autowired
	WebServiceTemplate webserviceTemplateForNfe;

	public WebServiceTemplate getWebserviceTemplateForNfe() {
		return webserviceTemplateForNfe;
	}

	/**
	 * This methods hits the external system to get the NFE data.
	 */
	@Override
	public ElectronicNotaFiscalResponse getNfeWrapper(
			final ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		 
		ElectronicNotaFiscalResponse electronicNotaFiscalResponse = null;

		// To be removed after testing
		JAXBContext jaxbContext;
		Marshaller marshaller;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + "Hitting the WerbSerivce to get the response.");
		}
		try
		{
			// Added to Out Request on the Console. Need to remove later. 
			jaxbContext = JAXBContext.newInstance(ReceiveElectronicNotaFiscalFromHybrisWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(receiveElectronicNotaFiscalFromHybrisWrapper, System.out);
			loadConnectionParamsFromProp(webserviceTemplateForNfe);
			 

			// Added to Out Response on the Console. Need to remove later.
		 
			marshaller = jaxbContext.createMarshaller();
		 

		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + "Illegal Argument Exception error Occured: " + illegalArgumentException.getMessage());
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + "WebServiceClientException occured: " + webServiceClientException.getMessage());
			throw new IntegrationException();
		}
		catch (final JAXBException jAXBException)
		{
			LOGGER.error(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + "JAXBException occured: " + jAXBException.getMessage());
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjNfeServiceImpl class" + throwable.getMessage());
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + METHOD_NAME
					+ Logging.HYPHEN + "Response recieved from WebService.");
		}

	 

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}


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
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_USER));
		LOGGER.debug("Password= "
				+ Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_USER),
				Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForNfe.setDefaultUri(Config
				.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_QUERY_NFE_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForNfe.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.QUERY_NFE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}
}
