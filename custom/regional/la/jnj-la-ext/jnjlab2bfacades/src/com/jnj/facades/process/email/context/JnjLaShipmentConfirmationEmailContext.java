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
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * This class represents the email context for the Back-Order Email process.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLaShipmentConfirmationEmailContext extends CustomerEmailContext
{
	/**
	 * Constant for <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjLaShipmentConfirmationEmailContext.class);

	/**
	 * Collection of <code>JnjNAShipmentEmailDto</code> containing shipment data of confirmation notification.
	 */
	private List<JnjGTShipmentEmailDto> shipmentConfirmationEmailData;

	private List<String> b2bUsersList;


	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		final String METHOD_NAME = "init()";
		JnjGTCoreUtil.logDebugMessage(METHOD_NAME, "initialized context",
				"Start of Method-init()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(),
				JnjLaShipmentConfirmationEmailContext.class);


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
				dto.setProductName((dataArray[1] != null) ? (("null").equals(dataArray[1]) ? StringUtils.EMPTY : dataArray[1])
						: StringUtils.EMPTY);
				dto.setProductCode(dataArray[2]);
				dto.setOrderType(dataArray[3]);
				dto.setBillingName(dataArray[4]);
				dto.setBillingAddress((dataArray[5] != null) ? (("nullnull").equals(dataArray[5]) ? StringUtils.EMPTY : dataArray[5])
						: StringUtils.EMPTY);
				dto.setShippingAddress((dataArray[6] != null) ? (("nullnull").equals(dataArray[6]) ? StringUtils.EMPTY : dataArray[6])
						: StringUtils.EMPTY);
				dto.setOrderChannel((dataArray[7] != null) ? (("null").equals(dataArray[7]) ? StringUtils.EMPTY : dataArray[7])
						: StringUtils.EMPTY);
				dto.setQuantity((dataArray[8] != null) ? (("null").equals(dataArray[8]) ? StringUtils.EMPTY : dataArray[8])
						: StringUtils.EMPTY);
				dto.setDeliveryDate((dataArray[9] != null) ? (("null").equals(dataArray[9]) ? StringUtils.EMPTY : dataArray[9])
						: StringUtils.EMPTY);
				dto.setHybrisOrder((dataArray[10] != null) ? (("null").equals(dataArray[10]) ? StringUtils.EMPTY : dataArray[10])
						: StringUtils.EMPTY);

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
		return new ArrayList<>(shipmentConfirmationEmailData);
	}


	/**
	 * @param shipmentConfirmationEmailData
	 *           the shipmentConfirmationEmailData to set
	 */
	public void setShipmentConfirmationEmailData(final List<JnjGTShipmentEmailDto> shipmentConfirmationEmailData)
	{
		this.shipmentConfirmationEmailData = new ArrayList<>(shipmentConfirmationEmailData);
	}

	public List<String> getB2bUsersList()
	{
		return new ArrayList<>(b2bUsersList);
	}

	public void setB2bUsersList(final List<String> b2bUsersList)
	{
		this.b2bUsersList = new ArrayList<>(b2bUsersList);
	}


}
