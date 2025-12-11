/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Sandeep kumar-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJCustomerDataService
{
	public boolean saveItemModel(ItemModel itemModel);

	public boolean saveModel(JnJB2bCustomerModel jnjB2bCustomerModel);

	public JnJB2bCustomerModel createModel();

	public AddressModel createAddressModel();

	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeBylId(String jnJb2bUnit, String sector);

	/**
	 * @param userId
	 * @return
	 */
	public List<SecretQuestionsAndAnswersModel> getSecretQuestionsForUser(String userId);

	/**
	 * @return
	 */
	public List<CountryModel> getCountries();

	/**
	 * @param country
	 * @return
	 */
	List<CountryModel> getRegions(String country);

	/**
	 * 
	 * @param loginId
	 * @return
	 */
	public Boolean isPasswordChangeRequired(final String loginId);

	public void changePassword(final JnJB2bCustomerModel customerModel, final String oldPassword, final String newPassword)
			throws PasswordMismatchException;

	/**
	 * This will change password from forgot password flow
	 * 
	 * @param customerModel
	 * @param newPassword
	 * @return
	 * @throws PasswordMismatchException
	 */
	public String changePassword(final UserModel userModel, final String newPassword) throws BusinessException;

	/**
	 * @param customerNumber
	 * @return
	 * 
	 */
	public List<JnJSalesOrgCustomerModel> getJnJSalesOrgCustomerModeBylId(String customerNumber);

	/**
	 * @param country
	 * @return List<JnjIndirectCustomerModel>
	 */
	public List<JnjIndirectCustomerModel> getJnjInidrectCustomer(String country);

	/**
	 * @param customerNumber
	 * @param country
	 * @return JnjIndirectCustomerModel
	 */
	public JnjIndirectCustomerModel getJnjInidrectCustomerByIDCountry(String customerNumber, String country);
}