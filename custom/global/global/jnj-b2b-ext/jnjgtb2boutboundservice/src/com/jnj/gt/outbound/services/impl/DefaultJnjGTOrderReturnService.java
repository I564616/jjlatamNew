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
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPInput;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.services.JnjGTOrderReturnService;



/**
 * The JnjGTOrderReturnServiceImpl class contains the definition of all the methods of the JnjGTOrderReturnService
 * interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTOrderReturnService implements JnjGTOrderReturnService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderReturnService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForOrderReturn;

	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	public WebServiceTemplate getWebserviceTemplateForOrderReturn() {
		return webserviceTemplateForOrderReturn;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	/**
	 * {!{@inheritDoc}
	 *
	 * @param orderChangeInSAPInput
	 * @return OrderChangeInSAPOutput
	 * @throws IntegrationException
	 */
	@Override
	public OrderReturnInSAPOutput orderReturnInSAP(final OrderReturnInSAPInput orderReturnInSAPInput) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "orderReturnInSAP()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		OrderReturnInSAPOutput orderReturnInSAPOutput = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(OrderReturnInSAPInput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(orderReturnInSAPInput, System.out);

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				// Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.OrderReturn.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); orderReturnInSAPOutput =
				 * (OrderReturnInSAPOutput) jaxbUnmarshaller.unmarshal(oldfile);
				 */
				orderReturnInSAPOutput = new OrderReturnInSAPOutput();
				orderReturnInSAPOutput.setStatus("true");
				orderReturnInSAPOutput.setOutSalesOrderNumber("123456");
			
				//throw new IntegrationException();
				
			}// Else block executes always when data would be required from the SAP.
			else
			{
				//Commented only to check GT functionality as Web Services were not in place
				/*loadConnectionParamsFromProp(webserviceTemplateForOrderReturn);
				orderReturnInSAPOutput = (OrderReturnInSAPOutput) ((JAXBElement) webserviceTemplateForOrderReturn
						.marshalSendAndReceive(orderReturnInSAPInput, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("MU007_EPIC_OrderReturn_v1_orderReturnWebservice_Binder_orderReturnInSAP");
							}
						})).getValue();*/
				orderReturnInSAPOutput = new OrderReturnInSAPOutput();
				orderReturnInSAPOutput.setStatus("true");
				orderReturnInSAPOutput.setOutSalesOrderNumber("123456");

			}

			jaxbContext = JAXBContext.newInstance(OrderReturnInSAPOutput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(orderReturnInSAPOutput, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.ORDER_RETURN + Logging.HYPHEN + "orderReturnInSAP()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTOrderReturnServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.ORDER_RETURN + Logging.HYPHEN + "orderReturnInSAP()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTOrderReturnServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.ORDER_RETURN + Logging.HYPHEN + "orderReturnInSAP()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTOrderReturnServiceImpl class" + throwable.getMessage(), throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "orderReturnInSAP()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderReturnInSAPOutput;
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
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForOrderReturn) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.OrderReturn.WEBSERVICE_ORDER_URL));

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
		webserviceTemplateForOrderReturn.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.OrderReturn.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForOrderReturn.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}



}
