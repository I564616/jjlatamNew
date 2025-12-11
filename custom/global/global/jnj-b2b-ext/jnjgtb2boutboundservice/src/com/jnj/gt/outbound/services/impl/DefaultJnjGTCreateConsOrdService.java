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
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InRequest;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.OutResponse;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.gt.outbound.services.JnjGTCreateConsOrdService;


/**
 * The JnjGTCreateConsOrdServiceImpl class contains the definition of all the methods of the JnjGTCreateConsOrdService
 * interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTCreateConsOrdService implements JnjGTCreateConsOrdService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateConsOrdService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForConsumerCreate;
	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;
	

	public WebServiceTemplate getWebserviceTemplateForConsumerCreate() {
		return webserviceTemplateForConsumerCreate;
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
	public OutResponse createOrder(final InRequest inRequest, final JnjGTSapWsData wsData) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OutResponse outResponse = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(InRequest.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(inRequest, System.out);

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				// Code for testing the response logic through mocking the response xml.
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.CreateConsOrd.MOCK_XML_CLASS_PATH)); final
				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); outResponse = (OutResponse)
				 * jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForConsumerCreate, wsData);
				outResponse = (OutResponse) ((JAXBElement) webserviceTemplateForConsumerCreate.marshalSendAndReceive(inRequest,
						new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("SG910_BtBEPIC_IN2903_OrderCreate_HYBRIS_Source_v1_webservices_createOrder_WS_Binder_createOrder");
							}
						})).getValue();

			}

			jaxbContext = JAXBContext.newInstance(OutResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(outResponse, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.CREATE_CONS_ORD + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTCreateConsOrdServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.CREATE_CONS_ORD + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTCreateConsOrdServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTCreateConsOrdServiceImpl class" + throwable.getMessage(), throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "createOrder()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return outResponse;
	}

	/**
	 * Load connection params from property file.
	 *
	 * @param wsData
	 *           YTODO
	 * @param webServiceTemplate
	 *           the web service template
	 *
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForConsumerCreate,
			final JnjGTSapWsData wsData) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= "
				+ Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CONSUMER_USER));
		LOGGER.debug("Password= "
				+ Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CONSUMER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.CreateConsOrd.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CONSUMER_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CONSUMER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);

		messageSender.setConnectionTimeout(Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		messageSender.setReadTimeout(Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		LOGGER.info("Consumer : Create Order > Used Connection Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		LOGGER.info("Consumer : Create Order > Used Read Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));


		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForConsumerCreate.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CreateConsOrd.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForConsumerCreate.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


}
