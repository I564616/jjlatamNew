/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.impex.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ImportProcessor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.imp.ValueLine.ValueEntry;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSyncOrderImpExImportReader extends ImpExImportReader
{
	protected static final Logger LOGGER = Logger.getLogger(JnjSyncOrderImpExImportReader.class.getName());
	protected static final String INVALID_ORDER_NUMBER = "InvalidOrderNumber";
	protected SessionService sessionService;
	protected JnjInterfaceOperationArchUtility interfaceOperationArchUtility;

	/**
	 * @param reader
	 * @param legacyMode
	 */
	public JnjSyncOrderImpExImportReader(final CSVReader reader, final boolean legacyMode)
	{
		super(reader, legacyMode);
		this.setSessionService((SessionService) Registry.getApplicationContext().getBean("sessionService"));
		this.setInterfaceOperationArchUtility((JnjInterfaceOperationArchUtility) Registry.getApplicationContext().getBean(
				"jnjInterfaceOperationArchUtility"));
	}

	public JnjSyncOrderImpExImportReader(final CSVReader reader, final CSVWriter dumpWriter, final ImportProcessor processor,
			final boolean legacyMode)
	{
		super(reader, dumpWriter, processor, legacyMode);
	}


	@Override
	public Object readLine() throws ImpExException
	{
		try
		{
			return super.readLine();
		}
		catch (final ImpExException e)
		{
			handleIntefaceReadException(e);
			throw e;
		}
	}

	protected void handleIntefaceReadException(final Exception e)
	{
		LOGGER.info(Logging.IMPEX_IMPORT + Logging.HYPHEN
				+ "IMPEX Import has caused an exception, starting read fail over handling.");

		final ValueLine currentValueLine = this.getCurrentValueLine();
		final ValueEntry currentValueEntry = currentValueLine.getValueEntry(1);

		//Set currentValueLine.getCellValue(); in session. 
		getSessionService().setAttribute(INVALID_ORDER_NUMBER, currentValueEntry.getCellValue());

		//Need to set fileName.
		getInterfaceOperationArchUtility().updateReadDashboard(Logging.IMPEX_IMPORT, currentValueEntry.getCellValue(),
				RecordStatus.ERROR.toString(), false, e.getMessage());

	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public JnjInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	public void setInterfaceOperationArchUtility(final JnjInterfaceOperationArchUtility interfaceOperationArchUtility)
	{
		this.interfaceOperationArchUtility = interfaceOperationArchUtility;
	}



}