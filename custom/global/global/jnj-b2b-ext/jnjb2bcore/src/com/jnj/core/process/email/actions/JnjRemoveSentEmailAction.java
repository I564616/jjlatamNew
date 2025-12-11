/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.process.email.actions;

import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.actions.RemoveSentEmailAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Set;

import com.jnj.core.model.JnjInterfaceNotificationProcessModel;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjRemoveSentEmailAction extends RemoveSentEmailAction
{
	@Override
	public void executeAction(final BusinessProcessModel businessProcessModel)
	{
		boolean messageSent = false;
		for (final EmailMessageModel emailMessage : businessProcessModel.getEmails())
		{
			if (emailMessage.isSent())
			{
				messageSent = true;
				getModelService().remove(emailMessage);
			}
		}

		if (messageSent && (businessProcessModel instanceof JnjInterfaceNotificationProcessModel))
		{
			final JnjInterfaceNotificationProcessModel interfaceNotificationProcessModel = (JnjInterfaceNotificationProcessModel) businessProcessModel;
			setEmailNotificationSent(interfaceNotificationProcessModel);
		}
	}

	protected void setEmailNotificationSent(final JnjInterfaceNotificationProcessModel interfaceNotificationProcessModel)
	{
		final Set<JnjReadOperationDashboardModel> readDashboardModels = interfaceNotificationProcessModel.getReadDashboardRecords();
		for (final JnjReadOperationDashboardModel readDashboardModel : readDashboardModels)
		{
			readDashboardModel.setEmailNotificationSent(Boolean.TRUE);
		}

		getModelService().saveAll(readDashboardModels);


		final Set<JnjWriteOperationDashboardModel> writeDashboardModels = interfaceNotificationProcessModel
				.getWriteDashboardRecords();
		for (final JnjWriteOperationDashboardModel writeDashboardModel : writeDashboardModels)
		{
			writeDashboardModel.setEmailNotificationSent(Boolean.TRUE);
		}
		getModelService().saveAll(writeDashboardModels);
	}
}
