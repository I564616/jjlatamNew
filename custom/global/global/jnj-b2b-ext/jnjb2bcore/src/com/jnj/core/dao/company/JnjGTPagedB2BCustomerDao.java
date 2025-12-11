/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.company;


import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import com.jnj.core.dto.JnjGTPageableData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTPagedB2BCustomerDao<B2BCustomerModel> extends PagedGenericDao<B2BCustomerModel>
{

	/**
	 * @param pageableData
	 * @return
	 */
	public SearchPageData<B2BCustomerModel> searchCustomers(final JnjGTPageableData pageableData);

	/**
	 * @param pageableData
	 */
	public SearchPageData<B2BCustomerModel> findPagedCustomers(JnjGTPageableData pageableData);


}
