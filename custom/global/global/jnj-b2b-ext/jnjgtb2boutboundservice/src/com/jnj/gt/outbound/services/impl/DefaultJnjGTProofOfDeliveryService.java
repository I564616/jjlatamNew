/**
 * 
 */
package com.jnj.gt.outbound.services.impl;

import de.hybris.platform.util.Config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;

import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterReply;
import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterRequest;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.services.JnjGTProofOfDeliveryService;


/**
 * The JnjGTProofOfDeliveryServiceImpl class contains the definition of all the methods of the
 * JnjGTProofOfDeliveryService interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTProofOfDeliveryService implements JnjGTProofOfDeliveryService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProofOfDeliveryService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForProofOfDelivery;

	public WebServiceTemplate getWebserviceTemplateForProofOfDelivery() {
		return webserviceTemplateForProofOfDelivery;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 */
	@Override
	public SignatureProofOfDeliveryLetterReply retrieveSignatureProofOfDeliveryLetter(
			final SignatureProofOfDeliveryLetterRequest signatureProofOfDeliveryLetterRequest) throws IntegrationException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "retrieveSignatureProofOfDeliveryLetter()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		SignatureProofOfDeliveryLetterReply signatureProofOfDeliveryLetterReply = null;
		try
		{
			// Remove after testing and used for printing the xml.
			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterRequest.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(signatureProofOfDeliveryLetterRequest, System.out);
			// Please uncomment the below part to test it in local.
			/*
			 * // Code for testing the response logic through mocking the response xml. if
			 * (Config.getParameter(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING).equalsIgnoreCase(
			 * Jnjgtb2boutboundserviceConstants.Y_STRING)) { final File oldfile = new File(
			 * Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.MOCK_XML_CLASS_PATH)); final
			 * Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); signatureProofOfDeliveryLetterReply =
			 * (SignatureProofOfDeliveryLetterReply) jaxbUnmarshaller.unmarshal(oldfile); }// Else block executes always
			 * when data would be required from the SAP. else {
			 */
			loadConnectionParamsFromProp(webserviceTemplateForProofOfDelivery);
		/*	signatureProofOfDeliveryLetterReply = (SignatureProofOfDeliveryLetterReply) ((JAXBElement) webserviceTemplateForProofOfDelivery
					
					.marshalSendAndReceive(signatureProofOfDeliveryLetterRequest, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{
							((SoapMessage) arg0).setSoapAction("http://fedex.com/ws/track/v8/retrieveSignatureProofOfDeliveryLetter");
						}
					})).getValue();*/
			
			signatureProofOfDeliveryLetterReply = new SignatureProofOfDeliveryLetterReply();
			//}

			jaxbContext = JAXBContext.newInstance(com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterReply.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(signatureProofOfDeliveryLetterReply, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "retrieveSignatureProofOfDeliveryLetter()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in JnjGTProofOfDeliveryServiceImpl class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(
					Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "retrieveSignatureProofOfDeliveryLetter()" + Logging.HYPHEN
							+ "Web Service Client Exception Occured in JnjGTProofOfDeliveryServiceImpl class"
							+ webServiceClientException.getMessage(), webServiceClientException);
			throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "retrieveSignatureProofOfDeliveryLetter()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of JnjGTProofOfDeliveryServiceImpl class" + throwable.getMessage(),
					throwable);
			throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "retrieveSignatureProofOfDeliveryLetter()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return signatureProofOfDeliveryLetterReply;
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
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForProofOfDelivery) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.WEBSERVICE_ORDER_URL));

		messageSender.setAuthScope(AuthScope.ANY);
		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForProofOfDelivery.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.WEBSERVICE_ORDER_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForProofOfDelivery.setMessageSender(messageSender);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
