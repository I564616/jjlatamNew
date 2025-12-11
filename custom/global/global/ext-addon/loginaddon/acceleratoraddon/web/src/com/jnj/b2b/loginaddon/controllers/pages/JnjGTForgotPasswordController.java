/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import com.jnj.exceptions.BusinessException;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.b2b.loginaddon.utill.JnjForgotPasswordErrorCounter;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.SecretQuestionData;
import com.jnj.facades.company.JnjGTB2BCommerceUserFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;


/**
 * This is the controller class for the forgot password functionality
 * 
 * @author Accenture
 * @version 1.0
 */
/*Start AAOL 5074*/
@Controller("JnJGTForgotPasswordController")
/*End AAOL 5074*/
@RequestMapping("/passwordReset")
public class JnjGTForgotPasswordController extends AbstractPageController
{
	/** This is the Password error counter bean, that holds the attacker email and the count of invalid attempts **/
	@Resource(name = "jnjForgotPasswordErrorCounter")
	protected JnjForgotPasswordErrorCounter jnjForgotPasswordErrorCounter;

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	/** The session service **/
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name = "GTB2BCommerceUserFacade")
	protected JnjGTB2BCommerceUserFacade jnjGTB2BCommerceUserFacade;

	public JnjForgotPasswordErrorCounter getJnjForgotPasswordErrorCounter() {
		return jnjForgotPasswordErrorCounter;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjGTB2BCommerceUserFacade getJnjGTB2BCommerceUserFacade() {
		return jnjGTB2BCommerceUserFacade;
	}

	protected static final Logger LOG = Logger.getLogger(JnjGTForgotPasswordController.class);
	protected static final String STATUS = "status";
	protected static final String VALID = "valid";
	protected static final String INVALID = "invalid";
	protected static final String QUESTIONS = "questions";
	protected static final String ATTACK = "attack";
	protected static final String DISABLED = "disabled";
	protected static final String PASSWORD_EXPIRED ="passwordExipred";

	/**
	 * This method returns the view for the forgot password light box
	 * 
	 * @return forgot password view
	 */
	@GetMapping
	//AAOL-4915 
	public String getForgotPasswordPopup(final Model model,@RequestParam(value = "passwordExipred", required = false) final String passwordExipred)
	{
		final String METHOD_NAME = "getForgotPasswordPopup()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		//AAOL-4915 passwordExipred flag setted
		if(passwordExipred!=null && passwordExipred.equals("true")){
			model.addAttribute(PASSWORD_EXPIRED, passwordExipred);
		}
		return getView(LoginaddonControllerConstants.Views.Pages.Password.PasswordResetChangePage);
	}

	/**
	 * This method is responsible for validating the email entered by the user It also blocks the entry if the user is
	 * registered as an attacker (Once invalid attempt count is 3 or more).
	 * 
	 * @param email
	 *           - the email entered by the user on the font end
	 * @return validationStatusString
	 */
	@GetMapping("/emailVerification")
	public @ResponseBody
	Map<String, String> validateEmailRequest(@RequestParam("email") String email)
	{
		final String METHOD_NAME = "validateEmailRequest()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		email = StringUtils.lowerCase(email);
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "User email received :: " + email);
		final Map<String, String> responseMap = new HashMap<String, String>();
		/** Checking if the user is registered in the attackers list **/
		if (jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().containsKey(email))
		{
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Attacker found in existing list");
			int attackCount = 0;
			try
			{
				/** Fetching the attack count **/
				attackCount = Integer.parseInt(jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().get(email));
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Attacker current count :: " + attackCount);
			}
			catch (final NumberFormatException numberFormatException)
			{
				LOG.error("Attack count is not in correct format :: " + numberFormatException.getMessage());
			}
			/** Checking if the attack counter is 3rd attempt or more **/
			if (attackCount >= 3)
			{
				LOG.info("Secret question attempts exceeded.");
				responseMap.put(STATUS, ATTACK);
				logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
				/** Returning status as ATTACK **/
				return responseMap;
			}
		}
		/** Validating if the user exists **/
		if (jnjGTCustomerFacade.validateUid(email))
		{
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Email validation result :: " + true);

			if (jnjGTCustomerFacade.isLoginDisabled(email))
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "User is login disabled!");
				responseMap.put(STATUS, DISABLED);
				logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
				/** Returning status as DISABLED **/
				return responseMap;
			}

			final StringBuilder questionsDelimitedString = new StringBuilder();
			/** Fetching all the secret questions **/
			final List<String> questions = fetchAllSecretQuestions(email);
			if (!questions.isEmpty())
			{
				for (final String question : questions)
				{
					questionsDelimitedString.append(question);
					questionsDelimitedString.append(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL);
				}
				/** Setting status valid as the user email is successfully validated **/
				responseMap.put(STATUS, VALID);
				/** Setting string of all secret questions **/
				responseMap.put(QUESTIONS, String.valueOf(questionsDelimitedString));
				LOG.info("Email validation result :: " + true);
			}
			else
			{
				/** Setting Invalid status as question were not found **/
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "No Questions found! Returning invalid");
				responseMap.put(STATUS, INVALID);
			}
		}
		else
		{
			/** Setting Invalid status as email was not valid **/
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Email validation result :: " + false);
			responseMap.put(STATUS, INVALID);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return responseMap;
	}

	/**
	 * This method validates the user entered secret questions and answers with the ones stored in the database
	 * 
	 * @param questionsAndAnswers
	 * @param email
	 * @return validation result
	 */
	@PostMapping("/questionsVerification")
	public @ResponseBody
	Map<String, String> validateSecurityAnswers(@RequestParam("questionsAndAnswers") final String questionsAndAnswers,
			@RequestParam("email") final String email)
	{
		final String METHOD_NAME = "validateSecurityAnswers()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "user email received :: " + email
				+ " :: user questions and answers received :: " + questionsAndAnswers);

		final Map<String, String> responseMap = new HashMap<String, String>();

		/** Fetching the validation status of the secret questions and answers entered by the user **/
		final boolean validationStatus = jnjGTCustomerFacade.validateSecretQuestionsAndAnswers(email,
				prepareUserEnteredSecretQuestions(questionsAndAnswers));

		/** Checking if status if false **/
		if (!validationStatus)
		{
			/** Scenario : Invalid **/
			/** Check if the incremented attack count has reached 3 attempts **/
			if (incrementAttackCount(email))
			{
				responseMap.put(ATTACK, ATTACK);
			}
			else
			{
				responseMap.put(STATUS, INVALID);
			}
		}
		else
		{
			/** Scenario : Valid **/
			responseMap.put(STATUS, VALID);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return responseMap;
	}

	/**
	 * This method is responsible for updating the password for the user
	 * 
	 * @param password
	 * @param email
	 * @return updateStatus
	 */
	@PostMapping("/revisionVerification")
	public @ResponseBody
	String updateCustomerPassword(@RequestParam("password") final String password, @RequestParam("email") final String email)
	{
		final String METHOD_NAME = "updateCustomerPassword()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final String TRUE = String.valueOf(Boolean.TRUE);
		final String EXCEPTION = "Exception";
		String error = "";

		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Email received :: " + email + " :: Password received :: "
				+ password);
		try
		{

			/** Verify eligibility to change Password **/
			error = jnjGTCustomerFacade.verifyPassword(password, email);
			if (TRUE.equalsIgnoreCase(error.trim()))
			{
				/** Change password for the user **/
				try
				{
					jnjGTCustomerFacade.updateUserPassword(password, email);
					/** SUCCESS SCENARIO **/
					logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password has been updated successfully");
				}
				catch (final TokenInvalidatedException tokenInvalidatedException)
				{
					/** FAILURE SCENARIO **/
					logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password update failed!");
					error = jnjCommonFacadeUtil.getMessageFromImpex("password.expiry.errormessage.invalidtoken");
					LOG.error("Password update error :: " + tokenInvalidatedException);
				}
			}
			else
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password update failed!");
				error = jnjCommonFacadeUtil.getMessageFromImpex("profile.changePassword.errormessage.oldPasswords");
			}

		}
		catch (PasswordMismatchException | BusinessException exception)
		{
			error = EXCEPTION;
			LOG.error("Exception in password verification process :: " + exception.getMessage());
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return error;
	}

	/**
	 * This method fetches the list of all the secret questions that need to be present in the drop downs
	 * 
	 * @return allSecretQuestions
	 */
	protected List<String> fetchAllSecretQuestions(final String email)
	{
		final String METHOD_NAME = "fetchAllSecretQuestions()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<String> allSecretQuestions = new ArrayList<String>();
		/** Fetching the secret questions for the user **/
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME,
				"Calling the facade Layer for fetching the secret questions for the user :: " + email);
		final List<SecretQuestionData> secretQuestionDataList = jnjGTCustomerFacade.getSecretQuestionsForUser(email);
		LOG.info("Facade Layer returned secret questions :: " + secretQuestionDataList);
		if (secretQuestionDataList != null && !secretQuestionDataList.isEmpty())
		{
			/** Iterating over secret question data **/
			for (final SecretQuestionData secretQuestionData : secretQuestionDataList)
			{
				/** Preparing delimiter separated string with secret question data **/
				allSecretQuestions.add(secretQuestionData.getQuestion() + Jnjb2bCoreConstants.HASH_SYMBOL
						+ secretQuestionData.getCode());
			}
		}
		else
		{
			LOG.warn("No Secret Questions found!");
		}
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, "validateEmailRequest()", "Questions retrieved :: " + allSecretQuestions);
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return allSecretQuestions;
	}

	/**
	 * This method increases the attack counter for every failed attempt by the user during secret questions verification
	 * 
	 * @param email
	 * @return isAttemptExceeded
	 */
	protected boolean incrementAttackCount(final String email)
	{
		final String METHOD_NAME = "incrementAttackCount()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		if (jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().containsKey(email))
		{
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Attacker found in existing list");
			/** Fetching the attack information for the user **/
			final String attackCounterInfo = jnjForgotPasswordErrorCounter.getSecretQuestionAttackCounter().get(email);
			int attackCount = 0;
			try
			{
				/** Obtaining the attack count **/
				attackCount = Integer.parseInt(attackCounterInfo.split(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL)[0]);
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Attacker current count :: " + attackCount);
				attackCount++;
				final Map<String, String> secretQuestionAttackCounter = new HashMap<String, String>();
				secretQuestionAttackCounter.put(email, String.valueOf(attackCount));
				jnjForgotPasswordErrorCounter.setSecretQuestionAttackCounter(secretQuestionAttackCounter);
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Attacker count updated to :: " + attackCount);
			}
			catch (final NumberFormatException numberFormatException)
			{
				LOG.error("Attack count is not in correct format :: " + numberFormatException.getMessage());
			}
			if (attackCount >= 3)
			{
				LOG.info("Attempts exceeded. Blocking the user.");

				/** Blocking the user **/
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Going to block the user - " + email);
				jnjGTCustomerFacade.blockUser(email);
				LOG.info(Logging.FORGOT_PASSWORD_NAME + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "USER - " + email
						+ " - BLOCKED!");
				return true;
			}
		}
		else
		{
			final Map<String, String> secretQuestionAttackCounter = new HashMap<String, String>();
			secretQuestionAttackCounter.put(email, Jnjb2bCoreConstants.NUMBER_ONE);
			jnjForgotPasswordErrorCounter.setSecretQuestionAttackCounter(secretQuestionAttackCounter);
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Secret question attack counter populated with :: "
					+ secretQuestionAttackCounter);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return false;
	}

	/**
	 * This method splits the question answer pair string and converts it into a list of SecretQuestionData
	 * 
	 * @param questionsAndAnswers
	 * @return userEnteredSecretQuestionsData
	 */
	protected List<SecretQuestionData> prepareUserEnteredSecretQuestions(final String questionsAndAnswers)
	{
		final String METHOD_NAME = "prepareUserEnteredSecretQuestions()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final List<SecretQuestionData> userEnteredSecretQuestionsData = new ArrayList<SecretQuestionData>();
		final String[] questionAnswerSets = questionsAndAnswers.split(Jnjb2bCoreConstants.HASH_SYMBOL);
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "User question and answer sets :: " + questionAnswerSets);
		for (final String questionAnswerPair : questionAnswerSets)
		{
			final SecretQuestionData secretQuestionData = new SecretQuestionData();
			secretQuestionData.setCode(questionAnswerPair.split(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL)[0]);
			secretQuestionData.setAnswer(questionAnswerPair.split(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL)[1]);
			userEnteredSecretQuestionsData.add(secretQuestionData);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return userEnteredSecretQuestionsData;
	}



	/**
	 * This method is responsible for updating the password for the user
	 * 
	 * @param password
	 * @param email
	 * @return updateStatus
	 */
	@PostMapping("/passwordToken")
	public @ResponseBody
	String updateCustomerPasswordByToken(@RequestParam("password") final String password,
			@RequestParam("email") final String email, @RequestParam("passwordExpireToken") final String passwordExpireToken)
	{
		final String METHOD_NAME = "updateCustomerPasswordByToken()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final String TRUE = String.valueOf(Boolean.TRUE);
		final String EXCEPTION = "Exception";
		String error = "";
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Email received :: " + email + " :: Password received :: "
				+ password);
		try
		{

			/** Verify Password Token **/
			if (jnjGTB2BCommerceUserFacade.verifyPasswordToken(passwordExpireToken, email))
			{
				/** Verify eligibility to change Password **/
				error = jnjGTCustomerFacade.verifyPassword(password, email);
				if (TRUE.equalsIgnoreCase(error.trim()))
				{
					/** Change password for the user **/
					try
					{
						final JnJB2bCustomerModel b2bCustomerModel = userService.getUserForUID(email.toLowerCase(),
								JnJB2bCustomerModel.class);
						jnjGTCustomerFacade.updatePasswordWithToken(password, b2bCustomerModel, passwordExpireToken);
						/** SUCCESS SCENARIO **/
						logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password has been updated successfully");
					}
					catch (final Exception tokenInvalidatedException)
					{
						/** FAILURE SCENARIO **/
						logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password update failed!");
						error = jnjCommonFacadeUtil.getMessageFromImpex("password.expiry.errormessage.invalidtoken");
						LOG.error("Password update error :: " + tokenInvalidatedException);
					}
				}
				else
				{
					logDebugMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, "Password update failed!");
				}
			}
			else
			{
				error = jnjCommonFacadeUtil.getMessageFromImpex("password.expiry.errormessage.invalidtoken");
				LOG.error("Exception in password verification process :: " + "Token does not exists or token does not match");
			}

		}
		catch (PasswordMismatchException | BusinessException | DuplicateUidException exception)
		{
			error = EXCEPTION;
			LOG.error("Exception in password verification process :: " + exception.getMessage());
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return error;
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
	
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
	@GetMapping("/validateRegistrationCaptchaResponse")
	@ResponseBody
	public boolean validateRegistrationCaptchaResponse(@RequestParam("captchaResponse") final String captchaResponse , final HttpServletRequest request)
	{
		LOG.info("Validating Captcha response");
		return validateCaptchaResponse(captchaResponse);
		
	}

	
}

