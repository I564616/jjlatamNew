/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.interceptor;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * Removes the record of B2Bcustomer
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTB2BCustomerRemoveInterceptor implements RemoveInterceptor
{
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.RemoveInterceptor#onRemove(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onRemove(final Object model, final InterceptorContext arg1) throws InterceptorException
	{
		// YTODO Auto-generated method stub


		if (model instanceof UserGroupModel && !(model instanceof B2BUnitModel))
		{
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) model;

			if (modelService.isNew(JnJB2bCustomerModel))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for create new user");
				}

				this.logAuditData(null, "New user removal event. User removed is: " + JnJB2bCustomerModel.getUid(), null, true,
						false, new Date());
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for existing user");
				}

				this.logAuditData(null, "Existing user removal Event. ", null, true, false, new Date());
			}

		}
	}


	protected void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent, null);
	}


}
