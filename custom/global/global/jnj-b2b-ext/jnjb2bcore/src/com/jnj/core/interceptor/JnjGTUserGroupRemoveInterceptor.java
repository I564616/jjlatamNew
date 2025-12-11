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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * Modifies the record of UserGroup
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTUserGroupRemoveInterceptor implements RemoveInterceptor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.RemoveInterceptor#onRemove(java.lang.Object,
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



	protected final List<String> userGroups = new ArrayList<String>();

	{
		userGroups.add("viewOnlyBuyerGroup");
		userGroups.add("placeOrderBuyerGroup");
		userGroups.add("viewOnlySalesRepGroup");
		userGroups.add("placeOrderSalesGroup");
		userGroups.add("jnjGTAdminGroup");
		userGroups.add("jnjGTCsrGroup");
		//AAOL-5669
		userGroups.add("jnjGTSerializationGroup");
	}


	@Override
	public void onRemove(final Object model, final InterceptorContext arg1) throws InterceptorException
	{
		// YTODO Auto-generated method stub



		if (model instanceof UserGroupModel && !(model instanceof B2BUnitModel))
		{
			final UserGroupModel JnjGTUserGroup = (UserGroupModel) model;

			if (modelService.isNew(JnjGTUserGroup))
			{
				// going to log event for create new user group

				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for removing new user group");
				}
				this.logAuditData(null, "New User remove event. User created is: " + JnjGTUserGroup.getUid(), null, true, false,
						new Date());
			}
			else if (userGroups.contains(JnjGTUserGroup.getUid()))
			{

				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for existing user group");
				}

				this.logAuditData(null, "Existing usergroup remove Event. ", null, true, false, new Date());
			}
		}
	}

	protected void logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent)
	{

		jnjGTOperationsService.logAuditData(systemOrProcess, descriptionOfEvent, ipAddress, isAuthorised, isSuccess, timeOfEvent, null);
	}

}
