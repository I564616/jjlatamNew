/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.Set;

import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;


/**
 * The Class JnjInterfaceNotificationEvent used during Email Process for Interface Notifications.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjInterfaceNotificationEvent extends AbstractCommerceUserEvent
{
	/**
	 * 
	 */
	protected Set<JnjReadOperationDashboardModel> readDashboardRecords;

	/**
	 * 
	 */
	protected Set<JnjWriteOperationDashboardModel> writeDashboardRecords;

	/**
	 * @return the readDashboardRecords
	 */
	public Set<JnjReadOperationDashboardModel> getReadDashboardRecords()
	{
		return readDashboardRecords;
	}

	/**
	 * @param readDashboardRecords
	 *           the readDashboardRecords to set
	 */
	public void setReadDashboardRecords(final Set<JnjReadOperationDashboardModel> readDashboardRecords)
	{
		this.readDashboardRecords = readDashboardRecords;
	}

	/**
	 * @return the writeDashboardRecords
	 */
	public Set<JnjWriteOperationDashboardModel> getWriteDashboardRecords()
	{
		return writeDashboardRecords;
	}

	/**
	 * @param writeDashboardRecords
	 *           the writeDashboardRecords to set
	 */
	public void setWriteDashboardRecords(final Set<JnjWriteOperationDashboardModel> writeDashboardRecords)
	{
		this.writeDashboardRecords = writeDashboardRecords;
	}

}
