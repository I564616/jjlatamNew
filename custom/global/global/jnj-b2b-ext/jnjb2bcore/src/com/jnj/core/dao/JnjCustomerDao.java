/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.core.model.JnJB2bCustomerModel;


/**
 * 
 * TODO:<Balinder - class level comments are missing>
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjCustomerDao extends PagedGenericDao<B2BCustomerModel>
{
	public SearchPageData<B2BCustomerModel> findPagedCustomersCaseInsenitiveByGroupMembership(final String sortCode,
			final PageableData pageableData, final String... userGroupid);

	/**
	 * @return
	 */
	public List<JnJB2bCustomerModel> getNewlyRegisteredUsers();

	public List<JnJB2bCustomerModel> getAllJnjUsers();
}
