/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.company;

import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */


public interface JnjB2BUnitFacade extends B2BUnitFacade
{
	List<AddressData> getShippingAddresses();
	
	List<AddressData> getBillingAddresses();
	/**
	 * Gets user groups for a given company
	 * 
	 * @param uid
	 *           - the uid of the company
	 * @return - the list of user groups
	 */
	List<B2BUserGroupData> getUserGroupsForUnit(String editableuser);

	/**
	 * Gets the pagable data for addresses of the company.
	 * 
	 * @return the list of addresses
	 */
	SearchPageData<AddressData> getPagedAddresses(final PageableData pageableData);
}
