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
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.gt.outbound.services.JnjGTCreateOrderService;



/**
 * The jnjGTCreateOrderServiceImpl class contains the definition of all the methods of the jnjGTCreateOrderService
 * interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTCreateOrderService implements JnjGTCreateOrderService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateOrderService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForCreate;

	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;


	public WebServiceTemplate getWebserviceTemplateForCreate() {
		return webserviceTemplateForCreate;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	@Override
	public OrderCreationInSAPOutput orderCreationInSAP(final OrderCreationInSAPInput orderCreationInSAPInput,
			final JnjGTSapWsData sapWsData) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "orderCreate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		OrderCreationInSAPOutput orderCreationInSAPOutput = null;
		LOGGER.debug("1 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));

		if(!Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){
			
			try
			{
				// Remove after testing and used for printing the xml.
				JAXBContext jaxbContext;
				Marshaller marshaller;

				jaxbContext = JAXBContext.newInstance(OrderCreationInSAPInput.class);
				marshaller = jaxbContext.createMarshaller();
				marshaller.marshal(orderCreationInSAPInput, System.out);

				if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
						jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
				{
					// Code for testing the response logic through mocking the response xml.
					/*
					 * final File oldfile = new
					 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.CreateOrder.MOCK_XML_CLASS_PATH)); final
					 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); orderCreationInSAPOutput =
					 * (OrderCreationInSAPOutput) jaxbUnmarshaller.unmarshal(oldfile);
					 */
					throw new IntegrationException();
				}// Else block executes always when data would be required from the SAP.
				else
				{
					loadConnectionParamsFromProp(webserviceTemplateForCreate, sapWsData);
					orderCreationInSAPOutput = (OrderCreationInSAPOutput) ((JAXBElement) webserviceTemplateForCreate
							.marshalSendAndReceive(orderCreationInSAPInput, new WebServiceMessageCallback()
							{
								@Override
								public void doWithMessage(final WebServiceMessage arg0)
								{
									((SoapMessage) arg0)
											.setSoapAction("MU007_EPIC_OrderCreate_v1_orderCreationInSAPWebservice_Binder_orderCreationInSAP");
								}
							})).getValue();

				}

				jaxbContext = JAXBContext.newInstance(OrderCreationInSAPOutput.class);
				marshaller = jaxbContext.createMarshaller();
				marshaller.marshal(orderCreationInSAPOutput, System.out);
			}
			catch (final IllegalArgumentException illegalArgumentException)
			{
				LOGGER.error(
						Logging.CREATE_ORDER + Logging.HYPHEN + "orderCreate()" + Logging.HYPHEN
								+ "Illegal Argument Exception Occured in jnjGTCreateOrderServiceImpl class"
								+ illegalArgumentException.getMessage(), illegalArgumentException);
				throw new IntegrationException();
			}

			catch (final WebServiceClientException webServiceClientException)
			{
				LOGGER.error(
						Logging.CREATE_ORDER + Logging.HYPHEN + "orderCreate()" + Logging.HYPHEN
								+ "Web Service Client Exception Occured in jnjGTCreateOrderServiceImpl class"
								+ webServiceClientException.getMessage(), webServiceClientException);
				throw new IntegrationException();
			}
			catch (final Throwable throwable)
			{
				LOGGER.error(Logging.CREATE_ORDER + Logging.HYPHEN + "orderCreate()" + Logging.HYPHEN
						+ "Exception caught in Throwable block of jnjGTCreateOrderServiceImpl class" + throwable.getMessage(), throwable);
				throw new IntegrationException();
			}
		}
	
		LOGGER.debug("2 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));

		if(Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){

			orderCreationInSAPOutput = new OrderCreationInSAPOutput();
		}


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "orderCreate()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderCreationInSAPOutput;
	}

	/**
	 * Load connection params from property file.
	 *
	 * @param webserviceTemplateForCreate
	 *           the webservice template for create
	 * @param wsData
	 *           the ws data
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForCreate, final JnjGTSapWsData wsData)
			throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.CreateOrder.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);

		messageSender.setConnectionTimeout(Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		messageSender.setReadTimeout(Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		LOGGER.info("Standard(Common) : Create Order > Used Connection Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		LOGGER.info("Standard(Common) : Create Order > Used Read Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForCreate.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CreateOrder.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForCreate.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
