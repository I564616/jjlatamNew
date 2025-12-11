/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.model.process.StoreFrontProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTComprehensiveOrderConfirmationEmailBusinessProcessModel;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.utils.CommonUtil;



/**
 * This class represents the email context for the Back-Order Email process.
 *
 * @author hreddy4
 * @version 1.0
 */
public class JnjGTComprehensiveOrderConfirmationEmailContext extends AbstractEmailContext<StoreFrontProcessModel>
{


	/**
	 * Constant for <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTComprehensiveOrderConfirmationEmailContext.class);
	private static final String FROM_DISPLAY_NAME_VALUE = Config
			.getParameter(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_DISPLAY_NAME);
	protected JnjGTOrderData orderData;

	protected String orderStatus = null;

	protected String layManOrderChannel = null;


	/**
	 * @return the layManOrderChannel
	 */
	public String getLayManOrderChannel()
	{
		return layManOrderChannel;
	}

	/**
	 * @param layManOrderChannel
	 *           the layManOrderChannel to set
	 */
	public void setLayManOrderChannel(final String layManOrderChannel)
	{
		this.layManOrderChannel = layManOrderChannel;
	}

	/**
	 * Client Order/PO Number.
	 */
	protected String clientOrderNumber;

	@Autowired
	protected Converter<OrderModel, OrderData> orderConverter;

	/**
	 * @return the clientOrderNumber
	 */
	public String getClientOrderNumber()
	{
		return clientOrderNumber;
	}

	/**
	 * @param clientOrderNumber
	 *           the clientOrderNumber to set
	 */
	public void setClientOrderNumber(final String clientOrderNumber)
	{
		this.clientOrderNumber = clientOrderNumber;
	}

	/**
	 * @return the orderData
	 */
	public JnjGTOrderData getOrderData()
	{
		return orderData;
	}

	/**
	 * @param orderData
	 *           the orderData to set
	 */
	public void setOrderData(final JnjGTOrderData orderData)
	{
		this.orderData = orderData;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *           the orderStatus to set
	 */
	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	@Override
	public void init(final StoreFrontProcessModel storeFrontProcessModel, final EmailPageModel emailPageModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + "Start of Method-init()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(storeFrontProcessModel, emailPageModel);
		if (storeFrontProcessModel instanceof JnjGTComprehensiveOrderConfirmationEmailBusinessProcessModel)
		{
			String b2bUsersEmail = null;

			final JnjGTComprehensiveOrderConfirmationEmailBusinessProcessModel OrderConfirmationEmailBusinessProcessModel = (JnjGTComprehensiveOrderConfirmationEmailBusinessProcessModel) storeFrontProcessModel;
			final List<String> orderConfirmationEmailList = new ArrayList<>(
					OrderConfirmationEmailBusinessProcessModel.getB2bUsersList());

			b2bUsersEmail = StringUtils.join(orderConfirmationEmailList, ",");

			setLayManOrderChannel(OrderConfirmationEmailBusinessProcessModel.getLayManOrderChannel());
			orderStatus = OrderConfirmationEmailBusinessProcessModel.getOrderStatus();
			setOrderStatus(orderStatus);

			final OrderModel order = OrderConfirmationEmailBusinessProcessModel.getOrder();
			if (null != OrderConfirmationEmailBusinessProcessModel.getOrder())
			{

				final OrderData populatedOrderData = orderConverter.convert(order, new JnjGTOrderData());

				if (populatedOrderData instanceof JnjGTOrderData)
				{

					final JnjGTOrderData orderData = (JnjGTOrderData) populatedOrderData;
					setOrderData(orderData);
					setClientOrderNumber(orderData.getCode());
					CommonUtil.logDebugMessage("Order Confirmation Notification", "onSiteEvent()",
							"Order Data Has been set in the JnjGTComprehensiveOrderConfirmationEmailContext!", LOGGER);
				}
			}
			put(EMAIL, b2bUsersEmail);// configured email address

			/** Setting the To Display Name **/
			put(DISPLAY_NAME, Config.getParameter(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_DISPLAY_NAME));// configured Display Names

			/** Setting the From Email address **/
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_FROM_EMAIL));// configured email address
			/** Setting the from display name **/
			put(FROM_DISPLAY_NAME, FROM_DISPLAY_NAME_VALUE);// configured Name for the Display Name
		}
	}


	@Override
	protected BaseSiteModel getSite(final StoreFrontProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final StoreFrontProcessModel businessProcessModel)
	{
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(final StoreFrontProcessModel businessProcessModel)
	{
		return null;
	}

}
