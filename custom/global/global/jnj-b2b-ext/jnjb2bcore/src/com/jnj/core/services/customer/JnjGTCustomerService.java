/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.JnjCustomerService;
import com.jnj.exceptions.BusinessException;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;


/**
 * This class represents the service layer of the customer flow
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCustomerService extends JnjCustomerService
{

	public JnJB2bCustomerModel getJnJB2bCustomerModel(JnJB2bCustomerModel JnJB2bCustomerModel);

	public String validateAccountNumbers(final String[] accountNumbers);

	public Set<JnJB2BUnitModel> getB2bUnitsForAccountNumbers(final String accountNumbers);

	/**
	 * This method saves the JnJB2bCustomerModel by using model service
	 *
	 * @author sanchit.a.kumar
	 * @param currentCustomer
	 */
	public boolean saveJnjGTB2bCustomer(JnJB2bCustomerModel currentCustomer);

	/**
	 * This method fetches the map containing information for sector specific accounts.
	 *
	 * @param addCurrentB2BUnit
	 * @param isUCNFlag
	 * @return Map<String, String>
	 */
	public Map<String, String> getSectorSpecificAccountsMap(final boolean addCurrentB2BUnit, final boolean isUCNFlag);

	/**
	 * This method is used to get current logged in Customer
	 *
	 * @return JnJB2bCustomerModel the current user
	 */
	JnJB2bCustomerModel getCurrentUser();

	public List<String> getPhoneCodes();

	public void setUserDiableLogin();

	public List<JnJB2bCustomerModel> getAllJnjGTUsers();

	public boolean sendDisableLoginWarningMail(final JnJB2bCustomerModel user,int daysbeforeDisable);

	/**
	 * This method returns the division data for the Customer passed in the argument. If the parameter is passed as
	 * "null" it will return division data for current User.
	 *
	 * @param customerModel
	 *           the customer model
	 * @return the populated division data
	 */
	public JnjGTDivisonData getPopulatedDivisionData(JnJB2bCustomerModel customerModel);

	/**
	 * This method is used to save the current country for the PCM user.
	 *
	 * @param countryCode
	 * @return
	 */
	public boolean setCurrentCountryAndUnitForPCM(String countryCode);

	/**
	 * This method validates accounts for the self registration process
	 *
	 * @param accountNumbers
	 * @return String
	 */
	public String validateAccountNumbersRegistration(String[] accountNumbers);

	List<JnJB2bCustomerModel> getAllNewPCMUsers();

	/**
	 * TODO <<Replace this line with the method description and describe each parameter below>>
	 *
	 * @param jnjGTB2bCustomer
	 * @param siteUrl
	 * @param forPCMFlag
	 * @throws DuplicateUidException
	 * @throws UnsupportedEncodingException
	 */
	void sentPasswordExpiryEmail(JnJB2bCustomerModel JnjGTB2bCustomer, String siteUrl, final boolean forPCMFlag)
			throws DuplicateUidException, UnsupportedEncodingException;

	/**
	 * @param currentCustomer
	 * @return
	 */
	boolean saveJnjGTB2bCustomerFeed(JnJB2bCustomerModel currentCustomer);

	/**
	 * @param JnJB2bCustomerModel
	 * @return
	 */
	List<JnJB2bCustomerModel> getJnJB2bCustomerModels(JnJB2bCustomerModel JnJB2bCustomerModel);

	/**
	 * Gets the product manager for the Given Product Catalog.
	 *
	 * @param catalogModel
	 *           the catalog model
	 * @return the product manager
	 * @throws BusinessException
	 *            the business exception
	 */
	public UserGroupModel getProductManagerGroupForCatalog(final CatalogModel catalogModel) throws BusinessException;

	public String generateToken(JnJB2bCustomerModel customer);

	public String generateTemporaryPassword(JnJB2bCustomerModel customer);
	/**
	 * This method checks if the user's registration is complete by checking the secret questions.
	 *
	 * @return true - if registration is complete ELSE false
	 */
	public boolean isRegistrationComplete();

	/**
	 * @return
	 */
	public void resetPasswordForUserWithGenerateTokenTrue();

	/**
	 * @param customer
	 * @throws UnsupportedEncodingException
	 */
	public void generateResetPasswordDetails(JnJB2bCustomerModel customer);

	/**
	 *
	 * This method fetches accounts
	 *
	 * @param addCurrentB2BUnit
	 * @param isSectorSpecific
	 * @param isUCN
	 * @param pageableData
	 * @return JnjGTAccountSelectionData
	 */
	public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCN, final JnjGTPageableData pageableData);

	/**
	 *
	 */
	public void sendActivationEmail();

	/**
	 * This method sends cut notification email
	 *
	 */
	public void sendCutNotificationEmail();
	
	/**
	 * @param wwid
	 * @return
	 */
	public String validateWwid(final String wwid);

	/**
	 * @param pageableData
	 * @return
	 */
	public boolean validateDEfaultAccount(JnjGTPageableData pageableData);
	
	/**
	 * @return
	 */
	public boolean isResetPasswordComplete();
	
	/**
	 * Returns List of Accounts for entered text
	 * @param searchText
	 * @return
	 */
	public List<String> getAccountListForAutoSuggest(String searchText);

	/**
	 * This method gives whether the user list is generated and stored properly.
	 * @return isUserListAvailable
	 */
	public boolean extractActiveUserDetailList();


}
