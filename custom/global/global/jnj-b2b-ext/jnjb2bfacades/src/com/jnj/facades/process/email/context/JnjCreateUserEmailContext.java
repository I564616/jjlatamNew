/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjCreateUserProcessModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCreateUserEmailContext extends CustomerEmailContext
{
	private static final Logger LOG = Logger.getLogger(JnjCreateUserEmailContext.class);
	private static final String EMAIL_SUBJECT_KEY = "user.create.emailSubject";
	private static final String USER_ID_KEY = "profile.email";
	private static final String USER_ROLE_KEY = "profile.role";
	private static final String USER_FIRST_NAME_KEY = "profile.firstName";
	private static final String USER_LAST_NAME_KEY = "profile.lastName";
	private static final String EMAIL_NOTIFICATION_KEY = "profile.emailNotification";
	private static final String USER_LANGUAGE_KEY = "profile.langString";
	private static final String USER_PROFILE_KEY = "user.role";
	private static final String MSG_GREETING_KEY = "user.email.newUser.dearuser";
	private static final String MSG_DETAIL1_KEY = "user.email.newUser.msg1";
	private static final String MSG_DETAIL2_KEY = "user.email.newUser.msg2";
	private static final String MSG_DETAIL3_KEY = "user.email.newUser.msg3";
	private static final String MSG_DETAIL4_KEY = "user.email.newUser.msg4";
	private static final String MSG_DETAIL5_KEY = "user.email.newUser.msg5";
	private static final String MSG_DETAIL6_KEY = "user.email.newUser.msg6";
	private static final String MSG_TEMP_PASSWORD_EXPIRATION_PERIOD = "user.password.expirationPeriod";
	private static final String LOGIN_URL = "https://dev.jnjb2b.com:9002/store/en/login";


	private String password;
	private String emailSubject;
	private String userId;
	private List<String> userRoles;
	private String userFirstName;
	private String userLastName;
	private String userPhoneNumber;
	private String userMobileNumber;
	private String userEmailNotification;
	private String userLanguage;
	private String userIdLabel;
	private String userRolesLabel;
	private String userFirstNameLabel;
	private String userLastNameLabel;
	private String userPhoneNumberLabel;
	private String userMobileNumberLabel;
	private String userEmailNotificationLabel;
	private String userProfileKey;
	private String userEmailGreetingMsg;
	private String userEmailInitialDescriptiveMsg;
	private String portalName;
	private String portalURL;
	private String adminUser;
	private String logoURL;
	private boolean regisrationEmailSent;

	/**
	 * @return the regisrationEmailSent
	 */
	public boolean getRegisrationEmailSent()
	{
		return regisrationEmailSent;
	}

	/**
	 * @param regisrationEmailSent
	 *           the regisrationEmailSent to set
	 */
	public void setRegisrationEmailSent(final boolean regisrationEmailSent)
	{
		this.regisrationEmailSent = regisrationEmailSent;
	}


	/**
	 * @return the portalName
	 */
	public String getPortalName()
	{
		return portalName;
	}

	/**
	 * @param portalName
	 *           the portalName to set
	 */
	public void setPortalName(final String portalName)
	{
		this.portalName = portalName;
	}

	/**
	 * @return the portalURL
	 */
	public String getPortalURL()
	{
		return portalURL;
	}

	/**
	 * @param portalURL
	 *           the portalURL to set
	 */
	public void setPortalURL(final String portalURL)
	{
		this.portalURL = portalURL;
	}

	/**
	 * @return the adminUser
	 */
	public String getAdminUser()
	{
		return adminUser;
	}

	/**
	 * @param adminUser
	 *           the adminUser to set
	 */
	public void setAdminUser(final String adminUser)
	{
		this.adminUser = adminUser;
	}

	/**
	 * @return the userEmailGreetingMsg
	 */
	public String getUserEmailGreetingMsg()
	{
		return userEmailGreetingMsg;
	}

	/**
	 * @param userEmailGreetingMsg
	 *           the userEmailGreetingMsg to set
	 */
	public void setUserEmailGreetingMsg(final String userEmailGreetingMsg)
	{
		this.userEmailGreetingMsg = userEmailGreetingMsg;
	}

	/**
	 * @return the userEmailInitialDescriptiveMsg
	 */
	public String getUserEmailInitialDescriptiveMsg()
	{
		return userEmailInitialDescriptiveMsg;
	}

	/**
	 * @param userEmailInitialDescriptiveMsg
	 *           the userEmailInitialDescriptiveMsg to set
	 */
	public void setUserEmailInitialDescriptiveMsg(final String userEmailInitialDescriptiveMsg)
	{
		this.userEmailInitialDescriptiveMsg = userEmailInitialDescriptiveMsg;
	}

	/**
	 * @return the userProfileKey
	 */
	public String getUserProfileKey()
	{
		return userProfileKey;
	}

	/**
	 * @param userProfileKey
	 *           the userProfileKey to set
	 */
	public void setUserProfileKey(final String userProfileKey)
	{
		this.userProfileKey = userProfileKey;
	}

	/**
	 * @return the userIdLabel
	 */
	public String getUserIdLabel()
	{
		return userIdLabel;
	}

	/**
	 * @param userIdLabel
	 *           the userIdLabel to set
	 */
	public void setUserIdLabel(final String userIdLabel)
	{
		this.userIdLabel = userIdLabel;
	}

	/**
	 * @return the userRolesLabel
	 */
	public String getUserRolesLabel()
	{
		return userRolesLabel;
	}

	/**
	 * @param userRolesLabel
	 *           the userRolesLabel to set
	 */
	public void setUserRolesLabel(final String userRolesLabel)
	{
		this.userRolesLabel = userRolesLabel;
	}

	/**
	 * @return the userFirstNameLabel
	 */
	public String getUserFirstNameLabel()
	{
		return userFirstNameLabel;
	}

	/**
	 * @param userFirstNameLabel
	 *           the userFirstNameLabel to set
	 */
	public void setUserFirstNameLabel(final String userFirstNameLabel)
	{
		this.userFirstNameLabel = userFirstNameLabel;
	}

	/**
	 * @return the userLastNameLabel
	 */
	public String getUserLastNameLabel()
	{
		return userLastNameLabel;
	}

	/**
	 * @param userLastNameLabel
	 *           the userLastNameLabel to set
	 */
	public void setUserLastNameLabel(final String userLastNameLabel)
	{
		this.userLastNameLabel = userLastNameLabel;
	}

	/**
	 * @return the userPhoneNumberLabel
	 */
	public String getUserPhoneNumberLabel()
	{
		return userPhoneNumberLabel;
	}

	/**
	 * @param userPhoneNumberLabel
	 *           the userPhoneNumberLabel to set
	 */
	public void setUserPhoneNumberLabel(final String userPhoneNumberLabel)
	{
		this.userPhoneNumberLabel = userPhoneNumberLabel;
	}

	/**
	 * @return the userMobileNumberLabel
	 */
	public String getUserMobileNumberLabel()
	{
		return userMobileNumberLabel;
	}

	/**
	 * @param userMobileNumberLabel
	 *           the userMobileNumberLabel to set
	 */
	public void setUserMobileNumberLabel(final String userMobileNumberLabel)
	{
		this.userMobileNumberLabel = userMobileNumberLabel;
	}

	/**
	 * @return the userEmailNotificationLabel
	 */
	public String getUserEmailNotificationLabel()
	{
		return userEmailNotificationLabel;
	}

	/**
	 * @param userEmailNotificationLabel
	 *           the userEmailNotificationLabel to set
	 */
	public void setUserEmailNotificationLabel(final String userEmailNotificationLabel)
	{
		this.userEmailNotificationLabel = userEmailNotificationLabel;
	}

	/**
	 * @return the userLanguageLabel
	 */
	public String getUserLanguageLabel()
	{
		return userLanguageLabel;
	}

	/**
	 * @param userLanguageLabel
	 *           the userLanguageLabel to set
	 */
	public void setUserLanguageLabel(final String userLanguageLabel)
	{
		this.userLanguageLabel = userLanguageLabel;
	}

	private String userLanguageLabel;

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the userRoles
	 */
	public List<String> getUserRoles()
	{
		return userRoles;
	}

	/**
	 * @param userRoles
	 *           the userRoles to set
	 */
	public void setUserRoles(final List<String> userRoles)
	{
		this.userRoles = userRoles;
	}

	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName()
	{
		return userFirstName;
	}

	/**
	 * @param userFirstName
	 *           the userFirstName to set
	 */
	public void setUserFirstName(final String userFirstName)
	{
		this.userFirstName = userFirstName;
	}

	/**
	 * @return the userLastName
	 */
	public String getUserLastName()
	{
		return userLastName;
	}

	/**
	 * @param userLastName
	 *           the userLastName to set
	 */
	public void setUserLastName(final String userLastName)
	{
		this.userLastName = userLastName;
	}

	/**
	 * @return the userPhoneNumber
	 */
	public String getUserPhoneNumber()
	{
		return userPhoneNumber;
	}

	/**
	 * @param userPhoneNumber
	 *           the userPhoneNumber to set
	 */
	public void setUserPhoneNumber(final String userPhoneNumber)
	{
		this.userPhoneNumber = userPhoneNumber;
	}

	/**
	 * @return the userMobileNumber
	 */
	public String getUserMobileNumber()
	{
		return userMobileNumber;
	}

	/**
	 * @param userMobileNumber
	 *           the userMobileNumber to set
	 */
	public void setUserMobileNumber(final String userMobileNumber)
	{
		this.userMobileNumber = userMobileNumber;
	}

	/**
	 * @return the userEmailNotification
	 */
	public String getUserEmailNotification()
	{
		return userEmailNotification;
	}

	/**
	 * @param userEmailNotification
	 *           the userEmailNotification to set
	 */
	public void setUserEmailNotification(final String userEmailNotification)
	{
		this.userEmailNotification = userEmailNotification;
	}

	/**
	 * @return the userLanguage
	 */
	public String getUserLanguage()
	{
		return userLanguage;
	}

	/**
	 * @param userLanguage
	 *           the userLanguage to set
	 */
	public void setUserLanguage(final String userLanguage)
	{
		this.userLanguage = userLanguage;
	}

	/**
	 * @param emailSubject
	 *           the emailSubject to set
	 */
	public void setEmailSubject(final String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	@Autowired
	private MessageFacadeUtill messageFacade;

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);

		if (storeFrontCustomerProcessModel instanceof JnjCreateUserProcessModel)
		{
			setPassword(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getPassword());
			setUserId(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getEmailID());
			setUserFirstName(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getFirstName());
			setUserLanguage(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getLanguage().getName());
			setUserLastName(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getLastName());
			setUserRoles(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getRoles());
			setUserEmailNotification(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getEmailNotification());
			setPortalName(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getPortalName());
			setAdminUser(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getAdminUser());
			setLogoURL(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getLogoUrl());
			setRegisrationEmailSent(((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getRegistrationEmailSent()
					.booleanValue());

			try
			{
				setUserIdLabel(messageFacade.getMessageTextForCode(USER_ID_KEY));
				setUserRolesLabel(messageFacade.getMessageTextForCode(USER_ROLE_KEY));
				setUserFirstNameLabel(messageFacade.getMessageTextForCode(USER_FIRST_NAME_KEY));
				setUserLastNameLabel(messageFacade.getMessageTextForCode(USER_LAST_NAME_KEY));
				setUserEmailNotificationLabel(messageFacade.getMessageTextForCode(EMAIL_NOTIFICATION_KEY));
				setUserLanguageLabel(messageFacade.getMessageTextForCode(USER_LANGUAGE_KEY));
				setUserProfileKey(messageFacade.getMessageTextForCode(USER_PROFILE_KEY));
				setEmailSubject(messageFacade.getMessageTextForCode(MSG_DETAIL1_KEY) + " " + portalName); //Welcome to [PORTALNAME]
				setUserEmailGreetingMsg(messageFacade.getMessageTextForCode(MSG_GREETING_KEY));

				setUserEmailInitialDescriptiveMsg(getCommonEmailMessage(getPortalName(), getAdminUser(),
						((JnjCreateUserProcessModel) storeFrontCustomerProcessModel).getLoginUrl(), getPassword(),
						messageFacade.getMessageTextForCode(MSG_TEMP_PASSWORD_EXPIRATION_PERIOD), false));
				//				put(FROM_EMAIL, "customerservices@jnj.com");
				//				put(FROM_DISPLAY_NAME, "customer care");
				//				put(EMAIL, "rob@rnwood.co.uk");

			}
			catch (final BusinessException exp)
			{
				LOG.error("Unable to fetch subject for the create user email::" + exp.getMessage());

			}


		}

	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject()
	{
		return emailSubject;
	}

	private String getCommonEmailMessage(final String portalName, final String adminName, final String portalURL,
			final String tempPassword, final String passwordExpiration, final boolean regsitrationEmailSent)
			throws BusinessException
	{
		final StringBuffer emailMessage = new StringBuffer();
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL1_KEY) + " "); //Welcome to
		emailMessage.append(portalName + " "); //[PORTALNAME]
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL2_KEY) + " "); //Portal. Your access have been granted by 
		emailMessage.append(adminName + " "); //[USERADMINNAME]
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL3_KEY) + " "); //. Please take a moment to finish your registration accessing
		emailMessage.append(portalURL + " "); //[PORTALURL]
		if (regsitrationEmailSent)
		{
			emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL4_KEY) + " "); //. As this will be your first access were providing you a temporary password: 
			emailMessage.append(tempPassword + " "); //[TEMPORARYPASSWORD]
			emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL5_KEY) + " "); //. You will need to change it. Its valid for
			emailMessage.append(passwordExpiration + " "); //[TEMPORARYPASSWORDEXPIRATION]
		}
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL6_KEY) + " "); //. Please find below your profile information:
		return emailMessage.toString();
	}

	/**
	 * @return the logoURL
	 */
	public String getLogoURL()
	{
		return logoURL;
	}

	/**
	 * @param logoURL
	 *           the logoURL to set
	 */
	public void setLogoURL(final String logoURL)
	{
		this.logoURL = logoURL;
	}

}
