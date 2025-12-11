/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.email.notification;

import java.util.Collection;

import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjEmailNotificationDao
{
	/**
	 * 
	 * @return
	 */
	Collection<JnjReadOperationDashboardModel> getReadDashboardRecordsForNotification();

	/**
	 * 
	 * @return
	 */
	Collection<JnjWriteOperationDashboardModel> getWriteDashboardRecordsForNotification();
}
