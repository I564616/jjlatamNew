/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.customer.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerReversePopulator;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.Sets;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjParagraphComponentModel;
import com.jnj.core.model.OldPasswordModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjCustomerService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.facades.customer.impl.DefaultJnjCustomerFacade;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjRegistrationData;
import com.jnj.core.dto.JnjUserInfoData;
import com.jnj.core.event.JnjGTRegistrationConfirmationEmailEvent;
import com.jnj.core.event.JnjGTSuccessfulRegistrationEmailEvent;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.event.JnjGTAddAccountEvent;
import com.jnj.core.event.JnjGTAddExistingAccountEvent;
import com.jnj.facades.converters.populator.customer.JnjGTCompanyInfoReversePopulator;
import com.jnj.facades.converters.populator.customer.JnjGTCustomerReversePopulator;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.core.model.CompanyInfoModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTSurveyLinkComponentModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;

import jakarta.annotation.Resource;




/**
 * This class represents the facade layer of the customer flow.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCustomerFacade extends DefaultJnjCustomerFacade implements JnjGTCustomerFacade
{

	/** The Constant JNJ_NA_CUSTOMER_FACADE. */
	protected static final String JNJ_NA_CUSTOMER_FACADE = "JnjGTCustomerFacade";

	/** The Constant B2BCUSTOMERGROUP. */
	protected static final String B2BCUSTOMERGROUP = "b2bcustomergroup";

	/** The Constant CUST_ACCOUNT. */
	protected static final String CUST_ACCOUNT = "custAccount";

	/** The Constant JJ_EMPLOYEE. */
	protected static final String JJ_EMPLOYEE = "jjEmployee";

	/** The Constant JJ_CUSTOMER. */
	protected static final String JJ_CUSTOMER = "jjCustomer";

	/** The Constant REGISTER_USER_PASSWORD_ENCODING. */
	protected static final String REGISTER_USER_PASSWORD_ENCODING = "register.user.password.encoding";

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTCustomerFacade.class);

	/** The Company B2B Commerce service. */
	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/

	@Autowired
	protected B2BCommerceUnitService b2BCommerceUnitService;

	/** The message facade. */
	@Autowired
	protected MessageFacadeUtill messageFacade;

	/** The Password encoder service. */
	@Autowired
	protected PasswordEncoderService passwordEncoderService;

	/** The project properties service. */
	@Autowired
	protected JnjConfigService jnjConfigService;

	/** The NA Customer Service. */
	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	/** The b2 b commerce b2 b user group service. */
	@Autowired
	protected B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

 	@Autowired
	protected JnjGTB2BUnitDao jnjGTB2BUnitDao;

	/** The message service. */
	@Autowired
	protected MessageService messageService;

	/** I18NService to retrieve the current locale. */
	@Autowired
	protected I18NService i18nService;

	/** The user service. */
	@Autowired
	protected UserService userService;

	/** Utility class for Site-Logo in email */
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/** The company info reverse populator. */
	@Autowired
	@Qualifier(value = "companyInfoReversePopulator")
	protected JnjGTCompanyInfoReversePopulator companyInfoReversePopulator;

	/** The customer reverse populator. */
	@Autowired
	@Qualifier(value = "customerReversePopulator")
	protected JnjGTCustomerReversePopulator jnjGTCustomerReversePopulator;

	/** The CMS component service. */
	@Autowired
	protected CMSComponentService cMSComponentService;

	/** The JNJ Customer Service. */
	@Autowired
	protected JnjCustomerService jnjCustomerService;

	/** The event service required for the email flow **/
	@Autowired
	protected EventService eventService;

	/** Base Store Service for Email Event Population **/
	@Autowired
	protected BaseStoreService baseStoreService;

	/** Base Site Service for Email Event Population **/
	@Autowired
	protected BaseSiteService baseSiteService;

	/** Internationalization Service for Email Event Population **/
	@Autowired
	protected CommonI18NService commonI18NService;

	/** Session service to save account level attributes **/
	@Autowired
	protected SessionService sessionService;

	/** The jnj na b2 b unit service. */
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	/** Basic Customer Converter **/
	protected Converter<CustomerModel, CustomerData> customerBasicConverter;

	/** Constant for Password error key **/
	protected static final String PASSWORD_ERROR_MESSAGE_KEY = "profile.password.error";

	/** The customer converter. */
	@Autowired
	protected Converter<CustomerModel, CustomerData> customerConverter;

	/** The b2 b customer reverse populator. */
	@Autowired
	protected B2BCustomerReversePopulator b2BCustomerReversePopulator;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	@Autowired
	protected JnjGTTerritoryService jnjGTTerritoryService;

	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjgtb2bCommerceUserService;

	/** Added for AAOL-5219*/
	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;

	/**
	 * This method is used to verify the eligibility of password for change.
	 *
	 * @param newPassword
	 *           the new password
	 * @param email
	 *           the email
	 * @return verification status string
	 * @throws BusinessException
	 *            the business exception
	 */
	@Override
	public String verifyPassword(final String newPassword, final String email) throws BusinessException
	{
		final String METHOD_NAME = "verifyPassword";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final String TRUE = String.valueOf(Boolean.TRUE);
		/** Fetching current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		LOG.info("Current customer fetched :: " + currentCustomer);
		List<OldPasswordModel> oldPassList = null;
		/** Fetching Old Passwords List **/
		if (currentCustomer.getOldPasswords() != null && !currentCustomer.getOldPasswords().isEmpty())
		{
			oldPassList = (List<OldPasswordModel>) currentCustomer.getOldPasswords();
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Old Passwords List received.");
		}
		/** If there are no old passwords stored **/
		if (oldPassList == null)
		{
			logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
			return TRUE;
		}
		else
		{
			OldPasswordModel oldPass = null;
			/** Fetching encoded password **/
			final String newEncodedPass = passwordEncoderService.encode(currentCustomer, newPassword,
					currentCustomer.getPasswordEncoding());
			/** Iterating over the old password list **/
			for (int i = oldPassList.size() - 1; i >= 0; i--)
			{
				oldPass = oldPassList.get(i);
				/** Checking if any of the 5 old passwords match the new password **/
				if ((oldPassList.size() - i <= 5 && oldPass.getOldPassword().equals(newEncodedPass)))
				{
					logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "New Password matches one of five last passwords.");
					logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
					return messageFacade.getMessageTextForCode(PASSWORD_ERROR_MESSAGE_KEY);
				}
			}
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return TRUE;
	}

	/**
	 * Gets the secret question data for the user.
	 *
	 * @param email
	 *           the user's email
	 * @return the secret questions data for user
	 */
	@Override
	public List<SecretQuestionData> getSecretQuestionsForUser(final String email)
	{
		final String METHOD_NAME = "getSecretQuestionsForUser";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final List<SecretQuestionData> secretQuestionDataList = new ArrayList<SecretQuestionData>();

		/** Fetching the current customer **/
		final JnJB2bCustomerModel currentCustomer =  (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		LOG.info("Current customer fetched :: " + currentCustomer);

		/** Fetching the List of the secret Questions from the current customer **/
		final List<SecretQuestionsAndAnswersModel> secretQuestions = currentCustomer.getSecretQuestionsAndAnswersList();
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Secret questions and answers list fetched");

		/** Iterating over the List of the secret Questions from the service layer **/
		for (final SecretQuestionsAndAnswersModel secretQuestion : secretQuestions)
		{
			/** Setting secret question info in the data object **/
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(secretQuestion.getQuestionId());
			secretQuestionData.setAnswer(secretQuestion.getAnswer());
			try
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Setting Question from messages for question code :: "
						+ secretQuestion.getQuestionId());
				secretQuestionData.setQuestion(messageFacade.getMessageTextForCode(secretQuestion.getQuestionId()));
				secretQuestionData.setAnswer(secretQuestion.getAnswer());
			}
			catch (final BusinessException businessException)
			{
				LOG.error("Error while getting secret questions list for user - " + businessException.getMessage());
			}
			secretQuestionDataList.add(secretQuestionData);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return secretQuestionDataList;
	}

	/**
	 * This method validates the user entered secret questions and answers with the ones stored in the database.
	 *
	 * @param uid
	 * @param userEnteredSecretQuestionsData
	 *
	 * @return validationStatus
	 */
	@Override
	public boolean validateSecretQuestionsAndAnswers(final String uid,
			final List<SecretQuestionData> userEnteredSecretQuestionsData)
	{
		final String METHOD_NAME = "validateSecretQuestionsAndAnswers";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		boolean validationStatus = false;
		int matchCount = 0;
		/** Fetching the secret questions for user from the Database **/
		final List<SecretQuestionData> secretQuestionsForUserFromDatabase = getSecretQuestionsForUser(uid);
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Questions retrieved from database for the user :: " + uid
				+ ", are :: " + secretQuestionsForUserFromDatabase);

		/** Null / empty check for the secret questions from database and user entered secret question data **/
		if (null != secretQuestionsForUserFromDatabase && !secretQuestionsForUserFromDatabase.isEmpty()
				&& userEnteredSecretQuestionsData != null && !userEnteredSecretQuestionsData.isEmpty())
		{
			/** Iterating over the userEnteredSecretQuestionsData **/
			for (final SecretQuestionData secretQuestionUserData : userEnteredSecretQuestionsData)
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME,
						"Looking for a match for the user entered secret question :: " + secretQuestionUserData.getCode());
				/** Iterating over the secretQuestionsForUserFromDatabase **/
				for (final SecretQuestionData secretQuestionDatabaseData : secretQuestionsForUserFromDatabase)
				{
					/** Checking if the code AND the answer match **/
					if (secretQuestionUserData.getCode().equalsIgnoreCase(secretQuestionDatabaseData.getCode())
							&& secretQuestionUserData.getAnswer().equalsIgnoreCase(secretQuestionDatabaseData.getAnswer()))
					{
						/** Incrementing the match count **/
						matchCount++;
						logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, secretQuestionUserData.getCode()
								+ " :: Match found! Incrementing the match count to :: " + matchCount);
						break;
					}
					/** Code / answer does not match **/
					else
					{
						logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, secretQuestionUserData.getCode()
								+ " :: Match not found!");
					}
				}
				/**
				 * Checking if all user entered secret questions were a match or not. Since user has to enter 3 secret
				 * questions, therefore we check the match count if equal to 3
				 **/
				if (matchCount == 3)
				{
					/** Making the validation status as true **/
					validationStatus = true;
					break;
				}
			}
		}
		/** Secret questions and answers found null **/
		else
		{
			LOG.warn(Logging.FORGOT_PASSWORD_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Secret Questions null or empty! secretQuestionsForUserFromDatabase :: " + secretQuestionsForUserFromDatabase
					+ "; userEnteredSecretQuestionsData :: " + userEnteredSecretQuestionsData);
		}
		LOG.info(Logging.FORGOT_PASSWORD_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Secret questions validation result :: " + validationStatus);

		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return validationStatus;
	}

	/**
	 * This method checks if there is a mismatch between current privacy policy version and the version which the user
	 * agreed to.
	 *
	 * @return boolean
	 */
	@Override
	public boolean checkPrivacyPolicyVersions()
	{
		final String METHOD_NAME = "checkPrivacyPolicyVersions";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean versionCheck = false;
		/** Fetching the current customer **/
		final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
		try
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN, METHOD_NAME,
					"Fetcing PrivacyPolicyComponent.");
			/** Fetching the CMS component for the privacy policy **/
			final JnjParagraphComponentModel privacyPolicyParagraphComponentModel = cMSComponentService
					.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.PRIVACY_POLICY_COMPONENT_UID_KEY));

			/** Checking if the privacy policy version matches the one for the user **/
			if (null != jnJB2bCustomerModel.getPrivacyPolicyVersion()
					&& jnJB2bCustomerModel.getPrivacyPolicyVersion() == privacyPolicyParagraphComponentModel.getComponentVersion())
			{
				LOG.info(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "PrivacyPolicyComponent Versions in Sync with the Logged in User: [" + jnJB2bCustomerModel.getUid()
						+ "]");
				versionCheck = true;
			}
			else
			{
				LOG.info(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "PrivacyPolicyComponent Versions out of Sync with the Logged in User: ["
						+ jnJB2bCustomerModel.getUid() + "]");
			}
		}
		catch (final CMSItemNotFoundException cMSItemNotFoundException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to fetch PrivacyPolicyComponent for the Logged in User: [" + jnJB2bCustomerModel.getUid()
					+ "] with exception: " + cMSItemNotFoundException);
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.END_OF_METHOD);
		return versionCheck;
	}

	/**
	 * This method updates the version of the privacy policy in the jnjb2bcustomer.
	 *
	 * @return boolean
	 */
	@Override
	public boolean updatePrivacyPolicyVersion()
	{
		final String METHOD_NAME = "updatePrivacyPolicyVersion";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		boolean versionUpdate = false;
		/** Fetching the current customer **/
		final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();

		/** Setting the latest privacy policy version **/
		setPrivacyPolicyVersions(jnJB2bCustomerModel);

		/** Saving the updated customer model **/
		versionUpdate = jnjCustomerService.saveJnjB2bCustomer(jnJB2bCustomerModel);

		/** Checking if update was successful **/
		if (versionUpdate)
		{
			LOG.info(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Privacy Policy Updated Successfully.");
		}
		else
		{
			LOG.error(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Privacy Policy Updation failed.");
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.END_OF_METHOD);
		return versionUpdate;
	}

	/**
	 * This method sets the Current Versions of Privacy Policy in the Current User.
	 *
	 * @param newCustomer
	 */
	protected void setPrivacyPolicyVersions(final JnJB2bCustomerModel newCustomer)
	{
		final String METHOD_NAME = "setPrivacyPolicyVersions()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		try
		{
			/** Fetching the privacy policy component **/
			final JnjParagraphComponentModel privacyPolicyParagraphComponentModel = cMSComponentService
					.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.PRIVACY_POLICY_COMPONENT_UID_KEY));

			newCustomer.setPrivacyPolicyVersion(privacyPolicyParagraphComponentModel.getComponentVersion());
		}
		catch (final CMSItemNotFoundException cMSItemNotFoundException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to fetch PrivacyPolicyComponent with exception :: " + cMSItemNotFoundException);
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method fetches the map containing information for all accounts.
	 *
	 * @param addCurrentB2BUnit
	 *           the add current b2 b unit
	 * @return Map<String, String>
	 */
	@Override
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit)
	{
		final String METHOD_NAME = "getAccountsMap()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTCustomerService.getAccountsMap(addCurrentB2BUnit);
	}

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
	@Override
	public JnjGTAccountSelectionData getAccountsMap(final boolean addCurrentB2BUnit, final boolean isSectorSpecific,
			final boolean isUCN, final JnjGTPageableData pageableData)
	{
		final String METHOD_NAME = "getAccountsMap()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTCustomerService.getAccountsMap(addCurrentB2BUnit, isSectorSpecific, isUCN, pageableData);
	}

	/**
	 * This method fetches the map containing information for sector specific accounts.
	 *
	 * @param addCurrentB2BUnit
	 *           the add current b2 b unit
	 * @param isUCNFlag
	 *           the is ucn flag
	 * @return Map<String, String>
	 */
	@Override
	public Map<String, String> getSectorSpecificAccountsMap(final boolean addCurrentB2BUnit, final boolean isUCNFlag)
	{
		final String METHOD_NAME = "getSectorSpecificAccountsMap()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTCustomerService.getSectorSpecificAccountsMap(addCurrentB2BUnit, isUCNFlag);
	}

	/**
	 * This method performs the change of current account for the user.
	 *
	 * @param selectedAccountUid
	 *           the selected account uid
	 * @return success / failure
	 */
	@Override
	public boolean changeAccount(final String selectedAccountUid)
	{
		final String METHOD_NAME = "changeAccount()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Fetching the current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Current customer :: " + currentCustomer);

		/** Fetching the selected b2b unit by id **/
		final JnJB2BUnitModel selectedUnit = jnjGTB2BUnitDao.getB2BUnitByUid(selectedAccountUid);
		logDebugMessage(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, "Selected unit :: " + selectedUnit);

		/** setting the selected unit in the customer **/
		currentCustomer.setCurrentB2BUnit(selectedUnit);
		/** setting the selected unit in the session services **/
		sessionService.setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, selectedUnit);

		/** saving the customer model **/
		final boolean status = jnjGTCustomerService.saveJnjGTB2bCustomer(currentCustomer);
		LOG.info(Jnjb2bCoreConstants.Logging.ACCOUNTS + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "Current Customer save status :: " + status);

		/** setting the account specific attributes at session level **/
		sessionService.setAttribute("isInternationAff", Boolean.valueOf(jnjGTB2BUnitService.isCustomerInternationalAff()));

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	/**
	 * This method fetches the current customer data.
	 *
	 * @return CustomerData
	 */
	@Override
	public CustomerData getCurrentGTCustomer()
	{
		final String METHOD_NAME = "getCurrentNACustomer()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD);
		/** Calling the customer basic converter to convert the current user data into the customer data **/
		return customerBasicConverter.convert((CustomerModel) userService.getCurrentUser());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.customer.JnjGTCustomerFacade#getCurrentSelectedAccountClassOfTrade()
	 */
	@Override
	public String getCurrentSelectedAccountClassOfTrade()
	{
		return jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator();
	}

	/**
	 * This method checks if the survey pop up needs to be displayed or not.
	 *
	 * @return true/false
	 */
	@Override
	public boolean checkSurvey()
	{
		final String METHOD_NAME = "checkSurvey()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean showSurvey = false;
		/** Fetching the current customer **/
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			/** Type-casting the UserModel to JnJB2bCustomerModel **/
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();
			try
			{
				logDebugMessage(Jnjb2bCoreConstants.Logging.SURVEY + Logging.HYPHEN, METHOD_NAME, "Fetcing survey component.");
				/** Fetching the CMS component for the survey **/
				final JnjGTSurveyLinkComponentModel jnjGTSurveyLinkComponentModel = cMSComponentService
						.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.SURVEY_COMPONENT_UID_KEY));

				/** Checking if Survey is enabled and the user **/
				if ((null != jnjGTSurveyLinkComponentModel && jnjGTSurveyLinkComponentModel.isSurveyEnabled())
						&& JnJB2bCustomerModel.isSurveyEnabled())
				{
					LOG.info(Jnjb2bCoreConstants.Logging.SURVEY + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
							+ "SurveyComponent and Logged in User : [" + JnJB2bCustomerModel.getCustomerID()
							+ "] are both enabled for survey");
					showSurvey = true;
				}
				else
				{
					LOG.info(Jnjb2bCoreConstants.Logging.SURVEY + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
							+ "Either SurveyComponent is not enabled or user is not enabled for survey.");
				}
			}
			catch (final CMSItemNotFoundException cMSItemNotFoundException)
			{
				LOG.error(Jnjb2bCoreConstants.Logging.SURVEY + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "Unable to fetch SurveyComponent for the Logged in User: [" + JnJB2bCustomerModel.getCustomerID()
						+ "] with exception: " + cMSItemNotFoundException);
			}
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
		return showSurvey;
	}

	/**
	 * This method disables the survey flag for the logged in user.
	 *
	 * @return true / false
	 */
	@Override
	public boolean disableSurveyFlag()
	{
		final String METHOD_NAME = "disableSurveyFlag()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;
		/** Fetching the current customer **/
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			/** Type-casting the UserModel to JnJB2bCustomerModel **/
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) userService.getCurrentUser();

			/** Setting survey enabled false in the JnJB2bCustomerModel **/
			JnJB2bCustomerModel.setSurveyEnabled(false);

			/** Saving the JnJB2bCustomerModel **/
			status = jnjGTCustomerService.saveJnjGTB2bCustomer(JnJB2bCustomerModel);
			LOG.info(Jnjb2bCoreConstants.Logging.SURVEY + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "User : ["
					+ JnJB2bCustomerModel.getCustomerID() + "] :: survey disabled status :: " + status);
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	/**
	 * This method blocks the user who's email is passed as parameter.
	 *
	 * @param email
	 * @return true/false
	 */
	@Override
	public boolean blockUser(final String email)
	{
		final String METHOD_NAME = "blockUser()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean status = false;
		/** Fetching current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		LOG.info("Current customer fetched :: " + currentCustomer);

		currentCustomer.setLoginDisabled(true);
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME + Logging.HYPHEN, METHOD_NAME, "User login disabled set! Now saving..");

		status = jnjGTCustomerService.saveJnjGTB2bCustomer(currentCustomer);
		LOG.info(Logging.FORGOT_PASSWORD_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
				+ "User login disabled - save status :: " + status);

		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return status;
	}

	/**
	 *
	 * This method checks if the user is login disabled or not
	 *
	 * @param email
	 * @return true/false
	 */
	@Override
	public boolean isLoginDisabled(final String email)
	{
		final String METHOD_NAME = "isLoginDisabled()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Fetching current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		LOG.info("Current customer fetched :: " + currentCustomer);

		/** Checking if login disabled **/
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return currentCustomer.isLoginDisabled();
	}

	/**
	 * This method sends the successful registration email to the CSR.
	 *
	 * @param jnjRegistrationData
	 * @param siteLogoURL
	 * @return true / false
	 */
	@Override
	public boolean sendSuccessfulRegistrationEmail(final JnjRegistrationData jnjRegistrationData, final String siteLogoURL,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "sendSuccessfulRegistrationEmail()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean sendStatus = false;
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to publish event JnjGTSuccessfulRegistrationEmailEvent");
		try
		{
			/** Publishing event responsible for successful registration email flow **/
			eventService.publishEvent(initializeEmailEvent(new JnjGTSuccessfulRegistrationEmailEvent(siteLogoURL
					+ jnjCommonFacadeUtil.createMediaLogoURL(), jnjRegistrationData), JnJB2bCustomerModel));
			sendStatus = true;
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
					"JnjGTSuccessfulRegistrationEmailEvent has been published!");
		}
		catch (final Exception exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to publish JnjGTSuccessfulRegistrationEmailEvent : " + exception.getMessage());
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return sendStatus;
	}

	/**
	 * This method sends the registration confirmation email to the user.
	 *
	 * @param siteLogoURL
	 * @return true / false
	 */
	@Override
	public boolean sendRegistrationConfirmationEmail(final String siteLogoURL, final JnjRegistrationData jnjRegistrationData,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "sendRegistrationConfirmationEmail()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean sendStatus = false;
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to publish event JnjGTRegistrationConfirmationEmailEvent");
		try
		{
			/** Creating event object and setting essential data **/
			final JnjGTRegistrationConfirmationEmailEvent jnjGTRegistrationConfirmationEmailEvent = new JnjGTRegistrationConfirmationEmailEvent(
					siteLogoURL + jnjCommonFacadeUtil.createMediaLogoURL());

			if (null != jnjRegistrationData && null != jnjRegistrationData.getJnjUserInfoData())
			{
				jnjGTRegistrationConfirmationEmailEvent.setUserEmail(jnjRegistrationData.getJnjUserInfoData().getEmailAddress());
				jnjGTRegistrationConfirmationEmailEvent.setUserFirstName(jnjRegistrationData.getJnjUserInfoData().getFirstName());
				jnjGTRegistrationConfirmationEmailEvent.setUserLastName(jnjRegistrationData.getJnjUserInfoData().getLastName());
				logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
						"jnjGTRegistrationConfirmationEmailEvent essential data has been set.");
			}
			else
			{
				logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
						"jnjGTRegistrationConfirmationEmailEvent essential data was not set!");
			}
			/** Publishing event responsible for registration confirmation email flow **/
			eventService.publishEvent(initializeEmailEvent(jnjGTRegistrationConfirmationEmailEvent, JnJB2bCustomerModel));

			sendStatus = true;
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
					"JnjGTRegistrationConfirmationEmailEvent has been published!");
		}
		catch (final Exception exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to publish JnjGTRegistrationConfirmationEmailEvent : " + exception.getMessage());
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return sendStatus;
	}

	/**
	 * This method populates the JnjPCMRequestAccountEmailEvent object.
	 *
	 * @return populated event object
	 */
	private <T extends AbstractCommerceUserEvent> T initializeRequestAccountEmailEvent(final T event)
	{
		final String METHOD_NAME = "initializeRequestAccountEmailEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjGTSuccessfulRegistrationEmailEvent object");

		/** populating the event with the basic required values **/
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME, "event object is populated");

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return event;
	}

	/**
	 * This method populates the JnjGTSuccessfulRegistrationEmailEvent object.
	 *
	 * @return populated event object
	 */
	protected  <T extends AbstractCommerceUserEvent> T initializeEmailEvent(final T event,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "initializeEmailEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjGTSuccessfulRegistrationEmailEvent object");

		/** populating the event with the basic required values **/
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer(JnJB2bCustomerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME, "event object is populated");

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return event;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#registerUser(com.jnj.core.dto.JnjRegistrationData,
	 * java.lang.String)
	 */
	@Override
	public boolean registerUser(final JnjRegistrationData jnjRegistrationData, final String siteLogoURL)
			throws DuplicateUidException, ModelSavingException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "registerUser()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		boolean status = false;
		final JnJB2bCustomerModel newCustomer = getModelService().create(JnJB2bCustomerModel.class);
		final CompanyInfoModel companyInfoModel = getModelService().create(CompanyInfoModel.class);
		final JnjGTCustomerData customerData = new JnjGTCustomerData();
		customerData.setFirstName(jnjRegistrationData.getJnjUserInfoData().getFirstName());
		customerData.setLastName(jnjRegistrationData.getJnjUserInfoData().getLastName());
		customerData.setSupervisorEmail(jnjRegistrationData.getJnjUserInfoData().getSupervisorEmail());
		customerData.setSupervisorPhone(jnjRegistrationData.getJnjUserInfoData().getSupervisorPhone());
		customerData.setSupervisorName(jnjRegistrationData.getJnjUserInfoData().getSupervisorName());
		customerData.setEmail(jnjRegistrationData.getJnjUserInfoData().getEmailAddress());
		customerData.setBackorderType(jnjRegistrationData.getBackorderEmailType());
		/*4910*/
		customerData.setPreferredMobileNumber(jnjRegistrationData.getJnjUserInfoData().getPreferredMobileNumber());
		if (StringUtils.isNotEmpty(jnjRegistrationData.getJnjSectorTypeData().getDivision()))
		{
			customerData.setDivison(jnjRegistrationData.getJnjSectorTypeData().getDivision());
		}
		customerData.setDepartment(jnjRegistrationData.getJnjUserInfoData().getDepartment());
		customerData.setWwid(jnjRegistrationData.getJnjSectorTypeData().getWwid());
		customerData.setOrgName(jnjRegistrationData.getJnjUserInfoData().getOrgName());
		customerData.setMddSector(Boolean.valueOf(jnjRegistrationData.getJnjSectorTypeData().isMddSector()));
		customerData.setConsumerSector(Boolean.valueOf(jnjRegistrationData.getJnjSectorTypeData().isConsumerProductsSector()));
	    customerData.setPharmaSector(Boolean.valueOf(jnjRegistrationData.getJnjSectorTypeData().isPharmaSector()));
		customerData.setUid(jnjRegistrationData.getJnjUserInfoData().getEmailAddress().toLowerCase());
		customerData.setContactAddress(setAddress(jnjRegistrationData.getJnjUserInfoData()));
		customerData.setSecretQuestionsAnswers(createSecretQuestionData(jnjRegistrationData));
		customerData.setEmailPreferences(jnjRegistrationData.getEmailPreferences());
		/*5506*/
		customerData.setSmsPreferences(jnjRegistrationData.getSmsPreferences());
		setCustomerStatus(customerData, jnjRegistrationData);
		customerData.setLoginDisabledFlag(Boolean.TRUE);
		customerData.setIsResetPassword(Boolean.TRUE);
		//Sets the Company Info for the user.
		if (jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile().equals(JJ_EMPLOYEE))
		{
			customerData.setAccessBy(AccessBy.WWID.name());
		}
		else
		{
			customerData.setAccessBy(AccessBy.NOT_SALES_REP.name());
		}
		customerData.setShippedOrderType(jnjRegistrationData.getShippedOrderEmailType());

		customerData.setInvoiceEmailPreferenceType(jnjRegistrationData.getInvoiceEmailPrefType());
		customerData.setDeliveryNoteEmailPreferenceType(jnjRegistrationData.getDeliveryNoteEmailPrefType());
		jnjGTCustomerReversePopulator.populate(customerData, newCustomer);
		//set the accounts and the role of the user.
		setGroups(newCustomer, jnjRegistrationData);
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		newCustomer.setPasswordEncoding(Config.getParameter(REGISTER_USER_PASSWORD_ENCODING));
		setUidForRegister(jnjRegistrationData, newCustomer);//Remove now it is not needed
		final String newEncodedPass = passwordEncoderService.encode(newCustomer, jnjRegistrationData.getPassword(),
				Config.getParameter(REGISTER_USER_PASSWORD_ENCODING));
		newCustomer.setEncodedPassword(newEncodedPass);
		setOldPasswords(newCustomer);
		//Sets the Company Info for the user.
		if (jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile().equals(JJ_CUSTOMER))
		{
			setCompanyInformation(newCustomer, companyInfoModel, jnjRegistrationData.getCompanyInfoData());
		}
//		setPrivacyPolicyVersions(newCustomer);
		try
		{
			getModelService().saveAll(newCustomer);
			getModelService().refresh(newCustomer);
			if (jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile().equals(JJ_EMPLOYEE))
			{
				jnjGTTerritoryService.alignCustomerWithTerritoryWithWwid(jnjRegistrationData.getJnjSectorTypeData().getWwid(),
						newCustomer);
			}
			status = true;
		}
		catch (final ModelSavingException exp)
		{
			exp.printStackTrace();
			if (LOG.isDebugEnabled())
			{
				LOG.error(Logging.REGISTRAION + Logging.HYPHEN + "register()" + Logging.HYPHEN + "Unable to save the new user : "
						+ exp);
			}
			status = false;
		}
		if (status)
		{
			/** Sending email to CSR - Successful Registration **/
			final boolean emailStatusCSR = sendSuccessfulRegistrationEmail(jnjRegistrationData, siteLogoURL, newCustomer);

			/** Sending email to User - Successful Registration **/
			//JJEPIC-600 - final boolean emailStatusUser = sendRegistrationConfirmationEmail(siteLogoURL, jnjRegistrationData, newCustomer);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Email to CSR sending status :: " + emailStatusCSR);
				//JJEPIC-600 - LOG.debug("Email to User sending status :: " + emailStatusUser);
			}

		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "register()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		//returning status of registration process.
		return status;
	}

	/**
	 * This method set the Status Of the User
	 *
	 * @param customerData
	 * @param jnjRegistrationData
	 */
	protected void setCustomerStatus(final JnjGTCustomerData customerData, final JnjRegistrationData jnjRegistrationData)
	{

		//Setting the Status For An External User With View Only  As Pending Profile Setup
		if (!jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile().equals(JJ_EMPLOYEE)
				&& jnjRegistrationData.getJnjUserInfoData().getPermissionLevel().equalsIgnoreCase("viewOnly"))
		{
			customerData.setStatus(CustomerStatus.PENDING_PROFILE_SETUP.getCode());
		}

		//Setting the Status For An Internal && External User With Place Order  As Pending Supervisor Response
		else
		{
			customerData.setStatus(CustomerStatus.PENDING_SUPERVISOR_RESPONSE.getCode());
		}
	}

	/**
	 * @param newCustomer
	 * @param jnjRegistrationData
	 */
	protected void setGroups(final JnJB2bCustomerModel newCustomer, final JnjRegistrationData jnjRegistrationData)
	{
		final String profile = jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile();
		final Set<PrincipalGroupModel> customerGroups = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
		switch (profile)
		{
			case CUST_ACCOUNT:
				if (jnjRegistrationData.getJnjSectorTypeData().isUnknownAccount())
				{
					customerGroups.addAll(getDummyUnitGroup(newCustomer));
					newCustomer.setGroups(customerGroups);
				}
				else
				{
					//Find account to add to the group.
					final Set<JnJB2BUnitModel> unitSet = jnjGTCustomerService.getB2bUnitsForAccountNumbers(jnjRegistrationData
							.getJnjSectorTypeData().getAccountNumbers());
					final Set<PrincipalGroupModel> userGroupSet = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
					userGroupSet.addAll(unitSet);
					newCustomer.setGroups(userGroupSet);
				}
				setRoleForUser(newCustomer, jnjRegistrationData);
				break;

			case JJ_CUSTOMER:
				customerGroups.addAll(getDummyUnitGroup(newCustomer));
				newCustomer.setGroups(customerGroups);
				setRoleForUser(newCustomer, jnjRegistrationData);
				break;

			case JJ_EMPLOYEE:

				final Set<PrincipalGroupModel> set = new HashSet<PrincipalGroupModel>();
				if (jnjRegistrationData.getJnjUserInfoData().getPermissionLevel().equals("viewOnly"))
				{
					set.add(userService.getUserGroupForUID(Config
							.getParameter(Jnjb2bCoreConstants.Register.VIEW_ORDER_SALES_USER_ROLE)));

				//AAOL-2429 and AAOL-2433 changes
				} else if (jnjRegistrationData.getJnjUserInfoData().getPermissionLevel().equals("noCharge")) {
					set.add(userService.getUserGroupForUID(Config
							.getParameter(Jnjb2bCoreConstants.Register.USER_GROUP_NO_CHARGE_USERS)));
					newCustomer.setNoCharge(Boolean.TRUE);
				}

				else
				{
					set.add(userService.getUserGroupForUID(Config
							.getParameter(Jnjb2bCoreConstants.Register.PLACE_ORDER_SALES_USER_ROLE)));
				}
				customerGroups.addAll(set);
				newCustomer.setGroups(customerGroups);
				//Added the units based on the account number as entered by the user.
				if (StringUtils.isEmpty(jnjRegistrationData.getJnjSectorTypeData().getGlnOrAccountNumber()))
				{
					customerGroups.addAll(getDummyUnitGroup(newCustomer));
					newCustomer.setGroups(customerGroups);
				}
				else
				{
					//Find account to add to the group.
					final Set<JnJB2BUnitModel> unitSet = jnjGTCustomerService.getB2bUnitsForAccountNumbers(jnjRegistrationData
							.getJnjSectorTypeData().getGlnOrAccountNumber());
					final Set<PrincipalGroupModel> userGroupSet = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
					userGroupSet.addAll(unitSet);
					newCustomer.setGroups(userGroupSet);
				}
				break;
		}
		//Added the default b2bCustomerGroup to the User, so that the user can login.
		final Set<PrincipalGroupModel> customerGroup = new HashSet<PrincipalGroupModel>();
		customerGroup.add(userService.getUserGroupForUID(B2BCUSTOMERGROUP));
		final Set<PrincipalGroupModel> customerGroupSet = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
		customerGroupSet.addAll(customerGroup);
		newCustomer.setGroups(customerGroupSet);
	}



	/**
	 * @param newCustomer
	 * @param jnjRegistrationData
	 */
	protected void setRoleForUser(final JnJB2bCustomerModel newCustomer, final JnjRegistrationData jnjRegistrationData)
	{
		final Set<PrincipalGroupModel> userGroupSet = new HashSet<PrincipalGroupModel>();
		if (jnjRegistrationData.getJnjUserInfoData().getPermissionLevel().equals("viewOnly"))
		{
			userGroupSet.add(userService.getUserGroupForUID(Config
					.getParameter(Jnjb2bCoreConstants.Register.VIEW_ORDER_BUYER_USER_ROLE)));
		}
		else
		{
			userGroupSet.add(userService.getUserGroupForUID(Config
					.getParameter(Jnjb2bCoreConstants.Register.PLACE_ORDER_BUYER_USER_ROLE)));
		}
		final Set<PrincipalGroupModel> customerGroups = new HashSet<PrincipalGroupModel>(newCustomer.getGroups());
		customerGroups.addAll(userGroupSet);
		newCustomer.setGroups(customerGroups);
	}


	/**
	 * This method will save the company info as entered by the user at the time of the registration.
	 *
	 * @param newCustomer
	 * @param companyInfoModel
	 * @param companyInfoData
	 */
	protected void setCompanyInformation(final JnJB2bCustomerModel newCustomer, final CompanyInfoModel companyInfoModel,
			final JnjCompanyInfoData companyInfoData)
	{
		companyInfoReversePopulator.populate(companyInfoData, companyInfoModel);
		newCustomer.setCompanyInformation(companyInfoModel);
	}

	/**
	 * This method creates the secret question data list by extracting the values from the register data.
	 *
	 * @param registrationData
	 * @return List<SecretQuestionData> secretQuestionData
	 */
	protected List<SecretQuestionData> createSecretQuestionData(final JnjRegistrationData registrationData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "createSecretQuestionData()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<SecretQuestionData> secretQuestionData = new ArrayList<SecretQuestionData>();
		for (final SecretQuestionData secretQuestion : registrationData.getSecretQuestionsAnswers())
		{
			if (secretQuestion.getCode() != null && StringUtils.isNotBlank(secretQuestion.getCode())
					&& secretQuestion.getAnswer() != null && StringUtils.isNotBlank(secretQuestion.getAnswer()))
			{
				secretQuestionData.add(secretQuestion);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "createSecretQuestionData()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return secretQuestionData;
	}

	/**
	 * This method sets the address in the address data and returns it.
	 *
	 * @param jnjUserInfoData
	 * @return AddressData
	 */
	protected AddressData setAddress(final JnjUserInfoData jnjUserInfoData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setAddress()"
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		//Creating instance of AddressData
		final JnjGTAddressData newAddress = new JnjGTAddressData();
		newAddress.setFirstName(jnjUserInfoData.getFirstName());
		newAddress.setLastName(jnjUserInfoData.getLastName());
		newAddress.setLine1(jnjUserInfoData.getAddressLine1());
		newAddress.setLine2(jnjUserInfoData.getAddressLine2());
		newAddress.setCompanyName(jnjUserInfoData.getOrgName());
		newAddress.setEmail(jnjUserInfoData.getEmailAddress());
		newAddress.setTown(jnjUserInfoData.getCity());
		newAddress.setPostalCode(jnjUserInfoData.getZip());
		newAddress.setPhone(jnjUserInfoData.getPhone());
		newAddress.setFax(jnjUserInfoData.getFax());
		newAddress.setMobile(jnjUserInfoData.getMobile());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(false);
		newAddress.setVisibleInAddressBook(true);
		newAddress.setDefaultAddress(true);


		final CountryData country = new CountryData();
		country.setIsocode(jnjUserInfoData.getCountry());
		newAddress.setCountry(country);


		if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.US, jnjUserInfoData.getCountry()))
		{
			final RegionData region = new RegionData();
			region.setName(jnjUserInfoData.getState());
			region.setIsocode(jnjUserInfoData.getState());
			region.setIsocodeShort(jnjUserInfoData.getState());
			region.setCountryIso(jnjUserInfoData.getCountry());
			newAddress.setRegion(region);
		}
		else
		{
			newAddress.setState(jnjUserInfoData.getState());
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN + "setAddress()"
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		return newAddress;
	}

	public List<SecretQuestionsAndAnswersModel> getSecretQuestionsForRegistration(
			final List<SecretQuestionData> secretQuestionDataList, final String emailId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final List<SecretQuestionsAndAnswersModel> questionsAndAnswersList = new ArrayList<SecretQuestionsAndAnswersModel>();
		for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
		{
			final SecretQuestionsAndAnswersModel secretQuestionModel = new SecretQuestionsAndAnswersModel();
			secretQuestionModel.setQuestionId(secretQuestionData.getCode());
			secretQuestionModel.setAnswer(secretQuestionData.getAnswer());

			try
			{
				questionsAndAnswersList.add(secretQuestionModel);
				getModelService().save(secretQuestionModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOG.error("Unable to save the secret questions for the user :" + emailId);
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.REGISTRAION + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "updateSecretQuestionsForRegistration()" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return questionsAndAnswersList;

	}


	protected void setUidForRegister(final JnjRegistrationData registerData, final JnJB2bCustomerModel customer)
	{
		customer.setUid(registerData.getJnjUserInfoData().getEmailAddress().toLowerCase());
		customer.setOriginalUid(registerData.getJnjUserInfoData().getEmailAddress());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#getEmailPreferences()
	 */
	@Override
	public Map<String, String> getEmailPreferences()
	{
		final Map<String, String> emailPrefMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> emailPrefKeyList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.EMAIL_PREFERENCE);
		for (final JnjConfigModel emailPref : emailPrefKeyList)
		{
			try
			{
				emailPrefMap.put(emailPref.getKey(),
						messageService.getMessageForCode(emailPref.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + emailPref.getKey(), exception);
			}
		}
		return emailPrefMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#getSecurityQuestions()
	 */

	@Override
	public Map<String, String> getEmailPreferences(String emailPrefKey)
	{
		final LinkedHashMap<String, String> emailPrefMap = new LinkedHashMap<String, String>();
		final LinkedHashMap<String, String> changedEmailPrefMap = new LinkedHashMap<String, String>();
		String key ="";
		final List<JnjConfigModel> emailPrefKeyList = getValuesBasedOnId(emailPrefKey+Jnjb2bCoreConstants.Register.EMAIL_PREFERENCE);
		for (final JnjConfigModel emailPref : emailPrefKeyList)
		{
			try
			{
				emailPrefMap.put(emailPref.getKey(),
						messageService.getMessageForCode(emailPref.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + emailPref.getKey(), exception);
			}
		}

		if(emailPrefKey.equalsIgnoreCase("placeorder")){
		 Map<String, String> treeMap = new TreeMap<String, String>(
	                new Comparator<String>() {

	                    @Override
	                    public int compare(String key1, String key2) {
	                        int k1= Integer.parseInt(key1.substring(15, key1.length()));
	                        int k2 = Integer.parseInt(key2.substring(15, key2.length()));
	                        return Integer.compare(k1, k2);
	                    }

	                });

		 treeMap.putAll(emailPrefMap);
		return treeMap;
		}else if(emailPrefKey.equalsIgnoreCase("noCharge")){
			 Map<String, String> treeMap = new TreeMap<String, String>(
		                new Comparator<String>() {

		                    @Override
		                    public int compare(String key1, String key2) {
		                        int k1= Integer.parseInt(key1.substring(15, key1.length()));
		                        int k2 = Integer.parseInt(key2.substring(15, key2.length()));
		                        return Integer.compare(k1, k2);
		                    }

		                });

			 treeMap.putAll(emailPrefMap);
			 emailPrefMap.putAll(treeMap);
			// changedEmailPrefMap =(LinkedHashMap<String, String>) emailPrefMap.clone();
			for (Entry<String, String> map : treeMap.entrySet()) {
				if (!map.getKey().equalsIgnoreCase("emailPreference6")) {
					changedEmailPrefMap.put(map.getKey(), map.getValue());
				}
				if (changedEmailPrefMap.containsKey("emailPreference8")
						&& !changedEmailPrefMap.containsKey("emailPreference6")) {
					changedEmailPrefMap.put("emailPreference6",emailPrefMap.get("emailPreference6"));
				}
			}
			return changedEmailPrefMap;
		}else{
			return emailPrefMap;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#getSecurityQuestions()
	 */

	@Override
	public Map<String, String> getSecurityQuestions()
	{
		final Map<String, String> questionsMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> questionKeysList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.SECURITY_QUESTIONS);
		for (final JnjConfigModel question : questionKeysList)
		{
			try
			{
				questionsMap.put(question.getKey(),
						messageService.getMessageForCode(question.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + question.getKey(), exception);
			}
		}
		return questionsMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#getDivisions()
	 */
	@Override
	public Map<String, String> getDivisions()
	{
		final Map<String, String> divisionMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> divisionKeyList = getSortedDivsioncodes(getValuesBasedOnId(Jnjb2bCoreConstants.Register.DIVISIONS));

		for (final JnjConfigModel division : divisionKeyList)
		{
			try
			{
				divisionMap.put(division.getKey(),
						messageService.getMessageForCode(division.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + division.getKey(), exception);
			}
		}
		return divisionMap;
	}

	protected List<JnjConfigModel> getSortedDivsioncodes(List<JnjConfigModel> divisionKeyList) {
		List<JnjConfigModel> keyList = new ArrayList<JnjConfigModel>(divisionKeyList);

		Collections.sort(keyList, new Comparator<JnjConfigModel>()
			{
				@Override
				public int compare(final JnjConfigModel arg0, final JnjConfigModel arg1)
				{
					String sa = null;
					String sb = null;
					try {
						sa = messageService.getMessageForCode(arg0.getKey(), i18nService.getCurrentLocale());
						sb = messageService.getMessageForCode(arg1.getKey(), i18nService.getCurrentLocale());
					} catch (BusinessException e) {
						LOG.error("Unable to render message text");
					}
					int v = 0;
					if (sa != null && sb != null)
					{
						v = sa.compareTo(sb);
					}
					return v;
				}
			});
		return keyList;
	}


	/**
	 * This method will return the list of territories to be displayed to the JnJ employee at the Edit User
	 *
	 * @return Map of territories.
	 */
	@Override
	public Map<String, String> getTerritories()
	{

		final Map<String, String> territoryMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> divisionKeyList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.TERRITORIES);
		for (final JnjConfigModel division : divisionKeyList)
		{
			try
			{
				territoryMap.put(division.getKey(),
						messageService.getMessageForCode(division.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + division.getKey(), exception);
			}
		}
		return territoryMap;
	}


	/**
	 * This method will return the list of division to be displayed to the JnJ employee at the time of registration and
	 * Edit User which includes also the Consumer Divisions
	 *
	 * @return Map of divisions.
	 */
	@Override
	public Map<String, String> getConsumerDivisons()
	{
		final Map<String, String> divisionMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> divisionKeyList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.DIVISIONS);
		for (final JnjConfigModel division : divisionKeyList)
		{
			try
			{
				divisionMap.put(division.getKey(),
						messageService.getMessageForCode(division.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + division.getKey(), exception);
			}
		}
		final List<JnjConfigModel> consumerdivisionKeyList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.CONSUMER_DIVISONS);
		for (final JnjConfigModel division : consumerdivisionKeyList)
		{
			try
			{
				divisionMap.put(division.getKey(),
						messageService.getMessageForCode(division.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + division.getKey(), exception);
			}
		}

		return divisionMap;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.customer.JnjGTCustomerFacade#getDepartments()
	 */
	@Override
	public Map<String, String> getDepartments()
	{
		final Map<String, String> departmentMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> departmentKeyList = getValuesBasedOnId(Jnjb2bCoreConstants.Register.DEPARTMENTS);
		for (final JnjConfigModel department : departmentKeyList)
		{
			try
			{
				departmentMap.put(department.getKey(),
						messageService.getMessageForCode(department.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{
				LOG.error("Unable to render message text for message code : " + department.getKey(), exception);
			}
		}
		return departmentMap;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#getCompanyInfoDropdowns()
	 */
	@Override
	public Map<String, Map<String, String>> getCompanyInfoDropdowns(final List<String> list)
	{
		final Map<String, Map<String, String>> dropdownMap = new LinkedHashMap<String, Map<String, String>>();

		for (final String key : list)
		{
			List<JnjConfigModel> dropdownKeyList = getValuesBasedOnId(key);

			List<JnjConfigModel> dropdownKeyListSorted = new ArrayList<JnjConfigModel>(dropdownKeyList);

			dropdownKeyListSorted = sortDropdownValues(dropdownKeyListSorted);

			final Map<String, String> dropdownList = new LinkedHashMap<String, String>();
			for (final JnjConfigModel dropdownKey : dropdownKeyListSorted)
			{
				try
				{
					dropdownList.put(dropdownKey.getKey(),
							messageService.getMessageForCode(dropdownKey.getKey(), i18nService.getCurrentLocale()));
				}
				catch (final BusinessException exception)
				{
					LOG.error("Unable to render message text for message code : " + dropdownKey.getKey(), exception);
				}
			}
			dropdownMap.put(key, dropdownList);
		}
		return dropdownMap;
	}


	protected static List<JnjConfigModel> sortDropdownValues(List<JnjConfigModel> dropdownList) {

	      Collections.sort(dropdownList, new java.util.Comparator<JnjConfigModel>() {
	              public int compare(JnjConfigModel obj1, JnjConfigModel obj2) {
	                  return getIndexValue(obj1).compareTo(getIndexValue(obj2));
	              }
	      });

	      return dropdownList;
	  }


	protected static Integer getIndexValue(JnjConfigModel obj) {
		 Integer indexValue = 0;
		 for(int i=obj.getKey().length()-1; i >= 0; i-- ) {

   		  if(!Character.isDigit(obj.getKey().charAt(i))) {
   			indexValue = Integer.valueOf(obj.getKey().substring(i+1));
   			  break;
   		  }

   	  }
		return indexValue;
	}

	/*
	 * This method is used for saving the updates to the User in Edit Profile
	 */

	@Override
	public void updateCustomer(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel;
		if (StringUtils.isEmpty(customerData.getUid()))
		{
			customerModel = getModelService().create(JnJB2bCustomerModel.class);
			//If the CSR has not selected the Account for the User Management
			if (CollectionUtils.isEmpty(customerData.getGroups()))
			{
				customerModel.setGroups(getDummyUnitGroup(customerModel));
			}
		}
		else
		{
			customerModel = (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		}
		jnjGTCustomerReversePopulator.populate(customerData, customerModel);
		/** Setting the latest privacy policy version **/
		setPrivacyPolicyVersions(customerModel);
		getModelService().save(customerModel);
		if (StringUtils.isNotEmpty(customerData.getWwid()))
		{
			jnjGTTerritoryService.alignCustomerWithTerritoryWithWwid(customerData.getWwid(), customerModel);
		}
		getModelService().refresh(customerModel);
	}

	/* This method is used for add New Account For User In Add Account Module */

	@Override
	public boolean addNewAccount(final JnjCompanyInfoData companyData)
	{
		boolean savedCompanyModel = false;
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		//wrong
		final CompanyInfoModel company = getModelService().create(CompanyInfoModel.class);
		companyInfoReversePopulator.populate(companyData, company);
		try
		{
			getModelService().save(company);
			currentCustomer.setCompanyInformation(company);
			getModelService().saveAll(currentCustomer);
			final JnjGTAddAccountEvent jnjGTAddAccountEvent = new JnjGTAddAccountEvent(companyData);
			eventService.publishEvent(initializeEvent(jnjGTAddAccountEvent));
			savedCompanyModel = true;
		}
		catch (final ModelSavingException exception)
		{
			LOG.error("Unable to save data", exception);
		}
		return savedCompanyModel;
	}


	protected JnjGTAddAccountEvent initializeEvent(final JnjGTAddAccountEvent event)
	{
		LOG.debug("Entered the initializeEvent() method");
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		LOG.debug("Exiting the initializeEvent() method");
		return event;
	}



	protected List<JnjConfigModel> getValuesBasedOnId(final String id)
	{
		return jnjConfigService.getConfigModelsByIdAndKey(id, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.na.facades.login.jnjGTUserFacade#validateAccountNumbers(java.lang.String[])
	 */
	@Override
	public String validateAccountNumbers(final String[] accountNumbers)
	{
		return jnjGTCustomerService.validateAccountNumbers(accountNumbers);
	}

	/**
	 * This method is used to validate the account numbers + GLNs for registration process
	 *
	 * @param accountNumbers
	 * @return String
	 */
	@Override
	public String validateAccountNumbersRegistration(final String[] accountNumbers)
	{
		return jnjGTCustomerService.validateAccountNumbersRegistration(accountNumbers);
	}

	@Override
	public String validateWwid(final String wwid)
	{
		return jnjGTCustomerService.validateWwid(wwid);
	}

	/**
	 * Customer Basic Converter Getter
	 *
	 * @return the customerBasicConverter
	 */
	public Converter<CustomerModel, CustomerData> getCustomerBasicConverter()
	{
		return customerBasicConverter;
	}

	/**
	 * Customer Basic Converter Setter
	 *
	 * @param customerBasicConverter
	 *           the customerBasicConverter to set
	 */
	public void setCustomerBasicConverter(final Converter<CustomerModel, CustomerData> customerBasicConverter)
	{
		this.customerBasicConverter = customerBasicConverter;
	}

	@Override
	public boolean verifyExistingPassword(final String currentPassword, final String email) throws BusinessException
	{
		boolean passVerify = false;
		/** Fetching current customer **/
		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		return passwordEncoderService.isValid(currentCustomer, currentPassword);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.jnj.facades.customer.JnjGTCustomerFacade#updateUserInformation(com.jnj.facades.data.JnjGTCustomerData)
	 */
	@Override
	public List<String> getPhoneCodes()
	{

		return jnjGTCustomerService.getPhoneCodes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.customer.JnjGTCustomerFacade#addExistingAccount(com.jnj.core.dto.JnjCompanyInfoData)
	 */
	@Override
	public boolean addExistingAccount(final String[] accountNumbers)
	{
		eventService.publishEvent(initializeEvent(new JnjGTAddExistingAccountEvent(), accountNumbers));
		return true;
	}

	protected JnjGTAddExistingAccountEvent initializeEvent(final JnjGTAddExistingAccountEvent event, final String[] accountNumbers)
	{
		LOG.debug("Entered the initializeEvent() method");
		event.setFirstName(getCurrentGTCustomer().getFirstName());
		event.setLastName(getCurrentGTCustomer().getLastName());
		event.setAccountNumbers(accountNumbers);
		event.setEmail(getCurrentGTCustomer().getUid());
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		LOG.debug("Exiting the initializeEvent() method");
		return event;
	}

	@Override
	public boolean updateSecretQuestionsForUser(final List<SecretQuestionData> secretQuestionDataList, final String emailId)
	{
		boolean saved = false;
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(emailId, B2BCustomerModel.class);
		final List<SecretQuestionsAndAnswersModel> secretQuestionModelList = new ArrayList<SecretQuestionsAndAnswersModel>();
		for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
		{
			final SecretQuestionsAndAnswersModel secretQuestionModel = new SecretQuestionsAndAnswersModel();
			if ((StringUtils.isNotBlank(secretQuestionData.getCode())) && (StringUtils.isNotEmpty(secretQuestionData.getCode())))
			{
				secretQuestionModel.setQuestionId(secretQuestionData.getCode());
				secretQuestionModel.setAnswer(secretQuestionData.getAnswer());
				try
				{
					getModelService().save(secretQuestionModel);
				}
				catch (final ModelSavingException modelSavingException)
				{
					LOG.error("Unable to save the secret questions for the user :" + customer.getUid());

				}
				secretQuestionModelList.add(secretQuestionModel);
			}
		}
		try
		{
			customer.setSecretQuestionsAndAnswersList(secretQuestionModelList);
			getModelService().save(customer);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOG.error("Unable to save the secret questions for the user :" + customer.getUid());
			modelSavingException.getMessage();
		}
		return saved;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTCustomerData getCustomerForUid(final String user)
	{

		final JnJB2bCustomerModel jnjGTB2BCustomer = userService.getUserForUID(user, JnJB2bCustomerModel.class);
		final JnjGTCustomerData jnjGTcustomerData = new JnjGTCustomerData();
		customerConverter.convert(jnjGTB2BCustomer, jnjGTcustomerData);  //going to JnjGTCustomerPopulator
		return jnjGTcustomerData;
	}

	@Override
	public void sentPasswordExpirymail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException
	{
		final String METHOD_NAME = "sentPasswordExpirymail()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.USER_PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
				Logging.BEGIN_OF_METHOD, LOG);
		final JnJB2bCustomerModel jnjGTB2BCustomer = userService.getUserForUID(userId, JnJB2bCustomerModel.class);
		jnjGTCustomerService.sentPasswordExpiryEmail(jnjGTB2BCustomer, siteUrl, false);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.USER_PASSWORD_EXPIRY_EMAIL, METHOD_NAME,
				Logging.BEGIN_OF_METHOD, LOG);
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 *
	 * @throws Exception
	 */
	@Override
	public boolean resetUserPassword(final String user)
	{
		try
		{
			final JnJB2bCustomerModel jnjGTB2BCustomer = userService.getUserForUID(user, JnJB2bCustomerModel.class);
			jnjGTB2BCustomer.setForcefulExpired(Boolean.TRUE);
			getModelService().save(jnjGTB2BCustomer);
			getModelService().refresh(jnjGTB2BCustomer);
			return true;
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			return false;
		}
	}

	@Override
	public JnJB2bCustomerModel getJnjGTcustomerModel()
	{
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			return (JnJB2bCustomerModel) userService.getCurrentUser();
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean saveJnjGTCustomerModel(final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		return jnjGTCustomerService.saveJnjGTB2bCustomer(JnJB2bCustomerModel);
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public Map<String, String> getAccountsMap(final boolean addCurrentB2BUnit, final List<String> userAccounts)
	{

		final Map<String, String> accountsMap = getAccountsMap(true);

		if (CollectionUtils.isEmpty(userAccounts))
		{
			return accountsMap;
		}
		final Map<String, String> finalAccountsMap = new LinkedHashMap<>();

		for (final String selectedAccount : userAccounts)
		{
			final String selectedAccountData = accountsMap.get(selectedAccount);
			if (StringUtils.isNotEmpty(selectedAccountData))
			{
				finalAccountsMap.put(selectedAccount, selectedAccountData);
			}
		}

		for (final Entry<String, String> entry : accountsMap.entrySet())
		{
			if (!finalAccountsMap.containsKey(entry.getKey()))
			{
				finalAccountsMap.put(entry.getKey(), entry.getValue());
			}
		}

		return finalAccountsMap;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public String addToExistingNotes( final String existingNotes)
	{
		final B2BCustomerModel csrUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final StringBuilder notes = new StringBuilder(existingNotes).append("\n")
				.append(" - " + csrUser.getName() + " - ").append(new Date().toString());
		return notes.toString();
	}


	/**
	 * This method checks if the user's Has A JnjDummyUnit
	 *
	 * @param JnJB2bCustomerModel
	 * @return true/false
	 */
	@Override
	public boolean userWithDummyUnit(final JnJB2bCustomerModel currentUser)
	{

		boolean isDummyUnitExist = false;
		final Set<PrincipalGroupModel> groups = currentUser.getGroups();
		final JnJB2BUnitModel dummyUnit = (JnJB2BUnitModel) jnjGTB2BUnitService
				.getUnitForUid(Jnjb2bCoreConstants.B2BUnit.JNJDUMMYUNIT);

		if (groups.contains(dummyUnit))
		{
			isDummyUnitExist = true;
		}
		return isDummyUnitExist;
	}

	/**
	 * This method checks if the user has the user group for PCM associated with it.
	 *
	 * @param currentUser
	 * @return true/false
	 */
	/*@Override
	public boolean isPCMUser(final JnJB2bCustomerModel currentUser)
	{

		boolean isPCMUser = false;
		final Set<PrincipalGroupModel> groups = currentUser.getAllGroups();
		final UserGroupModel pcmUserGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID(JnjPCMCoreConstants.PCM_USER_GROUP,
				UserGroupModel.class);
		if (groups.contains(pcmUserGroup))
		{
			isPCMUser = true;
		}
		return isPCMUser;
	}*/

	/**
	 * This method will return the list of associated countries with a PCM user.
	 *
	 * @return list of associated country data.
	 */
	@Override
	public List<CountryData> fetchAssociatedCountries()
	{
		final List<CountryData> countryList = new ArrayList<CountryData>();
		/*JnJB2bCustomerModel jnjGTCustomerModel = null;
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		if (customerModel instanceof JnJB2bCustomerModel)
		{
			jnjGTCustomerModel = (JnJB2bCustomerModel) customerModel;
			for (final CountryModel country : jnjGTCustomerModel.getAssociatedCountries())
			{
				final CountryData countryData = new CountryData();
				countryData.setIsocode(country.getIsocode());
				countryData.setName(country.getName());
				countryList.add(countryData);
			}
		}*/
		return countryList;
	}

	/**
	 * This method will return the list of associated countries with a PCM user.
	 *
	 * @return list of associated country data.
	 */
	@Override
	public CountryData getCurrentCountry()
	{
		final String methodName = "getCurrentCountry()";
		CommonUtil.logMethodStartOrEnd("Fetch Current Country for PCM user", methodName, Logging.BEGIN_OF_METHOD, LOG);
		CountryData countryData = null;
		/*final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		CommonUtil.logDebugMessage("Fetch Current Country for PCM user", methodName,
				"The following customer model is found for the current user " + customerModel, LOG);
		if (customerModel instanceof JnJB2bCustomerModel)
		{
			jnjGTCustomerModel = (JnJB2bCustomerModel) customerModel;
			final CountryModel country = jnjGTCustomerModel.getCurrentCountry();
			if (country != null)
			{
				CommonUtil.logDebugMessage("Fetch Current Country for PCM user", methodName,
						"The current country associated with the user is " + country.getName(), LOG);
				countryData = new CountryData();
				countryData.setIsocode(country.getIsocode());
				countryData.setName(country.getName());
			}
		}
		CommonUtil.logMethodStartOrEnd("Fetch Current Country for PCM user", methodName, Logging.END_OF_METHOD, LOG);*/
		return countryData;
	}

	@Override
	public boolean setCurrentCountryAndUnitForPCM(final String countryCode)
	{
		//return jnjGTCustomerService.setCurrentCountryAndUnitForPCM(countryCode);
		return false;
	}

	/**
	 * This method is used to send an email to the CSR when the user request an account for the PCM site.
	 *
	 * @return boolean
	 */
	/*@Override
	public boolean sendRequestAccountEmailToCSR(final RequestAccountData requestAccountData, final String siteLogoUrl)
	{
		final String METHOD_NAME = "sendRequestAccountEmailToCSR()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean sendStatus = false;
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to publish event JnjPCMRequestAccountEmailEvent");
		try
		{
			*//** Publishing event responsible for successful registration email flow **//*
			eventService.publishEvent(initializeRequestAccountEmailEvent(new JnjPCMRequestAccountEmailEvent(siteLogoUrl
					+ jnjCommonFacadeUtil.createMediaLogoURL(), requestAccountData)));
			sendStatus = true;
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
					"JnjPCMRequestAccountEmailEvent has been published!");
		}
		catch (final Exception exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to publish JnjPCMRequestAccountEmailEvent : " + exception.getMessage());
		}
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return sendStatus;
		return false;
	}*/

	/*
	 * @see com.jnj.facades.customer.JnjGTCustomerFacade#updateDataForNewPCMUser(java.lang.String)
	 */
	/*@Override
	public void updateDataForNewPCMUser(final JnjGTCustomerData jnjGTCustomerData)
	{
		final String METHOD_NAME = "updateDataForNewPCMUser()";
		final List<CountryData> countryDatas = fetchAssociatedCountries();
		CountryModel country = null;
		final List<String> b2bUnits = new ArrayList<String>();
		JnJB2BUnitModel JnJB2BUnitModel = null;
		boolean noCountryAssociated = false;
		String countryCode = null;
		if (CollectionUtils.isNotEmpty(countryDatas))// check if country is associated for the user or not.
		{
			for (final CountryData countryData : countryDatas)
			{
				countryCode = countryData.getIsocode();
				country = commonI18NService.getCountry(countryData.getIsocode());
				JnJB2BUnitModel = new JnJB2BUnitModel();
				JnJB2BUnitModel.setCountry(country);
				JnJB2BUnitModel.setSourceSysId(JnjPCMCoreConstants.PCM);
				JnJB2BUnitModel = flexibleSearchService.getModelByExample(JnJB2BUnitModel);
				b2bUnits.add(JnJB2BUnitModel.getUid());
			}
		}
		else
		{
			countryCode = JnJCommonUtil.getValue(Login.PCM_DEFAULT_COUNTRY_FOR_USER);
			country = commonI18NService.getCountry(JnJCommonUtil.getValue(Login.PCM_DEFAULT_COUNTRY_FOR_USER));
			JnJB2BUnitModel = new JnJB2BUnitModel();
			JnJB2BUnitModel.setCountry(country);
			JnJB2BUnitModel.setSourceSysId(JnjPCMCoreConstants.PCM);
			JnJB2BUnitModel = flexibleSearchService.getModelByExample(JnJB2BUnitModel);
			b2bUnits.add(JnJB2BUnitModel.getUid());
			noCountryAssociated = true;
		}
		final List<String> userGroupUids = new ArrayList<String>();
		String userGroupUid = null;
		UserGroupModel userGroup = null;
		jnjGTCustomerData.setGroups(b2bUnits);
		final JnJB2bCustomerModel customerModel = companyB2BCommerceService.getCustomerForUid(jnjGTCustomerData.getUid());
		for (final CountryData countryData : countryDatas)
		{
			countryCode = countryData.getIsocode();
			if (countryCode != null)
			{
				if (StringUtils.isNotBlank(customerModel.getWwid()))
				{
					userGroupUid = ("CA").equalsIgnoreCase(countryCode) ? JnJCommonUtil
							.getValue(JnjPCMCoreConstants.ProductWorkflows.NEW_PRODUCT_WORKFLOW_TO_EMAIL_CA) : JnJCommonUtil
							.getValue(JnjPCMCoreConstants.ProductWorkflows.NEW_PRODUCT_WORKFLOW_TO_EMAIL_US);
				}
				else
				{
					userGroupUid = ("CA").equalsIgnoreCase(countryCode) ? JnJCommonUtil
							.getValue(JnjPCMCoreConstants.ProductWorkflows.EXTERNAL_USER_GROUP_CA) : JnJCommonUtil
							.getValue(JnjPCMCoreConstants.ProductWorkflows.EXTERNAL_USER_GROUP_US);
				}
				userGroupUids.add(userGroupUid);
			}
		}
		try
		{
			final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(customerModel.getGroups());
			for (final String groupId : userGroupUids)
			{
				userGroup = userService.getUserGroupForUID(groupId);
				groups.add(userGroup);
			}
			customerModel.setGroups(groups);
		}
		catch (final UnknownIdentifierException exception)
		{
			LOG.error(exception.getMessage(), exception);
		}
		try
		{
			*//** Fetching the privacy policy component **//*
			final JnjParagraphComponentModel privacyPolicyParagraphComponentModel = cMSComponentService
					.getAbstractCMSComponent(Config.getParameter(Jnjb2bCoreConstants.Login.PRIVACY_POLICY_COMPONENT_UID_KEY));
			jnjGTCustomerData.setPolicyVersion(privacyPolicyParagraphComponentModel.getComponentVersion());
		}
		catch (final CMSItemNotFoundException cMSItemNotFoundException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.PRIVACY_POLICY_CHECK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to fetch PrivacyPolicyComponent with exception :: " + cMSItemNotFoundException);
		}
		customerModel.setCurrentB2BUnit(JnJB2BUnitModel);
		customerModel.setCurrentCountry(country);
		if (noCountryAssociated)
		{
			customerModel.setAssociatedCountries(Sets.newHashSet(country));
		}
		try
		{
			updateCustomer(jnjGTCustomerData);
		}
		catch (final DuplicateUidException e)
		{
			LOG.error("Exception occured while trying to update the PCM user with the default values", e);
		}
	}*/

	@Override
	public JnJB2BUnitModel getCurrentB2bUnit()
	{
		return jnjGTB2BUnitService.getCurrentB2BUnit();
	}

	/**
	 * This method checks if the user's registration is complete by checking the secret questions by calling the service
	 *
	 * @return true - if registration is complete ELSE false
	 */
	@Override
	public boolean isRegistrationComplete()
	{
		final String METHOD_NAME = "isRegistrationComplete()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerService.isRegistrationComplete();
	}

	@Override
	public String getEmailType(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel;
		customerModel = (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		return customerModel.getBackorderEmailType();
	}
	@Override
	public String getInoviceEmailType(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel;
		customerModel = (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		return customerModel.getInvoiceEmailPrefType();
	}
	@Override
	public String getDeliveryNoteEmailType(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel;
		customerModel = (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		return customerModel.getDeliveryNoteEmailPrefType();
	}

	@Override
	public String getShippedOrderEmailType(final JnjGTCustomerData customerData) throws DuplicateUidException
	{
		final JnJB2bCustomerModel customerModel;
		customerModel = (JnJB2bCustomerModel) userService.getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		return customerModel.getShippedOrderEmailType();
	}

	@Override
	public List<RegionData> getRegions(final String countryIso)
	{
		final CountryModel country = getCommonI18NService().getCountry(countryIso);
		final List<RegionData> resultList = new ArrayList<RegionData>();
		/*final List<String> usRegionsList = Arrays.asList(Config.getParameter(JnjPCMCoreConstants.PCM_REGIONS).split(","));
		final Map<String, String> usRegionMap = new HashMap<String, String>();
		for (final RegionModel regionModel : country.getRegions())
		{
			if (regionModel.getActive().booleanValue())
			{
				final RegionData regionData = new RegionData();
				regionData.setIsocode(regionModel.getIsocode());
				regionData.setName(regionModel.getName());
				if (!countryIso.equalsIgnoreCase(JnjPCMCoreConstants.US))
				{
					resultList.add(regionData);
				}
				else
				{
					if (usRegionsList.contains(regionModel.getIsocode()))
					{
						usRegionMap.put(regionModel.getIsocode(), regionModel.getName());
					}
					else
					{
						resultList.add(regionData);
					}
				}
			}
		}
		JnjObjectComparator jnjObjectComparator = null;
		jnjObjectComparator = new JnjObjectComparator(RegionData.class, "getName", true, true);
		Collections.sort(resultList, jnjObjectComparator);
		if (countryIso.equalsIgnoreCase(JnjPCMCoreConstants.US))
		{
			for (final String isoCode : usRegionsList)
			{
				final RegionData regionData = new RegionData();
				regionData.setIsocode(isoCode);
				regionData.setName(usRegionMap.get(isoCode));
				resultList.add(regionData);
			}
		}*/
		return resultList;
	}

	@Override
	public JnJB2bCustomerModel getCurrentSessionCustomer()
	{
		return (JnJB2bCustomerModel) getUserService().getCurrentUser();
	}

	/**
	 * @return the passwordEncoderService
	 */
	public PasswordEncoderService getPasswordEncoderService() {
		return passwordEncoderService;
	}

	/**
	 * @param passwordEncoderService the passwordEncoderService to set
	 */
	public void setPasswordEncoderService(
			PasswordEncoderService passwordEncoderService) {
		this.passwordEncoderService = passwordEncoderService;
	}

	/**
	 * @return the jnjGTCustomerService
	 */
	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	/**
	 * @param jnjGTCustomerService the jnjGTCustomerService to set
	 */
	public void setJnjGTCustomerService(JnjGTCustomerService jnjGTCustomerService) {
		this.jnjGTCustomerService = jnjGTCustomerService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the jnjGTCustomerReversePopulator
	 */
	public JnjGTCustomerReversePopulator getJnjGTCustomerReversePopulator() {
		return jnjGTCustomerReversePopulator;
	}

	/**
	 * @param jnjGTCustomerReversePopulator the jnjGTCustomerReversePopulator to set
	 */
	public void setJnjGTCustomerReversePopulator(
			JnjGTCustomerReversePopulator jnjGTCustomerReversePopulator) {
		this.jnjGTCustomerReversePopulator = jnjGTCustomerReversePopulator;
	}

	/**
	 * @return the jnjGTTerritoryService
	 */
	public JnjGTTerritoryService getJnjGTTerritoryService() {
		return jnjGTTerritoryService;
	}

	/**
	 * @param jnjGTTerritoryService the jnjGTTerritoryService to set
	 */
	public void setJnjGTTerritoryService(JnjGTTerritoryService jnjGTTerritoryService) {
		this.jnjGTTerritoryService = jnjGTTerritoryService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	@Override
	public JnJB2BUnitModel getDefaulB2bUnitForCurrentCust() {
		return null;
	}

	@Override
	public boolean validateDefaultAccount(JnJB2BUnitModel defaultB2BUnit) {

		return false;
	}

	public B2BCommerceUnitService getB2BCommerceUnitService()
	{
		return b2BCommerceUnitService;
	}


	@Override
	public boolean isResetPasswordComplete(){
		final String METHOD_NAME = "isResetPasswordComplete()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerService.isResetPasswordComplete();
	}

	/* (non-Javadoc)
	 * @see com.jnj.facades.customer.JnjGTCustomerFacade#resetPasswordUrlFirstTimeLogin(java.lang.String, java.lang.String)
	 */
	@Override
	public String resetPasswordUrlFirstTimeLogin(String userId, String siteUrl) throws UnsupportedEncodingException{
		final String METHOD_NAME = "resetPasswordUrlFirstTimeLogin()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
      final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
      String token = jnjgtb2bCommerceUserService.generateTokenForApprovedEmail(customer);
      customer.setToken(token);
      token = URLEncoder.encode(token, "UTF-8");
      modelService.save(customer);
      String restetPasswrodUrl = "?passwordExpireToken="+	token + Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL_QUERY_PARAM + customer.getUid();
      CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
      return restetPasswrodUrl;
	}

	@Override
	public String enableOrDisableUser(boolean status,String userId) {

		final String METHOD_NAME = "enableOrDisableUser()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId,
				B2BCustomerModel.class);
		customer.setLoginDisabled(status);
		String loginStatus = "";
		if (status) {
			customer.setStatus(CustomerStatus.DISABLED);
			loginStatus = "Disabled";
		} else {
			customer.setStatus(CustomerStatus.ACTIVE);
			loginStatus = "Enable";
		}
		modelService.save(customer);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return loginStatus;
	}

	/* (non-Javadoc)
	 * @see de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade#loginSuccess()
	 */
	@Override
	public void loginSuccess()
	{
		final CustomerData userData = getCurrentCustomer();

		// First thing to do is to try to change the user on the session cart
		if (getCartService().hasSessionCart())
		{
			getCartService().changeCurrentCartUser(userService.getCurrentUser());
		}

		// Update the session currency (which might change the cart currency)
		if (!updateSessionCurrency(userData.getCurrency(), getStoreSessionFacade().getDefaultCurrency()))
		{
			// Update the user
			getUserFacade().syncSessionCurrency();
		}

		LOG.info("BYEPASS_LANGUAGE_OVERRIDE_OPTION :: " + Config.getParameter(Jnjb2bCoreConstants.BYEPASS_LANGUAGE_OVERRIDE_OPTION));
		if(Config.getParameter(Jnjb2bCoreConstants.BYEPASS_LANGUAGE_OVERRIDE_OPTION).equalsIgnoreCase("true")){
		// Update the user
			getUserFacade().syncSessionLanguage();
		}


		// Calculate the cart after setting everything up
		if (getCartService().hasSessionCart())
		{
			final CartModel sessionCart = getCartService().getSessionCart();

			// Clean the existing info on the cart if it does not beling to the current user
			getCartCleanStrategy().cleanCart(sessionCart);
			try
			{
				final CommerceCartParameter parameter = new CommerceCartParameter();
				parameter.setEnableHooks(true);
				parameter.setCart(sessionCart);
				getCommerceCartService().recalculateCart(parameter);
			}
			catch (final CalculationException ex)
			{
				LOG.error("Failed to recalculate order [" + sessionCart.getCode() + "]", ex);
			}
		}
	}

	//for GT no need this delete func
	/*@Override
	public String deleteUser(String uid)	{
		final String METHOD_NAME = "deleteUser()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		String status = "FAIL";
		try{
   		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(uid, B2BCustomerModel.class);
   		customer.setIsDelete(true); //doing soft delete instead of hard delete
   		modelService.save(customer);
   		modelService.refresh(customer);
   		status = "SUCCESS";
		} catch (final ModelSavingException exp) {
		exp.printStackTrace();
		if (LOG.isDebugEnabled()) {
			LOG.error(Logging.USER_MANAGEMENT + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Unable to save the new user : "
					+ exp);
		}
		status = "FAIL";
	}
		return status;

	}*/
	/* 4659 */
	public JnJB2BUnitModel getB2BUnitForUid(String b2bUnit)
	{
		return jnjGTB2BUnitDao.getB2BUnitByUid(b2bUnit);
	}
	/* 4659 */

	@Override
	public boolean updateDefaultAccAndOrder(String defaultAccount, String defaultOrder) {

		boolean updated = false;
		JnJB2BUnitModel defaultUnit = getB2BUnitForUid(defaultAccount);
		JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel)userService.getCurrentUser();

		try{
			customerModel.setDefaultB2BUnit(defaultUnit);
			customerModel.setDefaultOrderType(defaultOrder);
			getModelService().save(customerModel);
			updated = true;

		}
		catch (ModelSavingException saveException){
			LOG.error("Unable to save the default choices for the customer :" + customerModel.getUid());
		}
		return updated;
	}

	@Override
	public List<String> getAccountsListForAutoSuggest(String searchText) {

		final String METHOD_NAME = "getAccountsListForAutoSuggest()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ACCOUNTS, METHOD_NAME, Logging.END_OF_METHOD);
		return jnjGTCustomerService.getAccountListForAutoSuggest(searchText);


	}
	/**
	 * Created for franchise in Order History AAOL-5219
	 * @return
	 */
	@Override
	public List<CategoryModel> getUserFranchise(){
		List<CategoryModel> categories=new ArrayList<CategoryModel>();
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = ((JnJB2bCustomerModel) userService.getCurrentUser());
			categories = currentUser.getAllowedFranchise();
		}
		return categories;
	}

	/**
	 * Soumitra - adding the method to fetch all the franchise present in the portal. AAOL-4913
	 * @return
	 */
	@Override
	public List<CategoryModel> getAllFranchise(){
		final CategoryModel category = commerceCategoryService.getCategoryForCode("Categories");
		final List<CategoryModel> allFranchise = new ArrayList<>();

		//test if no categories are available in the portal.
		if(CollectionUtils.isNotEmpty(category.getCategories())){
			LOG.info("getAllFranchise() Fetching all available Franchises from hybris");
			for (final CategoryModel subCategory : category.getCategories()){
				allFranchise.add(subCategory);
			}
		}
		return allFranchise;
	}

	/*Soumitra - adding the method to fetch the category data for a single CategoryCode. AAOL-4913 */
	@Override
	public CategoryData getCategoryForCode(String code) {
		final CategoryModel category = commerceCategoryService.getCategoryForCode(code);
		CategoryData categoryData = new CategoryData();
		categoryData.setCode(category.getCode());
		categoryData.setName(category.getName());
		return categoryData;
	}

	//AFFG-20804
	@Override
	public List<CategoryModel> getAllNewFranchise() {
		final CategoryModel category = commerceCategoryService.getCategoryForCode("ProductCategories");
		final List<CategoryModel> allFranchise = new ArrayList<>();

		//test if no categories are available in the portal.
		if(CollectionUtils.isNotEmpty(category.getCategories())){
			LOG.info("getAllFranchise() Fetching all available Franchises from hybris");
			for (final CategoryModel subCategory : category.getCategories()){
				allFranchise.add(subCategory);
			}
		}
		return allFranchise;
	}
}
