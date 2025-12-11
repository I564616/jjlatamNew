/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import com.jnj.core.dto.JnjCountryData;
import com.jnj.core.dto.JnjRegisterData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjCustomerData;
import com.jnj.facades.data.SecretQuestionData;


/**
 * 
 * This class is responsible for calling the service for creating new user account.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCustomerFacade extends CustomerFacade
{

	/**
	 * This method registers a user with given parameters and invokes service class to create account for that user.
	 * 
	 * @param registerData
	 *           the user data the user will be registered with
	 * @param siteLogoURL
	 *           the site logo media URL
	 * @return true, if successful
	 * @throws DuplicateUidException
	 *            if the login is not unique
	 * @throws UnknownIdentifierException
	 *            if the title code is invalid
	 * @throws IllegalArgumentException
	 *            if required data is missing
	 */
	boolean register(JnjRegisterData registerData, String siteLogoURL) throws DuplicateUidException, UnknownIdentifierException,
			IllegalArgumentException;

	/**
	 * Updates the profile of the customer.
	 * 
	 * @param customerData
	 *           the customer data
	 * @throws DuplicateUidException
	 *            if the user id is not unique
	 */
	public void updateFullProfile(final JnjCustomerData customerData) throws DuplicateUidException;


	/**
	 * Validate data before update.
	 * 
	 * @param customerData
	 *           the customer data
	 */
	public void validateDataBeforeUpdate(final JnjCustomerData customerData);

	/**
	 * Gets the secret questions.
	 * 
	 * @return a list of the secret questions
	 */
	List<SecretQuestionData> getSecretQuestions();

	/**
	 * Update pwd.
	 * 
	 * @param newPassword
	 *           the new password
	 * @param email
	 *           the email
	 * @throws TokenInvalidatedException
	 *            the token invalidated exception
	 */
	void updatePwd(String newPassword, String email) throws TokenInvalidatedException;




	/**
	 * Update profile.
	 * 
	 * @param customerData
	 *           the customer data
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	void updateProfile(JnjCustomerData customerData) throws DuplicateUidException;

	/**
	 * Gets the lang list.
	 * 
	 * @return the lang list
	 */
	Set<LanguageData> getLangList();

	/**
	 * Gets the secret questions for user.
	 * 
	 * @return list of questions for a particular user
	 */
	List<SecretQuestionData> getSecretQuestionsForUser();

	/**
	 * Updates the secret questions for the user.
	 * 
	 * @param secretQuestionData
	 *           the secret question data
	 * @return true if updated successfully
	 */
	boolean updateSecretQuestions(List<SecretQuestionData> secretQuestionData);

	/**
	 * Updates a customer.
	 * 
	 * @param customerData
	 *           the customer data
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	void updateCustomer(final JnjCustomerData customerData) throws DuplicateUidException;

	/**
	 * Create a customer.
	 * 
	 * @param customerData
	 *           the customer data
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	void createCustomer(final JnjCustomerData customerData) throws DuplicateUidException;



	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	List<JnjCountryData> getCountries();

	/**
	 * Gets the secret questions for registration.
	 * 
	 * @param userUid
	 *           the user uid
	 * @return the secret questions for registration
	 */
	List<SecretQuestionData> getSecretQuestionsForRegistration(String userUid);

	/**
	 * Gets the regions.
	 * 
	 * @param country
	 *           the country
	 * @return the regions
	 */
	List<RegionData> getRegions(String country);

	/**
	 * Gets the codes.
	 * 
	 * @param country
	 *           the country
	 * @return the codes
	 */
	List<JnjCountryData> getCodes(String country);

	/**
	 * This will get the list of URLs to be synced with the acoount landing page.
	 * 
	 * @param contentId
	 *           the content id
	 * @param request
	 *           the request
	 * @return the visible urls
	 */
	public List<String> getVisibleUrls(String contentId, HttpServletRequest request);

	/**
	 * Checks if is password change required.
	 * 
	 * @return the boolean
	 */
	public Boolean isPasswordChangeRequired();

	/**
	 * Change user password.
	 * 
	 * @param oldPassword
	 *           the old password
	 * @param newPassword
	 *           the new password
	 * @return the string
	 * @throws PasswordMismatchException
	 *            the password mismatch exception
	 */
	public String changeUserPassword(final String oldPassword, final String newPassword) throws PasswordMismatchException;

	/**
	 * Verify password.
	 * 
	 * @param newPassword
	 *           the new password
	 * @return the string
	 * @throws PasswordMismatchException
	 *            the password mismatch exception
	 * @throws BusinessException
	 *            the business exception
	 */
	String verifyPassword(String newPassword) throws PasswordMismatchException, BusinessException;

	/**
	 * Verify password.
	 * 
	 * @param newPassword
	 *           the new password
	 * @param email
	 *           the email
	 * @return the string
	 * @throws PasswordMismatchException
	 *            the password mismatch exception
	 * @throws BusinessException
	 *            the business exception
	 */
	String verifyPassword(String newPassword, String email) throws PasswordMismatchException, BusinessException;

	/**
	 * Gets the user name.
	 * 
	 * @param uid
	 *           the uid
	 * @return the user name
	 */
	String getUserName(String uid);

	/**
	 * Checks if is admin user.
	 * 
	 * @param userId
	 *           the user id
	 * @return true, if is admin user
	 */
	boolean isAdminUser(String userId);


	/**
	 * Gets the secret questions for user.
	 * 
	 * @param email
	 *           the email
	 * @return the secret questions for user
	 */
	List<SecretQuestionData> getSecretQuestionsForUser(String email);

	/**
	 * This is used to get the list of accounts for portal user.
	 * 
	 * @param addCurrentB2BUnit
	 *           the add current b2 b unit
	 * @return Map<String, String>
	 */
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit);

	/**
	 * This methods checks the versions of the Legal Notice and Privacy Policy Components with that of the Current User
	 * and returns TRUE if there is a match.
	 * 
	 * @return true, if successful
	 */
	boolean checkLegalPrivacyVersions();

	/**
	 * 
	 */
	void sendAccountActiveEmail();

	/**
	 * This method updated the Legal Policy and Privacy Policy versions of the Current User to the latest.
	 * 
	 * @return true, if successful
	 */
	boolean updateLegalPrivacyPolicyVersion();

	/**
	 * This method gets list of uid, lastEmailSent For the Interval, last password change date
	 * 
	 * @return
	 */
	public Set<JnJB2bCustomerModel> getAllJnjUsers();

	public void sendPasswordExpiryMail();

	/**
	 * This method will accept the Uid and perform validation on it.
	 * 
	 * @param uid
	 * @return true/false
	 */
	public boolean validateUid(final String uid);

	/**
	 * This method validates the user entered secret questions and answers with the one stored in the database.
	 * 
	 * @param uid
	 * @param userEnteredSecretQuestionsData
	 */
	public boolean validateSecretQuestionsAndAnswers(final String uid,
			final List<SecretQuestionData> userEnteredSecretQuestionsData);

	/**
	 * This method performs password update
	 * 
	 * @param newPassword
	 * @param email
	 * @throws TokenInvalidatedException
	 */
	public void updateUserPassword(final String newPassword, final String email) throws TokenInvalidatedException;

	/**
	 * @param newPassword
	 * @param b2bCustomerModel
	 * @param token
	 * @throws TokenInvalidatedException
	 */
	void updatePasswordWithToken(String newPassword, JnJB2bCustomerModel b2bCustomerModel, String token)
			throws TokenInvalidatedException;

}
