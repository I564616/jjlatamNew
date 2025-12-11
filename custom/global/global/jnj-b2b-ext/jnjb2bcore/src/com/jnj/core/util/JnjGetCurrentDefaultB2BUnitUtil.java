/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.util;

import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGetCurrentDefaultB2BUnitUtil
{
	protected static final Logger LOGGER = Logger.getLogger(JnjGetCurrentDefaultB2BUnitUtil.class);
	/** The user service. */
	@Autowired
	protected UserService userService;

	@Autowired
	JnjCustomerEligibilityService customerEligibilityService;
	
	@Autowired
	protected JnjSalesOrgCustService jnjSalesOrgCustService;
	
	@Resource(name = "GTCustomerService")
	JnjGTCustomerService jnjGTCustomerService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	protected BaseStoreService baseStoreService;
	
	
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * Gets the default b2b unit.
	 * 
	 * @return the default b2b unit
	 */
	public JnJB2BUnitModel getDefaultB2BUnit()
	{
		final JnJB2bCustomerModel jnjB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();

		final JnJB2BUnitModel jnjB2BUnitModel = (JnJB2BUnitModel) jnjB2bCustomerModel.getDefaultB2BUnit();

		return jnjB2BUnitModel;

	}
	
	/**
	 * Gets the country of logged in user.
	 * 
	 * @return the country of current user
	 */
	public CountryModel getCurrentCountryForSite()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCountryOfLoggedInUser()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final BaseStoreModel baseStoreModel = baseStoreService.getCurrentBaseStore();
		
		//Changes to handle scenarios for Cronjob
		if(baseStoreModel!=null){
			final Collection<CountryModel> deliveryCountries = baseStoreModel.getDeliveryCountries();
			if (!deliveryCountries.isEmpty())
			{
				return deliveryCountries.iterator().next();
			}
		}
		

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCountryOfLoggedInUser()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return null;
	} 
	
	/**
	 * This method is used to check if current user can do add to cart/Checkout. Default B2bUnit must have mapping for
	 * Sales org and Product Sectors
	 * 
	 * @return true, if current user can do addToCart.
	 */
	public boolean validateCheckoutUser()
	{
		List<String> requiredProductSectors = null;
		List<String> requiredCheckoutRoles = null;
		boolean hasProperRole = true;
		boolean hasSectorMapping = true;
		final String requiredPordSectorMapping = Config.getParameter(Jnjb2bCoreConstants.REQ_PROD_SECOTRS_KEY);

		/*
		 * Sales org and product sector mapping is required for user to checkout.{PHR and MDD}. In case of User have any
		 * of the category is restricted sales org mapping is not required for this segment.
		 */
		if (StringUtils.isNotEmpty(requiredPordSectorMapping))
		{
			final HashMap<String, String> sectorSalesOrgMapping = jnjSalesOrgCustService.getSectorAndSalesOrgMapping();
			// To set those value in a new list as the Arrays.asList will always give unmodifiable collection and so it throws an UnsupportedOperationException.
			requiredProductSectors = new ArrayList(Arrays.asList(requiredPordSectorMapping.split(Jnjb2bCoreConstants.CONST_COMMA)));
			//Get all the restricted categories for the current customer
			final List<String> restrictedCategories = getRestrictedCategories();
			requiredProductSectors.removeAll(restrictedCategories);
			if (CollectionUtils.isEmpty(requiredProductSectors))
			{
				//User can not be able to do add to cart in case of all categories are restricted
				hasSectorMapping = false;
			}
			else
			{
				hasSectorMapping = sectorSalesOrgMapping.keySet().containsAll(requiredProductSectors);
			}
		}
		final String requiredRolesForCheckout = Config.getParameter(Jnjb2bCoreConstants.VALID_CHEKOUT_ROLES_KEY);
		if (StringUtils.isNotEmpty(requiredRolesForCheckout))
		{
			requiredCheckoutRoles = Arrays.asList(requiredRolesForCheckout.split(Jnjb2bCoreConstants.CONST_COMMA));
			final Set<String> userRolesSet = getAllRoles();
			hasProperRole = !Collections.disjoint(userRolesSet, requiredCheckoutRoles);
		}
		return hasSectorMapping && hasProperRole;
	}
	
	/**
	 * Gets the all roles of current Logged In User.
	 * 
	 * @return Set<String> containing all the roles as Set of Strings.
	 */
	public Set<String> getAllRoles()
	{
		final Set<String> userRoles = new HashSet<String>();
		final Set<PrincipalGroupModel> userRolesSet = jnjGTCustomerService.getRolesForPortalUser();
		if (CollectionUtils.isNotEmpty(userRolesSet))
		{
			for (final PrincipalGroupModel userRole : userRolesSet)
			{
				userRoles.add(userRole.getUid());
			}
		}
		return userRoles;
	}
	
	/**
	 * Gets the restricted categories for current customer.
	 * 
	 * @return List of restricted categories
	 */
	protected List<String> getRestrictedCategories()
	{
		final JnJB2BUnitModel jnjB2BUnitModel = getDefaultB2BUnit();
		// To retrieve the restricted categories set.
		final Set<String> restrictedCategories = customerEligibilityService.getRestrictedCategory(jnjB2BUnitModel.getUid());
		final List restrictedCategoryCodes = new ArrayList<String>(2);

		if (CollectionUtils.isNotEmpty(restrictedCategories))
		{
			for (final String restrictedCategory : restrictedCategories)
			{
				// Check the code as empty and null then compare it with mdd category value, if true set the value as MDD
				if (restrictedCategory.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.MDD_CATEGORY_CODE)))
				{
					restrictedCategoryCodes.add(Jnjb2bCoreConstants.MDD_SECTOR);
				}
				// else if Check the code as empty and null then compare it pharma category value, if true set the value as MDD
				else if (restrictedCategory.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.PHARMA_CATEGORY_CODE)))
				{
					restrictedCategoryCodes.add(Jnjb2bCoreConstants.PHR_SECTOR);
				}
			}
		}
		return restrictedCategoryCodes;
	}
}