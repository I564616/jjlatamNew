/**
 * 
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.core.model.order.OrderModel;

import jakarta.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.HEADERDETAILS;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercancel_v1.requestorderchangewebservice.OrderChangeInSAPOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.data.JnjGTOrderChangeResponseData;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.gt.outbound.mapper.JnjGTReqOrderChangeMapper;
import com.jnj.gt.outbound.services.JnjGTReqOrderChangeService;



/**
 * The JnjGTReqOrderChangeMapperImpl class contains the definition of all the method of the JnjGTReqOrderChangeMapper
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTReqOrderChangeMapper implements JnjGTReqOrderChangeMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTReqOrderChangeMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private JnjGTReqOrderChangeService jnjGTReqOrderChangeService;

	public JnjGTReqOrderChangeService getJnjGTReqOrderChangeService() {
		return jnjGTReqOrderChangeService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 */
	@Override
	public JnjGTOrderChangeResponseData mapChangeOrderRequestResponse(final OrderModel orderModel, final String sapOrderNumber,
			final boolean isCallAfterCreateOrder) throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "mapChangeOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTOrderChangeResponseData jnjGTOrderChangeResponseData = new JnjGTOrderChangeResponseData();
		final OrderChangeInSAPInput orderChangeInSAPInput = new OrderChangeInSAPInput();
		if (StringUtils.isNotEmpty(orderModel.getPurchaseOrderNumber()) && !isCallAfterCreateOrder)
		{
			final JAXBElement<String> purchaseOrderNumber = objectFactory.createOrderChangeInSAPInputInPONumber(orderModel
					.getPurchaseOrderNumber());
			orderChangeInSAPInput.setInPONumber(purchaseOrderNumber);
		}
		else
		{
			orderChangeInSAPInput.setInPONumber(objectFactory
					.createOrderChangeInSAPInputInPONumber((Jnjgtb2boutboundserviceConstants.EMPTY_STRING)));
		}
		orderChangeInSAPInput.setInSAPOrdernumber(sapOrderNumber);
		// calling the private method to map the surgeon data.
		final JAXBElement<HEADERDETAILS> headerDetailsJaxb = objectFactory
				.createOrderChangeInSAPInputHEADERDETAILS(mapSurgeonDataWithHeaderDetails(orderModel));
		orderChangeInSAPInput.setInHoldCode(objectFactory
				.createOrderChangeInSAPInputInHoldCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		orderChangeInSAPInput.setInReasonforRejection(objectFactory
				.createOrderChangeInSAPInputInReasonforRejection(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		orderChangeInSAPInput.setHEADERDETAILS(headerDetailsJaxb);
		// Calling the Service to get the update the surgeon info in SAP.
		final OrderChangeInSAPOutput orderChangeInSAPOutput = jnjGTReqOrderChangeService.requestOrderChange(orderChangeInSAPInput);  //sap call
		if (null != orderChangeInSAPOutput
				&& (null == orderChangeInSAPOutput.getErrorMessage() || Jnjgtb2boutboundserviceConstants.EMPTY_STRING
						.equals(orderChangeInSAPOutput.getErrorMessage().getValue())))
		{
			jnjGTOrderChangeResponseData.setSapResponseStatus(true);
		}
		else if (null != orderChangeInSAPOutput && null != orderChangeInSAPOutput.getErrorMessage())
		{
			jnjGTOrderChangeResponseData.setSapResponseStatus(false);
			jnjGTOrderChangeResponseData.setErrorMessage(orderChangeInSAPOutput.getErrorMessage().getValue());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "mapChangeOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTOrderChangeResponseData;
	}

	/**
	 * Map surgeon data with header details.
	 * 
	 * @param orderModel
	 *           the order model
	 * @return the header details
	 */
	protected HEADERDETAILS mapSurgeonDataWithHeaderDetails(final OrderModel orderModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "mapSurgeonDataWithHeaderDetails()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		HEADERDETAILS headerDetails = null;
		if (null != orderModel.getSurgeon())
		{
			headerDetails = new HEADERDETAILS();
			final JnjGTSurgeonModel jnjGTSurgeonModel = orderModel.getSurgeon();
			headerDetails.setCASEDATE(jnjGTSurgeonModel.getCaseDate());
			headerDetails.setINTERBODY(jnjGTSurgeonModel.getInterBody());
			headerDetails.setLEVELINSTRU(jnjGTSurgeonModel.getLevelsInstrumented());
			headerDetails.setZZONE(jnjGTSurgeonModel.getZone());
			headerDetails.setORTHOBIO(jnjGTSurgeonModel.getOrthoBio());
			headerDetails.setPATHOLOGY(jnjGTSurgeonModel.getPathology());
			headerDetails.setSURGEONID(jnjGTSurgeonModel.getSurgeonId());
			headerDetails.setSURGEONNAME(getSurgeonName(jnjGTSurgeonModel.getFirstName(), jnjGTSurgeonModel.getMiddleName(),
					jnjGTSurgeonModel.getLastName()));
			headerDetails.setSURGEONSPEC(jnjGTSurgeonModel.getSpeciality());
			headerDetails.setSURGICALAPPR(jnjGTSurgeonModel.getApproach());
			headerDetails.setEMAIL(jnjGTSurgeonModel.getEmailAddress());
			headerDetails.setZZFILE(jnjGTSurgeonModel.getCaseNumber());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "mapSurgeonDataWithHeaderDetails()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return headerDetails;
	}


	/**
	 * Gets the surgeon name.
	 * 
	 * @param firstName
	 *           the first name
	 * @param middleName
	 *           the middle name
	 * @param lastName
	 *           the last name
	 * @return the surgeon name
	 */
	protected String getSurgeonName(final String firstName, final String middleName, final String lastName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "getSurgeonName()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String finalName = StringUtils.EMPTY;

		if (StringUtils.isNotEmpty(firstName))
		{
			finalName = firstName;
		}
		if (StringUtils.isNotEmpty(middleName))
		{
			finalName = finalName.concat(" ").concat(middleName);
		}
		if (StringUtils.isNotEmpty(lastName))
		{
			finalName = finalName.concat(" ").concat(lastName);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_CHANGE + Logging.HYPHEN + "getSurgeonName()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return finalName.trim();
	}

}
