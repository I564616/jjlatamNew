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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;
import com.jnj.exceptions.IntegrationException;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Serialization;
import com.jnj.gt.outbound.services.JnjGTSerializationService;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequest;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequestResponse;

import de.hybris.platform.util.Config;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTSerializationService implements JnjGTSerializationService {
	
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSerializationService.class);
	@Autowired
	private WebServiceTemplate webserviceTemplateForSerialization;
	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	public WebServiceTemplate getWebserviceTemplateForSerialization() {
		return webserviceTemplateForSerialization;
	}

	public JnjConfigServiceImpl getJnjConfigServiceImpl() {
		return jnjConfigServiceImpl;
	}
	/* (non-Javadoc)
	 * @see com.jnj.gt.outbound.services.JnjGTSerializationService#getSerialResponse(com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequest)
	 */
	@Override
	public ProcessSNVerificationRequestResponse getSerialResponse(ProcessSNVerificationRequest processSNRequest) {
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Serialization.VERIFY_SERIAL_RESPONSE+ Logging.HYPHEN + "getSerialResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		ProcessSNVerificationRequestResponse processSNVerificationResponse = null;
		try
		{

			JAXBContext jaxbContext;
			Marshaller marshaller;

			jaxbContext = JAXBContext.newInstance(ProcessSNVerificationRequest.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(processSNRequest, System.out);

			if (StringUtils.equalsIgnoreCase(Jnjgtb2boutboundserviceConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjgtb2boutboundserviceConstants.STOP_SAP_OUTBOUND_CALLING)))
			{
				
				throw new IntegrationException();
			}// Else block executes always when data would be required from the ATTP
			else
			{
				loadConnectionParamsFromProp(webserviceTemplateForSerialization);
				processSNVerificationResponse = (ProcessSNVerificationRequestResponse) ((JAXBElement) webserviceTemplateForSerialization
						.marshalSendAndReceive(processSNRequest, new WebServiceMessageCallback()
						{
							@Override
							public void doWithMessage(final WebServiceMessage arg0)
							{
								((SoapMessage) arg0)
										.setSoapAction("PU023_ATLAS_SN_Verification_v1_webservices_SNVerificationRequestResponse_Binder_processSNVerificationRequest");
							}
						})).getValue();

			}

			jaxbContext = JAXBContext.newInstance(ProcessSNVerificationRequestResponse.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(processSNVerificationResponse, System.out);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(
					Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "getSerialResponse()" + Logging.HYPHEN
							+ "Illegal Argument Exception Occured in DefaultJnjGTSerializationService class"
							+ illegalArgumentException.getMessage(), illegalArgumentException);
//			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			LOGGER.error(Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "getSerialResponse()" + Logging.HYPHEN
					+ "Web Service Client Exception Occured in DefaultJnjGTSerializationService class"
					+ webServiceClientException.getMessage(), webServiceClientException);
			//throw new IntegrationException();
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "getSerialResponse()" + Logging.HYPHEN
					+ "Exception caught in Throwable block of DefaultJnjGTSerializationService class" + throwable.getMessage(),
					throwable);
			//throw new IntegrationException();
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "simulateDeliveredOrders()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return processSNVerificationResponse;
	}
	
	/**
	 * Load connection params from property file.
	 * 
	 * @param webserviceTemplateForSimulateDelivered
	 *           the webservice template for simulate delivered
	 * @param wsData
	 *           YTODO
	 * @throws Exception
	 *            the exception
	 */
	@SuppressWarnings("deprecation")
	protected void loadConnectionParamsFromProp(final WebServiceTemplate webserviceTemplateForSerialization) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CommonsHttpMessageSender messageSender = new CommonsHttpMessageSender();
		LOGGER.debug("User= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_USER));
		LOGGER.debug("Password= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_PWD));
		LOGGER.debug("URL= " + Config.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_URL));

		final Credentials credentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_USER),
				Config.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_PWD));
		messageSender.setCredentials(credentials);
		messageSender.setAuthScope(AuthScope.ANY);

		/*messageSender.setConnectionTimeout(Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		messageSender.setReadTimeout(Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));

		LOGGER.info("Delivered : Simulate Order > Used Connection Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getConnectionTimeOutKey())));
		LOGGER.info("Delivered : Simulate Order > Used Read Time Out : "
				+ Integer.parseInt(Config.getParameter(wsData.getReadTimeOutKey())));*/


		messageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		webserviceTemplateForSerialization.setDefaultUri(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.Serialization.WEBSERVICE_SERIAL_URL));
		messageSender.afterPropertiesSet();
		webserviceTemplateForSerialization.setMessageSender(messageSender);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Serialization.VERIFY_SERIAL_RESPONSE + Logging.HYPHEN + "loadConnectionParamsFromProp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

}
