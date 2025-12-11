/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.email.notification;

import java.util.List;

import com.jnj.exceptions.BusinessException;


/**
 * Interface for Email Notification. Holds the methods related to email functionality.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjEmailNotificationFacade
{

	/**
	 * Sends the email notification for interface for any failover scenario.
	 * 
	 * @param interfaceName
	 *           the interface name
	 * @param errorFileList
	 *           the error file list
	 * @throws BusinessException
	 *            the business exception
	 */
	void sendEmailNotificationForInterface(final String interfaceName, final List<String> errorFileList) throws BusinessException;

	/**
	 * Sends the email notification for interface for any failover scenario.
	 * 
	 * @param interfaceName
	 *           the interface name
	 * @param errorFile
	 *           the error file
	 * @throws BusinessException
	 *            the business exception
	 */
	void sendEmailNotificationForInterface(final String interfaceName, final String errorFile) throws BusinessException;
}
