/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * Modifies the record of B2BUnit
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTB2BUnitPrepareInterceptor implements PrepareInterceptor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Autowired
	protected JnjGTOperationsService jnjGTOperationsService;

	@Autowired
	protected ModelService modelService;

	
	public JnjGTOperationsService getJnjGTOperationsService() {
		return jnjGTOperationsService;
	}


	public ModelService getModelService() {
		return modelService;
	}


	protected static final Logger LOG = Logger.getLogger(JnjGTUserGroupPrepareInterceptor.class);



	@Override
	public void onPrepare(final Object model, final InterceptorContext arg1) throws InterceptorException
	{
		// YTODO Auto-generated method stub

		if (model instanceof JnJB2BUnitModel)
		{
			final JnJB2BUnitModel JnjGTB2BUnit = (JnJB2BUnitModel) model;


			if (modelService.isNew(JnjGTB2BUnit))
			{

				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for create new B2B unit");
				}

				this.logAuditData(null, "New B2B unit create event. User created is: " + JnjGTB2BUnit.getUid(), null, true, false,
						new Date());
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for existing user group");
				}
				this.logAuditData(null, "Existing B2BUnit modification Event. ", null, true, false, new Date());

			}



		}
	}


	protected void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent, null);
	}

}
