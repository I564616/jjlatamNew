/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjInterfaceNotificationProcessModel;
import com.jnj.core.model.JnjReadOperationDashboardModel;
import com.jnj.core.model.JnjWriteOperationDashboardModel;
import com.jnj.facades.data.JnjReadDashboardData;
import com.jnj.facades.data.JnjWriteDashboardData;


/**
 * The context class for Interface Notification email Process.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjInterfaceNotificationContext extends CustomerEmailContext
{
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjInterfaceNotificationContext.class);

	/**
	 * Data bean to store data from <code>JnjReadOperationDashboard</code>
	 */
	private Collection<JnjReadDashboardData> readDashboardData;

	/**
	 * Data bean to store data from <code>JnjWriteOperationDashboard</code>
	 */
	private Collection<JnjWriteDashboardData> writeDashboardData;

	private static final String TO_EMAIL_ID = Config.getParameter(Jnjb2bCoreConstants.INTERFACE_NOTIFICATION_EMAIL_ID_KEY);

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_INIT = "Context - init()";
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof JnjInterfaceNotificationProcessModel)
		{
			final JnjInterfaceNotificationProcessModel interfaceNotificationProcessModel = (JnjInterfaceNotificationProcessModel) storeFrontCustomerProcessModel;
			//Setting Email Body Parameters
			populateReadDashboardData(interfaceNotificationProcessModel.getReadDashboardRecords());
			populateWriteDashboardData(interfaceNotificationProcessModel.getWriteDashboardRecords());
		}

		put(EMAIL, TO_EMAIL_ID);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * 
	 * @param readDashboardRecords
	 */
	private void populateReadDashboardData(final Set<JnjReadOperationDashboardModel> readDashboardRecords)
	{
		final Collection<JnjReadDashboardData> readDashboardData = new HashSet<JnjReadDashboardData>();

		for (final JnjReadOperationDashboardModel readDashboardRecord : readDashboardRecords)
		{
			final JnjReadDashboardData data = new JnjReadDashboardData();

			data.setFileName(readDashboardRecord.getFileName());
			data.setInterfaceName(readDashboardRecord.getInterfaceName());
			data.setStatus(readDashboardRecord.getStatus());
			data.setErrorMessage(readDashboardRecord.getErrorMessage());
			data.setDate(readDashboardRecord.getDate());

			readDashboardData.add(data);
		}
		setReadDashboardData(readDashboardData);
	}

	/**
	 * 
	 * @param writeDashboardRecords
	 */
	private void populateWriteDashboardData(final Set<JnjWriteOperationDashboardModel> writeDashboardRecords)
	{
		final Collection<JnjWriteDashboardData> writeDashboardData = new HashSet<JnjWriteDashboardData>();

		for (final JnjWriteOperationDashboardModel writeDashboardRecord : writeDashboardRecords)
		{
			final JnjWriteDashboardData data = new JnjWriteDashboardData();

			data.setFileName(writeDashboardRecord.getFileName());
			data.setInterfaceName(writeDashboardRecord.getInterfaceName());
			data.setIdocNumber(writeDashboardRecord.getIdocNumber());
			data.setErrorMessage(writeDashboardRecord.getErrorMessage());
			data.setDate(writeDashboardRecord.getDate());

			writeDashboardData.add(data);
		}
		setWriteDashboardData(writeDashboardData);
	}

	/**
	 * @return the readDashboardData
	 */
	public Collection<JnjReadDashboardData> getReadDashboardData()
	{
		return readDashboardData;
	}

	/**
	 * @param readDashboardData
	 *           the readDashboardData to set
	 */
	public void setReadDashboardData(final Collection<JnjReadDashboardData> readDashboardData)
	{
		this.readDashboardData = readDashboardData;
	}

	/**
	 * @return the writeDashboardData
	 */
	public Collection<JnjWriteDashboardData> getWriteDashboardData()
	{
		return writeDashboardData;
	}

	/**
	 * @param writeDashboardData
	 *           the writeDashboardData to set
	 */
	public void setWriteDashboardData(final Collection<JnjWriteDashboardData> writeDashboardData)
	{
		this.writeDashboardData = writeDashboardData;
	}



}
