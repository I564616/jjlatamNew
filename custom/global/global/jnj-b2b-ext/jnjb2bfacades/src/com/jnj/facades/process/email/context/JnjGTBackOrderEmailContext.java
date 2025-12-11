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
import com.jnj.core.dto.JnjGTBackorderEmailDto;
import com.jnj.core.model.JnjGTBackOrderEmailBusinessProcessModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;



/**
 * This class represents the email context for the Back-Order Email process.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBackOrderEmailContext extends CustomerEmailContext
{
	/**
	 * Constant for <code>Logger</code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTBackOrderEmailContext.class);

	/**
	 * Collection of <code>JnjGTBackorderEmailDto</code> containing email data of back-order notification.
	 */
	protected List<JnjGTBackorderEmailDto> backorderEmailData;
	protected String openLinebackorderCheck;

	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + "Start of Method-init()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjGTBackOrderEmailBusinessProcessModel)
		{
			final JnjGTBackOrderEmailBusinessProcessModel backOrderEmailBusinessProcessModel = (JnjGTBackOrderEmailBusinessProcessModel) businessProcessModel;
			final List<JnjGTBackorderEmailDto> backorderEmailData = new ArrayList<>();
			if (backOrderEmailBusinessProcessModel.getOpenlineBackorderCheck().equals("false"))
			{
				setOpenLinebackorderCheck("false");
				for (final String data : backOrderEmailBusinessProcessModel.getBackOrderEmailData())
				{
					final JnjGTBackorderEmailDto dto = new JnjGTBackorderEmailDto();

					/***
					 * Split back the back-order email data from BPM with the same delimiter ('|'), and populate dto object
					 * from it.
					 ***/
					final String[] dataArray = StringUtils.split(data, Jnjb2bCoreConstants.SYMBOl_PIPE);
					dto.setOrderNumber(dataArray[0]);
					/*** Replace with an empty string if the product name is not present at the second index. ***/
					dto.setProductName((dataArray[1] != null) ? (dataArray[1].equals("null") ? StringUtils.EMPTY : dataArray[1])
							: StringUtils.EMPTY);
					dto.setProductCode(dataArray[2]);
					dto.setAvailabilityDate(dataArray[3]);

					/*
					 * dto.setAccountNumber(dataArray[4]); fields added for JJEPIC -825 backorder email notification
					 * dto.setOperatingCompany(dataArray[5]); dto.setCustomerPO(dataArray[6]); dto.setQuantity(dataArray[7]);
					 * dto.setUnit(dataArray[8]); dto.setItemPrice(dataArray[9]); dto.setExtendedPrice(dataArray[10]);
					 * dto.setShipDate(dataArray[11]); dto.setDeliveryDate(dataArray[12]); dto.setStatus(dataArray[13]);
					 * dto.setAdditionComments(dataArray[14]);
					 */
					backorderEmailData.add(dto);

				}
			}
			else
			{
				setOpenLinebackorderCheck("true");
				for (final String data : backOrderEmailBusinessProcessModel.getBackOrderEmailData())
				{
					final JnjGTBackorderEmailDto dto = new JnjGTBackorderEmailDto();

					/***
					 * Split back the back-order email data from BPM with the same delimiter ('|'), and populate dto object
					 * from it.
					 ***/
					final String[] dataArray = StringUtils.split(data, Jnjb2bCoreConstants.SYMBOl_PIPE);
					dto.setOrderNumber(dataArray[0]);
					/*** Replace with an empty string if the product name is not present at the second index. ***/
					dto.setProductName((dataArray[1] != null) ? (dataArray[1].equals("null") ? StringUtils.EMPTY : dataArray[1])
							: StringUtils.EMPTY);
					dto.setProductCode(dataArray[2]);
					dto.setAvailabilityDate(dataArray[3]);
					dto.setAccountNumber(dataArray[4]);
					/* fields added for JJEPIC -825 backorder email notification */
					dto.setOperatingCompany(dataArray[5]);
					dto.setCustomerPO(dataArray[6]);
					dto.setQuantity(dataArray[7]);
					dto.setUnit(dataArray[8]);
					dto.setItemPrice(dataArray[9]);
					dto.setExtendedPrice(dataArray[10]);
					dto.setShipDate(dataArray[11]);
					dto.setDeliveryDate(dataArray[12]);
					dto.setStatus(dataArray[13]);
					dto.setAdditionComments(dataArray[14]);
					dto.setOrderDate(dataArray[15]);

					backorderEmailData.add(dto);
				}
			}
			setBackorderEmailData(backorderEmailData);
		}

		/** Set the From Email address i.e. customer email **/
		put(FROM_EMAIL, JnJCommonUtil.getValue(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_ADDRESS));

		/** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
		put(FROM_DISPLAY_NAME, JnJCommonUtil.getValue(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_NAME));
	}

	/**
	 * @return the backorderEmailData
	 */
	public List<JnjGTBackorderEmailDto> getBackorderEmailData()
	{
		return backorderEmailData;
	}

	/**
	 * @param backorderEmailData
	 *           the backorderEmailData to set
	 */
	public void setBackorderEmailData(final List<JnjGTBackorderEmailDto> backorderEmailData)
	{
		this.backorderEmailData = backorderEmailData;
	}

	/**
	 * @return the openLinebackorderCheck
	 */
	public String getOpenLinebackorderCheck()
	{
		return openLinebackorderCheck;
	}

	/**
	 * @param openLinebackorderCheck
	 *           the openLinebackorderCheck to set
	 */
	public void setOpenLinebackorderCheck(final String openLinebackorderCheck)
	{
		this.openLinebackorderCheck = openLinebackorderCheck;
	}

}
