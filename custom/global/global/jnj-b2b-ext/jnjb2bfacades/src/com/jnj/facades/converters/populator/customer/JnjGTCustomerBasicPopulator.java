/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator.customer;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;

import com.jnj.facades.data.JnjGTCustomerBasicData;
import com.jnj.core.model.JnJB2bCustomerModel;


/**
 * This class represents the populator for the customer basic data.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCustomerBasicPopulator implements Populator<CustomerModel, CustomerData>
{
	@Autowired
	private SessionService sessionService;

	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		if (target instanceof JnjGTCustomerBasicData && source instanceof JnJB2bCustomerModel)
		{
			/**
			 * Type casting both CustomerModel and CustomerData to JnJB2bCustomerModel and JnjGTCustomerBasicData
			 * respectively
			 **/
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) source;
			final JnjGTCustomerBasicData jnjGTCustomerBasicData = (JnjGTCustomerBasicData) target;

			/** Fetching user tier type : Added for CR 12th January **/
			final Object userTierType = sessionService.getAttribute(Jnjb2bCoreConstants.UserSearch.USER_TIER_TYPE);
			
			/** Fetching user role : Added for AAOL-5857 **/
			final Object userRole = sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE);
			
			if (null != userTierType
					&& (Jnjb2bCoreConstants.UserSearch.USER_TIER1.equalsIgnoreCase(String.valueOf(userTierType)) || Jnjb2bCoreConstants.UserSearch.USER_TIER2
							.equalsIgnoreCase(String.valueOf(userTierType))))
			{
				jnjGTCustomerBasicData.setAdminUser(true);
			}
			else
			{
				jnjGTCustomerBasicData.setAdminUser(false);
			}
			
			//AAOL-5857
			jnjGTCustomerBasicData.setUserRole(String.valueOf(userRole));

			/** Setting essential data **/

			if(null != sessionService.getAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT))
			{
				jnjGTCustomerBasicData.setShowChangeAccount(sessionService.getAttribute(Jnjb2bCoreConstants.Login.SHOW_CHANGE_ACCOUNT).toString());
			}

			jnjGTCustomerBasicData.setUid(JnJB2bCustomerModel.getUid());

			jnjGTCustomerBasicData.setEmailPrefrences(JnJB2bCustomerModel.getEmailPreferences());

			/** Checking getCurrentB2BUnit not null - setting current B2B Unit related info **/
			if (null != JnJB2bCustomerModel.getCurrentB2BUnit())
			{
				jnjGTCustomerBasicData.setCurrentB2BUnitID(JnJB2bCustomerModel.getCurrentB2BUnit().getUid());
				jnjGTCustomerBasicData.setCurrentB2BUnitName(JnJB2bCustomerModel.getCurrentB2BUnit().getLocName());
				jnjGTCustomerBasicData.setCurrentB2BUnitGLN(JnJB2bCustomerModel.getCurrentB2BUnit().getGlobalLocNo());
				jnjGTCustomerBasicData.setJnjSiteName(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME));
			}

			/** Checking name not null **/
			if (null != JnJB2bCustomerModel.getName())
			{
				/** Splitting Name by SPACE to retrieve first name and last name **/
				final String[] customerSplitName = JnJB2bCustomerModel.getName().split(Jnjb2bCoreConstants.SPACE);

				/** SET first name **/
				jnjGTCustomerBasicData.setFirstName(customerSplitName[0]);

				/**
				 * If splitting by space gave length 2 then we have both first and last names : Done to prevent Array Index
				 * Out of Bounds Exception
				 **/
				if (customerSplitName.length == 2)
				{
					/** SET Last Name **/
					jnjGTCustomerBasicData.setLastName(customerSplitName[1]);
				}
			}
			if (StringUtils.isNotBlank(JnJB2bCustomerModel.getWwid()))
			{
				jnjGTCustomerBasicData.setWwid(JnJB2bCustomerModel.getWwid());
			}
			else
			{
				jnjGTCustomerBasicData.setWwid("");
			}

		}
	}
}
