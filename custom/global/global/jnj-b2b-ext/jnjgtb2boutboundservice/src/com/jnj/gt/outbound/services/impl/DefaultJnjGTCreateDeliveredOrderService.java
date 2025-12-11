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
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersInput;

import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.gt.outbound.services.JnjGTCreateDeliveredOrderService;


/**
 * The JnjGTCreateDeliveredOrderServiceImpl class contains the definition of all the methods of the
 * JnjGTCreateDeliveredOrderService interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTCreateDeliveredOrderService implements JnjGTCreateDeliveredOrderService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateDeliveredOrderService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForCreateDelivered;
	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	public WebServiceTemplate getWebserviceTemplateForCreateDelivered() {
		return webserviceTemplateForCreateDelivered;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public CreateDeliveredOrdersOutput createDeliveredOrders(final CreateDeliveredOrdersInput createDeliveredOrdersInput,
			final JnjGTSapWsData sapWsData) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDeliveredOrders()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		CreateDeliveredOrdersOutput createDeliveredOrdersOutput = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(CreateDeliveredOrdersInput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(createDeliveredOrdersInput, System.out);

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				// Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new File(
				 * Config.getParameter(Jnjnab2boutboundserviceConstants.CreateDeliveredOrder.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); createDeliveredOrdersOutput =
				 * (CreateDeliveredOrdersOutput) jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForCreateDelivered, sapWsData);
				createDeliveredOrdersOutput = (CreateDeliveredOrdersOutput) ((JAXBElement) webserviceTemplateForCreateDelivered
						.marshalSendAndReceive(createDeliveredOrdersInput, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("MU007_EPIC_CreateDelOrder_v1_createDeliveredOrdersWebservice_Binder_createDeliveredOrders");
							}
						})).getValue();

			}

			jaxbContext = JAXBContext.newInstance(CreateDeliveredOrdersOutput.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(createDeliveredOrdersOutput, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDeliveredOrders()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTCreateDeliveredOrderServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDeliveredOrders()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTCreateDeliveredOrderServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDeliveredOrders()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTCreateDeliveredOrderServiceImpl class" + throwable.getMessage(),
					throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDeliveredOrders()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return createDeliveredOrdersOutput;
	}

	/**
	 * Load connection params from property file.
	 *
	 * @param webserviceTemplateForCreateDelivered
	 *           the webservice template for create delivered
	 * @param wsData
	 *           the ws data
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForCreateDelivered,
			final JnjGTSapWsData wsData) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.CreateDeliveredOrder.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);

		//		if (null != wsData && wsData.isTimeOutExtended())
		//		{
		//			messageSender.setConnectionTimeout(Integer.parseInt(Config
		//					.getParameter(Jnjnab2boutboundserviceConstants.WebServiceConnection.CREATE_WS_EXTENDED_CONNECTION_TIME_OUT)));
		//			messageSender.setReadTimeout(Integer.parseInt(Config
		//					.getParameter(Jnjnab2boutboundserviceConstants.WebServiceConnection.ORDER_WS_EXTENDED_READ_TIME_OUT)));
		//		}
		//		else
		//		{
		//			messageSender.setConnectionTimeout(Integer.parseInt(Config
		//					.getParameter(Jnjnab2boutboundserviceConstants.WebServiceConnection.CREATE_WS_STANDARD_CONNECTION_TIME_OUT)));
		//			messageSender.setReadTimeout(Integer.parseInt(Config
		//					.getParameter(Jnjnab2boutboundserviceConstants.WebServiceConnection.ORDER_WS_STANDARD_READ_TIME_OUT)));
		//		}

		messageSender.setConnectionTimeout(Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		messageSender.setReadTimeout(Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		LOGGER.info("Delivered : Create Order > Used Connection Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		LOGGER.info("Delivered : Create Order > Used Read Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForCreateDelivered.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CreateDeliveredOrder.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForCreateDelivered.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
