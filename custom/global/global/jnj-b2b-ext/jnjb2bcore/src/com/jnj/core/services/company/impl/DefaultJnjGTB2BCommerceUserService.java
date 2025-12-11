/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.company.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
//import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultB2BCommerceUserService;
import de.hybris.platform.b2b.company.impl.DefaultB2BCommerceUserService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dao.company.JnjGTPagedB2BCustomerDao;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;



/**
 * This Facade is used for Search Users.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTB2BCommerceUserService extends DefaultB2BCommerceUserService implements JnjGTB2BCommerceUserService
{

	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTB2BCommerceUserService.class);
	@Resource(name = "pagedB2BCustomerDao")
	protected JnjGTPagedB2BCustomerDao jnjGTPagedB2BCustomerDao;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;
	
	@Resource(name = "userService")
	protected UserService userService;
	
	public JnjGTPagedB2BCustomerDao getJnjGTPagedB2BCustomerDao() {
		return jnjGTPagedB2BCustomerDao;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	/*
	 * This method is used for searching Customer on User Profile Search Page.This method calculate that whether we have
	 * Default search or Specific Search
	 */
	@Override
	public SearchPageData<B2BCustomerModel> searchCustomers(final JnjGTPageableData pageableData)
	{

		final String METHOD_NAME = Jnjb2bCoreConstants.Logging.USER_MAMANGEMENT_SEARCH_SERVICE;
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.USER_MANAGEMENT, METHOD_NAME, "Enter User Search Page", LOG);
		SearchPageData<B2BCustomerModel> searchPagedData = null;
		//Check whether the User is searching 
		if (pageableData.isSearchUserFlag())
		{

			searchPagedData = jnjGTPagedB2BCustomerDao.searchCustomers(pageableData);

		}
		//Check whether the User is Landing Or Resetting the Search Page
		else
		{
			searchPagedData = jnjGTPagedB2BCustomerDao.findPagedCustomers(pageableData);

		}
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return searchPagedData;
	}

	@Override
	public List<String> getUserDivisions(final JnJB2bCustomerModel customer)
	{
		final List<String> divisions = new ArrayList<>();

		final AccessBy accessBy = customer.getAccessBy();
		if (AccessBy.WWID.equals(accessBy))
		{
			divisions.add(customer.getDivison());
		}
		else if (AccessBy.TERRITORIES.equals(accessBy))
		{
			final List<String> teritoryDivisions = customer.getTerritoryDiv();
			if (teritoryDivisions != null)
			{
				for (final String teritoryDivision : teritoryDivisions)
				{
					final String[] teritoryDiv = teritoryDivision.split("\\|", 2);
					try
					{
						divisions.add(teritoryDiv[1]);
					}
					catch (final ArrayIndexOutOfBoundsException exception)
					{
						exception.printStackTrace();
					}
				}
			}
		}
		return divisions;
	}


	@Override
	public String generateTokenForApprovedEmail(final JnJB2bCustomerModel customer)
	{
		return jnjGTCustomerService.generateToken(customer);
	}
	@Override
	public String generateTemporaryPwdForEmail(JnJB2bCustomerModel customer) {
		return jnjGTCustomerService.generateTemporaryPassword(customer);
	}
	
	/**
	 * This returns the current sector of the user. 
	 * @return userSector
	 */
	public String getCurrentUserSector()
	{
		String userSector = null;
		try
		{
			final CustomerModel user = (CustomerModel) userService.getCurrentUser();
			if(user instanceof JnJB2bCustomerModel)
			{
				JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) user;
				if(currentUser.isMddSector() && currentUser.isPharmaSector())
				{
					userSector = "MDD_PHARMA";
				}
				else if(currentUser.isMddSector())
				{
					userSector = "MDD";
				}
				else if(currentUser.isPharmaSector())
				{
					userSector = "PHARMA";
				}
				else if(currentUser.isConsumerSector())
				{
					userSector = "CONSUMER";
				}
				if(LOG.isDebugEnabled()) 
				{
					LOG.debug("CurrentUserSector : "+userSector);
				}
			}
		}	
		catch(final ClassCastException exception)
		{
			LOG.error("getCurrentUserSector() error occurred" + exception);
		}
		return userSector;
	}
}
