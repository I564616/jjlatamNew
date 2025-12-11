/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.services.impl;

import de.hybris.platform.util.Config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsReply;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsRequest;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.services.JnjGTCmodRgaService;


/**
 * The JnjGTCmodRgaServiceImpl class contains the definition of all the methods of the JnjGTCmodRgaService interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTCmodRgaService implements JnjGTCmodRgaService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCmodRgaService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForCmodRga;
	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	public WebServiceTemplate getWebserviceTemplateForCmodRga() {
		return webserviceTemplateForCmodRga;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	/**
	 * {!{@inheritDoc}
	 *
	 * @throws IntegrationException
	 */
	@Override
	public ReportsReply getReports(final ReportsRequest reportsRequest) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "getReports()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		ReportsReply reportsReply = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(ReportsRequest.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(reportsRequest, System.out);

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))

			{
				// Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); reportsReply = (ReportsReply)
				 * jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForCmodRga);
				reportsReply = (ReportsReply) ((JAXBElement) webserviceTemplateForCmodRga.marshalSendAndReceive(reportsRequest,
						new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0).setSoapAction("MU007_EPIC_Reports_CMOD_v1_wsd_getReports_Binder_getReports");
							}
						})).getValue();
			}

			jaxbContext = JAXBContext.newInstance(ReportsReply.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(reportsReply, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "getReports()" + Logging.HYPHEN
					+ "Illegal Argument Exception Occured in JnjGTCmodRgaServiceImpl class" + illegalArgumentException.getMessage(),
					illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.CMOD_RGA_CALL + Logging.HYPHEN + "getReports()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTCmodRgaServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "getReports()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTCmodRgaServiceImpl class" + throwable.getMessage(), throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "getReports()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return reportsReply;
	}

	/**
	 * Load connection params from property file.
	 *
	 * @param webServiceTemplate
	 *           the web service template
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForCmodRga) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.setConnectionTimeout(Integer.parseInt(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_CONNECTION_TIME_OUT)));
		messageSender.setReadTimeout(Integer.parseInt(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_READ_TIME_OUT)));
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForCmodRga.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForCmodRga.setMessageSender(messageSender);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
