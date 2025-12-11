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
package com.jnj.facades.customer;

import com.jnj.facades.data.JnjGTCustomerData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.List;
import java.util.Set;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjUserInfoData;
import com.jnj.facades.data.JnjLaCustomerData;
import com.jnj.la.core.dto.JnjLatamCompanyInfoData;
import com.jnj.la.core.dto.JnjLatamRegistrationData;


/**
 *
 */
public interface JnjLatamCustomerFacade extends JnjGTCustomerFacade
{
	public boolean registerUser(final JnjLatamRegistrationData jnjRegistrationData, final String siteLogoURL)
			throws DuplicateUidException, ModelSavingException;

	public void setCustomerStatus(final JnjLaCustomerData customerData, final JnjLatamRegistrationData jnjRegistrationData);

	public AddressData setAddress(final JnjUserInfoData jnjUserInfoData, final JnjLatamCompanyInfoData jnjLatamCompanyInfoData);

	public List<String> getPhoneCodes(final String country);


	public Set<UserGroupModel> getUserMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific, final boolean isUCN,
			final JnjGTPageableData pageableData);

    void createCustomer(JnjGTCustomerData customerData) throws DuplicateUidException;

}
