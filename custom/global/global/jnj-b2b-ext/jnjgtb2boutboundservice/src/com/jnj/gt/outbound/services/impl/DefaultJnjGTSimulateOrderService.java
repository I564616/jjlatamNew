/**
 *
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
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.gt.outbound.services.JnjGTSimulateOrderService;


/**
 * The jnjGTSimulateOrderServiceImpl class contains the definition of all the methods of the jnjGTSimulateOrderService
 * interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTSimulateOrderService implements JnjGTSimulateOrderService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSimulateOrderService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForSimulate;

	public WebServiceTemplate getWebserviceTemplateForSimulate() {
		return webserviceTemplateForSimulate;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	/**
	 * {!{@inheritDoc}
	 *
	 * @throws IntegrationException
	 */
	@Override
	public TestPricingFromGatewayOutput orderSimulate(final TestPricingFromGatewayInput testPricingFromGatewayInput,
			final JnjGTSapWsData wsData) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "orderSimulate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		TestPricingFromGatewayOutput testPricingFromGatewayOutput = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(TestPricingFromGatewayInput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(testPricingFromGatewayInput, System.out);

			//if (Config.getParameter(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING).equalsIgnoreCase(
			//	Jnjgtb2boutboundserviceConstants.Y_STRING))
			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				//Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.SimulateOrder.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); testPricingFromGatewayOutput =
				 * (TestPricingFromGatewayOutput) jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForSimulate, wsData);
				testPricingFromGatewayOutput = (TestPricingFromGatewayOutput) ((JAXBElement) webserviceTemplateForSimulate
						.marshalSendAndReceive(testPricingFromGatewayInput, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("MU007_EPIC_OrderSimulate_v1_orderSimulateWebservice_Binder_orderSimulate");
							}
						})).getValue();
			}

			jaxbContext = JAXBContext.newInstance(TestPricingFromGatewayOutput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(testPricingFromGatewayOutput, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.SIMULATE_ORDER + Logging.HYPHEN + "orderSimulate()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in jnjGTSimulateOrderServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.SIMULATE_ORDER + Logging.HYPHEN + "orderSimulate()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in jnjGTSimulateOrderServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.SIMULATE_ORDER + Logging.HYPHEN + "orderSimulate()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of jnjGTSimulateOrderServiceImpl class" + throwable.getMessage(), throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "orderSimulate()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return testPricingFromGatewayOutput;
	}

	/**
	 * Load connection params from property file.
	 *
	 * @param webserviceTemplateForSimulate
	 *           the webservice template for simulate
	 * @param wsData
	 *           the ws data
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForSimulate, final JnjGTSapWsData wsData)
			throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.SimulateOrder.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);

		messageSender.setConnectionTimeout(Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		messageSender.setReadTimeout(Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		LOGGER.info("Standard(Common) : Simulate Order > Used Connection Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		LOGGER.info("Standard(Common) : Simulate Order > Used Read Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForSimulate.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.SimulateOrder.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForSimulate.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
