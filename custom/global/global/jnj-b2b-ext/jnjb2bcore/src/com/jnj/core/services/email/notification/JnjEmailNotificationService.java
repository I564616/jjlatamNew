/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.email.notification;

import com.jnj.exceptions.BusinessException;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjEmailNotificationService
{

	/**
	 * Sends the email notification for over interface Data load status. Extracts report from:
	 * <ul>
	 * <li>JnjReadOperationDashboard</li>
	 * <li>JnjWriteOperationDashboard</li>
	 * 
	 * 
	 * @throws BusinessException
	 */
	void sendEmailNotification();
}
