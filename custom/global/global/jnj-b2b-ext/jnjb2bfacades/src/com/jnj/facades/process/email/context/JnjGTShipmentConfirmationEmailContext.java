/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjGTShipmentEmailDto;
import com.jnj.core.model.JnjGTShipmentConfirmationEmailBusinessProcessModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;


/**
 * This class represents the email context for the Back-Order Email process.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTShipmentConfirmationEmailContext extends CustomerEmailContext
{
	/**
	 * Constant for <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTShipmentConfirmationEmailContext.class);

	/**
	 * Collection of <code>JnjNAShipmentEmailDto</code> containing shipment data of confirmation notification.
	 */
	private List<JnjGTShipmentEmailDto> shipmentConfirmationEmailData;

	private List<String> b2bUsersList;


	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + "Start of Method-init()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjGTShipmentConfirmationEmailBusinessProcessModel)
		{
			final JnjGTShipmentConfirmationEmailBusinessProcessModel shipmentConfirmationEmailBusinessProcessModel = (JnjGTShipmentConfirmationEmailBusinessProcessModel) businessProcessModel;
			final List<JnjGTShipmentEmailDto> shipmentConfirmationEmailData = new ArrayList<>();

			String b2bUsersEmailList = null;


			final List<String> shipmentConfirmUsersEmailList = new ArrayList<>(
					shipmentConfirmationEmailBusinessProcessModel.getB2bUsersList());

			b2bUsersEmailList = StringUtils.join(shipmentConfirmUsersEmailList, ",");

			for (final String data : shipmentConfirmationEmailBusinessProcessModel.getShipmentConfirmationEmailData())
			{
				final JnjGTShipmentEmailDto dto = new JnjGTShipmentEmailDto();

				/***
				 * Split back the shipment email data from BPM with the same delimiter ('|'), and populate dto object from
				 * it.
				 ***/
				final String[] dataArray = StringUtils.split(data, Jnjb2bCoreConstants.SYMBOl_PIPE);
				dto.setOrderNumber(dataArray[0]);
				/*** Replace with an empty string if the product name is not present at the second index. ***/
				dto.setProductName(
						(dataArray[1] != null) ? (dataArray[1].equals("null") ? StringUtils.EMPTY : dataArray[1]) : StringUtils.EMPTY);
				dto.setProductCode(dataArray[2]);
				dto.setOrderType(dataArray[3]);
				dto.setBillingName(dataArray[4]);
				dto.setBillingAddress(dataArray[5]);
				dto.setShippingAddress(dataArray[6]);
				dto.setOrderChannel(dataArray[7]);
				dto.setGtin(dataArray[8]);
				dto.setShipUOM(dataArray[9]);
				dto.setEachUOM(dataArray[10]);
				dto.setEachDefinition(dataArray[11]);
				dto.setShippingMethod(dataArray[12]);
				dto.setTrackingNumber(dataArray[13]);
				dto.setUnit(dataArray[14]);
				dto.setCustomerPO(dataArray[15]);
				dto.setQuantity(dataArray[16]);
				dto.setDeliveryDate(dataArray[17]);
				dto.setHybrisOrder(dataArray[18]);
				dto.setBolNumber(dataArray[19]);
				shipmentConfirmationEmailData.add(dto);

			}

			setShipmentConfirmationEmailData(shipmentConfirmationEmailData);

			put(EMAIL, b2bUsersEmailList);
		}


		/** Set the From Email address i.e. customer email **/
		put(FROM_EMAIL, JnJCommonUtil.getValue(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_FROM_EMAIL));

		/** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
		put(FROM_DISPLAY_NAME, JnJCommonUtil.getValue(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_DISPLAY_NAME));
	}


	/**
	 * @return the shipmentConfirmationEmailData
	 */
	public List<JnjGTShipmentEmailDto> getShipmentConfirmationEmailData()
	{
		return shipmentConfirmationEmailData;
	}


	/**
	 * @param shipmentConfirmationEmailData
	 *           the shipmentConfirmationEmailData to set
	 */
	public void setShipmentConfirmationEmailData(final List<JnjGTShipmentEmailDto> shipmentConfirmationEmailData)
	{
		this.shipmentConfirmationEmailData = shipmentConfirmationEmailData;
	}

	public List<String> getB2bUsersList()
	{
		return b2bUsersList;
	}

	public void setB2bUsersList(final List<String> b2bUsersList)
	{
		this.b2bUsersList = b2bUsersList;
	}


}
