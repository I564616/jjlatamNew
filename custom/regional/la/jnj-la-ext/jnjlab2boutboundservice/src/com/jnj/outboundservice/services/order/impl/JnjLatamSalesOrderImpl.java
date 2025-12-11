package com.jnj.outboundservice.services.order.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationWrapperResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderIn;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingWrapperResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationWrapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationWrapperResponse;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.WebServiceConnection;
import com.jnj.outboundservice.services.order.JnjLatamSalesOrder;
import de.hybris.platform.util.Config;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.concurrent.TimeoutException;


public class JnjLatamSalesOrderImpl extends WebServiceGatewaySupport implements JnjLatamSalesOrder
{
	private static final Class currentClass = JnjLatamSalesOrderImpl.class;

	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private SalesOrderCreationResponse lasalesOrderCreationResponse;

	@Autowired
	private SalesOrderSimulationResponse lasalesOrderSimulationResponse;

	@Autowired
	private SalesOrderPricingResponse salesOrderPricingResponse;

	@Autowired
	protected WebServiceTemplate lawebserviceTemplate;

	private Marshaller lamarshaller;
	
	private static final String HYBRIS_ORDER_NUMBER = "Hybris order number: ";

	/**
	 * @return the lamarshaller
	 */
	public Marshaller getLamarshaller()
	{
		return lamarshaller;
	}

	/**
	 * @param lamarshaller
	 *           the lamarshaller to set
	 */
	public void setLamarshaller(final Marshaller lamarshaller)
	{
		this.lamarshaller = lamarshaller;
	}

	@Override
	public SalesOrderPricingResponse salesOrderPricingWrapper(final SalesOrderPricingRequest salesOrderPricingRequest)
			throws IntegrationException, TimeoutException
	{
		final String methodName = "salesOrderPricingWrapper()";
		JnjGTCoreUtil.logDebugMessage("Order pricing", methodName,
				Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN +
						Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		SalesOrderPricingWrapperResponse salesOrderPricingWrapperResponse = null;
		SalesOrderPricingResponse salesOrderPricingResponse = null;
		try
		{
			final SalesOrderPricingWrapper salesOrderPricingWrapper = new SalesOrderPricingWrapper();
			salesOrderPricingWrapper.setSalesOrderPricingRequest(salesOrderPricingRequest);
			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller lamarshaller;
			jaxbContext = JAXBContext.newInstance(SalesOrderPricingWrapper.class);
			lamarshaller = jaxbContext.createMarshaller();
			lamarshaller.marshal(salesOrderPricingWrapper, System.out);
			loadConnectionParamsFromProp(lawebserviceTemplate);
			salesOrderPricingWrapperResponse = (SalesOrderPricingWrapperResponse) ((JAXBElement) lawebserviceTemplate
					.marshalSendAndReceive(salesOrderPricingWrapper, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{
							((SoapMessage) arg0).setSoapAction(
									"SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderPricingWrapper");
						}
					})).getValue();
			jaxbContext = JAXBContext.newInstance(SalesOrderPricingWrapperResponse.class);
			lamarshaller = jaxbContext.createMarshaller();
			lamarshaller.marshal(salesOrderPricingWrapperResponse, System.out);
		}
		catch (final JAXBException jaxbException)
		{
			JnjGTCoreUtil.logErrorMessage("Order pricing", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "JAXB Exception Occurred in JnjSalesOrderImpl class",
					jaxbException, currentClass);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage("Order pricing", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Illegal Argument Exception Occurred in JnjSalesOrderImpl class",
					illegalArgumentException, currentClass);
			throw new IntegrationException();
		}
		catch (final WebServiceClientException webServiceClientException)
		{
			JnjGTCoreUtil.logErrorMessage("Order pricing", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Web Service Client Exception Occurred in JnjSalesOrderImpl class",
					webServiceClientException, currentClass);
			throw new IntegrationException();
		}
		catch (final TimeoutException timeoutException)
		{
			JnjGTCoreUtil.logErrorMessage("Order pricing", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Time out Exception Occurred in JnjSalesOrderImpl class",
					timeoutException, currentClass);
			throw new TimeoutException();
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage("Order pricing", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Exception caught in Throwable block of JnjSalesOrderImpl class",
					throwable, currentClass);
			throw new IntegrationException();
		}

		if (null != salesOrderPricingWrapperResponse)
		{
			salesOrderPricingResponse = salesOrderPricingWrapperResponse.getSalesOrderPricingResponse();
		}

		JnjGTCoreUtil.logDebugMessage("Order pricing", methodName,
				Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
						+ JnJCommonUtil.getCurrentDateTime(), currentClass);
		return salesOrderPricingResponse;

	}

	@Override
	public SalesOrderCreationResponse salesOrderCreationWrapper(final SalesOrderCreationRequest salesOrderCreationRequest)
			throws IntegrationException
	{
		final String methodName = "salesOrderCreationWrapper()";
		JnjGTCoreUtil.logDebugMessage("Order creation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		SalesOrderCreationWrapperResponse responseWrapper;
		SalesOrderCreationResponse salesOrderCreationResponse = null;
		final SalesOrderCreationWrapper salesOrderCreationWrapper = new SalesOrderCreationWrapper();
		
		final SalesOrderIn salesOrderIn=salesOrderCreationRequest.getSalesOrderIn();
		final JAXBElement<String> customerPortalNumber=salesOrderIn.getInCustomerPortalOrdernumber();
		final String hybrisOrderNumber = customerPortalNumber.getValue().toString();
		
		JnjGTCoreUtil.logInfoMessage("ORDER CREATION", methodName,
				"HYBRIS ORDER NUMBER: ########### " + hybrisOrderNumber, currentClass);
		
		try
		{
			final StringWriter stringWriter = new StringWriter();
			
			loadConnectionParamsFromProp(lawebserviceTemplate);
			
			salesOrderCreationWrapper.setSalesOrderCreationRequest(salesOrderCreationRequest);

			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller lamarshaller;

			jaxbContext = JAXBContext.newInstance(SalesOrderCreationWrapper.class);
			lamarshaller = jaxbContext.createMarshaller();
			lamarshaller.marshal(salesOrderCreationWrapper, stringWriter);
			
			JnjGTCoreUtil.logInfoMessage("String writer", methodName,
					"ORDER CREATION " + "[" + hybrisOrderNumber + "]" + " - Request XML:######################### " + stringWriter.toString(), currentClass);

			responseWrapper = (SalesOrderCreationWrapperResponse) ((JAXBElement) lawebserviceTemplate
					.marshalSendAndReceive(salesOrderCreationWrapper, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{
							((SoapMessage) arg0).setSoapAction(
									"SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderCreationWrapper");
						}
					})).getValue();

			jaxbContext = JAXBContext.newInstance(SalesOrderCreationWrapperResponse.class);
			lamarshaller = jaxbContext.createMarshaller();
			lamarshaller.marshal(responseWrapper, stringWriter);
			
			JnjGTCoreUtil.logInfoMessage("String writer", methodName,
					"ORDER CREATION " + "[" + hybrisOrderNumber + "]" + " - Response XML: ####################" + stringWriter.toString(), currentClass);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage("Exception in order creation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + HYBRIS_ORDER_NUMBER + hybrisOrderNumber + Logging.HYPHEN + "Illegal Argument Exception Occurred in JnjSalesOrderImpl class",
					illegalArgumentException, currentClass);
			
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			JnjGTCoreUtil.logErrorMessage("Exception in order creation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + HYBRIS_ORDER_NUMBER + hybrisOrderNumber + Logging.HYPHEN + "Web Service Client Exception Occurred in JnjSalesOrderImpl class",
					webServiceClientException, currentClass);
			
			throw new IntegrationException();
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage("Excpetion in order creation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + HYBRIS_ORDER_NUMBER + hybrisOrderNumber + Logging.HYPHEN + "Exception caught in Throwable block of JnjSalesOrderImpl class",
					throwable, currentClass);
			throw new IntegrationException();
		}
		if (null != responseWrapper)
		{
			salesOrderCreationResponse = responseWrapper.getSalesOrderCreationResponse();
		}

		JnjGTCoreUtil.logInfoMessage("Order creation", methodName, Logging.SUBMIT_ORDER
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return salesOrderCreationResponse;
	}

	@Override
	public SalesOrderSimulationResponse salesOrderSimulationWrapper(final SalesOrderSimulationRequest salesOrderSimulationRequest)
			throws IntegrationException, TimeoutException
	{
		final String methodName = "salesOrderSimulationWrapper()";

		JnjGTCoreUtil.logDebugMessage("Order simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		SalesOrderSimulationWrapperResponse salesOrderSimulationWrapperResponse;
		SalesOrderSimulationResponse simulationResponse = null;
		try
		{
			final SalesOrderSimulationWrapper salesOrderSimulationWrapper = new SalesOrderSimulationWrapper();
			salesOrderSimulationWrapper.setSalesOrderSimulationRequest(salesOrderSimulationRequest);
			// Remove after testing
			JAXBContext jaxbContext;
			Marshaller lamarshaller;
			final StringWriter stringWriter = new StringWriter();
			jaxbContext = JAXBContext.newInstance(SalesOrderSimulationWrapper.class);
			lamarshaller = jaxbContext.createMarshaller();
			lamarshaller.marshal(salesOrderSimulationWrapper, stringWriter);

			JnjGTCoreUtil.logInfoMessage("Order simulation", methodName,
					"ORDER SIMULATION - Request XML:######################### " + stringWriter.toString(), currentClass);
			loadConnectionParamsFromProp(lawebserviceTemplate);
			salesOrderSimulationWrapperResponse = (SalesOrderSimulationWrapperResponse) ((JAXBElement) lawebserviceTemplate
					.marshalSendAndReceive(salesOrderSimulationWrapper, new WebServiceMessageCallback()
					{
						@Override
						public void doWithMessage(final WebServiceMessage arg0)
						{
							((SoapMessage) arg0).setSoapAction(
									"SG910_BtB_IN0498_SalesOrder_Global_Source_v1_webService_salesOrderWS_Binder_salesOrderSimulationWrapper");
						}
					})).getValue();
			jaxbContext = JAXBContext.newInstance(SalesOrderSimulationWrapperResponse.class);
			lamarshaller = jaxbContext.createMarshaller();

			JnjGTCoreUtil.logInfoMessage("Marshaller", methodName, "marshaller for response=" + lamarshaller, currentClass);
			lamarshaller.marshal(salesOrderSimulationWrapperResponse, stringWriter);
			JnjGTCoreUtil.logInfoMessage("String writer", methodName,
					"ORDER SIMULATION - Response XML: ####################" + stringWriter.toString(), currentClass);

			JnjGTCoreUtil.logInfoMessage("Response", methodName,
					"salesOrderSimulationWrapperResponse=" + salesOrderSimulationWrapperResponse, currentClass);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage("Order Simulation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Illegal Argument Exception Occurred in JnjSalesOrderImpl class",
					illegalArgumentException, currentClass);
			throw new IntegrationException();
		}

		catch (final WebServiceClientException webServiceClientException)
		{
			JnjGTCoreUtil.logErrorMessage("Order Simulation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Web Service Client Exception Occurred in JnjSalesOrderImpl class",
					webServiceClientException, currentClass);
			throw new IntegrationException();
		}
		catch (final TimeoutException timeoutException)
		{
			JnjGTCoreUtil.logErrorMessage("Order Simulation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Time out Exception Occurred in JnjSalesOrderImpl class",
					timeoutException, currentClass);
			throw new TimeoutException();
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage("Order simulation", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Exception caught in Throwable block of JnjSalesOrderImpl class",
					throwable, currentClass);
			throw new IntegrationException();
		}
		if (null != salesOrderSimulationWrapperResponse)
		{
			simulationResponse = salesOrderSimulationWrapperResponse.getSalesOrderSimulationResponse();
		}
		JnjGTCoreUtil.logDebugMessage("Order simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		return simulationResponse;

	}

	@SuppressWarnings("deprecation")
	private void loadConnectionParamsFromProp(final WebServiceTemplate lawebServiceTemplate) throws Exception
	{
		final String methodName = "loadConnectionParamsFromProp()";
		JnjGTCoreUtil.logDebugMessage("Load connection", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		final CommonsHttpMessageSender lamessageSender = new CommonsHttpMessageSender();

		JnjGTCoreUtil.logDebugMessage("Load connection", methodName,
				"URL= " + Config.getParameter(Jnjlab2bcoreConstants.WebServiceConnection.WEBSERVICE_ORDER_URL), currentClass);

		final Credentials lacredentials = new UsernamePasswordCredentials(
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_USER),
				Config.getParameter(Jnjb2bCoreConstants.WebServiceConnection.WEBSERVICE_ORDER_PWD));
		lamessageSender.setCredentials(lacredentials);
		lamessageSender.setAuthScope(AuthScope.ANY);
		lamessageSender.setReadTimeout(
				Config.getInt(WebServiceConnection.WEBSERVICE_READ_TIMEOUT, WebServiceConnection.WEBSERVICE_DEFAULT_TIMEOUT));
		lamessageSender.setConnectionTimeout(
				Config.getInt(WebServiceConnection.WEBSERVICE_CONNECTION_TIMEOUT, WebServiceConnection.WEBSERVICE_DEFAULT_TIMEOUT));
		lamessageSender.getHttpClient().getParams().setAuthenticationPreemptive(true);
		lawebserviceTemplate.setDefaultUri(Config.getParameter(Jnjlab2bcoreConstants.WebServiceConnection.WEBSERVICE_ORDER_URL));
		lamessageSender.afterPropertiesSet();
		lawebserviceTemplate.setMessageSender(lamessageSender);

		JnjGTCoreUtil.logDebugMessage("Load connection", methodName,
				Logging.SUBMIT_ORDER + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.END_OF_METHOD
						+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
	}
}