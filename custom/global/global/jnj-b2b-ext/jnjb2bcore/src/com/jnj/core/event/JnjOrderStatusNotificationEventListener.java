/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import jakarta.annotation.Resource;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjOrderStatusNotificationProcessModel;
import com.jnj.core.services.synchronizeOrders.impl.DefaultJnjSAPOrdersService;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderStatusNotificationEventListener extends AbstractSiteEventListener<JnjOrderStatusNotificationEvent>
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjSAPOrdersService.class);

	@Resource(name = "userService")
	UserService userService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected B2BOrderService b2boOrderService;

	@Autowired
	protected BusinessProcessService businessProcessService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected I18NService i18nService;



	public B2BOrderService getB2boOrderService() {
		return b2boOrderService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	@Override
	protected void onSiteEvent(final JnjOrderStatusNotificationEvent orderStatusChangeNotificationEvent)
	{

		final JnjOrderStatusNotificationProcessModel syncOrderStatusNotificationProcessModel = (JnjOrderStatusNotificationProcessModel) businessProcessService
				.createProcess("syncOrderStatusNotification" + "-" + System.currentTimeMillis(), "jnjOrderStatusNotificationProcess");

		syncOrderStatusNotificationProcessModel.setSite(orderStatusChangeNotificationEvent.getSite());
		syncOrderStatusNotificationProcessModel.setCustomer(orderStatusChangeNotificationEvent.getCustomer());
		syncOrderStatusNotificationProcessModel.setLanguage(orderStatusChangeNotificationEvent.getLanguage());
		syncOrderStatusNotificationProcessModel.setCurrency(orderStatusChangeNotificationEvent.getCurrency());
		syncOrderStatusNotificationProcessModel.setStore(orderStatusChangeNotificationEvent.getBaseStore());
		syncOrderStatusNotificationProcessModel.setCustomer(orderStatusChangeNotificationEvent.getCustomer());
		syncOrderStatusNotificationProcessModel.setBaseUrl(orderStatusChangeNotificationEvent.getBaseUrl());
		/*START AAOL 4911*/
		syncOrderStatusNotificationProcessModel.setToEmail(orderStatusChangeNotificationEvent.getToEmail());
		/*END AAOL 4911*/
		
		if (null != orderStatusChangeNotificationEvent.getOrderCode())
		{
			//userService.getAdminUser();
			//final OrderModel orderModel = b2boOrderService.getOrderForCode(orderStatusChangeNotificationEvent.getOrderCode());
			//final OrderModel orderModel = b2boOrderService.getOrderForCode("00002000");
			
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2boOrderService.getOrderForCode(orderStatusChangeNotificationEvent.getOrderCode());
				}
			}, userService.getAdminUser());
			
		
			if (null != orderModel)
			{
				syncOrderStatusNotificationProcessModel.setOrder(orderModel);
				CommonUtil.logDebugMessage("Order Status Notification", "onSiteEvent()",
						"Order Has been set in the syncOrderStatusNotificationProcessModel!", LOGGER);
			}
		}
		else
		{
			syncOrderStatusNotificationProcessModel.setClientOrderNumber(orderStatusChangeNotificationEvent.getClientOrderNumber());
			syncOrderStatusNotificationProcessModel.setCurrentStatus(orderStatusChangeNotificationEvent.getCurrentStatus());
			syncOrderStatusNotificationProcessModel.setPreviousStatus(orderStatusChangeNotificationEvent.getPreviousStatus());
			syncOrderStatusNotificationProcessModel.setClientOrderNumber(orderStatusChangeNotificationEvent.getClientOrderNumber());
			syncOrderStatusNotificationProcessModel.setSapOrderNumber(orderStatusChangeNotificationEvent.getSapOrderNumber());
			syncOrderStatusNotificationProcessModel.setJnjOrderNumber(orderStatusChangeNotificationEvent.getJnjOrderNumber());
			syncOrderStatusNotificationProcessModel.setSyncOrderNotification(orderStatusChangeNotificationEvent
					.getSyncOrderNotification() == null ? Boolean.FALSE : orderStatusChangeNotificationEvent
					.getSyncOrderNotification());
		}

		//syncOrderStatusNotificationProcessModel.setBaseUrl(orderStatusChangeNotificationEvent.getBaseUrl());
		//syncOrderStatusNotificationProcessModel.setMediaUrl(orderStatusChangeNotificationEvent.getMediaUrl());

		try
		{
			modelService.save(syncOrderStatusNotificationProcessModel);
			LOGGER.debug("Saving syncOrderStatusNotificationProcessModel.. " + syncOrderStatusNotificationProcessModel);
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error("Saving 'JnjInterfaceNotificationProcessModel' has caused exception: " + modelSavingException.getMessage());
		}

		try{
		LOGGER.debug("Start Processing mail for Order Status Notification.. " + orderStatusChangeNotificationEvent.getJnjOrderNumber());
		businessProcessService.startProcess(syncOrderStatusNotificationProcessModel);
		LOGGER.debug("End Processed mail successfully for Order Status Notification.. " + orderStatusChangeNotificationEvent.getJnjOrderNumber());
		}catch(Exception e){
			LOGGER.error("Exception occured while processing mail for the order .." + orderStatusChangeNotificationEvent.getJnjOrderNumber());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method generates the address string
	 * 
	 * @param addressData
	 * @return addressString
	 */
	protected String generateAddressString(final JnjGTAddressData addressData)
	{
		final StringBuilder addressBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(addressData.getCompanyName()))
		{
			addressBuilder.append(addressData.getCompanyName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getLine1()))
		{
			addressBuilder.append(addressData.getLine1());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getLine2()))
		{
			addressBuilder.append(addressData.getLine2());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getTown()))
		{
			addressBuilder.append(addressData.getTown());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (null != addressData.getRegion() && StringUtils.isNotEmpty(addressData.getRegion().getName()))
		{
			addressBuilder.append(addressData.getRegion().getName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (null != addressData.getCountry() && StringUtils.isNotEmpty(addressData.getCountry().getName()))
		{
			addressBuilder.append(addressData.getCountry().getName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getPostalCode()))
		{
			addressBuilder.append(addressData.getPostalCode());
		}
		String addressString;
		if (StringUtils.endsWith(String.valueOf(addressBuilder), Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE))
		{
			addressString = addressBuilder.substring(0, addressBuilder.length() - 2);
		}
		else
		{
			addressString = String.valueOf(addressBuilder);
		}
		return addressString;
	}

	@Override
	protected boolean shouldHandleEvent(final JnjOrderStatusNotificationEvent arg0)
	{
		return true;
	}

}
