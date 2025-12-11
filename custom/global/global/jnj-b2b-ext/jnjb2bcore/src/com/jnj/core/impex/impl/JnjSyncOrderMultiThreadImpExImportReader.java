/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.impex.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImportProcessor;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import org.apache.log4j.Logger;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSyncOrderMultiThreadImpExImportReader extends MultiThreadedImpExImportReader
{
	private static final Logger LOG = Logger.getLogger(JnjSyncOrderMultiThreadImpExImportReader.class.getName());

	/**
	 * @param reader
	 * @param legacyMode
	 */
	public JnjSyncOrderMultiThreadImpExImportReader(final CSVReader reader, final boolean legacyMode)
	{
		super(reader, legacyMode);
	}

	public JnjSyncOrderMultiThreadImpExImportReader(final CSVReader reader, final CSVWriter dumpWriter,
			final MultiThreadedImportProcessor processor, final boolean legacyMode)
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
			handleErrorScenario();
			throw e;
		}
	}

	private void handleErrorScenario()
	{
		LOG.error("Impex Exception Occured");
	}
}