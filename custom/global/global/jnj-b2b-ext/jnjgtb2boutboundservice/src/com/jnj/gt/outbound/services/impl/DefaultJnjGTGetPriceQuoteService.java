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
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.services.JnjGTGetPriceQuoteService;


/**
 * The JnjGTGetPriceQuoteServiceImpl class contains the definition of all the methods of the JnjGTGetPriceQuoteService
 * interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */

public class DefaultJnjGTGetPriceQuoteService implements JnjGTGetPriceQuoteService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTGetPriceQuoteService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForPriceQuote;
	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	public WebServiceTemplate getWebserviceTemplateForPriceQuote() {
		return webserviceTemplateForPriceQuote;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	/**
	 * {!{@inheritDoc}
	 *
	 *
	 * @return OrderChangeInSAPOutput
	 * @throws IntegrationException
	 */
	@Override
	public TestPricingFromGatewayOutput getPriceQuoteInSAP(final TestPricingFromGatewayInput testPricingFromGatewayInput)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "getPriceQuoteInSAP()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
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

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				// Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.GetPriceQuote.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); testPricingFromGatewayOutput =
				 * (TestPricingFromGatewayOutput) jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForPriceQuote);
				testPricingFromGatewayOutput = (TestPricingFromGatewayOutput) ((JAXBElement) webserviceTemplateForPriceQuote
						.marshalSendAndReceive(testPricingFromGatewayInput, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("MU007_EPIC_PriceQuote_v1_getPriceQuoteWebservice_Binder_getPriceQuote");
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
					Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "getPriceQuoteInSAP()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTGetPriceQuoteServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "getPriceQuoteInSAP()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTGetPriceQuoteServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "getPriceQuoteInSAP()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTGetPriceQuoteServiceImpl class" + throwable.getMessage(), throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "getPriceQuoteInSAP()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return testPricingFromGatewayOutput;
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
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForPriceQuote) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.GetPriceQuote.WEBSERVICE_ORDER_URL));

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
		webserviceTemplateForPriceQuote.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.GetPriceQuote.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForPriceQuote.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}



}
