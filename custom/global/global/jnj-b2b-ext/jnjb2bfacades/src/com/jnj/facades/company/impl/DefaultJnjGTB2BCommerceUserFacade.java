/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.company.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUserFacade;
import de.hybris.platform.b2bcommercefacades.company.util.B2BCompanyUtils;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.event.JnjGTApprovedUserEvent;
import com.jnj.core.event.JnjGTCreateUserEvent;
import com.jnj.core.event.JnjGTRejectUserEvent;
import com.jnj.core.event.JnjGTTemporaryPasswordEvent;
import com.jnj.facades.company.JnjGTB2BCommerceUserFacade;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.utils.CommonUtil;


/**
 * This Facade is used for Search Users.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTB2BCommerceUserFacade extends DefaultB2BUserFacade implements JnjGTB2BCommerceUserFacade
{


	private static final Logger LOG = Logger.getLogger(DefaultJnjGTB2BCommerceUserFacade.class);
	/** The Constant CREATE_USER_PASSWORD_ENCODING. */
	protected static final String CREATE_USER_PASSWORD_ENCODING = "register.user.password.encoding";

	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjgtb2bCommerceUserService;

	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/

	/** Base Store Service for Email Event Population **/
	@Autowired
	protected BaseStoreService baseStoreService;

	/** Base Site Service for Email Event Population **/
	@Autowired
	protected BaseSiteService baseSiteService;

	/** The event service required for the email flow **/
	@Autowired
	protected EventService eventService;
	@Autowired
	protected JnjConfigService jnjConfigService;


	/** Internationalization Service for Email Event Population **/
	@Autowired
	protected CommonI18NService commonI18NService;
	
	@Autowired
	 protected ModelService modelService; 
	 /** The user service. */
	/** The Password encoder service. */
	@Autowired
	protected PasswordEncoderService passwordEncoderService;
	
	 @Autowired
	 private UserService userService;

	public ModelService getModelService()
	{
		return modelService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	/* This method is used for searching Customer on User Profile Search Page */
	@Override
	public SearchPageData<CustomerData> searchCustomers(final JnjGTPageableData pageableData)
	{
		final String METHOD_NAME = Jnjb2bCoreConstants.Logging.USER_MAMANGEMENT_SEARCH_FACADE;
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.USER_MANAGEMENT, METHOD_NAME, "Enter User Search Page", LOG);
		//Calling the  Service method for Search Users.
		final SearchPageData<B2BCustomerModel> b2bCustomer = jnjgtb2bCommerceUserService.searchCustomers(pageableData);
		//Calling the  Populator for converting the Customer Model into Customer Data
		CommonUtil.logMethodStartOrEnd(Logging.USER_MANAGEMENT, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		 return B2BCompanyUtils.convertPageData(b2bCustomer, getB2BCustomerConverter());
	}

	/**
	 * This method populates the JnjGTSuccessfulRegistrationEmailEvent object.
	 *
	 * @return populated event object
	 */
	protected <T extends AbstractCommerceUserEvent> T initializeEmailEvent(final T event,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "initializeEmailEvent()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjGTSuccessfulRegistrationEmailEvent object", LOG);

		/** populating the event with the basic required values **/
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer(JnJB2bCustomerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"event object is populated", LOG);

		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return event;
	}

	/**
	 * This method is used for sending Approval Email
	 *
	 * @param string
	 * @return
	 * @throws DuplicateUidException
	 */
	@Override
	public void sentApprovedProfileEmail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException
	{
		final String METHOD_NAME = "sentApprovedProfileEmail()";
		CommonUtil.logMethodStartOrEnd(Logging.USER_APPROVER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnjGTApprovedUserEvent event = new JnjGTApprovedUserEvent();
		//final JnJB2bCustomerModel customer = companyB2BCommerceService.getCustomerForUid(userId);
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
		//Setting the Email Specific Attributes
		event.setBuisnessEmail(customer.getEmail());
		event.setBaseUrl(siteUrl);
		event.setLogoURL(siteLogoUrl);
		event.setHelpUrl(siteUrl + Jnjb2bCoreConstants.UserSearch.HELP_URL);
		event.setCustomerServiceNumber(jnjConfigService
				.getConfigValueById(Jnjb2bCoreConstants.UserSearch.CUSTOMER_SERVICES_NUMBER));
		/* Start CR-119 */
		customer.setSendActivationMail(Boolean.FALSE);
		customer.setResetPasswordUrl("");
		//Setting the logic whether email for Complete Profile should be sent or Incomplete Profile
		if (CollectionUtils.isNotEmpty(customer.getSecretQuestionsAndAnswersList()))
		{
			event.setCompleteProfileFlag(true);
		}
		else
		{
			event.setCompleteProfileFlag(false);
			String token = jnjgtb2bCommerceUserService.generateTokenForApprovedEmail(customer);
			customer.setToken(token);
			token = URLEncoder.encode(token, "UTF-8");
			//companyB2BCommerceService.saveModel(customer);
			modelService.save(customer);
			//Setting the  passwordResetUrl in the Event
			event.setPasswordResetUrl(siteUrl + Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL + token
					+ Jnjb2bCoreConstants.UserSearch.PASSWORD_RESET_URL_QUERY_PARAM + customer.getUid());
			LOG.info("Email Map Has Been Set With Email '" + customer.getEmail() + "' Site Url " + siteUrl);
		}
		eventService.publishEvent(initializeEmailEvent(event, customer));
		CommonUtil.logMethodStartOrEnd(Logging.USER_APPROVER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
	}

	@Override
	public void sentRejectionEmail(final String siteLogoUrl, final String userId, final String siteUrl)

	{
		final String METHOD_NAME = "sentRejectionEmail()";
		CommonUtil.logMethodStartOrEnd(Logging.USER_REJECTION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		final JnjGTRejectUserEvent event = new JnjGTRejectUserEvent();
		//final JnJB2bCustomerModel customer = companyB2BCommerceService.getCustomerForUid(userId);
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
		event.setBuisnessEmail(customer.getEmail());
		event.setBaseUrl(siteUrl);
		event.setLogoURL(siteLogoUrl);
		event.setCustomerServiceNumber(jnjConfigService
				.getConfigValueById(Jnjb2bCoreConstants.UserSearch.CUSTOMER_SERVICES_NUMBER));
		eventService.publishEvent(initializeEmailEvent(event, customer));
		CommonUtil.logMethodStartOrEnd(Logging.USER_REJECTION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
	}


	/**
	 * This method will compare the Password Token with the Stored Token for the User
	 *
	 * @param passwordExpireToken
	 * @param email
	 * @throws DuplicateUidException
	 */

	@Override
	public boolean verifyPasswordToken(final String passwordExpireToken, final String email) throws DuplicateUidException
	{
		//final String METHOD_NAME = "verifyPasswordToken()";
		//	CommonUtil.logMethodStartOrEnd(Logging.VERIFY_PASSWORD_TOKEN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		boolean passwordTokenValidity = false;
		//final JnJB2bCustomerModel customer = companyB2BCommerceService.getCustomerForUid(email);
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(email, B2BCustomerModel.class);
		if (null != customer.getToken() && passwordExpireToken.equals(customer.getToken()))
		{
			passwordTokenValidity = true;
			//companyB2BCommerceService.saveModel(customer);
			modelService.save(customer);
		}
		//CommonUtil.logMethodStartOrEnd(Logging.VERIFY_PASSWORD_TOKEN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		return passwordTokenValidity;
	}
	/**
	 * This method is used for sending creatuser email with portal link and user name 
	 */

	@Override
	public void sentCreateProfileEmail(String siteLogoUrl, String userId, String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException {
		
		final String METHOD_NAME = "sentCreateProfileEmail()";
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		//create Event
		final JnjGTCreateUserEvent event = new JnjGTCreateUserEvent();
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
		//Setting the Email Specific Attributes
		event.setBuisnessEmail(customer.getEmail());
		event.setBaseUrl(siteUrl);
		modelService.save(customer);
		LOG.info("sentCreateProfileEmail Email Map Has Been Set With Email '" + customer.getEmail() + "' Site Url " + siteUrl);
		eventService.publishEvent(initializeEmailEvent(event, customer));
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		
	}

	@Override
	public void sentTemporaryPasswordEmail(String siteLogoUrl, String userId, String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException {
		
		final String METHOD_NAME = "sentTemporaryPasswordEmail()";
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		//create Event
		final JnjGTTemporaryPasswordEvent event = new JnjGTTemporaryPasswordEvent();
		final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) userService.getUserForUID(userId, B2BCustomerModel.class);
		//Setting the Email Specific Attributes
		event.setBuisnessEmail(customer.getEmail());
		
		String temporaryPasswrod = jnjgtb2bCommerceUserService.generateTemporaryPwdForEmail(customer);
		event.setTemporaryPassword(temporaryPasswrod);
		customer.setPasswordEncoding(Config.getParameter(CREATE_USER_PASSWORD_ENCODING));
		customer.setEncodedPassword(passwordEncoderService.encode(customer, temporaryPasswrod,Config.getParameter(CREATE_USER_PASSWORD_ENCODING)));
		customer.setLoginDisabled(false);
		modelService.save(customer);
		
		LOG.info("sentTemporaryPasswordEmail Email Map Has Been Set With Email '" + customer.getEmail() + "' Site Url " + siteUrl);
		eventService.publishEvent(initializeEmailEvent(event, customer));
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
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
	
}
