/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.customer;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.jnj.exceptions.BusinessException;
import com.jnj.facades.customer.JnjCustomerFacade;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjRegistrationData;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;


/**
 * This class represents the facade layer of the customer flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCustomerFacade extends JnjCustomerFacade
{
	/**
	 * This method checks if there is a mismatch between current privacy policy version and the version which the user
	 * agreed to.
	 * 
	 * @return boolean
	 */
	boolean checkPrivacyPolicyVersions();

	/**
	 * This method updates the version of the privacy policy in the jnjb2bcustomer.
	 * 
	 * @return boolean
	 */
	boolean updatePrivacyPolicyVersion();

	/**
	 * This method fetches the current customer data.
	 * 
	 * @return CustomerData
	 */
	public CustomerData getCurrentGTCustomer();

	/**
	 * This method checks if the survey pop up needs to be displayed or not.
	 * 
	 * @return true/false
	 */
	public boolean checkSurvey();

	/**
	 * This method disables the survey flag for the logged in user.
	 * 
	 * @return true / false
	 */
	boolean disableSurveyFlag();

	/**
	 * THis method blocks the user who's email is passed as parameter.
	 * 
	 * @param email
	 *           the email
	 * @return true/false
	 */
	public boolean blockUser(final String email);

	/**
	 * THis method checks if the user is login disabled or not.
	 * 
	 * @param email
	 *           the email
	 * @return true/false
	 */
	public boolean isLoginDisabled(final String email);

	/**
	 * This method sends the successful registration email to the CSR.
	 * 
	 * @param jnjRegistrationData
	 *           the jnj registration data
	 * @param siteLogoURL
	 *           the site logo url
	 * @param JnJB2bCustomerModel
	 *           the jnj na b2b customer model
	 * @return true / false
	 */
	boolean sendSuccessfulRegistrationEmail(final JnjRegistrationData jnjRegistrationData, final String siteLogoURL,
			final JnJB2bCustomerModel JnJB2bCustomerModel);

	/**
	 * This method sends the registration confirmation email to the user.
	 * 
	 * @param siteLogoURL
	 *           the site logo url
	 * @param jnjRegistrationData
	 *           the jnj registration data
	 * @param JnJB2bCustomerModel
	 *           the jnj na b2b customer model
	 * @return true / false
	 */
	boolean sendRegistrationConfirmationEmail(final String siteLogoURL, final JnjRegistrationData jnjRegistrationData,
			final JnJB2bCustomerModel JnJB2bCustomerModel);

	/**
	 * This method is used to register the user.
	 * 
	 * @param jnjRegistrationData
	 *           the jnj registration data
	 * @param siteLogoURL
	 *           the site logo url
	 * @return List of values
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 * @throws ModelSavingException
	 *            the model saving exception
	 */
	public boolean registerUser(final JnjRegistrationData jnjRegistrationData, String siteLogoURL) throws DuplicateUidException,
			ModelSavingException;


	/**
	 * This method is used to validate the list of accountNumbers and return the invalid accountNumber.
	 * 
	 * @param accountNumbers
	 *           the account numbers
	 * @return String of comma separated invalid account numbers
	 */
	public String validateAccountNumbers(String[] accountNumbers);

	/**
	 * This method will fetch the keys from the config table and then the values corresponding to the keys from the
	 * messages.impex file.
	 * 
	 * @return Map of the email preferences options present for the user.
	 */
	public Map<String, String> getEmailPreferences();
	public Map<String, String> getEmailPreferences(String preferenceKey);

	/**
	 * Gets the security questions.
	 * 
	 * @return Map of the security questions present for the user
	 */
	public Map<String, String> getSecurityQuestions();

	/**
	 * This method will fetch the values to be populated for the Step 2 of the registration process like the .
	 * 
	 * @param list
	 *           the list
	 * @return Map with the key as the name of the dropdown and the value will be the list of values to be present in the
	 *         dropdown.
	 */
	public Map<String, Map<String, String>> getCompanyInfoDropdowns(List<String> list);

	/**
	 * This method will return the list of division to be displayed to the JnJ employee at the time of registration.
	 * 
	 * @return Map of divisions.
	 */
	public Map<String, String> getDivisions();

	/**
	 * This method will return the list of Departments to be displayed to the user at the time of registration.
	 * 
	 * @return Map of Departments.
	 */
	public Map<String, String> getDepartments();

	/**
	 * This method performs the change of current account for the user.
	 * 
	 * @param selectedAccountUid
	 *           the selected account uid
	 * @return success / failure
	 */
	boolean changeAccount(String selectedAccountUid);

	/**
	 * This method fetches the map containing information for sector specific accounts.
	 * 
	 * @param addCurrentB2BUnit
	 *           the add current b2 b unit
	 * @param isUCNFlag
	 *           the is ucn flag
	 * @return Map<String, String>
	 */
	public Map<String, String> getSectorSpecificAccountsMap(final boolean addCurrentB2BUnit, final boolean isUCNFlag);

	/**
	 * Verify existing password.
	 * 
	 * @param currentPassword
	 *           the current password
	 * @param email
	 *           the email
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	boolean verifyExistingPassword(String currentPassword, String email) throws BusinessException;

	/**
	 * This method updates the Customer for My profile.
	 * 
	 * @param customerData
	 *           the customer data
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	void updateCustomer(JnjGTCustomerData customerData) throws DuplicateUidException;


	/**
	 * This method add a New Account For the USER.
	 * 
	 * @param companyData
	 *           the company data
	 * @return true, if successful
	 */

	public boolean addNewAccount(JnjCompanyInfoData companyData);

	/**
	 * This method fetches the phone codes to be populated at the time of USER Registration.
	 * 
	 * @return the phone codes
	 */
	public List<String> getPhoneCodes();

	/**
	 * Adds the existing account.
	 * 
	 * @param accountNumbers
	 *           the account numbers
	 * @return true, if successful
	 */
	public boolean addExistingAccount(String[] accountNumbers);

	/**
	 * This method saves the secret questions and answers as set by the user at the time of profile activation.
	 * 
	 * @param secretQuestionData
	 *           the secret question data
	 * @param emailId
	 *           the email id
	 * @return true / false based on whether or not the secret questions are saved for the user.
	 */
	public boolean updateSecretQuestionsForUser(List<SecretQuestionData> secretQuestionData, final String emailId);

	/**
	 * Gets the current selected account class of trade.
	 * 
	 * @return the current selected account class of trade
	 */
	public String getCurrentSelectedAccountClassOfTrade();

	/**
	 * Gets the customer for uid.
	 * 
	 * @param user
	 *           the user
	 * @return the customer for uid
	 */
	public JnjGTCustomerData getCustomerForUid(String user);

	/**
	 * Sent password expirymail.
	 * 
	 * @param siteLogoUrl
	 *           the site logo url
	 * @param userId
	 *           the user id
	 * @param siteUrl
	 *           the site url
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 * @throws UnsupportedEncodingException
	 */
	public void sentPasswordExpirymail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException;

	/**
	 * Reset user password.
	 * 
	 * @param lowerCase
	 *           the lower case
	 * @return true, if successful
	 */
	public boolean resetUserPassword(String lowerCase);

	/**
	 * Gets the jnj n acustomer model.
	 * 
	 * @return the jnj n acustomer model
	 */
	public JnJB2bCustomerModel getJnjGTcustomerModel();

	/**
	 * Save jnj na customer model.
	 * 
	 * @param JnJB2bCustomerModel
	 *           the jn jna b2b customer model
	 * @return true, if successful
	 */
	public boolean saveJnjGTCustomerModel(final JnJB2bCustomerModel JnJB2bCustomerModel);

	/**
	 * Gets the accounts map.
	 * 
	 * @param addCurrentB2BUnit
	 *           the add current b2 b unit
	 * @param userAccounts
	 *           the user accounts
	 * @return the accounts map
	 */
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit, final List<String> userAccounts);

	/**
	 * Adds the to existing notes.
	 * 
	 * @param csrNotes
	 *           the csr notes
	 * @param existingNotes
	 *           the existing notes
	 * @return the string
	 */
	public String addToExistingNotes( String existingNotes);


	/**
	 * This method will return the list of territories to be displayed to the JnJ employee at the Edit User.
	 * 
	 * @return Map of territories.
	 */
	public Map<String, String> getTerritories();

	/**
	 * This method will return the list of division to be displayed to the JnJ employee at the time of registration.
	 * 
	 * @return Map of divisions.
	 */
	public Map<String, String> getConsumerDivisons();

	/**
	 * User with dummy unit.
	 * 
	 * @param currentUser
	 *           the current user
	 * @return true, if successful
	 */
	public boolean userWithDummyUnit(final JnJB2bCustomerModel currentUser);

	/**
	 * This method checks if the user has the user group for PCM associated with it.
	 * 
	 * @param currentUser
	 *           the current user
	 * @return boolean
	 */
	//boolean isPCMUser(JnJB2bCustomerModel currentUser);

	/**
	 * This method will return the list of associated countries with a PCM user.
	 * 
	 * @return List<String>
	 */
	List<CountryData> fetchAssociatedCountries();

	/**
	 * This method will return the value of the current country associated with the user.
	 * 
	 * @return data of the current country
	 */
	CountryData getCurrentCountry();

	/**
	 * This method will set the value of the current country with the current user.Also will change the current base
	 * store that is needed to change.
	 * 
	 * @param countryCode
	 *           the country code
	 * @return true/ false based on whether or not the user's country was updated.
	 */
	boolean setCurrentCountryAndUnitForPCM(String countryCode);

	/**
	 * This method is used to send an email to the CSR when the user request an account for the PCM site.
	 * 
	 * @param requestAccountData
	 *           the request account data
	 * @param siteLogoUrl
	 *           the site logo url
	 * @return boolean
	 */
	//public boolean sendRequestAccountEmailToCSR(RequestAccountData requestAccountData, String siteLogoUrl);

	/**
	 * Validate account numbers registration.
	 * 
	 * @param accountNumbers
	 *           the account numbers
	 * @return String
	 */
	public String validateAccountNumbersRegistration(String[] accountNumbers);

	/**
	 * Updates the customer model for the newly created PCM user with the default country and default b2b unit
	 * 
	 * @param jnjGTCustomerData
	 *           the customer data of the current user
	 */
	//public void updateDataForNewPCMUser(final JnjGTCustomerData jnjGTCustomerData);

	/**
	 * Returns current B2B unit set in the session user.
	 * 
	 * @return JnJB2BUnitModel
	 */
	public JnJB2BUnitModel getCurrentB2bUnit();

	/**
	 * This method checks if the user's registration is complete by checking the secret questions by calling the service
	 * 
	 * @return true - if registration is complete ELSE false
	 */
	boolean isRegistrationComplete();

	/**
	 * This method fetches the map containing information for all accounts.
	 * 
	 * @param addCurrentB2BUnit
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * 
	 * @return JnjGTAccountSelectionData
	 */
	public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCN, final JnjGTPageableData pageableData);
	
	public JnJB2bCustomerModel getCurrentSessionCustomer();
	
	public String validateWwid(final String wwid);
	
	/**
	 * @return
	 */
	public JnJB2BUnitModel getDefaulB2bUnitForCurrentCust();

	/**
	 * @param defaultB2BUnit
	 * @return
	 */
	public boolean validateDefaultAccount(JnJB2BUnitModel defaultB2BUnit);

	/**
	 * @param customerData
	 * @return
	 * @throws DuplicateUidException
	 */
	public String getEmailType(final JnjGTCustomerData customerData) throws DuplicateUidException;

	/**
	 * @param customerData
	 * @return
	 * @throws DuplicateUidException
	 */
	public String getShippedOrderEmailType(JnjGTCustomerData customerData)
			throws DuplicateUidException;
	
	public String getInoviceEmailType(JnjGTCustomerData customerData)
			throws DuplicateUidException;
	
	public String getDeliveryNoteEmailType(JnjGTCustomerData customerData)
			throws DuplicateUidException;
	/**
	 * @return
	 */
	boolean isResetPasswordComplete();
	
	/**
	 * @param userId
	 * @param siteUrl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String resetPasswordUrlFirstTimeLogin(String userId,String siteUrl) throws UnsupportedEncodingException;
	
	
	/**
	 * @param status
	 */
	public String enableOrDisableUser(boolean status,String userId);
	
	/**
	 * @param uid
	 * @param email
	 * @return
	 */ //for GT no need this delete func
	/*public String deleteUser(String uid);*/
	
	/*4659*/
	
	public JnJB2BUnitModel getB2BUnitForUid(String Uid);
	
	/**
	 * This Method is used to update Default B2bUnit and Default Order.
	 * @param defaultAccount
	 * @param defaultOrder
	 * @return
	 */
	boolean updateDefaultAccAndOrder(String defaultAccount, String defaultOrder);
	
	/**
	 * Returns List for Auto Suggest
	 * @param valueOf
	 * @return
	 */
	List<String> getAccountsListForAutoSuggest(String searchtext);
/**
 * Added for AAOL-5219
 * @return
 */
	public List<CategoryModel> getUserFranchise();
	
	/**
	 * Added for AAOL-5219
	 * @return
	 */
	public List<CategoryModel> getAllFranchise();
	CategoryData getCategoryForCode(String code);

	/**
	 * Added for AFFG-20804
	 * @return
	 */
	public List<CategoryModel> getAllNewFranchise();
}
