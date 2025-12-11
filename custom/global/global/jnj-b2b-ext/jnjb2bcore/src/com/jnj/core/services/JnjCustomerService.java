/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jnj.core.model.JnJB2bCustomerModel;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCustomerService
{
	
	void registerCustomer(JnJB2bCustomerModel customerModel, String password) throws DuplicateUidException;

	void register(JnJB2bCustomerModel customerModel, String password) throws DuplicateUidException;

	void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException;

	public SearchPageData<B2BCustomerModel> getPagedCustomersCaseInsenitiveByGroupMembership(final PageableData pageableData,
			final String... userGroupUid);

	/**
	 * 
	 * This is used to get the list of accounts for portal user
	 * 
	 * @param addCurrentB2BUnit
	 * @return Map<String, String>
	 */
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit);

	/**
	 * This method is used to save the JnJB2bCustomerModel.
	 * 
	 * @param jnJB2bCustomerModel
	 *           the jn j b2b customer model
	 * @return true, if successful
	 */
	boolean saveJnjB2bCustomer(JnJB2bCustomerModel jnJB2bCustomerModel);

	public Set<JnJB2bCustomerModel> getAllJnjUsers();

	/**
	 * @return
	 */
	List<JnJB2bCustomerModel> getNewlyRegisteredUsers();
	
	/**
	 * This is used to get the roles of logged in user of portal.
	 *
	 * @return Set<UserGroupModel>
	 */
	public Set<PrincipalGroupModel> getRolesForPortalUser();
	
	/**
	 *
	 * @return
	 */
	String getPasswordEncoding();

}
