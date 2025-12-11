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

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjGTOrderShipmentEmailBusinessProcessModel;


/**
 * This class represents the email context for the Order Shipment Email process.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderShipmentEmailContext extends CustomerEmailContext
{
	/**
	 * Constant for <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTOrderShipmentEmailContext.class);

	/**
	 * SAP based order number.
	 */
	private String orderNumber;

	/**
	 * SAP based order number.
	 */
	private String orderCode;

	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + "Start of Method-init()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjGTOrderShipmentEmailBusinessProcessModel)
		{
			final JnjGTOrderShipmentEmailBusinessProcessModel orderShipmentEmailBusinessProcessModel = (JnjGTOrderShipmentEmailBusinessProcessModel) businessProcessModel;
			setOrderCode(orderShipmentEmailBusinessProcessModel.getOrderCode());
			setOrderNumber(orderShipmentEmailBusinessProcessModel.getOrderNumber());
		}

		/** Set the From Email address i.e. customer email **/
		put(FROM_EMAIL, JnJCommonUtil.getValue(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_ADDRESS));

		/** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
		put(FROM_DISPLAY_NAME, JnJCommonUtil.getValue(Jnjb2bCoreConstants.ORDER360_FROM_EMAIL_NAME));
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	public void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

}
