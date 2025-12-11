/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.enums.OrderStatus;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderStatusNotificationEvent extends AbstractCommerceUserEvent
{

	/**
	 * Order status before processing.
	 */
	private OrderStatus previousStatus;

	/**
	 * Order status post processing.
	 */
	private OrderStatus currentStatus;

	/**
	 * JnJ Order Number.
	 */
	private String jnjOrderNumber;

	/**
	 * SAP based/originated Order Number.
	 */
	private String sapOrderNumber;

	/**
	 * Client Order/PO Number.
	 */
	private String clientOrderNumber;

	/**
	 * Base URL for the BR/MX Site.
	 */
	private String baseUrl;

	/**
	 * Indicates if the email trigger is from Synchronize Order or other flow.
	 */
	private Boolean syncOrderNotification;

	/**
	 * Indicates the media URL for Logo
	 */

	private String mediaUrl;

	/**
	 * Indicates the Order code
	 */
	private String orderCode;
	

	/**
	 * Indicates the To Email
	 */
	private String toEmail;

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

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *           the mediaUrl to set
	 */
	public void setMediaUrl(final String mediaUrl)
	{
		this.mediaUrl = mediaUrl;
	}

	/**
	 * @return the previousStatus
	 */
	public OrderStatus getPreviousStatus()
	{
		return previousStatus;
	}

	/**
	 * @param previousStatus
	 *           the previousStatus to set
	 */
	public void setPreviousStatus(final OrderStatus previousStatus)
	{
		this.previousStatus = previousStatus;
	}

	/**
	 * @return the currentStatus
	 */
	public OrderStatus getCurrentStatus()
	{
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *           the currentStatus to set
	 */
	public void setCurrentStatus(final OrderStatus currentStatus)
	{
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the jnjOrderNumber
	 */
	public String getJnjOrderNumber()
	{
		return jnjOrderNumber;
	}

	/**
	 * @param jnjOrderNumber
	 *           the jnjOrderNumber to set
	 */
	public void setJnjOrderNumber(final String jnjOrderNumber)
	{
		this.jnjOrderNumber = jnjOrderNumber;
	}

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
	}

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
	 * @return the baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the syncOrderNotification
	 */
	public Boolean getSyncOrderNotification()
	{
		return syncOrderNotification;
	}

	/**
	 * @param syncOrderNotification
	 *           the syncOrderNotification to set
	 */
	public void setSyncOrderNotification(final Boolean syncOrderNotification)
	{
		this.syncOrderNotification = syncOrderNotification;
	}

	
	/**
	 * @return the toEmail
	 */
	/**
	 * @return
	 */
	public String getToEmail() {
		return toEmail;
	}

	/**
	 * @param toEmail
	 * 			the toEmail to set
	 */		
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
}
