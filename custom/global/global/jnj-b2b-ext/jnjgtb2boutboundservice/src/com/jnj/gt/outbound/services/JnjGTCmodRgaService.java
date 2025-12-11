/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsReply;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsRequest;



/**
 * The JnjNACmodRgaService interface contains the declaration of all the methods of the JnjNACmodRgaServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTCmodRgaService
{

	/**
	 * The getReports method accepts the ReportsRequest object as its input parameters and passes the same to sap service
	 * to get the bytes which we have to show the user in frontend. object.
	 * 
	 * @param reportsRequest
	 *           the cmod / rga reports request
	 * @return the test pricing from gateway output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public ReportsReply getReports(final ReportsRequest reportsRequest) throws IntegrationException;
}
