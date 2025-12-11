/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.outboundservice.services.order.impl;

import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants;
import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants.Logging;
import de.hybris.platform.util.Config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
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
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;





import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationWrapperResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingWrapperResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationWrapperResponse;
import com.jnj.outboundservice.services.order.JnjSalesOrder;


/**
 * The JnjSalesOrderImpl class interacts with the SAP Submit Order Service on the basis of different object.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjSalesOrder extends WebServiceGatewaySupport implements JnjSalesOrder
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjSalesOrder.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private SalesOrderCreationResponse salesOrderCreationResponse;

	@Autowired
	private SalesOrderSimulationResponse salesOrderSimulationResponse;

	@Autowired
	private SalesOrderPricingResponse salesOrderPricingResponse;

	@Autowired
	WebServiceTemplate webserviceTemplate;


	public SalesOrderCreationResponse getSalesOrderCreationResponse() {
		return salesOrderCreationResponse;
	}

	public SalesOrderSimulationResponse getSalesOrderSimulationResponse() {
		return salesOrderSimulationResponse;
	}

	public SalesOrderPricingResponse getSalesOrderPricingResponse() {
		return salesOrderPricingResponse;
	}

	public WebServiceTemplate getWebserviceTemplate() {
		return webserviceTemplate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IntegrationException
	 */
	@Override
	public SalesOrderPricingResponse salesOrderPricingWrapper(final SalesOrderPricingRequest salesOrderPricingRequest)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		SalesOrderPricingWrapperResponse salesOrderPricingWrapperResponse = null;
		SalesOrderPricingResponse salesOrderPricingResponse = null;
		try
		{
			final SalesOrderPricingWrapper salesOrderPricingWrapper = new SalesOrderPricingWrapper();
			salesOrderPricingWrapper.setSalesOrderPricingRequest(salesOrderPricingRequest);
			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(SalesOrderPricingWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(salesOrderPricingWrapper, System.out);
			loadConnectionParamsFromProp(webserviceTemplate);
			salesOrderPricingWrapperResponse = (SalesOrderPricingWrapperResponse) ((JAXBElement) webserviceTemplate
					.marshalSendAndReceive(salesOrderPricingWrapper, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{

							((SoapMessage) arg0)
									.setSoapAction("SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderPricingWrapper");

						}
					})).getValue();

			jaxbContext = JAXBContext.newInstance(SalesOrderPricingWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(salesOrderPricingWrapperResponse, System.out);

		}
		catch (final JAXBException jaxbException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ "JAXB Exception Occured in JnjSalesOrderImpl class" + jaxbException.getMessage());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ "Illegal Argument Exception Occured in JnjSalesOrderImpl class" + illegalArgumentException.getMessage());
			throw new IntegrationException();
		}
		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ "Web Service Client Exception Occured in JnjSalesOrderImpl class" + webServiceClientException.getMessage());
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjSalesOrderImpl class" + throwable.getMessage());
			throw new IntegrationException();
		}

		if (null != salesOrderPricingWrapperResponse)
		{
			salesOrderPricingResponse = salesOrderPricingWrapperResponse.getSalesOrderPricingResponse();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderPricingWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrderPricingResponse;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IntegrationException
	 */
	@Override
	public SalesOrderSimulationResponse salesOrderSimulationWrapper(final SalesOrderSimulationRequest salesOrderSimulationRequest)
			throws IntegrationException

	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderSimulationWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		SalesOrderSimulationWrapperResponse salesOrderSimulationWrapperResponse = null;
		SalesOrderSimulationResponse simulationResponse = null;
		try
		{
			final SalesOrderSimulationWrapper salesOrderSimulationWrapper = new SalesOrderSimulationWrapper();
			salesOrderSimulationWrapper.setSalesOrderSimulationRequest(salesOrderSimulationRequest);
			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(SalesOrderSimulationWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(salesOrderSimulationWrapper, System.out);
			loadConnectionParamsFromProp(webserviceTemplate);
			salesOrderSimulationWrapperResponse = (SalesOrderSimulationWrapperResponse) ((JAXBElement) webserviceTemplate
					.marshalSendAndReceive(salesOrderSimulationWrapper, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{

							((SoapMessage) arg0)
									.setSoapAction("SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderSimulationWrapper");

						}
					})).getValue();

			jaxbContext = JAXBContext.newInstance(SalesOrderSimulationWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(salesOrderSimulationWrapperResponse, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderSimulationWrapper()" + Logging.HYPHEN
					+ "Illegal Argument Exception Occured in JnjSalesOrderImpl class" + illegalArgumentException.getMessage());
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderSimulationWrapper()" + Logging.HYPHEN
					+ "Web Service Client Exception Occured in JnjSalesOrderImpl class" + webServiceClientException.getMessage());
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderSimulationWrapper()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjSalesOrderImpl class" + throwable.getMessage());
			throw new IntegrationException();
		}
		if (null != salesOrderSimulationWrapperResponse)
		{
			simulationResponse = salesOrderSimulationWrapperResponse.getSalesOrderSimulationResponse();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderSimulationWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return simulationResponse;
	}

	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webServiceTemplate) throws Exception
	{
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_INVOICE_DOC_URL));
		

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplate.setDefaultUri(Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplate.setMessageSender(messageSender);

	}

	/**
	 * {@inheritDoc}
	 * 
	 */

	@Override
	public SalesOrderCreationResponse salesOrderCreationWrapper(final SalesOrderCreationRequest salesOrderCreationRequest)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderCreationWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		SalesOrderCreationWrapperResponse responseWrapper = null;
		SalesOrderCreationResponse salesOrderCreationResponse = null;
		try
		{






			loadConnectionParamsFromProp(webserviceTemplate);
			final SalesOrderCreationWrapper salesOrderCreationWrapper = new SalesOrderCreationWrapper();
			salesOrderCreationWrapper.setSalesOrderCreationRequest(salesOrderCreationRequest);

			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(SalesOrderCreationWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(salesOrderCreationWrapper, System.out);

			responseWrapper = (SalesOrderCreationWrapperResponse) ((JAXBElement) webserviceTemplate.marshalSendAndReceive(
					salesOrderCreationWrapper, new WebServiceMessageCallback()
					{

						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{

							((SoapMessage) arg0)
									.setSoapAction("SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderCreationWrapper");

						}
					})).getValue();

			jaxbContext = JAXBContext.newInstance(SalesOrderCreationWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(responseWrapper, System.out);

		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderCreationWrapper()" + Logging.HYPHEN
					+ "Illegal Argument Exception Occured in JnjSalesOrderImpl class" + illegalArgumentException.getMessage());
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderCreationWrapper()" + Logging.HYPHEN
					+ "Web Service Client Exception Occured in JnjSalesOrderImpl class" + webServiceClientException.getMessage());
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderCreationWrapper()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjSalesOrderImpl class" + throwable.getMessage());
			throw new IntegrationException();
		}
		if (null != responseWrapper)
		{
			salesOrderCreationResponse = responseWrapper.getSalesOrderCreationResponse();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "salesOrderCreationWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrderCreationResponse;
	}
}
