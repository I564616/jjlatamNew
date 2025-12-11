/**
 * 
 */
package com.jnj.gt.outbound.services.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
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
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.services.JnjGTReqOrderChangeService;

import de.hybris.platform.util.Config;



/**
 * The JnjGTReqOrderChangeServiceImpl class contains the definition of all the methods of the JnjGTReqOrderChangeService
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTReqOrderChangeService implements JnjGTReqOrderChangeService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTReqOrderChangeService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForOrderChange;

	public WebServiceTemplate getWebserviceTemplateForOrderChange() {
		return webserviceTemplateForOrderChange;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @param orderChangeInSAPInput
	 * @return OrderChangeInSAPOutput
	 * @throws IntegrationException
	 */
	@Override
	public OrderChangeInSAPOutput requestOrderChange(final OrderChangeInSAPInput orderChangeInSAPInput)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "requestOrderChange()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		OrderChangeInSAPOutput orderChangeInSAPOutput = null;
		//byepass the sap service call temp for GT
		LOGGER.debug("1 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));
		if(!Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){
   		try {
   			// Remove after testing and used for printing the xml.
   			JAXBContext jaxbContext;
   			Marshaller marshaller;
   
   			jaxbContext = JAXBContext.newInstance(OrderChangeInSAPInput.class);
   			marshaller = jaxbContext.createMarshaller();
   			marshaller.marshal(orderChangeInSAPInput, System.out);
   			// Code for testing the response logic through mocking the response xml.
   			if (Config.getParameter(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING).equalsIgnoreCase(
   					Jnjgtb2boutboundserviceConstants.Y_STRING))
   			{
   				/*
   				 * final File oldfile = new
   				 * File(Config.getParameter(Jnjgtb2boutboundserviceConstants.OrderChange.MOCK_XML_CLASS_PATH)); final
   				 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); orderChangeInSAPOutput =
   				 * (OrderChangeInSAPOutput) jaxbUnmarshaller.unmarshal(oldfile);
   				 */
   				throw new IntegrationException();
   			}// Else block executes always when data would be required from the SAP.
   			else
   			{
   				loadConnectionParamsFromProp(webserviceTemplateForOrderChange);
   				orderChangeInSAPOutput = (OrderChangeInSAPOutput) ((JAXBElement) webserviceTemplateForOrderChange
   						.marshalSendAndReceive(orderChangeInSAPInput, new WebServiceMessageCallback()
   						{
   							@Override
   							public void doWithMessage(final WebServiceMessage arg0)
   							{
   								((SoapMessage) arg0)
   										.setSoapAction("MU007_EPIC_OrderCancel_v1_requestOrderChangeWebservice_Binder_requestOrderChange");
   							}
   						})).getValue();
   
   			}
   
   			jaxbContext = JAXBContext.newInstance(OrderChangeInSAPOutput.class);
   			marshaller = jaxbContext.createMarshaller();
   			marshaller.marshal(orderChangeInSAPOutput, System.out);
   		}
   		catch (final IllegalArgumentException illegalArgumentException) {
   			LOGGER.error(
   					Logging.ORDER_CHANGE + Logging.HYPHEN + "requestOrderChange()" + Logging.HYPHEN
   							+ "Illegal Argument Exception Occured in JnjGTReqOrderChangeServiceImpl class"
   							+ illegalArgumentException.getMessage(), illegalArgumentException);
   			throw new IntegrationException();
   		}
   		catch (final WebServiceClientException webServiceClientException) {
   			LOGGER.error(
   					Logging.ORDER_CHANGE + Logging.HYPHEN + "requestOrderChange()" + Logging.HYPHEN
   							+ "Web Service Client Exception Occured in JnjGTReqOrderChangeServiceImpl class"
   							+ webServiceClientException.getMessage(), webServiceClientException);
   			throw new IntegrationException();
   		}
   		catch (final Throwable throwable) {
   			LOGGER.error(Logging.ORDER_CHANGE + Logging.HYPHEN + "requestOrderChange()" + Logging.HYPHEN
   					+ "Exception caught in Throwable block of JnjGTReqOrderChangeServiceImpl class" + throwable.getMessage(),
   					throwable);
   			throw new IntegrationException();
   		}
		}
   
      LOGGER.debug("2 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));
   	if(Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){
   		orderChangeInSAPOutput = new OrderChangeInSAPOutput();
   	}
	
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "requestOrderChange()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderChangeInSAPOutput;
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
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForOrderChange) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.OrderChange.WEBSERVICE_ORDER_URL));

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
		webserviceTemplateForOrderChange.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.OrderChange.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForOrderChange.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}



}
