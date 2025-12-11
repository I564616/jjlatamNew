
package com.jnj.core.outbound.services.impl;

import de.hybris.platform.util.Config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;

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
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.ContractPriceReply;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapper;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapperResponse;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.outbound.services.JnjGTGetContractPriceService;


/**
 * The JnjGTGetContractPriceServiceImpl class contains the definition of all the methods of the
 * JnjGTGetContractPriceService interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTGetContractPriceService implements JnjGTGetContractPriceService
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTGetContractPriceService.class);
	@Autowired
	protected WebServiceTemplate webserviceTemplateForContractPrice;

	public WebServiceTemplate getWebserviceTemplateForContractPrice() {
		return webserviceTemplateForContractPrice;
	}

	/**
	 * {!{@inheritDoc}.
	 * 
	 * @param getContractPriceWrapper
	 *           the get contract price wrapper
	 * @return the gets the contract price wrapper response
	 * @throws IntegrationException
	 *            the integration exception
	 */
	@Override
	public GetContractPriceWrapperResponse getContractPriceWrapper(final GetContractPriceWrapper getContractPriceWrapper)
			throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "getContractPriceWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		GetContractPriceWrapperResponse getContractPriceWrapperResponse = null;
		LOGGER.debug("1 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));
	if(!Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(GetContractPriceWrapper.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(getContractPriceWrapper, System.out);
			// Code for testing the response logic through mocking the response xml.
			if (Config.getParameter(Jnjb2bCoreConstants.STOP_SAP_OUTBOUND_CALLING)
					.equalsIgnoreCase(Jnjb2bCoreConstants.Y_STRING))
			{
				/*
				 * final File oldfile = new
				 * File(Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.MOCK_XML_CLASS_PATH)); final Unmarshaller
				 * jaxbUnmarshaller = jaxbContext.createUnmarshaller(); getContractPriceWrapperResponse =
				 * (GetContractPriceWrapperResponse) jaxbUnmarshaller.unmarshal(oldfile);
				 */
				throw new IntegrationException();
			}// Else block executes always when data would be required from the SAP.
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForContractPrice);
				getContractPriceWrapperResponse = (GetContractPriceWrapperResponse) ((JAXBElement) webserviceTemplateForContractPrice
						.marshalSendAndReceive(getContractPriceWrapper, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("MU007_EPIC_ContractPrice_v1_getContractPriceWrapperWebservice_Binder_getContractPriceWrapper");
							}
						})).getValue();

			}

			jaxbContext = JAXBContext.newInstance(GetContractPriceWrapperResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(getContractPriceWrapperResponse, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "getContractPriceWrapper()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTGetContractPriceServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "getContractPriceWrapper()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTGetContractPriceServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "getContractPriceWrapper()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTGetContractPriceServiceImpl class" + throwable.getMessage(),
					throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "getContractPriceWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		
	}
	LOGGER.debug("2 Byepass flag " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL));
	if(Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.BYEPASS_PRICE_SERVICE_CALL).equalsIgnoreCase("true")){
		 getContractPriceWrapperResponse = new GetContractPriceWrapperResponse();
	}
		QName fooQName = new QName("http://www.example.org/schema", "123456789");
        JAXBElement<String> fooValue = new JAXBElement<String>(fooQName, String.class, "123456789");
        
        QName fooQName1 = new QName("http://www.example.org/schema", "890.00");
        JAXBElement<String> fooValue1 = new JAXBElement<String>(fooQName1, String.class, "890.00");
        
        QName fooQName2 = new QName("http://www.example.org/schema", "1");
        JAXBElement<String> fooValue2 = new JAXBElement<String>(fooQName, String.class, "1");
        
		ContractPriceReply mockContact = new ContractPriceReply();
		mockContact.setContractNumber(fooValue);	
		mockContact.setContractPrice(fooValue1);
		mockContact.setPer(fooValue2);
		getContractPriceWrapperResponse.setContractPriceReply(mockContact);
		getContractPriceWrapperResponse.setStatus("true");
		getContractPriceWrapperResponse.setErrorMessage(null);
		return getContractPriceWrapperResponse;
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
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForContractPrice) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjb2bCoreConstants.GetContractPrice.WEBSERVICE_ORDER_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.setConnectionTimeout(Integer.parseInt(Config
				.getParameter(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE_WEBSERVICE_CONNECTION_TIME_OUT)));
		messageSender.setReadTimeout(Integer.parseInt(Config
				.getParameter(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE_WEBSERVICE_READ_TIME_OUT)));
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForContractPrice.setDefaultUri(Config
				.getParameter(Jnjb2bCoreConstants.GetContractPrice.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForContractPrice.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
