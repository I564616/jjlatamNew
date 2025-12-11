/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.util;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;
import java.util.Date;

/**
 * TODO:<Akash-class level comments are missing>.
 *
 * @author mpanda3
 * @version 1.0
 */
public interface JnjLaInterfaceOperationArchUtility extends JnjInterfaceOperationArchUtility
{

	String getLastSuccesfulStartTimeForJob(final JnjIntegrationRSACronJobModel arg0);

	void setLastSuccesfulStartTimeForJob(final String lastUpdatedate, final JnjIntegrationRSACronJobModel arg0);

	String builRSAQueryLastUpdatedDate(final JnjIntegrationRSACronJobModel jobModel, final String columnName);

	String buildRSAQueryLastUpdatedEndDate(final String queryConstraints, final String lastUpdatedEndDateProperty,
										   final String columnName);

	/** Add date parameter to the query considering 9 months before the current date. */
	String buildRSAQueryOrderCreationDate(String queryConstraints, String columnName);

	String buildRSAQuerySalesOrgList(final String queryConstraints, final String salesOrgListProperty, final String columnName);
	
	String buildRSAQueryOrderLastUpdatedDate(final JnjIntegrationRSACronJobModel jobModel, final String columnName);

	String buildRSAQueryForLastUpdatedDate(final Date lowerDate, final Date upperDate, final String columnName);

	String buildRSAQueryLastUpdatedDateForProduct(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel jobModel, final String columnName);
}
