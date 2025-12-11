/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.dao;

import de.hybris.platform.core.model.c2l.CountryModel;

import java.util.Date;
import java.util.List;

import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.model.OldPasswordModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;


/**
 * TODO:<Komal - class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJCustomerDataDao
{



	/**
	 * Gets the jn j sales org customer mode byl id.
	 * 
	 * @param jnjb2bUnit
	 *           the jnjb2b unit
	 * @param sector
	 *           the sector
	 * @return the jn j sales org customer mode byl id
	 */
	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeBylId(String jnjb2bUnit, String sector);

	/**
	 * Gets secret questions for a user.
	 * 
	 * @param userId
	 *           the user id
	 * @return list of secret questions
	 */
	public List<SecretQuestionsAndAnswersModel> getSecretQuestionsForUser(String userId);

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	public List<CountryModel> getCountries();

	/**
	 * Gets the regions.
	 * 
	 * @param country
	 *           the country
	 * @return the regions
	 */
	public List<CountryModel> getRegions(String country);

	/**
	 * This method will get the date to check if the password is expired or not.
	 * 
	 * @param loginId
	 *           the login id
	 * @return the last password change date
	 */
	public Date getLastPasswordChangeDate(final String loginId);

	/**
	 * Gets the old password list.
	 * 
	 * @param loginId
	 *           the login id
	 * @return the old password list
	 */
	public List<OldPasswordModel> getOldPasswordList(final String loginId);

	/**
	 * @param customerNumber
	 * @return
	 */
	public List<JnJSalesOrgCustomerModel> getJnJSalesOrgCustomerModeBylId(String customerNumber);

	/**
	 * Gets the indirect customer by using the country details.
	 * 
	 * @param country
	 *           the country
	 * @return the list of indirect customer
	 */
	public List<JnjIndirectCustomerModel> getIndirectCustomer(final String country);

	/**
	 * Gets the indirect customer by using the country details.
	 * 
	 * @param country
	 *           the country
	 * @param customerNumber
	 *           the customerNumber
	 * @return the indirect customer
	 */
	public JnjIndirectCustomerModel getIndirectCustomerByIdCountry(final String customerNumber, final String country);
}
