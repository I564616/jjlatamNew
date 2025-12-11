/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.search.impl;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.search.JnjSolrService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.enums.AccessBy;
import org.apache.commons.lang3.StringUtils;


/**
 * This is implementation class of JnjSolrService, is used to get field on which restriction needs to be added as well
 * the values which needs to be restricted.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTSolrService implements JnjSolrService
{


	@Autowired
	UserService userService;

	@Autowired
	SessionService sessionService;
	
	

	public UserService getUserService() {
		return userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	private final static Logger LOGGER = Logger.getLogger(DefaultJnjGTSolrService.class);

	@Override
	public Collection<String> getRestrictedValues()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Solr.GET_RESTRICTED_VALUES + Logging.HYPHEN + Logging.BEGIN_OF_METHOD);
		}
		final Collection<String> restrictedValues = new ArrayList<>();

		// Get the current unit assigned to the current customer
		final UserModel userModel = userService.getCurrentUser();
		if (userModel != null && userModel instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) userModel;
			
			if (JnJB2bCustomerModel.getCurrentB2BUnit() != null)
			{
				restrictedValues.add(JnJB2bCustomerModel.getCurrentB2BUnit().getUid());
				restrictedValues.add(Jnjgtb2bCONSConstants.ALLB2BUNIT);
			}
		}
		else {
			LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedValues : Customer is not belonging to JnJB2bCustomerModel *****" + userModel);
			if (userModel != null) {
				LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedValues : Customer is not belonging to JnJB2bCustomerModel uid*****" + userModel.getUid());
				LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedValues : Customer is not belonging to JnJB2bCustomerModel pk*****" + userModel.getPk().getLongValue());
			}
		}	
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Solr.GET_RESTRICTED_VALUES + Logging.HYPHEN + restrictedValues + Logging.HYPHEN
					+ Logging.END_OF_METHOD);
		}
		return restrictedValues;
	}

	@Override
	public String getRestrictedField()
	{
		// From session get the current site i.e. MDD/CONS
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		// If site is CONS, restriction needs to be applied else no restriction is required
		if (currentSite != null && currentSite.equals(Jnjb2bCoreConstants.CONS))
		{
			return JnJCommonUtil.getValue(Jnjb2bCoreConstants.Solr.RESTRICTED_FIELD);
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method is used to fetch the value of the field which needs to be filtered.Done for JJEPIC-320.
	 */
	@Override
	public String getRestrictedManufacturerField()
	{
		// From session get the current site i.e. MDD/CONS
		String result = null;
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final String houseAccName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_HOUSE);
		final UserModel userModel = userService.getCurrentUser();
		// If site is CONS, restriction needs to be applied else no restriction is required
		if (currentSite.equals(Jnjb2bCoreConstants.CONS))
		{
			// If account is house account and the access by of the user is by WWID, restriction needs to be applied else no restriction is required
			if (userModel instanceof JnJB2bCustomerModel ) {
				final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userModel;
				if (jnJB2bCustomerModel.getCurrentB2BUnit().getIndicator() != null
						&& houseAccName.equalsIgnoreCase(jnJB2bCustomerModel.getCurrentB2BUnit().getIndicator())
						&& AccessBy.WWID.equals(jnJB2bCustomerModel.getAccessBy()) && jnJB2bCustomerModel.getDivison() != null)
				{
					result = JnJCommonUtil.getValue(Jnjb2bCoreConstants.Solr.RESTRICTED_MANU_FIELD);
				}
			} else {
				LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedManufacturerField : Customer is not belonging to JnJB2bCustomerModel *****" + userModel);
				if (userModel != null) {
					LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedManufacturerField : Customer is not belonging to JnJB2bCustomerModel uid*****" + userModel.getUid());
					LOGGER.warn(" ***** JnjGTSolrServiceImpl : getRestrictedManufacturerField : Customer is not belonging to JnJB2bCustomerModel pk*****" + userModel.getPk().getLongValue());
				}
			}
			
		}
		return result;
	}
	
	/**
	 * This method is used to fetch the values that need to be filtered.Done for JJEPIC-320.
	 */
	@Override
	public Collection<String> getEnabledManufacturerAIDValues()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Solr.GET_RESTRICTED_VALUES + Logging.HYPHEN + Logging.BEGIN_OF_METHOD);
		}
		final Collection<String> restrictedValues = new ArrayList<>();

		// Get the current unit assigned to the current customer
		final UserModel userModel = userService.getCurrentUser();
		if (userModel instanceof JnJB2bCustomerModel ) {
			final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userModel;
			if (jnJB2bCustomerModel != null && jnJB2bCustomerModel.getDivison() != null)
			{
				final String[] consDivArray = StringUtils.split(jnJB2bCustomerModel.getDivison(), ',');
				for (final String consDiv : consDivArray)
				{
					restrictedValues.add(consDiv);
				}
			}
		} else {
			LOGGER.warn(" ***** JnjGTSolrServiceImpl : getEnabledManufacturerAIDValues : Customer is not belonging to JnJB2bCustomerModel *****" + userModel);
			if (userModel != null) {
				LOGGER.warn(" ***** JnjGTSolrServiceImpl : getEnabledManufacturerAIDValues : Customer is not belonging to JnJB2bCustomerModel uid*****" + userModel.getUid());
				LOGGER.warn(" ***** JnjGTSolrServiceImpl : getEnabledManufacturerAIDValues : Customer is not belonging to JnJB2bCustomerModel pk*****" + userModel.getPk().getLongValue());
			}
		}
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Solr.GET_RESTRICTED_VALUES + Logging.HYPHEN + restrictedValues + Logging.HYPHEN
					+ Logging.END_OF_METHOD);
		}
		return restrictedValues;
	}



}
