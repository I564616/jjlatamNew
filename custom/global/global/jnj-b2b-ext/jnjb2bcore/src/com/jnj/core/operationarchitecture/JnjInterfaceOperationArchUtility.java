/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.operationarchitecture;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;

import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjInterfaceOperationArchUtility
{
	/**
	 * Updates the <code>JnjWriteOperationDashboard</code> for a failed/successful XMl being parsed, with parameterized
	 * details received.
	 * 
	 * @param interfaceName
	 * @param fileName
	 * @param status
	 * @param errorMessage
	 * @return boolean
	 */
	void updateReadDashboard(final String interfaceName, final String fileName, final String status, final boolean isSuccess,
			final String errorMessage);

	/**
	 * Updates the Write Dashboard with the records failed in processing from SAP like intermediary table.
	 * 
	 * @param interfaceName
	 * @param fileName
	 * @param uid
	 * @param errorMessage
	 * @return
	 */
	boolean updateWriteDashboard(final String interfaceName, final String fileName, final String uid, final String errorMessage);

	/**
	 * Removes the invalid records from the system, using <code>ModelService</code> API, left out with status as
	 * <code>RecordStatus.LOADING</code>
	 * 
	 * @param invalidRecords
	 */
	void removeIntermediaryTableRecords(Collection<? extends ItemModel> invalidRecords, final String interfaceName);

	/**
	 * Saves new Read/Write Dashboard entry in the Hybris system.
	 * 
	 * @param dashboard
	 * @return
	 */
	boolean saveDashboard(ItemModel dashboard);

	/**
	 * 
	 */
	void cleanIntermediaryTableRecords() throws BusinessException;

	/**
	 * 
	 */
	void sendEmailNotification();
	
	/**
	 * Removes the invalid records from the system, using <code>ModelService</code> API, left out with status as
	 * <code>RecordStatus.LOADING</code>
	 * 
	 * @param invalidRecords
	 */
	void removeIntermediaryTableRecords(Collection<? extends ItemModel> invalidRecords, final boolean errorRecord);

}
