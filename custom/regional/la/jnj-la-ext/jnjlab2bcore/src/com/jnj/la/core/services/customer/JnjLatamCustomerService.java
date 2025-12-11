/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.customer;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.exceptions.BusinessException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Set;

public interface JnjLatamCustomerService extends JnjGTCustomerService
{

    Set<String> getAllAccountsForCustomer(CustomerModel currentCustomer);

    JnJB2BUnitModel getAccountForCustomer(CustomerModel currentCustomer, String accountNumber);

    CountryModel getCountryOfUser() throws BusinessException;
    String getCountry();

}
