/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.facades;

import de.hybris.platform.util.Config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.core.model.JnjInterfaceCronJobModel;
import com.jnj.gt.enums.JnjGTRecordsLoadingStatus;
import com.jnj.gt.service.common.impl.DefaultJnjGTStgSerivce;


/**
 * @author Accenture
 * @version 1.0
 */

public abstract class JnjGTIntFacade
{

	protected final int REC_STATUS_LOADING = 0;
	protected final int REC_STATUS_PENDING = 1;
	protected final int REC_STATUS_PROCESSED = 2;

	@Autowired
	private DefaultJnjGTStgSerivce jnjGTStgSerivce;

	/**
	 * Start interface feed.
	 * 
	 * Note: we've made it final because this method must be used as it is in all the feed.
	 * 
	 * @param impexFileName
	 *           the impex file name
	 */
	public final void startInterfaceFeed(final String impexFileName, final JnjInterfaceCronJobModel interfaceCronJob)
	{

		if (JnjGTRecordsLoadingStatus.COMPLETE.equals(interfaceCronJob.getRecordsLoadingStatus())
				|| JnjGTRecordsLoadingStatus.STAGE_TO_INTERMEDIATE.equals(interfaceCronJob.getRecordsLoadingStatus()))
		{
			//cleanInvalidRecords(RecordStatus.LOADING);
			// Setting read status to Ready to read
			if (interfaceCronJob.getCode().equals(
					Config.getParameter(Jnjgtb2binboundserviceConstants.Order.EXECUTE_ORDER_PROCEDURE_CRONJOB_NAME)))
			{
				jnjGTStgSerivce.executeStgOrdersProcedures();
			}
			else
			{
				jnjGTStgSerivce.updateReadStatusForStgTables(interfaceCronJob.getAssociatedStgTables(), REC_STATUS_PENDING,
						REC_STATUS_LOADING);
			}
			readImpexFile(impexFileName, interfaceCronJob.getCode());
			// Setting read status to processed
			jnjGTStgSerivce.updateReadStatusForStgTables(interfaceCronJob.getAssociatedStgTables(), REC_STATUS_PROCESSED,
					REC_STATUS_PENDING);
		}
		if (JnjGTRecordsLoadingStatus.COMPLETE.equals(interfaceCronJob.getRecordsLoadingStatus())
				|| JnjGTRecordsLoadingStatus.INTERMEDIATE_TO_HYBRIS.equals(interfaceCronJob.getRecordsLoadingStatus()))
		{
			System.out.println("Starting moving data from intermidiate tables to Hybris Application tables.");
			processIntermediaryRecords();
		}
	}

	/**
	 *
	 */
	public void readImpexFile(final String impexFilePath, final String interfaceName)
	{
		jnjGTStgSerivce.loadFeedData(impexFilePath, interfaceName);
	}

	public void cleanInvalidRecords(final RecordStatus recordStatus)
	{
		cleanInvalidRecords(recordStatus, null);
	}

	/**
	 * Request service class to clean all invalid records having <code>RecordStatus</code> as
	 * <code>RecordStatus.LOADING</code> from an Intermediary table.
	 */
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		//Override in subclass. Default Implementation
	}

	/**
	 *
	 */
	public void processIntermediaryRecords()
	{
		//Override in subclass. Default Implementation
	}

	/**
	 * @return the jnjGTStgSerivce
	 */
	public DefaultJnjGTStgSerivce getJnjGTStgSerivce()
	{
		return jnjGTStgSerivce;
	}

	/**
	 * @param jnjGTStgSerivce
	 *           the jnjGTStgSerivce to set
	 */
	public void setJnjGTStgSerivce(final DefaultJnjGTStgSerivce jnjGTStgSerivce)
	{
		this.jnjGTStgSerivce = jnjGTStgSerivce;
	}

}
