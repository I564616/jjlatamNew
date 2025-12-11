/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.services.company;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.company.impl.JnjLatamPagedB2BCustomerDaoImpl;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.services.company.impl.DefaultJnjGTB2BCommerceUserService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;


/**
 *
 */
public class JnjLatamB2BCommerceUserServiceImpl extends DefaultJnjGTB2BCommerceUserService
{

	@Autowired
	protected JnjLatamPagedB2BCustomerDaoImpl JnjLatamPagedB2BCustomerDaoImpl;
	
	private static final String PHR_SECTOR_CODE = "PHR";
	private static final String SECTOR_DELIMITER = "_";
	private static final String PHARMA_SECTOR = "PHARMA";
	
	@Override
	public SearchPageData<B2BCustomerModel> searchCustomers(final JnjGTPageableData pageableData)
	{

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.UserSearch.SEARCH_CUSTOMERS,
				Jnjb2bCoreConstants.Logging.USER_MAMANGEMENT_SEARCH_FACADE, "Enter User Search Page",
				JnjLatamB2BCommerceUserServiceImpl.class);
		SearchPageData<B2BCustomerModel> searchPagedData;
		//Check whether the User is searching
		if (pageableData.isSearchUserFlag())
		{

			searchPagedData = jnjGTPagedB2BCustomerDao.searchCustomers(pageableData);

		}
		//Check whether the User is Landing Or Resetting the Search Page
		else
		{
			searchPagedData = JnjLatamPagedB2BCustomerDaoImpl.findPagedCustomers(pageableData);

		}
		return searchPagedData;
	}
	
	/**
	 * This returns the Sector of the user. 
	 * @return userSector
	 */
	@Override
	public String getCurrentUserSector() {
		String userSector;
		List<JnJSalesOrgCustomerModel> userSalesOrg = new ArrayList<>();
		final CustomerModel user = (CustomerModel) userService.getCurrentUser();
		if (user instanceof JnJB2bCustomerModel) {
			final JnJLaB2BUnitModel currentB2bUnit = (JnJLaB2BUnitModel) ((JnJB2bCustomerModel) user)
					.getCurrentB2BUnit();
			if (null != currentB2bUnit) {
				userSalesOrg = currentB2bUnit.getSalesOrg();
			}
		}
		StringBuilder userSectorBuilder = new StringBuilder();
		for (JnJSalesOrgCustomerModel salesOrg : userSalesOrg) {
			userSectorBuilder.append(salesOrg.getSector().equals(PHR_SECTOR_CODE) ? PHARMA_SECTOR : salesOrg.getSector());
			userSectorBuilder.append(SECTOR_DELIMITER);
		}
		userSector = userSectorBuilder.toString();
		if(!ObjectUtils.isEmpty(userSector)) {
			userSector = userSector.substring(0,userSector.length() - 1);
		}
		return userSector;
	}

}
