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
package com.jnj.facades.company.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.event.JnjLAResetPasswordEvent;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.company.JnjLatamB2BCommerceUserServiceImpl;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.utils.CommonUtil;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.util.B2BCompanyUtils;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;


/**
 *
 */
public class JnjLatamB2BCommerceUserFacadeImpl extends DefaultJnjGTB2BCommerceUserFacade
{
	private static final Logger LOG = Logger.getLogger(JnjLatamB2BCommerceUserFacadeImpl.class);
	private static final String RESET_TOKEN_EMAIL = "sentPasswordResetEmail()";
	private static final String SITE_URL = "Site Url";

	private UserService userService;
	@Autowired
	protected JnjLatamB2BCommerceUserServiceImpl jnjLatamB2BCommerceUserServiceImpl;



	@Override
	public SearchPageData<CustomerData> searchCustomers(final JnjGTPageableData pageableData)
	{

		//Calling the  Service method for Search Users.
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.UserSearch.SEARCH_CUSTOMERS,
				Jnjb2bCoreConstants.Logging.USER_MAMANGEMENT_SEARCH_FACADE, "Enter User Search Page",
				JnjLatamB2BCommerceUserFacadeImpl.class);
		final SearchPageData<B2BCustomerModel> b2bCustomer = jnjLatamB2BCommerceUserServiceImpl.searchCustomers(pageableData);
		//Calling the  Populator for converting the Customer Model into Customer Data
		return B2BCompanyUtils.convertPageData(b2bCustomer, getB2BCustomerConverter());
	}

	public void sendPasswordResetEmail(final String siteLogoUrl, final String userId,  final String siteUrl, final String resetPwdUrl, final String token)
			throws  UnsupportedEncodingException {
		final String methodName = "sentPasswordResetEmail()";
		//removed DuplicateUidException, please add
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, LOG);
		//create Event
		JnjLAResetPasswordEvent event = new JnjLAResetPasswordEvent();
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
		//Setting the Email Specific Attributes
		event.setBusinessEmail(customer.getEmail());
		event.setResetPwdURL(resetPwdUrl+"?token="+token);
		event.setBaseUrl(siteUrl);
		event.setLogoURL(siteLogoUrl);
		LOG.info("sentPasswordResetEmail Email Map Has Been Set With Email '" + customer.getEmail() + SITE_URL + resetPwdUrl);
		eventService.publishEvent(initializeEmailEvent(event, customer));
		CommonUtil.logMethodStartOrEnd(RESET_TOKEN_EMAIL, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, LOG);

	}

	@Override
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
