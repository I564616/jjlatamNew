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

import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * Modifies the record of B2Bcustomer
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTB2BCustomerPrepareInterceptor implements PrepareInterceptor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Autowired
	protected JnjGTOperationsService jnjGTOperationsService;

	/** Auto-wired Model Service **/
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



		if (model instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) model;

			if (modelService.isNew(JnJB2bCustomerModel))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for create new user");
				}

				this.logAuditData(null, "New user creation event. User created is: " + JnJB2bCustomerModel.getUid(), null, true,
						false, new Date());
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for existing user");
				}

				this.logAuditData(null, "Existing user modification Event. ", null, true, false, new Date());


			}
			/* START CR_119 */
			if (JnJB2bCustomerModel.getStatus() != null && arg1.isModified(JnJB2bCustomerModel, JnJB2bCustomerModel.STATUS))
			{

				if (JnJB2bCustomerModel.getStatus().equals(CustomerStatus.ACTIVE)
						&& (JnJB2bCustomerModel.getSendActivationMail() != null)
						&& (!(JnJB2bCustomerModel.getSendActivationMail().booleanValue())))
				{
					JnJB2bCustomerModel.setSendActivationMail(Boolean.TRUE);
				}

			}
			/* END CR_119 */
		}
	}

	protected void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent,
				null);
	}
}
